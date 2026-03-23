package io.youtrack.model

import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class SprintRead(
  public val id: String? = null,
  public val agile: AgileRead? = null,
  public val name: String? = null,
  public val goal: String? = null,
  public val start: Long? = null,
  public val finish: Long? = null,
  public val archived: Boolean? = null,
  public val isDefault: Boolean? = null,
  public val issues: List<IssueRead>? = null,
  public val unresolvedIssuesCount: Int? = null,
  public val previousSprint: SprintRead? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
