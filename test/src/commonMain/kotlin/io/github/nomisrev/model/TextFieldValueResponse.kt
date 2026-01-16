package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class TextFieldValueResponse(
    val id: String? = null,
    val text: String? = null,
    val markdownText: String? = null,
    @SerialName($$"$type") val type: String? = null,
)
