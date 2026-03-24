package io.openai.model

import kotlin.Long
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * For the GPT image models only, the token usage information for the image generation.
 *
 */
@Serializable
public data class ImagesUsage(
  @SerialName("total_tokens")
  public val totalTokens: Long,
  @SerialName("input_tokens")
  public val inputTokens: Long,
  @SerialName("output_tokens")
  public val outputTokens: Long,
  @SerialName("input_tokens_details")
  public val inputTokensDetails: InputTokensDetails,
) {
  /**
   * The input tokens detailed information for the image generation.
   */
  @Serializable
  public data class InputTokensDetails(
    @SerialName("text_tokens")
    public val textTokens: Long,
    @SerialName("image_tokens")
    public val imageTokens: Long,
  )
}
