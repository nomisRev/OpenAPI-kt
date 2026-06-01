package io.youtrack.model

import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * Represents a base article entity.
 */
@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("${'$'}type")
@Serializable
public sealed interface BaseArticleRead {
  public val id: String?

  public val attachments: List<ArticleAttachmentRead>?

  public val content: String?

  public val reporter: UserRead?

  public val summary: String?

  public val visibility: VisibilityRead?

  @SerialName("BaseArticle")
  @Serializable
  public data class Default(
    override val id: String? = null,
    override val attachments: List<ArticleAttachmentRead>? = null,
    override val content: String? = null,
    override val reporter: UserRead? = null,
    override val summary: String? = null,
    override val visibility: VisibilityRead? = null,
  ) : BaseArticleRead

  @SerialName("Article")
  @Serializable
  public data class Article(
    override val id: String? = null,
    override val attachments: List<ArticleAttachmentRead>? = null,
    override val content: String? = null,
    override val reporter: UserRead? = null,
    override val summary: String? = null,
    override val visibility: VisibilityRead? = null,
    public val childArticles: List<Article>? = null,
    public val comments: List<ArticleCommentRead>? = null,
    public val created: Long? = null,
    public val externalArticle: ExternalArticle? = null,
    public val hasChildren: Boolean? = null,
    public val hasStar: Boolean? = null,
    public val idReadable: String? = null,
    public val ordinal: Long? = null,
    public val parentArticle: Article? = null,
    public val pinnedComments: List<ArticleCommentRead>? = null,
    public val project: IssueFolderRead.Project? = null,
    public val tags: List<IssueFolderRead.Tag>? = null,
    public val updated: Long? = null,
    public val updatedBy: UserRead? = null,
  ) : BaseArticleRead
}
