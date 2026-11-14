package io.github.model

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The permissions granted to the user access token.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class AppPermissions(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val actions: Actions? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val administration: Administration? = null,
  @SerialName("artifact_metadata")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val artifactMetadata: ArtifactMetadata? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val attestations: Attestations? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val checks: Checks? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val codespaces: Codespaces? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val contents: Contents? = null,
  @SerialName("dependabot_secrets")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val dependabotSecrets: DependabotSecrets? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val deployments: Deployments? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val discussions: Discussions? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val environments: Environments? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val issues: Issues? = null,
  @SerialName("merge_queues")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val mergeQueues: MergeQueues? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val metadata: Metadata? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val packages: Packages? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val pages: Pages? = null,
  @SerialName("pull_requests")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val pullRequests: PullRequests? = null,
  @SerialName("repository_custom_properties")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val repositoryCustomProperties: RepositoryCustomProperties? = null,
  @SerialName("repository_hooks")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val repositoryHooks: RepositoryHooks? = null,
  @SerialName("repository_projects")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val repositoryProjects: RepositoryProjects? = null,
  @SerialName("secret_scanning_alerts")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val secretScanningAlerts: SecretScanningAlerts? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val secrets: Secrets? = null,
  @SerialName("security_events")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val securityEvents: SecurityEvents? = null,
  @SerialName("single_file")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val singleFile: SingleFile? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val statuses: Statuses? = null,
  @SerialName("vulnerability_alerts")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val vulnerabilityAlerts: VulnerabilityAlerts? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val workflows: Workflows? = null,
  @SerialName("custom_properties_for_organizations")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val customPropertiesForOrganizations: CustomPropertiesForOrganizations? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val members: Members? = null,
  @SerialName("organization_administration")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val organizationAdministration: OrganizationAdministration? = null,
  @SerialName("organization_custom_roles")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val organizationCustomRoles: OrganizationCustomRoles? = null,
  @SerialName("organization_custom_org_roles")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val organizationCustomOrgRoles: OrganizationCustomOrgRoles? = null,
  @SerialName("organization_custom_properties")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val organizationCustomProperties: OrganizationCustomProperties? = null,
  @SerialName("organization_copilot_seat_management")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val organizationCopilotSeatManagement: OrganizationCopilotSeatManagement? = null,
  @SerialName("organization_announcement_banners")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val organizationAnnouncementBanners: OrganizationAnnouncementBanners? = null,
  @SerialName("organization_events")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val organizationEvents: OrganizationEvents? = null,
  @SerialName("organization_hooks")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val organizationHooks: OrganizationHooks? = null,
  @SerialName("organization_personal_access_tokens")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val organizationPersonalAccessTokens: OrganizationPersonalAccessTokens? = null,
  @SerialName("organization_personal_access_token_requests")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val organizationPersonalAccessTokenRequests:
      OrganizationPersonalAccessTokenRequests? = null,
  @SerialName("organization_plan")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val organizationPlan: OrganizationPlan? = null,
  @SerialName("organization_projects")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val organizationProjects: OrganizationProjects? = null,
  @SerialName("organization_packages")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val organizationPackages: OrganizationPackages? = null,
  @SerialName("organization_secrets")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val organizationSecrets: OrganizationSecrets? = null,
  @SerialName("organization_self_hosted_runners")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val organizationSelfHostedRunners: OrganizationSelfHostedRunners? = null,
  @SerialName("organization_user_blocking")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val organizationUserBlocking: OrganizationUserBlocking? = null,
  @SerialName("email_addresses")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val emailAddresses: EmailAddresses? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val followers: Followers? = null,
  @SerialName("git_ssh_keys")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val gitSshKeys: GitSshKeys? = null,
  @SerialName("gpg_keys")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val gpgKeys: GpgKeys? = null,
  @SerialName("interaction_limits")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val interactionLimits: InteractionLimits? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val profile: Profile? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val starring: Starring? = null,
  @SerialName("enterprise_custom_properties_for_organizations")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
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
