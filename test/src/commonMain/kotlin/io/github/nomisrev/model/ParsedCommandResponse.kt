package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ParsedCommandResponse(
    val id: String? = null,
    val description: String? = null,
    val error: Boolean? = null,
    val delete: Boolean? = null,
    @SerialName($$"$type") val type: String? = null,
)
