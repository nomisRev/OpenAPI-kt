package io.github.model

import kotlin.Boolean
import kotlin.Double
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Issue Search Result Item
 */
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
  public val activeLockReason: String? = null,
  public val assignees: List<SimpleUser>? = null,
  public val user: NullableSimpleUser?,
  public val labels: List<Labels>,
  @SerialName("sub_issues_summary")
  public val subIssuesSummary: SubIssuesSummary? = null,
  @SerialName("issue_dependencies_summary")
  public val issueDependenciesSummary: IssueDependenciesSummary? = null,
  @SerialName("issue_field_values")
  public val issueFieldValues: List<IssueFieldValue>? = null,
  public val state: String,
  @SerialName("state_reason")
  public val stateReason: String? = null,
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
  public val textMatches: SearchResultTextMatches? = null,
  @SerialName("pull_request")
  public val pullRequest: PullRequest? = null,
  public val body: String? = null,
  public val score: Double,
  @SerialName("author_association")
  public val authorAssociation: AuthorAssociation,
  public val draft: Boolean? = null,
  public val repository: Repository? = null,
  @SerialName("body_html")
  public val bodyHtml: String? = null,
  @SerialName("body_text")
  public val bodyText: String? = null,
  @SerialName("timeline_url")
  public val timelineUrl: String? = null,
  public val type: IssueType? = null,
  @SerialName("performed_via_github_app")
  public val performedViaGithubApp: NullableIntegration? = null,
  @SerialName("pinned_comment")
  public val pinnedComment: NullableIssueComment? = null,
  public val reactions: ReactionRollup? = null,
) {
  @Serializable
  public data class Labels(
    public val id: Long? = null,
    @SerialName("node_id")
    public val nodeId: String? = null,
    public val url: String? = null,
    public val name: String? = null,
    public val color: String? = null,
    public val default: Boolean? = null,
    public val description: String? = null,
  )

  @Serializable
  public data class PullRequest(
    @SerialName("merged_at")
    public val mergedAt: Instant? = null,
    @SerialName("diff_url")
    public val diffUrl: String?,
    @SerialName("html_url")
    public val htmlUrl: String?,
    @SerialName("patch_url")
    public val patchUrl: String?,
    public val url: String?,
  )
}
