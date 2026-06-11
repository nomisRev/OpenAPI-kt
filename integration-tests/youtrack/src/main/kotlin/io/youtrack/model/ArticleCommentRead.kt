package io.youtrack.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ArticleCommentRead(
  public val id: String? = null,
  public val article: BaseArticleRead.Article? = null,
  public val attachments: List<ArticleAttachmentRead>? = null,
  public val author: UserRead? = null,
  public val created: Long? = null,
  public val pinned: Boolean? = null,
  public val reactions: List<ReactionRead>? = null,
  public val text: String? = null,
  public val updated: Long? = null,
  public val visibility: VisibilityRead? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
