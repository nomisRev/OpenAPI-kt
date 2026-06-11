package io.openai.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Approximate location parameters for the search.
 */
@Serializable
public data class WebSearchLocation(
  public val country: String? = null,
  public val region: String? = null,
  public val city: String? = null,
  public val timezone: String? = null,
)
