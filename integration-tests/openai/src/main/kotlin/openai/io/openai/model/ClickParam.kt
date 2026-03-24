package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A click action.
 */
@Serializable
public data class ClickParam(
  @Required
  public val type: Type = Type.Click,
  public val button: ClickButtonType,
  public val x: Long,
  public val y: Long,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("click")
    Click("click"),
    ;
  }
}
