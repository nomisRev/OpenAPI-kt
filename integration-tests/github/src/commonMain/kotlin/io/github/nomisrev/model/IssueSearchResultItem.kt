package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class IssueSearchResultItem(
    val url: String,
    @SerialName("repository_url") val repositoryUrl: String,
    @SerialName("labels_url") val labelsUrl: String,
    @SerialName("comments_url") val commentsUrl: String,
    @SerialName("events_url") val eventsUrl: String,
    @SerialName("html_url") val htmlUrl: String,
    val id: Long,
    @SerialName("node_id") val nodeId: String,
    val number: Long,
    val title: String,
    val locked: Boolean,
    @SerialName("active_lock_reason") val activeLockReason: String? = null,
    val assignees: List<SimpleUser>? = null,
    val user: NullableSimpleUser?,
    val labels: List<Labels>,
    @SerialName("sub_issues_summary") val subIssuesSummary: SubIssuesSummary? = null,
    @SerialName("issue_dependencies_summary") val issueDependenciesSummary: IssueDependenciesSummary? = null,
    @SerialName("issue_field_values") val issueFieldValues: List<IssueFieldValue>? = null,
    val state: String,
    @SerialName("state_reason") val stateReason: String? = null,
    val assignee: NullableSimpleUser?,
    val milestone: NullableMilestone?,
    val comments: Long,
    @SerialName("created_at") val createdAt: LocalDateTime,
    @SerialName("updated_at") val updatedAt: LocalDateTime,
    @SerialName("closed_at") val closedAt: LocalDateTime?,
    @SerialName("text_matches") val textMatches: SearchResultTextMatches? = null,
    @SerialName("pull_request") val pullRequest: PullRequest? = null,
    val body: String? = null,
    val score: Double,
    @SerialName("author_association") val authorAssociation: AuthorAssociation,
    val draft: Boolean? = null,
    val repository: Repository? = null,
    @SerialName("body_html") val bodyHtml: String? = null,
    @SerialName("body_text") val bodyText: String? = null,
    @SerialName("timeline_url") val timelineUrl: String? = null,
    val type: IssueType? = null,
    @SerialName("performed_via_github_app") val performedViaGithubApp: NullableIntegration? = null,
    @SerialName("pinned_comment") val pinnedComment: NullableIssueComment? = null,
    val reactions: ReactionRollup? = null,
) {
    @Serializable
    data class Labels(
        val id: Long? = null,
        @SerialName("node_id") val nodeId: String? = null,
        val url: String? = null,
        val name: String? = null,
        val color: String? = null,
        val default: Boolean? = null,
        val description: String? = null,
    )

    @Serializable
    data class PullRequest(
        @SerialName("merged_at") val mergedAt: LocalDateTime? = null,
        @SerialName("diff_url") val diffUrl: String?,
        @SerialName("html_url") val htmlUrl: String?,
        @SerialName("patch_url") val patchUrl: String?,
        val url: String?,
    )
}
