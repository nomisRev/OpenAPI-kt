package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Learn about [audio inputs](/docs/guides/audio).
 *
 */
@Serializable
public data class ChatCompletionRequestMessageContentPartAudio(
  public val type: Type,
  @SerialName("input_audio")
  public val inputAudio: InputAudio,
) {
  @Serializable
  public data class InputAudio(
    public val `data`: String,
    public val format: Format,
  ) {
    @Serializable
    public enum class Format(
      public val `value`: String,
    ) {
      @SerialName("wav")
      Wav("wav"),
      @SerialName("mp3")
      Mp3("mp3"),
      ;
    }
  }

  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("input_audio")
    InputAudio("input_audio"),
    ;
  }
}
