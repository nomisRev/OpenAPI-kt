package io.openai.model

import kotlin.String
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Quoted snippet that the user referenced in their message.
 */
@Serializable
public data class UserMessageQuotedText(
  @Required
  public val type: Type = Type.QuotedText,
  public val text: String,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("quoted_text")
    QuotedText("quoted_text"),
    ;
  }
}
