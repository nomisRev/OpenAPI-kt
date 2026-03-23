package io.github.model

import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * Hovercard
 */
@JvmInline
@Serializable
public value class Hovercard(
  public val contexts: List<Contexts>,
) {
  @Serializable
  public data class Contexts(
    public val message: String,
    public val octicon: String,
  )
}
