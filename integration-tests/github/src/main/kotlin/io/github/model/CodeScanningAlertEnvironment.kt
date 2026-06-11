package io.github.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * Identifies the variable values associated with the environment in which the analysis that generated this alert instance was performed, such as the language that was analyzed.
 */
@JvmInline
@Serializable
public value class CodeScanningAlertEnvironment(
  public val `value`: String,
)
