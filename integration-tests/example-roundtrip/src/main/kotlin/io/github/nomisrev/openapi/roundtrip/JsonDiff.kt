package io.github.nomisrev.openapi.roundtrip

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

/** One difference between the original example and the re-encoded value. */
internal data class Difference(
    val path: String,
    val kind: String,
    val text: String,
    val steps: List<Step> = emptyList(),
    /** The example's value at [path] when the two sides differ in type or value (null for added/missing keys). */
    val original: JsonElement? = null,
) {
    /** Path-free form, used to group findings across examples. */
    val signature: String get() = kind
}

internal fun jsonType(element: JsonElement): String = when (element) {
    JsonNull -> "null"
    is JsonObject -> "object"
    is JsonArray -> "array"
    is JsonPrimitive -> when {
        element.isString -> "string"
        element.content == "true" || element.content == "false" -> "boolean"
        else -> "number"
    }
}

private fun render(element: JsonElement): String =
    element.toString().let { if (it.length > 48) it.take(45) + "..." else it }

/**
 * Compares [original] with [reencoded] and lists the differences, applying exactly these normalisations (and no
 * others):
 *
 *  1. Key order is irrelevant (objects are compared as maps).
 *  2. Absent and explicit `null` are the same. A Kotlin type system guarantees that the decoder only produces or
 *     drops a `null` for a property that is optional or nullable, so no schema lookup is needed.
 *  3. A key absent from the example and present in the output is accepted when its value equals a `default` the spec
 *     declares for that property ([defaultsAt]); the client encodes defaults (`encodeDefaults = true`) and an absent
 *     value means the default. Any other added key is reported.
 *  4. Two JSON numbers are equal when they are numerically equal (`1` = `1.0` = `1.00`): the textual form of a number
 *     is not part of its value. A number and a string, or a boolean and a string, are NEVER equal.
 *  5. Two strings that both parse as RFC 3339 date-times and denote the same instant are equal
 *     (`2017-07-08T16:18:44-04:00` = `2017-07-08T20:18:44Z` = `2017-07-08T20:18:44.000Z`): the client writes UTC.
 *     This hides that the original offset is not preserved; it does not hide a different instant.
 */
internal fun diff(
    original: JsonElement,
    reencoded: JsonElement,
    defaultsAt: (List<Step>) -> List<JsonElement>,
): List<Difference> {
    val out = mutableListOf<Difference>()
    fun walk(path: String, steps: List<Step>, a: JsonElement, b: JsonElement) {
        when {
            a is JsonObject && b is JsonObject -> {
                for (key in (a.keys + b.keys).sorted()) {
                    val child = "$path.$key"
                    val av = a[key]
                    val bv = b[key]
                    when {
                        av != null && bv != null -> walk(child, steps + Step.Key(key), av, bv)
                        av != null -> if (av != JsonNull) {
                            out += Difference(child, "missing key '$key'", "$child: ${render(av)} -> (missing)", steps + Step.Key(key))
                        }
                        bv != null -> if (bv != JsonNull) {
                            val declared = defaultsAt(steps + Step.Key(key))
                            if (declared.none { numericOrExactEqual(it, bv) }) {
                                out += Difference(child, "added key '$key'", "$child: (absent) -> ${render(bv)}", steps + Step.Key(key))
                            }
                        }
                    }
                }
            }
            a is JsonArray && b is JsonArray -> {
                if (a.size != b.size) out += Difference(path, "array length", "$path: ${a.size} items -> ${b.size} items", steps)
                for (i in 0 until minOf(a.size, b.size)) walk("$path[$i]", steps + Step.Index(i), a[i], b[i])
            }
            a is JsonPrimitive && b is JsonPrimitive && !numericOrExactEqual(a, b) -> {
                val (from, to) = jsonType(a) to jsonType(b)
                out += Difference(path, "changed $from->$to", "$path: ${render(a)} -> ${render(b)} ($from->$to)", steps, a)
            }
            a is JsonPrimitive && b is JsonPrimitive -> Unit
            else -> {
                val (from, to) = jsonType(a) to jsonType(b)
                out += Difference(path, "changed $from->$to", "$path: ${render(a)} -> ${render(b)} ($from->$to)", steps, a)
            }
        }
    }
    walk("$", emptyList(), original, reencoded)
    return out
}

private fun numericOrExactEqual(a: JsonElement, b: JsonElement): Boolean {
    if (a !is JsonPrimitive || b !is JsonPrimitive) return a == b
    if (jsonType(a) == "number" && jsonType(b) == "number") {
        val x = a.content.toBigDecimalOrNull()
        val y = b.content.toBigDecimalOrNull()
        if (x != null && y != null) return x.compareTo(y) == 0
    }
    if (a.isString && b.isString) {
        if (a.content == b.content) return true
        val x = instantOrNull(a.content)
        val y = instantOrNull(b.content)
        return x != null && y != null && x == y
    }
    return a.isString == b.isString && a.content == b.content
}

internal fun instantOrNull(text: String): java.time.Instant? =
    if (text.length < 20 || text.length > 40 || text[4] != '-') null
    else try { java.time.OffsetDateTime.parse(text).toInstant() } catch (_: java.time.format.DateTimeParseException) { null }
