package io.youtrack.model

import kotlin.Boolean
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
public sealed interface BaseArticleWrite {
  public val attachments: List<ArticleAttachmentWrite>?

  public val content: String?

  public val reporter: UserWrite?

  public val summary: String?

  public val visibility: VisibilityWrite?

  @SerialName("Default")
  @Serializable
  public data class Default(
    override val attachments: List<ArticleAttachmentWrite>? = null,
    override val content: String? = null,
    override val reporter: UserWrite? = null,
    override val summary: String? = null,
    override val visibility: VisibilityWrite? = null,
  ) : BaseArticleWrite

  @SerialName("Article")
  @Serializable
  public data class Article(
    override val attachments: List<ArticleAttachmentWrite>? = null,
    override val content: String? = null,
    override val reporter: UserWrite? = null,
    override val summary: String? = null,
    override val visibility: VisibilityWrite? = null,
    public val childArticles: List<Article>? = null,
    public val comments: List<ArticleCommentWrite>? = null,
    public val hasStar: Boolean? = null,
    public val parentArticle: Article? = null,
    public val tags: List<IssueFolderWrite.Tag>? = null,
  ) : BaseArticleWrite
}
