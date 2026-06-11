package io.github.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Review Dismissed Issue Event
 */
@Serializable
public data class ReviewDismissedIssueEvent(
  public val id: Long,
  @SerialName("node_id")
  public val nodeId: String,
  public val url: String,
  public val actor: SimpleUser,
  public val event: String,
  @SerialName("commit_id")
  public val commitId: String?,
  @SerialName("commit_url")
  public val commitUrl: String?,
  @SerialName("created_at")
  public val createdAt: String,
  @SerialName("performed_via_github_app")
  public val performedViaGithubApp: NullableIntegration?,
  @SerialName("dismissed_review")
  public val dismissedReview: DismissedReview,
) {
  @Serializable
  public data class DismissedReview(
    public val state: String,
    @SerialName("review_id")
    public val reviewId: Long,
    @SerialName("dismissal_message")
    public val dismissalMessage: String?,
    @SerialName("dismissal_commit_id")
    public val dismissalCommitId: String? = null,
  )
}
