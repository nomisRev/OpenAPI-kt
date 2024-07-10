package io.github.nomisrev.openapi

data class NumberConstraint(
  val exclusiveMinimum: Boolean,
  val minimum: Double,
  val exclusiveMaximum: Boolean,
  val maximum: Double,
  val multipleOf: Double?
) {
  constructor(
    schema: Schema
  ) : this(
    schema.exclusiveMinimum ?: false,
    schema.minimum ?: Double.NEGATIVE_INFINITY,
    schema.exclusiveMaximum ?: false,
    schema.maximum ?: Double.POSITIVE_INFINITY,
    schema.multipleOf
  )

  companion object {
    val NONE =
      NumberConstraint(false, Double.NEGATIVE_INFINITY, false, Double.POSITIVE_INFINITY, null)
  }
}

data class TextConstraint(val maxLength: Int, val minLength: Int, val pattern: String?) {
  constructor(
    schema: Schema
  ) : this(schema.maxLength ?: Int.MAX_VALUE, schema.minLength ?: 0, schema.pattern)

  companion object {
    val NONE = TextConstraint(Int.MAX_VALUE, 0, null)
  }
}

data class CollectionConstraint(
  val minItems: Int,
  val maxItems: Int,
) {
  constructor(
    schema: Schema
  ) : this(
    schema.minItems ?: 0,
    schema.maxItems ?: Int.MAX_VALUE,
  )

  companion object {
    val NONE = CollectionConstraint(0, Int.MAX_VALUE)
  }
}

// TODO `not` is not supported yet
/**
 * minProperties and maxProperties let you restrict the number of properties allowed in an object.
 * This can be useful when using additionalProperties, or free-form objects.
 */
data class ObjectConstraint(
  val minProperties: Int,
  val maxProperties: Int,
) {
  constructor(
    schema: Schema
  ) : this(
    schema.minProperties ?: 0,
    schema.maxProperties ?: Int.MAX_VALUE,
  )

  companion object {
    val NONE = ObjectConstraint(0, Int.MAX_VALUE)
  }
}
