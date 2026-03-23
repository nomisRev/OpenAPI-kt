package io.youtrack.model

import kotlin.Boolean
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class IssueTimeTracker(
  public val id: String? = null,
  public val workItems: List<BaseWorkItemRead.IssueWorkItem>? = null,
  public val enabled: Boolean? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
