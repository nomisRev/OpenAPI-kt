package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Branch protections protect branches
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class ProtectedBranch(
  public val url: String,
  @SerialName("required_status_checks")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val requiredStatusChecks: StatusCheckPolicy? = null,
  @SerialName("required_pull_request_reviews")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val requiredPullRequestReviews: RequiredPullRequestReviews? = null,
  @SerialName("required_signatures")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val requiredSignatures: RequiredSignatures? = null,
  @SerialName("enforce_admins")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val enforceAdmins: EnforceAdmins? = null,
  @SerialName("required_linear_history")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val requiredLinearHistory: RequiredLinearHistory? = null,
  @SerialName("allow_force_pushes")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val allowForcePushes: AllowForcePushes? = null,
  @SerialName("allow_deletions")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val allowDeletions: AllowDeletions? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val restrictions: BranchRestrictionPolicy? = null,
  @SerialName("required_conversation_resolution")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val requiredConversationResolution: RequiredConversationResolution? = null,
  @SerialName("block_creations")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val blockCreations: BlockCreations? = null,
  @SerialName("lock_branch")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val lockBranch: LockBranch? = null,
  @SerialName("allow_fork_syncing")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val allowForkSyncing: AllowForkSyncing? = null,
) {
  @Serializable
  public data class AllowDeletions(
    public val enabled: Boolean,
  )

  @Serializable
  public data class AllowForcePushes(
    public val enabled: Boolean,
  )

  /**
   * Whether users can pull changes from upstream when the branch is locked. Set to `true` to allow fork syncing. Set to `false` to prevent fork syncing.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class AllowForkSyncing(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val enabled: Boolean? = null,
  )

  @Serializable
  public data class BlockCreations(
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
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class LockBranch(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val enabled: Boolean? = null,
  )

  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class RequiredConversationResolution(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val enabled: Boolean? = null,
  )

  @Serializable
  public data class RequiredLinearHistory(
    public val enabled: Boolean,
  )

  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class RequiredPullRequestReviews(
    public val url: String,
    @SerialName("dismiss_stale_reviews")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val dismissStaleReviews: Boolean? = null,
    @SerialName("require_code_owner_reviews")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val requireCodeOwnerReviews: Boolean? = null,
    @SerialName("required_approving_review_count")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val requiredApprovingReviewCount: Long? = null,
    @SerialName("require_last_push_approval")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val requireLastPushApproval: Boolean? = null,
    @SerialName("dismissal_restrictions")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val dismissalRestrictions: DismissalRestrictions? = null,
    @SerialName("bypass_pull_request_allowances")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val bypassPullRequestAllowances: BypassPullRequestAllowances? = null,
  ) {
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class BypassPullRequestAllowances(
      public val users: List<SimpleUser>,
      public val teams: List<Team>,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val apps: List<Integration?>? = null,
    )

    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class DismissalRestrictions(
      public val url: String,
      @SerialName("users_url")
      public val usersUrl: String,
      @SerialName("teams_url")
      public val teamsUrl: String,
      public val users: List<SimpleUser>,
      public val teams: List<Team>,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val apps: List<Integration?>? = null,
    )
  }

  @Serializable
  public data class RequiredSignatures(
    public val url: String,
    public val enabled: Boolean,
  )
}
