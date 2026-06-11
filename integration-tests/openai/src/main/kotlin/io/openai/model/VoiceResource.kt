package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A custom voice that can be used for audio output.
 */
@Serializable
public data class VoiceResource(
  public val `object`: Object,
  public val id: String,
  public val name: String,
  @SerialName("created_at")
  public val createdAt: Long,
) {
  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("audio.voice")
    AudioVoice("audio.voice"),
    ;
  }
}
