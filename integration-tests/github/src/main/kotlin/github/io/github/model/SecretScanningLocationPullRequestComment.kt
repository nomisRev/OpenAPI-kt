package io.github.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a 'pull_request_comment' secret scanning location type. This location type shows that a secret was detected in a comment on a pull request.
 */
@JvmInline
@Serializable
public value class SecretScanningLocationPullRequestComment(
  @SerialName("pull_request_comment_url")
  public val pullRequestCommentUrl: String,
)
