package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Emitted when the full audio transcript is completed.
 */
@Serializable
public data class ResponseAudioTranscriptDoneEvent(
  public val type: Type,
  @SerialName("sequence_number")
  public val sequenceNumber: Long,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("response.audio.transcript.done")
    ResponseAudioTranscriptDone("response.audio.transcript.done"),
    ;
  }
}
