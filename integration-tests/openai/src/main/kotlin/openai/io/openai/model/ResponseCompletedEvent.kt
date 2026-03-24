package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Emitted when the model response is complete.
 */
@Serializable
public data class ResponseCompletedEvent(
  public val type: Type,
  public val response: Response,
  @SerialName("sequence_number")
  public val sequenceNumber: Long,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("response.completed")
    ResponseCompleted("response.completed"),
    ;
  }
}
