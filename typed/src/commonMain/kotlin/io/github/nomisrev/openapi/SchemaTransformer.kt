package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.Model.FreeFormJson
import io.github.nomisrev.openapi.Model.Object
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.ReferenceOr.Companion.schema
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.registry.ResolvedSchema
import io.github.nomisrev.openapi.registry.description
import io.github.nomisrev.openapi.registry.isAnyOfNullableType
import io.github.nomisrev.openapi.registry.isObjectWithDiscriminator
import io.github.nomisrev.openapi.registry.isOneOfNullableType
import io.github.nomisrev.openapi.registry.isOpenEnumeration
import io.github.nomisrev.openapi.registry.resolve
import io.github.nomisrev.openapi.registry.toModel
import io.github.nomisrev.openapi.transformers.collection
import io.github.nomisrev.openapi.transformers.primitive
import io.github.nomisrev.openapi.transformers.toClosedEnum
import io.github.nomisrev.openapi.transformers.toObject
import io.github.nomisrev.openapi.transformers.toOpenEnum
import io.github.nomisrev.openapi.transformers.union

context(ctx: Registry.Scope)
suspend fun ResolvedSchema.toModel(context: SchemaContext): Model = when {
    this is ResolvedSchema.Recursive -> Model.Reference(name, description(), isNullable)
    this is ResolvedSchema.Reference && isObjectWithDiscriminator() -> TODO()
    isOpenEnumeration() -> toOpenEnum(context, schema.anyOf!!)
    schema.enum != null -> toClosedEnum(context, schema.enum!!)
    schema.allOf != null -> TODO()

    isOneOfNullableType() -> flattenNull(context, schema.oneOf!!)
    schema.oneOf?.size == 1 -> schema.oneOf!!.single().toModel(name, context)
    schema.oneOf != null -> union(context, schema.oneOf!!)

    isAnyOfNullableType() -> flattenNull(context, schema.anyOf!!)
    schema.anyOf?.size == 1 -> schema.anyOf!!.single().toModel(name, context)
    schema.anyOf != null -> TODO()

    schema.type != null -> when (val type = schema.type!!) {
        is Schema.Type.Array -> { // Flatten Null
            val resolved = if (type.types.contains(Schema.Type.Basic.Null) && schema.nullable != true) {
                val newSchema = schema.copy(nullable = true)
                when (this) {
                    is ResolvedSchema.Reference -> copy(name = name, schema = newSchema)
                    is ResolvedSchema.Value -> copy(name = name, schema = newSchema)
                }
            } else this

            resolved.union(context, (type.types - Schema.Type.Basic.Null).map { ReferenceOr.value(Schema(type = it)) })
        }

        Schema.Type.Basic.Object if schema.properties != null -> toObject(context, schema.properties!!)
        Schema.Type.Basic.Object -> objectWithoutProperties(context)
        Schema.Type.Basic.Array -> collection(context)
        Schema.Type.Basic.Number,
        Schema.Type.Basic.Boolean,
        Schema.Type.Basic.Integer,
        Schema.Type.Basic.String -> primitive()

        Schema.Type.Basic.Null ->
            throw IllegalStateException("Null  should always be resolved to result in nullable types. Please report this bug. $schema")
    }

    schema.properties != null -> toObject(context, schema.properties!!)
    schema.additionalProperties != null -> objectWithoutProperties(context)
    schema.items != null -> collection(context)
    else -> fallback()
}

context(ctx: Registry.Scope)
private suspend fun ResolvedSchema.objectWithoutProperties(context: SchemaContext): Model =
    when (val ap = schema.additionalProperties) {
        null -> fallback()
        is PSchema -> ap.value.toModel(name, context)
        is Allowed -> if (ap.value) fallback() else Model.Primitive.Unit(description(), isNullable)
    }

context(ctx: Registry.Scope)
private suspend fun ResolvedSchema.fallback(): Model = when (this) {
    is Value -> FreeFormJson(description(), Constraints.Object(schema), isNullable)
    is Reference -> Object.value(name, FreeFormJson(description(), Constraints.Object(schema), isNullable))
    is Recursive -> Model.Reference(name, description(), isNullable)
}

/**
 * oneOf: [{ type: null }, { type: string }]
 * anyOf: [{ type: null }, { type: string }]
 * become
 * {
 *   type: string
 *   nullable: true
 * }
 */
context(ctx: Registry.Scope)
private suspend fun ResolvedSchema.flattenNull(context: SchemaContext, schemas: List<ReferenceOr<Schema>>): Model =
    schemas.firstNotNullOf {
        it.resolve(name, context) {
            if (it.schema.type == Schema.Type.Basic.Null) null else it.toModel(context)
        }
    }.with(isNullable = true)
