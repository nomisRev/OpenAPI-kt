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
 * Represents a diarized transcription response returned by the model, including the combined transcript and speaker-segment annotations.
 *
 */
@Serializable
public data class CreateTranscriptionResponseDiarizedJson(
  public val task: Task,
  public val duration: Double,
  public val text: String,
  public val segments: List<TranscriptionDiarizedSegment>,
  public val usage: Usage? = null,
) {
  @Serializable
  public enum class Task(
    public val `value`: String,
  ) {
    @SerialName("transcribe")
    Transcribe("transcribe"),
    ;
  }

  /**
   * Token or duration usage statistics for the request.
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
