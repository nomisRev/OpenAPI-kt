package io.github.nomisrev.openapi.transformers

import io.github.nomisrev.openapi.Constraints
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.Model.Default
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.routes.SchemaContext
import io.github.nomisrev.openapi.parser.ExampleValue
import io.github.nomisrev.openapi.registry.ResolvedSchema
import io.github.nomisrev.openapi.registry.description
import io.github.nomisrev.openapi.registry.toModel

context(ctx: Registry.Scope)
suspend fun ResolvedSchema.collection(context: SchemaContext): Model {
    val inner = schema.items?.toModel(name, context)
        ?: Model.FreeFormJson(description = null, constraint = null, isNullable = false, title = null)
    return Model.Collection(inner, collectionDefault(), description(), Constraints.Collection(schema), isNullable, schema.title)
}

private fun ResolvedSchema.collectionDefault() = when (val example = schema.default) {
    null -> null
    is ExampleValue.Multiple -> Default.Value(example.values)
    is ExampleValue.Single -> {
        val value = example.value
        when {
            value == "[]" -> Default.Value(emptyList())
            value.equals("null", ignoreCase = true) ->
                if (schema.nullable == true) Default.Null
                else throw IllegalArgumentException("Null default for non-nullable collection.")

            else -> Default.Value(listOf(value))
        }
    }
}
