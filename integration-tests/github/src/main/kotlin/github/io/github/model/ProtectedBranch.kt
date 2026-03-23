package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Branch protections protect branches
 */
@Serializable
public data class ProtectedBranch(
  public val url: String,
  @SerialName("required_status_checks")
  public val requiredStatusChecks: StatusCheckPolicy? = null,
  @SerialName("required_pull_request_reviews")
  public val requiredPullRequestReviews: RequiredPullRequestReviews? = null,
  @SerialName("required_signatures")
  public val requiredSignatures: RequiredSignatures? = null,
  @SerialName("enforce_admins")
  public val enforceAdmins: EnforceAdmins? = null,
  @SerialName("required_linear_history")
  public val requiredLinearHistory: RequiredLinearHistory? = null,
  @SerialName("allow_force_pushes")
  public val allowForcePushes: AllowForcePushes? = null,
  @SerialName("allow_deletions")
  public val allowDeletions: AllowDeletions? = null,
  public val restrictions: BranchRestrictionPolicy? = null,
  @SerialName("required_conversation_resolution")
  public val requiredConversationResolution: RequiredConversationResolution? = null,
  @SerialName("block_creations")
  public val blockCreations: BlockCreations? = null,
  @SerialName("lock_branch")
  public val lockBranch: LockBranch? = null,
  @SerialName("allow_fork_syncing")
  public val allowForkSyncing: AllowForkSyncing? = null,
) {
  @JvmInline
  @Serializable
  public value class AllowDeletions(
    public val enabled: Boolean,
  )

  @JvmInline
  @Serializable
  public value class AllowForcePushes(
    public val enabled: Boolean,
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
    public val enabled: Boolean,
  )

  @Serializable
  public data class EnforceAdmins(
    public val url: String,
    public val enabled: Boolean,
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
    public val enabled: Boolean,
  )

  @Serializable
  public data class RequiredPullRequestReviews(
    public val url: String,
    @SerialName("dismiss_stale_reviews")
    public val dismissStaleReviews: Boolean? = null,
    @SerialName("require_code_owner_reviews")
    public val requireCodeOwnerReviews: Boolean? = null,
    @SerialName("required_approving_review_count")
    public val requiredApprovingReviewCount: Long? = null,
    @SerialName("require_last_push_approval")
    public val requireLastPushApproval: Boolean? = null,
    @SerialName("dismissal_restrictions")
    public val dismissalRestrictions: DismissalRestrictions? = null,
    @SerialName("bypass_pull_request_allowances")
    public val bypassPullRequestAllowances: BypassPullRequestAllowances? = null,
  ) {
    @Serializable
    public data class BypassPullRequestAllowances(
      public val users: List<SimpleUser>,
      public val teams: List<Team>,
      public val apps: List<Integration?>? = null,
    )

    @Serializable
    public data class DismissalRestrictions(
      public val url: String,
      @SerialName("users_url")
      public val usersUrl: String,
      @SerialName("teams_url")
      public val teamsUrl: String,
      public val users: List<SimpleUser>,
      public val teams: List<Team>,
      public val apps: List<Integration?>? = null,
    )
  }

  @Serializable
  public data class RequiredSignatures(
    public val url: String,
    public val enabled: Boolean,
  )
}
