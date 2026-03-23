package io.github.model

import kotlin.Long
import kotlin.time.Instant
import kotlinx.serialization.Serializable

@Serializable
public data class Traffic(
  public val timestamp: Instant,
  public val uniques: Long,
  public val count: Long,
)
