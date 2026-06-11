package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A migration.
 */
@Serializable
public data class Migration(
  public val id: Long,
  public val owner: NullableSimpleUser?,
  public val guid: String,
  public val state: String,
  @SerialName("lock_repositories")
  public val lockRepositories: Boolean,
  @SerialName("exclude_metadata")
  public val excludeMetadata: Boolean,
  @SerialName("exclude_git_data")
  public val excludeGitData: Boolean,
  @SerialName("exclude_attachments")
  public val excludeAttachments: Boolean,
  @SerialName("exclude_releases")
  public val excludeReleases: Boolean,
  @SerialName("exclude_owner_projects")
  public val excludeOwnerProjects: Boolean,
  @SerialName("org_metadata_only")
  public val orgMetadataOnly: Boolean,
  public val repositories: List<Repository>,
  public val url: String,
  @SerialName("created_at")
  public val createdAt: Instant,
  @SerialName("updated_at")
  public val updatedAt: Instant,
  @SerialName("node_id")
  public val nodeId: String,
  @SerialName("archive_url")
  public val archiveUrl: String? = null,
  public val exclude: List<String>? = null,
)
