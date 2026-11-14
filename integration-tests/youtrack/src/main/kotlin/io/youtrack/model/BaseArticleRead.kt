package io.youtrack.model

import kotlin.Boolean
import kotlin.Long
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
public sealed interface BaseArticleRead {
  public val id: String?

  public val attachments: List<ArticleAttachmentRead>?

  public val content: String?

  public val reporter: UserRead?

  public val summary: String?

  public val visibility: VisibilityRead?

  @SerialName("BaseArticle")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Default(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val attachments: List<ArticleAttachmentRead>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val content: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val reporter: UserRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val summary: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val visibility: VisibilityRead? = null,
  ) : BaseArticleRead

  @SerialName("Article")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Article(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val attachments: List<ArticleAttachmentRead>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val content: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val reporter: UserRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val summary: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val visibility: VisibilityRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val childArticles: List<Article>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val comments: List<ArticleCommentRead>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val created: Long? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val externalArticle: ExternalArticle? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val hasChildren: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val hasStar: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val idReadable: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val ordinal: Long? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val parentArticle: Article? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val pinnedComments: List<ArticleCommentRead>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val project: IssueFolderRead.Project? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val tags: List<IssueFolderRead.Tag>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val updated: Long? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val updatedBy: UserRead? = null,
  ) : BaseArticleRead
}
