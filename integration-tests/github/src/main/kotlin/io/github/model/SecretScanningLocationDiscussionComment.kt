package io.github.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a 'discussion_comment' secret scanning location type. This location type shows that a secret was detected in a comment on a discussion.
 */
@Serializable
public data class SecretScanningLocationDiscussionComment(
  @SerialName("discussion_comment_url")
  public val discussionCommentUrl: String,
)
