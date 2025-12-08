package io.github.nomisrev.openapi.transformers

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.Registry
import io.github.nomisrev.openapi.resolve
import io.github.nomisrev.openapi.ResolvedSchema
import io.github.nomisrev.openapi.SchemaContext
import io.github.nomisrev.openapi.description
import io.github.nomisrev.openapi.parser.ExampleValue
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
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
    val enumDefault = default()
    require(!(enumDefault == Model.Default.Null && !isNullable)) {
        "The default value $enumDefault is not present in the enum values: ${schema.enum} & schema is not nullable."
    }
    return Model.Enum.Closed(name, inner, schema.enum!!, enumDefault, description(), schema.nullable ?: false)
}

context(ctx: Registry)
suspend fun ResolvedSchema.toOpenEnum(context: SchemaContext, anyOf: List<ReferenceOr<Schema>>): Model {
    // using name for the nested resolve is safe because there are guaranteed only two schemas, and one is String.
    val schemas = anyOf.map { it.resolve(name, context) }
    return when (val enumSchema = schemas.single { it.schema.enum != null }) {
        is ResolvedSchema.Reference -> TODO("Union")
        is ResolvedSchema.Value -> {
            require(enumSchema.schema.enum!!.isNotEmpty()) { "OpenEnum requires at least 1 possible value" }
            Model.Enum.Closed(
                name,
                ResolvedSchema.Value(
                    enumSchema.name,
                    enumSchema.schema.copy(description = null, default = null, enum = null, nullable = false)
                ).toModel(context),
                enumSchema.schema.enum!!,
                enumSchema.default(),
                description(),
                schema.nullable ?: false
            )
        }
    }
}

private fun ResolvedSchema.default(): Model.Default<String>? = when (val _default = schema.default) {
    is ExampleValue.Single if _default.value.equals("null", ignoreCase = true) -> Model.Default.Null
    is ExampleValue.Single -> Model.Default.Value(_default.value)
    null -> null
    is ExampleValue.Multiple -> throw IllegalArgumentException("Multiple default values not supported for enums.")
}
