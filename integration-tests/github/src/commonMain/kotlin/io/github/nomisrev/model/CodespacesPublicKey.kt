package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class CodespacesPublicKey(
    @SerialName("key_id") val keyId: String,
    val key: String,
    val id: Long? = null,
    val url: String? = null,
    val title: String? = null,
    @SerialName("created_at") val createdAt: String? = null,
)
