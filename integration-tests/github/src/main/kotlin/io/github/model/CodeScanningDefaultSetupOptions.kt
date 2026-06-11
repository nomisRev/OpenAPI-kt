package io.github.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Feature options for code scanning default setup
 */
@Serializable
public data class CodeScanningDefaultSetupOptions(
  @SerialName("runner_type")
  public val runnerType: RunnerType? = null,
  @SerialName("runner_label")
  public val runnerLabel: String? = null,
) {
  @Serializable
  public enum class RunnerType(
    public val `value`: String,
  ) {
    @SerialName("standard")
    Standard("standard"),
    @SerialName("labeled")
    Labeled("labeled"),
    @SerialName("not_set")
    NotSet("not_set"),
    ;
  }
}
