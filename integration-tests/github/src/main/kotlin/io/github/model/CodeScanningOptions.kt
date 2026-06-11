package io.github.model

import kotlin.Boolean
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Security Configuration feature options for code scanning
 */
@JvmInline
@Serializable
public value class CodeScanningOptions(
  @SerialName("allow_advanced")
  public val allowAdvanced: Boolean? = null,
)
