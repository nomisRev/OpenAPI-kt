package io.openai.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * A log of a user action or configuration change within this organization.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class AuditLog(
  public val id: String,
  public val type: AuditLogEventType,
  @SerialName("effective_at")
  public val effectiveAt: Long,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val project: Project? = null,
  public val actor: AuditLogActor,
  @SerialName("api_key.created")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val apiKeyCreated: ApiKeyCreated? = null,
  @SerialName("api_key.updated")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val apiKeyUpdated: ApiKeyUpdated? = null,
  @SerialName("api_key.deleted")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val apiKeyDeleted: ApiKeyDeleted? = null,
  @SerialName("checkpoint.permission.created")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val checkpointPermissionCreated: CheckpointPermissionCreated? = null,
  @SerialName("checkpoint.permission.deleted")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val checkpointPermissionDeleted: CheckpointPermissionDeleted? = null,
  @SerialName("external_key.registered")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val externalKeyRegistered: ExternalKeyRegistered? = null,
  @SerialName("external_key.removed")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val externalKeyRemoved: ExternalKeyRemoved? = null,
  @SerialName("group.created")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val groupCreated: GroupCreated? = null,
  @SerialName("group.updated")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val groupUpdated: GroupUpdated? = null,
  @SerialName("group.deleted")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val groupDeleted: GroupDeleted? = null,
  @SerialName("scim.enabled")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val scimEnabled: ScimEnabled? = null,
  @SerialName("scim.disabled")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val scimDisabled: ScimDisabled? = null,
  @SerialName("invite.sent")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val inviteSent: InviteSent? = null,
  @SerialName("invite.accepted")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val inviteAccepted: InviteAccepted? = null,
  @SerialName("invite.deleted")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val inviteDeleted: InviteDeleted? = null,
  @SerialName("ip_allowlist.created")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val ipAllowlistCreated: IpAllowlistCreated? = null,
  @SerialName("ip_allowlist.updated")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val ipAllowlistUpdated: IpAllowlistUpdated? = null,
  @SerialName("ip_allowlist.deleted")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val ipAllowlistDeleted: IpAllowlistDeleted? = null,
  @SerialName("ip_allowlist.config.activated")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val ipAllowlistConfigActivated: IpAllowlistConfigActivated? = null,
  @SerialName("ip_allowlist.config.deactivated")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val ipAllowlistConfigDeactivated: IpAllowlistConfigDeactivated? = null,
  @SerialName("login.succeeded")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val loginSucceeded: JsonElement? = null,
  @SerialName("login.failed")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val loginFailed: LoginFailed? = null,
  @SerialName("logout.succeeded")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val logoutSucceeded: JsonElement? = null,
  @SerialName("logout.failed")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val logoutFailed: LogoutFailed? = null,
  @SerialName("organization.updated")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val organizationUpdated: OrganizationUpdated? = null,
  @SerialName("project.created")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val projectCreated: ProjectCreated? = null,
  @SerialName("project.updated")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val projectUpdated: ProjectUpdated? = null,
  @SerialName("project.archived")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val projectArchived: ProjectArchived? = null,
  @SerialName("project.deleted")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val projectDeleted: ProjectDeleted? = null,
  @SerialName("rate_limit.updated")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val rateLimitUpdated: RateLimitUpdated? = null,
  @SerialName("rate_limit.deleted")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val rateLimitDeleted: RateLimitDeleted? = null,
  @SerialName("role.created")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val roleCreated: RoleCreated? = null,
  @SerialName("role.updated")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val roleUpdated: RoleUpdated? = null,
  @SerialName("role.deleted")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val roleDeleted: RoleDeleted? = null,
  @SerialName("role.assignment.created")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val roleAssignmentCreated: RoleAssignmentCreated? = null,
  @SerialName("role.assignment.deleted")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val roleAssignmentDeleted: RoleAssignmentDeleted? = null,
  @SerialName("service_account.created")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val serviceAccountCreated: ServiceAccountCreated? = null,
  @SerialName("service_account.updated")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val serviceAccountUpdated: ServiceAccountUpdated? = null,
  @SerialName("service_account.deleted")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val serviceAccountDeleted: ServiceAccountDeleted? = null,
  @SerialName("user.added")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val userAdded: UserAdded? = null,
  @SerialName("user.updated")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val userUpdated: UserUpdated? = null,
  @SerialName("user.deleted")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val userDeleted: UserDeleted? = null,
  @SerialName("certificate.created")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val certificateCreated: CertificateCreated? = null,
  @SerialName("certificate.updated")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val certificateUpdated: CertificateUpdated? = null,
  @SerialName("certificate.deleted")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val certificateDeleted: CertificateDeleted? = null,
  @SerialName("certificates.activated")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val certificatesActivated: CertificatesActivated? = null,
  @SerialName("certificates.deactivated")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val certificatesDeactivated: CertificatesDeactivated? = null,
) {
  /**
   * The details for events with this `type`.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class ApiKeyCreated(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `data`: Data? = null,
  ) {
    /**
     * The payload used to create the API key.
     */
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class Data(
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val scopes: List<String>? = null,
    )
  }

  /**
   * The details for events with this `type`.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class ApiKeyDeleted(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val id: String? = null,
  )

  /**
   * The details for events with this `type`.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class ApiKeyUpdated(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val id: String? = null,
    @SerialName("changes_requested")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val changesRequested: ChangesRequested? = null,
  ) {
    /**
     * The payload used to update the API key.
     */
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class ChangesRequested(
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val scopes: List<String>? = null,
    )
  }

  /**
   * The details for events with this `type`.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class CertificateCreated(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val name: String? = null,
  )

  /**
   * The details for events with this `type`.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class CertificateDeleted(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val certificate: String? = null,
  )

  /**
   * The details for events with this `type`.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class CertificateUpdated(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val name: String? = null,
  )

  /**
   * The details for events with this `type`.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class CertificatesActivated(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val certificates: List<Certificates>? = null,
  ) {
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class Certificates(
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val id: String? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val name: String? = null,
    )
  }

  /**
   * The details for events with this `type`.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class CertificatesDeactivated(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val certificates: List<Certificates>? = null,
  ) {
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class Certificates(
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val id: String? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val name: String? = null,
    )
  }

  /**
   * The project and fine-tuned model checkpoint that the checkpoint permission was created for.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class CheckpointPermissionCreated(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `data`: Data? = null,
  ) {
    /**
     * The payload used to create the checkpoint permission.
     */
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class Data(
      @SerialName("project_id")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val projectId: String? = null,
      @SerialName("fine_tuned_model_checkpoint")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val fineTunedModelCheckpoint: String? = null,
    )
  }

  /**
   * The details for events with this `type`.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class CheckpointPermissionDeleted(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val id: String? = null,
  )

  /**
   * The details for events with this `type`.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class ExternalKeyRegistered(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `data`: JsonElement? = null,
  )

  /**
   * The details for events with this `type`.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class ExternalKeyRemoved(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val id: String? = null,
  )

  /**
   * The details for events with this `type`.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class GroupCreated(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `data`: Data? = null,
  ) {
    /**
     * Information about the created group.
     */
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class Data(
      @SerialName("group_name")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val groupName: String? = null,
    )
  }

  /**
   * The details for events with this `type`.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class GroupDeleted(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val id: String? = null,
  )

  /**
   * The details for events with this `type`.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class GroupUpdated(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val id: String? = null,
    @SerialName("changes_requested")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val changesRequested: ChangesRequested? = null,
  ) {
    /**
     * The payload used to update the group.
     */
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class ChangesRequested(
      @SerialName("group_name")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val groupName: String? = null,
    )
  }

  /**
   * The details for events with this `type`.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class InviteAccepted(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val id: String? = null,
  )

  /**
   * The details for events with this `type`.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class InviteDeleted(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val id: String? = null,
  )

  /**
   * The details for events with this `type`.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class InviteSent(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `data`: Data? = null,
  ) {
    /**
     * The payload used to create the invite.
     */
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class Data(
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val email: String? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val role: String? = null,
    )
  }

  /**
   * The details for events with this `type`.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class IpAllowlistConfigActivated(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val configs: List<Configs>? = null,
  ) {
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class Configs(
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val id: String? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val name: String? = null,
    )
  }

  /**
   * The details for events with this `type`.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class IpAllowlistConfigDeactivated(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val configs: List<Configs>? = null,
  ) {
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class Configs(
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val id: String? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val name: String? = null,
    )
  }

  /**
   * The details for events with this `type`.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class IpAllowlistCreated(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val name: String? = null,
    @SerialName("allowed_ips")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val allowedIps: List<String>? = null,
  )

  /**
   * The details for events with this `type`.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class IpAllowlistDeleted(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val name: String? = null,
    @SerialName("allowed_ips")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val allowedIps: List<String>? = null,
  )

  /**
   * The details for events with this `type`.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class IpAllowlistUpdated(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val id: String? = null,
    @SerialName("allowed_ips")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val allowedIps: List<String>? = null,
  )

  /**
   * The details for events with this `type`.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class LoginFailed(
    @SerialName("error_code")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val errorCode: String? = null,
    @SerialName("error_message")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val errorMessage: String? = null,
  )

  /**
   * The details for events with this `type`.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class LogoutFailed(
    @SerialName("error_code")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val errorCode: String? = null,
    @SerialName("error_message")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val errorMessage: String? = null,
  )

  /**
   * The details for events with this `type`.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class OrganizationUpdated(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val id: String? = null,
    @SerialName("changes_requested")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val changesRequested: ChangesRequested? = null,
  ) {
    /**
     * The payload used to update the organization settings.
     */
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class ChangesRequested(
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val title: String? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val description: String? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val name: String? = null,
      @SerialName("threads_ui_visibility")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val threadsUiVisibility: String? = null,
      @SerialName("usage_dashboard_visibility")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val usageDashboardVisibility: String? = null,
      @SerialName("api_call_logging")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val apiCallLogging: String? = null,
      @SerialName("api_call_logging_project_ids")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val apiCallLoggingProjectIds: String? = null,
    )
  }

  /**
   * The project that the action was scoped to. Absent for actions not scoped to projects. Note that any admin actions taken via Admin API keys are associated with the default project.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Project(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val name: String? = null,
  )

  /**
   * The details for events with this `type`.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class ProjectArchived(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val id: String? = null,
  )

  /**
   * The details for events with this `type`.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class ProjectCreated(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `data`: Data? = null,
  ) {
    /**
     * The payload used to create the project.
     */
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class Data(
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val name: String? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val title: String? = null,
    )
  }

  /**
   * The details for events with this `type`.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class ProjectDeleted(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val id: String? = null,
  )

  /**
   * The details for events with this `type`.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class ProjectUpdated(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val id: String? = null,
    @SerialName("changes_requested")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val changesRequested: ChangesRequested? = null,
  ) {
    /**
     * The payload used to update the project.
     */
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class ChangesRequested(
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val title: String? = null,
    )
  }

  /**
   * The details for events with this `type`.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class RateLimitDeleted(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val id: String? = null,
  )

  /**
   * The details for events with this `type`.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class RateLimitUpdated(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val id: String? = null,
    @SerialName("changes_requested")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val changesRequested: ChangesRequested? = null,
  ) {
    /**
     * The payload used to update the rate limits.
     */
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class ChangesRequested(
      @SerialName("max_requests_per_1_minute")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val maxRequestsPer1Minute: Long? = null,
      @SerialName("max_tokens_per_1_minute")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val maxTokensPer1Minute: Long? = null,
      @SerialName("max_images_per_1_minute")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val maxImagesPer1Minute: Long? = null,
      @SerialName("max_audio_megabytes_per_1_minute")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val maxAudioMegabytesPer1Minute: Long? = null,
      @SerialName("max_requests_per_1_day")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val maxRequestsPer1Day: Long? = null,
      @SerialName("batch_1_day_max_input_tokens")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val batch1DayMaxInputTokens: Long? = null,
    )
  }

  /**
   * The details for events with this `type`.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class RoleAssignmentCreated(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val id: String? = null,
    @SerialName("principal_id")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val principalId: String? = null,
    @SerialName("principal_type")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val principalType: String? = null,
    @SerialName("resource_id")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val resourceId: String? = null,
    @SerialName("resource_type")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val resourceType: String? = null,
  )

  /**
   * The details for events with this `type`.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class RoleAssignmentDeleted(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val id: String? = null,
    @SerialName("principal_id")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val principalId: String? = null,
    @SerialName("principal_type")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val principalType: String? = null,
    @SerialName("resource_id")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val resourceId: String? = null,
    @SerialName("resource_type")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val resourceType: String? = null,
  )

  /**
   * The details for events with this `type`.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class RoleCreated(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val id: String? = null,
    @SerialName("role_name")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val roleName: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val permissions: List<String>? = null,
    @SerialName("resource_type")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val resourceType: String? = null,
    @SerialName("resource_id")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val resourceId: String? = null,
  )

  /**
   * The details for events with this `type`.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class RoleDeleted(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val id: String? = null,
  )

  /**
   * The details for events with this `type`.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class RoleUpdated(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val id: String? = null,
    @SerialName("changes_requested")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val changesRequested: ChangesRequested? = null,
  ) {
    /**
     * The payload used to update the role.
     */
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class ChangesRequested(
      @SerialName("role_name")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val roleName: String? = null,
      @SerialName("resource_id")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val resourceId: String? = null,
      @SerialName("resource_type")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val resourceType: String? = null,
      @SerialName("permissions_added")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val permissionsAdded: List<String>? = null,
      @SerialName("permissions_removed")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val permissionsRemoved: List<String>? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val description: String? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val metadata: JsonElement? = null,
    )
  }

  /**
   * The details for events with this `type`.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class ScimDisabled(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val id: String? = null,
  )

  /**
   * The details for events with this `type`.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class ScimEnabled(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val id: String? = null,
  )

  /**
   * The details for events with this `type`.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class ServiceAccountCreated(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `data`: Data? = null,
  ) {
    /**
     * The payload used to create the service account.
     */
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class Data(
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val role: String? = null,
    )
  }

  /**
   * The details for events with this `type`.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class ServiceAccountDeleted(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val id: String? = null,
  )

  /**
   * The details for events with this `type`.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class ServiceAccountUpdated(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val id: String? = null,
    @SerialName("changes_requested")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val changesRequested: ChangesRequested? = null,
  ) {
    /**
     * The payload used to updated the service account.
     */
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class ChangesRequested(
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val role: String? = null,
    )
  }

  /**
   * The details for events with this `type`.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class UserAdded(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `data`: Data? = null,
  ) {
    /**
     * The payload used to add the user to the project.
     */
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class Data(
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val role: String? = null,
    )
  }

  /**
   * The details for events with this `type`.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class UserDeleted(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val id: String? = null,
  )

  /**
   * The details for events with this `type`.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class UserUpdated(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val id: String? = null,
    @SerialName("changes_requested")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val changesRequested: ChangesRequested? = null,
  ) {
    /**
     * The payload used to update the user.
     */
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class ChangesRequested(
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val role: String? = null,
    )
  }
}
