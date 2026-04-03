package io.github.nomisrev.openapi.transformers

import io.github.nomisrev.openapi.Constraints
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.Model.FreeFormJson
import io.github.nomisrev.openapi.Model.Object
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.enumLikeValues
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.registry.ResolvedSchema
import io.github.nomisrev.openapi.registry.description
import io.github.nomisrev.openapi.registry.isAllOfNullableType
import io.github.nomisrev.openapi.registry.isAnyOfNullableType
import io.github.nomisrev.openapi.registry.isNull
import io.github.nomisrev.openapi.registry.isObjectWithDiscriminator
import io.github.nomisrev.openapi.registry.isOneOfNullableType
import io.github.nomisrev.openapi.registry.peek
import io.github.nomisrev.openapi.registry.toModel
import io.github.nomisrev.openapi.parser.AdditionalProperties.Allowed
import io.github.nomisrev.openapi.parser.AdditionalProperties.PSchema
import io.github.nomisrev.openapi.routes.SchemaContext

// TODO: resolveReference is always false for nested calls, and true for top-level.. Split the diff and remove bool?
@Suppress("UnsafeCallOnNullableType", "LongMethod")
context(ctx: Registry.Scope)
suspend fun ResolvedSchema.toModel(context: SchemaContext, resolveReference: Boolean): Model {
    val enumLikeValues = schema.enumLikeValues()
    val compositeTakesPrecedence  = schema.type == null || schema.compositeShouldTakePrecedenceOverType()
    return when {
        this is ResolvedSchema.Reference && !resolveReference ->
            Model.Reference(
                schema.discriminatedSubtypeOrNull(context, reference.name) ?: name,
                description(),
                isNullable,
                schema.title
            )

        this is ResolvedSchema.Reference && schema.discriminatedSubtypeOrNull(context, reference.name) != null ->
            Model.Reference(
                schema.discriminatedSubtypeOrNull(context, reference.name)!!,
                description(),
                isNullable,
                schema.title
            )

        this is ResolvedSchema.Recursive -> Model.Reference(name, description(), isNullable, schema.title)

        this is ResolvedSchema.Reference && isObjectWithDiscriminator() -> toDiscriminatedObject(context)
        compositeTakesPrecedence && isAllOfNullableType() -> flattenNull(schema.allOf!!) { nonNullSchemas ->
            if (nonNullSchemas.size == 1) {
                flattenedSingleNullableBranch(nonNullSchemas.single(), context)
            } else {
                allOf(context, nonNullSchemas)
            }
        }
        compositeTakesPrecedence && schema.allOf != null -> allOf(context, schema.allOf!!)

        compositeTakesPrecedence && isOneOfNullableType() -> flattenNull(schema.oneOf!!) { nonNullSchemas ->
            if (nonNullSchemas.size == 1) {
                flattenedSingleNullableBranch(nonNullSchemas.single(), context)
            } else {
                buildOneOf(context, nonNullSchemas)
            }
        }
        compositeTakesPrecedence && schema.oneOf?.size == 1 -> schema.oneOf!!.single().toModel(name, context)
        compositeTakesPrecedence && schema.oneOf?.isNotEmpty() == true -> buildOneOf(context, schema.oneOf!!)

        compositeTakesPrecedence && isAnyOfNullableType() -> flattenNull(schema.anyOf!!) { nonNullSchemas ->
            if (nonNullSchemas.size == 1) {
                flattenedSingleNullableBranch(nonNullSchemas.single(), context)
            } else {
                buildAnyOf(context, nonNullSchemas)
            }
        }
        compositeTakesPrecedence && schema.anyOf?.size == 1 -> schema.anyOf!!.single().toModel(name, context)
        compositeTakesPrecedence && schema.anyOf?.isNotEmpty() == true -> buildAnyOf(context, schema.anyOf!!)

        schema.type != null -> when (val type = schema.type!!) {
            is Schema.Type.Array -> { // Flatten Null
                val resolved = if (type.types.contains(Schema.Type.Basic.Null) && schema.nullable != true) {
                    when (this) {
                        is ResolvedSchema.Recursive -> copy(schema = schema.copy(nullable = true))
                        is ResolvedSchema.Reference -> copy(schema = schema.copy(nullable = true))
                        is ResolvedSchema.Value -> copy(schema = schema.copy(nullable = true))
                    }
                } else this

                resolved.union(context, (type.types - Schema.Type.Basic.Null).map { ReferenceOr.value(Schema(type = it)) })
            }

            Schema.Type.Basic.Object if schema.properties?.isNotEmpty() == true -> toObject(context, schema.properties!!)
            Schema.Type.Basic.Object -> objectWithoutProperties(context) ?: fallback()
            Schema.Type.Basic.Array -> collection(context)
            Schema.Type.Basic.String if enumLikeValues != null -> toClosedEnum(context, enumLikeValues)
            Schema.Type.Basic.Number if enumLikeValues != null -> toClosedEnum(context, enumLikeValues)
            Schema.Type.Basic.Boolean if enumLikeValues != null -> toClosedEnum(context, enumLikeValues)
            Schema.Type.Basic.Integer if enumLikeValues != null -> toClosedEnum(context, enumLikeValues)
            Schema.Type.Basic.Number,
            Schema.Type.Basic.Boolean,
            Schema.Type.Basic.Integer,
            Schema.Type.Basic.String -> primitive()

            Schema.Type.Basic.Null ->
                error("Null  should always be resolved to result in nullable types. Please report this bug. $schema")
        }

        enumLikeValues != null -> toClosedEnum(context, enumLikeValues)

        schema.properties?.isNotEmpty() == true -> toObject(context, schema.properties!!)
        schema.additionalProperties != null -> objectWithoutProperties(context) ?: fallback()
        schema.items != null -> collection(context)
        else -> fallback()
    }
}

private fun Schema.compositeShouldTakePrecedenceOverType(): Boolean =
    listOfNotNull(allOf, oneOf, anyOf)
        .flatten()
        .any(ReferenceOr<Schema>::addsStructuralCompositeShape)

private fun ReferenceOr<Schema>.addsStructuralCompositeShape(): Boolean =
    when (this) {
        is ReferenceOr.Reference -> true
        is ReferenceOr.Value<Schema> -> value.addsStructuralCompositeShape()
    }

private fun Schema.addsStructuralCompositeShape(): Boolean =
    type != null ||
            properties?.isNotEmpty() == true ||
            additionalProperties != null ||
            items != null ||
            enumLikeValues() != null ||
            allOf.orEmpty().isNotEmpty() ||
            oneOf.orEmpty().isNotEmpty() ||
            anyOf.orEmpty().isNotEmpty() ||
            format != null


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
private suspend fun ResolvedSchema.flattenNull(
    schemas: List<ReferenceOr<Schema>>,
    build: suspend (List<ReferenceOr<Schema>>) -> Model,
): Model {
    val nonNullSchemas = schemas.filterNot { it.peek().isNull() }
    require(nonNullSchemas.isNotEmpty()) {
        "Null should always be resolved to result in nullable types. Please report this bug. $schema"
    }
    val model = build(nonNullSchemas)
    return model.with(
        description = description() ?: model.description,
        isNullable = true,
        title = schema.title ?: model.title
    )
}

context(ctx: Registry.Scope)
private suspend fun ResolvedSchema.flattenedSingleNullableBranch(
    branch: ReferenceOr<Schema>,
    context: SchemaContext,
): Model = when (branch) {
    is ReferenceOr.Reference -> branch.toModel(name, context)
    is ReferenceOr.Value<Schema> -> when (this) {
        is ResolvedSchema.Reference -> ResolvedSchema.Reference(reference, branch.value).toModel(context, true)
        is ResolvedSchema.Recursive -> ResolvedSchema.Recursive(name, branch.value).toModel(context, true)
        is ResolvedSchema.Value -> branch.toModel(name, context)
    }
}
