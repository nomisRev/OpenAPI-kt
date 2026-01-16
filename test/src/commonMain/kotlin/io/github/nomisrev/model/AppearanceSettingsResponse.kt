package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class AppearanceSettingsResponse(
    val id: String? = null,
    val timeZone: TimeZoneDescriptorResponse? = null,
    val dateFieldFormat: DateFormatDescriptorResponse? = null,
    val logo: LogoResponse? = null,
    @SerialName($$"$type") val type: String? = null,
)
