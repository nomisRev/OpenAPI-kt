package io.github.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * The REST API URL for fetching the list of instances for an alert.
 */
@JvmInline
@Serializable
public value class AlertInstancesUrl(
  public val `value`: String,
)
