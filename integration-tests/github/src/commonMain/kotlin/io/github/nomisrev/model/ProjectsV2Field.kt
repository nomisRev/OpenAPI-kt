package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ProjectsV2Field(
    val id: Long,
    @SerialName("issue_field_id") val issueFieldId: Long? = null,
    @SerialName("node_id") val nodeId: String? = null,
    @SerialName("project_url") val projectUrl: String,
    val name: String,
    @SerialName("data_type") val dataType: DataType,
    val options: List<ProjectsV2SingleSelectOptions>? = null,
    val configuration: Configuration? = null,
    @SerialName("created_at") val createdAt: LocalDateTime,
    @SerialName("updated_at") val updatedAt: LocalDateTime,
) {
    @Serializable
    enum class DataType {
        @SerialName("assignees")
        Assignees,
        @SerialName("linked_pull_requests")
        LinkedPullRequests,
        @SerialName("reviewers")
        Reviewers,
        @SerialName("labels")
        Labels,
        @SerialName("milestone")
        Milestone,
        @SerialName("repository")
        Repository,
        @SerialName("title")
        Title,
        @SerialName("text")
        Text,
        @SerialName("single_select")
        SingleSelect,
        @SerialName("number")
        Number,
        @SerialName("date")
        Date,
        @SerialName("iteration")
        Iteration,
        @SerialName("issue_type")
        IssueType,
        @SerialName("parent_issue")
        ParentIssue,
        @SerialName("sub_issues_progress")
        SubIssuesProgress;
    }

    @Serializable
    data class Configuration(
        @SerialName("start_day") val startDay: Long? = null,
        val duration: Long? = null,
        val iterations: List<ProjectsV2IterationSettings>? = null,
    )
}
