package io.github.nomisrev.openapi.transformers

import com.charleskorn.kaml.Yaml.Companion.default
import io.github.nomisrev.openapi.Constraints
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.SchemaContext
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.registry.ResolvedSchema
import io.github.nomisrev.openapi.registry.toModel

context(scope: Registry.Scope)
suspend fun ResolvedSchema.allOf(context: SchemaContext, allOf: List<ReferenceOr<Schema>>): Model =
    allOf.map { it.toModel(name, context) }.reduce { acc, or -> acc.merge(or, name) }

private fun Model.merge(other: Model, name: NamingContext): Model = when (this) {
    is Model.Reference if (other is Model.Reference && this.context == other.context) -> this
    is Model.Primitive if other is Model.Primitive -> merge(other)
    is Model.Object if other is Model.Object -> merge(name, other)
    is Model.Collection if other is Model.Collection -> Model.Collection(
        inner = inner.merge(other.inner, name),
        default = default ?: other.default,
        description = description ?: other.description,
        isNullable = isNullable || other.isNullable,
        constraint = constraint.merge(other.constraint)
    )

    is Model.Union if other is Model.Union -> TODO("Implement Model.Union merge in allOf")
    is Model.Enum if other is Model.Enum -> TODO("Union of values")
    is Model.Collection,
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
    is Model.Uuid -> throw IllegalStateException("Cannot merge allOf $this with $other")
}

private fun Model.Primitive.merge(other: Model.Primitive): Model.Primitive = when (this) {
    is Model.Primitive.Boolean if other is Model.Primitive.Boolean -> copy(
        default = default ?: other.default,
        description = description ?: other.description,
        isNullable = isNullable || other.isNullable
    )

    is Model.Primitive.Double if other is Model.Primitive.Double -> copy(
        default = default ?: other.default,
        description = description ?: other.description,
        isNullable = isNullable || other.isNullable,
        constraint = constraint.merge(other.constraint)
    )

    is Model.Primitive.Float if other is Model.Primitive.Float -> Model.Primitive.Float(
        default = default ?: other.default,
        description = description ?: other.description,
        isNullable = isNullable || other.isNullable,
        constraint = constraint.merge(other.constraint)
    )

    is Model.Primitive.Int if other is Model.Primitive.Int -> Model.Primitive.Int(
        default = default ?: other.default,
        description = description ?: other.description,
        isNullable = isNullable || other.isNullable,
        constraint = constraint.merge(other.constraint)
    )

    is Model.Primitive.Long if other is Model.Primitive.Long -> Model.Primitive.Long(
        default = default ?: other.default,
        description = description ?: other.description,
        isNullable = isNullable || other.isNullable,
        constraint = constraint.merge(other.constraint)
    )

    is Model.Primitive.String if other is Model.Primitive.String -> Model.Primitive.String(
        default = default ?: other.default,
        description = description ?: other.description,
        isNullable = isNullable || other.isNullable,
        constraint = constraint.merge(other.constraint)
    )

    is Model.Primitive.Unit if other is Model.Primitive.Unit -> Model.Primitive.Unit(
        description = description ?: other.description,
        isNullable = isNullable || other.isNullable
    )

    is Model.Primitive -> throw IllegalStateException("Cannot merge allOf $this with $other")
}

private fun <A> let(a: A?, b: A?, block: (A, A) -> A): A? =
    if (a != null && b != null) block(a, b) else a ?: b

private fun Constraints.Number?.merge(other: Constraints.Number?): Constraints.Number? = let(this, other) { a, b ->
    Constraints.Number(
        minimum = let(a.minimum, b.minimum, ::maxOf),
        maximum = let(a.maximum, b.maximum, ::minOf),
        exclusiveMinimum = a.exclusiveMinimum ?: false || b.exclusiveMinimum ?: false,
        exclusiveMaximum = a.exclusiveMaximum ?: false || b.exclusiveMaximum ?: false,
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
    val properties = (properties + model.properties)
        .groupBy { it.baseName }
        .values
        .map { props -> props.reduce { acc, prop -> acc.merge(prop, context) } }
    return Model.Object(
        context,
        description ?: model.description,
        properties,
        inline + model.inline,
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
        is Model.Object.AdditionalProperties.Allowed -> throw IllegalStateException("Cannot merge allOf $this with $other")
    }

private fun Model.Object.Property.merge(other: Model.Object.Property, context: NamingContext): Model.Object.Property =
    Model.Object.Property(baseName, model.merge(other.model, context), isRequired || other.isRequired)
