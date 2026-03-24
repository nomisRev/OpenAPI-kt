package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Emitted when there is a partial transcript of audio.
 */
@Serializable
public data class ResponseAudioTranscriptDeltaEvent(
  public val type: Type,
  public val delta: String,
  @SerialName("sequence_number")
  public val sequenceNumber: Long,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("response.audio.transcript.delta")
    ResponseAudioTranscriptDelta("response.audio.transcript.delta"),
    ;
  }
}
