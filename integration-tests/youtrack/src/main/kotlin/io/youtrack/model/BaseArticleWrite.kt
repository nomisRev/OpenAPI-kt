package io.youtrack.model

import kotlin.Boolean
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
@Serializable(with = BaseArticleWrite.Serializer::class)
public sealed interface BaseArticleWrite {
  public val attachments: List<ArticleAttachmentWrite>?

  public val content: String?

  public val reporter: UserWrite?

  public val summary: String?

  public val visibility: VisibilityWrite?

  @SerialName("BaseArticle")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = Default.Serializer::class)
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
  ) : BaseArticleWrite {
    public object Serializer : TaggedCaseSerializer<Default>(Default.generatedSerializer(), "${'$'}type", "BaseArticle")
  }

  @SerialName("Article")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = Article.Serializer::class)
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
  ) : BaseArticleWrite {
    public object Serializer : TaggedCaseSerializer<Article>(Article.generatedSerializer(), "${'$'}type", "Article")
  }

  public object Serializer : JsonContentPolymorphicSerializer<BaseArticleWrite>(BaseArticleWrite::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<BaseArticleWrite> = when (val tag = element.discriminatorValue("${'$'}type")) {
      "BaseArticle" -> Default.serializer()
      "Article" -> Article.serializer()
      else -> throw SerializationException("'${'$'}type' of BaseArticleWrite selects none of BaseArticle, Article: " + tag)
    }
  }
}
