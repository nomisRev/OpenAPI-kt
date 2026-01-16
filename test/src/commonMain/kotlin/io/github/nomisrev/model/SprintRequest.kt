package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class SprintRequest(
    val agile: AgileRequest? = null,
    val name: String? = null,
    val goal: String? = null,
    val start: Long? = null,
    val finish: Long? = null,
    val archived: Boolean? = null,
    val isDefault: Boolean? = null,
    val issues: List<IssueRequest>? = null,
    val previousSprint: SprintRequest? = null,
)
