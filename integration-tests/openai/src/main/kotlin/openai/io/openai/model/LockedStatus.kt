package io.openai.model

import kotlin.String
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Indicates that a thread is locked and cannot accept new input.
 */
@Serializable
public data class LockedStatus(
  @Required
  public val type: Type = Type.Locked,
  public val reason: String?,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("locked")
    Locked("locked"),
    ;
  }
}
