package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class AppPermissions(
    val actions: Actions? = null,
    val administration: Administration? = null,
    @SerialName("artifact_metadata") val artifactMetadata: ArtifactMetadata? = null,
    val attestations: Attestations? = null,
    val checks: Checks? = null,
    val codespaces: Codespaces? = null,
    val contents: Contents? = null,
    @SerialName("dependabot_secrets") val dependabotSecrets: DependabotSecrets? = null,
    val deployments: Deployments? = null,
    val discussions: Discussions? = null,
    val environments: Environments? = null,
    val issues: Issues? = null,
    @SerialName("merge_queues") val mergeQueues: MergeQueues? = null,
    val metadata: Metadata? = null,
    val packages: Packages? = null,
    val pages: Pages? = null,
    @SerialName("pull_requests") val pullRequests: PullRequests? = null,
    @SerialName("repository_custom_properties") val repositoryCustomProperties: RepositoryCustomProperties? = null,
    @SerialName("repository_hooks") val repositoryHooks: RepositoryHooks? = null,
    @SerialName("repository_projects") val repositoryProjects: RepositoryProjects? = null,
    @SerialName("secret_scanning_alerts") val secretScanningAlerts: SecretScanningAlerts? = null,
    val secrets: Secrets? = null,
    @SerialName("security_events") val securityEvents: SecurityEvents? = null,
    @SerialName("single_file") val singleFile: SingleFile? = null,
    val statuses: Statuses? = null,
    @SerialName("vulnerability_alerts") val vulnerabilityAlerts: VulnerabilityAlerts? = null,
    val workflows: Workflows? = null,
    @SerialName("custom_properties_for_organizations") val customPropertiesForOrganizations: CustomPropertiesForOrganizations? = null,
    val members: Members? = null,
    @SerialName("organization_administration") val organizationAdministration: OrganizationAdministration? = null,
    @SerialName("organization_custom_roles") val organizationCustomRoles: OrganizationCustomRoles? = null,
    @SerialName("organization_custom_org_roles") val organizationCustomOrgRoles: OrganizationCustomOrgRoles? = null,
    @SerialName("organization_custom_properties") val organizationCustomProperties: OrganizationCustomProperties? = null,
    @SerialName("organization_copilot_seat_management") val organizationCopilotSeatManagement: OrganizationCopilotSeatManagement? = null,
    @SerialName("organization_announcement_banners") val organizationAnnouncementBanners: OrganizationAnnouncementBanners? = null,
    @SerialName("organization_events") val organizationEvents: OrganizationEvents? = null,
    @SerialName("organization_hooks") val organizationHooks: OrganizationHooks? = null,
    @SerialName("organization_personal_access_tokens") val organizationPersonalAccessTokens: OrganizationPersonalAccessTokens? = null,
    @SerialName("organization_personal_access_token_requests") val organizationPersonalAccessTokenRequests: OrganizationPersonalAccessTokenRequests? = null,
    @SerialName("organization_plan") val organizationPlan: OrganizationPlan? = null,
    @SerialName("organization_projects") val organizationProjects: OrganizationProjects? = null,
    @SerialName("organization_packages") val organizationPackages: OrganizationPackages? = null,
    @SerialName("organization_secrets") val organizationSecrets: OrganizationSecrets? = null,
    @SerialName("organization_self_hosted_runners") val organizationSelfHostedRunners: OrganizationSelfHostedRunners? = null,
    @SerialName("organization_user_blocking") val organizationUserBlocking: OrganizationUserBlocking? = null,
    @SerialName("email_addresses") val emailAddresses: EmailAddresses? = null,
    val followers: Followers? = null,
    @SerialName("git_ssh_keys") val gitSshKeys: GitSshKeys? = null,
    @SerialName("gpg_keys") val gpgKeys: GpgKeys? = null,
    @SerialName("interaction_limits") val interactionLimits: InteractionLimits? = null,
    val profile: Profile? = null,
    val starring: Starring? = null,
    @SerialName("enterprise_custom_properties_for_organizations") val enterpriseCustomPropertiesForOrganizations: EnterpriseCustomPropertiesForOrganizations? = null,
) {
    @Serializable
    enum class Actions {
        @SerialName("read") Read, @SerialName("write") Write;
    }

    @Serializable
    enum class Administration {
        @SerialName("read") Read, @SerialName("write") Write;
    }

    @Serializable
    enum class ArtifactMetadata {
        @SerialName("read") Read, @SerialName("write") Write;
    }

    @Serializable
    enum class Attestations {
        @SerialName("read") Read, @SerialName("write") Write;
    }

    @Serializable
    enum class Checks {
        @SerialName("read") Read, @SerialName("write") Write;
    }

    @Serializable
    enum class Codespaces {
        @SerialName("read") Read, @SerialName("write") Write;
    }

    @Serializable
    enum class Contents {
        @SerialName("read") Read, @SerialName("write") Write;
    }

    @Serializable
    enum class DependabotSecrets {
        @SerialName("read") Read, @SerialName("write") Write;
    }

    @Serializable
    enum class Deployments {
        @SerialName("read") Read, @SerialName("write") Write;
    }

    @Serializable
    enum class Discussions {
        @SerialName("read") Read, @SerialName("write") Write;
    }

    @Serializable
    enum class Environments {
        @SerialName("read") Read, @SerialName("write") Write;
    }

    @Serializable
    enum class Issues {
        @SerialName("read") Read, @SerialName("write") Write;
    }

    @Serializable
    enum class MergeQueues {
        @SerialName("read") Read, @SerialName("write") Write;
    }

    @Serializable
    enum class Metadata {
        @SerialName("read") Read, @SerialName("write") Write;
    }

    @Serializable
    enum class Packages {
        @SerialName("read") Read, @SerialName("write") Write;
    }

    @Serializable
    enum class Pages {
        @SerialName("read") Read, @SerialName("write") Write;
    }

    @Serializable
    enum class PullRequests {
        @SerialName("read") Read, @SerialName("write") Write;
    }

    @Serializable
    enum class RepositoryCustomProperties {
        @SerialName("read") Read, @SerialName("write") Write;
    }

    @Serializable
    enum class RepositoryHooks {
        @SerialName("read") Read, @SerialName("write") Write;
    }

    @Serializable
    enum class RepositoryProjects {
        @SerialName("read") Read, @SerialName("write") Write, @SerialName("admin") Admin;
    }

    @Serializable
    enum class SecretScanningAlerts {
        @SerialName("read") Read, @SerialName("write") Write;
    }

    @Serializable
    enum class Secrets {
        @SerialName("read") Read, @SerialName("write") Write;
    }

    @Serializable
    enum class SecurityEvents {
        @SerialName("read") Read, @SerialName("write") Write;
    }

    @Serializable
    enum class SingleFile {
        @SerialName("read") Read, @SerialName("write") Write;
    }

    @Serializable
    enum class Statuses {
        @SerialName("read") Read, @SerialName("write") Write;
    }

    @Serializable
    enum class VulnerabilityAlerts {
        @SerialName("read") Read, @SerialName("write") Write;
    }

    @Serializable
    enum class Workflows {
        @SerialName("write") Write;
    }

    @Serializable
    enum class CustomPropertiesForOrganizations {
        @SerialName("read") Read, @SerialName("write") Write;
    }

    @Serializable
    enum class Members {
        @SerialName("read") Read, @SerialName("write") Write;
    }

    @Serializable
    enum class OrganizationAdministration {
        @SerialName("read") Read, @SerialName("write") Write;
    }

    @Serializable
    enum class OrganizationCustomRoles {
        @SerialName("read") Read, @SerialName("write") Write;
    }

    @Serializable
    enum class OrganizationCustomOrgRoles {
        @SerialName("read") Read, @SerialName("write") Write;
    }

    @Serializable
    enum class OrganizationCustomProperties {
        @SerialName("read") Read, @SerialName("write") Write, @SerialName("admin") Admin;
    }

    @Serializable
    enum class OrganizationCopilotSeatManagement {
        @SerialName("write") Write;
    }

    @Serializable
    enum class OrganizationAnnouncementBanners {
        @SerialName("read") Read, @SerialName("write") Write;
    }

    @Serializable
    enum class OrganizationEvents {
        @SerialName("read") Read;
    }

    @Serializable
    enum class OrganizationHooks {
        @SerialName("read") Read, @SerialName("write") Write;
    }

    @Serializable
    enum class OrganizationPersonalAccessTokens {
        @SerialName("read") Read, @SerialName("write") Write;
    }

    @Serializable
    enum class OrganizationPersonalAccessTokenRequests {
        @SerialName("read") Read, @SerialName("write") Write;
    }

    @Serializable
    enum class OrganizationPlan {
        @SerialName("read") Read;
    }

    @Serializable
    enum class OrganizationProjects {
        @SerialName("read") Read, @SerialName("write") Write, @SerialName("admin") Admin;
    }

    @Serializable
    enum class OrganizationPackages {
        @SerialName("read") Read, @SerialName("write") Write;
    }

    @Serializable
    enum class OrganizationSecrets {
        @SerialName("read") Read, @SerialName("write") Write;
    }

    @Serializable
    enum class OrganizationSelfHostedRunners {
        @SerialName("read") Read, @SerialName("write") Write;
    }

    @Serializable
    enum class OrganizationUserBlocking {
        @SerialName("read") Read, @SerialName("write") Write;
    }

    @Serializable
    enum class EmailAddresses {
        @SerialName("read") Read, @SerialName("write") Write;
    }

    @Serializable
    enum class Followers {
        @SerialName("read") Read, @SerialName("write") Write;
    }

    @Serializable
    enum class GitSshKeys {
        @SerialName("read") Read, @SerialName("write") Write;
    }

    @Serializable
    enum class GpgKeys {
        @SerialName("read") Read, @SerialName("write") Write;
    }

    @Serializable
    enum class InteractionLimits {
        @SerialName("read") Read, @SerialName("write") Write;
    }

    @Serializable
    enum class Profile {
        @SerialName("write") Write;
    }

    @Serializable
    enum class Starring {
        @SerialName("read") Read, @SerialName("write") Write;
    }

    @Serializable
    enum class EnterpriseCustomPropertiesForOrganizations {
        @SerialName("read") Read, @SerialName("write") Write, @SerialName("admin") Admin;
    }
}
