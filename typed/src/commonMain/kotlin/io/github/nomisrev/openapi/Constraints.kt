package io.github.nomisrev.openapi

data class NumberConstraint(
  val exclusiveMinimum: Boolean,
  val minimum: Double,
  val exclusiveMaximum: Boolean,
  val maximum: Double,
  val multipleOf: Double?
) {
  constructor(schema: Schema) : this(
    schema.exclusiveMinimum ?: false,
    schema.minimum ?: Double.NEGATIVE_INFINITY,
    schema.exclusiveMaximum ?: false,
    schema.maximum ?: Double.POSITIVE_INFINITY,
    schema.multipleOf
  )
}

data class TextConstraint(val maxLength: Int, val minLength: Int, val pattern: String?) {
  constructor(schema: Schema) : this(schema.maxLength ?: Int.MAX_VALUE, schema.minLength ?: 0, schema.pattern)
}

data class CollectionConstraint(
  val minItems: Int,
  val maxItems: Int,
) {
  constructor(schema: Schema) : this(
    schema.minItems ?: 0,
    schema.maxItems ?: Int.MAX_VALUE,
  )
}

// TODO `not` is not supported yet
data class ObjectConstraint(
  val minProperties: Int,
  val maxProperties: Int,
) {
  constructor(schema: Schema): this(
    schema.minProperties ?: 0,
    schema.maxProperties ?: Int.MAX_VALUE,
  )
}
