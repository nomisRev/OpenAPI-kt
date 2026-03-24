package io.openai.model

import kotlin.Long
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents token usage details including input tokens, output tokens,
 * a breakdown of output tokens, and the total tokens used.
 *
 */
@Serializable
public data class ResponseUsage(
  @SerialName("input_tokens")
  public val inputTokens: Long,
  @SerialName("input_tokens_details")
  public val inputTokensDetails: InputTokensDetails,
  @SerialName("output_tokens")
  public val outputTokens: Long,
  @SerialName("output_tokens_details")
  public val outputTokensDetails: OutputTokensDetails,
  @SerialName("total_tokens")
  public val totalTokens: Long,
) {
  /**
   * A detailed breakdown of the input tokens.
   */
  @JvmInline
  @Serializable
  public value class InputTokensDetails(
    @SerialName("cached_tokens")
    public val cachedTokens: Long,
  )

  /**
   * A detailed breakdown of the output tokens.
   */
  @JvmInline
  @Serializable
  public value class OutputTokensDetails(
    @SerialName("reasoning_tokens")
    public val reasoningTokens: Long,
  )
}
