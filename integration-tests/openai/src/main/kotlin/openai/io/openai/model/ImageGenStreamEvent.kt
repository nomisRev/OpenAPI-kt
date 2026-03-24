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
public sealed interface ImageGenStreamEvent {
  @Serializable
  @JvmInline
  @SerialName("ImageGenPartialImageEvent")
  public value class ImageGenPartialImageEvent(
    public val `value`: io.openai.model.ImageGenPartialImageEvent,
  ) : ImageGenStreamEvent

  @Serializable
  @JvmInline
  @SerialName("ImageGenCompletedEvent")
  public value class ImageGenCompletedEvent(
    public val `value`: io.openai.model.ImageGenCompletedEvent,
  ) : ImageGenStreamEvent
}
