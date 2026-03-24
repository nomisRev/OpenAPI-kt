package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Emitted when the response is in progress.
 */
@Serializable
public data class ResponseInProgressEvent(
  public val type: Type,
  public val response: Response,
  @SerialName("sequence_number")
  public val sequenceNumber: Long,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("response.in_progress")
    ResponseInProgress("response.in_progress"),
    ;
  }
}
