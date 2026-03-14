package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ActionsArtifactAndLogRetentionResponse(
    val days: Long,
    @SerialName("maximum_allowed_days") val maximumAllowedDays: Long,
)
