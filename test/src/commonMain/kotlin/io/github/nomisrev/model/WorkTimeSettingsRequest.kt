package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class WorkTimeSettingsRequest(val minutesADay: Int? = null, val workDays: List<Int>? = null)
