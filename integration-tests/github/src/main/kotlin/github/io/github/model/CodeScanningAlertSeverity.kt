package io.github.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class CodeScanningAlertSeverity(
  public val `value`: String,
) {
  @SerialName("critical")
  Critical("critical"),
  @SerialName("high")
  High("high"),
  @SerialName("medium")
  Medium("medium"),
  @SerialName("low")
  Low("low"),
  @SerialName("warning")
  Warning("warning"),
  @SerialName("note")
  Note("note"),
  @SerialName("error")
  Error("error"),
  ;
}
