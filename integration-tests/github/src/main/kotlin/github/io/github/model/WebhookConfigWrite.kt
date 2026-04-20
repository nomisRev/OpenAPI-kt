package io.github.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Configuration object of the webhook
 */
@Serializable
public data class WebhookConfigWrite(
  public val url: WebhookConfigUrlWrite? = null,
  @SerialName("content_type")
  public val contentType: WebhookConfigContentTypeWrite? = null,
  public val secret: WebhookConfigSecretWrite? = null,
  @SerialName("insecure_ssl")
  public val insecureSsl: WebhookConfigInsecureSslWrite? = null,
)
