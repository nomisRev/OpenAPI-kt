package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class GeneralUserProfileResponse(
    val id: String? = null,
    val dateFieldFormat: DateFormatDescriptorResponse? = null,
    val timezone: TimeZoneDescriptorResponse? = null,
    val locale: LocaleDescriptorResponse? = null,
    @SerialName($$"$type") val type: String? = null,
)
