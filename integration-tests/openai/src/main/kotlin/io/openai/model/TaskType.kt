package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class TaskType(
  public val `value`: String,
) {
  @SerialName("custom")
  Custom("custom"),
  @SerialName("thought")
  Thought("thought"),
  ;
}
