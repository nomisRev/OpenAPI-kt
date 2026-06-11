package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class ToolChoiceOptions(
  public val `value`: String,
) {
  @SerialName("none")
  None("none"),
  @SerialName("auto")
  Auto("auto"),
  @SerialName("required")
  Required("required"),
  ;
}
