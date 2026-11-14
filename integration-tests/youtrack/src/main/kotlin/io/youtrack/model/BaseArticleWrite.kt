package io.youtrack.model

import kotlin.Boolean
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
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

  @SerialName("BaseArticle")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Default(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val attachments: List<ArticleAttachmentWrite>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val content: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val reporter: UserWrite? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val summary: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val visibility: VisibilityWrite? = null,
  ) : BaseArticleWrite

  @SerialName("Article")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Article(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val attachments: List<ArticleAttachmentWrite>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val content: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val reporter: UserWrite? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val summary: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val visibility: VisibilityWrite? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val childArticles: List<Article>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val comments: List<ArticleCommentWrite>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val hasStar: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val parentArticle: Article? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val tags: List<IssueFolderWrite.Tag>? = null,
  ) : BaseArticleWrite
}
