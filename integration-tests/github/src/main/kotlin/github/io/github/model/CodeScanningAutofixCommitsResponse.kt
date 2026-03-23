package io.github.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class CodeScanningAutofixCommitsResponse(
  @SerialName("target_ref")
  public val targetRef: String? = null,
  public val sha: String? = null,
)
