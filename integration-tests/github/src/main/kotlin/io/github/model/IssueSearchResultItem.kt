package io.github.model

import kotlin.Boolean
import kotlin.Double
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.time.Instant
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Issue Search Result Item
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class IssueSearchResultItem(
  public val url: String,
  @SerialName("repository_url")
  public val repositoryUrl: String,
  @SerialName("labels_url")
  public val labelsUrl: String,
  @SerialName("comments_url")
  public val commentsUrl: String,
  @SerialName("events_url")
  public val eventsUrl: String,
  @SerialName("html_url")
  public val htmlUrl: String,
  public val id: Long,
  @SerialName("node_id")
  public val nodeId: String,
  public val number: Long,
  public val title: String,
  public val locked: Boolean,
  @SerialName("active_lock_reason")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val activeLockReason: Optional<String> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val assignees: Optional<List<SimpleUser>> = Optional.Absent,
  public val user: NullableSimpleUser?,
  public val labels: List<Labels>,
  @SerialName("sub_issues_summary")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val subIssuesSummary: SubIssuesSummary? = null,
  @SerialName("issue_dependencies_summary")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val issueDependenciesSummary: IssueDependenciesSummary? = null,
  @SerialName("issue_field_values")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val issueFieldValues: List<IssueFieldValue>? = null,
  public val state: String,
  @SerialName("state_reason")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val stateReason: Optional<String> = Optional.Absent,
  public val assignee: NullableSimpleUser?,
  public val milestone: NullableMilestone?,
  public val comments: Long,
  @SerialName("created_at")
  public val createdAt: Instant,
  @SerialName("updated_at")
  public val updatedAt: Instant,
  @SerialName("closed_at")
  public val closedAt: Instant?,
  @SerialName("text_matches")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val textMatches: SearchResultTextMatches? = null,
  @SerialName("pull_request")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val pullRequest: PullRequest? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val body: String? = null,
  public val score: Double,
  @SerialName("author_association")
  public val authorAssociation: AuthorAssociation,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val draft: Boolean? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val repository: Repository? = null,
  @SerialName("body_html")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val bodyHtml: String? = null,
  @SerialName("body_text")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val bodyText: String? = null,
  @SerialName("timeline_url")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val timelineUrl: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val type: Optional<IssueType> = Optional.Absent,
  @SerialName("performed_via_github_app")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val performedViaGithubApp: Optional<NullableIntegration> = Optional.Absent,
  @SerialName("pinned_comment")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val pinnedComment: Optional<NullableIssueComment> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val reactions: ReactionRollup? = null,
) {
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Labels(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val id: Long? = null,
    @SerialName("node_id")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val nodeId: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val url: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val color: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val default: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val description: Optional<String> = Optional.Absent,
  )

  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class PullRequest(
    @SerialName("merged_at")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val mergedAt: Optional<Instant> = Optional.Absent,
    @SerialName("diff_url")
    public val diffUrl: String?,
    @SerialName("html_url")
    public val htmlUrl: String?,
    @SerialName("patch_url")
    public val patchUrl: String?,
    public val url: String?,
  )
}
