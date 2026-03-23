package io.github.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * Identifies the variable values associated with the environment in which this analysis was performed.
 */
@JvmInline
@Serializable
public value class CodeScanningAnalysisEnvironment(
  public val `value`: String,
)
