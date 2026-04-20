package io.github.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class CodeScanningAlertDismissedReasonRead(
  public val `value`: String,
) {
  @SerialName("false positive")
  FalsePositive("false positive"),
  @SerialName("won't fix")
  `Won'tFix`("won't fix"),
  @SerialName("used in tests")
  UsedInTests("used in tests"),
  ;
}
