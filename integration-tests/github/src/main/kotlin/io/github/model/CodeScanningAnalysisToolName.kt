package io.github.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * The name of the tool used to generate the code scanning analysis.
 */
@JvmInline
@Serializable
public value class CodeScanningAnalysisToolName(
  public val `value`: String,
)
