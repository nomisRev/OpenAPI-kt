package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Emitted for each chunk of audio data generated during speech synthesis.
 */
@Serializable
public data class SpeechAudioDeltaEvent(
  public val type: Type,
  public val audio: String,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("speech.audio.delta")
    SpeechAudioDelta("speech.audio.delta"),
    ;
  }
}
