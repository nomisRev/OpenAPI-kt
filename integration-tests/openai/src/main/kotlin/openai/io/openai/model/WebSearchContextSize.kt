package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class WebSearchContextSize(
  public val `value`: String,
) {
  @SerialName("low")
  Low("low"),
  @SerialName("medium")
  Medium("medium"),
  @SerialName("high")
  High("high"),
  ;
}
