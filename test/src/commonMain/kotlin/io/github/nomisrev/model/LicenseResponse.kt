package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class LicenseResponse(
    val id: String? = null,
    val username: String? = null,
    val license: String? = null,
    val error: String? = null,
    @SerialName($$"$type") val type: String? = null,
)
