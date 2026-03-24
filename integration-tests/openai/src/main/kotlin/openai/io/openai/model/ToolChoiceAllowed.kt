package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray

/**
 * Constrains the tools available to the model to a pre-defined set.
 *
 */
@Serializable
public data class ToolChoiceAllowed(
  public val type: Type,
  public val mode: Mode,
  public val tools: JsonArray,
) {
  @Serializable
  public enum class Mode(
    public val `value`: String,
  ) {
    @SerialName("auto")
    Auto("auto"),
    @SerialName("required")
    Required("required"),
    ;
  }

  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("allowed_tools")
    AllowedTools("allowed_tools"),
    ;
  }
}
