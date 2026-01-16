package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class ArticleAttachmentRequest(
    val name: String? = null,
    val author: UserRequest? = null,
    val base64Content: String? = null,
    val visibility: VisibilityRequest? = null,
    val article: BaseArticleRequest? = null,
    val comment: ArticleCommentRequest? = null,
)
