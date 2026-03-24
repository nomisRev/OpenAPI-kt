package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class ComputerEnvironment(
  public val `value`: String,
) {
  @SerialName("windows")
  Windows("windows"),
  @SerialName("mac")
  Mac("mac"),
  @SerialName("linux")
  Linux("linux"),
  @SerialName("ubuntu")
  Ubuntu("ubuntu"),
  @SerialName("browser")
  Browser("browser"),
  ;
}
