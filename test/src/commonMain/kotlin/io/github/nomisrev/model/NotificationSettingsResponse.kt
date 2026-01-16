package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class NotificationSettingsResponse(
    val id: String? = null,
    val emailSettings: EmailSettingsResponse? = null,
    @SerialName($$"$type") val type: String? = null,
)
