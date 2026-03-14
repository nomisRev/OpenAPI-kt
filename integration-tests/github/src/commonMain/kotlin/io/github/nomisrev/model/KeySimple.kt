package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class KeySimple(
    val id: Long,
    val key: String,
    @SerialName("created_at") val createdAt: LocalDateTime? = null,
    @SerialName("last_used") val lastUsed: LocalDateTime? = null,
)
