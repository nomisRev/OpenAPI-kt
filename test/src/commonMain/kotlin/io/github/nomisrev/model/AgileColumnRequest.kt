package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class AgileColumnRequest(
    val ordinal: Int? = null,
    val parent: ColumnSettingsRequest? = null,
    val wipLimit: WIPLimitRequest? = null,
    val fieldValues: List<AgileColumnFieldValue>? = null,
)
