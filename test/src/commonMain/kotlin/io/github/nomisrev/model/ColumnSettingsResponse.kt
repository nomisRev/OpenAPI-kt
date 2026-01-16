package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ColumnSettingsResponse(
    val id: String? = null,
    val field: CustomFieldResponse? = null,
    val columns: List<AgileColumnResponse>? = null,
    @SerialName($$"$type") val type: String? = null,
)
