package io.github.nomisrev.openapi

sealed interface Constraints {
  data class Number(
    val exclusiveMinimum: Boolean,
    val minimum: Double,
    val exclusiveMaximum: Boolean,
    val maximum: Double,
    val multipleOf: Double?,
  ) : Constraints {
    companion object {
      operator fun invoke(schema: Schema): Number? =
        if (schema.minimum != null || schema.maximum != null || schema.multipleOf != null)
          Number(
            schema.exclusiveMinimum ?: false,
            schema.minimum ?: Double.NEGATIVE_INFINITY,
            schema.exclusiveMaximum ?: false,
            schema.maximum ?: Double.POSITIVE_INFINITY,
            schema.multipleOf,
          )
        else null
    }
  }

  data class Text(val minLength: Int, val maxLength: Int, val pattern: String?) : Constraints {
    companion object {
      operator fun invoke(schema: Schema): Text? =
        if (schema.maxLength != null || schema.minLength != null || schema.pattern != null)
          Text(schema.minLength ?: 0, schema.maxLength ?: Int.MAX_VALUE, schema.pattern)
        else null
    }
  }

  data class Collection(val minItems: Int, val maxItems: Int, val uniqueItems: Boolean? = null) :
    Constraints {
    companion object {
      operator fun invoke(schema: Schema): Collection? =
        if (schema.minItems != null || schema.maxItems != null || schema.uniqueItems == true)
          Collection(
            minItems = schema.minItems ?: 0,
            maxItems = schema.maxItems ?: Int.MAX_VALUE,
            uniqueItems = schema.uniqueItems,
          )
        else null
    }
  }

  // TODO `not` is not supported yet
  /**
   * minProperties and maxProperties let you restrict the number of properties allowed in an object.
   * This can be useful when using additionalProperties, or free-form objects.
   */
  data class Object(val minProperties: Int, val maxProperties: Int) : Constraints {
    companion object {
      operator fun invoke(schema: Schema): Object? =
        if (schema.minProperties != null || schema.maxProperties != null)
          Object(schema.minProperties ?: 0, schema.maxProperties ?: Int.MAX_VALUE)
        else null
    }
  }
}
