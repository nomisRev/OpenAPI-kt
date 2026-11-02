package io.github.nomisrev.render.test.object_.type.array.nullable.optional

import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class OptionalNullableMessage(
  public val message: String? = null,
)
