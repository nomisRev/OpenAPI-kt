package io.github.nomisrev.render.test.model

import kotlin.String
import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@Serializable
public data class Nullability(
  public val requiredNotNullable: String,
  public val optionalNotNullable: String? = null,
  public val requiredNullableNoDefault: String?,
  @Required
  public val requiredNullableWithDefault: String? = null,
)
