package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A text output from the model.
 *
 */
@Serializable
public data class EvalItemContentOutputText(
  public val type: Type,
  public val text: String,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("output_text")
    OutputText("output_text"),
    ;
  }
}
