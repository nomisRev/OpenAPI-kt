package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class IssueWatchersResponse(
    val id: String? = null,
    val hasStar: Boolean? = null,
    val issueWatchers: List<IssueWatcherResponse>? = null,
    val duplicateWatchers: List<IssueWatcherResponse>? = null,
    @SerialName($$"$type") val type: String? = null,
)
