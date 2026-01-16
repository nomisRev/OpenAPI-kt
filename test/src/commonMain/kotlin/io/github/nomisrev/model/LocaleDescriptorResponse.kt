package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class LocaleDescriptorResponse(
    val id: String? = null,
    val locale: String? = null,
    val language: String? = null,
    val community: Boolean? = null,
    val name: String? = null,
    @SerialName($$"$type") val type: String? = null,
)
