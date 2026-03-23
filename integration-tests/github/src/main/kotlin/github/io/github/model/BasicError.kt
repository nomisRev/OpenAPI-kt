package io.github.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Basic Error
 */
@Serializable
public data class BasicError(
  public val message: String? = null,
  @SerialName("documentation_url")
  public val documentationUrl: String? = null,
  public val url: String? = null,
  public val status: String? = null,
)
