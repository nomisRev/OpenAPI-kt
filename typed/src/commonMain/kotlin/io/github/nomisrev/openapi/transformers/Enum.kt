package io.github.nomisrev.openapi.transformers

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.routes.SchemaContext
import io.github.nomisrev.openapi.registry.ResolvedSchema
import io.github.nomisrev.openapi.registry.description
import kotlin.text.equals

context(ctx: Registry.Scope)
suspend fun ResolvedSchema.toClosedEnum(context: SchemaContext, enum: List<String?>): Model.Enum {
    require(enum.isNotEmpty()) { "Enum requires at least 1 possible value. $schema" }
    val nestedNull = enum.any { it.equals("null", ignoreCase = true) || it == null } || schema.nullable == true
    val inner = ResolvedSchema.Value(
        name,
        schema.copy(description = null, default = null, enum = null, nullable = false)
    ).toModel(context, false)
    val enumDefault = default()
    require(!(enumDefault == Model.Default.Null && !nestedNull)) {
        "The default value $enumDefault is not present in the enum values: ${schema.enum} & schema is not nullable."
    }
    return Model.Enum(name, inner, schema.enum!!, enumDefault, description(), schema.title, isNullable)
}

fun ResolvedSchema.default(): Model.Default<String>? = when (val _default = schema.default) {
    is Single if _default.value.equals("null", ignoreCase = true) -> Model.Default.Null
    is Single -> Model.Default.Value(_default.value)
    is Multiple -> throw IllegalArgumentException("Multiple default values not supported for enums.")
    null -> null
}
