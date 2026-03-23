package io.github.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class CodeScanningAutofix(
  public val status: CodeScanningAutofixStatus,
  public val description: CodeScanningAutofixDescription?,
  @SerialName("started_at")
  public val startedAt: CodeScanningAutofixStartedAt,
)
