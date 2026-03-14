package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlin.jvm.JvmInline

@Serializable
data class BranchProtection(
    val url: String? = null,
    val enabled: Boolean? = null,
    @SerialName("required_status_checks") val requiredStatusChecks: ProtectedBranchRequiredStatusCheck? = null,
    @SerialName("enforce_admins") val enforceAdmins: ProtectedBranchAdminEnforced? = null,
    @SerialName("required_pull_request_reviews") val requiredPullRequestReviews: ProtectedBranchPullRequestReview? = null,
    val restrictions: BranchRestrictionPolicy? = null,
    @SerialName("required_linear_history") val requiredLinearHistory: RequiredLinearHistory? = null,
    @SerialName("allow_force_pushes") val allowForcePushes: AllowForcePushes? = null,
    @SerialName("allow_deletions") val allowDeletions: AllowDeletions? = null,
    @SerialName("block_creations") val blockCreations: BlockCreations? = null,
    @SerialName("required_conversation_resolution") val requiredConversationResolution: RequiredConversationResolution? = null,
    val name: String? = null,
    @SerialName("protection_url") val protectionUrl: String? = null,
    @SerialName("required_signatures") val requiredSignatures: RequiredSignatures? = null,
    @SerialName("lock_branch") val lockBranch: LockBranch? = null,
    @SerialName("allow_fork_syncing") val allowForkSyncing: AllowForkSyncing? = null,
) {
    @Serializable
    @JvmInline
    value class RequiredLinearHistory(val enabled: Boolean? = null)

    @Serializable
    @JvmInline
    value class AllowForcePushes(val enabled: Boolean? = null)

    @Serializable
    @JvmInline
    value class AllowDeletions(val enabled: Boolean? = null)

    @Serializable
    @JvmInline
    value class BlockCreations(val enabled: Boolean? = null)

    @Serializable
    @JvmInline
    value class RequiredConversationResolution(val enabled: Boolean? = null)

    @Serializable
    data class RequiredSignatures(val url: String, val enabled: Boolean)

    @Serializable
    @JvmInline
    value class LockBranch(val enabled: Boolean? = null)

    @Serializable
    @JvmInline
    value class AllowForkSyncing(val enabled: Boolean? = null)
}
