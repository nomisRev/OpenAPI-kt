package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ProtectedBranchPullRequestReview(
    val url: String? = null,
    @SerialName("dismissal_restrictions") val dismissalRestrictions: DismissalRestrictions? = null,
    @SerialName("bypass_pull_request_allowances") val bypassPullRequestAllowances: BypassPullRequestAllowances? = null,
    @SerialName("dismiss_stale_reviews") val dismissStaleReviews: Boolean,
    @SerialName("require_code_owner_reviews") val requireCodeOwnerReviews: Boolean,
    @SerialName("required_approving_review_count") val requiredApprovingReviewCount: Long? = null,
    @SerialName("require_last_push_approval") val requireLastPushApproval: Boolean? = null,
) {
    @Serializable
    data class DismissalRestrictions(
        val users: List<SimpleUser>? = null,
        val teams: List<Team>? = null,
        val apps: List<Integration>? = null,
        val url: String? = null,
        @SerialName("users_url") val usersUrl: String? = null,
        @SerialName("teams_url") val teamsUrl: String? = null,
    )

    @Serializable
    data class BypassPullRequestAllowances(
        val users: List<SimpleUser>? = null,
        val teams: List<Team>? = null,
        val apps: List<Integration>? = null,
    )
}
