package io.github.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Timeline Commit Commented Event
 */
@Serializable
public data class TimelineCommitCommentedEvent(
  public val event: String? = null,
  @SerialName("node_id")
  public val nodeId: String? = null,
  @SerialName("commit_id")
  public val commitId: String? = null,
  public val comments: List<CommitComment>? = null,
)
