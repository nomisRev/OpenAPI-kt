package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class LocaleSettingsResponse(
    val id: String? = null,
    val locale: LocaleDescriptorResponse? = null,
    val isRTL: Boolean? = null,
    @SerialName($$"$type") val type: String? = null,
)
