package io.github.nomisrev.openapi.roundtrip

import it.krzeminski.snakeyaml.engine.kmp.api.Load
import it.krzeminski.snakeyaml.engine.kmp.api.LoadSettings
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import java.math.BigDecimal
import java.math.BigInteger

/**
 * The spec as a plain [JsonElement] tree, read WITHOUT the repo's parser.
 *
 * Why not the parser: `ExampleValue` stores every primitive example as a string (`5` and `"5"` are the same
 * `Single("5")`, `[1, 2]` becomes `["1", "2"]`), and kaml, which the parser uses for YAML, does not keep scalar
 * quoting. Both would make the oracle lose the very JSON types this harness exists to compare. JSON specs are read
 * with kotlinx.serialization; YAML specs with snakeyaml-engine, which resolves `300` to a number and `"300"` to a
 * string. The parser is still used for everything that is about the generator (see [GeneratedModels]).
 */
internal fun loadRawSpec(text: String, yaml: Boolean): JsonObject {
    val element = if (yaml) {
        // The default code point limit (3 MB) is smaller than some specs.
        toJson(Load(LoadSettings(codePointLimit = Int.MAX_VALUE)).loadOne(text))
    } else {
        Json.parseToJsonElement(text)
    }
    return element as? JsonObject ?: error("The spec root is not an object")
}

private fun toJson(value: Any?): JsonElement = when (value) {
    null -> JsonNull
    is Boolean -> JsonPrimitive(value)
    is String -> JsonPrimitive(value)
    is Int, is Long, is Short, is Byte -> JsonPrimitive((value as Number).toLong())
    is BigInteger -> JsonPrimitive(value)
    is BigDecimal -> JsonPrimitive(value)
    is Float -> JsonPrimitive(value.toDouble())
    is Double -> JsonPrimitive(value)
    is Number -> JsonPrimitive(value)
    is Map<*, *> -> JsonObject(value.entries.associate { (k, v) -> k.toString() to toJson(v) })
    is Iterable<*> -> JsonArray(value.map(::toJson))
    else -> JsonPrimitive(value.toString())
}

internal fun escapePointer(segment: String): String = segment.replace("~", "~0").replace("/", "~1")

internal fun JsonObject.str(key: String): String? = (this[key] as? JsonPrimitive)?.takeIf { it.isString }?.content

internal fun JsonElement?.obj(): JsonObject? = this as? JsonObject
