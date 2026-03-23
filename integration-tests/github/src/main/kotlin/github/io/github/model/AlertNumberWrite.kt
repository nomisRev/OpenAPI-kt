package io.github.model

import kotlin.Long
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * The security alert number.
 */
@JvmInline
@Serializable
public value class AlertNumberWrite(
  public val `value`: Long,
)
