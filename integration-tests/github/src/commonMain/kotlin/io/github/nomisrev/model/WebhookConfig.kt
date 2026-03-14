package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class WebhookConfig(
    val url: WebhookConfigUrl? = null,
    @SerialName("content_type") val contentType: WebhookConfigContentType? = null,
    val secret: WebhookConfigSecret? = null,
    @SerialName("insecure_ssl") val insecureSsl: WebhookConfigInsecureSsl? = null,
)
