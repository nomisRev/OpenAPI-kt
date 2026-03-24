package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * An event that is emitted when a response fails.
 *
 */
@Serializable
public data class ResponseFailedEvent(
  public val type: Type,
  @SerialName("sequence_number")
  public val sequenceNumber: Long,
  public val response: Response,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("response.failed")
    ResponseFailed("response.failed"),
    ;
  }
}
