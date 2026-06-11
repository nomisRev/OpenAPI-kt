package io.openai.model

import kotlin.Long
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Usage statistics for the completion request.
 */
@Serializable
public data class CompletionUsage(
  @SerialName("completion_tokens")
  @Required
  public val completionTokens: Long = 0L,
  @SerialName("prompt_tokens")
  @Required
  public val promptTokens: Long = 0L,
  @SerialName("total_tokens")
  @Required
  public val totalTokens: Long = 0L,
  @SerialName("completion_tokens_details")
  public val completionTokensDetails: CompletionTokensDetails? = null,
  @SerialName("prompt_tokens_details")
  public val promptTokensDetails: PromptTokensDetails? = null,
) {
  /**
   * Breakdown of tokens used in a completion.
   */
  @Serializable
  public data class CompletionTokensDetails(
    @SerialName("accepted_prediction_tokens")
    public val acceptedPredictionTokens: Long? = null,
    @SerialName("audio_tokens")
    public val audioTokens: Long? = null,
    @SerialName("reasoning_tokens")
    public val reasoningTokens: Long? = null,
    @SerialName("rejected_prediction_tokens")
    public val rejectedPredictionTokens: Long? = null,
  )

  /**
   * Breakdown of tokens used in the prompt.
   */
  @Serializable
  public data class PromptTokensDetails(
    @SerialName("audio_tokens")
    public val audioTokens: Long? = null,
    @SerialName("cached_tokens")
    public val cachedTokens: Long? = null,
  )
}
