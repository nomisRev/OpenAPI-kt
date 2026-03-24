package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ImageRefParam2(
  @SerialName("image_url")
  public val imageUrl: String? = null,
  @SerialName("file_id")
  public val fileId: String? = null,
)
