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
public sealed interface ImageEditStreamEvent {
  @Serializable
  @JvmInline
  @SerialName("ImageEditPartialImageEvent")
  public value class ImageEditPartialImageEvent(
    public val `value`: io.openai.model.ImageEditPartialImageEvent,
  ) : ImageEditStreamEvent

  @Serializable
  @JvmInline
  @SerialName("ImageEditCompletedEvent")
  public value class ImageEditCompletedEvent(
    public val `value`: io.openai.model.ImageEditCompletedEvent,
  ) : ImageEditStreamEvent
}
