package io.github.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * The URL to which the payloads will be delivered.
 */
@JvmInline
@Serializable
public value class WebhookConfigUrl(
  public val `value`: String,
)
