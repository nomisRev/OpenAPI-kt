package io.openai.model

import kotlin.Long
import kotlin.OptIn
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * For `gpt-image-1` only, the token usage information for the image generation.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class ImageGenUsage(
  @SerialName("input_tokens")
  public val inputTokens: Long,
  @SerialName("total_tokens")
  public val totalTokens: Long,
  @SerialName("output_tokens")
  public val outputTokens: Long,
  @SerialName("output_tokens_details")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val outputTokensDetails: ImageGenOutputTokensDetails? = null,
  @SerialName("input_tokens_details")
  public val inputTokensDetails: ImageGenInputUsageDetails,
)
