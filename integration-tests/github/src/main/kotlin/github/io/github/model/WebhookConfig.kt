package io.github.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Configuration object of the webhook
 */
@Serializable
public data class WebhookConfig(
  public val url: WebhookConfigUrl? = null,
  @SerialName("content_type")
  public val contentType: WebhookConfigContentType? = null,
  public val secret: WebhookConfigSecret? = null,
  @SerialName("insecure_ssl")
  public val insecureSsl: WebhookConfigInsecureSsl? = null,
)
