package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class ClickButtonType(
  public val `value`: String,
) {
  @SerialName("left")
  Left("left"),
  @SerialName("right")
  Right("right"),
  @SerialName("wheel")
  Wheel("wheel"),
  @SerialName("back")
  Back("back"),
  @SerialName("forward")
  Forward("forward"),
  ;
}
