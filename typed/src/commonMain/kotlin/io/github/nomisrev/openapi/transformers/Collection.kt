package io.github.nomisrev.openapi.transformers

import io.github.nomisrev.openapi.Constraints
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.Model.Default
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.Registry
import io.github.nomisrev.openapi.ResolvedSchema
import io.github.nomisrev.openapi.SchemaContext
import io.github.nomisrev.openapi.description
import io.github.nomisrev.openapi.parser.ExampleValue
import io.github.nomisrev.openapi.resolve
import io.github.nomisrev.openapi.toModel

context(ctx: Registry)
suspend fun ResolvedSchema.collection(context: SchemaContext): Model {
    val inner = schema.items?.resolve(name, context)?.toModel(context) ?: Model.FreeFormJson(
        description = null,
        constraint = null,
        isNullable = false
    )
    return Model.Collection.List(
        inner,
        default(),
        description(),
        Constraints.Collection(schema),
        schema.nullable ?: false
    )
}

private fun ResolvedSchema.default() = when (val example = schema.default) {
    null -> null
    is ExampleValue.Multiple -> Default.Value(example.values)
    is ExampleValue.Single -> {
        val value = example.value
        when {
            value == "[]" -> Default.Value(emptyList<String>())
            value.equals(
                "null",
                ignoreCase = true
            ) -> if (schema.nullable == true) Default.Null else throw IllegalArgumentException("Null default for non-nullable collection.")

            else -> Default.Value(listOf(value))
        }
    }
}
