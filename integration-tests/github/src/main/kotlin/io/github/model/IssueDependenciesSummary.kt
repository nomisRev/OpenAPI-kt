package io.github.model

import kotlin.Long
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class IssueDependenciesSummary(
  @SerialName("blocked_by")
  public val blockedBy: Long,
  public val blocking: Long,
  @SerialName("total_blocked_by")
  public val totalBlockedBy: Long,
  @SerialName("total_blocking")
  public val totalBlocking: Long,
)
