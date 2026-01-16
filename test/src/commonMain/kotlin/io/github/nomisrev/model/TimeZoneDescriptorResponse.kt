package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class TimeZoneDescriptorResponse(
    val id: String? = null,
    val presentation: String? = null,
    val offset: Int? = null,
    @SerialName($$"$type") val type: String? = null,
)
