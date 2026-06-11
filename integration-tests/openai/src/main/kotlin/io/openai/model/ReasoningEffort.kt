package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class ReasoningEffort(
  public val `value`: String,
) {
  @SerialName("none")
  None("none"),
  @SerialName("minimal")
  Minimal("minimal"),
  @SerialName("low")
  Low("low"),
  @SerialName("medium")
  Medium("medium"),
  @SerialName("high")
  High("high"),
  @SerialName("xhigh")
  Xhigh("xhigh"),
  ;
}
