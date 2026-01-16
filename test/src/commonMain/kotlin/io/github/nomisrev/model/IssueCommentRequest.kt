package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class IssueCommentRequest(
    val attachments: List<IssueAttachmentRequest>? = null,
    val author: UserRequest? = null,
    val deleted: Boolean? = null,
    val issue: IssueRequest? = null,
    val pinned: Boolean? = null,
    val reactions: List<ReactionRequest>? = null,
    val text: String? = null,
    val visibility: VisibilityRequest? = null,
)
