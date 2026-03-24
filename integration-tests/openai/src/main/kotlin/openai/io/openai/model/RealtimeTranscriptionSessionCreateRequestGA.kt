package io.openai.model

import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Realtime transcription session object configuration.
 */
@Serializable
public data class RealtimeTranscriptionSessionCreateRequestGA(
  public val type: Type,
  public val audio: Audio? = null,
  public val include: List<Include>? = null,
) {
  /**
   * Configuration for input and output audio.
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
      public val turnDetection: RealtimeTurnDetection? = null,
    ) {
      /**
       * Configuration for input audio noise reduction. This can be set to `null` to turn off.
       * Noise reduction filters audio added to the input audio buffer before it is sent to VAD and the model.
       * Filtering the audio can improve VAD and turn detection accuracy (reducing false positives) and model performance by improving perception of the input audio.
       *
       */
      @JvmInline
      @Serializable
      public value class NoiseReduction(
        public val type: NoiseReductionType? = null,
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
