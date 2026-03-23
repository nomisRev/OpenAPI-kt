package io.github.model

import kotlin.Long
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * View Traffic
 */
@Serializable
public data class ViewTraffic(
  public val count: Long,
  public val uniques: Long,
  public val views: List<Traffic>,
)
