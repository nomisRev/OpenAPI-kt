package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * An audio input to the model.
 *
 */
@Serializable
public data class InputAudio(
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
      @SerialName("mp3")
      Mp3("mp3"),
      @SerialName("wav")
      Wav("wav"),
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
