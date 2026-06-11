package io.openai.model

import kotlin.Double
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Realtime transcription session object configuration.
 */
@Serializable
public data class RealtimeTranscriptionSessionCreateRequest(
  @SerialName("turn_detection")
  public val turnDetection: TurnDetection? = null,
  @SerialName("input_audio_noise_reduction")
  public val inputAudioNoiseReduction: InputAudioNoiseReduction? = null,
  @SerialName("input_audio_format")
  public val inputAudioFormat: InputAudioFormat? = null,
  @SerialName("input_audio_transcription")
  public val inputAudioTranscription: AudioTranscription? = null,
  public val include: List<Include>? = null,
) {
  @Serializable
  public enum class Include(
    public val `value`: String,
  ) {
    @SerialName("item.input_audio_transcription.logprobs")
    ItemInputAudioTranscriptionLogprobs("item.input_audio_transcription.logprobs"),
    ;
  }

  @Serializable
  public enum class InputAudioFormat(
    public val `value`: String,
  ) {
    @SerialName("pcm16")
    Pcm16("pcm16"),
    @SerialName("g711_ulaw")
    G711Ulaw("g711_ulaw"),
    @SerialName("g711_alaw")
    G711Alaw("g711_alaw"),
    ;
  }

  /**
   * Configuration for input audio noise reduction. This can be set to `null` to turn off.
   * Noise reduction filters audio added to the input audio buffer before it is sent to VAD and the model.
   * Filtering the audio can improve VAD and turn detection accuracy (reducing false positives) and model performance by improving perception of the input audio.
   *
   */
  @JvmInline
  @Serializable
  public value class InputAudioNoiseReduction(
    public val type: NoiseReductionType? = null,
  )

  /**
   * Configuration for turn detection. Can be set to `null` to turn off. Server VAD means that the model will detect the start and end of speech based on audio volume and respond at the end of user speech.
   *
   */
  @Serializable
  public data class TurnDetection(
    public val type: Type? = null,
    public val threshold: Double? = null,
    @SerialName("prefix_padding_ms")
    public val prefixPaddingMs: Long? = null,
    @SerialName("silence_duration_ms")
    public val silenceDurationMs: Long? = null,
  ) {
    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("server_vad")
      ServerVad("server_vad"),
      ;
    }
  }
}
