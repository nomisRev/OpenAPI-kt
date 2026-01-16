package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class CommandListResponse(
    val id: String? = null,
    val comment: String? = null,
    val visibility: CommandVisibilityResponse? = null,
    val query: String? = null,
    val caret: Int? = null,
    val silent: Boolean? = null,
    val runAs: String? = null,
    val commands: List<ParsedCommandResponse>? = null,
    val issues: List<IssueResponse>? = null,
    val suggestions: List<SuggestionResponse>? = null,
    @SerialName($$"$type") val type: String? = null,
)
