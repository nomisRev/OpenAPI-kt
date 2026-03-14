package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class SecretScanningScan(
    val type: String? = null,
    val status: String? = null,
    @SerialName("completed_at") val completedAt: LocalDateTime? = null,
    @SerialName("started_at") val startedAt: LocalDateTime? = null,
)
