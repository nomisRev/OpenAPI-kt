package io.github.model

import kotlin.OptIn
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Configuration object of the webhook
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class WebhookConfig(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val url: WebhookConfigUrl? = null,
  @SerialName("content_type")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val contentType: WebhookConfigContentType? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val secret: WebhookConfigSecret? = null,
  @SerialName("insecure_ssl")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val insecureSsl: WebhookConfigInsecureSsl? = null,
)
