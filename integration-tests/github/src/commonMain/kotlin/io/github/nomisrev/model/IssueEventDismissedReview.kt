package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class IssueEventDismissedReview(
    val state: String,
    @SerialName("review_id") val reviewId: Long,
    @SerialName("dismissal_message") val dismissalMessage: String?,
    @SerialName("dismissal_commit_id") val dismissalCommitId: String? = null,
)
