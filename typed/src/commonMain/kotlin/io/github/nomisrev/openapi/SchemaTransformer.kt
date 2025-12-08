package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.transformers.collection
import io.github.nomisrev.openapi.transformers.primitive
import io.github.nomisrev.openapi.transformers.toClosedEnum
import io.github.nomisrev.openapi.transformers.toObject

context(ctx: Registry)
suspend fun ResolvedSchema.toModel(context: SchemaContext): Model = when {
    this is ResolvedSchema.Reference && isObjectWithDiscriminator() -> TODO()
    isOpenEnumeration() -> TODO("OpenEnum")
    schema.enum != null -> toClosedEnum(context, schema.enum!!)
    schema.allOf != null -> TODO()

    isOneOfNullableType() -> TODO()
    schema.oneOf?.size == 1 -> TODO()
    schema.oneOf != null -> TODO()

    isAnyOfNullableType() -> TODO()
    schema.anyOf?.size == 1 -> TODO()
    schema.anyOf != null -> TODO()

    schema.type != null -> when (schema.type!!) {
        Schema.Type.Basic.Object if schema.properties != null -> toObject(context, schema.properties!!)
        Schema.Type.Basic.Object if schema.additionalProperties == null -> fallback()
        Schema.Type.Basic.Object -> TODO()
        Schema.Type.Basic.Array -> collection(context)
        Schema.Type.Basic.Number,
        Schema.Type.Basic.Boolean,
        Schema.Type.Basic.Integer,
        Schema.Type.Basic.String -> primitive()
        is Schema.Type.Array -> TODO()
        Schema.Type.Basic.Null ->
            throw IllegalStateException("Null  should always be resolved to result in nullable types. Please report this bug. $schema")
    }

    schema.properties != null -> TODO()
    schema.additionalProperties != null -> TODO()
    schema.items != null -> TODO()
    else -> fallback()
}

context(ctx: Registry)
private suspend fun ResolvedSchema.fallback(): Model = when (this) {
    is ResolvedSchema.Value -> Model.FreeFormJson(description(), Constraints.Object(schema), schema.nullable ?: false)
    is ResolvedSchema.Reference -> Model.Object.value(
        name,
        Model.FreeFormJson(description(), Constraints.Object(schema), schema.nullable ?: false)
    )
}
