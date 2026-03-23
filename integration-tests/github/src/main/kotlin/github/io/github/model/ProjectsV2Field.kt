package io.github.model

import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A field inside a projects v2 project
 */
@Serializable
public data class ProjectsV2Field(
  public val id: Long,
  @SerialName("issue_field_id")
  public val issueFieldId: Long? = null,
  @SerialName("node_id")
  public val nodeId: String? = null,
  @SerialName("project_url")
  public val projectUrl: String,
  public val name: String,
  @SerialName("data_type")
  public val dataType: DataType,
  public val options: List<ProjectsV2SingleSelectOptions>? = null,
  public val configuration: Configuration? = null,
  @SerialName("created_at")
  public val createdAt: Instant,
  @SerialName("updated_at")
  public val updatedAt: Instant,
) {
  /**
   * Configuration for iteration fields.
   */
  @Serializable
  public data class Configuration(
    @SerialName("start_day")
    public val startDay: Long? = null,
    public val duration: Long? = null,
    public val iterations: List<ProjectsV2IterationSettings>? = null,
  )

  @Serializable
  public enum class DataType(
    public val `value`: String,
  ) {
    @SerialName("assignees")
    Assignees("assignees"),
    @SerialName("linked_pull_requests")
    LinkedPullRequests("linked_pull_requests"),
    @SerialName("reviewers")
    Reviewers("reviewers"),
    @SerialName("labels")
    Labels("labels"),
    @SerialName("milestone")
    Milestone("milestone"),
    @SerialName("repository")
    Repository("repository"),
    @SerialName("title")
    Title("title"),
    @SerialName("text")
    Text("text"),
    @SerialName("single_select")
    SingleSelect("single_select"),
    @SerialName("number")
    Number("number"),
    @SerialName("date")
    Date("date"),
    @SerialName("iteration")
    Iteration("iteration"),
    @SerialName("issue_type")
    IssueType("issue_type"),
    @SerialName("parent_issue")
    ParentIssue("parent_issue"),
    @SerialName("sub_issues_progress")
    SubIssuesProgress("sub_issues_progress"),
    ;
  }
}
