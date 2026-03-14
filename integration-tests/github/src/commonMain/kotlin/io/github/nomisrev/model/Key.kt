package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Key(
    val key: String,
    val id: Long,
    val url: String,
    val title: String,
    @SerialName("created_at") val createdAt: LocalDateTime,
    val verified: Boolean,
    @SerialName("read_only") val readOnly: Boolean,
    @SerialName("last_used") val lastUsed: LocalDateTime? = null,
)
