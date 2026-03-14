package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName

@Serializable
@JvmInline
value class SecretScanningLocationPullRequestReviewComment(@SerialName("pull_request_review_comment_url") val pullRequestReviewCommentUrl: String)
