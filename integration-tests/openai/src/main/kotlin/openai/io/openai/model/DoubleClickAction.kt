package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A double click action.
 */
@Serializable
public data class DoubleClickAction(
  @Required
  public val type: Type = Type.DoubleClick,
  public val x: Long,
  public val y: Long,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("double_click")
    DoubleClick("double_click"),
    ;
  }
}
