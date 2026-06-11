package io.github.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Commit an autofix for a code scanning alert
 */
@Serializable
public data class CodeScanningAutofixCommits(
  @SerialName("target_ref")
  public val targetRef: String? = null,
  public val message: String? = null,
)
