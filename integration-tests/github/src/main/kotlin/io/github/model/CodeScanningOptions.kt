package io.github.model

import kotlin.Boolean
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Security Configuration feature options for code scanning
 */
@Serializable
public data class CodeScanningOptions(
  @SerialName("allow_advanced")
  public val allowAdvanced: Boolean? = null,
)
