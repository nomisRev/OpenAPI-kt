package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class InteractionLimitResponse(
    val limit: InteractionGroup,
    val origin: String,
    @SerialName("expires_at") val expiresAt: LocalDateTime,
)
