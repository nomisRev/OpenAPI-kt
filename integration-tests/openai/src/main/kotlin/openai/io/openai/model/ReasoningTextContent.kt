package io.openai.model

import kotlin.String
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Reasoning text from the model.
 */
@Serializable
public data class ReasoningTextContent(
  @Required
  public val type: Type = Type.ReasoningText,
  public val text: String,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("reasoning_text")
    ReasoningText("reasoning_text"),
    ;
  }
}
