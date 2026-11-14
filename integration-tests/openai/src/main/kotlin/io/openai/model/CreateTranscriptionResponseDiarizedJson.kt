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
 * Represents a diarized transcription response returned by the model, including the combined transcript and speaker-segment annotations.
 *
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class CreateTranscriptionResponseDiarizedJson(
  public val task: Task,
  public val duration: Double,
  public val text: String,
  public val segments: List<TranscriptionDiarizedSegment>,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
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
