package io.youtrack.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

@Serializable
public data class SprintWrite(
  public val name: String? = null,
  public val goal: String? = null,
  public val start: Long? = null,
  public val finish: Long? = null,
  public val archived: Boolean? = null,
  public val isDefault: Boolean? = null,
  public val issues: List<IssueWrite>? = null,
  public val previousSprint: SprintWrite? = null,
)
