package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Emitted when there is a partial audio response.
 */
@Serializable
public data class ResponseAudioDeltaEvent(
  public val type: Type,
  @SerialName("sequence_number")
  public val sequenceNumber: Long,
  public val delta: String,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("response.audio.delta")
    ResponseAudioDelta("response.audio.delta"),
    ;
  }
}
