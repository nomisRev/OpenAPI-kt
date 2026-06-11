package io.github.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a 'discussion_comment' secret scanning location type. This location type shows that a secret was detected in a comment on a discussion.
 */
@JvmInline
@Serializable
public value class SecretScanningLocationDiscussionComment(
  @SerialName("discussion_comment_url")
  public val discussionCommentUrl: String,
)
