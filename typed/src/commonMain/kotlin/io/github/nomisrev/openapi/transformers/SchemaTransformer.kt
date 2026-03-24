package io.github.nomisrev.openapi.transformers

import io.github.nomisrev.openapi.Constraints
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.Model.FreeFormJson
import io.github.nomisrev.openapi.Model.Object
import io.github.nomisrev.openapi.NamingContext
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
import io.github.nomisrev.openapi.parser.AdditionalProperties.PSchema
import io.github.nomisrev.openapi.routes.SchemaContext

// TODO: resolveReference is always false for nested calls, and true for top-level.. Split the diff and remove bool?
@Suppress("UnsafeCallOnNullableType", "LongMethod")
context(ctx: Registry.Scope)
suspend fun ResolvedSchema.toModel(context: SchemaContext, resolveReference: Boolean): Model = when {
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
        Schema.Type.Basic.Object -> objectWithoutProperties(context)
        Schema.Type.Basic.Array -> collection(context)
        Schema.Type.Basic.String if schema.enum != null -> toClosedEnum(context, schema.enum!!)
        Schema.Type.Basic.Number if schema.enum != null -> toClosedEnum(context, schema.enum!!)
        Schema.Type.Basic.Boolean if schema.enum != null -> toClosedEnum(context, schema.enum!!)
        Schema.Type.Basic.Integer if schema.enum != null -> toClosedEnum(context, schema.enum!!)
        Schema.Type.Basic.Number,
        Schema.Type.Basic.Boolean,
        Schema.Type.Basic.Integer,
        Schema.Type.Basic.String -> primitive()

        Schema.Type.Basic.Null ->
            error("Null  should always be resolved to result in nullable types. Please report this bug. $schema")
    }

    schema.enum != null -> toClosedEnum(context, schema.enum!!)

    isAllOfNullableType() -> flattenNull(schema.allOf!!) { nonNullSchemas ->
        if (nonNullSchemas.size == 1) {
            flattenedSingleNullableBranch(nonNullSchemas.single(), context)
        } else {
            allOf(context, nonNullSchemas)
        }
    }
    schema.allOf != null -> allOf(context, schema.allOf!!)

    isOneOfNullableType() -> flattenNull(schema.oneOf!!) { nonNullSchemas ->
        if (nonNullSchemas.size == 1) {
            flattenedSingleNullableBranch(nonNullSchemas.single(), context)
        } else {
            buildOneOf(context, nonNullSchemas)
        }
    }
    schema.oneOf?.size == 1 -> schema.oneOf!!.single().toModel(name, context)
    schema.oneOf?.isNotEmpty() == true -> buildOneOf(context, schema.oneOf!!)

    isAnyOfNullableType() -> flattenNull(schema.anyOf!!) { nonNullSchemas ->
        if (nonNullSchemas.size == 1) {
            flattenedSingleNullableBranch(nonNullSchemas.single(), context)
        } else {
            buildAnyOf(context, nonNullSchemas)
        }
    }
    schema.anyOf?.size == 1 -> schema.anyOf!!.single().toModel(name, context)
    schema.anyOf?.isNotEmpty() == true -> buildAnyOf(context, schema.anyOf!!)

    schema.properties?.isNotEmpty() == true -> toObject(context, schema.properties!!)
    schema.additionalProperties != null -> objectWithoutProperties(context)
    schema.items != null -> collection(context)
    else -> fallback()
}

context(ctx: Registry.Scope)
private suspend fun ResolvedSchema.objectWithoutProperties(context: SchemaContext): Model =
    when (val ap = schema.additionalProperties) {
        null -> fallback()
        is PSchema -> buildTypedAdditionalPropertiesModel(context, ap)
        is Allowed -> if (ap.value) fallback() else buildAllowedAdditionalPropertiesModel()
    }

context(ctx: Registry.Scope)
private suspend fun ResolvedSchema.buildTypedAdditionalPropertiesModel(
    context: SchemaContext,
    ap: PSchema,
): Model =
    when (this) {
        is ResolvedSchema.Recursive, is ResolvedSchema.Reference ->
            Object(
                name,
                description(),
                schema.title,
                mapOf(
                    "values" to Object.Property(
                        Model.Collection(
                            inner = ap.value.toModel(name.nest(NamingContext.AdditionalProperties), context),
                            default = Model.Default.Value(emptyList()),
                            description = null,
                            title = null,
                            constraint = null,
                            isNullable = false,
                        ),
                        false
                    )
                ),
                Object.AdditionalProperties.False,
                isNullable
            )

        is ResolvedSchema.Value ->
            Model.Collection(
                inner = ap.value.toModel(name, context),
                default = Model.Default.Value(emptyList()),
                description = description(),
                constraint = null,
                isNullable = isNullable,
                title = schema.title
            )
    }

context(ctx: Registry.Scope)
private suspend fun ResolvedSchema.buildAllowedAdditionalPropertiesModel(): Model =
    when (this) {
        is ResolvedSchema.Recursive if name.isTopLevel() ->
            Object(name, description(), schema.title, emptyMap(), false, isNullable)

        is ResolvedSchema.Reference ->
            Object(name, description(), schema.title, emptyMap(), false, isNullable)

        is ResolvedSchema.Recursive -> Model.Primitive.Unit(description(), isNullable, schema.title)
        is ResolvedSchema.Value -> Model.Primitive.Unit(description(), isNullable, schema.title)
    }

context(ctx: Registry.Scope)
private suspend fun ResolvedSchema.fallback(): Model = when (this) {
    is Value -> FreeFormJson(description(), Constraints.Object(schema), isNullable, schema.title)
    is Recursive -> Model.Reference(name, description(), isNullable, schema.title)
    is Reference -> Object.value(
        reference,
        FreeFormJson(description(), Constraints.Object(schema), isNullable, schema.title),
        title = schema.title
    )
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
