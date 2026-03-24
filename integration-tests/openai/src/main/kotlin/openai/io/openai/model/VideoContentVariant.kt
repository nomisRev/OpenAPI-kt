package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class VideoContentVariant(
  public val `value`: String,
) {
  @SerialName("video")
  Video("video"),
  @SerialName("thumbnail")
  Thumbnail("thumbnail"),
  @SerialName("spritesheet")
  Spritesheet("spritesheet"),
  ;
}
