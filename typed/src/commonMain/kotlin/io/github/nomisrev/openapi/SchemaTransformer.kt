package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.parser.AdditionalProperties
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.transformers.collection
import io.github.nomisrev.openapi.transformers.flattenNull
import io.github.nomisrev.openapi.transformers.primitive
import io.github.nomisrev.openapi.transformers.toClosedEnum
import io.github.nomisrev.openapi.transformers.toObject
import io.github.nomisrev.openapi.transformers.toOpenEnum

context(ctx: Registry)
suspend fun ResolvedSchema.toModel(context: SchemaContext): Model = when {
    this is ResolvedSchema.Reference && isObjectWithDiscriminator() -> TODO()
    isOpenEnumeration() -> toOpenEnum(context, schema.anyOf!!)
    schema.enum != null -> toClosedEnum(context, schema.enum!!)
    schema.allOf != null -> TODO()

    isOneOfNullableType() -> flattenNull(context, schema.oneOf!!)
    schema.oneOf?.size == 1 -> schema.oneOf!!.single().resolve(name, context).toModel(context)
    schema.oneOf != null -> TODO()

    isAnyOfNullableType() -> flattenNull(context, schema.anyOf!!)
    schema.anyOf?.size == 1 -> schema.anyOf!!.single().resolve(name, context).toModel(context)
    schema.anyOf != null -> TODO()

    schema.type != null -> when (schema.type!!) {
        Schema.Type.Basic.Object if schema.properties != null -> toObject(context, schema.properties!!)
        Schema.Type.Basic.Object -> when(val ap = schema.additionalProperties) {
            is AdditionalProperties.Allowed -> if (ap.value) fallback() else Model.Primitive.Unit(description(), schema.nullable ?: false)
            is AdditionalProperties.PSchema -> ap.value.resolve(name, context).toModel(context)
            null -> fallback()
        }
        Schema.Type.Basic.Array -> collection(context)
        Schema.Type.Basic.Number,
        Schema.Type.Basic.Boolean,
        Schema.Type.Basic.Integer,
        Schema.Type.Basic.String -> primitive()

        is Schema.Type.Array -> TODO()
        Schema.Type.Basic.Null ->
            throw IllegalStateException("Null  should always be resolved to result in nullable types. Please report this bug. $schema")
    }

    schema.properties != null -> toObject(context, schema.properties!!)
    schema.additionalProperties != null -> when(val ap = schema.additionalProperties!!) {
        is AdditionalProperties.Allowed -> if(ap.value) fallback() else Model.Primitive.Unit(description(), schema.nullable ?: false)
        is AdditionalProperties.PSchema -> ap.value.resolve(name, context).toModel(context)
    }
    schema.items != null -> collection(context)
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
