package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class AppearanceSettingsRequest(
    val timeZone: TimeZoneDescriptorRequest? = null,
    val dateFieldFormat: DateFormatDescriptorRequest? = null,
    val logo: LogoRequest? = null,
)
