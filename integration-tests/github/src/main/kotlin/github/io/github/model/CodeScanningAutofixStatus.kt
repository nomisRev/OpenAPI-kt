package io.github.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class CodeScanningAutofixStatus(
  public val `value`: String,
) {
  @SerialName("pending")
  Pending("pending"),
  @SerialName("error")
  Error("error"),
  @SerialName("success")
  Success("success"),
  @SerialName("outdated")
  Outdated("outdated"),
  ;
}
