package io.openai.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A text output from the model.
 */
@Serializable
public data class OutputTextContent(
  @Required
  public val type: Type = Type.OutputText,
  public val text: String,
  public val annotations: List<Annotation>,
  public val logprobs: List<LogProb>,
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
