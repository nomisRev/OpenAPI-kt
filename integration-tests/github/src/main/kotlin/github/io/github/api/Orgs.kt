package io.github.api

import io.github.model.ActionsArtifactAndLogRetention
import io.github.model.ActionsArtifactAndLogRetentionResponse
import io.github.model.ActionsCacheUsageByRepository
import io.github.model.ActionsCacheUsageOrgEnterprise
import io.github.model.ActionsForkPrContributorApproval
import io.github.model.ActionsForkPrWorkflowsPrivateRepos
import io.github.model.ActionsForkPrWorkflowsPrivateReposRequest
import io.github.model.ActionsGetDefaultWorkflowPermissions
import io.github.model.ActionsHostedRunner
import io.github.model.ActionsHostedRunnerCuratedImage
import io.github.model.ActionsHostedRunnerCustomImage
import io.github.model.ActionsHostedRunnerCustomImageVersion
import io.github.model.ActionsHostedRunnerLimits
import io.github.model.ActionsHostedRunnerMachineSpec
import io.github.model.ActionsOrganizationPermissions
import io.github.model.ActionsPublicKey
import io.github.model.ActionsSetDefaultWorkflowPermissions
import io.github.model.AllowedActions
import io.github.model.ApiInsightsRouteStats
import io.github.model.ApiInsightsSubjectStats
import io.github.model.ApiInsightsSummaryStats
import io.github.model.ApiInsightsTimeStats
import io.github.model.ApiInsightsUserStats
import io.github.model.ArtifactDeploymentRecord
import io.github.model.AuthenticationToken
import io.github.model.BasicError
import io.github.model.CampaignState
import io.github.model.CampaignSummary
import io.github.model.CodeScanningAlertSeverity
import io.github.model.CodeScanningAlertStateQuery
import io.github.model.CodeScanningDefaultSetupOptions
import io.github.model.CodeScanningOptions
import io.github.model.CodeScanningOrganizationAlertItems
import io.github.model.CodeSecurityConfiguration
import io.github.model.CodeSecurityConfigurationRepositories
import io.github.model.CodeSecurityDefaultConfigurations
import io.github.model.Codespace
import io.github.model.CodespacesOrgSecret
import io.github.model.CodespacesPublicKey
import io.github.model.CopilotOrganizationContentExclusionDetails
import io.github.model.CopilotOrganizationDetails
import io.github.model.CopilotSeatDetails
import io.github.model.CopilotUsageMetricsDay
import io.github.model.CustomProperty
import io.github.model.CustomPropertySetPayload
import io.github.model.CustomPropertyValue
import io.github.model.DependabotAlertWithRepository
import io.github.model.DependabotPublicKey
import io.github.model.EmptyObject
import io.github.model.EnabledRepositories
import io.github.model.Event
import io.github.model.FullRepository
import io.github.model.HookDelivery
import io.github.model.HookDeliveryItem
import io.github.model.ImmutableReleasesOrganizationSettings
import io.github.model.InteractionLimit
import io.github.model.InteractionLimitResponse
import io.github.model.Issue
import io.github.model.IssueField
import io.github.model.IssueType
import io.github.model.Migration
import io.github.model.MinimalRepository
import io.github.model.NetworkConfiguration
import io.github.model.OidcCustomPropertyInclusion
import io.github.model.OidcCustomPropertyInclusionInput
import io.github.model.OidcCustomSub
import io.github.model.OrgHook
import io.github.model.OrgMembership
import io.github.model.OrgPrivateRegistryConfiguration
import io.github.model.OrgPrivateRegistryConfigurationWithSelectedRepositories
import io.github.model.OrgRepoCustomPropertyValues
import io.github.model.OrgRules
import io.github.model.OrgRulesetConditions
import io.github.model.OrganizationActionsSecret
import io.github.model.OrganizationActionsVariable
import io.github.model.OrganizationCreateIssueField
import io.github.model.OrganizationCreateIssueType
import io.github.model.OrganizationDependabotSecret
import io.github.model.OrganizationFull
import io.github.model.OrganizationInvitation
import io.github.model.OrganizationProgrammaticAccessGrant
import io.github.model.OrganizationProgrammaticAccessGrantRequest
import io.github.model.OrganizationRole
import io.github.model.OrganizationSecretScanningAlert
import io.github.model.OrganizationUpdateIssueField
import io.github.model.OrganizationUpdateIssueType
import io.github.model.Package
import io.github.model.PackageVersion
import io.github.model.ProjectsV2Field
import io.github.model.ProjectsV2FieldIterationConfiguration
import io.github.model.ProjectsV2FieldSingleSelectOption
import io.github.model.ProjectsV2ItemSimple
import io.github.model.ProjectsV2ItemWithContent
import io.github.model.ProjectsV2View
import io.github.model.Repository
import io.github.model.RepositoryAdvisory
import io.github.model.RepositoryRuleEnforcement
import io.github.model.RepositoryRuleset
import io.github.model.RepositoryRulesetBypassActor
import io.github.model.RuleSuite
import io.github.model.RulesetVersion
import io.github.model.RulesetVersionWithState
import io.github.model.Runner
import io.github.model.RunnerApplication
import io.github.model.RunnerGroupsOrg
import io.github.model.RunnerLabel
import io.github.model.SecretScanningPatternConfiguration
import io.github.model.SecretScanningRowVersion
import io.github.model.SelfHostedRunnersSettings
import io.github.model.ShaPinningRequired
import io.github.model.SimpleUser
import io.github.model.TeamFull
import io.github.model.TeamMembership
import io.github.model.TeamRepository
import io.github.model.TeamRoleAssignment
import io.github.model.TeamSimple
import io.github.model.UserRoleAssignment
import io.github.model.ValidationError
import io.github.model.ValidationErrorSimple
import io.github.model.WebhookConfig
import io.github.model.WebhookConfigContentType
import io.github.model.WebhookConfigInsecureSsl
import io.github.model.WebhookConfigSecret
import io.github.model.WebhookConfigUrl
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.delete
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.Boolean
import kotlin.Deprecated
import kotlin.Double
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.jvm.JvmInline
import kotlin.time.Instant
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject

public class Orgs internal constructor(
  private val client: HttpClient,
) {
  public fun org(org: String): OrgPath = OrgPath(client, org)

  public class OrgPath internal constructor(
    private val client: HttpClient,
    private val org: String,
  ) {
    public val delete: Delete = Delete(client, org)

    public val `get`: Get = Get(client, org)

    public val patch: Patch = Patch(client, org)

    public val actions: Actions = Actions(client, org)

    public val artifacts: Artifacts = Artifacts(client, org)

    public val attestations: Attestations = Attestations(client, org)

    public val blocks: Blocks = Blocks(client, org)

    public val campaigns: Campaigns = Campaigns(client, org)

    public val codeScanning: CodeScanning = CodeScanning(client, org)

    public val codeSecurity: CodeSecurity = CodeSecurity(client, org)

    public val codespaces: Codespaces = Codespaces(client, org)

    public val copilot: Copilot = Copilot(client, org)

    public val dependabot: Dependabot = Dependabot(client, org)

    public val docker: Docker = Docker(client, org)

    public val events: Events = Events(client, org)

    public val failedInvitations: FailedInvitations = FailedInvitations(client, org)

    public val hooks: Hooks = Hooks(client, org)

    public val insights: Insights = Insights(client, org)

    public val installation: Installation = Installation(client, org)

    public val installations: Installations = Installations(client, org)

    public val interactionLimits: InteractionLimits = InteractionLimits(client, org)

    public val invitations: Invitations = Invitations(client, org)

    public val issueFields: IssueFields = IssueFields(client, org)

    public val issueTypes: IssueTypes = IssueTypes(client, org)

    public val issues: Issues = Issues(client, org)

    public val members: Members = Members(client, org)

    public val memberships: Memberships = Memberships(client, org)

    public val migrations: Migrations = Migrations(client, org)

    public val organizationRoles: OrganizationRoles = OrganizationRoles(client, org)

    public val outsideCollaborators: OutsideCollaborators = OutsideCollaborators(client, org)

    public val packages: Packages = Packages(client, org)

    public val personalAccessTokenRequests: PersonalAccessTokenRequests =
        PersonalAccessTokenRequests(client, org)

    public val personalAccessTokens: PersonalAccessTokens = PersonalAccessTokens(client, org)

    public val privateRegistries: PrivateRegistries = PrivateRegistries(client, org)

    public val projectsV2: ProjectsV2 = ProjectsV2(client, org)

    public val properties: Properties = Properties(client, org)

    public val publicMembers: PublicMembers = PublicMembers(client, org)

    public val repos: Repos = Repos(client, org)

    public val rulesets: Rulesets = Rulesets(client, org)

    public val secretScanning: SecretScanning = SecretScanning(client, org)

    public val securityAdvisories: SecurityAdvisories = SecurityAdvisories(client, org)

    public val securityManagers: SecurityManagers = SecurityManagers(client, org)

    public val settings: Settings = Settings(client, org)

    public val team: Team = Team(client, org)

    public val teams: Teams = Teams(client, org)

    public val dependencyGraph: DependencyGraph = DependencyGraph(client, org)

    public val dependabotAlerts: DependabotAlerts = DependabotAlerts(client, org)

    public val dependabotSecurityUpdates: DependabotSecurityUpdates =
        DependabotSecurityUpdates(client, org)

    public val advancedSecurity: AdvancedSecurity = AdvancedSecurity(client, org)

    public val codeScanningDefaultSetup: CodeScanningDefaultSetup =
        CodeScanningDefaultSetup(client, org)

    public val secretScanningPushProtection: SecretScanningPushProtection =
        SecretScanningPushProtection(client, org)

    public class Delete internal constructor(
      private val client: HttpClient,
      private val org: String,
    ) {
      public suspend operator fun invoke(): Response {
        val response = client.delete("/orgs/$org")
        return when (response.status.value) {
          202 -> Response.Accepted(response.body())
          403 -> Response.Forbidden(response.body())
          404 -> Response.NotFound(response.body())
          else -> throw ResponseException(response, "")
        }
      }

      public sealed interface Response {
        public data class Accepted(
          public val `value`: JsonElement,
        ) : Response

        public data class Forbidden(
          public val `value`: BasicError,
        ) : Response

        public data class NotFound(
          public val `value`: BasicError,
        ) : Response
      }
    }

    public class Get internal constructor(
      private val client: HttpClient,
      private val org: String,
    ) {
      public suspend operator fun invoke(): Response {
        val response = client.get("/orgs/$org")
        return when (response.status.value) {
          200 -> Response.Ok(response.body())
          404 -> Response.NotFound(response.body())
          else -> throw ResponseException(response, "")
        }
      }

      public sealed interface Response {
        public data class Ok(
          public val `value`: OrganizationFull,
        ) : Response

        public data class NotFound(
          public val `value`: BasicError,
        ) : Response
      }
    }

    public class Patch internal constructor(
      private val client: HttpClient,
      private val org: String,
    ) {
      public suspend operator fun invoke(
        billingEmail: String? = null,
        company: String? = null,
        email: String? = null,
        twitterUsername: String? = null,
        location: String? = null,
        name: String? = null,
        description: String? = null,
        hasOrganizationProjects: Boolean? = null,
        hasRepositoryProjects: Boolean? = null,
        defaultRepositoryPermission: DefaultRepositoryPermission? = null,
        membersCanCreateRepositories: Boolean? = null,
        membersCanCreateInternalRepositories: Boolean? = null,
        membersCanCreatePrivateRepositories: Boolean? = null,
        membersCanCreatePublicRepositories: Boolean? = null,
        membersAllowedRepositoryCreationType: MembersAllowedRepositoryCreationType? = null,
        membersCanCreatePages: Boolean? = null,
        membersCanCreatePublicPages: Boolean? = null,
        membersCanCreatePrivatePages: Boolean? = null,
        membersCanForkPrivateRepositories: Boolean? = null,
        webCommitSignoffRequired: Boolean? = null,
        blog: String? = null,
        advancedSecurityEnabledForNewRepositories: Boolean? = null,
        dependabotAlertsEnabledForNewRepositories: Boolean? = null,
        dependabotSecurityUpdatesEnabledForNewRepositories: Boolean? = null,
        dependencyGraphEnabledForNewRepositories: Boolean? = null,
        secretScanningEnabledForNewRepositories: Boolean? = null,
        secretScanningPushProtectionEnabledForNewRepositories: Boolean? = null,
        secretScanningPushProtectionCustomLinkEnabled: Boolean? = null,
        secretScanningPushProtectionCustomLink: String? = null,
        deployKeysEnabledForRepositories: Boolean? = null,
      ): Response {
        val response = client.patch("/orgs/$org") {
          if (billingEmail != null || company != null || email != null || twitterUsername != null || location != null || name != null || description != null || hasOrganizationProjects != null || hasRepositoryProjects != null || defaultRepositoryPermission != null || membersCanCreateRepositories != null || membersCanCreateInternalRepositories != null || membersCanCreatePrivateRepositories != null || membersCanCreatePublicRepositories != null || membersAllowedRepositoryCreationType != null || membersCanCreatePages != null || membersCanCreatePublicPages != null || membersCanCreatePrivatePages != null || membersCanForkPrivateRepositories != null || webCommitSignoffRequired != null || blog != null || advancedSecurityEnabledForNewRepositories != null || dependabotAlertsEnabledForNewRepositories != null || dependabotSecurityUpdatesEnabledForNewRepositories != null || dependencyGraphEnabledForNewRepositories != null || secretScanningEnabledForNewRepositories != null || secretScanningPushProtectionEnabledForNewRepositories != null || secretScanningPushProtectionCustomLinkEnabled != null || secretScanningPushProtectionCustomLink != null || deployKeysEnabledForRepositories != null) {
            contentType(ContentType.Application.Json)
            setBody(Body(billingEmail = billingEmail, company = company, email = email, twitterUsername = twitterUsername, location = location, name = name, description = description, hasOrganizationProjects = hasOrganizationProjects, hasRepositoryProjects = hasRepositoryProjects, defaultRepositoryPermission = defaultRepositoryPermission, membersCanCreateRepositories = membersCanCreateRepositories, membersCanCreateInternalRepositories = membersCanCreateInternalRepositories, membersCanCreatePrivateRepositories = membersCanCreatePrivateRepositories, membersCanCreatePublicRepositories = membersCanCreatePublicRepositories, membersAllowedRepositoryCreationType = membersAllowedRepositoryCreationType, membersCanCreatePages = membersCanCreatePages, membersCanCreatePublicPages = membersCanCreatePublicPages, membersCanCreatePrivatePages = membersCanCreatePrivatePages, membersCanForkPrivateRepositories = membersCanForkPrivateRepositories, webCommitSignoffRequired = webCommitSignoffRequired, blog = blog, advancedSecurityEnabledForNewRepositories = advancedSecurityEnabledForNewRepositories, dependabotAlertsEnabledForNewRepositories = dependabotAlertsEnabledForNewRepositories, dependabotSecurityUpdatesEnabledForNewRepositories = dependabotSecurityUpdatesEnabledForNewRepositories, dependencyGraphEnabledForNewRepositories = dependencyGraphEnabledForNewRepositories, secretScanningEnabledForNewRepositories = secretScanningEnabledForNewRepositories, secretScanningPushProtectionEnabledForNewRepositories = secretScanningPushProtectionEnabledForNewRepositories, secretScanningPushProtectionCustomLinkEnabled = secretScanningPushProtectionCustomLinkEnabled, secretScanningPushProtectionCustomLink = secretScanningPushProtectionCustomLink, deployKeysEnabledForRepositories = deployKeysEnabledForRepositories))
          }
        }
        return when (response.status.value) {
          200 -> Response.Ok(response.body())
          409 -> Response.Conflict(response.body())
          422 -> response.body<Response.UnprocessableEntity>()
          else -> throw ResponseException(response, "")
        }
      }

      @Serializable
      public enum class DefaultRepositoryPermission(
        public val `value`: String,
      ) {
        @SerialName("read")
        Read("read"),
        @SerialName("write")
        Write("write"),
        @SerialName("admin")
        Admin("admin"),
        @SerialName("none")
        None("none"),
        ;
      }

      @Serializable
      public enum class MembersAllowedRepositoryCreationType(
        public val `value`: String,
      ) {
        @SerialName("all")
        All("all"),
        @SerialName("private")
        Private("private"),
        @SerialName("none")
        None("none"),
        ;
      }

      @Serializable
      internal data class Body(
        @SerialName("billing_email")
        public val billingEmail: String? = null,
        public val company: String? = null,
        public val email: String? = null,
        @SerialName("twitter_username")
        public val twitterUsername: String? = null,
        public val location: String? = null,
        public val name: String? = null,
        public val description: String? = null,
        @SerialName("has_organization_projects")
        public val hasOrganizationProjects: Boolean? = null,
        @SerialName("has_repository_projects")
        public val hasRepositoryProjects: Boolean? = null,
        @SerialName("default_repository_permission")
        public val defaultRepositoryPermission: DefaultRepositoryPermission? = null,
        @SerialName("members_can_create_repositories")
        public val membersCanCreateRepositories: Boolean? = null,
        @SerialName("members_can_create_internal_repositories")
        public val membersCanCreateInternalRepositories: Boolean? = null,
        @SerialName("members_can_create_private_repositories")
        public val membersCanCreatePrivateRepositories: Boolean? = null,
        @SerialName("members_can_create_public_repositories")
        public val membersCanCreatePublicRepositories: Boolean? = null,
        @SerialName("members_allowed_repository_creation_type")
        public val membersAllowedRepositoryCreationType:
            MembersAllowedRepositoryCreationType? = null,
        @SerialName("members_can_create_pages")
        public val membersCanCreatePages: Boolean? = null,
        @SerialName("members_can_create_public_pages")
        public val membersCanCreatePublicPages: Boolean? = null,
        @SerialName("members_can_create_private_pages")
        public val membersCanCreatePrivatePages: Boolean? = null,
        @SerialName("members_can_fork_private_repositories")
        public val membersCanForkPrivateRepositories: Boolean? = null,
        @SerialName("web_commit_signoff_required")
        public val webCommitSignoffRequired: Boolean? = null,
        public val blog: String? = null,
        @SerialName("advanced_security_enabled_for_new_repositories")
        public val advancedSecurityEnabledForNewRepositories: Boolean? = null,
        @SerialName("dependabot_alerts_enabled_for_new_repositories")
        public val dependabotAlertsEnabledForNewRepositories: Boolean? = null,
        @SerialName("dependabot_security_updates_enabled_for_new_repositories")
        public val dependabotSecurityUpdatesEnabledForNewRepositories: Boolean? = null,
        @SerialName("dependency_graph_enabled_for_new_repositories")
        public val dependencyGraphEnabledForNewRepositories: Boolean? = null,
        @SerialName("secret_scanning_enabled_for_new_repositories")
        public val secretScanningEnabledForNewRepositories: Boolean? = null,
        @SerialName("secret_scanning_push_protection_enabled_for_new_repositories")
        public val secretScanningPushProtectionEnabledForNewRepositories: Boolean? = null,
        @SerialName("secret_scanning_push_protection_custom_link_enabled")
        public val secretScanningPushProtectionCustomLinkEnabled: Boolean? = null,
        @SerialName("secret_scanning_push_protection_custom_link")
        public val secretScanningPushProtectionCustomLink: String? = null,
        @SerialName("deploy_keys_enabled_for_repositories")
        public val deployKeysEnabledForRepositories: Boolean? = null,
      )

      public sealed interface Response {
        public data class Ok(
          public val `value`: OrganizationFull,
        ) : Response

        public data class Conflict(
          public val `value`: BasicError,
        ) : Response

        @Serializable(with = UnprocessableEntity.Serializer::class)
        public sealed interface UnprocessableEntity : Response {
          @Serializable
          @JvmInline
          public value class CaseValidationError(
            public val `value`: ValidationError,
          ) : UnprocessableEntity

          @Serializable
          @JvmInline
          public value class CaseValidationErrorSimple(
            public val `value`: ValidationErrorSimple,
          ) : UnprocessableEntity

          public object Serializer : KSerializer<UnprocessableEntity> {
            @OptIn(
              InternalSerializationApi::class,
              ExperimentalSerializationApi::class,
            )
            override val descriptor: SerialDescriptor =
                buildSerialDescriptor("io.github.api.Orgs.OrgPath.Patch.Response.UnprocessableEntity", PolymorphicKind.SEALED) {
              element("CaseValidationError", ValidationError.serializer().descriptor)
              element("CaseValidationErrorSimple", ValidationErrorSimple.serializer().descriptor)
            }

            override fun deserialize(decoder: Decoder): UnprocessableEntity {
              val value = decoder.decodeSerializableValue(JsonElement.serializer())
              val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
              return json.attemptDeserialize(
                value,
                CaseValidationError::class to { CaseValidationError(decodeFromJsonElement(ValidationError.serializer(), it)) },
                CaseValidationErrorSimple::class to { CaseValidationErrorSimple(decodeFromJsonElement(ValidationErrorSimple.serializer(), it)) },
              )
            }

            override fun serialize(encoder: Encoder, `value`: UnprocessableEntity) {
              when(value) {
                is CaseValidationError -> encoder.encodeSerializableValue(ValidationError.serializer(), value.value)
                is CaseValidationErrorSimple -> encoder.encodeSerializableValue(ValidationErrorSimple.serializer(), value.value)
              }
            }
          }
        }
      }
    }

    public class Actions internal constructor(
      private val client: HttpClient,
      private val org: String,
    ) {
      public val cache: Cache = Cache(client, org)

      public val hostedRunners: HostedRunners = HostedRunners(client, org)

      public val oidc: Oidc = Oidc(client, org)

      public val permissions: Permissions = Permissions(client, org)

      public val runnerGroups: RunnerGroups = RunnerGroups(client, org)

      public val runners: Runners = Runners(client, org)

      public val secrets: Secrets = Secrets(client, org)

      public val variables: Variables = Variables(client, org)

      public class Cache internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public val usage: Usage = Usage(client, org)

        public val usageByRepository: UsageByRepository = UsageByRepository(client, org)

        public class Usage internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public val `get`: Get = Get(client, org)

          public class Get internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public suspend operator fun invoke(): ActionsCacheUsageOrgEnterprise = client.get("/orgs/$org/actions/cache/usage").body()
          }
        }

        public class UsageByRepository internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public val `get`: Get = Get(client, org)

          public class Get internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response = client.get("/orgs/$org/actions/cache/usage-by-repository") {
              perPage?.let { parameter("per_page", it) }
              page?.let { parameter("page", it) }
            }.body()

            @Serializable
            public data class Response(
              @SerialName("total_count")
              public val totalCount: Long,
              @SerialName("repository_cache_usages")
              public val repositoryCacheUsages: List<ActionsCacheUsageByRepository>,
            )
          }
        }
      }

      public class HostedRunners internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public val `get`: Get = Get(client, org)

        public val post: Post = Post(client, org)

        public val images: Images = Images(client, org)

        public val limits: Limits = Limits(client, org)

        public val machineSizes: MachineSizes = MachineSizes(client, org)

        public val platforms: Platforms = Platforms(client, org)

        public fun hostedRunnerId(hostedRunnerId: Long): HostedRunnerIdPath = HostedRunnerIdPath(client, org, hostedRunnerId)

        public class Get internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response = client.get("/orgs/$org/actions/hosted-runners") {
            perPage?.let { parameter("per_page", it) }
            page?.let { parameter("page", it) }
          }.body()

          @Serializable
          public data class Response(
            @SerialName("total_count")
            public val totalCount: Long,
            public val runners: List<ActionsHostedRunner>,
          )
        }

        public class Post internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public suspend operator fun invoke(
            name: String,
            image: Image,
            size: String,
            runnerGroupId: Long,
            maximumRunners: Long? = null,
            enableStaticIp: Boolean? = null,
            imageGen: Boolean? = null,
          ): ActionsHostedRunner = client.post("/orgs/$org/actions/hosted-runners") {
            contentType(ContentType.Application.Json)
            setBody(Body(name = name, image = image, size = size, runnerGroupId = runnerGroupId, maximumRunners = maximumRunners, enableStaticIp = enableStaticIp, imageGen = imageGen))
          }.body()

          /**
           * The image of runner. To list all available images, use `GET /actions/hosted-runners/images/github-owned` or `GET /actions/hosted-runners/images/partner`.
           */
          @Serializable
          public data class Image(
            public val id: String? = null,
            public val source: Source? = null,
            public val version: String? = null,
          ) {
            @Serializable
            public enum class Source(
              public val `value`: String,
            ) {
              @SerialName("github")
              Github("github"),
              @SerialName("partner")
              Partner("partner"),
              @SerialName("custom")
              Custom("custom"),
              ;
            }
          }

          @Serializable
          internal data class Body(
            public val name: String,
            public val image: Image,
            public val size: String,
            @SerialName("runner_group_id")
            public val runnerGroupId: Long,
            @SerialName("maximum_runners")
            public val maximumRunners: Long? = null,
            @SerialName("enable_static_ip")
            public val enableStaticIp: Boolean? = null,
            @SerialName("image_gen")
            public val imageGen: Boolean? = null,
          )
        }

        public class Images internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public val custom: Custom = Custom(client, org)

          public val githubOwned: GithubOwned = GithubOwned(client, org)

          public val partner: Partner = Partner(client, org)

          public class Custom internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public val `get`: Get = Get(client, org)

            public fun imageDefinitionId(imageDefinitionId: Long): ImageDefinitionIdPath = ImageDefinitionIdPath(client, org, imageDefinitionId)

            public class Get internal constructor(
              private val client: HttpClient,
              private val org: String,
            ) {
              public suspend operator fun invoke(): Response = client.get("/orgs/$org/actions/hosted-runners/images/custom").body()

              @Serializable
              public data class Response(
                @SerialName("total_count")
                public val totalCount: Long,
                public val images: List<ActionsHostedRunnerCustomImage>,
              )
            }

            public class ImageDefinitionIdPath internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val imageDefinitionId: Long,
            ) {
              public val delete: Delete = Delete(client, org, imageDefinitionId)

              public val `get`: Get = Get(client, org, imageDefinitionId)

              public val versions: Versions = Versions(client, org, imageDefinitionId)

              public class Delete internal constructor(
                private val client: HttpClient,
                private val org: String,
                private val imageDefinitionId: Long,
              ) {
                public suspend operator fun invoke() {
                  client.delete("/orgs/$org/actions/hosted-runners/images/custom/$imageDefinitionId")
                }
              }

              public class Get internal constructor(
                private val client: HttpClient,
                private val org: String,
                private val imageDefinitionId: Long,
              ) {
                public suspend operator fun invoke(): ActionsHostedRunnerCustomImage = client.get("/orgs/$org/actions/hosted-runners/images/custom/$imageDefinitionId").body()
              }

              public class Versions internal constructor(
                private val client: HttpClient,
                private val org: String,
                private val imageDefinitionId: Long,
              ) {
                public val `get`: Get = Get(client, org, imageDefinitionId)

                public fun version(version: String): VersionPath = VersionPath(client, org, imageDefinitionId, version)

                public class Get internal constructor(
                  private val client: HttpClient,
                  private val org: String,
                  private val imageDefinitionId: Long,
                ) {
                  public suspend operator fun invoke(): Response = client.get("/orgs/$org/actions/hosted-runners/images/custom/$imageDefinitionId/versions").body()

                  @Serializable
                  public data class Response(
                    @SerialName("total_count")
                    public val totalCount: Long,
                    @SerialName("image_versions")
                    public val imageVersions: List<ActionsHostedRunnerCustomImageVersion>,
                  )
                }

                public class VersionPath internal constructor(
                  private val client: HttpClient,
                  private val org: String,
                  private val imageDefinitionId: Long,
                  private val version: String,
                ) {
                  public val delete: Delete = Delete(client, org, imageDefinitionId, version)

                  public val `get`: Get = Get(client, org, imageDefinitionId, version)

                  public class Delete internal constructor(
                    private val client: HttpClient,
                    private val org: String,
                    private val imageDefinitionId: Long,
                    private val version: String,
                  ) {
                    public suspend operator fun invoke() {
                      client.delete("/orgs/$org/actions/hosted-runners/images/custom/$imageDefinitionId/versions/$version")
                    }
                  }

                  public class Get internal constructor(
                    private val client: HttpClient,
                    private val org: String,
                    private val imageDefinitionId: Long,
                    private val version: String,
                  ) {
                    public suspend operator fun invoke(): ActionsHostedRunnerCustomImageVersion = client.get("/orgs/$org/actions/hosted-runners/images/custom/$imageDefinitionId/versions/$version").body()
                  }
                }
              }
            }
          }

          public class GithubOwned internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public val `get`: Get = Get(client, org)

            public class Get internal constructor(
              private val client: HttpClient,
              private val org: String,
            ) {
              public suspend operator fun invoke(): Response = client.get("/orgs/$org/actions/hosted-runners/images/github-owned").body()

              @Serializable
              public data class Response(
                @SerialName("total_count")
                public val totalCount: Long,
                public val images: List<ActionsHostedRunnerCuratedImage>,
              )
            }
          }

          public class Partner internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public val `get`: Get = Get(client, org)

            public class Get internal constructor(
              private val client: HttpClient,
              private val org: String,
            ) {
              public suspend operator fun invoke(): Response = client.get("/orgs/$org/actions/hosted-runners/images/partner").body()

              @Serializable
              public data class Response(
                @SerialName("total_count")
                public val totalCount: Long,
                public val images: List<ActionsHostedRunnerCuratedImage>,
              )
            }
          }
        }

        public class Limits internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public val `get`: Get = Get(client, org)

          public class Get internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public suspend operator fun invoke(): ActionsHostedRunnerLimits = client.get("/orgs/$org/actions/hosted-runners/limits").body()
          }
        }

        public class MachineSizes internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public val `get`: Get = Get(client, org)

          public class Get internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public suspend operator fun invoke(): Response = client.get("/orgs/$org/actions/hosted-runners/machine-sizes").body()

            @Serializable
            public data class Response(
              @SerialName("total_count")
              public val totalCount: Long,
              @SerialName("machine_specs")
              public val machineSpecs: List<ActionsHostedRunnerMachineSpec>,
            )
          }
        }

        public class Platforms internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public val `get`: Get = Get(client, org)

          public class Get internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public suspend operator fun invoke(): Response = client.get("/orgs/$org/actions/hosted-runners/platforms").body()

            @Serializable
            public data class Response(
              @SerialName("total_count")
              public val totalCount: Long,
              public val platforms: List<String>,
            )
          }
        }

        public class HostedRunnerIdPath internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val hostedRunnerId: Long,
        ) {
          public val delete: Delete = Delete(client, org, hostedRunnerId)

          public val `get`: Get = Get(client, org, hostedRunnerId)

          public val patch: Patch = Patch(client, org, hostedRunnerId)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val hostedRunnerId: Long,
          ) {
            public suspend operator fun invoke(): ActionsHostedRunner = client.delete("/orgs/$org/actions/hosted-runners/$hostedRunnerId").body()
          }

          public class Get internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val hostedRunnerId: Long,
          ) {
            public suspend operator fun invoke(): ActionsHostedRunner = client.get("/orgs/$org/actions/hosted-runners/$hostedRunnerId").body()
          }

          public class Patch internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val hostedRunnerId: Long,
          ) {
            public suspend operator fun invoke(
              name: String? = null,
              runnerGroupId: Long? = null,
              maximumRunners: Long? = null,
              enableStaticIp: Boolean? = null,
              size: String? = null,
              imageId: String? = null,
              imageVersion: String? = null,
            ): ActionsHostedRunner = client.patch("/orgs/$org/actions/hosted-runners/$hostedRunnerId") {
              contentType(ContentType.Application.Json)
              setBody(Body(name = name, runnerGroupId = runnerGroupId, maximumRunners = maximumRunners, enableStaticIp = enableStaticIp, size = size, imageId = imageId, imageVersion = imageVersion))
            }.body()

            @Serializable
            internal data class Body(
              public val name: String? = null,
              @SerialName("runner_group_id")
              public val runnerGroupId: Long? = null,
              @SerialName("maximum_runners")
              public val maximumRunners: Long? = null,
              @SerialName("enable_static_ip")
              public val enableStaticIp: Boolean? = null,
              public val size: String? = null,
              @SerialName("image_id")
              public val imageId: String? = null,
              @SerialName("image_version")
              public val imageVersion: String? = null,
            )
          }
        }
      }

      public class Oidc internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public val customization: Customization = Customization(client, org)

        public class Customization internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public val properties: Properties = Properties(client, org)

          public val sub: Sub = Sub(client, org)

          public class Properties internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public val repo: Repo = Repo(client, org)

            public class Repo internal constructor(
              private val client: HttpClient,
              private val org: String,
            ) {
              public val `get`: Get = Get(client, org)

              public val post: Post = Post(client, org)

              public fun customPropertyName(customPropertyName: String): CustomPropertyNamePath = CustomPropertyNamePath(client, org, customPropertyName)

              public class Get internal constructor(
                private val client: HttpClient,
                private val org: String,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.get("/orgs/$org/actions/oidc/customization/properties/repo")
                  return when (response.status.value) {
                    200 -> Response.Ok(response.body())
                    403 -> Response.Forbidden(response.body())
                    404 -> Response.NotFound(response.body())
                    else -> throw ResponseException(response, "")
                  }
                }

                public sealed interface Response {
                  public data class Ok(
                    public val `value`: List<OidcCustomPropertyInclusion>,
                  ) : Response

                  public data class Forbidden(
                    public val `value`: BasicError,
                  ) : Response

                  public data class NotFound(
                    public val `value`: BasicError,
                  ) : Response
                }
              }

              public class Post internal constructor(
                private val client: HttpClient,
                private val org: String,
              ) {
                public suspend operator fun invoke(body: OidcCustomPropertyInclusionInput): Response {
                  val response = client.post("/orgs/$org/actions/oidc/customization/properties/repo") {
                    contentType(ContentType.Application.Json)
                    setBody(body)
                  }
                  return when (response.status.value) {
                    201 -> Response.Created(response.body())
                    400 -> Response.BadRequest
                    403 -> Response.Forbidden(response.body())
                    422 -> Response.UnprocessableEntity
                    else -> throw ResponseException(response, "")
                  }
                }

                public sealed interface Response {
                  public data class Created(
                    public val `value`: OidcCustomPropertyInclusion,
                  ) : Response

                  public data object BadRequest : Response

                  public data class Forbidden(
                    public val `value`: BasicError,
                  ) : Response

                  public data object UnprocessableEntity : Response
                }
              }

              public class CustomPropertyNamePath internal constructor(
                private val client: HttpClient,
                private val org: String,
                private val customPropertyName: String,
              ) {
                public val delete: Delete = Delete(client, org, customPropertyName)

                public class Delete internal constructor(
                  private val client: HttpClient,
                  private val org: String,
                  private val customPropertyName: String,
                ) {
                  public suspend operator fun invoke(): Response {
                    val response = client.delete("/orgs/$org/actions/oidc/customization/properties/repo/$customPropertyName")
                    return when (response.status.value) {
                      204 -> Response.NoContent
                      400 -> Response.BadRequest
                      403 -> Response.Forbidden(response.body())
                      404 -> Response.NotFound
                      else -> throw ResponseException(response, "")
                    }
                  }

                  public sealed interface Response {
                    public data object NoContent : Response

                    public data object BadRequest : Response

                    public data class Forbidden(
                      public val `value`: BasicError,
                    ) : Response

                    public data object NotFound : Response
                  }
                }
              }
            }
          }

          public class Sub internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public val `get`: Get = Get(client, org)

            public val put: Put = Put(client, org)

            public class Get internal constructor(
              private val client: HttpClient,
              private val org: String,
            ) {
              public suspend operator fun invoke(): OidcCustomSub = client.get("/orgs/$org/actions/oidc/customization/sub").body()
            }

            public class Put internal constructor(
              private val client: HttpClient,
              private val org: String,
            ) {
              public suspend operator fun invoke(body: OidcCustomSub): Response {
                val response = client.put("/orgs/$org/actions/oidc/customization/sub") {
                  contentType(ContentType.Application.Json)
                  setBody(body)
                }
                return when (response.status.value) {
                  201 -> Response.Created(response.body())
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data class Created(
                  public val `value`: EmptyObject,
                ) : Response

                public data class Forbidden(
                  public val `value`: BasicError,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response
              }
            }
          }
        }
      }

      public class Permissions internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public val `get`: Get = Get(client, org)

        public val put: Put = Put(client, org)

        public val artifactAndLogRetention: ArtifactAndLogRetention =
            ArtifactAndLogRetention(client, org)

        public val forkPrContributorApproval: ForkPrContributorApproval =
            ForkPrContributorApproval(client, org)

        public val forkPrWorkflowsPrivateRepos: ForkPrWorkflowsPrivateRepos =
            ForkPrWorkflowsPrivateRepos(client, org)

        public val repositories: Repositories = Repositories(client, org)

        public val selectedActions: SelectedActions = SelectedActions(client, org)

        public val selfHostedRunners: SelfHostedRunners = SelfHostedRunners(client, org)

        public val workflow: Workflow = Workflow(client, org)

        public class Get internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public suspend operator fun invoke(): ActionsOrganizationPermissions = client.get("/orgs/$org/actions/permissions").body()
        }

        public class Put internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public suspend operator fun invoke(
            enabledRepositories: EnabledRepositories,
            allowedActions: AllowedActions? = null,
            shaPinningRequired: ShaPinningRequired? = null,
          ) {
            client.put("/orgs/$org/actions/permissions") {
              contentType(ContentType.Application.Json)
              setBody(Body(enabledRepositories = enabledRepositories, allowedActions = allowedActions, shaPinningRequired = shaPinningRequired))
            }
          }

          @Serializable
          internal data class Body(
            @SerialName("enabled_repositories")
            public val enabledRepositories: EnabledRepositories,
            @SerialName("allowed_actions")
            public val allowedActions: AllowedActions? = null,
            @SerialName("sha_pinning_required")
            public val shaPinningRequired: ShaPinningRequired? = null,
          )
        }

        public class ArtifactAndLogRetention internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public val `get`: Get = Get(client, org)

          public val put: Put = Put(client, org)

          public class Get internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.get("/orgs/$org/actions/permissions/artifact-and-log-retention")
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: ActionsArtifactAndLogRetentionResponse,
              ) : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response
            }
          }

          public class Put internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public suspend operator fun invoke(body: ActionsArtifactAndLogRetention): Response {
              val response = client.put("/orgs/$org/actions/permissions/artifact-and-log-retention") {
                contentType(ContentType.Application.Json)
                setBody(body)
              }
              return when (response.status.value) {
                204 -> Response.NoContent
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                409 -> Response.Conflict(response.body())
                422 -> Response.UnprocessableEntity(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data object NoContent : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response

              public data class Conflict(
                public val `value`: BasicError,
              ) : Response

              public data class UnprocessableEntity(
                public val `value`: ValidationError,
              ) : Response
            }
          }
        }

        public class ForkPrContributorApproval internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public val `get`: Get = Get(client, org)

          public val put: Put = Put(client, org)

          public class Get internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.get("/orgs/$org/actions/permissions/fork-pr-contributor-approval")
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: ActionsForkPrContributorApproval,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response
            }
          }

          public class Put internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public suspend operator fun invoke(body: ActionsForkPrContributorApproval): Response {
              val response = client.put("/orgs/$org/actions/permissions/fork-pr-contributor-approval") {
                contentType(ContentType.Application.Json)
                setBody(body)
              }
              return when (response.status.value) {
                204 -> Response.NoContent
                404 -> Response.NotFound(response.body())
                422 -> Response.UnprocessableEntity(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data object NoContent : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response

              public data class UnprocessableEntity(
                public val `value`: ValidationError,
              ) : Response
            }
          }
        }

        public class ForkPrWorkflowsPrivateRepos internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public val `get`: Get = Get(client, org)

          public val put: Put = Put(client, org)

          public class Get internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.get("/orgs/$org/actions/permissions/fork-pr-workflows-private-repos")
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: ActionsForkPrWorkflowsPrivateRepos,
              ) : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response
            }
          }

          public class Put internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public suspend operator fun invoke(body: ActionsForkPrWorkflowsPrivateReposRequest): Response {
              val response = client.put("/orgs/$org/actions/permissions/fork-pr-workflows-private-repos") {
                contentType(ContentType.Application.Json)
                setBody(body)
              }
              return when (response.status.value) {
                204 -> Response.NoContent
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                422 -> Response.UnprocessableEntity(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data object NoContent : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response

              public data class UnprocessableEntity(
                public val `value`: ValidationError,
              ) : Response
            }
          }
        }

        public class Repositories internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public val `get`: Get = Get(client, org)

          public val put: Put = Put(client, org)

          public fun repositoryId(repositoryId: Long): RepositoryIdPath = RepositoryIdPath(client, org, repositoryId)

          public class Get internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response = client.get("/orgs/$org/actions/permissions/repositories") {
              perPage?.let { parameter("per_page", it) }
              page?.let { parameter("page", it) }
            }.body()

            @Serializable
            public data class Response(
              @SerialName("total_count")
              public val totalCount: Double,
              public val repositories: List<Repository>,
            )
          }

          public class Put internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public suspend operator fun invoke(selectedRepositoryIds: List<Long>) {
              client.put("/orgs/$org/actions/permissions/repositories") {
                contentType(ContentType.Application.Json)
                setBody(Body(selectedRepositoryIds = selectedRepositoryIds))
              }
            }

            @JvmInline
            @Serializable
            internal value class Body(
              @SerialName("selected_repository_ids")
              public val selectedRepositoryIds: List<Long>,
            )
          }

          public class RepositoryIdPath internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val repositoryId: Long,
          ) {
            public val delete: Delete = Delete(client, org, repositoryId)

            public val put: Put = Put(client, org, repositoryId)

            public class Delete internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val repositoryId: Long,
            ) {
              public suspend operator fun invoke() {
                client.delete("/orgs/$org/actions/permissions/repositories/$repositoryId")
              }
            }

            public class Put internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val repositoryId: Long,
            ) {
              public suspend operator fun invoke() {
                client.put("/orgs/$org/actions/permissions/repositories/$repositoryId")
              }
            }
          }
        }

        public class SelectedActions internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public val `get`: Get = Get(client, org)

          public val put: Put = Put(client, org)

          public class Get internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public suspend operator fun invoke(): io.github.model.SelectedActions = client.get("/orgs/$org/actions/permissions/selected-actions").body()
          }

          public class Put internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public suspend operator fun invoke(body: io.github.model.SelectedActions? = null) {
              client.put("/orgs/$org/actions/permissions/selected-actions") {
                body?.let {
                  contentType(ContentType.Application.Json)
                  setBody(it)
                }
              }
            }
          }
        }

        public class SelfHostedRunners internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public val `get`: Get = Get(client, org)

          public val put: Put = Put(client, org)

          public val repositories: Repositories = Repositories(client, org)

          public class Get internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.get("/orgs/$org/actions/permissions/self-hosted-runners")
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: SelfHostedRunnersSettings,
              ) : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response
            }
          }

          public class Put internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public suspend operator fun invoke(enabledRepositories: EnabledRepositories): Response {
              val response = client.put("/orgs/$org/actions/permissions/self-hosted-runners") {
                contentType(ContentType.Application.Json)
                setBody(Body(enabledRepositories = enabledRepositories))
              }
              return when (response.status.value) {
                204 -> Response.NoContent
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                409 -> Response.Conflict(response.body())
                422 -> Response.UnprocessableEntity(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            @Serializable
            public enum class EnabledRepositories(
              public val `value`: String,
            ) {
              @SerialName("all")
              All("all"),
              @SerialName("selected")
              Selected("selected"),
              @SerialName("none")
              None("none"),
              ;
            }

            @JvmInline
            @Serializable
            internal value class Body(
              @SerialName("enabled_repositories")
              public val enabledRepositories: EnabledRepositories,
            )

            public sealed interface Response {
              public data object NoContent : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response

              public data class Conflict(
                public val `value`: BasicError,
              ) : Response

              public data class UnprocessableEntity(
                public val `value`: ValidationError,
              ) : Response
            }
          }

          public class Repositories internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public val `get`: Get = Get(client, org)

            public val put: Put = Put(client, org)

            public fun repositoryId(repositoryId: Long): RepositoryIdPath = RepositoryIdPath(client, org, repositoryId)

            public class Get internal constructor(
              private val client: HttpClient,
              private val org: String,
            ) {
              public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
                val response = client.get("/orgs/$org/actions/permissions/self-hosted-runners/repositories") {
                  perPage?.let { parameter("per_page", it) }
                  page?.let { parameter("page", it) }
                }
                return when (response.status.value) {
                  200 -> response.body<Response.Ok>()
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                @Serializable
                public data class Ok(
                  @SerialName("total_count")
                  public val totalCount: Long? = null,
                  public val repositories: List<Repository>? = null,
                ) : Response

                public data class Forbidden(
                  public val `value`: BasicError,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response
              }
            }

            public class Put internal constructor(
              private val client: HttpClient,
              private val org: String,
            ) {
              public suspend operator fun invoke(selectedRepositoryIds: List<Long>): Response {
                val response = client.put("/orgs/$org/actions/permissions/self-hosted-runners/repositories") {
                  contentType(ContentType.Application.Json)
                  setBody(Body(selectedRepositoryIds = selectedRepositoryIds))
                }
                return when (response.status.value) {
                  204 -> Response.NoContent
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  422 -> Response.UnprocessableEntity(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              @JvmInline
              @Serializable
              internal value class Body(
                @SerialName("selected_repository_ids")
                public val selectedRepositoryIds: List<Long>,
              )

              public sealed interface Response {
                public data object NoContent : Response

                public data class Forbidden(
                  public val `value`: BasicError,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response

                public data class UnprocessableEntity(
                  public val `value`: ValidationError,
                ) : Response
              }
            }

            public class RepositoryIdPath internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val repositoryId: Long,
            ) {
              public val delete: Delete = Delete(client, org, repositoryId)

              public val put: Put = Put(client, org, repositoryId)

              public class Delete internal constructor(
                private val client: HttpClient,
                private val org: String,
                private val repositoryId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.delete("/orgs/$org/actions/permissions/self-hosted-runners/repositories/$repositoryId")
                  return when (response.status.value) {
                    204 -> Response.NoContent
                    403 -> Response.Forbidden(response.body())
                    404 -> Response.NotFound(response.body())
                    409 -> Response.Conflict(response.body())
                    422 -> Response.UnprocessableEntity(response.body())
                    else -> throw ResponseException(response, "")
                  }
                }

                public sealed interface Response {
                  public data object NoContent : Response

                  public data class Forbidden(
                    public val `value`: BasicError,
                  ) : Response

                  public data class NotFound(
                    public val `value`: BasicError,
                  ) : Response

                  public data class Conflict(
                    public val `value`: BasicError,
                  ) : Response

                  public data class UnprocessableEntity(
                    public val `value`: ValidationError,
                  ) : Response
                }
              }

              public class Put internal constructor(
                private val client: HttpClient,
                private val org: String,
                private val repositoryId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.put("/orgs/$org/actions/permissions/self-hosted-runners/repositories/$repositoryId")
                  return when (response.status.value) {
                    204 -> Response.NoContent
                    403 -> Response.Forbidden(response.body())
                    404 -> Response.NotFound(response.body())
                    409 -> Response.Conflict(response.body())
                    422 -> Response.UnprocessableEntity(response.body())
                    else -> throw ResponseException(response, "")
                  }
                }

                public sealed interface Response {
                  public data object NoContent : Response

                  public data class Forbidden(
                    public val `value`: BasicError,
                  ) : Response

                  public data class NotFound(
                    public val `value`: BasicError,
                  ) : Response

                  public data class Conflict(
                    public val `value`: BasicError,
                  ) : Response

                  public data class UnprocessableEntity(
                    public val `value`: ValidationError,
                  ) : Response
                }
              }
            }
          }
        }

        public class Workflow internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public val `get`: Get = Get(client, org)

          public val put: Put = Put(client, org)

          public class Get internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public suspend operator fun invoke(): ActionsGetDefaultWorkflowPermissions = client.get("/orgs/$org/actions/permissions/workflow").body()
          }

          public class Put internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public suspend operator fun invoke(body: ActionsSetDefaultWorkflowPermissions? = null) {
              client.put("/orgs/$org/actions/permissions/workflow") {
                body?.let {
                  contentType(ContentType.Application.Json)
                  setBody(it)
                }
              }
            }
          }
        }
      }

      public class RunnerGroups internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public val `get`: Get = Get(client, org)

        public val post: Post = Post(client, org)

        public fun runnerGroupId(runnerGroupId: Long): RunnerGroupIdPath = RunnerGroupIdPath(client, org, runnerGroupId)

        public class Get internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public suspend operator fun invoke(
            perPage: Long? = 30L,
            page: Long? = 1L,
            visibleToRepository: String? = null,
          ): Response = client.get("/orgs/$org/actions/runner-groups") {
            perPage?.let { parameter("per_page", it) }
            page?.let { parameter("page", it) }
            visibleToRepository?.let { parameter("visible_to_repository", it) }
          }.body()

          @Serializable
          public data class Response(
            @SerialName("total_count")
            public val totalCount: Double,
            @SerialName("runner_groups")
            public val runnerGroups: List<RunnerGroupsOrg>,
          )
        }

        public class Post internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public suspend operator fun invoke(
            name: String,
            visibility: Visibility? = null,
            selectedRepositoryIds: List<Long>? = null,
            runners: List<Long>? = null,
            allowsPublicRepositories: Boolean? = null,
            restrictedToWorkflows: Boolean? = null,
            selectedWorkflows: List<String>? = null,
            networkConfigurationId: String? = null,
          ): RunnerGroupsOrg = client.post("/orgs/$org/actions/runner-groups") {
            contentType(ContentType.Application.Json)
            setBody(Body(name = name, visibility = visibility, selectedRepositoryIds = selectedRepositoryIds, runners = runners, allowsPublicRepositories = allowsPublicRepositories, restrictedToWorkflows = restrictedToWorkflows, selectedWorkflows = selectedWorkflows, networkConfigurationId = networkConfigurationId))
          }.body()

          @Serializable
          public enum class Visibility(
            public val `value`: String,
          ) {
            @SerialName("selected")
            Selected("selected"),
            @SerialName("all")
            All("all"),
            @SerialName("private")
            Private("private"),
            ;
          }

          @Serializable
          internal data class Body(
            public val name: String,
            public val visibility: Visibility? = null,
            @SerialName("selected_repository_ids")
            public val selectedRepositoryIds: List<Long>? = null,
            public val runners: List<Long>? = null,
            @SerialName("allows_public_repositories")
            public val allowsPublicRepositories: Boolean? = null,
            @SerialName("restricted_to_workflows")
            public val restrictedToWorkflows: Boolean? = null,
            @SerialName("selected_workflows")
            public val selectedWorkflows: List<String>? = null,
            @SerialName("network_configuration_id")
            public val networkConfigurationId: String? = null,
          )
        }

        public class RunnerGroupIdPath internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val runnerGroupId: Long,
        ) {
          public val delete: Delete = Delete(client, org, runnerGroupId)

          public val `get`: Get = Get(client, org, runnerGroupId)

          public val patch: Patch = Patch(client, org, runnerGroupId)

          public val hostedRunners: HostedRunners = HostedRunners(client, org, runnerGroupId)

          public val repositories: Repositories = Repositories(client, org, runnerGroupId)

          public val runners: Runners = Runners(client, org, runnerGroupId)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val runnerGroupId: Long,
          ) {
            public suspend operator fun invoke() {
              client.delete("/orgs/$org/actions/runner-groups/$runnerGroupId")
            }
          }

          public class Get internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val runnerGroupId: Long,
          ) {
            public suspend operator fun invoke(): RunnerGroupsOrg = client.get("/orgs/$org/actions/runner-groups/$runnerGroupId").body()
          }

          public class Patch internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val runnerGroupId: Long,
          ) {
            public suspend operator fun invoke(
              name: String,
              visibility: Visibility? = null,
              allowsPublicRepositories: Boolean? = null,
              restrictedToWorkflows: Boolean? = null,
              selectedWorkflows: List<String>? = null,
              networkConfigurationId: String? = null,
            ): RunnerGroupsOrg = client.patch("/orgs/$org/actions/runner-groups/$runnerGroupId") {
              contentType(ContentType.Application.Json)
              setBody(Body(name = name, visibility = visibility, allowsPublicRepositories = allowsPublicRepositories, restrictedToWorkflows = restrictedToWorkflows, selectedWorkflows = selectedWorkflows, networkConfigurationId = networkConfigurationId))
            }.body()

            @Serializable
            public enum class Visibility(
              public val `value`: String,
            ) {
              @SerialName("selected")
              Selected("selected"),
              @SerialName("all")
              All("all"),
              @SerialName("private")
              Private("private"),
              ;
            }

            @Serializable
            internal data class Body(
              public val name: String,
              public val visibility: Visibility? = null,
              @SerialName("allows_public_repositories")
              public val allowsPublicRepositories: Boolean? = null,
              @SerialName("restricted_to_workflows")
              public val restrictedToWorkflows: Boolean? = null,
              @SerialName("selected_workflows")
              public val selectedWorkflows: List<String>? = null,
              @SerialName("network_configuration_id")
              public val networkConfigurationId: String? = null,
            )
          }

          public class HostedRunners internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val runnerGroupId: Long,
          ) {
            public val `get`: Get = Get(client, org, runnerGroupId)

            public class Get internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val runnerGroupId: Long,
            ) {
              public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response = client.get("/orgs/$org/actions/runner-groups/$runnerGroupId/hosted-runners") {
                perPage?.let { parameter("per_page", it) }
                page?.let { parameter("page", it) }
              }.body()

              @Serializable
              public data class Response(
                @SerialName("total_count")
                public val totalCount: Double,
                public val runners: List<ActionsHostedRunner>,
              )
            }
          }

          public class Repositories internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val runnerGroupId: Long,
          ) {
            public val `get`: Get = Get(client, org, runnerGroupId)

            public val put: Put = Put(client, org, runnerGroupId)

            public fun repositoryId(repositoryId: Long): RepositoryIdPath = RepositoryIdPath(client, org, runnerGroupId, repositoryId)

            public class Get internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val runnerGroupId: Long,
            ) {
              public suspend operator fun invoke(page: Long? = 1L, perPage: Long? = 30L): Response = client.get("/orgs/$org/actions/runner-groups/$runnerGroupId/repositories") {
                page?.let { parameter("page", it) }
                perPage?.let { parameter("per_page", it) }
              }.body()

              @Serializable
              public data class Response(
                @SerialName("total_count")
                public val totalCount: Double,
                public val repositories: List<MinimalRepository>,
              )
            }

            public class Put internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val runnerGroupId: Long,
            ) {
              public suspend operator fun invoke(selectedRepositoryIds: List<Long>) {
                client.put("/orgs/$org/actions/runner-groups/$runnerGroupId/repositories") {
                  contentType(ContentType.Application.Json)
                  setBody(Body(selectedRepositoryIds = selectedRepositoryIds))
                }
              }

              @JvmInline
              @Serializable
              internal value class Body(
                @SerialName("selected_repository_ids")
                public val selectedRepositoryIds: List<Long>,
              )
            }

            public class RepositoryIdPath internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val runnerGroupId: Long,
              private val repositoryId: Long,
            ) {
              public val delete: Delete = Delete(client, org, runnerGroupId, repositoryId)

              public val put: Put = Put(client, org, runnerGroupId, repositoryId)

              public class Delete internal constructor(
                private val client: HttpClient,
                private val org: String,
                private val runnerGroupId: Long,
                private val repositoryId: Long,
              ) {
                public suspend operator fun invoke() {
                  client.delete("/orgs/$org/actions/runner-groups/$runnerGroupId/repositories/$repositoryId")
                }
              }

              public class Put internal constructor(
                private val client: HttpClient,
                private val org: String,
                private val runnerGroupId: Long,
                private val repositoryId: Long,
              ) {
                public suspend operator fun invoke() {
                  client.put("/orgs/$org/actions/runner-groups/$runnerGroupId/repositories/$repositoryId")
                }
              }
            }
          }

          public class Runners internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val runnerGroupId: Long,
          ) {
            public val `get`: Get = Get(client, org, runnerGroupId)

            public val put: Put = Put(client, org, runnerGroupId)

            public fun runnerId(runnerId: Long): RunnerIdPath = RunnerIdPath(client, org, runnerGroupId, runnerId)

            public class Get internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val runnerGroupId: Long,
            ) {
              public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response = client.get("/orgs/$org/actions/runner-groups/$runnerGroupId/runners") {
                perPage?.let { parameter("per_page", it) }
                page?.let { parameter("page", it) }
              }.body()

              @Serializable
              public data class Response(
                @SerialName("total_count")
                public val totalCount: Double,
                public val runners: List<Runner>,
              )
            }

            public class Put internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val runnerGroupId: Long,
            ) {
              public suspend operator fun invoke(runners: List<Long>) {
                client.put("/orgs/$org/actions/runner-groups/$runnerGroupId/runners") {
                  contentType(ContentType.Application.Json)
                  setBody(Body(runners = runners))
                }
              }

              @JvmInline
              @Serializable
              internal value class Body(
                public val runners: List<Long>,
              )
            }

            public class RunnerIdPath internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val runnerGroupId: Long,
              private val runnerId: Long,
            ) {
              public val delete: Delete = Delete(client, org, runnerGroupId, runnerId)

              public val put: Put = Put(client, org, runnerGroupId, runnerId)

              public class Delete internal constructor(
                private val client: HttpClient,
                private val org: String,
                private val runnerGroupId: Long,
                private val runnerId: Long,
              ) {
                public suspend operator fun invoke() {
                  client.delete("/orgs/$org/actions/runner-groups/$runnerGroupId/runners/$runnerId")
                }
              }

              public class Put internal constructor(
                private val client: HttpClient,
                private val org: String,
                private val runnerGroupId: Long,
                private val runnerId: Long,
              ) {
                public suspend operator fun invoke() {
                  client.put("/orgs/$org/actions/runner-groups/$runnerGroupId/runners/$runnerId")
                }
              }
            }
          }
        }
      }

      public class Runners internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public val `get`: Get = Get(client, org)

        public val downloads: Downloads = Downloads(client, org)

        public val generateJitconfig: GenerateJitconfig = GenerateJitconfig(client, org)

        public val registrationToken: RegistrationToken = RegistrationToken(client, org)

        public val removeToken: RemoveToken = RemoveToken(client, org)

        public fun runnerId(runnerId: Long): RunnerIdPath = RunnerIdPath(client, org, runnerId)

        public class Get internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public suspend operator fun invoke(
            name: String? = null,
            perPage: Long? = 30L,
            page: Long? = 1L,
          ): Response = client.get("/orgs/$org/actions/runners") {
            name?.let { parameter("name", it) }
            perPage?.let { parameter("per_page", it) }
            page?.let { parameter("page", it) }
          }.body()

          @Serializable
          public data class Response(
            @SerialName("total_count")
            public val totalCount: Long,
            public val runners: List<Runner>,
          )
        }

        public class Downloads internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public val `get`: Get = Get(client, org)

          public class Get internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public suspend operator fun invoke(): List<RunnerApplication> = client.get("/orgs/$org/actions/runners/downloads").body()
          }
        }

        public class GenerateJitconfig internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public val post: Post = Post(client, org)

          public class Post internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public suspend operator fun invoke(
              name: String,
              runnerGroupId: Long,
              labels: List<String>,
              workFolder: String? = null,
            ): Response {
              val response = client.post("/orgs/$org/actions/runners/generate-jitconfig") {
                contentType(ContentType.Application.Json)
                setBody(Body(name = name, runnerGroupId = runnerGroupId, labels = labels, workFolder = workFolder))
              }
              return when (response.status.value) {
                201 -> response.body<Response.Created>()
                404 -> Response.NotFound(response.body())
                409 -> Response.Conflict(response.body())
                422 -> Response.UnprocessableEntity(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            @Serializable
            internal data class Body(
              public val name: String,
              @SerialName("runner_group_id")
              public val runnerGroupId: Long,
              public val labels: List<String>,
              @SerialName("work_folder")
              public val workFolder: String? = null,
            )

            public sealed interface Response {
              @Serializable
              public data class Created(
                public val runner: Runner,
                @SerialName("encoded_jit_config")
                public val encodedJitConfig: String,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response

              public data class Conflict(
                public val `value`: BasicError,
              ) : Response

              public data class UnprocessableEntity(
                public val `value`: ValidationErrorSimple,
              ) : Response
            }
          }
        }

        public class RegistrationToken internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public val post: Post = Post(client, org)

          public class Post internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public suspend operator fun invoke(): AuthenticationToken = client.post("/orgs/$org/actions/runners/registration-token").body()
          }
        }

        public class RemoveToken internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public val post: Post = Post(client, org)

          public class Post internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public suspend operator fun invoke(): AuthenticationToken = client.post("/orgs/$org/actions/runners/remove-token").body()
          }
        }

        public class RunnerIdPath internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val runnerId: Long,
        ) {
          public val delete: Delete = Delete(client, org, runnerId)

          public val `get`: Get = Get(client, org, runnerId)

          public val labels: Labels = Labels(client, org, runnerId)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val runnerId: Long,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.delete("/orgs/$org/actions/runners/$runnerId")
              return when (response.status.value) {
                204 -> Response.NoContent
                422 -> Response.UnprocessableEntity(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data object NoContent : Response

              public data class UnprocessableEntity(
                public val `value`: ValidationErrorSimple,
              ) : Response
            }
          }

          public class Get internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val runnerId: Long,
          ) {
            public suspend operator fun invoke(): Runner = client.get("/orgs/$org/actions/runners/$runnerId").body()
          }

          public class Labels internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val runnerId: Long,
          ) {
            public val delete: Delete = Delete(client, org, runnerId)

            public val `get`: Get = Get(client, org, runnerId)

            public val post: Post = Post(client, org, runnerId)

            public val put: Put = Put(client, org, runnerId)

            public fun name(name: String): NamePath = NamePath(client, org, runnerId, name)

            public class Delete internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val runnerId: Long,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.delete("/orgs/$org/actions/runners/$runnerId/labels")
                return when (response.status.value) {
                  200 -> response.body<Response.Ok>()
                  404 -> Response.NotFound(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                @Serializable
                public data class Ok(
                  @SerialName("total_count")
                  public val totalCount: Long,
                  public val labels: List<RunnerLabel>,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response
              }
            }

            public class Get internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val runnerId: Long,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.get("/orgs/$org/actions/runners/$runnerId/labels")
                return when (response.status.value) {
                  200 -> response.body<Response.Ok>()
                  404 -> Response.NotFound(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                @Serializable
                public data class Ok(
                  @SerialName("total_count")
                  public val totalCount: Long,
                  public val labels: List<RunnerLabel>,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response
              }
            }

            public class Post internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val runnerId: Long,
            ) {
              public suspend operator fun invoke(labels: List<String>): Response {
                val response = client.post("/orgs/$org/actions/runners/$runnerId/labels") {
                  contentType(ContentType.Application.Json)
                  setBody(Body(labels = labels))
                }
                return when (response.status.value) {
                  200 -> response.body<Response.Ok>()
                  404 -> Response.NotFound(response.body())
                  422 -> Response.UnprocessableEntity(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              @JvmInline
              @Serializable
              internal value class Body(
                public val labels: List<String>,
              )

              public sealed interface Response {
                @Serializable
                public data class Ok(
                  @SerialName("total_count")
                  public val totalCount: Long,
                  public val labels: List<RunnerLabel>,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response

                public data class UnprocessableEntity(
                  public val `value`: ValidationErrorSimple,
                ) : Response
              }
            }

            public class Put internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val runnerId: Long,
            ) {
              public suspend operator fun invoke(labels: List<String>): Response {
                val response = client.put("/orgs/$org/actions/runners/$runnerId/labels") {
                  contentType(ContentType.Application.Json)
                  setBody(Body(labels = labels))
                }
                return when (response.status.value) {
                  200 -> response.body<Response.Ok>()
                  404 -> Response.NotFound(response.body())
                  422 -> Response.UnprocessableEntity(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              @JvmInline
              @Serializable
              internal value class Body(
                public val labels: List<String>,
              )

              public sealed interface Response {
                @Serializable
                public data class Ok(
                  @SerialName("total_count")
                  public val totalCount: Long,
                  public val labels: List<RunnerLabel>,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response

                public data class UnprocessableEntity(
                  public val `value`: ValidationErrorSimple,
                ) : Response
              }
            }

            public class NamePath internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val runnerId: Long,
              private val name: String,
            ) {
              public val delete: Delete = Delete(client, org, runnerId, name)

              public class Delete internal constructor(
                private val client: HttpClient,
                private val org: String,
                private val runnerId: Long,
                private val name: String,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.delete("/orgs/$org/actions/runners/$runnerId/labels/$name")
                  return when (response.status.value) {
                    200 -> response.body<Response.Ok>()
                    404 -> Response.NotFound(response.body())
                    422 -> Response.UnprocessableEntity(response.body())
                    else -> throw ResponseException(response, "")
                  }
                }

                public sealed interface Response {
                  @Serializable
                  public data class Ok(
                    @SerialName("total_count")
                    public val totalCount: Long,
                    public val labels: List<RunnerLabel>,
                  ) : Response

                  public data class NotFound(
                    public val `value`: BasicError,
                  ) : Response

                  public data class UnprocessableEntity(
                    public val `value`: ValidationErrorSimple,
                  ) : Response
                }
              }
            }
          }
        }
      }

      public class Secrets internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public val `get`: Get = Get(client, org)

        public val publicKey: PublicKey = PublicKey(client, org)

        public fun secretName(secretName: String): SecretNamePath = SecretNamePath(client, org, secretName)

        public class Get internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response = client.get("/orgs/$org/actions/secrets") {
            perPage?.let { parameter("per_page", it) }
            page?.let { parameter("page", it) }
          }.body()

          @Serializable
          public data class Response(
            @SerialName("total_count")
            public val totalCount: Long,
            public val secrets: List<OrganizationActionsSecret>,
          )
        }

        public class PublicKey internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public val `get`: Get = Get(client, org)

          public class Get internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public suspend operator fun invoke(): ActionsPublicKey = client.get("/orgs/$org/actions/secrets/public-key").body()
          }
        }

        public class SecretNamePath internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val secretName: String,
        ) {
          public val delete: Delete = Delete(client, org, secretName)

          public val `get`: Get = Get(client, org, secretName)

          public val put: Put = Put(client, org, secretName)

          public val repositories: Repositories = Repositories(client, org, secretName)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val secretName: String,
          ) {
            public suspend operator fun invoke() {
              client.delete("/orgs/$org/actions/secrets/$secretName")
            }
          }

          public class Get internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val secretName: String,
          ) {
            public suspend operator fun invoke(): OrganizationActionsSecret = client.get("/orgs/$org/actions/secrets/$secretName").body()
          }

          public class Put internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val secretName: String,
          ) {
            public suspend operator fun invoke(
              encryptedValue: String,
              keyId: String,
              visibility: Visibility,
              selectedRepositoryIds: List<Long>? = null,
            ): Response {
              val response = client.put("/orgs/$org/actions/secrets/$secretName") {
                contentType(ContentType.Application.Json)
                setBody(Body(encryptedValue = encryptedValue, keyId = keyId, visibility = visibility, selectedRepositoryIds = selectedRepositoryIds))
              }
              return when (response.status.value) {
                201 -> Response.Created(response.body())
                204 -> Response.NoContent
                else -> throw ResponseException(response, "")
              }
            }

            @Serializable
            public enum class Visibility(
              public val `value`: String,
            ) {
              @SerialName("all")
              All("all"),
              @SerialName("private")
              Private("private"),
              @SerialName("selected")
              Selected("selected"),
              ;
            }

            @Serializable
            internal data class Body(
              @SerialName("encrypted_value")
              public val encryptedValue: String,
              @SerialName("key_id")
              public val keyId: String,
              public val visibility: Visibility,
              @SerialName("selected_repository_ids")
              public val selectedRepositoryIds: List<Long>? = null,
            )

            public sealed interface Response {
              public data class Created(
                public val `value`: EmptyObject,
              ) : Response

              public data object NoContent : Response
            }
          }

          public class Repositories internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val secretName: String,
          ) {
            public val `get`: Get = Get(client, org, secretName)

            public val put: Put = Put(client, org, secretName)

            public fun repositoryId(repositoryId: Long): RepositoryIdPath = RepositoryIdPath(client, org, secretName, repositoryId)

            public class Get internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val secretName: String,
            ) {
              public suspend operator fun invoke(page: Long? = 1L, perPage: Long? = 30L): Response = client.get("/orgs/$org/actions/secrets/$secretName/repositories") {
                page?.let { parameter("page", it) }
                perPage?.let { parameter("per_page", it) }
              }.body()

              @Serializable
              public data class Response(
                @SerialName("total_count")
                public val totalCount: Long,
                public val repositories: List<MinimalRepository>,
              )
            }

            public class Put internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val secretName: String,
            ) {
              public suspend operator fun invoke(selectedRepositoryIds: List<Long>) {
                client.put("/orgs/$org/actions/secrets/$secretName/repositories") {
                  contentType(ContentType.Application.Json)
                  setBody(Body(selectedRepositoryIds = selectedRepositoryIds))
                }
              }

              @JvmInline
              @Serializable
              internal value class Body(
                @SerialName("selected_repository_ids")
                public val selectedRepositoryIds: List<Long>,
              )
            }

            public class RepositoryIdPath internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val secretName: String,
              private val repositoryId: Long,
            ) {
              public val delete: Delete = Delete(client, org, secretName, repositoryId)

              public val put: Put = Put(client, org, secretName, repositoryId)

              public class Delete internal constructor(
                private val client: HttpClient,
                private val org: String,
                private val secretName: String,
                private val repositoryId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.delete("/orgs/$org/actions/secrets/$secretName/repositories/$repositoryId")
                  return when (response.status.value) {
                    204 -> Response.NoContent
                    409 -> Response.Conflict
                    else -> throw ResponseException(response, "")
                  }
                }

                public sealed interface Response {
                  public data object NoContent : Response

                  public data object Conflict : Response
                }
              }

              public class Put internal constructor(
                private val client: HttpClient,
                private val org: String,
                private val secretName: String,
                private val repositoryId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.put("/orgs/$org/actions/secrets/$secretName/repositories/$repositoryId")
                  return when (response.status.value) {
                    204 -> Response.NoContent
                    409 -> Response.Conflict
                    else -> throw ResponseException(response, "")
                  }
                }

                public sealed interface Response {
                  public data object NoContent : Response

                  public data object Conflict : Response
                }
              }
            }
          }
        }
      }

      public class Variables internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public val `get`: Get = Get(client, org)

        public val post: Post = Post(client, org)

        public fun name(name: String): NamePath = NamePath(client, org, name)

        public class Get internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public suspend operator fun invoke(perPage: Long? = 10L, page: Long? = 1L): Response = client.get("/orgs/$org/actions/variables") {
            perPage?.let { parameter("per_page", it) }
            page?.let { parameter("page", it) }
          }.body()

          @Serializable
          public data class Response(
            @SerialName("total_count")
            public val totalCount: Long,
            public val variables: List<OrganizationActionsVariable>,
          )
        }

        public class Post internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public suspend operator fun invoke(
            name: String,
            `value`: String,
            visibility: Visibility,
            selectedRepositoryIds: List<Long>? = null,
          ): EmptyObject = client.post("/orgs/$org/actions/variables") {
            contentType(ContentType.Application.Json)
            setBody(Body(name = name, value = value, visibility = visibility, selectedRepositoryIds = selectedRepositoryIds))
          }.body()

          @Serializable
          public enum class Visibility(
            public val `value`: String,
          ) {
            @SerialName("all")
            All("all"),
            @SerialName("private")
            Private("private"),
            @SerialName("selected")
            Selected("selected"),
            ;
          }

          @Serializable
          internal data class Body(
            public val name: String,
            public val `value`: String,
            public val visibility: Visibility,
            @SerialName("selected_repository_ids")
            public val selectedRepositoryIds: List<Long>? = null,
          )
        }

        public class NamePath internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val name: String,
        ) {
          public val delete: Delete = Delete(client, org, name)

          public val `get`: Get = Get(client, org, name)

          public val patch: Patch = Patch(client, org, name)

          public val repositories: Repositories = Repositories(client, org, name)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val name: String,
          ) {
            public suspend operator fun invoke() {
              client.delete("/orgs/$org/actions/variables/$name")
            }
          }

          public class Get internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val name: String,
          ) {
            public suspend operator fun invoke(): OrganizationActionsVariable = client.get("/orgs/$org/actions/variables/$name").body()
          }

          public class Patch internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val name: String,
          ) {
            public suspend operator fun invoke(
              name: String? = null,
              `value`: String? = null,
              visibility: Visibility? = null,
              selectedRepositoryIds: List<Long>? = null,
            ) {
              client.patch("/orgs/$org/actions/variables/$name") {
                contentType(ContentType.Application.Json)
                setBody(Body(name = name, value = value, visibility = visibility, selectedRepositoryIds = selectedRepositoryIds))
              }
            }

            @Serializable
            public enum class Visibility(
              public val `value`: String,
            ) {
              @SerialName("all")
              All("all"),
              @SerialName("private")
              Private("private"),
              @SerialName("selected")
              Selected("selected"),
              ;
            }

            @Serializable
            internal data class Body(
              public val name: String? = null,
              public val `value`: String? = null,
              public val visibility: Visibility? = null,
              @SerialName("selected_repository_ids")
              public val selectedRepositoryIds: List<Long>? = null,
            )
          }

          public class Repositories internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val name: String,
          ) {
            public val `get`: Get = Get(client, org, name)

            public val put: Put = Put(client, org, name)

            public fun repositoryId(repositoryId: Long): RepositoryIdPath = RepositoryIdPath(client, org, name, repositoryId)

            public class Get internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val name: String,
            ) {
              public suspend operator fun invoke(page: Long? = 1L, perPage: Long? = 30L): Response {
                val response = client.get("/orgs/$org/actions/variables/$name/repositories") {
                  page?.let { parameter("page", it) }
                  perPage?.let { parameter("per_page", it) }
                }
                return when (response.status.value) {
                  200 -> response.body<Response.Ok>()
                  409 -> Response.Conflict
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                @Serializable
                public data class Ok(
                  @SerialName("total_count")
                  public val totalCount: Long,
                  public val repositories: List<MinimalRepository>,
                ) : Response

                public data object Conflict : Response
              }
            }

            public class Put internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val name: String,
            ) {
              public suspend operator fun invoke(selectedRepositoryIds: List<Long>): Response {
                val response = client.put("/orgs/$org/actions/variables/$name/repositories") {
                  contentType(ContentType.Application.Json)
                  setBody(Body(selectedRepositoryIds = selectedRepositoryIds))
                }
                return when (response.status.value) {
                  204 -> Response.NoContent
                  409 -> Response.Conflict
                  else -> throw ResponseException(response, "")
                }
              }

              @JvmInline
              @Serializable
              internal value class Body(
                @SerialName("selected_repository_ids")
                public val selectedRepositoryIds: List<Long>,
              )

              public sealed interface Response {
                public data object NoContent : Response

                public data object Conflict : Response
              }
            }

            public class RepositoryIdPath internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val name: String,
              private val repositoryId: Long,
            ) {
              public val delete: Delete = Delete(client, org, name, repositoryId)

              public val put: Put = Put(client, org, name, repositoryId)

              public class Delete internal constructor(
                private val client: HttpClient,
                private val org: String,
                private val name: String,
                private val repositoryId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.delete("/orgs/$org/actions/variables/$name/repositories/$repositoryId")
                  return when (response.status.value) {
                    204 -> Response.NoContent
                    409 -> Response.Conflict
                    else -> throw ResponseException(response, "")
                  }
                }

                public sealed interface Response {
                  public data object NoContent : Response

                  public data object Conflict : Response
                }
              }

              public class Put internal constructor(
                private val client: HttpClient,
                private val org: String,
                private val name: String,
                private val repositoryId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.put("/orgs/$org/actions/variables/$name/repositories/$repositoryId")
                  return when (response.status.value) {
                    204 -> Response.NoContent
                    409 -> Response.Conflict
                    else -> throw ResponseException(response, "")
                  }
                }

                public sealed interface Response {
                  public data object NoContent : Response

                  public data object Conflict : Response
                }
              }
            }
          }
        }
      }
    }

    public class Artifacts internal constructor(
      private val client: HttpClient,
      private val org: String,
    ) {
      public val metadata: Metadata = Metadata(client, org)

      public fun subjectDigest(subjectDigest: String): SubjectDigestPath = SubjectDigestPath(client, org, subjectDigest)

      public class Metadata internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public val deploymentRecord: DeploymentRecord = DeploymentRecord(client, org)

        public val storageRecord: StorageRecord = StorageRecord(client, org)

        public class DeploymentRecord internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public val post: Post = Post(client, org)

          public val cluster: Cluster = Cluster(client, org)

          public class Post internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public suspend operator fun invoke(
              name: String,
              digest: String,
              version: String? = null,
              status: Status,
              logicalEnvironment: String,
              physicalEnvironment: String? = null,
              cluster: String? = null,
              deploymentName: String,
              tags: List<String>? = null,
              runtimeRisks: List<RuntimeRisks>? = null,
              githubRepository: String? = null,
            ): Response = client.post("/orgs/$org/artifacts/metadata/deployment-record") {
              contentType(ContentType.Application.Json)
              setBody(Body(name = name, digest = digest, version = version, status = status, logicalEnvironment = logicalEnvironment, physicalEnvironment = physicalEnvironment, cluster = cluster, deploymentName = deploymentName, tags = tags, runtimeRisks = runtimeRisks, githubRepository = githubRepository))
            }.body()

            @Serializable
            public enum class Status(
              public val `value`: String,
            ) {
              @SerialName("deployed")
              Deployed("deployed"),
              @SerialName("decommissioned")
              Decommissioned("decommissioned"),
              ;
            }

            @Serializable
            public enum class RuntimeRisks(
              public val `value`: String,
            ) {
              @SerialName("critical-resource")
              CriticalResource("critical-resource"),
              @SerialName("internet-exposed")
              InternetExposed("internet-exposed"),
              @SerialName("lateral-movement")
              LateralMovement("lateral-movement"),
              @SerialName("sensitive-data")
              SensitiveData("sensitive-data"),
              ;
            }

            @Serializable
            internal data class Body(
              public val name: String,
              public val digest: String,
              public val version: String? = null,
              public val status: Status,
              @SerialName("logical_environment")
              public val logicalEnvironment: String,
              @SerialName("physical_environment")
              public val physicalEnvironment: String? = null,
              public val cluster: String? = null,
              @SerialName("deployment_name")
              public val deploymentName: String,
              public val tags: List<String>? = null,
              @SerialName("runtime_risks")
              public val runtimeRisks: List<RuntimeRisks>? = null,
              @SerialName("github_repository")
              public val githubRepository: String? = null,
            )

            @Serializable
            public data class Response(
              @SerialName("total_count")
              public val totalCount: Long? = null,
              @SerialName("deployment_records")
              public val deploymentRecords: List<ArtifactDeploymentRecord>? = null,
            )
          }

          public class Cluster internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public fun cluster(cluster: String): ClusterPath = ClusterPath(client, org, cluster)

            public class ClusterPath internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val cluster: String,
            ) {
              public val post: Post = Post(client, org, cluster)

              public class Post internal constructor(
                private val client: HttpClient,
                private val org: String,
                private val cluster: String,
              ) {
                public suspend operator fun invoke(
                  logicalEnvironment: String,
                  physicalEnvironment: String? = null,
                  deployments: List<Deployments>,
                ): Response = client.post("/orgs/$org/artifacts/metadata/deployment-record/cluster/$cluster") {
                  contentType(ContentType.Application.Json)
                  setBody(Body(logicalEnvironment = logicalEnvironment, physicalEnvironment = physicalEnvironment, deployments = deployments))
                }.body()

                @Serializable
                public data class Deployments(
                  public val name: String,
                  public val digest: String,
                  public val version: String? = null,
                  public val status: Status? = null,
                  @SerialName("deployment_name")
                  public val deploymentName: String,
                  @SerialName("github_repository")
                  public val githubRepository: String? = null,
                  public val tags: List<String>? = null,
                  @SerialName("runtime_risks")
                  public val runtimeRisks: List<RuntimeRisks>? = null,
                ) {
                  @Serializable
                  public enum class RuntimeRisks(
                    public val `value`: String,
                  ) {
                    @SerialName("critical-resource")
                    CriticalResource("critical-resource"),
                    @SerialName("internet-exposed")
                    InternetExposed("internet-exposed"),
                    @SerialName("lateral-movement")
                    LateralMovement("lateral-movement"),
                    @SerialName("sensitive-data")
                    SensitiveData("sensitive-data"),
                    ;
                  }

                  @Serializable
                  public enum class Status(
                    public val `value`: String,
                  ) {
                    @SerialName("deployed")
                    Deployed("deployed"),
                    @SerialName("decommissioned")
                    Decommissioned("decommissioned"),
                    ;
                  }
                }

                @Serializable
                internal data class Body(
                  @SerialName("logical_environment")
                  public val logicalEnvironment: String,
                  @SerialName("physical_environment")
                  public val physicalEnvironment: String? = null,
                  public val deployments: List<Deployments>,
                )

                @Serializable
                public data class Response(
                  @SerialName("total_count")
                  public val totalCount: Long? = null,
                  @SerialName("deployment_records")
                  public val deploymentRecords: List<ArtifactDeploymentRecord>? = null,
                )
              }
            }
          }
        }

        public class StorageRecord internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public val post: Post = Post(client, org)

          public class Post internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public suspend operator fun invoke(
              name: String,
              digest: String,
              version: String? = null,
              artifactUrl: String? = null,
              path: String? = null,
              registryUrl: String,
              repository: String? = null,
              status: Status? = null,
              githubRepository: String? = null,
            ): Response = client.post("/orgs/$org/artifacts/metadata/storage-record") {
              contentType(ContentType.Application.Json)
              setBody(Body(name = name, digest = digest, version = version, artifactUrl = artifactUrl, path = path, registryUrl = registryUrl, repository = repository, status = status, githubRepository = githubRepository))
            }.body()

            @Serializable
            public enum class Status(
              public val `value`: String,
            ) {
              @SerialName("active")
              Active("active"),
              @SerialName("eol")
              Eol("eol"),
              @SerialName("deleted")
              Deleted("deleted"),
              ;
            }

            @Serializable
            internal data class Body(
              public val name: String,
              public val digest: String,
              public val version: String? = null,
              @SerialName("artifact_url")
              public val artifactUrl: String? = null,
              public val path: String? = null,
              @SerialName("registry_url")
              public val registryUrl: String,
              public val repository: String? = null,
              public val status: Status? = null,
              @SerialName("github_repository")
              public val githubRepository: String? = null,
            )

            @Serializable
            public data class Response(
              @SerialName("total_count")
              public val totalCount: Long? = null,
              @SerialName("storage_records")
              public val storageRecords: List<StorageRecords>? = null,
            ) {
              @Serializable
              public data class StorageRecords(
                public val id: Long? = null,
                public val name: String? = null,
                public val digest: String? = null,
                @SerialName("artifact_url")
                public val artifactUrl: String? = null,
                @SerialName("registry_url")
                public val registryUrl: String? = null,
                public val repository: String? = null,
                public val status: String? = null,
                @SerialName("created_at")
                public val createdAt: String? = null,
                @SerialName("updated_at")
                public val updatedAt: String? = null,
              )
            }
          }
        }
      }

      public class SubjectDigestPath internal constructor(
        private val client: HttpClient,
        private val org: String,
        private val subjectDigest: String,
      ) {
        public val metadata: Metadata = Metadata(client, org, subjectDigest)

        public class Metadata internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val subjectDigest: String,
        ) {
          public val deploymentRecords: DeploymentRecords =
              DeploymentRecords(client, org, subjectDigest)

          public val storageRecords: StorageRecords = StorageRecords(client, org, subjectDigest)

          public class DeploymentRecords internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val subjectDigest: String,
          ) {
            public val `get`: Get = Get(client, org, subjectDigest)

            public class Get internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val subjectDigest: String,
            ) {
              public suspend operator fun invoke(): Response = client.get("/orgs/$org/artifacts/$subjectDigest/metadata/deployment-records").body()

              @Serializable
              public data class Response(
                @SerialName("total_count")
                public val totalCount: Long? = null,
                @SerialName("deployment_records")
                public val deploymentRecords: List<ArtifactDeploymentRecord>? = null,
              )
            }
          }

          public class StorageRecords internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val subjectDigest: String,
          ) {
            public val `get`: Get = Get(client, org, subjectDigest)

            public class Get internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val subjectDigest: String,
            ) {
              public suspend operator fun invoke(): Response = client.get("/orgs/$org/artifacts/$subjectDigest/metadata/storage-records").body()

              @Serializable
              public data class Response(
                @SerialName("total_count")
                public val totalCount: Long? = null,
                @SerialName("storage_records")
                public val storageRecords: List<StorageRecords>? = null,
              ) {
                @Serializable
                public data class StorageRecords(
                  public val id: Long? = null,
                  public val name: String? = null,
                  public val digest: String? = null,
                  @SerialName("artifact_url")
                  public val artifactUrl: String? = null,
                  @SerialName("registry_url")
                  public val registryUrl: String? = null,
                  public val repository: String? = null,
                  public val status: String? = null,
                  @SerialName("created_at")
                  public val createdAt: String? = null,
                  @SerialName("updated_at")
                  public val updatedAt: String? = null,
                )
              }
            }
          }
        }
      }
    }

    public class Attestations internal constructor(
      private val client: HttpClient,
      private val org: String,
    ) {
      public val bulkList: BulkList = BulkList(client, org)

      public val deleteRequest: DeleteRequest = DeleteRequest(client, org)

      public val digest: Digest = Digest(client, org)

      public val repositories: Repositories = Repositories(client, org)

      public fun attestationId(attestationId: Long): AttestationIdPath = AttestationIdPath(client, org, attestationId)

      public fun subjectDigest(subjectDigest: String): SubjectDigestPath = SubjectDigestPath(client, org, subjectDigest)

      public class BulkList internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public val post: Post = Post(client, org)

        public class Post internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public suspend operator fun invoke(
            subjectDigests: List<String>,
            predicateType: String? = null,
            perPage: Long? = 30L,
            before: String? = null,
            after: String? = null,
          ): Response = client.post("/orgs/$org/attestations/bulk-list") {
            perPage?.let { parameter("per_page", it) }
            before?.let { parameter("before", it) }
            after?.let { parameter("after", it) }
            contentType(ContentType.Application.Json)
            setBody(Body(subjectDigests = subjectDigests, predicateType = predicateType))
          }.body()

          @Serializable
          internal data class Body(
            @SerialName("subject_digests")
            public val subjectDigests: List<String>,
            @SerialName("predicate_type")
            public val predicateType: String? = null,
          )

          @Serializable
          public data class Response(
            @SerialName("attestations_subject_digests")
            public val attestationsSubjectDigests: List<List<AttestationsSubjectDigests>?>? = null,
            @SerialName("page_info")
            public val pageInfo: PageInfo? = null,
          ) {
            @Serializable
            public data class AttestationsSubjectDigests(
              public val bundle: Bundle? = null,
              @SerialName("repository_id")
              public val repositoryId: Long? = null,
              @SerialName("bundle_url")
              public val bundleUrl: String? = null,
            ) {
              /**
               * The bundle of the attestation.
               */
              @Serializable
              public data class Bundle(
                public val mediaType: String? = null,
                public val verificationMaterial: JsonElement? = null,
                public val dsseEnvelope: JsonElement? = null,
              )
            }

            /**
             * Information about the current page.
             */
            @Serializable
            public data class PageInfo(
              @SerialName("has_next")
              public val hasNext: Boolean? = null,
              @SerialName("has_previous")
              public val hasPrevious: Boolean? = null,
              public val next: String? = null,
              public val previous: String? = null,
            )
          }
        }
      }

      public class DeleteRequest internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public val post: Post = Post(client, org)

        public class Post internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public suspend operator fun invoke(body: JsonElement): Response {
            val response = client.post("/orgs/$org/attestations/delete-request") {
              contentType(ContentType.Application.Json)
              setBody(body)
            }
            return when (response.status.value) {
              200 -> Response.Ok
              404 -> Response.NotFound(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data object Ok : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response
          }
        }
      }

      public class Digest internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public fun subjectDigest(subjectDigest: String): SubjectDigestPath = SubjectDigestPath(client, org, subjectDigest)

        public class SubjectDigestPath internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val subjectDigest: String,
        ) {
          public val delete: Delete = Delete(client, org, subjectDigest)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val subjectDigest: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.delete("/orgs/$org/attestations/digest/$subjectDigest")
              return when (response.status.value) {
                200 -> Response.Ok
                204 -> Response.NoContent
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data object Ok : Response

              public data object NoContent : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response
            }
          }
        }
      }

      public class Repositories internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public val `get`: Get = Get(client, org)

        public class Get internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public suspend operator fun invoke(
            perPage: Long? = 30L,
            before: String? = null,
            after: String? = null,
            predicateType: String? = null,
          ): List<IdAndName> = client.get("/orgs/$org/attestations/repositories") {
            perPage?.let { parameter("per_page", it) }
            before?.let { parameter("before", it) }
            after?.let { parameter("after", it) }
            predicateType?.let { parameter("predicate_type", it) }
          }.body()

          @Serializable
          public data class IdAndName(
            public val id: Long? = null,
            public val name: String? = null,
          )
        }
      }

      public class AttestationIdPath internal constructor(
        private val client: HttpClient,
        private val org: String,
        private val attestationId: Long,
      ) {
        public val delete: Delete = Delete(client, org, attestationId)

        public class Delete internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val attestationId: Long,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.delete("/orgs/$org/attestations/$attestationId")
            return when (response.status.value) {
              200 -> Response.Ok
              204 -> Response.NoContent
              403 -> Response.Forbidden(response.body())
              404 -> Response.NotFound(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data object Ok : Response

            public data object NoContent : Response

            public data class Forbidden(
              public val `value`: BasicError,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response
          }
        }
      }

      public class SubjectDigestPath internal constructor(
        private val client: HttpClient,
        private val org: String,
        private val subjectDigest: String,
      ) {
        public val `get`: Get = Get(client, org, subjectDigest)

        public class Get internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val subjectDigest: String,
        ) {
          public suspend operator fun invoke(
            perPage: Long? = 30L,
            before: String? = null,
            after: String? = null,
            predicateType: String? = null,
          ): Response = client.get("/orgs/$org/attestations/$subjectDigest") {
            perPage?.let { parameter("per_page", it) }
            before?.let { parameter("before", it) }
            after?.let { parameter("after", it) }
            predicateType?.let { parameter("predicate_type", it) }
          }.body()

          @JvmInline
          @Serializable
          public value class Response(
            public val attestations: List<Attestations>? = null,
          ) {
            @Serializable
            public data class Attestations(
              public val bundle: Bundle? = null,
              @SerialName("repository_id")
              public val repositoryId: Long? = null,
              @SerialName("bundle_url")
              public val bundleUrl: String? = null,
              public val initiator: String? = null,
            ) {
              /**
               * The attestation's Sigstore Bundle.
               * Refer to the [Sigstore Bundle Specification](https://github.com/sigstore/protobuf-specs/blob/main/protos/sigstore_bundle.proto) for more information.
               */
              @Serializable
              public data class Bundle(
                public val mediaType: String? = null,
                public val verificationMaterial: JsonElement? = null,
                public val dsseEnvelope: JsonElement? = null,
              )
            }
          }
        }
      }
    }

    public class Blocks internal constructor(
      private val client: HttpClient,
      private val org: String,
    ) {
      public val `get`: Get = Get(client, org)

      public fun username(username: String): UsernamePath = UsernamePath(client, org, username)

      public class Get internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<SimpleUser> = client.get("/orgs/$org/blocks") {
          perPage?.let { parameter("per_page", it) }
          page?.let { parameter("page", it) }
        }.body()
      }

      public class UsernamePath internal constructor(
        private val client: HttpClient,
        private val org: String,
        private val username: String,
      ) {
        public val delete: Delete = Delete(client, org, username)

        public val `get`: Get = Get(client, org, username)

        public val put: Put = Put(client, org, username)

        public class Delete internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val username: String,
        ) {
          public suspend operator fun invoke() {
            client.delete("/orgs/$org/blocks/$username")
          }
        }

        public class Get internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val username: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.get("/orgs/$org/blocks/$username")
            return when (response.status.value) {
              204 -> Response.NoContent
              404 -> Response.NotFound(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data object NoContent : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class Put internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val username: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.put("/orgs/$org/blocks/$username")
            return when (response.status.value) {
              204 -> Response.NoContent
              422 -> Response.UnprocessableEntity(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data object NoContent : Response

            public data class UnprocessableEntity(
              public val `value`: ValidationError,
            ) : Response
          }
        }
      }
    }

    public class Campaigns internal constructor(
      private val client: HttpClient,
      private val org: String,
    ) {
      public val `get`: Get = Get(client, org)

      public val post: Post = Post(client, org)

      public fun campaignNumber(campaignNumber: Long): CampaignNumberPath = CampaignNumberPath(client, org, campaignNumber)

      public class Get internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public suspend operator fun invoke(
          page: Long? = 1L,
          perPage: Long? = 30L,
          direction: Direction? = Direction.Desc,
          state: CampaignState? = null,
          sort: Sort? = Sort.Created,
        ): Response {
          val response = client.get("/orgs/$org/campaigns") {
            page?.let { parameter("page", it) }
            perPage?.let { parameter("per_page", it) }
            direction?.let { parameter("direction", it.value) }
            state?.let { parameter("state", it.value) }
            sort?.let { parameter("sort", it.value) }
          }
          return when (response.status.value) {
            200 -> Response.Ok(response.body())
            404 -> Response.NotFound(response.body())
            503 -> response.body<Response.ServiceUnavailable>()
            else -> throw ResponseException(response, "")
          }
        }

        @Serializable
        public enum class Direction(
          public val `value`: String,
        ) {
          @SerialName("asc")
          Asc("asc"),
          @SerialName("desc")
          Desc("desc"),
          ;
        }

        @Serializable
        public enum class Sort(
          public val `value`: String,
        ) {
          @SerialName("created")
          Created("created"),
          @SerialName("updated")
          Updated("updated"),
          @SerialName("ends_at")
          EndsAt("ends_at"),
          @SerialName("published")
          Published("published"),
          ;
        }

        public sealed interface Response {
          public data class Ok(
            public val `value`: List<CampaignSummary>,
          ) : Response

          public data class NotFound(
            public val `value`: BasicError,
          ) : Response

          @Serializable
          public data class ServiceUnavailable(
            public val code: String? = null,
            public val message: String? = null,
            @SerialName("documentation_url")
            public val documentationUrl: String? = null,
          ) : Response
        }
      }

      public class Post internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public suspend operator fun invoke(
          name: String,
          description: String,
          managers: List<String>? = null,
          teamManagers: List<String>? = null,
          endsAt: Instant,
          contactLink: String? = null,
          codeScanningAlerts: List<CodeScanningAlerts>? = null,
          generateIssues: Boolean? = null,
        ): Response {
          val response = client.post("/orgs/$org/campaigns") {
            contentType(ContentType.Application.Json)
            setBody(Body(name = name, description = description, managers = managers, teamManagers = teamManagers, endsAt = endsAt, contactLink = contactLink, codeScanningAlerts = codeScanningAlerts, generateIssues = generateIssues))
          }
          return when (response.status.value) {
            200 -> Response.Ok(response.body())
            400 -> Response.BadRequest(response.body())
            404 -> Response.NotFound(response.body())
            422 -> Response.UnprocessableEntity(response.body())
            429 -> Response.TooManyRequests
            503 -> response.body<Response.ServiceUnavailable>()
            else -> throw ResponseException(response, "")
          }
        }

        @Serializable
        public data class CodeScanningAlerts(
          @SerialName("repository_id")
          public val repositoryId: Long,
          @SerialName("alert_numbers")
          public val alertNumbers: List<Long>,
        )

        @Serializable
        internal data class Body(
          public val name: String,
          public val description: String,
          public val managers: List<String>? = null,
          @SerialName("team_managers")
          public val teamManagers: List<String>? = null,
          @SerialName("ends_at")
          public val endsAt: Instant,
          @SerialName("contact_link")
          public val contactLink: String? = null,
          @SerialName("code_scanning_alerts")
          public val codeScanningAlerts: List<CodeScanningAlerts>? = null,
          @SerialName("generate_issues")
          public val generateIssues: Boolean? = null,
        )

        public sealed interface Response {
          public data class Ok(
            public val `value`: CampaignSummary,
          ) : Response

          public data class BadRequest(
            public val `value`: BasicError,
          ) : Response

          public data class NotFound(
            public val `value`: BasicError,
          ) : Response

          public data class UnprocessableEntity(
            public val `value`: BasicError,
          ) : Response

          public data object TooManyRequests : Response

          @Serializable
          public data class ServiceUnavailable(
            public val code: String? = null,
            public val message: String? = null,
            @SerialName("documentation_url")
            public val documentationUrl: String? = null,
          ) : Response
        }
      }

      public class CampaignNumberPath internal constructor(
        private val client: HttpClient,
        private val org: String,
        private val campaignNumber: Long,
      ) {
        public val delete: Delete = Delete(client, org, campaignNumber)

        public val `get`: Get = Get(client, org, campaignNumber)

        public val patch: Patch = Patch(client, org, campaignNumber)

        public class Delete internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val campaignNumber: Long,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.delete("/orgs/$org/campaigns/$campaignNumber")
            return when (response.status.value) {
              204 -> Response.NoContent
              404 -> Response.NotFound(response.body())
              503 -> response.body<Response.ServiceUnavailable>()
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data object NoContent : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response

            @Serializable
            public data class ServiceUnavailable(
              public val code: String? = null,
              public val message: String? = null,
              @SerialName("documentation_url")
              public val documentationUrl: String? = null,
            ) : Response
          }
        }

        public class Get internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val campaignNumber: Long,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.get("/orgs/$org/campaigns/$campaignNumber")
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              404 -> Response.NotFound(response.body())
              422 -> Response.UnprocessableEntity(response.body())
              503 -> response.body<Response.ServiceUnavailable>()
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: CampaignSummary,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response

            public data class UnprocessableEntity(
              public val `value`: BasicError,
            ) : Response

            @Serializable
            public data class ServiceUnavailable(
              public val code: String? = null,
              public val message: String? = null,
              @SerialName("documentation_url")
              public val documentationUrl: String? = null,
            ) : Response
          }
        }

        public class Patch internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val campaignNumber: Long,
        ) {
          public suspend operator fun invoke(
            name: String? = null,
            description: String? = null,
            managers: List<String>? = null,
            teamManagers: List<String>? = null,
            endsAt: Instant? = null,
            contactLink: String? = null,
            state: CampaignState? = null,
          ): Response {
            val response = client.patch("/orgs/$org/campaigns/$campaignNumber") {
              contentType(ContentType.Application.Json)
              setBody(Body(name = name, description = description, managers = managers, teamManagers = teamManagers, endsAt = endsAt, contactLink = contactLink, state = state))
            }
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              400 -> Response.BadRequest(response.body())
              404 -> Response.NotFound(response.body())
              422 -> Response.UnprocessableEntity(response.body())
              503 -> response.body<Response.ServiceUnavailable>()
              else -> throw ResponseException(response, "")
            }
          }

          @Serializable
          internal data class Body(
            public val name: String? = null,
            public val description: String? = null,
            public val managers: List<String>? = null,
            @SerialName("team_managers")
            public val teamManagers: List<String>? = null,
            @SerialName("ends_at")
            public val endsAt: Instant? = null,
            @SerialName("contact_link")
            public val contactLink: String? = null,
            public val state: CampaignState? = null,
          )

          public sealed interface Response {
            public data class Ok(
              public val `value`: CampaignSummary,
            ) : Response

            public data class BadRequest(
              public val `value`: BasicError,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response

            public data class UnprocessableEntity(
              public val `value`: BasicError,
            ) : Response

            @Serializable
            public data class ServiceUnavailable(
              public val code: String? = null,
              public val message: String? = null,
              @SerialName("documentation_url")
              public val documentationUrl: String? = null,
            ) : Response
          }
        }
      }
    }

    public class CodeScanning internal constructor(
      private val client: HttpClient,
      private val org: String,
    ) {
      public val alerts: Alerts = Alerts(client, org)

      public class Alerts internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public val `get`: Get = Get(client, org)

        public class Get internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public suspend operator fun invoke(
            toolName: String? = null,
            toolGuid: String? = null,
            before: String? = null,
            after: String? = null,
            page: Long? = 1L,
            perPage: Long? = 30L,
            direction: Direction? = Direction.Desc,
            state: CodeScanningAlertStateQuery? = null,
            sort: Sort? = Sort.Created,
            severity: CodeScanningAlertSeverity? = null,
            assignees: String? = null,
          ): Response {
            val response = client.get("/orgs/$org/code-scanning/alerts") {
              toolName?.let { parameter("tool_name", it) }
              toolGuid?.let { parameter("tool_guid", it) }
              before?.let { parameter("before", it) }
              after?.let { parameter("after", it) }
              page?.let { parameter("page", it) }
              perPage?.let { parameter("per_page", it) }
              direction?.let { parameter("direction", it.value) }
              state?.let { parameter("state", it.value) }
              sort?.let { parameter("sort", it.value) }
              severity?.let { parameter("severity", it.value) }
              assignees?.let { parameter("assignees", it) }
            }
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              404 -> Response.NotFound(response.body())
              503 -> response.body<Response.ServiceUnavailable>()
              else -> throw ResponseException(response, "")
            }
          }

          @Serializable
          public enum class Direction(
            public val `value`: String,
          ) {
            @SerialName("asc")
            Asc("asc"),
            @SerialName("desc")
            Desc("desc"),
            ;
          }

          @Serializable
          public enum class Sort(
            public val `value`: String,
          ) {
            @SerialName("created")
            Created("created"),
            @SerialName("updated")
            Updated("updated"),
            ;
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: List<CodeScanningOrganizationAlertItems>,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response

            @Serializable
            public data class ServiceUnavailable(
              public val code: String? = null,
              public val message: String? = null,
              @SerialName("documentation_url")
              public val documentationUrl: String? = null,
            ) : Response
          }
        }
      }
    }

    public class CodeSecurity internal constructor(
      private val client: HttpClient,
      private val org: String,
    ) {
      public val configurations: Configurations = Configurations(client, org)

      public class Configurations internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public val `get`: Get = Get(client, org)

        public val post: Post = Post(client, org)

        public val defaults: Defaults = Defaults(client, org)

        public val detach: Detach = Detach(client, org)

        public fun configurationId(configurationId: Long): ConfigurationIdPath = ConfigurationIdPath(client, org, configurationId)

        public class Get internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public suspend operator fun invoke(
            targetType: TargetType? = TargetType.All,
            perPage: Long? = 30L,
            before: String? = null,
            after: String? = null,
          ): Response {
            val response = client.get("/orgs/$org/code-security/configurations") {
              targetType?.let { parameter("target_type", it.value) }
              perPage?.let { parameter("per_page", it) }
              before?.let { parameter("before", it) }
              after?.let { parameter("after", it) }
            }
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              403 -> Response.Forbidden(response.body())
              404 -> Response.NotFound(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          @Serializable
          public enum class TargetType(
            public val `value`: String,
          ) {
            @SerialName("global")
            Global("global"),
            @SerialName("all")
            All("all"),
            ;
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: List<CodeSecurityConfiguration>,
            ) : Response

            public data class Forbidden(
              public val `value`: BasicError,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class Post internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public suspend operator fun invoke(
            name: String,
            description: String,
            advancedSecurity: AdvancedSecurity? = null,
            codeSecurity: CodeSecurity? = null,
            dependencyGraph: DependencyGraph? = null,
            dependencyGraphAutosubmitAction: DependencyGraphAutosubmitAction? = null,
            dependencyGraphAutosubmitActionOptions: DependencyGraphAutosubmitActionOptions? = null,
            dependabotAlerts: DependabotAlerts? = null,
            dependabotSecurityUpdates: DependabotSecurityUpdates? = null,
            dependabotDelegatedAlertDismissal: DependabotDelegatedAlertDismissal? = null,
            codeScanningOptions: CodeScanningOptions? = null,
            codeScanningDefaultSetup: CodeScanningDefaultSetup? = null,
            codeScanningDefaultSetupOptions: CodeScanningDefaultSetupOptions? = null,
            codeScanningDelegatedAlertDismissal: CodeScanningDelegatedAlertDismissal? = null,
            secretProtection: SecretProtection? = null,
            secretScanning: SecretScanning? = null,
            secretScanningPushProtection: SecretScanningPushProtection? = null,
            secretScanningDelegatedBypass: SecretScanningDelegatedBypass? = null,
            secretScanningDelegatedBypassOptions: SecretScanningDelegatedBypassOptions? = null,
            secretScanningValidityChecks: SecretScanningValidityChecks? = null,
            secretScanningNonProviderPatterns: SecretScanningNonProviderPatterns? = null,
            secretScanningGenericSecrets: SecretScanningGenericSecrets? = null,
            secretScanningDelegatedAlertDismissal: SecretScanningDelegatedAlertDismissal? = null,
            secretScanningExtendedMetadata: SecretScanningExtendedMetadata? = null,
            privateVulnerabilityReporting: PrivateVulnerabilityReporting? = null,
            enforcement: Enforcement? = null,
          ): CodeSecurityConfiguration = client.post("/orgs/$org/code-security/configurations") {
            contentType(ContentType.Application.Json)
            setBody(Body(name = name, description = description, advancedSecurity = advancedSecurity, codeSecurity = codeSecurity, dependencyGraph = dependencyGraph, dependencyGraphAutosubmitAction = dependencyGraphAutosubmitAction, dependencyGraphAutosubmitActionOptions = dependencyGraphAutosubmitActionOptions, dependabotAlerts = dependabotAlerts, dependabotSecurityUpdates = dependabotSecurityUpdates, dependabotDelegatedAlertDismissal = dependabotDelegatedAlertDismissal, codeScanningOptions = codeScanningOptions, codeScanningDefaultSetup = codeScanningDefaultSetup, codeScanningDefaultSetupOptions = codeScanningDefaultSetupOptions, codeScanningDelegatedAlertDismissal = codeScanningDelegatedAlertDismissal, secretProtection = secretProtection, secretScanning = secretScanning, secretScanningPushProtection = secretScanningPushProtection, secretScanningDelegatedBypass = secretScanningDelegatedBypass, secretScanningDelegatedBypassOptions = secretScanningDelegatedBypassOptions, secretScanningValidityChecks = secretScanningValidityChecks, secretScanningNonProviderPatterns = secretScanningNonProviderPatterns, secretScanningGenericSecrets = secretScanningGenericSecrets, secretScanningDelegatedAlertDismissal = secretScanningDelegatedAlertDismissal, secretScanningExtendedMetadata = secretScanningExtendedMetadata, privateVulnerabilityReporting = privateVulnerabilityReporting, enforcement = enforcement))
          }.body()

          @Serializable
          public enum class AdvancedSecurity(
            public val `value`: String,
          ) {
            @SerialName("enabled")
            Enabled("enabled"),
            @SerialName("disabled")
            Disabled("disabled"),
            @SerialName("code_security")
            CodeSecurity("code_security"),
            @SerialName("secret_protection")
            SecretProtection("secret_protection"),
            ;
          }

          @Serializable
          public enum class CodeSecurity(
            public val `value`: String,
          ) {
            @SerialName("enabled")
            Enabled("enabled"),
            @SerialName("disabled")
            Disabled("disabled"),
            @SerialName("not_set")
            NotSet("not_set"),
            ;
          }

          @Serializable
          public enum class DependencyGraph(
            public val `value`: String,
          ) {
            @SerialName("enabled")
            Enabled("enabled"),
            @SerialName("disabled")
            Disabled("disabled"),
            @SerialName("not_set")
            NotSet("not_set"),
            ;
          }

          @Serializable
          public enum class DependencyGraphAutosubmitAction(
            public val `value`: String,
          ) {
            @SerialName("enabled")
            Enabled("enabled"),
            @SerialName("disabled")
            Disabled("disabled"),
            @SerialName("not_set")
            NotSet("not_set"),
            ;
          }

          /**
           * Feature options for Automatic dependency submission
           */
          @JvmInline
          @Serializable
          public value class DependencyGraphAutosubmitActionOptions(
            @SerialName("labeled_runners")
            public val labeledRunners: Boolean? = null,
          )

          @Serializable
          public enum class DependabotAlerts(
            public val `value`: String,
          ) {
            @SerialName("enabled")
            Enabled("enabled"),
            @SerialName("disabled")
            Disabled("disabled"),
            @SerialName("not_set")
            NotSet("not_set"),
            ;
          }

          @Serializable
          public enum class DependabotSecurityUpdates(
            public val `value`: String,
          ) {
            @SerialName("enabled")
            Enabled("enabled"),
            @SerialName("disabled")
            Disabled("disabled"),
            @SerialName("not_set")
            NotSet("not_set"),
            ;
          }

          @Serializable
          public enum class DependabotDelegatedAlertDismissal(
            public val `value`: String,
          ) {
            @SerialName("enabled")
            Enabled("enabled"),
            @SerialName("disabled")
            Disabled("disabled"),
            @SerialName("not_set")
            NotSet("not_set"),
            ;
          }

          @Serializable
          public enum class CodeScanningDefaultSetup(
            public val `value`: String,
          ) {
            @SerialName("enabled")
            Enabled("enabled"),
            @SerialName("disabled")
            Disabled("disabled"),
            @SerialName("not_set")
            NotSet("not_set"),
            ;
          }

          @Serializable
          public enum class CodeScanningDelegatedAlertDismissal(
            public val `value`: String,
          ) {
            @SerialName("enabled")
            Enabled("enabled"),
            @SerialName("disabled")
            Disabled("disabled"),
            @SerialName("not_set")
            NotSet("not_set"),
            ;
          }

          @Serializable
          public enum class SecretProtection(
            public val `value`: String,
          ) {
            @SerialName("enabled")
            Enabled("enabled"),
            @SerialName("disabled")
            Disabled("disabled"),
            @SerialName("not_set")
            NotSet("not_set"),
            ;
          }

          @Serializable
          public enum class SecretScanning(
            public val `value`: String,
          ) {
            @SerialName("enabled")
            Enabled("enabled"),
            @SerialName("disabled")
            Disabled("disabled"),
            @SerialName("not_set")
            NotSet("not_set"),
            ;
          }

          @Serializable
          public enum class SecretScanningPushProtection(
            public val `value`: String,
          ) {
            @SerialName("enabled")
            Enabled("enabled"),
            @SerialName("disabled")
            Disabled("disabled"),
            @SerialName("not_set")
            NotSet("not_set"),
            ;
          }

          @Serializable
          public enum class SecretScanningDelegatedBypass(
            public val `value`: String,
          ) {
            @SerialName("enabled")
            Enabled("enabled"),
            @SerialName("disabled")
            Disabled("disabled"),
            @SerialName("not_set")
            NotSet("not_set"),
            ;
          }

          /**
           * Feature options for secret scanning delegated bypass
           */
          @JvmInline
          @Serializable
          public value class SecretScanningDelegatedBypassOptions(
            public val reviewers: List<Reviewers>? = null,
          ) {
            @Serializable
            public data class Reviewers(
              @SerialName("reviewer_id")
              public val reviewerId: Long,
              @SerialName("reviewer_type")
              public val reviewerType: ReviewerType,
            ) {
              @Serializable
              public enum class ReviewerType {
                TEAM,
                ROLE,
              }
            }
          }

          @Serializable
          public enum class SecretScanningValidityChecks(
            public val `value`: String,
          ) {
            @SerialName("enabled")
            Enabled("enabled"),
            @SerialName("disabled")
            Disabled("disabled"),
            @SerialName("not_set")
            NotSet("not_set"),
            ;
          }

          @Serializable
          public enum class SecretScanningNonProviderPatterns(
            public val `value`: String,
          ) {
            @SerialName("enabled")
            Enabled("enabled"),
            @SerialName("disabled")
            Disabled("disabled"),
            @SerialName("not_set")
            NotSet("not_set"),
            ;
          }

          @Serializable
          public enum class SecretScanningGenericSecrets(
            public val `value`: String,
          ) {
            @SerialName("enabled")
            Enabled("enabled"),
            @SerialName("disabled")
            Disabled("disabled"),
            @SerialName("not_set")
            NotSet("not_set"),
            ;
          }

          @Serializable
          public enum class SecretScanningDelegatedAlertDismissal(
            public val `value`: String,
          ) {
            @SerialName("enabled")
            Enabled("enabled"),
            @SerialName("disabled")
            Disabled("disabled"),
            @SerialName("not_set")
            NotSet("not_set"),
            ;
          }

          @Serializable
          public enum class SecretScanningExtendedMetadata(
            public val `value`: String,
          ) {
            @SerialName("enabled")
            Enabled("enabled"),
            @SerialName("disabled")
            Disabled("disabled"),
            @SerialName("not_set")
            NotSet("not_set"),
            ;
          }

          @Serializable
          public enum class PrivateVulnerabilityReporting(
            public val `value`: String,
          ) {
            @SerialName("enabled")
            Enabled("enabled"),
            @SerialName("disabled")
            Disabled("disabled"),
            @SerialName("not_set")
            NotSet("not_set"),
            ;
          }

          @Serializable
          public enum class Enforcement(
            public val `value`: String,
          ) {
            @SerialName("enforced")
            Enforced("enforced"),
            @SerialName("unenforced")
            Unenforced("unenforced"),
            ;
          }

          @Serializable
          internal data class Body(
            public val name: String,
            public val description: String,
            @SerialName("advanced_security")
            public val advancedSecurity: AdvancedSecurity? = null,
            @SerialName("code_security")
            public val codeSecurity: CodeSecurity? = null,
            @SerialName("dependency_graph")
            public val dependencyGraph: DependencyGraph? = null,
            @SerialName("dependency_graph_autosubmit_action")
            public val dependencyGraphAutosubmitAction: DependencyGraphAutosubmitAction? = null,
            @SerialName("dependency_graph_autosubmit_action_options")
            public val dependencyGraphAutosubmitActionOptions:
                DependencyGraphAutosubmitActionOptions? = null,
            @SerialName("dependabot_alerts")
            public val dependabotAlerts: DependabotAlerts? = null,
            @SerialName("dependabot_security_updates")
            public val dependabotSecurityUpdates: DependabotSecurityUpdates? = null,
            @SerialName("dependabot_delegated_alert_dismissal")
            public val dependabotDelegatedAlertDismissal: DependabotDelegatedAlertDismissal? = null,
            @SerialName("code_scanning_options")
            public val codeScanningOptions: CodeScanningOptions? = null,
            @SerialName("code_scanning_default_setup")
            public val codeScanningDefaultSetup: CodeScanningDefaultSetup? = null,
            @SerialName("code_scanning_default_setup_options")
            public val codeScanningDefaultSetupOptions: CodeScanningDefaultSetupOptions? = null,
            @SerialName("code_scanning_delegated_alert_dismissal")
            public val codeScanningDelegatedAlertDismissal:
                CodeScanningDelegatedAlertDismissal? = null,
            @SerialName("secret_protection")
            public val secretProtection: SecretProtection? = null,
            @SerialName("secret_scanning")
            public val secretScanning: SecretScanning? = null,
            @SerialName("secret_scanning_push_protection")
            public val secretScanningPushProtection: SecretScanningPushProtection? = null,
            @SerialName("secret_scanning_delegated_bypass")
            public val secretScanningDelegatedBypass: SecretScanningDelegatedBypass? = null,
            @SerialName("secret_scanning_delegated_bypass_options")
            public val secretScanningDelegatedBypassOptions:
                SecretScanningDelegatedBypassOptions? = null,
            @SerialName("secret_scanning_validity_checks")
            public val secretScanningValidityChecks: SecretScanningValidityChecks? = null,
            @SerialName("secret_scanning_non_provider_patterns")
            public val secretScanningNonProviderPatterns: SecretScanningNonProviderPatterns? = null,
            @SerialName("secret_scanning_generic_secrets")
            public val secretScanningGenericSecrets: SecretScanningGenericSecrets? = null,
            @SerialName("secret_scanning_delegated_alert_dismissal")
            public val secretScanningDelegatedAlertDismissal:
                SecretScanningDelegatedAlertDismissal? = null,
            @SerialName("secret_scanning_extended_metadata")
            public val secretScanningExtendedMetadata: SecretScanningExtendedMetadata? = null,
            @SerialName("private_vulnerability_reporting")
            public val privateVulnerabilityReporting: PrivateVulnerabilityReporting? = null,
            public val enforcement: Enforcement? = null,
          )
        }

        public class Defaults internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public val `get`: Get = Get(client, org)

          public class Get internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.get("/orgs/$org/code-security/configurations/defaults")
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                304 -> Response.NotModified
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: CodeSecurityDefaultConfigurations,
              ) : Response

              public data object NotModified : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response
            }
          }
        }

        public class Detach internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public val delete: Delete = Delete(client, org)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public suspend operator fun invoke(selectedRepositoryIds: List<Long>? = null): Response {
              val response = client.delete("/orgs/$org/code-security/configurations/detach") {
                contentType(ContentType.Application.Json)
                setBody(Body(selectedRepositoryIds = selectedRepositoryIds))
              }
              return when (response.status.value) {
                204 -> Response.NoContent
                400 -> Response.BadRequest(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                409 -> Response.Conflict(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            @JvmInline
            @Serializable
            internal value class Body(
              @SerialName("selected_repository_ids")
              public val selectedRepositoryIds: List<Long>? = null,
            )

            public sealed interface Response {
              public data object NoContent : Response

              public data class BadRequest(
                public val `value`: BasicError,
              ) : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response

              public data class Conflict(
                public val `value`: BasicError,
              ) : Response
            }
          }
        }

        public class ConfigurationIdPath internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val configurationId: Long,
        ) {
          public val delete: Delete = Delete(client, org, configurationId)

          public val `get`: Get = Get(client, org, configurationId)

          public val patch: Patch = Patch(client, org, configurationId)

          public val attach: Attach = Attach(client, org, configurationId)

          public val defaults: Defaults = Defaults(client, org, configurationId)

          public val repositories: Repositories = Repositories(client, org, configurationId)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val configurationId: Long,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.delete("/orgs/$org/code-security/configurations/$configurationId")
              return when (response.status.value) {
                204 -> Response.NoContent
                400 -> Response.BadRequest(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                409 -> Response.Conflict(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data object NoContent : Response

              public data class BadRequest(
                public val `value`: BasicError,
              ) : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response

              public data class Conflict(
                public val `value`: BasicError,
              ) : Response
            }
          }

          public class Get internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val configurationId: Long,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.get("/orgs/$org/code-security/configurations/$configurationId")
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                304 -> Response.NotModified
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: CodeSecurityConfiguration,
              ) : Response

              public data object NotModified : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response
            }
          }

          public class Patch internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val configurationId: Long,
          ) {
            public suspend operator fun invoke(
              name: String? = null,
              description: String? = null,
              advancedSecurity: AdvancedSecurity? = null,
              codeSecurity: CodeSecurity? = null,
              dependencyGraph: DependencyGraph? = null,
              dependencyGraphAutosubmitAction: DependencyGraphAutosubmitAction? = null,
              dependencyGraphAutosubmitActionOptions: DependencyGraphAutosubmitActionOptions? = null,
              dependabotAlerts: DependabotAlerts? = null,
              dependabotSecurityUpdates: DependabotSecurityUpdates? = null,
              dependabotDelegatedAlertDismissal: DependabotDelegatedAlertDismissal? = null,
              codeScanningDefaultSetup: CodeScanningDefaultSetup? = null,
              codeScanningDefaultSetupOptions: CodeScanningDefaultSetupOptions? = null,
              codeScanningOptions: CodeScanningOptions? = null,
              codeScanningDelegatedAlertDismissal: CodeScanningDelegatedAlertDismissal? = null,
              secretProtection: SecretProtection? = null,
              secretScanning: SecretScanning? = null,
              secretScanningPushProtection: SecretScanningPushProtection? = null,
              secretScanningDelegatedBypass: SecretScanningDelegatedBypass? = null,
              secretScanningDelegatedBypassOptions: SecretScanningDelegatedBypassOptions? = null,
              secretScanningValidityChecks: SecretScanningValidityChecks? = null,
              secretScanningNonProviderPatterns: SecretScanningNonProviderPatterns? = null,
              secretScanningGenericSecrets: SecretScanningGenericSecrets? = null,
              secretScanningDelegatedAlertDismissal: SecretScanningDelegatedAlertDismissal? = null,
              secretScanningExtendedMetadata: SecretScanningExtendedMetadata? = null,
              privateVulnerabilityReporting: PrivateVulnerabilityReporting? = null,
              enforcement: Enforcement? = null,
            ): Response {
              val response = client.patch("/orgs/$org/code-security/configurations/$configurationId") {
                contentType(ContentType.Application.Json)
                setBody(Body(name = name, description = description, advancedSecurity = advancedSecurity, codeSecurity = codeSecurity, dependencyGraph = dependencyGraph, dependencyGraphAutosubmitAction = dependencyGraphAutosubmitAction, dependencyGraphAutosubmitActionOptions = dependencyGraphAutosubmitActionOptions, dependabotAlerts = dependabotAlerts, dependabotSecurityUpdates = dependabotSecurityUpdates, dependabotDelegatedAlertDismissal = dependabotDelegatedAlertDismissal, codeScanningDefaultSetup = codeScanningDefaultSetup, codeScanningDefaultSetupOptions = codeScanningDefaultSetupOptions, codeScanningOptions = codeScanningOptions, codeScanningDelegatedAlertDismissal = codeScanningDelegatedAlertDismissal, secretProtection = secretProtection, secretScanning = secretScanning, secretScanningPushProtection = secretScanningPushProtection, secretScanningDelegatedBypass = secretScanningDelegatedBypass, secretScanningDelegatedBypassOptions = secretScanningDelegatedBypassOptions, secretScanningValidityChecks = secretScanningValidityChecks, secretScanningNonProviderPatterns = secretScanningNonProviderPatterns, secretScanningGenericSecrets = secretScanningGenericSecrets, secretScanningDelegatedAlertDismissal = secretScanningDelegatedAlertDismissal, secretScanningExtendedMetadata = secretScanningExtendedMetadata, privateVulnerabilityReporting = privateVulnerabilityReporting, enforcement = enforcement))
              }
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                204 -> Response.NoContent
                else -> throw ResponseException(response, "")
              }
            }

            @Serializable
            public enum class AdvancedSecurity(
              public val `value`: String,
            ) {
              @SerialName("enabled")
              Enabled("enabled"),
              @SerialName("disabled")
              Disabled("disabled"),
              @SerialName("code_security")
              CodeSecurity("code_security"),
              @SerialName("secret_protection")
              SecretProtection("secret_protection"),
              ;
            }

            @Serializable
            public enum class CodeSecurity(
              public val `value`: String,
            ) {
              @SerialName("enabled")
              Enabled("enabled"),
              @SerialName("disabled")
              Disabled("disabled"),
              @SerialName("not_set")
              NotSet("not_set"),
              ;
            }

            @Serializable
            public enum class DependencyGraph(
              public val `value`: String,
            ) {
              @SerialName("enabled")
              Enabled("enabled"),
              @SerialName("disabled")
              Disabled("disabled"),
              @SerialName("not_set")
              NotSet("not_set"),
              ;
            }

            @Serializable
            public enum class DependencyGraphAutosubmitAction(
              public val `value`: String,
            ) {
              @SerialName("enabled")
              Enabled("enabled"),
              @SerialName("disabled")
              Disabled("disabled"),
              @SerialName("not_set")
              NotSet("not_set"),
              ;
            }

            /**
             * Feature options for Automatic dependency submission
             */
            @JvmInline
            @Serializable
            public value class DependencyGraphAutosubmitActionOptions(
              @SerialName("labeled_runners")
              public val labeledRunners: Boolean? = null,
            )

            @Serializable
            public enum class DependabotAlerts(
              public val `value`: String,
            ) {
              @SerialName("enabled")
              Enabled("enabled"),
              @SerialName("disabled")
              Disabled("disabled"),
              @SerialName("not_set")
              NotSet("not_set"),
              ;
            }

            @Serializable
            public enum class DependabotSecurityUpdates(
              public val `value`: String,
            ) {
              @SerialName("enabled")
              Enabled("enabled"),
              @SerialName("disabled")
              Disabled("disabled"),
              @SerialName("not_set")
              NotSet("not_set"),
              ;
            }

            @Serializable
            public enum class DependabotDelegatedAlertDismissal(
              public val `value`: String,
            ) {
              @SerialName("enabled")
              Enabled("enabled"),
              @SerialName("disabled")
              Disabled("disabled"),
              @SerialName("not_set")
              NotSet("not_set"),
              ;
            }

            @Serializable
            public enum class CodeScanningDefaultSetup(
              public val `value`: String,
            ) {
              @SerialName("enabled")
              Enabled("enabled"),
              @SerialName("disabled")
              Disabled("disabled"),
              @SerialName("not_set")
              NotSet("not_set"),
              ;
            }

            @Serializable
            public enum class CodeScanningDelegatedAlertDismissal(
              public val `value`: String,
            ) {
              @SerialName("enabled")
              Enabled("enabled"),
              @SerialName("disabled")
              Disabled("disabled"),
              @SerialName("not_set")
              NotSet("not_set"),
              ;
            }

            @Serializable
            public enum class SecretProtection(
              public val `value`: String,
            ) {
              @SerialName("enabled")
              Enabled("enabled"),
              @SerialName("disabled")
              Disabled("disabled"),
              @SerialName("not_set")
              NotSet("not_set"),
              ;
            }

            @Serializable
            public enum class SecretScanning(
              public val `value`: String,
            ) {
              @SerialName("enabled")
              Enabled("enabled"),
              @SerialName("disabled")
              Disabled("disabled"),
              @SerialName("not_set")
              NotSet("not_set"),
              ;
            }

            @Serializable
            public enum class SecretScanningPushProtection(
              public val `value`: String,
            ) {
              @SerialName("enabled")
              Enabled("enabled"),
              @SerialName("disabled")
              Disabled("disabled"),
              @SerialName("not_set")
              NotSet("not_set"),
              ;
            }

            @Serializable
            public enum class SecretScanningDelegatedBypass(
              public val `value`: String,
            ) {
              @SerialName("enabled")
              Enabled("enabled"),
              @SerialName("disabled")
              Disabled("disabled"),
              @SerialName("not_set")
              NotSet("not_set"),
              ;
            }

            /**
             * Feature options for secret scanning delegated bypass
             */
            @JvmInline
            @Serializable
            public value class SecretScanningDelegatedBypassOptions(
              public val reviewers: List<Reviewers>? = null,
            ) {
              @Serializable
              public data class Reviewers(
                @SerialName("reviewer_id")
                public val reviewerId: Long,
                @SerialName("reviewer_type")
                public val reviewerType: ReviewerType,
              ) {
                @Serializable
                public enum class ReviewerType {
                  TEAM,
                  ROLE,
                }
              }
            }

            @Serializable
            public enum class SecretScanningValidityChecks(
              public val `value`: String,
            ) {
              @SerialName("enabled")
              Enabled("enabled"),
              @SerialName("disabled")
              Disabled("disabled"),
              @SerialName("not_set")
              NotSet("not_set"),
              ;
            }

            @Serializable
            public enum class SecretScanningNonProviderPatterns(
              public val `value`: String,
            ) {
              @SerialName("enabled")
              Enabled("enabled"),
              @SerialName("disabled")
              Disabled("disabled"),
              @SerialName("not_set")
              NotSet("not_set"),
              ;
            }

            @Serializable
            public enum class SecretScanningGenericSecrets(
              public val `value`: String,
            ) {
              @SerialName("enabled")
              Enabled("enabled"),
              @SerialName("disabled")
              Disabled("disabled"),
              @SerialName("not_set")
              NotSet("not_set"),
              ;
            }

            @Serializable
            public enum class SecretScanningDelegatedAlertDismissal(
              public val `value`: String,
            ) {
              @SerialName("enabled")
              Enabled("enabled"),
              @SerialName("disabled")
              Disabled("disabled"),
              @SerialName("not_set")
              NotSet("not_set"),
              ;
            }

            @Serializable
            public enum class SecretScanningExtendedMetadata(
              public val `value`: String,
            ) {
              @SerialName("enabled")
              Enabled("enabled"),
              @SerialName("disabled")
              Disabled("disabled"),
              @SerialName("not_set")
              NotSet("not_set"),
              ;
            }

            @Serializable
            public enum class PrivateVulnerabilityReporting(
              public val `value`: String,
            ) {
              @SerialName("enabled")
              Enabled("enabled"),
              @SerialName("disabled")
              Disabled("disabled"),
              @SerialName("not_set")
              NotSet("not_set"),
              ;
            }

            @Serializable
            public enum class Enforcement(
              public val `value`: String,
            ) {
              @SerialName("enforced")
              Enforced("enforced"),
              @SerialName("unenforced")
              Unenforced("unenforced"),
              ;
            }

            @Serializable
            internal data class Body(
              public val name: String? = null,
              public val description: String? = null,
              @SerialName("advanced_security")
              public val advancedSecurity: AdvancedSecurity? = null,
              @SerialName("code_security")
              public val codeSecurity: CodeSecurity? = null,
              @SerialName("dependency_graph")
              public val dependencyGraph: DependencyGraph? = null,
              @SerialName("dependency_graph_autosubmit_action")
              public val dependencyGraphAutosubmitAction: DependencyGraphAutosubmitAction? = null,
              @SerialName("dependency_graph_autosubmit_action_options")
              public val dependencyGraphAutosubmitActionOptions:
                  DependencyGraphAutosubmitActionOptions? = null,
              @SerialName("dependabot_alerts")
              public val dependabotAlerts: DependabotAlerts? = null,
              @SerialName("dependabot_security_updates")
              public val dependabotSecurityUpdates: DependabotSecurityUpdates? = null,
              @SerialName("dependabot_delegated_alert_dismissal")
              public val dependabotDelegatedAlertDismissal:
                  DependabotDelegatedAlertDismissal? = null,
              @SerialName("code_scanning_default_setup")
              public val codeScanningDefaultSetup: CodeScanningDefaultSetup? = null,
              @SerialName("code_scanning_default_setup_options")
              public val codeScanningDefaultSetupOptions: CodeScanningDefaultSetupOptions? = null,
              @SerialName("code_scanning_options")
              public val codeScanningOptions: CodeScanningOptions? = null,
              @SerialName("code_scanning_delegated_alert_dismissal")
              public val codeScanningDelegatedAlertDismissal:
                  CodeScanningDelegatedAlertDismissal? = null,
              @SerialName("secret_protection")
              public val secretProtection: SecretProtection? = null,
              @SerialName("secret_scanning")
              public val secretScanning: SecretScanning? = null,
              @SerialName("secret_scanning_push_protection")
              public val secretScanningPushProtection: SecretScanningPushProtection? = null,
              @SerialName("secret_scanning_delegated_bypass")
              public val secretScanningDelegatedBypass: SecretScanningDelegatedBypass? = null,
              @SerialName("secret_scanning_delegated_bypass_options")
              public val secretScanningDelegatedBypassOptions:
                  SecretScanningDelegatedBypassOptions? = null,
              @SerialName("secret_scanning_validity_checks")
              public val secretScanningValidityChecks: SecretScanningValidityChecks? = null,
              @SerialName("secret_scanning_non_provider_patterns")
              public val secretScanningNonProviderPatterns:
                  SecretScanningNonProviderPatterns? = null,
              @SerialName("secret_scanning_generic_secrets")
              public val secretScanningGenericSecrets: SecretScanningGenericSecrets? = null,
              @SerialName("secret_scanning_delegated_alert_dismissal")
              public val secretScanningDelegatedAlertDismissal:
                  SecretScanningDelegatedAlertDismissal? = null,
              @SerialName("secret_scanning_extended_metadata")
              public val secretScanningExtendedMetadata: SecretScanningExtendedMetadata? = null,
              @SerialName("private_vulnerability_reporting")
              public val privateVulnerabilityReporting: PrivateVulnerabilityReporting? = null,
              public val enforcement: Enforcement? = null,
            )

            public sealed interface Response {
              public data class Ok(
                public val `value`: CodeSecurityConfiguration,
              ) : Response

              public data object NoContent : Response
            }
          }

          public class Attach internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val configurationId: Long,
          ) {
            public val post: Post = Post(client, org, configurationId)

            public class Post internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val configurationId: Long,
            ) {
              public suspend operator fun invoke(scope: Scope, selectedRepositoryIds: List<Long>? = null): JsonElement = client.post("/orgs/$org/code-security/configurations/$configurationId/attach") {
                contentType(ContentType.Application.Json)
                setBody(Body(scope = scope, selectedRepositoryIds = selectedRepositoryIds))
              }.body()

              @Serializable
              public enum class Scope(
                public val `value`: String,
              ) {
                @SerialName("all")
                All("all"),
                @SerialName("all_without_configurations")
                AllWithoutConfigurations("all_without_configurations"),
                @SerialName("public")
                Public("public"),
                @SerialName("private_or_internal")
                PrivateOrInternal("private_or_internal"),
                @SerialName("selected")
                Selected("selected"),
                ;
              }

              @Serializable
              internal data class Body(
                public val scope: Scope,
                @SerialName("selected_repository_ids")
                public val selectedRepositoryIds: List<Long>? = null,
              )
            }
          }

          public class Defaults internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val configurationId: Long,
          ) {
            public val put: Put = Put(client, org, configurationId)

            public class Put internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val configurationId: Long,
            ) {
              public suspend operator fun invoke(defaultForNewRepos: DefaultForNewRepos? = null): Response {
                val response = client.put("/orgs/$org/code-security/configurations/$configurationId/defaults") {
                  contentType(ContentType.Application.Json)
                  setBody(Body(defaultForNewRepos = defaultForNewRepos))
                }
                return when (response.status.value) {
                  200 -> response.body<Response.Ok>()
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              @Serializable
              public enum class DefaultForNewRepos(
                public val `value`: String,
              ) {
                @SerialName("all")
                All("all"),
                @SerialName("none")
                None("none"),
                @SerialName("private_and_internal")
                PrivateAndInternal("private_and_internal"),
                @SerialName("public")
                Public("public"),
                ;
              }

              @JvmInline
              @Serializable
              internal value class Body(
                @SerialName("default_for_new_repos")
                public val defaultForNewRepos: DefaultForNewRepos? = null,
              )

              public sealed interface Response {
                @Serializable
                public data class Ok(
                  @SerialName("default_for_new_repos")
                  public val defaultForNewRepos: DefaultForNewRepos? = null,
                  public val configuration: CodeSecurityConfiguration? = null,
                ) : Response {
                  @Serializable
                  public enum class DefaultForNewRepos(
                    public val `value`: String,
                  ) {
                    @SerialName("all")
                    All("all"),
                    @SerialName("none")
                    None("none"),
                    @SerialName("private_and_internal")
                    PrivateAndInternal("private_and_internal"),
                    @SerialName("public")
                    Public("public"),
                    ;
                  }
                }

                public data class Forbidden(
                  public val `value`: BasicError,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response
              }
            }
          }

          public class Repositories internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val configurationId: Long,
          ) {
            public val `get`: Get = Get(client, org, configurationId)

            public class Get internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val configurationId: Long,
            ) {
              public suspend operator fun invoke(
                perPage: Long? = 30L,
                before: String? = null,
                after: String? = null,
                status: String? = "all",
              ): Response {
                val response = client.get("/orgs/$org/code-security/configurations/$configurationId/repositories") {
                  perPage?.let { parameter("per_page", it) }
                  before?.let { parameter("before", it) }
                  after?.let { parameter("after", it) }
                  status?.let { parameter("status", it) }
                }
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: List<CodeSecurityConfigurationRepositories>,
                ) : Response

                public data class Forbidden(
                  public val `value`: BasicError,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response
              }
            }
          }
        }
      }
    }

    public class Codespaces internal constructor(
      private val client: HttpClient,
      private val org: String,
    ) {
      public val `get`: Get = Get(client, org)

      public val access: Access = Access(client, org)

      public val secrets: Secrets = Secrets(client, org)

      public class Get internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
          val response = client.get("/orgs/$org/codespaces") {
            perPage?.let { parameter("per_page", it) }
            page?.let { parameter("page", it) }
          }
          return when (response.status.value) {
            200 -> response.body<Response.Ok>()
            304 -> Response.NotModified
            401 -> Response.Unauthorized(response.body())
            403 -> Response.Forbidden(response.body())
            404 -> Response.NotFound(response.body())
            500 -> Response.InternalServerError(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        public sealed interface Response {
          @Serializable
          public data class Ok(
            @SerialName("total_count")
            public val totalCount: Long,
            public val codespaces: List<Codespace>,
          ) : Response

          public data object NotModified : Response

          public data class Unauthorized(
            public val `value`: BasicError,
          ) : Response

          public data class Forbidden(
            public val `value`: BasicError,
          ) : Response

          public data class NotFound(
            public val `value`: BasicError,
          ) : Response

          public data class InternalServerError(
            public val `value`: BasicError,
          ) : Response
        }
      }

      public class Access internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        @Deprecated("Deprecated by the API provider")
        public val put: Put = Put(client, org)

        public val selectedUsers: SelectedUsers = SelectedUsers(client, org)

        @Deprecated("Deprecated by the API provider")
        public class Put internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          @Deprecated("Deprecated by the API provider")
          public suspend operator fun invoke(visibility: Visibility, selectedUsernames: List<String>? = null): Response {
            val response = client.put("/orgs/$org/codespaces/access") {
              contentType(ContentType.Application.Json)
              setBody(Body(visibility = visibility, selectedUsernames = selectedUsernames))
            }
            return when (response.status.value) {
              204 -> Response.NoContent
              304 -> Response.NotModified
              400 -> Response.BadRequest
              404 -> Response.NotFound(response.body())
              422 -> Response.UnprocessableEntity(response.body())
              500 -> Response.InternalServerError(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          @Serializable
          public enum class Visibility(
            public val `value`: String,
          ) {
            @SerialName("disabled")
            Disabled("disabled"),
            @SerialName("selected_members")
            SelectedMembers("selected_members"),
            @SerialName("all_members")
            AllMembers("all_members"),
            @SerialName("all_members_and_outside_collaborators")
            AllMembersAndOutsideCollaborators("all_members_and_outside_collaborators"),
            ;
          }

          @Serializable
          internal data class Body(
            public val visibility: Visibility,
            @SerialName("selected_usernames")
            public val selectedUsernames: List<String>? = null,
          )

          public sealed interface Response {
            public data object NoContent : Response

            public data object NotModified : Response

            public data object BadRequest : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response

            public data class UnprocessableEntity(
              public val `value`: ValidationError,
            ) : Response

            public data class InternalServerError(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class SelectedUsers internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          @Deprecated("Deprecated by the API provider")
          public val delete: Delete = Delete(client, org)

          @Deprecated("Deprecated by the API provider")
          public val post: Post = Post(client, org)

          @Deprecated("Deprecated by the API provider")
          public class Delete internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            @Deprecated("Deprecated by the API provider")
            public suspend operator fun invoke(selectedUsernames: List<String>): Response {
              val response = client.delete("/orgs/$org/codespaces/access/selected_users") {
                contentType(ContentType.Application.Json)
                setBody(Body(selectedUsernames = selectedUsernames))
              }
              return when (response.status.value) {
                204 -> Response.NoContent
                304 -> Response.NotModified
                400 -> Response.BadRequest
                404 -> Response.NotFound(response.body())
                422 -> Response.UnprocessableEntity(response.body())
                500 -> Response.InternalServerError(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            @JvmInline
            @Serializable
            internal value class Body(
              @SerialName("selected_usernames")
              public val selectedUsernames: List<String>,
            )

            public sealed interface Response {
              public data object NoContent : Response

              public data object NotModified : Response

              public data object BadRequest : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response

              public data class UnprocessableEntity(
                public val `value`: ValidationError,
              ) : Response

              public data class InternalServerError(
                public val `value`: BasicError,
              ) : Response
            }
          }

          @Deprecated("Deprecated by the API provider")
          public class Post internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            @Deprecated("Deprecated by the API provider")
            public suspend operator fun invoke(selectedUsernames: List<String>): Response {
              val response = client.post("/orgs/$org/codespaces/access/selected_users") {
                contentType(ContentType.Application.Json)
                setBody(Body(selectedUsernames = selectedUsernames))
              }
              return when (response.status.value) {
                204 -> Response.NoContent
                304 -> Response.NotModified
                400 -> Response.BadRequest
                404 -> Response.NotFound(response.body())
                422 -> Response.UnprocessableEntity(response.body())
                500 -> Response.InternalServerError(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            @JvmInline
            @Serializable
            internal value class Body(
              @SerialName("selected_usernames")
              public val selectedUsernames: List<String>,
            )

            public sealed interface Response {
              public data object NoContent : Response

              public data object NotModified : Response

              public data object BadRequest : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response

              public data class UnprocessableEntity(
                public val `value`: ValidationError,
              ) : Response

              public data class InternalServerError(
                public val `value`: BasicError,
              ) : Response
            }
          }
        }
      }

      public class Secrets internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public val `get`: Get = Get(client, org)

        public val publicKey: PublicKey = PublicKey(client, org)

        public fun secretName(secretName: String): SecretNamePath = SecretNamePath(client, org, secretName)

        public class Get internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response = client.get("/orgs/$org/codespaces/secrets") {
            perPage?.let { parameter("per_page", it) }
            page?.let { parameter("page", it) }
          }.body()

          @Serializable
          public data class Response(
            @SerialName("total_count")
            public val totalCount: Long,
            public val secrets: List<CodespacesOrgSecret>,
          )
        }

        public class PublicKey internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public val `get`: Get = Get(client, org)

          public class Get internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public suspend operator fun invoke(): CodespacesPublicKey = client.get("/orgs/$org/codespaces/secrets/public-key").body()
          }
        }

        public class SecretNamePath internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val secretName: String,
        ) {
          public val delete: Delete = Delete(client, org, secretName)

          public val `get`: Get = Get(client, org, secretName)

          public val put: Put = Put(client, org, secretName)

          public val repositories: Repositories = Repositories(client, org, secretName)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val secretName: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.delete("/orgs/$org/codespaces/secrets/$secretName")
              return when (response.status.value) {
                204 -> Response.NoContent
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data object NoContent : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response
            }
          }

          public class Get internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val secretName: String,
          ) {
            public suspend operator fun invoke(): CodespacesOrgSecret = client.get("/orgs/$org/codespaces/secrets/$secretName").body()
          }

          public class Put internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val secretName: String,
          ) {
            public suspend operator fun invoke(
              encryptedValue: String? = null,
              keyId: String? = null,
              visibility: Visibility,
              selectedRepositoryIds: List<Long>? = null,
            ): Response {
              val response = client.put("/orgs/$org/codespaces/secrets/$secretName") {
                contentType(ContentType.Application.Json)
                setBody(Body(encryptedValue = encryptedValue, keyId = keyId, visibility = visibility, selectedRepositoryIds = selectedRepositoryIds))
              }
              return when (response.status.value) {
                201 -> Response.Created(response.body())
                204 -> Response.NoContent
                404 -> Response.NotFound(response.body())
                422 -> Response.UnprocessableEntity(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            @Serializable
            public enum class Visibility(
              public val `value`: String,
            ) {
              @SerialName("all")
              All("all"),
              @SerialName("private")
              Private("private"),
              @SerialName("selected")
              Selected("selected"),
              ;
            }

            @Serializable
            internal data class Body(
              @SerialName("encrypted_value")
              public val encryptedValue: String? = null,
              @SerialName("key_id")
              public val keyId: String? = null,
              public val visibility: Visibility,
              @SerialName("selected_repository_ids")
              public val selectedRepositoryIds: List<Long>? = null,
            )

            public sealed interface Response {
              public data class Created(
                public val `value`: EmptyObject,
              ) : Response

              public data object NoContent : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response

              public data class UnprocessableEntity(
                public val `value`: ValidationError,
              ) : Response
            }
          }

          public class Repositories internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val secretName: String,
          ) {
            public val `get`: Get = Get(client, org, secretName)

            public val put: Put = Put(client, org, secretName)

            public fun repositoryId(repositoryId: Long): RepositoryIdPath = RepositoryIdPath(client, org, secretName, repositoryId)

            public class Get internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val secretName: String,
            ) {
              public suspend operator fun invoke(page: Long? = 1L, perPage: Long? = 30L): Response {
                val response = client.get("/orgs/$org/codespaces/secrets/$secretName/repositories") {
                  page?.let { parameter("page", it) }
                  perPage?.let { parameter("per_page", it) }
                }
                return when (response.status.value) {
                  200 -> response.body<Response.Ok>()
                  404 -> Response.NotFound(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                @Serializable
                public data class Ok(
                  @SerialName("total_count")
                  public val totalCount: Long,
                  public val repositories: List<MinimalRepository>,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response
              }
            }

            public class Put internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val secretName: String,
            ) {
              public suspend operator fun invoke(selectedRepositoryIds: List<Long>): Response {
                val response = client.put("/orgs/$org/codespaces/secrets/$secretName/repositories") {
                  contentType(ContentType.Application.Json)
                  setBody(Body(selectedRepositoryIds = selectedRepositoryIds))
                }
                return when (response.status.value) {
                  204 -> Response.NoContent
                  404 -> Response.NotFound(response.body())
                  409 -> Response.Conflict
                  else -> throw ResponseException(response, "")
                }
              }

              @JvmInline
              @Serializable
              internal value class Body(
                @SerialName("selected_repository_ids")
                public val selectedRepositoryIds: List<Long>,
              )

              public sealed interface Response {
                public data object NoContent : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response

                public data object Conflict : Response
              }
            }

            public class RepositoryIdPath internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val secretName: String,
              private val repositoryId: Long,
            ) {
              public val delete: Delete = Delete(client, org, secretName, repositoryId)

              public val put: Put = Put(client, org, secretName, repositoryId)

              public class Delete internal constructor(
                private val client: HttpClient,
                private val org: String,
                private val secretName: String,
                private val repositoryId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.delete("/orgs/$org/codespaces/secrets/$secretName/repositories/$repositoryId")
                  return when (response.status.value) {
                    204 -> Response.NoContent
                    404 -> Response.NotFound(response.body())
                    409 -> Response.Conflict
                    422 -> Response.UnprocessableEntity(response.body())
                    else -> throw ResponseException(response, "")
                  }
                }

                public sealed interface Response {
                  public data object NoContent : Response

                  public data class NotFound(
                    public val `value`: BasicError,
                  ) : Response

                  public data object Conflict : Response

                  public data class UnprocessableEntity(
                    public val `value`: ValidationError,
                  ) : Response
                }
              }

              public class Put internal constructor(
                private val client: HttpClient,
                private val org: String,
                private val secretName: String,
                private val repositoryId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.put("/orgs/$org/codespaces/secrets/$secretName/repositories/$repositoryId")
                  return when (response.status.value) {
                    204 -> Response.NoContent
                    404 -> Response.NotFound(response.body())
                    409 -> Response.Conflict
                    422 -> Response.UnprocessableEntity(response.body())
                    else -> throw ResponseException(response, "")
                  }
                }

                public sealed interface Response {
                  public data object NoContent : Response

                  public data class NotFound(
                    public val `value`: BasicError,
                  ) : Response

                  public data object Conflict : Response

                  public data class UnprocessableEntity(
                    public val `value`: ValidationError,
                  ) : Response
                }
              }
            }
          }
        }
      }
    }

    public class Copilot internal constructor(
      private val client: HttpClient,
      private val org: String,
    ) {
      public val billing: Billing = Billing(client, org)

      public val contentExclusion: ContentExclusion = ContentExclusion(client, org)

      public val metrics: Metrics = Metrics(client, org)

      public class Billing internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public val `get`: Get = Get(client, org)

        public val seats: Seats = Seats(client, org)

        public val selectedTeams: SelectedTeams = SelectedTeams(client, org)

        public val selectedUsers: SelectedUsers = SelectedUsers(client, org)

        public class Get internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.get("/orgs/$org/copilot/billing")
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              401 -> Response.Unauthorized(response.body())
              403 -> Response.Forbidden(response.body())
              404 -> Response.NotFound(response.body())
              422 -> Response.UnprocessableEntity
              500 -> Response.InternalServerError(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: CopilotOrganizationDetails,
            ) : Response

            public data class Unauthorized(
              public val `value`: BasicError,
            ) : Response

            public data class Forbidden(
              public val `value`: BasicError,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response

            public data object UnprocessableEntity : Response

            public data class InternalServerError(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class Seats internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public val `get`: Get = Get(client, org)

          public class Get internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public suspend operator fun invoke(page: Long? = 1L, perPage: Long? = 50L): Response {
              val response = client.get("/orgs/$org/copilot/billing/seats") {
                page?.let { parameter("page", it) }
                perPage?.let { parameter("per_page", it) }
              }
              return when (response.status.value) {
                200 -> response.body<Response.Ok>()
                401 -> Response.Unauthorized(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                500 -> Response.InternalServerError(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              @Serializable
              public data class Ok(
                @SerialName("total_seats")
                public val totalSeats: Long? = null,
                public val seats: List<CopilotSeatDetails>? = null,
              ) : Response

              public data class Unauthorized(
                public val `value`: BasicError,
              ) : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response

              public data class InternalServerError(
                public val `value`: BasicError,
              ) : Response
            }
          }
        }

        public class SelectedTeams internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public val delete: Delete = Delete(client, org)

          public val post: Post = Post(client, org)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public suspend operator fun invoke(selectedTeams: List<String>): Response {
              val response = client.delete("/orgs/$org/copilot/billing/selected_teams") {
                contentType(ContentType.Application.Json)
                setBody(Body(selectedTeams = selectedTeams))
              }
              return when (response.status.value) {
                200 -> response.body<Response.Ok>()
                401 -> Response.Unauthorized(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                422 -> Response.UnprocessableEntity
                500 -> Response.InternalServerError(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            @JvmInline
            @Serializable
            internal value class Body(
              @SerialName("selected_teams")
              public val selectedTeams: List<String>,
            )

            public sealed interface Response {
              /**
               * The total number of seats set to "pending cancellation" for members of the specified team(s).
               */
              @JvmInline
              @Serializable
              public value class Ok(
                @SerialName("seats_cancelled")
                public val seatsCancelled: Long,
              ) : Response

              public data class Unauthorized(
                public val `value`: BasicError,
              ) : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response

              public data object UnprocessableEntity : Response

              public data class InternalServerError(
                public val `value`: BasicError,
              ) : Response
            }
          }

          public class Post internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public suspend operator fun invoke(selectedTeams: List<String>): Response {
              val response = client.post("/orgs/$org/copilot/billing/selected_teams") {
                contentType(ContentType.Application.Json)
                setBody(Body(selectedTeams = selectedTeams))
              }
              return when (response.status.value) {
                201 -> response.body<Response.Created>()
                401 -> Response.Unauthorized(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                422 -> Response.UnprocessableEntity
                500 -> Response.InternalServerError(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            @JvmInline
            @Serializable
            internal value class Body(
              @SerialName("selected_teams")
              public val selectedTeams: List<String>,
            )

            public sealed interface Response {
              /**
               * The total number of seats created for members of the specified team(s).
               */
              @JvmInline
              @Serializable
              public value class Created(
                @SerialName("seats_created")
                public val seatsCreated: Long,
              ) : Response

              public data class Unauthorized(
                public val `value`: BasicError,
              ) : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response

              public data object UnprocessableEntity : Response

              public data class InternalServerError(
                public val `value`: BasicError,
              ) : Response
            }
          }
        }

        public class SelectedUsers internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public val delete: Delete = Delete(client, org)

          public val post: Post = Post(client, org)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public suspend operator fun invoke(selectedUsernames: List<String>): Response {
              val response = client.delete("/orgs/$org/copilot/billing/selected_users") {
                contentType(ContentType.Application.Json)
                setBody(Body(selectedUsernames = selectedUsernames))
              }
              return when (response.status.value) {
                200 -> response.body<Response.Ok>()
                401 -> Response.Unauthorized(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                422 -> Response.UnprocessableEntity
                500 -> Response.InternalServerError(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            @JvmInline
            @Serializable
            internal value class Body(
              @SerialName("selected_usernames")
              public val selectedUsernames: List<String>,
            )

            public sealed interface Response {
              /**
               * The total number of seats set to "pending cancellation" for the specified users.
               */
              @JvmInline
              @Serializable
              public value class Ok(
                @SerialName("seats_cancelled")
                public val seatsCancelled: Long,
              ) : Response

              public data class Unauthorized(
                public val `value`: BasicError,
              ) : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response

              public data object UnprocessableEntity : Response

              public data class InternalServerError(
                public val `value`: BasicError,
              ) : Response
            }
          }

          public class Post internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public suspend operator fun invoke(selectedUsernames: List<String>): Response {
              val response = client.post("/orgs/$org/copilot/billing/selected_users") {
                contentType(ContentType.Application.Json)
                setBody(Body(selectedUsernames = selectedUsernames))
              }
              return when (response.status.value) {
                201 -> response.body<Response.Created>()
                401 -> Response.Unauthorized(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                422 -> Response.UnprocessableEntity
                500 -> Response.InternalServerError(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            @JvmInline
            @Serializable
            internal value class Body(
              @SerialName("selected_usernames")
              public val selectedUsernames: List<String>,
            )

            public sealed interface Response {
              /**
               * The total number of seats created for the specified user(s).
               */
              @JvmInline
              @Serializable
              public value class Created(
                @SerialName("seats_created")
                public val seatsCreated: Long,
              ) : Response

              public data class Unauthorized(
                public val `value`: BasicError,
              ) : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response

              public data object UnprocessableEntity : Response

              public data class InternalServerError(
                public val `value`: BasicError,
              ) : Response
            }
          }
        }
      }

      public class ContentExclusion internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public val `get`: Get = Get(client, org)

        public val put: Put = Put(client, org)

        public class Get internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.get("/orgs/$org/copilot/content_exclusion")
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              401 -> Response.Unauthorized(response.body())
              403 -> Response.Forbidden(response.body())
              404 -> Response.NotFound(response.body())
              500 -> Response.InternalServerError(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: CopilotOrganizationContentExclusionDetails,
            ) : Response

            public data class Unauthorized(
              public val `value`: BasicError,
            ) : Response

            public data class Forbidden(
              public val `value`: BasicError,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response

            public data class InternalServerError(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class Put internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public suspend operator fun invoke(body: Body): Response {
            val response = client.put("/orgs/$org/copilot/content_exclusion") {
              contentType(ContentType.Application.Json)
              setBody(body)
            }
            return when (response.status.value) {
              200 -> response.body<Response.Ok>()
              401 -> Response.Unauthorized(response.body())
              403 -> Response.Forbidden(response.body())
              404 -> Response.NotFound(response.body())
              413 -> Response.PayloadTooLarge(response.body())
              422 -> Response.UnprocessableEntity(response.body())
              500 -> Response.InternalServerError(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          @OptIn(ExperimentalSerializationApi::class)
          @KeepGeneratedSerializer
          @Serializable(with = Body.Serializer::class)
          public data class Body(
            public val additional: Map<String, List<AdditionalProperties>>? = null,
          ) {
            @Serializable(with = AdditionalProperties.Serializer::class)
            public sealed interface AdditionalProperties {
              @Serializable
              @JvmInline
              public value class CaseString(
                public val `value`: String,
              ) : AdditionalProperties

              @JvmInline
              @Serializable
              public value class IfAnyMatchStrings(
                public val ifAnyMatch: List<String>,
              ) : AdditionalProperties

              @JvmInline
              @Serializable
              public value class IfNoneMatchStrings(
                public val ifNoneMatch: List<String>,
              ) : AdditionalProperties

              public object Serializer : KSerializer<AdditionalProperties> {
                @OptIn(
                  InternalSerializationApi::class,
                  ExperimentalSerializationApi::class,
                )
                override val descriptor: SerialDescriptor =
                    buildSerialDescriptor("io.github.api.Orgs.OrgPath.Copilot.ContentExclusion.Put.Body.AdditionalProperties", PolymorphicKind.SEALED) {
                  element("CaseString", String.serializer().descriptor)
                  element("IfAnyMatchStrings", IfAnyMatchStrings.serializer().descriptor)
                  element("IfNoneMatchStrings", IfNoneMatchStrings.serializer().descriptor)
                }

                override fun deserialize(decoder: Decoder): AdditionalProperties {
                  val value = decoder.decodeSerializableValue(JsonElement.serializer())
                  val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                  return json.attemptDeserialize(
                    value,
                    IfAnyMatchStrings::class to { decodeFromJsonElement(IfAnyMatchStrings.serializer(), it) },
                    IfNoneMatchStrings::class to { decodeFromJsonElement(IfNoneMatchStrings.serializer(), it) },
                    CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                  )
                }

                override fun serialize(encoder: Encoder, `value`: AdditionalProperties) {
                  when(value) {
                    is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                    is IfAnyMatchStrings -> encoder.encodeSerializableValue(IfAnyMatchStrings.serializer(), value)
                    is IfNoneMatchStrings -> encoder.encodeSerializableValue(IfNoneMatchStrings.serializer(), value)
                  }
                }
              }
            }

            public object Serializer : KSerializer<Body> {
              override val descriptor: SerialDescriptor = generatedSerializer().descriptor

              override fun serialize(encoder: Encoder, `value`: Body) {
                val json = (encoder as JsonEncoder).json
                val known = json.encodeToJsonElement(generatedSerializer(), value.copy(additional = null)) as JsonObject
                val content = mutableMapOf<String, JsonElement>()
                known.forEach { (key, jsonElement) ->
                  if (key != "additional") {
                    content[key] = jsonElement
                  }
                }
                value.additional?.forEach { (key, additionalValue) ->
                  content[key] = json.encodeToJsonElement(ListSerializer(AdditionalProperties.serializer()), additionalValue)
                }
                encoder.encodeSerializableValue(JsonObject.serializer(), JsonObject(content))
              }

              override fun deserialize(decoder: Decoder): Body {
                val json = (decoder as JsonDecoder).json
                val element = decoder.decodeSerializableValue(JsonObject.serializer())
                val knownNames = emptySet<String>()
                val known = json.decodeFromJsonElement(generatedSerializer(), JsonObject(element.filterKeys { it in knownNames }))
                val additional = (element - knownNames)
                  .mapValues { (_, jsonElement) -> json.decodeFromJsonElement(ListSerializer(AdditionalProperties.serializer()), jsonElement) }
                  .ifEmpty { null }
                return known.copy(additional = additional)
              }
            }
          }

          public sealed interface Response {
            @JvmInline
            @Serializable
            public value class Ok(
              public val message: String? = null,
            ) : Response

            public data class Unauthorized(
              public val `value`: BasicError,
            ) : Response

            public data class Forbidden(
              public val `value`: BasicError,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response

            public data class PayloadTooLarge(
              public val `value`: BasicError,
            ) : Response

            public data class UnprocessableEntity(
              public val `value`: ValidationErrorSimple,
            ) : Response

            public data class InternalServerError(
              public val `value`: BasicError,
            ) : Response
          }
        }
      }

      public class Metrics internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public val `get`: Get = Get(client, org)

        public class Get internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public suspend operator fun invoke(
            since: String? = null,
            until: String? = null,
            page: Long? = 1L,
            perPage: Long? = 100L,
          ): Response {
            val response = client.get("/orgs/$org/copilot/metrics") {
              since?.let { parameter("since", it) }
              until?.let { parameter("until", it) }
              page?.let { parameter("page", it) }
              perPage?.let { parameter("per_page", it) }
            }
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              403 -> Response.Forbidden(response.body())
              404 -> Response.NotFound(response.body())
              422 -> Response.UnprocessableEntity(response.body())
              500 -> Response.InternalServerError(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: List<CopilotUsageMetricsDay>,
            ) : Response

            public data class Forbidden(
              public val `value`: BasicError,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response

            public data class UnprocessableEntity(
              public val `value`: BasicError,
            ) : Response

            public data class InternalServerError(
              public val `value`: BasicError,
            ) : Response
          }
        }
      }
    }

    public class Dependabot internal constructor(
      private val client: HttpClient,
      private val org: String,
    ) {
      public val alerts: Alerts = Alerts(client, org)

      public val secrets: Secrets = Secrets(client, org)

      public class Alerts internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public val `get`: Get = Get(client, org)

        public class Get internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public suspend operator fun invoke(
            state: String? = null,
            severity: String? = null,
            ecosystem: String? = null,
            `package`: String? = null,
            epssPercentage: String? = null,
            artifactRegistryUrl: String? = null,
            artifactRegistry: String? = null,
            has: Has? = null,
            assignee: String? = null,
            runtimeRisk: String? = null,
            scope: Scope? = null,
            sort: Sort? = Sort.Created,
            direction: Direction? = Direction.Desc,
            before: String? = null,
            after: String? = null,
            perPage: Long? = 30L,
          ): Response {
            val response = client.get("/orgs/$org/dependabot/alerts") {
              state?.let { parameter("state", it) }
              severity?.let { parameter("severity", it) }
              ecosystem?.let { parameter("ecosystem", it) }
              `package`?.let { parameter("package", it) }
              epssPercentage?.let { parameter("epss_percentage", it) }
              artifactRegistryUrl?.let { parameter("artifact_registry_url", it) }
              artifactRegistry?.let { parameter("artifact_registry", it) }
              has?.let { parameter("has", it) }
              assignee?.let { parameter("assignee", it) }
              runtimeRisk?.let { parameter("runtime_risk", it) }
              scope?.let { parameter("scope", it.value) }
              sort?.let { parameter("sort", it.value) }
              direction?.let { parameter("direction", it.value) }
              before?.let { parameter("before", it) }
              after?.let { parameter("after", it) }
              perPage?.let { parameter("per_page", it) }
            }
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              304 -> Response.NotModified
              400 -> Response.BadRequest(response.body())
              403 -> Response.Forbidden(response.body())
              404 -> Response.NotFound(response.body())
              422 -> Response.UnprocessableEntity(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          @Serializable(with = Has.Serializer::class)
          public sealed interface Has {
            @Serializable
            @JvmInline
            public value class CaseString(
              public val `value`: String,
            ) : Has

            @Serializable
            @JvmInline
            public value class CasePatchOrDeploymentList(
              public val `value`: List<PatchOrDeployment>,
            ) : Has

            @Serializable
            public enum class PatchOrDeployment(
              public val `value`: String,
            ) {
              @SerialName("patch")
              Patch("patch"),
              @SerialName("deployment")
              Deployment("deployment"),
              ;
            }

            public object Serializer : KSerializer<Has> {
              @OptIn(
                InternalSerializationApi::class,
                ExperimentalSerializationApi::class,
              )
              override val descriptor: SerialDescriptor =
                  buildSerialDescriptor("io.github.api.Orgs.OrgPath.Dependabot.Alerts.Get.Has", PolymorphicKind.SEALED) {
                element("CaseString", String.serializer().descriptor)
                element("CasePatchOrDeploymentList", ListSerializer(PatchOrDeployment.serializer()).descriptor)
              }

              override fun deserialize(decoder: Decoder): Has {
                val value = decoder.decodeSerializableValue(JsonElement.serializer())
                val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                return json.attemptDeserialize(
                  value,
                  CasePatchOrDeploymentList::class to { CasePatchOrDeploymentList(decodeFromJsonElement(ListSerializer(PatchOrDeployment.serializer()), it)) },
                  CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                )
              }

              override fun serialize(encoder: Encoder, `value`: Has) {
                when(value) {
                  is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                  is CasePatchOrDeploymentList -> encoder.encodeSerializableValue(ListSerializer(PatchOrDeployment.serializer()), value.value)
                }
              }
            }
          }

          @Serializable
          public enum class Scope(
            public val `value`: String,
          ) {
            @SerialName("development")
            Development("development"),
            @SerialName("runtime")
            Runtime("runtime"),
            ;
          }

          @Serializable
          public enum class Sort(
            public val `value`: String,
          ) {
            @SerialName("created")
            Created("created"),
            @SerialName("updated")
            Updated("updated"),
            @SerialName("epss_percentage")
            EpssPercentage("epss_percentage"),
            ;
          }

          @Serializable
          public enum class Direction(
            public val `value`: String,
          ) {
            @SerialName("asc")
            Asc("asc"),
            @SerialName("desc")
            Desc("desc"),
            ;
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: List<DependabotAlertWithRepository>,
            ) : Response

            public data object NotModified : Response

            public data class BadRequest(
              public val `value`: BasicError,
            ) : Response

            public data class Forbidden(
              public val `value`: BasicError,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response

            public data class UnprocessableEntity(
              public val `value`: ValidationErrorSimple,
            ) : Response
          }
        }
      }

      public class Secrets internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public val `get`: Get = Get(client, org)

        public val publicKey: PublicKey = PublicKey(client, org)

        public fun secretName(secretName: String): SecretNamePath = SecretNamePath(client, org, secretName)

        public class Get internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response = client.get("/orgs/$org/dependabot/secrets") {
            perPage?.let { parameter("per_page", it) }
            page?.let { parameter("page", it) }
          }.body()

          @Serializable
          public data class Response(
            @SerialName("total_count")
            public val totalCount: Long,
            public val secrets: List<OrganizationDependabotSecret>,
          )
        }

        public class PublicKey internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public val `get`: Get = Get(client, org)

          public class Get internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public suspend operator fun invoke(): DependabotPublicKey = client.get("/orgs/$org/dependabot/secrets/public-key").body()
          }
        }

        public class SecretNamePath internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val secretName: String,
        ) {
          public val delete: Delete = Delete(client, org, secretName)

          public val `get`: Get = Get(client, org, secretName)

          public val put: Put = Put(client, org, secretName)

          public val repositories: Repositories = Repositories(client, org, secretName)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val secretName: String,
          ) {
            public suspend operator fun invoke() {
              client.delete("/orgs/$org/dependabot/secrets/$secretName")
            }
          }

          public class Get internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val secretName: String,
          ) {
            public suspend operator fun invoke(): OrganizationDependabotSecret = client.get("/orgs/$org/dependabot/secrets/$secretName").body()
          }

          public class Put internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val secretName: String,
          ) {
            public suspend operator fun invoke(
              encryptedValue: String? = null,
              keyId: String? = null,
              visibility: Visibility,
              selectedRepositoryIds: List<SelectedRepositoryIds>? = null,
            ): Response {
              val response = client.put("/orgs/$org/dependabot/secrets/$secretName") {
                contentType(ContentType.Application.Json)
                setBody(Body(encryptedValue = encryptedValue, keyId = keyId, visibility = visibility, selectedRepositoryIds = selectedRepositoryIds))
              }
              return when (response.status.value) {
                201 -> Response.Created(response.body())
                204 -> Response.NoContent
                else -> throw ResponseException(response, "")
              }
            }

            @Serializable
            public enum class Visibility(
              public val `value`: String,
            ) {
              @SerialName("all")
              All("all"),
              @SerialName("private")
              Private("private"),
              @SerialName("selected")
              Selected("selected"),
              ;
            }

            @Serializable(with = SelectedRepositoryIds.Serializer::class)
            public sealed interface SelectedRepositoryIds {
              @Serializable
              @JvmInline
              public value class CaseLong(
                public val `value`: Long,
              ) : SelectedRepositoryIds

              @Serializable
              @JvmInline
              public value class CaseString(
                public val `value`: String,
              ) : SelectedRepositoryIds

              public object Serializer : KSerializer<SelectedRepositoryIds> {
                @OptIn(
                  InternalSerializationApi::class,
                  ExperimentalSerializationApi::class,
                )
                override val descriptor: SerialDescriptor =
                    buildSerialDescriptor("io.github.api.Orgs.OrgPath.Dependabot.Secrets.SecretNamePath.Put.SelectedRepositoryIds", PolymorphicKind.SEALED) {
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

                override fun serialize(encoder: Encoder, `value`: SelectedRepositoryIds) {
                  when(value) {
                    is CaseLong -> encoder.encodeSerializableValue(Long.serializer(), value.value)
                    is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                  }
                }
              }
            }

            @Serializable
            internal data class Body(
              @SerialName("encrypted_value")
              public val encryptedValue: String? = null,
              @SerialName("key_id")
              public val keyId: String? = null,
              public val visibility: Visibility,
              @SerialName("selected_repository_ids")
              public val selectedRepositoryIds: List<SelectedRepositoryIds>? = null,
            )

            public sealed interface Response {
              public data class Created(
                public val `value`: EmptyObject,
              ) : Response

              public data object NoContent : Response
            }
          }

          public class Repositories internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val secretName: String,
          ) {
            public val `get`: Get = Get(client, org, secretName)

            public val put: Put = Put(client, org, secretName)

            public fun repositoryId(repositoryId: Long): RepositoryIdPath = RepositoryIdPath(client, org, secretName, repositoryId)

            public class Get internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val secretName: String,
            ) {
              public suspend operator fun invoke(page: Long? = 1L, perPage: Long? = 30L): Response = client.get("/orgs/$org/dependabot/secrets/$secretName/repositories") {
                page?.let { parameter("page", it) }
                perPage?.let { parameter("per_page", it) }
              }.body()

              @Serializable
              public data class Response(
                @SerialName("total_count")
                public val totalCount: Long,
                public val repositories: List<MinimalRepository>,
              )
            }

            public class Put internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val secretName: String,
            ) {
              public suspend operator fun invoke(selectedRepositoryIds: List<Long>) {
                client.put("/orgs/$org/dependabot/secrets/$secretName/repositories") {
                  contentType(ContentType.Application.Json)
                  setBody(Body(selectedRepositoryIds = selectedRepositoryIds))
                }
              }

              @JvmInline
              @Serializable
              internal value class Body(
                @SerialName("selected_repository_ids")
                public val selectedRepositoryIds: List<Long>,
              )
            }

            public class RepositoryIdPath internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val secretName: String,
              private val repositoryId: Long,
            ) {
              public val delete: Delete = Delete(client, org, secretName, repositoryId)

              public val put: Put = Put(client, org, secretName, repositoryId)

              public class Delete internal constructor(
                private val client: HttpClient,
                private val org: String,
                private val secretName: String,
                private val repositoryId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.delete("/orgs/$org/dependabot/secrets/$secretName/repositories/$repositoryId")
                  return when (response.status.value) {
                    204 -> Response.NoContent
                    409 -> Response.Conflict
                    else -> throw ResponseException(response, "")
                  }
                }

                public sealed interface Response {
                  public data object NoContent : Response

                  public data object Conflict : Response
                }
              }

              public class Put internal constructor(
                private val client: HttpClient,
                private val org: String,
                private val secretName: String,
                private val repositoryId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.put("/orgs/$org/dependabot/secrets/$secretName/repositories/$repositoryId")
                  return when (response.status.value) {
                    204 -> Response.NoContent
                    409 -> Response.Conflict
                    else -> throw ResponseException(response, "")
                  }
                }

                public sealed interface Response {
                  public data object NoContent : Response

                  public data object Conflict : Response
                }
              }
            }
          }
        }
      }
    }

    public class Docker internal constructor(
      private val client: HttpClient,
      private val org: String,
    ) {
      public val conflicts: Conflicts = Conflicts(client, org)

      public class Conflicts internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public val `get`: Get = Get(client, org)

        public class Get internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.get("/orgs/$org/docker/conflicts")
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              401 -> Response.Unauthorized(response.body())
              403 -> Response.Forbidden(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: List<Package>,
            ) : Response

            public data class Unauthorized(
              public val `value`: BasicError,
            ) : Response

            public data class Forbidden(
              public val `value`: BasicError,
            ) : Response
          }
        }
      }
    }

    public class Events internal constructor(
      private val client: HttpClient,
      private val org: String,
    ) {
      public val `get`: Get = Get(client, org)

      public class Get internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<Event> = client.get("/orgs/$org/events") {
          perPage?.let { parameter("per_page", it) }
          page?.let { parameter("page", it) }
        }.body()
      }
    }

    public class FailedInvitations internal constructor(
      private val client: HttpClient,
      private val org: String,
    ) {
      public val `get`: Get = Get(client, org)

      public class Get internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
          val response = client.get("/orgs/$org/failed_invitations") {
            perPage?.let { parameter("per_page", it) }
            page?.let { parameter("page", it) }
          }
          return when (response.status.value) {
            200 -> Response.Ok(response.body())
            404 -> Response.NotFound(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        public sealed interface Response {
          public data class Ok(
            public val `value`: List<OrganizationInvitation>,
          ) : Response

          public data class NotFound(
            public val `value`: BasicError,
          ) : Response
        }
      }
    }

    public class Hooks internal constructor(
      private val client: HttpClient,
      private val org: String,
    ) {
      public val `get`: Get = Get(client, org)

      public val post: Post = Post(client, org)

      public fun hookId(hookId: Long): HookIdPath = HookIdPath(client, org, hookId)

      public class Get internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
          val response = client.get("/orgs/$org/hooks") {
            perPage?.let { parameter("per_page", it) }
            page?.let { parameter("page", it) }
          }
          return when (response.status.value) {
            200 -> Response.Ok(response.body())
            404 -> Response.NotFound(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        public sealed interface Response {
          public data class Ok(
            public val `value`: List<OrgHook>,
          ) : Response

          public data class NotFound(
            public val `value`: BasicError,
          ) : Response
        }
      }

      public class Post internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public suspend operator fun invoke(
          name: String,
          config: Config,
          events: List<String>? = null,
          active: Boolean? = null,
        ): Response {
          val response = client.post("/orgs/$org/hooks") {
            contentType(ContentType.Application.Json)
            setBody(Body(name = name, config = config, events = events, active = active))
          }
          return when (response.status.value) {
            201 -> Response.Created(response.body())
            404 -> Response.NotFound(response.body())
            422 -> Response.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        /**
         * Key/value pairs to provide settings for this webhook.
         */
        @Serializable
        public data class Config(
          public val url: WebhookConfigUrl,
          @SerialName("content_type")
          public val contentType: WebhookConfigContentType? = null,
          public val secret: WebhookConfigSecret? = null,
          @SerialName("insecure_ssl")
          public val insecureSsl: WebhookConfigInsecureSsl? = null,
          public val username: String? = null,
          public val password: String? = null,
        )

        @Serializable
        internal data class Body(
          public val name: String,
          public val config: Config,
          public val events: List<String>? = null,
          public val active: Boolean? = null,
        )

        public sealed interface Response {
          public data class Created(
            public val `value`: OrgHook,
          ) : Response

          public data class NotFound(
            public val `value`: BasicError,
          ) : Response

          public data class UnprocessableEntity(
            public val `value`: ValidationError,
          ) : Response
        }
      }

      public class HookIdPath internal constructor(
        private val client: HttpClient,
        private val org: String,
        private val hookId: Long,
      ) {
        public val delete: Delete = Delete(client, org, hookId)

        public val `get`: Get = Get(client, org, hookId)

        public val patch: Patch = Patch(client, org, hookId)

        public val config: Config = Config(client, org, hookId)

        public val deliveries: Deliveries = Deliveries(client, org, hookId)

        public val pings: Pings = Pings(client, org, hookId)

        public class Delete internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val hookId: Long,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.delete("/orgs/$org/hooks/$hookId")
            return when (response.status.value) {
              204 -> Response.NoContent
              404 -> Response.NotFound(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data object NoContent : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class Get internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val hookId: Long,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.get("/orgs/$org/hooks/$hookId")
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              404 -> Response.NotFound(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: OrgHook,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class Patch internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val hookId: Long,
        ) {
          public suspend operator fun invoke(
            config: Config? = null,
            events: List<String>? = null,
            active: Boolean? = null,
            name: String? = null,
          ): Response {
            val response = client.patch("/orgs/$org/hooks/$hookId") {
              if (config != null || events != null || active != null || name != null) {
                contentType(ContentType.Application.Json)
                setBody(Body(config = config, events = events, active = active, name = name))
              }
            }
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              404 -> Response.NotFound(response.body())
              422 -> Response.UnprocessableEntity(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          /**
           * Key/value pairs to provide settings for this webhook.
           */
          @Serializable
          public data class Config(
            public val url: WebhookConfigUrl,
            @SerialName("content_type")
            public val contentType: WebhookConfigContentType? = null,
            public val secret: WebhookConfigSecret? = null,
            @SerialName("insecure_ssl")
            public val insecureSsl: WebhookConfigInsecureSsl? = null,
          )

          @Serializable
          internal data class Body(
            public val config: Config? = null,
            public val events: List<String>? = null,
            public val active: Boolean? = null,
            public val name: String? = null,
          )

          public sealed interface Response {
            public data class Ok(
              public val `value`: OrgHook,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response

            public data class UnprocessableEntity(
              public val `value`: ValidationError,
            ) : Response
          }
        }

        public class Config internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val hookId: Long,
        ) {
          public val `get`: Get = Get(client, org, hookId)

          public val patch: Patch = Patch(client, org, hookId)

          public class Get internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val hookId: Long,
          ) {
            public suspend operator fun invoke(): WebhookConfig = client.get("/orgs/$org/hooks/$hookId/config").body()
          }

          public class Patch internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val hookId: Long,
          ) {
            public suspend operator fun invoke(
              url: WebhookConfigUrl? = null,
              contentType: WebhookConfigContentType? = null,
              secret: WebhookConfigSecret? = null,
              insecureSsl: WebhookConfigInsecureSsl? = null,
            ): WebhookConfig = client.patch("/orgs/$org/hooks/$hookId/config") {
              if (url != null || contentType != null || secret != null || insecureSsl != null) {
                contentType(ContentType.Application.Json)
                setBody(Body(url = url, contentType = contentType, secret = secret, insecureSsl = insecureSsl))
              }
            }.body()

            @Serializable
            internal data class Body(
              public val url: WebhookConfigUrl? = null,
              @SerialName("content_type")
              public val contentType: WebhookConfigContentType? = null,
              public val secret: WebhookConfigSecret? = null,
              @SerialName("insecure_ssl")
              public val insecureSsl: WebhookConfigInsecureSsl? = null,
            )
          }
        }

        public class Deliveries internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val hookId: Long,
        ) {
          public val `get`: Get = Get(client, org, hookId)

          public fun deliveryId(deliveryId: Long): DeliveryIdPath = DeliveryIdPath(client, org, hookId, deliveryId)

          public class Get internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val hookId: Long,
          ) {
            public suspend operator fun invoke(perPage: Long? = 30L, cursor: String? = null): Response {
              val response = client.get("/orgs/$org/hooks/$hookId/deliveries") {
                perPage?.let { parameter("per_page", it) }
                cursor?.let { parameter("cursor", it) }
              }
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                400 -> Response.BadRequest(response.body())
                422 -> Response.UnprocessableEntity(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: List<HookDeliveryItem>,
              ) : Response

              public data class BadRequest(
                public val `value`: BasicError,
              ) : Response

              public data class UnprocessableEntity(
                public val `value`: ValidationError,
              ) : Response
            }
          }

          public class DeliveryIdPath internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val hookId: Long,
            private val deliveryId: Long,
          ) {
            public val `get`: Get = Get(client, org, hookId, deliveryId)

            public val attempts: Attempts = Attempts(client, org, hookId, deliveryId)

            public class Get internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val hookId: Long,
              private val deliveryId: Long,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.get("/orgs/$org/hooks/$hookId/deliveries/$deliveryId")
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  400 -> Response.BadRequest(response.body())
                  422 -> Response.UnprocessableEntity(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: HookDelivery,
                ) : Response

                public data class BadRequest(
                  public val `value`: BasicError,
                ) : Response

                public data class UnprocessableEntity(
                  public val `value`: ValidationError,
                ) : Response
              }
            }

            public class Attempts internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val hookId: Long,
              private val deliveryId: Long,
            ) {
              public val post: Post = Post(client, org, hookId, deliveryId)

              public class Post internal constructor(
                private val client: HttpClient,
                private val org: String,
                private val hookId: Long,
                private val deliveryId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.post("/orgs/$org/hooks/$hookId/deliveries/$deliveryId/attempts")
                  return when (response.status.value) {
                    202 -> Response.Accepted(response.body())
                    400 -> Response.BadRequest(response.body())
                    422 -> Response.UnprocessableEntity(response.body())
                    else -> throw ResponseException(response, "")
                  }
                }

                public sealed interface Response {
                  public data class Accepted(
                    public val `value`: JsonElement,
                  ) : Response

                  public data class BadRequest(
                    public val `value`: BasicError,
                  ) : Response

                  public data class UnprocessableEntity(
                    public val `value`: ValidationError,
                  ) : Response
                }
              }
            }
          }
        }

        public class Pings internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val hookId: Long,
        ) {
          public val post: Post = Post(client, org, hookId)

          public class Post internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val hookId: Long,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.post("/orgs/$org/hooks/$hookId/pings")
              return when (response.status.value) {
                204 -> Response.NoContent
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data object NoContent : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response
            }
          }
        }
      }
    }

    public class Insights internal constructor(
      private val client: HttpClient,
      private val org: String,
    ) {
      public val api: Api = Api(client, org)

      public class Api internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public val routeStats: RouteStats = RouteStats(client, org)

        public val subjectStats: SubjectStats = SubjectStats(client, org)

        public val summaryStats: SummaryStats = SummaryStats(client, org)

        public val timeStats: TimeStats = TimeStats(client, org)

        public val userStats: UserStats = UserStats(client, org)

        public class RouteStats internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public val installation: Installation = Installation(client, org)

          public val classicPat: ClassicPat = ClassicPat(client, org)

          public val fineGrainedPat: FineGrainedPat = FineGrainedPat(client, org)

          public val oauthApp: OauthApp = OauthApp(client, org)

          public val githubAppUserToServer: GithubAppUserToServer =
              GithubAppUserToServer(client, org)

          public class Installation internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public fun actorId(actorId: Long): ActorIdPath = ActorIdPath(client, org, actorId)

            public class ActorIdPath internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val actorId: Long,
            ) {
              public val `get`: Get = Get(client, org, actorId)

              public class Get internal constructor(
                private val client: HttpClient,
                private val org: String,
                private val actorId: Long,
              ) {
                public suspend operator fun invoke(
                  minTimestamp: String,
                  maxTimestamp: String? = null,
                  page: Long? = 1L,
                  perPage: Long? = 30L,
                  direction: Direction? = Direction.Desc,
                  sort: List<Sort>? = null,
                  apiRouteSubstring: String? = null,
                ): ApiInsightsRouteStats = client.get("/orgs/$org/insights/api/route-stats/installation/$actorId") {
                  parameter("min_timestamp", minTimestamp)
                  maxTimestamp?.let { parameter("max_timestamp", it) }
                  page?.let { parameter("page", it) }
                  perPage?.let { parameter("per_page", it) }
                  direction?.let { parameter("direction", it.value) }
                  sort?.let { parameter("sort", it) }
                  apiRouteSubstring?.let { parameter("api_route_substring", it) }
                }.body()

                @Serializable
                public enum class Direction(
                  public val `value`: String,
                ) {
                  @SerialName("asc")
                  Asc("asc"),
                  @SerialName("desc")
                  Desc("desc"),
                  ;
                }

                @Serializable
                public enum class Sort(
                  public val `value`: String,
                ) {
                  @SerialName("last_rate_limited_timestamp")
                  LastRateLimitedTimestamp("last_rate_limited_timestamp"),
                  @SerialName("last_request_timestamp")
                  LastRequestTimestamp("last_request_timestamp"),
                  @SerialName("rate_limited_request_count")
                  RateLimitedRequestCount("rate_limited_request_count"),
                  @SerialName("http_method")
                  HttpMethod("http_method"),
                  @SerialName("api_route")
                  ApiRoute("api_route"),
                  @SerialName("total_request_count")
                  TotalRequestCount("total_request_count"),
                  ;
                }
              }
            }
          }

          public class ClassicPat internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public fun actorId(actorId: Long): ActorIdPath = ActorIdPath(client, org, actorId)

            public class ActorIdPath internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val actorId: Long,
            ) {
              public val `get`: Get = Get(client, org, actorId)

              public class Get internal constructor(
                private val client: HttpClient,
                private val org: String,
                private val actorId: Long,
              ) {
                public suspend operator fun invoke(
                  minTimestamp: String,
                  maxTimestamp: String? = null,
                  page: Long? = 1L,
                  perPage: Long? = 30L,
                  direction: Direction? = Direction.Desc,
                  sort: List<Sort>? = null,
                  apiRouteSubstring: String? = null,
                ): ApiInsightsRouteStats = client.get("/orgs/$org/insights/api/route-stats/classic_pat/$actorId") {
                  parameter("min_timestamp", minTimestamp)
                  maxTimestamp?.let { parameter("max_timestamp", it) }
                  page?.let { parameter("page", it) }
                  perPage?.let { parameter("per_page", it) }
                  direction?.let { parameter("direction", it.value) }
                  sort?.let { parameter("sort", it) }
                  apiRouteSubstring?.let { parameter("api_route_substring", it) }
                }.body()

                @Serializable
                public enum class Direction(
                  public val `value`: String,
                ) {
                  @SerialName("asc")
                  Asc("asc"),
                  @SerialName("desc")
                  Desc("desc"),
                  ;
                }

                @Serializable
                public enum class Sort(
                  public val `value`: String,
                ) {
                  @SerialName("last_rate_limited_timestamp")
                  LastRateLimitedTimestamp("last_rate_limited_timestamp"),
                  @SerialName("last_request_timestamp")
                  LastRequestTimestamp("last_request_timestamp"),
                  @SerialName("rate_limited_request_count")
                  RateLimitedRequestCount("rate_limited_request_count"),
                  @SerialName("http_method")
                  HttpMethod("http_method"),
                  @SerialName("api_route")
                  ApiRoute("api_route"),
                  @SerialName("total_request_count")
                  TotalRequestCount("total_request_count"),
                  ;
                }
              }
            }
          }

          public class FineGrainedPat internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public fun actorId(actorId: Long): ActorIdPath = ActorIdPath(client, org, actorId)

            public class ActorIdPath internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val actorId: Long,
            ) {
              public val `get`: Get = Get(client, org, actorId)

              public class Get internal constructor(
                private val client: HttpClient,
                private val org: String,
                private val actorId: Long,
              ) {
                public suspend operator fun invoke(
                  minTimestamp: String,
                  maxTimestamp: String? = null,
                  page: Long? = 1L,
                  perPage: Long? = 30L,
                  direction: Direction? = Direction.Desc,
                  sort: List<Sort>? = null,
                  apiRouteSubstring: String? = null,
                ): ApiInsightsRouteStats = client.get("/orgs/$org/insights/api/route-stats/fine_grained_pat/$actorId") {
                  parameter("min_timestamp", minTimestamp)
                  maxTimestamp?.let { parameter("max_timestamp", it) }
                  page?.let { parameter("page", it) }
                  perPage?.let { parameter("per_page", it) }
                  direction?.let { parameter("direction", it.value) }
                  sort?.let { parameter("sort", it) }
                  apiRouteSubstring?.let { parameter("api_route_substring", it) }
                }.body()

                @Serializable
                public enum class Direction(
                  public val `value`: String,
                ) {
                  @SerialName("asc")
                  Asc("asc"),
                  @SerialName("desc")
                  Desc("desc"),
                  ;
                }

                @Serializable
                public enum class Sort(
                  public val `value`: String,
                ) {
                  @SerialName("last_rate_limited_timestamp")
                  LastRateLimitedTimestamp("last_rate_limited_timestamp"),
                  @SerialName("last_request_timestamp")
                  LastRequestTimestamp("last_request_timestamp"),
                  @SerialName("rate_limited_request_count")
                  RateLimitedRequestCount("rate_limited_request_count"),
                  @SerialName("http_method")
                  HttpMethod("http_method"),
                  @SerialName("api_route")
                  ApiRoute("api_route"),
                  @SerialName("total_request_count")
                  TotalRequestCount("total_request_count"),
                  ;
                }
              }
            }
          }

          public class OauthApp internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public fun actorId(actorId: Long): ActorIdPath = ActorIdPath(client, org, actorId)

            public class ActorIdPath internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val actorId: Long,
            ) {
              public val `get`: Get = Get(client, org, actorId)

              public class Get internal constructor(
                private val client: HttpClient,
                private val org: String,
                private val actorId: Long,
              ) {
                public suspend operator fun invoke(
                  minTimestamp: String,
                  maxTimestamp: String? = null,
                  page: Long? = 1L,
                  perPage: Long? = 30L,
                  direction: Direction? = Direction.Desc,
                  sort: List<Sort>? = null,
                  apiRouteSubstring: String? = null,
                ): ApiInsightsRouteStats = client.get("/orgs/$org/insights/api/route-stats/oauth_app/$actorId") {
                  parameter("min_timestamp", minTimestamp)
                  maxTimestamp?.let { parameter("max_timestamp", it) }
                  page?.let { parameter("page", it) }
                  perPage?.let { parameter("per_page", it) }
                  direction?.let { parameter("direction", it.value) }
                  sort?.let { parameter("sort", it) }
                  apiRouteSubstring?.let { parameter("api_route_substring", it) }
                }.body()

                @Serializable
                public enum class Direction(
                  public val `value`: String,
                ) {
                  @SerialName("asc")
                  Asc("asc"),
                  @SerialName("desc")
                  Desc("desc"),
                  ;
                }

                @Serializable
                public enum class Sort(
                  public val `value`: String,
                ) {
                  @SerialName("last_rate_limited_timestamp")
                  LastRateLimitedTimestamp("last_rate_limited_timestamp"),
                  @SerialName("last_request_timestamp")
                  LastRequestTimestamp("last_request_timestamp"),
                  @SerialName("rate_limited_request_count")
                  RateLimitedRequestCount("rate_limited_request_count"),
                  @SerialName("http_method")
                  HttpMethod("http_method"),
                  @SerialName("api_route")
                  ApiRoute("api_route"),
                  @SerialName("total_request_count")
                  TotalRequestCount("total_request_count"),
                  ;
                }
              }
            }
          }

          public class GithubAppUserToServer internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public fun actorId(actorId: Long): ActorIdPath = ActorIdPath(client, org, actorId)

            public class ActorIdPath internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val actorId: Long,
            ) {
              public val `get`: Get = Get(client, org, actorId)

              public class Get internal constructor(
                private val client: HttpClient,
                private val org: String,
                private val actorId: Long,
              ) {
                public suspend operator fun invoke(
                  minTimestamp: String,
                  maxTimestamp: String? = null,
                  page: Long? = 1L,
                  perPage: Long? = 30L,
                  direction: Direction? = Direction.Desc,
                  sort: List<Sort>? = null,
                  apiRouteSubstring: String? = null,
                ): ApiInsightsRouteStats = client.get("/orgs/$org/insights/api/route-stats/github_app_user_to_server/$actorId") {
                  parameter("min_timestamp", minTimestamp)
                  maxTimestamp?.let { parameter("max_timestamp", it) }
                  page?.let { parameter("page", it) }
                  perPage?.let { parameter("per_page", it) }
                  direction?.let { parameter("direction", it.value) }
                  sort?.let { parameter("sort", it) }
                  apiRouteSubstring?.let { parameter("api_route_substring", it) }
                }.body()

                @Serializable
                public enum class Direction(
                  public val `value`: String,
                ) {
                  @SerialName("asc")
                  Asc("asc"),
                  @SerialName("desc")
                  Desc("desc"),
                  ;
                }

                @Serializable
                public enum class Sort(
                  public val `value`: String,
                ) {
                  @SerialName("last_rate_limited_timestamp")
                  LastRateLimitedTimestamp("last_rate_limited_timestamp"),
                  @SerialName("last_request_timestamp")
                  LastRequestTimestamp("last_request_timestamp"),
                  @SerialName("rate_limited_request_count")
                  RateLimitedRequestCount("rate_limited_request_count"),
                  @SerialName("http_method")
                  HttpMethod("http_method"),
                  @SerialName("api_route")
                  ApiRoute("api_route"),
                  @SerialName("total_request_count")
                  TotalRequestCount("total_request_count"),
                  ;
                }
              }
            }
          }
        }

        public class SubjectStats internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public val `get`: Get = Get(client, org)

          public class Get internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public suspend operator fun invoke(
              minTimestamp: String,
              maxTimestamp: String? = null,
              page: Long? = 1L,
              perPage: Long? = 30L,
              direction: Direction? = Direction.Desc,
              sort: List<Sort>? = null,
              subjectNameSubstring: String? = null,
            ): ApiInsightsSubjectStats = client.get("/orgs/$org/insights/api/subject-stats") {
              parameter("min_timestamp", minTimestamp)
              maxTimestamp?.let { parameter("max_timestamp", it) }
              page?.let { parameter("page", it) }
              perPage?.let { parameter("per_page", it) }
              direction?.let { parameter("direction", it.value) }
              sort?.let { parameter("sort", it) }
              subjectNameSubstring?.let { parameter("subject_name_substring", it) }
            }.body()

            @Serializable
            public enum class Direction(
              public val `value`: String,
            ) {
              @SerialName("asc")
              Asc("asc"),
              @SerialName("desc")
              Desc("desc"),
              ;
            }

            @Serializable
            public enum class Sort(
              public val `value`: String,
            ) {
              @SerialName("last_rate_limited_timestamp")
              LastRateLimitedTimestamp("last_rate_limited_timestamp"),
              @SerialName("last_request_timestamp")
              LastRequestTimestamp("last_request_timestamp"),
              @SerialName("rate_limited_request_count")
              RateLimitedRequestCount("rate_limited_request_count"),
              @SerialName("subject_name")
              SubjectName("subject_name"),
              @SerialName("total_request_count")
              TotalRequestCount("total_request_count"),
              ;
            }
          }
        }

        public class SummaryStats internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public val `get`: Get = Get(client, org)

          public val users: Users = Users(client, org)

          public val installation: Installation = Installation(client, org)

          public val classicPat: ClassicPat = ClassicPat(client, org)

          public val fineGrainedPat: FineGrainedPat = FineGrainedPat(client, org)

          public val oauthApp: OauthApp = OauthApp(client, org)

          public val githubAppUserToServer: GithubAppUserToServer =
              GithubAppUserToServer(client, org)

          public class Get internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public suspend operator fun invoke(minTimestamp: String, maxTimestamp: String? = null): ApiInsightsSummaryStats = client.get("/orgs/$org/insights/api/summary-stats") {
              parameter("min_timestamp", minTimestamp)
              maxTimestamp?.let { parameter("max_timestamp", it) }
            }.body()
          }

          public class Users internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public fun userId(userId: String): UserIdPath = UserIdPath(client, org, userId)

            public class UserIdPath internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val userId: String,
            ) {
              public val `get`: Get = Get(client, org, userId)

              public class Get internal constructor(
                private val client: HttpClient,
                private val org: String,
                private val userId: String,
              ) {
                public suspend operator fun invoke(minTimestamp: String, maxTimestamp: String? = null): ApiInsightsSummaryStats = client.get("/orgs/$org/insights/api/summary-stats/users/$userId") {
                  parameter("min_timestamp", minTimestamp)
                  maxTimestamp?.let { parameter("max_timestamp", it) }
                }.body()
              }
            }
          }

          public class Installation internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public fun actorId(actorId: Long): ActorIdPath = ActorIdPath(client, org, actorId)

            public class ActorIdPath internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val actorId: Long,
            ) {
              public val `get`: Get = Get(client, org, actorId)

              public class Get internal constructor(
                private val client: HttpClient,
                private val org: String,
                private val actorId: Long,
              ) {
                public suspend operator fun invoke(minTimestamp: String, maxTimestamp: String? = null): ApiInsightsSummaryStats = client.get("/orgs/$org/insights/api/summary-stats/installation/$actorId") {
                  parameter("min_timestamp", minTimestamp)
                  maxTimestamp?.let { parameter("max_timestamp", it) }
                }.body()
              }
            }
          }

          public class ClassicPat internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public fun actorId(actorId: Long): ActorIdPath = ActorIdPath(client, org, actorId)

            public class ActorIdPath internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val actorId: Long,
            ) {
              public val `get`: Get = Get(client, org, actorId)

              public class Get internal constructor(
                private val client: HttpClient,
                private val org: String,
                private val actorId: Long,
              ) {
                public suspend operator fun invoke(minTimestamp: String, maxTimestamp: String? = null): ApiInsightsSummaryStats = client.get("/orgs/$org/insights/api/summary-stats/classic_pat/$actorId") {
                  parameter("min_timestamp", minTimestamp)
                  maxTimestamp?.let { parameter("max_timestamp", it) }
                }.body()
              }
            }
          }

          public class FineGrainedPat internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public fun actorId(actorId: Long): ActorIdPath = ActorIdPath(client, org, actorId)

            public class ActorIdPath internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val actorId: Long,
            ) {
              public val `get`: Get = Get(client, org, actorId)

              public class Get internal constructor(
                private val client: HttpClient,
                private val org: String,
                private val actorId: Long,
              ) {
                public suspend operator fun invoke(minTimestamp: String, maxTimestamp: String? = null): ApiInsightsSummaryStats = client.get("/orgs/$org/insights/api/summary-stats/fine_grained_pat/$actorId") {
                  parameter("min_timestamp", minTimestamp)
                  maxTimestamp?.let { parameter("max_timestamp", it) }
                }.body()
              }
            }
          }

          public class OauthApp internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public fun actorId(actorId: Long): ActorIdPath = ActorIdPath(client, org, actorId)

            public class ActorIdPath internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val actorId: Long,
            ) {
              public val `get`: Get = Get(client, org, actorId)

              public class Get internal constructor(
                private val client: HttpClient,
                private val org: String,
                private val actorId: Long,
              ) {
                public suspend operator fun invoke(minTimestamp: String, maxTimestamp: String? = null): ApiInsightsSummaryStats = client.get("/orgs/$org/insights/api/summary-stats/oauth_app/$actorId") {
                  parameter("min_timestamp", minTimestamp)
                  maxTimestamp?.let { parameter("max_timestamp", it) }
                }.body()
              }
            }
          }

          public class GithubAppUserToServer internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public fun actorId(actorId: Long): ActorIdPath = ActorIdPath(client, org, actorId)

            public class ActorIdPath internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val actorId: Long,
            ) {
              public val `get`: Get = Get(client, org, actorId)

              public class Get internal constructor(
                private val client: HttpClient,
                private val org: String,
                private val actorId: Long,
              ) {
                public suspend operator fun invoke(minTimestamp: String, maxTimestamp: String? = null): ApiInsightsSummaryStats = client.get("/orgs/$org/insights/api/summary-stats/github_app_user_to_server/$actorId") {
                  parameter("min_timestamp", minTimestamp)
                  maxTimestamp?.let { parameter("max_timestamp", it) }
                }.body()
              }
            }
          }
        }

        public class TimeStats internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public val `get`: Get = Get(client, org)

          public val users: Users = Users(client, org)

          public val installation: Installation = Installation(client, org)

          public val classicPat: ClassicPat = ClassicPat(client, org)

          public val fineGrainedPat: FineGrainedPat = FineGrainedPat(client, org)

          public val oauthApp: OauthApp = OauthApp(client, org)

          public val githubAppUserToServer: GithubAppUserToServer =
              GithubAppUserToServer(client, org)

          public class Get internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public suspend operator fun invoke(
              minTimestamp: String,
              timestampIncrement: String,
              maxTimestamp: String? = null,
            ): ApiInsightsTimeStats = client.get("/orgs/$org/insights/api/time-stats") {
              parameter("min_timestamp", minTimestamp)
              maxTimestamp?.let { parameter("max_timestamp", it) }
              parameter("timestamp_increment", timestampIncrement)
            }.body()
          }

          public class Users internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public fun userId(userId: String): UserIdPath = UserIdPath(client, org, userId)

            public class UserIdPath internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val userId: String,
            ) {
              public val `get`: Get = Get(client, org, userId)

              public class Get internal constructor(
                private val client: HttpClient,
                private val org: String,
                private val userId: String,
              ) {
                public suspend operator fun invoke(
                  minTimestamp: String,
                  timestampIncrement: String,
                  maxTimestamp: String? = null,
                ): ApiInsightsTimeStats = client.get("/orgs/$org/insights/api/time-stats/users/$userId") {
                  parameter("min_timestamp", minTimestamp)
                  maxTimestamp?.let { parameter("max_timestamp", it) }
                  parameter("timestamp_increment", timestampIncrement)
                }.body()
              }
            }
          }

          public class Installation internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public fun actorId(actorId: Long): ActorIdPath = ActorIdPath(client, org, actorId)

            public class ActorIdPath internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val actorId: Long,
            ) {
              public val `get`: Get = Get(client, org, actorId)

              public class Get internal constructor(
                private val client: HttpClient,
                private val org: String,
                private val actorId: Long,
              ) {
                public suspend operator fun invoke(
                  minTimestamp: String,
                  timestampIncrement: String,
                  maxTimestamp: String? = null,
                ): ApiInsightsTimeStats = client.get("/orgs/$org/insights/api/time-stats/installation/$actorId") {
                  parameter("min_timestamp", minTimestamp)
                  maxTimestamp?.let { parameter("max_timestamp", it) }
                  parameter("timestamp_increment", timestampIncrement)
                }.body()
              }
            }
          }

          public class ClassicPat internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public fun actorId(actorId: Long): ActorIdPath = ActorIdPath(client, org, actorId)

            public class ActorIdPath internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val actorId: Long,
            ) {
              public val `get`: Get = Get(client, org, actorId)

              public class Get internal constructor(
                private val client: HttpClient,
                private val org: String,
                private val actorId: Long,
              ) {
                public suspend operator fun invoke(
                  minTimestamp: String,
                  timestampIncrement: String,
                  maxTimestamp: String? = null,
                ): ApiInsightsTimeStats = client.get("/orgs/$org/insights/api/time-stats/classic_pat/$actorId") {
                  parameter("min_timestamp", minTimestamp)
                  maxTimestamp?.let { parameter("max_timestamp", it) }
                  parameter("timestamp_increment", timestampIncrement)
                }.body()
              }
            }
          }

          public class FineGrainedPat internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public fun actorId(actorId: Long): ActorIdPath = ActorIdPath(client, org, actorId)

            public class ActorIdPath internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val actorId: Long,
            ) {
              public val `get`: Get = Get(client, org, actorId)

              public class Get internal constructor(
                private val client: HttpClient,
                private val org: String,
                private val actorId: Long,
              ) {
                public suspend operator fun invoke(
                  minTimestamp: String,
                  timestampIncrement: String,
                  maxTimestamp: String? = null,
                ): ApiInsightsTimeStats = client.get("/orgs/$org/insights/api/time-stats/fine_grained_pat/$actorId") {
                  parameter("min_timestamp", minTimestamp)
                  maxTimestamp?.let { parameter("max_timestamp", it) }
                  parameter("timestamp_increment", timestampIncrement)
                }.body()
              }
            }
          }

          public class OauthApp internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public fun actorId(actorId: Long): ActorIdPath = ActorIdPath(client, org, actorId)

            public class ActorIdPath internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val actorId: Long,
            ) {
              public val `get`: Get = Get(client, org, actorId)

              public class Get internal constructor(
                private val client: HttpClient,
                private val org: String,
                private val actorId: Long,
              ) {
                public suspend operator fun invoke(
                  minTimestamp: String,
                  timestampIncrement: String,
                  maxTimestamp: String? = null,
                ): ApiInsightsTimeStats = client.get("/orgs/$org/insights/api/time-stats/oauth_app/$actorId") {
                  parameter("min_timestamp", minTimestamp)
                  maxTimestamp?.let { parameter("max_timestamp", it) }
                  parameter("timestamp_increment", timestampIncrement)
                }.body()
              }
            }
          }

          public class GithubAppUserToServer internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public fun actorId(actorId: Long): ActorIdPath = ActorIdPath(client, org, actorId)

            public class ActorIdPath internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val actorId: Long,
            ) {
              public val `get`: Get = Get(client, org, actorId)

              public class Get internal constructor(
                private val client: HttpClient,
                private val org: String,
                private val actorId: Long,
              ) {
                public suspend operator fun invoke(
                  minTimestamp: String,
                  timestampIncrement: String,
                  maxTimestamp: String? = null,
                ): ApiInsightsTimeStats = client.get("/orgs/$org/insights/api/time-stats/github_app_user_to_server/$actorId") {
                  parameter("min_timestamp", minTimestamp)
                  maxTimestamp?.let { parameter("max_timestamp", it) }
                  parameter("timestamp_increment", timestampIncrement)
                }.body()
              }
            }
          }
        }

        public class UserStats internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public fun userId(userId: String): UserIdPath = UserIdPath(client, org, userId)

          public class UserIdPath internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val userId: String,
          ) {
            public val `get`: Get = Get(client, org, userId)

            public class Get internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val userId: String,
            ) {
              public suspend operator fun invoke(
                minTimestamp: String,
                maxTimestamp: String? = null,
                page: Long? = 1L,
                perPage: Long? = 30L,
                direction: Direction? = Direction.Desc,
                sort: List<Sort>? = null,
                actorNameSubstring: String? = null,
              ): ApiInsightsUserStats = client.get("/orgs/$org/insights/api/user-stats/$userId") {
                parameter("min_timestamp", minTimestamp)
                maxTimestamp?.let { parameter("max_timestamp", it) }
                page?.let { parameter("page", it) }
                perPage?.let { parameter("per_page", it) }
                direction?.let { parameter("direction", it.value) }
                sort?.let { parameter("sort", it) }
                actorNameSubstring?.let { parameter("actor_name_substring", it) }
              }.body()

              @Serializable
              public enum class Direction(
                public val `value`: String,
              ) {
                @SerialName("asc")
                Asc("asc"),
                @SerialName("desc")
                Desc("desc"),
                ;
              }

              @Serializable
              public enum class Sort(
                public val `value`: String,
              ) {
                @SerialName("last_rate_limited_timestamp")
                LastRateLimitedTimestamp("last_rate_limited_timestamp"),
                @SerialName("last_request_timestamp")
                LastRequestTimestamp("last_request_timestamp"),
                @SerialName("rate_limited_request_count")
                RateLimitedRequestCount("rate_limited_request_count"),
                @SerialName("subject_name")
                SubjectName("subject_name"),
                @SerialName("total_request_count")
                TotalRequestCount("total_request_count"),
                ;
              }
            }
          }
        }
      }
    }

    public class Installation internal constructor(
      private val client: HttpClient,
      private val org: String,
    ) {
      public val `get`: Get = Get(client, org)

      public class Get internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public suspend operator fun invoke(): io.github.model.Installation = client.get("/orgs/$org/installation").body()
      }
    }

    public class Installations internal constructor(
      private val client: HttpClient,
      private val org: String,
    ) {
      public val `get`: Get = Get(client, org)

      public class Get internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response = client.get("/orgs/$org/installations") {
          perPage?.let { parameter("per_page", it) }
          page?.let { parameter("page", it) }
        }.body()

        @Serializable
        public data class Response(
          @SerialName("total_count")
          public val totalCount: Long,
          public val installations: List<io.github.model.Installation>,
        )
      }
    }

    public class InteractionLimits internal constructor(
      private val client: HttpClient,
      private val org: String,
    ) {
      public val delete: Delete = Delete(client, org)

      public val `get`: Get = Get(client, org)

      public val put: Put = Put(client, org)

      public class Delete internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public suspend operator fun invoke() {
          client.delete("/orgs/$org/interaction-limits")
        }
      }

      public class Get internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public suspend operator fun invoke(): Response = client.get("/orgs/$org/interaction-limits").body()

        @Serializable(with = Response.Serializer::class)
        public sealed interface Response {
          @Serializable
          @JvmInline
          public value class CaseInteractionLimitResponse(
            public val `value`: InteractionLimitResponse,
          ) : Response

          @Serializable
          public data object Empty : Response

          public object Serializer : KSerializer<Response> {
            @OptIn(
              InternalSerializationApi::class,
              ExperimentalSerializationApi::class,
            )
            override val descriptor: SerialDescriptor =
                buildSerialDescriptor("io.github.api.Orgs.OrgPath.InteractionLimits.Get.Response", PolymorphicKind.SEALED) {
              element("CaseInteractionLimitResponse", InteractionLimitResponse.serializer().descriptor)
              element("Empty", Empty.serializer().descriptor)
            }

            override fun deserialize(decoder: Decoder): Response {
              val value = decoder.decodeSerializableValue(JsonElement.serializer())
              val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
              return json.attemptDeserialize(
                value,
                CaseInteractionLimitResponse::class to { CaseInteractionLimitResponse(decodeFromJsonElement(InteractionLimitResponse.serializer(), it)) },
                Empty::class to { decodeFromJsonElement(Empty.serializer(), it) },
              )
            }

            override fun serialize(encoder: Encoder, `value`: Response) {
              when(value) {
                is CaseInteractionLimitResponse -> encoder.encodeSerializableValue(InteractionLimitResponse.serializer(), value.value)
                is Empty -> encoder.encodeSerializableValue(Empty.serializer(), value)
              }
            }
          }
        }
      }

      public class Put internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public suspend operator fun invoke(body: InteractionLimit): Response {
          val response = client.put("/orgs/$org/interaction-limits") {
            contentType(ContentType.Application.Json)
            setBody(body)
          }
          return when (response.status.value) {
            200 -> Response.Ok(response.body())
            422 -> Response.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        public sealed interface Response {
          public data class Ok(
            public val `value`: InteractionLimitResponse,
          ) : Response

          public data class UnprocessableEntity(
            public val `value`: ValidationError,
          ) : Response
        }
      }
    }

    public class Invitations internal constructor(
      private val client: HttpClient,
      private val org: String,
    ) {
      public val `get`: Get = Get(client, org)

      public val post: Post = Post(client, org)

      public fun invitationId(invitationId: Long): InvitationIdPath = InvitationIdPath(client, org, invitationId)

      public class Get internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public suspend operator fun invoke(
          perPage: Long? = 30L,
          page: Long? = 1L,
          role: Role? = Role.All,
          invitationSource: InvitationSource? = InvitationSource.All,
        ): Response {
          val response = client.get("/orgs/$org/invitations") {
            perPage?.let { parameter("per_page", it) }
            page?.let { parameter("page", it) }
            role?.let { parameter("role", it.value) }
            invitationSource?.let { parameter("invitation_source", it.value) }
          }
          return when (response.status.value) {
            200 -> Response.Ok(response.body())
            404 -> Response.NotFound(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        @Serializable
        public enum class Role(
          public val `value`: String,
        ) {
          @SerialName("all")
          All("all"),
          @SerialName("admin")
          Admin("admin"),
          @SerialName("direct_member")
          DirectMember("direct_member"),
          @SerialName("billing_manager")
          BillingManager("billing_manager"),
          @SerialName("hiring_manager")
          HiringManager("hiring_manager"),
          ;
        }

        @Serializable
        public enum class InvitationSource(
          public val `value`: String,
        ) {
          @SerialName("all")
          All("all"),
          @SerialName("member")
          Member("member"),
          @SerialName("scim")
          Scim("scim"),
          ;
        }

        public sealed interface Response {
          public data class Ok(
            public val `value`: List<OrganizationInvitation>,
          ) : Response

          public data class NotFound(
            public val `value`: BasicError,
          ) : Response
        }
      }

      public class Post internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public suspend operator fun invoke(
          inviteeId: Long? = null,
          email: String? = null,
          role: Role? = null,
          teamIds: List<Long>? = null,
        ): Response {
          val response = client.post("/orgs/$org/invitations") {
            if (inviteeId != null || email != null || role != null || teamIds != null) {
              contentType(ContentType.Application.Json)
              setBody(Body(inviteeId = inviteeId, email = email, role = role, teamIds = teamIds))
            }
          }
          return when (response.status.value) {
            201 -> Response.Created(response.body())
            404 -> Response.NotFound(response.body())
            422 -> Response.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        @Serializable
        public enum class Role(
          public val `value`: String,
        ) {
          @SerialName("admin")
          Admin("admin"),
          @SerialName("direct_member")
          DirectMember("direct_member"),
          @SerialName("billing_manager")
          BillingManager("billing_manager"),
          @SerialName("reinstate")
          Reinstate("reinstate"),
          ;
        }

        @Serializable
        internal data class Body(
          @SerialName("invitee_id")
          public val inviteeId: Long? = null,
          public val email: String? = null,
          public val role: Role? = null,
          @SerialName("team_ids")
          public val teamIds: List<Long>? = null,
        )

        public sealed interface Response {
          public data class Created(
            public val `value`: OrganizationInvitation,
          ) : Response

          public data class NotFound(
            public val `value`: BasicError,
          ) : Response

          public data class UnprocessableEntity(
            public val `value`: ValidationError,
          ) : Response
        }
      }

      public class InvitationIdPath internal constructor(
        private val client: HttpClient,
        private val org: String,
        private val invitationId: Long,
      ) {
        public val delete: Delete = Delete(client, org, invitationId)

        public val teams: Teams = Teams(client, org, invitationId)

        public class Delete internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val invitationId: Long,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.delete("/orgs/$org/invitations/$invitationId")
            return when (response.status.value) {
              204 -> Response.NoContent
              404 -> Response.NotFound(response.body())
              422 -> Response.UnprocessableEntity(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data object NoContent : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response

            public data class UnprocessableEntity(
              public val `value`: ValidationError,
            ) : Response
          }
        }

        public class Teams internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val invitationId: Long,
        ) {
          public val `get`: Get = Get(client, org, invitationId)

          public class Get internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val invitationId: Long,
          ) {
            public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
              val response = client.get("/orgs/$org/invitations/$invitationId/teams") {
                perPage?.let { parameter("per_page", it) }
                page?.let { parameter("page", it) }
              }
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: List<io.github.model.Team>,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response
            }
          }
        }
      }
    }

    public class IssueFields internal constructor(
      private val client: HttpClient,
      private val org: String,
    ) {
      public val `get`: Get = Get(client, org)

      public val post: Post = Post(client, org)

      public fun issueFieldId(issueFieldId: Long): IssueFieldIdPath = IssueFieldIdPath(client, org, issueFieldId)

      public class Get internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public suspend operator fun invoke(): Response {
          val response = client.get("/orgs/$org/issue-fields")
          return when (response.status.value) {
            200 -> Response.Ok(response.body())
            404 -> Response.NotFound(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        public sealed interface Response {
          public data class Ok(
            public val `value`: List<IssueField?>,
          ) : Response

          public data class NotFound(
            public val `value`: BasicError,
          ) : Response
        }
      }

      public class Post internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public suspend operator fun invoke(body: OrganizationCreateIssueField): Response {
          val response = client.post("/orgs/$org/issue-fields") {
            contentType(ContentType.Application.Json)
            setBody(body)
          }
          return when (response.status.value) {
            200 -> Response.Ok(response.body())
            404 -> Response.NotFound(response.body())
            422 -> Response.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        public sealed interface Response {
          public data class Ok(
            public val `value`: IssueField?,
          ) : Response

          public data class NotFound(
            public val `value`: BasicError,
          ) : Response

          public data class UnprocessableEntity(
            public val `value`: ValidationErrorSimple,
          ) : Response
        }
      }

      public class IssueFieldIdPath internal constructor(
        private val client: HttpClient,
        private val org: String,
        private val issueFieldId: Long,
      ) {
        public val delete: Delete = Delete(client, org, issueFieldId)

        public val patch: Patch = Patch(client, org, issueFieldId)

        public class Delete internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val issueFieldId: Long,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.delete("/orgs/$org/issue-fields/$issueFieldId")
            return when (response.status.value) {
              204 -> Response.NoContent
              404 -> Response.NotFound(response.body())
              422 -> Response.UnprocessableEntity(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data object NoContent : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response

            public data class UnprocessableEntity(
              public val `value`: ValidationErrorSimple,
            ) : Response
          }
        }

        public class Patch internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val issueFieldId: Long,
        ) {
          public suspend operator fun invoke(body: OrganizationUpdateIssueField): Response {
            val response = client.patch("/orgs/$org/issue-fields/$issueFieldId") {
              contentType(ContentType.Application.Json)
              setBody(body)
            }
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              404 -> Response.NotFound(response.body())
              422 -> Response.UnprocessableEntity(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: IssueField?,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response

            public data class UnprocessableEntity(
              public val `value`: ValidationErrorSimple,
            ) : Response
          }
        }
      }
    }

    public class IssueTypes internal constructor(
      private val client: HttpClient,
      private val org: String,
    ) {
      public val `get`: Get = Get(client, org)

      public val post: Post = Post(client, org)

      public fun issueTypeId(issueTypeId: Long): IssueTypeIdPath = IssueTypeIdPath(client, org, issueTypeId)

      public class Get internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public suspend operator fun invoke(): Response {
          val response = client.get("/orgs/$org/issue-types")
          return when (response.status.value) {
            200 -> Response.Ok(response.body())
            404 -> Response.NotFound(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        public sealed interface Response {
          public data class Ok(
            public val `value`: List<IssueType?>,
          ) : Response

          public data class NotFound(
            public val `value`: BasicError,
          ) : Response
        }
      }

      public class Post internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public suspend operator fun invoke(body: OrganizationCreateIssueType): Response {
          val response = client.post("/orgs/$org/issue-types") {
            contentType(ContentType.Application.Json)
            setBody(body)
          }
          return when (response.status.value) {
            200 -> Response.Ok(response.body())
            404 -> Response.NotFound(response.body())
            422 -> Response.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        public sealed interface Response {
          public data class Ok(
            public val `value`: IssueType?,
          ) : Response

          public data class NotFound(
            public val `value`: BasicError,
          ) : Response

          public data class UnprocessableEntity(
            public val `value`: ValidationErrorSimple,
          ) : Response
        }
      }

      public class IssueTypeIdPath internal constructor(
        private val client: HttpClient,
        private val org: String,
        private val issueTypeId: Long,
      ) {
        public val delete: Delete = Delete(client, org, issueTypeId)

        public val put: Put = Put(client, org, issueTypeId)

        public class Delete internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val issueTypeId: Long,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.delete("/orgs/$org/issue-types/$issueTypeId")
            return when (response.status.value) {
              204 -> Response.NoContent
              404 -> Response.NotFound(response.body())
              422 -> Response.UnprocessableEntity(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data object NoContent : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response

            public data class UnprocessableEntity(
              public val `value`: ValidationErrorSimple,
            ) : Response
          }
        }

        public class Put internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val issueTypeId: Long,
        ) {
          public suspend operator fun invoke(body: OrganizationUpdateIssueType): Response {
            val response = client.put("/orgs/$org/issue-types/$issueTypeId") {
              contentType(ContentType.Application.Json)
              setBody(body)
            }
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              404 -> Response.NotFound(response.body())
              422 -> Response.UnprocessableEntity(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: IssueType?,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response

            public data class UnprocessableEntity(
              public val `value`: ValidationErrorSimple,
            ) : Response
          }
        }
      }
    }

    public class Issues internal constructor(
      private val client: HttpClient,
      private val org: String,
    ) {
      public val `get`: Get = Get(client, org)

      public class Get internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public suspend operator fun invoke(
          filter: Filter? = Filter.Assigned,
          state: State? = State.Open,
          labels: String? = null,
          type: String? = null,
          sort: Sort? = Sort.Created,
          direction: Direction? = Direction.Desc,
          since: Instant? = null,
          perPage: Long? = 30L,
          page: Long? = 1L,
        ): Response {
          val response = client.get("/orgs/$org/issues") {
            filter?.let { parameter("filter", it.value) }
            state?.let { parameter("state", it.value) }
            labels?.let { parameter("labels", it) }
            type?.let { parameter("type", it) }
            sort?.let { parameter("sort", it.value) }
            direction?.let { parameter("direction", it.value) }
            since?.let { parameter("since", it.toString()) }
            perPage?.let { parameter("per_page", it) }
            page?.let { parameter("page", it) }
          }
          return when (response.status.value) {
            200 -> Response.Ok(response.body())
            404 -> Response.NotFound(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        @Serializable
        public enum class Filter(
          public val `value`: String,
        ) {
          @SerialName("assigned")
          Assigned("assigned"),
          @SerialName("created")
          Created("created"),
          @SerialName("mentioned")
          Mentioned("mentioned"),
          @SerialName("subscribed")
          Subscribed("subscribed"),
          @SerialName("repos")
          Repos("repos"),
          @SerialName("all")
          All("all"),
          ;
        }

        @Serializable
        public enum class State(
          public val `value`: String,
        ) {
          @SerialName("open")
          Open("open"),
          @SerialName("closed")
          Closed("closed"),
          @SerialName("all")
          All("all"),
          ;
        }

        @Serializable
        public enum class Sort(
          public val `value`: String,
        ) {
          @SerialName("created")
          Created("created"),
          @SerialName("updated")
          Updated("updated"),
          @SerialName("comments")
          Comments("comments"),
          ;
        }

        @Serializable
        public enum class Direction(
          public val `value`: String,
        ) {
          @SerialName("asc")
          Asc("asc"),
          @SerialName("desc")
          Desc("desc"),
          ;
        }

        public sealed interface Response {
          public data class Ok(
            public val `value`: List<Issue>,
          ) : Response

          public data class NotFound(
            public val `value`: BasicError,
          ) : Response
        }
      }
    }

    public class Members internal constructor(
      private val client: HttpClient,
      private val org: String,
    ) {
      public val `get`: Get = Get(client, org)

      public fun username(username: String): UsernamePath = UsernamePath(client, org, username)

      public class Get internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public suspend operator fun invoke(
          filter: Filter? = Filter.All,
          role: Role? = Role.All,
          perPage: Long? = 30L,
          page: Long? = 1L,
        ): Response {
          val response = client.get("/orgs/$org/members") {
            filter?.let { parameter("filter", it.value) }
            role?.let { parameter("role", it.value) }
            perPage?.let { parameter("per_page", it) }
            page?.let { parameter("page", it) }
          }
          return when (response.status.value) {
            200 -> Response.Ok(response.body())
            422 -> Response.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        @Serializable
        public enum class Filter(
          public val `value`: String,
        ) {
          @SerialName("2fa_disabled")
          `2faDisabled`("2fa_disabled"),
          @SerialName("2fa_insecure")
          `2faInsecure`("2fa_insecure"),
          @SerialName("all")
          All("all"),
          ;
        }

        @Serializable
        public enum class Role(
          public val `value`: String,
        ) {
          @SerialName("all")
          All("all"),
          @SerialName("admin")
          Admin("admin"),
          @SerialName("member")
          Member("member"),
          ;
        }

        public sealed interface Response {
          public data class Ok(
            public val `value`: List<SimpleUser>,
          ) : Response

          public data class UnprocessableEntity(
            public val `value`: ValidationError,
          ) : Response
        }
      }

      public class UsernamePath internal constructor(
        private val client: HttpClient,
        private val org: String,
        private val username: String,
      ) {
        public val delete: Delete = Delete(client, org, username)

        public val `get`: Get = Get(client, org, username)

        public val codespaces: Codespaces = Codespaces(client, org, username)

        public val copilot: Copilot = Copilot(client, org, username)

        public class Delete internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val username: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.delete("/orgs/$org/members/$username")
            return when (response.status.value) {
              204 -> Response.NoContent
              403 -> Response.Forbidden(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data object NoContent : Response

            public data class Forbidden(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class Get internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val username: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.get("/orgs/$org/members/$username")
            return when (response.status.value) {
              204 -> Response.NoContent
              302 -> Response.Found
              404 -> Response.NotFound
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data object NoContent : Response

            public data object Found : Response

            public data object NotFound : Response
          }
        }

        public class Codespaces internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val username: String,
        ) {
          public val `get`: Get = Get(client, org, username)

          public fun codespaceName(codespaceName: String): CodespaceNamePath = CodespaceNamePath(client, org, username, codespaceName)

          public class Get internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val username: String,
          ) {
            public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
              val response = client.get("/orgs/$org/members/$username/codespaces") {
                perPage?.let { parameter("per_page", it) }
                page?.let { parameter("page", it) }
              }
              return when (response.status.value) {
                200 -> response.body<Response.Ok>()
                304 -> Response.NotModified
                401 -> Response.Unauthorized(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                500 -> Response.InternalServerError(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              @Serializable
              public data class Ok(
                @SerialName("total_count")
                public val totalCount: Long,
                public val codespaces: List<Codespace>,
              ) : Response

              public data object NotModified : Response

              public data class Unauthorized(
                public val `value`: BasicError,
              ) : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response

              public data class InternalServerError(
                public val `value`: BasicError,
              ) : Response
            }
          }

          public class CodespaceNamePath internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val username: String,
            private val codespaceName: String,
          ) {
            public val delete: Delete = Delete(client, org, username, codespaceName)

            public val stop: Stop = Stop(client, org, username, codespaceName)

            public class Delete internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val username: String,
              private val codespaceName: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.delete("/orgs/$org/members/$username/codespaces/$codespaceName")
                return when (response.status.value) {
                  202 -> Response.Accepted(response.body())
                  304 -> Response.NotModified
                  401 -> Response.Unauthorized(response.body())
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  500 -> Response.InternalServerError(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data class Accepted(
                  public val `value`: JsonElement,
                ) : Response

                public data object NotModified : Response

                public data class Unauthorized(
                  public val `value`: BasicError,
                ) : Response

                public data class Forbidden(
                  public val `value`: BasicError,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response

                public data class InternalServerError(
                  public val `value`: BasicError,
                ) : Response
              }
            }

            public class Stop internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val username: String,
              private val codespaceName: String,
            ) {
              public val post: Post = Post(client, org, username, codespaceName)

              public class Post internal constructor(
                private val client: HttpClient,
                private val org: String,
                private val username: String,
                private val codespaceName: String,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.post("/orgs/$org/members/$username/codespaces/$codespaceName/stop")
                  return when (response.status.value) {
                    200 -> Response.Ok(response.body())
                    304 -> Response.NotModified
                    401 -> Response.Unauthorized(response.body())
                    403 -> Response.Forbidden(response.body())
                    404 -> Response.NotFound(response.body())
                    500 -> Response.InternalServerError(response.body())
                    else -> throw ResponseException(response, "")
                  }
                }

                public sealed interface Response {
                  public data class Ok(
                    public val `value`: Codespace,
                  ) : Response

                  public data object NotModified : Response

                  public data class Unauthorized(
                    public val `value`: BasicError,
                  ) : Response

                  public data class Forbidden(
                    public val `value`: BasicError,
                  ) : Response

                  public data class NotFound(
                    public val `value`: BasicError,
                  ) : Response

                  public data class InternalServerError(
                    public val `value`: BasicError,
                  ) : Response
                }
              }
            }
          }
        }

        public class Copilot internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val username: String,
        ) {
          public val `get`: Get = Get(client, org, username)

          public class Get internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val username: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.get("/orgs/$org/members/$username/copilot")
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                401 -> Response.Unauthorized(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                422 -> Response.UnprocessableEntity
                500 -> Response.InternalServerError(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: CopilotSeatDetails,
              ) : Response

              public data class Unauthorized(
                public val `value`: BasicError,
              ) : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response

              public data object UnprocessableEntity : Response

              public data class InternalServerError(
                public val `value`: BasicError,
              ) : Response
            }
          }
        }
      }
    }

    public class Memberships internal constructor(
      private val client: HttpClient,
      private val org: String,
    ) {
      public fun username(username: String): UsernamePath = UsernamePath(client, org, username)

      public class UsernamePath internal constructor(
        private val client: HttpClient,
        private val org: String,
        private val username: String,
      ) {
        public val delete: Delete = Delete(client, org, username)

        public val `get`: Get = Get(client, org, username)

        public val put: Put = Put(client, org, username)

        public class Delete internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val username: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.delete("/orgs/$org/memberships/$username")
            return when (response.status.value) {
              204 -> Response.NoContent
              403 -> Response.Forbidden(response.body())
              404 -> Response.NotFound(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data object NoContent : Response

            public data class Forbidden(
              public val `value`: BasicError,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class Get internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val username: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.get("/orgs/$org/memberships/$username")
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              403 -> Response.Forbidden(response.body())
              404 -> Response.NotFound(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: OrgMembership,
            ) : Response

            public data class Forbidden(
              public val `value`: BasicError,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class Put internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val username: String,
        ) {
          public suspend operator fun invoke(role: Role? = null): Response {
            val response = client.put("/orgs/$org/memberships/$username") {
              if (role != null) {
                contentType(ContentType.Application.Json)
                setBody(Body(role = role))
              }
            }
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              403 -> Response.Forbidden(response.body())
              422 -> Response.UnprocessableEntity(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          @Serializable
          public enum class Role(
            public val `value`: String,
          ) {
            @SerialName("admin")
            Admin("admin"),
            @SerialName("member")
            Member("member"),
            ;
          }

          @JvmInline
          @Serializable
          internal value class Body(
            public val role: Role? = null,
          )

          public sealed interface Response {
            public data class Ok(
              public val `value`: OrgMembership,
            ) : Response

            public data class Forbidden(
              public val `value`: BasicError,
            ) : Response

            public data class UnprocessableEntity(
              public val `value`: ValidationError,
            ) : Response
          }
        }
      }
    }

    public class Migrations internal constructor(
      private val client: HttpClient,
      private val org: String,
    ) {
      public val `get`: Get = Get(client, org)

      public val post: Post = Post(client, org)

      public fun migrationId(migrationId: Long): MigrationIdPath = MigrationIdPath(client, org, migrationId)

      public class Get internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public suspend operator fun invoke(
          perPage: Long? = 30L,
          page: Long? = 1L,
          exclude: List<Exclude>? = null,
        ): List<Migration> = client.get("/orgs/$org/migrations") {
          perPage?.let { parameter("per_page", it) }
          page?.let { parameter("page", it) }
          exclude?.let { parameter("exclude", it) }
        }.body()

        @Serializable
        public enum class Exclude(
          public val `value`: String,
        ) {
          @SerialName("repositories")
          Repositories("repositories"),
          ;
        }
      }

      public class Post internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public suspend operator fun invoke(
          repositories: List<String>,
          lockRepositories: Boolean? = null,
          excludeMetadata: Boolean? = null,
          excludeGitData: Boolean? = null,
          excludeAttachments: Boolean? = null,
          excludeReleases: Boolean? = null,
          excludeOwnerProjects: Boolean? = null,
          orgMetadataOnly: Boolean? = null,
          exclude: List<Exclude>? = null,
        ): Response {
          val response = client.post("/orgs/$org/migrations") {
            contentType(ContentType.Application.Json)
            setBody(Body(repositories = repositories, lockRepositories = lockRepositories, excludeMetadata = excludeMetadata, excludeGitData = excludeGitData, excludeAttachments = excludeAttachments, excludeReleases = excludeReleases, excludeOwnerProjects = excludeOwnerProjects, orgMetadataOnly = orgMetadataOnly, exclude = exclude))
          }
          return when (response.status.value) {
            201 -> Response.Created(response.body())
            404 -> Response.NotFound(response.body())
            422 -> Response.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        @Serializable
        public enum class Exclude(
          public val `value`: String,
        ) {
          @SerialName("repositories")
          Repositories("repositories"),
          ;
        }

        @Serializable
        internal data class Body(
          public val repositories: List<String>,
          @SerialName("lock_repositories")
          public val lockRepositories: Boolean? = null,
          @SerialName("exclude_metadata")
          public val excludeMetadata: Boolean? = null,
          @SerialName("exclude_git_data")
          public val excludeGitData: Boolean? = null,
          @SerialName("exclude_attachments")
          public val excludeAttachments: Boolean? = null,
          @SerialName("exclude_releases")
          public val excludeReleases: Boolean? = null,
          @SerialName("exclude_owner_projects")
          public val excludeOwnerProjects: Boolean? = null,
          @SerialName("org_metadata_only")
          public val orgMetadataOnly: Boolean? = null,
          public val exclude: List<Exclude>? = null,
        )

        public sealed interface Response {
          public data class Created(
            public val `value`: Migration,
          ) : Response

          public data class NotFound(
            public val `value`: BasicError,
          ) : Response

          public data class UnprocessableEntity(
            public val `value`: ValidationError,
          ) : Response
        }
      }

      public class MigrationIdPath internal constructor(
        private val client: HttpClient,
        private val org: String,
        private val migrationId: Long,
      ) {
        public val `get`: Get = Get(client, org, migrationId)

        public val archive: Archive = Archive(client, org, migrationId)

        public val repos: Repos = Repos(client, org, migrationId)

        public val repositories: Repositories = Repositories(client, org, migrationId)

        public class Get internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val migrationId: Long,
        ) {
          public suspend operator fun invoke(exclude: List<Exclude>? = null): Response {
            val response = client.get("/orgs/$org/migrations/$migrationId") {
              exclude?.let { parameter("exclude", it) }
            }
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              404 -> Response.NotFound(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          @Serializable
          public enum class Exclude(
            public val `value`: String,
          ) {
            @SerialName("repositories")
            Repositories("repositories"),
            ;
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: Migration,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class Archive internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val migrationId: Long,
        ) {
          public val delete: Delete = Delete(client, org, migrationId)

          public val `get`: Get = Get(client, org, migrationId)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val migrationId: Long,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.delete("/orgs/$org/migrations/$migrationId/archive")
              return when (response.status.value) {
                204 -> Response.NoContent
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data object NoContent : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response
            }
          }

          public class Get internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val migrationId: Long,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.get("/orgs/$org/migrations/$migrationId/archive")
              return when (response.status.value) {
                302 -> Response.Found
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data object Found : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response
            }
          }
        }

        public class Repos internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val migrationId: Long,
        ) {
          public fun repoName(repoName: String): RepoNamePath = RepoNamePath(client, org, migrationId, repoName)

          public class RepoNamePath internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val migrationId: Long,
            private val repoName: String,
          ) {
            public val lock: Lock = Lock(client, org, migrationId, repoName)

            public class Lock internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val migrationId: Long,
              private val repoName: String,
            ) {
              public val delete: Delete = Delete(client, org, migrationId, repoName)

              public class Delete internal constructor(
                private val client: HttpClient,
                private val org: String,
                private val migrationId: Long,
                private val repoName: String,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.delete("/orgs/$org/migrations/$migrationId/repos/$repoName/lock")
                  return when (response.status.value) {
                    204 -> Response.NoContent
                    404 -> Response.NotFound(response.body())
                    else -> throw ResponseException(response, "")
                  }
                }

                public sealed interface Response {
                  public data object NoContent : Response

                  public data class NotFound(
                    public val `value`: BasicError,
                  ) : Response
                }
              }
            }
          }
        }

        public class Repositories internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val migrationId: Long,
        ) {
          public val `get`: Get = Get(client, org, migrationId)

          public class Get internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val migrationId: Long,
          ) {
            public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
              val response = client.get("/orgs/$org/migrations/$migrationId/repositories") {
                perPage?.let { parameter("per_page", it) }
                page?.let { parameter("page", it) }
              }
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: List<MinimalRepository>,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response
            }
          }
        }
      }
    }

    public class OrganizationRoles internal constructor(
      private val client: HttpClient,
      private val org: String,
    ) {
      public val `get`: Get = Get(client, org)

      public val teams: Teams = Teams(client, org)

      public val users: Users = Users(client, org)

      public fun roleId(roleId: Long): RoleIdPath = RoleIdPath(client, org, roleId)

      public class Get internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public suspend operator fun invoke(): Response {
          val response = client.get("/orgs/$org/organization-roles")
          return when (response.status.value) {
            200 -> response.body<Response.Ok>()
            404 -> Response.NotFound(response.body())
            422 -> Response.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        public sealed interface Response {
          @Serializable
          public data class Ok(
            @SerialName("total_count")
            public val totalCount: Long? = null,
            public val roles: List<OrganizationRole>? = null,
          ) : Response

          public data class NotFound(
            public val `value`: BasicError,
          ) : Response

          public data class UnprocessableEntity(
            public val `value`: ValidationError,
          ) : Response
        }
      }

      public class Teams internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public fun teamSlug(teamSlug: String): TeamSlugPath = TeamSlugPath(client, org, teamSlug)

        public class TeamSlugPath internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val teamSlug: String,
        ) {
          public val delete: Delete = Delete(client, org, teamSlug)

          public fun roleId(roleId: Long): RoleIdPath = RoleIdPath(client, org, teamSlug, roleId)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val teamSlug: String,
          ) {
            public suspend operator fun invoke() {
              client.delete("/orgs/$org/organization-roles/teams/$teamSlug")
            }
          }

          public class RoleIdPath internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val teamSlug: String,
            private val roleId: Long,
          ) {
            public val delete: Delete = Delete(client, org, teamSlug, roleId)

            public val put: Put = Put(client, org, teamSlug, roleId)

            public class Delete internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val teamSlug: String,
              private val roleId: Long,
            ) {
              public suspend operator fun invoke() {
                client.delete("/orgs/$org/organization-roles/teams/$teamSlug/$roleId")
              }
            }

            public class Put internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val teamSlug: String,
              private val roleId: Long,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.put("/orgs/$org/organization-roles/teams/$teamSlug/$roleId")
                return when (response.status.value) {
                  204 -> Response.NoContent
                  404 -> Response.NotFound
                  422 -> Response.UnprocessableEntity
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data object NoContent : Response

                public data object NotFound : Response

                public data object UnprocessableEntity : Response
              }
            }
          }
        }
      }

      public class Users internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public fun username(username: String): UsernamePath = UsernamePath(client, org, username)

        public class UsernamePath internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val username: String,
        ) {
          public val delete: Delete = Delete(client, org, username)

          public fun roleId(roleId: Long): RoleIdPath = RoleIdPath(client, org, username, roleId)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val username: String,
          ) {
            public suspend operator fun invoke() {
              client.delete("/orgs/$org/organization-roles/users/$username")
            }
          }

          public class RoleIdPath internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val username: String,
            private val roleId: Long,
          ) {
            public val delete: Delete = Delete(client, org, username, roleId)

            public val put: Put = Put(client, org, username, roleId)

            public class Delete internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val username: String,
              private val roleId: Long,
            ) {
              public suspend operator fun invoke() {
                client.delete("/orgs/$org/organization-roles/users/$username/$roleId")
              }
            }

            public class Put internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val username: String,
              private val roleId: Long,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.put("/orgs/$org/organization-roles/users/$username/$roleId")
                return when (response.status.value) {
                  204 -> Response.NoContent
                  404 -> Response.NotFound
                  422 -> Response.UnprocessableEntity
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data object NoContent : Response

                public data object NotFound : Response

                public data object UnprocessableEntity : Response
              }
            }
          }
        }
      }

      public class RoleIdPath internal constructor(
        private val client: HttpClient,
        private val org: String,
        private val roleId: Long,
      ) {
        public val `get`: Get = Get(client, org, roleId)

        public val teams: Teams = Teams(client, org, roleId)

        public val users: Users = Users(client, org, roleId)

        public class Get internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val roleId: Long,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.get("/orgs/$org/organization-roles/$roleId")
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              404 -> Response.NotFound(response.body())
              422 -> Response.UnprocessableEntity(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: OrganizationRole,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response

            public data class UnprocessableEntity(
              public val `value`: ValidationError,
            ) : Response
          }
        }

        public class Teams internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val roleId: Long,
        ) {
          public val `get`: Get = Get(client, org, roleId)

          public class Get internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val roleId: Long,
          ) {
            public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
              val response = client.get("/orgs/$org/organization-roles/$roleId/teams") {
                perPage?.let { parameter("per_page", it) }
                page?.let { parameter("page", it) }
              }
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                404 -> Response.NotFound
                422 -> Response.UnprocessableEntity
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: List<TeamRoleAssignment>,
              ) : Response

              public data object NotFound : Response

              public data object UnprocessableEntity : Response
            }
          }
        }

        public class Users internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val roleId: Long,
        ) {
          public val `get`: Get = Get(client, org, roleId)

          public class Get internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val roleId: Long,
          ) {
            public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
              val response = client.get("/orgs/$org/organization-roles/$roleId/users") {
                perPage?.let { parameter("per_page", it) }
                page?.let { parameter("page", it) }
              }
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                404 -> Response.NotFound
                422 -> Response.UnprocessableEntity
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: List<UserRoleAssignment>,
              ) : Response

              public data object NotFound : Response

              public data object UnprocessableEntity : Response
            }
          }
        }
      }
    }

    public class OutsideCollaborators internal constructor(
      private val client: HttpClient,
      private val org: String,
    ) {
      public val `get`: Get = Get(client, org)

      public fun username(username: String): UsernamePath = UsernamePath(client, org, username)

      public class Get internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public suspend operator fun invoke(
          filter: Filter? = Filter.All,
          perPage: Long? = 30L,
          page: Long? = 1L,
        ): List<SimpleUser> = client.get("/orgs/$org/outside_collaborators") {
          filter?.let { parameter("filter", it.value) }
          perPage?.let { parameter("per_page", it) }
          page?.let { parameter("page", it) }
        }.body()

        @Serializable
        public enum class Filter(
          public val `value`: String,
        ) {
          @SerialName("2fa_disabled")
          `2faDisabled`("2fa_disabled"),
          @SerialName("2fa_insecure")
          `2faInsecure`("2fa_insecure"),
          @SerialName("all")
          All("all"),
          ;
        }
      }

      public class UsernamePath internal constructor(
        private val client: HttpClient,
        private val org: String,
        private val username: String,
      ) {
        public val delete: Delete = Delete(client, org, username)

        public val put: Put = Put(client, org, username)

        public class Delete internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val username: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.delete("/orgs/$org/outside_collaborators/$username")
            return when (response.status.value) {
              204 -> Response.NoContent
              422 -> response.body<Response.UnprocessableEntity>()
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data object NoContent : Response

            @Serializable
            public data class UnprocessableEntity(
              public val message: String? = null,
              @SerialName("documentation_url")
              public val documentationUrl: String? = null,
            ) : Response
          }
        }

        public class Put internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val username: String,
        ) {
          public suspend operator fun invoke(async: Boolean? = null): Response {
            val response = client.put("/orgs/$org/outside_collaborators/$username") {
              if (async != null) {
                contentType(ContentType.Application.Json)
                setBody(Body(async = async))
              }
            }
            return when (response.status.value) {
              202 -> Response.Accepted
              204 -> Response.NoContent
              403 -> Response.Forbidden
              404 -> Response.NotFound(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          @JvmInline
          @Serializable
          internal value class Body(
            public val async: Boolean? = null,
          )

          public sealed interface Response {
            public data object Accepted : Response

            public data object NoContent : Response

            public data object Forbidden : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response
          }
        }
      }
    }

    public class Packages internal constructor(
      private val client: HttpClient,
      private val org: String,
    ) {
      public val `get`: Get = Get(client, org)

      public val npm: Npm = Npm(client, org)

      public val maven: Maven = Maven(client, org)

      public val rubygems: Rubygems = Rubygems(client, org)

      public val docker: Docker = Docker(client, org)

      public val nuget: Nuget = Nuget(client, org)

      public val container: Container = Container(client, org)

      public class Get internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public suspend operator fun invoke(
          packageType: PackageType,
          visibility: Visibility? = null,
          page: Long? = 1L,
          perPage: Long? = 30L,
        ): Response {
          val response = client.get("/orgs/$org/packages") {
            parameter("package_type", packageType.value)
            visibility?.let { parameter("visibility", it.value) }
            page?.let { parameter("page", it) }
            perPage?.let { parameter("per_page", it) }
          }
          return when (response.status.value) {
            200 -> Response.Ok(response.body())
            400 -> Response.BadRequest
            401 -> Response.Unauthorized(response.body())
            403 -> Response.Forbidden(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        @Serializable
        public enum class PackageType(
          public val `value`: String,
        ) {
          @SerialName("npm")
          Npm("npm"),
          @SerialName("maven")
          Maven("maven"),
          @SerialName("rubygems")
          Rubygems("rubygems"),
          @SerialName("docker")
          Docker("docker"),
          @SerialName("nuget")
          Nuget("nuget"),
          @SerialName("container")
          Container("container"),
          ;
        }

        @Serializable
        public enum class Visibility(
          public val `value`: String,
        ) {
          @SerialName("public")
          Public("public"),
          @SerialName("private")
          Private("private"),
          @SerialName("internal")
          Internal("internal"),
          ;
        }

        public sealed interface Response {
          public data class Ok(
            public val `value`: List<Package>,
          ) : Response

          public data object BadRequest : Response

          public data class Unauthorized(
            public val `value`: BasicError,
          ) : Response

          public data class Forbidden(
            public val `value`: BasicError,
          ) : Response
        }
      }

      public class Npm internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public fun packageName(packageName: String): PackageNamePath = PackageNamePath(client, org, packageName)

        public class PackageNamePath internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val packageName: String,
        ) {
          public val delete: Delete = Delete(client, org, packageName)

          public val `get`: Get = Get(client, org, packageName)

          public val restore: Restore = Restore(client, org, packageName)

          public val versions: Versions = Versions(client, org, packageName)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val packageName: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.delete("/orgs/$org/packages/npm/$packageName")
              return when (response.status.value) {
                204 -> Response.NoContent
                401 -> Response.Unauthorized(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data object NoContent : Response

              public data class Unauthorized(
                public val `value`: BasicError,
              ) : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response
            }
          }

          public class Get internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val packageName: String,
          ) {
            public suspend operator fun invoke(): Package = client.get("/orgs/$org/packages/npm/$packageName").body()
          }

          public class Restore internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val packageName: String,
          ) {
            public val post: Post = Post(client, org, packageName)

            public class Post internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val packageName: String,
            ) {
              public suspend operator fun invoke(token: String? = null): Response {
                val response = client.post("/orgs/$org/packages/npm/$packageName/restore") {
                  token?.let { parameter("token", it) }
                }
                return when (response.status.value) {
                  204 -> Response.NoContent
                  401 -> Response.Unauthorized(response.body())
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data object NoContent : Response

                public data class Unauthorized(
                  public val `value`: BasicError,
                ) : Response

                public data class Forbidden(
                  public val `value`: BasicError,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response
              }
            }
          }

          public class Versions internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val packageName: String,
          ) {
            public val `get`: Get = Get(client, org, packageName)

            public fun packageVersionId(packageVersionId: Long): PackageVersionIdPath = PackageVersionIdPath(client, org, packageName, packageVersionId)

            public class Get internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val packageName: String,
            ) {
              public suspend operator fun invoke(
                page: Long? = 1L,
                perPage: Long? = 30L,
                state: State? = State.Active,
              ): Response {
                val response = client.get("/orgs/$org/packages/npm/$packageName/versions") {
                  page?.let { parameter("page", it) }
                  perPage?.let { parameter("per_page", it) }
                  state?.let { parameter("state", it.value) }
                }
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  401 -> Response.Unauthorized(response.body())
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              @Serializable
              public enum class State(
                public val `value`: String,
              ) {
                @SerialName("active")
                Active("active"),
                @SerialName("deleted")
                Deleted("deleted"),
                ;
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: List<PackageVersion>,
                ) : Response

                public data class Unauthorized(
                  public val `value`: BasicError,
                ) : Response

                public data class Forbidden(
                  public val `value`: BasicError,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response
              }
            }

            public class PackageVersionIdPath internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val packageName: String,
              private val packageVersionId: Long,
            ) {
              public val delete: Delete = Delete(client, org, packageName, packageVersionId)

              public val `get`: Get = Get(client, org, packageName, packageVersionId)

              public val restore: Restore = Restore(client, org, packageName, packageVersionId)

              public class Delete internal constructor(
                private val client: HttpClient,
                private val org: String,
                private val packageName: String,
                private val packageVersionId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.delete("/orgs/$org/packages/npm/$packageName/versions/$packageVersionId")
                  return when (response.status.value) {
                    204 -> Response.NoContent
                    401 -> Response.Unauthorized(response.body())
                    403 -> Response.Forbidden(response.body())
                    404 -> Response.NotFound(response.body())
                    else -> throw ResponseException(response, "")
                  }
                }

                public sealed interface Response {
                  public data object NoContent : Response

                  public data class Unauthorized(
                    public val `value`: BasicError,
                  ) : Response

                  public data class Forbidden(
                    public val `value`: BasicError,
                  ) : Response

                  public data class NotFound(
                    public val `value`: BasicError,
                  ) : Response
                }
              }

              public class Get internal constructor(
                private val client: HttpClient,
                private val org: String,
                private val packageName: String,
                private val packageVersionId: Long,
              ) {
                public suspend operator fun invoke(): PackageVersion = client.get("/orgs/$org/packages/npm/$packageName/versions/$packageVersionId").body()
              }

              public class Restore internal constructor(
                private val client: HttpClient,
                private val org: String,
                private val packageName: String,
                private val packageVersionId: Long,
              ) {
                public val post: Post = Post(client, org, packageName, packageVersionId)

                public class Post internal constructor(
                  private val client: HttpClient,
                  private val org: String,
                  private val packageName: String,
                  private val packageVersionId: Long,
                ) {
                  public suspend operator fun invoke(): Response {
                    val response = client.post("/orgs/$org/packages/npm/$packageName/versions/$packageVersionId/restore")
                    return when (response.status.value) {
                      204 -> Response.NoContent
                      401 -> Response.Unauthorized(response.body())
                      403 -> Response.Forbidden(response.body())
                      404 -> Response.NotFound(response.body())
                      else -> throw ResponseException(response, "")
                    }
                  }

                  public sealed interface Response {
                    public data object NoContent : Response

                    public data class Unauthorized(
                      public val `value`: BasicError,
                    ) : Response

                    public data class Forbidden(
                      public val `value`: BasicError,
                    ) : Response

                    public data class NotFound(
                      public val `value`: BasicError,
                    ) : Response
                  }
                }
              }
            }
          }
        }
      }

      public class Maven internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public fun packageName(packageName: String): PackageNamePath = PackageNamePath(client, org, packageName)

        public class PackageNamePath internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val packageName: String,
        ) {
          public val delete: Delete = Delete(client, org, packageName)

          public val `get`: Get = Get(client, org, packageName)

          public val restore: Restore = Restore(client, org, packageName)

          public val versions: Versions = Versions(client, org, packageName)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val packageName: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.delete("/orgs/$org/packages/maven/$packageName")
              return when (response.status.value) {
                204 -> Response.NoContent
                401 -> Response.Unauthorized(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data object NoContent : Response

              public data class Unauthorized(
                public val `value`: BasicError,
              ) : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response
            }
          }

          public class Get internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val packageName: String,
          ) {
            public suspend operator fun invoke(): Package = client.get("/orgs/$org/packages/maven/$packageName").body()
          }

          public class Restore internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val packageName: String,
          ) {
            public val post: Post = Post(client, org, packageName)

            public class Post internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val packageName: String,
            ) {
              public suspend operator fun invoke(token: String? = null): Response {
                val response = client.post("/orgs/$org/packages/maven/$packageName/restore") {
                  token?.let { parameter("token", it) }
                }
                return when (response.status.value) {
                  204 -> Response.NoContent
                  401 -> Response.Unauthorized(response.body())
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data object NoContent : Response

                public data class Unauthorized(
                  public val `value`: BasicError,
                ) : Response

                public data class Forbidden(
                  public val `value`: BasicError,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response
              }
            }
          }

          public class Versions internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val packageName: String,
          ) {
            public val `get`: Get = Get(client, org, packageName)

            public fun packageVersionId(packageVersionId: Long): PackageVersionIdPath = PackageVersionIdPath(client, org, packageName, packageVersionId)

            public class Get internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val packageName: String,
            ) {
              public suspend operator fun invoke(
                page: Long? = 1L,
                perPage: Long? = 30L,
                state: State? = State.Active,
              ): Response {
                val response = client.get("/orgs/$org/packages/maven/$packageName/versions") {
                  page?.let { parameter("page", it) }
                  perPage?.let { parameter("per_page", it) }
                  state?.let { parameter("state", it.value) }
                }
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  401 -> Response.Unauthorized(response.body())
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              @Serializable
              public enum class State(
                public val `value`: String,
              ) {
                @SerialName("active")
                Active("active"),
                @SerialName("deleted")
                Deleted("deleted"),
                ;
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: List<PackageVersion>,
                ) : Response

                public data class Unauthorized(
                  public val `value`: BasicError,
                ) : Response

                public data class Forbidden(
                  public val `value`: BasicError,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response
              }
            }

            public class PackageVersionIdPath internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val packageName: String,
              private val packageVersionId: Long,
            ) {
              public val delete: Delete = Delete(client, org, packageName, packageVersionId)

              public val `get`: Get = Get(client, org, packageName, packageVersionId)

              public val restore: Restore = Restore(client, org, packageName, packageVersionId)

              public class Delete internal constructor(
                private val client: HttpClient,
                private val org: String,
                private val packageName: String,
                private val packageVersionId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.delete("/orgs/$org/packages/maven/$packageName/versions/$packageVersionId")
                  return when (response.status.value) {
                    204 -> Response.NoContent
                    401 -> Response.Unauthorized(response.body())
                    403 -> Response.Forbidden(response.body())
                    404 -> Response.NotFound(response.body())
                    else -> throw ResponseException(response, "")
                  }
                }

                public sealed interface Response {
                  public data object NoContent : Response

                  public data class Unauthorized(
                    public val `value`: BasicError,
                  ) : Response

                  public data class Forbidden(
                    public val `value`: BasicError,
                  ) : Response

                  public data class NotFound(
                    public val `value`: BasicError,
                  ) : Response
                }
              }

              public class Get internal constructor(
                private val client: HttpClient,
                private val org: String,
                private val packageName: String,
                private val packageVersionId: Long,
              ) {
                public suspend operator fun invoke(): PackageVersion = client.get("/orgs/$org/packages/maven/$packageName/versions/$packageVersionId").body()
              }

              public class Restore internal constructor(
                private val client: HttpClient,
                private val org: String,
                private val packageName: String,
                private val packageVersionId: Long,
              ) {
                public val post: Post = Post(client, org, packageName, packageVersionId)

                public class Post internal constructor(
                  private val client: HttpClient,
                  private val org: String,
                  private val packageName: String,
                  private val packageVersionId: Long,
                ) {
                  public suspend operator fun invoke(): Response {
                    val response = client.post("/orgs/$org/packages/maven/$packageName/versions/$packageVersionId/restore")
                    return when (response.status.value) {
                      204 -> Response.NoContent
                      401 -> Response.Unauthorized(response.body())
                      403 -> Response.Forbidden(response.body())
                      404 -> Response.NotFound(response.body())
                      else -> throw ResponseException(response, "")
                    }
                  }

                  public sealed interface Response {
                    public data object NoContent : Response

                    public data class Unauthorized(
                      public val `value`: BasicError,
                    ) : Response

                    public data class Forbidden(
                      public val `value`: BasicError,
                    ) : Response

                    public data class NotFound(
                      public val `value`: BasicError,
                    ) : Response
                  }
                }
              }
            }
          }
        }
      }

      public class Rubygems internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public fun packageName(packageName: String): PackageNamePath = PackageNamePath(client, org, packageName)

        public class PackageNamePath internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val packageName: String,
        ) {
          public val delete: Delete = Delete(client, org, packageName)

          public val `get`: Get = Get(client, org, packageName)

          public val restore: Restore = Restore(client, org, packageName)

          public val versions: Versions = Versions(client, org, packageName)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val packageName: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.delete("/orgs/$org/packages/rubygems/$packageName")
              return when (response.status.value) {
                204 -> Response.NoContent
                401 -> Response.Unauthorized(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data object NoContent : Response

              public data class Unauthorized(
                public val `value`: BasicError,
              ) : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response
            }
          }

          public class Get internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val packageName: String,
          ) {
            public suspend operator fun invoke(): Package = client.get("/orgs/$org/packages/rubygems/$packageName").body()
          }

          public class Restore internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val packageName: String,
          ) {
            public val post: Post = Post(client, org, packageName)

            public class Post internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val packageName: String,
            ) {
              public suspend operator fun invoke(token: String? = null): Response {
                val response = client.post("/orgs/$org/packages/rubygems/$packageName/restore") {
                  token?.let { parameter("token", it) }
                }
                return when (response.status.value) {
                  204 -> Response.NoContent
                  401 -> Response.Unauthorized(response.body())
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data object NoContent : Response

                public data class Unauthorized(
                  public val `value`: BasicError,
                ) : Response

                public data class Forbidden(
                  public val `value`: BasicError,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response
              }
            }
          }

          public class Versions internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val packageName: String,
          ) {
            public val `get`: Get = Get(client, org, packageName)

            public fun packageVersionId(packageVersionId: Long): PackageVersionIdPath = PackageVersionIdPath(client, org, packageName, packageVersionId)

            public class Get internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val packageName: String,
            ) {
              public suspend operator fun invoke(
                page: Long? = 1L,
                perPage: Long? = 30L,
                state: State? = State.Active,
              ): Response {
                val response = client.get("/orgs/$org/packages/rubygems/$packageName/versions") {
                  page?.let { parameter("page", it) }
                  perPage?.let { parameter("per_page", it) }
                  state?.let { parameter("state", it.value) }
                }
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  401 -> Response.Unauthorized(response.body())
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              @Serializable
              public enum class State(
                public val `value`: String,
              ) {
                @SerialName("active")
                Active("active"),
                @SerialName("deleted")
                Deleted("deleted"),
                ;
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: List<PackageVersion>,
                ) : Response

                public data class Unauthorized(
                  public val `value`: BasicError,
                ) : Response

                public data class Forbidden(
                  public val `value`: BasicError,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response
              }
            }

            public class PackageVersionIdPath internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val packageName: String,
              private val packageVersionId: Long,
            ) {
              public val delete: Delete = Delete(client, org, packageName, packageVersionId)

              public val `get`: Get = Get(client, org, packageName, packageVersionId)

              public val restore: Restore = Restore(client, org, packageName, packageVersionId)

              public class Delete internal constructor(
                private val client: HttpClient,
                private val org: String,
                private val packageName: String,
                private val packageVersionId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.delete("/orgs/$org/packages/rubygems/$packageName/versions/$packageVersionId")
                  return when (response.status.value) {
                    204 -> Response.NoContent
                    401 -> Response.Unauthorized(response.body())
                    403 -> Response.Forbidden(response.body())
                    404 -> Response.NotFound(response.body())
                    else -> throw ResponseException(response, "")
                  }
                }

                public sealed interface Response {
                  public data object NoContent : Response

                  public data class Unauthorized(
                    public val `value`: BasicError,
                  ) : Response

                  public data class Forbidden(
                    public val `value`: BasicError,
                  ) : Response

                  public data class NotFound(
                    public val `value`: BasicError,
                  ) : Response
                }
              }

              public class Get internal constructor(
                private val client: HttpClient,
                private val org: String,
                private val packageName: String,
                private val packageVersionId: Long,
              ) {
                public suspend operator fun invoke(): PackageVersion = client.get("/orgs/$org/packages/rubygems/$packageName/versions/$packageVersionId").body()
              }

              public class Restore internal constructor(
                private val client: HttpClient,
                private val org: String,
                private val packageName: String,
                private val packageVersionId: Long,
              ) {
                public val post: Post = Post(client, org, packageName, packageVersionId)

                public class Post internal constructor(
                  private val client: HttpClient,
                  private val org: String,
                  private val packageName: String,
                  private val packageVersionId: Long,
                ) {
                  public suspend operator fun invoke(): Response {
                    val response = client.post("/orgs/$org/packages/rubygems/$packageName/versions/$packageVersionId/restore")
                    return when (response.status.value) {
                      204 -> Response.NoContent
                      401 -> Response.Unauthorized(response.body())
                      403 -> Response.Forbidden(response.body())
                      404 -> Response.NotFound(response.body())
                      else -> throw ResponseException(response, "")
                    }
                  }

                  public sealed interface Response {
                    public data object NoContent : Response

                    public data class Unauthorized(
                      public val `value`: BasicError,
                    ) : Response

                    public data class Forbidden(
                      public val `value`: BasicError,
                    ) : Response

                    public data class NotFound(
                      public val `value`: BasicError,
                    ) : Response
                  }
                }
              }
            }
          }
        }
      }

      public class Docker internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public fun packageName(packageName: String): PackageNamePath = PackageNamePath(client, org, packageName)

        public class PackageNamePath internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val packageName: String,
        ) {
          public val delete: Delete = Delete(client, org, packageName)

          public val `get`: Get = Get(client, org, packageName)

          public val restore: Restore = Restore(client, org, packageName)

          public val versions: Versions = Versions(client, org, packageName)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val packageName: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.delete("/orgs/$org/packages/docker/$packageName")
              return when (response.status.value) {
                204 -> Response.NoContent
                401 -> Response.Unauthorized(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data object NoContent : Response

              public data class Unauthorized(
                public val `value`: BasicError,
              ) : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response
            }
          }

          public class Get internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val packageName: String,
          ) {
            public suspend operator fun invoke(): Package = client.get("/orgs/$org/packages/docker/$packageName").body()
          }

          public class Restore internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val packageName: String,
          ) {
            public val post: Post = Post(client, org, packageName)

            public class Post internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val packageName: String,
            ) {
              public suspend operator fun invoke(token: String? = null): Response {
                val response = client.post("/orgs/$org/packages/docker/$packageName/restore") {
                  token?.let { parameter("token", it) }
                }
                return when (response.status.value) {
                  204 -> Response.NoContent
                  401 -> Response.Unauthorized(response.body())
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data object NoContent : Response

                public data class Unauthorized(
                  public val `value`: BasicError,
                ) : Response

                public data class Forbidden(
                  public val `value`: BasicError,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response
              }
            }
          }

          public class Versions internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val packageName: String,
          ) {
            public val `get`: Get = Get(client, org, packageName)

            public fun packageVersionId(packageVersionId: Long): PackageVersionIdPath = PackageVersionIdPath(client, org, packageName, packageVersionId)

            public class Get internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val packageName: String,
            ) {
              public suspend operator fun invoke(
                page: Long? = 1L,
                perPage: Long? = 30L,
                state: State? = State.Active,
              ): Response {
                val response = client.get("/orgs/$org/packages/docker/$packageName/versions") {
                  page?.let { parameter("page", it) }
                  perPage?.let { parameter("per_page", it) }
                  state?.let { parameter("state", it.value) }
                }
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  401 -> Response.Unauthorized(response.body())
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              @Serializable
              public enum class State(
                public val `value`: String,
              ) {
                @SerialName("active")
                Active("active"),
                @SerialName("deleted")
                Deleted("deleted"),
                ;
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: List<PackageVersion>,
                ) : Response

                public data class Unauthorized(
                  public val `value`: BasicError,
                ) : Response

                public data class Forbidden(
                  public val `value`: BasicError,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response
              }
            }

            public class PackageVersionIdPath internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val packageName: String,
              private val packageVersionId: Long,
            ) {
              public val delete: Delete = Delete(client, org, packageName, packageVersionId)

              public val `get`: Get = Get(client, org, packageName, packageVersionId)

              public val restore: Restore = Restore(client, org, packageName, packageVersionId)

              public class Delete internal constructor(
                private val client: HttpClient,
                private val org: String,
                private val packageName: String,
                private val packageVersionId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.delete("/orgs/$org/packages/docker/$packageName/versions/$packageVersionId")
                  return when (response.status.value) {
                    204 -> Response.NoContent
                    401 -> Response.Unauthorized(response.body())
                    403 -> Response.Forbidden(response.body())
                    404 -> Response.NotFound(response.body())
                    else -> throw ResponseException(response, "")
                  }
                }

                public sealed interface Response {
                  public data object NoContent : Response

                  public data class Unauthorized(
                    public val `value`: BasicError,
                  ) : Response

                  public data class Forbidden(
                    public val `value`: BasicError,
                  ) : Response

                  public data class NotFound(
                    public val `value`: BasicError,
                  ) : Response
                }
              }

              public class Get internal constructor(
                private val client: HttpClient,
                private val org: String,
                private val packageName: String,
                private val packageVersionId: Long,
              ) {
                public suspend operator fun invoke(): PackageVersion = client.get("/orgs/$org/packages/docker/$packageName/versions/$packageVersionId").body()
              }

              public class Restore internal constructor(
                private val client: HttpClient,
                private val org: String,
                private val packageName: String,
                private val packageVersionId: Long,
              ) {
                public val post: Post = Post(client, org, packageName, packageVersionId)

                public class Post internal constructor(
                  private val client: HttpClient,
                  private val org: String,
                  private val packageName: String,
                  private val packageVersionId: Long,
                ) {
                  public suspend operator fun invoke(): Response {
                    val response = client.post("/orgs/$org/packages/docker/$packageName/versions/$packageVersionId/restore")
                    return when (response.status.value) {
                      204 -> Response.NoContent
                      401 -> Response.Unauthorized(response.body())
                      403 -> Response.Forbidden(response.body())
                      404 -> Response.NotFound(response.body())
                      else -> throw ResponseException(response, "")
                    }
                  }

                  public sealed interface Response {
                    public data object NoContent : Response

                    public data class Unauthorized(
                      public val `value`: BasicError,
                    ) : Response

                    public data class Forbidden(
                      public val `value`: BasicError,
                    ) : Response

                    public data class NotFound(
                      public val `value`: BasicError,
                    ) : Response
                  }
                }
              }
            }
          }
        }
      }

      public class Nuget internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public fun packageName(packageName: String): PackageNamePath = PackageNamePath(client, org, packageName)

        public class PackageNamePath internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val packageName: String,
        ) {
          public val delete: Delete = Delete(client, org, packageName)

          public val `get`: Get = Get(client, org, packageName)

          public val restore: Restore = Restore(client, org, packageName)

          public val versions: Versions = Versions(client, org, packageName)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val packageName: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.delete("/orgs/$org/packages/nuget/$packageName")
              return when (response.status.value) {
                204 -> Response.NoContent
                401 -> Response.Unauthorized(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data object NoContent : Response

              public data class Unauthorized(
                public val `value`: BasicError,
              ) : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response
            }
          }

          public class Get internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val packageName: String,
          ) {
            public suspend operator fun invoke(): Package = client.get("/orgs/$org/packages/nuget/$packageName").body()
          }

          public class Restore internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val packageName: String,
          ) {
            public val post: Post = Post(client, org, packageName)

            public class Post internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val packageName: String,
            ) {
              public suspend operator fun invoke(token: String? = null): Response {
                val response = client.post("/orgs/$org/packages/nuget/$packageName/restore") {
                  token?.let { parameter("token", it) }
                }
                return when (response.status.value) {
                  204 -> Response.NoContent
                  401 -> Response.Unauthorized(response.body())
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data object NoContent : Response

                public data class Unauthorized(
                  public val `value`: BasicError,
                ) : Response

                public data class Forbidden(
                  public val `value`: BasicError,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response
              }
            }
          }

          public class Versions internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val packageName: String,
          ) {
            public val `get`: Get = Get(client, org, packageName)

            public fun packageVersionId(packageVersionId: Long): PackageVersionIdPath = PackageVersionIdPath(client, org, packageName, packageVersionId)

            public class Get internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val packageName: String,
            ) {
              public suspend operator fun invoke(
                page: Long? = 1L,
                perPage: Long? = 30L,
                state: State? = State.Active,
              ): Response {
                val response = client.get("/orgs/$org/packages/nuget/$packageName/versions") {
                  page?.let { parameter("page", it) }
                  perPage?.let { parameter("per_page", it) }
                  state?.let { parameter("state", it.value) }
                }
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  401 -> Response.Unauthorized(response.body())
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              @Serializable
              public enum class State(
                public val `value`: String,
              ) {
                @SerialName("active")
                Active("active"),
                @SerialName("deleted")
                Deleted("deleted"),
                ;
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: List<PackageVersion>,
                ) : Response

                public data class Unauthorized(
                  public val `value`: BasicError,
                ) : Response

                public data class Forbidden(
                  public val `value`: BasicError,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response
              }
            }

            public class PackageVersionIdPath internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val packageName: String,
              private val packageVersionId: Long,
            ) {
              public val delete: Delete = Delete(client, org, packageName, packageVersionId)

              public val `get`: Get = Get(client, org, packageName, packageVersionId)

              public val restore: Restore = Restore(client, org, packageName, packageVersionId)

              public class Delete internal constructor(
                private val client: HttpClient,
                private val org: String,
                private val packageName: String,
                private val packageVersionId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.delete("/orgs/$org/packages/nuget/$packageName/versions/$packageVersionId")
                  return when (response.status.value) {
                    204 -> Response.NoContent
                    401 -> Response.Unauthorized(response.body())
                    403 -> Response.Forbidden(response.body())
                    404 -> Response.NotFound(response.body())
                    else -> throw ResponseException(response, "")
                  }
                }

                public sealed interface Response {
                  public data object NoContent : Response

                  public data class Unauthorized(
                    public val `value`: BasicError,
                  ) : Response

                  public data class Forbidden(
                    public val `value`: BasicError,
                  ) : Response

                  public data class NotFound(
                    public val `value`: BasicError,
                  ) : Response
                }
              }

              public class Get internal constructor(
                private val client: HttpClient,
                private val org: String,
                private val packageName: String,
                private val packageVersionId: Long,
              ) {
                public suspend operator fun invoke(): PackageVersion = client.get("/orgs/$org/packages/nuget/$packageName/versions/$packageVersionId").body()
              }

              public class Restore internal constructor(
                private val client: HttpClient,
                private val org: String,
                private val packageName: String,
                private val packageVersionId: Long,
              ) {
                public val post: Post = Post(client, org, packageName, packageVersionId)

                public class Post internal constructor(
                  private val client: HttpClient,
                  private val org: String,
                  private val packageName: String,
                  private val packageVersionId: Long,
                ) {
                  public suspend operator fun invoke(): Response {
                    val response = client.post("/orgs/$org/packages/nuget/$packageName/versions/$packageVersionId/restore")
                    return when (response.status.value) {
                      204 -> Response.NoContent
                      401 -> Response.Unauthorized(response.body())
                      403 -> Response.Forbidden(response.body())
                      404 -> Response.NotFound(response.body())
                      else -> throw ResponseException(response, "")
                    }
                  }

                  public sealed interface Response {
                    public data object NoContent : Response

                    public data class Unauthorized(
                      public val `value`: BasicError,
                    ) : Response

                    public data class Forbidden(
                      public val `value`: BasicError,
                    ) : Response

                    public data class NotFound(
                      public val `value`: BasicError,
                    ) : Response
                  }
                }
              }
            }
          }
        }
      }

      public class Container internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public fun packageName(packageName: String): PackageNamePath = PackageNamePath(client, org, packageName)

        public class PackageNamePath internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val packageName: String,
        ) {
          public val delete: Delete = Delete(client, org, packageName)

          public val `get`: Get = Get(client, org, packageName)

          public val restore: Restore = Restore(client, org, packageName)

          public val versions: Versions = Versions(client, org, packageName)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val packageName: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.delete("/orgs/$org/packages/container/$packageName")
              return when (response.status.value) {
                204 -> Response.NoContent
                401 -> Response.Unauthorized(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data object NoContent : Response

              public data class Unauthorized(
                public val `value`: BasicError,
              ) : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response
            }
          }

          public class Get internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val packageName: String,
          ) {
            public suspend operator fun invoke(): Package = client.get("/orgs/$org/packages/container/$packageName").body()
          }

          public class Restore internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val packageName: String,
          ) {
            public val post: Post = Post(client, org, packageName)

            public class Post internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val packageName: String,
            ) {
              public suspend operator fun invoke(token: String? = null): Response {
                val response = client.post("/orgs/$org/packages/container/$packageName/restore") {
                  token?.let { parameter("token", it) }
                }
                return when (response.status.value) {
                  204 -> Response.NoContent
                  401 -> Response.Unauthorized(response.body())
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data object NoContent : Response

                public data class Unauthorized(
                  public val `value`: BasicError,
                ) : Response

                public data class Forbidden(
                  public val `value`: BasicError,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response
              }
            }
          }

          public class Versions internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val packageName: String,
          ) {
            public val `get`: Get = Get(client, org, packageName)

            public fun packageVersionId(packageVersionId: Long): PackageVersionIdPath = PackageVersionIdPath(client, org, packageName, packageVersionId)

            public class Get internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val packageName: String,
            ) {
              public suspend operator fun invoke(
                page: Long? = 1L,
                perPage: Long? = 30L,
                state: State? = State.Active,
              ): Response {
                val response = client.get("/orgs/$org/packages/container/$packageName/versions") {
                  page?.let { parameter("page", it) }
                  perPage?.let { parameter("per_page", it) }
                  state?.let { parameter("state", it.value) }
                }
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  401 -> Response.Unauthorized(response.body())
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              @Serializable
              public enum class State(
                public val `value`: String,
              ) {
                @SerialName("active")
                Active("active"),
                @SerialName("deleted")
                Deleted("deleted"),
                ;
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: List<PackageVersion>,
                ) : Response

                public data class Unauthorized(
                  public val `value`: BasicError,
                ) : Response

                public data class Forbidden(
                  public val `value`: BasicError,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response
              }
            }

            public class PackageVersionIdPath internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val packageName: String,
              private val packageVersionId: Long,
            ) {
              public val delete: Delete = Delete(client, org, packageName, packageVersionId)

              public val `get`: Get = Get(client, org, packageName, packageVersionId)

              public val restore: Restore = Restore(client, org, packageName, packageVersionId)

              public class Delete internal constructor(
                private val client: HttpClient,
                private val org: String,
                private val packageName: String,
                private val packageVersionId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.delete("/orgs/$org/packages/container/$packageName/versions/$packageVersionId")
                  return when (response.status.value) {
                    204 -> Response.NoContent
                    401 -> Response.Unauthorized(response.body())
                    403 -> Response.Forbidden(response.body())
                    404 -> Response.NotFound(response.body())
                    else -> throw ResponseException(response, "")
                  }
                }

                public sealed interface Response {
                  public data object NoContent : Response

                  public data class Unauthorized(
                    public val `value`: BasicError,
                  ) : Response

                  public data class Forbidden(
                    public val `value`: BasicError,
                  ) : Response

                  public data class NotFound(
                    public val `value`: BasicError,
                  ) : Response
                }
              }

              public class Get internal constructor(
                private val client: HttpClient,
                private val org: String,
                private val packageName: String,
                private val packageVersionId: Long,
              ) {
                public suspend operator fun invoke(): PackageVersion = client.get("/orgs/$org/packages/container/$packageName/versions/$packageVersionId").body()
              }

              public class Restore internal constructor(
                private val client: HttpClient,
                private val org: String,
                private val packageName: String,
                private val packageVersionId: Long,
              ) {
                public val post: Post = Post(client, org, packageName, packageVersionId)

                public class Post internal constructor(
                  private val client: HttpClient,
                  private val org: String,
                  private val packageName: String,
                  private val packageVersionId: Long,
                ) {
                  public suspend operator fun invoke(): Response {
                    val response = client.post("/orgs/$org/packages/container/$packageName/versions/$packageVersionId/restore")
                    return when (response.status.value) {
                      204 -> Response.NoContent
                      401 -> Response.Unauthorized(response.body())
                      403 -> Response.Forbidden(response.body())
                      404 -> Response.NotFound(response.body())
                      else -> throw ResponseException(response, "")
                    }
                  }

                  public sealed interface Response {
                    public data object NoContent : Response

                    public data class Unauthorized(
                      public val `value`: BasicError,
                    ) : Response

                    public data class Forbidden(
                      public val `value`: BasicError,
                    ) : Response

                    public data class NotFound(
                      public val `value`: BasicError,
                    ) : Response
                  }
                }
              }
            }
          }
        }
      }
    }

    public class PersonalAccessTokenRequests internal constructor(
      private val client: HttpClient,
      private val org: String,
    ) {
      public val `get`: Get = Get(client, org)

      public val post: Post = Post(client, org)

      public fun patRequestId(patRequestId: Long): PatRequestIdPath = PatRequestIdPath(client, org, patRequestId)

      public class Get internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public suspend operator fun invoke(
          perPage: Long? = 30L,
          page: Long? = 1L,
          sort: Sort? = Sort.CreatedAt,
          direction: Direction? = Direction.Desc,
          owner: List<String>? = null,
          repository: String? = null,
          permission: String? = null,
          lastUsedBefore: Instant? = null,
          lastUsedAfter: Instant? = null,
          tokenId: List<String>? = null,
        ): Response {
          val response = client.get("/orgs/$org/personal-access-token-requests") {
            perPage?.let { parameter("per_page", it) }
            page?.let { parameter("page", it) }
            sort?.let { parameter("sort", it.value) }
            direction?.let { parameter("direction", it.value) }
            owner?.let { parameter("owner", it) }
            repository?.let { parameter("repository", it) }
            permission?.let { parameter("permission", it) }
            lastUsedBefore?.let { parameter("last_used_before", it.toString()) }
            lastUsedAfter?.let { parameter("last_used_after", it.toString()) }
            tokenId?.let { parameter("token_id", it) }
          }
          return when (response.status.value) {
            200 -> Response.Ok(response.body())
            403 -> Response.Forbidden(response.body())
            404 -> Response.NotFound(response.body())
            422 -> Response.UnprocessableEntity(response.body())
            500 -> Response.InternalServerError(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        @Serializable
        public enum class Sort(
          public val `value`: String,
        ) {
          @SerialName("created_at")
          CreatedAt("created_at"),
          ;
        }

        @Serializable
        public enum class Direction(
          public val `value`: String,
        ) {
          @SerialName("asc")
          Asc("asc"),
          @SerialName("desc")
          Desc("desc"),
          ;
        }

        public sealed interface Response {
          public data class Ok(
            public val `value`: List<OrganizationProgrammaticAccessGrantRequest>,
          ) : Response

          public data class Forbidden(
            public val `value`: BasicError,
          ) : Response

          public data class NotFound(
            public val `value`: BasicError,
          ) : Response

          public data class UnprocessableEntity(
            public val `value`: ValidationError,
          ) : Response

          public data class InternalServerError(
            public val `value`: BasicError,
          ) : Response
        }
      }

      public class Post internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public suspend operator fun invoke(
          patRequestIds: List<Long>? = null,
          action: Action,
          reason: String? = null,
        ): Response {
          val response = client.post("/orgs/$org/personal-access-token-requests") {
            contentType(ContentType.Application.Json)
            setBody(Body(patRequestIds = patRequestIds, action = action, reason = reason))
          }
          return when (response.status.value) {
            202 -> Response.Accepted(response.body())
            403 -> Response.Forbidden(response.body())
            404 -> Response.NotFound(response.body())
            422 -> Response.UnprocessableEntity(response.body())
            500 -> Response.InternalServerError(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        @Serializable
        public enum class Action(
          public val `value`: String,
        ) {
          @SerialName("approve")
          Approve("approve"),
          @SerialName("deny")
          Deny("deny"),
          ;
        }

        @Serializable
        internal data class Body(
          @SerialName("pat_request_ids")
          public val patRequestIds: List<Long>? = null,
          public val action: Action,
          public val reason: String? = null,
        )

        public sealed interface Response {
          public data class Accepted(
            public val `value`: JsonElement,
          ) : Response

          public data class Forbidden(
            public val `value`: BasicError,
          ) : Response

          public data class NotFound(
            public val `value`: BasicError,
          ) : Response

          public data class UnprocessableEntity(
            public val `value`: ValidationError,
          ) : Response

          public data class InternalServerError(
            public val `value`: BasicError,
          ) : Response
        }
      }

      public class PatRequestIdPath internal constructor(
        private val client: HttpClient,
        private val org: String,
        private val patRequestId: Long,
      ) {
        public val post: Post = Post(client, org, patRequestId)

        public val repositories: Repositories = Repositories(client, org, patRequestId)

        public class Post internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val patRequestId: Long,
        ) {
          public suspend operator fun invoke(action: Action, reason: String? = null): Response {
            val response = client.post("/orgs/$org/personal-access-token-requests/$patRequestId") {
              contentType(ContentType.Application.Json)
              setBody(Body(action = action, reason = reason))
            }
            return when (response.status.value) {
              204 -> Response.NoContent
              403 -> Response.Forbidden(response.body())
              404 -> Response.NotFound(response.body())
              422 -> Response.UnprocessableEntity(response.body())
              500 -> Response.InternalServerError(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          @Serializable
          public enum class Action(
            public val `value`: String,
          ) {
            @SerialName("approve")
            Approve("approve"),
            @SerialName("deny")
            Deny("deny"),
            ;
          }

          @Serializable
          internal data class Body(
            public val action: Action,
            public val reason: String? = null,
          )

          public sealed interface Response {
            public data object NoContent : Response

            public data class Forbidden(
              public val `value`: BasicError,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response

            public data class UnprocessableEntity(
              public val `value`: ValidationError,
            ) : Response

            public data class InternalServerError(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class Repositories internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val patRequestId: Long,
        ) {
          public val `get`: Get = Get(client, org, patRequestId)

          public class Get internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val patRequestId: Long,
          ) {
            public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
              val response = client.get("/orgs/$org/personal-access-token-requests/$patRequestId/repositories") {
                perPage?.let { parameter("per_page", it) }
                page?.let { parameter("page", it) }
              }
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                500 -> Response.InternalServerError(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: List<MinimalRepository>,
              ) : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response

              public data class InternalServerError(
                public val `value`: BasicError,
              ) : Response
            }
          }
        }
      }
    }

    public class PersonalAccessTokens internal constructor(
      private val client: HttpClient,
      private val org: String,
    ) {
      public val `get`: Get = Get(client, org)

      public val post: Post = Post(client, org)

      public fun patId(patId: Long): PatIdPath = PatIdPath(client, org, patId)

      public class Get internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public suspend operator fun invoke(
          perPage: Long? = 30L,
          page: Long? = 1L,
          sort: Sort? = Sort.CreatedAt,
          direction: Direction? = Direction.Desc,
          owner: List<String>? = null,
          repository: String? = null,
          permission: String? = null,
          lastUsedBefore: Instant? = null,
          lastUsedAfter: Instant? = null,
          tokenId: List<String>? = null,
        ): Response {
          val response = client.get("/orgs/$org/personal-access-tokens") {
            perPage?.let { parameter("per_page", it) }
            page?.let { parameter("page", it) }
            sort?.let { parameter("sort", it.value) }
            direction?.let { parameter("direction", it.value) }
            owner?.let { parameter("owner", it) }
            repository?.let { parameter("repository", it) }
            permission?.let { parameter("permission", it) }
            lastUsedBefore?.let { parameter("last_used_before", it.toString()) }
            lastUsedAfter?.let { parameter("last_used_after", it.toString()) }
            tokenId?.let { parameter("token_id", it) }
          }
          return when (response.status.value) {
            200 -> Response.Ok(response.body())
            403 -> Response.Forbidden(response.body())
            404 -> Response.NotFound(response.body())
            422 -> Response.UnprocessableEntity(response.body())
            500 -> Response.InternalServerError(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        @Serializable
        public enum class Sort(
          public val `value`: String,
        ) {
          @SerialName("created_at")
          CreatedAt("created_at"),
          ;
        }

        @Serializable
        public enum class Direction(
          public val `value`: String,
        ) {
          @SerialName("asc")
          Asc("asc"),
          @SerialName("desc")
          Desc("desc"),
          ;
        }

        public sealed interface Response {
          public data class Ok(
            public val `value`: List<OrganizationProgrammaticAccessGrant>,
          ) : Response

          public data class Forbidden(
            public val `value`: BasicError,
          ) : Response

          public data class NotFound(
            public val `value`: BasicError,
          ) : Response

          public data class UnprocessableEntity(
            public val `value`: ValidationError,
          ) : Response

          public data class InternalServerError(
            public val `value`: BasicError,
          ) : Response
        }
      }

      public class Post internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public suspend operator fun invoke(action: Action, patIds: List<Long>): Response {
          val response = client.post("/orgs/$org/personal-access-tokens") {
            contentType(ContentType.Application.Json)
            setBody(Body(action = action, patIds = patIds))
          }
          return when (response.status.value) {
            202 -> Response.Accepted(response.body())
            403 -> Response.Forbidden(response.body())
            404 -> Response.NotFound(response.body())
            422 -> Response.UnprocessableEntity(response.body())
            500 -> Response.InternalServerError(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        @Serializable
        public enum class Action(
          public val `value`: String,
        ) {
          @SerialName("revoke")
          Revoke("revoke"),
          ;
        }

        @Serializable
        internal data class Body(
          public val action: Action,
          @SerialName("pat_ids")
          public val patIds: List<Long>,
        )

        public sealed interface Response {
          public data class Accepted(
            public val `value`: JsonElement,
          ) : Response

          public data class Forbidden(
            public val `value`: BasicError,
          ) : Response

          public data class NotFound(
            public val `value`: BasicError,
          ) : Response

          public data class UnprocessableEntity(
            public val `value`: ValidationError,
          ) : Response

          public data class InternalServerError(
            public val `value`: BasicError,
          ) : Response
        }
      }

      public class PatIdPath internal constructor(
        private val client: HttpClient,
        private val org: String,
        private val patId: Long,
      ) {
        public val post: Post = Post(client, org, patId)

        public val repositories: Repositories = Repositories(client, org, patId)

        public class Post internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val patId: Long,
        ) {
          public suspend operator fun invoke(action: Action): Response {
            val response = client.post("/orgs/$org/personal-access-tokens/$patId") {
              contentType(ContentType.Application.Json)
              setBody(Body(action = action))
            }
            return when (response.status.value) {
              204 -> Response.NoContent
              403 -> Response.Forbidden(response.body())
              404 -> Response.NotFound(response.body())
              422 -> Response.UnprocessableEntity(response.body())
              500 -> Response.InternalServerError(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          @Serializable
          public enum class Action(
            public val `value`: String,
          ) {
            @SerialName("revoke")
            Revoke("revoke"),
            ;
          }

          @JvmInline
          @Serializable
          internal value class Body(
            public val action: Action,
          )

          public sealed interface Response {
            public data object NoContent : Response

            public data class Forbidden(
              public val `value`: BasicError,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response

            public data class UnprocessableEntity(
              public val `value`: ValidationError,
            ) : Response

            public data class InternalServerError(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class Repositories internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val patId: Long,
        ) {
          public val `get`: Get = Get(client, org, patId)

          public class Get internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val patId: Long,
          ) {
            public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
              val response = client.get("/orgs/$org/personal-access-tokens/$patId/repositories") {
                perPage?.let { parameter("per_page", it) }
                page?.let { parameter("page", it) }
              }
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                500 -> Response.InternalServerError(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: List<MinimalRepository>,
              ) : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response

              public data class InternalServerError(
                public val `value`: BasicError,
              ) : Response
            }
          }
        }
      }
    }

    public class PrivateRegistries internal constructor(
      private val client: HttpClient,
      private val org: String,
    ) {
      public val `get`: Get = Get(client, org)

      public val post: Post = Post(client, org)

      public val publicKey: PublicKey = PublicKey(client, org)

      public fun secretName(secretName: String): SecretNamePath = SecretNamePath(client, org, secretName)

      public class Get internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
          val response = client.get("/orgs/$org/private-registries") {
            perPage?.let { parameter("per_page", it) }
            page?.let { parameter("page", it) }
          }
          return when (response.status.value) {
            200 -> response.body<Response.Ok>()
            400 -> Response.BadRequest(response.body())
            404 -> Response.NotFound(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        public sealed interface Response {
          @Serializable
          public data class Ok(
            @SerialName("total_count")
            public val totalCount: Long,
            public val configurations: List<OrgPrivateRegistryConfiguration>,
          ) : Response

          public data class BadRequest(
            public val `value`: BasicError,
          ) : Response

          public data class NotFound(
            public val `value`: BasicError,
          ) : Response
        }
      }

      public class Post internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public suspend operator fun invoke(
          registryType: RegistryType,
          url: String,
          username: String? = null,
          replacesBase: Boolean? = null,
          encryptedValue: String,
          keyId: String,
          visibility: Visibility,
          selectedRepositoryIds: List<Long>? = null,
        ): Response {
          val response = client.post("/orgs/$org/private-registries") {
            contentType(ContentType.Application.Json)
            setBody(Body(registryType = registryType, url = url, username = username, replacesBase = replacesBase, encryptedValue = encryptedValue, keyId = keyId, visibility = visibility, selectedRepositoryIds = selectedRepositoryIds))
          }
          return when (response.status.value) {
            201 -> Response.Created(response.body())
            404 -> Response.NotFound(response.body())
            422 -> Response.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        @Serializable
        public enum class RegistryType(
          public val `value`: String,
        ) {
          @SerialName("maven_repository")
          MavenRepository("maven_repository"),
          @SerialName("nuget_feed")
          NugetFeed("nuget_feed"),
          @SerialName("goproxy_server")
          GoproxyServer("goproxy_server"),
          @SerialName("npm_registry")
          NpmRegistry("npm_registry"),
          @SerialName("rubygems_server")
          RubygemsServer("rubygems_server"),
          @SerialName("cargo_registry")
          CargoRegistry("cargo_registry"),
          @SerialName("composer_repository")
          ComposerRepository("composer_repository"),
          @SerialName("docker_registry")
          DockerRegistry("docker_registry"),
          @SerialName("git_source")
          GitSource("git_source"),
          @SerialName("helm_registry")
          HelmRegistry("helm_registry"),
          @SerialName("hex_organization")
          HexOrganization("hex_organization"),
          @SerialName("hex_repository")
          HexRepository("hex_repository"),
          @SerialName("pub_repository")
          PubRepository("pub_repository"),
          @SerialName("python_index")
          PythonIndex("python_index"),
          @SerialName("terraform_registry")
          TerraformRegistry("terraform_registry"),
          ;
        }

        @Serializable
        public enum class Visibility(
          public val `value`: String,
        ) {
          @SerialName("all")
          All("all"),
          @SerialName("private")
          Private("private"),
          @SerialName("selected")
          Selected("selected"),
          ;
        }

        @Serializable
        internal data class Body(
          @SerialName("registry_type")
          public val registryType: RegistryType,
          public val url: String,
          public val username: String? = null,
          @SerialName("replaces_base")
          public val replacesBase: Boolean? = null,
          @SerialName("encrypted_value")
          public val encryptedValue: String,
          @SerialName("key_id")
          public val keyId: String,
          public val visibility: Visibility,
          @SerialName("selected_repository_ids")
          public val selectedRepositoryIds: List<Long>? = null,
        )

        public sealed interface Response {
          public data class Created(
            public val `value`: OrgPrivateRegistryConfigurationWithSelectedRepositories,
          ) : Response

          public data class NotFound(
            public val `value`: BasicError,
          ) : Response

          public data class UnprocessableEntity(
            public val `value`: ValidationError,
          ) : Response
        }
      }

      public class PublicKey internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public val `get`: Get = Get(client, org)

        public class Get internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.get("/orgs/$org/private-registries/public-key")
            return when (response.status.value) {
              200 -> response.body<Response.Ok>()
              404 -> Response.NotFound(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            @Serializable
            public data class Ok(
              @SerialName("key_id")
              public val keyId: String,
              public val key: String,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response
          }
        }
      }

      public class SecretNamePath internal constructor(
        private val client: HttpClient,
        private val org: String,
        private val secretName: String,
      ) {
        public val delete: Delete = Delete(client, org, secretName)

        public val `get`: Get = Get(client, org, secretName)

        public val patch: Patch = Patch(client, org, secretName)

        public class Delete internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val secretName: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.delete("/orgs/$org/private-registries/$secretName")
            return when (response.status.value) {
              204 -> Response.NoContent
              400 -> Response.BadRequest(response.body())
              404 -> Response.NotFound(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data object NoContent : Response

            public data class BadRequest(
              public val `value`: BasicError,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class Get internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val secretName: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.get("/orgs/$org/private-registries/$secretName")
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              404 -> Response.NotFound(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: OrgPrivateRegistryConfiguration,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class Patch internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val secretName: String,
        ) {
          public suspend operator fun invoke(
            registryType: RegistryType? = null,
            url: String? = null,
            username: String? = null,
            replacesBase: Boolean? = null,
            encryptedValue: String? = null,
            keyId: String? = null,
            visibility: Visibility? = null,
            selectedRepositoryIds: List<Long>? = null,
          ): Response {
            val response = client.patch("/orgs/$org/private-registries/$secretName") {
              contentType(ContentType.Application.Json)
              setBody(Body(registryType = registryType, url = url, username = username, replacesBase = replacesBase, encryptedValue = encryptedValue, keyId = keyId, visibility = visibility, selectedRepositoryIds = selectedRepositoryIds))
            }
            return when (response.status.value) {
              204 -> Response.NoContent
              404 -> Response.NotFound(response.body())
              422 -> Response.UnprocessableEntity(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          @Serializable
          public enum class RegistryType(
            public val `value`: String,
          ) {
            @SerialName("maven_repository")
            MavenRepository("maven_repository"),
            @SerialName("nuget_feed")
            NugetFeed("nuget_feed"),
            @SerialName("goproxy_server")
            GoproxyServer("goproxy_server"),
            @SerialName("npm_registry")
            NpmRegistry("npm_registry"),
            @SerialName("rubygems_server")
            RubygemsServer("rubygems_server"),
            @SerialName("cargo_registry")
            CargoRegistry("cargo_registry"),
            @SerialName("composer_repository")
            ComposerRepository("composer_repository"),
            @SerialName("docker_registry")
            DockerRegistry("docker_registry"),
            @SerialName("git_source")
            GitSource("git_source"),
            @SerialName("helm_registry")
            HelmRegistry("helm_registry"),
            @SerialName("hex_organization")
            HexOrganization("hex_organization"),
            @SerialName("hex_repository")
            HexRepository("hex_repository"),
            @SerialName("pub_repository")
            PubRepository("pub_repository"),
            @SerialName("python_index")
            PythonIndex("python_index"),
            @SerialName("terraform_registry")
            TerraformRegistry("terraform_registry"),
            ;
          }

          @Serializable
          public enum class Visibility(
            public val `value`: String,
          ) {
            @SerialName("all")
            All("all"),
            @SerialName("private")
            Private("private"),
            @SerialName("selected")
            Selected("selected"),
            ;
          }

          @Serializable
          internal data class Body(
            @SerialName("registry_type")
            public val registryType: RegistryType? = null,
            public val url: String? = null,
            public val username: String? = null,
            @SerialName("replaces_base")
            public val replacesBase: Boolean? = null,
            @SerialName("encrypted_value")
            public val encryptedValue: String? = null,
            @SerialName("key_id")
            public val keyId: String? = null,
            public val visibility: Visibility? = null,
            @SerialName("selected_repository_ids")
            public val selectedRepositoryIds: List<Long>? = null,
          )

          public sealed interface Response {
            public data object NoContent : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response

            public data class UnprocessableEntity(
              public val `value`: ValidationError,
            ) : Response
          }
        }
      }
    }

    public class ProjectsV2 internal constructor(
      private val client: HttpClient,
      private val org: String,
    ) {
      public val `get`: Get = Get(client, org)

      public fun projectNumber(projectNumber: Long): ProjectNumberPath = ProjectNumberPath(client, org, projectNumber)

      public class Get internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public suspend operator fun invoke(
          q: String? = null,
          before: String? = null,
          after: String? = null,
          perPage: Long? = 30L,
        ): Response {
          val response = client.get("/orgs/$org/projectsV2") {
            q?.let { parameter("q", it) }
            before?.let { parameter("before", it) }
            after?.let { parameter("after", it) }
            perPage?.let { parameter("per_page", it) }
          }
          return when (response.status.value) {
            200 -> Response.Ok(response.body())
            304 -> Response.NotModified
            401 -> Response.Unauthorized(response.body())
            403 -> Response.Forbidden(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        public sealed interface Response {
          public data class Ok(
            public val `value`: List<io.github.model.ProjectsV2>,
          ) : Response

          public data object NotModified : Response

          public data class Unauthorized(
            public val `value`: BasicError,
          ) : Response

          public data class Forbidden(
            public val `value`: BasicError,
          ) : Response
        }
      }

      public class ProjectNumberPath internal constructor(
        private val client: HttpClient,
        private val org: String,
        private val projectNumber: Long,
      ) {
        public val `get`: Get = Get(client, org, projectNumber)

        public val drafts: Drafts = Drafts(client, org, projectNumber)

        public val fields: Fields = Fields(client, org, projectNumber)

        public val items: Items = Items(client, org, projectNumber)

        public val views: Views = Views(client, org, projectNumber)

        public class Get internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val projectNumber: Long,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.get("/orgs/$org/projectsV2/$projectNumber")
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              304 -> Response.NotModified
              401 -> Response.Unauthorized(response.body())
              403 -> Response.Forbidden(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: io.github.model.ProjectsV2,
            ) : Response

            public data object NotModified : Response

            public data class Unauthorized(
              public val `value`: BasicError,
            ) : Response

            public data class Forbidden(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class Drafts internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val projectNumber: Long,
        ) {
          public val post: Post = Post(client, org, projectNumber)

          public class Post internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val projectNumber: Long,
          ) {
            public suspend operator fun invoke(title: String, body: String? = null): Response {
              val response = client.post("/orgs/$org/projectsV2/$projectNumber/drafts") {
                contentType(ContentType.Application.Json)
                setBody(Body(title = title, body = body))
              }
              return when (response.status.value) {
                201 -> Response.Created(response.body())
                304 -> Response.NotModified
                401 -> Response.Unauthorized(response.body())
                403 -> Response.Forbidden(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            @Serializable
            internal data class Body(
              public val title: String,
              public val body: String? = null,
            )

            public sealed interface Response {
              public data class Created(
                public val `value`: ProjectsV2ItemSimple,
              ) : Response

              public data object NotModified : Response

              public data class Unauthorized(
                public val `value`: BasicError,
              ) : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response
            }
          }
        }

        public class Fields internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val projectNumber: Long,
        ) {
          public val `get`: Get = Get(client, org, projectNumber)

          public val post: Post = Post(client, org, projectNumber)

          public fun fieldId(fieldId: Long): FieldIdPath = FieldIdPath(client, org, projectNumber, fieldId)

          public class Get internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val projectNumber: Long,
          ) {
            public suspend operator fun invoke(
              perPage: Long? = 30L,
              before: String? = null,
              after: String? = null,
            ): Response {
              val response = client.get("/orgs/$org/projectsV2/$projectNumber/fields") {
                perPage?.let { parameter("per_page", it) }
                before?.let { parameter("before", it) }
                after?.let { parameter("after", it) }
              }
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                304 -> Response.NotModified
                401 -> Response.Unauthorized(response.body())
                403 -> Response.Forbidden(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: List<ProjectsV2Field>,
              ) : Response

              public data object NotModified : Response

              public data class Unauthorized(
                public val `value`: BasicError,
              ) : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response
            }
          }

          public class Post internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val projectNumber: Long,
          ) {
            public suspend operator fun invoke(body: IssueFieldId): Response {
              val response = client.post("/orgs/$org/projectsV2/$projectNumber/fields") {
                contentType(ContentType.Application.Json)
                setBody(body)
              }
              return when (response.status.value) {
                201 -> Response.Created(response.body())
                304 -> Response.NotModified
                401 -> Response.Unauthorized(response.body())
                403 -> Response.Forbidden(response.body())
                422 -> Response.UnprocessableEntity(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public suspend operator fun invoke(body: NameAndDataType): Response {
              val response = client.post("/orgs/$org/projectsV2/$projectNumber/fields") {
                contentType(ContentType.Application.Json)
                setBody(body)
              }
              return when (response.status.value) {
                201 -> Response.Created(response.body())
                304 -> Response.NotModified
                401 -> Response.Unauthorized(response.body())
                403 -> Response.Forbidden(response.body())
                422 -> Response.UnprocessableEntity(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public suspend operator fun invoke(body: NameAndDataTypeAndSingleSelectOptions): Response {
              val response = client.post("/orgs/$org/projectsV2/$projectNumber/fields") {
                contentType(ContentType.Application.Json)
                setBody(body)
              }
              return when (response.status.value) {
                201 -> Response.Created(response.body())
                304 -> Response.NotModified
                401 -> Response.Unauthorized(response.body())
                403 -> Response.Forbidden(response.body())
                422 -> Response.UnprocessableEntity(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public suspend operator fun invoke(body: NameAndDataTypeAndIterationConfiguration): Response {
              val response = client.post("/orgs/$org/projectsV2/$projectNumber/fields") {
                contentType(ContentType.Application.Json)
                setBody(body)
              }
              return when (response.status.value) {
                201 -> Response.Created(response.body())
                304 -> Response.NotModified
                401 -> Response.Unauthorized(response.body())
                403 -> Response.Forbidden(response.body())
                422 -> Response.UnprocessableEntity(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            @JvmInline
            @Serializable
            public value class IssueFieldId(
              @SerialName("issue_field_id")
              public val issueFieldId: Long,
            )

            @Serializable
            public data class NameAndDataType(
              public val name: String,
              @SerialName("data_type")
              public val dataType: DataType,
            ) {
              @Serializable
              public enum class DataType(
                public val `value`: String,
              ) {
                @SerialName("text")
                Text("text"),
                @SerialName("number")
                Number("number"),
                @SerialName("date")
                Date("date"),
                ;
              }
            }

            @Serializable
            public data class NameAndDataTypeAndSingleSelectOptions(
              public val name: String,
              @SerialName("data_type")
              public val dataType: DataType,
              @SerialName("single_select_options")
              public val singleSelectOptions: List<ProjectsV2FieldSingleSelectOption>,
            ) {
              @Serializable
              public enum class DataType(
                public val `value`: String,
              ) {
                @SerialName("single_select")
                SingleSelect("single_select"),
                ;
              }
            }

            @Serializable
            public data class NameAndDataTypeAndIterationConfiguration(
              public val name: String,
              @SerialName("data_type")
              public val dataType: DataType,
              @SerialName("iteration_configuration")
              public val iterationConfiguration: ProjectsV2FieldIterationConfiguration,
            ) {
              @Serializable
              public enum class DataType(
                public val `value`: String,
              ) {
                @SerialName("iteration")
                Iteration("iteration"),
                ;
              }
            }

            public sealed interface Response {
              public data class Created(
                public val `value`: ProjectsV2Field,
              ) : Response

              public data object NotModified : Response

              public data class Unauthorized(
                public val `value`: BasicError,
              ) : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response

              public data class UnprocessableEntity(
                public val `value`: ValidationError,
              ) : Response
            }
          }

          public class FieldIdPath internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val projectNumber: Long,
            private val fieldId: Long,
          ) {
            public val `get`: Get = Get(client, org, projectNumber, fieldId)

            public class Get internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val projectNumber: Long,
              private val fieldId: Long,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.get("/orgs/$org/projectsV2/$projectNumber/fields/$fieldId")
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  304 -> Response.NotModified
                  401 -> Response.Unauthorized(response.body())
                  403 -> Response.Forbidden(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: ProjectsV2Field,
                ) : Response

                public data object NotModified : Response

                public data class Unauthorized(
                  public val `value`: BasicError,
                ) : Response

                public data class Forbidden(
                  public val `value`: BasicError,
                ) : Response
              }
            }
          }
        }

        public class Items internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val projectNumber: Long,
        ) {
          public val `get`: Get = Get(client, org, projectNumber)

          public val post: Post = Post(client, org, projectNumber)

          public fun itemId(itemId: Long): ItemIdPath = ItemIdPath(client, org, projectNumber, itemId)

          public class Get internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val projectNumber: Long,
          ) {
            public suspend operator fun invoke(
              q: String? = null,
              fields: Fields? = null,
              before: String? = null,
              after: String? = null,
              perPage: Long? = 30L,
            ): Response {
              val response = client.get("/orgs/$org/projectsV2/$projectNumber/items") {
                q?.let { parameter("q", it) }
                fields?.let { parameter("fields", it) }
                before?.let { parameter("before", it) }
                after?.let { parameter("after", it) }
                perPage?.let { parameter("per_page", it) }
              }
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                304 -> Response.NotModified
                401 -> Response.Unauthorized(response.body())
                403 -> Response.Forbidden(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            @Serializable(with = Fields.Serializer::class)
            public sealed interface Fields {
              @Serializable
              @JvmInline
              public value class CaseString(
                public val `value`: String,
              ) : Fields

              @Serializable
              @JvmInline
              public value class CaseStrings(
                public val `value`: List<String>,
              ) : Fields

              public object Serializer : KSerializer<Fields> {
                @OptIn(
                  InternalSerializationApi::class,
                  ExperimentalSerializationApi::class,
                )
                override val descriptor: SerialDescriptor =
                    buildSerialDescriptor("io.github.api.Orgs.OrgPath.ProjectsV2.ProjectNumberPath.Items.Get.Fields", PolymorphicKind.SEALED) {
                  element("CaseString", String.serializer().descriptor)
                  element("CaseStrings", ListSerializer(String.serializer()).descriptor)
                }

                override fun deserialize(decoder: Decoder): Fields {
                  val value = decoder.decodeSerializableValue(JsonElement.serializer())
                  val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                  return json.attemptDeserialize(
                    value,
                    CaseStrings::class to { CaseStrings(decodeFromJsonElement(ListSerializer(String.serializer()), it)) },
                    CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                  )
                }

                override fun serialize(encoder: Encoder, `value`: Fields) {
                  when(value) {
                    is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                    is CaseStrings -> encoder.encodeSerializableValue(ListSerializer(String.serializer()), value.value)
                  }
                }
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: List<ProjectsV2ItemWithContent>,
              ) : Response

              public data object NotModified : Response

              public data class Unauthorized(
                public val `value`: BasicError,
              ) : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response
            }
          }

          public class Post internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val projectNumber: Long,
          ) {
            public suspend operator fun invoke(
              type: Type,
              id: Long? = null,
              owner: String? = null,
              repo: String? = null,
              number: Long? = null,
            ): Response {
              val response = client.post("/orgs/$org/projectsV2/$projectNumber/items") {
                contentType(ContentType.Application.Json)
                setBody(Body(type = type, id = id, owner = owner, repo = repo, number = number))
              }
              return when (response.status.value) {
                201 -> Response.Created(response.body())
                304 -> Response.NotModified
                401 -> Response.Unauthorized(response.body())
                403 -> Response.Forbidden(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            @Serializable
            public enum class Type {
              Issue,
              PullRequest,
            }

            @Serializable
            internal data class Body(
              public val type: Type,
              public val id: Long? = null,
              public val owner: String? = null,
              public val repo: String? = null,
              public val number: Long? = null,
            )

            public sealed interface Response {
              public data class Created(
                public val `value`: ProjectsV2ItemSimple,
              ) : Response

              public data object NotModified : Response

              public data class Unauthorized(
                public val `value`: BasicError,
              ) : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response
            }
          }

          public class ItemIdPath internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val projectNumber: Long,
            private val itemId: Long,
          ) {
            public val delete: Delete = Delete(client, org, projectNumber, itemId)

            public val `get`: Get = Get(client, org, projectNumber, itemId)

            public val patch: Patch = Patch(client, org, projectNumber, itemId)

            public class Delete internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val projectNumber: Long,
              private val itemId: Long,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.delete("/orgs/$org/projectsV2/$projectNumber/items/$itemId")
                return when (response.status.value) {
                  204 -> Response.NoContent
                  401 -> Response.Unauthorized(response.body())
                  403 -> Response.Forbidden(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data object NoContent : Response

                public data class Unauthorized(
                  public val `value`: BasicError,
                ) : Response

                public data class Forbidden(
                  public val `value`: BasicError,
                ) : Response
              }
            }

            public class Get internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val projectNumber: Long,
              private val itemId: Long,
            ) {
              public suspend operator fun invoke(fields: Fields? = null): Response {
                val response = client.get("/orgs/$org/projectsV2/$projectNumber/items/$itemId") {
                  fields?.let { parameter("fields", it) }
                }
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  304 -> Response.NotModified
                  401 -> Response.Unauthorized(response.body())
                  403 -> Response.Forbidden(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              @Serializable(with = Fields.Serializer::class)
              public sealed interface Fields {
                @Serializable
                @JvmInline
                public value class CaseString(
                  public val `value`: String,
                ) : Fields

                @Serializable
                @JvmInline
                public value class CaseStrings(
                  public val `value`: List<String>,
                ) : Fields

                public object Serializer : KSerializer<Fields> {
                  @OptIn(
                    InternalSerializationApi::class,
                    ExperimentalSerializationApi::class,
                  )
                  override val descriptor: SerialDescriptor =
                      buildSerialDescriptor("io.github.api.Orgs.OrgPath.ProjectsV2.ProjectNumberPath.Items.ItemIdPath.Get.Fields", PolymorphicKind.SEALED) {
                    element("CaseString", String.serializer().descriptor)
                    element("CaseStrings", ListSerializer(String.serializer()).descriptor)
                  }

                  override fun deserialize(decoder: Decoder): Fields {
                    val value = decoder.decodeSerializableValue(JsonElement.serializer())
                    val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                    return json.attemptDeserialize(
                      value,
                      CaseStrings::class to { CaseStrings(decodeFromJsonElement(ListSerializer(String.serializer()), it)) },
                      CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                    )
                  }

                  override fun serialize(encoder: Encoder, `value`: Fields) {
                    when(value) {
                      is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                      is CaseStrings -> encoder.encodeSerializableValue(ListSerializer(String.serializer()), value.value)
                    }
                  }
                }
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: ProjectsV2ItemWithContent,
                ) : Response

                public data object NotModified : Response

                public data class Unauthorized(
                  public val `value`: BasicError,
                ) : Response

                public data class Forbidden(
                  public val `value`: BasicError,
                ) : Response
              }
            }

            public class Patch internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val projectNumber: Long,
              private val itemId: Long,
            ) {
              public suspend operator fun invoke(fields: List<Fields>): Response {
                val response = client.patch("/orgs/$org/projectsV2/$projectNumber/items/$itemId") {
                  contentType(ContentType.Application.Json)
                  setBody(Body(fields = fields))
                }
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  401 -> Response.Unauthorized(response.body())
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  422 -> Response.UnprocessableEntity(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              @Serializable
              public data class Fields(
                public val id: Long,
                public val `value`: Value?,
              ) {
                /**
                 * The new value for the field:
                 * - For text, number, and date fields, provide the new value directly.
                 * - For single select and iteration fields, provide the ID of the option or iteration.
                 * - To clear the field, set this to null.
                 */
                @Serializable(with = Value.Serializer::class)
                public sealed interface Value {
                  @Serializable
                  @JvmInline
                  public value class CaseString(
                    public val `value`: String,
                  ) : Value

                  @Serializable
                  @JvmInline
                  public value class CaseDouble(
                    public val `value`: Double,
                  ) : Value

                  public object Serializer : KSerializer<Value> {
                    @OptIn(
                      InternalSerializationApi::class,
                      ExperimentalSerializationApi::class,
                    )
                    override val descriptor: SerialDescriptor =
                        buildSerialDescriptor("io.github.api.Orgs.OrgPath.ProjectsV2.ProjectNumberPath.Items.ItemIdPath.Patch.Fields.Value", PolymorphicKind.SEALED) {
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

                    override fun serialize(encoder: Encoder, `value`: Value) {
                      when(value) {
                        is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                        is CaseDouble -> encoder.encodeSerializableValue(Double.serializer(), value.value)
                      }
                    }
                  }
                }
              }

              @JvmInline
              @Serializable
              internal value class Body(
                public val fields: List<Fields>,
              )

              public sealed interface Response {
                public data class Ok(
                  public val `value`: ProjectsV2ItemWithContent,
                ) : Response

                public data class Unauthorized(
                  public val `value`: BasicError,
                ) : Response

                public data class Forbidden(
                  public val `value`: BasicError,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response

                public data class UnprocessableEntity(
                  public val `value`: ValidationError,
                ) : Response
              }
            }
          }
        }

        public class Views internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val projectNumber: Long,
        ) {
          public val post: Post = Post(client, org, projectNumber)

          public fun viewNumber(viewNumber: Long): ViewNumberPath = ViewNumberPath(client, org, projectNumber, viewNumber)

          public class Post internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val projectNumber: Long,
          ) {
            public suspend operator fun invoke(
              name: String,
              layout: Layout,
              filter: String? = null,
              visibleFields: List<Long>? = null,
            ): Response {
              val response = client.post("/orgs/$org/projectsV2/$projectNumber/views") {
                contentType(ContentType.Application.Json)
                setBody(Body(name = name, layout = layout, filter = filter, visibleFields = visibleFields))
              }
              return when (response.status.value) {
                201 -> Response.Created(response.body())
                304 -> Response.NotModified
                401 -> Response.Unauthorized(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                422 -> Response.UnprocessableEntity(response.body())
                503 -> Response.ServiceUnavailable(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            @Serializable
            public enum class Layout(
              public val `value`: String,
            ) {
              @SerialName("table")
              Table("table"),
              @SerialName("board")
              Board("board"),
              @SerialName("roadmap")
              Roadmap("roadmap"),
              ;
            }

            @Serializable
            internal data class Body(
              public val name: String,
              public val layout: Layout,
              public val filter: String? = null,
              @SerialName("visible_fields")
              public val visibleFields: List<Long>? = null,
            )

            public sealed interface Response {
              public data class Created(
                public val `value`: ProjectsV2View,
              ) : Response

              public data object NotModified : Response

              public data class Unauthorized(
                public val `value`: BasicError,
              ) : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response

              public data class UnprocessableEntity(
                public val `value`: ValidationError,
              ) : Response

              public data class ServiceUnavailable(
                public val `value`: BasicError,
              ) : Response
            }
          }

          public class ViewNumberPath internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val projectNumber: Long,
            private val viewNumber: Long,
          ) {
            public val items: Items = Items(client, org, projectNumber, viewNumber)

            public class Items internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val projectNumber: Long,
              private val viewNumber: Long,
            ) {
              public val `get`: Get = Get(client, org, projectNumber, viewNumber)

              public class Get internal constructor(
                private val client: HttpClient,
                private val org: String,
                private val projectNumber: Long,
                private val viewNumber: Long,
              ) {
                public suspend operator fun invoke(
                  fields: Fields? = null,
                  before: String? = null,
                  after: String? = null,
                  perPage: Long? = 30L,
                ): Response {
                  val response = client.get("/orgs/$org/projectsV2/$projectNumber/views/$viewNumber/items") {
                    fields?.let { parameter("fields", it) }
                    before?.let { parameter("before", it) }
                    after?.let { parameter("after", it) }
                    perPage?.let { parameter("per_page", it) }
                  }
                  return when (response.status.value) {
                    200 -> Response.Ok(response.body())
                    304 -> Response.NotModified
                    401 -> Response.Unauthorized(response.body())
                    403 -> Response.Forbidden(response.body())
                    404 -> Response.NotFound(response.body())
                    else -> throw ResponseException(response, "")
                  }
                }

                @Serializable(with = Fields.Serializer::class)
                public sealed interface Fields {
                  @Serializable
                  @JvmInline
                  public value class CaseString(
                    public val `value`: String,
                  ) : Fields

                  @Serializable
                  @JvmInline
                  public value class CaseStrings(
                    public val `value`: List<String>,
                  ) : Fields

                  public object Serializer : KSerializer<Fields> {
                    @OptIn(
                      InternalSerializationApi::class,
                      ExperimentalSerializationApi::class,
                    )
                    override val descriptor: SerialDescriptor =
                        buildSerialDescriptor("io.github.api.Orgs.OrgPath.ProjectsV2.ProjectNumberPath.Views.ViewNumberPath.Items.Get.Fields", PolymorphicKind.SEALED) {
                      element("CaseString", String.serializer().descriptor)
                      element("CaseStrings", ListSerializer(String.serializer()).descriptor)
                    }

                    override fun deserialize(decoder: Decoder): Fields {
                      val value = decoder.decodeSerializableValue(JsonElement.serializer())
                      val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                      return json.attemptDeserialize(
                        value,
                        CaseStrings::class to { CaseStrings(decodeFromJsonElement(ListSerializer(String.serializer()), it)) },
                        CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                      )
                    }

                    override fun serialize(encoder: Encoder, `value`: Fields) {
                      when(value) {
                        is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                        is CaseStrings -> encoder.encodeSerializableValue(ListSerializer(String.serializer()), value.value)
                      }
                    }
                  }
                }

                public sealed interface Response {
                  public data class Ok(
                    public val `value`: List<ProjectsV2ItemWithContent>,
                  ) : Response

                  public data object NotModified : Response

                  public data class Unauthorized(
                    public val `value`: BasicError,
                  ) : Response

                  public data class Forbidden(
                    public val `value`: BasicError,
                  ) : Response

                  public data class NotFound(
                    public val `value`: BasicError,
                  ) : Response
                }
              }
            }
          }
        }
      }
    }

    public class Properties internal constructor(
      private val client: HttpClient,
      private val org: String,
    ) {
      public val schema: Schema = Schema(client, org)

      public val values: Values = Values(client, org)

      public class Schema internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public val `get`: Get = Get(client, org)

        public val patch: Patch = Patch(client, org)

        public fun customPropertyName(customPropertyName: String): CustomPropertyNamePath = CustomPropertyNamePath(client, org, customPropertyName)

        public class Get internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.get("/orgs/$org/properties/schema")
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              403 -> Response.Forbidden(response.body())
              404 -> Response.NotFound(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: List<CustomProperty>,
            ) : Response

            public data class Forbidden(
              public val `value`: BasicError,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class Patch internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public suspend operator fun invoke(properties: List<CustomProperty>): Response {
            val response = client.patch("/orgs/$org/properties/schema") {
              contentType(ContentType.Application.Json)
              setBody(Body(properties = properties))
            }
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              403 -> Response.Forbidden(response.body())
              404 -> Response.NotFound(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          @JvmInline
          @Serializable
          internal value class Body(
            public val properties: List<CustomProperty>,
          )

          public sealed interface Response {
            public data class Ok(
              public val `value`: List<CustomProperty>,
            ) : Response

            public data class Forbidden(
              public val `value`: BasicError,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class CustomPropertyNamePath internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val customPropertyName: String,
        ) {
          public val delete: Delete = Delete(client, org, customPropertyName)

          public val `get`: Get = Get(client, org, customPropertyName)

          public val put: Put = Put(client, org, customPropertyName)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val customPropertyName: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.delete("/orgs/$org/properties/schema/$customPropertyName")
              return when (response.status.value) {
                204 -> Response.NoContent
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data object NoContent : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response
            }
          }

          public class Get internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val customPropertyName: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.get("/orgs/$org/properties/schema/$customPropertyName")
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: CustomProperty,
              ) : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response
            }
          }

          public class Put internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val customPropertyName: String,
          ) {
            public suspend operator fun invoke(body: CustomPropertySetPayload): Response {
              val response = client.put("/orgs/$org/properties/schema/$customPropertyName") {
                contentType(ContentType.Application.Json)
                setBody(body)
              }
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: CustomProperty,
              ) : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response
            }
          }
        }
      }

      public class Values internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public val `get`: Get = Get(client, org)

        public val patch: Patch = Patch(client, org)

        public class Get internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public suspend operator fun invoke(
            perPage: Long? = 30L,
            page: Long? = 1L,
            repositoryQuery: String? = null,
          ): Response {
            val response = client.get("/orgs/$org/properties/values") {
              perPage?.let { parameter("per_page", it) }
              page?.let { parameter("page", it) }
              repositoryQuery?.let { parameter("repository_query", it) }
            }
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              403 -> Response.Forbidden(response.body())
              404 -> Response.NotFound(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: List<OrgRepoCustomPropertyValues>,
            ) : Response

            public data class Forbidden(
              public val `value`: BasicError,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class Patch internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public suspend operator fun invoke(repositoryNames: List<String>, properties: List<CustomPropertyValue>): Response {
            val response = client.patch("/orgs/$org/properties/values") {
              contentType(ContentType.Application.Json)
              setBody(Body(repositoryNames = repositoryNames, properties = properties))
            }
            return when (response.status.value) {
              204 -> Response.NoContent
              403 -> Response.Forbidden(response.body())
              404 -> Response.NotFound(response.body())
              422 -> Response.UnprocessableEntity(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          @Serializable
          internal data class Body(
            @SerialName("repository_names")
            public val repositoryNames: List<String>,
            public val properties: List<CustomPropertyValue>,
          )

          public sealed interface Response {
            public data object NoContent : Response

            public data class Forbidden(
              public val `value`: BasicError,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response

            public data class UnprocessableEntity(
              public val `value`: ValidationError,
            ) : Response
          }
        }
      }
    }

    public class PublicMembers internal constructor(
      private val client: HttpClient,
      private val org: String,
    ) {
      public val `get`: Get = Get(client, org)

      public fun username(username: String): UsernamePath = UsernamePath(client, org, username)

      public class Get internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<SimpleUser> = client.get("/orgs/$org/public_members") {
          perPage?.let { parameter("per_page", it) }
          page?.let { parameter("page", it) }
        }.body()
      }

      public class UsernamePath internal constructor(
        private val client: HttpClient,
        private val org: String,
        private val username: String,
      ) {
        public val delete: Delete = Delete(client, org, username)

        public val `get`: Get = Get(client, org, username)

        public val put: Put = Put(client, org, username)

        public class Delete internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val username: String,
        ) {
          public suspend operator fun invoke() {
            client.delete("/orgs/$org/public_members/$username")
          }
        }

        public class Get internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val username: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.get("/orgs/$org/public_members/$username")
            return when (response.status.value) {
              204 -> Response.NoContent
              404 -> Response.NotFound
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data object NoContent : Response

            public data object NotFound : Response
          }
        }

        public class Put internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val username: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.put("/orgs/$org/public_members/$username")
            return when (response.status.value) {
              204 -> Response.NoContent
              403 -> Response.Forbidden(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data object NoContent : Response

            public data class Forbidden(
              public val `value`: BasicError,
            ) : Response
          }
        }
      }
    }

    public class Repos internal constructor(
      private val client: HttpClient,
      private val org: String,
    ) {
      public val `get`: Get = Get(client, org)

      public val post: Post = Post(client, org)

      public class Get internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public suspend operator fun invoke(
          type: Type? = Type.All,
          sort: Sort? = Sort.Created,
          direction: Direction? = null,
          perPage: Long? = 30L,
          page: Long? = 1L,
        ): List<MinimalRepository> = client.get("/orgs/$org/repos") {
          type?.let { parameter("type", it.value) }
          sort?.let { parameter("sort", it.value) }
          direction?.let { parameter("direction", it.value) }
          perPage?.let { parameter("per_page", it) }
          page?.let { parameter("page", it) }
        }.body()

        @Serializable
        public enum class Type(
          public val `value`: String,
        ) {
          @SerialName("all")
          All("all"),
          @SerialName("public")
          Public("public"),
          @SerialName("private")
          Private("private"),
          @SerialName("forks")
          Forks("forks"),
          @SerialName("sources")
          Sources("sources"),
          @SerialName("member")
          Member("member"),
          ;
        }

        @Serializable
        public enum class Sort(
          public val `value`: String,
        ) {
          @SerialName("created")
          Created("created"),
          @SerialName("updated")
          Updated("updated"),
          @SerialName("pushed")
          Pushed("pushed"),
          @SerialName("full_name")
          FullName("full_name"),
          ;
        }

        @Serializable
        public enum class Direction(
          public val `value`: String,
        ) {
          @SerialName("asc")
          Asc("asc"),
          @SerialName("desc")
          Desc("desc"),
          ;
        }
      }

      public class Post internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public suspend operator fun invoke(
          name: String,
          description: String? = null,
          homepage: String? = null,
          `private`: Boolean? = null,
          visibility: Visibility? = null,
          hasIssues: Boolean? = null,
          hasProjects: Boolean? = null,
          hasWiki: Boolean? = null,
          hasDownloads: Boolean? = null,
          isTemplate: Boolean? = null,
          teamId: Long? = null,
          autoInit: Boolean? = null,
          gitignoreTemplate: String? = null,
          licenseTemplate: String? = null,
          allowSquashMerge: Boolean? = null,
          allowMergeCommit: Boolean? = null,
          allowRebaseMerge: Boolean? = null,
          allowAutoMerge: Boolean? = null,
          deleteBranchOnMerge: Boolean? = null,
          useSquashPrTitleAsDefault: Boolean? = null,
          squashMergeCommitTitle: SquashMergeCommitTitle? = null,
          squashMergeCommitMessage: SquashMergeCommitMessage? = null,
          mergeCommitTitle: MergeCommitTitle? = null,
          mergeCommitMessage: MergeCommitMessage? = null,
          customProperties: JsonElement? = null,
        ): Response {
          val response = client.post("/orgs/$org/repos") {
            contentType(ContentType.Application.Json)
            setBody(Body(name = name, description = description, homepage = homepage, private = private, visibility = visibility, hasIssues = hasIssues, hasProjects = hasProjects, hasWiki = hasWiki, hasDownloads = hasDownloads, isTemplate = isTemplate, teamId = teamId, autoInit = autoInit, gitignoreTemplate = gitignoreTemplate, licenseTemplate = licenseTemplate, allowSquashMerge = allowSquashMerge, allowMergeCommit = allowMergeCommit, allowRebaseMerge = allowRebaseMerge, allowAutoMerge = allowAutoMerge, deleteBranchOnMerge = deleteBranchOnMerge, useSquashPrTitleAsDefault = useSquashPrTitleAsDefault, squashMergeCommitTitle = squashMergeCommitTitle, squashMergeCommitMessage = squashMergeCommitMessage, mergeCommitTitle = mergeCommitTitle, mergeCommitMessage = mergeCommitMessage, customProperties = customProperties))
          }
          return when (response.status.value) {
            201 -> Response.Created(response.body())
            403 -> Response.Forbidden(response.body())
            422 -> Response.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        @Serializable
        public enum class Visibility(
          public val `value`: String,
        ) {
          @SerialName("public")
          Public("public"),
          @SerialName("private")
          Private("private"),
          ;
        }

        @Serializable
        public enum class SquashMergeCommitTitle(
          public val `value`: String,
        ) {
          @SerialName("PR_TITLE")
          PRTITLE("PR_TITLE"),
          @SerialName("COMMIT_OR_PR_TITLE")
          COMMITORPRTITLE("COMMIT_OR_PR_TITLE"),
          ;
        }

        @Serializable
        public enum class SquashMergeCommitMessage(
          public val `value`: String,
        ) {
          @SerialName("PR_BODY")
          PRBODY("PR_BODY"),
          @SerialName("COMMIT_MESSAGES")
          COMMITMESSAGES("COMMIT_MESSAGES"),
          BLANK("BLANK"),
          ;
        }

        @Serializable
        public enum class MergeCommitTitle(
          public val `value`: String,
        ) {
          @SerialName("PR_TITLE")
          PRTITLE("PR_TITLE"),
          @SerialName("MERGE_MESSAGE")
          MERGEMESSAGE("MERGE_MESSAGE"),
          ;
        }

        @Serializable
        public enum class MergeCommitMessage(
          public val `value`: String,
        ) {
          @SerialName("PR_BODY")
          PRBODY("PR_BODY"),
          @SerialName("PR_TITLE")
          PRTITLE("PR_TITLE"),
          BLANK("BLANK"),
          ;
        }

        @Serializable
        internal data class Body(
          public val name: String,
          public val description: String? = null,
          public val homepage: String? = null,
          public val `private`: Boolean? = null,
          public val visibility: Visibility? = null,
          @SerialName("has_issues")
          public val hasIssues: Boolean? = null,
          @SerialName("has_projects")
          public val hasProjects: Boolean? = null,
          @SerialName("has_wiki")
          public val hasWiki: Boolean? = null,
          @SerialName("has_downloads")
          public val hasDownloads: Boolean? = null,
          @SerialName("is_template")
          public val isTemplate: Boolean? = null,
          @SerialName("team_id")
          public val teamId: Long? = null,
          @SerialName("auto_init")
          public val autoInit: Boolean? = null,
          @SerialName("gitignore_template")
          public val gitignoreTemplate: String? = null,
          @SerialName("license_template")
          public val licenseTemplate: String? = null,
          @SerialName("allow_squash_merge")
          public val allowSquashMerge: Boolean? = null,
          @SerialName("allow_merge_commit")
          public val allowMergeCommit: Boolean? = null,
          @SerialName("allow_rebase_merge")
          public val allowRebaseMerge: Boolean? = null,
          @SerialName("allow_auto_merge")
          public val allowAutoMerge: Boolean? = null,
          @SerialName("delete_branch_on_merge")
          public val deleteBranchOnMerge: Boolean? = null,
          @SerialName("use_squash_pr_title_as_default")
          public val useSquashPrTitleAsDefault: Boolean? = null,
          @SerialName("squash_merge_commit_title")
          public val squashMergeCommitTitle: SquashMergeCommitTitle? = null,
          @SerialName("squash_merge_commit_message")
          public val squashMergeCommitMessage: SquashMergeCommitMessage? = null,
          @SerialName("merge_commit_title")
          public val mergeCommitTitle: MergeCommitTitle? = null,
          @SerialName("merge_commit_message")
          public val mergeCommitMessage: MergeCommitMessage? = null,
          @SerialName("custom_properties")
          public val customProperties: JsonElement? = null,
        )

        public sealed interface Response {
          public data class Created(
            public val `value`: FullRepository,
          ) : Response

          public data class Forbidden(
            public val `value`: BasicError,
          ) : Response

          public data class UnprocessableEntity(
            public val `value`: ValidationError,
          ) : Response
        }
      }
    }

    public class Rulesets internal constructor(
      private val client: HttpClient,
      private val org: String,
    ) {
      public val `get`: Get = Get(client, org)

      public val post: Post = Post(client, org)

      public val ruleSuites: RuleSuites = RuleSuites(client, org)

      public fun rulesetId(rulesetId: Long): RulesetIdPath = RulesetIdPath(client, org, rulesetId)

      public class Get internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public suspend operator fun invoke(
          perPage: Long? = 30L,
          page: Long? = 1L,
          targets: String? = null,
        ): Response {
          val response = client.get("/orgs/$org/rulesets") {
            perPage?.let { parameter("per_page", it) }
            page?.let { parameter("page", it) }
            targets?.let { parameter("targets", it) }
          }
          return when (response.status.value) {
            200 -> Response.Ok(response.body())
            404 -> Response.NotFound(response.body())
            500 -> Response.InternalServerError(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        public sealed interface Response {
          public data class Ok(
            public val `value`: List<RepositoryRuleset>,
          ) : Response

          public data class NotFound(
            public val `value`: BasicError,
          ) : Response

          public data class InternalServerError(
            public val `value`: BasicError,
          ) : Response
        }
      }

      public class Post internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public suspend operator fun invoke(
          name: String,
          target: Target? = null,
          enforcement: RepositoryRuleEnforcement,
          bypassActors: List<RepositoryRulesetBypassActor>? = null,
          conditions: OrgRulesetConditions? = null,
          rules: List<OrgRules>? = null,
        ): Response {
          val response = client.post("/orgs/$org/rulesets") {
            contentType(ContentType.Application.Json)
            setBody(Body(name = name, target = target, enforcement = enforcement, bypassActors = bypassActors, conditions = conditions, rules = rules))
          }
          return when (response.status.value) {
            201 -> Response.Created(response.body())
            404 -> Response.NotFound(response.body())
            422 -> Response.UnprocessableEntity(response.body())
            500 -> Response.InternalServerError(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        @Serializable
        public enum class Target(
          public val `value`: String,
        ) {
          @SerialName("branch")
          Branch("branch"),
          @SerialName("tag")
          Tag("tag"),
          @SerialName("push")
          Push("push"),
          @SerialName("repository")
          Repository("repository"),
          ;
        }

        @Serializable
        internal data class Body(
          public val name: String,
          public val target: Target? = null,
          public val enforcement: RepositoryRuleEnforcement,
          @SerialName("bypass_actors")
          public val bypassActors: List<RepositoryRulesetBypassActor>? = null,
          public val conditions: OrgRulesetConditions? = null,
          public val rules: List<OrgRules>? = null,
        )

        public sealed interface Response {
          public data class Created(
            public val `value`: RepositoryRuleset,
          ) : Response

          public data class NotFound(
            public val `value`: BasicError,
          ) : Response

          public data class UnprocessableEntity(
            public val `value`: ValidationError,
          ) : Response

          public data class InternalServerError(
            public val `value`: BasicError,
          ) : Response
        }
      }

      public class RuleSuites internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public val `get`: Get = Get(client, org)

        public fun ruleSuiteId(ruleSuiteId: Long): RuleSuiteIdPath = RuleSuiteIdPath(client, org, ruleSuiteId)

        public class Get internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public suspend operator fun invoke(
            ref: String? = null,
            repositoryName: String? = null,
            timePeriod: TimePeriod? = TimePeriod.Day,
            actorName: String? = null,
            ruleSuiteResult: RuleSuiteResult? = RuleSuiteResult.All,
            perPage: Long? = 30L,
            page: Long? = 1L,
          ): Response {
            val response = client.get("/orgs/$org/rulesets/rule-suites") {
              ref?.let { parameter("ref", it) }
              repositoryName?.let { parameter("repository_name", it) }
              timePeriod?.let { parameter("time_period", it.value) }
              actorName?.let { parameter("actor_name", it) }
              ruleSuiteResult?.let { parameter("rule_suite_result", it.value) }
              perPage?.let { parameter("per_page", it) }
              page?.let { parameter("page", it) }
            }
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              404 -> Response.NotFound(response.body())
              500 -> Response.InternalServerError(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          @Serializable
          public enum class TimePeriod(
            public val `value`: String,
          ) {
            @SerialName("hour")
            Hour("hour"),
            @SerialName("day")
            Day("day"),
            @SerialName("week")
            Week("week"),
            @SerialName("month")
            Month("month"),
            ;
          }

          @Serializable
          public enum class RuleSuiteResult(
            public val `value`: String,
          ) {
            @SerialName("pass")
            Pass("pass"),
            @SerialName("fail")
            Fail("fail"),
            @SerialName("bypass")
            Bypass("bypass"),
            @SerialName("all")
            All("all"),
            ;
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: io.github.model.RuleSuites,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response

            public data class InternalServerError(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class RuleSuiteIdPath internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val ruleSuiteId: Long,
        ) {
          public val `get`: Get = Get(client, org, ruleSuiteId)

          public class Get internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val ruleSuiteId: Long,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.get("/orgs/$org/rulesets/rule-suites/$ruleSuiteId")
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                404 -> Response.NotFound(response.body())
                500 -> Response.InternalServerError(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: RuleSuite,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response

              public data class InternalServerError(
                public val `value`: BasicError,
              ) : Response
            }
          }
        }
      }

      public class RulesetIdPath internal constructor(
        private val client: HttpClient,
        private val org: String,
        private val rulesetId: Long,
      ) {
        public val delete: Delete = Delete(client, org, rulesetId)

        public val `get`: Get = Get(client, org, rulesetId)

        public val put: Put = Put(client, org, rulesetId)

        public val history: History = History(client, org, rulesetId)

        public class Delete internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val rulesetId: Long,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.delete("/orgs/$org/rulesets/$rulesetId")
            return when (response.status.value) {
              204 -> Response.NoContent
              404 -> Response.NotFound(response.body())
              500 -> Response.InternalServerError(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data object NoContent : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response

            public data class InternalServerError(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class Get internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val rulesetId: Long,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.get("/orgs/$org/rulesets/$rulesetId")
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              404 -> Response.NotFound(response.body())
              500 -> Response.InternalServerError(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: RepositoryRuleset,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response

            public data class InternalServerError(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class Put internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val rulesetId: Long,
        ) {
          public suspend operator fun invoke(
            name: String? = null,
            target: Target? = null,
            enforcement: RepositoryRuleEnforcement? = null,
            bypassActors: List<RepositoryRulesetBypassActor>? = null,
            conditions: OrgRulesetConditions? = null,
            rules: List<OrgRules>? = null,
          ): Response {
            val response = client.put("/orgs/$org/rulesets/$rulesetId") {
              if (name != null || target != null || enforcement != null || bypassActors != null || conditions != null || rules != null) {
                contentType(ContentType.Application.Json)
                setBody(Body(name = name, target = target, enforcement = enforcement, bypassActors = bypassActors, conditions = conditions, rules = rules))
              }
            }
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              404 -> Response.NotFound(response.body())
              422 -> Response.UnprocessableEntity(response.body())
              500 -> Response.InternalServerError(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          @Serializable
          public enum class Target(
            public val `value`: String,
          ) {
            @SerialName("branch")
            Branch("branch"),
            @SerialName("tag")
            Tag("tag"),
            @SerialName("push")
            Push("push"),
            @SerialName("repository")
            Repository("repository"),
            ;
          }

          @Serializable
          internal data class Body(
            public val name: String? = null,
            public val target: Target? = null,
            public val enforcement: RepositoryRuleEnforcement? = null,
            @SerialName("bypass_actors")
            public val bypassActors: List<RepositoryRulesetBypassActor>? = null,
            public val conditions: OrgRulesetConditions? = null,
            public val rules: List<OrgRules>? = null,
          )

          public sealed interface Response {
            public data class Ok(
              public val `value`: RepositoryRuleset,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response

            public data class UnprocessableEntity(
              public val `value`: ValidationError,
            ) : Response

            public data class InternalServerError(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class History internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val rulesetId: Long,
        ) {
          public val `get`: Get = Get(client, org, rulesetId)

          public fun versionId(versionId: Long): VersionIdPath = VersionIdPath(client, org, rulesetId, versionId)

          public class Get internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val rulesetId: Long,
          ) {
            public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
              val response = client.get("/orgs/$org/rulesets/$rulesetId/history") {
                perPage?.let { parameter("per_page", it) }
                page?.let { parameter("page", it) }
              }
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                404 -> Response.NotFound(response.body())
                500 -> Response.InternalServerError(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: List<RulesetVersion>,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response

              public data class InternalServerError(
                public val `value`: BasicError,
              ) : Response
            }
          }

          public class VersionIdPath internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val rulesetId: Long,
            private val versionId: Long,
          ) {
            public val `get`: Get = Get(client, org, rulesetId, versionId)

            public class Get internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val rulesetId: Long,
              private val versionId: Long,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.get("/orgs/$org/rulesets/$rulesetId/history/$versionId")
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  404 -> Response.NotFound(response.body())
                  500 -> Response.InternalServerError(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: RulesetVersionWithState,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response

                public data class InternalServerError(
                  public val `value`: BasicError,
                ) : Response
              }
            }
          }
        }
      }
    }

    public class SecretScanning internal constructor(
      private val client: HttpClient,
      private val org: String,
    ) {
      public val alerts: Alerts = Alerts(client, org)

      public val patternConfigurations: PatternConfigurations = PatternConfigurations(client, org)

      public val enableAll: EnableAll = EnableAll(client, org)

      public val disableAll: DisableAll = DisableAll(client, org)

      public class Alerts internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public val `get`: Get = Get(client, org)

        public class Get internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public suspend operator fun invoke(
            state: State? = null,
            secretType: String? = null,
            resolution: String? = null,
            assignee: String? = null,
            sort: Sort? = Sort.Created,
            direction: Direction? = Direction.Desc,
            page: Long? = 1L,
            perPage: Long? = 30L,
            before: String? = null,
            after: String? = null,
            validity: String? = null,
            isPubliclyLeaked: Boolean? = false,
            isMultiRepo: Boolean? = false,
            hideSecret: Boolean? = false,
          ): Response {
            val response = client.get("/orgs/$org/secret-scanning/alerts") {
              state?.let { parameter("state", it.value) }
              secretType?.let { parameter("secret_type", it) }
              resolution?.let { parameter("resolution", it) }
              assignee?.let { parameter("assignee", it) }
              sort?.let { parameter("sort", it.value) }
              direction?.let { parameter("direction", it.value) }
              page?.let { parameter("page", it) }
              perPage?.let { parameter("per_page", it) }
              before?.let { parameter("before", it) }
              after?.let { parameter("after", it) }
              validity?.let { parameter("validity", it) }
              isPubliclyLeaked?.let { parameter("is_publicly_leaked", it) }
              isMultiRepo?.let { parameter("is_multi_repo", it) }
              hideSecret?.let { parameter("hide_secret", it) }
            }
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              404 -> Response.NotFound(response.body())
              503 -> response.body<Response.ServiceUnavailable>()
              else -> throw ResponseException(response, "")
            }
          }

          @Serializable
          public enum class State(
            public val `value`: String,
          ) {
            @SerialName("open")
            Open("open"),
            @SerialName("resolved")
            Resolved("resolved"),
            ;
          }

          @Serializable
          public enum class Sort(
            public val `value`: String,
          ) {
            @SerialName("created")
            Created("created"),
            @SerialName("updated")
            Updated("updated"),
            ;
          }

          @Serializable
          public enum class Direction(
            public val `value`: String,
          ) {
            @SerialName("asc")
            Asc("asc"),
            @SerialName("desc")
            Desc("desc"),
            ;
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: List<OrganizationSecretScanningAlert>,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response

            @Serializable
            public data class ServiceUnavailable(
              public val code: String? = null,
              public val message: String? = null,
              @SerialName("documentation_url")
              public val documentationUrl: String? = null,
            ) : Response
          }
        }
      }

      public class PatternConfigurations internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public val `get`: Get = Get(client, org)

        public val patch: Patch = Patch(client, org)

        public class Get internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.get("/orgs/$org/secret-scanning/pattern-configurations")
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              403 -> Response.Forbidden(response.body())
              404 -> Response.NotFound(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: SecretScanningPatternConfiguration,
            ) : Response

            public data class Forbidden(
              public val `value`: BasicError,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class Patch internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public suspend operator fun invoke(
            patternConfigVersion: SecretScanningRowVersion? = null,
            providerPatternSettings: List<ProviderPatternSettings>? = null,
            customPatternSettings: List<CustomPatternSettings>? = null,
          ): Response {
            val response = client.patch("/orgs/$org/secret-scanning/pattern-configurations") {
              contentType(ContentType.Application.Json)
              setBody(Body(patternConfigVersion = patternConfigVersion, providerPatternSettings = providerPatternSettings, customPatternSettings = customPatternSettings))
            }
            return when (response.status.value) {
              200 -> response.body<Response.Ok>()
              400 -> Response.BadRequest(response.body())
              403 -> Response.Forbidden(response.body())
              404 -> Response.NotFound(response.body())
              409 -> Response.Conflict(response.body())
              422 -> Response.UnprocessableEntity(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          @Serializable
          public data class ProviderPatternSettings(
            @SerialName("token_type")
            public val tokenType: String? = null,
            @SerialName("push_protection_setting")
            public val pushProtectionSetting: PushProtectionSetting? = null,
          ) {
            @Serializable
            public enum class PushProtectionSetting(
              public val `value`: String,
            ) {
              @SerialName("not-set")
              NotSet("not-set"),
              @SerialName("disabled")
              Disabled("disabled"),
              @SerialName("enabled")
              Enabled("enabled"),
              ;
            }
          }

          @Serializable
          public data class CustomPatternSettings(
            @SerialName("token_type")
            public val tokenType: String? = null,
            @SerialName("custom_pattern_version")
            public val customPatternVersion: SecretScanningRowVersion? = null,
            @SerialName("push_protection_setting")
            public val pushProtectionSetting: PushProtectionSetting? = null,
          ) {
            @Serializable
            public enum class PushProtectionSetting(
              public val `value`: String,
            ) {
              @SerialName("disabled")
              Disabled("disabled"),
              @SerialName("enabled")
              Enabled("enabled"),
              ;
            }
          }

          @Serializable
          internal data class Body(
            @SerialName("pattern_config_version")
            public val patternConfigVersion: SecretScanningRowVersion? = null,
            @SerialName("provider_pattern_settings")
            public val providerPatternSettings: List<ProviderPatternSettings>? = null,
            @SerialName("custom_pattern_settings")
            public val customPatternSettings: List<CustomPatternSettings>? = null,
          )

          public sealed interface Response {
            @JvmInline
            @Serializable
            public value class Ok(
              @SerialName("pattern_config_version")
              public val patternConfigVersion: String? = null,
            ) : Response

            public data class BadRequest(
              public val `value`: BasicError,
            ) : Response

            public data class Forbidden(
              public val `value`: BasicError,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response

            public data class Conflict(
              public val `value`: BasicError,
            ) : Response

            public data class UnprocessableEntity(
              public val `value`: ValidationError,
            ) : Response
          }
        }
      }

      public class EnableAll internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        @Deprecated("Deprecated by the API provider")
        public val post: Post = Post(client, org)

        @Deprecated("Deprecated by the API provider")
        public class Post internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          @Deprecated("Deprecated by the API provider")
          public suspend operator fun invoke(querySuite: QuerySuite? = null): Response {
            val response = client.post("/orgs/$org/secret_scanning/enable_all") {
              if (querySuite != null) {
                contentType(ContentType.Application.Json)
                setBody(Body(querySuite = querySuite))
              }
            }
            return when (response.status.value) {
              204 -> Response.NoContent
              422 -> Response.UnprocessableEntity
              else -> throw ResponseException(response, "")
            }
          }

          @Serializable
          public enum class QuerySuite(
            public val `value`: String,
          ) {
            @SerialName("default")
            Default("default"),
            @SerialName("extended")
            Extended("extended"),
            ;
          }

          @JvmInline
          @Serializable
          internal value class Body(
            @SerialName("query_suite")
            public val querySuite: QuerySuite? = null,
          )

          public sealed interface Response {
            public data object NoContent : Response

            public data object UnprocessableEntity : Response
          }
        }
      }

      public class DisableAll internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        @Deprecated("Deprecated by the API provider")
        public val post: Post = Post(client, org)

        @Deprecated("Deprecated by the API provider")
        public class Post internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          @Deprecated("Deprecated by the API provider")
          public suspend operator fun invoke(querySuite: QuerySuite? = null): Response {
            val response = client.post("/orgs/$org/secret_scanning/disable_all") {
              if (querySuite != null) {
                contentType(ContentType.Application.Json)
                setBody(Body(querySuite = querySuite))
              }
            }
            return when (response.status.value) {
              204 -> Response.NoContent
              422 -> Response.UnprocessableEntity
              else -> throw ResponseException(response, "")
            }
          }

          @Serializable
          public enum class QuerySuite(
            public val `value`: String,
          ) {
            @SerialName("default")
            Default("default"),
            @SerialName("extended")
            Extended("extended"),
            ;
          }

          @JvmInline
          @Serializable
          internal value class Body(
            @SerialName("query_suite")
            public val querySuite: QuerySuite? = null,
          )

          public sealed interface Response {
            public data object NoContent : Response

            public data object UnprocessableEntity : Response
          }
        }
      }
    }

    public class SecurityAdvisories internal constructor(
      private val client: HttpClient,
      private val org: String,
    ) {
      public val `get`: Get = Get(client, org)

      public class Get internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public suspend operator fun invoke(
          direction: Direction? = Direction.Desc,
          sort: Sort? = Sort.Created,
          before: String? = null,
          after: String? = null,
          perPage: Long? = 30L,
          state: State? = null,
        ): Response {
          val response = client.get("/orgs/$org/security-advisories") {
            direction?.let { parameter("direction", it.value) }
            sort?.let { parameter("sort", it.value) }
            before?.let { parameter("before", it) }
            after?.let { parameter("after", it) }
            perPage?.let { parameter("per_page", it) }
            state?.let { parameter("state", it.value) }
          }
          return when (response.status.value) {
            200 -> Response.Ok(response.body())
            400 -> Response.BadRequest(response.body())
            404 -> Response.NotFound(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        @Serializable
        public enum class Direction(
          public val `value`: String,
        ) {
          @SerialName("asc")
          Asc("asc"),
          @SerialName("desc")
          Desc("desc"),
          ;
        }

        @Serializable
        public enum class Sort(
          public val `value`: String,
        ) {
          @SerialName("created")
          Created("created"),
          @SerialName("updated")
          Updated("updated"),
          @SerialName("published")
          Published("published"),
          ;
        }

        @Serializable
        public enum class State(
          public val `value`: String,
        ) {
          @SerialName("triage")
          Triage("triage"),
          @SerialName("draft")
          Draft("draft"),
          @SerialName("published")
          Published("published"),
          @SerialName("closed")
          Closed("closed"),
          ;
        }

        public sealed interface Response {
          public data class Ok(
            public val `value`: List<RepositoryAdvisory>,
          ) : Response

          public data class BadRequest(
            public val `value`: BasicError,
          ) : Response

          public data class NotFound(
            public val `value`: BasicError,
          ) : Response
        }
      }
    }

    public class SecurityManagers internal constructor(
      private val client: HttpClient,
      private val org: String,
    ) {
      @Deprecated("Deprecated by the API provider")
      public val `get`: Get = Get(client, org)

      public val teams: Teams = Teams(client, org)

      @Deprecated("Deprecated by the API provider")
      public class Get internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        @Deprecated("Deprecated by the API provider")
        public suspend operator fun invoke(): List<TeamSimple> = client.get("/orgs/$org/security-managers").body()
      }

      public class Teams internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public fun teamSlug(teamSlug: String): TeamSlugPath = TeamSlugPath(client, org, teamSlug)

        public class TeamSlugPath internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val teamSlug: String,
        ) {
          @Deprecated("Deprecated by the API provider")
          public val delete: Delete = Delete(client, org, teamSlug)

          @Deprecated("Deprecated by the API provider")
          public val put: Put = Put(client, org, teamSlug)

          @Deprecated("Deprecated by the API provider")
          public class Delete internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val teamSlug: String,
          ) {
            @Deprecated("Deprecated by the API provider")
            public suspend operator fun invoke() {
              client.delete("/orgs/$org/security-managers/teams/$teamSlug")
            }
          }

          @Deprecated("Deprecated by the API provider")
          public class Put internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val teamSlug: String,
          ) {
            @Deprecated("Deprecated by the API provider")
            public suspend operator fun invoke() {
              client.put("/orgs/$org/security-managers/teams/$teamSlug")
            }
          }
        }
      }
    }

    public class Settings internal constructor(
      private val client: HttpClient,
      private val org: String,
    ) {
      public val immutableReleases: ImmutableReleases = ImmutableReleases(client, org)

      public val networkConfigurations: NetworkConfigurations = NetworkConfigurations(client, org)

      public val networkSettings: NetworkSettings = NetworkSettings(client, org)

      public class ImmutableReleases internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public val `get`: Get = Get(client, org)

        public val put: Put = Put(client, org)

        public val repositories: Repositories = Repositories(client, org)

        public class Get internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public suspend operator fun invoke(): ImmutableReleasesOrganizationSettings = client.get("/orgs/$org/settings/immutable-releases").body()
        }

        public class Put internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public suspend operator fun invoke(enforcedRepositories: EnforcedRepositories, selectedRepositoryIds: List<Long>? = null) {
            client.put("/orgs/$org/settings/immutable-releases") {
              contentType(ContentType.Application.Json)
              setBody(Body(enforcedRepositories = enforcedRepositories, selectedRepositoryIds = selectedRepositoryIds))
            }
          }

          @Serializable
          public enum class EnforcedRepositories(
            public val `value`: String,
          ) {
            @SerialName("all")
            All("all"),
            @SerialName("none")
            None("none"),
            @SerialName("selected")
            Selected("selected"),
            ;
          }

          @Serializable
          internal data class Body(
            @SerialName("enforced_repositories")
            public val enforcedRepositories: EnforcedRepositories,
            @SerialName("selected_repository_ids")
            public val selectedRepositoryIds: List<Long>? = null,
          )
        }

        public class Repositories internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public val `get`: Get = Get(client, org)

          public val put: Put = Put(client, org)

          public fun repositoryId(repositoryId: Long): RepositoryIdPath = RepositoryIdPath(client, org, repositoryId)

          public class Get internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public suspend operator fun invoke(page: Long? = 1L, perPage: Long? = 30L): Response = client.get("/orgs/$org/settings/immutable-releases/repositories") {
              page?.let { parameter("page", it) }
              perPage?.let { parameter("per_page", it) }
            }.body()

            @Serializable
            public data class Response(
              @SerialName("total_count")
              public val totalCount: Long,
              public val repositories: List<MinimalRepository>,
            )
          }

          public class Put internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public suspend operator fun invoke(selectedRepositoryIds: List<Long>) {
              client.put("/orgs/$org/settings/immutable-releases/repositories") {
                contentType(ContentType.Application.Json)
                setBody(Body(selectedRepositoryIds = selectedRepositoryIds))
              }
            }

            @JvmInline
            @Serializable
            internal value class Body(
              @SerialName("selected_repository_ids")
              public val selectedRepositoryIds: List<Long>,
            )
          }

          public class RepositoryIdPath internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val repositoryId: Long,
          ) {
            public val delete: Delete = Delete(client, org, repositoryId)

            public val put: Put = Put(client, org, repositoryId)

            public class Delete internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val repositoryId: Long,
            ) {
              public suspend operator fun invoke() {
                client.delete("/orgs/$org/settings/immutable-releases/repositories/$repositoryId")
              }
            }

            public class Put internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val repositoryId: Long,
            ) {
              public suspend operator fun invoke() {
                client.put("/orgs/$org/settings/immutable-releases/repositories/$repositoryId")
              }
            }
          }
        }
      }

      public class NetworkConfigurations internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public val `get`: Get = Get(client, org)

        public val post: Post = Post(client, org)

        public fun networkConfigurationId(networkConfigurationId: String): NetworkConfigurationIdPath = NetworkConfigurationIdPath(client, org, networkConfigurationId)

        public class Get internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response = client.get("/orgs/$org/settings/network-configurations") {
            perPage?.let { parameter("per_page", it) }
            page?.let { parameter("page", it) }
          }.body()

          @Serializable
          public data class Response(
            @SerialName("total_count")
            public val totalCount: Long,
            @SerialName("network_configurations")
            public val networkConfigurations: List<NetworkConfiguration>,
          )
        }

        public class Post internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public suspend operator fun invoke(
            name: String,
            computeService: ComputeService? = null,
            networkSettingsIds: List<String>,
          ): NetworkConfiguration = client.post("/orgs/$org/settings/network-configurations") {
            contentType(ContentType.Application.Json)
            setBody(Body(name = name, computeService = computeService, networkSettingsIds = networkSettingsIds))
          }.body()

          @Serializable
          public enum class ComputeService(
            public val `value`: String,
          ) {
            @SerialName("none")
            None("none"),
            @SerialName("actions")
            Actions("actions"),
            ;
          }

          @Serializable
          internal data class Body(
            public val name: String,
            @SerialName("compute_service")
            public val computeService: ComputeService? = null,
            @SerialName("network_settings_ids")
            public val networkSettingsIds: List<String>,
          )
        }

        public class NetworkConfigurationIdPath internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val networkConfigurationId: String,
        ) {
          public val delete: Delete = Delete(client, org, networkConfigurationId)

          public val `get`: Get = Get(client, org, networkConfigurationId)

          public val patch: Patch = Patch(client, org, networkConfigurationId)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val networkConfigurationId: String,
          ) {
            public suspend operator fun invoke() {
              client.delete("/orgs/$org/settings/network-configurations/$networkConfigurationId")
            }
          }

          public class Get internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val networkConfigurationId: String,
          ) {
            public suspend operator fun invoke(): NetworkConfiguration = client.get("/orgs/$org/settings/network-configurations/$networkConfigurationId").body()
          }

          public class Patch internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val networkConfigurationId: String,
          ) {
            public suspend operator fun invoke(
              name: String? = null,
              computeService: ComputeService? = null,
              networkSettingsIds: List<String>? = null,
            ): NetworkConfiguration = client.patch("/orgs/$org/settings/network-configurations/$networkConfigurationId") {
              contentType(ContentType.Application.Json)
              setBody(Body(name = name, computeService = computeService, networkSettingsIds = networkSettingsIds))
            }.body()

            @Serializable
            public enum class ComputeService(
              public val `value`: String,
            ) {
              @SerialName("none")
              None("none"),
              @SerialName("actions")
              Actions("actions"),
              ;
            }

            @Serializable
            internal data class Body(
              public val name: String? = null,
              @SerialName("compute_service")
              public val computeService: ComputeService? = null,
              @SerialName("network_settings_ids")
              public val networkSettingsIds: List<String>? = null,
            )
          }
        }
      }

      public class NetworkSettings internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public fun networkSettingsId(networkSettingsId: String): NetworkSettingsIdPath = NetworkSettingsIdPath(client, org, networkSettingsId)

        public class NetworkSettingsIdPath internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val networkSettingsId: String,
        ) {
          public val `get`: Get = Get(client, org, networkSettingsId)

          public class Get internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val networkSettingsId: String,
          ) {
            public suspend operator fun invoke(): io.github.model.NetworkSettings = client.get("/orgs/$org/settings/network-settings/$networkSettingsId").body()
          }
        }
      }
    }

    public class Team internal constructor(
      private val client: HttpClient,
      private val org: String,
    ) {
      public fun teamSlug(teamSlug: String): TeamSlugPath = TeamSlugPath(client, org, teamSlug)

      public class TeamSlugPath internal constructor(
        private val client: HttpClient,
        private val org: String,
        private val teamSlug: String,
      ) {
        public val copilot: Copilot = Copilot(client, org, teamSlug)

        public class Copilot internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val teamSlug: String,
        ) {
          public val metrics: Metrics = Metrics(client, org, teamSlug)

          public class Metrics internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val teamSlug: String,
          ) {
            public val `get`: Get = Get(client, org, teamSlug)

            public class Get internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val teamSlug: String,
            ) {
              public suspend operator fun invoke(
                since: String? = null,
                until: String? = null,
                page: Long? = 1L,
                perPage: Long? = 100L,
              ): Response {
                val response = client.get("/orgs/$org/team/$teamSlug/copilot/metrics") {
                  since?.let { parameter("since", it) }
                  until?.let { parameter("until", it) }
                  page?.let { parameter("page", it) }
                  perPage?.let { parameter("per_page", it) }
                }
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  422 -> Response.UnprocessableEntity(response.body())
                  500 -> Response.InternalServerError(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: List<CopilotUsageMetricsDay>,
                ) : Response

                public data class Forbidden(
                  public val `value`: BasicError,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response

                public data class UnprocessableEntity(
                  public val `value`: BasicError,
                ) : Response

                public data class InternalServerError(
                  public val `value`: BasicError,
                ) : Response
              }
            }
          }
        }
      }
    }

    public class Teams internal constructor(
      private val client: HttpClient,
      private val org: String,
    ) {
      public val `get`: Get = Get(client, org)

      public val post: Post = Post(client, org)

      public fun teamSlug(teamSlug: String): TeamSlugPath = TeamSlugPath(client, org, teamSlug)

      public class Get internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public suspend operator fun invoke(
          perPage: Long? = 30L,
          page: Long? = 1L,
          teamType: TeamType? = TeamType.All,
        ): Response {
          val response = client.get("/orgs/$org/teams") {
            perPage?.let { parameter("per_page", it) }
            page?.let { parameter("page", it) }
            teamType?.let { parameter("team_type", it.value) }
          }
          return when (response.status.value) {
            200 -> Response.Ok(response.body())
            403 -> Response.Forbidden(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        @Serializable
        public enum class TeamType(
          public val `value`: String,
        ) {
          @SerialName("all")
          All("all"),
          @SerialName("enterprise")
          Enterprise("enterprise"),
          @SerialName("organization")
          Organization("organization"),
          ;
        }

        public sealed interface Response {
          public data class Ok(
            public val `value`: List<io.github.model.Team>,
          ) : Response

          public data class Forbidden(
            public val `value`: BasicError,
          ) : Response
        }
      }

      public class Post internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public suspend operator fun invoke(
          name: String,
          description: String? = null,
          maintainers: List<String>? = null,
          repoNames: List<String>? = null,
          privacy: Privacy? = null,
          notificationSetting: NotificationSetting? = null,
          permission: Permission? = null,
          parentTeamId: Long? = null,
        ): Response {
          val response = client.post("/orgs/$org/teams") {
            contentType(ContentType.Application.Json)
            setBody(Body(name = name, description = description, maintainers = maintainers, repoNames = repoNames, privacy = privacy, notificationSetting = notificationSetting, permission = permission, parentTeamId = parentTeamId))
          }
          return when (response.status.value) {
            201 -> Response.Created(response.body())
            403 -> Response.Forbidden(response.body())
            422 -> Response.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        @Serializable
        public enum class Privacy(
          public val `value`: String,
        ) {
          @SerialName("secret")
          Secret("secret"),
          @SerialName("closed")
          Closed("closed"),
          ;
        }

        @Serializable
        public enum class NotificationSetting(
          public val `value`: String,
        ) {
          @SerialName("notifications_enabled")
          NotificationsEnabled("notifications_enabled"),
          @SerialName("notifications_disabled")
          NotificationsDisabled("notifications_disabled"),
          ;
        }

        @Serializable
        public enum class Permission(
          public val `value`: String,
        ) {
          @SerialName("pull")
          Pull("pull"),
          @SerialName("push")
          Push("push"),
          ;
        }

        @Serializable
        internal data class Body(
          public val name: String,
          public val description: String? = null,
          public val maintainers: List<String>? = null,
          @SerialName("repo_names")
          public val repoNames: List<String>? = null,
          public val privacy: Privacy? = null,
          @SerialName("notification_setting")
          public val notificationSetting: NotificationSetting? = null,
          public val permission: Permission? = null,
          @SerialName("parent_team_id")
          public val parentTeamId: Long? = null,
        )

        public sealed interface Response {
          public data class Created(
            public val `value`: TeamFull,
          ) : Response

          public data class Forbidden(
            public val `value`: BasicError,
          ) : Response

          public data class UnprocessableEntity(
            public val `value`: ValidationError,
          ) : Response
        }
      }

      public class TeamSlugPath internal constructor(
        private val client: HttpClient,
        private val org: String,
        private val teamSlug: String,
      ) {
        public val delete: Delete = Delete(client, org, teamSlug)

        public val `get`: Get = Get(client, org, teamSlug)

        public val patch: Patch = Patch(client, org, teamSlug)

        public val invitations: Invitations = Invitations(client, org, teamSlug)

        public val members: Members = Members(client, org, teamSlug)

        public val memberships: Memberships = Memberships(client, org, teamSlug)

        public val repos: Repos = Repos(client, org, teamSlug)

        public val teams: Teams = Teams(client, org, teamSlug)

        public class Delete internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val teamSlug: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.delete("/orgs/$org/teams/$teamSlug")
            return when (response.status.value) {
              204 -> Response.NoContent
              422 -> Response.UnprocessableEntity
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data object NoContent : Response

            public data object UnprocessableEntity : Response
          }
        }

        public class Get internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val teamSlug: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.get("/orgs/$org/teams/$teamSlug")
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              404 -> Response.NotFound(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: TeamFull,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class Patch internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val teamSlug: String,
        ) {
          public suspend operator fun invoke(
            name: String? = null,
            description: String? = null,
            privacy: Privacy? = null,
            notificationSetting: NotificationSetting? = null,
            permission: Permission? = null,
            parentTeamId: Long? = null,
          ): Response {
            val response = client.patch("/orgs/$org/teams/$teamSlug") {
              if (name != null || description != null || privacy != null || notificationSetting != null || permission != null || parentTeamId != null) {
                contentType(ContentType.Application.Json)
                setBody(Body(name = name, description = description, privacy = privacy, notificationSetting = notificationSetting, permission = permission, parentTeamId = parentTeamId))
              }
            }
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              201 -> Response.Created(response.body())
              403 -> Response.Forbidden(response.body())
              404 -> Response.NotFound(response.body())
              422 -> Response.UnprocessableEntity(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          @Serializable
          public enum class Privacy(
            public val `value`: String,
          ) {
            @SerialName("secret")
            Secret("secret"),
            @SerialName("closed")
            Closed("closed"),
            ;
          }

          @Serializable
          public enum class NotificationSetting(
            public val `value`: String,
          ) {
            @SerialName("notifications_enabled")
            NotificationsEnabled("notifications_enabled"),
            @SerialName("notifications_disabled")
            NotificationsDisabled("notifications_disabled"),
            ;
          }

          @Serializable
          public enum class Permission(
            public val `value`: String,
          ) {
            @SerialName("pull")
            Pull("pull"),
            @SerialName("push")
            Push("push"),
            @SerialName("admin")
            Admin("admin"),
            ;
          }

          @Serializable
          internal data class Body(
            public val name: String? = null,
            public val description: String? = null,
            public val privacy: Privacy? = null,
            @SerialName("notification_setting")
            public val notificationSetting: NotificationSetting? = null,
            public val permission: Permission? = null,
            @SerialName("parent_team_id")
            public val parentTeamId: Long? = null,
          )

          public sealed interface Response {
            public data class Ok(
              public val `value`: TeamFull,
            ) : Response

            public data class Created(
              public val `value`: TeamFull,
            ) : Response

            public data class Forbidden(
              public val `value`: BasicError,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response

            public data class UnprocessableEntity(
              public val `value`: ValidationError,
            ) : Response
          }
        }

        public class Invitations internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val teamSlug: String,
        ) {
          public val `get`: Get = Get(client, org, teamSlug)

          public class Get internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val teamSlug: String,
          ) {
            public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
              val response = client.get("/orgs/$org/teams/$teamSlug/invitations") {
                perPage?.let { parameter("per_page", it) }
                page?.let { parameter("page", it) }
              }
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                422 -> Response.UnprocessableEntity
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: List<OrganizationInvitation>,
              ) : Response

              public data object UnprocessableEntity : Response
            }
          }
        }

        public class Members internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val teamSlug: String,
        ) {
          public val `get`: Get = Get(client, org, teamSlug)

          public class Get internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val teamSlug: String,
          ) {
            public suspend operator fun invoke(
              role: Role? = Role.All,
              perPage: Long? = 30L,
              page: Long? = 1L,
            ): List<SimpleUser> = client.get("/orgs/$org/teams/$teamSlug/members") {
              role?.let { parameter("role", it.value) }
              perPage?.let { parameter("per_page", it) }
              page?.let { parameter("page", it) }
            }.body()

            @Serializable
            public enum class Role(
              public val `value`: String,
            ) {
              @SerialName("member")
              Member("member"),
              @SerialName("maintainer")
              Maintainer("maintainer"),
              @SerialName("all")
              All("all"),
              ;
            }
          }
        }

        public class Memberships internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val teamSlug: String,
        ) {
          public fun username(username: String): UsernamePath = UsernamePath(client, org, teamSlug, username)

          public class UsernamePath internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val teamSlug: String,
            private val username: String,
          ) {
            public val delete: Delete = Delete(client, org, teamSlug, username)

            public val `get`: Get = Get(client, org, teamSlug, username)

            public val put: Put = Put(client, org, teamSlug, username)

            public class Delete internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val teamSlug: String,
              private val username: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.delete("/orgs/$org/teams/$teamSlug/memberships/$username")
                return when (response.status.value) {
                  204 -> Response.NoContent
                  403 -> Response.Forbidden
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data object NoContent : Response

                public data object Forbidden : Response
              }
            }

            public class Get internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val teamSlug: String,
              private val username: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.get("/orgs/$org/teams/$teamSlug/memberships/$username")
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  404 -> Response.NotFound
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: TeamMembership,
                ) : Response

                public data object NotFound : Response
              }
            }

            public class Put internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val teamSlug: String,
              private val username: String,
            ) {
              public suspend operator fun invoke(role: Role? = null): Response {
                val response = client.put("/orgs/$org/teams/$teamSlug/memberships/$username") {
                  if (role != null) {
                    contentType(ContentType.Application.Json)
                    setBody(Body(role = role))
                  }
                }
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  403 -> Response.Forbidden
                  422 -> Response.UnprocessableEntity
                  else -> throw ResponseException(response, "")
                }
              }

              @Serializable
              public enum class Role(
                public val `value`: String,
              ) {
                @SerialName("member")
                Member("member"),
                @SerialName("maintainer")
                Maintainer("maintainer"),
                ;
              }

              @JvmInline
              @Serializable
              internal value class Body(
                public val role: Role? = null,
              )

              public sealed interface Response {
                public data class Ok(
                  public val `value`: TeamMembership,
                ) : Response

                public data object Forbidden : Response

                public data object UnprocessableEntity : Response
              }
            }
          }
        }

        public class Repos internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val teamSlug: String,
        ) {
          public val `get`: Get = Get(client, org, teamSlug)

          public fun owner(owner: String): OwnerPath = OwnerPath(client, org, teamSlug, owner)

          public class Get internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val teamSlug: String,
          ) {
            public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<MinimalRepository> = client.get("/orgs/$org/teams/$teamSlug/repos") {
              perPage?.let { parameter("per_page", it) }
              page?.let { parameter("page", it) }
            }.body()
          }

          public class OwnerPath internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val teamSlug: String,
            private val owner: String,
          ) {
            public fun repo(repo: String): RepoPath = RepoPath(client, org, teamSlug, owner, repo)

            public class RepoPath internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val teamSlug: String,
              private val owner: String,
              private val repo: String,
            ) {
              public val delete: Delete = Delete(client, org, teamSlug, owner, repo)

              public val `get`: Get = Get(client, org, teamSlug, owner, repo)

              public val put: Put = Put(client, org, teamSlug, owner, repo)

              public class Delete internal constructor(
                private val client: HttpClient,
                private val org: String,
                private val teamSlug: String,
                private val owner: String,
                private val repo: String,
              ) {
                public suspend operator fun invoke() {
                  client.delete("/orgs/$org/teams/$teamSlug/repos/$owner/$repo")
                }
              }

              public class Get internal constructor(
                private val client: HttpClient,
                private val org: String,
                private val teamSlug: String,
                private val owner: String,
                private val repo: String,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.get("/orgs/$org/teams/$teamSlug/repos/$owner/$repo")
                  return when (response.status.value) {
                    200 -> Response.Ok(response.body())
                    204 -> Response.NoContent
                    404 -> Response.NotFound
                    else -> throw ResponseException(response, "")
                  }
                }

                public sealed interface Response {
                  public data class Ok(
                    public val `value`: TeamRepository,
                  ) : Response

                  public data object NoContent : Response

                  public data object NotFound : Response
                }
              }

              public class Put internal constructor(
                private val client: HttpClient,
                private val org: String,
                private val teamSlug: String,
                private val owner: String,
                private val repo: String,
              ) {
                public suspend operator fun invoke(permission: String? = null) {
                  client.put("/orgs/$org/teams/$teamSlug/repos/$owner/$repo") {
                    if (permission != null) {
                      contentType(ContentType.Application.Json)
                      setBody(Body(permission = permission))
                    }
                  }
                }

                @JvmInline
                @Serializable
                internal value class Body(
                  public val permission: String? = null,
                )
              }
            }
          }
        }

        public class Teams internal constructor(
          private val client: HttpClient,
          private val org: String,
          private val teamSlug: String,
        ) {
          public val `get`: Get = Get(client, org, teamSlug)

          public class Get internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val teamSlug: String,
          ) {
            public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<io.github.model.Team> = client.get("/orgs/$org/teams/$teamSlug/teams") {
              perPage?.let { parameter("per_page", it) }
              page?.let { parameter("page", it) }
            }.body()
          }
        }
      }
    }

    public class DependencyGraph internal constructor(
      private val client: HttpClient,
      private val org: String,
    ) {
      public val enableAll: EnableAll = EnableAll(client, org)

      public val disableAll: DisableAll = DisableAll(client, org)

      public class EnableAll internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        @Deprecated("Deprecated by the API provider")
        public val post: Post = Post(client, org)

        @Deprecated("Deprecated by the API provider")
        public class Post internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          @Deprecated("Deprecated by the API provider")
          public suspend operator fun invoke(querySuite: QuerySuite? = null): Response {
            val response = client.post("/orgs/$org/dependency_graph/enable_all") {
              if (querySuite != null) {
                contentType(ContentType.Application.Json)
                setBody(Body(querySuite = querySuite))
              }
            }
            return when (response.status.value) {
              204 -> Response.NoContent
              422 -> Response.UnprocessableEntity
              else -> throw ResponseException(response, "")
            }
          }

          @Serializable
          public enum class QuerySuite(
            public val `value`: String,
          ) {
            @SerialName("default")
            Default("default"),
            @SerialName("extended")
            Extended("extended"),
            ;
          }

          @JvmInline
          @Serializable
          internal value class Body(
            @SerialName("query_suite")
            public val querySuite: QuerySuite? = null,
          )

          public sealed interface Response {
            public data object NoContent : Response

            public data object UnprocessableEntity : Response
          }
        }
      }

      public class DisableAll internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        @Deprecated("Deprecated by the API provider")
        public val post: Post = Post(client, org)

        @Deprecated("Deprecated by the API provider")
        public class Post internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          @Deprecated("Deprecated by the API provider")
          public suspend operator fun invoke(querySuite: QuerySuite? = null): Response {
            val response = client.post("/orgs/$org/dependency_graph/disable_all") {
              if (querySuite != null) {
                contentType(ContentType.Application.Json)
                setBody(Body(querySuite = querySuite))
              }
            }
            return when (response.status.value) {
              204 -> Response.NoContent
              422 -> Response.UnprocessableEntity
              else -> throw ResponseException(response, "")
            }
          }

          @Serializable
          public enum class QuerySuite(
            public val `value`: String,
          ) {
            @SerialName("default")
            Default("default"),
            @SerialName("extended")
            Extended("extended"),
            ;
          }

          @JvmInline
          @Serializable
          internal value class Body(
            @SerialName("query_suite")
            public val querySuite: QuerySuite? = null,
          )

          public sealed interface Response {
            public data object NoContent : Response

            public data object UnprocessableEntity : Response
          }
        }
      }
    }

    public class DependabotAlerts internal constructor(
      private val client: HttpClient,
      private val org: String,
    ) {
      public val enableAll: EnableAll = EnableAll(client, org)

      public val disableAll: DisableAll = DisableAll(client, org)

      public class EnableAll internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        @Deprecated("Deprecated by the API provider")
        public val post: Post = Post(client, org)

        @Deprecated("Deprecated by the API provider")
        public class Post internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          @Deprecated("Deprecated by the API provider")
          public suspend operator fun invoke(querySuite: QuerySuite? = null): Response {
            val response = client.post("/orgs/$org/dependabot_alerts/enable_all") {
              if (querySuite != null) {
                contentType(ContentType.Application.Json)
                setBody(Body(querySuite = querySuite))
              }
            }
            return when (response.status.value) {
              204 -> Response.NoContent
              422 -> Response.UnprocessableEntity
              else -> throw ResponseException(response, "")
            }
          }

          @Serializable
          public enum class QuerySuite(
            public val `value`: String,
          ) {
            @SerialName("default")
            Default("default"),
            @SerialName("extended")
            Extended("extended"),
            ;
          }

          @JvmInline
          @Serializable
          internal value class Body(
            @SerialName("query_suite")
            public val querySuite: QuerySuite? = null,
          )

          public sealed interface Response {
            public data object NoContent : Response

            public data object UnprocessableEntity : Response
          }
        }
      }

      public class DisableAll internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        @Deprecated("Deprecated by the API provider")
        public val post: Post = Post(client, org)

        @Deprecated("Deprecated by the API provider")
        public class Post internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          @Deprecated("Deprecated by the API provider")
          public suspend operator fun invoke(querySuite: QuerySuite? = null): Response {
            val response = client.post("/orgs/$org/dependabot_alerts/disable_all") {
              if (querySuite != null) {
                contentType(ContentType.Application.Json)
                setBody(Body(querySuite = querySuite))
              }
            }
            return when (response.status.value) {
              204 -> Response.NoContent
              422 -> Response.UnprocessableEntity
              else -> throw ResponseException(response, "")
            }
          }

          @Serializable
          public enum class QuerySuite(
            public val `value`: String,
          ) {
            @SerialName("default")
            Default("default"),
            @SerialName("extended")
            Extended("extended"),
            ;
          }

          @JvmInline
          @Serializable
          internal value class Body(
            @SerialName("query_suite")
            public val querySuite: QuerySuite? = null,
          )

          public sealed interface Response {
            public data object NoContent : Response

            public data object UnprocessableEntity : Response
          }
        }
      }
    }

    public class DependabotSecurityUpdates internal constructor(
      private val client: HttpClient,
      private val org: String,
    ) {
      public val enableAll: EnableAll = EnableAll(client, org)

      public val disableAll: DisableAll = DisableAll(client, org)

      public class EnableAll internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        @Deprecated("Deprecated by the API provider")
        public val post: Post = Post(client, org)

        @Deprecated("Deprecated by the API provider")
        public class Post internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          @Deprecated("Deprecated by the API provider")
          public suspend operator fun invoke(querySuite: QuerySuite? = null): Response {
            val response = client.post("/orgs/$org/dependabot_security_updates/enable_all") {
              if (querySuite != null) {
                contentType(ContentType.Application.Json)
                setBody(Body(querySuite = querySuite))
              }
            }
            return when (response.status.value) {
              204 -> Response.NoContent
              422 -> Response.UnprocessableEntity
              else -> throw ResponseException(response, "")
            }
          }

          @Serializable
          public enum class QuerySuite(
            public val `value`: String,
          ) {
            @SerialName("default")
            Default("default"),
            @SerialName("extended")
            Extended("extended"),
            ;
          }

          @JvmInline
          @Serializable
          internal value class Body(
            @SerialName("query_suite")
            public val querySuite: QuerySuite? = null,
          )

          public sealed interface Response {
            public data object NoContent : Response

            public data object UnprocessableEntity : Response
          }
        }
      }

      public class DisableAll internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        @Deprecated("Deprecated by the API provider")
        public val post: Post = Post(client, org)

        @Deprecated("Deprecated by the API provider")
        public class Post internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          @Deprecated("Deprecated by the API provider")
          public suspend operator fun invoke(querySuite: QuerySuite? = null): Response {
            val response = client.post("/orgs/$org/dependabot_security_updates/disable_all") {
              if (querySuite != null) {
                contentType(ContentType.Application.Json)
                setBody(Body(querySuite = querySuite))
              }
            }
            return when (response.status.value) {
              204 -> Response.NoContent
              422 -> Response.UnprocessableEntity
              else -> throw ResponseException(response, "")
            }
          }

          @Serializable
          public enum class QuerySuite(
            public val `value`: String,
          ) {
            @SerialName("default")
            Default("default"),
            @SerialName("extended")
            Extended("extended"),
            ;
          }

          @JvmInline
          @Serializable
          internal value class Body(
            @SerialName("query_suite")
            public val querySuite: QuerySuite? = null,
          )

          public sealed interface Response {
            public data object NoContent : Response

            public data object UnprocessableEntity : Response
          }
        }
      }
    }

    public class AdvancedSecurity internal constructor(
      private val client: HttpClient,
      private val org: String,
    ) {
      public val enableAll: EnableAll = EnableAll(client, org)

      public val disableAll: DisableAll = DisableAll(client, org)

      public class EnableAll internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        @Deprecated("Deprecated by the API provider")
        public val post: Post = Post(client, org)

        @Deprecated("Deprecated by the API provider")
        public class Post internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          @Deprecated("Deprecated by the API provider")
          public suspend operator fun invoke(querySuite: QuerySuite? = null): Response {
            val response = client.post("/orgs/$org/advanced_security/enable_all") {
              if (querySuite != null) {
                contentType(ContentType.Application.Json)
                setBody(Body(querySuite = querySuite))
              }
            }
            return when (response.status.value) {
              204 -> Response.NoContent
              422 -> Response.UnprocessableEntity
              else -> throw ResponseException(response, "")
            }
          }

          @Serializable
          public enum class QuerySuite(
            public val `value`: String,
          ) {
            @SerialName("default")
            Default("default"),
            @SerialName("extended")
            Extended("extended"),
            ;
          }

          @JvmInline
          @Serializable
          internal value class Body(
            @SerialName("query_suite")
            public val querySuite: QuerySuite? = null,
          )

          public sealed interface Response {
            public data object NoContent : Response

            public data object UnprocessableEntity : Response
          }
        }
      }

      public class DisableAll internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        @Deprecated("Deprecated by the API provider")
        public val post: Post = Post(client, org)

        @Deprecated("Deprecated by the API provider")
        public class Post internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          @Deprecated("Deprecated by the API provider")
          public suspend operator fun invoke(querySuite: QuerySuite? = null): Response {
            val response = client.post("/orgs/$org/advanced_security/disable_all") {
              if (querySuite != null) {
                contentType(ContentType.Application.Json)
                setBody(Body(querySuite = querySuite))
              }
            }
            return when (response.status.value) {
              204 -> Response.NoContent
              422 -> Response.UnprocessableEntity
              else -> throw ResponseException(response, "")
            }
          }

          @Serializable
          public enum class QuerySuite(
            public val `value`: String,
          ) {
            @SerialName("default")
            Default("default"),
            @SerialName("extended")
            Extended("extended"),
            ;
          }

          @JvmInline
          @Serializable
          internal value class Body(
            @SerialName("query_suite")
            public val querySuite: QuerySuite? = null,
          )

          public sealed interface Response {
            public data object NoContent : Response

            public data object UnprocessableEntity : Response
          }
        }
      }
    }

    public class CodeScanningDefaultSetup internal constructor(
      private val client: HttpClient,
      private val org: String,
    ) {
      public val enableAll: EnableAll = EnableAll(client, org)

      public val disableAll: DisableAll = DisableAll(client, org)

      public class EnableAll internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        @Deprecated("Deprecated by the API provider")
        public val post: Post = Post(client, org)

        @Deprecated("Deprecated by the API provider")
        public class Post internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          @Deprecated("Deprecated by the API provider")
          public suspend operator fun invoke(querySuite: QuerySuite? = null): Response {
            val response = client.post("/orgs/$org/code_scanning_default_setup/enable_all") {
              if (querySuite != null) {
                contentType(ContentType.Application.Json)
                setBody(Body(querySuite = querySuite))
              }
            }
            return when (response.status.value) {
              204 -> Response.NoContent
              422 -> Response.UnprocessableEntity
              else -> throw ResponseException(response, "")
            }
          }

          @Serializable
          public enum class QuerySuite(
            public val `value`: String,
          ) {
            @SerialName("default")
            Default("default"),
            @SerialName("extended")
            Extended("extended"),
            ;
          }

          @JvmInline
          @Serializable
          internal value class Body(
            @SerialName("query_suite")
            public val querySuite: QuerySuite? = null,
          )

          public sealed interface Response {
            public data object NoContent : Response

            public data object UnprocessableEntity : Response
          }
        }
      }

      public class DisableAll internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        @Deprecated("Deprecated by the API provider")
        public val post: Post = Post(client, org)

        @Deprecated("Deprecated by the API provider")
        public class Post internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          @Deprecated("Deprecated by the API provider")
          public suspend operator fun invoke(querySuite: QuerySuite? = null): Response {
            val response = client.post("/orgs/$org/code_scanning_default_setup/disable_all") {
              if (querySuite != null) {
                contentType(ContentType.Application.Json)
                setBody(Body(querySuite = querySuite))
              }
            }
            return when (response.status.value) {
              204 -> Response.NoContent
              422 -> Response.UnprocessableEntity
              else -> throw ResponseException(response, "")
            }
          }

          @Serializable
          public enum class QuerySuite(
            public val `value`: String,
          ) {
            @SerialName("default")
            Default("default"),
            @SerialName("extended")
            Extended("extended"),
            ;
          }

          @JvmInline
          @Serializable
          internal value class Body(
            @SerialName("query_suite")
            public val querySuite: QuerySuite? = null,
          )

          public sealed interface Response {
            public data object NoContent : Response

            public data object UnprocessableEntity : Response
          }
        }
      }
    }

    public class SecretScanningPushProtection internal constructor(
      private val client: HttpClient,
      private val org: String,
    ) {
      public val enableAll: EnableAll = EnableAll(client, org)

      public val disableAll: DisableAll = DisableAll(client, org)

      public class EnableAll internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        @Deprecated("Deprecated by the API provider")
        public val post: Post = Post(client, org)

        @Deprecated("Deprecated by the API provider")
        public class Post internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          @Deprecated("Deprecated by the API provider")
          public suspend operator fun invoke(querySuite: QuerySuite? = null): Response {
            val response = client.post("/orgs/$org/secret_scanning_push_protection/enable_all") {
              if (querySuite != null) {
                contentType(ContentType.Application.Json)
                setBody(Body(querySuite = querySuite))
              }
            }
            return when (response.status.value) {
              204 -> Response.NoContent
              422 -> Response.UnprocessableEntity
              else -> throw ResponseException(response, "")
            }
          }

          @Serializable
          public enum class QuerySuite(
            public val `value`: String,
          ) {
            @SerialName("default")
            Default("default"),
            @SerialName("extended")
            Extended("extended"),
            ;
          }

          @JvmInline
          @Serializable
          internal value class Body(
            @SerialName("query_suite")
            public val querySuite: QuerySuite? = null,
          )

          public sealed interface Response {
            public data object NoContent : Response

            public data object UnprocessableEntity : Response
          }
        }
      }

      public class DisableAll internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        @Deprecated("Deprecated by the API provider")
        public val post: Post = Post(client, org)

        @Deprecated("Deprecated by the API provider")
        public class Post internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          @Deprecated("Deprecated by the API provider")
          public suspend operator fun invoke(querySuite: QuerySuite? = null): Response {
            val response = client.post("/orgs/$org/secret_scanning_push_protection/disable_all") {
              if (querySuite != null) {
                contentType(ContentType.Application.Json)
                setBody(Body(querySuite = querySuite))
              }
            }
            return when (response.status.value) {
              204 -> Response.NoContent
              422 -> Response.UnprocessableEntity
              else -> throw ResponseException(response, "")
            }
          }

          @Serializable
          public enum class QuerySuite(
            public val `value`: String,
          ) {
            @SerialName("default")
            Default("default"),
            @SerialName("extended")
            Extended("extended"),
            ;
          }

          @JvmInline
          @Serializable
          internal value class Body(
            @SerialName("query_suite")
            public val querySuite: QuerySuite? = null,
          )

          public sealed interface Response {
            public data object NoContent : Response

            public data object UnprocessableEntity : Response
          }
        }
      }
    }
  }
}
