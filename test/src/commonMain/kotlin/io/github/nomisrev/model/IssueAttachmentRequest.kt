package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class IssueAttachmentRequest(
    val name: String? = null,
    val author: UserRequest? = null,
    val base64Content: String? = null,
    val visibility: VisibilityRequest? = null,
    val issue: IssueRequest? = null,
    val comment: IssueCommentRequest? = null,
)
