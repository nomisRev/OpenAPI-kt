package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class SubIssuesSummary(
    val total: Long,
    val completed: Long,
    @SerialName("percent_completed") val percentCompleted: Long,
)
