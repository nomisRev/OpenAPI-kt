package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class IssueCountResponseRequest(
    val count: Long? = null,
    val unresolvedOnly: Boolean? = null,
    val query: String? = null,
    val folder: IssueFolderRequest? = null,
)
