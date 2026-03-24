package io.openai.model

import kotlin.String
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A summary text from the model.
 */
@Serializable
public data class SummaryTextContent(
  @Required
  public val type: Type = Type.SummaryText,
  public val text: String,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("summary_text")
    SummaryText("summary_text"),
    ;
  }
}
