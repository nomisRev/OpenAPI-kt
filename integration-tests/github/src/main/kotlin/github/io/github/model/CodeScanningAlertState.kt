package io.github.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class CodeScanningAlertState(
  public val `value`: String,
) {
  @SerialName("open")
  Open("open"),
  @SerialName("dismissed")
  Dismissed("dismissed"),
  @SerialName("fixed")
  Fixed("fixed"),
  ;
}
