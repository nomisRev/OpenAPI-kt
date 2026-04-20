package io.github.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * The media type used to serialize the payloads. Supported values include `json` and `form`. The default is `form`.
 */
@JvmInline
@Serializable
public value class WebhookConfigContentTypeWrite(
  public val `value`: String,
)
