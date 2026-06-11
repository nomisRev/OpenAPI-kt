package io.github.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * The dismissal comment associated with the dismissal of the alert.
 */
@JvmInline
@Serializable
public value class CodeScanningAlertDismissedComment(
  public val `value`: String,
)
