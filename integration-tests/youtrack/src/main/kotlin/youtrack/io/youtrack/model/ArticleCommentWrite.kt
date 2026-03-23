package io.youtrack.model

import kotlin.Boolean
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

@Serializable
public data class ArticleCommentWrite(
  public val attachments: List<ArticleAttachmentWrite>? = null,
  public val pinned: Boolean? = null,
  public val reactions: List<ReactionWrite>? = null,
  public val text: String? = null,
  public val visibility: VisibilityWrite? = null,
)
