package io.github.nomisrev.openapi.roundtrip

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

/** One step of the path to a value inside an example. */
internal sealed interface Step {
    data class Key(val name: String) : Step
    data class Index(val position: Int = -1) : Step
}

/**
 * Navigates the RAW spec (not the generator's model) so that normalisations and the `example-not-in-schema`
 * classification do not depend on the code under test.
 *
 * Best effort and deliberately permissive: through `allOf` / `oneOf` / `anyOf` every member is a candidate, so a
 * declared key is never reported as undeclared (which would hide a generator bug that dropped it).
 */
internal class SchemaWalker(private val root: JsonObject) {

    /** Follows local `$ref`s (`#/a/b`) until a non-reference object. */
    fun resolve(node: JsonElement?): JsonObject? {
        var current = node as? JsonObject ?: return null
        var hops = 0
        while (true) {
            val ref = current.str("\$ref") ?: return current
            if (!ref.startsWith("#/") || ++hops > 32) return null
            current = pointer(ref.removePrefix("#")) as? JsonObject ?: return null
        }
    }

    fun pointer(pointer: String): JsonElement? {
        var current: JsonElement = root
        for (raw in pointer.split('/').drop(1)) {
            val segment = raw.replace("~1", "/").replace("~0", "~")
            current = when (current) {
                is JsonObject -> current[segment] ?: return null
                is JsonArray -> current.getOrNull(segment.toIntOrNull() ?: return null) ?: return null
                else -> return null
            }
        }
        return current
    }

    /** [schema] and every schema reachable through composition keywords. */
    private fun members(schema: JsonObject, seen: MutableSet<JsonObject> = mutableSetOf()): List<JsonObject> {
        val resolved = resolve(schema) ?: return emptyList()
        if (!seen.add(resolved)) return emptyList()
        val nested = listOf("allOf", "oneOf", "anyOf").flatMap { key ->
            (resolved[key] as? JsonArray).orEmpty().flatMap { members(it as? JsonObject ?: return@flatMap emptyList(), seen) }
        }
        return listOf(resolved) + nested
    }

    private fun step(schemas: List<JsonObject>, step: Step): List<JsonObject> = schemas.flatMap { schema ->
        members(schema).flatMap { member ->
            when (step) {
                is Step.Key -> listOfNotNull(
                    resolve((member["properties"] as? JsonObject)?.get(step.name)),
                    // Only a schema-valued additionalProperties describes the value of an arbitrary key.
                    resolve(member["additionalProperties"]).takeIf { member["properties"].obj()?.containsKey(step.name) != true },
                )
                is Step.Index -> listOfNotNull(resolve(member["items"]))
            }
        }
    }

    private fun candidates(root: JsonObject, path: List<Step>): List<JsonObject> =
        path.fold(listOf(root)) { schemas, step -> if (schemas.isEmpty()) schemas else step(schemas, step) }

    /** Is [key] a declared property of the object at [path] under [rootSchema]? Unknown paths count as declared. */
    fun isDeclared(rootSchema: JsonObject, path: List<Step>, key: String): Boolean {
        val at = candidates(rootSchema, path)
        if (at.isEmpty()) return true
        return at.any { schema ->
            members(schema).any { member ->
                (member["properties"] as? JsonObject)?.containsKey(key) == true ||
                    member["additionalProperties"].let { it is JsonObject } ||
                    (member["additionalProperties"] as? JsonPrimitive)?.content == "true" ||
                    (member["patternProperties"] as? JsonObject)?.isNotEmpty() == true
            }
        }
    }

    /** Does the spec list [key] under `required` for the object at [path]? Unknown paths count as required. */
    fun isRequired(rootSchema: JsonObject, path: List<Step>, key: String): Boolean {
        val at = candidates(rootSchema, path)
        if (at.isEmpty()) return true
        return at.any { schema ->
            members(schema).any { member -> (member["required"] as? JsonArray).orEmpty().any { (it as? JsonPrimitive)?.content == key } }
        }
    }

    /**
     * The JSON types the spec allows for the value at [path] (`string`, `number`, `integer`, `boolean`, `object`,
     * `array`, `null`), or `null` when that cannot be said (no schema found, or a schema without a `type`).
     */
    fun declaredTypes(rootSchema: JsonObject, path: List<Step>): Set<String>? {
        val at = candidates(rootSchema, path)
        if (at.isEmpty()) return null
        val types = mutableSetOf<String>()
        for (member in at.flatMap { members(it) }) {
            when (val type = member["type"]) {
                is JsonPrimitive -> types += type.content
                is JsonArray -> type.forEach { types += (it as? JsonPrimitive)?.content ?: return null }
                else -> when {
                    member.containsKey("properties") || member.containsKey("required") -> types += "object"
                    member.containsKey("items") -> types += "array"
                    listOf("allOf", "oneOf", "anyOf").any { member.containsKey(it) } -> Unit
                    else -> return null
                }
            }
            if ((member["nullable"] as? JsonPrimitive)?.content == "true") types += "null"
        }
        return types.takeIf { it.isNotEmpty() }
    }

    /** `false` only when the spec lists `enum` values at [path] and [value] is not one of them. */
    fun enumAllows(rootSchema: JsonObject, path: List<Step>, value: String): Boolean {
        val enums = candidates(rootSchema, path).flatMap { members(it) }.mapNotNull { it["enum"] as? JsonArray }
        return enums.isEmpty() || enums.any { values -> values.any { (it as? JsonPrimitive)?.content == value } }
    }

    /** The `default` values the spec declares for the property at [path] (the last step must be a key). */
    fun defaultsAt(rootSchema: JsonObject, path: List<Step>): List<JsonElement> =
        candidates(rootSchema, path).flatMap { members(it) }.mapNotNull { it["default"] }
}

/** Parses the path of a kotlinx.serialization message (`$.a.b[0]['odd key']`) into steps. */
internal fun parseSerializationPath(path: String): List<Step> {
    val steps = mutableListOf<Step>()
    var i = if (path.startsWith("$")) 1 else 0
    while (i < path.length) {
        when (path[i]) {
            '.' -> {
                val end = (i + 1 until path.length).firstOrNull { path[it] == '.' || path[it] == '[' } ?: path.length
                steps += Step.Key(path.substring(i + 1, end))
                i = end
            }
            '[' -> {
                if (path.getOrNull(i + 1) == '\'') {
                    val end = path.indexOf("']", i)
                    if (end < 0) return steps
                    steps += Step.Key(path.substring(i + 2, end))
                    i = end + 2
                } else {
                    val end = path.indexOf(']', i)
                    if (end < 0) return steps
                    steps += Step.Index(path.substring(i + 1, end).toIntOrNull() ?: -1)
                    i = end + 1
                }
            }
            else -> return steps
        }
    }
    return steps
}
