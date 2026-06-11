package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Use this option to force the model to call a specific custom tool.
 *
 */
@Serializable
public data class ToolChoiceCustom(
  public val type: Type,
  public val name: String,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("custom")
    Custom("custom"),
    ;
  }
}
