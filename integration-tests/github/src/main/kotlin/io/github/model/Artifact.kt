package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * An artifact
 */
@OptIn(ExperimentalSerializationApi::class)
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
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val digest: Optional<String> = Optional.Absent,
  @SerialName("workflow_run")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val workflowRun: Optional<WorkflowRun> = Optional.Absent,
) {
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class WorkflowRun(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val id: Long? = null,
    @SerialName("repository_id")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val repositoryId: Long? = null,
    @SerialName("head_repository_id")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val headRepositoryId: Long? = null,
    @SerialName("head_branch")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val headBranch: String? = null,
    @SerialName("head_sha")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val headSha: String? = null,
  )
}
