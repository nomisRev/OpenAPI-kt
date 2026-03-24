package io.openai.model

import kotlin.Double
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A Realtime transcription session configuration object.
 *
 */
@Serializable
public data class RealtimeTranscriptionSessionCreateResponseGA(
  public val type: Type,
  public val id: String,
  public val `object`: String,
  @SerialName("expires_at")
  public val expiresAt: Long? = null,
  public val include: List<Include>? = null,
  public val audio: Audio? = null,
) {
  /**
   * Configuration for input audio for the session.
   *
   */
  @JvmInline
  @Serializable
  public value class Audio(
    public val input: Input? = null,
  ) {
    @Serializable
    public data class Input(
      public val format: RealtimeAudioFormats? = null,
      public val transcription: AudioTranscription? = null,
      @SerialName("noise_reduction")
      public val noiseReduction: NoiseReduction? = null,
      @SerialName("turn_detection")
      public val turnDetection: TurnDetection? = null,
    ) {
      /**
       * Configuration for input audio noise reduction.
       *
       */
      @JvmInline
      @Serializable
      public value class NoiseReduction(
        public val type: NoiseReductionType? = null,
      )

      /**
       * Configuration for turn detection. Can be set to `null` to turn off. Server
       * VAD means that the model will detect the start and end of speech based on
       * audio volume and respond at the end of user speech.
       *
       */
      @Serializable
      public data class TurnDetection(
        public val type: String? = null,
        public val threshold: Double? = null,
        @SerialName("prefix_padding_ms")
        public val prefixPaddingMs: Long? = null,
        @SerialName("silence_duration_ms")
        public val silenceDurationMs: Long? = null,
      )
    }
  }

  @Serializable
  public enum class Include(
    public val `value`: String,
  ) {
    @SerialName("item.input_audio_transcription.logprobs")
    ItemInputAudioTranscriptionLogprobs("item.input_audio_transcription.logprobs"),
    ;
  }

  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("transcription")
    Transcription("transcription"),
    ;
  }
}
