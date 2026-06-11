package io.github.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * The REST API URL of the analysis resource.
 */
@JvmInline
@Serializable
public value class CodeScanningAnalysisUrl(
  public val `value`: String,
)
