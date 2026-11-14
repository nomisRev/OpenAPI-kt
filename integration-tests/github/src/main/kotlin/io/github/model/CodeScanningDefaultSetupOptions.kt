package io.github.model

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Feature options for code scanning default setup
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class CodeScanningDefaultSetupOptions(
  @SerialName("runner_type")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val runnerType: RunnerType? = null,
  @SerialName("runner_label")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val runnerLabel: Optional<String> = Optional.Absent,
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
