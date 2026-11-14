package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.time.Instant
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A codespace.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class CodespaceWithFullRepository(
  public val id: Long,
  public val name: String,
  @SerialName("display_name")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val displayName: Optional<String> = Optional.Absent,
  @SerialName("environment_id")
  public val environmentId: String?,
  public val owner: SimpleUser,
  @SerialName("billable_owner")
  public val billableOwner: SimpleUser,
  public val repository: FullRepository,
  public val machine: NullableCodespaceMachine?,
  @SerialName("devcontainer_path")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val devcontainerPath: Optional<String> = Optional.Absent,
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
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val publishUrl: Optional<String> = Optional.Absent,
  @SerialName("pulls_url")
  public val pullsUrl: String?,
  @SerialName("recent_folders")
  public val recentFolders: List<String>,
  @SerialName("runtime_constraints")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val runtimeConstraints: RuntimeConstraints? = null,
  @SerialName("pending_operation")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val pendingOperation: Optional<Boolean> = Optional.Absent,
  @SerialName("pending_operation_disabled_reason")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val pendingOperationDisabledReason: Optional<String> = Optional.Absent,
  @SerialName("idle_timeout_notice")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val idleTimeoutNotice: Optional<String> = Optional.Absent,
  @SerialName("retention_period_minutes")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val retentionPeriodMinutes: Optional<Long> = Optional.Absent,
  @SerialName("retention_expires_at")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val retentionExpiresAt: Optional<Instant> = Optional.Absent,
) {
  /**
   * Details about the codespace's git repository.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class GitStatus(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val ahead: Long? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val behind: Long? = null,
    @SerialName("has_unpushed_changes")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val hasUnpushedChanges: Boolean? = null,
    @SerialName("has_uncommitted_changes")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val hasUncommittedChanges: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val ref: String? = null,
  )

  @Serializable
  public enum class Location {
    EastUs,
    SouthEastAsia,
    WestEurope,
    WestUs2,
  }

  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class RuntimeConstraints(
    @SerialName("allowed_port_privacy_settings")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val allowedPortPrivacySettings: Optional<List<String>> = Optional.Absent,
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
