package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Emitted when a response is queued and waiting to be processed.
 *
 */
@Serializable
public data class ResponseQueuedEvent(
  public val type: Type,
  public val response: Response,
  @SerialName("sequence_number")
  public val sequenceNumber: Long,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("response.queued")
    ResponseQueued("response.queued"),
    ;
  }
}
