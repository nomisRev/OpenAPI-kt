package io.openai.model

import kotlin.OptIn
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("type")
@Serializable
public sealed interface CreateSpeechResponseStreamEvent {
  @Serializable
  @JvmInline
  @SerialName("SpeechAudioDeltaEvent")
  public value class SpeechAudioDeltaEvent(
    public val `value`: io.openai.model.SpeechAudioDeltaEvent,
  ) : CreateSpeechResponseStreamEvent

  @Serializable
  @JvmInline
  @SerialName("SpeechAudioDoneEvent")
  public value class SpeechAudioDoneEvent(
    public val `value`: io.openai.model.SpeechAudioDoneEvent,
  ) : CreateSpeechResponseStreamEvent
}
