package io.openai.model

import kotlin.Long
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The input tokens detailed information for the image generation.
 */
@Serializable
public data class ImageGenInputUsageDetails(
  @SerialName("text_tokens")
  public val textTokens: Long,
  @SerialName("image_tokens")
  public val imageTokens: Long,
)
