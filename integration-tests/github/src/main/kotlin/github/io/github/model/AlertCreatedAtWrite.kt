package io.github.model

import kotlin.jvm.JvmInline
import kotlin.time.Instant
import kotlinx.serialization.Serializable

/**
 * The time that the alert was created in ISO 8601 format: `YYYY-MM-DDTHH:MM:SSZ`.
 */
@JvmInline
@Serializable
public value class AlertCreatedAtWrite(
  public val `value`: Instant,
)
