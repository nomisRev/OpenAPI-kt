package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Authorization(
    val id: Long,
    val url: String,
    val scopes: List<String>?,
    val token: String,
    @SerialName("token_last_eight") val tokenLastEight: String?,
    @SerialName("hashed_token") val hashedToken: String?,
    val app: App,
    val note: String?,
    @SerialName("note_url") val noteUrl: String?,
    @SerialName("updated_at") val updatedAt: LocalDateTime,
    @SerialName("created_at") val createdAt: LocalDateTime,
    val fingerprint: String?,
    val user: NullableSimpleUser? = null,
    val installation: NullableScopedInstallation? = null,
    @SerialName("expires_at") val expiresAt: LocalDateTime?,
) {
    @Serializable
    data class App(@SerialName("client_id") val clientId: String, val name: String, val url: String)
}
