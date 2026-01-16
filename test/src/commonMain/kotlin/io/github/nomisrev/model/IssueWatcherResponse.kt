package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class IssueWatcherResponse(
    val id: String? = null,
    val user: UserResponse? = null,
    val issue: IssueResponse? = null,
    val isStarred: Boolean? = null,
    @SerialName($$"$type") val type: String? = null,
)
