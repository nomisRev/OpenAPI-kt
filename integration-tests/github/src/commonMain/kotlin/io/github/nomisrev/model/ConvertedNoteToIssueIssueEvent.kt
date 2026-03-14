package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ConvertedNoteToIssueIssueEvent(
    val id: Long,
    @SerialName("node_id") val nodeId: String,
    val url: String,
    val actor: SimpleUser,
    val event: String,
    @SerialName("commit_id") val commitId: String?,
    @SerialName("commit_url") val commitUrl: String?,
    @SerialName("created_at") val createdAt: String,
    @SerialName("performed_via_github_app") val performedViaGithubApp: Integration?,
    @SerialName("project_card") val projectCard: ProjectCard? = null,
) {
    @Serializable
    data class ProjectCard(
        val id: Long,
        val url: String,
        @SerialName("project_id") val projectId: Long,
        @SerialName("project_url") val projectUrl: String,
        @SerialName("column_name") val columnName: String,
        @SerialName("previous_column_name") val previousColumnName: String? = null,
    )
}
