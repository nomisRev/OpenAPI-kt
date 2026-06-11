package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * An artifact
 */
@Serializable
public data class Artifact(
  public val id: Long,
  @SerialName("node_id")
  public val nodeId: String,
  public val name: String,
  @SerialName("size_in_bytes")
  public val sizeInBytes: Long,
  public val url: String,
  @SerialName("archive_download_url")
  public val archiveDownloadUrl: String,
  public val expired: Boolean,
  @SerialName("created_at")
  public val createdAt: Instant?,
  @SerialName("expires_at")
  public val expiresAt: Instant?,
  @SerialName("updated_at")
  public val updatedAt: Instant?,
  public val digest: String? = null,
  @SerialName("workflow_run")
  public val workflowRun: WorkflowRun? = null,
) {
  @Serializable
  public data class WorkflowRun(
    public val id: Long? = null,
    @SerialName("repository_id")
    public val repositoryId: Long? = null,
    @SerialName("head_repository_id")
    public val headRepositoryId: Long? = null,
    @SerialName("head_branch")
    public val headBranch: String? = null,
    @SerialName("head_sha")
    public val headSha: String? = null,
  )
}
