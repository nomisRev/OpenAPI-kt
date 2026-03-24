package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * An event that is emitted when a response is created.
 *
 */
@Serializable
public data class ResponseCreatedEvent(
  public val type: Type,
  public val response: Response,
  @SerialName("sequence_number")
  public val sequenceNumber: Long,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("response.created")
    ResponseCreated("response.created"),
    ;
  }
}
