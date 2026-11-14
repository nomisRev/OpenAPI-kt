package io.github.model

import kotlin.Boolean
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Branch Protection
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class BranchProtection(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val url: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val enabled: Boolean? = null,
  @SerialName("required_status_checks")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val requiredStatusChecks: ProtectedBranchRequiredStatusCheck? = null,
  @SerialName("enforce_admins")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val enforceAdmins: ProtectedBranchAdminEnforced? = null,
  @SerialName("required_pull_request_reviews")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val requiredPullRequestReviews: ProtectedBranchPullRequestReview? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val restrictions: BranchRestrictionPolicy? = null,
  @SerialName("required_linear_history")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val requiredLinearHistory: RequiredLinearHistory? = null,
  @SerialName("allow_force_pushes")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val allowForcePushes: AllowForcePushes? = null,
  @SerialName("allow_deletions")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val allowDeletions: AllowDeletions? = null,
  @SerialName("block_creations")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val blockCreations: BlockCreations? = null,
  @SerialName("required_conversation_resolution")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val requiredConversationResolution: RequiredConversationResolution? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val name: String? = null,
  @SerialName("protection_url")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val protectionUrl: String? = null,
  @SerialName("required_signatures")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val requiredSignatures: RequiredSignatures? = null,
  @SerialName("lock_branch")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val lockBranch: LockBranch? = null,
  @SerialName("allow_fork_syncing")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val allowForkSyncing: AllowForkSyncing? = null,
) {
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class AllowDeletions(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val enabled: Boolean? = null,
  )

  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class AllowForcePushes(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val enabled: Boolean? = null,
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

  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class BlockCreations(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val enabled: Boolean? = null,
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

  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class RequiredLinearHistory(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val enabled: Boolean? = null,
  )

  @Serializable
  public data class RequiredSignatures(
    public val url: String,
    public val enabled: Boolean,
  )
}
