package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class TimeTrackingUserProfileResponse(
    val id: String? = null,
    val periodFormat: PeriodFieldFormatResponse? = null,
    @SerialName($$"$type") val type: String? = null,
)
