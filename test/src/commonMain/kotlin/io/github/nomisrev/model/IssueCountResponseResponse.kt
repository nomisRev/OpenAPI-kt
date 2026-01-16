package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class IssueCountResponseResponse(
    val id: String? = null,
    val count: Long? = null,
    val unresolvedOnly: Boolean? = null,
    val query: String? = null,
    val folder: IssueFolderResponse? = null,
    @SerialName($$"$type") val type: String? = null,
)
