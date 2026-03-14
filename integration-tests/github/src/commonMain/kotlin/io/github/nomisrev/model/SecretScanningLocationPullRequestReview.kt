package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName

@Serializable
@JvmInline
value class SecretScanningLocationPullRequestReview(@SerialName("pull_request_review_url") val pullRequestReviewUrl: String)
