package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class SshSigningKey(
    val key: String,
    val id: Long,
    val title: String,
    @SerialName("created_at") val createdAt: LocalDateTime,
)
