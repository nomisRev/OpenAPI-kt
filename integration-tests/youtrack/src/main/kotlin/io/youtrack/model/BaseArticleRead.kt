package io.youtrack.model

import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement

/**
 * Represents a base article entity.
 */
@Serializable(with = BaseArticleRead.Serializer::class)
public sealed interface BaseArticleRead {
  public val id: String?

  public val attachments: List<ArticleAttachmentRead>?

  public val content: String?

  public val reporter: UserRead?

  public val summary: String?

  public val visibility: VisibilityRead?

  @SerialName("BaseArticle")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = Default.Serializer::class)
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
  ) : BaseArticleRead {
    public object Serializer : TaggedCaseSerializer<Default>(Default.generatedSerializer(), "${'$'}type", "BaseArticle")
  }

  @SerialName("Article")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = Article.Serializer::class)
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
  ) : BaseArticleRead {
    public object Serializer : TaggedCaseSerializer<Article>(Article.generatedSerializer(), "${'$'}type", "Article")
  }

  public object Serializer : JsonContentPolymorphicSerializer<BaseArticleRead>(BaseArticleRead::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<BaseArticleRead> = when (val tag = element.discriminatorValue("${'$'}type")) {
      "BaseArticle" -> Default.serializer()
      "Article" -> Article.serializer()
      else -> throw SerializationException("'${'$'}type' of BaseArticleRead selects none of BaseArticle, Article: " + tag)
    }
  }
}
