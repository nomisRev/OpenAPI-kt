package io.github.model

import kotlin.Boolean
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * If `true`, attempt to create an alert dismissal request.
 */
@JvmInline
@Serializable
public value class CodeScanningAlertCreateRequest(
  public val `value`: Boolean,
)
