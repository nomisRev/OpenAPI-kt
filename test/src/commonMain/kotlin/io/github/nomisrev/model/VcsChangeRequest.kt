package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class VcsChangeRequest(
    val author: UserRequest? = null,
    val version: String? = null,
    val issue: IssueRequest? = null,
    val state: Int? = null,
)
