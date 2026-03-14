package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ReactionRollup(
    val url: String,
    @SerialName("total_count") val totalCount: Long,
    @SerialName("+1") val `+1`: Long,
    @SerialName("-1") val `-1`: Long,
    val laugh: Long,
    val confused: Long,
    val heart: Long,
    val hooray: Long,
    val eyes: Long,
    val rocket: Long,
)
