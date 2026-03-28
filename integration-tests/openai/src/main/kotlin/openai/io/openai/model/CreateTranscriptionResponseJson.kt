package io.openai.model

import kotlin.Double
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * Represents a transcription response returned by model, based on the provided input.
 */
@Serializable
public data class CreateTranscriptionResponseJson(
  public val text: String,
  public val logprobs: List<Logprobs>? = null,
  public val usage: Usage? = null,
) {
  @Serializable
  public data class Logprobs(
    public val token: String? = null,
    public val logprob: Double? = null,
    public val bytes: List<Double>? = null,
  )

  /**
   * Token usage statistics for the request.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @JsonClassDiscriminator("type")
  @Serializable
  public sealed interface Usage {
    /**
     * Usage statistics for models billed by token usage.
     */
    @SerialName("tokens")
    @Serializable
    public data class Tokens(
      @SerialName("input_tokens")
      public val inputTokens: Long,
      @SerialName("input_token_details")
      public val inputTokenDetails: InputTokenDetails? = null,
      @SerialName("output_tokens")
      public val outputTokens: Long,
      @SerialName("total_tokens")
      public val totalTokens: Long,
    ) : Usage {
      /**
       * Details about the input tokens billed for this request.
       */
      @Serializable
      public data class InputTokenDetails(
        @SerialName("text_tokens")
        public val textTokens: Long? = null,
        @SerialName("audio_tokens")
        public val audioTokens: Long? = null,
      )
    }

    /**
     * Usage statistics for models billed by audio input duration.
     */
    @JvmInline
    @SerialName("duration")
    @Serializable
    public value class Duration(
      public val seconds: Double,
    ) : Usage
  }
}
