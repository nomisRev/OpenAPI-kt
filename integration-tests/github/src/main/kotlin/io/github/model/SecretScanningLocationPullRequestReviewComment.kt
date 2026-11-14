package io.github.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a 'pull_request_review_comment' secret scanning location type. This location type shows that a secret was detected in a review comment on a pull request.
 */
@Serializable
public data class SecretScanningLocationPullRequestReviewComment(
  @SerialName("pull_request_review_comment_url")
  public val pullRequestReviewCommentUrl: String,
)
