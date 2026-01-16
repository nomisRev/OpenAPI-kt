package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class PeriodValueResponse(
    val id: String? = null,
    val minutes: Int? = null,
    val presentation: String? = null,
    @SerialName($$"$type") val type: String? = null,
)
