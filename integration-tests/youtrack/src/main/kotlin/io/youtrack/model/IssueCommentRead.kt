package io.youtrack.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class IssueCommentRead(
  public val id: String? = null,
  public val attachments: List<IssueAttachmentRead>? = null,
  public val author: UserRead? = null,
  public val created: Long? = null,
  public val deleted: Boolean? = null,
  public val issue: IssueRead? = null,
  public val pinned: Boolean? = null,
  public val reactions: List<ReactionRead>? = null,
  public val text: String? = null,
  public val textPreview: String? = null,
  public val updated: Long? = null,
  public val visibility: VisibilityRead? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
