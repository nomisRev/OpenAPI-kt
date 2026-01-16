package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class FieldStyleResponse(
    val id: String? = null,
    val background: String? = null,
    val foreground: String? = null,
    @SerialName($$"$type") val type: String? = null,
)
