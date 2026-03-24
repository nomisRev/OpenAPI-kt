package io.openai.model

import kotlin.String
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Indicates that a thread has been closed.
 */
@Serializable
public data class ClosedStatus(
  @Required
  public val type: Type = Type.Closed,
  public val reason: String?,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("closed")
    Closed("closed"),
    ;
  }
}
