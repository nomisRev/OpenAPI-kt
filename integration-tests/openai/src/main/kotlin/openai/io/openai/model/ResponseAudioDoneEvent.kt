package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Emitted when the audio response is complete.
 */
@Serializable
public data class ResponseAudioDoneEvent(
  public val type: Type,
  @SerialName("sequence_number")
  public val sequenceNumber: Long,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("response.audio.done")
    ResponseAudioDone("response.audio.done"),
    ;
  }
}
