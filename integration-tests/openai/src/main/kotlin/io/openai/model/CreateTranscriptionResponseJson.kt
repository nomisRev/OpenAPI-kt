package io.openai.model

import kotlin.Double
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * Represents a transcription response returned by model, based on the provided input.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class CreateTranscriptionResponseJson(
  public val text: String,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val logprobs: List<Logprobs>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val usage: Usage? = null,
) {
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Logprobs(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val token: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val logprob: Double? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
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
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class Tokens(
      @SerialName("input_tokens")
      public val inputTokens: Long,
      @SerialName("input_token_details")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val inputTokenDetails: InputTokenDetails? = null,
      @SerialName("output_tokens")
      public val outputTokens: Long,
      @SerialName("total_tokens")
      public val totalTokens: Long,
    ) : Usage {
      /**
       * Details about the input tokens billed for this request.
       */
      @OptIn(ExperimentalSerializationApi::class)
      @Serializable
      public data class InputTokenDetails(
        @SerialName("text_tokens")
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val textTokens: Long? = null,
        @SerialName("audio_tokens")
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val audioTokens: Long? = null,
      )
    }

    /**
     * Usage statistics for models billed by audio input duration.
     */
    @SerialName("duration")
    @Serializable
    public data class Duration(
      public val seconds: Double,
    ) : Usage
  }
}
