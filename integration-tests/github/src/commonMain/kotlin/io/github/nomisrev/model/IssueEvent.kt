package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class IssueEvent(
    val id: Long,
    @SerialName("node_id") val nodeId: String,
    val url: String,
    val actor: NullableSimpleUser?,
    val event: String,
    @SerialName("commit_id") val commitId: String?,
    @SerialName("commit_url") val commitUrl: String?,
    @SerialName("created_at") val createdAt: LocalDateTime,
    val issue: NullableIssue? = null,
    val label: IssueEventLabel? = null,
    val assignee: NullableSimpleUser? = null,
    val assigner: NullableSimpleUser? = null,
    @SerialName("review_requester") val reviewRequester: NullableSimpleUser? = null,
    @SerialName("requested_reviewer") val requestedReviewer: NullableSimpleUser? = null,
    @SerialName("requested_team") val requestedTeam: Team? = null,
    @SerialName("dismissed_review") val dismissedReview: IssueEventDismissedReview? = null,
    val milestone: IssueEventMilestone? = null,
    @SerialName("project_card") val projectCard: IssueEventProjectCard? = null,
    val rename: IssueEventRename? = null,
    @SerialName("author_association") val authorAssociation: AuthorAssociation? = null,
    @SerialName("lock_reason") val lockReason: String? = null,
    @SerialName("performed_via_github_app") val performedViaGithubApp: NullableIntegration? = null,
)
