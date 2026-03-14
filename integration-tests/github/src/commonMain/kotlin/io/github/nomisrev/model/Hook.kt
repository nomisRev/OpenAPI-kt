package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Hook(
    val type: String,
    val id: Long,
    val name: String,
    val active: Boolean,
    val events: List<String>,
    val config: WebhookConfig,
    @SerialName("updated_at") val updatedAt: LocalDateTime,
    @SerialName("created_at") val createdAt: LocalDateTime,
    val url: String,
    @SerialName("test_url") val testUrl: String,
    @SerialName("ping_url") val pingUrl: String,
    @SerialName("deliveries_url") val deliveriesUrl: String? = null,
    @SerialName("last_response") val lastResponse: HookResponse,
)
