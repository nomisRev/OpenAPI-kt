package io.openai.model

import kotlin.Long
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The output token details for the image generation.
 */
@Serializable
public data class ImageGenOutputTokensDetails(
  @SerialName("image_tokens")
  public val imageTokens: Long,
  @SerialName("text_tokens")
  public val textTokens: Long,
)
