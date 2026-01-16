package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ActivityCursorPageResponse(
    val id: String? = null,
    val activities: List<ActivityItemResponse>? = null,
    val afterCursor: String? = null,
    val beforeCursor: String? = null,
    val hasAfter: Boolean? = null,
    val hasBefore: Boolean? = null,
    val reverse: Boolean? = null,
    @SerialName($$"$type") val type: String? = null,
)
