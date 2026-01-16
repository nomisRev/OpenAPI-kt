package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class ArticleCommentRequest(
    val article: Article? = null,
    val attachments: List<ArticleAttachmentRequest>? = null,
    val author: UserRequest? = null,
    val pinned: Boolean? = null,
    val reactions: List<ReactionRequest>? = null,
    val text: String? = null,
    val visibility: VisibilityRequest? = null,
)
