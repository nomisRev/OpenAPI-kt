package io.github.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * The REST API URL of the alert resource.
 */
@JvmInline
@Serializable
public value class AlertUrlRead(
  public val `value`: String,
)
