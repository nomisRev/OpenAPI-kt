package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class IssueWatcherRequest(
    val user: UserRequest? = null,
    val issue: IssueRequest? = null,
    val isStarred: Boolean? = null,
)
