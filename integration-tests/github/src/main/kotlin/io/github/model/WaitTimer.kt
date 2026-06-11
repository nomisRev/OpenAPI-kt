package io.github.model

import kotlin.Long
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * The amount of time to delay a job after the job is initially triggered. The time (in minutes) must be an integer between 0 and 43,200 (30 days).
 */
@JvmInline
@Serializable
public value class WaitTimer(
  public val `value`: Long,
)
