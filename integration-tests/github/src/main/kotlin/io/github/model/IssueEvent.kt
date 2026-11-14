package io.github.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Issue Event
 */
@OptIn(ExperimentalSerializationApi::class)
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
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val issue: Optional<NullableIssue> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val label: IssueEventLabel? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val assignee: Optional<NullableSimpleUser> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val assigner: Optional<NullableSimpleUser> = Optional.Absent,
  @SerialName("review_requester")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val reviewRequester: Optional<NullableSimpleUser> = Optional.Absent,
  @SerialName("requested_reviewer")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val requestedReviewer: Optional<NullableSimpleUser> = Optional.Absent,
  @SerialName("requested_team")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val requestedTeam: Team? = null,
  @SerialName("dismissed_review")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val dismissedReview: IssueEventDismissedReview? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val milestone: IssueEventMilestone? = null,
  @SerialName("project_card")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val projectCard: IssueEventProjectCard? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val rename: IssueEventRename? = null,
  @SerialName("author_association")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val authorAssociation: AuthorAssociation? = null,
  @SerialName("lock_reason")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val lockReason: Optional<String> = Optional.Absent,
  @SerialName("performed_via_github_app")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val performedViaGithubApp: Optional<NullableIntegration> = Optional.Absent,
)
