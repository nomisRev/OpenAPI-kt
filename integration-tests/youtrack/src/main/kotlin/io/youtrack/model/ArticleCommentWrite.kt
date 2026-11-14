package io.youtrack.model

import kotlin.Boolean
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class ArticleCommentWrite(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val attachments: List<ArticleAttachmentWrite>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val pinned: Boolean? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val reactions: List<ReactionWrite>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val text: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val visibility: VisibilityWrite? = null,
)
