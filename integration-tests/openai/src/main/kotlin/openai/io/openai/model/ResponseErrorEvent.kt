package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Emitted when an error occurs.
 */
@Serializable
public data class ResponseErrorEvent(
  public val type: Type,
  public val code: String?,
  public val message: String,
  public val `param`: String?,
  @SerialName("sequence_number")
  public val sequenceNumber: Long,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("error")
    Error("error"),
    ;
  }
}
