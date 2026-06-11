package io.github.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a 'pull_request_review' secret scanning location type. This location type shows that a secret was detected in a review on a pull request.
 */
@JvmInline
@Serializable
public value class SecretScanningLocationPullRequestReview(
  @SerialName("pull_request_review_url")
  public val pullRequestReviewUrl: String,
)
