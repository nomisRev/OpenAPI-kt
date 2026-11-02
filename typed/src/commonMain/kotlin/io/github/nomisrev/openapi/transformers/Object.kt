package io.github.nomisrev.openapi.transformers

import io.github.nomisrev.openapi.Constraints
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.parser.AdditionalProperties
import io.github.nomisrev.openapi.parser.AdditionalProperties.Allowed
import io.github.nomisrev.openapi.parser.AdditionalProperties.PSchema
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.registry.ResolvedSchema
import io.github.nomisrev.openapi.registry.booleanSchema
import io.github.nomisrev.openapi.registry.description
import io.github.nomisrev.openapi.registry.readOnly
import io.github.nomisrev.openapi.registry.resolve
import io.github.nomisrev.openapi.registry.toModel
import io.github.nomisrev.openapi.registry.writeOnly
import io.github.nomisrev.openapi.routes.SchemaContext

context(ctx: Registry.Scope)
suspend fun ResolvedSchema.toObject(
    context: SchemaContext,
    properties: Map<String, ReferenceOr<Schema>>
): Model.Object {
    val filteredProperties = properties(properties, context)

    val additionalProperties = additionalProperties(context)

    // If the spec had properties but the context-specific stripping removed some or all of them,
    // we need to flag this so the renderer can emit a `data class` rather than a `data object`
    // (zero properties) or a `value class` (single property), since those would be semantically
    // wrong for schemas that were designed to have multiple fields.
    // Properties with the boolean schema `false` can never be present, so they are not fields by
    // design rather than stripped, see [properties].
    val possibleProperties = properties.count { [_, schema] -> schema.booleanSchema() != false }
    val hadPropertiesBeforeStripping = possibleProperties > 0 && filteredProperties.size < possibleProperties

    return Model.Object(
        context = name,
        description = description(),
        title = schema.title,
        properties = filteredProperties,
        additionalProperties = additionalProperties,
        isNullable = isNullable,
        hadPropertiesBeforeStripping = hadPropertiesBeforeStripping,
    )
}

context(ctx: Registry.Scope)
suspend fun ReferenceOr<Schema>.takeIf(context: SchemaContext): ReferenceOr<Schema>? = when (context) {
    SchemaContext.Write if readOnly() == true -> null
    SchemaContext.Read if writeOnly() == true -> null
    SchemaContext.Write,
    SchemaContext.Read,
    SchemaContext.Null -> this
}

context(ctx: Registry.Scope)
suspend fun ResolvedSchema.properties(
    properties: Map<String, ReferenceOr<Schema>>,
    context: SchemaContext
): Map<String, Model.Object.Property> = buildMap {
    properties.forEach { [name, refOrSchema] ->
        // A property with the boolean schema `false` can never be present: it is not a field.
        // If it is also required, no object can satisfy the schema.
        if (refOrSchema.booleanSchema() == false) {
            require(name !in schema.required) {
                "Property '$name' of schema ${this@properties.name} is required but has the boolean schema `false`, " +
                        "so no object can satisfy it."
            }
            return@forEach
        }
        refOrSchema.takeIf(context)
            ?.resolve(this@properties.name.nest(NamingContext.ObjectProperty(name)), context) { scope, propSchema ->
                val model = scope.registry().engine.transform(scope, scope.registry(), propSchema, context, false)
                put(name, Model.Object.Property(model, schema.required.contains(name)))
            }
    }
}

context(ctx: Registry.Scope)
private suspend fun ResolvedSchema.additionalProperties(context: SchemaContext) =
    when (val ap = schema.additionalProperties.withoutBooleanSchema()) {
        is Allowed -> Model.Object.AdditionalProperties.Allowed(ap.value)
        null -> Model.Object.AdditionalProperties.Allowed(false)
        is PSchema -> Model.Object.AdditionalProperties.Schema(
            ap.value.toModel(name.nest(NamingContext.AdditionalProperties), context)
        )
    }

context(ctx: Registry.Scope)
suspend fun ResolvedSchema.objectWithoutProperties(context: SchemaContext): Model? =
    when (val ap = schema.additionalProperties.withoutBooleanSchema()) {
        null -> null
        is PSchema -> buildTypedAdditionalPropertiesModel(context, ap)
        is Allowed -> if (ap.value) null else buildAllowedAdditionalPropertiesModel()
    }

/**
 * `additionalProperties` given as a schema that is a boolean schema (e.g. a `$ref` to a component
 * that is `true` or `false`) means the same as `additionalProperties: true/false`.
 */
context(ctx: Registry.Scope)
private suspend fun AdditionalProperties?.withoutBooleanSchema(): AdditionalProperties? =
    when (this) {
        is PSchema -> value.booleanSchema()?.let(::Allowed) ?: this
        is Allowed, null -> this
    }

context(ctx: Registry.Scope)
internal suspend fun ResolvedSchema.buildTypedAdditionalPropertiesModel(
    context: SchemaContext,
    ap: PSchema,
): Model =
    when (this) {
        is ResolvedSchema.Recursive, is ResolvedSchema.Reference ->
            Model.Object(
                name,
                description(),
                schema.title,
                mapOf(
                    "values" to Model.Object.Property(
                        Model.Dictionary(
                            inner = ap.value.toModel(name.nest(NamingContext.AdditionalProperties), context),
                            description = null,
                            constraint = Constraints.Object(schema),
                            isNullable = false,
                            title = null,
                        ),
                        true
                    )
                ),
                Model.Object.AdditionalProperties.False,
                isNullable,
                isTransparentWrapper = true,
            )

        is ResolvedSchema.Value ->
            Model.Dictionary(
                inner = ap.value.toModel(name, context),
                description = description(),
                constraint = Constraints.Object(schema),
                isNullable = isNullable,
                title = schema.title
            )
    }

context(ctx: Registry.Scope)
internal suspend fun ResolvedSchema.buildAllowedAdditionalPropertiesModel(): Model =
    when (this) {
        is ResolvedSchema.Recursive if name.isTopLevel() ->
            Model.Object(name, description(), schema.title, emptyMap(), false, isNullable)

        is ResolvedSchema.Reference ->
            Model.Object(name, description(), schema.title, emptyMap(), false, isNullable)

        is ResolvedSchema.Recursive -> Model.Primitive.Unit(description(), isNullable, schema.title)
        is ResolvedSchema.Value -> Model.Primitive.Unit(description(), isNullable, schema.title)
    }

context(ctx: Registry.Scope)
internal suspend fun ResolvedSchema.fallback(): Model = when (this) {
    is ResolvedSchema.Value -> Model.FreeFormJson(description(), Constraints.Object(schema), isNullable, schema.title)
    is ResolvedSchema.Recursive -> Model.Reference(name, description(), isNullable, schema.title)
    is ResolvedSchema.Reference -> Model.Object.value(
        reference,
        Model.FreeFormJson(description(), Constraints.Object(schema), isNullable, schema.title),
        title = schema.title
    )
}
