package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class IssuesEvent(
    val action: String,
    val issue: Issue,
    val assignee: SimpleUser? = null,
    val assignees: List<SimpleUser>? = null,
    val label: Label? = null,
    val labels: List<Label>? = null,
)
