package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class HookDeliveryItem(
    val id: Long,
    val guid: String,
    @SerialName("delivered_at") val deliveredAt: LocalDateTime,
    val redelivery: Boolean,
    val duration: Double,
    val status: String,
    @SerialName("status_code") val statusCode: Long,
    val event: String,
    val action: String?,
    @SerialName("installation_id") val installationId: Long?,
    @SerialName("repository_id") val repositoryId: Long?,
    @SerialName("throttled_at") val throttledAt: LocalDateTime? = null,
)
