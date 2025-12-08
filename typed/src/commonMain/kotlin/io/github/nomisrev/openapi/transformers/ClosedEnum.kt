package io.github.nomisrev.openapi.transformers

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.Registry
import io.github.nomisrev.openapi.ResolvedSchema
import io.github.nomisrev.openapi.SchemaContext
import io.github.nomisrev.openapi.description
import io.github.nomisrev.openapi.parser.ExampleValue
import io.github.nomisrev.openapi.toModel

context(ctx: Registry)
suspend fun ResolvedSchema.toClosedEnum(context: SchemaContext, enum: List<String?>): Model.Enum.Closed {
    require(enum.isNotEmpty()) { "Enum requires at least 1 possible value. $schema" }
    val nestedNull = enum.any { it.equals("null", ignoreCase = true) || it == null }
    val isNullable = nestedNull || schema.nullable == true
    val inner = ResolvedSchema.Value(
        name,
        schema.copy(description = null, default = null, enum = null, nullable = false)
    ).toModel(context)
    val enumDefault = when (val _default = schema.default) {
        is ExampleValue.Single if _default.value.equals("null", ignoreCase = true) -> Model.Default.Null
        is ExampleValue.Single -> Model.Default.Value(_default.value)
        null -> null
        is ExampleValue.Multiple -> throw IllegalArgumentException("Multiple default values not supported for enums.")
    }
    require(!(enumDefault == Model.Default.Null && !isNullable)) {
        "The default value $enumDefault is not present in the enum values: ${schema.enum} & schema is not nullable."
    }
    return Model.Enum.Closed(name, inner, schema.enum!!, enumDefault, description(), schema.nullable ?: false)
}
