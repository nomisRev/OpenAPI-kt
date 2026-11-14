package io.youtrack.model

import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class IssueAttachmentRead(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val name: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val author: UserRead? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val created: Long? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val updated: Long? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val size: Long? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val extension: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val charset: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val mimeType: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val metaData: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val draft: Boolean? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val removed: Boolean? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val base64Content: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val url: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val visibility: VisibilityRead? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val issue: IssueRead? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val comment: IssueCommentRead? = null,
  @SerialName("thumbnailURL")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val thumbnailUrl: String? = null,
  @SerialName("${'$'}type")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val type: String? = null,
)
