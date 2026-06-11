package io.github.model

import kotlin.Long
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * Clone Traffic
 */
@Serializable
public data class CloneTraffic(
  public val count: Long,
  public val uniques: Long,
  public val clones: List<Traffic>,
)
