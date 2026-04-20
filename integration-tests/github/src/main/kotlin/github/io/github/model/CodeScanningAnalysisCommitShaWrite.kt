package io.github.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * The SHA of the commit to which the analysis you are uploading relates.
 */
@JvmInline
@Serializable
public value class CodeScanningAnalysisCommitShaWrite(
  public val `value`: String,
)
