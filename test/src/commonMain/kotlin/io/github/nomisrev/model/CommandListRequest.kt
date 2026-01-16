package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class CommandListRequest(
    val comment: String? = null,
    val visibility: CommandVisibilityRequest? = null,
    val query: String? = null,
    val caret: Int? = null,
    val silent: Boolean? = null,
    val runAs: String? = null,
    val issues: List<IssueRequest>? = null,
)
