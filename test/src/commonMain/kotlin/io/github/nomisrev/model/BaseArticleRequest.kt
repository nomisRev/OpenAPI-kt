package io.github.nomisrev.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator($$"$type")
@Serializable
sealed interface BaseArticleRequest {
    val attachments: List<ArticleAttachmentRequest>?
    val content: String?
    val reporter: UserRequest?
    val summary: String?
    val visibility: VisibilityRequest?

    @SerialName("Default")
    @Serializable
    data class Default(
        override val attachments: List<ArticleAttachmentRequest>? = null,
        override val content: String? = null,
        override val reporter: UserRequest? = null,
        override val summary: String? = null,
        override val visibility: VisibilityRequest? = null,
    ) : BaseArticleRequest

    @SerialName("Article")
    @Serializable
    data class Article(
        override val attachments: List<ArticleAttachmentRequest>? = null,
        override val content: String? = null,
        override val reporter: UserRequest? = null,
        override val summary: String? = null,
        override val visibility: VisibilityRequest? = null,
        val childArticles: List<Article>? = null,
        val comments: List<ArticleCommentRequest>? = null,
        val externalArticle: ExternalArticleRequest? = null,
        val hasStar: Boolean? = null,
        val parentArticle: Article? = null,
        val project: Project? = null,
        val tags: List<Tag>? = null,
        val updatedBy: UserRequest? = null,
    ) : BaseArticleRequest
}
