package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A codespace.
 */
@Serializable
public data class CodespaceWithFullRepository(
  public val id: Long,
  public val name: String,
  @SerialName("display_name")
  public val displayName: String? = null,
  @SerialName("environment_id")
  public val environmentId: String?,
  public val owner: SimpleUser,
  @SerialName("billable_owner")
  public val billableOwner: SimpleUser,
  public val repository: FullRepository,
  public val machine: NullableCodespaceMachine?,
  @SerialName("devcontainer_path")
  public val devcontainerPath: String? = null,
  public val prebuild: Boolean?,
  @SerialName("created_at")
  public val createdAt: Instant,
  @SerialName("updated_at")
  public val updatedAt: Instant,
  @SerialName("last_used_at")
  public val lastUsedAt: Instant,
  public val state: State,
  public val url: String,
  @SerialName("git_status")
  public val gitStatus: GitStatus,
  public val location: Location,
  @SerialName("idle_timeout_minutes")
  public val idleTimeoutMinutes: Long?,
  @SerialName("web_url")
  public val webUrl: String,
  @SerialName("machines_url")
  public val machinesUrl: String,
  @SerialName("start_url")
  public val startUrl: String,
  @SerialName("stop_url")
  public val stopUrl: String,
  @SerialName("publish_url")
  public val publishUrl: String? = null,
  @SerialName("pulls_url")
  public val pullsUrl: String?,
  @SerialName("recent_folders")
  public val recentFolders: List<String>,
  @SerialName("runtime_constraints")
  public val runtimeConstraints: RuntimeConstraints? = null,
  @SerialName("pending_operation")
  public val pendingOperation: Boolean? = null,
  @SerialName("pending_operation_disabled_reason")
  public val pendingOperationDisabledReason: String? = null,
  @SerialName("idle_timeout_notice")
  public val idleTimeoutNotice: String? = null,
  @SerialName("retention_period_minutes")
  public val retentionPeriodMinutes: Long? = null,
  @SerialName("retention_expires_at")
  public val retentionExpiresAt: Instant? = null,
) {
  /**
   * Details about the codespace's git repository.
   */
  @Serializable
  public data class GitStatus(
    public val ahead: Long? = null,
    public val behind: Long? = null,
    @SerialName("has_unpushed_changes")
    public val hasUnpushedChanges: Boolean? = null,
    @SerialName("has_uncommitted_changes")
    public val hasUncommittedChanges: Boolean? = null,
    public val ref: String? = null,
  )

  @Serializable
  public enum class Location {
    EastUs,
    SouthEastAsia,
    WestEurope,
    WestUs2,
  }

  @JvmInline
  @Serializable
  public value class RuntimeConstraints(
    @SerialName("allowed_port_privacy_settings")
    public val allowedPortPrivacySettings: List<String>? = null,
  )

  @Serializable
  public enum class State {
    Unknown,
    Created,
    Queued,
    Provisioning,
    Available,
    Awaiting,
    Unavailable,
    Deleted,
    Moved,
    Shutdown,
    Archived,
    Starting,
    ShuttingDown,
    Failed,
    Exporting,
    Updating,
    Rebuilding,
  }
}
