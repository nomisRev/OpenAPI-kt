package io.github.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Converted Note to Issue Issue Event
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class ConvertedNoteToIssueIssueEvent(
  public val id: Long,
  @SerialName("node_id")
  public val nodeId: String,
  public val url: String,
  public val actor: SimpleUser,
  public val event: String,
  @SerialName("commit_id")
  public val commitId: String?,
  @SerialName("commit_url")
  public val commitUrl: String?,
  @SerialName("created_at")
  public val createdAt: String,
  @SerialName("performed_via_github_app")
  public val performedViaGithubApp: Integration?,
  @SerialName("project_card")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val projectCard: ProjectCard? = null,
) {
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class ProjectCard(
    public val id: Long,
    public val url: String,
    @SerialName("project_id")
    public val projectId: Long,
    @SerialName("project_url")
    public val projectUrl: String,
    @SerialName("column_name")
    public val columnName: String,
    @SerialName("previous_column_name")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val previousColumnName: String? = null,
  )
}
