package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class AgileColumnResponse(
    val id: String? = null,
    val presentation: String? = null,
    val isResolved: Boolean? = null,
    val ordinal: Int? = null,
    val parent: ColumnSettingsResponse? = null,
    val wipLimit: WIPLimitResponse? = null,
    val fieldValues: List<AgileColumnFieldValue>? = null,
    @SerialName($$"$type") val type: String? = null,
)
