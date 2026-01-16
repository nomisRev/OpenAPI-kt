package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class GeneralUserProfileRequest(
    val dateFieldFormat: DateFormatDescriptorRequest? = null,
    val timezone: TimeZoneDescriptorRequest? = null,
    val locale: LocaleDescriptorRequest? = null,
)
