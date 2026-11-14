package io.github.nomisrev.openapi.roundtrip

import kotlinx.serialization.KSerializer
import kotlinx.serialization.MissingFieldException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

enum class Classification(val label: String) {
    Ok("ok"),
    DecodeFailed("decode-failed"),
    Lossy("lossy"),
    NoClass("no-class"),
    ExampleNotInSchema("example-not-in-schema"),
}

/**
 * The outcome of one example. [detail] is stable across runs (no offsets, no timestamps) because it is stored in the
 * baseline; [signature] is the path-free grouping key used by the report.
 */
data class Finding(
    val id: String,
    val classification: Classification,
    val signature: String,
    val detail: String,
    val className: String?,
)

private val UnknownKey = Regex("""unknown key '((?:[^'\\]|\\.)*)'""")
private val AtPath = Regex("""at path:? (\$\S*)""")
private val NotAnElement = Regex("""does not contain element with name '((?:[^'\\]|\\.)*)'""")
private val Offset = Regex("""\boffset \d+""")
private const val MAX_PEELED_KEYS = 50
private const val MAX_DIFFS_IN_DETAIL = 6
private const val MAX_LINE = 220

internal class RoundTripper(
    private val json: Json,
    private val models: GeneratedModels,
    private val rawSpec: JsonObject,
) {
    private val walker = SchemaWalker(rawSpec)

    fun run(case: ExampleCase): Finding {
        val resolved = models.resolve(case.schemaName, case.role)
        if (resolved !is Resolution.Found) {
            val reason = (resolved as Resolution.Missing).reason
            return Finding(case.id, Classification.NoClass, "no-class: $reason", "${case.schemaName}: $reason", null)
        }
        val schemaNode = rawSpec["components"].obj()?.get("schemas").obj()?.get(case.schemaName).obj()
            ?: JsonObject(emptyMap())
        fun finding(c: Classification, signature: String, detail: String) =
            Finding(case.id, c, signature, detail, resolved.className)

        var serializer: KSerializer<Any?> = resolved.serializer
        var current: JsonElement = case.value
        if (case.property != null) {
            when (val lookup = propertySerializer(resolved.serializer, case.property)) {
                // The example is decoded as the value of that property inside an object, the way it is on the wire:
                // some serializers behave differently as the root of a document.
                is PropertyLookup.Found -> {
                    serializer = PropertyInObject(case.property, lookup.serializer)
                    current = JsonObject(mapOf(case.property to case.value))
                }
                PropertyLookup.NoElements -> return finding(
                    Classification.NoClass, "no-class: property serializer not reachable",
                    "${resolved.className} has a hand-written serializer; its property '${case.property}' cannot be reached",
                )
                PropertyLookup.NotAProperty -> return finding(
                    Classification.NoClass, "no-class: property missing from generated class",
                    "${resolved.className} has no property '${case.property}'",
                )
            }
        }
        if (case.isArray) {
            if (current !is JsonArray) {
                return finding(
                    Classification.ExampleNotInSchema, "example is not an array",
                    "the schema is an array of ${case.schemaName}, the example is ${jsonType(current)}",
                )
            }
            serializer = ListSerializer(serializer) as KSerializer<Any?>
        }
        // For arrays the first step is the index into the example, which is not part of the schema path.
        val stepsFor = { steps: List<Step> -> if (case.isArray) steps.drop(1) else steps }

        // Undeclared example keys the model refuses are peeled off one at a time so that they cannot mask a real
        // failure deeper in the same example.
        val peeled = mutableListOf<String>()
        while (true) {
            val attempt = attempt(serializer, current)
            if (attempt is Attempt.Failed) {
                val error = attempt.error
                val message = error.message.orEmpty()
                val pathText = AtPath.find(message)?.groupValues?.get(1) ?: "$"
                val path = parseSerializationPath(pathText)
                val note = peelNote(peeled)

                val unknown = unknownKeyOf(message)
                if (unknown != null && peeled.size < MAX_PEELED_KEYS && !walker.isDeclared(schemaNode, stepsFor(path), unknown)) {
                    val removed = remove(current, path, unknown)
                    if (removed != null) {
                        peeled += "$pathText.$unknown"
                        current = removed
                        continue
                    }
                }

                // The failures below are the example's fault when the spec itself says so.
                val violation = exampleViolation(error, message, schemaNode, stepsFor(path), path, pathText, current)
                if (violation != null) {
                    return finding(Classification.ExampleNotInSchema, violation.first, violation.second + note)
                }
                return finding(Classification.DecodeFailed, signatureOf(error), messageOf(error) + note)
            }
            val reencoded = (attempt as Attempt.Ok).value
            val diffs = diff(current, reencoded) { steps ->
                val schemaSteps = stepsFor(steps)
                if (schemaSteps.lastOrNull() is Step.Key) walker.defaultsAt(schemaNode, schemaSteps) else emptyList()
            }
            // A changed JSON type that the spec does not allow in the first place is the example's fault.
            val (violating, real) = diffs.partition { d ->
                d.original != null && d.kind.startsWith("changed") && typeNotAllowed(schemaNode, stepsFor(d.steps), d.original)
            }
            if (real.isNotEmpty()) {
                val shown = (real + violating).take(MAX_DIFFS_IN_DETAIL).joinToString("; ") {
                    it.text + if (it in violating) " [example type not allowed by the schema]" else ""
                }
                val more = if (diffs.size > MAX_DIFFS_IN_DETAIL) "; (+${diffs.size - MAX_DIFFS_IN_DETAIL} more)" else ""
                val signature = real.map(Difference::signature).distinct().sorted().joinToString(", ")
                return finding(Classification.Lossy, signature, shown + more + peelNote(peeled))
            }
            if (violating.isNotEmpty()) {
                val shown = violating.take(MAX_DIFFS_IN_DETAIL).joinToString("; ") { it.text }
                return finding(
                    Classification.ExampleNotInSchema, "example value of a type the schema does not allow",
                    "the model normalised a value the schema does not allow: $shown" + peelNote(peeled),
                )
            }
            if (peeled.isNotEmpty()) {
                return finding(
                    Classification.ExampleNotInSchema, "undeclared example key(s)",
                    "the example has keys the schema does not declare: ${peeled.joinToString(", ") { "'$it'" }}",
                )
            }
            return finding(Classification.Ok, "ok", "")
        }
    }

    /**
     * Decides whether a failed decode is the example's fault, from the raw spec only: undeclared keys were handled by
     * the caller; here a missing key the spec marks `required`, a JSON type the spec does not allow at the failing path,
     * and an enum value the spec does not list. Returns (signature, detail) or `null` when the model is to blame.
     */
    private fun exampleViolation(
        error: Throwable,
        message: String,
        schemaNode: JsonObject,
        path: List<Step>,
        examplePath: List<Step>,
        pathText: String,
        example: JsonElement,
    ): Pair<String, String>? {
        (error as? MissingFieldException)?.missingFields?.let { missing ->
            if (missing.all { walker.isRequired(schemaNode, path, it) }) {
                return "example lacks required key(s)" to
                    "the example omits keys the schema requires: ${missing.joinToString(", ") { "'$it'" }} at $pathText"
            }
            return null
        }
        NotAnElement.find(message)?.groupValues?.get(1)?.let { value ->
            return if (walker.enumAllows(schemaNode, path, value)) null
            else "example value is not in the schema's enum" to "'$value' at $pathText is not one of the enum values the schema lists"
        }
        // A date-time parse failure on a string that is not an RFC 3339 date-time at all (a typo in the spec). The
        // datetime library names the offending text but not its path, so look for that string in the example.
        if (error::class.simpleName.orEmpty().contains("FormatException")) {
            val bad = strings(example).firstOrNull { it.isNotEmpty() && it in message && instantOrNull(it) == null }
            if (bad != null) {
                return "example string is not a valid date-time" to "'$bad' cannot be parsed as an RFC 3339 date-time"
            }
        }
        val actual = valueAt(example, examplePath) ?: return null
        if (typeNotAllowed(schemaNode, path, actual)) {
            val declared = walker.declaredTypes(schemaNode, path).orEmpty().sorted().joinToString("|")
            return "example has a JSON type the schema does not allow" to
                "the example has ${jsonType(actual)} at $pathText, the schema declares $declared"
        }
        return null
    }

    private fun typeNotAllowed(schemaNode: JsonObject, path: List<Step>, value: JsonElement): Boolean {
        val declared = walker.declaredTypes(schemaNode, path) ?: return false
        val actual = jsonType(value)
        return !(actual in declared || (actual == "number" && "integer" in declared))
    }

    private fun strings(element: JsonElement): Sequence<String> = when (element) {
        is JsonObject -> element.values.asSequence().flatMap(::strings)
        is JsonArray -> element.asSequence().flatMap(::strings)
        is JsonPrimitive -> if (element.isString) sequenceOf(element.content) else emptySequence()
    }

    private fun valueAt(root: JsonElement, path: List<Step>): JsonElement? {
        var node: JsonElement = root
        for (step in path) {
            node = when (step) {
                is Step.Key -> (node as? JsonObject)?.get(step.name) ?: return null
                is Step.Index -> (node as? JsonArray)?.getOrNull(step.position) ?: return null
            }
        }
        return node
    }

    private fun peelNote(peeled: List<String>) =
        if (peeled.isEmpty()) "" else " [after removing ${peeled.size} undeclared example key(s): ${peeled.joinToString(", ")}]"

    private sealed interface Attempt {
        data class Ok(val value: JsonElement) : Attempt
        data class Failed(val error: Throwable) : Attempt
    }

    private fun attempt(serializer: KSerializer<Any?>, example: JsonElement): Attempt = try {
        // Decode from text and encode to text, like the Ktor ContentNegotiation plugin does on the wire.
        val decoded = json.decodeFromString(serializer, example.toString())
        Attempt.Ok(Json.parseToJsonElement(json.encodeToString(serializer, decoded)))
    } catch (e: Exception) {
        Attempt.Failed(e)
    } catch (e: StackOverflowError) {
        Attempt.Failed(e)
    }

    private fun unknownKeyOf(message: String): String? =
        UnknownKey.find(message)?.groupValues?.get(1)?.replace("\\'", "'")

    /** A copy of [node] without [key] in the object at [path]; `null` when the path does not exist. */
    private fun remove(node: JsonElement, path: List<Step>, key: String): JsonElement? {
        if (path.isEmpty()) return (node as? JsonObject)?.takeIf { key in it }?.let { JsonObject(it - key) }
        return when (val step = path.first()) {
            is Step.Key -> (node as? JsonObject)?.get(step.name)?.let { child ->
                remove(child, path.drop(1), key)?.let { JsonObject(node + (step.name to it)) }
            }
            is Step.Index -> (node as? JsonArray)?.getOrNull(step.position)?.let { child ->
                remove(child, path.drop(1), key)?.let { JsonArray(node.toMutableList().also { list -> list[step.position] = it }) }
            }
        }
    }

    private fun signatureOf(error: Throwable): String =
        "${error::class.simpleName}: ${firstLine(error)}".let { line ->
            val cause = rootCause(error)
            val withCause = if (cause !== error) "$line <- ${cause::class.simpleName}: ${firstLine(cause).take(100)}" else line
            withCause.replace(Regex("""\[\d+]"""), "[N]")
        }

    private fun messageOf(error: Throwable): String {
        val cause = rootCause(error)
        val own = "${error::class.simpleName}: ${firstLine(error)}"
        return (if (cause !== error) "$own <- ${cause::class.simpleName}: ${firstLine(cause)}" else own).take(2 * MAX_LINE)
    }

    private fun rootCause(error: Throwable): Throwable {
        var current = error
        var hops = 0
        while (current.cause != null && current.cause !== current && ++hops < 16) current = current.cause!!
        return current
    }

    /** First message line, without offsets and without the JSON text some exceptions quote. */
    private fun firstLine(error: Throwable): String =
        (error.message ?: "(no message)").lineSequence().first().trim()
            .replace(Offset, "offset N")
            .replace(Regex("""(Failed to deserialize Json:) .*"""), "$1 <json>")
            .let { if (it.length > MAX_LINE) it.take(MAX_LINE - 3) + "..." else it }
}

/**
 * Decodes `{"<name>": <value>}` through [child] and encodes it back the same way, so that a property's serializer is
 * exercised as an object member (like in the generated class) rather than as a document root.
 */
private class PropertyInObject(private val name: String, private val child: KSerializer<Any?>) : KSerializer<Any?> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("PropertyInObject") { element(name, child.descriptor) }

    override fun serialize(encoder: Encoder, value: Any?) =
        encoder.encodeStructure(descriptor) { encodeSerializableElement(descriptor, 0, child, value) }

    override fun deserialize(decoder: Decoder): Any? = decoder.decodeStructure(descriptor) {
        var value: Any? = null
        while (true) {
            when (val index = decodeElementIndex(descriptor)) {
                0 -> value = decodeSerializableElement(descriptor, 0, child)
                CompositeDecoder.DECODE_DONE -> break
                else -> error("Unexpected index $index")
            }
        }
        value
    }
}
