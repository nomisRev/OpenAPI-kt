package io.github.nomisrev

import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.parser.Schema

context(openAPI: OpenAPI)
suspend fun Schema.printAllProperties() {
    val properties = mutableMapOf<String, Schema>()

    fun collectAllOfProperties(schema: Schema) {
        schema.allOf?.forEach { refOrSchema ->
            when (refOrSchema) {
                is io.github.nomisrev.openapi.parser.ReferenceOr.Value -> {
                    refOrSchema.value.properties?.forEach { (key, propRefOrSchema) ->
                        when (propRefOrSchema) {
                            is io.github.nomisrev.openapi.parser.ReferenceOr.Value ->
                                properties[key] = propRefOrSchema.value
                            is io.github.nomisrev.openapi.parser.ReferenceOr.Reference -> {
                                val refName = propRefOrSchema.ref.substringAfterLast("/")
                                openAPI.components.schemas[refName]?.let { schemaRef ->
                                    when (schemaRef) {
                                        is io.github.nomisrev.openapi.parser.ReferenceOr.Value ->
                                            properties[key] = schemaRef.value
                                        else -> {}
                                    }
                                }
                            }
                        }
                    }
                    collectAllOfProperties(refOrSchema.value)
                }
                is io.github.nomisrev.openapi.parser.ReferenceOr.Reference -> {
                    val refName = refOrSchema.ref.substringAfterLast("/")
                    openAPI.components.schemas[refName]?.let { schemaRef ->
                        when (schemaRef) {
                            is io.github.nomisrev.openapi.parser.ReferenceOr.Value ->
                                collectAllOfProperties(schemaRef.value)
                            else -> {}
                        }
                    }
                }
            }
        }
    }

    collectAllOfProperties(this)

    println("All properties:")
    properties.forEach { (name, schema) ->
        println("  $name: ${schema.type} (readOnly=${schema.readOnly})")
    }
}