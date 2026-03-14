package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Required

@Serializable
data class NullableMilestone(
    val url: String,
    @SerialName("html_url") val htmlUrl: String,
    @SerialName("labels_url") val labelsUrl: String,
    val id: Long,
    @SerialName("node_id") val nodeId: String,
    val number: Long,
    @Required val state: State,
    val title: String,
    val description: String?,
    val creator: NullableSimpleUser?,
    @SerialName("open_issues") val openIssues: Long,
    @SerialName("closed_issues") val closedIssues: Long,
    @SerialName("created_at") val createdAt: LocalDateTime,
    @SerialName("updated_at") val updatedAt: LocalDateTime,
    @SerialName("closed_at") val closedAt: LocalDateTime?,
    @SerialName("due_on") val dueOn: LocalDateTime?,
) {
    @Serializable
    enum class State {
        @SerialName("open") Open, @SerialName("closed") Closed;
    }
}
