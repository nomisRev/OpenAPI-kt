package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class InputFidelity(
  public val `value`: String,
) {
  @SerialName("high")
  High("high"),
  @SerialName("low")
  Low("low"),
  ;
}
