package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class RestCorsSettingsResponse(
    val id: String? = null,
    val allowedOrigins: List<String>? = null,
    val allowAllOrigins: Boolean? = null,
    @SerialName($$"$type") val type: String? = null,
)
