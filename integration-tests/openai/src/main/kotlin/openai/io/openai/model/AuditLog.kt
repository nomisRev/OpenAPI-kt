package io.openai.model

import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * A log of a user action or configuration change within this organization.
 */
@Serializable
public data class AuditLog(
  public val id: String,
  public val type: AuditLogEventType,
  @SerialName("effective_at")
  public val effectiveAt: Long,
  public val project: Project? = null,
  public val actor: AuditLogActor,
  @SerialName("api_key.created")
  public val apiKeyCreated: ApiKeyCreated? = null,
  @SerialName("api_key.updated")
  public val apiKeyUpdated: ApiKeyUpdated? = null,
  @SerialName("api_key.deleted")
  public val apiKeyDeleted: ApiKeyDeleted? = null,
  @SerialName("checkpoint.permission.created")
  public val checkpointPermissionCreated: CheckpointPermissionCreated? = null,
  @SerialName("checkpoint.permission.deleted")
  public val checkpointPermissionDeleted: CheckpointPermissionDeleted? = null,
  @SerialName("external_key.registered")
  public val externalKeyRegistered: ExternalKeyRegistered? = null,
  @SerialName("external_key.removed")
  public val externalKeyRemoved: ExternalKeyRemoved? = null,
  @SerialName("group.created")
  public val groupCreated: GroupCreated? = null,
  @SerialName("group.updated")
  public val groupUpdated: GroupUpdated? = null,
  @SerialName("group.deleted")
  public val groupDeleted: GroupDeleted? = null,
  @SerialName("scim.enabled")
  public val scimEnabled: ScimEnabled? = null,
  @SerialName("scim.disabled")
  public val scimDisabled: ScimDisabled? = null,
  @SerialName("invite.sent")
  public val inviteSent: InviteSent? = null,
  @SerialName("invite.accepted")
  public val inviteAccepted: InviteAccepted? = null,
  @SerialName("invite.deleted")
  public val inviteDeleted: InviteDeleted? = null,
  @SerialName("ip_allowlist.created")
  public val ipAllowlistCreated: IpAllowlistCreated? = null,
  @SerialName("ip_allowlist.updated")
  public val ipAllowlistUpdated: IpAllowlistUpdated? = null,
  @SerialName("ip_allowlist.deleted")
  public val ipAllowlistDeleted: IpAllowlistDeleted? = null,
  @SerialName("ip_allowlist.config.activated")
  public val ipAllowlistConfigActivated: IpAllowlistConfigActivated? = null,
  @SerialName("ip_allowlist.config.deactivated")
  public val ipAllowlistConfigDeactivated: IpAllowlistConfigDeactivated? = null,
  @SerialName("login.succeeded")
  public val loginSucceeded: JsonElement? = null,
  @SerialName("login.failed")
  public val loginFailed: LoginFailed? = null,
  @SerialName("logout.succeeded")
  public val logoutSucceeded: JsonElement? = null,
  @SerialName("logout.failed")
  public val logoutFailed: LogoutFailed? = null,
  @SerialName("organization.updated")
  public val organizationUpdated: OrganizationUpdated? = null,
  @SerialName("project.created")
  public val projectCreated: ProjectCreated? = null,
  @SerialName("project.updated")
  public val projectUpdated: ProjectUpdated? = null,
  @SerialName("project.archived")
  public val projectArchived: ProjectArchived? = null,
  @SerialName("project.deleted")
  public val projectDeleted: ProjectDeleted? = null,
  @SerialName("rate_limit.updated")
  public val rateLimitUpdated: RateLimitUpdated? = null,
  @SerialName("rate_limit.deleted")
  public val rateLimitDeleted: RateLimitDeleted? = null,
  @SerialName("role.created")
  public val roleCreated: RoleCreated? = null,
  @SerialName("role.updated")
  public val roleUpdated: RoleUpdated? = null,
  @SerialName("role.deleted")
  public val roleDeleted: RoleDeleted? = null,
  @SerialName("role.assignment.created")
  public val roleAssignmentCreated: RoleAssignmentCreated? = null,
  @SerialName("role.assignment.deleted")
  public val roleAssignmentDeleted: RoleAssignmentDeleted? = null,
  @SerialName("service_account.created")
  public val serviceAccountCreated: ServiceAccountCreated? = null,
  @SerialName("service_account.updated")
  public val serviceAccountUpdated: ServiceAccountUpdated? = null,
  @SerialName("service_account.deleted")
  public val serviceAccountDeleted: ServiceAccountDeleted? = null,
  @SerialName("user.added")
  public val userAdded: UserAdded? = null,
  @SerialName("user.updated")
  public val userUpdated: UserUpdated? = null,
  @SerialName("user.deleted")
  public val userDeleted: UserDeleted? = null,
  @SerialName("certificate.created")
  public val certificateCreated: CertificateCreated? = null,
  @SerialName("certificate.updated")
  public val certificateUpdated: CertificateUpdated? = null,
  @SerialName("certificate.deleted")
  public val certificateDeleted: CertificateDeleted? = null,
  @SerialName("certificates.activated")
  public val certificatesActivated: CertificatesActivated? = null,
  @SerialName("certificates.deactivated")
  public val certificatesDeactivated: CertificatesDeactivated? = null,
) {
  /**
   * The details for events with this `type`.
   */
  @Serializable
  public data class ApiKeyCreated(
    public val id: String? = null,
    public val `data`: Data? = null,
  ) {
    /**
     * The payload used to create the API key.
     */
    @JvmInline
    @Serializable
    public value class Data(
      public val scopes: List<String>? = null,
    )
  }

  /**
   * The details for events with this `type`.
   */
  @JvmInline
  @Serializable
  public value class ApiKeyDeleted(
    public val id: String? = null,
  )

  /**
   * The details for events with this `type`.
   */
  @Serializable
  public data class ApiKeyUpdated(
    public val id: String? = null,
    @SerialName("changes_requested")
    public val changesRequested: ChangesRequested? = null,
  ) {
    /**
     * The payload used to update the API key.
     */
    @JvmInline
    @Serializable
    public value class ChangesRequested(
      public val scopes: List<String>? = null,
    )
  }

  /**
   * The details for events with this `type`.
   */
  @Serializable
  public data class CertificateCreated(
    public val id: String? = null,
    public val name: String? = null,
  )

  /**
   * The details for events with this `type`.
   */
  @Serializable
  public data class CertificateDeleted(
    public val id: String? = null,
    public val name: String? = null,
    public val certificate: String? = null,
  )

  /**
   * The details for events with this `type`.
   */
  @Serializable
  public data class CertificateUpdated(
    public val id: String? = null,
    public val name: String? = null,
  )

  /**
   * The details for events with this `type`.
   */
  @JvmInline
  @Serializable
  public value class CertificatesActivated(
    public val certificates: List<Certificates>? = null,
  ) {
    @Serializable
    public data class Certificates(
      public val id: String? = null,
      public val name: String? = null,
    )
  }

  /**
   * The details for events with this `type`.
   */
  @JvmInline
  @Serializable
  public value class CertificatesDeactivated(
    public val certificates: List<Certificates>? = null,
  ) {
    @Serializable
    public data class Certificates(
      public val id: String? = null,
      public val name: String? = null,
    )
  }

  /**
   * The project and fine-tuned model checkpoint that the checkpoint permission was created for.
   */
  @Serializable
  public data class CheckpointPermissionCreated(
    public val id: String? = null,
    public val `data`: Data? = null,
  ) {
    /**
     * The payload used to create the checkpoint permission.
     */
    @Serializable
    public data class Data(
      @SerialName("project_id")
      public val projectId: String? = null,
      @SerialName("fine_tuned_model_checkpoint")
      public val fineTunedModelCheckpoint: String? = null,
    )
  }

  /**
   * The details for events with this `type`.
   */
  @JvmInline
  @Serializable
  public value class CheckpointPermissionDeleted(
    public val id: String? = null,
  )

  /**
   * The details for events with this `type`.
   */
  @Serializable
  public data class ExternalKeyRegistered(
    public val id: String? = null,
    public val `data`: JsonElement? = null,
  )

  /**
   * The details for events with this `type`.
   */
  @JvmInline
  @Serializable
  public value class ExternalKeyRemoved(
    public val id: String? = null,
  )

  /**
   * The details for events with this `type`.
   */
  @Serializable
  public data class GroupCreated(
    public val id: String? = null,
    public val `data`: Data? = null,
  ) {
    /**
     * Information about the created group.
     */
    @JvmInline
    @Serializable
    public value class Data(
      @SerialName("group_name")
      public val groupName: String? = null,
    )
  }

  /**
   * The details for events with this `type`.
   */
  @JvmInline
  @Serializable
  public value class GroupDeleted(
    public val id: String? = null,
  )

  /**
   * The details for events with this `type`.
   */
  @Serializable
  public data class GroupUpdated(
    public val id: String? = null,
    @SerialName("changes_requested")
    public val changesRequested: ChangesRequested? = null,
  ) {
    /**
     * The payload used to update the group.
     */
    @JvmInline
    @Serializable
    public value class ChangesRequested(
      @SerialName("group_name")
      public val groupName: String? = null,
    )
  }

  /**
   * The details for events with this `type`.
   */
  @JvmInline
  @Serializable
  public value class InviteAccepted(
    public val id: String? = null,
  )

  /**
   * The details for events with this `type`.
   */
  @JvmInline
  @Serializable
  public value class InviteDeleted(
    public val id: String? = null,
  )

  /**
   * The details for events with this `type`.
   */
  @Serializable
  public data class InviteSent(
    public val id: String? = null,
    public val `data`: Data? = null,
  ) {
    /**
     * The payload used to create the invite.
     */
    @Serializable
    public data class Data(
      public val email: String? = null,
      public val role: String? = null,
    )
  }

  /**
   * The details for events with this `type`.
   */
  @JvmInline
  @Serializable
  public value class IpAllowlistConfigActivated(
    public val configs: List<Configs>? = null,
  ) {
    @Serializable
    public data class Configs(
      public val id: String? = null,
      public val name: String? = null,
    )
  }

  /**
   * The details for events with this `type`.
   */
  @JvmInline
  @Serializable
  public value class IpAllowlistConfigDeactivated(
    public val configs: List<Configs>? = null,
  ) {
    @Serializable
    public data class Configs(
      public val id: String? = null,
      public val name: String? = null,
    )
  }

  /**
   * The details for events with this `type`.
   */
  @Serializable
  public data class IpAllowlistCreated(
    public val id: String? = null,
    public val name: String? = null,
    @SerialName("allowed_ips")
    public val allowedIps: List<String>? = null,
  )

  /**
   * The details for events with this `type`.
   */
  @Serializable
  public data class IpAllowlistDeleted(
    public val id: String? = null,
    public val name: String? = null,
    @SerialName("allowed_ips")
    public val allowedIps: List<String>? = null,
  )

  /**
   * The details for events with this `type`.
   */
  @Serializable
  public data class IpAllowlistUpdated(
    public val id: String? = null,
    @SerialName("allowed_ips")
    public val allowedIps: List<String>? = null,
  )

  /**
   * The details for events with this `type`.
   */
  @Serializable
  public data class LoginFailed(
    @SerialName("error_code")
    public val errorCode: String? = null,
    @SerialName("error_message")
    public val errorMessage: String? = null,
  )

  /**
   * The details for events with this `type`.
   */
  @Serializable
  public data class LogoutFailed(
    @SerialName("error_code")
    public val errorCode: String? = null,
    @SerialName("error_message")
    public val errorMessage: String? = null,
  )

  /**
   * The details for events with this `type`.
   */
  @Serializable
  public data class OrganizationUpdated(
    public val id: String? = null,
    @SerialName("changes_requested")
    public val changesRequested: ChangesRequested? = null,
  ) {
    /**
     * The payload used to update the organization settings.
     */
    @Serializable
    public data class ChangesRequested(
      public val title: String? = null,
      public val description: String? = null,
      public val name: String? = null,
      @SerialName("threads_ui_visibility")
      public val threadsUiVisibility: String? = null,
      @SerialName("usage_dashboard_visibility")
      public val usageDashboardVisibility: String? = null,
      @SerialName("api_call_logging")
      public val apiCallLogging: String? = null,
      @SerialName("api_call_logging_project_ids")
      public val apiCallLoggingProjectIds: String? = null,
    )
  }

  /**
   * The project that the action was scoped to. Absent for actions not scoped to projects. Note that any admin actions taken via Admin API keys are associated with the default project.
   */
  @Serializable
  public data class Project(
    public val id: String? = null,
    public val name: String? = null,
  )

  /**
   * The details for events with this `type`.
   */
  @JvmInline
  @Serializable
  public value class ProjectArchived(
    public val id: String? = null,
  )

  /**
   * The details for events with this `type`.
   */
  @Serializable
  public data class ProjectCreated(
    public val id: String? = null,
    public val `data`: Data? = null,
  ) {
    /**
     * The payload used to create the project.
     */
    @Serializable
    public data class Data(
      public val name: String? = null,
      public val title: String? = null,
    )
  }

  /**
   * The details for events with this `type`.
   */
  @JvmInline
  @Serializable
  public value class ProjectDeleted(
    public val id: String? = null,
  )

  /**
   * The details for events with this `type`.
   */
  @Serializable
  public data class ProjectUpdated(
    public val id: String? = null,
    @SerialName("changes_requested")
    public val changesRequested: ChangesRequested? = null,
  ) {
    /**
     * The payload used to update the project.
     */
    @JvmInline
    @Serializable
    public value class ChangesRequested(
      public val title: String? = null,
    )
  }

  /**
   * The details for events with this `type`.
   */
  @JvmInline
  @Serializable
  public value class RateLimitDeleted(
    public val id: String? = null,
  )

  /**
   * The details for events with this `type`.
   */
  @Serializable
  public data class RateLimitUpdated(
    public val id: String? = null,
    @SerialName("changes_requested")
    public val changesRequested: ChangesRequested? = null,
  ) {
    /**
     * The payload used to update the rate limits.
     */
    @Serializable
    public data class ChangesRequested(
      @SerialName("max_requests_per_1_minute")
      public val maxRequestsPer1Minute: Long? = null,
      @SerialName("max_tokens_per_1_minute")
      public val maxTokensPer1Minute: Long? = null,
      @SerialName("max_images_per_1_minute")
      public val maxImagesPer1Minute: Long? = null,
      @SerialName("max_audio_megabytes_per_1_minute")
      public val maxAudioMegabytesPer1Minute: Long? = null,
      @SerialName("max_requests_per_1_day")
      public val maxRequestsPer1Day: Long? = null,
      @SerialName("batch_1_day_max_input_tokens")
      public val batch1DayMaxInputTokens: Long? = null,
    )
  }

  /**
   * The details for events with this `type`.
   */
  @Serializable
  public data class RoleAssignmentCreated(
    public val id: String? = null,
    @SerialName("principal_id")
    public val principalId: String? = null,
    @SerialName("principal_type")
    public val principalType: String? = null,
    @SerialName("resource_id")
    public val resourceId: String? = null,
    @SerialName("resource_type")
    public val resourceType: String? = null,
  )

  /**
   * The details for events with this `type`.
   */
  @Serializable
  public data class RoleAssignmentDeleted(
    public val id: String? = null,
    @SerialName("principal_id")
    public val principalId: String? = null,
    @SerialName("principal_type")
    public val principalType: String? = null,
    @SerialName("resource_id")
    public val resourceId: String? = null,
    @SerialName("resource_type")
    public val resourceType: String? = null,
  )

  /**
   * The details for events with this `type`.
   */
  @Serializable
  public data class RoleCreated(
    public val id: String? = null,
    @SerialName("role_name")
    public val roleName: String? = null,
    public val permissions: List<String>? = null,
    @SerialName("resource_type")
    public val resourceType: String? = null,
    @SerialName("resource_id")
    public val resourceId: String? = null,
  )

  /**
   * The details for events with this `type`.
   */
  @JvmInline
  @Serializable
  public value class RoleDeleted(
    public val id: String? = null,
  )

  /**
   * The details for events with this `type`.
   */
  @Serializable
  public data class RoleUpdated(
    public val id: String? = null,
    @SerialName("changes_requested")
    public val changesRequested: ChangesRequested? = null,
  ) {
    /**
     * The payload used to update the role.
     */
    @Serializable
    public data class ChangesRequested(
      @SerialName("role_name")
      public val roleName: String? = null,
      @SerialName("resource_id")
      public val resourceId: String? = null,
      @SerialName("resource_type")
      public val resourceType: String? = null,
      @SerialName("permissions_added")
      public val permissionsAdded: List<String>? = null,
      @SerialName("permissions_removed")
      public val permissionsRemoved: List<String>? = null,
      public val description: String? = null,
      public val metadata: JsonElement? = null,
    )
  }

  /**
   * The details for events with this `type`.
   */
  @JvmInline
  @Serializable
  public value class ScimDisabled(
    public val id: String? = null,
  )

  /**
   * The details for events with this `type`.
   */
  @JvmInline
  @Serializable
  public value class ScimEnabled(
    public val id: String? = null,
  )

  /**
   * The details for events with this `type`.
   */
  @Serializable
  public data class ServiceAccountCreated(
    public val id: String? = null,
    public val `data`: Data? = null,
  ) {
    /**
     * The payload used to create the service account.
     */
    @JvmInline
    @Serializable
    public value class Data(
      public val role: String? = null,
    )
  }

  /**
   * The details for events with this `type`.
   */
  @JvmInline
  @Serializable
  public value class ServiceAccountDeleted(
    public val id: String? = null,
  )

  /**
   * The details for events with this `type`.
   */
  @Serializable
  public data class ServiceAccountUpdated(
    public val id: String? = null,
    @SerialName("changes_requested")
    public val changesRequested: ChangesRequested? = null,
  ) {
    /**
     * The payload used to updated the service account.
     */
    @JvmInline
    @Serializable
    public value class ChangesRequested(
      public val role: String? = null,
    )
  }

  /**
   * The details for events with this `type`.
   */
  @Serializable
  public data class UserAdded(
    public val id: String? = null,
    public val `data`: Data? = null,
  ) {
    /**
     * The payload used to add the user to the project.
     */
    @JvmInline
    @Serializable
    public value class Data(
      public val role: String? = null,
    )
  }

  /**
   * The details for events with this `type`.
   */
  @JvmInline
  @Serializable
  public value class UserDeleted(
    public val id: String? = null,
  )

  /**
   * The details for events with this `type`.
   */
  @Serializable
  public data class UserUpdated(
    public val id: String? = null,
    @SerialName("changes_requested")
    public val changesRequested: ChangesRequested? = null,
  ) {
    /**
     * The payload used to update the user.
     */
    @JvmInline
    @Serializable
    public value class ChangesRequested(
      public val role: String? = null,
    )
  }
}
