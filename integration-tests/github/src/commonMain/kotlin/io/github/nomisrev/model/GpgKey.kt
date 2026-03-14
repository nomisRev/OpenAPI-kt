package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonArray

@Serializable
data class GpgKey(
    val id: Long,
    val name: String? = null,
    @SerialName("primary_key_id") val primaryKeyId: Long?,
    @SerialName("key_id") val keyId: String,
    @SerialName("public_key") val publicKey: String,
    val emails: List<Emails>,
    val subkeys: List<Subkeys>,
    @SerialName("can_sign") val canSign: Boolean,
    @SerialName("can_encrypt_comms") val canEncryptComms: Boolean,
    @SerialName("can_encrypt_storage") val canEncryptStorage: Boolean,
    @SerialName("can_certify") val canCertify: Boolean,
    @SerialName("created_at") val createdAt: LocalDateTime,
    @SerialName("expires_at") val expiresAt: LocalDateTime?,
    val revoked: Boolean,
    @SerialName("raw_key") val rawKey: String?,
) {
    @Serializable
    data class Emails(val email: String? = null, val verified: Boolean? = null)

    @Serializable
    data class Subkeys(
        val id: Long? = null,
        @SerialName("primary_key_id") val primaryKeyId: Long? = null,
        @SerialName("key_id") val keyId: String? = null,
        @SerialName("public_key") val publicKey: String? = null,
        val emails: List<Emails>? = null,
        val subkeys: JsonArray? = null,
        @SerialName("can_sign") val canSign: Boolean? = null,
        @SerialName("can_encrypt_comms") val canEncryptComms: Boolean? = null,
        @SerialName("can_encrypt_storage") val canEncryptStorage: Boolean? = null,
        @SerialName("can_certify") val canCertify: Boolean? = null,
        @SerialName("created_at") val createdAt: String? = null,
        @SerialName("expires_at") val expiresAt: String? = null,
        @SerialName("raw_key") val rawKey: String? = null,
        val revoked: Boolean? = null,
    ) {
        @Serializable
        data class Emails(val email: String? = null, val verified: Boolean? = null)
    }
}
