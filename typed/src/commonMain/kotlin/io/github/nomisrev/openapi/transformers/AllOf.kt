@file:Suppress("TooManyFunctions")
package io.github.nomisrev.openapi.transformers

import io.github.nomisrev.openapi.Constraints
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.combine
import io.github.nomisrev.openapi.merge
import io.github.nomisrev.openapi.mergeMaximumBound
import io.github.nomisrev.openapi.mergeMinimumBound
import io.github.nomisrev.openapi.routes.SchemaContext
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.pipeline.plugins.composite.singleBranch
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.registry.ResolvedSchema
import io.github.nomisrev.openapi.registry.description
import io.github.nomisrev.openapi.registry.isObjectWithDiscriminator
import io.github.nomisrev.openapi.registry.peek
import io.github.nomisrev.openapi.registry.schemaName
import io.github.nomisrev.openapi.registry.toModel

context(scope: Registry.Scope)
suspend fun ResolvedSchema.allOf(context: SchemaContext, allOf: List<ReferenceOr<Schema>>): Model =
    if (this is ResolvedSchema.Reference && allOf.size in 1..2) {
        schema.discriminatedSubtypeOrNull(context, reference.name)?.let { naming ->
            Model.Reference(naming, description(), isNullable, schema.title)
        } ?: resolveAllOf(allOf, context)
    } else {
        resolveAllOf(allOf, context)
    }

/**
 * A named component `allOf: [{$ref: X}]` is an alias of X with its own name, like `anyOf`/`oneOf: [{$ref: X}]`:
 * X's schema modelled under the component's name, see [singleBranch]. A named component with several members is the
 * merged schema modelled under the component's name, as if the component declared the merged keywords itself:
 * `{allOf: [{type: array, items: X}, {minItems: 1}]}` is `{type: array, items: X, minItems: 1}`.
 * Merging as an inline value instead models a primitive, collection or map as its bare value, so the component,
 * which `$ref`s to it still name, is never generated while array items, map values and bodies silently use the
 * bare value.
 */
context(scope: Registry.Scope)
private suspend fun ResolvedSchema.resolveAllOf(
    allOf: List<ReferenceOr<Schema>>,
    context: SchemaContext
): Model = when (val single = allOf.singleOrNull()) {
    is ReferenceOr.Reference if this is ResolvedSchema.Reference -> singleBranch(single, context)
    else -> with(scope.registry()) {
        val merged = try {
            allOf.map { it.peek() }.reduce { acc, or -> acc.merge(or) }
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("allOf members of '${name.describe()}' are incompatible: ${e.message}", e)
        }
        when (val resolved = this@resolveAllOf) {
            is ResolvedSchema.Reference ->
                engine.transform(scope, this, ResolvedSchema.Reference(resolved.reference, merged), context, true)

            else -> ReferenceOr.value(merged).toModel(name, context)
        }
    }
}

/** The schema this naming context names, such as `Pet` or `Pet.owner`, for error messages. */
internal fun NamingContext.describe(): String {
    val head = when (val head = head) {
        is NamingContext.Reference -> head.name
        is NamingContext.Path -> "${head.method.value} /${head.segments.joinToString("/") { it.name }}"
    }
    return (listOf(head) + nested.map { nested ->
        when (nested) {
            is NamingContext.ObjectProperty -> nested.name
            else -> nested.toString()
        }
    }).joinToString(".")
}

context(scope: Registry.Scope)
private tailrec suspend fun ReferenceOr<Schema>.getSuperTypeOrNull(
    context: SchemaContext,
    name: String,
): NamingContext? {
    val parentSchema = peek()
    val superTypeOrNull = parentSchema
        .allOf
        ?.singleOrNull { it.isObjectWithDiscriminator() }
    return if (superTypeOrNull != null) {
        superTypeOrNull.getSuperTypeOrNull(context, name)
    } else {
        if (parentSchema.discriminator?.mapping?.contains(name) == true) {
            parentSchema.discriminator
                ?.mapping
                ?.entries
                ?.singleOrNull { [_, ref] ->
                    ref.schemaName() == name
                }?.let { [mappingName, _] ->
                    NamingContext.reference((this as ReferenceOr.Reference).ref.schemaName(), context)
                        .nest(NamingContext.DiscriminatedObjectCase(mappingName))
                }
        } else null
    }
}

private fun Model.merge(other: Model, name: NamingContext): Model = when (this) {
    else if this == other -> this
    is Model.Reference if (other is Model.Reference && this.context == other.context) -> this
    is Model.Primitive if other is Model.Primitive -> merge(other)
    is Model.Object if other is Model.Object -> merge(name, other)
    is Model.Collection if other is Model.Collection -> Model.Collection(
        inner = inner.merge(other.inner, name),
        default = default ?: other.default,
        description = description ?: other.description,
        constraint = constraint.merge(other.constraint),
        isNullable = isNullable || other.isNullable,
        title = title ?: other.title
    )

    is Model.Dictionary if other is Model.Dictionary -> Model.Dictionary(
        inner = inner.merge(other.inner, name),
        description = description ?: other.description,
        constraint = constraint.merge(other.constraint),
        isNullable = isNullable || other.isNullable,
        title = title ?: other.title
    )

    is Model.Object if other is Model.FreeFormJson -> copy(
        additionalProperties =
            additionalProperties.merge(Model.Object.AdditionalProperties.Allowed(true), context)
    )

    is Model.FreeFormJson if other is Model.Object -> other.copy(
        additionalProperties =
            other.additionalProperties.merge(Model.Object.AdditionalProperties.Allowed(true), name)
    )

    is Model.FreeFormJson if other is Model.FreeFormJson -> copy(
        description = description ?: other.description,
        constraint = constraint.merge(other.constraint),
        isNullable = isNullable || other.isNullable
    )

    is Model.FreeFormJson if other is Model.Union -> TODO()
    is Model.Union if other is Model.Union -> TODO("Implement Model.Union merge in allOf")
    is Model.Enum if other is Model.Enum -> {
        TODO("allOf Enum")
    }

    is Model.Collection,
    is Model.Dictionary,
    is Model.Enum,
    is Model.DiscriminatedObject,
    is Model.Object,
    is Model.Union,
    is Model.Reference,
    is Model.FreeFormJson,
    is Model.ByteArray,
    is Model.Date,
    is Model.Primitive,
    is Model.DateTime,
    is Model.Uuid -> error("Cannot merge allOf $this \n\n $other")
}

private fun Model.Primitive.merge(other: Model.Primitive): Model.Primitive = when (this) {
    is Model.Primitive.Boolean if other is Model.Primitive.Boolean -> Model.Primitive.Boolean(
        default = default ?: other.default,
        description = description ?: other.description,
        isNullable = isNullable || other.isNullable,
        title = title ?: other.title
    )

    is Model.Primitive.Double if other is Model.Primitive.Double -> Model.Primitive.Double(
        default = default ?: other.default,
        description = description ?: other.description,
        isNullable = isNullable || other.isNullable,
        constraint = constraint.merge(other.constraint),
        title = title ?: other.title
    )

    is Model.Primitive.Float if other is Model.Primitive.Float -> Model.Primitive.Float(
        default = default ?: other.default,
        description = description ?: other.description,
        constraint = constraint.merge(other.constraint),
        isNullable = isNullable || other.isNullable,
        title = title ?: other.title
    )

    is Model.Primitive.Int if other is Model.Primitive.Int -> Model.Primitive.Int(
        default = default ?: other.default,
        description = description ?: other.description,
        constraint = constraint.merge(other.constraint),
        isNullable = isNullable || other.isNullable,
        title = title ?: other.title,
    )

    is Model.Primitive.Long if other is Model.Primitive.Long -> Model.Primitive.Long(
        default = default ?: other.default,
        description = description ?: other.description,
        constraint = constraint.merge(other.constraint),
        isNullable = isNullable || other.isNullable,
        title = title ?: other.title
    )

    is Model.Primitive.String if other is Model.Primitive.String -> Model.Primitive.String(
        default = default ?: other.default,
        description = description ?: other.description,
        constraint = constraint.merge(other.constraint),
        isNullable = isNullable || other.isNullable,
        title = title ?: other.title
    )

    is Model.Primitive.Unit if other is Model.Primitive.Unit -> Model.Primitive.Unit(
        description = description ?: other.description,
        isNullable = isNullable || other.isNullable,
        title = title ?: other.title
    )

    is Model.Primitive -> error("Cannot merge allOf $this with $other")
}

private fun <A> let(a: A?, b: A?, block: (A, A) -> A): A? =
    if (a != null && b != null) block(a, b) else a ?: b

private fun Constraints.Number?.merge(other: Constraints.Number?): Constraints.Number? = let(this, other) { a, b ->
    Constraints.Number(
        minimum = mergeMinimumBound(a.minimum, b.minimum),
        maximum = mergeMaximumBound(a.maximum, b.maximum),
        multipleOf = a.multipleOf ?: b.multipleOf // TODO validate anything? calculate common denominator?
    )
}

private fun Constraints.Text?.merge(other: Constraints.Text?): Constraints.Text? = let(this, other) { a, b ->
    Constraints.Text(
        minLength = let(a.minLength, b.minLength, ::maxOf),
        maxLength = let(a.maxLength, b.maxLength, ::minOf),
        pattern = a.pattern ?: b.pattern // TODO
    )
}

private fun Constraints.Object?.merge(other: Constraints.Object?): Constraints.Object? = let(this, other) { a, b ->
    Constraints.Object(
        minProperties = let(a.minProperties, b.minProperties, ::maxOf),
        maxProperties = let(a.maxProperties, b.maxProperties, ::minOf)
    )
}

private fun Constraints.Collection?.merge(other: Constraints.Collection?): Constraints.Collection? =
    let(this, other) { a, b ->
        Constraints.Collection(
            minItems = let(a.minItems, b.minItems, ::maxOf),
            maxItems = let(a.maxItems, b.maxItems, ::minOf),
            uniqueItems = a.uniqueItems ?: false || b.uniqueItems ?: false
        )
    }

private fun Model.Object.merge(context: NamingContext, model: Model.Object): Model.Object {
    val properties = properties.combine(model.properties) { _, a, b ->
        Model.Object.Property(a.model.merge(b.model, context), a.isRequired || b.isRequired)
    }
    return Model.Object(
        context,
        description ?: model.description,
        title ?: model.title,
        properties,
        additionalProperties.merge(model.additionalProperties, context),
        isNullable || model.isNullable
    )
}

private fun Model.Object.AdditionalProperties.merge(
    other: Model.Object.AdditionalProperties,
    context: NamingContext
): Model.Object.AdditionalProperties =
    when (this) {
        is Model.Object.AdditionalProperties.Allowed if other is Model.Object.AdditionalProperties.Allowed ->
            Model.Object.AdditionalProperties.Allowed(value && other.value)

        is Model.Object.AdditionalProperties.Allowed if value -> other
        is Model.Object.AdditionalProperties.Schema if (other is Model.Object.AdditionalProperties.Allowed && other.value) -> this
        is Model.Object.AdditionalProperties.Schema if other is Model.Object.AdditionalProperties.Schema ->
            Model.Object.AdditionalProperties.Schema(value.merge(other.value, context))

        is Model.Object.AdditionalProperties.Schema,
        is Model.Object.AdditionalProperties.Allowed -> error("Cannot merge allOf $this with $other")
    }
