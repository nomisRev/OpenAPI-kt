package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class WorkTimeSettingsResponse(
    val id: String? = null,
    val minutesADay: Int? = null,
    val workDays: List<Int>? = null,
    val firstDayOfWeek: Int? = null,
    val daysAWeek: Int? = null,
    @SerialName($$"$type") val type: String? = null,
)
