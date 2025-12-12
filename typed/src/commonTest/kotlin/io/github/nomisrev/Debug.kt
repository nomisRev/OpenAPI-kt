package io.github.nomisrev

import io.github.nomisrev.openapi.parser.AdditionalProperties
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.registry.registry
import kotlinx.serialization.json.Json

// Only allOf currently supported
suspend fun OpenAPI.debug(name: String) {
    registry(api) {
        val schema = components.schemas[name]?.valueOrNull() ?: error("No schema $name")
        val allSchemas = buildMap {
            put("#/components/schemas/$name", schema)
            schema.collectAll()
        }
        println(Json.encodeToString(allSchemas))
    }
}

context(buffer: MutableMap<String, Schema>, openAPI: OpenAPI)
suspend fun Schema.collectAll() = forEach { refOrNull, schema -> if (refOrNull != null) buffer[refOrNull] = schema }

context(openAPI: OpenAPI)
fun ReferenceOr<Schema>.get(): Schema = when (this) {
    is ReferenceOr.Value<Schema> -> value
    is ReferenceOr.Reference -> {
        val refName = ref.drop("#/components/schemas/".length)
        requireNotNull(openAPI.components.schemas[refName]?.valueOrNull()) { "No schema $refName" }
    }
}

context(openAPI: OpenAPI)
suspend fun Schema.forEach(block: (String?, Schema) -> Unit) {
    suspend fun Schema.collectAll(visited: MutableSet<String>) {
        fun ReferenceOr<Schema>.schema(): Schema? = when (this) {
            is ReferenceOr.Value<Schema> -> value.also { block(null, it) }
            is ReferenceOr.Reference if (ref == "#" || ref in visited) -> null
            is ReferenceOr.Reference -> {
                visited.add(ref)
                val refName = ref.drop("#/components/schemas/".length)
                val schema = openAPI.components.schemas[refName]?.valueOrNull() ?: return null
                block(refName, schema)
                schema
            }
        }

        suspend fun Collection<ReferenceOr<Schema>>?.readOrWriteOnly() = orEmpty().forEach { refOrSchema ->
            refOrSchema.schema()?.collectAll(visited)
        }

        allOf.readOrWriteOnly()
        oneOf.readOrWriteOnly()
        properties?.values.readOrWriteOnly()
        anyOf.readOrWriteOnly()
        listOfNotNull(items).readOrWriteOnly()
        discriminator?.mapping?.values?.forEach { listOf(ReferenceOr.Reference(it)).readOrWriteOnly() }
        listOfNotNull((additionalProperties as? AdditionalProperties.PSchema)?.value).readOrWriteOnly()
    }

    return collectAll(mutableSetOf())
}