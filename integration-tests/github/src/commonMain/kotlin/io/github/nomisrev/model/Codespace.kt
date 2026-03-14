package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlin.jvm.JvmInline

@Serializable
data class Codespace(
    val id: Long,
    val name: String,
    @SerialName("display_name") val displayName: String? = null,
    @SerialName("environment_id") val environmentId: String?,
    val owner: SimpleUser,
    @SerialName("billable_owner") val billableOwner: SimpleUser,
    val repository: MinimalRepository,
    val machine: NullableCodespaceMachine?,
    @SerialName("devcontainer_path") val devcontainerPath: String? = null,
    val prebuild: Boolean?,
    @SerialName("created_at") val createdAt: LocalDateTime,
    @SerialName("updated_at") val updatedAt: LocalDateTime,
    @SerialName("last_used_at") val lastUsedAt: LocalDateTime,
    val state: State,
    val url: String,
    @SerialName("git_status") val gitStatus: GitStatus,
    val location: Location,
    @SerialName("idle_timeout_minutes") val idleTimeoutMinutes: Long?,
    @SerialName("web_url") val webUrl: String,
    @SerialName("machines_url") val machinesUrl: String,
    @SerialName("start_url") val startUrl: String,
    @SerialName("stop_url") val stopUrl: String,
    @SerialName("publish_url") val publishUrl: String? = null,
    @SerialName("pulls_url") val pullsUrl: String?,
    @SerialName("recent_folders") val recentFolders: List<String>,
    @SerialName("runtime_constraints") val runtimeConstraints: RuntimeConstraints? = null,
    @SerialName("pending_operation") val pendingOperation: Boolean? = null,
    @SerialName("pending_operation_disabled_reason") val pendingOperationDisabledReason: String? = null,
    @SerialName("idle_timeout_notice") val idleTimeoutNotice: String? = null,
    @SerialName("retention_period_minutes") val retentionPeriodMinutes: Long? = null,
    @SerialName("retention_expires_at") val retentionExpiresAt: LocalDateTime? = null,
    @SerialName("last_known_stop_notice") val lastKnownStopNotice: String? = null,
) {
    @Serializable
    enum class State {
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
        Rebuilding;
    }

    @Serializable
    data class GitStatus(
        val ahead: Long? = null,
        val behind: Long? = null,
        @SerialName("has_unpushed_changes") val hasUnpushedChanges: Boolean? = null,
        @SerialName("has_uncommitted_changes") val hasUncommittedChanges: Boolean? = null,
        val ref: String? = null,
    )

    @Serializable
    enum class Location {
        EastUs, SouthEastAsia, WestEurope, WestUs2;
    }

    @Serializable
    @JvmInline
    value class RuntimeConstraints(@SerialName("allowed_port_privacy_settings") val allowedPortPrivacySettings: List<String>? = null)
}
