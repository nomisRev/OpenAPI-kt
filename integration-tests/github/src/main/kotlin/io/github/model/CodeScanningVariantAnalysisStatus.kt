package io.github.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class CodeScanningVariantAnalysisStatus(
  public val `value`: String,
) {
  @SerialName("pending")
  Pending("pending"),
  @SerialName("in_progress")
  InProgress("in_progress"),
  @SerialName("succeeded")
  Succeeded("succeeded"),
  @SerialName("failed")
  Failed("failed"),
  @SerialName("canceled")
  Canceled("canceled"),
  @SerialName("timed_out")
  TimedOut("timed_out"),
  ;
}
