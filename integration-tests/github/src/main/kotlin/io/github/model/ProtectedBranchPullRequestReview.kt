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
 * Protected Branch Pull Request Review
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class ProtectedBranchPullRequestReview(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val url: String? = null,
  @SerialName("dismissal_restrictions")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val dismissalRestrictions: DismissalRestrictions? = null,
  @SerialName("bypass_pull_request_allowances")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val bypassPullRequestAllowances: BypassPullRequestAllowances? = null,
  @SerialName("dismiss_stale_reviews")
  public val dismissStaleReviews: Boolean,
  @SerialName("require_code_owner_reviews")
  public val requireCodeOwnerReviews: Boolean,
  @SerialName("required_approving_review_count")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val requiredApprovingReviewCount: Long? = null,
  @SerialName("require_last_push_approval")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val requireLastPushApproval: Boolean? = null,
) {
  /**
   * Allow specific users, teams, or apps to bypass pull request requirements.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class BypassPullRequestAllowances(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val users: List<SimpleUser>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val teams: List<Team>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val apps: List<Integration?>? = null,
  )

  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class DismissalRestrictions(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val users: List<SimpleUser>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val teams: List<Team>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val apps: List<Integration?>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val url: String? = null,
    @SerialName("users_url")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val usersUrl: String? = null,
    @SerialName("teams_url")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val teamsUrl: String? = null,
  )
}
