package io.github.model

import kotlin.jvm.JvmInline
import kotlin.time.Instant
import kotlinx.serialization.Serializable

/**
 * The time that the alert was dismissed in ISO 8601 format: `YYYY-MM-DDTHH:MM:SSZ`.
 */
@JvmInline
@Serializable
public value class AlertDismissedAt(
  public val `value`: Instant,
)
