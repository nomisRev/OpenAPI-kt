package io.github.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ReactionRollup(
  public val url: String,
  @SerialName("total_count")
  public val totalCount: Long,
  public val `+1`: Long,
  public val `-1`: Long,
  public val laugh: Long,
  public val confused: Long,
  public val heart: Long,
  public val hooray: Long,
  public val eyes: Long,
  public val rocket: Long,
)
