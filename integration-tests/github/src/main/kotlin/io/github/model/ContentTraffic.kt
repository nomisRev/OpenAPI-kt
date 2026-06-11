package io.github.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Content Traffic
 */
@Serializable
public data class ContentTraffic(
  public val path: String,
  public val title: String,
  public val count: Long,
  public val uniques: Long,
)
