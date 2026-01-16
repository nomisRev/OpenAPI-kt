package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class SprintResponse(
    val id: String? = null,
    val agile: AgileResponse? = null,
    val name: String? = null,
    val goal: String? = null,
    val start: Long? = null,
    val finish: Long? = null,
    val archived: Boolean? = null,
    val isDefault: Boolean? = null,
    val issues: List<IssueResponse>? = null,
    val unresolvedIssuesCount: Int? = null,
    val previousSprint: SprintResponse? = null,
    @SerialName($$"$type") val type: String? = null,
)
