package io.youtrack.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ArticleAttachmentRead(
  public val id: String? = null,
  public val name: String? = null,
  public val author: UserRead? = null,
  public val created: Long? = null,
  public val updated: Long? = null,
  public val size: Long? = null,
  public val extension: String? = null,
  public val charset: String? = null,
  public val mimeType: String? = null,
  public val metaData: String? = null,
  public val draft: Boolean? = null,
  public val removed: Boolean? = null,
  public val base64Content: String? = null,
  public val url: String? = null,
  public val visibility: VisibilityRead? = null,
  public val article: BaseArticleRead? = null,
  public val comment: ArticleCommentRead? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
