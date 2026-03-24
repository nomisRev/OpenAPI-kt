package io.openai.model

import kotlin.String
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Text block that a user contributed to the thread.
 */
@Serializable
public data class UserMessageInputText(
  @Required
  public val type: Type = Type.InputText,
  public val text: String,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("input_text")
    InputText("input_text"),
    ;
  }
}
