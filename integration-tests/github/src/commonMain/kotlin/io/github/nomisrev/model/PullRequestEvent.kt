package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class PullRequestEvent(
    val action: String,
    val number: Long,
    @SerialName("pull_request") val pullRequest: PullRequestMinimal,
    val assignee: SimpleUser? = null,
    val assignees: List<SimpleUser>? = null,
    val label: Label? = null,
    val labels: List<Label>? = null,
)
