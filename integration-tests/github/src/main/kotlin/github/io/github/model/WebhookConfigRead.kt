package io.github.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Configuration object of the webhook
 */
@Serializable
public data class WebhookConfigRead(
  public val url: WebhookConfigUrlRead? = null,
  @SerialName("content_type")
  public val contentType: WebhookConfigContentTypeRead? = null,
  public val secret: WebhookConfigSecretRead? = null,
  @SerialName("insecure_ssl")
  public val insecureSsl: WebhookConfigInsecureSslRead? = null,
)
