package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class IssueCommentResponse(
    val id: String? = null,
    val attachments: List<IssueAttachmentResponse>? = null,
    val author: UserResponse? = null,
    val created: Long? = null,
    val deleted: Boolean? = null,
    val issue: IssueResponse? = null,
    val pinned: Boolean? = null,
    val reactions: List<ReactionResponse>? = null,
    val text: String? = null,
    val textPreview: String? = null,
    val updated: Long? = null,
    val visibility: VisibilityResponse? = null,
    @SerialName($$"$type") val type: String? = null,
)
