package io.github.nomisrev.openapi.roundtrip

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

/** Where the example is used; decides which Read/Write variant of a context-split schema it belongs to. */
enum class Role { Read, Write, Schema }

/**
 * One example to round-trip.
 *
 * @property id JSON Pointer to the example value in the spec. When one `components/examples` entry is used by several
 * schemas the id gets a `#Schema/Role` suffix so it stays unique.
 * @property schemaName the component schema (`#/components/schemas/<schemaName>`) the example is an instance of.
 * @property isArray the example is an array whose items are instances of the schema.
 * @property property when set, the example sits on `properties.<property>` of the component schema and is an instance
 * of that property's generated type (extension (d): this is where most real-world specs keep their examples).
 */
data class ExampleCase(
    val id: String,
    val schemaName: String,
    val role: Role,
    val isArray: Boolean,
    val value: JsonElement,
    /** Set for an example on a property of the component schema: it is an instance of that property's type. */
    val property: String? = null,
)

data class CollectedExamples(val cases: List<ExampleCase>, val skipped: Map<String, Int>)

private const val SCHEMA_REF_PREFIX = "#/components/schemas/"
private val HttpMethods = listOf("get", "put", "post", "delete", "options", "head", "patch", "trace")

/**
 * Collects the examples whose schema is a NAMED component schema:
 *  a. `example` / `examples[i]` on a component schema itself,
 *  b. media-type `example` / `examples.*.value` in responses and request bodies whose `schema` is a direct `$ref`,
 *  c. the same where the schema is `type: array` with `items` a direct `$ref` (the example is the whole array),
 *  d. `example` / `examples[i]` written directly on an inline property schema of a component schema (not behind a
 *     `$ref`); the generated class' property serializer is the decoder. Not asked for by the task, added because
 *     (a)-(c) alone find 10 examples in OpenAI's spec and none in YouTrack's.
 *
 * Everything else is counted in [CollectedExamples.skipped] by reason; nothing is skipped silently.
 */
internal fun collectExamples(spec: JsonObject): CollectedExamples {
    val walker = SchemaWalker(spec)
    val found = linkedMapOf<Triple<String, String, Role>, ExampleCase>()
    val skipped = sortedMapOf<String, Int>()
    fun skip(reason: String) { skipped[reason] = (skipped[reason] ?: 0) + 1 }

    // (a) examples on component schemas.
    val schemas = spec["components"].obj()?.get("schemas").obj().orEmpty()
    for ((name, schemaNode) in schemas) {
        val schema = schemaNode as? JsonObject ?: continue
        val base = "/components/schemas/${escapePointer(name)}"
        if (schema.containsKey("example")) {
            val pointer = "$base/example"
            found[Triple(pointer, name, Role.Schema)] = ExampleCase(pointer, name, Role.Schema, false, schema.getValue("example"))
        }
        (schema["examples"] as? JsonArray)?.forEachIndexed { index, value ->
            val pointer = "$base/examples/$index"
            found[Triple(pointer, name, Role.Schema)] = ExampleCase(pointer, name, Role.Schema, false, value)
        }
        for ((propertyName, propertyNode) in schema["properties"].obj().orEmpty()) {
            val property = propertyNode as? JsonObject ?: continue
            val propertyBase = "$base/properties/${escapePointer(propertyName)}"
            fun add(pointer: String, value: JsonElement) {
                found[Triple(pointer, name, Role.Schema)] = ExampleCase(pointer, name, Role.Schema, false, value, propertyName)
            }
            if (property.containsKey("example")) add("$propertyBase/example", property.getValue("example"))
            (property["examples"] as? JsonArray)?.forEachIndexed { index, value -> add("$propertyBase/examples/$index", value) }
        }
    }

    // (b) and (c) examples in media types of operations.
    fun mediaTypes(container: JsonObject, containerPointer: String, role: Role) {
        val content = container["content"].obj() ?: return
        for ((mediaType, mediaNode) in content) {
            val media = mediaNode as? JsonObject ?: continue
            val hasExamples = media.containsKey("example") || (media["examples"].obj()?.isNotEmpty() == true)
            if (!hasExamples) continue
            val mediaPointer = "$containerPointer/content/${escapePointer(mediaType)}"
            val type = mediaType.substringBefore(';').trim().lowercase()
            val isJson = type == "application/json" || type.endsWith("+json") || type == "text/json"
            if (!isJson) { skip("non-json-media-type"); continue }

            val schemaNode = media["schema"].obj()
            val schemaRef = schemaNode?.str("\$ref")
            val itemRef = schemaNode?.takeIf { schemaRef == null && it.str("type") == "array" }?.get("items").obj()?.str("\$ref")
            val ref = schemaRef ?: itemRef
            val schemaName = ref?.takeIf { it.startsWith(SCHEMA_REF_PREFIX) }?.removePrefix(SCHEMA_REF_PREFIX)
                ?.replace("~1", "/")?.replace("~0", "~")
            if (schemaName == null || schemaName !in schemas) {
                skip(if (schemaNode == null) "no-schema" else "schema-not-a-direct-component-ref")
                continue
            }
            val isArray = schemaRef == null

            fun add(pointer: String, value: JsonElement) {
                found.putIfAbsent(Triple(pointer, schemaName, role), ExampleCase(pointer, schemaName, role, isArray, value))
            }
            if (media.containsKey("example")) add("$mediaPointer/example", media.getValue("example"))
            for ((exampleName, exampleNode) in media["examples"].obj().orEmpty()) {
                var pointer = "$mediaPointer/examples/${escapePointer(exampleName)}"
                var example = exampleNode as? JsonObject ?: continue
                val ref = example.str("\$ref")
                if (ref != null) {
                    val resolved = walker.resolve(example)
                    if (resolved == null) { skip("unresolvable-example-ref"); continue }
                    example = resolved
                    pointer = ref.removePrefix("#")
                }
                when {
                    example.containsKey("value") -> add("$pointer/value", example.getValue("value"))
                    example.containsKey("externalValue") -> skip("externalValue")
                    else -> skip("example-without-value")
                }
            }
        }
    }

    for ((path, itemNode) in spec["paths"].obj().orEmpty()) {
        val item = itemNode as? JsonObject ?: continue
        for (method in HttpMethods) {
            val operation = item[method].obj() ?: continue
            val operationPointer = "/paths/${escapePointer(path)}/$method"

            operation["requestBody"]?.let { node ->
                val body = walker.resolve(node) ?: return@let
                val pointer = node.obj()?.str("\$ref")?.removePrefix("#") ?: "$operationPointer/requestBody"
                mediaTypes(body, pointer, Role.Write)
            }
            for ((code, responseNode) in operation["responses"].obj().orEmpty()) {
                val response = walker.resolve(responseNode) ?: continue
                val pointer = responseNode.obj()?.str("\$ref")?.removePrefix("#")
                    ?: "$operationPointer/responses/${escapePointer(code)}"
                mediaTypes(response, pointer, Role.Read)
            }
        }
    }

    // Unique ids: an example shared by several schemas/roles gets a suffix.
    val byId = found.values.groupBy(ExampleCase::id)
    val cases = found.values.map { case ->
        if (byId.getValue(case.id).size > 1) case.copy(id = "${case.id}#${case.schemaName}/${case.role}") else case
    }.sortedBy(ExampleCase::id)
    return CollectedExamples(cases, skipped)
}
