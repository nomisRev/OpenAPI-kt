package io.openai.model

import kotlin.Long
import kotlin.OptIn
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Usage statistics for the completion request.
 */
@OptIn(ExperimentalSerializationApi::class)
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
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val completionTokensDetails: CompletionTokensDetails? = null,
  @SerialName("prompt_tokens_details")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val promptTokensDetails: PromptTokensDetails? = null,
) {
  /**
   * Breakdown of tokens used in a completion.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class CompletionTokensDetails(
    @SerialName("accepted_prediction_tokens")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val acceptedPredictionTokens: Long? = null,
    @SerialName("audio_tokens")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val audioTokens: Long? = null,
    @SerialName("reasoning_tokens")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val reasoningTokens: Long? = null,
    @SerialName("rejected_prediction_tokens")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val rejectedPredictionTokens: Long? = null,
  )

  /**
   * Breakdown of tokens used in the prompt.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class PromptTokensDetails(
    @SerialName("audio_tokens")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val audioTokens: Long? = null,
    @SerialName("cached_tokens")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val cachedTokens: Long? = null,
  )
}
