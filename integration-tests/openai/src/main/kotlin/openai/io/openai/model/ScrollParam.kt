package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A scroll action.
 */
@Serializable
public data class ScrollParam(
  @Required
  public val type: Type = Type.Scroll,
  public val x: Long,
  public val y: Long,
  @SerialName("scroll_x")
  public val scrollX: Long,
  @SerialName("scroll_y")
  public val scrollY: Long,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("scroll")
    Scroll("scroll"),
    ;
  }
}
