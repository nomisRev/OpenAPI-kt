package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class FieldTypeResponse(val id: String? = null, @SerialName($$"$type") val type: String? = null)
