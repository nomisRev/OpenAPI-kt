package io.github.model

import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Timeline Line Commented Event
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class TimelineLineCommentedEvent(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val event: String? = null,
  @SerialName("node_id")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val nodeId: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val comments: List<PullRequestReviewComment>? = null,
)
