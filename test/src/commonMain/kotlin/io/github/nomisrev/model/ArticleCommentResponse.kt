package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ArticleCommentResponse(
    val id: String? = null,
    val article: Article? = null,
    val attachments: List<ArticleAttachmentResponse>? = null,
    val author: UserResponse? = null,
    val created: Long? = null,
    val pinned: Boolean? = null,
    val reactions: List<ReactionResponse>? = null,
    val text: String? = null,
    val updated: Long? = null,
    val visibility: VisibilityResponse? = null,
    @SerialName($$"$type") val type: String? = null,
)
