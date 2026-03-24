package io.openai.model

import kotlin.String
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A text content.
 */
@Serializable
public data class TextContent(
  @Required
  public val type: Type = Type.Text,
  public val text: String,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("text")
    Text("text"),
    ;
  }
}
