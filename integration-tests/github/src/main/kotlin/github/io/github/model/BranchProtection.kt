package io.github.model

import kotlin.Boolean
import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Branch Protection
 */
@Serializable
public data class BranchProtection(
  public val url: String? = null,
  public val enabled: Boolean? = null,
  @SerialName("required_status_checks")
  public val requiredStatusChecks: ProtectedBranchRequiredStatusCheck? = null,
  @SerialName("enforce_admins")
  public val enforceAdmins: ProtectedBranchAdminEnforced? = null,
  @SerialName("required_pull_request_reviews")
  public val requiredPullRequestReviews: ProtectedBranchPullRequestReview? = null,
  public val restrictions: BranchRestrictionPolicy? = null,
  @SerialName("required_linear_history")
  public val requiredLinearHistory: RequiredLinearHistory? = null,
  @SerialName("allow_force_pushes")
  public val allowForcePushes: AllowForcePushes? = null,
  @SerialName("allow_deletions")
  public val allowDeletions: AllowDeletions? = null,
  @SerialName("block_creations")
  public val blockCreations: BlockCreations? = null,
  @SerialName("required_conversation_resolution")
  public val requiredConversationResolution: RequiredConversationResolution? = null,
  public val name: String? = null,
  @SerialName("protection_url")
  public val protectionUrl: String? = null,
  @SerialName("required_signatures")
  public val requiredSignatures: RequiredSignatures? = null,
  @SerialName("lock_branch")
  public val lockBranch: LockBranch? = null,
  @SerialName("allow_fork_syncing")
  public val allowForkSyncing: AllowForkSyncing? = null,
) {
  @JvmInline
  @Serializable
  public value class AllowDeletions(
    public val enabled: Boolean? = null,
  )

  @JvmInline
  @Serializable
  public value class AllowForcePushes(
    public val enabled: Boolean? = null,
  )

  /**
   * Whether users can pull changes from upstream when the branch is locked. Set to `true` to allow fork syncing. Set to `false` to prevent fork syncing.
   */
  @JvmInline
  @Serializable
  public value class AllowForkSyncing(
    public val enabled: Boolean? = null,
  )

  @JvmInline
  @Serializable
  public value class BlockCreations(
    public val enabled: Boolean? = null,
  )

  /**
   * Whether to set the branch as read-only. If this is true, users will not be able to push to the branch.
   */
  @JvmInline
  @Serializable
  public value class LockBranch(
    public val enabled: Boolean? = null,
  )

  @JvmInline
  @Serializable
  public value class RequiredConversationResolution(
    public val enabled: Boolean? = null,
  )

  @JvmInline
  @Serializable
  public value class RequiredLinearHistory(
    public val enabled: Boolean? = null,
  )

  @Serializable
  public data class RequiredSignatures(
    public val url: String,
    public val enabled: Boolean,
  )
}
