package io.github.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * An identifier for the upload.
 */
@JvmInline
@Serializable
public value class CodeScanningAnalysisSarifId(
  public val `value`: String,
)
