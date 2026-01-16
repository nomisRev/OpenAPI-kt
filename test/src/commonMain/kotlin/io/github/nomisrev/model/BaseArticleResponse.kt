package io.github.nomisrev.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator($$"$type")
@Serializable
sealed interface BaseArticleResponse {
    val id: String?
    val attachments: List<ArticleAttachmentResponse>?
    val content: String?
    val reporter: UserResponse?
    val summary: String?
    val visibility: VisibilityResponse?

    @SerialName("Default")
    @Serializable
    data class Default(
        override val id: String? = null,
        override val attachments: List<ArticleAttachmentResponse>? = null,
        override val content: String? = null,
        override val reporter: UserResponse? = null,
        override val summary: String? = null,
        override val visibility: VisibilityResponse? = null,
    ) : BaseArticleResponse

    @SerialName("Article")
    @Serializable
    data class Article(
        override val id: String? = null,
        override val attachments: List<ArticleAttachmentResponse>? = null,
        override val content: String? = null,
        override val reporter: UserResponse? = null,
        override val summary: String? = null,
        override val visibility: VisibilityResponse? = null,
        val childArticles: List<Article>? = null,
        val comments: List<ArticleCommentResponse>? = null,
        val created: Long? = null,
        val externalArticle: ExternalArticleResponse? = null,
        val hasChildren: Boolean? = null,
        val hasStar: Boolean? = null,
        val idReadable: String? = null,
        val ordinal: Long? = null,
        val parentArticle: Article? = null,
        val pinnedComments: List<ArticleCommentResponse>? = null,
        val project: Project? = null,
        val tags: List<Tag>? = null,
        val updated: Long? = null,
        val updatedBy: UserResponse? = null,
    ) : BaseArticleResponse
}
