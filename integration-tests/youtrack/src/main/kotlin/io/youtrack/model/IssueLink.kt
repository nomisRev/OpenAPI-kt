package io.youtrack.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class IssueLink(
  public val id: String? = null,
  public val direction: Direction? = null,
  public val linkType: IssueLinkTypeRead? = null,
  public val issues: List<IssueRead>? = null,
  public val trimmedIssues: List<IssueRead>? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
) {
  @Serializable
  public enum class Direction {
    OUTWARD,
    INWARD,
    BOTH,
  }
}
