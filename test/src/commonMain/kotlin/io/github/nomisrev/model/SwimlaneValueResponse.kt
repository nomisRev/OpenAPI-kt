package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class SwimlaneValueResponse(
    val id: String? = null,
    val name: String? = null,
    @SerialName($$"$type") val type: String? = null,
)
