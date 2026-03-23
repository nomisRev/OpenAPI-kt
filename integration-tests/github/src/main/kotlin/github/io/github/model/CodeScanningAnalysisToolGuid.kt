package io.github.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * The GUID of the tool used to generate the code scanning analysis, if provided in the uploaded SARIF data.
 */
@JvmInline
@Serializable
public value class CodeScanningAnalysisToolGuid(
  public val `value`: String,
)
