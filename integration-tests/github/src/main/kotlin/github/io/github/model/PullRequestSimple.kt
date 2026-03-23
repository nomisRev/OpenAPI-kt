package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Pull Request Simple
 */
@Serializable
public data class PullRequestSimple(
  public val url: String,
  public val id: Long,
  @SerialName("node_id")
  public val nodeId: String,
  @SerialName("html_url")
  public val htmlUrl: String,
  @SerialName("diff_url")
  public val diffUrl: String,
  @SerialName("patch_url")
  public val patchUrl: String,
  @SerialName("issue_url")
  public val issueUrl: String,
  @SerialName("commits_url")
  public val commitsUrl: String,
  @SerialName("review_comments_url")
  public val reviewCommentsUrl: String,
  @SerialName("review_comment_url")
  public val reviewCommentUrl: String,
  @SerialName("comments_url")
  public val commentsUrl: String,
  @SerialName("statuses_url")
  public val statusesUrl: String,
  public val number: Long,
  public val state: String,
  public val locked: Boolean,
  public val title: String,
  public val user: NullableSimpleUser?,
  public val body: String?,
  public val labels: List<Labels>,
  public val milestone: NullableMilestone?,
  @SerialName("active_lock_reason")
  public val activeLockReason: String? = null,
  @SerialName("created_at")
  public val createdAt: Instant,
  @SerialName("updated_at")
  public val updatedAt: Instant,
  @SerialName("closed_at")
  public val closedAt: Instant?,
  @SerialName("merged_at")
  public val mergedAt: Instant?,
  @SerialName("merge_commit_sha")
  public val mergeCommitSha: String?,
  public val assignee: NullableSimpleUser?,
  public val assignees: List<SimpleUser>? = null,
  @SerialName("requested_reviewers")
  public val requestedReviewers: List<SimpleUser>? = null,
  @SerialName("requested_teams")
  public val requestedTeams: List<Team>? = null,
  public val head: Head,
  public val base: Base,
  @SerialName("_links")
  public val links: Links,
  @SerialName("author_association")
  public val authorAssociation: AuthorAssociation,
  @SerialName("auto_merge")
  public val autoMerge: AutoMerge?,
  public val draft: Boolean? = null,
) {
  @Serializable
  public data class Base(
    public val label: String,
    public val ref: String,
    public val repo: Repository,
    public val sha: String,
    public val user: NullableSimpleUser?,
  )

  @Serializable
  public data class Head(
    public val label: String,
    public val ref: String,
    public val repo: Repository,
    public val sha: String,
    public val user: NullableSimpleUser?,
  )

  @Serializable
  public data class Labels(
    public val id: Long,
    @SerialName("node_id")
    public val nodeId: String,
    public val url: String,
    public val name: String,
    public val description: String,
    public val color: String,
    public val default: Boolean,
  )

  @Serializable
  public data class Links(
    public val comments: Link,
    public val commits: Link,
    public val statuses: Link,
    public val html: Link,
    public val issue: Link,
    @SerialName("review_comments")
    public val reviewComments: Link,
    @SerialName("review_comment")
    public val reviewComment: Link,
    public val self: Link,
  )
}
