package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class IssueTimeTrackerResponse(
    val id: String? = null,
    val workItems: List<IssueWorkItem>? = null,
    val enabled: Boolean? = null,
    @SerialName($$"$type") val type: String? = null,
)
