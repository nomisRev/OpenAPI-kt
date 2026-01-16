package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class LogoResponse(
    val id: String? = null,
    val url: String? = null,
    @SerialName($$"$type") val type: String? = null,
)
