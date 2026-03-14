package io.github.nomisrev.api

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlin.jvm.JvmInline
import io.github.nomisrev.model.ValidationError
import io.github.nomisrev.model.ValidationErrorSimple
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonDecoder
import io.github.nomisrev.model.attemptDeserialize
import io.github.nomisrev.model.OrganizationFull
import io.github.nomisrev.model.BasicError
import io.github.nomisrev.model.ActionsCacheUsageOrgEnterprise
import io.github.nomisrev.model.ActionsCacheUsageByRepository
import io.github.nomisrev.model.ActionsHostedRunner
import io.github.nomisrev.model.ActionsHostedRunnerCustomImage
import io.github.nomisrev.model.ActionsHostedRunnerCustomImageVersion
import io.github.nomisrev.model.ActionsHostedRunnerCuratedImage
import io.github.nomisrev.model.ActionsHostedRunnerLimits
import io.github.nomisrev.model.ActionsHostedRunnerMachineSpec
import io.github.nomisrev.model.OidcCustomPropertyInclusion
import io.github.nomisrev.model.OidcCustomPropertyInclusionInput
import io.github.nomisrev.model.OidcCustomSub
import io.github.nomisrev.model.EmptyObject
import io.github.nomisrev.model.EnabledRepositories
import io.github.nomisrev.model.AllowedActions
import io.github.nomisrev.model.ShaPinningRequired
import io.github.nomisrev.model.ActionsOrganizationPermissions
import io.github.nomisrev.model.ActionsArtifactAndLogRetentionResponse
import io.github.nomisrev.model.ActionsArtifactAndLogRetention
import io.github.nomisrev.model.ActionsForkPrContributorApproval
import io.github.nomisrev.model.ActionsForkPrWorkflowsPrivateRepos
import io.github.nomisrev.model.ActionsForkPrWorkflowsPrivateReposRequest
import io.github.nomisrev.model.Repository
import io.github.nomisrev.model.SelectedActions
import io.github.nomisrev.model.SelfHostedRunnersSettings
import io.github.nomisrev.model.ActionsGetDefaultWorkflowPermissions
import io.github.nomisrev.model.ActionsSetDefaultWorkflowPermissions
import io.github.nomisrev.model.RunnerGroupsOrg
import io.github.nomisrev.model.MinimalRepository
import io.github.nomisrev.model.Runner
import io.github.nomisrev.model.RunnerApplication
import io.github.nomisrev.model.AuthenticationToken
import io.github.nomisrev.model.RunnerLabel
import io.github.nomisrev.model.OrganizationActionsSecret
import io.github.nomisrev.model.ActionsPublicKey
import io.github.nomisrev.model.OrganizationActionsVariable
import io.github.nomisrev.model.ArtifactDeploymentRecord
import io.github.nomisrev.model.SimpleUser
import kotlinx.datetime.LocalDateTime
import io.github.nomisrev.model.CampaignState
import io.github.nomisrev.model.CampaignSummary
import io.github.nomisrev.model.CodeScanningOrganizationAlertItemsResponse
import io.github.nomisrev.model.CodeScanningAlertSeverity
import io.github.nomisrev.model.CodeScanningAlertStateQuery
import io.github.nomisrev.model.CodeScanningAnalysisToolGuid
import io.github.nomisrev.model.CodeScanningAnalysisToolName
import io.github.nomisrev.model.CodeScanningOptions
import io.github.nomisrev.model.CodeScanningDefaultSetupOptions
import io.github.nomisrev.model.CodeSecurityConfiguration
import io.github.nomisrev.model.CodeSecurityDefaultConfigurations
import io.github.nomisrev.model.CodeSecurityConfigurationRepositories
import io.github.nomisrev.model.Codespace
import io.github.nomisrev.model.CodespacesOrgSecret
import io.github.nomisrev.model.CodespacesPublicKey
import io.github.nomisrev.model.CopilotOrganizationDetails
import io.github.nomisrev.model.CopilotSeatDetails
import kotlinx.serialization.builtins.serializer
import io.github.nomisrev.model.CopilotOrganizationContentExclusionDetails
import io.github.nomisrev.model.CopilotUsageMetricsDay
import kotlinx.serialization.builtins.ListSerializer
import io.github.nomisrev.model.DependabotAlertWithRepositoryResponse
import io.github.nomisrev.model.OrganizationDependabotSecret
import io.github.nomisrev.model.DependabotPublicKey
import io.github.nomisrev.model.Package
import io.github.nomisrev.model.Event
import io.github.nomisrev.model.OrganizationInvitation
import io.github.nomisrev.model.WebhookConfigUrl
import io.github.nomisrev.model.WebhookConfigContentType
import io.github.nomisrev.model.WebhookConfigSecret
import io.github.nomisrev.model.WebhookConfigInsecureSsl
import io.github.nomisrev.model.OrgHook
import io.github.nomisrev.model.WebhookConfig
import io.github.nomisrev.model.HookDeliveryItem
import io.github.nomisrev.model.HookDelivery
import io.github.nomisrev.model.ApiInsightsRouteStats
import io.github.nomisrev.model.ApiInsightsSubjectStats
import io.github.nomisrev.model.ApiInsightsSummaryStats
import io.github.nomisrev.model.ApiInsightsTimeStats
import io.github.nomisrev.model.ApiInsightsUserStats
import io.github.nomisrev.model.Installation
import io.github.nomisrev.model.InteractionLimitResponse
import io.github.nomisrev.model.InteractionLimit
import io.github.nomisrev.model.Team
import io.github.nomisrev.model.IssueField
import io.github.nomisrev.model.OrganizationCreateIssueField
import io.github.nomisrev.model.OrganizationUpdateIssueField
import io.github.nomisrev.model.IssueType
import io.github.nomisrev.model.OrganizationCreateIssueType
import io.github.nomisrev.model.OrganizationUpdateIssueType
import io.github.nomisrev.model.Issue
import kotlin.js.JsName
import io.github.nomisrev.model.OrgMembership
import io.github.nomisrev.model.Migration
import io.github.nomisrev.model.OrganizationRole
import io.github.nomisrev.model.TeamRoleAssignment
import io.github.nomisrev.model.UserRoleAssignment
import io.github.nomisrev.model.PackageVersion
import io.github.nomisrev.model.OrganizationProgrammaticAccessGrantRequest
import io.github.nomisrev.model.OrganizationProgrammaticAccessGrant
import io.github.nomisrev.model.OrgPrivateRegistryConfiguration
import io.github.nomisrev.model.OrgPrivateRegistryConfigurationWithSelectedRepositories
import io.github.nomisrev.model.ProjectsV2
import io.github.nomisrev.model.ProjectsV2ItemSimple
import io.github.nomisrev.model.ProjectsV2FieldSingleSelectOption
import io.github.nomisrev.model.ProjectsV2FieldIterationConfiguration
import io.github.nomisrev.model.ProjectsV2Field
import io.github.nomisrev.model.ProjectsV2ItemWithContent
import io.github.nomisrev.model.ProjectsV2View
import io.github.nomisrev.model.CustomProperty
import io.github.nomisrev.model.CustomPropertySetPayload
import io.github.nomisrev.model.CustomPropertyValue
import io.github.nomisrev.model.OrgRepoCustomPropertyValues
import io.github.nomisrev.model.FullRepository
import io.github.nomisrev.model.RepositoryRuleEnforcement
import io.github.nomisrev.model.RepositoryRulesetBypassActor
import io.github.nomisrev.model.OrgRulesetConditions
import io.github.nomisrev.model.OrgRules
import io.github.nomisrev.model.RepositoryRuleset
import io.github.nomisrev.model.RuleSuites
import io.github.nomisrev.model.RuleSuite
import io.github.nomisrev.model.RulesetVersion
import io.github.nomisrev.model.RulesetVersionWithState
import io.github.nomisrev.model.OrganizationSecretScanningAlertResponse
import io.github.nomisrev.model.SecretScanningRowVersion
import io.github.nomisrev.model.SecretScanningPatternConfiguration
import io.github.nomisrev.model.RepositoryAdvisoryResponse
import io.github.nomisrev.model.TeamSimple
import io.github.nomisrev.model.ImmutableReleasesOrganizationSettings
import io.github.nomisrev.model.NetworkConfiguration
import io.github.nomisrev.model.NetworkSettings
import io.github.nomisrev.model.TeamFull
import io.github.nomisrev.model.TeamMembership
import io.github.nomisrev.model.TeamRepository
import io.ktor.client.HttpClient
import io.ktor.http.HttpStatusCode
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.http.ContentType
import io.ktor.client.request.setBody
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.http.contentType

interface Orgs {
    val actions: Orgs.Actions

    val artifacts: Orgs.Artifacts

    val attestations: Orgs.Attestations

    val blocks: Orgs.Blocks

    val campaigns: Orgs.Campaigns

    val codeScanning: Orgs.CodeScanning

    val codeSecurity: Orgs.CodeSecurity

    val codespaces: Orgs.Codespaces

    val copilot: Orgs.Copilot

    val dependabot: Orgs.Dependabot

    val docker: Orgs.Docker

    val events: Orgs.Events

    val failedInvitations: Orgs.FailedInvitations

    val hooks: Orgs.Hooks

    val insights: Orgs.Insights

    val installation: Orgs.Installation

    val installations: Orgs.Installations

    val interactionLimits: Orgs.InteractionLimits

    val invitations: Orgs.Invitations

    val issueFields: Orgs.IssueFields

    val issueTypes: Orgs.IssueTypes

    val issues: Orgs.Issues

    val members: Orgs.Members

    val memberships: Orgs.Memberships

    val migrations: Orgs.Migrations

    val organizationRoles: Orgs.OrganizationRoles

    val outsideCollaborators: Orgs.OutsideCollaborators

    val packages: Orgs.Packages

    val personalAccessTokenRequests: Orgs.PersonalAccessTokenRequests

    val personalAccessTokens: Orgs.PersonalAccessTokens

    val privateRegistries: Orgs.PrivateRegistries

    val projectsV2: Orgs.ProjectsV2

    val properties: Orgs.Properties

    val publicMembers: Orgs.PublicMembers

    val repos: Orgs.Repos

    val rulesets: Orgs.Rulesets

    val secretScanning: Orgs.SecretScanning

    val securityAdvisories: Orgs.SecurityAdvisories

    val securityManagers: Orgs.SecurityManagers

    val settings: Orgs.Settings

    val team: Orgs.Team

    val teams: Orgs.Teams

    @Serializable
    enum class Enablement {
        @SerialName("enable_all") EnableAll, @SerialName("disable_all") DisableAll;
    }


    @Serializable
    @JvmInline
    value class OrgsEnableOrDisableSecurityProductOnAllOrgReposBody(@SerialName("query_suite") val querySuite: QuerySuite? = null) {
        @Serializable
        enum class QuerySuite {
            @SerialName("default") Default, @SerialName("extended") Extended;
        }
    }


    @Serializable
    data class OrgsUpdateBody(
        @SerialName("billing_email") val billingEmail: String? = null,
        val company: String? = null,
        val email: String? = null,
        @SerialName("twitter_username") val twitterUsername: String? = null,
        val location: String? = null,
        val name: String? = null,
        val description: String? = null,
        @SerialName("has_organization_projects") val hasOrganizationProjects: Boolean? = null,
        @SerialName("has_repository_projects") val hasRepositoryProjects: Boolean? = null,
        @SerialName("default_repository_permission") val defaultRepositoryPermission: DefaultRepositoryPermission? = null,
        @SerialName("members_can_create_repositories") val membersCanCreateRepositories: Boolean? = null,
        @SerialName("members_can_create_internal_repositories") val membersCanCreateInternalRepositories: Boolean? = null,
        @SerialName("members_can_create_private_repositories") val membersCanCreatePrivateRepositories: Boolean? = null,
        @SerialName("members_can_create_public_repositories") val membersCanCreatePublicRepositories: Boolean? = null,
        @SerialName("members_allowed_repository_creation_type") val membersAllowedRepositoryCreationType: MembersAllowedRepositoryCreationType? = null,
        @SerialName("members_can_create_pages") val membersCanCreatePages: Boolean? = null,
        @SerialName("members_can_create_public_pages") val membersCanCreatePublicPages: Boolean? = null,
        @SerialName("members_can_create_private_pages") val membersCanCreatePrivatePages: Boolean? = null,
        @SerialName("members_can_fork_private_repositories") val membersCanForkPrivateRepositories: Boolean? = null,
        @SerialName("web_commit_signoff_required") val webCommitSignoffRequired: Boolean? = null,
        val blog: String? = null,
        @SerialName("advanced_security_enabled_for_new_repositories") val advancedSecurityEnabledForNewRepositories: Boolean? = null,
        @SerialName("dependabot_alerts_enabled_for_new_repositories") val dependabotAlertsEnabledForNewRepositories: Boolean? = null,
        @SerialName("dependabot_security_updates_enabled_for_new_repositories") val dependabotSecurityUpdatesEnabledForNewRepositories: Boolean? = null,
        @SerialName("dependency_graph_enabled_for_new_repositories") val dependencyGraphEnabledForNewRepositories: Boolean? = null,
        @SerialName("secret_scanning_enabled_for_new_repositories") val secretScanningEnabledForNewRepositories: Boolean? = null,
        @SerialName("secret_scanning_push_protection_enabled_for_new_repositories") val secretScanningPushProtectionEnabledForNewRepositories: Boolean? = null,
        @SerialName("secret_scanning_push_protection_custom_link_enabled") val secretScanningPushProtectionCustomLinkEnabled: Boolean? = null,
        @SerialName("secret_scanning_push_protection_custom_link") val secretScanningPushProtectionCustomLink: String? = null,
        @SerialName("deploy_keys_enabled_for_repositories") val deployKeysEnabledForRepositories: Boolean? = null,
    ) {
        @Serializable
        enum class DefaultRepositoryPermission {
            @SerialName("read") Read, @SerialName("write") Write, @SerialName("admin") Admin, @SerialName("none") None;
        }

        @Serializable
        enum class MembersAllowedRepositoryCreationType {
            @SerialName("all") All, @SerialName("private") Private, @SerialName("none") None;
        }
    }


    @Serializable(with = OrgsUpdateResponse.Serializer::class)
    sealed interface OrgsUpdateResponse {
        @Serializable
        @JvmInline
        value class CaseValidationError(val value: ValidationError) : OrgsUpdateResponse

        @Serializable
        @JvmInline
        value class CaseValidationErrorSimple(val value: ValidationErrorSimple) : OrgsUpdateResponse

        object Serializer : KSerializer<OrgsUpdateResponse> {
            @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
            override val descriptor: SerialDescriptor =
                buildSerialDescriptor("io.github.nomisrev.api.Orgs.OrgsUpdateResponse", PolymorphicKind.SEALED) {
                    element("CaseValidationError", ValidationError.serializer().descriptor)
                    element("CaseValidationErrorSimple", ValidationErrorSimple.serializer().descriptor)
                }

            override fun deserialize(decoder: Decoder): OrgsUpdateResponse {
                val value = decoder.decodeSerializableValue(JsonElement.serializer())
                val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                return json.attemptDeserialize(
                    value,
                    CaseValidationError::class to { CaseValidationError(decodeFromJsonElement(ValidationError.serializer(), it)) },
                    CaseValidationErrorSimple::class to { CaseValidationErrorSimple(decodeFromJsonElement(ValidationErrorSimple.serializer(), it)) },
                )
            }

            override fun serialize(encoder: Encoder, value: OrgsUpdateResponse) = when(value) {
                is CaseValidationError -> encoder.encodeSerializableValue(ValidationError.serializer(), value.value)
                is CaseValidationErrorSimple -> encoder.encodeSerializableValue(ValidationErrorSimple.serializer(), value.value)
            }
        }
    }


    @Serializable
    enum class SecurityProduct {
        @SerialName("dependency_graph")
        DependencyGraph,
        @SerialName("dependabot_alerts")
        DependabotAlerts,
        @SerialName("dependabot_security_updates")
        DependabotSecurityUpdates,
        @SerialName("advanced_security")
        AdvancedSecurity,
        @SerialName("code_scanning_default_setup")
        CodeScanningDefaultSetup,
        @SerialName("secret_scanning")
        SecretScanning,
        @SerialName("secret_scanning_push_protection")
        SecretScanningPushProtection;
    }

    sealed interface OrgsGetResult {
        data class OK(val value: OrganizationFull) : OrgsGetResult

        data class NotFound(val value: BasicError) : OrgsGetResult
    }

    suspend fun orgsGet(
        org: String,
    ): OrgsGetResult

    sealed interface OrgsDeleteResult {
        data class Accepted(val value: JsonElement) : OrgsDeleteResult

        data class Forbidden(val value: BasicError) : OrgsDeleteResult

        data class NotFound(val value: BasicError) : OrgsDeleteResult
    }

    suspend fun orgsDelete(
        org: String,
    ): OrgsDeleteResult

    sealed interface OrgsUpdateResult {
        data class OK(val value: OrganizationFull) : OrgsUpdateResult

        data class Conflict(val value: BasicError) : OrgsUpdateResult

        data class UnprocessableEntity(val value: OrgsUpdateResponse) : OrgsUpdateResult
    }

    suspend fun orgsUpdate(
        org: String,
        body: OrgsUpdateBody? = null,
    ): OrgsUpdateResult

    sealed interface OrgsEnableOrDisableSecurityProductOnAllOrgReposResult {
        data object NoContent : OrgsEnableOrDisableSecurityProductOnAllOrgReposResult

        data object UnprocessableEntity : OrgsEnableOrDisableSecurityProductOnAllOrgReposResult
    }

    @Deprecated("Deprecated by the API provider")
    suspend fun orgsEnableOrDisableSecurityProductOnAllOrgRepos(
        org: String,
        securityProduct: SecurityProduct,
        enablement: Enablement,
        body: OrgsEnableOrDisableSecurityProductOnAllOrgReposBody? = null,
    ): OrgsEnableOrDisableSecurityProductOnAllOrgReposResult

    interface Actions {
        val cache: Orgs.Actions.Cache

        val hostedRunners: Orgs.Actions.HostedRunners

        val oidc: Orgs.Actions.Oidc

        val permissions: Orgs.Actions.Permissions

        val runnerGroups: Orgs.Actions.RunnerGroups

        val runners: Orgs.Actions.Runners

        val secrets: Orgs.Actions.Secrets

        val variables: Orgs.Actions.Variables

        interface Cache {
            val usage: Orgs.Actions.Cache.Usage

            val usageByRepository: Orgs.Actions.Cache.UsageByRepository

            interface Usage {
                suspend fun actionsGetActionsCacheUsageForOrg(
                    org: String,
                ): ActionsCacheUsageOrgEnterprise
            }

            interface UsageByRepository {
                @Serializable
                data class ActionsGetActionsCacheUsageByRepoForOrgResponse(
                    @SerialName("total_count") val totalCount: Long,
                    @SerialName("repository_cache_usages") val repositoryCacheUsages: List<ActionsCacheUsageByRepository>,
                )

                suspend fun actionsGetActionsCacheUsageByRepoForOrg(
                    org: String,
                    page: Long = 1L,
                    perPage: Long = 30L,
                ): ActionsGetActionsCacheUsageByRepoForOrgResponse
            }
        }

        interface HostedRunners {
            val images: Orgs.Actions.HostedRunners.Images

            val limits: Orgs.Actions.HostedRunners.Limits

            val machineSizes: Orgs.Actions.HostedRunners.MachineSizes

            val platforms: Orgs.Actions.HostedRunners.Platforms

            @Serializable
            data class ActionsCreateHostedRunnerForOrgBody(
                val name: String,
                val image: Image,
                val size: String,
                @SerialName("runner_group_id") val runnerGroupId: Long,
                @SerialName("maximum_runners") val maximumRunners: Long? = null,
                @SerialName("enable_static_ip") val enableStaticIp: Boolean? = null,
                @SerialName("image_gen") val imageGen: Boolean? = null,
            ) {
                @Serializable
                data class Image(val id: String? = null, val source: Source? = null, val version: String? = null) {
                    @Serializable
                    enum class Source {
                        @SerialName("github") Github, @SerialName("partner") Partner, @SerialName("custom") Custom;
                    }
                }
            }


            @Serializable
            data class ActionsListHostedRunnersForOrgResponse(
                @SerialName("total_count") val totalCount: Long,
                val runners: List<ActionsHostedRunner>,
            )


            @Serializable
            data class ActionsUpdateHostedRunnerForOrgBody(
                val name: String? = null,
                @SerialName("runner_group_id") val runnerGroupId: Long? = null,
                @SerialName("maximum_runners") val maximumRunners: Long? = null,
                @SerialName("enable_static_ip") val enableStaticIp: Boolean? = null,
                val size: String? = null,
                @SerialName("image_id") val imageId: String? = null,
                @SerialName("image_version") val imageVersion: String? = null,
            )

            suspend fun actionsListHostedRunnersForOrg(
                org: String,
                page: Long = 1L,
                perPage: Long = 30L,
            ): ActionsListHostedRunnersForOrgResponse

            suspend fun actionsCreateHostedRunnerForOrg(
                org: String,
                body: ActionsCreateHostedRunnerForOrgBody,
            ): ActionsHostedRunner

            suspend fun actionsGetHostedRunnerForOrg(
                org: String,
                hostedRunnerId: Long,
            ): ActionsHostedRunner

            suspend fun actionsDeleteHostedRunnerForOrg(
                org: String,
                hostedRunnerId: Long,
            ): ActionsHostedRunner

            suspend fun actionsUpdateHostedRunnerForOrg(
                org: String,
                hostedRunnerId: Long,
                body: ActionsUpdateHostedRunnerForOrgBody,
            ): ActionsHostedRunner

            interface Images {
                val custom: Orgs.Actions.HostedRunners.Images.Custom

                val githubOwned: Orgs.Actions.HostedRunners.Images.GithubOwned

                val partner: Orgs.Actions.HostedRunners.Images.Partner

                interface Custom {
                    val versions: Orgs.Actions.HostedRunners.Images.Custom.Versions

                    @Serializable
                    data class ActionsListCustomImagesForOrgResponse(
                        @SerialName("total_count") val totalCount: Long,
                        val images: List<ActionsHostedRunnerCustomImage>,
                    )

                    suspend fun actionsListCustomImagesForOrg(
                        org: String,
                    ): ActionsListCustomImagesForOrgResponse

                    suspend fun actionsGetCustomImageForOrg(
                        org: String,
                        imageDefinitionId: Long,
                    ): ActionsHostedRunnerCustomImage

                    suspend fun actionsDeleteCustomImageFromOrg(
                        org: String,
                        imageDefinitionId: Long,
                    ): Unit

                    interface Versions {
                        @Serializable
                        data class ActionsListCustomImageVersionsForOrgResponse(
                            @SerialName("total_count") val totalCount: Long,
                            @SerialName("image_versions") val imageVersions: List<ActionsHostedRunnerCustomImageVersion>,
                        )

                        suspend fun actionsListCustomImageVersionsForOrg(
                            org: String,
                            imageDefinitionId: Long,
                        ): ActionsListCustomImageVersionsForOrgResponse

                        suspend fun actionsGetCustomImageVersionForOrg(
                            org: String,
                            imageDefinitionId: Long,
                            version: String,
                        ): ActionsHostedRunnerCustomImageVersion

                        suspend fun actionsDeleteCustomImageVersionFromOrg(
                            org: String,
                            imageDefinitionId: Long,
                            version: String,
                        ): Unit
                    }
                }

                interface GithubOwned {
                    @Serializable
                    data class ActionsGetHostedRunnersGithubOwnedImagesForOrgResponse(
                        @SerialName("total_count") val totalCount: Long,
                        val images: List<ActionsHostedRunnerCuratedImage>,
                    )

                    suspend fun actionsGetHostedRunnersGithubOwnedImagesForOrg(
                        org: String,
                    ): ActionsGetHostedRunnersGithubOwnedImagesForOrgResponse
                }

                interface Partner {
                    @Serializable
                    data class ActionsGetHostedRunnersPartnerImagesForOrgResponse(
                        @SerialName("total_count") val totalCount: Long,
                        val images: List<ActionsHostedRunnerCuratedImage>,
                    )

                    suspend fun actionsGetHostedRunnersPartnerImagesForOrg(
                        org: String,
                    ): ActionsGetHostedRunnersPartnerImagesForOrgResponse
                }
            }

            interface Limits {
                suspend fun actionsGetHostedRunnersLimitsForOrg(
                    org: String,
                ): ActionsHostedRunnerLimits
            }

            interface MachineSizes {
                @Serializable
                data class ActionsGetHostedRunnersMachineSpecsForOrgResponse(
                    @SerialName("total_count") val totalCount: Long,
                    @SerialName("machine_specs") val machineSpecs: List<ActionsHostedRunnerMachineSpec>,
                )

                suspend fun actionsGetHostedRunnersMachineSpecsForOrg(
                    org: String,
                ): ActionsGetHostedRunnersMachineSpecsForOrgResponse
            }

            interface Platforms {
                @Serializable
                data class ActionsGetHostedRunnersPlatformsForOrgResponse(
                    @SerialName("total_count") val totalCount: Long,
                    val platforms: List<String>,
                )

                suspend fun actionsGetHostedRunnersPlatformsForOrg(
                    org: String,
                ): ActionsGetHostedRunnersPlatformsForOrgResponse
            }
        }

        interface Oidc {
            val customization: Orgs.Actions.Oidc.Customization

            interface Customization {
                val properties: Orgs.Actions.Oidc.Customization.PropertiesApi

                val sub: Orgs.Actions.Oidc.Customization.Sub

                interface PropertiesApi {
                    val repo: Orgs.Actions.Oidc.Customization.PropertiesApi.Repo

                    interface Repo {
                        sealed interface OidcListOidcCustomPropertyInclusionsForOrgResult {
                            data class OK(val value: List<OidcCustomPropertyInclusion>) : OidcListOidcCustomPropertyInclusionsForOrgResult

                            data class Forbidden(val value: BasicError) : OidcListOidcCustomPropertyInclusionsForOrgResult

                            data class NotFound(val value: BasicError) : OidcListOidcCustomPropertyInclusionsForOrgResult
                        }

                        suspend fun oidcListOidcCustomPropertyInclusionsForOrg(
                            org: String,
                        ): OidcListOidcCustomPropertyInclusionsForOrgResult

                        sealed interface OidcCreateOidcCustomPropertyInclusionForOrgResult {
                            data class Created(val value: OidcCustomPropertyInclusion) : OidcCreateOidcCustomPropertyInclusionForOrgResult

                            data object BadRequest : OidcCreateOidcCustomPropertyInclusionForOrgResult

                            data class Forbidden(val value: BasicError) : OidcCreateOidcCustomPropertyInclusionForOrgResult

                            data object UnprocessableEntity : OidcCreateOidcCustomPropertyInclusionForOrgResult
                        }

                        suspend fun oidcCreateOidcCustomPropertyInclusionForOrg(
                            org: String,
                            body: OidcCustomPropertyInclusionInput,
                        ): OidcCreateOidcCustomPropertyInclusionForOrgResult

                        sealed interface OidcDeleteOidcCustomPropertyInclusionForOrgResult {
                            data object NoContent : OidcDeleteOidcCustomPropertyInclusionForOrgResult

                            data object BadRequest : OidcDeleteOidcCustomPropertyInclusionForOrgResult

                            data class Forbidden(val value: BasicError) : OidcDeleteOidcCustomPropertyInclusionForOrgResult

                            data object NotFound : OidcDeleteOidcCustomPropertyInclusionForOrgResult
                        }

                        suspend fun oidcDeleteOidcCustomPropertyInclusionForOrg(
                            org: String,
                            customPropertyName: String,
                        ): OidcDeleteOidcCustomPropertyInclusionForOrgResult
                    }
                }

                interface Sub {
                    suspend fun oidcGetOidcCustomSubTemplateForOrg(
                        org: String,
                    ): OidcCustomSub

                    sealed interface OidcUpdateOidcCustomSubTemplateForOrgResult {
                        data class Created(val value: EmptyObject) : OidcUpdateOidcCustomSubTemplateForOrgResult

                        data class Forbidden(val value: BasicError) : OidcUpdateOidcCustomSubTemplateForOrgResult

                        data class NotFound(val value: BasicError) : OidcUpdateOidcCustomSubTemplateForOrgResult
                    }

                    suspend fun oidcUpdateOidcCustomSubTemplateForOrg(
                        org: String,
                        body: OidcCustomSub,
                    ): OidcUpdateOidcCustomSubTemplateForOrgResult
                }
            }
        }

        interface Permissions {
            val artifactAndLogRetention: Orgs.Actions.Permissions.ArtifactAndLogRetention

            val forkPrContributorApproval: Orgs.Actions.Permissions.ForkPrContributorApproval

            val forkPrWorkflowsPrivateRepos: Orgs.Actions.Permissions.ForkPrWorkflowsPrivateRepos

            val repositories: Orgs.Actions.Permissions.Repositories

            val selectedActions: Orgs.Actions.Permissions.SelectedActions

            val selfHostedRunners: Orgs.Actions.Permissions.SelfHostedRunners

            val workflow: Orgs.Actions.Permissions.Workflow

            @Serializable
            data class ActionsSetGithubActionsPermissionsOrganizationBody(
                @SerialName("enabled_repositories") val enabledRepositories: EnabledRepositories,
                @SerialName("allowed_actions") val allowedActions: AllowedActions? = null,
                @SerialName("sha_pinning_required") val shaPinningRequired: ShaPinningRequired? = null,
            )

            suspend fun actionsGetGithubActionsPermissionsOrganization(
                org: String,
            ): ActionsOrganizationPermissions

            suspend fun actionsSetGithubActionsPermissionsOrganization(
                org: String,
                body: ActionsSetGithubActionsPermissionsOrganizationBody,
            ): Unit

            interface ArtifactAndLogRetention {
                sealed interface ActionsGetArtifactAndLogRetentionSettingsOrganizationResult {
                    data class OK(val value: ActionsArtifactAndLogRetentionResponse) : ActionsGetArtifactAndLogRetentionSettingsOrganizationResult

                    data class Forbidden(val value: BasicError) : ActionsGetArtifactAndLogRetentionSettingsOrganizationResult

                    data class NotFound(val value: BasicError) : ActionsGetArtifactAndLogRetentionSettingsOrganizationResult
                }

                suspend fun actionsGetArtifactAndLogRetentionSettingsOrganization(
                    org: String,
                ): ActionsGetArtifactAndLogRetentionSettingsOrganizationResult

                sealed interface ActionsSetArtifactAndLogRetentionSettingsOrganizationResult {
                    data object NoContent : ActionsSetArtifactAndLogRetentionSettingsOrganizationResult

                    data class Forbidden(val value: BasicError) : ActionsSetArtifactAndLogRetentionSettingsOrganizationResult

                    data class NotFound(val value: BasicError) : ActionsSetArtifactAndLogRetentionSettingsOrganizationResult

                    data class Conflict(val value: BasicError) : ActionsSetArtifactAndLogRetentionSettingsOrganizationResult

                    data class UnprocessableEntity(val value: ValidationError) : ActionsSetArtifactAndLogRetentionSettingsOrganizationResult
                }

                suspend fun actionsSetArtifactAndLogRetentionSettingsOrganization(
                    org: String,
                    body: ActionsArtifactAndLogRetention,
                ): ActionsSetArtifactAndLogRetentionSettingsOrganizationResult
            }

            interface ForkPrContributorApproval {
                sealed interface ActionsGetForkPrContributorApprovalPermissionsOrganizationResult {
                    data class OK(val value: ActionsForkPrContributorApproval) : ActionsGetForkPrContributorApprovalPermissionsOrganizationResult

                    data class NotFound(val value: BasicError) : ActionsGetForkPrContributorApprovalPermissionsOrganizationResult
                }

                suspend fun actionsGetForkPrContributorApprovalPermissionsOrganization(
                    org: String,
                ): ActionsGetForkPrContributorApprovalPermissionsOrganizationResult

                sealed interface ActionsSetForkPrContributorApprovalPermissionsOrganizationResult {
                    data object NoContent : ActionsSetForkPrContributorApprovalPermissionsOrganizationResult

                    data class NotFound(val value: BasicError) : ActionsSetForkPrContributorApprovalPermissionsOrganizationResult

                    data class UnprocessableEntity(val value: ValidationError) : ActionsSetForkPrContributorApprovalPermissionsOrganizationResult
                }

                suspend fun actionsSetForkPrContributorApprovalPermissionsOrganization(
                    org: String,
                    body: ActionsForkPrContributorApproval,
                ): ActionsSetForkPrContributorApprovalPermissionsOrganizationResult
            }

            interface ForkPrWorkflowsPrivateRepos {
                sealed interface ActionsGetPrivateRepoForkPrWorkflowsSettingsOrganizationResult {
                    data class OK(val value: ActionsForkPrWorkflowsPrivateRepos) : ActionsGetPrivateRepoForkPrWorkflowsSettingsOrganizationResult

                    data class Forbidden(val value: BasicError) : ActionsGetPrivateRepoForkPrWorkflowsSettingsOrganizationResult

                    data class NotFound(val value: BasicError) : ActionsGetPrivateRepoForkPrWorkflowsSettingsOrganizationResult
                }

                suspend fun actionsGetPrivateRepoForkPrWorkflowsSettingsOrganization(
                    org: String,
                ): ActionsGetPrivateRepoForkPrWorkflowsSettingsOrganizationResult

                sealed interface ActionsSetPrivateRepoForkPrWorkflowsSettingsOrganizationResult {
                    data object NoContent : ActionsSetPrivateRepoForkPrWorkflowsSettingsOrganizationResult

                    data class Forbidden(val value: BasicError) : ActionsSetPrivateRepoForkPrWorkflowsSettingsOrganizationResult

                    data class NotFound(val value: BasicError) : ActionsSetPrivateRepoForkPrWorkflowsSettingsOrganizationResult

                    data class UnprocessableEntity(val value: ValidationError) : ActionsSetPrivateRepoForkPrWorkflowsSettingsOrganizationResult
                }

                suspend fun actionsSetPrivateRepoForkPrWorkflowsSettingsOrganization(
                    org: String,
                    body: ActionsForkPrWorkflowsPrivateReposRequest,
                ): ActionsSetPrivateRepoForkPrWorkflowsSettingsOrganizationResult
            }

            interface Repositories {
                @Serializable
                data class ActionsListSelectedRepositoriesEnabledGithubActionsOrganizationResponse(
                    @SerialName("total_count") val totalCount: Double,
                    val repositories: List<Repository>,
                )


                @Serializable
                @JvmInline
                value class ActionsSetSelectedRepositoriesEnabledGithubActionsOrganizationBody(@SerialName("selected_repository_ids") val selectedRepositoryIds: List<Long>)

                suspend fun actionsListSelectedRepositoriesEnabledGithubActionsOrganization(
                    org: String,
                    page: Long = 1L,
                    perPage: Long = 30L,
                ): ActionsListSelectedRepositoriesEnabledGithubActionsOrganizationResponse

                suspend fun actionsSetSelectedRepositoriesEnabledGithubActionsOrganization(
                    org: String,
                    body: ActionsSetSelectedRepositoriesEnabledGithubActionsOrganizationBody,
                ): Unit

                suspend fun actionsEnableSelectedRepositoryGithubActionsOrganization(
                    org: String,
                    repositoryId: Long,
                ): Unit

                suspend fun actionsDisableSelectedRepositoryGithubActionsOrganization(
                    org: String,
                    repositoryId: Long,
                ): Unit
            }

            interface SelectedActions {
                suspend fun actionsGetAllowedActionsOrganization(
                    org: String,
                ): SelectedActions

                suspend fun actionsSetAllowedActionsOrganization(
                    org: String,
                    body: SelectedActions? = null,
                ): Unit
            }

            interface SelfHostedRunners {
                val repositories: Orgs.Actions.Permissions.SelfHostedRunners.RepositoriesApi

                @Serializable
                @JvmInline
                value class ActionsSetSelfHostedRunnersPermissionsOrganizationBody(@SerialName("enabled_repositories") val enabledRepositories: EnabledRepositories) {
                    @Serializable
                    enum class EnabledRepositories {
                        @SerialName("all") All, @SerialName("selected") Selected, @SerialName("none") None;
                    }
                }

                sealed interface ActionsGetSelfHostedRunnersPermissionsOrganizationResult {
                    data class OK(val value: SelfHostedRunnersSettings) : ActionsGetSelfHostedRunnersPermissionsOrganizationResult

                    data class Forbidden(val value: BasicError) : ActionsGetSelfHostedRunnersPermissionsOrganizationResult

                    data class NotFound(val value: BasicError) : ActionsGetSelfHostedRunnersPermissionsOrganizationResult
                }

                suspend fun actionsGetSelfHostedRunnersPermissionsOrganization(
                    org: String,
                ): ActionsGetSelfHostedRunnersPermissionsOrganizationResult

                sealed interface ActionsSetSelfHostedRunnersPermissionsOrganizationResult {
                    data object NoContent : ActionsSetSelfHostedRunnersPermissionsOrganizationResult

                    data class Forbidden(val value: BasicError) : ActionsSetSelfHostedRunnersPermissionsOrganizationResult

                    data class NotFound(val value: BasicError) : ActionsSetSelfHostedRunnersPermissionsOrganizationResult

                    data class Conflict(val value: BasicError) : ActionsSetSelfHostedRunnersPermissionsOrganizationResult

                    data class UnprocessableEntity(val value: ValidationError) : ActionsSetSelfHostedRunnersPermissionsOrganizationResult
                }

                suspend fun actionsSetSelfHostedRunnersPermissionsOrganization(
                    org: String,
                    body: ActionsSetSelfHostedRunnersPermissionsOrganizationBody,
                ): ActionsSetSelfHostedRunnersPermissionsOrganizationResult

                interface RepositoriesApi {
                    @Serializable
                    data class ActionsListSelectedRepositoriesSelfHostedRunnersOrganizationResponse(
                        @SerialName("total_count") val totalCount: Long? = null,
                        val repositories: List<Repository>? = null,
                    )


                    @Serializable
                    @JvmInline
                    value class ActionsSetSelectedRepositoriesSelfHostedRunnersOrganizationBody(@SerialName("selected_repository_ids") val selectedRepositoryIds: List<Long>)

                    sealed interface ActionsListSelectedRepositoriesSelfHostedRunnersOrganizationResult {
                        data class OK(val value: ActionsListSelectedRepositoriesSelfHostedRunnersOrganizationResponse) : ActionsListSelectedRepositoriesSelfHostedRunnersOrganizationResult

                        data class Forbidden(val value: BasicError) : ActionsListSelectedRepositoriesSelfHostedRunnersOrganizationResult

                        data class NotFound(val value: BasicError) : ActionsListSelectedRepositoriesSelfHostedRunnersOrganizationResult
                    }

                    suspend fun actionsListSelectedRepositoriesSelfHostedRunnersOrganization(
                        org: String,
                        page: Long = 1L,
                        perPage: Long = 30L,
                    ): ActionsListSelectedRepositoriesSelfHostedRunnersOrganizationResult

                    sealed interface ActionsSetSelectedRepositoriesSelfHostedRunnersOrganizationResult {
                        data object NoContent : ActionsSetSelectedRepositoriesSelfHostedRunnersOrganizationResult

                        data class Forbidden(val value: BasicError) : ActionsSetSelectedRepositoriesSelfHostedRunnersOrganizationResult

                        data class NotFound(val value: BasicError) : ActionsSetSelectedRepositoriesSelfHostedRunnersOrganizationResult

                        data class UnprocessableEntity(val value: ValidationError) : ActionsSetSelectedRepositoriesSelfHostedRunnersOrganizationResult
                    }

                    suspend fun actionsSetSelectedRepositoriesSelfHostedRunnersOrganization(
                        org: String,
                        body: ActionsSetSelectedRepositoriesSelfHostedRunnersOrganizationBody,
                    ): ActionsSetSelectedRepositoriesSelfHostedRunnersOrganizationResult

                    sealed interface ActionsEnableSelectedRepositorySelfHostedRunnersOrganizationResult {
                        data object NoContent : ActionsEnableSelectedRepositorySelfHostedRunnersOrganizationResult

                        data class Forbidden(val value: BasicError) : ActionsEnableSelectedRepositorySelfHostedRunnersOrganizationResult

                        data class NotFound(val value: BasicError) : ActionsEnableSelectedRepositorySelfHostedRunnersOrganizationResult

                        data class Conflict(val value: BasicError) : ActionsEnableSelectedRepositorySelfHostedRunnersOrganizationResult

                        data class UnprocessableEntity(val value: ValidationError) : ActionsEnableSelectedRepositorySelfHostedRunnersOrganizationResult
                    }

                    suspend fun actionsEnableSelectedRepositorySelfHostedRunnersOrganization(
                        org: String,
                        repositoryId: Long,
                    ): ActionsEnableSelectedRepositorySelfHostedRunnersOrganizationResult

                    sealed interface ActionsDisableSelectedRepositorySelfHostedRunnersOrganizationResult {
                        data object NoContent : ActionsDisableSelectedRepositorySelfHostedRunnersOrganizationResult

                        data class Forbidden(val value: BasicError) : ActionsDisableSelectedRepositorySelfHostedRunnersOrganizationResult

                        data class NotFound(val value: BasicError) : ActionsDisableSelectedRepositorySelfHostedRunnersOrganizationResult

                        data class Conflict(val value: BasicError) : ActionsDisableSelectedRepositorySelfHostedRunnersOrganizationResult

                        data class UnprocessableEntity(val value: ValidationError) : ActionsDisableSelectedRepositorySelfHostedRunnersOrganizationResult
                    }

                    suspend fun actionsDisableSelectedRepositorySelfHostedRunnersOrganization(
                        org: String,
                        repositoryId: Long,
                    ): ActionsDisableSelectedRepositorySelfHostedRunnersOrganizationResult
                }
            }

            interface Workflow {
                suspend fun actionsGetGithubActionsDefaultWorkflowPermissionsOrganization(
                    org: String,
                ): ActionsGetDefaultWorkflowPermissions

                suspend fun actionsSetGithubActionsDefaultWorkflowPermissionsOrganization(
                    org: String,
                    body: ActionsSetDefaultWorkflowPermissions? = null,
                ): Unit
            }
        }

        interface RunnerGroups {
            val hostedRunners: Orgs.Actions.RunnerGroups.HostedRunnersApi

            val repositories: Orgs.Actions.RunnerGroups.Repositories

            val runners: Orgs.Actions.RunnerGroups.RunnersApi

            @Serializable
            data class ActionsCreateSelfHostedRunnerGroupForOrgBody(
                val name: String,
                val visibility: Visibility? = null,
                @SerialName("selected_repository_ids") val selectedRepositoryIds: List<Long>? = null,
                val runners: List<Long>? = null,
                @SerialName("allows_public_repositories") val allowsPublicRepositories: Boolean? = null,
                @SerialName("restricted_to_workflows") val restrictedToWorkflows: Boolean? = null,
                @SerialName("selected_workflows") val selectedWorkflows: List<String>? = null,
                @SerialName("network_configuration_id") val networkConfigurationId: String? = null,
            ) {
                @Serializable
                enum class Visibility {
                    @SerialName("selected") Selected, @SerialName("all") All, @SerialName("private") Private;
                }
            }


            @Serializable
            data class ActionsListSelfHostedRunnerGroupsForOrgResponse(
                @SerialName("total_count") val totalCount: Double,
                @SerialName("runner_groups") val runnerGroups: List<RunnerGroupsOrg>,
            )


            @Serializable
            data class ActionsUpdateSelfHostedRunnerGroupForOrgBody(
                val name: String,
                val visibility: Visibility? = null,
                @SerialName("allows_public_repositories") val allowsPublicRepositories: Boolean? = null,
                @SerialName("restricted_to_workflows") val restrictedToWorkflows: Boolean? = null,
                @SerialName("selected_workflows") val selectedWorkflows: List<String>? = null,
                @SerialName("network_configuration_id") val networkConfigurationId: String? = null,
            ) {
                @Serializable
                enum class Visibility {
                    @SerialName("selected") Selected, @SerialName("all") All, @SerialName("private") Private;
                }
            }

            suspend fun actionsListSelfHostedRunnerGroupsForOrg(
                org: String,
                page: Long = 1L,
                perPage: Long = 30L,
                visibleToRepository: String? = null,
            ): ActionsListSelfHostedRunnerGroupsForOrgResponse

            suspend fun actionsCreateSelfHostedRunnerGroupForOrg(
                org: String,
                body: ActionsCreateSelfHostedRunnerGroupForOrgBody,
            ): RunnerGroupsOrg

            suspend fun actionsGetSelfHostedRunnerGroupForOrg(
                org: String,
                runnerGroupId: Long,
            ): RunnerGroupsOrg

            suspend fun actionsDeleteSelfHostedRunnerGroupFromOrg(
                org: String,
                runnerGroupId: Long,
            ): Unit

            suspend fun actionsUpdateSelfHostedRunnerGroupForOrg(
                org: String,
                runnerGroupId: Long,
                body: ActionsUpdateSelfHostedRunnerGroupForOrgBody,
            ): RunnerGroupsOrg

            interface HostedRunnersApi {
                @Serializable
                data class ActionsListGithubHostedRunnersInGroupForOrgResponse(
                    @SerialName("total_count") val totalCount: Double,
                    val runners: List<ActionsHostedRunner>,
                )

                suspend fun actionsListGithubHostedRunnersInGroupForOrg(
                    org: String,
                    runnerGroupId: Long,
                    page: Long = 1L,
                    perPage: Long = 30L,
                ): ActionsListGithubHostedRunnersInGroupForOrgResponse
            }

            interface Repositories {
                @Serializable
                data class ActionsListRepoAccessToSelfHostedRunnerGroupInOrgResponse(
                    @SerialName("total_count") val totalCount: Double,
                    val repositories: List<MinimalRepository>,
                )


                @Serializable
                @JvmInline
                value class ActionsSetRepoAccessToSelfHostedRunnerGroupInOrgBody(@SerialName("selected_repository_ids") val selectedRepositoryIds: List<Long>)

                suspend fun actionsListRepoAccessToSelfHostedRunnerGroupInOrg(
                    org: String,
                    runnerGroupId: Long,
                    page: Long = 1L,
                    perPage: Long = 30L,
                ): ActionsListRepoAccessToSelfHostedRunnerGroupInOrgResponse

                suspend fun actionsSetRepoAccessToSelfHostedRunnerGroupInOrg(
                    org: String,
                    runnerGroupId: Long,
                    body: ActionsSetRepoAccessToSelfHostedRunnerGroupInOrgBody,
                ): Unit

                suspend fun actionsAddRepoAccessToSelfHostedRunnerGroupInOrg(
                    org: String,
                    runnerGroupId: Long,
                    repositoryId: Long,
                ): Unit

                suspend fun actionsRemoveRepoAccessToSelfHostedRunnerGroupInOrg(
                    org: String,
                    runnerGroupId: Long,
                    repositoryId: Long,
                ): Unit
            }

            interface RunnersApi {
                @Serializable
                data class ActionsListSelfHostedRunnersInGroupForOrgResponse(
                    @SerialName("total_count") val totalCount: Double,
                    val runners: List<Runner>,
                )


                @Serializable
                @JvmInline
                value class ActionsSetSelfHostedRunnersInGroupForOrgBody(val runners: List<Long>)

                suspend fun actionsListSelfHostedRunnersInGroupForOrg(
                    org: String,
                    runnerGroupId: Long,
                    page: Long = 1L,
                    perPage: Long = 30L,
                ): ActionsListSelfHostedRunnersInGroupForOrgResponse

                suspend fun actionsSetSelfHostedRunnersInGroupForOrg(
                    org: String,
                    runnerGroupId: Long,
                    body: ActionsSetSelfHostedRunnersInGroupForOrgBody,
                ): Unit

                suspend fun actionsAddSelfHostedRunnerToGroupForOrg(
                    org: String,
                    runnerGroupId: Long,
                    runnerId: Long,
                ): Unit

                suspend fun actionsRemoveSelfHostedRunnerFromGroupForOrg(
                    org: String,
                    runnerGroupId: Long,
                    runnerId: Long,
                ): Unit
            }
        }

        interface Runners {
            val downloads: Orgs.Actions.Runners.Downloads

            val generateJitconfig: Orgs.Actions.Runners.GenerateJitconfig

            val registrationToken: Orgs.Actions.Runners.RegistrationToken

            val removeToken: Orgs.Actions.Runners.RemoveToken

            val labels: Orgs.Actions.Runners.Labels

            @Serializable
            data class ActionsListSelfHostedRunnersForOrgResponse(
                @SerialName("total_count") val totalCount: Long,
                val runners: List<Runner>,
            )

            suspend fun actionsListSelfHostedRunnersForOrg(
                org: String,
                page: Long = 1L,
                perPage: Long = 30L,
                name: String? = null,
            ): ActionsListSelfHostedRunnersForOrgResponse

            suspend fun actionsGetSelfHostedRunnerForOrg(
                org: String,
                runnerId: Long,
            ): Runner

            sealed interface ActionsDeleteSelfHostedRunnerFromOrgResult {
                data object NoContent : ActionsDeleteSelfHostedRunnerFromOrgResult

                data class UnprocessableEntity(val value: ValidationErrorSimple) : ActionsDeleteSelfHostedRunnerFromOrgResult
            }

            suspend fun actionsDeleteSelfHostedRunnerFromOrg(
                org: String,
                runnerId: Long,
            ): ActionsDeleteSelfHostedRunnerFromOrgResult

            interface Downloads {
                suspend fun actionsListRunnerApplicationsForOrg(
                    org: String,
                ): List<RunnerApplication>
            }

            interface GenerateJitconfig {
                @Serializable
                data class ActionsGenerateRunnerJitconfigForOrgBody(
                    val name: String,
                    @SerialName("runner_group_id") val runnerGroupId: Long,
                    val labels: List<String>,
                    @SerialName("work_folder") val workFolder: String? = null,
                )


                @Serializable
                data class ActionsGenerateRunnerJitconfigForOrgResponse(
                    val runner: Runner,
                    @SerialName("encoded_jit_config") val encodedJitConfig: String,
                )

                sealed interface ActionsGenerateRunnerJitconfigForOrgResult {
                    data class Created(val value: ActionsGenerateRunnerJitconfigForOrgResponse) : ActionsGenerateRunnerJitconfigForOrgResult

                    data class NotFound(val value: BasicError) : ActionsGenerateRunnerJitconfigForOrgResult

                    data class Conflict(val value: BasicError) : ActionsGenerateRunnerJitconfigForOrgResult

                    data class UnprocessableEntity(val value: ValidationErrorSimple) : ActionsGenerateRunnerJitconfigForOrgResult
                }

                suspend fun actionsGenerateRunnerJitconfigForOrg(
                    org: String,
                    body: ActionsGenerateRunnerJitconfigForOrgBody,
                ): ActionsGenerateRunnerJitconfigForOrgResult
            }

            interface RegistrationToken {
                suspend fun actionsCreateRegistrationTokenForOrg(
                    org: String,
                ): AuthenticationToken
            }

            interface RemoveToken {
                suspend fun actionsCreateRemoveTokenForOrg(
                    org: String,
                ): AuthenticationToken
            }

            interface Labels {
                @Serializable
                @JvmInline
                value class ActionsAddCustomLabelsToSelfHostedRunnerForOrgBody(val labels: List<String>)


                @Serializable
                data class ActionsAddCustomLabelsToSelfHostedRunnerForOrgResponse(
                    @SerialName("total_count") val totalCount: Long,
                    val labels: List<RunnerLabel>,
                )


                @Serializable
                data class ActionsListLabelsForSelfHostedRunnerForOrgResponse(
                    @SerialName("total_count") val totalCount: Long,
                    val labels: List<RunnerLabel>,
                )


                @Serializable
                data class ActionsRemoveAllCustomLabelsFromSelfHostedRunnerForOrgResponse(
                    @SerialName("total_count") val totalCount: Long,
                    val labels: List<RunnerLabel>,
                )


                @Serializable
                data class ActionsRemoveCustomLabelFromSelfHostedRunnerForOrgResponse(
                    @SerialName("total_count") val totalCount: Long,
                    val labels: List<RunnerLabel>,
                )


                @Serializable
                @JvmInline
                value class ActionsSetCustomLabelsForSelfHostedRunnerForOrgBody(val labels: List<String>)


                @Serializable
                data class ActionsSetCustomLabelsForSelfHostedRunnerForOrgResponse(
                    @SerialName("total_count") val totalCount: Long,
                    val labels: List<RunnerLabel>,
                )

                sealed interface ActionsListLabelsForSelfHostedRunnerForOrgResult {
                    data class OK(val value: ActionsListLabelsForSelfHostedRunnerForOrgResponse) : ActionsListLabelsForSelfHostedRunnerForOrgResult

                    data class NotFound(val value: BasicError) : ActionsListLabelsForSelfHostedRunnerForOrgResult
                }

                suspend fun actionsListLabelsForSelfHostedRunnerForOrg(
                    org: String,
                    runnerId: Long,
                ): ActionsListLabelsForSelfHostedRunnerForOrgResult

                sealed interface ActionsSetCustomLabelsForSelfHostedRunnerForOrgResult {
                    data class OK(val value: ActionsSetCustomLabelsForSelfHostedRunnerForOrgResponse) : ActionsSetCustomLabelsForSelfHostedRunnerForOrgResult

                    data class NotFound(val value: BasicError) : ActionsSetCustomLabelsForSelfHostedRunnerForOrgResult

                    data class UnprocessableEntity(val value: ValidationErrorSimple) : ActionsSetCustomLabelsForSelfHostedRunnerForOrgResult
                }

                suspend fun actionsSetCustomLabelsForSelfHostedRunnerForOrg(
                    org: String,
                    runnerId: Long,
                    body: ActionsSetCustomLabelsForSelfHostedRunnerForOrgBody,
                ): ActionsSetCustomLabelsForSelfHostedRunnerForOrgResult

                sealed interface ActionsAddCustomLabelsToSelfHostedRunnerForOrgResult {
                    data class OK(val value: ActionsAddCustomLabelsToSelfHostedRunnerForOrgResponse) : ActionsAddCustomLabelsToSelfHostedRunnerForOrgResult

                    data class NotFound(val value: BasicError) : ActionsAddCustomLabelsToSelfHostedRunnerForOrgResult

                    data class UnprocessableEntity(val value: ValidationErrorSimple) : ActionsAddCustomLabelsToSelfHostedRunnerForOrgResult
                }

                suspend fun actionsAddCustomLabelsToSelfHostedRunnerForOrg(
                    org: String,
                    runnerId: Long,
                    body: ActionsAddCustomLabelsToSelfHostedRunnerForOrgBody,
                ): ActionsAddCustomLabelsToSelfHostedRunnerForOrgResult

                sealed interface ActionsRemoveAllCustomLabelsFromSelfHostedRunnerForOrgResult {
                    data class OK(val value: ActionsRemoveAllCustomLabelsFromSelfHostedRunnerForOrgResponse) : ActionsRemoveAllCustomLabelsFromSelfHostedRunnerForOrgResult

                    data class NotFound(val value: BasicError) : ActionsRemoveAllCustomLabelsFromSelfHostedRunnerForOrgResult
                }

                suspend fun actionsRemoveAllCustomLabelsFromSelfHostedRunnerForOrg(
                    org: String,
                    runnerId: Long,
                ): ActionsRemoveAllCustomLabelsFromSelfHostedRunnerForOrgResult

                sealed interface ActionsRemoveCustomLabelFromSelfHostedRunnerForOrgResult {
                    data class OK(val value: ActionsRemoveCustomLabelFromSelfHostedRunnerForOrgResponse) : ActionsRemoveCustomLabelFromSelfHostedRunnerForOrgResult

                    data class NotFound(val value: BasicError) : ActionsRemoveCustomLabelFromSelfHostedRunnerForOrgResult

                    data class UnprocessableEntity(val value: ValidationErrorSimple) : ActionsRemoveCustomLabelFromSelfHostedRunnerForOrgResult
                }

                suspend fun actionsRemoveCustomLabelFromSelfHostedRunnerForOrg(
                    org: String,
                    runnerId: Long,
                    name: String,
                ): ActionsRemoveCustomLabelFromSelfHostedRunnerForOrgResult
            }
        }

        interface Secrets {
            val publicKey: Orgs.Actions.Secrets.PublicKey

            val repositories: Orgs.Actions.Secrets.Repositories

            @Serializable
            data class ActionsCreateOrUpdateOrgSecretBody(
                @SerialName("encrypted_value") val encryptedValue: String,
                @SerialName("key_id") val keyId: String,
                val visibility: Visibility,
                @SerialName("selected_repository_ids") val selectedRepositoryIds: List<Long>? = null,
            ) {
                @Serializable
                enum class Visibility {
                    @SerialName("all") All, @SerialName("private") Private, @SerialName("selected") Selected;
                }
            }


            @Serializable
            data class ActionsListOrgSecretsResponse(
                @SerialName("total_count") val totalCount: Long,
                val secrets: List<OrganizationActionsSecret>,
            )

            suspend fun actionsListOrgSecrets(
                org: String,
                page: Long = 1L,
                perPage: Long = 30L,
            ): ActionsListOrgSecretsResponse

            suspend fun actionsGetOrgSecret(
                org: String,
                secretName: String,
            ): OrganizationActionsSecret

            sealed interface ActionsCreateOrUpdateOrgSecretResult {
                data class Created(val value: EmptyObject) : ActionsCreateOrUpdateOrgSecretResult

                data object NoContent : ActionsCreateOrUpdateOrgSecretResult
            }

            suspend fun actionsCreateOrUpdateOrgSecret(
                org: String,
                secretName: String,
                body: ActionsCreateOrUpdateOrgSecretBody,
            ): ActionsCreateOrUpdateOrgSecretResult

            suspend fun actionsDeleteOrgSecret(
                org: String,
                secretName: String,
            ): Unit

            interface PublicKey {
                suspend fun actionsGetOrgPublicKey(
                    org: String,
                ): ActionsPublicKey
            }

            interface Repositories {
                @Serializable
                data class ActionsListSelectedReposForOrgSecretResponse(
                    @SerialName("total_count") val totalCount: Long,
                    val repositories: List<MinimalRepository>,
                )


                @Serializable
                @JvmInline
                value class ActionsSetSelectedReposForOrgSecretBody(@SerialName("selected_repository_ids") val selectedRepositoryIds: List<Long>)

                suspend fun actionsListSelectedReposForOrgSecret(
                    org: String,
                    secretName: String,
                    page: Long = 1L,
                    perPage: Long = 30L,
                ): ActionsListSelectedReposForOrgSecretResponse

                suspend fun actionsSetSelectedReposForOrgSecret(
                    org: String,
                    secretName: String,
                    body: ActionsSetSelectedReposForOrgSecretBody,
                ): Unit

                sealed interface ActionsAddSelectedRepoToOrgSecretResult {
                    data object NoContent : ActionsAddSelectedRepoToOrgSecretResult

                    data object Conflict : ActionsAddSelectedRepoToOrgSecretResult
                }

                suspend fun actionsAddSelectedRepoToOrgSecret(
                    org: String,
                    secretName: String,
                    repositoryId: Long,
                ): ActionsAddSelectedRepoToOrgSecretResult

                sealed interface ActionsRemoveSelectedRepoFromOrgSecretResult {
                    data object NoContent : ActionsRemoveSelectedRepoFromOrgSecretResult

                    data object Conflict : ActionsRemoveSelectedRepoFromOrgSecretResult
                }

                suspend fun actionsRemoveSelectedRepoFromOrgSecret(
                    org: String,
                    secretName: String,
                    repositoryId: Long,
                ): ActionsRemoveSelectedRepoFromOrgSecretResult
            }
        }

        interface Variables {
            val repositories: Orgs.Actions.Variables.Repositories

            @Serializable
            data class ActionsCreateOrgVariableBody(
                val name: String,
                val value: String,
                val visibility: Visibility,
                @SerialName("selected_repository_ids") val selectedRepositoryIds: List<Long>? = null,
            ) {
                @Serializable
                enum class Visibility {
                    @SerialName("all") All, @SerialName("private") Private, @SerialName("selected") Selected;
                }
            }


            @Serializable
            data class ActionsListOrgVariablesResponse(
                @SerialName("total_count") val totalCount: Long,
                val variables: List<OrganizationActionsVariable>,
            )


            @Serializable
            data class ActionsUpdateOrgVariableBody(
                val name: String? = null,
                val value: String? = null,
                val visibility: Visibility? = null,
                @SerialName("selected_repository_ids") val selectedRepositoryIds: List<Long>? = null,
            ) {
                @Serializable
                enum class Visibility {
                    @SerialName("all") All, @SerialName("private") Private, @SerialName("selected") Selected;
                }
            }

            suspend fun actionsListOrgVariables(
                org: String,
                page: Long = 1L,
                perPage: Long = 10L,
            ): ActionsListOrgVariablesResponse

            suspend fun actionsCreateOrgVariable(
                org: String,
                body: ActionsCreateOrgVariableBody,
            ): EmptyObject

            suspend fun actionsGetOrgVariable(
                org: String,
                name: String,
            ): OrganizationActionsVariable

            suspend fun actionsDeleteOrgVariable(
                org: String,
                name: String,
            ): Unit

            suspend fun actionsUpdateOrgVariable(
                org: String,
                name: String,
                body: ActionsUpdateOrgVariableBody,
            ): Unit

            interface Repositories {
                @Serializable
                data class ActionsListSelectedReposForOrgVariableResponse(
                    @SerialName("total_count") val totalCount: Long,
                    val repositories: List<MinimalRepository>,
                )


                @Serializable
                @JvmInline
                value class ActionsSetSelectedReposForOrgVariableBody(@SerialName("selected_repository_ids") val selectedRepositoryIds: List<Long>)

                sealed interface ActionsListSelectedReposForOrgVariableResult {
                    data class OK(val value: ActionsListSelectedReposForOrgVariableResponse) : ActionsListSelectedReposForOrgVariableResult

                    data object Conflict : ActionsListSelectedReposForOrgVariableResult
                }

                suspend fun actionsListSelectedReposForOrgVariable(
                    org: String,
                    name: String,
                    page: Long = 1L,
                    perPage: Long = 30L,
                ): ActionsListSelectedReposForOrgVariableResult

                sealed interface ActionsSetSelectedReposForOrgVariableResult {
                    data object NoContent : ActionsSetSelectedReposForOrgVariableResult

                    data object Conflict : ActionsSetSelectedReposForOrgVariableResult
                }

                suspend fun actionsSetSelectedReposForOrgVariable(
                    org: String,
                    name: String,
                    body: ActionsSetSelectedReposForOrgVariableBody,
                ): ActionsSetSelectedReposForOrgVariableResult

                sealed interface ActionsAddSelectedRepoToOrgVariableResult {
                    data object NoContent : ActionsAddSelectedRepoToOrgVariableResult

                    data object Conflict : ActionsAddSelectedRepoToOrgVariableResult
                }

                suspend fun actionsAddSelectedRepoToOrgVariable(
                    org: String,
                    name: String,
                    repositoryId: Long,
                ): ActionsAddSelectedRepoToOrgVariableResult

                sealed interface ActionsRemoveSelectedRepoFromOrgVariableResult {
                    data object NoContent : ActionsRemoveSelectedRepoFromOrgVariableResult

                    data object Conflict : ActionsRemoveSelectedRepoFromOrgVariableResult
                }

                suspend fun actionsRemoveSelectedRepoFromOrgVariable(
                    org: String,
                    name: String,
                    repositoryId: Long,
                ): ActionsRemoveSelectedRepoFromOrgVariableResult
            }
        }
    }

    interface Artifacts {
        val metadata: Orgs.Artifacts.Metadata

        interface Metadata {
            val deploymentRecord: Orgs.Artifacts.Metadata.DeploymentRecord

            val storageRecord: Orgs.Artifacts.Metadata.StorageRecord

            val deploymentRecords: Orgs.Artifacts.Metadata.DeploymentRecords

            val storageRecords: Orgs.Artifacts.Metadata.StorageRecords

            interface DeploymentRecord {
                val cluster: Orgs.Artifacts.Metadata.DeploymentRecord.Cluster

                @Serializable
                data class OrgsCreateArtifactDeploymentRecordBody(
                    val name: String,
                    val digest: String,
                    val version: String? = null,
                    val status: Status,
                    @SerialName("logical_environment") val logicalEnvironment: String,
                    @SerialName("physical_environment") val physicalEnvironment: String? = null,
                    val cluster: String? = null,
                    @SerialName("deployment_name") val deploymentName: String,
                    val tags: List<String>? = null,
                    @SerialName("runtime_risks") val runtimeRisks: List<RuntimeRisks>? = null,
                    @SerialName("github_repository") val githubRepository: String? = null,
                ) {
                    @Serializable
                    enum class Status {
                        @SerialName("deployed") Deployed, @SerialName("decommissioned") Decommissioned;
                    }

                    @Serializable
                    enum class RuntimeRisks {
                        @SerialName("critical-resource")
                        CriticalResource,
                        @SerialName("internet-exposed")
                        InternetExposed,
                        @SerialName("lateral-movement")
                        LateralMovement,
                        @SerialName("sensitive-data")
                        SensitiveData;
                    }
                }


                @Serializable
                data class OrgsCreateArtifactDeploymentRecordResponse(
                    @SerialName("total_count") val totalCount: Long? = null,
                    @SerialName("deployment_records") val deploymentRecords: List<ArtifactDeploymentRecord>? = null,
                )

                suspend fun orgsCreateArtifactDeploymentRecord(
                    org: String,
                    body: OrgsCreateArtifactDeploymentRecordBody,
                ): OrgsCreateArtifactDeploymentRecordResponse

                interface Cluster {
                    @Serializable
                    data class OrgsSetClusterDeploymentRecordsBody(
                        @SerialName("logical_environment") val logicalEnvironment: String,
                        @SerialName("physical_environment") val physicalEnvironment: String? = null,
                        val deployments: List<Deployments>,
                    ) {
                        @Serializable
                        data class Deployments(
                            val name: String,
                            val digest: String,
                            val version: String? = null,
                            val status: Status? = null,
                            @SerialName("deployment_name") val deploymentName: String,
                            @SerialName("github_repository") val githubRepository: String? = null,
                            val tags: List<String>? = null,
                            @SerialName("runtime_risks") val runtimeRisks: List<RuntimeRisks>? = null,
                        ) {
                            @Serializable
                            enum class Status {
                                @SerialName("deployed") Deployed, @SerialName("decommissioned") Decommissioned;
                            }

                            @Serializable
                            enum class RuntimeRisks {
                                @SerialName("critical-resource")
                                CriticalResource,
                                @SerialName("internet-exposed")
                                InternetExposed,
                                @SerialName("lateral-movement")
                                LateralMovement,
                                @SerialName("sensitive-data")
                                SensitiveData;
                            }
                        }
                    }


                    @Serializable
                    data class OrgsSetClusterDeploymentRecordsResponse(
                        @SerialName("total_count") val totalCount: Long? = null,
                        @SerialName("deployment_records") val deploymentRecords: List<ArtifactDeploymentRecord>? = null,
                    )

                    suspend fun orgsSetClusterDeploymentRecords(
                        org: String,
                        cluster: String,
                        body: OrgsSetClusterDeploymentRecordsBody,
                    ): OrgsSetClusterDeploymentRecordsResponse
                }
            }

            interface StorageRecord {
                @Serializable
                data class OrgsCreateArtifactStorageRecordBody(
                    val name: String,
                    val digest: String,
                    val version: String? = null,
                    @SerialName("artifact_url") val artifactUrl: String? = null,
                    val path: String? = null,
                    @SerialName("registry_url") val registryUrl: String,
                    val repository: String? = null,
                    val status: Status? = null,
                    @SerialName("github_repository") val githubRepository: String? = null,
                ) {
                    @Serializable
                    enum class Status {
                        @SerialName("active") Active, @SerialName("eol") Eol, @SerialName("deleted") Deleted;
                    }
                }


                @Serializable
                data class OrgsCreateArtifactStorageRecordResponse(
                    @SerialName("total_count") val totalCount: Long? = null,
                    @SerialName("storage_records") val storageRecords: List<StorageRecords>? = null,
                ) {
                    @Serializable
                    data class StorageRecords(
                        val id: Long? = null,
                        val name: String? = null,
                        val digest: String? = null,
                        @SerialName("artifact_url") val artifactUrl: String? = null,
                        @SerialName("registry_url") val registryUrl: String? = null,
                        val repository: String? = null,
                        val status: String? = null,
                        @SerialName("created_at") val createdAt: String? = null,
                        @SerialName("updated_at") val updatedAt: String? = null,
                    )
                }

                suspend fun orgsCreateArtifactStorageRecord(
                    org: String,
                    body: OrgsCreateArtifactStorageRecordBody,
                ): OrgsCreateArtifactStorageRecordResponse
            }

            interface DeploymentRecords {
                @Serializable
                data class OrgsListArtifactDeploymentRecordsResponse(
                    @SerialName("total_count") val totalCount: Long? = null,
                    @SerialName("deployment_records") val deploymentRecords: List<ArtifactDeploymentRecord>? = null,
                )

                suspend fun orgsListArtifactDeploymentRecords(
                    org: String,
                    subjectDigest: String,
                ): OrgsListArtifactDeploymentRecordsResponse
            }

            interface StorageRecords {
                @Serializable
                data class OrgsListArtifactStorageRecordsResponse(
                    @SerialName("total_count") val totalCount: Long? = null,
                    @SerialName("storage_records") val storageRecords: List<StorageRecords>? = null,
                ) {
                    @Serializable
                    data class StorageRecords(
                        val id: Long? = null,
                        val name: String? = null,
                        val digest: String? = null,
                        @SerialName("artifact_url") val artifactUrl: String? = null,
                        @SerialName("registry_url") val registryUrl: String? = null,
                        val repository: String? = null,
                        val status: String? = null,
                        @SerialName("created_at") val createdAt: String? = null,
                        @SerialName("updated_at") val updatedAt: String? = null,
                    )
                }

                suspend fun orgsListArtifactStorageRecords(
                    org: String,
                    subjectDigest: String,
                ): OrgsListArtifactStorageRecordsResponse
            }
        }
    }

    interface Attestations {
        val bulkList: Orgs.Attestations.BulkList

        val deleteRequest: Orgs.Attestations.DeleteRequest

        val digest: Orgs.Attestations.Digest

        val repositories: Orgs.Attestations.Repositories

        @Serializable
        @JvmInline
        value class OrgsListAttestationsResponse(val attestations: List<Attestations>? = null) {
            @Serializable
            data class Attestations(
                val bundle: Bundle? = null,
                @SerialName("repository_id") val repositoryId: Long? = null,
                @SerialName("bundle_url") val bundleUrl: String? = null,
                val initiator: String? = null,
            ) {
                @Serializable
                data class Bundle(
                    val mediaType: String? = null,
                    val verificationMaterial: JsonElement? = null,
                    val dsseEnvelope: JsonElement? = null,
                )
            }
        }

        sealed interface OrgsDeleteAttestationsByIdResult {
            data object OK : OrgsDeleteAttestationsByIdResult

            data object NoContent : OrgsDeleteAttestationsByIdResult

            data class Forbidden(val value: BasicError) : OrgsDeleteAttestationsByIdResult

            data class NotFound(val value: BasicError) : OrgsDeleteAttestationsByIdResult
        }

        suspend fun orgsDeleteAttestationsById(
            org: String,
            attestationId: Long,
        ): OrgsDeleteAttestationsByIdResult

        suspend fun orgsListAttestations(
            org: String,
            subjectDigest: String,
            perPage: Long = 30L,
            after: String? = null,
            before: String? = null,
            predicateType: String? = null,
        ): OrgsListAttestationsResponse

        interface BulkList {
            @Serializable
            data class OrgsListAttestationsBulkBody(
                @SerialName("subject_digests") val subjectDigests: List<String>,
                @SerialName("predicate_type") val predicateType: String? = null,
            )


            @Serializable
            data class OrgsListAttestationsBulkResponse(
                @SerialName("attestations_subject_digests") val attestationsSubjectDigests: List<List<AttestationsSubjectDigests>>? = null,
                @SerialName("page_info") val pageInfo: PageInfo? = null,
            ) {
                @Serializable
                data class AttestationsSubjectDigests(
                    val bundle: Bundle? = null,
                    @SerialName("repository_id") val repositoryId: Long? = null,
                    @SerialName("bundle_url") val bundleUrl: String? = null,
                ) {
                    @Serializable
                    data class Bundle(
                        val mediaType: String? = null,
                        val verificationMaterial: JsonElement? = null,
                        val dsseEnvelope: JsonElement? = null,
                    )
                }

                @Serializable
                data class PageInfo(
                    @SerialName("has_next") val hasNext: Boolean? = null,
                    @SerialName("has_previous") val hasPrevious: Boolean? = null,
                    val next: String? = null,
                    val previous: String? = null,
                )
            }

            suspend fun orgsListAttestationsBulk(
                org: String,
                perPage: Long = 30L,
                body: OrgsListAttestationsBulkBody,
                after: String? = null,
                before: String? = null,
            ): OrgsListAttestationsBulkResponse
        }

        interface DeleteRequest {
            sealed interface OrgsDeleteAttestationsBulkResult {
                data object OK : OrgsDeleteAttestationsBulkResult

                data class NotFound(val value: BasicError) : OrgsDeleteAttestationsBulkResult
            }

            suspend fun orgsDeleteAttestationsBulk(
                org: String,
                body: JsonElement,
            ): OrgsDeleteAttestationsBulkResult
        }

        interface Digest {
            sealed interface OrgsDeleteAttestationsBySubjectDigestResult {
                data object OK : OrgsDeleteAttestationsBySubjectDigestResult

                data object NoContent : OrgsDeleteAttestationsBySubjectDigestResult

                data class NotFound(val value: BasicError) : OrgsDeleteAttestationsBySubjectDigestResult
            }

            suspend fun orgsDeleteAttestationsBySubjectDigest(
                org: String,
                subjectDigest: String,
            ): OrgsDeleteAttestationsBySubjectDigestResult
        }

        interface Repositories {
            @Serializable
            data class OrgsListAttestationRepositoriesResponse(val id: Long? = null, val name: String? = null)

            suspend fun orgsListAttestationRepositories(
                org: String,
                perPage: Long = 30L,
                after: String? = null,
                before: String? = null,
                predicateType: String? = null,
            ): List<OrgsListAttestationRepositoriesResponse>
        }
    }

    interface Blocks {
        suspend fun orgsListBlockedUsers(
            org: String,
            page: Long = 1L,
            perPage: Long = 30L,
        ): List<SimpleUser>

        sealed interface OrgsCheckBlockedUserResult {
            data object NoContent : OrgsCheckBlockedUserResult

            data class NotFound(val value: BasicError) : OrgsCheckBlockedUserResult
        }

        suspend fun orgsCheckBlockedUser(
            org: String,
            username: String,
        ): OrgsCheckBlockedUserResult

        sealed interface OrgsBlockUserResult {
            data object NoContent : OrgsBlockUserResult

            data class UnprocessableEntity(val value: ValidationError) : OrgsBlockUserResult
        }

        suspend fun orgsBlockUser(
            org: String,
            username: String,
        ): OrgsBlockUserResult

        suspend fun orgsUnblockUser(
            org: String,
            username: String,
        ): Unit
    }

    interface Campaigns {
        @Serializable
        data class CampaignsCreateCampaignBody(
            val name: String,
            val description: String,
            val managers: List<String>? = null,
            @SerialName("team_managers") val teamManagers: List<String>? = null,
            @SerialName("ends_at") val endsAt: LocalDateTime,
            @SerialName("contact_link") val contactLink: String? = null,
            @SerialName("code_scanning_alerts") val codeScanningAlerts: List<CodeScanningAlerts>? = null,
            @SerialName("generate_issues") val generateIssues: Boolean? = null,
        ) {
            @Serializable
            data class CodeScanningAlerts(
                @SerialName("repository_id") val repositoryId: Long,
                @SerialName("alert_numbers") val alertNumbers: List<Long>,
            )
        }


        @Serializable
        data class CampaignsCreateCampaignResponse(
            val code: String? = null,
            val message: String? = null,
            @SerialName("documentation_url") val documentationUrl: String? = null,
        )


        @Serializable
        data class CampaignsDeleteCampaignResponse(
            val code: String? = null,
            val message: String? = null,
            @SerialName("documentation_url") val documentationUrl: String? = null,
        )


        @Serializable
        data class CampaignsGetCampaignSummaryResponse(
            val code: String? = null,
            val message: String? = null,
            @SerialName("documentation_url") val documentationUrl: String? = null,
        )


        @Serializable
        data class CampaignsListOrgCampaignsResponse(
            val code: String? = null,
            val message: String? = null,
            @SerialName("documentation_url") val documentationUrl: String? = null,
        )


        @Serializable
        data class CampaignsUpdateCampaignBody(
            val name: String? = null,
            val description: String? = null,
            val managers: List<String>? = null,
            @SerialName("team_managers") val teamManagers: List<String>? = null,
            @SerialName("ends_at") val endsAt: LocalDateTime? = null,
            @SerialName("contact_link") val contactLink: String? = null,
            val state: CampaignState? = null,
        )


        @Serializable
        data class CampaignsUpdateCampaignResponse(
            val code: String? = null,
            val message: String? = null,
            @SerialName("documentation_url") val documentationUrl: String? = null,
        )


        @Serializable
        enum class Direction {
            @SerialName("asc") Asc, @SerialName("desc") Desc;
        }


        @Serializable
        enum class Sort {
            @SerialName("created")
            Created,
            @SerialName("updated")
            Updated,
            @SerialName("ends_at")
            EndsAt,
            @SerialName("published")
            Published;
        }

        sealed interface CampaignsListOrgCampaignsResult {
            data class OK(val value: List<CampaignSummary>) : CampaignsListOrgCampaignsResult

            data class NotFound(val value: BasicError) : CampaignsListOrgCampaignsResult

            data class ServiceUnavailable(val value: CampaignsListOrgCampaignsResponse) : CampaignsListOrgCampaignsResult
        }

        suspend fun campaignsListOrgCampaigns(
            org: String,
            direction: Direction = Direction.Desc,
            page: Long = 1L,
            perPage: Long = 30L,
            sort: Sort = Sort.Created,
            state: CampaignState? = null,
        ): CampaignsListOrgCampaignsResult

        sealed interface CampaignsCreateCampaignResult {
            data class OK(val value: CampaignSummary) : CampaignsCreateCampaignResult

            data class BadRequest(val value: BasicError) : CampaignsCreateCampaignResult

            data class NotFound(val value: BasicError) : CampaignsCreateCampaignResult

            data class UnprocessableEntity(val value: BasicError) : CampaignsCreateCampaignResult

            data object TooManyRequests : CampaignsCreateCampaignResult

            data class ServiceUnavailable(val value: CampaignsCreateCampaignResponse) : CampaignsCreateCampaignResult
        }

        suspend fun campaignsCreateCampaign(
            org: String,
            body: CampaignsCreateCampaignBody,
        ): CampaignsCreateCampaignResult

        sealed interface CampaignsGetCampaignSummaryResult {
            data class OK(val value: CampaignSummary) : CampaignsGetCampaignSummaryResult

            data class NotFound(val value: BasicError) : CampaignsGetCampaignSummaryResult

            data class UnprocessableEntity(val value: BasicError) : CampaignsGetCampaignSummaryResult

            data class ServiceUnavailable(val value: CampaignsGetCampaignSummaryResponse) : CampaignsGetCampaignSummaryResult
        }

        suspend fun campaignsGetCampaignSummary(
            org: String,
            campaignNumber: Long,
        ): CampaignsGetCampaignSummaryResult

        sealed interface CampaignsDeleteCampaignResult {
            data object NoContent : CampaignsDeleteCampaignResult

            data class NotFound(val value: BasicError) : CampaignsDeleteCampaignResult

            data class ServiceUnavailable(val value: CampaignsDeleteCampaignResponse) : CampaignsDeleteCampaignResult
        }

        suspend fun campaignsDeleteCampaign(
            org: String,
            campaignNumber: Long,
        ): CampaignsDeleteCampaignResult

        sealed interface CampaignsUpdateCampaignResult {
            data class OK(val value: CampaignSummary) : CampaignsUpdateCampaignResult

            data class BadRequest(val value: BasicError) : CampaignsUpdateCampaignResult

            data class NotFound(val value: BasicError) : CampaignsUpdateCampaignResult

            data class UnprocessableEntity(val value: BasicError) : CampaignsUpdateCampaignResult

            data class ServiceUnavailable(val value: CampaignsUpdateCampaignResponse) : CampaignsUpdateCampaignResult
        }

        suspend fun campaignsUpdateCampaign(
            org: String,
            campaignNumber: Long,
            body: CampaignsUpdateCampaignBody,
        ): CampaignsUpdateCampaignResult
    }

    interface CodeScanning {
        val alerts: Orgs.CodeScanning.Alerts

        interface Alerts {
            @Serializable
            data class CodeScanningListAlertsForOrgResponse(
                val code: String? = null,
                val message: String? = null,
                @SerialName("documentation_url") val documentationUrl: String? = null,
            )


            @Serializable
            enum class Direction {
                @SerialName("asc") Asc, @SerialName("desc") Desc;
            }


            @Serializable
            enum class Sort {
                @SerialName("created") Created, @SerialName("updated") Updated;
            }

            sealed interface CodeScanningListAlertsForOrgResult {
                data class OK(val value: List<CodeScanningOrganizationAlertItemsResponse>) : CodeScanningListAlertsForOrgResult

                data class NotFound(val value: BasicError) : CodeScanningListAlertsForOrgResult

                data class ServiceUnavailable(val value: CodeScanningListAlertsForOrgResponse) : CodeScanningListAlertsForOrgResult
            }

            suspend fun codeScanningListAlertsForOrg(
                org: String,
                direction: Direction = Direction.Desc,
                page: Long = 1L,
                perPage: Long = 30L,
                sort: Sort = Sort.Created,
                after: String? = null,
                assignees: String? = null,
                before: String? = null,
                severity: CodeScanningAlertSeverity? = null,
                state: CodeScanningAlertStateQuery? = null,
                toolGuid: CodeScanningAnalysisToolGuid? = null,
                toolName: CodeScanningAnalysisToolName? = null,
            ): CodeScanningListAlertsForOrgResult
        }
    }

    interface CodeSecurity {
        val configurations: Orgs.CodeSecurity.Configurations

        interface Configurations {
            val defaults: Orgs.CodeSecurity.Configurations.Defaults

            val detach: Orgs.CodeSecurity.Configurations.Detach

            val attach: Orgs.CodeSecurity.Configurations.Attach

            val repositories: Orgs.CodeSecurity.Configurations.Repositories

            @Serializable
            data class CodeSecurityCreateConfigurationBody(
                val name: String,
                val description: String,
                @SerialName("advanced_security") val advancedSecurity: AdvancedSecurity? = null,
                @SerialName("code_security") val codeSecurity: CodeSecurity? = null,
                @SerialName("dependency_graph") val dependencyGraph: DependencyGraph? = null,
                @SerialName("dependency_graph_autosubmit_action") val dependencyGraphAutosubmitAction: DependencyGraphAutosubmitAction? = null,
                @SerialName("dependency_graph_autosubmit_action_options") val dependencyGraphAutosubmitActionOptions: DependencyGraphAutosubmitActionOptions? = null,
                @SerialName("dependabot_alerts") val dependabotAlerts: DependabotAlerts? = null,
                @SerialName("dependabot_security_updates") val dependabotSecurityUpdates: DependabotSecurityUpdates? = null,
                @SerialName("dependabot_delegated_alert_dismissal") val dependabotDelegatedAlertDismissal: DependabotDelegatedAlertDismissal? = null,
                @SerialName("code_scanning_options") val codeScanningOptions: CodeScanningOptions? = null,
                @SerialName("code_scanning_default_setup") val codeScanningDefaultSetup: CodeScanningDefaultSetup? = null,
                @SerialName("code_scanning_default_setup_options") val codeScanningDefaultSetupOptions: CodeScanningDefaultSetupOptions? = null,
                @SerialName("code_scanning_delegated_alert_dismissal") val codeScanningDelegatedAlertDismissal: CodeScanningDelegatedAlertDismissal? = null,
                @SerialName("secret_protection") val secretProtection: SecretProtection? = null,
                @SerialName("secret_scanning") val secretScanning: SecretScanning? = null,
                @SerialName("secret_scanning_push_protection") val secretScanningPushProtection: SecretScanningPushProtection? = null,
                @SerialName("secret_scanning_delegated_bypass") val secretScanningDelegatedBypass: SecretScanningDelegatedBypass? = null,
                @SerialName("secret_scanning_delegated_bypass_options") val secretScanningDelegatedBypassOptions: SecretScanningDelegatedBypassOptions? = null,
                @SerialName("secret_scanning_validity_checks") val secretScanningValidityChecks: SecretScanningValidityChecks? = null,
                @SerialName("secret_scanning_non_provider_patterns") val secretScanningNonProviderPatterns: SecretScanningNonProviderPatterns? = null,
                @SerialName("secret_scanning_generic_secrets") val secretScanningGenericSecrets: SecretScanningGenericSecrets? = null,
                @SerialName("secret_scanning_delegated_alert_dismissal") val secretScanningDelegatedAlertDismissal: SecretScanningDelegatedAlertDismissal? = null,
                @SerialName("secret_scanning_extended_metadata") val secretScanningExtendedMetadata: SecretScanningExtendedMetadata? = null,
                @SerialName("private_vulnerability_reporting") val privateVulnerabilityReporting: PrivateVulnerabilityReporting? = null,
                val enforcement: Enforcement? = null,
            ) {
                @Serializable
                enum class AdvancedSecurity {
                    @SerialName("enabled")
                    Enabled,
                    @SerialName("disabled")
                    Disabled,
                    @SerialName("code_security")
                    CodeSecurity,
                    @SerialName("secret_protection")
                    SecretProtection;
                }

                @Serializable
                enum class CodeSecurity {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                enum class DependencyGraph {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                enum class DependencyGraphAutosubmitAction {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                @JvmInline
                value class DependencyGraphAutosubmitActionOptions(@SerialName("labeled_runners") val labeledRunners: Boolean? = null)

                @Serializable
                enum class DependabotAlerts {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                enum class DependabotSecurityUpdates {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                enum class DependabotDelegatedAlertDismissal {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                enum class CodeScanningDefaultSetup {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                enum class CodeScanningDelegatedAlertDismissal {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                enum class SecretProtection {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                enum class SecretScanning {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                enum class SecretScanningPushProtection {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                enum class SecretScanningDelegatedBypass {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                @JvmInline
                value class SecretScanningDelegatedBypassOptions(val reviewers: List<Reviewers>? = null) {
                    @Serializable
                    data class Reviewers(
                        @SerialName("reviewer_id") val reviewerId: Long,
                        @SerialName("reviewer_type") val reviewerType: ReviewerType,
                    ) {
                        @Serializable
                        enum class ReviewerType {
                            TEAM, ROLE;
                        }
                    }
                }

                @Serializable
                enum class SecretScanningValidityChecks {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                enum class SecretScanningNonProviderPatterns {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                enum class SecretScanningGenericSecrets {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                enum class SecretScanningDelegatedAlertDismissal {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                enum class SecretScanningExtendedMetadata {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                enum class PrivateVulnerabilityReporting {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                enum class Enforcement {
                    @SerialName("enforced") Enforced, @SerialName("unenforced") Unenforced;
                }
            }


            @Serializable
            data class CodeSecurityUpdateConfigurationBody(
                val name: String? = null,
                val description: String? = null,
                @SerialName("advanced_security") val advancedSecurity: AdvancedSecurity? = null,
                @SerialName("code_security") val codeSecurity: CodeSecurity? = null,
                @SerialName("dependency_graph") val dependencyGraph: DependencyGraph? = null,
                @SerialName("dependency_graph_autosubmit_action") val dependencyGraphAutosubmitAction: DependencyGraphAutosubmitAction? = null,
                @SerialName("dependency_graph_autosubmit_action_options") val dependencyGraphAutosubmitActionOptions: DependencyGraphAutosubmitActionOptions? = null,
                @SerialName("dependabot_alerts") val dependabotAlerts: DependabotAlerts? = null,
                @SerialName("dependabot_security_updates") val dependabotSecurityUpdates: DependabotSecurityUpdates? = null,
                @SerialName("dependabot_delegated_alert_dismissal") val dependabotDelegatedAlertDismissal: DependabotDelegatedAlertDismissal? = null,
                @SerialName("code_scanning_default_setup") val codeScanningDefaultSetup: CodeScanningDefaultSetup? = null,
                @SerialName("code_scanning_default_setup_options") val codeScanningDefaultSetupOptions: CodeScanningDefaultSetupOptions? = null,
                @SerialName("code_scanning_options") val codeScanningOptions: CodeScanningOptions? = null,
                @SerialName("code_scanning_delegated_alert_dismissal") val codeScanningDelegatedAlertDismissal: CodeScanningDelegatedAlertDismissal? = null,
                @SerialName("secret_protection") val secretProtection: SecretProtection? = null,
                @SerialName("secret_scanning") val secretScanning: SecretScanning? = null,
                @SerialName("secret_scanning_push_protection") val secretScanningPushProtection: SecretScanningPushProtection? = null,
                @SerialName("secret_scanning_delegated_bypass") val secretScanningDelegatedBypass: SecretScanningDelegatedBypass? = null,
                @SerialName("secret_scanning_delegated_bypass_options") val secretScanningDelegatedBypassOptions: SecretScanningDelegatedBypassOptions? = null,
                @SerialName("secret_scanning_validity_checks") val secretScanningValidityChecks: SecretScanningValidityChecks? = null,
                @SerialName("secret_scanning_non_provider_patterns") val secretScanningNonProviderPatterns: SecretScanningNonProviderPatterns? = null,
                @SerialName("secret_scanning_generic_secrets") val secretScanningGenericSecrets: SecretScanningGenericSecrets? = null,
                @SerialName("secret_scanning_delegated_alert_dismissal") val secretScanningDelegatedAlertDismissal: SecretScanningDelegatedAlertDismissal? = null,
                @SerialName("secret_scanning_extended_metadata") val secretScanningExtendedMetadata: SecretScanningExtendedMetadata? = null,
                @SerialName("private_vulnerability_reporting") val privateVulnerabilityReporting: PrivateVulnerabilityReporting? = null,
                val enforcement: Enforcement? = null,
            ) {
                @Serializable
                enum class AdvancedSecurity {
                    @SerialName("enabled")
                    Enabled,
                    @SerialName("disabled")
                    Disabled,
                    @SerialName("code_security")
                    CodeSecurity,
                    @SerialName("secret_protection")
                    SecretProtection;
                }

                @Serializable
                enum class CodeSecurity {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                enum class DependencyGraph {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                enum class DependencyGraphAutosubmitAction {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                @JvmInline
                value class DependencyGraphAutosubmitActionOptions(@SerialName("labeled_runners") val labeledRunners: Boolean? = null)

                @Serializable
                enum class DependabotAlerts {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                enum class DependabotSecurityUpdates {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                enum class DependabotDelegatedAlertDismissal {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                enum class CodeScanningDefaultSetup {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                enum class CodeScanningDelegatedAlertDismissal {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                enum class SecretProtection {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                enum class SecretScanning {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                enum class SecretScanningPushProtection {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                enum class SecretScanningDelegatedBypass {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                @JvmInline
                value class SecretScanningDelegatedBypassOptions(val reviewers: List<Reviewers>? = null) {
                    @Serializable
                    data class Reviewers(
                        @SerialName("reviewer_id") val reviewerId: Long,
                        @SerialName("reviewer_type") val reviewerType: ReviewerType,
                    ) {
                        @Serializable
                        enum class ReviewerType {
                            TEAM, ROLE;
                        }
                    }
                }

                @Serializable
                enum class SecretScanningValidityChecks {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                enum class SecretScanningNonProviderPatterns {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                enum class SecretScanningGenericSecrets {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                enum class SecretScanningDelegatedAlertDismissal {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                enum class SecretScanningExtendedMetadata {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                enum class PrivateVulnerabilityReporting {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                enum class Enforcement {
                    @SerialName("enforced") Enforced, @SerialName("unenforced") Unenforced;
                }
            }


            @Serializable
            enum class TargetType {
                @SerialName("global") Global, @SerialName("all") All;
            }

            sealed interface CodeSecurityGetConfigurationsForOrgResult {
                data class OK(val value: List<CodeSecurityConfiguration>) : CodeSecurityGetConfigurationsForOrgResult

                data class Forbidden(val value: BasicError) : CodeSecurityGetConfigurationsForOrgResult

                data class NotFound(val value: BasicError) : CodeSecurityGetConfigurationsForOrgResult
            }

            suspend fun codeSecurityGetConfigurationsForOrg(
                org: String,
                perPage: Long = 30L,
                targetType: TargetType = TargetType.All,
                after: String? = null,
                before: String? = null,
            ): CodeSecurityGetConfigurationsForOrgResult

            suspend fun codeSecurityCreateConfiguration(
                org: String,
                body: CodeSecurityCreateConfigurationBody,
            ): CodeSecurityConfiguration

            sealed interface CodeSecurityGetConfigurationResult {
                data class OK(val value: CodeSecurityConfiguration) : CodeSecurityGetConfigurationResult

                data object NotModified : CodeSecurityGetConfigurationResult

                data class Forbidden(val value: BasicError) : CodeSecurityGetConfigurationResult

                data class NotFound(val value: BasicError) : CodeSecurityGetConfigurationResult
            }

            suspend fun codeSecurityGetConfiguration(
                org: String,
                configurationId: Long,
            ): CodeSecurityGetConfigurationResult

            sealed interface CodeSecurityDeleteConfigurationResult {
                data object NoContent : CodeSecurityDeleteConfigurationResult

                data class BadRequest(val value: BasicError) : CodeSecurityDeleteConfigurationResult

                data class Forbidden(val value: BasicError) : CodeSecurityDeleteConfigurationResult

                data class NotFound(val value: BasicError) : CodeSecurityDeleteConfigurationResult

                data class Conflict(val value: BasicError) : CodeSecurityDeleteConfigurationResult
            }

            suspend fun codeSecurityDeleteConfiguration(
                org: String,
                configurationId: Long,
            ): CodeSecurityDeleteConfigurationResult

            sealed interface CodeSecurityUpdateConfigurationResult {
                data class OK(val value: CodeSecurityConfiguration) : CodeSecurityUpdateConfigurationResult

                data object NoContent : CodeSecurityUpdateConfigurationResult
            }

            suspend fun codeSecurityUpdateConfiguration(
                org: String,
                configurationId: Long,
                body: CodeSecurityUpdateConfigurationBody,
            ): CodeSecurityUpdateConfigurationResult

            interface Defaults {
                @Serializable
                @JvmInline
                value class CodeSecuritySetConfigurationAsDefaultBody(@SerialName("default_for_new_repos") val defaultForNewRepos: DefaultForNewRepos? = null) {
                    @Serializable
                    enum class DefaultForNewRepos {
                        @SerialName("all")
                        All,
                        @SerialName("none")
                        None,
                        @SerialName("private_and_internal")
                        PrivateAndInternal,
                        @SerialName("public")
                        Public;
                    }
                }


                @Serializable
                data class CodeSecuritySetConfigurationAsDefaultResponse(
                    @SerialName("default_for_new_repos") val defaultForNewRepos: DefaultForNewRepos? = null,
                    val configuration: CodeSecurityConfiguration? = null,
                ) {
                    @Serializable
                    enum class DefaultForNewRepos {
                        @SerialName("all")
                        All,
                        @SerialName("none")
                        None,
                        @SerialName("private_and_internal")
                        PrivateAndInternal,
                        @SerialName("public")
                        Public;
                    }
                }

                sealed interface CodeSecurityGetDefaultConfigurationsResult {
                    data class OK(val value: CodeSecurityDefaultConfigurations) : CodeSecurityGetDefaultConfigurationsResult

                    data object NotModified : CodeSecurityGetDefaultConfigurationsResult

                    data class Forbidden(val value: BasicError) : CodeSecurityGetDefaultConfigurationsResult

                    data class NotFound(val value: BasicError) : CodeSecurityGetDefaultConfigurationsResult
                }

                suspend fun codeSecurityGetDefaultConfigurations(
                    org: String,
                ): CodeSecurityGetDefaultConfigurationsResult

                sealed interface CodeSecuritySetConfigurationAsDefaultResult {
                    data class OK(val value: CodeSecuritySetConfigurationAsDefaultResponse) : CodeSecuritySetConfigurationAsDefaultResult

                    data class Forbidden(val value: BasicError) : CodeSecuritySetConfigurationAsDefaultResult

                    data class NotFound(val value: BasicError) : CodeSecuritySetConfigurationAsDefaultResult
                }

                suspend fun codeSecuritySetConfigurationAsDefault(
                    org: String,
                    configurationId: Long,
                    body: CodeSecuritySetConfigurationAsDefaultBody,
                ): CodeSecuritySetConfigurationAsDefaultResult
            }

            interface Detach {
                @Serializable
                @JvmInline
                value class CodeSecurityDetachConfigurationBody(@SerialName("selected_repository_ids") val selectedRepositoryIds: List<Long>? = null)

                sealed interface CodeSecurityDetachConfigurationResult {
                    data object NoContent : CodeSecurityDetachConfigurationResult

                    data class BadRequest(val value: BasicError) : CodeSecurityDetachConfigurationResult

                    data class Forbidden(val value: BasicError) : CodeSecurityDetachConfigurationResult

                    data class NotFound(val value: BasicError) : CodeSecurityDetachConfigurationResult

                    data class Conflict(val value: BasicError) : CodeSecurityDetachConfigurationResult
                }

                suspend fun codeSecurityDetachConfiguration(
                    org: String,
                    body: CodeSecurityDetachConfigurationBody,
                ): CodeSecurityDetachConfigurationResult
            }

            interface Attach {
                @Serializable
                data class CodeSecurityAttachConfigurationBody(
                    val scope: Scope,
                    @SerialName("selected_repository_ids") val selectedRepositoryIds: List<Long>? = null,
                ) {
                    @Serializable
                    enum class Scope {
                        @SerialName("all")
                        All,
                        @SerialName("all_without_configurations")
                        AllWithoutConfigurations,
                        @SerialName("public")
                        Public,
                        @SerialName("private_or_internal")
                        PrivateOrInternal,
                        @SerialName("selected")
                        Selected;
                    }
                }

                suspend fun codeSecurityAttachConfiguration(
                    org: String,
                    configurationId: Long,
                    body: CodeSecurityAttachConfigurationBody,
                ): JsonElement
            }

            interface Repositories {
                sealed interface CodeSecurityGetRepositoriesForConfigurationResult {
                    data class OK(val value: List<CodeSecurityConfigurationRepositories>) : CodeSecurityGetRepositoriesForConfigurationResult

                    data class Forbidden(val value: BasicError) : CodeSecurityGetRepositoriesForConfigurationResult

                    data class NotFound(val value: BasicError) : CodeSecurityGetRepositoriesForConfigurationResult
                }

                suspend fun codeSecurityGetRepositoriesForConfiguration(
                    org: String,
                    configurationId: Long,
                    perPage: Long = 30L,
                    status: String = "all",
                    after: String? = null,
                    before: String? = null,
                ): CodeSecurityGetRepositoriesForConfigurationResult
            }
        }
    }

    interface Codespaces {
        val access: Orgs.Codespaces.Access

        val secrets: Orgs.Codespaces.Secrets

        @Serializable
        data class CodespacesListInOrganizationResponse(
            @SerialName("total_count") val totalCount: Long,
            val codespaces: List<Codespace>,
        )

        sealed interface CodespacesListInOrganizationResult {
            data class OK(val value: CodespacesListInOrganizationResponse) : CodespacesListInOrganizationResult

            data object NotModified : CodespacesListInOrganizationResult

            data class Unauthorized(val value: BasicError) : CodespacesListInOrganizationResult

            data class Forbidden(val value: BasicError) : CodespacesListInOrganizationResult

            data class NotFound(val value: BasicError) : CodespacesListInOrganizationResult

            data class InternalServerError(val value: BasicError) : CodespacesListInOrganizationResult
        }

        suspend fun codespacesListInOrganization(
            org: String,
            page: Long = 1L,
            perPage: Long = 30L,
        ): CodespacesListInOrganizationResult

        interface Access {
            val selectedUsers: Orgs.Codespaces.Access.SelectedUsers

            @Serializable
            data class CodespacesSetCodespacesAccessBody(
                val visibility: Visibility,
                @SerialName("selected_usernames") val selectedUsernames: List<String>? = null,
            ) {
                @Serializable
                enum class Visibility {
                    @SerialName("disabled")
                    Disabled,
                    @SerialName("selected_members")
                    SelectedMembers,
                    @SerialName("all_members")
                    AllMembers,
                    @SerialName("all_members_and_outside_collaborators")
                    AllMembersAndOutsideCollaborators;
                }
            }

            sealed interface CodespacesSetCodespacesAccessResult {
                data object NoContent : CodespacesSetCodespacesAccessResult

                data object NotModified : CodespacesSetCodespacesAccessResult

                data object BadRequest : CodespacesSetCodespacesAccessResult

                data class NotFound(val value: BasicError) : CodespacesSetCodespacesAccessResult

                data class UnprocessableEntity(val value: ValidationError) : CodespacesSetCodespacesAccessResult

                data class InternalServerError(val value: BasicError) : CodespacesSetCodespacesAccessResult
            }

            @Deprecated("Deprecated by the API provider")
            suspend fun codespacesSetCodespacesAccess(
                org: String,
                body: CodespacesSetCodespacesAccessBody,
            ): CodespacesSetCodespacesAccessResult

            interface SelectedUsers {
                @Serializable
                @JvmInline
                value class CodespacesDeleteCodespacesAccessUsersBody(@SerialName("selected_usernames") val selectedUsernames: List<String>)


                @Serializable
                @JvmInline
                value class CodespacesSetCodespacesAccessUsersBody(@SerialName("selected_usernames") val selectedUsernames: List<String>)

                sealed interface CodespacesSetCodespacesAccessUsersResult {
                    data object NoContent : CodespacesSetCodespacesAccessUsersResult

                    data object NotModified : CodespacesSetCodespacesAccessUsersResult

                    data object BadRequest : CodespacesSetCodespacesAccessUsersResult

                    data class NotFound(val value: BasicError) : CodespacesSetCodespacesAccessUsersResult

                    data class UnprocessableEntity(val value: ValidationError) : CodespacesSetCodespacesAccessUsersResult

                    data class InternalServerError(val value: BasicError) : CodespacesSetCodespacesAccessUsersResult
                }

                @Deprecated("Deprecated by the API provider")
                suspend fun codespacesSetCodespacesAccessUsers(
                    org: String,
                    body: CodespacesSetCodespacesAccessUsersBody,
                ): CodespacesSetCodespacesAccessUsersResult

                sealed interface CodespacesDeleteCodespacesAccessUsersResult {
                    data object NoContent : CodespacesDeleteCodespacesAccessUsersResult

                    data object NotModified : CodespacesDeleteCodespacesAccessUsersResult

                    data object BadRequest : CodespacesDeleteCodespacesAccessUsersResult

                    data class NotFound(val value: BasicError) : CodespacesDeleteCodespacesAccessUsersResult

                    data class UnprocessableEntity(val value: ValidationError) : CodespacesDeleteCodespacesAccessUsersResult

                    data class InternalServerError(val value: BasicError) : CodespacesDeleteCodespacesAccessUsersResult
                }

                @Deprecated("Deprecated by the API provider")
                suspend fun codespacesDeleteCodespacesAccessUsers(
                    org: String,
                    body: CodespacesDeleteCodespacesAccessUsersBody,
                ): CodespacesDeleteCodespacesAccessUsersResult
            }
        }

        interface Secrets {
            val publicKey: Orgs.Codespaces.Secrets.PublicKey

            val repositories: Orgs.Codespaces.Secrets.Repositories

            @Serializable
            data class CodespacesCreateOrUpdateOrgSecretBody(
                @SerialName("encrypted_value") val encryptedValue: String? = null,
                @SerialName("key_id") val keyId: String? = null,
                val visibility: Visibility,
                @SerialName("selected_repository_ids") val selectedRepositoryIds: List<Long>? = null,
            ) {
                @Serializable
                enum class Visibility {
                    @SerialName("all") All, @SerialName("private") Private, @SerialName("selected") Selected;
                }
            }


            @Serializable
            data class CodespacesListOrgSecretsResponse(
                @SerialName("total_count") val totalCount: Long,
                val secrets: List<CodespacesOrgSecret>,
            )

            suspend fun codespacesListOrgSecrets(
                org: String,
                page: Long = 1L,
                perPage: Long = 30L,
            ): CodespacesListOrgSecretsResponse

            suspend fun codespacesGetOrgSecret(
                org: String,
                secretName: String,
            ): CodespacesOrgSecret

            sealed interface CodespacesCreateOrUpdateOrgSecretResult {
                data class Created(val value: EmptyObject) : CodespacesCreateOrUpdateOrgSecretResult

                data object NoContent : CodespacesCreateOrUpdateOrgSecretResult

                data class NotFound(val value: BasicError) : CodespacesCreateOrUpdateOrgSecretResult

                data class UnprocessableEntity(val value: ValidationError) : CodespacesCreateOrUpdateOrgSecretResult
            }

            suspend fun codespacesCreateOrUpdateOrgSecret(
                org: String,
                secretName: String,
                body: CodespacesCreateOrUpdateOrgSecretBody,
            ): CodespacesCreateOrUpdateOrgSecretResult

            sealed interface CodespacesDeleteOrgSecretResult {
                data object NoContent : CodespacesDeleteOrgSecretResult

                data class NotFound(val value: BasicError) : CodespacesDeleteOrgSecretResult
            }

            suspend fun codespacesDeleteOrgSecret(
                org: String,
                secretName: String,
            ): CodespacesDeleteOrgSecretResult

            interface PublicKey {
                suspend fun codespacesGetOrgPublicKey(
                    org: String,
                ): CodespacesPublicKey
            }

            interface Repositories {
                @Serializable
                data class CodespacesListSelectedReposForOrgSecretResponse(
                    @SerialName("total_count") val totalCount: Long,
                    val repositories: List<MinimalRepository>,
                )


                @Serializable
                @JvmInline
                value class CodespacesSetSelectedReposForOrgSecretBody(@SerialName("selected_repository_ids") val selectedRepositoryIds: List<Long>)

                sealed interface CodespacesListSelectedReposForOrgSecretResult {
                    data class OK(val value: CodespacesListSelectedReposForOrgSecretResponse) : CodespacesListSelectedReposForOrgSecretResult

                    data class NotFound(val value: BasicError) : CodespacesListSelectedReposForOrgSecretResult
                }

                suspend fun codespacesListSelectedReposForOrgSecret(
                    org: String,
                    secretName: String,
                    page: Long = 1L,
                    perPage: Long = 30L,
                ): CodespacesListSelectedReposForOrgSecretResult

                sealed interface CodespacesSetSelectedReposForOrgSecretResult {
                    data object NoContent : CodespacesSetSelectedReposForOrgSecretResult

                    data class NotFound(val value: BasicError) : CodespacesSetSelectedReposForOrgSecretResult

                    data object Conflict : CodespacesSetSelectedReposForOrgSecretResult
                }

                suspend fun codespacesSetSelectedReposForOrgSecret(
                    org: String,
                    secretName: String,
                    body: CodespacesSetSelectedReposForOrgSecretBody,
                ): CodespacesSetSelectedReposForOrgSecretResult

                sealed interface CodespacesAddSelectedRepoToOrgSecretResult {
                    data object NoContent : CodespacesAddSelectedRepoToOrgSecretResult

                    data class NotFound(val value: BasicError) : CodespacesAddSelectedRepoToOrgSecretResult

                    data object Conflict : CodespacesAddSelectedRepoToOrgSecretResult

                    data class UnprocessableEntity(val value: ValidationError) : CodespacesAddSelectedRepoToOrgSecretResult
                }

                suspend fun codespacesAddSelectedRepoToOrgSecret(
                    org: String,
                    secretName: String,
                    repositoryId: Long,
                ): CodespacesAddSelectedRepoToOrgSecretResult

                sealed interface CodespacesRemoveSelectedRepoFromOrgSecretResult {
                    data object NoContent : CodespacesRemoveSelectedRepoFromOrgSecretResult

                    data class NotFound(val value: BasicError) : CodespacesRemoveSelectedRepoFromOrgSecretResult

                    data object Conflict : CodespacesRemoveSelectedRepoFromOrgSecretResult

                    data class UnprocessableEntity(val value: ValidationError) : CodespacesRemoveSelectedRepoFromOrgSecretResult
                }

                suspend fun codespacesRemoveSelectedRepoFromOrgSecret(
                    org: String,
                    secretName: String,
                    repositoryId: Long,
                ): CodespacesRemoveSelectedRepoFromOrgSecretResult
            }
        }
    }

    interface Copilot {
        val billing: Orgs.Copilot.Billing

        val contentExclusion: Orgs.Copilot.ContentExclusion

        val metrics: Orgs.Copilot.Metrics

        interface Billing {
            val seats: Orgs.Copilot.Billing.Seats

            val selectedTeams: Orgs.Copilot.Billing.SelectedTeams

            val selectedUsers: Orgs.Copilot.Billing.SelectedUsers

            sealed interface CopilotGetCopilotOrganizationDetailsResult {
                data class OK(val value: CopilotOrganizationDetails) : CopilotGetCopilotOrganizationDetailsResult

                data class Unauthorized(val value: BasicError) : CopilotGetCopilotOrganizationDetailsResult

                data class Forbidden(val value: BasicError) : CopilotGetCopilotOrganizationDetailsResult

                data class NotFound(val value: BasicError) : CopilotGetCopilotOrganizationDetailsResult

                data object UnprocessableEntity : CopilotGetCopilotOrganizationDetailsResult

                data class InternalServerError(val value: BasicError) : CopilotGetCopilotOrganizationDetailsResult
            }

            suspend fun copilotGetCopilotOrganizationDetails(
                org: String,
            ): CopilotGetCopilotOrganizationDetailsResult

            interface Seats {
                @Serializable
                data class CopilotListCopilotSeatsResponse(
                    @SerialName("total_seats") val totalSeats: Long? = null,
                    val seats: List<CopilotSeatDetails>? = null,
                )

                sealed interface CopilotListCopilotSeatsResult {
                    data class OK(val value: CopilotListCopilotSeatsResponse) : CopilotListCopilotSeatsResult

                    data class Unauthorized(val value: BasicError) : CopilotListCopilotSeatsResult

                    data class Forbidden(val value: BasicError) : CopilotListCopilotSeatsResult

                    data class NotFound(val value: BasicError) : CopilotListCopilotSeatsResult

                    data class InternalServerError(val value: BasicError) : CopilotListCopilotSeatsResult
                }

                suspend fun copilotListCopilotSeats(
                    org: String,
                    page: Long = 1L,
                    perPage: Long = 50L,
                ): CopilotListCopilotSeatsResult
            }

            interface SelectedTeams {
                @Serializable
                @JvmInline
                value class CopilotAddCopilotSeatsForTeamsBody(@SerialName("selected_teams") val selectedTeams: List<String>)


                @Serializable
                @JvmInline
                value class CopilotAddCopilotSeatsForTeamsResponse(@SerialName("seats_created") val seatsCreated: Long)


                @Serializable
                @JvmInline
                value class CopilotCancelCopilotSeatAssignmentForTeamsBody(@SerialName("selected_teams") val selectedTeams: List<String>)


                @Serializable
                @JvmInline
                value class CopilotCancelCopilotSeatAssignmentForTeamsResponse(@SerialName("seats_cancelled") val seatsCancelled: Long)

                sealed interface CopilotAddCopilotSeatsForTeamsResult {
                    data class Created(val value: CopilotAddCopilotSeatsForTeamsResponse) : CopilotAddCopilotSeatsForTeamsResult

                    data class Unauthorized(val value: BasicError) : CopilotAddCopilotSeatsForTeamsResult

                    data class Forbidden(val value: BasicError) : CopilotAddCopilotSeatsForTeamsResult

                    data class NotFound(val value: BasicError) : CopilotAddCopilotSeatsForTeamsResult

                    data object UnprocessableEntity : CopilotAddCopilotSeatsForTeamsResult

                    data class InternalServerError(val value: BasicError) : CopilotAddCopilotSeatsForTeamsResult
                }

                suspend fun copilotAddCopilotSeatsForTeams(
                    org: String,
                    body: CopilotAddCopilotSeatsForTeamsBody,
                ): CopilotAddCopilotSeatsForTeamsResult

                sealed interface CopilotCancelCopilotSeatAssignmentForTeamsResult {
                    data class OK(val value: CopilotCancelCopilotSeatAssignmentForTeamsResponse) : CopilotCancelCopilotSeatAssignmentForTeamsResult

                    data class Unauthorized(val value: BasicError) : CopilotCancelCopilotSeatAssignmentForTeamsResult

                    data class Forbidden(val value: BasicError) : CopilotCancelCopilotSeatAssignmentForTeamsResult

                    data class NotFound(val value: BasicError) : CopilotCancelCopilotSeatAssignmentForTeamsResult

                    data object UnprocessableEntity : CopilotCancelCopilotSeatAssignmentForTeamsResult

                    data class InternalServerError(val value: BasicError) : CopilotCancelCopilotSeatAssignmentForTeamsResult
                }

                suspend fun copilotCancelCopilotSeatAssignmentForTeams(
                    org: String,
                    body: CopilotCancelCopilotSeatAssignmentForTeamsBody,
                ): CopilotCancelCopilotSeatAssignmentForTeamsResult
            }

            interface SelectedUsers {
                @Serializable
                @JvmInline
                value class CopilotAddCopilotSeatsForUsersBody(@SerialName("selected_usernames") val selectedUsernames: List<String>)


                @Serializable
                @JvmInline
                value class CopilotAddCopilotSeatsForUsersResponse(@SerialName("seats_created") val seatsCreated: Long)


                @Serializable
                @JvmInline
                value class CopilotCancelCopilotSeatAssignmentForUsersBody(@SerialName("selected_usernames") val selectedUsernames: List<String>)


                @Serializable
                @JvmInline
                value class CopilotCancelCopilotSeatAssignmentForUsersResponse(@SerialName("seats_cancelled") val seatsCancelled: Long)

                sealed interface CopilotAddCopilotSeatsForUsersResult {
                    data class Created(val value: CopilotAddCopilotSeatsForUsersResponse) : CopilotAddCopilotSeatsForUsersResult

                    data class Unauthorized(val value: BasicError) : CopilotAddCopilotSeatsForUsersResult

                    data class Forbidden(val value: BasicError) : CopilotAddCopilotSeatsForUsersResult

                    data class NotFound(val value: BasicError) : CopilotAddCopilotSeatsForUsersResult

                    data object UnprocessableEntity : CopilotAddCopilotSeatsForUsersResult

                    data class InternalServerError(val value: BasicError) : CopilotAddCopilotSeatsForUsersResult
                }

                suspend fun copilotAddCopilotSeatsForUsers(
                    org: String,
                    body: CopilotAddCopilotSeatsForUsersBody,
                ): CopilotAddCopilotSeatsForUsersResult

                sealed interface CopilotCancelCopilotSeatAssignmentForUsersResult {
                    data class OK(val value: CopilotCancelCopilotSeatAssignmentForUsersResponse) : CopilotCancelCopilotSeatAssignmentForUsersResult

                    data class Unauthorized(val value: BasicError) : CopilotCancelCopilotSeatAssignmentForUsersResult

                    data class Forbidden(val value: BasicError) : CopilotCancelCopilotSeatAssignmentForUsersResult

                    data class NotFound(val value: BasicError) : CopilotCancelCopilotSeatAssignmentForUsersResult

                    data object UnprocessableEntity : CopilotCancelCopilotSeatAssignmentForUsersResult

                    data class InternalServerError(val value: BasicError) : CopilotCancelCopilotSeatAssignmentForUsersResult
                }

                suspend fun copilotCancelCopilotSeatAssignmentForUsers(
                    org: String,
                    body: CopilotCancelCopilotSeatAssignmentForUsersBody,
                ): CopilotCancelCopilotSeatAssignmentForUsersResult
            }
        }

        interface ContentExclusion {
            @Serializable(with = CopilotSetCopilotContentExclusionForOrganizationBody.Serializer::class)
            sealed interface CopilotSetCopilotContentExclusionForOrganizationBody {
                @Serializable
                @JvmInline
                value class CaseString(val value: String) : CopilotSetCopilotContentExclusionForOrganizationBody

                @Serializable
                @JvmInline
                value class IfAnyMatch(val ifAnyMatch: List<String>) : CopilotSetCopilotContentExclusionForOrganizationBody

                @Serializable
                @JvmInline
                value class IfNoneMatch(val ifNoneMatch: List<String>) : CopilotSetCopilotContentExclusionForOrganizationBody

                object Serializer : KSerializer<CopilotSetCopilotContentExclusionForOrganizationBody> {
                    @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
                    override val descriptor: SerialDescriptor =
                        buildSerialDescriptor("io.github.nomisrev.api.Orgs.Copilot.ContentExclusion.CopilotSetCopilotContentExclusionForOrganizationBody", PolymorphicKind.SEALED) {
                            element("CaseString", String.serializer().descriptor)
                            element("IfAnyMatch", CopilotSetCopilotContentExclusionForOrganizationBody.IfAnyMatch.serializer().descriptor)
                            element("IfNoneMatch", CopilotSetCopilotContentExclusionForOrganizationBody.IfNoneMatch.serializer().descriptor)
                        }

                    override fun deserialize(decoder: Decoder): CopilotSetCopilotContentExclusionForOrganizationBody {
                        val value = decoder.decodeSerializableValue(JsonElement.serializer())
                        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                        return json.attemptDeserialize(
                            value,
                            IfAnyMatch::class to { decodeFromJsonElement(CopilotSetCopilotContentExclusionForOrganizationBody.IfAnyMatch.serializer(), it) },
                            IfNoneMatch::class to { decodeFromJsonElement(CopilotSetCopilotContentExclusionForOrganizationBody.IfNoneMatch.serializer(), it) },
                            CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                        )
                    }

                    override fun serialize(encoder: Encoder, value: CopilotSetCopilotContentExclusionForOrganizationBody) = when(value) {
                        is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                        is IfAnyMatch -> encoder.encodeSerializableValue(CopilotSetCopilotContentExclusionForOrganizationBody.IfAnyMatch.serializer(), value)
                        is IfNoneMatch -> encoder.encodeSerializableValue(CopilotSetCopilotContentExclusionForOrganizationBody.IfNoneMatch.serializer(), value)
                    }
                }
            }


            @Serializable
            @JvmInline
            value class CopilotSetCopilotContentExclusionForOrganizationResponse(val message: String? = null)

            sealed interface CopilotCopilotContentExclusionForOrganizationResult {
                data class OK(val value: CopilotOrganizationContentExclusionDetails) : CopilotCopilotContentExclusionForOrganizationResult

                data class Unauthorized(val value: BasicError) : CopilotCopilotContentExclusionForOrganizationResult

                data class Forbidden(val value: BasicError) : CopilotCopilotContentExclusionForOrganizationResult

                data class NotFound(val value: BasicError) : CopilotCopilotContentExclusionForOrganizationResult

                data class InternalServerError(val value: BasicError) : CopilotCopilotContentExclusionForOrganizationResult
            }

            suspend fun copilotCopilotContentExclusionForOrganization(
                org: String,
            ): CopilotCopilotContentExclusionForOrganizationResult

            sealed interface CopilotSetCopilotContentExclusionForOrganizationResult {
                data class OK(val value: CopilotSetCopilotContentExclusionForOrganizationResponse) : CopilotSetCopilotContentExclusionForOrganizationResult

                data class Unauthorized(val value: BasicError) : CopilotSetCopilotContentExclusionForOrganizationResult

                data class Forbidden(val value: BasicError) : CopilotSetCopilotContentExclusionForOrganizationResult

                data class NotFound(val value: BasicError) : CopilotSetCopilotContentExclusionForOrganizationResult

                data class PayloadTooLarge(val value: BasicError) : CopilotSetCopilotContentExclusionForOrganizationResult

                data class UnprocessableEntity(val value: ValidationErrorSimple) : CopilotSetCopilotContentExclusionForOrganizationResult

                data class InternalServerError(val value: BasicError) : CopilotSetCopilotContentExclusionForOrganizationResult
            }

            suspend fun copilotSetCopilotContentExclusionForOrganization(
                org: String,
                body: List<List<CopilotSetCopilotContentExclusionForOrganizationBody>> = null,
            ): CopilotSetCopilotContentExclusionForOrganizationResult
        }

        interface Metrics {
            sealed interface CopilotCopilotMetricsForOrganizationResult {
                data class OK(val value: List<CopilotUsageMetricsDay>) : CopilotCopilotMetricsForOrganizationResult

                data class Forbidden(val value: BasicError) : CopilotCopilotMetricsForOrganizationResult

                data class NotFound(val value: BasicError) : CopilotCopilotMetricsForOrganizationResult

                data class UnprocessableEntity(val value: BasicError) : CopilotCopilotMetricsForOrganizationResult

                data class InternalServerError(val value: BasicError) : CopilotCopilotMetricsForOrganizationResult
            }

            suspend fun copilotCopilotMetricsForOrganization(
                org: String,
                page: Long = 1L,
                perPage: Long = 100L,
                since: String? = null,
                until: String? = null,
            ): CopilotCopilotMetricsForOrganizationResult
        }
    }

    interface Dependabot {
        val alerts: Orgs.Dependabot.Alerts

        val secrets: Orgs.Dependabot.Secrets

        interface Alerts {
            @Serializable
            enum class Direction {
                @SerialName("asc") Asc, @SerialName("desc") Desc;
            }


            @Serializable(with = Has.Serializer::class)
            sealed interface Has {
                @Serializable
                @JvmInline
                value class CaseString(val value: String) : Has

                @Serializable
                @JvmInline
                value class PatchOrDeployments(val value: List<PatchOrDeployment>) : Has {
                    @Serializable
                    enum class PatchOrDeployment {
                        @SerialName("patch") Patch, @SerialName("deployment") Deployment;
                    }
                }

                object Serializer : KSerializer<Has> {
                    @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
                    override val descriptor: SerialDescriptor =
                        buildSerialDescriptor("io.github.nomisrev.api.Orgs.Dependabot.Alerts.Has", PolymorphicKind.SEALED) {
                            element("CaseString", String.serializer().descriptor)
                            element("PatchOrDeployments", ListSerializer(PatchOrDeployments.PatchOrDeployment.serializer()).descriptor)
                        }

                    override fun deserialize(decoder: Decoder): Has {
                        val value = decoder.decodeSerializableValue(JsonElement.serializer())
                        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                        return json.attemptDeserialize(
                            value,
                            PatchOrDeployments::class to { PatchOrDeployments(decodeFromJsonElement(ListSerializer(PatchOrDeployments.PatchOrDeployment.serializer()), it)) },
                            CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                        )
                    }

                    override fun serialize(encoder: Encoder, value: Has) = when(value) {
                        is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                        is PatchOrDeployments -> encoder.encodeSerializableValue(ListSerializer(PatchOrDeployments.PatchOrDeployment.serializer()), value.value)
                    }
                }
            }


            @Serializable
            enum class Scope {
                @SerialName("development") Development, @SerialName("runtime") Runtime;
            }


            @Serializable
            enum class Sort {
                @SerialName("created") Created, @SerialName("updated") Updated, @SerialName("epss_percentage") EpssPercentage;
            }

            sealed interface DependabotListAlertsForOrgResult {
                data class OK(val value: List<DependabotAlertWithRepositoryResponse>) : DependabotListAlertsForOrgResult

                data object NotModified : DependabotListAlertsForOrgResult

                data class BadRequest(val value: BasicError) : DependabotListAlertsForOrgResult

                data class Forbidden(val value: BasicError) : DependabotListAlertsForOrgResult

                data class NotFound(val value: BasicError) : DependabotListAlertsForOrgResult

                data class UnprocessableEntity(val value: ValidationErrorSimple) : DependabotListAlertsForOrgResult
            }

            suspend fun dependabotListAlertsForOrg(
                org: String,
                direction: Direction = Direction.Desc,
                perPage: Long = 30L,
                sort: Sort = Sort.Created,
                after: String? = null,
                artifactRegistry: String? = null,
                artifactRegistryUrl: String? = null,
                assignee: String? = null,
                before: String? = null,
                ecosystem: String? = null,
                epssPercentage: String? = null,
                has: Has? = null,
                `package`: String? = null,
                runtimeRisk: String? = null,
                scope: Scope? = null,
                severity: String? = null,
                state: String? = null,
            ): DependabotListAlertsForOrgResult
        }

        interface Secrets {
            val publicKey: Orgs.Dependabot.Secrets.PublicKey

            val repositories: Orgs.Dependabot.Secrets.Repositories

            @Serializable
            data class DependabotCreateOrUpdateOrgSecretBody(
                @SerialName("encrypted_value") val encryptedValue: String? = null,
                @SerialName("key_id") val keyId: String? = null,
                val visibility: Visibility,
                @SerialName("selected_repository_ids") val selectedRepositoryIds: List<SelectedRepositoryIds>? = null,
            ) {
                @Serializable
                enum class Visibility {
                    @SerialName("all") All, @SerialName("private") Private, @SerialName("selected") Selected;
                }

                @Serializable(with = SelectedRepositoryIds.Serializer::class)
                sealed interface SelectedRepositoryIds {
                    @Serializable
                    @JvmInline
                    value class CaseLong(val value: Long) : SelectedRepositoryIds

                    @Serializable
                    @JvmInline
                    value class CaseString(val value: String) : SelectedRepositoryIds

                    object Serializer : KSerializer<SelectedRepositoryIds> {
                        @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
                        override val descriptor: SerialDescriptor =
                            buildSerialDescriptor("io.github.nomisrev.api.Orgs.Dependabot.Secrets.DependabotCreateOrUpdateOrgSecretBody.SelectedRepositoryIds", PolymorphicKind.SEALED) {
                                element("CaseLong", Long.serializer().descriptor)
                                element("CaseString", String.serializer().descriptor)
                            }

                        override fun deserialize(decoder: Decoder): SelectedRepositoryIds {
                            val value = decoder.decodeSerializableValue(JsonElement.serializer())
                            val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                            return json.attemptDeserialize(
                                value,
                                CaseLong::class to { CaseLong(decodeFromJsonElement(Long.serializer(), it)) },
                                CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                            )
                        }

                        override fun serialize(encoder: Encoder, value: SelectedRepositoryIds) = when(value) {
                            is CaseLong -> encoder.encodeSerializableValue(Long.serializer(), value.value)
                            is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                        }
                    }
                }
            }


            @Serializable
            data class DependabotListOrgSecretsResponse(
                @SerialName("total_count") val totalCount: Long,
                val secrets: List<OrganizationDependabotSecret>,
            )

            suspend fun dependabotListOrgSecrets(
                org: String,
                page: Long = 1L,
                perPage: Long = 30L,
            ): DependabotListOrgSecretsResponse

            suspend fun dependabotGetOrgSecret(
                org: String,
                secretName: String,
            ): OrganizationDependabotSecret

            sealed interface DependabotCreateOrUpdateOrgSecretResult {
                data class Created(val value: EmptyObject) : DependabotCreateOrUpdateOrgSecretResult

                data object NoContent : DependabotCreateOrUpdateOrgSecretResult
            }

            suspend fun dependabotCreateOrUpdateOrgSecret(
                org: String,
                secretName: String,
                body: DependabotCreateOrUpdateOrgSecretBody,
            ): DependabotCreateOrUpdateOrgSecretResult

            suspend fun dependabotDeleteOrgSecret(
                org: String,
                secretName: String,
            ): Unit

            interface PublicKey {
                suspend fun dependabotGetOrgPublicKey(
                    org: String,
                ): DependabotPublicKey
            }

            interface Repositories {
                @Serializable
                data class DependabotListSelectedReposForOrgSecretResponse(
                    @SerialName("total_count") val totalCount: Long,
                    val repositories: List<MinimalRepository>,
                )


                @Serializable
                @JvmInline
                value class DependabotSetSelectedReposForOrgSecretBody(@SerialName("selected_repository_ids") val selectedRepositoryIds: List<Long>)

                suspend fun dependabotListSelectedReposForOrgSecret(
                    org: String,
                    secretName: String,
                    page: Long = 1L,
                    perPage: Long = 30L,
                ): DependabotListSelectedReposForOrgSecretResponse

                suspend fun dependabotSetSelectedReposForOrgSecret(
                    org: String,
                    secretName: String,
                    body: DependabotSetSelectedReposForOrgSecretBody,
                ): Unit

                sealed interface DependabotAddSelectedRepoToOrgSecretResult {
                    data object NoContent : DependabotAddSelectedRepoToOrgSecretResult

                    data object Conflict : DependabotAddSelectedRepoToOrgSecretResult
                }

                suspend fun dependabotAddSelectedRepoToOrgSecret(
                    org: String,
                    secretName: String,
                    repositoryId: Long,
                ): DependabotAddSelectedRepoToOrgSecretResult

                sealed interface DependabotRemoveSelectedRepoFromOrgSecretResult {
                    data object NoContent : DependabotRemoveSelectedRepoFromOrgSecretResult

                    data object Conflict : DependabotRemoveSelectedRepoFromOrgSecretResult
                }

                suspend fun dependabotRemoveSelectedRepoFromOrgSecret(
                    org: String,
                    secretName: String,
                    repositoryId: Long,
                ): DependabotRemoveSelectedRepoFromOrgSecretResult
            }
        }
    }

    interface Docker {
        val conflicts: Orgs.Docker.Conflicts

        interface Conflicts {
            sealed interface PackagesListDockerMigrationConflictingPackagesForOrganizationResult {
                data class OK(val value: List<Package>) : PackagesListDockerMigrationConflictingPackagesForOrganizationResult

                data class Unauthorized(val value: BasicError) : PackagesListDockerMigrationConflictingPackagesForOrganizationResult

                data class Forbidden(val value: BasicError) : PackagesListDockerMigrationConflictingPackagesForOrganizationResult
            }

            suspend fun packagesListDockerMigrationConflictingPackagesForOrganization(
                org: String,
            ): PackagesListDockerMigrationConflictingPackagesForOrganizationResult
        }
    }

    interface Events {
        suspend fun activityListPublicOrgEvents(
            org: String,
            page: Long = 1L,
            perPage: Long = 30L,
        ): List<Event>
    }

    interface FailedInvitations {
        sealed interface OrgsListFailedInvitationsResult {
            data class OK(val value: List<OrganizationInvitation>) : OrgsListFailedInvitationsResult

            data class NotFound(val value: BasicError) : OrgsListFailedInvitationsResult
        }

        suspend fun orgsListFailedInvitations(
            org: String,
            page: Long = 1L,
            perPage: Long = 30L,
        ): OrgsListFailedInvitationsResult
    }

    interface Hooks {
        val config: Orgs.Hooks.Config

        val deliveries: Orgs.Hooks.Deliveries

        val pings: Orgs.Hooks.Pings

        @Serializable
        data class OrgsCreateWebhookBody(
            val name: String,
            val config: Config,
            val events: List<String>? = null,
            val active: Boolean? = null,
        ) {
            @Serializable
            data class Config(
                val url: WebhookConfigUrl,
                @SerialName("content_type") val contentType: WebhookConfigContentType? = null,
                val secret: WebhookConfigSecret? = null,
                @SerialName("insecure_ssl") val insecureSsl: WebhookConfigInsecureSsl? = null,
                val username: String? = null,
                val password: String? = null,
            )
        }


        @Serializable
        data class OrgsUpdateWebhookBody(
            val config: Config? = null,
            val events: List<String>? = null,
            val active: Boolean? = null,
            val name: String? = null,
        ) {
            @Serializable
            data class Config(
                val url: WebhookConfigUrl,
                @SerialName("content_type") val contentType: WebhookConfigContentType? = null,
                val secret: WebhookConfigSecret? = null,
                @SerialName("insecure_ssl") val insecureSsl: WebhookConfigInsecureSsl? = null,
            )
        }

        sealed interface OrgsListWebhooksResult {
            data class OK(val value: List<OrgHook>) : OrgsListWebhooksResult

            data class NotFound(val value: BasicError) : OrgsListWebhooksResult
        }

        suspend fun orgsListWebhooks(
            org: String,
            page: Long = 1L,
            perPage: Long = 30L,
        ): OrgsListWebhooksResult

        sealed interface OrgsCreateWebhookResult {
            data class Created(val value: OrgHook) : OrgsCreateWebhookResult

            data class NotFound(val value: BasicError) : OrgsCreateWebhookResult

            data class UnprocessableEntity(val value: ValidationError) : OrgsCreateWebhookResult
        }

        suspend fun orgsCreateWebhook(
            org: String,
            body: OrgsCreateWebhookBody,
        ): OrgsCreateWebhookResult

        sealed interface OrgsGetWebhookResult {
            data class OK(val value: OrgHook) : OrgsGetWebhookResult

            data class NotFound(val value: BasicError) : OrgsGetWebhookResult
        }

        suspend fun orgsGetWebhook(
            org: String,
            hookId: Long,
        ): OrgsGetWebhookResult

        sealed interface OrgsDeleteWebhookResult {
            data object NoContent : OrgsDeleteWebhookResult

            data class NotFound(val value: BasicError) : OrgsDeleteWebhookResult
        }

        suspend fun orgsDeleteWebhook(
            org: String,
            hookId: Long,
        ): OrgsDeleteWebhookResult

        sealed interface OrgsUpdateWebhookResult {
            data class OK(val value: OrgHook) : OrgsUpdateWebhookResult

            data class NotFound(val value: BasicError) : OrgsUpdateWebhookResult

            data class UnprocessableEntity(val value: ValidationError) : OrgsUpdateWebhookResult
        }

        suspend fun orgsUpdateWebhook(
            org: String,
            hookId: Long,
            body: OrgsUpdateWebhookBody? = null,
        ): OrgsUpdateWebhookResult

        interface Config {
            @Serializable
            data class OrgsUpdateWebhookConfigForOrgBody(
                val url: WebhookConfigUrl? = null,
                @SerialName("content_type") val contentType: WebhookConfigContentType? = null,
                val secret: WebhookConfigSecret? = null,
                @SerialName("insecure_ssl") val insecureSsl: WebhookConfigInsecureSsl? = null,
            )

            suspend fun orgsGetWebhookConfigForOrg(
                org: String,
                hookId: Long,
            ): WebhookConfig

            suspend fun orgsUpdateWebhookConfigForOrg(
                org: String,
                hookId: Long,
                body: OrgsUpdateWebhookConfigForOrgBody? = null,
            ): WebhookConfig
        }

        interface Deliveries {
            val attempts: Orgs.Hooks.Deliveries.Attempts

            sealed interface OrgsListWebhookDeliveriesResult {
                data class OK(val value: List<HookDeliveryItem>) : OrgsListWebhookDeliveriesResult

                data class BadRequest(val value: BasicError) : OrgsListWebhookDeliveriesResult

                data class UnprocessableEntity(val value: ValidationError) : OrgsListWebhookDeliveriesResult
            }

            suspend fun orgsListWebhookDeliveries(
                org: String,
                hookId: Long,
                perPage: Long = 30L,
                cursor: String? = null,
            ): OrgsListWebhookDeliveriesResult

            sealed interface OrgsGetWebhookDeliveryResult {
                data class OK(val value: HookDelivery) : OrgsGetWebhookDeliveryResult

                data class BadRequest(val value: BasicError) : OrgsGetWebhookDeliveryResult

                data class UnprocessableEntity(val value: ValidationError) : OrgsGetWebhookDeliveryResult
            }

            suspend fun orgsGetWebhookDelivery(
                org: String,
                hookId: Long,
                deliveryId: Long,
            ): OrgsGetWebhookDeliveryResult

            interface Attempts {
                sealed interface OrgsRedeliverWebhookDeliveryResult {
                    data class Accepted(val value: JsonElement) : OrgsRedeliverWebhookDeliveryResult

                    data class BadRequest(val value: BasicError) : OrgsRedeliverWebhookDeliveryResult

                    data class UnprocessableEntity(val value: ValidationError) : OrgsRedeliverWebhookDeliveryResult
                }

                suspend fun orgsRedeliverWebhookDelivery(
                    org: String,
                    hookId: Long,
                    deliveryId: Long,
                ): OrgsRedeliverWebhookDeliveryResult
            }
        }

        interface Pings {
            sealed interface OrgsPingWebhookResult {
                data object NoContent : OrgsPingWebhookResult

                data class NotFound(val value: BasicError) : OrgsPingWebhookResult
            }

            suspend fun orgsPingWebhook(
                org: String,
                hookId: Long,
            ): OrgsPingWebhookResult
        }
    }

    interface Insights {
        val api: Orgs.Insights.Api

        interface Api {
            val routeStats: Orgs.Insights.Api.RouteStats

            val subjectStats: Orgs.Insights.Api.SubjectStats

            val summaryStats: Orgs.Insights.Api.SummaryStats

            val timeStats: Orgs.Insights.Api.TimeStats

            val userStats: Orgs.Insights.Api.UserStats

            interface RouteStats {
                @Serializable
                enum class ActorType {
                    @SerialName("installation")
                    Installation,
                    @SerialName("classic_pat")
                    ClassicPat,
                    @SerialName("fine_grained_pat")
                    FineGrainedPat,
                    @SerialName("oauth_app")
                    OauthApp,
                    @SerialName("github_app_user_to_server")
                    GithubAppUserToServer;
                }


                @Serializable
                enum class Direction {
                    @SerialName("asc") Asc, @SerialName("desc") Desc;
                }


                @Serializable
                enum class Sort {
                    @SerialName("last_rate_limited_timestamp")
                    LastRateLimitedTimestamp,
                    @SerialName("last_request_timestamp")
                    LastRequestTimestamp,
                    @SerialName("rate_limited_request_count")
                    RateLimitedRequestCount,
                    @SerialName("http_method")
                    HttpMethod,
                    @SerialName("api_route")
                    ApiRoute,
                    @SerialName("total_request_count")
                    TotalRequestCount;
                }

                suspend fun apiInsightsGetRouteStatsByActor(
                    org: String,
                    actorType: ActorType,
                    actorId: Long,
                    direction: Direction = Direction.Desc,
                    minTimestamp: String,
                    page: Long = 1L,
                    perPage: Long = 30L,
                    apiRouteSubstring: String? = null,
                    maxTimestamp: String? = null,
                    sort: List<Sort>? = null,
                ): ApiInsightsRouteStats
            }

            interface SubjectStats {
                @Serializable
                enum class Direction {
                    @SerialName("asc") Asc, @SerialName("desc") Desc;
                }


                @Serializable
                enum class Sort {
                    @SerialName("last_rate_limited_timestamp")
                    LastRateLimitedTimestamp,
                    @SerialName("last_request_timestamp")
                    LastRequestTimestamp,
                    @SerialName("rate_limited_request_count")
                    RateLimitedRequestCount,
                    @SerialName("subject_name")
                    SubjectName,
                    @SerialName("total_request_count")
                    TotalRequestCount;
                }

                suspend fun apiInsightsGetSubjectStats(
                    org: String,
                    direction: Direction = Direction.Desc,
                    minTimestamp: String,
                    page: Long = 1L,
                    perPage: Long = 30L,
                    maxTimestamp: String? = null,
                    sort: List<Sort>? = null,
                    subjectNameSubstring: String? = null,
                ): ApiInsightsSubjectStats
            }

            interface SummaryStats {
                val users: Orgs.Insights.Api.SummaryStats.Users

                @Serializable
                enum class ActorType {
                    @SerialName("installation")
                    Installation,
                    @SerialName("classic_pat")
                    ClassicPat,
                    @SerialName("fine_grained_pat")
                    FineGrainedPat,
                    @SerialName("oauth_app")
                    OauthApp,
                    @SerialName("github_app_user_to_server")
                    GithubAppUserToServer;
                }

                suspend fun apiInsightsGetSummaryStats(
                    org: String,
                    minTimestamp: String,
                    maxTimestamp: String? = null,
                ): ApiInsightsSummaryStats

                suspend fun apiInsightsGetSummaryStatsByActor(
                    org: String,
                    actorType: ActorType,
                    actorId: Long,
                    minTimestamp: String,
                    maxTimestamp: String? = null,
                ): ApiInsightsSummaryStats

                interface Users {
                    suspend fun apiInsightsGetSummaryStatsByUser(
                        org: String,
                        userId: String,
                        minTimestamp: String,
                        maxTimestamp: String? = null,
                    ): ApiInsightsSummaryStats
                }
            }

            interface TimeStats {
                val users: Orgs.Insights.Api.TimeStats.Users

                @Serializable
                enum class ActorType {
                    @SerialName("installation")
                    Installation,
                    @SerialName("classic_pat")
                    ClassicPat,
                    @SerialName("fine_grained_pat")
                    FineGrainedPat,
                    @SerialName("oauth_app")
                    OauthApp,
                    @SerialName("github_app_user_to_server")
                    GithubAppUserToServer;
                }

                suspend fun apiInsightsGetTimeStats(
                    org: String,
                    minTimestamp: String,
                    timestampIncrement: String,
                    maxTimestamp: String? = null,
                ): ApiInsightsTimeStats

                suspend fun apiInsightsGetTimeStatsByActor(
                    org: String,
                    actorType: ActorType,
                    actorId: Long,
                    minTimestamp: String,
                    timestampIncrement: String,
                    maxTimestamp: String? = null,
                ): ApiInsightsTimeStats

                interface Users {
                    suspend fun apiInsightsGetTimeStatsByUser(
                        org: String,
                        userId: String,
                        minTimestamp: String,
                        timestampIncrement: String,
                        maxTimestamp: String? = null,
                    ): ApiInsightsTimeStats
                }
            }

            interface UserStats {
                @Serializable
                enum class Direction {
                    @SerialName("asc") Asc, @SerialName("desc") Desc;
                }


                @Serializable
                enum class Sort {
                    @SerialName("last_rate_limited_timestamp")
                    LastRateLimitedTimestamp,
                    @SerialName("last_request_timestamp")
                    LastRequestTimestamp,
                    @SerialName("rate_limited_request_count")
                    RateLimitedRequestCount,
                    @SerialName("subject_name")
                    SubjectName,
                    @SerialName("total_request_count")
                    TotalRequestCount;
                }

                suspend fun apiInsightsGetUserStats(
                    org: String,
                    userId: String,
                    direction: Direction = Direction.Desc,
                    minTimestamp: String,
                    page: Long = 1L,
                    perPage: Long = 30L,
                    actorNameSubstring: String? = null,
                    maxTimestamp: String? = null,
                    sort: List<Sort>? = null,
                ): ApiInsightsUserStats
            }
        }
    }

    interface Installation {
        suspend fun appsGetOrgInstallation(
            org: String,
        ): Installation
    }

    interface Installations {
        @Serializable
        data class OrgsListAppInstallationsResponse(
            @SerialName("total_count") val totalCount: Long,
            val installations: List<Installation>,
        )

        suspend fun orgsListAppInstallations(
            org: String,
            page: Long = 1L,
            perPage: Long = 30L,
        ): OrgsListAppInstallationsResponse
    }

    interface InteractionLimits {
        @Serializable(with = InteractionsGetRestrictionsForOrgResponse.Serializer::class)
        sealed interface InteractionsGetRestrictionsForOrgResponse {
            @Serializable
            @JvmInline
            value class CaseInteractionLimitResponse(val value: InteractionLimitResponse) : InteractionsGetRestrictionsForOrgResponse

            @Serializable
            data object Empty : InteractionsGetRestrictionsForOrgResponse

            object Serializer : KSerializer<InteractionsGetRestrictionsForOrgResponse> {
                @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
                override val descriptor: SerialDescriptor =
                    buildSerialDescriptor("io.github.nomisrev.api.Orgs.InteractionLimits.InteractionsGetRestrictionsForOrgResponse", PolymorphicKind.SEALED) {
                        element("CaseInteractionLimitResponse", InteractionLimitResponse.serializer().descriptor)
                        element("Empty", Unit.serializer().descriptor)
                    }

                override fun deserialize(decoder: Decoder): InteractionsGetRestrictionsForOrgResponse {
                    val value = decoder.decodeSerializableValue(JsonElement.serializer())
                    val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                    return json.attemptDeserialize(
                        value,
                        CaseInteractionLimitResponse::class to { CaseInteractionLimitResponse(decodeFromJsonElement(InteractionLimitResponse.serializer(), it)) },
                        Empty::class to { decodeFromJsonElement(Empty.serializer(), it) },
                    )
                }

                override fun serialize(encoder: Encoder, value: InteractionsGetRestrictionsForOrgResponse) = when(value) {
                    is CaseInteractionLimitResponse -> encoder.encodeSerializableValue(InteractionLimitResponse.serializer(), value.value)
                    is Empty -> encoder.encodeSerializableValue(Empty.serializer(), value)
                }
            }
        }

        suspend fun interactionsGetRestrictionsForOrg(
            org: String,
        ): InteractionsGetRestrictionsForOrgResponse

        sealed interface InteractionsSetRestrictionsForOrgResult {
            data class OK(val value: InteractionLimitResponse) : InteractionsSetRestrictionsForOrgResult

            data class UnprocessableEntity(val value: ValidationError) : InteractionsSetRestrictionsForOrgResult
        }

        suspend fun interactionsSetRestrictionsForOrg(
            org: String,
            body: InteractionLimit,
        ): InteractionsSetRestrictionsForOrgResult

        suspend fun interactionsRemoveRestrictionsForOrg(
            org: String,
        ): Unit
    }

    interface Invitations {
        val teams: Orgs.Invitations.TeamsApi

        @Serializable
        enum class InvitationSource {
            @SerialName("all") All, @SerialName("member") Member, @SerialName("scim") Scim;
        }


        @Serializable
        data class OrgsCreateInvitationBody(
            @SerialName("invitee_id") val inviteeId: Long? = null,
            val email: String? = null,
            val role: Role? = null,
            @SerialName("team_ids") val teamIds: List<Long>? = null,
        ) {
            @Serializable
            enum class Role {
                @SerialName("admin")
                Admin,
                @SerialName("direct_member")
                DirectMember,
                @SerialName("billing_manager")
                BillingManager,
                @SerialName("reinstate")
                Reinstate;
            }
        }


        @Serializable
        enum class Role {
            @SerialName("all")
            All,
            @SerialName("admin")
            Admin,
            @SerialName("direct_member")
            DirectMember,
            @SerialName("billing_manager")
            BillingManager,
            @SerialName("hiring_manager")
            HiringManager;
        }

        sealed interface OrgsListPendingInvitationsResult {
            data class OK(val value: List<OrganizationInvitation>) : OrgsListPendingInvitationsResult

            data class NotFound(val value: BasicError) : OrgsListPendingInvitationsResult
        }

        suspend fun orgsListPendingInvitations(
            org: String,
            invitationSource: InvitationSource = InvitationSource.All,
            page: Long = 1L,
            perPage: Long = 30L,
            role: Role = Role.All,
        ): OrgsListPendingInvitationsResult

        sealed interface OrgsCreateInvitationResult {
            data class Created(val value: OrganizationInvitation) : OrgsCreateInvitationResult

            data class NotFound(val value: BasicError) : OrgsCreateInvitationResult

            data class UnprocessableEntity(val value: ValidationError) : OrgsCreateInvitationResult
        }

        suspend fun orgsCreateInvitation(
            org: String,
            body: OrgsCreateInvitationBody? = null,
        ): OrgsCreateInvitationResult

        sealed interface OrgsCancelInvitationResult {
            data object NoContent : OrgsCancelInvitationResult

            data class NotFound(val value: BasicError) : OrgsCancelInvitationResult

            data class UnprocessableEntity(val value: ValidationError) : OrgsCancelInvitationResult
        }

        suspend fun orgsCancelInvitation(
            org: String,
            invitationId: Long,
        ): OrgsCancelInvitationResult

        interface TeamsApi {
            sealed interface OrgsListInvitationTeamsResult {
                data class OK(val value: List<Team>) : OrgsListInvitationTeamsResult

                data class NotFound(val value: BasicError) : OrgsListInvitationTeamsResult
            }

            suspend fun orgsListInvitationTeams(
                org: String,
                invitationId: Long,
                page: Long = 1L,
                perPage: Long = 30L,
            ): OrgsListInvitationTeamsResult
        }
    }

    interface IssueFields {
        sealed interface OrgsListIssueFieldsResult {
            data class OK(val value: List<IssueField>) : OrgsListIssueFieldsResult

            data class NotFound(val value: BasicError) : OrgsListIssueFieldsResult
        }

        suspend fun orgsListIssueFields(
            org: String,
        ): OrgsListIssueFieldsResult

        sealed interface OrgsCreateIssueFieldResult {
            data class OK(val value: IssueField) : OrgsCreateIssueFieldResult

            data class NotFound(val value: BasicError) : OrgsCreateIssueFieldResult

            data class UnprocessableEntity(val value: ValidationErrorSimple) : OrgsCreateIssueFieldResult
        }

        suspend fun orgsCreateIssueField(
            org: String,
            body: OrganizationCreateIssueField,
        ): OrgsCreateIssueFieldResult

        sealed interface OrgsDeleteIssueFieldResult {
            data object NoContent : OrgsDeleteIssueFieldResult

            data class NotFound(val value: BasicError) : OrgsDeleteIssueFieldResult

            data class UnprocessableEntity(val value: ValidationErrorSimple) : OrgsDeleteIssueFieldResult
        }

        suspend fun orgsDeleteIssueField(
            org: String,
            issueFieldId: Long,
        ): OrgsDeleteIssueFieldResult

        sealed interface OrgsUpdateIssueFieldResult {
            data class OK(val value: IssueField) : OrgsUpdateIssueFieldResult

            data class NotFound(val value: BasicError) : OrgsUpdateIssueFieldResult

            data class UnprocessableEntity(val value: ValidationErrorSimple) : OrgsUpdateIssueFieldResult
        }

        suspend fun orgsUpdateIssueField(
            org: String,
            issueFieldId: Long,
            body: OrganizationUpdateIssueField,
        ): OrgsUpdateIssueFieldResult
    }

    interface IssueTypes {
        sealed interface OrgsListIssueTypesResult {
            data class OK(val value: List<IssueType>) : OrgsListIssueTypesResult

            data class NotFound(val value: BasicError) : OrgsListIssueTypesResult
        }

        suspend fun orgsListIssueTypes(
            org: String,
        ): OrgsListIssueTypesResult

        sealed interface OrgsCreateIssueTypeResult {
            data class OK(val value: IssueType) : OrgsCreateIssueTypeResult

            data class NotFound(val value: BasicError) : OrgsCreateIssueTypeResult

            data class UnprocessableEntity(val value: ValidationErrorSimple) : OrgsCreateIssueTypeResult
        }

        suspend fun orgsCreateIssueType(
            org: String,
            body: OrganizationCreateIssueType,
        ): OrgsCreateIssueTypeResult

        sealed interface OrgsUpdateIssueTypeResult {
            data class OK(val value: IssueType) : OrgsUpdateIssueTypeResult

            data class NotFound(val value: BasicError) : OrgsUpdateIssueTypeResult

            data class UnprocessableEntity(val value: ValidationErrorSimple) : OrgsUpdateIssueTypeResult
        }

        suspend fun orgsUpdateIssueType(
            org: String,
            issueTypeId: Long,
            body: OrganizationUpdateIssueType,
        ): OrgsUpdateIssueTypeResult

        sealed interface OrgsDeleteIssueTypeResult {
            data object NoContent : OrgsDeleteIssueTypeResult

            data class NotFound(val value: BasicError) : OrgsDeleteIssueTypeResult

            data class UnprocessableEntity(val value: ValidationErrorSimple) : OrgsDeleteIssueTypeResult
        }

        suspend fun orgsDeleteIssueType(
            org: String,
            issueTypeId: Long,
        ): OrgsDeleteIssueTypeResult
    }

    interface Issues {
        @Serializable
        enum class Direction {
            @SerialName("asc") Asc, @SerialName("desc") Desc;
        }


        @Serializable
        enum class Filter {
            @SerialName("assigned")
            Assigned,
            @SerialName("created")
            Created,
            @SerialName("mentioned")
            Mentioned,
            @SerialName("subscribed")
            Subscribed,
            @SerialName("repos")
            Repos,
            @SerialName("all")
            All;
        }


        @Serializable
        enum class Sort {
            @SerialName("created") Created, @SerialName("updated") Updated, @SerialName("comments") Comments;
        }


        @Serializable
        enum class State {
            @SerialName("open") Open, @SerialName("closed") Closed, @SerialName("all") All;
        }

        sealed interface IssuesListForOrgResult {
            data class OK(val value: List<Issue>) : IssuesListForOrgResult

            data class NotFound(val value: BasicError) : IssuesListForOrgResult
        }

        suspend fun issuesListForOrg(
            org: String,
            direction: Direction = Direction.Desc,
            filter: Filter = Filter.Assigned,
            page: Long = 1L,
            perPage: Long = 30L,
            sort: Sort = Sort.Created,
            state: State = State.Open,
            labels: String? = null,
            since: LocalDateTime? = null,
            type: String? = null,
        ): IssuesListForOrgResult
    }

    interface Members {
        val codespaces: Orgs.Members.CodespacesApi

        val copilot: Orgs.Members.CopilotApi

        @Serializable
        enum class Filter {
            @JsName("_2faDisabled")@SerialName("2fa_disabled")
            `2faDisabled`,
            @JsName("_2faInsecure")@SerialName("2fa_insecure")
            `2faInsecure`,
            @SerialName("all")
            All;
        }


        @Serializable
        enum class Role {
            @SerialName("all") All, @SerialName("admin") Admin, @SerialName("member") Member;
        }

        sealed interface OrgsListMembersResult {
            data class OK(val value: List<SimpleUser>) : OrgsListMembersResult

            data class UnprocessableEntity(val value: ValidationError) : OrgsListMembersResult
        }

        suspend fun orgsListMembers(
            org: String,
            filter: Filter = Filter.All,
            page: Long = 1L,
            perPage: Long = 30L,
            role: Role = Role.All,
        ): OrgsListMembersResult

        sealed interface OrgsCheckMembershipForUserResult {
            data object NoContent : OrgsCheckMembershipForUserResult

            data object Found : OrgsCheckMembershipForUserResult

            data object NotFound : OrgsCheckMembershipForUserResult
        }

        suspend fun orgsCheckMembershipForUser(
            org: String,
            username: String,
        ): OrgsCheckMembershipForUserResult

        sealed interface OrgsRemoveMemberResult {
            data object NoContent : OrgsRemoveMemberResult

            data class Forbidden(val value: BasicError) : OrgsRemoveMemberResult
        }

        suspend fun orgsRemoveMember(
            org: String,
            username: String,
        ): OrgsRemoveMemberResult

        interface CodespacesApi {
            val stop: Orgs.Members.CodespacesApi.Stop

            @Serializable
            data class CodespacesGetCodespacesForUserInOrgResponse(
                @SerialName("total_count") val totalCount: Long,
                val codespaces: List<Codespace>,
            )

            sealed interface CodespacesGetCodespacesForUserInOrgResult {
                data class OK(val value: CodespacesGetCodespacesForUserInOrgResponse) : CodespacesGetCodespacesForUserInOrgResult

                data object NotModified : CodespacesGetCodespacesForUserInOrgResult

                data class Unauthorized(val value: BasicError) : CodespacesGetCodespacesForUserInOrgResult

                data class Forbidden(val value: BasicError) : CodespacesGetCodespacesForUserInOrgResult

                data class NotFound(val value: BasicError) : CodespacesGetCodespacesForUserInOrgResult

                data class InternalServerError(val value: BasicError) : CodespacesGetCodespacesForUserInOrgResult
            }

            suspend fun codespacesGetCodespacesForUserInOrg(
                org: String,
                username: String,
                page: Long = 1L,
                perPage: Long = 30L,
            ): CodespacesGetCodespacesForUserInOrgResult

            sealed interface CodespacesDeleteFromOrganizationResult {
                data class Accepted(val value: JsonElement) : CodespacesDeleteFromOrganizationResult

                data object NotModified : CodespacesDeleteFromOrganizationResult

                data class Unauthorized(val value: BasicError) : CodespacesDeleteFromOrganizationResult

                data class Forbidden(val value: BasicError) : CodespacesDeleteFromOrganizationResult

                data class NotFound(val value: BasicError) : CodespacesDeleteFromOrganizationResult

                data class InternalServerError(val value: BasicError) : CodespacesDeleteFromOrganizationResult
            }

            suspend fun codespacesDeleteFromOrganization(
                org: String,
                username: String,
                codespaceName: String,
            ): CodespacesDeleteFromOrganizationResult

            interface Stop {
                sealed interface CodespacesStopInOrganizationResult {
                    data class OK(val value: Codespace) : CodespacesStopInOrganizationResult

                    data object NotModified : CodespacesStopInOrganizationResult

                    data class Unauthorized(val value: BasicError) : CodespacesStopInOrganizationResult

                    data class Forbidden(val value: BasicError) : CodespacesStopInOrganizationResult

                    data class NotFound(val value: BasicError) : CodespacesStopInOrganizationResult

                    data class InternalServerError(val value: BasicError) : CodespacesStopInOrganizationResult
                }

                suspend fun codespacesStopInOrganization(
                    org: String,
                    username: String,
                    codespaceName: String,
                ): CodespacesStopInOrganizationResult
            }
        }

        interface CopilotApi {
            sealed interface CopilotGetCopilotSeatDetailsForUserResult {
                data class OK(val value: CopilotSeatDetails) : CopilotGetCopilotSeatDetailsForUserResult

                data class Unauthorized(val value: BasicError) : CopilotGetCopilotSeatDetailsForUserResult

                data class Forbidden(val value: BasicError) : CopilotGetCopilotSeatDetailsForUserResult

                data class NotFound(val value: BasicError) : CopilotGetCopilotSeatDetailsForUserResult

                data object UnprocessableEntity : CopilotGetCopilotSeatDetailsForUserResult

                data class InternalServerError(val value: BasicError) : CopilotGetCopilotSeatDetailsForUserResult
            }

            suspend fun copilotGetCopilotSeatDetailsForUser(
                org: String,
                username: String,
            ): CopilotGetCopilotSeatDetailsForUserResult
        }
    }

    interface Memberships {
        @Serializable
        @JvmInline
        value class OrgsSetMembershipForUserBody(val role: Role? = null) {
            @Serializable
            enum class Role {
                @SerialName("admin") Admin, @SerialName("member") Member;
            }
        }

        sealed interface OrgsGetMembershipForUserResult {
            data class OK(val value: OrgMembership) : OrgsGetMembershipForUserResult

            data class Forbidden(val value: BasicError) : OrgsGetMembershipForUserResult

            data class NotFound(val value: BasicError) : OrgsGetMembershipForUserResult
        }

        suspend fun orgsGetMembershipForUser(
            org: String,
            username: String,
        ): OrgsGetMembershipForUserResult

        sealed interface OrgsSetMembershipForUserResult {
            data class OK(val value: OrgMembership) : OrgsSetMembershipForUserResult

            data class Forbidden(val value: BasicError) : OrgsSetMembershipForUserResult

            data class UnprocessableEntity(val value: ValidationError) : OrgsSetMembershipForUserResult
        }

        suspend fun orgsSetMembershipForUser(
            org: String,
            username: String,
            body: OrgsSetMembershipForUserBody? = null,
        ): OrgsSetMembershipForUserResult

        sealed interface OrgsRemoveMembershipForUserResult {
            data object NoContent : OrgsRemoveMembershipForUserResult

            data class Forbidden(val value: BasicError) : OrgsRemoveMembershipForUserResult

            data class NotFound(val value: BasicError) : OrgsRemoveMembershipForUserResult
        }

        suspend fun orgsRemoveMembershipForUser(
            org: String,
            username: String,
        ): OrgsRemoveMembershipForUserResult
    }

    interface Migrations {
        val archive: Orgs.Migrations.Archive

        val repos: Orgs.Migrations.ReposApi

        val repositories: Orgs.Migrations.Repositories

        @Serializable
        enum class Exclude {
            @SerialName("repositories") Repositories;
        }


        @Serializable
        enum class MigrationsListForOrgExclude {
            @SerialName("repositories") Repositories;
        }


        @Serializable
        data class MigrationsStartForOrgBody(
            val repositories: List<String>,
            @SerialName("lock_repositories") val lockRepositories: Boolean? = null,
            @SerialName("exclude_metadata") val excludeMetadata: Boolean? = null,
            @SerialName("exclude_git_data") val excludeGitData: Boolean? = null,
            @SerialName("exclude_attachments") val excludeAttachments: Boolean? = null,
            @SerialName("exclude_releases") val excludeReleases: Boolean? = null,
            @SerialName("exclude_owner_projects") val excludeOwnerProjects: Boolean? = null,
            @SerialName("org_metadata_only") val orgMetadataOnly: Boolean? = null,
            val exclude: List<Exclude>? = null,
        ) {
            @Serializable
            enum class Exclude {
                @SerialName("repositories") Repositories;
            }
        }

        suspend fun migrationsListForOrg(
            org: String,
            page: Long = 1L,
            perPage: Long = 30L,
            exclude: List<MigrationsListForOrgExclude>? = null,
        ): List<Migration>

        sealed interface MigrationsStartForOrgResult {
            data class Created(val value: Migration) : MigrationsStartForOrgResult

            data class NotFound(val value: BasicError) : MigrationsStartForOrgResult

            data class UnprocessableEntity(val value: ValidationError) : MigrationsStartForOrgResult
        }

        suspend fun migrationsStartForOrg(
            org: String,
            body: MigrationsStartForOrgBody,
        ): MigrationsStartForOrgResult

        sealed interface MigrationsGetStatusForOrgResult {
            data class OK(val value: Migration) : MigrationsGetStatusForOrgResult

            data class NotFound(val value: BasicError) : MigrationsGetStatusForOrgResult
        }

        suspend fun migrationsGetStatusForOrg(
            org: String,
            migrationId: Long,
            exclude: List<Exclude>? = null,
        ): MigrationsGetStatusForOrgResult

        interface Archive {
            sealed interface MigrationsDownloadArchiveForOrgResult {
                data object Found : MigrationsDownloadArchiveForOrgResult

                data class NotFound(val value: BasicError) : MigrationsDownloadArchiveForOrgResult
            }

            suspend fun migrationsDownloadArchiveForOrg(
                org: String,
                migrationId: Long,
            ): MigrationsDownloadArchiveForOrgResult

            sealed interface MigrationsDeleteArchiveForOrgResult {
                data object NoContent : MigrationsDeleteArchiveForOrgResult

                data class NotFound(val value: BasicError) : MigrationsDeleteArchiveForOrgResult
            }

            suspend fun migrationsDeleteArchiveForOrg(
                org: String,
                migrationId: Long,
            ): MigrationsDeleteArchiveForOrgResult
        }

        interface ReposApi {
            val lock: Orgs.Migrations.ReposApi.Lock

            interface Lock {
                sealed interface MigrationsUnlockRepoForOrgResult {
                    data object NoContent : MigrationsUnlockRepoForOrgResult

                    data class NotFound(val value: BasicError) : MigrationsUnlockRepoForOrgResult
                }

                suspend fun migrationsUnlockRepoForOrg(
                    org: String,
                    migrationId: Long,
                    repoName: String,
                ): MigrationsUnlockRepoForOrgResult
            }
        }

        interface Repositories {
            sealed interface MigrationsListReposForOrgResult {
                data class OK(val value: List<MinimalRepository>) : MigrationsListReposForOrgResult

                data class NotFound(val value: BasicError) : MigrationsListReposForOrgResult
            }

            suspend fun migrationsListReposForOrg(
                org: String,
                migrationId: Long,
                page: Long = 1L,
                perPage: Long = 30L,
            ): MigrationsListReposForOrgResult
        }
    }

    interface OrganizationRoles {
        val teams: Orgs.OrganizationRoles.TeamsApi

        val users: Orgs.OrganizationRoles.Users

        @Serializable
        data class OrgsListOrgRolesResponse(
            @SerialName("total_count") val totalCount: Long? = null,
            val roles: List<OrganizationRole>? = null,
        )

        sealed interface OrgsListOrgRolesResult {
            data class OK(val value: OrgsListOrgRolesResponse) : OrgsListOrgRolesResult

            data class NotFound(val value: BasicError) : OrgsListOrgRolesResult

            data class UnprocessableEntity(val value: ValidationError) : OrgsListOrgRolesResult
        }

        suspend fun orgsListOrgRoles(
            org: String,
        ): OrgsListOrgRolesResult

        sealed interface OrgsGetOrgRoleResult {
            data class OK(val value: OrganizationRole) : OrgsGetOrgRoleResult

            data class NotFound(val value: BasicError) : OrgsGetOrgRoleResult

            data class UnprocessableEntity(val value: ValidationError) : OrgsGetOrgRoleResult
        }

        suspend fun orgsGetOrgRole(
            org: String,
            roleId: Long,
        ): OrgsGetOrgRoleResult

        interface TeamsApi {
            suspend fun orgsRevokeAllOrgRolesTeam(
                org: String,
                teamSlug: String,
            ): Unit

            sealed interface OrgsAssignTeamToOrgRoleResult {
                data object NoContent : OrgsAssignTeamToOrgRoleResult

                data object NotFound : OrgsAssignTeamToOrgRoleResult

                data object UnprocessableEntity : OrgsAssignTeamToOrgRoleResult
            }

            suspend fun orgsAssignTeamToOrgRole(
                org: String,
                teamSlug: String,
                roleId: Long,
            ): OrgsAssignTeamToOrgRoleResult

            suspend fun orgsRevokeOrgRoleTeam(
                org: String,
                teamSlug: String,
                roleId: Long,
            ): Unit

            sealed interface OrgsListOrgRoleTeamsResult {
                data class OK(val value: List<TeamRoleAssignment>) : OrgsListOrgRoleTeamsResult

                data object NotFound : OrgsListOrgRoleTeamsResult

                data object UnprocessableEntity : OrgsListOrgRoleTeamsResult
            }

            suspend fun orgsListOrgRoleTeams(
                org: String,
                roleId: Long,
                page: Long = 1L,
                perPage: Long = 30L,
            ): OrgsListOrgRoleTeamsResult
        }

        interface Users {
            suspend fun orgsRevokeAllOrgRolesUser(
                org: String,
                username: String,
            ): Unit

            sealed interface OrgsAssignUserToOrgRoleResult {
                data object NoContent : OrgsAssignUserToOrgRoleResult

                data object NotFound : OrgsAssignUserToOrgRoleResult

                data object UnprocessableEntity : OrgsAssignUserToOrgRoleResult
            }

            suspend fun orgsAssignUserToOrgRole(
                org: String,
                username: String,
                roleId: Long,
            ): OrgsAssignUserToOrgRoleResult

            suspend fun orgsRevokeOrgRoleUser(
                org: String,
                username: String,
                roleId: Long,
            ): Unit

            sealed interface OrgsListOrgRoleUsersResult {
                data class OK(val value: List<UserRoleAssignment>) : OrgsListOrgRoleUsersResult

                data object NotFound : OrgsListOrgRoleUsersResult

                data object UnprocessableEntity : OrgsListOrgRoleUsersResult
            }

            suspend fun orgsListOrgRoleUsers(
                org: String,
                roleId: Long,
                page: Long = 1L,
                perPage: Long = 30L,
            ): OrgsListOrgRoleUsersResult
        }
    }

    interface OutsideCollaborators {
        @Serializable
        enum class Filter {
            @JsName("_2faDisabled")@SerialName("2fa_disabled")
            `2faDisabled`,
            @JsName("_2faInsecure")@SerialName("2fa_insecure")
            `2faInsecure`,
            @SerialName("all")
            All;
        }


        @Serializable
        @JvmInline
        value class OrgsConvertMemberToOutsideCollaboratorBody(val async: Boolean? = null)


        @Serializable
        data class OrgsRemoveOutsideCollaboratorResponse(
            val message: String? = null,
            @SerialName("documentation_url") val documentationUrl: String? = null,
        )

        suspend fun orgsListOutsideCollaborators(
            org: String,
            filter: Filter = Filter.All,
            page: Long = 1L,
            perPage: Long = 30L,
        ): List<SimpleUser>

        sealed interface OrgsConvertMemberToOutsideCollaboratorResult {
            data object Accepted : OrgsConvertMemberToOutsideCollaboratorResult

            data object NoContent : OrgsConvertMemberToOutsideCollaboratorResult

            data object Forbidden : OrgsConvertMemberToOutsideCollaboratorResult

            data class NotFound(val value: BasicError) : OrgsConvertMemberToOutsideCollaboratorResult
        }

        suspend fun orgsConvertMemberToOutsideCollaborator(
            org: String,
            username: String,
            body: OrgsConvertMemberToOutsideCollaboratorBody? = null,
        ): OrgsConvertMemberToOutsideCollaboratorResult

        sealed interface OrgsRemoveOutsideCollaboratorResult {
            data object NoContent : OrgsRemoveOutsideCollaboratorResult

            data class UnprocessableEntity(val value: OrgsRemoveOutsideCollaboratorResponse) : OrgsRemoveOutsideCollaboratorResult
        }

        suspend fun orgsRemoveOutsideCollaborator(
            org: String,
            username: String,
        ): OrgsRemoveOutsideCollaboratorResult
    }

    interface Packages {
        val restore: Orgs.Packages.Restore

        val versions: Orgs.Packages.Versions

        @Serializable
        enum class PackageType {
            @SerialName("npm")
            Npm,
            @SerialName("maven")
            Maven,
            @SerialName("rubygems")
            Rubygems,
            @SerialName("docker")
            Docker,
            @SerialName("nuget")
            Nuget,
            @SerialName("container")
            Container;
        }


        @Serializable
        enum class PackagesGetPackageForOrganizationPackageType {
            @SerialName("npm")
            Npm,
            @SerialName("maven")
            Maven,
            @SerialName("rubygems")
            Rubygems,
            @SerialName("docker")
            Docker,
            @SerialName("nuget")
            Nuget,
            @SerialName("container")
            Container;
        }


        @Serializable
        enum class PackagesListPackagesForOrganizationPackageType {
            @SerialName("npm")
            Npm,
            @SerialName("maven")
            Maven,
            @SerialName("rubygems")
            Rubygems,
            @SerialName("docker")
            Docker,
            @SerialName("nuget")
            Nuget,
            @SerialName("container")
            Container;
        }


        @Serializable
        enum class Visibility {
            @SerialName("public") Public, @SerialName("private") Private, @SerialName("internal") Internal;
        }

        sealed interface PackagesListPackagesForOrganizationResult {
            data class OK(val value: List<Package>) : PackagesListPackagesForOrganizationResult

            data object BadRequest : PackagesListPackagesForOrganizationResult

            data class Unauthorized(val value: BasicError) : PackagesListPackagesForOrganizationResult

            data class Forbidden(val value: BasicError) : PackagesListPackagesForOrganizationResult
        }

        suspend fun packagesListPackagesForOrganization(
            org: String,
            packageType: PackagesListPackagesForOrganizationPackageType,
            page: Long = 1L,
            perPage: Long = 30L,
            visibility: Visibility? = null,
        ): PackagesListPackagesForOrganizationResult

        suspend fun packagesGetPackageForOrganization(
            org: String,
            packageType: PackagesGetPackageForOrganizationPackageType,
            packageName: String,
        ): Package

        sealed interface PackagesDeletePackageForOrgResult {
            data object NoContent : PackagesDeletePackageForOrgResult

            data class Unauthorized(val value: BasicError) : PackagesDeletePackageForOrgResult

            data class Forbidden(val value: BasicError) : PackagesDeletePackageForOrgResult

            data class NotFound(val value: BasicError) : PackagesDeletePackageForOrgResult
        }

        suspend fun packagesDeletePackageForOrg(
            org: String,
            packageType: PackageType,
            packageName: String,
        ): PackagesDeletePackageForOrgResult

        interface Restore {
            @Serializable
            enum class PackagesRestorePackageForOrgPackageType {
                @SerialName("npm")
                Npm,
                @SerialName("maven")
                Maven,
                @SerialName("rubygems")
                Rubygems,
                @SerialName("docker")
                Docker,
                @SerialName("nuget")
                Nuget,
                @SerialName("container")
                Container;
            }

            sealed interface PackagesRestorePackageForOrgResult {
                data object NoContent : PackagesRestorePackageForOrgResult

                data class Unauthorized(val value: BasicError) : PackagesRestorePackageForOrgResult

                data class Forbidden(val value: BasicError) : PackagesRestorePackageForOrgResult

                data class NotFound(val value: BasicError) : PackagesRestorePackageForOrgResult
            }

            suspend fun packagesRestorePackageForOrg(
                org: String,
                packageType: PackagesRestorePackageForOrgPackageType,
                packageName: String,
                token: String? = null,
            ): PackagesRestorePackageForOrgResult
        }

        interface Versions {
            val restore: Orgs.Packages.Versions.RestoreApi

            @Serializable
            enum class PackagesDeletePackageVersionForOrgPackageType {
                @SerialName("npm")
                Npm,
                @SerialName("maven")
                Maven,
                @SerialName("rubygems")
                Rubygems,
                @SerialName("docker")
                Docker,
                @SerialName("nuget")
                Nuget,
                @SerialName("container")
                Container;
            }


            @Serializable
            enum class PackagesGetAllPackageVersionsForPackageOwnedByOrgPackageType {
                @SerialName("npm")
                Npm,
                @SerialName("maven")
                Maven,
                @SerialName("rubygems")
                Rubygems,
                @SerialName("docker")
                Docker,
                @SerialName("nuget")
                Nuget,
                @SerialName("container")
                Container;
            }


            @Serializable
            enum class PackagesGetPackageVersionForOrganizationPackageType {
                @SerialName("npm")
                Npm,
                @SerialName("maven")
                Maven,
                @SerialName("rubygems")
                Rubygems,
                @SerialName("docker")
                Docker,
                @SerialName("nuget")
                Nuget,
                @SerialName("container")
                Container;
            }


            @Serializable
            enum class State {
                @SerialName("active") Active, @SerialName("deleted") Deleted;
            }

            sealed interface PackagesGetAllPackageVersionsForPackageOwnedByOrgResult {
                data class OK(val value: List<PackageVersion>) : PackagesGetAllPackageVersionsForPackageOwnedByOrgResult

                data class Unauthorized(val value: BasicError) : PackagesGetAllPackageVersionsForPackageOwnedByOrgResult

                data class Forbidden(val value: BasicError) : PackagesGetAllPackageVersionsForPackageOwnedByOrgResult

                data class NotFound(val value: BasicError) : PackagesGetAllPackageVersionsForPackageOwnedByOrgResult
            }

            suspend fun packagesGetAllPackageVersionsForPackageOwnedByOrg(
                org: String,
                packageType: PackagesGetAllPackageVersionsForPackageOwnedByOrgPackageType,
                packageName: String,
                page: Long = 1L,
                perPage: Long = 30L,
                state: State = State.Active,
            ): PackagesGetAllPackageVersionsForPackageOwnedByOrgResult

            suspend fun packagesGetPackageVersionForOrganization(
                org: String,
                packageType: PackagesGetPackageVersionForOrganizationPackageType,
                packageName: String,
                packageVersionId: Long,
            ): PackageVersion

            sealed interface PackagesDeletePackageVersionForOrgResult {
                data object NoContent : PackagesDeletePackageVersionForOrgResult

                data class Unauthorized(val value: BasicError) : PackagesDeletePackageVersionForOrgResult

                data class Forbidden(val value: BasicError) : PackagesDeletePackageVersionForOrgResult

                data class NotFound(val value: BasicError) : PackagesDeletePackageVersionForOrgResult
            }

            suspend fun packagesDeletePackageVersionForOrg(
                org: String,
                packageType: PackagesDeletePackageVersionForOrgPackageType,
                packageName: String,
                packageVersionId: Long,
            ): PackagesDeletePackageVersionForOrgResult

            interface RestoreApi {
                @Serializable
                enum class PackagesRestorePackageVersionForOrgPackageType {
                    @SerialName("npm")
                    Npm,
                    @SerialName("maven")
                    Maven,
                    @SerialName("rubygems")
                    Rubygems,
                    @SerialName("docker")
                    Docker,
                    @SerialName("nuget")
                    Nuget,
                    @SerialName("container")
                    Container;
                }

                sealed interface PackagesRestorePackageVersionForOrgResult {
                    data object NoContent : PackagesRestorePackageVersionForOrgResult

                    data class Unauthorized(val value: BasicError) : PackagesRestorePackageVersionForOrgResult

                    data class Forbidden(val value: BasicError) : PackagesRestorePackageVersionForOrgResult

                    data class NotFound(val value: BasicError) : PackagesRestorePackageVersionForOrgResult
                }

                suspend fun packagesRestorePackageVersionForOrg(
                    org: String,
                    packageType: PackagesRestorePackageVersionForOrgPackageType,
                    packageName: String,
                    packageVersionId: Long,
                ): PackagesRestorePackageVersionForOrgResult
            }
        }
    }

    interface PersonalAccessTokenRequests {
        val repositories: Orgs.PersonalAccessTokenRequests.Repositories

        @Serializable
        enum class Direction {
            @SerialName("asc") Asc, @SerialName("desc") Desc;
        }


        @Serializable
        data class OrgsReviewPatGrantRequestBody(val action: Action, val reason: String? = null) {
            @Serializable
            enum class Action {
                @SerialName("approve") Approve, @SerialName("deny") Deny;
            }
        }


        @Serializable
        data class OrgsReviewPatGrantRequestsInBulkBody(
            @SerialName("pat_request_ids") val patRequestIds: List<Long>? = null,
            val action: Action,
            val reason: String? = null,
        ) {
            @Serializable
            enum class Action {
                @SerialName("approve") Approve, @SerialName("deny") Deny;
            }
        }


        @Serializable
        enum class Sort {
            @SerialName("created_at") CreatedAt;
        }

        sealed interface OrgsListPatGrantRequestsResult {
            data class OK(val value: List<OrganizationProgrammaticAccessGrantRequest>) : OrgsListPatGrantRequestsResult

            data class Forbidden(val value: BasicError) : OrgsListPatGrantRequestsResult

            data class NotFound(val value: BasicError) : OrgsListPatGrantRequestsResult

            data class UnprocessableEntity(val value: ValidationError) : OrgsListPatGrantRequestsResult

            data class InternalServerError(val value: BasicError) : OrgsListPatGrantRequestsResult
        }

        suspend fun orgsListPatGrantRequests(
            org: String,
            direction: Direction = Direction.Desc,
            page: Long = 1L,
            perPage: Long = 30L,
            sort: Sort = Sort.CreatedAt,
            lastUsedAfter: LocalDateTime? = null,
            lastUsedBefore: LocalDateTime? = null,
            owner: List<String>? = null,
            permission: String? = null,
            repository: String? = null,
            tokenId: List<String>? = null,
        ): OrgsListPatGrantRequestsResult

        sealed interface OrgsReviewPatGrantRequestsInBulkResult {
            data class Accepted(val value: JsonElement) : OrgsReviewPatGrantRequestsInBulkResult

            data class Forbidden(val value: BasicError) : OrgsReviewPatGrantRequestsInBulkResult

            data class NotFound(val value: BasicError) : OrgsReviewPatGrantRequestsInBulkResult

            data class UnprocessableEntity(val value: ValidationError) : OrgsReviewPatGrantRequestsInBulkResult

            data class InternalServerError(val value: BasicError) : OrgsReviewPatGrantRequestsInBulkResult
        }

        suspend fun orgsReviewPatGrantRequestsInBulk(
            org: String,
            body: OrgsReviewPatGrantRequestsInBulkBody,
        ): OrgsReviewPatGrantRequestsInBulkResult

        sealed interface OrgsReviewPatGrantRequestResult {
            data object NoContent : OrgsReviewPatGrantRequestResult

            data class Forbidden(val value: BasicError) : OrgsReviewPatGrantRequestResult

            data class NotFound(val value: BasicError) : OrgsReviewPatGrantRequestResult

            data class UnprocessableEntity(val value: ValidationError) : OrgsReviewPatGrantRequestResult

            data class InternalServerError(val value: BasicError) : OrgsReviewPatGrantRequestResult
        }

        suspend fun orgsReviewPatGrantRequest(
            org: String,
            patRequestId: Long,
            body: OrgsReviewPatGrantRequestBody,
        ): OrgsReviewPatGrantRequestResult

        interface Repositories {
            sealed interface OrgsListPatGrantRequestRepositoriesResult {
                data class OK(val value: List<MinimalRepository>) : OrgsListPatGrantRequestRepositoriesResult

                data class Forbidden(val value: BasicError) : OrgsListPatGrantRequestRepositoriesResult

                data class NotFound(val value: BasicError) : OrgsListPatGrantRequestRepositoriesResult

                data class InternalServerError(val value: BasicError) : OrgsListPatGrantRequestRepositoriesResult
            }

            suspend fun orgsListPatGrantRequestRepositories(
                org: String,
                patRequestId: Long,
                page: Long = 1L,
                perPage: Long = 30L,
            ): OrgsListPatGrantRequestRepositoriesResult
        }
    }

    interface PersonalAccessTokens {
        val repositories: Orgs.PersonalAccessTokens.Repositories

        @Serializable
        enum class Direction {
            @SerialName("asc") Asc, @SerialName("desc") Desc;
        }


        @Serializable
        @JvmInline
        value class OrgsUpdatePatAccessBody(val action: Action) {
            @Serializable
            enum class Action {
                @SerialName("revoke") Revoke;
            }
        }


        @Serializable
        data class OrgsUpdatePatAccessesBody(val action: Action, @SerialName("pat_ids") val patIds: List<Long>) {
            @Serializable
            enum class Action {
                @SerialName("revoke") Revoke;
            }
        }


        @Serializable
        enum class Sort {
            @SerialName("created_at") CreatedAt;
        }

        sealed interface OrgsListPatGrantsResult {
            data class OK(val value: List<OrganizationProgrammaticAccessGrant>) : OrgsListPatGrantsResult

            data class Forbidden(val value: BasicError) : OrgsListPatGrantsResult

            data class NotFound(val value: BasicError) : OrgsListPatGrantsResult

            data class UnprocessableEntity(val value: ValidationError) : OrgsListPatGrantsResult

            data class InternalServerError(val value: BasicError) : OrgsListPatGrantsResult
        }

        suspend fun orgsListPatGrants(
            org: String,
            direction: Direction = Direction.Desc,
            page: Long = 1L,
            perPage: Long = 30L,
            sort: Sort = Sort.CreatedAt,
            lastUsedAfter: LocalDateTime? = null,
            lastUsedBefore: LocalDateTime? = null,
            owner: List<String>? = null,
            permission: String? = null,
            repository: String? = null,
            tokenId: List<String>? = null,
        ): OrgsListPatGrantsResult

        sealed interface OrgsUpdatePatAccessesResult {
            data class Accepted(val value: JsonElement) : OrgsUpdatePatAccessesResult

            data class Forbidden(val value: BasicError) : OrgsUpdatePatAccessesResult

            data class NotFound(val value: BasicError) : OrgsUpdatePatAccessesResult

            data class UnprocessableEntity(val value: ValidationError) : OrgsUpdatePatAccessesResult

            data class InternalServerError(val value: BasicError) : OrgsUpdatePatAccessesResult
        }

        suspend fun orgsUpdatePatAccesses(
            org: String,
            body: OrgsUpdatePatAccessesBody,
        ): OrgsUpdatePatAccessesResult

        sealed interface OrgsUpdatePatAccessResult {
            data object NoContent : OrgsUpdatePatAccessResult

            data class Forbidden(val value: BasicError) : OrgsUpdatePatAccessResult

            data class NotFound(val value: BasicError) : OrgsUpdatePatAccessResult

            data class UnprocessableEntity(val value: ValidationError) : OrgsUpdatePatAccessResult

            data class InternalServerError(val value: BasicError) : OrgsUpdatePatAccessResult
        }

        suspend fun orgsUpdatePatAccess(
            org: String,
            patId: Long,
            body: OrgsUpdatePatAccessBody,
        ): OrgsUpdatePatAccessResult

        interface Repositories {
            sealed interface OrgsListPatGrantRepositoriesResult {
                data class OK(val value: List<MinimalRepository>) : OrgsListPatGrantRepositoriesResult

                data class Forbidden(val value: BasicError) : OrgsListPatGrantRepositoriesResult

                data class NotFound(val value: BasicError) : OrgsListPatGrantRepositoriesResult

                data class InternalServerError(val value: BasicError) : OrgsListPatGrantRepositoriesResult
            }

            suspend fun orgsListPatGrantRepositories(
                org: String,
                patId: Long,
                page: Long = 1L,
                perPage: Long = 30L,
            ): OrgsListPatGrantRepositoriesResult
        }
    }

    interface PrivateRegistries {
        val publicKey: Orgs.PrivateRegistries.PublicKey

        @Serializable
        data class PrivateRegistriesCreateOrgPrivateRegistryBody(
            @SerialName("registry_type") val registryType: RegistryType,
            val url: String,
            val username: String? = null,
            @SerialName("replaces_base") val replacesBase: Boolean? = null,
            @SerialName("encrypted_value") val encryptedValue: String,
            @SerialName("key_id") val keyId: String,
            val visibility: Visibility,
            @SerialName("selected_repository_ids") val selectedRepositoryIds: List<Long>? = null,
        ) {
            @Serializable
            enum class RegistryType {
                @SerialName("maven_repository")
                MavenRepository,
                @SerialName("nuget_feed")
                NugetFeed,
                @SerialName("goproxy_server")
                GoproxyServer,
                @SerialName("npm_registry")
                NpmRegistry,
                @SerialName("rubygems_server")
                RubygemsServer,
                @SerialName("cargo_registry")
                CargoRegistry,
                @SerialName("composer_repository")
                ComposerRepository,
                @SerialName("docker_registry")
                DockerRegistry,
                @SerialName("git_source")
                GitSource,
                @SerialName("helm_registry")
                HelmRegistry,
                @SerialName("hex_organization")
                HexOrganization,
                @SerialName("hex_repository")
                HexRepository,
                @SerialName("pub_repository")
                PubRepository,
                @SerialName("python_index")
                PythonIndex,
                @SerialName("terraform_registry")
                TerraformRegistry;
            }

            @Serializable
            enum class Visibility {
                @SerialName("all") All, @SerialName("private") Private, @SerialName("selected") Selected;
            }
        }


        @Serializable
        data class PrivateRegistriesListOrgPrivateRegistriesResponse(
            @SerialName("total_count") val totalCount: Long,
            val configurations: List<OrgPrivateRegistryConfiguration>,
        )


        @Serializable
        data class PrivateRegistriesUpdateOrgPrivateRegistryBody(
            @SerialName("registry_type") val registryType: RegistryType? = null,
            val url: String? = null,
            val username: String? = null,
            @SerialName("replaces_base") val replacesBase: Boolean? = null,
            @SerialName("encrypted_value") val encryptedValue: String? = null,
            @SerialName("key_id") val keyId: String? = null,
            val visibility: Visibility? = null,
            @SerialName("selected_repository_ids") val selectedRepositoryIds: List<Long>? = null,
        ) {
            @Serializable
            enum class RegistryType {
                @SerialName("maven_repository")
                MavenRepository,
                @SerialName("nuget_feed")
                NugetFeed,
                @SerialName("goproxy_server")
                GoproxyServer,
                @SerialName("npm_registry")
                NpmRegistry,
                @SerialName("rubygems_server")
                RubygemsServer,
                @SerialName("cargo_registry")
                CargoRegistry,
                @SerialName("composer_repository")
                ComposerRepository,
                @SerialName("docker_registry")
                DockerRegistry,
                @SerialName("git_source")
                GitSource,
                @SerialName("helm_registry")
                HelmRegistry,
                @SerialName("hex_organization")
                HexOrganization,
                @SerialName("hex_repository")
                HexRepository,
                @SerialName("pub_repository")
                PubRepository,
                @SerialName("python_index")
                PythonIndex,
                @SerialName("terraform_registry")
                TerraformRegistry;
            }

            @Serializable
            enum class Visibility {
                @SerialName("all") All, @SerialName("private") Private, @SerialName("selected") Selected;
            }
        }

        sealed interface PrivateRegistriesListOrgPrivateRegistriesResult {
            data class OK(val value: PrivateRegistriesListOrgPrivateRegistriesResponse) : PrivateRegistriesListOrgPrivateRegistriesResult

            data class BadRequest(val value: BasicError) : PrivateRegistriesListOrgPrivateRegistriesResult

            data class NotFound(val value: BasicError) : PrivateRegistriesListOrgPrivateRegistriesResult
        }

        suspend fun privateRegistriesListOrgPrivateRegistries(
            org: String,
            page: Long = 1L,
            perPage: Long = 30L,
        ): PrivateRegistriesListOrgPrivateRegistriesResult

        sealed interface PrivateRegistriesCreateOrgPrivateRegistryResult {
            data class Created(val value: OrgPrivateRegistryConfigurationWithSelectedRepositories) : PrivateRegistriesCreateOrgPrivateRegistryResult

            data class NotFound(val value: BasicError) : PrivateRegistriesCreateOrgPrivateRegistryResult

            data class UnprocessableEntity(val value: ValidationError) : PrivateRegistriesCreateOrgPrivateRegistryResult
        }

        suspend fun privateRegistriesCreateOrgPrivateRegistry(
            org: String,
            body: PrivateRegistriesCreateOrgPrivateRegistryBody,
        ): PrivateRegistriesCreateOrgPrivateRegistryResult

        sealed interface PrivateRegistriesGetOrgPrivateRegistryResult {
            data class OK(val value: OrgPrivateRegistryConfiguration) : PrivateRegistriesGetOrgPrivateRegistryResult

            data class NotFound(val value: BasicError) : PrivateRegistriesGetOrgPrivateRegistryResult
        }

        suspend fun privateRegistriesGetOrgPrivateRegistry(
            org: String,
            secretName: String,
        ): PrivateRegistriesGetOrgPrivateRegistryResult

        sealed interface PrivateRegistriesDeleteOrgPrivateRegistryResult {
            data object NoContent : PrivateRegistriesDeleteOrgPrivateRegistryResult

            data class BadRequest(val value: BasicError) : PrivateRegistriesDeleteOrgPrivateRegistryResult

            data class NotFound(val value: BasicError) : PrivateRegistriesDeleteOrgPrivateRegistryResult
        }

        suspend fun privateRegistriesDeleteOrgPrivateRegistry(
            org: String,
            secretName: String,
        ): PrivateRegistriesDeleteOrgPrivateRegistryResult

        sealed interface PrivateRegistriesUpdateOrgPrivateRegistryResult {
            data object NoContent : PrivateRegistriesUpdateOrgPrivateRegistryResult

            data class NotFound(val value: BasicError) : PrivateRegistriesUpdateOrgPrivateRegistryResult

            data class UnprocessableEntity(val value: ValidationError) : PrivateRegistriesUpdateOrgPrivateRegistryResult
        }

        suspend fun privateRegistriesUpdateOrgPrivateRegistry(
            org: String,
            secretName: String,
            body: PrivateRegistriesUpdateOrgPrivateRegistryBody,
        ): PrivateRegistriesUpdateOrgPrivateRegistryResult

        interface PublicKey {
            @Serializable
            data class PrivateRegistriesGetOrgPublicKeyResponse(@SerialName("key_id") val keyId: String, val key: String)

            sealed interface PrivateRegistriesGetOrgPublicKeyResult {
                data class OK(val value: PrivateRegistriesGetOrgPublicKeyResponse) : PrivateRegistriesGetOrgPublicKeyResult

                data class NotFound(val value: BasicError) : PrivateRegistriesGetOrgPublicKeyResult
            }

            suspend fun privateRegistriesGetOrgPublicKey(
                org: String,
            ): PrivateRegistriesGetOrgPublicKeyResult
        }
    }

    interface ProjectsV2 {
        val drafts: Orgs.ProjectsV2.Drafts

        val fields: Orgs.ProjectsV2.Fields

        val items: Orgs.ProjectsV2.Items

        val views: Orgs.ProjectsV2.Views

        sealed interface ProjectsListForOrgResult {
            data class OK(val value: List<ProjectsV2>) : ProjectsListForOrgResult

            data object NotModified : ProjectsListForOrgResult

            data class Unauthorized(val value: BasicError) : ProjectsListForOrgResult

            data class Forbidden(val value: BasicError) : ProjectsListForOrgResult
        }

        suspend fun projectsListForOrg(
            org: String,
            perPage: Long = 30L,
            after: String? = null,
            before: String? = null,
            q: String? = null,
        ): ProjectsListForOrgResult

        sealed interface ProjectsGetForOrgResult {
            data class OK(val value: ProjectsV2) : ProjectsGetForOrgResult

            data object NotModified : ProjectsGetForOrgResult

            data class Unauthorized(val value: BasicError) : ProjectsGetForOrgResult

            data class Forbidden(val value: BasicError) : ProjectsGetForOrgResult
        }

        suspend fun projectsGetForOrg(
            org: String,
            projectNumber: Long,
        ): ProjectsGetForOrgResult

        interface Drafts {
            @Serializable
            data class ProjectsCreateDraftItemForOrgBody(val title: String, val body: String? = null)

            sealed interface ProjectsCreateDraftItemForOrgResult {
                data class Created(val value: ProjectsV2ItemSimple) : ProjectsCreateDraftItemForOrgResult

                data object NotModified : ProjectsCreateDraftItemForOrgResult

                data class Unauthorized(val value: BasicError) : ProjectsCreateDraftItemForOrgResult

                data class Forbidden(val value: BasicError) : ProjectsCreateDraftItemForOrgResult
            }

            suspend fun projectsCreateDraftItemForOrg(
                org: String,
                projectNumber: Long,
                body: ProjectsCreateDraftItemForOrgBody,
            ): ProjectsCreateDraftItemForOrgResult
        }

        interface Fields {
            @Serializable(with = ProjectsAddFieldForOrgBody.Serializer::class)
            sealed interface ProjectsAddFieldForOrgBody {
                @Serializable
                @JvmInline
                value class IssueFieldId(@SerialName("issue_field_id") val issueFieldId: Long) : ProjectsAddFieldForOrgBody

                @Serializable
                data class NameAndDataType(
                    val name: String,
                    @SerialName("data_type") val dataType: DataType,
                ) : ProjectsAddFieldForOrgBody {
                    @Serializable
                    enum class DataType {
                        @SerialName("text") Text, @SerialName("number") Number, @SerialName("date") Date;
                    }
                }

                @Serializable
                data class NameAndDataTypeAndSingleSelectOptions(
                    val name: String,
                    @SerialName("data_type") val dataType: DataType,
                    @SerialName("single_select_options") val singleSelectOptions: List<ProjectsV2FieldSingleSelectOption>,
                ) : ProjectsAddFieldForOrgBody {
                    @Serializable
                    enum class DataType {
                        @SerialName("single_select") SingleSelect;
                    }
                }

                @Serializable
                data class NameAndDataTypeAndIterationConfiguration(
                    val name: String,
                    @SerialName("data_type") val dataType: DataType,
                    @SerialName("iteration_configuration") val iterationConfiguration: ProjectsV2FieldIterationConfiguration,
                ) : ProjectsAddFieldForOrgBody {
                    @Serializable
                    enum class DataType {
                        @SerialName("iteration") Iteration;
                    }
                }

                object Serializer : KSerializer<ProjectsAddFieldForOrgBody> {
                    @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
                    override val descriptor: SerialDescriptor =
                        buildSerialDescriptor("io.github.nomisrev.api.Orgs.ProjectsV2.Fields.ProjectsAddFieldForOrgBody", PolymorphicKind.SEALED) {
                            element("IssueFieldId", ProjectsAddFieldForOrgBody.IssueFieldId.serializer().descriptor)
                            element("NameAndDataType", ProjectsAddFieldForOrgBody.NameAndDataType.serializer().descriptor)
                            element("NameAndDataTypeAndSingleSelectOptions", ProjectsAddFieldForOrgBody.NameAndDataTypeAndSingleSelectOptions.serializer().descriptor)
                            element("NameAndDataTypeAndIterationConfiguration", ProjectsAddFieldForOrgBody.NameAndDataTypeAndIterationConfiguration.serializer().descriptor)
                        }

                    override fun deserialize(decoder: Decoder): ProjectsAddFieldForOrgBody {
                        val value = decoder.decodeSerializableValue(JsonElement.serializer())
                        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                        return json.attemptDeserialize(
                            value,
                            NameAndDataTypeAndSingleSelectOptions::class to { decodeFromJsonElement(ProjectsAddFieldForOrgBody.NameAndDataTypeAndSingleSelectOptions.serializer(), it) },
                            NameAndDataTypeAndIterationConfiguration::class to { decodeFromJsonElement(ProjectsAddFieldForOrgBody.NameAndDataTypeAndIterationConfiguration.serializer(), it) },
                            NameAndDataType::class to { decodeFromJsonElement(ProjectsAddFieldForOrgBody.NameAndDataType.serializer(), it) },
                            IssueFieldId::class to { decodeFromJsonElement(ProjectsAddFieldForOrgBody.IssueFieldId.serializer(), it) },
                        )
                    }

                    override fun serialize(encoder: Encoder, value: ProjectsAddFieldForOrgBody) = when(value) {
                        is IssueFieldId -> encoder.encodeSerializableValue(ProjectsAddFieldForOrgBody.IssueFieldId.serializer(), value)
                        is NameAndDataType -> encoder.encodeSerializableValue(ProjectsAddFieldForOrgBody.NameAndDataType.serializer(), value)
                        is NameAndDataTypeAndSingleSelectOptions -> encoder.encodeSerializableValue(ProjectsAddFieldForOrgBody.NameAndDataTypeAndSingleSelectOptions.serializer(), value)
                        is NameAndDataTypeAndIterationConfiguration -> encoder.encodeSerializableValue(ProjectsAddFieldForOrgBody.NameAndDataTypeAndIterationConfiguration.serializer(), value)
                    }
                }
            }

            sealed interface ProjectsListFieldsForOrgResult {
                data class OK(val value: List<ProjectsV2Field>) : ProjectsListFieldsForOrgResult

                data object NotModified : ProjectsListFieldsForOrgResult

                data class Unauthorized(val value: BasicError) : ProjectsListFieldsForOrgResult

                data class Forbidden(val value: BasicError) : ProjectsListFieldsForOrgResult
            }

            suspend fun projectsListFieldsForOrg(
                org: String,
                projectNumber: Long,
                perPage: Long = 30L,
                after: String? = null,
                before: String? = null,
            ): ProjectsListFieldsForOrgResult

            sealed interface ProjectsAddFieldForOrgResult {
                data class Created(val value: ProjectsV2Field) : ProjectsAddFieldForOrgResult

                data object NotModified : ProjectsAddFieldForOrgResult

                data class Unauthorized(val value: BasicError) : ProjectsAddFieldForOrgResult

                data class Forbidden(val value: BasicError) : ProjectsAddFieldForOrgResult

                data class UnprocessableEntity(val value: ValidationError) : ProjectsAddFieldForOrgResult
            }

            suspend fun projectsAddFieldForOrg(
                org: String,
                projectNumber: Long,
                body: ProjectsAddFieldForOrgBody,
            ): ProjectsAddFieldForOrgResult

            sealed interface ProjectsGetFieldForOrgResult {
                data class OK(val value: ProjectsV2Field) : ProjectsGetFieldForOrgResult

                data object NotModified : ProjectsGetFieldForOrgResult

                data class Unauthorized(val value: BasicError) : ProjectsGetFieldForOrgResult

                data class Forbidden(val value: BasicError) : ProjectsGetFieldForOrgResult
            }

            suspend fun projectsGetFieldForOrg(
                org: String,
                projectNumber: Long,
                fieldId: Long,
            ): ProjectsGetFieldForOrgResult
        }

        interface Items {
            @Serializable
            data class ProjectsAddItemForOrgBody(
                val type: Type,
                val id: Long? = null,
                val owner: String? = null,
                val repo: String? = null,
                val number: Long? = null,
            ) {
                @Serializable
                enum class Type {
                    Issue, PullRequest;
                }
            }


            @Serializable(with = ProjectsGetOrgItemFields.Serializer::class)
            sealed interface ProjectsGetOrgItemFields {
                @Serializable
                @JvmInline
                value class CaseString(val value: String) : ProjectsGetOrgItemFields

                @Serializable
                @JvmInline
                value class CaseStrings(val value: List<String>) : ProjectsGetOrgItemFields

                object Serializer : KSerializer<ProjectsGetOrgItemFields> {
                    @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
                    override val descriptor: SerialDescriptor =
                        buildSerialDescriptor("io.github.nomisrev.api.Orgs.ProjectsV2.Items.ProjectsGetOrgItemFields", PolymorphicKind.SEALED) {
                            element("CaseString", String.serializer().descriptor)
                            element("CaseStrings", ListSerializer(String.serializer()).descriptor)
                        }

                    override fun deserialize(decoder: Decoder): ProjectsGetOrgItemFields {
                        val value = decoder.decodeSerializableValue(JsonElement.serializer())
                        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                        return json.attemptDeserialize(
                            value,
                            CaseStrings::class to { CaseStrings(decodeFromJsonElement(ListSerializer(String.serializer()), it)) },
                            CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                        )
                    }

                    override fun serialize(encoder: Encoder, value: ProjectsGetOrgItemFields) = when(value) {
                        is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                        is CaseStrings -> encoder.encodeSerializableValue(ListSerializer(String.serializer()), value.value)
                    }
                }
            }


            @Serializable(with = ProjectsListItemsForOrgFields.Serializer::class)
            sealed interface ProjectsListItemsForOrgFields {
                @Serializable
                @JvmInline
                value class CaseString(val value: String) : ProjectsListItemsForOrgFields

                @Serializable
                @JvmInline
                value class CaseStrings(val value: List<String>) : ProjectsListItemsForOrgFields

                object Serializer : KSerializer<ProjectsListItemsForOrgFields> {
                    @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
                    override val descriptor: SerialDescriptor =
                        buildSerialDescriptor("io.github.nomisrev.api.Orgs.ProjectsV2.Items.ProjectsListItemsForOrgFields", PolymorphicKind.SEALED) {
                            element("CaseString", String.serializer().descriptor)
                            element("CaseStrings", ListSerializer(String.serializer()).descriptor)
                        }

                    override fun deserialize(decoder: Decoder): ProjectsListItemsForOrgFields {
                        val value = decoder.decodeSerializableValue(JsonElement.serializer())
                        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                        return json.attemptDeserialize(
                            value,
                            CaseStrings::class to { CaseStrings(decodeFromJsonElement(ListSerializer(String.serializer()), it)) },
                            CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                        )
                    }

                    override fun serialize(encoder: Encoder, value: ProjectsListItemsForOrgFields) = when(value) {
                        is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                        is CaseStrings -> encoder.encodeSerializableValue(ListSerializer(String.serializer()), value.value)
                    }
                }
            }


            @Serializable
            @JvmInline
            value class ProjectsUpdateItemForOrgBody(val fields: List<Fields>) {
                @Serializable
                data class Fields(val id: Long, val value: Value?) {
                    @Serializable(with = Value.Serializer::class)
                    sealed interface Value {
                        @Serializable
                        @JvmInline
                        value class CaseString(val value: String) : Value

                        @Serializable
                        @JvmInline
                        value class CaseDouble(val value: Double) : Value

                        object Serializer : KSerializer<Value> {
                            @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
                            override val descriptor: SerialDescriptor =
                                buildSerialDescriptor("io.github.nomisrev.api.Orgs.ProjectsV2.Items.ProjectsUpdateItemForOrgBody.Fields.Value", PolymorphicKind.SEALED) {
                                    element("CaseString", String.serializer().descriptor)
                                    element("CaseDouble", Double.serializer().descriptor)
                                }

                            override fun deserialize(decoder: Decoder): Value {
                                val value = decoder.decodeSerializableValue(JsonElement.serializer())
                                val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                                return json.attemptDeserialize(
                                    value,
                                    CaseDouble::class to { CaseDouble(decodeFromJsonElement(Double.serializer(), it)) },
                                    CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                                )
                            }

                            override fun serialize(encoder: Encoder, value: Value) = when(value) {
                                is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                                is CaseDouble -> encoder.encodeSerializableValue(Double.serializer(), value.value)
                            }
                        }
                    }
                }
            }

            sealed interface ProjectsListItemsForOrgResult {
                data class OK(val value: List<ProjectsV2ItemWithContent>) : ProjectsListItemsForOrgResult

                data object NotModified : ProjectsListItemsForOrgResult

                data class Unauthorized(val value: BasicError) : ProjectsListItemsForOrgResult

                data class Forbidden(val value: BasicError) : ProjectsListItemsForOrgResult
            }

            suspend fun projectsListItemsForOrg(
                org: String,
                projectNumber: Long,
                perPage: Long = 30L,
                after: String? = null,
                before: String? = null,
                fields: ProjectsListItemsForOrgFields? = null,
                q: String? = null,
            ): ProjectsListItemsForOrgResult

            sealed interface ProjectsAddItemForOrgResult {
                data class Created(val value: ProjectsV2ItemSimple) : ProjectsAddItemForOrgResult

                data object NotModified : ProjectsAddItemForOrgResult

                data class Unauthorized(val value: BasicError) : ProjectsAddItemForOrgResult

                data class Forbidden(val value: BasicError) : ProjectsAddItemForOrgResult
            }

            suspend fun projectsAddItemForOrg(
                org: String,
                projectNumber: Long,
                body: ProjectsAddItemForOrgBody,
            ): ProjectsAddItemForOrgResult

            sealed interface ProjectsGetOrgItemResult {
                data class OK(val value: ProjectsV2ItemWithContent) : ProjectsGetOrgItemResult

                data object NotModified : ProjectsGetOrgItemResult

                data class Unauthorized(val value: BasicError) : ProjectsGetOrgItemResult

                data class Forbidden(val value: BasicError) : ProjectsGetOrgItemResult
            }

            suspend fun projectsGetOrgItem(
                org: String,
                projectNumber: Long,
                itemId: Long,
                fields: ProjectsGetOrgItemFields? = null,
            ): ProjectsGetOrgItemResult

            sealed interface ProjectsDeleteItemForOrgResult {
                data object NoContent : ProjectsDeleteItemForOrgResult

                data class Unauthorized(val value: BasicError) : ProjectsDeleteItemForOrgResult

                data class Forbidden(val value: BasicError) : ProjectsDeleteItemForOrgResult
            }

            suspend fun projectsDeleteItemForOrg(
                org: String,
                projectNumber: Long,
                itemId: Long,
            ): ProjectsDeleteItemForOrgResult

            sealed interface ProjectsUpdateItemForOrgResult {
                data class OK(val value: ProjectsV2ItemWithContent) : ProjectsUpdateItemForOrgResult

                data class Unauthorized(val value: BasicError) : ProjectsUpdateItemForOrgResult

                data class Forbidden(val value: BasicError) : ProjectsUpdateItemForOrgResult

                data class NotFound(val value: BasicError) : ProjectsUpdateItemForOrgResult

                data class UnprocessableEntity(val value: ValidationError) : ProjectsUpdateItemForOrgResult
            }

            suspend fun projectsUpdateItemForOrg(
                org: String,
                projectNumber: Long,
                itemId: Long,
                body: ProjectsUpdateItemForOrgBody,
            ): ProjectsUpdateItemForOrgResult
        }

        interface Views {
            val items: Orgs.ProjectsV2.Views.ItemsApi

            @Serializable
            data class ProjectsCreateViewForOrgBody(
                val name: String,
                val layout: Layout,
                val filter: String? = null,
                @SerialName("visible_fields") val visibleFields: List<Long>? = null,
            ) {
                @Serializable
                enum class Layout {
                    @SerialName("table") Table, @SerialName("board") Board, @SerialName("roadmap") Roadmap;
                }
            }

            sealed interface ProjectsCreateViewForOrgResult {
                data class Created(val value: ProjectsV2View) : ProjectsCreateViewForOrgResult

                data object NotModified : ProjectsCreateViewForOrgResult

                data class Unauthorized(val value: BasicError) : ProjectsCreateViewForOrgResult

                data class Forbidden(val value: BasicError) : ProjectsCreateViewForOrgResult

                data class NotFound(val value: BasicError) : ProjectsCreateViewForOrgResult

                data class UnprocessableEntity(val value: ValidationError) : ProjectsCreateViewForOrgResult

                data class ServiceUnavailable(val value: BasicError) : ProjectsCreateViewForOrgResult
            }

            suspend fun projectsCreateViewForOrg(
                org: String,
                projectNumber: Long,
                body: ProjectsCreateViewForOrgBody,
            ): ProjectsCreateViewForOrgResult

            interface ItemsApi {
                @Serializable(with = ProjectsListViewItemsForOrgFields.Serializer::class)
                sealed interface ProjectsListViewItemsForOrgFields {
                    @Serializable
                    @JvmInline
                    value class CaseString(val value: String) : ProjectsListViewItemsForOrgFields

                    @Serializable
                    @JvmInline
                    value class CaseStrings(val value: List<String>) : ProjectsListViewItemsForOrgFields

                    object Serializer : KSerializer<ProjectsListViewItemsForOrgFields> {
                        @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
                        override val descriptor: SerialDescriptor =
                            buildSerialDescriptor("io.github.nomisrev.api.Orgs.ProjectsV2.Views.ItemsApi.ProjectsListViewItemsForOrgFields", PolymorphicKind.SEALED) {
                                element("CaseString", String.serializer().descriptor)
                                element("CaseStrings", ListSerializer(String.serializer()).descriptor)
                            }

                        override fun deserialize(decoder: Decoder): ProjectsListViewItemsForOrgFields {
                            val value = decoder.decodeSerializableValue(JsonElement.serializer())
                            val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                            return json.attemptDeserialize(
                                value,
                                CaseStrings::class to { CaseStrings(decodeFromJsonElement(ListSerializer(String.serializer()), it)) },
                                CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                            )
                        }

                        override fun serialize(encoder: Encoder, value: ProjectsListViewItemsForOrgFields) = when(value) {
                            is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                            is CaseStrings -> encoder.encodeSerializableValue(ListSerializer(String.serializer()), value.value)
                        }
                    }
                }

                sealed interface ProjectsListViewItemsForOrgResult {
                    data class OK(val value: List<ProjectsV2ItemWithContent>) : ProjectsListViewItemsForOrgResult

                    data object NotModified : ProjectsListViewItemsForOrgResult

                    data class Unauthorized(val value: BasicError) : ProjectsListViewItemsForOrgResult

                    data class Forbidden(val value: BasicError) : ProjectsListViewItemsForOrgResult

                    data class NotFound(val value: BasicError) : ProjectsListViewItemsForOrgResult
                }

                suspend fun projectsListViewItemsForOrg(
                    org: String,
                    projectNumber: Long,
                    viewNumber: Long,
                    perPage: Long = 30L,
                    after: String? = null,
                    before: String? = null,
                    fields: ProjectsListViewItemsForOrgFields? = null,
                ): ProjectsListViewItemsForOrgResult
            }
        }
    }

    interface Properties {
        val schema: Orgs.Properties.Schema

        val values: Orgs.Properties.Values

        interface Schema {
            @Serializable
            @JvmInline
            value class OrgsCustomPropertiesForReposCreateOrUpdateOrganizationDefinitionsBody(val properties: List<CustomProperty>)

            sealed interface OrgsCustomPropertiesForReposGetOrganizationDefinitionsResult {
                data class OK(val value: List<CustomProperty>) : OrgsCustomPropertiesForReposGetOrganizationDefinitionsResult

                data class Forbidden(val value: BasicError) : OrgsCustomPropertiesForReposGetOrganizationDefinitionsResult

                data class NotFound(val value: BasicError) : OrgsCustomPropertiesForReposGetOrganizationDefinitionsResult
            }

            suspend fun orgsCustomPropertiesForReposGetOrganizationDefinitions(
                org: String,
            ): OrgsCustomPropertiesForReposGetOrganizationDefinitionsResult

            sealed interface OrgsCustomPropertiesForReposCreateOrUpdateOrganizationDefinitionsResult {
                data class OK(val value: List<CustomProperty>) : OrgsCustomPropertiesForReposCreateOrUpdateOrganizationDefinitionsResult

                data class Forbidden(val value: BasicError) : OrgsCustomPropertiesForReposCreateOrUpdateOrganizationDefinitionsResult

                data class NotFound(val value: BasicError) : OrgsCustomPropertiesForReposCreateOrUpdateOrganizationDefinitionsResult
            }

            suspend fun orgsCustomPropertiesForReposCreateOrUpdateOrganizationDefinitions(
                org: String,
                body: OrgsCustomPropertiesForReposCreateOrUpdateOrganizationDefinitionsBody,
            ): OrgsCustomPropertiesForReposCreateOrUpdateOrganizationDefinitionsResult

            sealed interface OrgsCustomPropertiesForReposGetOrganizationDefinitionResult {
                data class OK(val value: CustomProperty) : OrgsCustomPropertiesForReposGetOrganizationDefinitionResult

                data class Forbidden(val value: BasicError) : OrgsCustomPropertiesForReposGetOrganizationDefinitionResult

                data class NotFound(val value: BasicError) : OrgsCustomPropertiesForReposGetOrganizationDefinitionResult
            }

            suspend fun orgsCustomPropertiesForReposGetOrganizationDefinition(
                org: String,
                customPropertyName: String,
            ): OrgsCustomPropertiesForReposGetOrganizationDefinitionResult

            sealed interface OrgsCustomPropertiesForReposCreateOrUpdateOrganizationDefinitionResult {
                data class OK(val value: CustomProperty) : OrgsCustomPropertiesForReposCreateOrUpdateOrganizationDefinitionResult

                data class Forbidden(val value: BasicError) : OrgsCustomPropertiesForReposCreateOrUpdateOrganizationDefinitionResult

                data class NotFound(val value: BasicError) : OrgsCustomPropertiesForReposCreateOrUpdateOrganizationDefinitionResult
            }

            suspend fun orgsCustomPropertiesForReposCreateOrUpdateOrganizationDefinition(
                org: String,
                customPropertyName: String,
                body: CustomPropertySetPayload,
            ): OrgsCustomPropertiesForReposCreateOrUpdateOrganizationDefinitionResult

            sealed interface OrgsCustomPropertiesForReposDeleteOrganizationDefinitionResult {
                data object NoContent : OrgsCustomPropertiesForReposDeleteOrganizationDefinitionResult

                data class Forbidden(val value: BasicError) : OrgsCustomPropertiesForReposDeleteOrganizationDefinitionResult

                data class NotFound(val value: BasicError) : OrgsCustomPropertiesForReposDeleteOrganizationDefinitionResult
            }

            suspend fun orgsCustomPropertiesForReposDeleteOrganizationDefinition(
                org: String,
                customPropertyName: String,
            ): OrgsCustomPropertiesForReposDeleteOrganizationDefinitionResult
        }

        interface Values {
            @Serializable
            data class OrgsCustomPropertiesForReposCreateOrUpdateOrganizationValuesBody(
                @SerialName("repository_names") val repositoryNames: List<String>,
                val properties: List<CustomPropertyValue>,
            )

            sealed interface OrgsCustomPropertiesForReposGetOrganizationValuesResult {
                data class OK(val value: List<OrgRepoCustomPropertyValues>) : OrgsCustomPropertiesForReposGetOrganizationValuesResult

                data class Forbidden(val value: BasicError) : OrgsCustomPropertiesForReposGetOrganizationValuesResult

                data class NotFound(val value: BasicError) : OrgsCustomPropertiesForReposGetOrganizationValuesResult
            }

            suspend fun orgsCustomPropertiesForReposGetOrganizationValues(
                org: String,
                page: Long = 1L,
                perPage: Long = 30L,
                repositoryQuery: String? = null,
            ): OrgsCustomPropertiesForReposGetOrganizationValuesResult

            sealed interface OrgsCustomPropertiesForReposCreateOrUpdateOrganizationValuesResult {
                data object NoContent : OrgsCustomPropertiesForReposCreateOrUpdateOrganizationValuesResult

                data class Forbidden(val value: BasicError) : OrgsCustomPropertiesForReposCreateOrUpdateOrganizationValuesResult

                data class NotFound(val value: BasicError) : OrgsCustomPropertiesForReposCreateOrUpdateOrganizationValuesResult

                data class UnprocessableEntity(val value: ValidationError) : OrgsCustomPropertiesForReposCreateOrUpdateOrganizationValuesResult
            }

            suspend fun orgsCustomPropertiesForReposCreateOrUpdateOrganizationValues(
                org: String,
                body: OrgsCustomPropertiesForReposCreateOrUpdateOrganizationValuesBody,
            ): OrgsCustomPropertiesForReposCreateOrUpdateOrganizationValuesResult
        }
    }

    interface PublicMembers {
        suspend fun orgsListPublicMembers(
            org: String,
            page: Long = 1L,
            perPage: Long = 30L,
        ): List<SimpleUser>

        sealed interface OrgsCheckPublicMembershipForUserResult {
            data object NoContent : OrgsCheckPublicMembershipForUserResult

            data object NotFound : OrgsCheckPublicMembershipForUserResult
        }

        suspend fun orgsCheckPublicMembershipForUser(
            org: String,
            username: String,
        ): OrgsCheckPublicMembershipForUserResult

        sealed interface OrgsSetPublicMembershipForAuthenticatedUserResult {
            data object NoContent : OrgsSetPublicMembershipForAuthenticatedUserResult

            data class Forbidden(val value: BasicError) : OrgsSetPublicMembershipForAuthenticatedUserResult
        }

        suspend fun orgsSetPublicMembershipForAuthenticatedUser(
            org: String,
            username: String,
        ): OrgsSetPublicMembershipForAuthenticatedUserResult

        suspend fun orgsRemovePublicMembershipForAuthenticatedUser(
            org: String,
            username: String,
        ): Unit
    }

    interface Repos {
        @Serializable
        enum class Direction {
            @SerialName("asc") Asc, @SerialName("desc") Desc;
        }


        @Serializable
        data class ReposCreateInOrgBody(
            val name: String,
            val description: String? = null,
            val homepage: String? = null,
            val private: Boolean? = null,
            val visibility: Visibility? = null,
            @SerialName("has_issues") val hasIssues: Boolean? = null,
            @SerialName("has_projects") val hasProjects: Boolean? = null,
            @SerialName("has_wiki") val hasWiki: Boolean? = null,
            @SerialName("has_downloads") val hasDownloads: Boolean? = null,
            @SerialName("is_template") val isTemplate: Boolean? = null,
            @SerialName("team_id") val teamId: Long? = null,
            @SerialName("auto_init") val autoInit: Boolean? = null,
            @SerialName("gitignore_template") val gitignoreTemplate: String? = null,
            @SerialName("license_template") val licenseTemplate: String? = null,
            @SerialName("allow_squash_merge") val allowSquashMerge: Boolean? = null,
            @SerialName("allow_merge_commit") val allowMergeCommit: Boolean? = null,
            @SerialName("allow_rebase_merge") val allowRebaseMerge: Boolean? = null,
            @SerialName("allow_auto_merge") val allowAutoMerge: Boolean? = null,
            @SerialName("delete_branch_on_merge") val deleteBranchOnMerge: Boolean? = null,
            @SerialName("use_squash_pr_title_as_default") val useSquashPrTitleAsDefault: Boolean? = null,
            @SerialName("squash_merge_commit_title") val squashMergeCommitTitle: SquashMergeCommitTitle? = null,
            @SerialName("squash_merge_commit_message") val squashMergeCommitMessage: SquashMergeCommitMessage? = null,
            @SerialName("merge_commit_title") val mergeCommitTitle: MergeCommitTitle? = null,
            @SerialName("merge_commit_message") val mergeCommitMessage: MergeCommitMessage? = null,
            @SerialName("custom_properties") val customProperties: JsonElement? = null,
        ) {
            @Serializable
            enum class Visibility {
                @SerialName("public") Public, @SerialName("private") Private;
            }

            @Serializable
            enum class SquashMergeCommitTitle {
                @SerialName("PR_TITLE") PRTITLE, @SerialName("COMMIT_OR_PR_TITLE") COMMITORPRTITLE;
            }

            @Serializable
            enum class SquashMergeCommitMessage {
                @SerialName("PR_BODY") PRBODY, @SerialName("COMMIT_MESSAGES") COMMITMESSAGES, BLANK;
            }

            @Serializable
            enum class MergeCommitTitle {
                @SerialName("PR_TITLE") PRTITLE, @SerialName("MERGE_MESSAGE") MERGEMESSAGE;
            }

            @Serializable
            enum class MergeCommitMessage {
                @SerialName("PR_BODY") PRBODY, @SerialName("PR_TITLE") PRTITLE, BLANK;
            }
        }


        @Serializable
        enum class Sort {
            @SerialName("created")
            Created,
            @SerialName("updated")
            Updated,
            @SerialName("pushed")
            Pushed,
            @SerialName("full_name")
            FullName;
        }


        @Serializable
        enum class Type {
            @SerialName("all")
            All,
            @SerialName("public")
            Public,
            @SerialName("private")
            Private,
            @SerialName("forks")
            Forks,
            @SerialName("sources")
            Sources,
            @SerialName("member")
            Member;
        }

        suspend fun reposListForOrg(
            org: String,
            page: Long = 1L,
            perPage: Long = 30L,
            sort: Sort = Sort.Created,
            type: Type = Type.All,
            direction: Direction? = null,
        ): List<MinimalRepository>

        sealed interface ReposCreateInOrgResult {
            data class Created(val value: FullRepository) : ReposCreateInOrgResult

            data class Forbidden(val value: BasicError) : ReposCreateInOrgResult

            data class UnprocessableEntity(val value: ValidationError) : ReposCreateInOrgResult
        }

        suspend fun reposCreateInOrg(
            org: String,
            body: ReposCreateInOrgBody,
        ): ReposCreateInOrgResult
    }

    interface Rulesets {
        val ruleSuites: Orgs.Rulesets.RuleSuites

        val history: Orgs.Rulesets.History

        @Serializable
        data class ReposCreateOrgRulesetBody(
            val name: String,
            val target: Target? = null,
            val enforcement: RepositoryRuleEnforcement,
            @SerialName("bypass_actors") val bypassActors: List<RepositoryRulesetBypassActor>? = null,
            val conditions: OrgRulesetConditions? = null,
            val rules: List<OrgRules>? = null,
        ) {
            @Serializable
            enum class Target {
                @SerialName("branch")
                Branch,
                @SerialName("tag")
                Tag,
                @SerialName("push")
                Push,
                @SerialName("repository")
                Repository;
            }
        }


        @Serializable
        data class ReposUpdateOrgRulesetBody(
            val name: String? = null,
            val target: Target? = null,
            val enforcement: RepositoryRuleEnforcement? = null,
            @SerialName("bypass_actors") val bypassActors: List<RepositoryRulesetBypassActor>? = null,
            val conditions: OrgRulesetConditions? = null,
            val rules: List<OrgRules>? = null,
        ) {
            @Serializable
            enum class Target {
                @SerialName("branch")
                Branch,
                @SerialName("tag")
                Tag,
                @SerialName("push")
                Push,
                @SerialName("repository")
                Repository;
            }
        }

        sealed interface ReposGetOrgRulesetsResult {
            data class OK(val value: List<RepositoryRuleset>) : ReposGetOrgRulesetsResult

            data class NotFound(val value: BasicError) : ReposGetOrgRulesetsResult

            data class InternalServerError(val value: BasicError) : ReposGetOrgRulesetsResult
        }

        suspend fun reposGetOrgRulesets(
            org: String,
            page: Long = 1L,
            perPage: Long = 30L,
            targets: String? = null,
        ): ReposGetOrgRulesetsResult

        sealed interface ReposCreateOrgRulesetResult {
            data class Created(val value: RepositoryRuleset) : ReposCreateOrgRulesetResult

            data class NotFound(val value: BasicError) : ReposCreateOrgRulesetResult

            data class UnprocessableEntity(val value: ValidationError) : ReposCreateOrgRulesetResult

            data class InternalServerError(val value: BasicError) : ReposCreateOrgRulesetResult
        }

        suspend fun reposCreateOrgRuleset(
            org: String,
            body: ReposCreateOrgRulesetBody,
        ): ReposCreateOrgRulesetResult

        sealed interface ReposGetOrgRulesetResult {
            data class OK(val value: RepositoryRuleset) : ReposGetOrgRulesetResult

            data class NotFound(val value: BasicError) : ReposGetOrgRulesetResult

            data class InternalServerError(val value: BasicError) : ReposGetOrgRulesetResult
        }

        suspend fun reposGetOrgRuleset(
            org: String,
            rulesetId: Long,
        ): ReposGetOrgRulesetResult

        sealed interface ReposUpdateOrgRulesetResult {
            data class OK(val value: RepositoryRuleset) : ReposUpdateOrgRulesetResult

            data class NotFound(val value: BasicError) : ReposUpdateOrgRulesetResult

            data class UnprocessableEntity(val value: ValidationError) : ReposUpdateOrgRulesetResult

            data class InternalServerError(val value: BasicError) : ReposUpdateOrgRulesetResult
        }

        suspend fun reposUpdateOrgRuleset(
            org: String,
            rulesetId: Long,
            body: ReposUpdateOrgRulesetBody? = null,
        ): ReposUpdateOrgRulesetResult

        sealed interface ReposDeleteOrgRulesetResult {
            data object NoContent : ReposDeleteOrgRulesetResult

            data class NotFound(val value: BasicError) : ReposDeleteOrgRulesetResult

            data class InternalServerError(val value: BasicError) : ReposDeleteOrgRulesetResult
        }

        suspend fun reposDeleteOrgRuleset(
            org: String,
            rulesetId: Long,
        ): ReposDeleteOrgRulesetResult

        interface RuleSuites {
            @Serializable
            enum class RuleSuiteResult {
                @SerialName("pass") Pass, @SerialName("fail") Fail, @SerialName("bypass") Bypass, @SerialName("all") All;
            }


            @Serializable
            enum class TimePeriod {
                @SerialName("hour") Hour, @SerialName("day") Day, @SerialName("week") Week, @SerialName("month") Month;
            }

            sealed interface ReposGetOrgRuleSuitesResult {
                data class OK(val value: RuleSuites) : ReposGetOrgRuleSuitesResult

                data class NotFound(val value: BasicError) : ReposGetOrgRuleSuitesResult

                data class InternalServerError(val value: BasicError) : ReposGetOrgRuleSuitesResult
            }

            suspend fun reposGetOrgRuleSuites(
                org: String,
                page: Long = 1L,
                perPage: Long = 30L,
                ruleSuiteResult: RuleSuiteResult = RuleSuiteResult.All,
                timePeriod: TimePeriod = TimePeriod.Day,
                actorName: String? = null,
                ref: String? = null,
                repositoryName: String? = null,
            ): ReposGetOrgRuleSuitesResult

            sealed interface ReposGetOrgRuleSuiteResult {
                data class OK(val value: RuleSuite) : ReposGetOrgRuleSuiteResult

                data class NotFound(val value: BasicError) : ReposGetOrgRuleSuiteResult

                data class InternalServerError(val value: BasicError) : ReposGetOrgRuleSuiteResult
            }

            suspend fun reposGetOrgRuleSuite(
                org: String,
                ruleSuiteId: Long,
            ): ReposGetOrgRuleSuiteResult
        }

        interface History {
            sealed interface OrgsGetOrgRulesetHistoryResult {
                data class OK(val value: List<RulesetVersion>) : OrgsGetOrgRulesetHistoryResult

                data class NotFound(val value: BasicError) : OrgsGetOrgRulesetHistoryResult

                data class InternalServerError(val value: BasicError) : OrgsGetOrgRulesetHistoryResult
            }

            suspend fun orgsGetOrgRulesetHistory(
                org: String,
                rulesetId: Long,
                page: Long = 1L,
                perPage: Long = 30L,
            ): OrgsGetOrgRulesetHistoryResult

            sealed interface OrgsGetOrgRulesetVersionResult {
                data class OK(val value: RulesetVersionWithState) : OrgsGetOrgRulesetVersionResult

                data class NotFound(val value: BasicError) : OrgsGetOrgRulesetVersionResult

                data class InternalServerError(val value: BasicError) : OrgsGetOrgRulesetVersionResult
            }

            suspend fun orgsGetOrgRulesetVersion(
                org: String,
                rulesetId: Long,
                versionId: Long,
            ): OrgsGetOrgRulesetVersionResult
        }
    }

    interface SecretScanning {
        val alerts: Orgs.SecretScanning.Alerts

        val patternConfigurations: Orgs.SecretScanning.PatternConfigurations

        interface Alerts {
            @Serializable
            enum class Direction {
                @SerialName("asc") Asc, @SerialName("desc") Desc;
            }


            @Serializable
            data class SecretScanningListAlertsForOrgResponse(
                val code: String? = null,
                val message: String? = null,
                @SerialName("documentation_url") val documentationUrl: String? = null,
            )


            @Serializable
            enum class Sort {
                @SerialName("created") Created, @SerialName("updated") Updated;
            }


            @Serializable
            enum class State {
                @SerialName("open") Open, @SerialName("resolved") Resolved;
            }

            sealed interface SecretScanningListAlertsForOrgResult {
                data class OK(val value: List<OrganizationSecretScanningAlertResponse>) : SecretScanningListAlertsForOrgResult

                data class NotFound(val value: BasicError) : SecretScanningListAlertsForOrgResult

                data class ServiceUnavailable(val value: SecretScanningListAlertsForOrgResponse) : SecretScanningListAlertsForOrgResult
            }

            suspend fun secretScanningListAlertsForOrg(
                org: String,
                direction: Direction = Direction.Desc,
                hideSecret: Boolean = false,
                isMultiRepo: Boolean = false,
                isPubliclyLeaked: Boolean = false,
                page: Long = 1L,
                perPage: Long = 30L,
                sort: Sort = Sort.Created,
                after: String? = null,
                assignee: String? = null,
                before: String? = null,
                resolution: String? = null,
                secretType: String? = null,
                state: State? = null,
                validity: String? = null,
            ): SecretScanningListAlertsForOrgResult
        }

        interface PatternConfigurations {
            @Serializable
            data class SecretScanningUpdateOrgPatternConfigsBody(
                @SerialName("pattern_config_version") val patternConfigVersion: SecretScanningRowVersion? = null,
                @SerialName("provider_pattern_settings") val providerPatternSettings: List<ProviderPatternSettings>? = null,
                @SerialName("custom_pattern_settings") val customPatternSettings: List<CustomPatternSettings>? = null,
            ) {
                @Serializable
                data class ProviderPatternSettings(
                    @SerialName("token_type") val tokenType: String? = null,
                    @SerialName("push_protection_setting") val pushProtectionSetting: PushProtectionSetting? = null,
                ) {
                    @Serializable
                    enum class PushProtectionSetting {
                        @SerialName("not-set") NotSet, @SerialName("disabled") Disabled, @SerialName("enabled") Enabled;
                    }
                }

                @Serializable
                data class CustomPatternSettings(
                    @SerialName("token_type") val tokenType: String? = null,
                    @SerialName("custom_pattern_version") val customPatternVersion: SecretScanningRowVersion? = null,
                    @SerialName("push_protection_setting") val pushProtectionSetting: PushProtectionSetting? = null,
                ) {
                    @Serializable
                    enum class PushProtectionSetting {
                        @SerialName("disabled") Disabled, @SerialName("enabled") Enabled;
                    }
                }
            }


            @Serializable
            @JvmInline
            value class SecretScanningUpdateOrgPatternConfigsResponse(@SerialName("pattern_config_version") val patternConfigVersion: String? = null)

            sealed interface SecretScanningListOrgPatternConfigsResult {
                data class OK(val value: SecretScanningPatternConfiguration) : SecretScanningListOrgPatternConfigsResult

                data class Forbidden(val value: BasicError) : SecretScanningListOrgPatternConfigsResult

                data class NotFound(val value: BasicError) : SecretScanningListOrgPatternConfigsResult
            }

            suspend fun secretScanningListOrgPatternConfigs(
                org: String,
            ): SecretScanningListOrgPatternConfigsResult

            sealed interface SecretScanningUpdateOrgPatternConfigsResult {
                data class OK(val value: SecretScanningUpdateOrgPatternConfigsResponse) : SecretScanningUpdateOrgPatternConfigsResult

                data class BadRequest(val value: BasicError) : SecretScanningUpdateOrgPatternConfigsResult

                data class Forbidden(val value: BasicError) : SecretScanningUpdateOrgPatternConfigsResult

                data class NotFound(val value: BasicError) : SecretScanningUpdateOrgPatternConfigsResult

                data class Conflict(val value: BasicError) : SecretScanningUpdateOrgPatternConfigsResult

                data class UnprocessableEntity(val value: ValidationError) : SecretScanningUpdateOrgPatternConfigsResult
            }

            suspend fun secretScanningUpdateOrgPatternConfigs(
                org: String,
                body: SecretScanningUpdateOrgPatternConfigsBody,
            ): SecretScanningUpdateOrgPatternConfigsResult
        }
    }

    interface SecurityAdvisories {
        @Serializable
        enum class Direction {
            @SerialName("asc") Asc, @SerialName("desc") Desc;
        }


        @Serializable
        enum class Sort {
            @SerialName("created") Created, @SerialName("updated") Updated, @SerialName("published") Published;
        }


        @Serializable
        enum class State {
            @SerialName("triage")
            Triage,
            @SerialName("draft")
            Draft,
            @SerialName("published")
            Published,
            @SerialName("closed")
            Closed;
        }

        sealed interface SecurityAdvisoriesListOrgRepositoryAdvisoriesResult {
            data class OK(val value: List<RepositoryAdvisoryResponse>) : SecurityAdvisoriesListOrgRepositoryAdvisoriesResult

            data class BadRequest(val value: BasicError) : SecurityAdvisoriesListOrgRepositoryAdvisoriesResult

            data class NotFound(val value: BasicError) : SecurityAdvisoriesListOrgRepositoryAdvisoriesResult
        }

        suspend fun securityAdvisoriesListOrgRepositoryAdvisories(
            org: String,
            direction: Direction = Direction.Desc,
            perPage: Long = 30L,
            sort: Sort = Sort.Created,
            after: String? = null,
            before: String? = null,
            state: State? = null,
        ): SecurityAdvisoriesListOrgRepositoryAdvisoriesResult
    }

    interface SecurityManagers {
        val teams: Orgs.SecurityManagers.TeamsApi

        @Deprecated("Deprecated by the API provider")
        suspend fun orgsListSecurityManagerTeams(
            org: String,
        ): List<TeamSimple>

        interface TeamsApi {
            @Deprecated("Deprecated by the API provider")
            suspend fun orgsAddSecurityManagerTeam(
                org: String,
                teamSlug: String,
            ): Unit

            @Deprecated("Deprecated by the API provider")
            suspend fun orgsRemoveSecurityManagerTeam(
                org: String,
                teamSlug: String,
            ): Unit
        }
    }

    interface Settings {
        val immutableReleases: Orgs.Settings.ImmutableReleases

        val networkConfigurations: Orgs.Settings.NetworkConfigurations

        val networkSettings: Orgs.Settings.NetworkSettings

        interface ImmutableReleases {
            val repositories: Orgs.Settings.ImmutableReleases.Repositories

            @Serializable
            data class OrgsSetImmutableReleasesSettingsBody(
                @SerialName("enforced_repositories") val enforcedRepositories: EnforcedRepositories,
                @SerialName("selected_repository_ids") val selectedRepositoryIds: List<Long>? = null,
            ) {
                @Serializable
                enum class EnforcedRepositories {
                    @SerialName("all") All, @SerialName("none") None, @SerialName("selected") Selected;
                }
            }

            suspend fun orgsGetImmutableReleasesSettings(
                org: String,
            ): ImmutableReleasesOrganizationSettings

            suspend fun orgsSetImmutableReleasesSettings(
                org: String,
                body: OrgsSetImmutableReleasesSettingsBody,
            ): Unit

            interface Repositories {
                @Serializable
                data class OrgsGetImmutableReleasesSettingsRepositoriesResponse(
                    @SerialName("total_count") val totalCount: Long,
                    val repositories: List<MinimalRepository>,
                )


                @Serializable
                @JvmInline
                value class OrgsSetImmutableReleasesSettingsRepositoriesBody(@SerialName("selected_repository_ids") val selectedRepositoryIds: List<Long>)

                suspend fun orgsGetImmutableReleasesSettingsRepositories(
                    org: String,
                    page: Long = 1L,
                    perPage: Long = 30L,
                ): OrgsGetImmutableReleasesSettingsRepositoriesResponse

                suspend fun orgsSetImmutableReleasesSettingsRepositories(
                    org: String,
                    body: OrgsSetImmutableReleasesSettingsRepositoriesBody,
                ): Unit

                suspend fun orgsEnableSelectedRepositoryImmutableReleasesOrganization(
                    org: String,
                    repositoryId: Long,
                ): Unit

                suspend fun orgsDisableSelectedRepositoryImmutableReleasesOrganization(
                    org: String,
                    repositoryId: Long,
                ): Unit
            }
        }

        interface NetworkConfigurations {
            @Serializable
            data class HostedComputeCreateNetworkConfigurationForOrgBody(
                val name: String,
                @SerialName("compute_service") val computeService: ComputeService? = null,
                @SerialName("network_settings_ids") val networkSettingsIds: List<String>,
            ) {
                @Serializable
                enum class ComputeService {
                    @SerialName("none") None, @SerialName("actions") Actions;
                }
            }


            @Serializable
            data class HostedComputeListNetworkConfigurationsForOrgResponse(
                @SerialName("total_count") val totalCount: Long,
                @SerialName("network_configurations") val networkConfigurations: List<NetworkConfiguration>,
            )


            @Serializable
            data class HostedComputeUpdateNetworkConfigurationForOrgBody(
                val name: String? = null,
                @SerialName("compute_service") val computeService: ComputeService? = null,
                @SerialName("network_settings_ids") val networkSettingsIds: List<String>? = null,
            ) {
                @Serializable
                enum class ComputeService {
                    @SerialName("none") None, @SerialName("actions") Actions;
                }
            }

            suspend fun hostedComputeListNetworkConfigurationsForOrg(
                org: String,
                page: Long = 1L,
                perPage: Long = 30L,
            ): HostedComputeListNetworkConfigurationsForOrgResponse

            suspend fun hostedComputeCreateNetworkConfigurationForOrg(
                org: String,
                body: HostedComputeCreateNetworkConfigurationForOrgBody,
            ): NetworkConfiguration

            suspend fun hostedComputeGetNetworkConfigurationForOrg(
                org: String,
                networkConfigurationId: String,
            ): NetworkConfiguration

            suspend fun hostedComputeDeleteNetworkConfigurationFromOrg(
                org: String,
                networkConfigurationId: String,
            ): Unit

            suspend fun hostedComputeUpdateNetworkConfigurationForOrg(
                org: String,
                networkConfigurationId: String,
                body: HostedComputeUpdateNetworkConfigurationForOrgBody,
            ): NetworkConfiguration
        }

        interface NetworkSettings {
            suspend fun hostedComputeGetNetworkSettingsForOrg(
                org: String,
                networkSettingsId: String,
            ): NetworkSettings
        }
    }

    interface Team {
        val copilot: Orgs.Team.CopilotApi

        interface CopilotApi {
            val metrics: Orgs.Team.CopilotApi.Metrics

            interface Metrics {
                sealed interface CopilotCopilotMetricsForTeamResult {
                    data class OK(val value: List<CopilotUsageMetricsDay>) : CopilotCopilotMetricsForTeamResult

                    data class Forbidden(val value: BasicError) : CopilotCopilotMetricsForTeamResult

                    data class NotFound(val value: BasicError) : CopilotCopilotMetricsForTeamResult

                    data class UnprocessableEntity(val value: BasicError) : CopilotCopilotMetricsForTeamResult

                    data class InternalServerError(val value: BasicError) : CopilotCopilotMetricsForTeamResult
                }

                suspend fun copilotCopilotMetricsForTeam(
                    org: String,
                    teamSlug: String,
                    page: Long = 1L,
                    perPage: Long = 100L,
                    since: String? = null,
                    until: String? = null,
                ): CopilotCopilotMetricsForTeamResult
            }
        }
    }

    interface Teams {
        val invitations: Orgs.Teams.InvitationsApi

        val members: Orgs.Teams.MembersApi

        val memberships: Orgs.Teams.MembershipsApi

        val repos: Orgs.Teams.ReposApi

        val teams: Orgs.Teams.TeamsApi

        @Serializable
        enum class TeamType {
            @SerialName("all") All, @SerialName("enterprise") Enterprise, @SerialName("organization") Organization;
        }


        @Serializable
        data class TeamsCreateBody(
            val name: String,
            val description: String? = null,
            val maintainers: List<String>? = null,
            @SerialName("repo_names") val repoNames: List<String>? = null,
            val privacy: Privacy? = null,
            @SerialName("notification_setting") val notificationSetting: NotificationSetting? = null,
            val permission: Permission? = null,
            @SerialName("parent_team_id") val parentTeamId: Long? = null,
        ) {
            @Serializable
            enum class Privacy {
                @SerialName("secret") Secret, @SerialName("closed") Closed;
            }

            @Serializable
            enum class NotificationSetting {
                @SerialName("notifications_enabled")
                NotificationsEnabled,
                @SerialName("notifications_disabled")
                NotificationsDisabled;
            }

            @Serializable
            enum class Permission {
                @SerialName("pull") Pull, @SerialName("push") Push;
            }
        }


        @Serializable
        data class TeamsUpdateInOrgBody(
            val name: String? = null,
            val description: String? = null,
            val privacy: Privacy? = null,
            @SerialName("notification_setting") val notificationSetting: NotificationSetting? = null,
            val permission: Permission? = null,
            @SerialName("parent_team_id") val parentTeamId: Long? = null,
        ) {
            @Serializable
            enum class Privacy {
                @SerialName("secret") Secret, @SerialName("closed") Closed;
            }

            @Serializable
            enum class NotificationSetting {
                @SerialName("notifications_enabled")
                NotificationsEnabled,
                @SerialName("notifications_disabled")
                NotificationsDisabled;
            }

            @Serializable
            enum class Permission {
                @SerialName("pull") Pull, @SerialName("push") Push, @SerialName("admin") Admin;
            }
        }

        sealed interface TeamsListResult {
            data class OK(val value: List<Team>) : TeamsListResult

            data class Forbidden(val value: BasicError) : TeamsListResult
        }

        suspend fun teamsList(
            org: String,
            page: Long = 1L,
            perPage: Long = 30L,
            teamType: TeamType = TeamType.All,
        ): TeamsListResult

        sealed interface TeamsCreateResult {
            data class Created(val value: TeamFull) : TeamsCreateResult

            data class Forbidden(val value: BasicError) : TeamsCreateResult

            data class UnprocessableEntity(val value: ValidationError) : TeamsCreateResult
        }

        suspend fun teamsCreate(
            org: String,
            body: TeamsCreateBody,
        ): TeamsCreateResult

        sealed interface TeamsGetByNameResult {
            data class OK(val value: TeamFull) : TeamsGetByNameResult

            data class NotFound(val value: BasicError) : TeamsGetByNameResult
        }

        suspend fun teamsGetByName(
            org: String,
            teamSlug: String,
        ): TeamsGetByNameResult

        sealed interface TeamsDeleteInOrgResult {
            data object NoContent : TeamsDeleteInOrgResult

            data object UnprocessableEntity : TeamsDeleteInOrgResult
        }

        suspend fun teamsDeleteInOrg(
            org: String,
            teamSlug: String,
        ): TeamsDeleteInOrgResult

        sealed interface TeamsUpdateInOrgResult {
            data class OK(val value: TeamFull) : TeamsUpdateInOrgResult

            data class Created(val value: TeamFull) : TeamsUpdateInOrgResult

            data class Forbidden(val value: BasicError) : TeamsUpdateInOrgResult

            data class NotFound(val value: BasicError) : TeamsUpdateInOrgResult

            data class UnprocessableEntity(val value: ValidationError) : TeamsUpdateInOrgResult
        }

        suspend fun teamsUpdateInOrg(
            org: String,
            teamSlug: String,
            body: TeamsUpdateInOrgBody? = null,
        ): TeamsUpdateInOrgResult

        interface InvitationsApi {
            sealed interface TeamsListPendingInvitationsInOrgResult {
                data class OK(val value: List<OrganizationInvitation>) : TeamsListPendingInvitationsInOrgResult

                data object UnprocessableEntity : TeamsListPendingInvitationsInOrgResult
            }

            suspend fun teamsListPendingInvitationsInOrg(
                org: String,
                teamSlug: String,
                page: Long = 1L,
                perPage: Long = 30L,
            ): TeamsListPendingInvitationsInOrgResult
        }

        interface MembersApi {
            @Serializable
            enum class Role {
                @SerialName("member") Member, @SerialName("maintainer") Maintainer, @SerialName("all") All;
            }

            suspend fun teamsListMembersInOrg(
                org: String,
                teamSlug: String,
                page: Long = 1L,
                perPage: Long = 30L,
                role: Role = Role.All,
            ): List<SimpleUser>
        }

        interface MembershipsApi {
            @Serializable
            @JvmInline
            value class TeamsAddOrUpdateMembershipForUserInOrgBody(val role: Role? = null) {
                @Serializable
                enum class Role {
                    @SerialName("member") Member, @SerialName("maintainer") Maintainer;
                }
            }

            sealed interface TeamsGetMembershipForUserInOrgResult {
                data class OK(val value: TeamMembership) : TeamsGetMembershipForUserInOrgResult

                data object NotFound : TeamsGetMembershipForUserInOrgResult
            }

            suspend fun teamsGetMembershipForUserInOrg(
                org: String,
                teamSlug: String,
                username: String,
            ): TeamsGetMembershipForUserInOrgResult

            sealed interface TeamsAddOrUpdateMembershipForUserInOrgResult {
                data class OK(val value: TeamMembership) : TeamsAddOrUpdateMembershipForUserInOrgResult

                data object Forbidden : TeamsAddOrUpdateMembershipForUserInOrgResult

                data object UnprocessableEntity : TeamsAddOrUpdateMembershipForUserInOrgResult
            }

            suspend fun teamsAddOrUpdateMembershipForUserInOrg(
                org: String,
                teamSlug: String,
                username: String,
                body: TeamsAddOrUpdateMembershipForUserInOrgBody? = null,
            ): TeamsAddOrUpdateMembershipForUserInOrgResult

            sealed interface TeamsRemoveMembershipForUserInOrgResult {
                data object NoContent : TeamsRemoveMembershipForUserInOrgResult

                data object Forbidden : TeamsRemoveMembershipForUserInOrgResult
            }

            suspend fun teamsRemoveMembershipForUserInOrg(
                org: String,
                teamSlug: String,
                username: String,
            ): TeamsRemoveMembershipForUserInOrgResult
        }

        interface ReposApi {
            @Serializable
            @JvmInline
            value class TeamsAddOrUpdateRepoPermissionsInOrgBody(val permission: String? = null)

            suspend fun teamsListReposInOrg(
                org: String,
                teamSlug: String,
                page: Long = 1L,
                perPage: Long = 30L,
            ): List<MinimalRepository>

            sealed interface TeamsCheckPermissionsForRepoInOrgResult {
                data class OK(val value: TeamRepository) : TeamsCheckPermissionsForRepoInOrgResult

                data object NoContent : TeamsCheckPermissionsForRepoInOrgResult

                data object NotFound : TeamsCheckPermissionsForRepoInOrgResult
            }

            suspend fun teamsCheckPermissionsForRepoInOrg(
                org: String,
                teamSlug: String,
                owner: String,
                repo: String,
            ): TeamsCheckPermissionsForRepoInOrgResult

            suspend fun teamsAddOrUpdateRepoPermissionsInOrg(
                org: String,
                teamSlug: String,
                owner: String,
                repo: String,
                body: TeamsAddOrUpdateRepoPermissionsInOrgBody? = null,
            ): Unit

            suspend fun teamsRemoveRepoInOrg(
                org: String,
                teamSlug: String,
                owner: String,
                repo: String,
            ): Unit
        }

        interface TeamsApi {
            suspend fun teamsListChildInOrg(
                org: String,
                teamSlug: String,
                page: Long = 1L,
                perPage: Long = 30L,
            ): List<Team>
        }
    }
}

internal class KtorOrgs(private val client: HttpClient) : Orgs {
    override val actions: Orgs.Actions = KtorOrgsActions(client)

    override val artifacts: Orgs.Artifacts = KtorOrgsArtifacts(client)

    override val attestations: Orgs.Attestations = KtorOrgsAttestations(client)

    override val blocks: Orgs.Blocks = KtorOrgsBlocks(client)

    override val campaigns: Orgs.Campaigns = KtorOrgsCampaigns(client)

    override val codeScanning: Orgs.CodeScanning = KtorOrgsCodeScanning(client)

    override val codeSecurity: Orgs.CodeSecurity = KtorOrgsCodeSecurity(client)

    override val codespaces: Orgs.Codespaces = KtorOrgsCodespaces(client)

    override val copilot: Orgs.Copilot = KtorOrgsCopilot(client)

    override val dependabot: Orgs.Dependabot = KtorOrgsDependabot(client)

    override val docker: Orgs.Docker = KtorOrgsDocker(client)

    override val events: Orgs.Events = KtorOrgsEvents(client)

    override val failedInvitations: Orgs.FailedInvitations = KtorOrgsFailedInvitations(client)

    override val hooks: Orgs.Hooks = KtorOrgsHooks(client)

    override val insights: Orgs.Insights = KtorOrgsInsights(client)

    override val installation: Orgs.Installation = KtorOrgsInstallation(client)

    override val installations: Orgs.Installations = KtorOrgsInstallations(client)

    override val interactionLimits: Orgs.InteractionLimits = KtorOrgsInteractionLimits(client)

    override val invitations: Orgs.Invitations = KtorOrgsInvitations(client)

    override val issueFields: Orgs.IssueFields = KtorOrgsIssueFields(client)

    override val issueTypes: Orgs.IssueTypes = KtorOrgsIssueTypes(client)

    override val issues: Orgs.Issues = KtorOrgsIssues(client)

    override val members: Orgs.Members = KtorOrgsMembers(client)

    override val memberships: Orgs.Memberships = KtorOrgsMemberships(client)

    override val migrations: Orgs.Migrations = KtorOrgsMigrations(client)

    override val organizationRoles: Orgs.OrganizationRoles = KtorOrgsOrganizationRoles(client)

    override val outsideCollaborators: Orgs.OutsideCollaborators = KtorOrgsOutsideCollaborators(client)

    override val packages: Orgs.Packages = KtorOrgsPackages(client)

    override val personalAccessTokenRequests: Orgs.PersonalAccessTokenRequests = KtorOrgsPersonalAccessTokenRequests(client)

    override val personalAccessTokens: Orgs.PersonalAccessTokens = KtorOrgsPersonalAccessTokens(client)

    override val privateRegistries: Orgs.PrivateRegistries = KtorOrgsPrivateRegistries(client)

    override val projectsV2: Orgs.ProjectsV2 = KtorOrgsProjectsV2(client)

    override val properties: Orgs.Properties = KtorOrgsProperties(client)

    override val publicMembers: Orgs.PublicMembers = KtorOrgsPublicMembers(client)

    override val repos: Orgs.Repos = KtorOrgsRepos(client)

    override val rulesets: Orgs.Rulesets = KtorOrgsRulesets(client)

    override val secretScanning: Orgs.SecretScanning = KtorOrgsSecretScanning(client)

    override val securityAdvisories: Orgs.SecurityAdvisories = KtorOrgsSecurityAdvisories(client)

    override val securityManagers: Orgs.SecurityManagers = KtorOrgsSecurityManagers(client)

    override val settings: Orgs.Settings = KtorOrgsSettings(client)

    override val team: Orgs.Team = KtorOrgsTeam(client)

    override val teams: Orgs.Teams = KtorOrgsTeams(client)

    override suspend fun orgsGet(org: String): Orgs.OrgsGetResult {
        val response = client.get("/orgs/$org")
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.OrgsGetResult.OK(response.body())
            HttpStatusCode.NotFound -> Orgs.OrgsGetResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun orgsDelete(org: String): Orgs.OrgsDeleteResult {
        val response = client.delete("/orgs/$org")
        return when (response.status) {
            HttpStatusCode.Accepted -> Orgs.OrgsDeleteResult.Accepted(response.body())
            HttpStatusCode.Forbidden -> Orgs.OrgsDeleteResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.OrgsDeleteResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun orgsUpdate(org: String, body: Orgs.OrgsUpdateBody?): Orgs.OrgsUpdateResult {
        val response = client.patch("/orgs/$org") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.OrgsUpdateResult.OK(response.body())
            HttpStatusCode.Conflict -> Orgs.OrgsUpdateResult.Conflict(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.OrgsUpdateResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    @Deprecated("Deprecated by the API provider")
    override suspend fun orgsEnableOrDisableSecurityProductOnAllOrgRepos(org: String, securityProduct: Orgs.SecurityProduct, enablement: Orgs.Enablement, body: Orgs.OrgsEnableOrDisableSecurityProductOnAllOrgReposBody?): Orgs.OrgsEnableOrDisableSecurityProductOnAllOrgReposResult {
        val response = client.post("/orgs/$org/$securityProduct/$enablement") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }
        return when (response.status) {
            HttpStatusCode.NoContent -> Orgs.OrgsEnableOrDisableSecurityProductOnAllOrgReposResult.NoContent
            HttpStatusCode.UnprocessableEntity -> Orgs.OrgsEnableOrDisableSecurityProductOnAllOrgReposResult.UnprocessableEntity
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsActions(private val client: HttpClient) : Orgs.Actions {
    override val cache: Orgs.Actions.Cache = KtorOrgsActionsCache(client)

    override val hostedRunners: Orgs.Actions.HostedRunners = KtorOrgsActionsHostedRunners(client)

    override val oidc: Orgs.Actions.Oidc = KtorOrgsActionsOidc(client)

    override val permissions: Orgs.Actions.Permissions = KtorOrgsActionsPermissions(client)

    override val runnerGroups: Orgs.Actions.RunnerGroups = KtorOrgsActionsRunnerGroups(client)

    override val runners: Orgs.Actions.Runners = KtorOrgsActionsRunners(client)

    override val secrets: Orgs.Actions.Secrets = KtorOrgsActionsSecrets(client)

    override val variables: Orgs.Actions.Variables = KtorOrgsActionsVariables(client)
}

internal class KtorOrgsActionsCache(private val client: HttpClient) : Orgs.Actions.Cache {
    override val usage: Orgs.Actions.Cache.Usage = KtorOrgsActionsCacheUsage(client)

    override val usageByRepository: Orgs.Actions.Cache.UsageByRepository = KtorOrgsActionsCacheUsageByRepository(client)
}

internal class KtorOrgsActionsCacheUsage(private val client: HttpClient) : Orgs.Actions.Cache.Usage {
    override suspend fun actionsGetActionsCacheUsageForOrg(org: String): ActionsCacheUsageOrgEnterprise =
        client.get("/orgs/$org/actions/cache/usage").body()
}

internal class KtorOrgsActionsCacheUsageByRepository(private val client: HttpClient) : Orgs.Actions.Cache.UsageByRepository {
    override suspend fun actionsGetActionsCacheUsageByRepoForOrg(org: String, page: Long, perPage: Long): Orgs.Actions.Cache.UsageByRepository.ActionsGetActionsCacheUsageByRepoForOrgResponse =
        client.get("/orgs/$org/actions/cache/usage-by-repository") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()
}

internal class KtorOrgsActionsHostedRunners(private val client: HttpClient) : Orgs.Actions.HostedRunners {
    override val images: Orgs.Actions.HostedRunners.Images = KtorOrgsActionsHostedRunnersImages(client)

    override val limits: Orgs.Actions.HostedRunners.Limits = KtorOrgsActionsHostedRunnersLimits(client)

    override val machineSizes: Orgs.Actions.HostedRunners.MachineSizes = KtorOrgsActionsHostedRunnersMachineSizes(client)

    override val platforms: Orgs.Actions.HostedRunners.Platforms = KtorOrgsActionsHostedRunnersPlatforms(client)

    override suspend fun actionsListHostedRunnersForOrg(org: String, page: Long, perPage: Long): Orgs.Actions.HostedRunners.ActionsListHostedRunnersForOrgResponse =
        client.get("/orgs/$org/actions/hosted-runners") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()

    override suspend fun actionsCreateHostedRunnerForOrg(org: String, body: Orgs.Actions.HostedRunners.ActionsCreateHostedRunnerForOrgBody): ActionsHostedRunner =
        client.post("/orgs/$org/actions/hosted-runners") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()

    override suspend fun actionsGetHostedRunnerForOrg(org: String, hostedRunnerId: Long): ActionsHostedRunner =
        client.get("/orgs/$org/actions/hosted-runners/$hostedRunnerId").body()

    override suspend fun actionsDeleteHostedRunnerForOrg(org: String, hostedRunnerId: Long): ActionsHostedRunner =
        client.delete("/orgs/$org/actions/hosted-runners/$hostedRunnerId").body()

    override suspend fun actionsUpdateHostedRunnerForOrg(org: String, hostedRunnerId: Long, body: Orgs.Actions.HostedRunners.ActionsUpdateHostedRunnerForOrgBody): ActionsHostedRunner =
        client.patch("/orgs/$org/actions/hosted-runners/$hostedRunnerId") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()
}

internal class KtorOrgsActionsHostedRunnersImages(private val client: HttpClient) : Orgs.Actions.HostedRunners.Images {
    override val custom: Orgs.Actions.HostedRunners.Images.Custom = KtorOrgsActionsHostedRunnersImagesCustom(client)

    override val githubOwned: Orgs.Actions.HostedRunners.Images.GithubOwned = KtorOrgsActionsHostedRunnersImagesGithubOwned(client)

    override val partner: Orgs.Actions.HostedRunners.Images.Partner = KtorOrgsActionsHostedRunnersImagesPartner(client)
}

internal class KtorOrgsActionsHostedRunnersImagesCustom(private val client: HttpClient) : Orgs.Actions.HostedRunners.Images.Custom {
    override val versions: Orgs.Actions.HostedRunners.Images.Custom.Versions = KtorOrgsActionsHostedRunnersImagesCustomVersions(client)

    override suspend fun actionsListCustomImagesForOrg(org: String): Orgs.Actions.HostedRunners.Images.Custom.ActionsListCustomImagesForOrgResponse =
        client.get("/orgs/$org/actions/hosted-runners/images/custom").body()

    override suspend fun actionsGetCustomImageForOrg(org: String, imageDefinitionId: Long): ActionsHostedRunnerCustomImage =
        client.get("/orgs/$org/actions/hosted-runners/images/custom/$imageDefinitionId").body()

    override suspend fun actionsDeleteCustomImageFromOrg(org: String, imageDefinitionId: Long): Unit =
        client.delete("/orgs/$org/actions/hosted-runners/images/custom/$imageDefinitionId").body()
}

internal class KtorOrgsActionsHostedRunnersImagesCustomVersions(private val client: HttpClient) : Orgs.Actions.HostedRunners.Images.Custom.Versions {
    override suspend fun actionsListCustomImageVersionsForOrg(org: String, imageDefinitionId: Long): Orgs.Actions.HostedRunners.Images.Custom.Versions.ActionsListCustomImageVersionsForOrgResponse =
        client.get("/orgs/$org/actions/hosted-runners/images/custom/$imageDefinitionId/versions").body()

    override suspend fun actionsGetCustomImageVersionForOrg(org: String, imageDefinitionId: Long, version: String): ActionsHostedRunnerCustomImageVersion =
        client.get("/orgs/$org/actions/hosted-runners/images/custom/$imageDefinitionId/versions/$version").body()

    override suspend fun actionsDeleteCustomImageVersionFromOrg(org: String, imageDefinitionId: Long, version: String): Unit =
        client.delete("/orgs/$org/actions/hosted-runners/images/custom/$imageDefinitionId/versions/$version").body()
}

internal class KtorOrgsActionsHostedRunnersImagesGithubOwned(private val client: HttpClient) : Orgs.Actions.HostedRunners.Images.GithubOwned {
    override suspend fun actionsGetHostedRunnersGithubOwnedImagesForOrg(org: String): Orgs.Actions.HostedRunners.Images.GithubOwned.ActionsGetHostedRunnersGithubOwnedImagesForOrgResponse =
        client.get("/orgs/$org/actions/hosted-runners/images/github-owned").body()
}

internal class KtorOrgsActionsHostedRunnersImagesPartner(private val client: HttpClient) : Orgs.Actions.HostedRunners.Images.Partner {
    override suspend fun actionsGetHostedRunnersPartnerImagesForOrg(org: String): Orgs.Actions.HostedRunners.Images.Partner.ActionsGetHostedRunnersPartnerImagesForOrgResponse =
        client.get("/orgs/$org/actions/hosted-runners/images/partner").body()
}

internal class KtorOrgsActionsHostedRunnersLimits(private val client: HttpClient) : Orgs.Actions.HostedRunners.Limits {
    override suspend fun actionsGetHostedRunnersLimitsForOrg(org: String): ActionsHostedRunnerLimits =
        client.get("/orgs/$org/actions/hosted-runners/limits").body()
}

internal class KtorOrgsActionsHostedRunnersMachineSizes(private val client: HttpClient) : Orgs.Actions.HostedRunners.MachineSizes {
    override suspend fun actionsGetHostedRunnersMachineSpecsForOrg(org: String): Orgs.Actions.HostedRunners.MachineSizes.ActionsGetHostedRunnersMachineSpecsForOrgResponse =
        client.get("/orgs/$org/actions/hosted-runners/machine-sizes").body()
}

internal class KtorOrgsActionsHostedRunnersPlatforms(private val client: HttpClient) : Orgs.Actions.HostedRunners.Platforms {
    override suspend fun actionsGetHostedRunnersPlatformsForOrg(org: String): Orgs.Actions.HostedRunners.Platforms.ActionsGetHostedRunnersPlatformsForOrgResponse =
        client.get("/orgs/$org/actions/hosted-runners/platforms").body()
}

internal class KtorOrgsActionsOidc(private val client: HttpClient) : Orgs.Actions.Oidc {
    override val customization: Orgs.Actions.Oidc.Customization = KtorOrgsActionsOidcCustomization(client)
}

internal class KtorOrgsActionsOidcCustomization(private val client: HttpClient) : Orgs.Actions.Oidc.Customization {
    override val properties: Orgs.Actions.Oidc.Customization.PropertiesApi = KtorOrgsActionsOidcCustomizationPropertiesApi(client)

    override val sub: Orgs.Actions.Oidc.Customization.Sub = KtorOrgsActionsOidcCustomizationSub(client)
}

internal class KtorOrgsActionsOidcCustomizationPropertiesApi(private val client: HttpClient) : Orgs.Actions.Oidc.Customization.PropertiesApi {
    override val repo: Orgs.Actions.Oidc.Customization.PropertiesApi.Repo = KtorOrgsActionsOidcCustomizationPropertiesApiRepo(client)
}

internal class KtorOrgsActionsOidcCustomizationPropertiesApiRepo(private val client: HttpClient) : Orgs.Actions.Oidc.Customization.PropertiesApi.Repo {
    override suspend fun oidcListOidcCustomPropertyInclusionsForOrg(org: String): Orgs.Actions.Oidc.Customization.PropertiesApi.Repo.OidcListOidcCustomPropertyInclusionsForOrgResult {
        val response = client.get("/orgs/$org/actions/oidc/customization/properties/repo")
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Actions.Oidc.Customization.PropertiesApi.Repo.OidcListOidcCustomPropertyInclusionsForOrgResult.OK(response.body())
            HttpStatusCode.Forbidden -> Orgs.Actions.Oidc.Customization.PropertiesApi.Repo.OidcListOidcCustomPropertyInclusionsForOrgResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.Actions.Oidc.Customization.PropertiesApi.Repo.OidcListOidcCustomPropertyInclusionsForOrgResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun oidcCreateOidcCustomPropertyInclusionForOrg(org: String, body: OidcCustomPropertyInclusionInput): Orgs.Actions.Oidc.Customization.PropertiesApi.Repo.OidcCreateOidcCustomPropertyInclusionForOrgResult {
        val response = client.post("/orgs/$org/actions/oidc/customization/properties/repo") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> Orgs.Actions.Oidc.Customization.PropertiesApi.Repo.OidcCreateOidcCustomPropertyInclusionForOrgResult.Created(response.body())
            HttpStatusCode.BadRequest -> Orgs.Actions.Oidc.Customization.PropertiesApi.Repo.OidcCreateOidcCustomPropertyInclusionForOrgResult.BadRequest
            HttpStatusCode.Forbidden -> Orgs.Actions.Oidc.Customization.PropertiesApi.Repo.OidcCreateOidcCustomPropertyInclusionForOrgResult.Forbidden(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.Actions.Oidc.Customization.PropertiesApi.Repo.OidcCreateOidcCustomPropertyInclusionForOrgResult.UnprocessableEntity
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun oidcDeleteOidcCustomPropertyInclusionForOrg(org: String, customPropertyName: String): Orgs.Actions.Oidc.Customization.PropertiesApi.Repo.OidcDeleteOidcCustomPropertyInclusionForOrgResult {
        val response = client.delete("/orgs/$org/actions/oidc/customization/properties/repo/$customPropertyName")
        return when (response.status) {
            HttpStatusCode.NoContent -> Orgs.Actions.Oidc.Customization.PropertiesApi.Repo.OidcDeleteOidcCustomPropertyInclusionForOrgResult.NoContent
            HttpStatusCode.BadRequest -> Orgs.Actions.Oidc.Customization.PropertiesApi.Repo.OidcDeleteOidcCustomPropertyInclusionForOrgResult.BadRequest
            HttpStatusCode.Forbidden -> Orgs.Actions.Oidc.Customization.PropertiesApi.Repo.OidcDeleteOidcCustomPropertyInclusionForOrgResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.Actions.Oidc.Customization.PropertiesApi.Repo.OidcDeleteOidcCustomPropertyInclusionForOrgResult.NotFound
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsActionsOidcCustomizationSub(private val client: HttpClient) : Orgs.Actions.Oidc.Customization.Sub {
    override suspend fun oidcGetOidcCustomSubTemplateForOrg(org: String): OidcCustomSub =
        client.get("/orgs/$org/actions/oidc/customization/sub").body()

    override suspend fun oidcUpdateOidcCustomSubTemplateForOrg(org: String, body: OidcCustomSub): Orgs.Actions.Oidc.Customization.Sub.OidcUpdateOidcCustomSubTemplateForOrgResult {
        val response = client.put("/orgs/$org/actions/oidc/customization/sub") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> Orgs.Actions.Oidc.Customization.Sub.OidcUpdateOidcCustomSubTemplateForOrgResult.Created(response.body())
            HttpStatusCode.Forbidden -> Orgs.Actions.Oidc.Customization.Sub.OidcUpdateOidcCustomSubTemplateForOrgResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.Actions.Oidc.Customization.Sub.OidcUpdateOidcCustomSubTemplateForOrgResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsActionsPermissions(private val client: HttpClient) : Orgs.Actions.Permissions {
    override val artifactAndLogRetention: Orgs.Actions.Permissions.ArtifactAndLogRetention = KtorOrgsActionsPermissionsArtifactAndLogRetention(client)

    override val forkPrContributorApproval: Orgs.Actions.Permissions.ForkPrContributorApproval = KtorOrgsActionsPermissionsForkPrContributorApproval(client)

    override val forkPrWorkflowsPrivateRepos: Orgs.Actions.Permissions.ForkPrWorkflowsPrivateRepos = KtorOrgsActionsPermissionsForkPrWorkflowsPrivateRepos(client)

    override val repositories: Orgs.Actions.Permissions.Repositories = KtorOrgsActionsPermissionsRepositories(client)

    override val selectedActions: Orgs.Actions.Permissions.SelectedActions = KtorOrgsActionsPermissionsSelectedActions(client)

    override val selfHostedRunners: Orgs.Actions.Permissions.SelfHostedRunners = KtorOrgsActionsPermissionsSelfHostedRunners(client)

    override val workflow: Orgs.Actions.Permissions.Workflow = KtorOrgsActionsPermissionsWorkflow(client)

    override suspend fun actionsGetGithubActionsPermissionsOrganization(org: String): ActionsOrganizationPermissions =
        client.get("/orgs/$org/actions/permissions").body()

    override suspend fun actionsSetGithubActionsPermissionsOrganization(org: String, body: Orgs.Actions.Permissions.ActionsSetGithubActionsPermissionsOrganizationBody): Unit =
        client.put("/orgs/$org/actions/permissions") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()
}

internal class KtorOrgsActionsPermissionsArtifactAndLogRetention(private val client: HttpClient) : Orgs.Actions.Permissions.ArtifactAndLogRetention {
    override suspend fun actionsGetArtifactAndLogRetentionSettingsOrganization(org: String): Orgs.Actions.Permissions.ArtifactAndLogRetention.ActionsGetArtifactAndLogRetentionSettingsOrganizationResult {
        val response = client.get("/orgs/$org/actions/permissions/artifact-and-log-retention")
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Actions.Permissions.ArtifactAndLogRetention.ActionsGetArtifactAndLogRetentionSettingsOrganizationResult.OK(response.body())
            HttpStatusCode.Forbidden -> Orgs.Actions.Permissions.ArtifactAndLogRetention.ActionsGetArtifactAndLogRetentionSettingsOrganizationResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.Actions.Permissions.ArtifactAndLogRetention.ActionsGetArtifactAndLogRetentionSettingsOrganizationResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun actionsSetArtifactAndLogRetentionSettingsOrganization(org: String, body: ActionsArtifactAndLogRetention): Orgs.Actions.Permissions.ArtifactAndLogRetention.ActionsSetArtifactAndLogRetentionSettingsOrganizationResult {
        val response = client.put("/orgs/$org/actions/permissions/artifact-and-log-retention") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.NoContent -> Orgs.Actions.Permissions.ArtifactAndLogRetention.ActionsSetArtifactAndLogRetentionSettingsOrganizationResult.NoContent
            HttpStatusCode.Forbidden -> Orgs.Actions.Permissions.ArtifactAndLogRetention.ActionsSetArtifactAndLogRetentionSettingsOrganizationResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.Actions.Permissions.ArtifactAndLogRetention.ActionsSetArtifactAndLogRetentionSettingsOrganizationResult.NotFound(response.body())
            HttpStatusCode.Conflict -> Orgs.Actions.Permissions.ArtifactAndLogRetention.ActionsSetArtifactAndLogRetentionSettingsOrganizationResult.Conflict(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.Actions.Permissions.ArtifactAndLogRetention.ActionsSetArtifactAndLogRetentionSettingsOrganizationResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsActionsPermissionsForkPrContributorApproval(private val client: HttpClient) : Orgs.Actions.Permissions.ForkPrContributorApproval {
    override suspend fun actionsGetForkPrContributorApprovalPermissionsOrganization(org: String): Orgs.Actions.Permissions.ForkPrContributorApproval.ActionsGetForkPrContributorApprovalPermissionsOrganizationResult {
        val response = client.get("/orgs/$org/actions/permissions/fork-pr-contributor-approval")
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Actions.Permissions.ForkPrContributorApproval.ActionsGetForkPrContributorApprovalPermissionsOrganizationResult.OK(response.body())
            HttpStatusCode.NotFound -> Orgs.Actions.Permissions.ForkPrContributorApproval.ActionsGetForkPrContributorApprovalPermissionsOrganizationResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun actionsSetForkPrContributorApprovalPermissionsOrganization(org: String, body: ActionsForkPrContributorApproval): Orgs.Actions.Permissions.ForkPrContributorApproval.ActionsSetForkPrContributorApprovalPermissionsOrganizationResult {
        val response = client.put("/orgs/$org/actions/permissions/fork-pr-contributor-approval") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.NoContent -> Orgs.Actions.Permissions.ForkPrContributorApproval.ActionsSetForkPrContributorApprovalPermissionsOrganizationResult.NoContent
            HttpStatusCode.NotFound -> Orgs.Actions.Permissions.ForkPrContributorApproval.ActionsSetForkPrContributorApprovalPermissionsOrganizationResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.Actions.Permissions.ForkPrContributorApproval.ActionsSetForkPrContributorApprovalPermissionsOrganizationResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsActionsPermissionsForkPrWorkflowsPrivateRepos(private val client: HttpClient) : Orgs.Actions.Permissions.ForkPrWorkflowsPrivateRepos {
    override suspend fun actionsGetPrivateRepoForkPrWorkflowsSettingsOrganization(org: String): Orgs.Actions.Permissions.ForkPrWorkflowsPrivateRepos.ActionsGetPrivateRepoForkPrWorkflowsSettingsOrganizationResult {
        val response = client.get("/orgs/$org/actions/permissions/fork-pr-workflows-private-repos")
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Actions.Permissions.ForkPrWorkflowsPrivateRepos.ActionsGetPrivateRepoForkPrWorkflowsSettingsOrganizationResult.OK(response.body())
            HttpStatusCode.Forbidden -> Orgs.Actions.Permissions.ForkPrWorkflowsPrivateRepos.ActionsGetPrivateRepoForkPrWorkflowsSettingsOrganizationResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.Actions.Permissions.ForkPrWorkflowsPrivateRepos.ActionsGetPrivateRepoForkPrWorkflowsSettingsOrganizationResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun actionsSetPrivateRepoForkPrWorkflowsSettingsOrganization(org: String, body: ActionsForkPrWorkflowsPrivateReposRequest): Orgs.Actions.Permissions.ForkPrWorkflowsPrivateRepos.ActionsSetPrivateRepoForkPrWorkflowsSettingsOrganizationResult {
        val response = client.put("/orgs/$org/actions/permissions/fork-pr-workflows-private-repos") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.NoContent -> Orgs.Actions.Permissions.ForkPrWorkflowsPrivateRepos.ActionsSetPrivateRepoForkPrWorkflowsSettingsOrganizationResult.NoContent
            HttpStatusCode.Forbidden -> Orgs.Actions.Permissions.ForkPrWorkflowsPrivateRepos.ActionsSetPrivateRepoForkPrWorkflowsSettingsOrganizationResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.Actions.Permissions.ForkPrWorkflowsPrivateRepos.ActionsSetPrivateRepoForkPrWorkflowsSettingsOrganizationResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.Actions.Permissions.ForkPrWorkflowsPrivateRepos.ActionsSetPrivateRepoForkPrWorkflowsSettingsOrganizationResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsActionsPermissionsRepositories(private val client: HttpClient) : Orgs.Actions.Permissions.Repositories {
    override suspend fun actionsListSelectedRepositoriesEnabledGithubActionsOrganization(org: String, page: Long, perPage: Long): Orgs.Actions.Permissions.Repositories.ActionsListSelectedRepositoriesEnabledGithubActionsOrganizationResponse =
        client.get("/orgs/$org/actions/permissions/repositories") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()

    override suspend fun actionsSetSelectedRepositoriesEnabledGithubActionsOrganization(org: String, body: Orgs.Actions.Permissions.Repositories.ActionsSetSelectedRepositoriesEnabledGithubActionsOrganizationBody): Unit =
        client.put("/orgs/$org/actions/permissions/repositories") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()

    override suspend fun actionsEnableSelectedRepositoryGithubActionsOrganization(org: String, repositoryId: Long): Unit =
        client.put("/orgs/$org/actions/permissions/repositories/$repositoryId").body()

    override suspend fun actionsDisableSelectedRepositoryGithubActionsOrganization(org: String, repositoryId: Long): Unit =
        client.delete("/orgs/$org/actions/permissions/repositories/$repositoryId").body()
}

internal class KtorOrgsActionsPermissionsSelectedActions(private val client: HttpClient) : Orgs.Actions.Permissions.SelectedActions {
    override suspend fun actionsGetAllowedActionsOrganization(org: String): SelectedActions =
        client.get("/orgs/$org/actions/permissions/selected-actions").body()

    override suspend fun actionsSetAllowedActionsOrganization(org: String, body: SelectedActions?): Unit =
        client.put("/orgs/$org/actions/permissions/selected-actions") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }.body()
}

internal class KtorOrgsActionsPermissionsSelfHostedRunners(private val client: HttpClient) : Orgs.Actions.Permissions.SelfHostedRunners {
    override val repositories: Orgs.Actions.Permissions.SelfHostedRunners.RepositoriesApi = KtorOrgsActionsPermissionsSelfHostedRunnersRepositoriesApi(client)

    override suspend fun actionsGetSelfHostedRunnersPermissionsOrganization(org: String): Orgs.Actions.Permissions.SelfHostedRunners.ActionsGetSelfHostedRunnersPermissionsOrganizationResult {
        val response = client.get("/orgs/$org/actions/permissions/self-hosted-runners")
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Actions.Permissions.SelfHostedRunners.ActionsGetSelfHostedRunnersPermissionsOrganizationResult.OK(response.body())
            HttpStatusCode.Forbidden -> Orgs.Actions.Permissions.SelfHostedRunners.ActionsGetSelfHostedRunnersPermissionsOrganizationResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.Actions.Permissions.SelfHostedRunners.ActionsGetSelfHostedRunnersPermissionsOrganizationResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun actionsSetSelfHostedRunnersPermissionsOrganization(org: String, body: Orgs.Actions.Permissions.SelfHostedRunners.ActionsSetSelfHostedRunnersPermissionsOrganizationBody): Orgs.Actions.Permissions.SelfHostedRunners.ActionsSetSelfHostedRunnersPermissionsOrganizationResult {
        val response = client.put("/orgs/$org/actions/permissions/self-hosted-runners") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.NoContent -> Orgs.Actions.Permissions.SelfHostedRunners.ActionsSetSelfHostedRunnersPermissionsOrganizationResult.NoContent
            HttpStatusCode.Forbidden -> Orgs.Actions.Permissions.SelfHostedRunners.ActionsSetSelfHostedRunnersPermissionsOrganizationResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.Actions.Permissions.SelfHostedRunners.ActionsSetSelfHostedRunnersPermissionsOrganizationResult.NotFound(response.body())
            HttpStatusCode.Conflict -> Orgs.Actions.Permissions.SelfHostedRunners.ActionsSetSelfHostedRunnersPermissionsOrganizationResult.Conflict(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.Actions.Permissions.SelfHostedRunners.ActionsSetSelfHostedRunnersPermissionsOrganizationResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsActionsPermissionsSelfHostedRunnersRepositoriesApi(private val client: HttpClient) : Orgs.Actions.Permissions.SelfHostedRunners.RepositoriesApi {
    override suspend fun actionsListSelectedRepositoriesSelfHostedRunnersOrganization(org: String, page: Long, perPage: Long): Orgs.Actions.Permissions.SelfHostedRunners.RepositoriesApi.ActionsListSelectedRepositoriesSelfHostedRunnersOrganizationResult {
        val response = client.get("/orgs/$org/actions/permissions/self-hosted-runners/repositories") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Actions.Permissions.SelfHostedRunners.RepositoriesApi.ActionsListSelectedRepositoriesSelfHostedRunnersOrganizationResult.OK(response.body())
            HttpStatusCode.Forbidden -> Orgs.Actions.Permissions.SelfHostedRunners.RepositoriesApi.ActionsListSelectedRepositoriesSelfHostedRunnersOrganizationResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.Actions.Permissions.SelfHostedRunners.RepositoriesApi.ActionsListSelectedRepositoriesSelfHostedRunnersOrganizationResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun actionsSetSelectedRepositoriesSelfHostedRunnersOrganization(org: String, body: Orgs.Actions.Permissions.SelfHostedRunners.RepositoriesApi.ActionsSetSelectedRepositoriesSelfHostedRunnersOrganizationBody): Orgs.Actions.Permissions.SelfHostedRunners.RepositoriesApi.ActionsSetSelectedRepositoriesSelfHostedRunnersOrganizationResult {
        val response = client.put("/orgs/$org/actions/permissions/self-hosted-runners/repositories") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.NoContent -> Orgs.Actions.Permissions.SelfHostedRunners.RepositoriesApi.ActionsSetSelectedRepositoriesSelfHostedRunnersOrganizationResult.NoContent
            HttpStatusCode.Forbidden -> Orgs.Actions.Permissions.SelfHostedRunners.RepositoriesApi.ActionsSetSelectedRepositoriesSelfHostedRunnersOrganizationResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.Actions.Permissions.SelfHostedRunners.RepositoriesApi.ActionsSetSelectedRepositoriesSelfHostedRunnersOrganizationResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.Actions.Permissions.SelfHostedRunners.RepositoriesApi.ActionsSetSelectedRepositoriesSelfHostedRunnersOrganizationResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun actionsEnableSelectedRepositorySelfHostedRunnersOrganization(org: String, repositoryId: Long): Orgs.Actions.Permissions.SelfHostedRunners.RepositoriesApi.ActionsEnableSelectedRepositorySelfHostedRunnersOrganizationResult {
        val response = client.put("/orgs/$org/actions/permissions/self-hosted-runners/repositories/$repositoryId")
        return when (response.status) {
            HttpStatusCode.NoContent -> Orgs.Actions.Permissions.SelfHostedRunners.RepositoriesApi.ActionsEnableSelectedRepositorySelfHostedRunnersOrganizationResult.NoContent
            HttpStatusCode.Forbidden -> Orgs.Actions.Permissions.SelfHostedRunners.RepositoriesApi.ActionsEnableSelectedRepositorySelfHostedRunnersOrganizationResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.Actions.Permissions.SelfHostedRunners.RepositoriesApi.ActionsEnableSelectedRepositorySelfHostedRunnersOrganizationResult.NotFound(response.body())
            HttpStatusCode.Conflict -> Orgs.Actions.Permissions.SelfHostedRunners.RepositoriesApi.ActionsEnableSelectedRepositorySelfHostedRunnersOrganizationResult.Conflict(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.Actions.Permissions.SelfHostedRunners.RepositoriesApi.ActionsEnableSelectedRepositorySelfHostedRunnersOrganizationResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun actionsDisableSelectedRepositorySelfHostedRunnersOrganization(org: String, repositoryId: Long): Orgs.Actions.Permissions.SelfHostedRunners.RepositoriesApi.ActionsDisableSelectedRepositorySelfHostedRunnersOrganizationResult {
        val response = client.delete("/orgs/$org/actions/permissions/self-hosted-runners/repositories/$repositoryId")
        return when (response.status) {
            HttpStatusCode.NoContent -> Orgs.Actions.Permissions.SelfHostedRunners.RepositoriesApi.ActionsDisableSelectedRepositorySelfHostedRunnersOrganizationResult.NoContent
            HttpStatusCode.Forbidden -> Orgs.Actions.Permissions.SelfHostedRunners.RepositoriesApi.ActionsDisableSelectedRepositorySelfHostedRunnersOrganizationResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.Actions.Permissions.SelfHostedRunners.RepositoriesApi.ActionsDisableSelectedRepositorySelfHostedRunnersOrganizationResult.NotFound(response.body())
            HttpStatusCode.Conflict -> Orgs.Actions.Permissions.SelfHostedRunners.RepositoriesApi.ActionsDisableSelectedRepositorySelfHostedRunnersOrganizationResult.Conflict(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.Actions.Permissions.SelfHostedRunners.RepositoriesApi.ActionsDisableSelectedRepositorySelfHostedRunnersOrganizationResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsActionsPermissionsWorkflow(private val client: HttpClient) : Orgs.Actions.Permissions.Workflow {
    override suspend fun actionsGetGithubActionsDefaultWorkflowPermissionsOrganization(org: String): ActionsGetDefaultWorkflowPermissions =
        client.get("/orgs/$org/actions/permissions/workflow").body()

    override suspend fun actionsSetGithubActionsDefaultWorkflowPermissionsOrganization(org: String, body: ActionsSetDefaultWorkflowPermissions?): Unit =
        client.put("/orgs/$org/actions/permissions/workflow") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }.body()
}

internal class KtorOrgsActionsRunnerGroups(private val client: HttpClient) : Orgs.Actions.RunnerGroups {
    override val hostedRunners: Orgs.Actions.RunnerGroups.HostedRunnersApi = KtorOrgsActionsRunnerGroupsHostedRunnersApi(client)

    override val repositories: Orgs.Actions.RunnerGroups.Repositories = KtorOrgsActionsRunnerGroupsRepositories(client)

    override val runners: Orgs.Actions.RunnerGroups.RunnersApi = KtorOrgsActionsRunnerGroupsRunnersApi(client)

    override suspend fun actionsListSelfHostedRunnerGroupsForOrg(org: String, page: Long, perPage: Long, visibleToRepository: String?): Orgs.Actions.RunnerGroups.ActionsListSelfHostedRunnerGroupsForOrgResponse =
        client.get("/orgs/$org/actions/runner-groups") {
            parameter("page", page)
            parameter("per_page", perPage)
            visibleToRepository?.let { parameter("visible_to_repository", it) }
        }.body()

    override suspend fun actionsCreateSelfHostedRunnerGroupForOrg(org: String, body: Orgs.Actions.RunnerGroups.ActionsCreateSelfHostedRunnerGroupForOrgBody): RunnerGroupsOrg =
        client.post("/orgs/$org/actions/runner-groups") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()

    override suspend fun actionsGetSelfHostedRunnerGroupForOrg(org: String, runnerGroupId: Long): RunnerGroupsOrg =
        client.get("/orgs/$org/actions/runner-groups/$runnerGroupId").body()

    override suspend fun actionsDeleteSelfHostedRunnerGroupFromOrg(org: String, runnerGroupId: Long): Unit =
        client.delete("/orgs/$org/actions/runner-groups/$runnerGroupId").body()

    override suspend fun actionsUpdateSelfHostedRunnerGroupForOrg(org: String, runnerGroupId: Long, body: Orgs.Actions.RunnerGroups.ActionsUpdateSelfHostedRunnerGroupForOrgBody): RunnerGroupsOrg =
        client.patch("/orgs/$org/actions/runner-groups/$runnerGroupId") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()
}

internal class KtorOrgsActionsRunnerGroupsHostedRunnersApi(private val client: HttpClient) : Orgs.Actions.RunnerGroups.HostedRunnersApi {
    override suspend fun actionsListGithubHostedRunnersInGroupForOrg(org: String, runnerGroupId: Long, page: Long, perPage: Long): Orgs.Actions.RunnerGroups.HostedRunnersApi.ActionsListGithubHostedRunnersInGroupForOrgResponse =
        client.get("/orgs/$org/actions/runner-groups/$runnerGroupId/hosted-runners") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()
}

internal class KtorOrgsActionsRunnerGroupsRepositories(private val client: HttpClient) : Orgs.Actions.RunnerGroups.Repositories {
    override suspend fun actionsListRepoAccessToSelfHostedRunnerGroupInOrg(org: String, runnerGroupId: Long, page: Long, perPage: Long): Orgs.Actions.RunnerGroups.Repositories.ActionsListRepoAccessToSelfHostedRunnerGroupInOrgResponse =
        client.get("/orgs/$org/actions/runner-groups/$runnerGroupId/repositories") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()

    override suspend fun actionsSetRepoAccessToSelfHostedRunnerGroupInOrg(org: String, runnerGroupId: Long, body: Orgs.Actions.RunnerGroups.Repositories.ActionsSetRepoAccessToSelfHostedRunnerGroupInOrgBody): Unit =
        client.put("/orgs/$org/actions/runner-groups/$runnerGroupId/repositories") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()

    override suspend fun actionsAddRepoAccessToSelfHostedRunnerGroupInOrg(org: String, runnerGroupId: Long, repositoryId: Long): Unit =
        client.put("/orgs/$org/actions/runner-groups/$runnerGroupId/repositories/$repositoryId").body()

    override suspend fun actionsRemoveRepoAccessToSelfHostedRunnerGroupInOrg(org: String, runnerGroupId: Long, repositoryId: Long): Unit =
        client.delete("/orgs/$org/actions/runner-groups/$runnerGroupId/repositories/$repositoryId").body()
}

internal class KtorOrgsActionsRunnerGroupsRunnersApi(private val client: HttpClient) : Orgs.Actions.RunnerGroups.RunnersApi {
    override suspend fun actionsListSelfHostedRunnersInGroupForOrg(org: String, runnerGroupId: Long, page: Long, perPage: Long): Orgs.Actions.RunnerGroups.RunnersApi.ActionsListSelfHostedRunnersInGroupForOrgResponse =
        client.get("/orgs/$org/actions/runner-groups/$runnerGroupId/runners") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()

    override suspend fun actionsSetSelfHostedRunnersInGroupForOrg(org: String, runnerGroupId: Long, body: Orgs.Actions.RunnerGroups.RunnersApi.ActionsSetSelfHostedRunnersInGroupForOrgBody): Unit =
        client.put("/orgs/$org/actions/runner-groups/$runnerGroupId/runners") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()

    override suspend fun actionsAddSelfHostedRunnerToGroupForOrg(org: String, runnerGroupId: Long, runnerId: Long): Unit =
        client.put("/orgs/$org/actions/runner-groups/$runnerGroupId/runners/$runnerId").body()

    override suspend fun actionsRemoveSelfHostedRunnerFromGroupForOrg(org: String, runnerGroupId: Long, runnerId: Long): Unit =
        client.delete("/orgs/$org/actions/runner-groups/$runnerGroupId/runners/$runnerId").body()
}

internal class KtorOrgsActionsRunners(private val client: HttpClient) : Orgs.Actions.Runners {
    override val downloads: Orgs.Actions.Runners.Downloads = KtorOrgsActionsRunnersDownloads(client)

    override val generateJitconfig: Orgs.Actions.Runners.GenerateJitconfig = KtorOrgsActionsRunnersGenerateJitconfig(client)

    override val registrationToken: Orgs.Actions.Runners.RegistrationToken = KtorOrgsActionsRunnersRegistrationToken(client)

    override val removeToken: Orgs.Actions.Runners.RemoveToken = KtorOrgsActionsRunnersRemoveToken(client)

    override val labels: Orgs.Actions.Runners.Labels = KtorOrgsActionsRunnersLabels(client)

    override suspend fun actionsListSelfHostedRunnersForOrg(org: String, page: Long, perPage: Long, name: String?): Orgs.Actions.Runners.ActionsListSelfHostedRunnersForOrgResponse =
        client.get("/orgs/$org/actions/runners") {
            parameter("page", page)
            parameter("per_page", perPage)
            name?.let { parameter("name", it) }
        }.body()

    override suspend fun actionsGetSelfHostedRunnerForOrg(org: String, runnerId: Long): Runner =
        client.get("/orgs/$org/actions/runners/$runnerId").body()

    override suspend fun actionsDeleteSelfHostedRunnerFromOrg(org: String, runnerId: Long): Orgs.Actions.Runners.ActionsDeleteSelfHostedRunnerFromOrgResult {
        val response = client.delete("/orgs/$org/actions/runners/$runnerId")
        return when (response.status) {
            HttpStatusCode.NoContent -> Orgs.Actions.Runners.ActionsDeleteSelfHostedRunnerFromOrgResult.NoContent
            HttpStatusCode.UnprocessableEntity -> Orgs.Actions.Runners.ActionsDeleteSelfHostedRunnerFromOrgResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsActionsRunnersDownloads(private val client: HttpClient) : Orgs.Actions.Runners.Downloads {
    override suspend fun actionsListRunnerApplicationsForOrg(org: String): List<RunnerApplication> =
        client.get("/orgs/$org/actions/runners/downloads").body()
}

internal class KtorOrgsActionsRunnersGenerateJitconfig(private val client: HttpClient) : Orgs.Actions.Runners.GenerateJitconfig {
    override suspend fun actionsGenerateRunnerJitconfigForOrg(org: String, body: Orgs.Actions.Runners.GenerateJitconfig.ActionsGenerateRunnerJitconfigForOrgBody): Orgs.Actions.Runners.GenerateJitconfig.ActionsGenerateRunnerJitconfigForOrgResult {
        val response = client.post("/orgs/$org/actions/runners/generate-jitconfig") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> Orgs.Actions.Runners.GenerateJitconfig.ActionsGenerateRunnerJitconfigForOrgResult.Created(response.body())
            HttpStatusCode.NotFound -> Orgs.Actions.Runners.GenerateJitconfig.ActionsGenerateRunnerJitconfigForOrgResult.NotFound(response.body())
            HttpStatusCode.Conflict -> Orgs.Actions.Runners.GenerateJitconfig.ActionsGenerateRunnerJitconfigForOrgResult.Conflict(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.Actions.Runners.GenerateJitconfig.ActionsGenerateRunnerJitconfigForOrgResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsActionsRunnersRegistrationToken(private val client: HttpClient) : Orgs.Actions.Runners.RegistrationToken {
    override suspend fun actionsCreateRegistrationTokenForOrg(org: String): AuthenticationToken =
        client.post("/orgs/$org/actions/runners/registration-token").body()
}

internal class KtorOrgsActionsRunnersRemoveToken(private val client: HttpClient) : Orgs.Actions.Runners.RemoveToken {
    override suspend fun actionsCreateRemoveTokenForOrg(org: String): AuthenticationToken =
        client.post("/orgs/$org/actions/runners/remove-token").body()
}

internal class KtorOrgsActionsRunnersLabels(private val client: HttpClient) : Orgs.Actions.Runners.Labels {
    override suspend fun actionsListLabelsForSelfHostedRunnerForOrg(org: String, runnerId: Long): Orgs.Actions.Runners.Labels.ActionsListLabelsForSelfHostedRunnerForOrgResult {
        val response = client.get("/orgs/$org/actions/runners/$runnerId/labels")
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Actions.Runners.Labels.ActionsListLabelsForSelfHostedRunnerForOrgResult.OK(response.body())
            HttpStatusCode.NotFound -> Orgs.Actions.Runners.Labels.ActionsListLabelsForSelfHostedRunnerForOrgResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun actionsSetCustomLabelsForSelfHostedRunnerForOrg(org: String, runnerId: Long, body: Orgs.Actions.Runners.Labels.ActionsSetCustomLabelsForSelfHostedRunnerForOrgBody): Orgs.Actions.Runners.Labels.ActionsSetCustomLabelsForSelfHostedRunnerForOrgResult {
        val response = client.put("/orgs/$org/actions/runners/$runnerId/labels") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Actions.Runners.Labels.ActionsSetCustomLabelsForSelfHostedRunnerForOrgResult.OK(response.body())
            HttpStatusCode.NotFound -> Orgs.Actions.Runners.Labels.ActionsSetCustomLabelsForSelfHostedRunnerForOrgResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.Actions.Runners.Labels.ActionsSetCustomLabelsForSelfHostedRunnerForOrgResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun actionsAddCustomLabelsToSelfHostedRunnerForOrg(org: String, runnerId: Long, body: Orgs.Actions.Runners.Labels.ActionsAddCustomLabelsToSelfHostedRunnerForOrgBody): Orgs.Actions.Runners.Labels.ActionsAddCustomLabelsToSelfHostedRunnerForOrgResult {
        val response = client.post("/orgs/$org/actions/runners/$runnerId/labels") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Actions.Runners.Labels.ActionsAddCustomLabelsToSelfHostedRunnerForOrgResult.OK(response.body())
            HttpStatusCode.NotFound -> Orgs.Actions.Runners.Labels.ActionsAddCustomLabelsToSelfHostedRunnerForOrgResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.Actions.Runners.Labels.ActionsAddCustomLabelsToSelfHostedRunnerForOrgResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun actionsRemoveAllCustomLabelsFromSelfHostedRunnerForOrg(org: String, runnerId: Long): Orgs.Actions.Runners.Labels.ActionsRemoveAllCustomLabelsFromSelfHostedRunnerForOrgResult {
        val response = client.delete("/orgs/$org/actions/runners/$runnerId/labels")
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Actions.Runners.Labels.ActionsRemoveAllCustomLabelsFromSelfHostedRunnerForOrgResult.OK(response.body())
            HttpStatusCode.NotFound -> Orgs.Actions.Runners.Labels.ActionsRemoveAllCustomLabelsFromSelfHostedRunnerForOrgResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun actionsRemoveCustomLabelFromSelfHostedRunnerForOrg(org: String, runnerId: Long, name: String): Orgs.Actions.Runners.Labels.ActionsRemoveCustomLabelFromSelfHostedRunnerForOrgResult {
        val response = client.delete("/orgs/$org/actions/runners/$runnerId/labels/$name")
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Actions.Runners.Labels.ActionsRemoveCustomLabelFromSelfHostedRunnerForOrgResult.OK(response.body())
            HttpStatusCode.NotFound -> Orgs.Actions.Runners.Labels.ActionsRemoveCustomLabelFromSelfHostedRunnerForOrgResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.Actions.Runners.Labels.ActionsRemoveCustomLabelFromSelfHostedRunnerForOrgResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsActionsSecrets(private val client: HttpClient) : Orgs.Actions.Secrets {
    override val publicKey: Orgs.Actions.Secrets.PublicKey = KtorOrgsActionsSecretsPublicKey(client)

    override val repositories: Orgs.Actions.Secrets.Repositories = KtorOrgsActionsSecretsRepositories(client)

    override suspend fun actionsListOrgSecrets(org: String, page: Long, perPage: Long): Orgs.Actions.Secrets.ActionsListOrgSecretsResponse =
        client.get("/orgs/$org/actions/secrets") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()

    override suspend fun actionsGetOrgSecret(org: String, secretName: String): OrganizationActionsSecret =
        client.get("/orgs/$org/actions/secrets/$secretName").body()

    override suspend fun actionsCreateOrUpdateOrgSecret(org: String, secretName: String, body: Orgs.Actions.Secrets.ActionsCreateOrUpdateOrgSecretBody): Orgs.Actions.Secrets.ActionsCreateOrUpdateOrgSecretResult {
        val response = client.put("/orgs/$org/actions/secrets/$secretName") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> Orgs.Actions.Secrets.ActionsCreateOrUpdateOrgSecretResult.Created(response.body())
            HttpStatusCode.NoContent -> Orgs.Actions.Secrets.ActionsCreateOrUpdateOrgSecretResult.NoContent
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun actionsDeleteOrgSecret(org: String, secretName: String): Unit =
        client.delete("/orgs/$org/actions/secrets/$secretName").body()
}

internal class KtorOrgsActionsSecretsPublicKey(private val client: HttpClient) : Orgs.Actions.Secrets.PublicKey {
    override suspend fun actionsGetOrgPublicKey(org: String): ActionsPublicKey =
        client.get("/orgs/$org/actions/secrets/public-key").body()
}

internal class KtorOrgsActionsSecretsRepositories(private val client: HttpClient) : Orgs.Actions.Secrets.Repositories {
    override suspend fun actionsListSelectedReposForOrgSecret(org: String, secretName: String, page: Long, perPage: Long): Orgs.Actions.Secrets.Repositories.ActionsListSelectedReposForOrgSecretResponse =
        client.get("/orgs/$org/actions/secrets/$secretName/repositories") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()

    override suspend fun actionsSetSelectedReposForOrgSecret(org: String, secretName: String, body: Orgs.Actions.Secrets.Repositories.ActionsSetSelectedReposForOrgSecretBody): Unit =
        client.put("/orgs/$org/actions/secrets/$secretName/repositories") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()

    override suspend fun actionsAddSelectedRepoToOrgSecret(org: String, secretName: String, repositoryId: Long): Orgs.Actions.Secrets.Repositories.ActionsAddSelectedRepoToOrgSecretResult {
        val response = client.put("/orgs/$org/actions/secrets/$secretName/repositories/$repositoryId")
        return when (response.status) {
            HttpStatusCode.NoContent -> Orgs.Actions.Secrets.Repositories.ActionsAddSelectedRepoToOrgSecretResult.NoContent
            HttpStatusCode.Conflict -> Orgs.Actions.Secrets.Repositories.ActionsAddSelectedRepoToOrgSecretResult.Conflict
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun actionsRemoveSelectedRepoFromOrgSecret(org: String, secretName: String, repositoryId: Long): Orgs.Actions.Secrets.Repositories.ActionsRemoveSelectedRepoFromOrgSecretResult {
        val response = client.delete("/orgs/$org/actions/secrets/$secretName/repositories/$repositoryId")
        return when (response.status) {
            HttpStatusCode.NoContent -> Orgs.Actions.Secrets.Repositories.ActionsRemoveSelectedRepoFromOrgSecretResult.NoContent
            HttpStatusCode.Conflict -> Orgs.Actions.Secrets.Repositories.ActionsRemoveSelectedRepoFromOrgSecretResult.Conflict
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsActionsVariables(private val client: HttpClient) : Orgs.Actions.Variables {
    override val repositories: Orgs.Actions.Variables.Repositories = KtorOrgsActionsVariablesRepositories(client)

    override suspend fun actionsListOrgVariables(org: String, page: Long, perPage: Long): Orgs.Actions.Variables.ActionsListOrgVariablesResponse =
        client.get("/orgs/$org/actions/variables") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()

    override suspend fun actionsCreateOrgVariable(org: String, body: Orgs.Actions.Variables.ActionsCreateOrgVariableBody): EmptyObject =
        client.post("/orgs/$org/actions/variables") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()

    override suspend fun actionsGetOrgVariable(org: String, name: String): OrganizationActionsVariable =
        client.get("/orgs/$org/actions/variables/$name").body()

    override suspend fun actionsDeleteOrgVariable(org: String, name: String): Unit =
        client.delete("/orgs/$org/actions/variables/$name").body()

    override suspend fun actionsUpdateOrgVariable(org: String, name: String, body: Orgs.Actions.Variables.ActionsUpdateOrgVariableBody): Unit =
        client.patch("/orgs/$org/actions/variables/$name") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()
}

internal class KtorOrgsActionsVariablesRepositories(private val client: HttpClient) : Orgs.Actions.Variables.Repositories {
    override suspend fun actionsListSelectedReposForOrgVariable(org: String, name: String, page: Long, perPage: Long): Orgs.Actions.Variables.Repositories.ActionsListSelectedReposForOrgVariableResult {
        val response = client.get("/orgs/$org/actions/variables/$name/repositories") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Actions.Variables.Repositories.ActionsListSelectedReposForOrgVariableResult.OK(response.body())
            HttpStatusCode.Conflict -> Orgs.Actions.Variables.Repositories.ActionsListSelectedReposForOrgVariableResult.Conflict
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun actionsSetSelectedReposForOrgVariable(org: String, name: String, body: Orgs.Actions.Variables.Repositories.ActionsSetSelectedReposForOrgVariableBody): Orgs.Actions.Variables.Repositories.ActionsSetSelectedReposForOrgVariableResult {
        val response = client.put("/orgs/$org/actions/variables/$name/repositories") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.NoContent -> Orgs.Actions.Variables.Repositories.ActionsSetSelectedReposForOrgVariableResult.NoContent
            HttpStatusCode.Conflict -> Orgs.Actions.Variables.Repositories.ActionsSetSelectedReposForOrgVariableResult.Conflict
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun actionsAddSelectedRepoToOrgVariable(org: String, name: String, repositoryId: Long): Orgs.Actions.Variables.Repositories.ActionsAddSelectedRepoToOrgVariableResult {
        val response = client.put("/orgs/$org/actions/variables/$name/repositories/$repositoryId")
        return when (response.status) {
            HttpStatusCode.NoContent -> Orgs.Actions.Variables.Repositories.ActionsAddSelectedRepoToOrgVariableResult.NoContent
            HttpStatusCode.Conflict -> Orgs.Actions.Variables.Repositories.ActionsAddSelectedRepoToOrgVariableResult.Conflict
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun actionsRemoveSelectedRepoFromOrgVariable(org: String, name: String, repositoryId: Long): Orgs.Actions.Variables.Repositories.ActionsRemoveSelectedRepoFromOrgVariableResult {
        val response = client.delete("/orgs/$org/actions/variables/$name/repositories/$repositoryId")
        return when (response.status) {
            HttpStatusCode.NoContent -> Orgs.Actions.Variables.Repositories.ActionsRemoveSelectedRepoFromOrgVariableResult.NoContent
            HttpStatusCode.Conflict -> Orgs.Actions.Variables.Repositories.ActionsRemoveSelectedRepoFromOrgVariableResult.Conflict
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsArtifacts(private val client: HttpClient) : Orgs.Artifacts {
    override val metadata: Orgs.Artifacts.Metadata = KtorOrgsArtifactsMetadata(client)
}

internal class KtorOrgsArtifactsMetadata(private val client: HttpClient) : Orgs.Artifacts.Metadata {
    override val deploymentRecord: Orgs.Artifacts.Metadata.DeploymentRecord = KtorOrgsArtifactsMetadataDeploymentRecord(client)

    override val storageRecord: Orgs.Artifacts.Metadata.StorageRecord = KtorOrgsArtifactsMetadataStorageRecord(client)

    override val deploymentRecords: Orgs.Artifacts.Metadata.DeploymentRecords = KtorOrgsArtifactsMetadataDeploymentRecords(client)

    override val storageRecords: Orgs.Artifacts.Metadata.StorageRecords = KtorOrgsArtifactsMetadataStorageRecords(client)
}

internal class KtorOrgsArtifactsMetadataDeploymentRecord(private val client: HttpClient) : Orgs.Artifacts.Metadata.DeploymentRecord {
    override val cluster: Orgs.Artifacts.Metadata.DeploymentRecord.Cluster = KtorOrgsArtifactsMetadataDeploymentRecordCluster(client)

    override suspend fun orgsCreateArtifactDeploymentRecord(org: String, body: Orgs.Artifacts.Metadata.DeploymentRecord.OrgsCreateArtifactDeploymentRecordBody): Orgs.Artifacts.Metadata.DeploymentRecord.OrgsCreateArtifactDeploymentRecordResponse =
        client.post("/orgs/$org/artifacts/metadata/deployment-record") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()
}

internal class KtorOrgsArtifactsMetadataDeploymentRecordCluster(private val client: HttpClient) : Orgs.Artifacts.Metadata.DeploymentRecord.Cluster {
    override suspend fun orgsSetClusterDeploymentRecords(org: String, cluster: String, body: Orgs.Artifacts.Metadata.DeploymentRecord.Cluster.OrgsSetClusterDeploymentRecordsBody): Orgs.Artifacts.Metadata.DeploymentRecord.Cluster.OrgsSetClusterDeploymentRecordsResponse =
        client.post("/orgs/$org/artifacts/metadata/deployment-record/cluster/$cluster") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()
}

internal class KtorOrgsArtifactsMetadataStorageRecord(private val client: HttpClient) : Orgs.Artifacts.Metadata.StorageRecord {
    override suspend fun orgsCreateArtifactStorageRecord(org: String, body: Orgs.Artifacts.Metadata.StorageRecord.OrgsCreateArtifactStorageRecordBody): Orgs.Artifacts.Metadata.StorageRecord.OrgsCreateArtifactStorageRecordResponse =
        client.post("/orgs/$org/artifacts/metadata/storage-record") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()
}

internal class KtorOrgsArtifactsMetadataDeploymentRecords(private val client: HttpClient) : Orgs.Artifacts.Metadata.DeploymentRecords {
    override suspend fun orgsListArtifactDeploymentRecords(org: String, subjectDigest: String): Orgs.Artifacts.Metadata.DeploymentRecords.OrgsListArtifactDeploymentRecordsResponse =
        client.get("/orgs/$org/artifacts/$subjectDigest/metadata/deployment-records").body()
}

internal class KtorOrgsArtifactsMetadataStorageRecords(private val client: HttpClient) : Orgs.Artifacts.Metadata.StorageRecords {
    override suspend fun orgsListArtifactStorageRecords(org: String, subjectDigest: String): Orgs.Artifacts.Metadata.StorageRecords.OrgsListArtifactStorageRecordsResponse =
        client.get("/orgs/$org/artifacts/$subjectDigest/metadata/storage-records").body()
}

internal class KtorOrgsAttestations(private val client: HttpClient) : Orgs.Attestations {
    override val bulkList: Orgs.Attestations.BulkList = KtorOrgsAttestationsBulkList(client)

    override val deleteRequest: Orgs.Attestations.DeleteRequest = KtorOrgsAttestationsDeleteRequest(client)

    override val digest: Orgs.Attestations.Digest = KtorOrgsAttestationsDigest(client)

    override val repositories: Orgs.Attestations.Repositories = KtorOrgsAttestationsRepositories(client)

    override suspend fun orgsDeleteAttestationsById(org: String, attestationId: Long): Orgs.Attestations.OrgsDeleteAttestationsByIdResult {
        val response = client.delete("/orgs/$org/attestations/$attestationId")
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Attestations.OrgsDeleteAttestationsByIdResult.OK
            HttpStatusCode.NoContent -> Orgs.Attestations.OrgsDeleteAttestationsByIdResult.NoContent
            HttpStatusCode.Forbidden -> Orgs.Attestations.OrgsDeleteAttestationsByIdResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.Attestations.OrgsDeleteAttestationsByIdResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun orgsListAttestations(org: String, subjectDigest: String, perPage: Long, after: String?, before: String?, predicateType: String?): Orgs.Attestations.OrgsListAttestationsResponse =
        client.get("/orgs/$org/attestations/$subjectDigest") {
            parameter("per_page", perPage)
            after?.let { parameter("after", it) }
            before?.let { parameter("before", it) }
            predicateType?.let { parameter("predicate_type", it) }
        }.body()
}

internal class KtorOrgsAttestationsBulkList(private val client: HttpClient) : Orgs.Attestations.BulkList {
    override suspend fun orgsListAttestationsBulk(org: String, perPage: Long, body: Orgs.Attestations.BulkList.OrgsListAttestationsBulkBody, after: String?, before: String?): Orgs.Attestations.BulkList.OrgsListAttestationsBulkResponse =
        client.post("/orgs/$org/attestations/bulk-list") {
            parameter("per_page", perPage)
            after?.let { parameter("after", it) }
            before?.let { parameter("before", it) }
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()
}

internal class KtorOrgsAttestationsDeleteRequest(private val client: HttpClient) : Orgs.Attestations.DeleteRequest {
    override suspend fun orgsDeleteAttestationsBulk(org: String, body: JsonElement): Orgs.Attestations.DeleteRequest.OrgsDeleteAttestationsBulkResult {
        val response = client.post("/orgs/$org/attestations/delete-request") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Attestations.DeleteRequest.OrgsDeleteAttestationsBulkResult.OK
            HttpStatusCode.NotFound -> Orgs.Attestations.DeleteRequest.OrgsDeleteAttestationsBulkResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsAttestationsDigest(private val client: HttpClient) : Orgs.Attestations.Digest {
    override suspend fun orgsDeleteAttestationsBySubjectDigest(org: String, subjectDigest: String): Orgs.Attestations.Digest.OrgsDeleteAttestationsBySubjectDigestResult {
        val response = client.delete("/orgs/$org/attestations/digest/$subjectDigest")
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Attestations.Digest.OrgsDeleteAttestationsBySubjectDigestResult.OK
            HttpStatusCode.NoContent -> Orgs.Attestations.Digest.OrgsDeleteAttestationsBySubjectDigestResult.NoContent
            HttpStatusCode.NotFound -> Orgs.Attestations.Digest.OrgsDeleteAttestationsBySubjectDigestResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsAttestationsRepositories(private val client: HttpClient) : Orgs.Attestations.Repositories {
    override suspend fun orgsListAttestationRepositories(org: String, perPage: Long, after: String?, before: String?, predicateType: String?): List<Orgs.Attestations.Repositories.OrgsListAttestationRepositoriesResponse> =
        client.get("/orgs/$org/attestations/repositories") {
            parameter("per_page", perPage)
            after?.let { parameter("after", it) }
            before?.let { parameter("before", it) }
            predicateType?.let { parameter("predicate_type", it) }
        }.body()
}

internal class KtorOrgsBlocks(private val client: HttpClient) : Orgs.Blocks {
    override suspend fun orgsListBlockedUsers(org: String, page: Long, perPage: Long): List<SimpleUser> =
        client.get("/orgs/$org/blocks") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()

    override suspend fun orgsCheckBlockedUser(org: String, username: String): Orgs.Blocks.OrgsCheckBlockedUserResult {
        val response = client.get("/orgs/$org/blocks/$username")
        return when (response.status) {
            HttpStatusCode.NoContent -> Orgs.Blocks.OrgsCheckBlockedUserResult.NoContent
            HttpStatusCode.NotFound -> Orgs.Blocks.OrgsCheckBlockedUserResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun orgsBlockUser(org: String, username: String): Orgs.Blocks.OrgsBlockUserResult {
        val response = client.put("/orgs/$org/blocks/$username")
        return when (response.status) {
            HttpStatusCode.NoContent -> Orgs.Blocks.OrgsBlockUserResult.NoContent
            HttpStatusCode.UnprocessableEntity -> Orgs.Blocks.OrgsBlockUserResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun orgsUnblockUser(org: String, username: String): Unit =
        client.delete("/orgs/$org/blocks/$username").body()
}

internal class KtorOrgsCampaigns(private val client: HttpClient) : Orgs.Campaigns {
    override suspend fun campaignsListOrgCampaigns(org: String, direction: Orgs.Campaigns.Direction, page: Long, perPage: Long, sort: Orgs.Campaigns.Sort, state: CampaignState?): Orgs.Campaigns.CampaignsListOrgCampaignsResult {
        val response = client.get("/orgs/$org/campaigns") {
            parameter("direction", direction)
            parameter("page", page)
            parameter("per_page", perPage)
            parameter("sort", sort)
            state?.let { parameter("state", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Campaigns.CampaignsListOrgCampaignsResult.OK(response.body())
            HttpStatusCode.NotFound -> Orgs.Campaigns.CampaignsListOrgCampaignsResult.NotFound(response.body())
            HttpStatusCode.ServiceUnavailable -> Orgs.Campaigns.CampaignsListOrgCampaignsResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun campaignsCreateCampaign(org: String, body: Orgs.Campaigns.CampaignsCreateCampaignBody): Orgs.Campaigns.CampaignsCreateCampaignResult {
        val response = client.post("/orgs/$org/campaigns") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Campaigns.CampaignsCreateCampaignResult.OK(response.body())
            HttpStatusCode.BadRequest -> Orgs.Campaigns.CampaignsCreateCampaignResult.BadRequest(response.body())
            HttpStatusCode.NotFound -> Orgs.Campaigns.CampaignsCreateCampaignResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.Campaigns.CampaignsCreateCampaignResult.UnprocessableEntity(response.body())
            HttpStatusCode.TooManyRequests -> Orgs.Campaigns.CampaignsCreateCampaignResult.TooManyRequests
            HttpStatusCode.ServiceUnavailable -> Orgs.Campaigns.CampaignsCreateCampaignResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun campaignsGetCampaignSummary(org: String, campaignNumber: Long): Orgs.Campaigns.CampaignsGetCampaignSummaryResult {
        val response = client.get("/orgs/$org/campaigns/$campaignNumber")
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Campaigns.CampaignsGetCampaignSummaryResult.OK(response.body())
            HttpStatusCode.NotFound -> Orgs.Campaigns.CampaignsGetCampaignSummaryResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.Campaigns.CampaignsGetCampaignSummaryResult.UnprocessableEntity(response.body())
            HttpStatusCode.ServiceUnavailable -> Orgs.Campaigns.CampaignsGetCampaignSummaryResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun campaignsDeleteCampaign(org: String, campaignNumber: Long): Orgs.Campaigns.CampaignsDeleteCampaignResult {
        val response = client.delete("/orgs/$org/campaigns/$campaignNumber")
        return when (response.status) {
            HttpStatusCode.NoContent -> Orgs.Campaigns.CampaignsDeleteCampaignResult.NoContent
            HttpStatusCode.NotFound -> Orgs.Campaigns.CampaignsDeleteCampaignResult.NotFound(response.body())
            HttpStatusCode.ServiceUnavailable -> Orgs.Campaigns.CampaignsDeleteCampaignResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun campaignsUpdateCampaign(org: String, campaignNumber: Long, body: Orgs.Campaigns.CampaignsUpdateCampaignBody): Orgs.Campaigns.CampaignsUpdateCampaignResult {
        val response = client.patch("/orgs/$org/campaigns/$campaignNumber") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Campaigns.CampaignsUpdateCampaignResult.OK(response.body())
            HttpStatusCode.BadRequest -> Orgs.Campaigns.CampaignsUpdateCampaignResult.BadRequest(response.body())
            HttpStatusCode.NotFound -> Orgs.Campaigns.CampaignsUpdateCampaignResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.Campaigns.CampaignsUpdateCampaignResult.UnprocessableEntity(response.body())
            HttpStatusCode.ServiceUnavailable -> Orgs.Campaigns.CampaignsUpdateCampaignResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsCodeScanning(private val client: HttpClient) : Orgs.CodeScanning {
    override val alerts: Orgs.CodeScanning.Alerts = KtorOrgsCodeScanningAlerts(client)
}

internal class KtorOrgsCodeScanningAlerts(private val client: HttpClient) : Orgs.CodeScanning.Alerts {
    override suspend fun codeScanningListAlertsForOrg(org: String, direction: Orgs.CodeScanning.Alerts.Direction, page: Long, perPage: Long, sort: Orgs.CodeScanning.Alerts.Sort, after: String?, assignees: String?, before: String?, severity: CodeScanningAlertSeverity?, state: CodeScanningAlertStateQuery?, toolGuid: CodeScanningAnalysisToolGuid?, toolName: CodeScanningAnalysisToolName?): Orgs.CodeScanning.Alerts.CodeScanningListAlertsForOrgResult {
        val response = client.get("/orgs/$org/code-scanning/alerts") {
            parameter("direction", direction)
            parameter("page", page)
            parameter("per_page", perPage)
            parameter("sort", sort)
            after?.let { parameter("after", it) }
            assignees?.let { parameter("assignees", it) }
            before?.let { parameter("before", it) }
            severity?.let { parameter("severity", it) }
            state?.let { parameter("state", it) }
            toolGuid?.let { parameter("tool_guid", it) }
            toolName?.let { parameter("tool_name", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.CodeScanning.Alerts.CodeScanningListAlertsForOrgResult.OK(response.body())
            HttpStatusCode.NotFound -> Orgs.CodeScanning.Alerts.CodeScanningListAlertsForOrgResult.NotFound(response.body())
            HttpStatusCode.ServiceUnavailable -> Orgs.CodeScanning.Alerts.CodeScanningListAlertsForOrgResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsCodeSecurity(private val client: HttpClient) : Orgs.CodeSecurity {
    override val configurations: Orgs.CodeSecurity.Configurations = KtorOrgsCodeSecurityConfigurations(client)
}

internal class KtorOrgsCodeSecurityConfigurations(private val client: HttpClient) : Orgs.CodeSecurity.Configurations {
    override val defaults: Orgs.CodeSecurity.Configurations.Defaults = KtorOrgsCodeSecurityConfigurationsDefaults(client)

    override val detach: Orgs.CodeSecurity.Configurations.Detach = KtorOrgsCodeSecurityConfigurationsDetach(client)

    override val attach: Orgs.CodeSecurity.Configurations.Attach = KtorOrgsCodeSecurityConfigurationsAttach(client)

    override val repositories: Orgs.CodeSecurity.Configurations.Repositories = KtorOrgsCodeSecurityConfigurationsRepositories(client)

    override suspend fun codeSecurityGetConfigurationsForOrg(org: String, perPage: Long, targetType: Orgs.CodeSecurity.Configurations.TargetType, after: String?, before: String?): Orgs.CodeSecurity.Configurations.CodeSecurityGetConfigurationsForOrgResult {
        val response = client.get("/orgs/$org/code-security/configurations") {
            parameter("per_page", perPage)
            parameter("target_type", targetType)
            after?.let { parameter("after", it) }
            before?.let { parameter("before", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.CodeSecurity.Configurations.CodeSecurityGetConfigurationsForOrgResult.OK(response.body())
            HttpStatusCode.Forbidden -> Orgs.CodeSecurity.Configurations.CodeSecurityGetConfigurationsForOrgResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.CodeSecurity.Configurations.CodeSecurityGetConfigurationsForOrgResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun codeSecurityCreateConfiguration(org: String, body: Orgs.CodeSecurity.Configurations.CodeSecurityCreateConfigurationBody): CodeSecurityConfiguration =
        client.post("/orgs/$org/code-security/configurations") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()

    override suspend fun codeSecurityGetConfiguration(org: String, configurationId: Long): Orgs.CodeSecurity.Configurations.CodeSecurityGetConfigurationResult {
        val response = client.get("/orgs/$org/code-security/configurations/$configurationId")
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.CodeSecurity.Configurations.CodeSecurityGetConfigurationResult.OK(response.body())
            HttpStatusCode.NotModified -> Orgs.CodeSecurity.Configurations.CodeSecurityGetConfigurationResult.NotModified
            HttpStatusCode.Forbidden -> Orgs.CodeSecurity.Configurations.CodeSecurityGetConfigurationResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.CodeSecurity.Configurations.CodeSecurityGetConfigurationResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun codeSecurityDeleteConfiguration(org: String, configurationId: Long): Orgs.CodeSecurity.Configurations.CodeSecurityDeleteConfigurationResult {
        val response = client.delete("/orgs/$org/code-security/configurations/$configurationId")
        return when (response.status) {
            HttpStatusCode.NoContent -> Orgs.CodeSecurity.Configurations.CodeSecurityDeleteConfigurationResult.NoContent
            HttpStatusCode.BadRequest -> Orgs.CodeSecurity.Configurations.CodeSecurityDeleteConfigurationResult.BadRequest(response.body())
            HttpStatusCode.Forbidden -> Orgs.CodeSecurity.Configurations.CodeSecurityDeleteConfigurationResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.CodeSecurity.Configurations.CodeSecurityDeleteConfigurationResult.NotFound(response.body())
            HttpStatusCode.Conflict -> Orgs.CodeSecurity.Configurations.CodeSecurityDeleteConfigurationResult.Conflict(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun codeSecurityUpdateConfiguration(org: String, configurationId: Long, body: Orgs.CodeSecurity.Configurations.CodeSecurityUpdateConfigurationBody): Orgs.CodeSecurity.Configurations.CodeSecurityUpdateConfigurationResult {
        val response = client.patch("/orgs/$org/code-security/configurations/$configurationId") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.CodeSecurity.Configurations.CodeSecurityUpdateConfigurationResult.OK(response.body())
            HttpStatusCode.NoContent -> Orgs.CodeSecurity.Configurations.CodeSecurityUpdateConfigurationResult.NoContent
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsCodeSecurityConfigurationsDefaults(private val client: HttpClient) : Orgs.CodeSecurity.Configurations.Defaults {
    override suspend fun codeSecurityGetDefaultConfigurations(org: String): Orgs.CodeSecurity.Configurations.Defaults.CodeSecurityGetDefaultConfigurationsResult {
        val response = client.get("/orgs/$org/code-security/configurations/defaults")
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.CodeSecurity.Configurations.Defaults.CodeSecurityGetDefaultConfigurationsResult.OK(response.body())
            HttpStatusCode.NotModified -> Orgs.CodeSecurity.Configurations.Defaults.CodeSecurityGetDefaultConfigurationsResult.NotModified
            HttpStatusCode.Forbidden -> Orgs.CodeSecurity.Configurations.Defaults.CodeSecurityGetDefaultConfigurationsResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.CodeSecurity.Configurations.Defaults.CodeSecurityGetDefaultConfigurationsResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun codeSecuritySetConfigurationAsDefault(org: String, configurationId: Long, body: Orgs.CodeSecurity.Configurations.Defaults.CodeSecuritySetConfigurationAsDefaultBody): Orgs.CodeSecurity.Configurations.Defaults.CodeSecuritySetConfigurationAsDefaultResult {
        val response = client.put("/orgs/$org/code-security/configurations/$configurationId/defaults") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.CodeSecurity.Configurations.Defaults.CodeSecuritySetConfigurationAsDefaultResult.OK(response.body())
            HttpStatusCode.Forbidden -> Orgs.CodeSecurity.Configurations.Defaults.CodeSecuritySetConfigurationAsDefaultResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.CodeSecurity.Configurations.Defaults.CodeSecuritySetConfigurationAsDefaultResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsCodeSecurityConfigurationsDetach(private val client: HttpClient) : Orgs.CodeSecurity.Configurations.Detach {
    override suspend fun codeSecurityDetachConfiguration(org: String, body: Orgs.CodeSecurity.Configurations.Detach.CodeSecurityDetachConfigurationBody): Orgs.CodeSecurity.Configurations.Detach.CodeSecurityDetachConfigurationResult {
        val response = client.delete("/orgs/$org/code-security/configurations/detach") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.NoContent -> Orgs.CodeSecurity.Configurations.Detach.CodeSecurityDetachConfigurationResult.NoContent
            HttpStatusCode.BadRequest -> Orgs.CodeSecurity.Configurations.Detach.CodeSecurityDetachConfigurationResult.BadRequest(response.body())
            HttpStatusCode.Forbidden -> Orgs.CodeSecurity.Configurations.Detach.CodeSecurityDetachConfigurationResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.CodeSecurity.Configurations.Detach.CodeSecurityDetachConfigurationResult.NotFound(response.body())
            HttpStatusCode.Conflict -> Orgs.CodeSecurity.Configurations.Detach.CodeSecurityDetachConfigurationResult.Conflict(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsCodeSecurityConfigurationsAttach(private val client: HttpClient) : Orgs.CodeSecurity.Configurations.Attach {
    override suspend fun codeSecurityAttachConfiguration(org: String, configurationId: Long, body: Orgs.CodeSecurity.Configurations.Attach.CodeSecurityAttachConfigurationBody): JsonElement =
        client.post("/orgs/$org/code-security/configurations/$configurationId/attach") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()
}

internal class KtorOrgsCodeSecurityConfigurationsRepositories(private val client: HttpClient) : Orgs.CodeSecurity.Configurations.Repositories {
    override suspend fun codeSecurityGetRepositoriesForConfiguration(org: String, configurationId: Long, perPage: Long, status: String, after: String?, before: String?): Orgs.CodeSecurity.Configurations.Repositories.CodeSecurityGetRepositoriesForConfigurationResult {
        val response = client.get("/orgs/$org/code-security/configurations/$configurationId/repositories") {
            parameter("per_page", perPage)
            parameter("status", status)
            after?.let { parameter("after", it) }
            before?.let { parameter("before", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.CodeSecurity.Configurations.Repositories.CodeSecurityGetRepositoriesForConfigurationResult.OK(response.body())
            HttpStatusCode.Forbidden -> Orgs.CodeSecurity.Configurations.Repositories.CodeSecurityGetRepositoriesForConfigurationResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.CodeSecurity.Configurations.Repositories.CodeSecurityGetRepositoriesForConfigurationResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsCodespaces(private val client: HttpClient) : Orgs.Codespaces {
    override val access: Orgs.Codespaces.Access = KtorOrgsCodespacesAccess(client)

    override val secrets: Orgs.Codespaces.Secrets = KtorOrgsCodespacesSecrets(client)

    override suspend fun codespacesListInOrganization(org: String, page: Long, perPage: Long): Orgs.Codespaces.CodespacesListInOrganizationResult {
        val response = client.get("/orgs/$org/codespaces") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Codespaces.CodespacesListInOrganizationResult.OK(response.body())
            HttpStatusCode.NotModified -> Orgs.Codespaces.CodespacesListInOrganizationResult.NotModified
            HttpStatusCode.Unauthorized -> Orgs.Codespaces.CodespacesListInOrganizationResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Orgs.Codespaces.CodespacesListInOrganizationResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.Codespaces.CodespacesListInOrganizationResult.NotFound(response.body())
            HttpStatusCode.InternalServerError -> Orgs.Codespaces.CodespacesListInOrganizationResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsCodespacesAccess(private val client: HttpClient) : Orgs.Codespaces.Access {
    override val selectedUsers: Orgs.Codespaces.Access.SelectedUsers = KtorOrgsCodespacesAccessSelectedUsers(client)

    @Deprecated("Deprecated by the API provider")
    override suspend fun codespacesSetCodespacesAccess(org: String, body: Orgs.Codespaces.Access.CodespacesSetCodespacesAccessBody): Orgs.Codespaces.Access.CodespacesSetCodespacesAccessResult {
        val response = client.put("/orgs/$org/codespaces/access") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.NoContent -> Orgs.Codespaces.Access.CodespacesSetCodespacesAccessResult.NoContent
            HttpStatusCode.NotModified -> Orgs.Codespaces.Access.CodespacesSetCodespacesAccessResult.NotModified
            HttpStatusCode.BadRequest -> Orgs.Codespaces.Access.CodespacesSetCodespacesAccessResult.BadRequest
            HttpStatusCode.NotFound -> Orgs.Codespaces.Access.CodespacesSetCodespacesAccessResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.Codespaces.Access.CodespacesSetCodespacesAccessResult.UnprocessableEntity(response.body())
            HttpStatusCode.InternalServerError -> Orgs.Codespaces.Access.CodespacesSetCodespacesAccessResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsCodespacesAccessSelectedUsers(private val client: HttpClient) : Orgs.Codespaces.Access.SelectedUsers {
    @Deprecated("Deprecated by the API provider")
    override suspend fun codespacesSetCodespacesAccessUsers(org: String, body: Orgs.Codespaces.Access.SelectedUsers.CodespacesSetCodespacesAccessUsersBody): Orgs.Codespaces.Access.SelectedUsers.CodespacesSetCodespacesAccessUsersResult {
        val response = client.post("/orgs/$org/codespaces/access/selected_users") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.NoContent -> Orgs.Codespaces.Access.SelectedUsers.CodespacesSetCodespacesAccessUsersResult.NoContent
            HttpStatusCode.NotModified -> Orgs.Codespaces.Access.SelectedUsers.CodespacesSetCodespacesAccessUsersResult.NotModified
            HttpStatusCode.BadRequest -> Orgs.Codespaces.Access.SelectedUsers.CodespacesSetCodespacesAccessUsersResult.BadRequest
            HttpStatusCode.NotFound -> Orgs.Codespaces.Access.SelectedUsers.CodespacesSetCodespacesAccessUsersResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.Codespaces.Access.SelectedUsers.CodespacesSetCodespacesAccessUsersResult.UnprocessableEntity(response.body())
            HttpStatusCode.InternalServerError -> Orgs.Codespaces.Access.SelectedUsers.CodespacesSetCodespacesAccessUsersResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    @Deprecated("Deprecated by the API provider")
    override suspend fun codespacesDeleteCodespacesAccessUsers(org: String, body: Orgs.Codespaces.Access.SelectedUsers.CodespacesDeleteCodespacesAccessUsersBody): Orgs.Codespaces.Access.SelectedUsers.CodespacesDeleteCodespacesAccessUsersResult {
        val response = client.delete("/orgs/$org/codespaces/access/selected_users") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.NoContent -> Orgs.Codespaces.Access.SelectedUsers.CodespacesDeleteCodespacesAccessUsersResult.NoContent
            HttpStatusCode.NotModified -> Orgs.Codespaces.Access.SelectedUsers.CodespacesDeleteCodespacesAccessUsersResult.NotModified
            HttpStatusCode.BadRequest -> Orgs.Codespaces.Access.SelectedUsers.CodespacesDeleteCodespacesAccessUsersResult.BadRequest
            HttpStatusCode.NotFound -> Orgs.Codespaces.Access.SelectedUsers.CodespacesDeleteCodespacesAccessUsersResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.Codespaces.Access.SelectedUsers.CodespacesDeleteCodespacesAccessUsersResult.UnprocessableEntity(response.body())
            HttpStatusCode.InternalServerError -> Orgs.Codespaces.Access.SelectedUsers.CodespacesDeleteCodespacesAccessUsersResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsCodespacesSecrets(private val client: HttpClient) : Orgs.Codespaces.Secrets {
    override val publicKey: Orgs.Codespaces.Secrets.PublicKey = KtorOrgsCodespacesSecretsPublicKey(client)

    override val repositories: Orgs.Codespaces.Secrets.Repositories = KtorOrgsCodespacesSecretsRepositories(client)

    override suspend fun codespacesListOrgSecrets(org: String, page: Long, perPage: Long): Orgs.Codespaces.Secrets.CodespacesListOrgSecretsResponse =
        client.get("/orgs/$org/codespaces/secrets") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()

    override suspend fun codespacesGetOrgSecret(org: String, secretName: String): CodespacesOrgSecret =
        client.get("/orgs/$org/codespaces/secrets/$secretName").body()

    override suspend fun codespacesCreateOrUpdateOrgSecret(org: String, secretName: String, body: Orgs.Codespaces.Secrets.CodespacesCreateOrUpdateOrgSecretBody): Orgs.Codespaces.Secrets.CodespacesCreateOrUpdateOrgSecretResult {
        val response = client.put("/orgs/$org/codespaces/secrets/$secretName") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> Orgs.Codespaces.Secrets.CodespacesCreateOrUpdateOrgSecretResult.Created(response.body())
            HttpStatusCode.NoContent -> Orgs.Codespaces.Secrets.CodespacesCreateOrUpdateOrgSecretResult.NoContent
            HttpStatusCode.NotFound -> Orgs.Codespaces.Secrets.CodespacesCreateOrUpdateOrgSecretResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.Codespaces.Secrets.CodespacesCreateOrUpdateOrgSecretResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun codespacesDeleteOrgSecret(org: String, secretName: String): Orgs.Codespaces.Secrets.CodespacesDeleteOrgSecretResult {
        val response = client.delete("/orgs/$org/codespaces/secrets/$secretName")
        return when (response.status) {
            HttpStatusCode.NoContent -> Orgs.Codespaces.Secrets.CodespacesDeleteOrgSecretResult.NoContent
            HttpStatusCode.NotFound -> Orgs.Codespaces.Secrets.CodespacesDeleteOrgSecretResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsCodespacesSecretsPublicKey(private val client: HttpClient) : Orgs.Codespaces.Secrets.PublicKey {
    override suspend fun codespacesGetOrgPublicKey(org: String): CodespacesPublicKey =
        client.get("/orgs/$org/codespaces/secrets/public-key").body()
}

internal class KtorOrgsCodespacesSecretsRepositories(private val client: HttpClient) : Orgs.Codespaces.Secrets.Repositories {
    override suspend fun codespacesListSelectedReposForOrgSecret(org: String, secretName: String, page: Long, perPage: Long): Orgs.Codespaces.Secrets.Repositories.CodespacesListSelectedReposForOrgSecretResult {
        val response = client.get("/orgs/$org/codespaces/secrets/$secretName/repositories") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Codespaces.Secrets.Repositories.CodespacesListSelectedReposForOrgSecretResult.OK(response.body())
            HttpStatusCode.NotFound -> Orgs.Codespaces.Secrets.Repositories.CodespacesListSelectedReposForOrgSecretResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun codespacesSetSelectedReposForOrgSecret(org: String, secretName: String, body: Orgs.Codespaces.Secrets.Repositories.CodespacesSetSelectedReposForOrgSecretBody): Orgs.Codespaces.Secrets.Repositories.CodespacesSetSelectedReposForOrgSecretResult {
        val response = client.put("/orgs/$org/codespaces/secrets/$secretName/repositories") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.NoContent -> Orgs.Codespaces.Secrets.Repositories.CodespacesSetSelectedReposForOrgSecretResult.NoContent
            HttpStatusCode.NotFound -> Orgs.Codespaces.Secrets.Repositories.CodespacesSetSelectedReposForOrgSecretResult.NotFound(response.body())
            HttpStatusCode.Conflict -> Orgs.Codespaces.Secrets.Repositories.CodespacesSetSelectedReposForOrgSecretResult.Conflict
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun codespacesAddSelectedRepoToOrgSecret(org: String, secretName: String, repositoryId: Long): Orgs.Codespaces.Secrets.Repositories.CodespacesAddSelectedRepoToOrgSecretResult {
        val response = client.put("/orgs/$org/codespaces/secrets/$secretName/repositories/$repositoryId")
        return when (response.status) {
            HttpStatusCode.NoContent -> Orgs.Codespaces.Secrets.Repositories.CodespacesAddSelectedRepoToOrgSecretResult.NoContent
            HttpStatusCode.NotFound -> Orgs.Codespaces.Secrets.Repositories.CodespacesAddSelectedRepoToOrgSecretResult.NotFound(response.body())
            HttpStatusCode.Conflict -> Orgs.Codespaces.Secrets.Repositories.CodespacesAddSelectedRepoToOrgSecretResult.Conflict
            HttpStatusCode.UnprocessableEntity -> Orgs.Codespaces.Secrets.Repositories.CodespacesAddSelectedRepoToOrgSecretResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun codespacesRemoveSelectedRepoFromOrgSecret(org: String, secretName: String, repositoryId: Long): Orgs.Codespaces.Secrets.Repositories.CodespacesRemoveSelectedRepoFromOrgSecretResult {
        val response = client.delete("/orgs/$org/codespaces/secrets/$secretName/repositories/$repositoryId")
        return when (response.status) {
            HttpStatusCode.NoContent -> Orgs.Codespaces.Secrets.Repositories.CodespacesRemoveSelectedRepoFromOrgSecretResult.NoContent
            HttpStatusCode.NotFound -> Orgs.Codespaces.Secrets.Repositories.CodespacesRemoveSelectedRepoFromOrgSecretResult.NotFound(response.body())
            HttpStatusCode.Conflict -> Orgs.Codespaces.Secrets.Repositories.CodespacesRemoveSelectedRepoFromOrgSecretResult.Conflict
            HttpStatusCode.UnprocessableEntity -> Orgs.Codespaces.Secrets.Repositories.CodespacesRemoveSelectedRepoFromOrgSecretResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsCopilot(private val client: HttpClient) : Orgs.Copilot {
    override val billing: Orgs.Copilot.Billing = KtorOrgsCopilotBilling(client)

    override val contentExclusion: Orgs.Copilot.ContentExclusion = KtorOrgsCopilotContentExclusion(client)

    override val metrics: Orgs.Copilot.Metrics = KtorOrgsCopilotMetrics(client)
}

internal class KtorOrgsCopilotBilling(private val client: HttpClient) : Orgs.Copilot.Billing {
    override val seats: Orgs.Copilot.Billing.Seats = KtorOrgsCopilotBillingSeats(client)

    override val selectedTeams: Orgs.Copilot.Billing.SelectedTeams = KtorOrgsCopilotBillingSelectedTeams(client)

    override val selectedUsers: Orgs.Copilot.Billing.SelectedUsers = KtorOrgsCopilotBillingSelectedUsers(client)

    override suspend fun copilotGetCopilotOrganizationDetails(org: String): Orgs.Copilot.Billing.CopilotGetCopilotOrganizationDetailsResult {
        val response = client.get("/orgs/$org/copilot/billing")
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Copilot.Billing.CopilotGetCopilotOrganizationDetailsResult.OK(response.body())
            HttpStatusCode.Unauthorized -> Orgs.Copilot.Billing.CopilotGetCopilotOrganizationDetailsResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Orgs.Copilot.Billing.CopilotGetCopilotOrganizationDetailsResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.Copilot.Billing.CopilotGetCopilotOrganizationDetailsResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.Copilot.Billing.CopilotGetCopilotOrganizationDetailsResult.UnprocessableEntity
            HttpStatusCode.InternalServerError -> Orgs.Copilot.Billing.CopilotGetCopilotOrganizationDetailsResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsCopilotBillingSeats(private val client: HttpClient) : Orgs.Copilot.Billing.Seats {
    override suspend fun copilotListCopilotSeats(org: String, page: Long, perPage: Long): Orgs.Copilot.Billing.Seats.CopilotListCopilotSeatsResult {
        val response = client.get("/orgs/$org/copilot/billing/seats") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Copilot.Billing.Seats.CopilotListCopilotSeatsResult.OK(response.body())
            HttpStatusCode.Unauthorized -> Orgs.Copilot.Billing.Seats.CopilotListCopilotSeatsResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Orgs.Copilot.Billing.Seats.CopilotListCopilotSeatsResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.Copilot.Billing.Seats.CopilotListCopilotSeatsResult.NotFound(response.body())
            HttpStatusCode.InternalServerError -> Orgs.Copilot.Billing.Seats.CopilotListCopilotSeatsResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsCopilotBillingSelectedTeams(private val client: HttpClient) : Orgs.Copilot.Billing.SelectedTeams {
    override suspend fun copilotAddCopilotSeatsForTeams(org: String, body: Orgs.Copilot.Billing.SelectedTeams.CopilotAddCopilotSeatsForTeamsBody): Orgs.Copilot.Billing.SelectedTeams.CopilotAddCopilotSeatsForTeamsResult {
        val response = client.post("/orgs/$org/copilot/billing/selected_teams") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> Orgs.Copilot.Billing.SelectedTeams.CopilotAddCopilotSeatsForTeamsResult.Created(response.body())
            HttpStatusCode.Unauthorized -> Orgs.Copilot.Billing.SelectedTeams.CopilotAddCopilotSeatsForTeamsResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Orgs.Copilot.Billing.SelectedTeams.CopilotAddCopilotSeatsForTeamsResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.Copilot.Billing.SelectedTeams.CopilotAddCopilotSeatsForTeamsResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.Copilot.Billing.SelectedTeams.CopilotAddCopilotSeatsForTeamsResult.UnprocessableEntity
            HttpStatusCode.InternalServerError -> Orgs.Copilot.Billing.SelectedTeams.CopilotAddCopilotSeatsForTeamsResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun copilotCancelCopilotSeatAssignmentForTeams(org: String, body: Orgs.Copilot.Billing.SelectedTeams.CopilotCancelCopilotSeatAssignmentForTeamsBody): Orgs.Copilot.Billing.SelectedTeams.CopilotCancelCopilotSeatAssignmentForTeamsResult {
        val response = client.delete("/orgs/$org/copilot/billing/selected_teams") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Copilot.Billing.SelectedTeams.CopilotCancelCopilotSeatAssignmentForTeamsResult.OK(response.body())
            HttpStatusCode.Unauthorized -> Orgs.Copilot.Billing.SelectedTeams.CopilotCancelCopilotSeatAssignmentForTeamsResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Orgs.Copilot.Billing.SelectedTeams.CopilotCancelCopilotSeatAssignmentForTeamsResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.Copilot.Billing.SelectedTeams.CopilotCancelCopilotSeatAssignmentForTeamsResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.Copilot.Billing.SelectedTeams.CopilotCancelCopilotSeatAssignmentForTeamsResult.UnprocessableEntity
            HttpStatusCode.InternalServerError -> Orgs.Copilot.Billing.SelectedTeams.CopilotCancelCopilotSeatAssignmentForTeamsResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsCopilotBillingSelectedUsers(private val client: HttpClient) : Orgs.Copilot.Billing.SelectedUsers {
    override suspend fun copilotAddCopilotSeatsForUsers(org: String, body: Orgs.Copilot.Billing.SelectedUsers.CopilotAddCopilotSeatsForUsersBody): Orgs.Copilot.Billing.SelectedUsers.CopilotAddCopilotSeatsForUsersResult {
        val response = client.post("/orgs/$org/copilot/billing/selected_users") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> Orgs.Copilot.Billing.SelectedUsers.CopilotAddCopilotSeatsForUsersResult.Created(response.body())
            HttpStatusCode.Unauthorized -> Orgs.Copilot.Billing.SelectedUsers.CopilotAddCopilotSeatsForUsersResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Orgs.Copilot.Billing.SelectedUsers.CopilotAddCopilotSeatsForUsersResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.Copilot.Billing.SelectedUsers.CopilotAddCopilotSeatsForUsersResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.Copilot.Billing.SelectedUsers.CopilotAddCopilotSeatsForUsersResult.UnprocessableEntity
            HttpStatusCode.InternalServerError -> Orgs.Copilot.Billing.SelectedUsers.CopilotAddCopilotSeatsForUsersResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun copilotCancelCopilotSeatAssignmentForUsers(org: String, body: Orgs.Copilot.Billing.SelectedUsers.CopilotCancelCopilotSeatAssignmentForUsersBody): Orgs.Copilot.Billing.SelectedUsers.CopilotCancelCopilotSeatAssignmentForUsersResult {
        val response = client.delete("/orgs/$org/copilot/billing/selected_users") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Copilot.Billing.SelectedUsers.CopilotCancelCopilotSeatAssignmentForUsersResult.OK(response.body())
            HttpStatusCode.Unauthorized -> Orgs.Copilot.Billing.SelectedUsers.CopilotCancelCopilotSeatAssignmentForUsersResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Orgs.Copilot.Billing.SelectedUsers.CopilotCancelCopilotSeatAssignmentForUsersResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.Copilot.Billing.SelectedUsers.CopilotCancelCopilotSeatAssignmentForUsersResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.Copilot.Billing.SelectedUsers.CopilotCancelCopilotSeatAssignmentForUsersResult.UnprocessableEntity
            HttpStatusCode.InternalServerError -> Orgs.Copilot.Billing.SelectedUsers.CopilotCancelCopilotSeatAssignmentForUsersResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsCopilotContentExclusion(private val client: HttpClient) : Orgs.Copilot.ContentExclusion {
    override suspend fun copilotCopilotContentExclusionForOrganization(org: String): Orgs.Copilot.ContentExclusion.CopilotCopilotContentExclusionForOrganizationResult {
        val response = client.get("/orgs/$org/copilot/content_exclusion")
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Copilot.ContentExclusion.CopilotCopilotContentExclusionForOrganizationResult.OK(response.body())
            HttpStatusCode.Unauthorized -> Orgs.Copilot.ContentExclusion.CopilotCopilotContentExclusionForOrganizationResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Orgs.Copilot.ContentExclusion.CopilotCopilotContentExclusionForOrganizationResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.Copilot.ContentExclusion.CopilotCopilotContentExclusionForOrganizationResult.NotFound(response.body())
            HttpStatusCode.InternalServerError -> Orgs.Copilot.ContentExclusion.CopilotCopilotContentExclusionForOrganizationResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun copilotSetCopilotContentExclusionForOrganization(org: String, body: List<List<Orgs.Copilot.ContentExclusion.CopilotSetCopilotContentExclusionForOrganizationBody>>): Orgs.Copilot.ContentExclusion.CopilotSetCopilotContentExclusionForOrganizationResult {
        val response = client.put("/orgs/$org/copilot/content_exclusion") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Copilot.ContentExclusion.CopilotSetCopilotContentExclusionForOrganizationResult.OK(response.body())
            HttpStatusCode.Unauthorized -> Orgs.Copilot.ContentExclusion.CopilotSetCopilotContentExclusionForOrganizationResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Orgs.Copilot.ContentExclusion.CopilotSetCopilotContentExclusionForOrganizationResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.Copilot.ContentExclusion.CopilotSetCopilotContentExclusionForOrganizationResult.NotFound(response.body())
            HttpStatusCode.PayloadTooLarge -> Orgs.Copilot.ContentExclusion.CopilotSetCopilotContentExclusionForOrganizationResult.PayloadTooLarge(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.Copilot.ContentExclusion.CopilotSetCopilotContentExclusionForOrganizationResult.UnprocessableEntity(response.body())
            HttpStatusCode.InternalServerError -> Orgs.Copilot.ContentExclusion.CopilotSetCopilotContentExclusionForOrganizationResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsCopilotMetrics(private val client: HttpClient) : Orgs.Copilot.Metrics {
    override suspend fun copilotCopilotMetricsForOrganization(org: String, page: Long, perPage: Long, since: String?, until: String?): Orgs.Copilot.Metrics.CopilotCopilotMetricsForOrganizationResult {
        val response = client.get("/orgs/$org/copilot/metrics") {
            parameter("page", page)
            parameter("per_page", perPage)
            since?.let { parameter("since", it) }
            until?.let { parameter("until", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Copilot.Metrics.CopilotCopilotMetricsForOrganizationResult.OK(response.body())
            HttpStatusCode.Forbidden -> Orgs.Copilot.Metrics.CopilotCopilotMetricsForOrganizationResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.Copilot.Metrics.CopilotCopilotMetricsForOrganizationResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.Copilot.Metrics.CopilotCopilotMetricsForOrganizationResult.UnprocessableEntity(response.body())
            HttpStatusCode.InternalServerError -> Orgs.Copilot.Metrics.CopilotCopilotMetricsForOrganizationResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsDependabot(private val client: HttpClient) : Orgs.Dependabot {
    override val alerts: Orgs.Dependabot.Alerts = KtorOrgsDependabotAlerts(client)

    override val secrets: Orgs.Dependabot.Secrets = KtorOrgsDependabotSecrets(client)
}

internal class KtorOrgsDependabotAlerts(private val client: HttpClient) : Orgs.Dependabot.Alerts {
    override suspend fun dependabotListAlertsForOrg(org: String, direction: Orgs.Dependabot.Alerts.Direction, perPage: Long, sort: Orgs.Dependabot.Alerts.Sort, after: String?, artifactRegistry: String?, artifactRegistryUrl: String?, assignee: String?, before: String?, ecosystem: String?, epssPercentage: String?, has: Orgs.Dependabot.Alerts.Has?, `package`: String?, runtimeRisk: String?, scope: Orgs.Dependabot.Alerts.Scope?, severity: String?, state: String?): Orgs.Dependabot.Alerts.DependabotListAlertsForOrgResult {
        val response = client.get("/orgs/$org/dependabot/alerts") {
            parameter("direction", direction)
            parameter("per_page", perPage)
            parameter("sort", sort)
            after?.let { parameter("after", it) }
            artifactRegistry?.let { parameter("artifact_registry", it) }
            artifactRegistryUrl?.let { parameter("artifact_registry_url", it) }
            assignee?.let { parameter("assignee", it) }
            before?.let { parameter("before", it) }
            ecosystem?.let { parameter("ecosystem", it) }
            epssPercentage?.let { parameter("epss_percentage", it) }
            has?.let { parameter("has", it) }
            `package`?.let { parameter("package", it) }
            runtimeRisk?.let { parameter("runtime_risk", it) }
            scope?.let { parameter("scope", it) }
            severity?.let { parameter("severity", it) }
            state?.let { parameter("state", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Dependabot.Alerts.DependabotListAlertsForOrgResult.OK(response.body())
            HttpStatusCode.NotModified -> Orgs.Dependabot.Alerts.DependabotListAlertsForOrgResult.NotModified
            HttpStatusCode.BadRequest -> Orgs.Dependabot.Alerts.DependabotListAlertsForOrgResult.BadRequest(response.body())
            HttpStatusCode.Forbidden -> Orgs.Dependabot.Alerts.DependabotListAlertsForOrgResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.Dependabot.Alerts.DependabotListAlertsForOrgResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.Dependabot.Alerts.DependabotListAlertsForOrgResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsDependabotSecrets(private val client: HttpClient) : Orgs.Dependabot.Secrets {
    override val publicKey: Orgs.Dependabot.Secrets.PublicKey = KtorOrgsDependabotSecretsPublicKey(client)

    override val repositories: Orgs.Dependabot.Secrets.Repositories = KtorOrgsDependabotSecretsRepositories(client)

    override suspend fun dependabotListOrgSecrets(org: String, page: Long, perPage: Long): Orgs.Dependabot.Secrets.DependabotListOrgSecretsResponse =
        client.get("/orgs/$org/dependabot/secrets") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()

    override suspend fun dependabotGetOrgSecret(org: String, secretName: String): OrganizationDependabotSecret =
        client.get("/orgs/$org/dependabot/secrets/$secretName").body()

    override suspend fun dependabotCreateOrUpdateOrgSecret(org: String, secretName: String, body: Orgs.Dependabot.Secrets.DependabotCreateOrUpdateOrgSecretBody): Orgs.Dependabot.Secrets.DependabotCreateOrUpdateOrgSecretResult {
        val response = client.put("/orgs/$org/dependabot/secrets/$secretName") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> Orgs.Dependabot.Secrets.DependabotCreateOrUpdateOrgSecretResult.Created(response.body())
            HttpStatusCode.NoContent -> Orgs.Dependabot.Secrets.DependabotCreateOrUpdateOrgSecretResult.NoContent
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun dependabotDeleteOrgSecret(org: String, secretName: String): Unit =
        client.delete("/orgs/$org/dependabot/secrets/$secretName").body()
}

internal class KtorOrgsDependabotSecretsPublicKey(private val client: HttpClient) : Orgs.Dependabot.Secrets.PublicKey {
    override suspend fun dependabotGetOrgPublicKey(org: String): DependabotPublicKey =
        client.get("/orgs/$org/dependabot/secrets/public-key").body()
}

internal class KtorOrgsDependabotSecretsRepositories(private val client: HttpClient) : Orgs.Dependabot.Secrets.Repositories {
    override suspend fun dependabotListSelectedReposForOrgSecret(org: String, secretName: String, page: Long, perPage: Long): Orgs.Dependabot.Secrets.Repositories.DependabotListSelectedReposForOrgSecretResponse =
        client.get("/orgs/$org/dependabot/secrets/$secretName/repositories") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()

    override suspend fun dependabotSetSelectedReposForOrgSecret(org: String, secretName: String, body: Orgs.Dependabot.Secrets.Repositories.DependabotSetSelectedReposForOrgSecretBody): Unit =
        client.put("/orgs/$org/dependabot/secrets/$secretName/repositories") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()

    override suspend fun dependabotAddSelectedRepoToOrgSecret(org: String, secretName: String, repositoryId: Long): Orgs.Dependabot.Secrets.Repositories.DependabotAddSelectedRepoToOrgSecretResult {
        val response = client.put("/orgs/$org/dependabot/secrets/$secretName/repositories/$repositoryId")
        return when (response.status) {
            HttpStatusCode.NoContent -> Orgs.Dependabot.Secrets.Repositories.DependabotAddSelectedRepoToOrgSecretResult.NoContent
            HttpStatusCode.Conflict -> Orgs.Dependabot.Secrets.Repositories.DependabotAddSelectedRepoToOrgSecretResult.Conflict
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun dependabotRemoveSelectedRepoFromOrgSecret(org: String, secretName: String, repositoryId: Long): Orgs.Dependabot.Secrets.Repositories.DependabotRemoveSelectedRepoFromOrgSecretResult {
        val response = client.delete("/orgs/$org/dependabot/secrets/$secretName/repositories/$repositoryId")
        return when (response.status) {
            HttpStatusCode.NoContent -> Orgs.Dependabot.Secrets.Repositories.DependabotRemoveSelectedRepoFromOrgSecretResult.NoContent
            HttpStatusCode.Conflict -> Orgs.Dependabot.Secrets.Repositories.DependabotRemoveSelectedRepoFromOrgSecretResult.Conflict
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsDocker(private val client: HttpClient) : Orgs.Docker {
    override val conflicts: Orgs.Docker.Conflicts = KtorOrgsDockerConflicts(client)
}

internal class KtorOrgsDockerConflicts(private val client: HttpClient) : Orgs.Docker.Conflicts {
    override suspend fun packagesListDockerMigrationConflictingPackagesForOrganization(org: String): Orgs.Docker.Conflicts.PackagesListDockerMigrationConflictingPackagesForOrganizationResult {
        val response = client.get("/orgs/$org/docker/conflicts")
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Docker.Conflicts.PackagesListDockerMigrationConflictingPackagesForOrganizationResult.OK(response.body())
            HttpStatusCode.Unauthorized -> Orgs.Docker.Conflicts.PackagesListDockerMigrationConflictingPackagesForOrganizationResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Orgs.Docker.Conflicts.PackagesListDockerMigrationConflictingPackagesForOrganizationResult.Forbidden(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsEvents(private val client: HttpClient) : Orgs.Events {
    override suspend fun activityListPublicOrgEvents(org: String, page: Long, perPage: Long): List<Event> =
        client.get("/orgs/$org/events") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()
}

internal class KtorOrgsFailedInvitations(private val client: HttpClient) : Orgs.FailedInvitations {
    override suspend fun orgsListFailedInvitations(org: String, page: Long, perPage: Long): Orgs.FailedInvitations.OrgsListFailedInvitationsResult {
        val response = client.get("/orgs/$org/failed_invitations") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.FailedInvitations.OrgsListFailedInvitationsResult.OK(response.body())
            HttpStatusCode.NotFound -> Orgs.FailedInvitations.OrgsListFailedInvitationsResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsHooks(private val client: HttpClient) : Orgs.Hooks {
    override val config: Orgs.Hooks.Config = KtorOrgsHooksConfig(client)

    override val deliveries: Orgs.Hooks.Deliveries = KtorOrgsHooksDeliveries(client)

    override val pings: Orgs.Hooks.Pings = KtorOrgsHooksPings(client)

    override suspend fun orgsListWebhooks(org: String, page: Long, perPage: Long): Orgs.Hooks.OrgsListWebhooksResult {
        val response = client.get("/orgs/$org/hooks") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Hooks.OrgsListWebhooksResult.OK(response.body())
            HttpStatusCode.NotFound -> Orgs.Hooks.OrgsListWebhooksResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun orgsCreateWebhook(org: String, body: Orgs.Hooks.OrgsCreateWebhookBody): Orgs.Hooks.OrgsCreateWebhookResult {
        val response = client.post("/orgs/$org/hooks") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> Orgs.Hooks.OrgsCreateWebhookResult.Created(response.body())
            HttpStatusCode.NotFound -> Orgs.Hooks.OrgsCreateWebhookResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.Hooks.OrgsCreateWebhookResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun orgsGetWebhook(org: String, hookId: Long): Orgs.Hooks.OrgsGetWebhookResult {
        val response = client.get("/orgs/$org/hooks/$hookId")
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Hooks.OrgsGetWebhookResult.OK(response.body())
            HttpStatusCode.NotFound -> Orgs.Hooks.OrgsGetWebhookResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun orgsDeleteWebhook(org: String, hookId: Long): Orgs.Hooks.OrgsDeleteWebhookResult {
        val response = client.delete("/orgs/$org/hooks/$hookId")
        return when (response.status) {
            HttpStatusCode.NoContent -> Orgs.Hooks.OrgsDeleteWebhookResult.NoContent
            HttpStatusCode.NotFound -> Orgs.Hooks.OrgsDeleteWebhookResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun orgsUpdateWebhook(org: String, hookId: Long, body: Orgs.Hooks.OrgsUpdateWebhookBody?): Orgs.Hooks.OrgsUpdateWebhookResult {
        val response = client.patch("/orgs/$org/hooks/$hookId") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Hooks.OrgsUpdateWebhookResult.OK(response.body())
            HttpStatusCode.NotFound -> Orgs.Hooks.OrgsUpdateWebhookResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.Hooks.OrgsUpdateWebhookResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsHooksConfig(private val client: HttpClient) : Orgs.Hooks.Config {
    override suspend fun orgsGetWebhookConfigForOrg(org: String, hookId: Long): WebhookConfig =
        client.get("/orgs/$org/hooks/$hookId/config").body()

    override suspend fun orgsUpdateWebhookConfigForOrg(org: String, hookId: Long, body: Orgs.Hooks.Config.OrgsUpdateWebhookConfigForOrgBody?): WebhookConfig =
        client.patch("/orgs/$org/hooks/$hookId/config") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }.body()
}

internal class KtorOrgsHooksDeliveries(private val client: HttpClient) : Orgs.Hooks.Deliveries {
    override val attempts: Orgs.Hooks.Deliveries.Attempts = KtorOrgsHooksDeliveriesAttempts(client)

    override suspend fun orgsListWebhookDeliveries(org: String, hookId: Long, perPage: Long, cursor: String?): Orgs.Hooks.Deliveries.OrgsListWebhookDeliveriesResult {
        val response = client.get("/orgs/$org/hooks/$hookId/deliveries") {
            parameter("per_page", perPage)
            cursor?.let { parameter("cursor", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Hooks.Deliveries.OrgsListWebhookDeliveriesResult.OK(response.body())
            HttpStatusCode.BadRequest -> Orgs.Hooks.Deliveries.OrgsListWebhookDeliveriesResult.BadRequest(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.Hooks.Deliveries.OrgsListWebhookDeliveriesResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun orgsGetWebhookDelivery(org: String, hookId: Long, deliveryId: Long): Orgs.Hooks.Deliveries.OrgsGetWebhookDeliveryResult {
        val response = client.get("/orgs/$org/hooks/$hookId/deliveries/$deliveryId")
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Hooks.Deliveries.OrgsGetWebhookDeliveryResult.OK(response.body())
            HttpStatusCode.BadRequest -> Orgs.Hooks.Deliveries.OrgsGetWebhookDeliveryResult.BadRequest(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.Hooks.Deliveries.OrgsGetWebhookDeliveryResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsHooksDeliveriesAttempts(private val client: HttpClient) : Orgs.Hooks.Deliveries.Attempts {
    override suspend fun orgsRedeliverWebhookDelivery(org: String, hookId: Long, deliveryId: Long): Orgs.Hooks.Deliveries.Attempts.OrgsRedeliverWebhookDeliveryResult {
        val response = client.post("/orgs/$org/hooks/$hookId/deliveries/$deliveryId/attempts")
        return when (response.status) {
            HttpStatusCode.Accepted -> Orgs.Hooks.Deliveries.Attempts.OrgsRedeliverWebhookDeliveryResult.Accepted(response.body())
            HttpStatusCode.BadRequest -> Orgs.Hooks.Deliveries.Attempts.OrgsRedeliverWebhookDeliveryResult.BadRequest(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.Hooks.Deliveries.Attempts.OrgsRedeliverWebhookDeliveryResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsHooksPings(private val client: HttpClient) : Orgs.Hooks.Pings {
    override suspend fun orgsPingWebhook(org: String, hookId: Long): Orgs.Hooks.Pings.OrgsPingWebhookResult {
        val response = client.post("/orgs/$org/hooks/$hookId/pings")
        return when (response.status) {
            HttpStatusCode.NoContent -> Orgs.Hooks.Pings.OrgsPingWebhookResult.NoContent
            HttpStatusCode.NotFound -> Orgs.Hooks.Pings.OrgsPingWebhookResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsInsights(private val client: HttpClient) : Orgs.Insights {
    override val api: Orgs.Insights.Api = KtorOrgsInsightsApi(client)
}

internal class KtorOrgsInsightsApi(private val client: HttpClient) : Orgs.Insights.Api {
    override val routeStats: Orgs.Insights.Api.RouteStats = KtorOrgsInsightsApiRouteStats(client)

    override val subjectStats: Orgs.Insights.Api.SubjectStats = KtorOrgsInsightsApiSubjectStats(client)

    override val summaryStats: Orgs.Insights.Api.SummaryStats = KtorOrgsInsightsApiSummaryStats(client)

    override val timeStats: Orgs.Insights.Api.TimeStats = KtorOrgsInsightsApiTimeStats(client)

    override val userStats: Orgs.Insights.Api.UserStats = KtorOrgsInsightsApiUserStats(client)
}

internal class KtorOrgsInsightsApiRouteStats(private val client: HttpClient) : Orgs.Insights.Api.RouteStats {
    override suspend fun apiInsightsGetRouteStatsByActor(org: String, actorType: Orgs.Insights.Api.RouteStats.ActorType, actorId: Long, direction: Orgs.Insights.Api.RouteStats.Direction, minTimestamp: String, page: Long, perPage: Long, apiRouteSubstring: String?, maxTimestamp: String?, sort: List<Orgs.Insights.Api.RouteStats.Sort>?): ApiInsightsRouteStats =
        client.get("/orgs/$org/insights/api/route-stats/$actorType/$actorId") {
            parameter("direction", direction)
            parameter("min_timestamp", minTimestamp)
            parameter("page", page)
            parameter("per_page", perPage)
            apiRouteSubstring?.let { parameter("api_route_substring", it) }
            maxTimestamp?.let { parameter("max_timestamp", it) }
            sort?.let { parameter("sort", it) }
        }.body()
}

internal class KtorOrgsInsightsApiSubjectStats(private val client: HttpClient) : Orgs.Insights.Api.SubjectStats {
    override suspend fun apiInsightsGetSubjectStats(org: String, direction: Orgs.Insights.Api.SubjectStats.Direction, minTimestamp: String, page: Long, perPage: Long, maxTimestamp: String?, sort: List<Orgs.Insights.Api.SubjectStats.Sort>?, subjectNameSubstring: String?): ApiInsightsSubjectStats =
        client.get("/orgs/$org/insights/api/subject-stats") {
            parameter("direction", direction)
            parameter("min_timestamp", minTimestamp)
            parameter("page", page)
            parameter("per_page", perPage)
            maxTimestamp?.let { parameter("max_timestamp", it) }
            sort?.let { parameter("sort", it) }
            subjectNameSubstring?.let { parameter("subject_name_substring", it) }
        }.body()
}

internal class KtorOrgsInsightsApiSummaryStats(private val client: HttpClient) : Orgs.Insights.Api.SummaryStats {
    override val users: Orgs.Insights.Api.SummaryStats.Users = KtorOrgsInsightsApiSummaryStatsUsers(client)

    override suspend fun apiInsightsGetSummaryStats(org: String, minTimestamp: String, maxTimestamp: String?): ApiInsightsSummaryStats =
        client.get("/orgs/$org/insights/api/summary-stats") {
            parameter("min_timestamp", minTimestamp)
            maxTimestamp?.let { parameter("max_timestamp", it) }
        }.body()

    override suspend fun apiInsightsGetSummaryStatsByActor(org: String, actorType: Orgs.Insights.Api.SummaryStats.ActorType, actorId: Long, minTimestamp: String, maxTimestamp: String?): ApiInsightsSummaryStats =
        client.get("/orgs/$org/insights/api/summary-stats/$actorType/$actorId") {
            parameter("min_timestamp", minTimestamp)
            maxTimestamp?.let { parameter("max_timestamp", it) }
        }.body()
}

internal class KtorOrgsInsightsApiSummaryStatsUsers(private val client: HttpClient) : Orgs.Insights.Api.SummaryStats.Users {
    override suspend fun apiInsightsGetSummaryStatsByUser(org: String, userId: String, minTimestamp: String, maxTimestamp: String?): ApiInsightsSummaryStats =
        client.get("/orgs/$org/insights/api/summary-stats/users/$userId") {
            parameter("min_timestamp", minTimestamp)
            maxTimestamp?.let { parameter("max_timestamp", it) }
        }.body()
}

internal class KtorOrgsInsightsApiTimeStats(private val client: HttpClient) : Orgs.Insights.Api.TimeStats {
    override val users: Orgs.Insights.Api.TimeStats.Users = KtorOrgsInsightsApiTimeStatsUsers(client)

    override suspend fun apiInsightsGetTimeStats(org: String, minTimestamp: String, timestampIncrement: String, maxTimestamp: String?): ApiInsightsTimeStats =
        client.get("/orgs/$org/insights/api/time-stats") {
            parameter("min_timestamp", minTimestamp)
            parameter("timestamp_increment", timestampIncrement)
            maxTimestamp?.let { parameter("max_timestamp", it) }
        }.body()

    override suspend fun apiInsightsGetTimeStatsByActor(org: String, actorType: Orgs.Insights.Api.TimeStats.ActorType, actorId: Long, minTimestamp: String, timestampIncrement: String, maxTimestamp: String?): ApiInsightsTimeStats =
        client.get("/orgs/$org/insights/api/time-stats/$actorType/$actorId") {
            parameter("min_timestamp", minTimestamp)
            parameter("timestamp_increment", timestampIncrement)
            maxTimestamp?.let { parameter("max_timestamp", it) }
        }.body()
}

internal class KtorOrgsInsightsApiTimeStatsUsers(private val client: HttpClient) : Orgs.Insights.Api.TimeStats.Users {
    override suspend fun apiInsightsGetTimeStatsByUser(org: String, userId: String, minTimestamp: String, timestampIncrement: String, maxTimestamp: String?): ApiInsightsTimeStats =
        client.get("/orgs/$org/insights/api/time-stats/users/$userId") {
            parameter("min_timestamp", minTimestamp)
            parameter("timestamp_increment", timestampIncrement)
            maxTimestamp?.let { parameter("max_timestamp", it) }
        }.body()
}

internal class KtorOrgsInsightsApiUserStats(private val client: HttpClient) : Orgs.Insights.Api.UserStats {
    override suspend fun apiInsightsGetUserStats(org: String, userId: String, direction: Orgs.Insights.Api.UserStats.Direction, minTimestamp: String, page: Long, perPage: Long, actorNameSubstring: String?, maxTimestamp: String?, sort: List<Orgs.Insights.Api.UserStats.Sort>?): ApiInsightsUserStats =
        client.get("/orgs/$org/insights/api/user-stats/$userId") {
            parameter("direction", direction)
            parameter("min_timestamp", minTimestamp)
            parameter("page", page)
            parameter("per_page", perPage)
            actorNameSubstring?.let { parameter("actor_name_substring", it) }
            maxTimestamp?.let { parameter("max_timestamp", it) }
            sort?.let { parameter("sort", it) }
        }.body()
}

internal class KtorOrgsInstallation(private val client: HttpClient) : Orgs.Installation {
    override suspend fun appsGetOrgInstallation(org: String): Installation =
        client.get("/orgs/$org/installation").body()
}

internal class KtorOrgsInstallations(private val client: HttpClient) : Orgs.Installations {
    override suspend fun orgsListAppInstallations(org: String, page: Long, perPage: Long): Orgs.Installations.OrgsListAppInstallationsResponse =
        client.get("/orgs/$org/installations") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()
}

internal class KtorOrgsInteractionLimits(private val client: HttpClient) : Orgs.InteractionLimits {
    override suspend fun interactionsGetRestrictionsForOrg(org: String): Orgs.InteractionLimits.InteractionsGetRestrictionsForOrgResponse =
        client.get("/orgs/$org/interaction-limits").body()

    override suspend fun interactionsSetRestrictionsForOrg(org: String, body: InteractionLimit): Orgs.InteractionLimits.InteractionsSetRestrictionsForOrgResult {
        val response = client.put("/orgs/$org/interaction-limits") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.InteractionLimits.InteractionsSetRestrictionsForOrgResult.OK(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.InteractionLimits.InteractionsSetRestrictionsForOrgResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun interactionsRemoveRestrictionsForOrg(org: String): Unit =
        client.delete("/orgs/$org/interaction-limits").body()
}

internal class KtorOrgsInvitations(private val client: HttpClient) : Orgs.Invitations {
    override val teams: Orgs.Invitations.TeamsApi = KtorOrgsInvitationsTeamsApi(client)

    override suspend fun orgsListPendingInvitations(org: String, invitationSource: Orgs.Invitations.InvitationSource, page: Long, perPage: Long, role: Orgs.Invitations.Role): Orgs.Invitations.OrgsListPendingInvitationsResult {
        val response = client.get("/orgs/$org/invitations") {
            parameter("invitation_source", invitationSource)
            parameter("page", page)
            parameter("per_page", perPage)
            parameter("role", role)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Invitations.OrgsListPendingInvitationsResult.OK(response.body())
            HttpStatusCode.NotFound -> Orgs.Invitations.OrgsListPendingInvitationsResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun orgsCreateInvitation(org: String, body: Orgs.Invitations.OrgsCreateInvitationBody?): Orgs.Invitations.OrgsCreateInvitationResult {
        val response = client.post("/orgs/$org/invitations") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }
        return when (response.status) {
            HttpStatusCode.Created -> Orgs.Invitations.OrgsCreateInvitationResult.Created(response.body())
            HttpStatusCode.NotFound -> Orgs.Invitations.OrgsCreateInvitationResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.Invitations.OrgsCreateInvitationResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun orgsCancelInvitation(org: String, invitationId: Long): Orgs.Invitations.OrgsCancelInvitationResult {
        val response = client.delete("/orgs/$org/invitations/$invitationId")
        return when (response.status) {
            HttpStatusCode.NoContent -> Orgs.Invitations.OrgsCancelInvitationResult.NoContent
            HttpStatusCode.NotFound -> Orgs.Invitations.OrgsCancelInvitationResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.Invitations.OrgsCancelInvitationResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsInvitationsTeamsApi(private val client: HttpClient) : Orgs.Invitations.TeamsApi {
    override suspend fun orgsListInvitationTeams(org: String, invitationId: Long, page: Long, perPage: Long): Orgs.Invitations.TeamsApi.OrgsListInvitationTeamsResult {
        val response = client.get("/orgs/$org/invitations/$invitationId/teams") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Invitations.TeamsApi.OrgsListInvitationTeamsResult.OK(response.body())
            HttpStatusCode.NotFound -> Orgs.Invitations.TeamsApi.OrgsListInvitationTeamsResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsIssueFields(private val client: HttpClient) : Orgs.IssueFields {
    override suspend fun orgsListIssueFields(org: String): Orgs.IssueFields.OrgsListIssueFieldsResult {
        val response = client.get("/orgs/$org/issue-fields")
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.IssueFields.OrgsListIssueFieldsResult.OK(response.body())
            HttpStatusCode.NotFound -> Orgs.IssueFields.OrgsListIssueFieldsResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun orgsCreateIssueField(org: String, body: OrganizationCreateIssueField): Orgs.IssueFields.OrgsCreateIssueFieldResult {
        val response = client.post("/orgs/$org/issue-fields") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.IssueFields.OrgsCreateIssueFieldResult.OK(response.body())
            HttpStatusCode.NotFound -> Orgs.IssueFields.OrgsCreateIssueFieldResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.IssueFields.OrgsCreateIssueFieldResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun orgsDeleteIssueField(org: String, issueFieldId: Long): Orgs.IssueFields.OrgsDeleteIssueFieldResult {
        val response = client.delete("/orgs/$org/issue-fields/$issueFieldId")
        return when (response.status) {
            HttpStatusCode.NoContent -> Orgs.IssueFields.OrgsDeleteIssueFieldResult.NoContent
            HttpStatusCode.NotFound -> Orgs.IssueFields.OrgsDeleteIssueFieldResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.IssueFields.OrgsDeleteIssueFieldResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun orgsUpdateIssueField(org: String, issueFieldId: Long, body: OrganizationUpdateIssueField): Orgs.IssueFields.OrgsUpdateIssueFieldResult {
        val response = client.patch("/orgs/$org/issue-fields/$issueFieldId") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.IssueFields.OrgsUpdateIssueFieldResult.OK(response.body())
            HttpStatusCode.NotFound -> Orgs.IssueFields.OrgsUpdateIssueFieldResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.IssueFields.OrgsUpdateIssueFieldResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsIssueTypes(private val client: HttpClient) : Orgs.IssueTypes {
    override suspend fun orgsListIssueTypes(org: String): Orgs.IssueTypes.OrgsListIssueTypesResult {
        val response = client.get("/orgs/$org/issue-types")
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.IssueTypes.OrgsListIssueTypesResult.OK(response.body())
            HttpStatusCode.NotFound -> Orgs.IssueTypes.OrgsListIssueTypesResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun orgsCreateIssueType(org: String, body: OrganizationCreateIssueType): Orgs.IssueTypes.OrgsCreateIssueTypeResult {
        val response = client.post("/orgs/$org/issue-types") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.IssueTypes.OrgsCreateIssueTypeResult.OK(response.body())
            HttpStatusCode.NotFound -> Orgs.IssueTypes.OrgsCreateIssueTypeResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.IssueTypes.OrgsCreateIssueTypeResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun orgsUpdateIssueType(org: String, issueTypeId: Long, body: OrganizationUpdateIssueType): Orgs.IssueTypes.OrgsUpdateIssueTypeResult {
        val response = client.put("/orgs/$org/issue-types/$issueTypeId") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.IssueTypes.OrgsUpdateIssueTypeResult.OK(response.body())
            HttpStatusCode.NotFound -> Orgs.IssueTypes.OrgsUpdateIssueTypeResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.IssueTypes.OrgsUpdateIssueTypeResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun orgsDeleteIssueType(org: String, issueTypeId: Long): Orgs.IssueTypes.OrgsDeleteIssueTypeResult {
        val response = client.delete("/orgs/$org/issue-types/$issueTypeId")
        return when (response.status) {
            HttpStatusCode.NoContent -> Orgs.IssueTypes.OrgsDeleteIssueTypeResult.NoContent
            HttpStatusCode.NotFound -> Orgs.IssueTypes.OrgsDeleteIssueTypeResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.IssueTypes.OrgsDeleteIssueTypeResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsIssues(private val client: HttpClient) : Orgs.Issues {
    override suspend fun issuesListForOrg(org: String, direction: Orgs.Issues.Direction, filter: Orgs.Issues.Filter, page: Long, perPage: Long, sort: Orgs.Issues.Sort, state: Orgs.Issues.State, labels: String?, since: LocalDateTime?, type: String?): Orgs.Issues.IssuesListForOrgResult {
        val response = client.get("/orgs/$org/issues") {
            parameter("direction", direction)
            parameter("filter", filter)
            parameter("page", page)
            parameter("per_page", perPage)
            parameter("sort", sort)
            parameter("state", state)
            labels?.let { parameter("labels", it) }
            since?.let { parameter("since", it) }
            type?.let { parameter("type", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Issues.IssuesListForOrgResult.OK(response.body())
            HttpStatusCode.NotFound -> Orgs.Issues.IssuesListForOrgResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsMembers(private val client: HttpClient) : Orgs.Members {
    override val codespaces: Orgs.Members.CodespacesApi = KtorOrgsMembersCodespacesApi(client)

    override val copilot: Orgs.Members.CopilotApi = KtorOrgsMembersCopilotApi(client)

    override suspend fun orgsListMembers(org: String, filter: Orgs.Members.Filter, page: Long, perPage: Long, role: Orgs.Members.Role): Orgs.Members.OrgsListMembersResult {
        val response = client.get("/orgs/$org/members") {
            parameter("filter", filter)
            parameter("page", page)
            parameter("per_page", perPage)
            parameter("role", role)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Members.OrgsListMembersResult.OK(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.Members.OrgsListMembersResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun orgsCheckMembershipForUser(org: String, username: String): Orgs.Members.OrgsCheckMembershipForUserResult {
        val response = client.get("/orgs/$org/members/$username")
        return when (response.status) {
            HttpStatusCode.NoContent -> Orgs.Members.OrgsCheckMembershipForUserResult.NoContent
            HttpStatusCode.Found -> Orgs.Members.OrgsCheckMembershipForUserResult.Found
            HttpStatusCode.NotFound -> Orgs.Members.OrgsCheckMembershipForUserResult.NotFound
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun orgsRemoveMember(org: String, username: String): Orgs.Members.OrgsRemoveMemberResult {
        val response = client.delete("/orgs/$org/members/$username")
        return when (response.status) {
            HttpStatusCode.NoContent -> Orgs.Members.OrgsRemoveMemberResult.NoContent
            HttpStatusCode.Forbidden -> Orgs.Members.OrgsRemoveMemberResult.Forbidden(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsMembersCodespacesApi(private val client: HttpClient) : Orgs.Members.CodespacesApi {
    override val stop: Orgs.Members.CodespacesApi.Stop = KtorOrgsMembersCodespacesApiStop(client)

    override suspend fun codespacesGetCodespacesForUserInOrg(org: String, username: String, page: Long, perPage: Long): Orgs.Members.CodespacesApi.CodespacesGetCodespacesForUserInOrgResult {
        val response = client.get("/orgs/$org/members/$username/codespaces") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Members.CodespacesApi.CodespacesGetCodespacesForUserInOrgResult.OK(response.body())
            HttpStatusCode.NotModified -> Orgs.Members.CodespacesApi.CodespacesGetCodespacesForUserInOrgResult.NotModified
            HttpStatusCode.Unauthorized -> Orgs.Members.CodespacesApi.CodespacesGetCodespacesForUserInOrgResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Orgs.Members.CodespacesApi.CodespacesGetCodespacesForUserInOrgResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.Members.CodespacesApi.CodespacesGetCodespacesForUserInOrgResult.NotFound(response.body())
            HttpStatusCode.InternalServerError -> Orgs.Members.CodespacesApi.CodespacesGetCodespacesForUserInOrgResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun codespacesDeleteFromOrganization(org: String, username: String, codespaceName: String): Orgs.Members.CodespacesApi.CodespacesDeleteFromOrganizationResult {
        val response = client.delete("/orgs/$org/members/$username/codespaces/$codespaceName")
        return when (response.status) {
            HttpStatusCode.Accepted -> Orgs.Members.CodespacesApi.CodespacesDeleteFromOrganizationResult.Accepted(response.body())
            HttpStatusCode.NotModified -> Orgs.Members.CodespacesApi.CodespacesDeleteFromOrganizationResult.NotModified
            HttpStatusCode.Unauthorized -> Orgs.Members.CodespacesApi.CodespacesDeleteFromOrganizationResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Orgs.Members.CodespacesApi.CodespacesDeleteFromOrganizationResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.Members.CodespacesApi.CodespacesDeleteFromOrganizationResult.NotFound(response.body())
            HttpStatusCode.InternalServerError -> Orgs.Members.CodespacesApi.CodespacesDeleteFromOrganizationResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsMembersCodespacesApiStop(private val client: HttpClient) : Orgs.Members.CodespacesApi.Stop {
    override suspend fun codespacesStopInOrganization(org: String, username: String, codespaceName: String): Orgs.Members.CodespacesApi.Stop.CodespacesStopInOrganizationResult {
        val response = client.post("/orgs/$org/members/$username/codespaces/$codespaceName/stop")
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Members.CodespacesApi.Stop.CodespacesStopInOrganizationResult.OK(response.body())
            HttpStatusCode.NotModified -> Orgs.Members.CodespacesApi.Stop.CodespacesStopInOrganizationResult.NotModified
            HttpStatusCode.Unauthorized -> Orgs.Members.CodespacesApi.Stop.CodespacesStopInOrganizationResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Orgs.Members.CodespacesApi.Stop.CodespacesStopInOrganizationResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.Members.CodespacesApi.Stop.CodespacesStopInOrganizationResult.NotFound(response.body())
            HttpStatusCode.InternalServerError -> Orgs.Members.CodespacesApi.Stop.CodespacesStopInOrganizationResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsMembersCopilotApi(private val client: HttpClient) : Orgs.Members.CopilotApi {
    override suspend fun copilotGetCopilotSeatDetailsForUser(org: String, username: String): Orgs.Members.CopilotApi.CopilotGetCopilotSeatDetailsForUserResult {
        val response = client.get("/orgs/$org/members/$username/copilot")
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Members.CopilotApi.CopilotGetCopilotSeatDetailsForUserResult.OK(response.body())
            HttpStatusCode.Unauthorized -> Orgs.Members.CopilotApi.CopilotGetCopilotSeatDetailsForUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Orgs.Members.CopilotApi.CopilotGetCopilotSeatDetailsForUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.Members.CopilotApi.CopilotGetCopilotSeatDetailsForUserResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.Members.CopilotApi.CopilotGetCopilotSeatDetailsForUserResult.UnprocessableEntity
            HttpStatusCode.InternalServerError -> Orgs.Members.CopilotApi.CopilotGetCopilotSeatDetailsForUserResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsMemberships(private val client: HttpClient) : Orgs.Memberships {
    override suspend fun orgsGetMembershipForUser(org: String, username: String): Orgs.Memberships.OrgsGetMembershipForUserResult {
        val response = client.get("/orgs/$org/memberships/$username")
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Memberships.OrgsGetMembershipForUserResult.OK(response.body())
            HttpStatusCode.Forbidden -> Orgs.Memberships.OrgsGetMembershipForUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.Memberships.OrgsGetMembershipForUserResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun orgsSetMembershipForUser(org: String, username: String, body: Orgs.Memberships.OrgsSetMembershipForUserBody?): Orgs.Memberships.OrgsSetMembershipForUserResult {
        val response = client.put("/orgs/$org/memberships/$username") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Memberships.OrgsSetMembershipForUserResult.OK(response.body())
            HttpStatusCode.Forbidden -> Orgs.Memberships.OrgsSetMembershipForUserResult.Forbidden(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.Memberships.OrgsSetMembershipForUserResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun orgsRemoveMembershipForUser(org: String, username: String): Orgs.Memberships.OrgsRemoveMembershipForUserResult {
        val response = client.delete("/orgs/$org/memberships/$username")
        return when (response.status) {
            HttpStatusCode.NoContent -> Orgs.Memberships.OrgsRemoveMembershipForUserResult.NoContent
            HttpStatusCode.Forbidden -> Orgs.Memberships.OrgsRemoveMembershipForUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.Memberships.OrgsRemoveMembershipForUserResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsMigrations(private val client: HttpClient) : Orgs.Migrations {
    override val archive: Orgs.Migrations.Archive = KtorOrgsMigrationsArchive(client)

    override val repos: Orgs.Migrations.ReposApi = KtorOrgsMigrationsReposApi(client)

    override val repositories: Orgs.Migrations.Repositories = KtorOrgsMigrationsRepositories(client)

    override suspend fun migrationsListForOrg(org: String, page: Long, perPage: Long, exclude: List<Orgs.Migrations.MigrationsListForOrgExclude>?): List<Migration> =
        client.get("/orgs/$org/migrations") {
            parameter("page", page)
            parameter("per_page", perPage)
            exclude?.let { parameter("exclude", it) }
        }.body()

    override suspend fun migrationsStartForOrg(org: String, body: Orgs.Migrations.MigrationsStartForOrgBody): Orgs.Migrations.MigrationsStartForOrgResult {
        val response = client.post("/orgs/$org/migrations") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> Orgs.Migrations.MigrationsStartForOrgResult.Created(response.body())
            HttpStatusCode.NotFound -> Orgs.Migrations.MigrationsStartForOrgResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.Migrations.MigrationsStartForOrgResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun migrationsGetStatusForOrg(org: String, migrationId: Long, exclude: List<Orgs.Migrations.Exclude>?): Orgs.Migrations.MigrationsGetStatusForOrgResult {
        val response = client.get("/orgs/$org/migrations/$migrationId") {
            exclude?.let { parameter("exclude", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Migrations.MigrationsGetStatusForOrgResult.OK(response.body())
            HttpStatusCode.NotFound -> Orgs.Migrations.MigrationsGetStatusForOrgResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsMigrationsArchive(private val client: HttpClient) : Orgs.Migrations.Archive {
    override suspend fun migrationsDownloadArchiveForOrg(org: String, migrationId: Long): Orgs.Migrations.Archive.MigrationsDownloadArchiveForOrgResult {
        val response = client.get("/orgs/$org/migrations/$migrationId/archive")
        return when (response.status) {
            HttpStatusCode.Found -> Orgs.Migrations.Archive.MigrationsDownloadArchiveForOrgResult.Found
            HttpStatusCode.NotFound -> Orgs.Migrations.Archive.MigrationsDownloadArchiveForOrgResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun migrationsDeleteArchiveForOrg(org: String, migrationId: Long): Orgs.Migrations.Archive.MigrationsDeleteArchiveForOrgResult {
        val response = client.delete("/orgs/$org/migrations/$migrationId/archive")
        return when (response.status) {
            HttpStatusCode.NoContent -> Orgs.Migrations.Archive.MigrationsDeleteArchiveForOrgResult.NoContent
            HttpStatusCode.NotFound -> Orgs.Migrations.Archive.MigrationsDeleteArchiveForOrgResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsMigrationsReposApi(private val client: HttpClient) : Orgs.Migrations.ReposApi {
    override val lock: Orgs.Migrations.ReposApi.Lock = KtorOrgsMigrationsReposApiLock(client)
}

internal class KtorOrgsMigrationsReposApiLock(private val client: HttpClient) : Orgs.Migrations.ReposApi.Lock {
    override suspend fun migrationsUnlockRepoForOrg(org: String, migrationId: Long, repoName: String): Orgs.Migrations.ReposApi.Lock.MigrationsUnlockRepoForOrgResult {
        val response = client.delete("/orgs/$org/migrations/$migrationId/repos/$repoName/lock")
        return when (response.status) {
            HttpStatusCode.NoContent -> Orgs.Migrations.ReposApi.Lock.MigrationsUnlockRepoForOrgResult.NoContent
            HttpStatusCode.NotFound -> Orgs.Migrations.ReposApi.Lock.MigrationsUnlockRepoForOrgResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsMigrationsRepositories(private val client: HttpClient) : Orgs.Migrations.Repositories {
    override suspend fun migrationsListReposForOrg(org: String, migrationId: Long, page: Long, perPage: Long): Orgs.Migrations.Repositories.MigrationsListReposForOrgResult {
        val response = client.get("/orgs/$org/migrations/$migrationId/repositories") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Migrations.Repositories.MigrationsListReposForOrgResult.OK(response.body())
            HttpStatusCode.NotFound -> Orgs.Migrations.Repositories.MigrationsListReposForOrgResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsOrganizationRoles(private val client: HttpClient) : Orgs.OrganizationRoles {
    override val teams: Orgs.OrganizationRoles.TeamsApi = KtorOrgsOrganizationRolesTeamsApi(client)

    override val users: Orgs.OrganizationRoles.Users = KtorOrgsOrganizationRolesUsers(client)

    override suspend fun orgsListOrgRoles(org: String): Orgs.OrganizationRoles.OrgsListOrgRolesResult {
        val response = client.get("/orgs/$org/organization-roles")
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.OrganizationRoles.OrgsListOrgRolesResult.OK(response.body())
            HttpStatusCode.NotFound -> Orgs.OrganizationRoles.OrgsListOrgRolesResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.OrganizationRoles.OrgsListOrgRolesResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun orgsGetOrgRole(org: String, roleId: Long): Orgs.OrganizationRoles.OrgsGetOrgRoleResult {
        val response = client.get("/orgs/$org/organization-roles/$roleId")
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.OrganizationRoles.OrgsGetOrgRoleResult.OK(response.body())
            HttpStatusCode.NotFound -> Orgs.OrganizationRoles.OrgsGetOrgRoleResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.OrganizationRoles.OrgsGetOrgRoleResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsOrganizationRolesTeamsApi(private val client: HttpClient) : Orgs.OrganizationRoles.TeamsApi {
    override suspend fun orgsRevokeAllOrgRolesTeam(org: String, teamSlug: String): Unit =
        client.delete("/orgs/$org/organization-roles/teams/$teamSlug").body()

    override suspend fun orgsAssignTeamToOrgRole(org: String, teamSlug: String, roleId: Long): Orgs.OrganizationRoles.TeamsApi.OrgsAssignTeamToOrgRoleResult {
        val response = client.put("/orgs/$org/organization-roles/teams/$teamSlug/$roleId")
        return when (response.status) {
            HttpStatusCode.NoContent -> Orgs.OrganizationRoles.TeamsApi.OrgsAssignTeamToOrgRoleResult.NoContent
            HttpStatusCode.NotFound -> Orgs.OrganizationRoles.TeamsApi.OrgsAssignTeamToOrgRoleResult.NotFound
            HttpStatusCode.UnprocessableEntity -> Orgs.OrganizationRoles.TeamsApi.OrgsAssignTeamToOrgRoleResult.UnprocessableEntity
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun orgsRevokeOrgRoleTeam(org: String, teamSlug: String, roleId: Long): Unit =
        client.delete("/orgs/$org/organization-roles/teams/$teamSlug/$roleId").body()

    override suspend fun orgsListOrgRoleTeams(org: String, roleId: Long, page: Long, perPage: Long): Orgs.OrganizationRoles.TeamsApi.OrgsListOrgRoleTeamsResult {
        val response = client.get("/orgs/$org/organization-roles/$roleId/teams") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.OrganizationRoles.TeamsApi.OrgsListOrgRoleTeamsResult.OK(response.body())
            HttpStatusCode.NotFound -> Orgs.OrganizationRoles.TeamsApi.OrgsListOrgRoleTeamsResult.NotFound
            HttpStatusCode.UnprocessableEntity -> Orgs.OrganizationRoles.TeamsApi.OrgsListOrgRoleTeamsResult.UnprocessableEntity
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsOrganizationRolesUsers(private val client: HttpClient) : Orgs.OrganizationRoles.Users {
    override suspend fun orgsRevokeAllOrgRolesUser(org: String, username: String): Unit =
        client.delete("/orgs/$org/organization-roles/users/$username").body()

    override suspend fun orgsAssignUserToOrgRole(org: String, username: String, roleId: Long): Orgs.OrganizationRoles.Users.OrgsAssignUserToOrgRoleResult {
        val response = client.put("/orgs/$org/organization-roles/users/$username/$roleId")
        return when (response.status) {
            HttpStatusCode.NoContent -> Orgs.OrganizationRoles.Users.OrgsAssignUserToOrgRoleResult.NoContent
            HttpStatusCode.NotFound -> Orgs.OrganizationRoles.Users.OrgsAssignUserToOrgRoleResult.NotFound
            HttpStatusCode.UnprocessableEntity -> Orgs.OrganizationRoles.Users.OrgsAssignUserToOrgRoleResult.UnprocessableEntity
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun orgsRevokeOrgRoleUser(org: String, username: String, roleId: Long): Unit =
        client.delete("/orgs/$org/organization-roles/users/$username/$roleId").body()

    override suspend fun orgsListOrgRoleUsers(org: String, roleId: Long, page: Long, perPage: Long): Orgs.OrganizationRoles.Users.OrgsListOrgRoleUsersResult {
        val response = client.get("/orgs/$org/organization-roles/$roleId/users") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.OrganizationRoles.Users.OrgsListOrgRoleUsersResult.OK(response.body())
            HttpStatusCode.NotFound -> Orgs.OrganizationRoles.Users.OrgsListOrgRoleUsersResult.NotFound
            HttpStatusCode.UnprocessableEntity -> Orgs.OrganizationRoles.Users.OrgsListOrgRoleUsersResult.UnprocessableEntity
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsOutsideCollaborators(private val client: HttpClient) : Orgs.OutsideCollaborators {
    override suspend fun orgsListOutsideCollaborators(org: String, filter: Orgs.OutsideCollaborators.Filter, page: Long, perPage: Long): List<SimpleUser> =
        client.get("/orgs/$org/outside_collaborators") {
            parameter("filter", filter)
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()

    override suspend fun orgsConvertMemberToOutsideCollaborator(org: String, username: String, body: Orgs.OutsideCollaborators.OrgsConvertMemberToOutsideCollaboratorBody?): Orgs.OutsideCollaborators.OrgsConvertMemberToOutsideCollaboratorResult {
        val response = client.put("/orgs/$org/outside_collaborators/$username") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }
        return when (response.status) {
            HttpStatusCode.Accepted -> Orgs.OutsideCollaborators.OrgsConvertMemberToOutsideCollaboratorResult.Accepted
            HttpStatusCode.NoContent -> Orgs.OutsideCollaborators.OrgsConvertMemberToOutsideCollaboratorResult.NoContent
            HttpStatusCode.Forbidden -> Orgs.OutsideCollaborators.OrgsConvertMemberToOutsideCollaboratorResult.Forbidden
            HttpStatusCode.NotFound -> Orgs.OutsideCollaborators.OrgsConvertMemberToOutsideCollaboratorResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun orgsRemoveOutsideCollaborator(org: String, username: String): Orgs.OutsideCollaborators.OrgsRemoveOutsideCollaboratorResult {
        val response = client.delete("/orgs/$org/outside_collaborators/$username")
        return when (response.status) {
            HttpStatusCode.NoContent -> Orgs.OutsideCollaborators.OrgsRemoveOutsideCollaboratorResult.NoContent
            HttpStatusCode.UnprocessableEntity -> Orgs.OutsideCollaborators.OrgsRemoveOutsideCollaboratorResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsPackages(private val client: HttpClient) : Orgs.Packages {
    override val restore: Orgs.Packages.Restore = KtorOrgsPackagesRestore(client)

    override val versions: Orgs.Packages.Versions = KtorOrgsPackagesVersions(client)

    override suspend fun packagesListPackagesForOrganization(org: String, packageType: Orgs.Packages.PackagesListPackagesForOrganizationPackageType, page: Long, perPage: Long, visibility: Orgs.Packages.Visibility?): Orgs.Packages.PackagesListPackagesForOrganizationResult {
        val response = client.get("/orgs/$org/packages") {
            parameter("package_type", packageType)
            parameter("page", page)
            parameter("per_page", perPage)
            visibility?.let { parameter("visibility", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Packages.PackagesListPackagesForOrganizationResult.OK(response.body())
            HttpStatusCode.BadRequest -> Orgs.Packages.PackagesListPackagesForOrganizationResult.BadRequest
            HttpStatusCode.Unauthorized -> Orgs.Packages.PackagesListPackagesForOrganizationResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Orgs.Packages.PackagesListPackagesForOrganizationResult.Forbidden(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun packagesGetPackageForOrganization(org: String, packageType: Orgs.Packages.PackagesGetPackageForOrganizationPackageType, packageName: String): Package =
        client.get("/orgs/$org/packages/$packageType/$packageName").body()

    override suspend fun packagesDeletePackageForOrg(org: String, packageType: Orgs.Packages.PackageType, packageName: String): Orgs.Packages.PackagesDeletePackageForOrgResult {
        val response = client.delete("/orgs/$org/packages/$packageType/$packageName")
        return when (response.status) {
            HttpStatusCode.NoContent -> Orgs.Packages.PackagesDeletePackageForOrgResult.NoContent
            HttpStatusCode.Unauthorized -> Orgs.Packages.PackagesDeletePackageForOrgResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Orgs.Packages.PackagesDeletePackageForOrgResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.Packages.PackagesDeletePackageForOrgResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsPackagesRestore(private val client: HttpClient) : Orgs.Packages.Restore {
    override suspend fun packagesRestorePackageForOrg(org: String, packageType: Orgs.Packages.Restore.PackagesRestorePackageForOrgPackageType, packageName: String, token: String?): Orgs.Packages.Restore.PackagesRestorePackageForOrgResult {
        val response = client.post("/orgs/$org/packages/$packageType/$packageName/restore") {
            token?.let { parameter("token", it) }
        }
        return when (response.status) {
            HttpStatusCode.NoContent -> Orgs.Packages.Restore.PackagesRestorePackageForOrgResult.NoContent
            HttpStatusCode.Unauthorized -> Orgs.Packages.Restore.PackagesRestorePackageForOrgResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Orgs.Packages.Restore.PackagesRestorePackageForOrgResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.Packages.Restore.PackagesRestorePackageForOrgResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsPackagesVersions(private val client: HttpClient) : Orgs.Packages.Versions {
    override val restore: Orgs.Packages.Versions.RestoreApi = KtorOrgsPackagesVersionsRestoreApi(client)

    override suspend fun packagesGetAllPackageVersionsForPackageOwnedByOrg(org: String, packageType: Orgs.Packages.Versions.PackagesGetAllPackageVersionsForPackageOwnedByOrgPackageType, packageName: String, page: Long, perPage: Long, state: Orgs.Packages.Versions.State): Orgs.Packages.Versions.PackagesGetAllPackageVersionsForPackageOwnedByOrgResult {
        val response = client.get("/orgs/$org/packages/$packageType/$packageName/versions") {
            parameter("page", page)
            parameter("per_page", perPage)
            parameter("state", state)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Packages.Versions.PackagesGetAllPackageVersionsForPackageOwnedByOrgResult.OK(response.body())
            HttpStatusCode.Unauthorized -> Orgs.Packages.Versions.PackagesGetAllPackageVersionsForPackageOwnedByOrgResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Orgs.Packages.Versions.PackagesGetAllPackageVersionsForPackageOwnedByOrgResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.Packages.Versions.PackagesGetAllPackageVersionsForPackageOwnedByOrgResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun packagesGetPackageVersionForOrganization(org: String, packageType: Orgs.Packages.Versions.PackagesGetPackageVersionForOrganizationPackageType, packageName: String, packageVersionId: Long): PackageVersion =
        client.get("/orgs/$org/packages/$packageType/$packageName/versions/$packageVersionId").body()

    override suspend fun packagesDeletePackageVersionForOrg(org: String, packageType: Orgs.Packages.Versions.PackagesDeletePackageVersionForOrgPackageType, packageName: String, packageVersionId: Long): Orgs.Packages.Versions.PackagesDeletePackageVersionForOrgResult {
        val response = client.delete("/orgs/$org/packages/$packageType/$packageName/versions/$packageVersionId")
        return when (response.status) {
            HttpStatusCode.NoContent -> Orgs.Packages.Versions.PackagesDeletePackageVersionForOrgResult.NoContent
            HttpStatusCode.Unauthorized -> Orgs.Packages.Versions.PackagesDeletePackageVersionForOrgResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Orgs.Packages.Versions.PackagesDeletePackageVersionForOrgResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.Packages.Versions.PackagesDeletePackageVersionForOrgResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsPackagesVersionsRestoreApi(private val client: HttpClient) : Orgs.Packages.Versions.RestoreApi {
    override suspend fun packagesRestorePackageVersionForOrg(org: String, packageType: Orgs.Packages.Versions.RestoreApi.PackagesRestorePackageVersionForOrgPackageType, packageName: String, packageVersionId: Long): Orgs.Packages.Versions.RestoreApi.PackagesRestorePackageVersionForOrgResult {
        val response = client.post("/orgs/$org/packages/$packageType/$packageName/versions/$packageVersionId/restore")
        return when (response.status) {
            HttpStatusCode.NoContent -> Orgs.Packages.Versions.RestoreApi.PackagesRestorePackageVersionForOrgResult.NoContent
            HttpStatusCode.Unauthorized -> Orgs.Packages.Versions.RestoreApi.PackagesRestorePackageVersionForOrgResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Orgs.Packages.Versions.RestoreApi.PackagesRestorePackageVersionForOrgResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.Packages.Versions.RestoreApi.PackagesRestorePackageVersionForOrgResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsPersonalAccessTokenRequests(private val client: HttpClient) : Orgs.PersonalAccessTokenRequests {
    override val repositories: Orgs.PersonalAccessTokenRequests.Repositories = KtorOrgsPersonalAccessTokenRequestsRepositories(client)

    override suspend fun orgsListPatGrantRequests(org: String, direction: Orgs.PersonalAccessTokenRequests.Direction, page: Long, perPage: Long, sort: Orgs.PersonalAccessTokenRequests.Sort, lastUsedAfter: LocalDateTime?, lastUsedBefore: LocalDateTime?, owner: List<String>?, permission: String?, repository: String?, tokenId: List<String>?): Orgs.PersonalAccessTokenRequests.OrgsListPatGrantRequestsResult {
        val response = client.get("/orgs/$org/personal-access-token-requests") {
            parameter("direction", direction)
            parameter("page", page)
            parameter("per_page", perPage)
            parameter("sort", sort)
            lastUsedAfter?.let { parameter("last_used_after", it) }
            lastUsedBefore?.let { parameter("last_used_before", it) }
            owner?.let { parameter("owner", it) }
            permission?.let { parameter("permission", it) }
            repository?.let { parameter("repository", it) }
            tokenId?.let { parameter("token_id", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.PersonalAccessTokenRequests.OrgsListPatGrantRequestsResult.OK(response.body())
            HttpStatusCode.Forbidden -> Orgs.PersonalAccessTokenRequests.OrgsListPatGrantRequestsResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.PersonalAccessTokenRequests.OrgsListPatGrantRequestsResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.PersonalAccessTokenRequests.OrgsListPatGrantRequestsResult.UnprocessableEntity(response.body())
            HttpStatusCode.InternalServerError -> Orgs.PersonalAccessTokenRequests.OrgsListPatGrantRequestsResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun orgsReviewPatGrantRequestsInBulk(org: String, body: Orgs.PersonalAccessTokenRequests.OrgsReviewPatGrantRequestsInBulkBody): Orgs.PersonalAccessTokenRequests.OrgsReviewPatGrantRequestsInBulkResult {
        val response = client.post("/orgs/$org/personal-access-token-requests") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Accepted -> Orgs.PersonalAccessTokenRequests.OrgsReviewPatGrantRequestsInBulkResult.Accepted(response.body())
            HttpStatusCode.Forbidden -> Orgs.PersonalAccessTokenRequests.OrgsReviewPatGrantRequestsInBulkResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.PersonalAccessTokenRequests.OrgsReviewPatGrantRequestsInBulkResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.PersonalAccessTokenRequests.OrgsReviewPatGrantRequestsInBulkResult.UnprocessableEntity(response.body())
            HttpStatusCode.InternalServerError -> Orgs.PersonalAccessTokenRequests.OrgsReviewPatGrantRequestsInBulkResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun orgsReviewPatGrantRequest(org: String, patRequestId: Long, body: Orgs.PersonalAccessTokenRequests.OrgsReviewPatGrantRequestBody): Orgs.PersonalAccessTokenRequests.OrgsReviewPatGrantRequestResult {
        val response = client.post("/orgs/$org/personal-access-token-requests/$patRequestId") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.NoContent -> Orgs.PersonalAccessTokenRequests.OrgsReviewPatGrantRequestResult.NoContent
            HttpStatusCode.Forbidden -> Orgs.PersonalAccessTokenRequests.OrgsReviewPatGrantRequestResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.PersonalAccessTokenRequests.OrgsReviewPatGrantRequestResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.PersonalAccessTokenRequests.OrgsReviewPatGrantRequestResult.UnprocessableEntity(response.body())
            HttpStatusCode.InternalServerError -> Orgs.PersonalAccessTokenRequests.OrgsReviewPatGrantRequestResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsPersonalAccessTokenRequestsRepositories(private val client: HttpClient) : Orgs.PersonalAccessTokenRequests.Repositories {
    override suspend fun orgsListPatGrantRequestRepositories(org: String, patRequestId: Long, page: Long, perPage: Long): Orgs.PersonalAccessTokenRequests.Repositories.OrgsListPatGrantRequestRepositoriesResult {
        val response = client.get("/orgs/$org/personal-access-token-requests/$patRequestId/repositories") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.PersonalAccessTokenRequests.Repositories.OrgsListPatGrantRequestRepositoriesResult.OK(response.body())
            HttpStatusCode.Forbidden -> Orgs.PersonalAccessTokenRequests.Repositories.OrgsListPatGrantRequestRepositoriesResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.PersonalAccessTokenRequests.Repositories.OrgsListPatGrantRequestRepositoriesResult.NotFound(response.body())
            HttpStatusCode.InternalServerError -> Orgs.PersonalAccessTokenRequests.Repositories.OrgsListPatGrantRequestRepositoriesResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsPersonalAccessTokens(private val client: HttpClient) : Orgs.PersonalAccessTokens {
    override val repositories: Orgs.PersonalAccessTokens.Repositories = KtorOrgsPersonalAccessTokensRepositories(client)

    override suspend fun orgsListPatGrants(org: String, direction: Orgs.PersonalAccessTokens.Direction, page: Long, perPage: Long, sort: Orgs.PersonalAccessTokens.Sort, lastUsedAfter: LocalDateTime?, lastUsedBefore: LocalDateTime?, owner: List<String>?, permission: String?, repository: String?, tokenId: List<String>?): Orgs.PersonalAccessTokens.OrgsListPatGrantsResult {
        val response = client.get("/orgs/$org/personal-access-tokens") {
            parameter("direction", direction)
            parameter("page", page)
            parameter("per_page", perPage)
            parameter("sort", sort)
            lastUsedAfter?.let { parameter("last_used_after", it) }
            lastUsedBefore?.let { parameter("last_used_before", it) }
            owner?.let { parameter("owner", it) }
            permission?.let { parameter("permission", it) }
            repository?.let { parameter("repository", it) }
            tokenId?.let { parameter("token_id", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.PersonalAccessTokens.OrgsListPatGrantsResult.OK(response.body())
            HttpStatusCode.Forbidden -> Orgs.PersonalAccessTokens.OrgsListPatGrantsResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.PersonalAccessTokens.OrgsListPatGrantsResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.PersonalAccessTokens.OrgsListPatGrantsResult.UnprocessableEntity(response.body())
            HttpStatusCode.InternalServerError -> Orgs.PersonalAccessTokens.OrgsListPatGrantsResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun orgsUpdatePatAccesses(org: String, body: Orgs.PersonalAccessTokens.OrgsUpdatePatAccessesBody): Orgs.PersonalAccessTokens.OrgsUpdatePatAccessesResult {
        val response = client.post("/orgs/$org/personal-access-tokens") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Accepted -> Orgs.PersonalAccessTokens.OrgsUpdatePatAccessesResult.Accepted(response.body())
            HttpStatusCode.Forbidden -> Orgs.PersonalAccessTokens.OrgsUpdatePatAccessesResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.PersonalAccessTokens.OrgsUpdatePatAccessesResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.PersonalAccessTokens.OrgsUpdatePatAccessesResult.UnprocessableEntity(response.body())
            HttpStatusCode.InternalServerError -> Orgs.PersonalAccessTokens.OrgsUpdatePatAccessesResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun orgsUpdatePatAccess(org: String, patId: Long, body: Orgs.PersonalAccessTokens.OrgsUpdatePatAccessBody): Orgs.PersonalAccessTokens.OrgsUpdatePatAccessResult {
        val response = client.post("/orgs/$org/personal-access-tokens/$patId") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.NoContent -> Orgs.PersonalAccessTokens.OrgsUpdatePatAccessResult.NoContent
            HttpStatusCode.Forbidden -> Orgs.PersonalAccessTokens.OrgsUpdatePatAccessResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.PersonalAccessTokens.OrgsUpdatePatAccessResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.PersonalAccessTokens.OrgsUpdatePatAccessResult.UnprocessableEntity(response.body())
            HttpStatusCode.InternalServerError -> Orgs.PersonalAccessTokens.OrgsUpdatePatAccessResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsPersonalAccessTokensRepositories(private val client: HttpClient) : Orgs.PersonalAccessTokens.Repositories {
    override suspend fun orgsListPatGrantRepositories(org: String, patId: Long, page: Long, perPage: Long): Orgs.PersonalAccessTokens.Repositories.OrgsListPatGrantRepositoriesResult {
        val response = client.get("/orgs/$org/personal-access-tokens/$patId/repositories") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.PersonalAccessTokens.Repositories.OrgsListPatGrantRepositoriesResult.OK(response.body())
            HttpStatusCode.Forbidden -> Orgs.PersonalAccessTokens.Repositories.OrgsListPatGrantRepositoriesResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.PersonalAccessTokens.Repositories.OrgsListPatGrantRepositoriesResult.NotFound(response.body())
            HttpStatusCode.InternalServerError -> Orgs.PersonalAccessTokens.Repositories.OrgsListPatGrantRepositoriesResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsPrivateRegistries(private val client: HttpClient) : Orgs.PrivateRegistries {
    override val publicKey: Orgs.PrivateRegistries.PublicKey = KtorOrgsPrivateRegistriesPublicKey(client)

    override suspend fun privateRegistriesListOrgPrivateRegistries(org: String, page: Long, perPage: Long): Orgs.PrivateRegistries.PrivateRegistriesListOrgPrivateRegistriesResult {
        val response = client.get("/orgs/$org/private-registries") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.PrivateRegistries.PrivateRegistriesListOrgPrivateRegistriesResult.OK(response.body())
            HttpStatusCode.BadRequest -> Orgs.PrivateRegistries.PrivateRegistriesListOrgPrivateRegistriesResult.BadRequest(response.body())
            HttpStatusCode.NotFound -> Orgs.PrivateRegistries.PrivateRegistriesListOrgPrivateRegistriesResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun privateRegistriesCreateOrgPrivateRegistry(org: String, body: Orgs.PrivateRegistries.PrivateRegistriesCreateOrgPrivateRegistryBody): Orgs.PrivateRegistries.PrivateRegistriesCreateOrgPrivateRegistryResult {
        val response = client.post("/orgs/$org/private-registries") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> Orgs.PrivateRegistries.PrivateRegistriesCreateOrgPrivateRegistryResult.Created(response.body())
            HttpStatusCode.NotFound -> Orgs.PrivateRegistries.PrivateRegistriesCreateOrgPrivateRegistryResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.PrivateRegistries.PrivateRegistriesCreateOrgPrivateRegistryResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun privateRegistriesGetOrgPrivateRegistry(org: String, secretName: String): Orgs.PrivateRegistries.PrivateRegistriesGetOrgPrivateRegistryResult {
        val response = client.get("/orgs/$org/private-registries/$secretName")
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.PrivateRegistries.PrivateRegistriesGetOrgPrivateRegistryResult.OK(response.body())
            HttpStatusCode.NotFound -> Orgs.PrivateRegistries.PrivateRegistriesGetOrgPrivateRegistryResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun privateRegistriesDeleteOrgPrivateRegistry(org: String, secretName: String): Orgs.PrivateRegistries.PrivateRegistriesDeleteOrgPrivateRegistryResult {
        val response = client.delete("/orgs/$org/private-registries/$secretName")
        return when (response.status) {
            HttpStatusCode.NoContent -> Orgs.PrivateRegistries.PrivateRegistriesDeleteOrgPrivateRegistryResult.NoContent
            HttpStatusCode.BadRequest -> Orgs.PrivateRegistries.PrivateRegistriesDeleteOrgPrivateRegistryResult.BadRequest(response.body())
            HttpStatusCode.NotFound -> Orgs.PrivateRegistries.PrivateRegistriesDeleteOrgPrivateRegistryResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun privateRegistriesUpdateOrgPrivateRegistry(org: String, secretName: String, body: Orgs.PrivateRegistries.PrivateRegistriesUpdateOrgPrivateRegistryBody): Orgs.PrivateRegistries.PrivateRegistriesUpdateOrgPrivateRegistryResult {
        val response = client.patch("/orgs/$org/private-registries/$secretName") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.NoContent -> Orgs.PrivateRegistries.PrivateRegistriesUpdateOrgPrivateRegistryResult.NoContent
            HttpStatusCode.NotFound -> Orgs.PrivateRegistries.PrivateRegistriesUpdateOrgPrivateRegistryResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.PrivateRegistries.PrivateRegistriesUpdateOrgPrivateRegistryResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsPrivateRegistriesPublicKey(private val client: HttpClient) : Orgs.PrivateRegistries.PublicKey {
    override suspend fun privateRegistriesGetOrgPublicKey(org: String): Orgs.PrivateRegistries.PublicKey.PrivateRegistriesGetOrgPublicKeyResult {
        val response = client.get("/orgs/$org/private-registries/public-key")
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.PrivateRegistries.PublicKey.PrivateRegistriesGetOrgPublicKeyResult.OK(response.body())
            HttpStatusCode.NotFound -> Orgs.PrivateRegistries.PublicKey.PrivateRegistriesGetOrgPublicKeyResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsProjectsV2(private val client: HttpClient) : Orgs.ProjectsV2 {
    override val drafts: Orgs.ProjectsV2.Drafts = KtorOrgsProjectsV2Drafts(client)

    override val fields: Orgs.ProjectsV2.Fields = KtorOrgsProjectsV2Fields(client)

    override val items: Orgs.ProjectsV2.Items = KtorOrgsProjectsV2Items(client)

    override val views: Orgs.ProjectsV2.Views = KtorOrgsProjectsV2Views(client)

    override suspend fun projectsListForOrg(org: String, perPage: Long, after: String?, before: String?, q: String?): Orgs.ProjectsV2.ProjectsListForOrgResult {
        val response = client.get("/orgs/$org/projectsV2") {
            parameter("per_page", perPage)
            after?.let { parameter("after", it) }
            before?.let { parameter("before", it) }
            q?.let { parameter("q", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.ProjectsV2.ProjectsListForOrgResult.OK(response.body())
            HttpStatusCode.NotModified -> Orgs.ProjectsV2.ProjectsListForOrgResult.NotModified
            HttpStatusCode.Unauthorized -> Orgs.ProjectsV2.ProjectsListForOrgResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Orgs.ProjectsV2.ProjectsListForOrgResult.Forbidden(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun projectsGetForOrg(org: String, projectNumber: Long): Orgs.ProjectsV2.ProjectsGetForOrgResult {
        val response = client.get("/orgs/$org/projectsV2/$projectNumber")
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.ProjectsV2.ProjectsGetForOrgResult.OK(response.body())
            HttpStatusCode.NotModified -> Orgs.ProjectsV2.ProjectsGetForOrgResult.NotModified
            HttpStatusCode.Unauthorized -> Orgs.ProjectsV2.ProjectsGetForOrgResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Orgs.ProjectsV2.ProjectsGetForOrgResult.Forbidden(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsProjectsV2Drafts(private val client: HttpClient) : Orgs.ProjectsV2.Drafts {
    override suspend fun projectsCreateDraftItemForOrg(org: String, projectNumber: Long, body: Orgs.ProjectsV2.Drafts.ProjectsCreateDraftItemForOrgBody): Orgs.ProjectsV2.Drafts.ProjectsCreateDraftItemForOrgResult {
        val response = client.post("/orgs/$org/projectsV2/$projectNumber/drafts") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> Orgs.ProjectsV2.Drafts.ProjectsCreateDraftItemForOrgResult.Created(response.body())
            HttpStatusCode.NotModified -> Orgs.ProjectsV2.Drafts.ProjectsCreateDraftItemForOrgResult.NotModified
            HttpStatusCode.Unauthorized -> Orgs.ProjectsV2.Drafts.ProjectsCreateDraftItemForOrgResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Orgs.ProjectsV2.Drafts.ProjectsCreateDraftItemForOrgResult.Forbidden(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsProjectsV2Fields(private val client: HttpClient) : Orgs.ProjectsV2.Fields {
    override suspend fun projectsListFieldsForOrg(org: String, projectNumber: Long, perPage: Long, after: String?, before: String?): Orgs.ProjectsV2.Fields.ProjectsListFieldsForOrgResult {
        val response = client.get("/orgs/$org/projectsV2/$projectNumber/fields") {
            parameter("per_page", perPage)
            after?.let { parameter("after", it) }
            before?.let { parameter("before", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.ProjectsV2.Fields.ProjectsListFieldsForOrgResult.OK(response.body())
            HttpStatusCode.NotModified -> Orgs.ProjectsV2.Fields.ProjectsListFieldsForOrgResult.NotModified
            HttpStatusCode.Unauthorized -> Orgs.ProjectsV2.Fields.ProjectsListFieldsForOrgResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Orgs.ProjectsV2.Fields.ProjectsListFieldsForOrgResult.Forbidden(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun projectsAddFieldForOrg(org: String, projectNumber: Long, body: Orgs.ProjectsV2.Fields.ProjectsAddFieldForOrgBody): Orgs.ProjectsV2.Fields.ProjectsAddFieldForOrgResult {
        val response = client.post("/orgs/$org/projectsV2/$projectNumber/fields") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> Orgs.ProjectsV2.Fields.ProjectsAddFieldForOrgResult.Created(response.body())
            HttpStatusCode.NotModified -> Orgs.ProjectsV2.Fields.ProjectsAddFieldForOrgResult.NotModified
            HttpStatusCode.Unauthorized -> Orgs.ProjectsV2.Fields.ProjectsAddFieldForOrgResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Orgs.ProjectsV2.Fields.ProjectsAddFieldForOrgResult.Forbidden(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.ProjectsV2.Fields.ProjectsAddFieldForOrgResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun projectsGetFieldForOrg(org: String, projectNumber: Long, fieldId: Long): Orgs.ProjectsV2.Fields.ProjectsGetFieldForOrgResult {
        val response = client.get("/orgs/$org/projectsV2/$projectNumber/fields/$fieldId")
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.ProjectsV2.Fields.ProjectsGetFieldForOrgResult.OK(response.body())
            HttpStatusCode.NotModified -> Orgs.ProjectsV2.Fields.ProjectsGetFieldForOrgResult.NotModified
            HttpStatusCode.Unauthorized -> Orgs.ProjectsV2.Fields.ProjectsGetFieldForOrgResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Orgs.ProjectsV2.Fields.ProjectsGetFieldForOrgResult.Forbidden(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsProjectsV2Items(private val client: HttpClient) : Orgs.ProjectsV2.Items {
    override suspend fun projectsListItemsForOrg(org: String, projectNumber: Long, perPage: Long, after: String?, before: String?, fields: Orgs.ProjectsV2.Items.ProjectsListItemsForOrgFields?, q: String?): Orgs.ProjectsV2.Items.ProjectsListItemsForOrgResult {
        val response = client.get("/orgs/$org/projectsV2/$projectNumber/items") {
            parameter("per_page", perPage)
            after?.let { parameter("after", it) }
            before?.let { parameter("before", it) }
            fields?.let { parameter("fields", it) }
            q?.let { parameter("q", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.ProjectsV2.Items.ProjectsListItemsForOrgResult.OK(response.body())
            HttpStatusCode.NotModified -> Orgs.ProjectsV2.Items.ProjectsListItemsForOrgResult.NotModified
            HttpStatusCode.Unauthorized -> Orgs.ProjectsV2.Items.ProjectsListItemsForOrgResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Orgs.ProjectsV2.Items.ProjectsListItemsForOrgResult.Forbidden(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun projectsAddItemForOrg(org: String, projectNumber: Long, body: Orgs.ProjectsV2.Items.ProjectsAddItemForOrgBody): Orgs.ProjectsV2.Items.ProjectsAddItemForOrgResult {
        val response = client.post("/orgs/$org/projectsV2/$projectNumber/items") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> Orgs.ProjectsV2.Items.ProjectsAddItemForOrgResult.Created(response.body())
            HttpStatusCode.NotModified -> Orgs.ProjectsV2.Items.ProjectsAddItemForOrgResult.NotModified
            HttpStatusCode.Unauthorized -> Orgs.ProjectsV2.Items.ProjectsAddItemForOrgResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Orgs.ProjectsV2.Items.ProjectsAddItemForOrgResult.Forbidden(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun projectsGetOrgItem(org: String, projectNumber: Long, itemId: Long, fields: Orgs.ProjectsV2.Items.ProjectsGetOrgItemFields?): Orgs.ProjectsV2.Items.ProjectsGetOrgItemResult {
        val response = client.get("/orgs/$org/projectsV2/$projectNumber/items/$itemId") {
            fields?.let { parameter("fields", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.ProjectsV2.Items.ProjectsGetOrgItemResult.OK(response.body())
            HttpStatusCode.NotModified -> Orgs.ProjectsV2.Items.ProjectsGetOrgItemResult.NotModified
            HttpStatusCode.Unauthorized -> Orgs.ProjectsV2.Items.ProjectsGetOrgItemResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Orgs.ProjectsV2.Items.ProjectsGetOrgItemResult.Forbidden(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun projectsDeleteItemForOrg(org: String, projectNumber: Long, itemId: Long): Orgs.ProjectsV2.Items.ProjectsDeleteItemForOrgResult {
        val response = client.delete("/orgs/$org/projectsV2/$projectNumber/items/$itemId")
        return when (response.status) {
            HttpStatusCode.NoContent -> Orgs.ProjectsV2.Items.ProjectsDeleteItemForOrgResult.NoContent
            HttpStatusCode.Unauthorized -> Orgs.ProjectsV2.Items.ProjectsDeleteItemForOrgResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Orgs.ProjectsV2.Items.ProjectsDeleteItemForOrgResult.Forbidden(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun projectsUpdateItemForOrg(org: String, projectNumber: Long, itemId: Long, body: Orgs.ProjectsV2.Items.ProjectsUpdateItemForOrgBody): Orgs.ProjectsV2.Items.ProjectsUpdateItemForOrgResult {
        val response = client.patch("/orgs/$org/projectsV2/$projectNumber/items/$itemId") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.ProjectsV2.Items.ProjectsUpdateItemForOrgResult.OK(response.body())
            HttpStatusCode.Unauthorized -> Orgs.ProjectsV2.Items.ProjectsUpdateItemForOrgResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Orgs.ProjectsV2.Items.ProjectsUpdateItemForOrgResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.ProjectsV2.Items.ProjectsUpdateItemForOrgResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.ProjectsV2.Items.ProjectsUpdateItemForOrgResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsProjectsV2Views(private val client: HttpClient) : Orgs.ProjectsV2.Views {
    override val items: Orgs.ProjectsV2.Views.ItemsApi = KtorOrgsProjectsV2ViewsItemsApi(client)

    override suspend fun projectsCreateViewForOrg(org: String, projectNumber: Long, body: Orgs.ProjectsV2.Views.ProjectsCreateViewForOrgBody): Orgs.ProjectsV2.Views.ProjectsCreateViewForOrgResult {
        val response = client.post("/orgs/$org/projectsV2/$projectNumber/views") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> Orgs.ProjectsV2.Views.ProjectsCreateViewForOrgResult.Created(response.body())
            HttpStatusCode.NotModified -> Orgs.ProjectsV2.Views.ProjectsCreateViewForOrgResult.NotModified
            HttpStatusCode.Unauthorized -> Orgs.ProjectsV2.Views.ProjectsCreateViewForOrgResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Orgs.ProjectsV2.Views.ProjectsCreateViewForOrgResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.ProjectsV2.Views.ProjectsCreateViewForOrgResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.ProjectsV2.Views.ProjectsCreateViewForOrgResult.UnprocessableEntity(response.body())
            HttpStatusCode.ServiceUnavailable -> Orgs.ProjectsV2.Views.ProjectsCreateViewForOrgResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsProjectsV2ViewsItemsApi(private val client: HttpClient) : Orgs.ProjectsV2.Views.ItemsApi {
    override suspend fun projectsListViewItemsForOrg(org: String, projectNumber: Long, viewNumber: Long, perPage: Long, after: String?, before: String?, fields: Orgs.ProjectsV2.Views.ItemsApi.ProjectsListViewItemsForOrgFields?): Orgs.ProjectsV2.Views.ItemsApi.ProjectsListViewItemsForOrgResult {
        val response = client.get("/orgs/$org/projectsV2/$projectNumber/views/$viewNumber/items") {
            parameter("per_page", perPage)
            after?.let { parameter("after", it) }
            before?.let { parameter("before", it) }
            fields?.let { parameter("fields", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.ProjectsV2.Views.ItemsApi.ProjectsListViewItemsForOrgResult.OK(response.body())
            HttpStatusCode.NotModified -> Orgs.ProjectsV2.Views.ItemsApi.ProjectsListViewItemsForOrgResult.NotModified
            HttpStatusCode.Unauthorized -> Orgs.ProjectsV2.Views.ItemsApi.ProjectsListViewItemsForOrgResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Orgs.ProjectsV2.Views.ItemsApi.ProjectsListViewItemsForOrgResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.ProjectsV2.Views.ItemsApi.ProjectsListViewItemsForOrgResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsProperties(private val client: HttpClient) : Orgs.Properties {
    override val schema: Orgs.Properties.Schema = KtorOrgsPropertiesSchema(client)

    override val values: Orgs.Properties.Values = KtorOrgsPropertiesValues(client)
}

internal class KtorOrgsPropertiesSchema(private val client: HttpClient) : Orgs.Properties.Schema {
    override suspend fun orgsCustomPropertiesForReposGetOrganizationDefinitions(org: String): Orgs.Properties.Schema.OrgsCustomPropertiesForReposGetOrganizationDefinitionsResult {
        val response = client.get("/orgs/$org/properties/schema")
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Properties.Schema.OrgsCustomPropertiesForReposGetOrganizationDefinitionsResult.OK(response.body())
            HttpStatusCode.Forbidden -> Orgs.Properties.Schema.OrgsCustomPropertiesForReposGetOrganizationDefinitionsResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.Properties.Schema.OrgsCustomPropertiesForReposGetOrganizationDefinitionsResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun orgsCustomPropertiesForReposCreateOrUpdateOrganizationDefinitions(org: String, body: Orgs.Properties.Schema.OrgsCustomPropertiesForReposCreateOrUpdateOrganizationDefinitionsBody): Orgs.Properties.Schema.OrgsCustomPropertiesForReposCreateOrUpdateOrganizationDefinitionsResult {
        val response = client.patch("/orgs/$org/properties/schema") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Properties.Schema.OrgsCustomPropertiesForReposCreateOrUpdateOrganizationDefinitionsResult.OK(response.body())
            HttpStatusCode.Forbidden -> Orgs.Properties.Schema.OrgsCustomPropertiesForReposCreateOrUpdateOrganizationDefinitionsResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.Properties.Schema.OrgsCustomPropertiesForReposCreateOrUpdateOrganizationDefinitionsResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun orgsCustomPropertiesForReposGetOrganizationDefinition(org: String, customPropertyName: String): Orgs.Properties.Schema.OrgsCustomPropertiesForReposGetOrganizationDefinitionResult {
        val response = client.get("/orgs/$org/properties/schema/$customPropertyName")
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Properties.Schema.OrgsCustomPropertiesForReposGetOrganizationDefinitionResult.OK(response.body())
            HttpStatusCode.Forbidden -> Orgs.Properties.Schema.OrgsCustomPropertiesForReposGetOrganizationDefinitionResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.Properties.Schema.OrgsCustomPropertiesForReposGetOrganizationDefinitionResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun orgsCustomPropertiesForReposCreateOrUpdateOrganizationDefinition(org: String, customPropertyName: String, body: CustomPropertySetPayload): Orgs.Properties.Schema.OrgsCustomPropertiesForReposCreateOrUpdateOrganizationDefinitionResult {
        val response = client.put("/orgs/$org/properties/schema/$customPropertyName") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Properties.Schema.OrgsCustomPropertiesForReposCreateOrUpdateOrganizationDefinitionResult.OK(response.body())
            HttpStatusCode.Forbidden -> Orgs.Properties.Schema.OrgsCustomPropertiesForReposCreateOrUpdateOrganizationDefinitionResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.Properties.Schema.OrgsCustomPropertiesForReposCreateOrUpdateOrganizationDefinitionResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun orgsCustomPropertiesForReposDeleteOrganizationDefinition(org: String, customPropertyName: String): Orgs.Properties.Schema.OrgsCustomPropertiesForReposDeleteOrganizationDefinitionResult {
        val response = client.delete("/orgs/$org/properties/schema/$customPropertyName")
        return when (response.status) {
            HttpStatusCode.NoContent -> Orgs.Properties.Schema.OrgsCustomPropertiesForReposDeleteOrganizationDefinitionResult.NoContent
            HttpStatusCode.Forbidden -> Orgs.Properties.Schema.OrgsCustomPropertiesForReposDeleteOrganizationDefinitionResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.Properties.Schema.OrgsCustomPropertiesForReposDeleteOrganizationDefinitionResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsPropertiesValues(private val client: HttpClient) : Orgs.Properties.Values {
    override suspend fun orgsCustomPropertiesForReposGetOrganizationValues(org: String, page: Long, perPage: Long, repositoryQuery: String?): Orgs.Properties.Values.OrgsCustomPropertiesForReposGetOrganizationValuesResult {
        val response = client.get("/orgs/$org/properties/values") {
            parameter("page", page)
            parameter("per_page", perPage)
            repositoryQuery?.let { parameter("repository_query", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Properties.Values.OrgsCustomPropertiesForReposGetOrganizationValuesResult.OK(response.body())
            HttpStatusCode.Forbidden -> Orgs.Properties.Values.OrgsCustomPropertiesForReposGetOrganizationValuesResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.Properties.Values.OrgsCustomPropertiesForReposGetOrganizationValuesResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun orgsCustomPropertiesForReposCreateOrUpdateOrganizationValues(org: String, body: Orgs.Properties.Values.OrgsCustomPropertiesForReposCreateOrUpdateOrganizationValuesBody): Orgs.Properties.Values.OrgsCustomPropertiesForReposCreateOrUpdateOrganizationValuesResult {
        val response = client.patch("/orgs/$org/properties/values") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.NoContent -> Orgs.Properties.Values.OrgsCustomPropertiesForReposCreateOrUpdateOrganizationValuesResult.NoContent
            HttpStatusCode.Forbidden -> Orgs.Properties.Values.OrgsCustomPropertiesForReposCreateOrUpdateOrganizationValuesResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.Properties.Values.OrgsCustomPropertiesForReposCreateOrUpdateOrganizationValuesResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.Properties.Values.OrgsCustomPropertiesForReposCreateOrUpdateOrganizationValuesResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsPublicMembers(private val client: HttpClient) : Orgs.PublicMembers {
    override suspend fun orgsListPublicMembers(org: String, page: Long, perPage: Long): List<SimpleUser> =
        client.get("/orgs/$org/public_members") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()

    override suspend fun orgsCheckPublicMembershipForUser(org: String, username: String): Orgs.PublicMembers.OrgsCheckPublicMembershipForUserResult {
        val response = client.get("/orgs/$org/public_members/$username")
        return when (response.status) {
            HttpStatusCode.NoContent -> Orgs.PublicMembers.OrgsCheckPublicMembershipForUserResult.NoContent
            HttpStatusCode.NotFound -> Orgs.PublicMembers.OrgsCheckPublicMembershipForUserResult.NotFound
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun orgsSetPublicMembershipForAuthenticatedUser(org: String, username: String): Orgs.PublicMembers.OrgsSetPublicMembershipForAuthenticatedUserResult {
        val response = client.put("/orgs/$org/public_members/$username")
        return when (response.status) {
            HttpStatusCode.NoContent -> Orgs.PublicMembers.OrgsSetPublicMembershipForAuthenticatedUserResult.NoContent
            HttpStatusCode.Forbidden -> Orgs.PublicMembers.OrgsSetPublicMembershipForAuthenticatedUserResult.Forbidden(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun orgsRemovePublicMembershipForAuthenticatedUser(org: String, username: String): Unit =
        client.delete("/orgs/$org/public_members/$username").body()
}

internal class KtorOrgsRepos(private val client: HttpClient) : Orgs.Repos {
    override suspend fun reposListForOrg(org: String, page: Long, perPage: Long, sort: Orgs.Repos.Sort, type: Orgs.Repos.Type, direction: Orgs.Repos.Direction?): List<MinimalRepository> =
        client.get("/orgs/$org/repos") {
            parameter("page", page)
            parameter("per_page", perPage)
            parameter("sort", sort)
            parameter("type", type)
            direction?.let { parameter("direction", it) }
        }.body()

    override suspend fun reposCreateInOrg(org: String, body: Orgs.Repos.ReposCreateInOrgBody): Orgs.Repos.ReposCreateInOrgResult {
        val response = client.post("/orgs/$org/repos") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> Orgs.Repos.ReposCreateInOrgResult.Created(response.body())
            HttpStatusCode.Forbidden -> Orgs.Repos.ReposCreateInOrgResult.Forbidden(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.Repos.ReposCreateInOrgResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsRulesets(private val client: HttpClient) : Orgs.Rulesets {
    override val ruleSuites: Orgs.Rulesets.RuleSuites = KtorOrgsRulesetsRuleSuites(client)

    override val history: Orgs.Rulesets.History = KtorOrgsRulesetsHistory(client)

    override suspend fun reposGetOrgRulesets(org: String, page: Long, perPage: Long, targets: String?): Orgs.Rulesets.ReposGetOrgRulesetsResult {
        val response = client.get("/orgs/$org/rulesets") {
            parameter("page", page)
            parameter("per_page", perPage)
            targets?.let { parameter("targets", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Rulesets.ReposGetOrgRulesetsResult.OK(response.body())
            HttpStatusCode.NotFound -> Orgs.Rulesets.ReposGetOrgRulesetsResult.NotFound(response.body())
            HttpStatusCode.InternalServerError -> Orgs.Rulesets.ReposGetOrgRulesetsResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposCreateOrgRuleset(org: String, body: Orgs.Rulesets.ReposCreateOrgRulesetBody): Orgs.Rulesets.ReposCreateOrgRulesetResult {
        val response = client.post("/orgs/$org/rulesets") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> Orgs.Rulesets.ReposCreateOrgRulesetResult.Created(response.body())
            HttpStatusCode.NotFound -> Orgs.Rulesets.ReposCreateOrgRulesetResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.Rulesets.ReposCreateOrgRulesetResult.UnprocessableEntity(response.body())
            HttpStatusCode.InternalServerError -> Orgs.Rulesets.ReposCreateOrgRulesetResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposGetOrgRuleset(org: String, rulesetId: Long): Orgs.Rulesets.ReposGetOrgRulesetResult {
        val response = client.get("/orgs/$org/rulesets/$rulesetId")
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Rulesets.ReposGetOrgRulesetResult.OK(response.body())
            HttpStatusCode.NotFound -> Orgs.Rulesets.ReposGetOrgRulesetResult.NotFound(response.body())
            HttpStatusCode.InternalServerError -> Orgs.Rulesets.ReposGetOrgRulesetResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposUpdateOrgRuleset(org: String, rulesetId: Long, body: Orgs.Rulesets.ReposUpdateOrgRulesetBody?): Orgs.Rulesets.ReposUpdateOrgRulesetResult {
        val response = client.put("/orgs/$org/rulesets/$rulesetId") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Rulesets.ReposUpdateOrgRulesetResult.OK(response.body())
            HttpStatusCode.NotFound -> Orgs.Rulesets.ReposUpdateOrgRulesetResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.Rulesets.ReposUpdateOrgRulesetResult.UnprocessableEntity(response.body())
            HttpStatusCode.InternalServerError -> Orgs.Rulesets.ReposUpdateOrgRulesetResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposDeleteOrgRuleset(org: String, rulesetId: Long): Orgs.Rulesets.ReposDeleteOrgRulesetResult {
        val response = client.delete("/orgs/$org/rulesets/$rulesetId")
        return when (response.status) {
            HttpStatusCode.NoContent -> Orgs.Rulesets.ReposDeleteOrgRulesetResult.NoContent
            HttpStatusCode.NotFound -> Orgs.Rulesets.ReposDeleteOrgRulesetResult.NotFound(response.body())
            HttpStatusCode.InternalServerError -> Orgs.Rulesets.ReposDeleteOrgRulesetResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsRulesetsRuleSuites(private val client: HttpClient) : Orgs.Rulesets.RuleSuites {
    override suspend fun reposGetOrgRuleSuites(org: String, page: Long, perPage: Long, ruleSuiteResult: Orgs.Rulesets.RuleSuites.RuleSuiteResult, timePeriod: Orgs.Rulesets.RuleSuites.TimePeriod, actorName: String?, ref: String?, repositoryName: String?): Orgs.Rulesets.RuleSuites.ReposGetOrgRuleSuitesResult {
        val response = client.get("/orgs/$org/rulesets/rule-suites") {
            parameter("page", page)
            parameter("per_page", perPage)
            parameter("rule_suite_result", ruleSuiteResult)
            parameter("time_period", timePeriod)
            actorName?.let { parameter("actor_name", it) }
            ref?.let { parameter("ref", it) }
            repositoryName?.let { parameter("repository_name", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Rulesets.RuleSuites.ReposGetOrgRuleSuitesResult.OK(response.body())
            HttpStatusCode.NotFound -> Orgs.Rulesets.RuleSuites.ReposGetOrgRuleSuitesResult.NotFound(response.body())
            HttpStatusCode.InternalServerError -> Orgs.Rulesets.RuleSuites.ReposGetOrgRuleSuitesResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposGetOrgRuleSuite(org: String, ruleSuiteId: Long): Orgs.Rulesets.RuleSuites.ReposGetOrgRuleSuiteResult {
        val response = client.get("/orgs/$org/rulesets/rule-suites/$ruleSuiteId")
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Rulesets.RuleSuites.ReposGetOrgRuleSuiteResult.OK(response.body())
            HttpStatusCode.NotFound -> Orgs.Rulesets.RuleSuites.ReposGetOrgRuleSuiteResult.NotFound(response.body())
            HttpStatusCode.InternalServerError -> Orgs.Rulesets.RuleSuites.ReposGetOrgRuleSuiteResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsRulesetsHistory(private val client: HttpClient) : Orgs.Rulesets.History {
    override suspend fun orgsGetOrgRulesetHistory(org: String, rulesetId: Long, page: Long, perPage: Long): Orgs.Rulesets.History.OrgsGetOrgRulesetHistoryResult {
        val response = client.get("/orgs/$org/rulesets/$rulesetId/history") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Rulesets.History.OrgsGetOrgRulesetHistoryResult.OK(response.body())
            HttpStatusCode.NotFound -> Orgs.Rulesets.History.OrgsGetOrgRulesetHistoryResult.NotFound(response.body())
            HttpStatusCode.InternalServerError -> Orgs.Rulesets.History.OrgsGetOrgRulesetHistoryResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun orgsGetOrgRulesetVersion(org: String, rulesetId: Long, versionId: Long): Orgs.Rulesets.History.OrgsGetOrgRulesetVersionResult {
        val response = client.get("/orgs/$org/rulesets/$rulesetId/history/$versionId")
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Rulesets.History.OrgsGetOrgRulesetVersionResult.OK(response.body())
            HttpStatusCode.NotFound -> Orgs.Rulesets.History.OrgsGetOrgRulesetVersionResult.NotFound(response.body())
            HttpStatusCode.InternalServerError -> Orgs.Rulesets.History.OrgsGetOrgRulesetVersionResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsSecretScanning(private val client: HttpClient) : Orgs.SecretScanning {
    override val alerts: Orgs.SecretScanning.Alerts = KtorOrgsSecretScanningAlerts(client)

    override val patternConfigurations: Orgs.SecretScanning.PatternConfigurations = KtorOrgsSecretScanningPatternConfigurations(client)
}

internal class KtorOrgsSecretScanningAlerts(private val client: HttpClient) : Orgs.SecretScanning.Alerts {
    override suspend fun secretScanningListAlertsForOrg(org: String, direction: Orgs.SecretScanning.Alerts.Direction, hideSecret: Boolean, isMultiRepo: Boolean, isPubliclyLeaked: Boolean, page: Long, perPage: Long, sort: Orgs.SecretScanning.Alerts.Sort, after: String?, assignee: String?, before: String?, resolution: String?, secretType: String?, state: Orgs.SecretScanning.Alerts.State?, validity: String?): Orgs.SecretScanning.Alerts.SecretScanningListAlertsForOrgResult {
        val response = client.get("/orgs/$org/secret-scanning/alerts") {
            parameter("direction", direction)
            parameter("hide_secret", hideSecret)
            parameter("is_multi_repo", isMultiRepo)
            parameter("is_publicly_leaked", isPubliclyLeaked)
            parameter("page", page)
            parameter("per_page", perPage)
            parameter("sort", sort)
            after?.let { parameter("after", it) }
            assignee?.let { parameter("assignee", it) }
            before?.let { parameter("before", it) }
            resolution?.let { parameter("resolution", it) }
            secretType?.let { parameter("secret_type", it) }
            state?.let { parameter("state", it) }
            validity?.let { parameter("validity", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.SecretScanning.Alerts.SecretScanningListAlertsForOrgResult.OK(response.body())
            HttpStatusCode.NotFound -> Orgs.SecretScanning.Alerts.SecretScanningListAlertsForOrgResult.NotFound(response.body())
            HttpStatusCode.ServiceUnavailable -> Orgs.SecretScanning.Alerts.SecretScanningListAlertsForOrgResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsSecretScanningPatternConfigurations(private val client: HttpClient) : Orgs.SecretScanning.PatternConfigurations {
    override suspend fun secretScanningListOrgPatternConfigs(org: String): Orgs.SecretScanning.PatternConfigurations.SecretScanningListOrgPatternConfigsResult {
        val response = client.get("/orgs/$org/secret-scanning/pattern-configurations")
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.SecretScanning.PatternConfigurations.SecretScanningListOrgPatternConfigsResult.OK(response.body())
            HttpStatusCode.Forbidden -> Orgs.SecretScanning.PatternConfigurations.SecretScanningListOrgPatternConfigsResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.SecretScanning.PatternConfigurations.SecretScanningListOrgPatternConfigsResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun secretScanningUpdateOrgPatternConfigs(org: String, body: Orgs.SecretScanning.PatternConfigurations.SecretScanningUpdateOrgPatternConfigsBody): Orgs.SecretScanning.PatternConfigurations.SecretScanningUpdateOrgPatternConfigsResult {
        val response = client.patch("/orgs/$org/secret-scanning/pattern-configurations") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.SecretScanning.PatternConfigurations.SecretScanningUpdateOrgPatternConfigsResult.OK(response.body())
            HttpStatusCode.BadRequest -> Orgs.SecretScanning.PatternConfigurations.SecretScanningUpdateOrgPatternConfigsResult.BadRequest(response.body())
            HttpStatusCode.Forbidden -> Orgs.SecretScanning.PatternConfigurations.SecretScanningUpdateOrgPatternConfigsResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.SecretScanning.PatternConfigurations.SecretScanningUpdateOrgPatternConfigsResult.NotFound(response.body())
            HttpStatusCode.Conflict -> Orgs.SecretScanning.PatternConfigurations.SecretScanningUpdateOrgPatternConfigsResult.Conflict(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.SecretScanning.PatternConfigurations.SecretScanningUpdateOrgPatternConfigsResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsSecurityAdvisories(private val client: HttpClient) : Orgs.SecurityAdvisories {
    override suspend fun securityAdvisoriesListOrgRepositoryAdvisories(org: String, direction: Orgs.SecurityAdvisories.Direction, perPage: Long, sort: Orgs.SecurityAdvisories.Sort, after: String?, before: String?, state: Orgs.SecurityAdvisories.State?): Orgs.SecurityAdvisories.SecurityAdvisoriesListOrgRepositoryAdvisoriesResult {
        val response = client.get("/orgs/$org/security-advisories") {
            parameter("direction", direction)
            parameter("per_page", perPage)
            parameter("sort", sort)
            after?.let { parameter("after", it) }
            before?.let { parameter("before", it) }
            state?.let { parameter("state", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.SecurityAdvisories.SecurityAdvisoriesListOrgRepositoryAdvisoriesResult.OK(response.body())
            HttpStatusCode.BadRequest -> Orgs.SecurityAdvisories.SecurityAdvisoriesListOrgRepositoryAdvisoriesResult.BadRequest(response.body())
            HttpStatusCode.NotFound -> Orgs.SecurityAdvisories.SecurityAdvisoriesListOrgRepositoryAdvisoriesResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsSecurityManagers(private val client: HttpClient) : Orgs.SecurityManagers {
    override val teams: Orgs.SecurityManagers.TeamsApi = KtorOrgsSecurityManagersTeamsApi(client)

    @Deprecated("Deprecated by the API provider")
    override suspend fun orgsListSecurityManagerTeams(org: String): List<TeamSimple> =
        client.get("/orgs/$org/security-managers").body()
}

internal class KtorOrgsSecurityManagersTeamsApi(private val client: HttpClient) : Orgs.SecurityManagers.TeamsApi {
    @Deprecated("Deprecated by the API provider")
    override suspend fun orgsAddSecurityManagerTeam(org: String, teamSlug: String): Unit =
        client.put("/orgs/$org/security-managers/teams/$teamSlug").body()

    @Deprecated("Deprecated by the API provider")
    override suspend fun orgsRemoveSecurityManagerTeam(org: String, teamSlug: String): Unit =
        client.delete("/orgs/$org/security-managers/teams/$teamSlug").body()
}

internal class KtorOrgsSettings(private val client: HttpClient) : Orgs.Settings {
    override val immutableReleases: Orgs.Settings.ImmutableReleases = KtorOrgsSettingsImmutableReleases(client)

    override val networkConfigurations: Orgs.Settings.NetworkConfigurations = KtorOrgsSettingsNetworkConfigurations(client)

    override val networkSettings: Orgs.Settings.NetworkSettings = KtorOrgsSettingsNetworkSettings(client)
}

internal class KtorOrgsSettingsImmutableReleases(private val client: HttpClient) : Orgs.Settings.ImmutableReleases {
    override val repositories: Orgs.Settings.ImmutableReleases.Repositories = KtorOrgsSettingsImmutableReleasesRepositories(client)

    override suspend fun orgsGetImmutableReleasesSettings(org: String): ImmutableReleasesOrganizationSettings =
        client.get("/orgs/$org/settings/immutable-releases").body()

    override suspend fun orgsSetImmutableReleasesSettings(org: String, body: Orgs.Settings.ImmutableReleases.OrgsSetImmutableReleasesSettingsBody): Unit =
        client.put("/orgs/$org/settings/immutable-releases") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()
}

internal class KtorOrgsSettingsImmutableReleasesRepositories(private val client: HttpClient) : Orgs.Settings.ImmutableReleases.Repositories {
    override suspend fun orgsGetImmutableReleasesSettingsRepositories(org: String, page: Long, perPage: Long): Orgs.Settings.ImmutableReleases.Repositories.OrgsGetImmutableReleasesSettingsRepositoriesResponse =
        client.get("/orgs/$org/settings/immutable-releases/repositories") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()

    override suspend fun orgsSetImmutableReleasesSettingsRepositories(org: String, body: Orgs.Settings.ImmutableReleases.Repositories.OrgsSetImmutableReleasesSettingsRepositoriesBody): Unit =
        client.put("/orgs/$org/settings/immutable-releases/repositories") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()

    override suspend fun orgsEnableSelectedRepositoryImmutableReleasesOrganization(org: String, repositoryId: Long): Unit =
        client.put("/orgs/$org/settings/immutable-releases/repositories/$repositoryId").body()

    override suspend fun orgsDisableSelectedRepositoryImmutableReleasesOrganization(org: String, repositoryId: Long): Unit =
        client.delete("/orgs/$org/settings/immutable-releases/repositories/$repositoryId").body()
}

internal class KtorOrgsSettingsNetworkConfigurations(private val client: HttpClient) : Orgs.Settings.NetworkConfigurations {
    override suspend fun hostedComputeListNetworkConfigurationsForOrg(org: String, page: Long, perPage: Long): Orgs.Settings.NetworkConfigurations.HostedComputeListNetworkConfigurationsForOrgResponse =
        client.get("/orgs/$org/settings/network-configurations") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()

    override suspend fun hostedComputeCreateNetworkConfigurationForOrg(org: String, body: Orgs.Settings.NetworkConfigurations.HostedComputeCreateNetworkConfigurationForOrgBody): NetworkConfiguration =
        client.post("/orgs/$org/settings/network-configurations") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()

    override suspend fun hostedComputeGetNetworkConfigurationForOrg(org: String, networkConfigurationId: String): NetworkConfiguration =
        client.get("/orgs/$org/settings/network-configurations/$networkConfigurationId").body()

    override suspend fun hostedComputeDeleteNetworkConfigurationFromOrg(org: String, networkConfigurationId: String): Unit =
        client.delete("/orgs/$org/settings/network-configurations/$networkConfigurationId").body()

    override suspend fun hostedComputeUpdateNetworkConfigurationForOrg(org: String, networkConfigurationId: String, body: Orgs.Settings.NetworkConfigurations.HostedComputeUpdateNetworkConfigurationForOrgBody): NetworkConfiguration =
        client.patch("/orgs/$org/settings/network-configurations/$networkConfigurationId") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()
}

internal class KtorOrgsSettingsNetworkSettings(private val client: HttpClient) : Orgs.Settings.NetworkSettings {
    override suspend fun hostedComputeGetNetworkSettingsForOrg(org: String, networkSettingsId: String): NetworkSettings =
        client.get("/orgs/$org/settings/network-settings/$networkSettingsId").body()
}

internal class KtorOrgsTeam(private val client: HttpClient) : Orgs.Team {
    override val copilot: Orgs.Team.CopilotApi = KtorOrgsTeamCopilotApi(client)
}

internal class KtorOrgsTeamCopilotApi(private val client: HttpClient) : Orgs.Team.CopilotApi {
    override val metrics: Orgs.Team.CopilotApi.Metrics = KtorOrgsTeamCopilotApiMetrics(client)
}

internal class KtorOrgsTeamCopilotApiMetrics(private val client: HttpClient) : Orgs.Team.CopilotApi.Metrics {
    override suspend fun copilotCopilotMetricsForTeam(org: String, teamSlug: String, page: Long, perPage: Long, since: String?, until: String?): Orgs.Team.CopilotApi.Metrics.CopilotCopilotMetricsForTeamResult {
        val response = client.get("/orgs/$org/team/$teamSlug/copilot/metrics") {
            parameter("page", page)
            parameter("per_page", perPage)
            since?.let { parameter("since", it) }
            until?.let { parameter("until", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Team.CopilotApi.Metrics.CopilotCopilotMetricsForTeamResult.OK(response.body())
            HttpStatusCode.Forbidden -> Orgs.Team.CopilotApi.Metrics.CopilotCopilotMetricsForTeamResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.Team.CopilotApi.Metrics.CopilotCopilotMetricsForTeamResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.Team.CopilotApi.Metrics.CopilotCopilotMetricsForTeamResult.UnprocessableEntity(response.body())
            HttpStatusCode.InternalServerError -> Orgs.Team.CopilotApi.Metrics.CopilotCopilotMetricsForTeamResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsTeams(private val client: HttpClient) : Orgs.Teams {
    override val invitations: Orgs.Teams.InvitationsApi = KtorOrgsTeamsInvitationsApi(client)

    override val members: Orgs.Teams.MembersApi = KtorOrgsTeamsMembersApi(client)

    override val memberships: Orgs.Teams.MembershipsApi = KtorOrgsTeamsMembershipsApi(client)

    override val repos: Orgs.Teams.ReposApi = KtorOrgsTeamsReposApi(client)

    override val teams: Orgs.Teams.TeamsApi = KtorOrgsTeamsTeamsApi(client)

    override suspend fun teamsList(org: String, page: Long, perPage: Long, teamType: Orgs.Teams.TeamType): Orgs.Teams.TeamsListResult {
        val response = client.get("/orgs/$org/teams") {
            parameter("page", page)
            parameter("per_page", perPage)
            parameter("team_type", teamType)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Teams.TeamsListResult.OK(response.body())
            HttpStatusCode.Forbidden -> Orgs.Teams.TeamsListResult.Forbidden(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun teamsCreate(org: String, body: Orgs.Teams.TeamsCreateBody): Orgs.Teams.TeamsCreateResult {
        val response = client.post("/orgs/$org/teams") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> Orgs.Teams.TeamsCreateResult.Created(response.body())
            HttpStatusCode.Forbidden -> Orgs.Teams.TeamsCreateResult.Forbidden(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.Teams.TeamsCreateResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun teamsGetByName(org: String, teamSlug: String): Orgs.Teams.TeamsGetByNameResult {
        val response = client.get("/orgs/$org/teams/$teamSlug")
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Teams.TeamsGetByNameResult.OK(response.body())
            HttpStatusCode.NotFound -> Orgs.Teams.TeamsGetByNameResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun teamsDeleteInOrg(org: String, teamSlug: String): Orgs.Teams.TeamsDeleteInOrgResult {
        val response = client.delete("/orgs/$org/teams/$teamSlug")
        return when (response.status) {
            HttpStatusCode.NoContent -> Orgs.Teams.TeamsDeleteInOrgResult.NoContent
            HttpStatusCode.UnprocessableEntity -> Orgs.Teams.TeamsDeleteInOrgResult.UnprocessableEntity
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun teamsUpdateInOrg(org: String, teamSlug: String, body: Orgs.Teams.TeamsUpdateInOrgBody?): Orgs.Teams.TeamsUpdateInOrgResult {
        val response = client.patch("/orgs/$org/teams/$teamSlug") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Teams.TeamsUpdateInOrgResult.OK(response.body())
            HttpStatusCode.Created -> Orgs.Teams.TeamsUpdateInOrgResult.Created(response.body())
            HttpStatusCode.Forbidden -> Orgs.Teams.TeamsUpdateInOrgResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Orgs.Teams.TeamsUpdateInOrgResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.Teams.TeamsUpdateInOrgResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsTeamsInvitationsApi(private val client: HttpClient) : Orgs.Teams.InvitationsApi {
    override suspend fun teamsListPendingInvitationsInOrg(org: String, teamSlug: String, page: Long, perPage: Long): Orgs.Teams.InvitationsApi.TeamsListPendingInvitationsInOrgResult {
        val response = client.get("/orgs/$org/teams/$teamSlug/invitations") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Teams.InvitationsApi.TeamsListPendingInvitationsInOrgResult.OK(response.body())
            HttpStatusCode.UnprocessableEntity -> Orgs.Teams.InvitationsApi.TeamsListPendingInvitationsInOrgResult.UnprocessableEntity
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsTeamsMembersApi(private val client: HttpClient) : Orgs.Teams.MembersApi {
    override suspend fun teamsListMembersInOrg(org: String, teamSlug: String, page: Long, perPage: Long, role: Orgs.Teams.MembersApi.Role): List<SimpleUser> =
        client.get("/orgs/$org/teams/$teamSlug/members") {
            parameter("page", page)
            parameter("per_page", perPage)
            parameter("role", role)
        }.body()
}

internal class KtorOrgsTeamsMembershipsApi(private val client: HttpClient) : Orgs.Teams.MembershipsApi {
    override suspend fun teamsGetMembershipForUserInOrg(org: String, teamSlug: String, username: String): Orgs.Teams.MembershipsApi.TeamsGetMembershipForUserInOrgResult {
        val response = client.get("/orgs/$org/teams/$teamSlug/memberships/$username")
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Teams.MembershipsApi.TeamsGetMembershipForUserInOrgResult.OK(response.body())
            HttpStatusCode.NotFound -> Orgs.Teams.MembershipsApi.TeamsGetMembershipForUserInOrgResult.NotFound
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun teamsAddOrUpdateMembershipForUserInOrg(org: String, teamSlug: String, username: String, body: Orgs.Teams.MembershipsApi.TeamsAddOrUpdateMembershipForUserInOrgBody?): Orgs.Teams.MembershipsApi.TeamsAddOrUpdateMembershipForUserInOrgResult {
        val response = client.put("/orgs/$org/teams/$teamSlug/memberships/$username") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Teams.MembershipsApi.TeamsAddOrUpdateMembershipForUserInOrgResult.OK(response.body())
            HttpStatusCode.Forbidden -> Orgs.Teams.MembershipsApi.TeamsAddOrUpdateMembershipForUserInOrgResult.Forbidden
            HttpStatusCode.UnprocessableEntity -> Orgs.Teams.MembershipsApi.TeamsAddOrUpdateMembershipForUserInOrgResult.UnprocessableEntity
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun teamsRemoveMembershipForUserInOrg(org: String, teamSlug: String, username: String): Orgs.Teams.MembershipsApi.TeamsRemoveMembershipForUserInOrgResult {
        val response = client.delete("/orgs/$org/teams/$teamSlug/memberships/$username")
        return when (response.status) {
            HttpStatusCode.NoContent -> Orgs.Teams.MembershipsApi.TeamsRemoveMembershipForUserInOrgResult.NoContent
            HttpStatusCode.Forbidden -> Orgs.Teams.MembershipsApi.TeamsRemoveMembershipForUserInOrgResult.Forbidden
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrgsTeamsReposApi(private val client: HttpClient) : Orgs.Teams.ReposApi {
    override suspend fun teamsListReposInOrg(org: String, teamSlug: String, page: Long, perPage: Long): List<MinimalRepository> =
        client.get("/orgs/$org/teams/$teamSlug/repos") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()

    override suspend fun teamsCheckPermissionsForRepoInOrg(org: String, teamSlug: String, owner: String, repo: String): Orgs.Teams.ReposApi.TeamsCheckPermissionsForRepoInOrgResult {
        val response = client.get("/orgs/$org/teams/$teamSlug/repos/$owner/$repo")
        return when (response.status) {
            HttpStatusCode.OK -> Orgs.Teams.ReposApi.TeamsCheckPermissionsForRepoInOrgResult.OK(response.body())
            HttpStatusCode.NoContent -> Orgs.Teams.ReposApi.TeamsCheckPermissionsForRepoInOrgResult.NoContent
            HttpStatusCode.NotFound -> Orgs.Teams.ReposApi.TeamsCheckPermissionsForRepoInOrgResult.NotFound
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun teamsAddOrUpdateRepoPermissionsInOrg(org: String, teamSlug: String, owner: String, repo: String, body: Orgs.Teams.ReposApi.TeamsAddOrUpdateRepoPermissionsInOrgBody?): Unit =
        client.put("/orgs/$org/teams/$teamSlug/repos/$owner/$repo") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }.body()

    override suspend fun teamsRemoveRepoInOrg(org: String, teamSlug: String, owner: String, repo: String): Unit =
        client.delete("/orgs/$org/teams/$teamSlug/repos/$owner/$repo").body()
}

internal class KtorOrgsTeamsTeamsApi(private val client: HttpClient) : Orgs.Teams.TeamsApi {
    override suspend fun teamsListChildInOrg(org: String, teamSlug: String, page: Long, perPage: Long): List<Team> =
        client.get("/orgs/$org/teams/$teamSlug/teams") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()
}
