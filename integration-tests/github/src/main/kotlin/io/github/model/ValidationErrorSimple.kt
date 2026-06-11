package io.github.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Validation Error Simple
 */
@Serializable
public data class ValidationErrorSimple(
  public val message: String,
  @SerialName("documentation_url")
  public val documentationUrl: String,
  public val errors: List<String>? = null,
)
