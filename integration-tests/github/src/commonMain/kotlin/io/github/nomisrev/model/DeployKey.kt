package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class DeployKey(
    val id: Long,
    val key: String,
    val url: String,
    val title: String,
    val verified: Boolean,
    @SerialName("created_at") val createdAt: String,
    @SerialName("read_only") val readOnly: Boolean,
    @SerialName("added_by") val addedBy: String? = null,
    @SerialName("last_used") val lastUsed: LocalDateTime? = null,
    val enabled: Boolean? = null,
)
