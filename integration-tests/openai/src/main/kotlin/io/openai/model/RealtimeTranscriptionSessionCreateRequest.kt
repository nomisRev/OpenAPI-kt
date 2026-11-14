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

/**
 * Realtime transcription session object configuration.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class RealtimeTranscriptionSessionCreateRequest(
  @SerialName("turn_detection")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val turnDetection: TurnDetection? = null,
  @SerialName("input_audio_noise_reduction")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val inputAudioNoiseReduction: InputAudioNoiseReduction? = null,
  @SerialName("input_audio_format")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val inputAudioFormat: InputAudioFormat? = null,
  @SerialName("input_audio_transcription")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val inputAudioTranscription: AudioTranscription? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
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
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class InputAudioNoiseReduction(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val type: NoiseReductionType? = null,
  )

  /**
   * Configuration for turn detection. Can be set to `null` to turn off. Server VAD means that the model will detect the start and end of speech based on audio volume and respond at the end of user speech.
   *
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class TurnDetection(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val type: Type? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val threshold: Double? = null,
    @SerialName("prefix_padding_ms")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val prefixPaddingMs: Long? = null,
    @SerialName("silence_duration_ms")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
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
