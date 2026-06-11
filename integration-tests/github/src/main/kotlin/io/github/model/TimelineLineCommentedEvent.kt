package io.github.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Timeline Line Commented Event
 */
@Serializable
public data class TimelineLineCommentedEvent(
  public val event: String? = null,
  @SerialName("node_id")
  public val nodeId: String? = null,
  public val comments: List<PullRequestReviewComment>? = null,
)
