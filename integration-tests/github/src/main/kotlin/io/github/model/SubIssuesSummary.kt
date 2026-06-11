package io.github.model

import kotlin.Long
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class SubIssuesSummary(
  public val total: Long,
  public val completed: Long,
  @SerialName("percent_completed")
  public val percentCompleted: Long,
)
