package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Attachment metadata included on thread items.
 */
@Serializable
public data class Attachment(
  public val type: AttachmentType,
  public val id: String,
  public val name: String,
  @SerialName("mime_type")
  public val mimeType: String,
  @SerialName("preview_url")
  public val previewUrl: String?,
)
