package io.github.nomisrev.openapi.transformers

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.SchemaContext
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.registry.ResolvedSchema
import io.github.nomisrev.openapi.registry.description
import io.github.nomisrev.openapi.registry.resolve
import io.github.nomisrev.openapi.toModel
import kotlin.text.equals

context(ctx: Registry.Scope)
suspend fun ResolvedSchema.toClosedEnum(context: SchemaContext, enum: List<String?>): Model.Enum {
    require(enum.isNotEmpty()) { "Enum requires at least 1 possible value. $schema" }
    val nestedNull = enum.any { it.equals("null", ignoreCase = true) || it == null } || schema.nullable == true
    val inner = ResolvedSchema.Value(
        name,
        schema.copy(description = null, default = null, enum = null, nullable = false)
    ).toModel(context)
    val enumDefault = default()
    require(!(enumDefault == Model.Default.Null && !nestedNull)) {
        "The default value $enumDefault is not present in the enum values: ${schema.enum} & schema is not nullable."
    }
    return Model.Enum(name, inner, schema.enum!!, enumDefault, false, description(), isNullable)
}

context(ctx: Registry.Scope)
suspend fun ResolvedSchema.toOpenEnum(context: SchemaContext, anyOf: List<ReferenceOr<Schema>>): Model {
    suspend fun toModel(enumSchema: ResolvedSchema) = when (enumSchema) {
        is Reference -> TODO("Union")
        is Recursive -> Model.Reference(name, description(), isNullable)
        is Value -> {
            require(enumSchema.schema.enum!!.isNotEmpty()) { "OpenEnum requires at least 1 possible value. $schema" }
            val nestedNull = enumSchema.schema.enum!!.any {
                it.equals(
                    "null",
                    ignoreCase = true
                ) || it == null
            } || enumSchema.schema.nullable == true
            val inner = ResolvedSchema.Value(
                name,
                enumSchema.schema.copy(description = null, default = null, enum = null, nullable = false)
            ).toModel(context)
            val enumDefault = default()
            require(!(enumDefault == Model.Default.Null && !nestedNull)) {
                "The default value $enumDefault is not present in the enum values: ${enumSchema.schema.enum} & schema is not nullable."
            }
            Model.Enum(
                name,
                inner,
                enumSchema.schema.enum!!,
                enumDefault,
                true,
                description(),
                isNullable
            )
        }
    }

    return anyOf.firstNotNullOf { case ->
        case.resolve(name, context) {
            if (it.schema.enum != null) toModel(it) else null
        }
    }
}

fun ResolvedSchema.default(): Model.Default<String>? = when (val _default = schema.default) {
    is Single if _default.value.equals("null", ignoreCase = true) -> Model.Default.Null
    is Single -> Model.Default.Value(_default.value)
    is Multiple -> throw IllegalArgumentException("Multiple default values not supported for enums.")
    null -> null
}
