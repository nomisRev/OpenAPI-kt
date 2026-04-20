package io.github.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The permissions granted to the user access token.
 */
@Serializable
public data class AppPermissionsWrite(
  public val actions: Actions? = null,
  public val administration: Administration? = null,
  @SerialName("artifact_metadata")
  public val artifactMetadata: ArtifactMetadata? = null,
  public val attestations: Attestations? = null,
  public val checks: Checks? = null,
  public val codespaces: Codespaces? = null,
  public val contents: Contents? = null,
  @SerialName("dependabot_secrets")
  public val dependabotSecrets: DependabotSecrets? = null,
  public val deployments: Deployments? = null,
  public val discussions: Discussions? = null,
  public val environments: Environments? = null,
  public val issues: Issues? = null,
  @SerialName("merge_queues")
  public val mergeQueues: MergeQueues? = null,
  public val metadata: Metadata? = null,
  public val packages: Packages? = null,
  public val pages: Pages? = null,
  @SerialName("pull_requests")
  public val pullRequests: PullRequests? = null,
  @SerialName("repository_custom_properties")
  public val repositoryCustomProperties: RepositoryCustomProperties? = null,
  @SerialName("repository_hooks")
  public val repositoryHooks: RepositoryHooks? = null,
  @SerialName("repository_projects")
  public val repositoryProjects: RepositoryProjects? = null,
  @SerialName("secret_scanning_alerts")
  public val secretScanningAlerts: SecretScanningAlerts? = null,
  public val secrets: Secrets? = null,
  @SerialName("security_events")
  public val securityEvents: SecurityEvents? = null,
  @SerialName("single_file")
  public val singleFile: SingleFile? = null,
  public val statuses: Statuses? = null,
  @SerialName("vulnerability_alerts")
  public val vulnerabilityAlerts: VulnerabilityAlerts? = null,
  public val workflows: Workflows? = null,
  @SerialName("custom_properties_for_organizations")
  public val customPropertiesForOrganizations: CustomPropertiesForOrganizations? = null,
  public val members: Members? = null,
  @SerialName("organization_administration")
  public val organizationAdministration: OrganizationAdministration? = null,
  @SerialName("organization_custom_roles")
  public val organizationCustomRoles: OrganizationCustomRoles? = null,
  @SerialName("organization_custom_org_roles")
  public val organizationCustomOrgRoles: OrganizationCustomOrgRoles? = null,
  @SerialName("organization_custom_properties")
  public val organizationCustomProperties: OrganizationCustomProperties? = null,
  @SerialName("organization_copilot_seat_management")
  public val organizationCopilotSeatManagement: OrganizationCopilotSeatManagement? = null,
  @SerialName("organization_announcement_banners")
  public val organizationAnnouncementBanners: OrganizationAnnouncementBanners? = null,
  @SerialName("organization_events")
  public val organizationEvents: OrganizationEvents? = null,
  @SerialName("organization_hooks")
  public val organizationHooks: OrganizationHooks? = null,
  @SerialName("organization_personal_access_tokens")
  public val organizationPersonalAccessTokens: OrganizationPersonalAccessTokens? = null,
  @SerialName("organization_personal_access_token_requests")
  public val organizationPersonalAccessTokenRequests:
      OrganizationPersonalAccessTokenRequests? = null,
  @SerialName("organization_plan")
  public val organizationPlan: OrganizationPlan? = null,
  @SerialName("organization_projects")
  public val organizationProjects: OrganizationProjects? = null,
  @SerialName("organization_packages")
  public val organizationPackages: OrganizationPackages? = null,
  @SerialName("organization_secrets")
  public val organizationSecrets: OrganizationSecrets? = null,
  @SerialName("organization_self_hosted_runners")
  public val organizationSelfHostedRunners: OrganizationSelfHostedRunners? = null,
  @SerialName("organization_user_blocking")
  public val organizationUserBlocking: OrganizationUserBlocking? = null,
  @SerialName("email_addresses")
  public val emailAddresses: EmailAddresses? = null,
  public val followers: Followers? = null,
  @SerialName("git_ssh_keys")
  public val gitSshKeys: GitSshKeys? = null,
  @SerialName("gpg_keys")
  public val gpgKeys: GpgKeys? = null,
  @SerialName("interaction_limits")
  public val interactionLimits: InteractionLimits? = null,
  public val profile: Profile? = null,
  public val starring: Starring? = null,
  @SerialName("enterprise_custom_properties_for_organizations")
  public val enterpriseCustomPropertiesForOrganizations:
      EnterpriseCustomPropertiesForOrganizations? = null,
) {
  @Serializable
  public enum class Actions(
    public val `value`: String,
  ) {
    @SerialName("read")
    Read("read"),
    @SerialName("write")
    Write("write"),
    ;
  }

  @Serializable
  public enum class Administration(
    public val `value`: String,
  ) {
    @SerialName("read")
    Read("read"),
    @SerialName("write")
    Write("write"),
    ;
  }

  @Serializable
  public enum class ArtifactMetadata(
    public val `value`: String,
  ) {
    @SerialName("read")
    Read("read"),
    @SerialName("write")
    Write("write"),
    ;
  }

  @Serializable
  public enum class Attestations(
    public val `value`: String,
  ) {
    @SerialName("read")
    Read("read"),
    @SerialName("write")
    Write("write"),
    ;
  }

  @Serializable
  public enum class Checks(
    public val `value`: String,
  ) {
    @SerialName("read")
    Read("read"),
    @SerialName("write")
    Write("write"),
    ;
  }

  @Serializable
  public enum class Codespaces(
    public val `value`: String,
  ) {
    @SerialName("read")
    Read("read"),
    @SerialName("write")
    Write("write"),
    ;
  }

  @Serializable
  public enum class Contents(
    public val `value`: String,
  ) {
    @SerialName("read")
    Read("read"),
    @SerialName("write")
    Write("write"),
    ;
  }

  @Serializable
  public enum class CustomPropertiesForOrganizations(
    public val `value`: String,
  ) {
    @SerialName("read")
    Read("read"),
    @SerialName("write")
    Write("write"),
    ;
  }

  @Serializable
  public enum class DependabotSecrets(
    public val `value`: String,
  ) {
    @SerialName("read")
    Read("read"),
    @SerialName("write")
    Write("write"),
    ;
  }

  @Serializable
  public enum class Deployments(
    public val `value`: String,
  ) {
    @SerialName("read")
    Read("read"),
    @SerialName("write")
    Write("write"),
    ;
  }

  @Serializable
  public enum class Discussions(
    public val `value`: String,
  ) {
    @SerialName("read")
    Read("read"),
    @SerialName("write")
    Write("write"),
    ;
  }

  @Serializable
  public enum class EmailAddresses(
    public val `value`: String,
  ) {
    @SerialName("read")
    Read("read"),
    @SerialName("write")
    Write("write"),
    ;
  }

  @Serializable
  public enum class EnterpriseCustomPropertiesForOrganizations(
    public val `value`: String,
  ) {
    @SerialName("read")
    Read("read"),
    @SerialName("write")
    Write("write"),
    @SerialName("admin")
    Admin("admin"),
    ;
  }

  @Serializable
  public enum class Environments(
    public val `value`: String,
  ) {
    @SerialName("read")
    Read("read"),
    @SerialName("write")
    Write("write"),
    ;
  }

  @Serializable
  public enum class Followers(
    public val `value`: String,
  ) {
    @SerialName("read")
    Read("read"),
    @SerialName("write")
    Write("write"),
    ;
  }

  @Serializable
  public enum class GitSshKeys(
    public val `value`: String,
  ) {
    @SerialName("read")
    Read("read"),
    @SerialName("write")
    Write("write"),
    ;
  }

  @Serializable
  public enum class GpgKeys(
    public val `value`: String,
  ) {
    @SerialName("read")
    Read("read"),
    @SerialName("write")
    Write("write"),
    ;
  }

  @Serializable
  public enum class InteractionLimits(
    public val `value`: String,
  ) {
    @SerialName("read")
    Read("read"),
    @SerialName("write")
    Write("write"),
    ;
  }

  @Serializable
  public enum class Issues(
    public val `value`: String,
  ) {
    @SerialName("read")
    Read("read"),
    @SerialName("write")
    Write("write"),
    ;
  }

  @Serializable
  public enum class Members(
    public val `value`: String,
  ) {
    @SerialName("read")
    Read("read"),
    @SerialName("write")
    Write("write"),
    ;
  }

  @Serializable
  public enum class MergeQueues(
    public val `value`: String,
  ) {
    @SerialName("read")
    Read("read"),
    @SerialName("write")
    Write("write"),
    ;
  }

  @Serializable
  public enum class Metadata(
    public val `value`: String,
  ) {
    @SerialName("read")
    Read("read"),
    @SerialName("write")
    Write("write"),
    ;
  }

  @Serializable
  public enum class OrganizationAdministration(
    public val `value`: String,
  ) {
    @SerialName("read")
    Read("read"),
    @SerialName("write")
    Write("write"),
    ;
  }

  @Serializable
  public enum class OrganizationAnnouncementBanners(
    public val `value`: String,
  ) {
    @SerialName("read")
    Read("read"),
    @SerialName("write")
    Write("write"),
    ;
  }

  @Serializable
  public enum class OrganizationCopilotSeatManagement(
    public val `value`: String,
  ) {
    @SerialName("write")
    Write("write"),
    ;
  }

  @Serializable
  public enum class OrganizationCustomOrgRoles(
    public val `value`: String,
  ) {
    @SerialName("read")
    Read("read"),
    @SerialName("write")
    Write("write"),
    ;
  }

  @Serializable
  public enum class OrganizationCustomProperties(
    public val `value`: String,
  ) {
    @SerialName("read")
    Read("read"),
    @SerialName("write")
    Write("write"),
    @SerialName("admin")
    Admin("admin"),
    ;
  }

  @Serializable
  public enum class OrganizationCustomRoles(
    public val `value`: String,
  ) {
    @SerialName("read")
    Read("read"),
    @SerialName("write")
    Write("write"),
    ;
  }

  @Serializable
  public enum class OrganizationEvents(
    public val `value`: String,
  ) {
    @SerialName("read")
    Read("read"),
    ;
  }

  @Serializable
  public enum class OrganizationHooks(
    public val `value`: String,
  ) {
    @SerialName("read")
    Read("read"),
    @SerialName("write")
    Write("write"),
    ;
  }

  @Serializable
  public enum class OrganizationPackages(
    public val `value`: String,
  ) {
    @SerialName("read")
    Read("read"),
    @SerialName("write")
    Write("write"),
    ;
  }

  @Serializable
  public enum class OrganizationPersonalAccessTokenRequests(
    public val `value`: String,
  ) {
    @SerialName("read")
    Read("read"),
    @SerialName("write")
    Write("write"),
    ;
  }

  @Serializable
  public enum class OrganizationPersonalAccessTokens(
    public val `value`: String,
  ) {
    @SerialName("read")
    Read("read"),
    @SerialName("write")
    Write("write"),
    ;
  }

  @Serializable
  public enum class OrganizationPlan(
    public val `value`: String,
  ) {
    @SerialName("read")
    Read("read"),
    ;
  }

  @Serializable
  public enum class OrganizationProjects(
    public val `value`: String,
  ) {
    @SerialName("read")
    Read("read"),
    @SerialName("write")
    Write("write"),
    @SerialName("admin")
    Admin("admin"),
    ;
  }

  @Serializable
  public enum class OrganizationSecrets(
    public val `value`: String,
  ) {
    @SerialName("read")
    Read("read"),
    @SerialName("write")
    Write("write"),
    ;
  }

  @Serializable
  public enum class OrganizationSelfHostedRunners(
    public val `value`: String,
  ) {
    @SerialName("read")
    Read("read"),
    @SerialName("write")
    Write("write"),
    ;
  }

  @Serializable
  public enum class OrganizationUserBlocking(
    public val `value`: String,
  ) {
    @SerialName("read")
    Read("read"),
    @SerialName("write")
    Write("write"),
    ;
  }

  @Serializable
  public enum class Packages(
    public val `value`: String,
  ) {
    @SerialName("read")
    Read("read"),
    @SerialName("write")
    Write("write"),
    ;
  }

  @Serializable
  public enum class Pages(
    public val `value`: String,
  ) {
    @SerialName("read")
    Read("read"),
    @SerialName("write")
    Write("write"),
    ;
  }

  @Serializable
  public enum class Profile(
    public val `value`: String,
  ) {
    @SerialName("write")
    Write("write"),
    ;
  }

  @Serializable
  public enum class PullRequests(
    public val `value`: String,
  ) {
    @SerialName("read")
    Read("read"),
    @SerialName("write")
    Write("write"),
    ;
  }

  @Serializable
  public enum class RepositoryCustomProperties(
    public val `value`: String,
  ) {
    @SerialName("read")
    Read("read"),
    @SerialName("write")
    Write("write"),
    ;
  }

  @Serializable
  public enum class RepositoryHooks(
    public val `value`: String,
  ) {
    @SerialName("read")
    Read("read"),
    @SerialName("write")
    Write("write"),
    ;
  }

  @Serializable
  public enum class RepositoryProjects(
    public val `value`: String,
  ) {
    @SerialName("read")
    Read("read"),
    @SerialName("write")
    Write("write"),
    @SerialName("admin")
    Admin("admin"),
    ;
  }

  @Serializable
  public enum class SecretScanningAlerts(
    public val `value`: String,
  ) {
    @SerialName("read")
    Read("read"),
    @SerialName("write")
    Write("write"),
    ;
  }

  @Serializable
  public enum class Secrets(
    public val `value`: String,
  ) {
    @SerialName("read")
    Read("read"),
    @SerialName("write")
    Write("write"),
    ;
  }

  @Serializable
  public enum class SecurityEvents(
    public val `value`: String,
  ) {
    @SerialName("read")
    Read("read"),
    @SerialName("write")
    Write("write"),
    ;
  }

  @Serializable
  public enum class SingleFile(
    public val `value`: String,
  ) {
    @SerialName("read")
    Read("read"),
    @SerialName("write")
    Write("write"),
    ;
  }

  @Serializable
  public enum class Starring(
    public val `value`: String,
  ) {
    @SerialName("read")
    Read("read"),
    @SerialName("write")
    Write("write"),
    ;
  }

  @Serializable
  public enum class Statuses(
    public val `value`: String,
  ) {
    @SerialName("read")
    Read("read"),
    @SerialName("write")
    Write("write"),
    ;
  }

  @Serializable
  public enum class VulnerabilityAlerts(
    public val `value`: String,
  ) {
    @SerialName("read")
    Read("read"),
    @SerialName("write")
    Write("write"),
    ;
  }

  @Serializable
  public enum class Workflows(
    public val `value`: String,
  ) {
    @SerialName("write")
    Write("write"),
    ;
  }
}
