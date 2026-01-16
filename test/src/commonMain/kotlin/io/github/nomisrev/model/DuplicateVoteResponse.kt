package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class DuplicateVoteResponse(
    val id: String? = null,
    val issue: IssueResponse? = null,
    val user: UserResponse? = null,
    @SerialName($$"$type") val type: String? = null,
)
