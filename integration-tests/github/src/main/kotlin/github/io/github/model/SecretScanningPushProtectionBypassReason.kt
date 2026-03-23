package io.github.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class SecretScanningPushProtectionBypassReason(
  public val `value`: String,
) {
  @SerialName("false_positive")
  FalsePositive("false_positive"),
  @SerialName("used_in_tests")
  UsedInTests("used_in_tests"),
  @SerialName("will_fix_later")
  WillFixLater("will_fix_later"),
  ;
}
