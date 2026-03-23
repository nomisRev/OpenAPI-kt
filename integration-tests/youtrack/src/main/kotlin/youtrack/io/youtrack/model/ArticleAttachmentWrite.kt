package io.youtrack.model

import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class ArticleAttachmentWrite(
  public val name: String? = null,
  public val base64Content: String? = null,
  public val visibility: VisibilityWrite? = null,
)
