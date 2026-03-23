package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Protected Branch Pull Request Review
 */
@Serializable
public data class ProtectedBranchPullRequestReview(
  public val url: String? = null,
  @SerialName("dismissal_restrictions")
  public val dismissalRestrictions: DismissalRestrictions? = null,
  @SerialName("bypass_pull_request_allowances")
  public val bypassPullRequestAllowances: BypassPullRequestAllowances? = null,
  @SerialName("dismiss_stale_reviews")
  public val dismissStaleReviews: Boolean,
  @SerialName("require_code_owner_reviews")
  public val requireCodeOwnerReviews: Boolean,
  @SerialName("required_approving_review_count")
  public val requiredApprovingReviewCount: Long? = null,
  @SerialName("require_last_push_approval")
  public val requireLastPushApproval: Boolean? = null,
) {
  /**
   * Allow specific users, teams, or apps to bypass pull request requirements.
   */
  @Serializable
  public data class BypassPullRequestAllowances(
    public val users: List<SimpleUser>? = null,
    public val teams: List<Team>? = null,
    public val apps: List<Integration?>? = null,
  )

  @Serializable
  public data class DismissalRestrictions(
    public val users: List<SimpleUser>? = null,
    public val teams: List<Team>? = null,
    public val apps: List<Integration?>? = null,
    public val url: String? = null,
    @SerialName("users_url")
    public val usersUrl: String? = null,
    @SerialName("teams_url")
    public val teamsUrl: String? = null,
  )
}
