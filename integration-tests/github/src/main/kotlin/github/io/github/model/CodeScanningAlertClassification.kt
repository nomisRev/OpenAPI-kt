package io.github.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class CodeScanningAlertClassification(
  public val `value`: String,
) {
  @SerialName("source")
  Source("source"),
  @SerialName("generated")
  Generated("generated"),
  @SerialName("test")
  Test("test"),
  @SerialName("library")
  Library("library"),
  ;
}
