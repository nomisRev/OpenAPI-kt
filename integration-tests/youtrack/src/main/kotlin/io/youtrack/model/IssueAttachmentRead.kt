package io.youtrack.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class IssueAttachmentRead(
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
  public val issue: IssueRead? = null,
  public val comment: IssueCommentRead? = null,
  @SerialName("thumbnailURL")
  public val thumbnailUrl: String? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
