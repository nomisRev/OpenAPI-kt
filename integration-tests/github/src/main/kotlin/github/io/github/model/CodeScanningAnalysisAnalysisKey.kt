package io.github.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * Identifies the configuration under which the analysis was executed. For example, in GitHub Actions this includes the workflow filename and job name.
 */
@JvmInline
@Serializable
public value class CodeScanningAnalysisAnalysisKey(
  public val `value`: String,
)
