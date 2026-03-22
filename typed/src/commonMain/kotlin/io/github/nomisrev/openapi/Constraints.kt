package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.parser.Schema
import kotlinx.serialization.Serializable

@Serializable
sealed interface Constraints {
    @Serializable
    data class Number(
        val exclusiveMinimum: Boolean?,
        val minimum: Double?,
        val exclusiveMaximum: Boolean?,
        val maximum: Double?,
        val multipleOf: Double?,
    ) : Constraints {
        companion object {
            operator fun invoke(schema: Schema): Number? {
                val hasValidation =
                    schema.exclusiveMinimum != null || schema.minimum != null || schema.maximum != null ||
                            schema.multipleOf != null || schema.exclusiveMaximum != null

                return if (hasValidation)
                    Number(
                        schema.exclusiveMinimum ?: false,
                        schema.minimum,
                        schema.exclusiveMaximum ?: false,
                        schema.maximum,
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
