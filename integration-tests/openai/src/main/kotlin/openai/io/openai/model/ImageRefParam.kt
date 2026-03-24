package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Reference an input image by either URL or uploaded file ID.
 * Provide exactly one of `image_url` or `file_id`.
 *
 */
@Serializable
public data class ImageRefParam(
  @SerialName("image_url")
  public val imageUrl: String? = null,
  @SerialName("file_id")
  public val fileId: String? = null,
)
