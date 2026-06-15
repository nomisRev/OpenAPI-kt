package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.parser.Schema
import kotlinx.serialization.Serializable

@Serializable
sealed interface Constraints {
    @Serializable
    data class Number(
        val minimum: Bound?,
        val maximum: Bound?,
        val multipleOf: Double?,
    ) : Constraints {
        @Serializable
        data class Bound(
            val value: Double,
            val exclusive: Boolean,
        )

        companion object {
            operator fun invoke(schema: Schema): Number? {
                val minimum = schema.effectiveMinimum()
                val maximum = schema.effectiveMaximum()
                val hasValidation = minimum != null || maximum != null || schema.multipleOf != null

                return if (hasValidation)
                    Number(
                        minimum,
                        maximum,
                        schema.multipleOf,
                    )
                else null
            }
        }
    }

    @Serializable
    data class Text(val minLength: Int?, val maxLength: Int?, val pattern: String?) : Constraints {
        companion object {
            operator fun invoke(schema: Schema): Text? =
                if (schema.maxLength != null || schema.minLength != null || schema.pattern != null)
                    Text(schema.minLength ?: 0, schema.maxLength, schema.pattern)
                else null
        }

        override fun toString(): String = buildString {
            if (minLength != null) append("minLength: $minLength, ")
            if (maxLength != null) append("maxLength: $maxLength, ")
            if (pattern != null) append("pattern: $pattern, ")
        }
    }

    @Serializable
    data class Collection(val minItems: Int?, val maxItems: Int?, val uniqueItems: Boolean?) :
        Constraints {
        companion object {
            operator fun invoke(schema: Schema): Collection? =
                if (schema.minItems != null || schema.maxItems != null || schema.uniqueItems == true)
                    Collection(
                        minItems = schema.minItems,
                        maxItems = schema.maxItems,
                        uniqueItems = schema.uniqueItems,
                    )
                else null
        }
    }

    // TODO `not` is not supported yet
    // TODO `anyOf` is not supported yet
    // TODO `oneOf` is not supported yet
    // TODO additionalProperties.PSchema is not supported yet
    /**
     * minProperties and maxProperties let you restrict the number of properties allowed in an object.
     * This can be useful when using additionalProperties, or free-form objects.
     */
    @Serializable
    data class Object(
        val minProperties: Int?,
        val maxProperties: Int?
    ) : Constraints {
        companion object {
            operator fun invoke(schema: Schema): Object? =
                if (schema.minProperties != null || schema.maxProperties != null)
                    Object(schema.minProperties, schema.maxProperties)
                else null
        }
    }
}

internal fun Schema.effectiveMinimum(): Constraints.Number.Bound? {
    val inclusive = minimum?.let { Constraints.Number.Bound(it, exclusive = false) }
    val exclusive = when (val exclusive = exclusiveMinimum) {
        is Schema.ExclusiveLimit.BooleanValue ->
            if (exclusive.value) minimum?.let { Constraints.Number.Bound(it, exclusive = true) } else null

        is Schema.ExclusiveLimit.NumberValue -> Constraints.Number.Bound(exclusive.value, exclusive = true)
        null -> null
    }
    return mergeMinimumBound(inclusive, exclusive)
}

internal fun Schema.effectiveMaximum(): Constraints.Number.Bound? {
    val inclusive = maximum?.let { Constraints.Number.Bound(it, exclusive = false) }
    val exclusive = when (val exclusive = exclusiveMaximum) {
        is Schema.ExclusiveLimit.BooleanValue ->
            if (exclusive.value) maximum?.let { Constraints.Number.Bound(it, exclusive = true) } else null

        is Schema.ExclusiveLimit.NumberValue -> Constraints.Number.Bound(exclusive.value, exclusive = true)
        null -> null
    }
    return mergeMaximumBound(inclusive, exclusive)
}

internal fun mergeMinimumBound(
    a: Constraints.Number.Bound?,
    b: Constraints.Number.Bound?,
): Constraints.Number.Bound? = when {
    a == null -> b
    b == null -> a
    a.value > b.value -> a
    b.value > a.value -> b
    else -> Constraints.Number.Bound(a.value, exclusive = a.exclusive || b.exclusive)
}

internal fun mergeMaximumBound(
    a: Constraints.Number.Bound?,
    b: Constraints.Number.Bound?,
): Constraints.Number.Bound? = when {
    a == null -> b
    b == null -> a
    a.value < b.value -> a
    b.value < a.value -> b
    else -> Constraints.Number.Bound(a.value, exclusive = a.exclusive || b.exclusive)
}
