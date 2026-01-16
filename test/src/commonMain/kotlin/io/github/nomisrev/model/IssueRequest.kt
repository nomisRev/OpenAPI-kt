package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class IssueRequest(
    val attachments: List<IssueAttachmentRequest>? = null,
    val comments: List<IssueCommentRequest>? = null,
    val description: String? = null,
    val draftOwner: UserRequest? = null,
    val externalIssue: ExternalIssueRequest? = null,
    val parent: IssueLinkRequest? = null,
    val project: Project? = null,
    val reporter: UserRequest? = null,
    val subtasks: IssueLinkRequest? = null,
    val summary: String? = null,
    val tags: List<Tag>? = null,
    val updater: UserRequest? = null,
    val visibility: VisibilityRequest? = null,
    val voters: IssueVotersRequest? = null,
    val watchers: IssueWatchersRequest? = null,
)
