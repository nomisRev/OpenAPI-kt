package io.github.model

import kotlin.jvm.JvmInline
import kotlin.time.Instant
import kotlinx.serialization.Serializable

/**
 * The start time of an autofix in ISO 8601 format: `YYYY-MM-DDTHH:MM:SSZ`.
 */
@JvmInline
@Serializable
public value class CodeScanningAutofixStartedAt(
  public val `value`: Instant,
)
