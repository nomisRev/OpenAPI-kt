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
public sealed interface CreateTranscriptionResponseStreamEvent {
  @Serializable
  @JvmInline
  @SerialName("TranscriptTextSegmentEvent")
  public value class TranscriptTextSegmentEvent(
    public val `value`: io.openai.model.TranscriptTextSegmentEvent,
  ) : CreateTranscriptionResponseStreamEvent

  @Serializable
  @JvmInline
  @SerialName("TranscriptTextDeltaEvent")
  public value class TranscriptTextDeltaEvent(
    public val `value`: io.openai.model.TranscriptTextDeltaEvent,
  ) : CreateTranscriptionResponseStreamEvent

  @Serializable
  @JvmInline
  @SerialName("TranscriptTextDoneEvent")
  public value class TranscriptTextDoneEvent(
    public val `value`: io.openai.model.TranscriptTextDoneEvent,
  ) : CreateTranscriptionResponseStreamEvent
}
