package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class IssueVotersResponse(
    val id: String? = null,
    val hasVote: Boolean? = null,
    val original: List<UserResponse>? = null,
    val duplicate: List<DuplicateVoteResponse>? = null,
    @SerialName($$"$type") val type: String? = null,
)
