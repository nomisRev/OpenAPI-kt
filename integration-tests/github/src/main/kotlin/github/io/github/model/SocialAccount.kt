package io.github.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Social media account
 */
@Serializable
public data class SocialAccount(
  public val provider: String,
  public val url: String,
)
