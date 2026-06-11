package io.github.nomisrev.render.test.union.discriminated.anyof.openai.speech

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("type")
@Serializable
public sealed interface CreateSpeechResponseStreamEvent {
  @JvmInline
  @SerialName("speech.audio.delta")
  @Serializable
  public value class SpeechAudioDelta(
    public val audio: String,
  ) : CreateSpeechResponseStreamEvent

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
