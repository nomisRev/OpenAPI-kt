package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class IssueAttachmentResponse(
    val id: String? = null,
    val name: String? = null,
    val author: UserResponse? = null,
    val created: Long? = null,
    val updated: Long? = null,
    val size: Long? = null,
    val extension: String? = null,
    val charset: String? = null,
    val mimeType: String? = null,
    val metaData: String? = null,
    val draft: Boolean? = null,
    val removed: Boolean? = null,
    val base64Content: String? = null,
    val url: String? = null,
    val visibility: VisibilityResponse? = null,
    val issue: IssueResponse? = null,
    val comment: IssueCommentResponse? = null,
    val thumbnailURL: String? = null,
    @SerialName($$"$type") val type: String? = null,
)
