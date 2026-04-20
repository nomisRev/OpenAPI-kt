package io.github.model

import kotlin.jvm.JvmInline
import kotlin.time.Instant
import kotlinx.serialization.Serializable

/**
 * The time that the alert was no longer detected and was considered fixed in ISO 8601 format: `YYYY-MM-DDTHH:MM:SSZ`.
 */
@JvmInline
@Serializable
public value class AlertFixedAt(
  public val `value`: Instant,
)
