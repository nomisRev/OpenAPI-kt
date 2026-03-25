package io.openai.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.json.jsonObject

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("type")
@Serializable
public sealed interface CreateSpeechResponseStreamEvent {
  /**
   * Emitted for each chunk of audio data generated during speech synthesis.
   */
  @JvmInline
  @SerialName("speech.audio.delta")
  @Serializable
  public value class SpeechAudioDelta(
    public val audio: String,
  ) : CreateSpeechResponseStreamEvent

  /**
   * Emitted when the speech synthesis is complete and all audio has been streamed.
   */
  @SerialName("speech.audio.done")
  @Serializable
  public data class SpeechAudioDone(
    @SerialName("input_tokens")
    public val inputTokens: Long,
    @SerialName("output_tokens")
    public val outputTokens: Long,
    @SerialName("total_tokens")
    public val totalTokens: Long,
  ) : CreateSpeechResponseStreamEvent
}
