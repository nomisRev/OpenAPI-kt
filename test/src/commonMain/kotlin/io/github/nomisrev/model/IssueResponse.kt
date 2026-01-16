package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class IssueResponse(
    val id: String? = null,
    val attachments: List<IssueAttachmentResponse>? = null,
    val comments: List<IssueCommentResponse>? = null,
    val commentsCount: Int? = null,
    val created: Long? = null,
    val customFields: List<IssueCustomFieldResponse>? = null,
    val description: String? = null,
    val draftOwner: UserResponse? = null,
    val externalIssue: ExternalIssueResponse? = null,
    val idReadable: String? = null,
    val isDraft: Boolean? = null,
    val links: List<IssueLinkResponse>? = null,
    val numberInProject: Long? = null,
    val parent: IssueLinkResponse? = null,
    val pinnedComments: List<IssueCommentResponse>? = null,
    val project: Project? = null,
    val reporter: UserResponse? = null,
    val resolved: Long? = null,
    val subtasks: IssueLinkResponse? = null,
    val summary: String? = null,
    val tags: List<Tag>? = null,
    val updated: Long? = null,
    val updater: UserResponse? = null,
    val visibility: VisibilityResponse? = null,
    val voters: IssueVotersResponse? = null,
    val votes: Int? = null,
    val watchers: IssueWatchersResponse? = null,
    val wikifiedDescription: String? = null,
    @SerialName($$"$type") val type: String? = null,
)
