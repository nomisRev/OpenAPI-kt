package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class OrgHook(
    val id: Long,
    val url: String,
    @SerialName("ping_url") val pingUrl: String,
    @SerialName("deliveries_url") val deliveriesUrl: String? = null,
    val name: String,
    val events: List<String>,
    val active: Boolean,
    val config: Config,
    @SerialName("updated_at") val updatedAt: LocalDateTime,
    @SerialName("created_at") val createdAt: LocalDateTime,
    val type: String,
) {
    @Serializable
    data class Config(
        val url: String? = null,
        @SerialName("insecure_ssl") val insecureSsl: String? = null,
        @SerialName("content_type") val contentType: String? = null,
        val secret: String? = null,
    )
}
