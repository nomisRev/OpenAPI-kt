package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlin.jvm.JvmInline

@Serializable
data class ProtectedBranch(
    val url: String,
    @SerialName("required_status_checks") val requiredStatusChecks: StatusCheckPolicy? = null,
    @SerialName("required_pull_request_reviews") val requiredPullRequestReviews: RequiredPullRequestReviews? = null,
    @SerialName("required_signatures") val requiredSignatures: RequiredSignatures? = null,
    @SerialName("enforce_admins") val enforceAdmins: EnforceAdmins? = null,
    @SerialName("required_linear_history") val requiredLinearHistory: RequiredLinearHistory? = null,
    @SerialName("allow_force_pushes") val allowForcePushes: AllowForcePushes? = null,
    @SerialName("allow_deletions") val allowDeletions: AllowDeletions? = null,
    val restrictions: BranchRestrictionPolicy? = null,
    @SerialName("required_conversation_resolution") val requiredConversationResolution: RequiredConversationResolution? = null,
    @SerialName("block_creations") val blockCreations: BlockCreations? = null,
    @SerialName("lock_branch") val lockBranch: LockBranch? = null,
    @SerialName("allow_fork_syncing") val allowForkSyncing: AllowForkSyncing? = null,
) {
    @Serializable
    data class RequiredPullRequestReviews(
        val url: String,
        @SerialName("dismiss_stale_reviews") val dismissStaleReviews: Boolean? = null,
        @SerialName("require_code_owner_reviews") val requireCodeOwnerReviews: Boolean? = null,
        @SerialName("required_approving_review_count") val requiredApprovingReviewCount: Long? = null,
        @SerialName("require_last_push_approval") val requireLastPushApproval: Boolean? = null,
        @SerialName("dismissal_restrictions") val dismissalRestrictions: DismissalRestrictions? = null,
        @SerialName("bypass_pull_request_allowances") val bypassPullRequestAllowances: BypassPullRequestAllowances? = null,
    ) {
        @Serializable
        data class DismissalRestrictions(
            val url: String,
            @SerialName("users_url") val usersUrl: String,
            @SerialName("teams_url") val teamsUrl: String,
            val users: List<SimpleUser>,
            val teams: List<Team>,
            val apps: List<Integration>? = null,
        )

        @Serializable
        data class BypassPullRequestAllowances(
            val users: List<SimpleUser>,
            val teams: List<Team>,
            val apps: List<Integration>? = null,
        )
    }

    @Serializable
    data class RequiredSignatures(val url: String, val enabled: Boolean)

    @Serializable
    data class EnforceAdmins(val url: String, val enabled: Boolean)

    @Serializable
    @JvmInline
    value class RequiredLinearHistory(val enabled: Boolean)

    @Serializable
    @JvmInline
    value class AllowForcePushes(val enabled: Boolean)

    @Serializable
    @JvmInline
    value class AllowDeletions(val enabled: Boolean)

    @Serializable
    @JvmInline
    value class RequiredConversationResolution(val enabled: Boolean? = null)

    @Serializable
    @JvmInline
    value class BlockCreations(val enabled: Boolean)

    @Serializable
    @JvmInline
    value class LockBranch(val enabled: Boolean? = null)

    @Serializable
    @JvmInline
    value class AllowForkSyncing(val enabled: Boolean? = null)
}
