package io.github.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * The version of the tool used to generate the code scanning analysis.
 */
@JvmInline
@Serializable
public value class CodeScanningAnalysisToolVersion(
  public val `value`: String,
)
