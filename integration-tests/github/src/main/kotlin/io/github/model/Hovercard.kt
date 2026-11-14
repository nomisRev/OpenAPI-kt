package io.github.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * Hovercard
 */
@Serializable
public data class Hovercard(
  public val contexts: List<Contexts>,
) {
  @Serializable
  public data class Contexts(
    public val message: String,
    public val octicon: String,
  )
}
