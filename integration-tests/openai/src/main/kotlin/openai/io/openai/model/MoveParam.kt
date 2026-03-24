package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A mouse move action.
 */
@Serializable
public data class MoveParam(
  @Required
  public val type: Type = Type.Move,
  public val x: Long,
  public val y: Long,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("move")
    Move("move"),
    ;
  }
}
