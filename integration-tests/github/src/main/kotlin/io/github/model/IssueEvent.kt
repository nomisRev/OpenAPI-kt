package io.github.model

import kotlin.Long
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Issue Event
 */
@Serializable
public data class IssueEvent(
  public val id: Long,
  @SerialName("node_id")
  public val nodeId: String,
  public val url: String,
  public val actor: NullableSimpleUser?,
  public val event: String,
  @SerialName("commit_id")
  public val commitId: String?,
  @SerialName("commit_url")
  public val commitUrl: String?,
  @SerialName("created_at")
  public val createdAt: Instant,
  public val issue: NullableIssue? = null,
  public val label: IssueEventLabel? = null,
  public val assignee: NullableSimpleUser? = null,
  public val assigner: NullableSimpleUser? = null,
  @SerialName("review_requester")
  public val reviewRequester: NullableSimpleUser? = null,
  @SerialName("requested_reviewer")
  public val requestedReviewer: NullableSimpleUser? = null,
  @SerialName("requested_team")
  public val requestedTeam: Team? = null,
  @SerialName("dismissed_review")
  public val dismissedReview: IssueEventDismissedReview? = null,
  public val milestone: IssueEventMilestone? = null,
  @SerialName("project_card")
  public val projectCard: IssueEventProjectCard? = null,
  public val rename: IssueEventRename? = null,
  @SerialName("author_association")
  public val authorAssociation: AuthorAssociation? = null,
  @SerialName("lock_reason")
  public val lockReason: String? = null,
  @SerialName("performed_via_github_app")
  public val performedViaGithubApp: NullableIntegration? = null,
)
