package io.github.model

import kotlin.Long
import kotlinx.serialization.Serializable

@Serializable
public data class RateLimit(
  public val limit: Long,
  public val remaining: Long,
  public val reset: Long,
  public val used: Long,
)
