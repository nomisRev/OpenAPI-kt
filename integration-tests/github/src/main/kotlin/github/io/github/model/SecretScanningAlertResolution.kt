package io.github.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class SecretScanningAlertResolution(
  public val `value`: String,
) {
  @SerialName("false_positive")
  FalsePositive("false_positive"),
  @SerialName("wont_fix")
  WontFix("wont_fix"),
  @SerialName("revoked")
  Revoked("revoked"),
  @SerialName("used_in_tests")
  UsedInTests("used_in_tests"),
  ;
}
