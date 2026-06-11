package io.youtrack.model

import kotlin.Boolean
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents users that are subscribed to notifications about the issue.
 */
@Serializable
public data class IssueWatchers(
  public val id: String? = null,
  public val hasStar: Boolean? = null,
  public val issueWatchers: List<IssueWatcher>? = null,
  public val duplicateWatchers: List<IssueWatcher>? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
