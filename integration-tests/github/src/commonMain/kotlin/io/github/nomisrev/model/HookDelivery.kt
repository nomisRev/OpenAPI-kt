package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonElement

@Serializable
data class HookDelivery(
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
    val url: String? = null,
    val request: Request,
    val response: Response,
) {
    @Serializable
    data class Request(val headers: JsonElement?, val payload: JsonElement?)

    @Serializable
    data class Response(val headers: JsonElement?, val payload: String?)
}
