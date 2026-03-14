package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ReviewDismissedIssueEvent(
    val id: Long,
    @SerialName("node_id") val nodeId: String,
    val url: String,
    val actor: SimpleUser,
    val event: String,
    @SerialName("commit_id") val commitId: String?,
    @SerialName("commit_url") val commitUrl: String?,
    @SerialName("created_at") val createdAt: String,
    @SerialName("performed_via_github_app") val performedViaGithubApp: NullableIntegration?,
    @SerialName("dismissed_review") val dismissedReview: DismissedReview,
) {
    @Serializable
    data class DismissedReview(
        val state: String,
        @SerialName("review_id") val reviewId: Long,
        @SerialName("dismissal_message") val dismissalMessage: String?,
        @SerialName("dismissal_commit_id") val dismissalCommitId: String? = null,
    )
}
