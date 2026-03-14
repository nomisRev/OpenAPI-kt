package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class PullRequestSimple(
    val url: String,
    val id: Long,
    @SerialName("node_id") val nodeId: String,
    @SerialName("html_url") val htmlUrl: String,
    @SerialName("diff_url") val diffUrl: String,
    @SerialName("patch_url") val patchUrl: String,
    @SerialName("issue_url") val issueUrl: String,
    @SerialName("commits_url") val commitsUrl: String,
    @SerialName("review_comments_url") val reviewCommentsUrl: String,
    @SerialName("review_comment_url") val reviewCommentUrl: String,
    @SerialName("comments_url") val commentsUrl: String,
    @SerialName("statuses_url") val statusesUrl: String,
    val number: Long,
    val state: String,
    val locked: Boolean,
    val title: String,
    val user: NullableSimpleUser?,
    val body: String?,
    val labels: List<Labels>,
    val milestone: NullableMilestone?,
    @SerialName("active_lock_reason") val activeLockReason: String? = null,
    @SerialName("created_at") val createdAt: LocalDateTime,
    @SerialName("updated_at") val updatedAt: LocalDateTime,
    @SerialName("closed_at") val closedAt: LocalDateTime?,
    @SerialName("merged_at") val mergedAt: LocalDateTime?,
    @SerialName("merge_commit_sha") val mergeCommitSha: String?,
    val assignee: NullableSimpleUser?,
    val assignees: List<SimpleUser>? = null,
    @SerialName("requested_reviewers") val requestedReviewers: List<SimpleUser>? = null,
    @SerialName("requested_teams") val requestedTeams: List<Team>? = null,
    val head: Head,
    val base: Base,
    @SerialName("_links") val links: Links,
    @SerialName("author_association") val authorAssociation: AuthorAssociation,
    @SerialName("auto_merge") val autoMerge: AutoMerge?,
    val draft: Boolean? = null,
) {
    @Serializable
    data class Labels(
        val id: Long,
        @SerialName("node_id") val nodeId: String,
        val url: String,
        val name: String,
        val description: String,
        val color: String,
        val default: Boolean,
    )

    @Serializable
    data class Head(
        val label: String,
        val ref: String,
        val repo: Repository,
        val sha: String,
        val user: NullableSimpleUser?,
    )

    @Serializable
    data class Base(
        val label: String,
        val ref: String,
        val repo: Repository,
        val sha: String,
        val user: NullableSimpleUser?,
    )

    @Serializable
    data class Links(
        val comments: Link,
        val commits: Link,
        val statuses: Link,
        val html: Link,
        val issue: Link,
        @SerialName("review_comments") val reviewComments: Link,
        @SerialName("review_comment") val reviewComment: Link,
        val self: Link,
    )
}
