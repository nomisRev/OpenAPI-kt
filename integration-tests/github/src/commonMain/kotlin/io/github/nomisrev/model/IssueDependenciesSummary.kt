package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class IssueDependenciesSummary(
    @SerialName("blocked_by") val blockedBy: Long,
    val blocking: Long,
    @SerialName("total_blocked_by") val totalBlockedBy: Long,
    @SerialName("total_blocking") val totalBlocking: Long,
)
