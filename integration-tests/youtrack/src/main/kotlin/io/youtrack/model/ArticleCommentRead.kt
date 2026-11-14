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

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class ArticleCommentRead(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val article: BaseArticleRead.Article? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val attachments: List<ArticleAttachmentRead>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val author: UserRead? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val created: Long? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val pinned: Boolean? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val reactions: List<ReactionRead>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val text: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val updated: Long? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val visibility: VisibilityRead? = null,
  @SerialName("${'$'}type")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val type: String? = null,
)
