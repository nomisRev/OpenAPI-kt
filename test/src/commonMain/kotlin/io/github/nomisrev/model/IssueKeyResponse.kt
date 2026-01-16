package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class IssueKeyResponse(
    val id: String? = null,
    val project: Project? = null,
    val numberInProject: Long? = null,
    @SerialName($$"$type") val type: String? = null,
)
