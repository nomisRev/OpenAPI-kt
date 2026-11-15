package io.github.api

import io.github.model.ActionsArtifactAndLogRetention
import io.github.model.ActionsArtifactAndLogRetentionResponse
import io.github.model.ActionsCacheList
import io.github.model.ActionsCacheRetentionLimitForRepository
import io.github.model.ActionsCacheStorageLimitForRepository
import io.github.model.ActionsCacheUsageByRepository
import io.github.model.ActionsEnabled
import io.github.model.ActionsForkPrContributorApproval
import io.github.model.ActionsForkPrWorkflowsPrivateRepos
import io.github.model.ActionsForkPrWorkflowsPrivateReposRequest
import io.github.model.ActionsGetDefaultWorkflowPermissions
import io.github.model.ActionsPublicKey
import io.github.model.ActionsRepositoryPermissions
import io.github.model.ActionsSecret
import io.github.model.ActionsSetDefaultWorkflowPermissions
import io.github.model.ActionsVariable
import io.github.model.ActionsWorkflowAccessToRepository
import io.github.model.AllowedActions
import io.github.model.Artifact
import io.github.model.AuthenticationToken
import io.github.model.Autolink
import io.github.model.BasicError
import io.github.model.Blob
import io.github.model.BranchProtection
import io.github.model.BranchRestrictionPolicy
import io.github.model.BranchShort
import io.github.model.BranchWithProtection
import io.github.model.CheckAnnotation
import io.github.model.CheckAutomatedSecurityFixes
import io.github.model.CheckImmutableReleases
import io.github.model.CheckRun
import io.github.model.CheckSuite
import io.github.model.CheckSuitePreference
import io.github.model.CloneTraffic
import io.github.model.CodeFrequencyStat
import io.github.model.CodeScanningAlert
import io.github.model.CodeScanningAlertAssignees
import io.github.model.CodeScanningAlertCreateRequest
import io.github.model.CodeScanningAlertDismissedComment
import io.github.model.CodeScanningAlertDismissedReason
import io.github.model.CodeScanningAlertInstanceList
import io.github.model.CodeScanningAlertItems
import io.github.model.CodeScanningAlertSetState
import io.github.model.CodeScanningAlertSeverity
import io.github.model.CodeScanningAlertStateQuery
import io.github.model.CodeScanningAnalysis
import io.github.model.CodeScanningAnalysisCommitSha
import io.github.model.CodeScanningAnalysisDeletion
import io.github.model.CodeScanningAnalysisSarifFile
import io.github.model.CodeScanningAutofix
import io.github.model.CodeScanningAutofixCommits
import io.github.model.CodeScanningAutofixCommitsResponse
import io.github.model.CodeScanningCodeqlDatabase
import io.github.model.CodeScanningDefaultSetup
import io.github.model.CodeScanningDefaultSetupUpdate
import io.github.model.CodeScanningDefaultSetupUpdateResponse
import io.github.model.CodeScanningRefFull
import io.github.model.CodeScanningSarifsReceipt
import io.github.model.CodeScanningSarifsStatus
import io.github.model.CodeScanningVariantAnalysis
import io.github.model.CodeScanningVariantAnalysisLanguage
import io.github.model.CodeScanningVariantAnalysisRepoTask
import io.github.model.CodeSecurityConfigurationForRepository
import io.github.model.CodeownersErrors
import io.github.model.Codespace
import io.github.model.CodespaceMachine
import io.github.model.CodespacesPermissionsCheckForDevcontainer
import io.github.model.CodespacesPublicKey
import io.github.model.Collaborator
import io.github.model.CombinedCommitStatus
import io.github.model.Commit
import io.github.model.CommitComment
import io.github.model.CommitComparison
import io.github.model.CommunityProfile
import io.github.model.ContentFile
import io.github.model.ContentTraffic
import io.github.model.ContentTree
import io.github.model.Contributor
import io.github.model.ContributorActivity
import io.github.model.CustomDeploymentRuleApp
import io.github.model.CustomPropertyValue
import io.github.model.DependabotAlert
import io.github.model.DependabotPublicKey
import io.github.model.DependabotSecret
import io.github.model.DependencyGraphDiff
import io.github.model.DependencyGraphSpdxSbom
import io.github.model.DeployKey
import io.github.model.Deployment
import io.github.model.DeploymentBranchPolicy
import io.github.model.DeploymentBranchPolicyNamePattern
import io.github.model.DeploymentBranchPolicyNamePatternWithType
import io.github.model.DeploymentBranchPolicySettings
import io.github.model.DeploymentProtectionRule
import io.github.model.DeploymentReviewerType
import io.github.model.DeploymentStatus
import io.github.model.DiffEntry
import io.github.model.EmptyObject
import io.github.model.Environment
import io.github.model.EnvironmentApprovals
import io.github.model.Event
import io.github.model.FileCommit
import io.github.model.FullRepository
import io.github.model.GitCommit
import io.github.model.GitRef
import io.github.model.GitTag
import io.github.model.GitTree
import io.github.model.Hook
import io.github.model.HookDelivery
import io.github.model.HookDeliveryItem
import io.github.model.Integration
import io.github.model.InteractionLimit
import io.github.model.InteractionLimitResponse
import io.github.model.Issue
import io.github.model.IssueComment
import io.github.model.IssueEvent
import io.github.model.IssueEventForIssue
import io.github.model.IssueFieldValue
import io.github.model.Job
import io.github.model.Label
import io.github.model.Language
import io.github.model.LicenseContent
import io.github.model.MergedUpstream
import io.github.model.Milestone
import io.github.model.MinimalRepository
import io.github.model.OidcCustomSubRepo
import io.github.model.Optional
import io.github.model.Page
import io.github.model.PageBuild
import io.github.model.PageBuildStatus
import io.github.model.PageDeployment
import io.github.model.PagesDeploymentStatus
import io.github.model.PagesHealthCheck
import io.github.model.ParticipationStats
import io.github.model.PendingDeployment
import io.github.model.PorterAuthor
import io.github.model.PorterLargeFile
import io.github.model.PreventSelfReview
import io.github.model.PrivateVulnerabilityReportCreate
import io.github.model.ProtectedBranch
import io.github.model.ProtectedBranchAdminEnforced
import io.github.model.ProtectedBranchPullRequestReview
import io.github.model.PullRequest
import io.github.model.PullRequestMergeResult
import io.github.model.PullRequestReview
import io.github.model.PullRequestReviewComment
import io.github.model.PullRequestReviewRequest
import io.github.model.PullRequestSimple
import io.github.model.Reaction
import io.github.model.ReferrerTraffic
import io.github.model.Release
import io.github.model.ReleaseAsset
import io.github.model.ReleaseNotesContent
import io.github.model.RepoCodespacesSecret
import io.github.model.RepositoryAdvisory
import io.github.model.RepositoryAdvisoryCreate
import io.github.model.RepositoryAdvisoryUpdate
import io.github.model.RepositoryCollaboratorPermission
import io.github.model.RepositoryInvitation
import io.github.model.RepositoryRule
import io.github.model.RepositoryRuleDetailed
import io.github.model.RepositoryRuleEnforcement
import io.github.model.RepositoryRuleViolationError
import io.github.model.RepositoryRuleset
import io.github.model.RepositoryRulesetBypassActor
import io.github.model.RepositoryRulesetConditions
import io.github.model.RepositorySubscription
import io.github.model.ReviewComment
import io.github.model.ReviewCustomGatesCommentRequired
import io.github.model.ReviewCustomGatesStateRequired
import io.github.model.RuleSuite
import io.github.model.RulesetVersion
import io.github.model.RulesetVersionWithState
import io.github.model.Runner
import io.github.model.RunnerApplication
import io.github.model.RunnerLabel
import io.github.model.SecretScanningAlert
import io.github.model.SecretScanningAlertAssignee
import io.github.model.SecretScanningAlertResolution
import io.github.model.SecretScanningAlertResolutionComment
import io.github.model.SecretScanningAlertState
import io.github.model.SecretScanningLocation
import io.github.model.SecretScanningPushProtectionBypass
import io.github.model.SecretScanningPushProtectionBypassPlaceholderId
import io.github.model.SecretScanningPushProtectionBypassReason
import io.github.model.SecretScanningScanHistory
import io.github.model.ShaPinningRequired
import io.github.model.ShortBlob
import io.github.model.ShortBranch
import io.github.model.SimpleUser
import io.github.model.Snapshot
import io.github.model.Stargazer
import io.github.model.Status
import io.github.model.StatusCheckPolicy
import io.github.model.Tag
import io.github.model.Team
import io.github.model.Thread
import io.github.model.TimelineIssueEvents
import io.github.model.Topic
import io.github.model.ValidationError
import io.github.model.ValidationErrorSimple
import io.github.model.ViewTraffic
import io.github.model.WaitTimer
import io.github.model.WebhookConfig
import io.github.model.WebhookConfigContentType
import io.github.model.WebhookConfigInsecureSsl
import io.github.model.WebhookConfigSecret
import io.github.model.WebhookConfigUrl
import io.github.model.Workflow
import io.github.model.WorkflowDispatchResponse
import io.github.model.WorkflowRun
import io.github.model.WorkflowRunUsage
import io.github.model.WorkflowUsage
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.`header`
import io.ktor.client.request.delete
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.encodeURLParameter
import kotlin.Boolean
import kotlin.ByteArray
import kotlin.Deprecated
import kotlin.Double
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlin.jvm.JvmName
import kotlin.time.Instant
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.doubleOrNull

public class Repos internal constructor(
  private val __client: HttpClient,
) {
  public fun owner(owner: String): OwnerPath = OwnerPath(__client, owner)

  public fun templateOwner(templateOwner: String): TemplateOwnerPath = TemplateOwnerPath(__client, templateOwner)

  public class OwnerPath internal constructor(
    private val __client: HttpClient,
    private val _owner: String,
  ) {
    public fun repo(repo: String): RepoPath = RepoPath(__client, _owner, repo)

    public class RepoPath internal constructor(
      private val __client: HttpClient,
      private val _owner: String,
      private val _repo: String,
    ) {
      public val delete: Delete = Delete(__client, _owner, _repo)

      public val `get`: Get = Get(__client, _owner, _repo)

      public val patch: Patch = Patch(__client, _owner, _repo)

      public val actions: Actions = Actions(__client, _owner, _repo)

      public val activity: Activity = Activity(__client, _owner, _repo)

      public val assignees: Assignees = Assignees(__client, _owner, _repo)

      public val attestations: Attestations = Attestations(__client, _owner, _repo)

      public val autolinks: Autolinks = Autolinks(__client, _owner, _repo)

      public val automatedSecurityFixes: AutomatedSecurityFixes =
          AutomatedSecurityFixes(__client, _owner, _repo)

      public val branches: Branches = Branches(__client, _owner, _repo)

      public val checkRuns: CheckRuns = CheckRuns(__client, _owner, _repo)

      public val checkSuites: CheckSuites = CheckSuites(__client, _owner, _repo)

      public val codeScanning: CodeScanning = CodeScanning(__client, _owner, _repo)

      public val codeSecurityConfiguration: CodeSecurityConfiguration =
          CodeSecurityConfiguration(__client, _owner, _repo)

      public val codeowners: Codeowners = Codeowners(__client, _owner, _repo)

      public val codespaces: Codespaces = Codespaces(__client, _owner, _repo)

      public val collaborators: Collaborators = Collaborators(__client, _owner, _repo)

      public val comments: Comments = Comments(__client, _owner, _repo)

      public val commits: Commits = Commits(__client, _owner, _repo)

      public val community: Community = Community(__client, _owner, _repo)

      public val compare: Compare = Compare(__client, _owner, _repo)

      public val contents: Contents = Contents(__client, _owner, _repo)

      public val contributors: Contributors = Contributors(__client, _owner, _repo)

      public val dependabot: Dependabot = Dependabot(__client, _owner, _repo)

      public val dependencyGraph: DependencyGraph = DependencyGraph(__client, _owner, _repo)

      public val deployments: Deployments = Deployments(__client, _owner, _repo)

      public val dispatches: Dispatches = Dispatches(__client, _owner, _repo)

      public val environments: Environments = Environments(__client, _owner, _repo)

      public val events: Events = Events(__client, _owner, _repo)

      public val forks: Forks = Forks(__client, _owner, _repo)

      public val git: Git = Git(__client, _owner, _repo)

      public val hooks: Hooks = Hooks(__client, _owner, _repo)

      public val immutableReleases: ImmutableReleases = ImmutableReleases(__client, _owner, _repo)

      public val `import`: Import = Import(__client, _owner, _repo)

      public val installation: Installation = Installation(__client, _owner, _repo)

      public val interactionLimits: InteractionLimits = InteractionLimits(__client, _owner, _repo)

      public val invitations: Invitations = Invitations(__client, _owner, _repo)

      public val issues: Issues = Issues(__client, _owner, _repo)

      public val keys: Keys = Keys(__client, _owner, _repo)

      public val labels: Labels = Labels(__client, _owner, _repo)

      public val languages: Languages = Languages(__client, _owner, _repo)

      public val license: License = License(__client, _owner, _repo)

      public val mergeUpstream: MergeUpstream = MergeUpstream(__client, _owner, _repo)

      public val merges: Merges = Merges(__client, _owner, _repo)

      public val milestones: Milestones = Milestones(__client, _owner, _repo)

      public val notifications: Notifications = Notifications(__client, _owner, _repo)

      public val pages: Pages = Pages(__client, _owner, _repo)

      public val privateVulnerabilityReporting: PrivateVulnerabilityReporting =
          PrivateVulnerabilityReporting(__client, _owner, _repo)

      public val properties: Properties = Properties(__client, _owner, _repo)

      public val pulls: Pulls = Pulls(__client, _owner, _repo)

      public val readme: Readme = Readme(__client, _owner, _repo)

      public val releases: Releases = Releases(__client, _owner, _repo)

      public val rules: Rules = Rules(__client, _owner, _repo)

      public val rulesets: Rulesets = Rulesets(__client, _owner, _repo)

      public val secretScanning: SecretScanning = SecretScanning(__client, _owner, _repo)

      public val securityAdvisories: SecurityAdvisories =
          SecurityAdvisories(__client, _owner, _repo)

      public val stargazers: Stargazers = Stargazers(__client, _owner, _repo)

      public val stats: Stats = Stats(__client, _owner, _repo)

      public val statuses: Statuses = Statuses(__client, _owner, _repo)

      public val subscribers: Subscribers = Subscribers(__client, _owner, _repo)

      public val subscription: Subscription = Subscription(__client, _owner, _repo)

      public val tags: Tags = Tags(__client, _owner, _repo)

      public val tarball: Tarball = Tarball(__client, _owner, _repo)

      public val teams: Teams = Teams(__client, _owner, _repo)

      public val topics: Topics = Topics(__client, _owner, _repo)

      public val traffic: Traffic = Traffic(__client, _owner, _repo)

      public val transfer: Transfer = Transfer(__client, _owner, _repo)

      public val vulnerabilityAlerts: VulnerabilityAlerts =
          VulnerabilityAlerts(__client, _owner, _repo)

      public val zipball: Zipball = Zipball(__client, _owner, _repo)

      public class Delete internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public suspend operator fun invoke(): Response {
          val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}")
          return when (response.status.value) {
            204 -> Response.NoContent
            307 -> Response.TemporaryRedirect(response.body())
            403 -> response.body<Response.Forbidden>()
            404 -> Response.NotFound(response.body())
            409 -> Response.Conflict(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        public sealed interface Response {
          public data object NoContent : Response

          public data class TemporaryRedirect(
            public val `value`: BasicError,
          ) : Response

          @OptIn(ExperimentalSerializationApi::class)
          @Serializable
          public data class Forbidden(
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val message: String? = null,
            @SerialName("documentation_url")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val documentationUrl: String? = null,
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
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public suspend operator fun invoke(): Response {
          val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}")
          return when (response.status.value) {
            200 -> Response.Ok(response.body())
            301 -> Response.MovedPermanently(response.body())
            403 -> Response.Forbidden(response.body())
            404 -> Response.NotFound(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        public sealed interface Response {
          public data class Ok(
            public val `value`: FullRepository,
          ) : Response

          public data class MovedPermanently(
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

      public class Patch internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public suspend operator fun invoke(): Response {
          val response = __client.patch("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}")
          return when (response.status.value) {
            200 -> Response.Ok(response.body())
            307 -> Response.TemporaryRedirect(response.body())
            403 -> Response.Forbidden(response.body())
            404 -> Response.NotFound(response.body())
            422 -> Response.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        public suspend operator fun invoke(
          name: String? = null,
          description: String? = null,
          homepage: String? = null,
          `private`: Boolean? = null,
          visibility: Visibility? = null,
          securityAndAnalysis: Optional<SecurityAndAnalysis> = Optional.Absent,
          hasIssues: Boolean? = null,
          hasProjects: Boolean? = null,
          hasWiki: Boolean? = null,
          isTemplate: Boolean? = null,
          defaultBranch: String? = null,
          allowSquashMerge: Boolean? = null,
          allowMergeCommit: Boolean? = null,
          allowRebaseMerge: Boolean? = null,
          allowAutoMerge: Boolean? = null,
          deleteBranchOnMerge: Boolean? = null,
          allowUpdateBranch: Boolean? = null,
          useSquashPrTitleAsDefault: Boolean? = null,
          squashMergeCommitTitle: SquashMergeCommitTitle? = null,
          squashMergeCommitMessage: SquashMergeCommitMessage? = null,
          mergeCommitTitle: MergeCommitTitle? = null,
          mergeCommitMessage: MergeCommitMessage? = null,
          archived: Boolean? = null,
          allowForking: Boolean? = null,
          webCommitSignoffRequired: Boolean? = null,
        ): Response {
          val response = __client.patch("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}") {
            contentType(ContentType.Application.Json)
            setBody(Body(name = name, description = description, homepage = homepage, private = private, visibility = visibility, securityAndAnalysis = securityAndAnalysis, hasIssues = hasIssues, hasProjects = hasProjects, hasWiki = hasWiki, isTemplate = isTemplate, defaultBranch = defaultBranch, allowSquashMerge = allowSquashMerge, allowMergeCommit = allowMergeCommit, allowRebaseMerge = allowRebaseMerge, allowAutoMerge = allowAutoMerge, deleteBranchOnMerge = deleteBranchOnMerge, allowUpdateBranch = allowUpdateBranch, useSquashPrTitleAsDefault = useSquashPrTitleAsDefault, squashMergeCommitTitle = squashMergeCommitTitle, squashMergeCommitMessage = squashMergeCommitMessage, mergeCommitTitle = mergeCommitTitle, mergeCommitMessage = mergeCommitMessage, archived = archived, allowForking = allowForking, webCommitSignoffRequired = webCommitSignoffRequired))
          }
          return when (response.status.value) {
            200 -> Response.Ok(response.body())
            307 -> Response.TemporaryRedirect(response.body())
            403 -> Response.Forbidden(response.body())
            404 -> Response.NotFound(response.body())
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

        /**
         * Specify which security and analysis features to enable or disable for the repository.
         *
         * To use this parameter, you must have admin permissions for the repository or be an owner or security manager for the organization that owns the repository. For more information, see "[Managing security managers in your organization](https://docs.github.com/organizations/managing-peoples-access-to-your-organization-with-roles/managing-security-managers-in-your-organization)."
         *
         * For example, to enable GitHub Advanced Security, use this data in the body of the `PATCH` request:
         * `{ "security_and_analysis": {"advanced_security": { "status": "enabled" } } }`.
         *
         * You can check which security and analysis features are currently enabled by using a `GET /repos/{owner}/{repo}` request.
         */
        @OptIn(ExperimentalSerializationApi::class)
        @Serializable
        public data class SecurityAndAnalysis(
          @SerialName("advanced_security")
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val advancedSecurity: AdvancedSecurity? = null,
          @SerialName("code_security")
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val codeSecurity: CodeSecurity? = null,
          @SerialName("secret_scanning")
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val secretScanning: SecretScanning? = null,
          @SerialName("secret_scanning_push_protection")
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val secretScanningPushProtection: SecretScanningPushProtection? = null,
          @SerialName("secret_scanning_ai_detection")
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val secretScanningAiDetection: SecretScanningAiDetection? = null,
          @SerialName("secret_scanning_non_provider_patterns")
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val secretScanningNonProviderPatterns: SecretScanningNonProviderPatterns? = null,
          @SerialName("secret_scanning_delegated_alert_dismissal")
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val secretScanningDelegatedAlertDismissal:
              SecretScanningDelegatedAlertDismissal? = null,
          @SerialName("secret_scanning_delegated_bypass")
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val secretScanningDelegatedBypass: SecretScanningDelegatedBypass? = null,
          @SerialName("secret_scanning_delegated_bypass_options")
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val secretScanningDelegatedBypassOptions:
              SecretScanningDelegatedBypassOptions? = null,
        ) {
          /**
           * Use the `status` property to enable or disable GitHub Advanced Security for this repository.
           * For more information, see "[About GitHub Advanced
           * Security](/github/getting-started-with-github/learning-about-github/about-github-advanced-security)."
           *
           * For standalone Code Scanning or Secret Protection products, this parameter cannot be used.
           */
          @OptIn(ExperimentalSerializationApi::class)
          @Serializable
          public data class AdvancedSecurity(
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val status: String? = null,
          )

          /**
           * Use the `status` property to enable or disable GitHub Code Security for this repository.
           */
          @OptIn(ExperimentalSerializationApi::class)
          @Serializable
          public data class CodeSecurity(
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val status: String? = null,
          )

          /**
           * Use the `status` property to enable or disable secret scanning for this repository. For more information, see "[About secret scanning](/code-security/secret-security/about-secret-scanning)."
           */
          @OptIn(ExperimentalSerializationApi::class)
          @Serializable
          public data class SecretScanning(
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val status: String? = null,
          )

          /**
           * Use the `status` property to enable or disable secret scanning AI detection for this repository. For more information, see "[Responsible detection of generic secrets with AI](https://docs.github.com/code-security/secret-scanning/using-advanced-secret-scanning-and-push-protection-features/generic-secret-detection/responsible-ai-generic-secrets)."
           */
          @OptIn(ExperimentalSerializationApi::class)
          @Serializable
          public data class SecretScanningAiDetection(
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val status: String? = null,
          )

          /**
           * Use the `status` property to enable or disable secret scanning delegated alert dismissal for this repository.
           */
          @OptIn(ExperimentalSerializationApi::class)
          @Serializable
          public data class SecretScanningDelegatedAlertDismissal(
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val status: String? = null,
          )

          /**
           * Use the `status` property to enable or disable secret scanning delegated bypass for this repository.
           */
          @OptIn(ExperimentalSerializationApi::class)
          @Serializable
          public data class SecretScanningDelegatedBypass(
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val status: String? = null,
          )

          /**
           * Feature options for secret scanning delegated bypass.
           * This object is only honored when `security_and_analysis.secret_scanning_delegated_bypass.status` is set to `enabled`.
           * You can send this object in the same request as `secret_scanning_delegated_bypass`, or update just the options in a separate request.
           */
          @OptIn(ExperimentalSerializationApi::class)
          @Serializable
          public data class SecretScanningDelegatedBypassOptions(
            @EncodeDefault(EncodeDefault.Mode.NEVER)
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

          /**
           * Use the `status` property to enable or disable secret scanning non-provider patterns for this repository. For more information, see "[Supported secret scanning patterns](/code-security/secret-scanning/introduction/supported-secret-scanning-patterns#supported-secrets)."
           */
          @OptIn(ExperimentalSerializationApi::class)
          @Serializable
          public data class SecretScanningNonProviderPatterns(
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val status: String? = null,
          )

          /**
           * Use the `status` property to enable or disable secret scanning push protection for this repository. For more information, see "[Protecting pushes with secret scanning](/code-security/secret-scanning/protecting-pushes-with-secret-scanning)."
           */
          @OptIn(ExperimentalSerializationApi::class)
          @Serializable
          public data class SecretScanningPushProtection(
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val status: String? = null,
          )
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

        @OptIn(ExperimentalSerializationApi::class)
        @Serializable
        internal data class Body(
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val name: String? = null,
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val description: String? = null,
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val homepage: String? = null,
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val `private`: Boolean? = null,
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val visibility: Visibility? = null,
          @SerialName("security_and_analysis")
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val securityAndAnalysis: Optional<SecurityAndAnalysis> = Optional.Absent,
          @SerialName("has_issues")
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val hasIssues: Boolean? = null,
          @SerialName("has_projects")
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val hasProjects: Boolean? = null,
          @SerialName("has_wiki")
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val hasWiki: Boolean? = null,
          @SerialName("is_template")
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val isTemplate: Boolean? = null,
          @SerialName("default_branch")
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val defaultBranch: String? = null,
          @SerialName("allow_squash_merge")
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val allowSquashMerge: Boolean? = null,
          @SerialName("allow_merge_commit")
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val allowMergeCommit: Boolean? = null,
          @SerialName("allow_rebase_merge")
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val allowRebaseMerge: Boolean? = null,
          @SerialName("allow_auto_merge")
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val allowAutoMerge: Boolean? = null,
          @SerialName("delete_branch_on_merge")
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val deleteBranchOnMerge: Boolean? = null,
          @SerialName("allow_update_branch")
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val allowUpdateBranch: Boolean? = null,
          @SerialName("use_squash_pr_title_as_default")
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val useSquashPrTitleAsDefault: Boolean? = null,
          @SerialName("squash_merge_commit_title")
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val squashMergeCommitTitle: SquashMergeCommitTitle? = null,
          @SerialName("squash_merge_commit_message")
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val squashMergeCommitMessage: SquashMergeCommitMessage? = null,
          @SerialName("merge_commit_title")
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val mergeCommitTitle: MergeCommitTitle? = null,
          @SerialName("merge_commit_message")
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val mergeCommitMessage: MergeCommitMessage? = null,
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val archived: Boolean? = null,
          @SerialName("allow_forking")
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val allowForking: Boolean? = null,
          @SerialName("web_commit_signoff_required")
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val webCommitSignoffRequired: Boolean? = null,
        )

        public sealed interface Response {
          public data class Ok(
            public val `value`: FullRepository,
          ) : Response

          public data class TemporaryRedirect(
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

      public class Actions internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public val artifacts: Artifacts = Artifacts(__client, _owner, _repo)

        public val cache: Cache = Cache(__client, _owner, _repo)

        public val caches: Caches = Caches(__client, _owner, _repo)

        public val jobs: Jobs = Jobs(__client, _owner, _repo)

        public val oidc: Oidc = Oidc(__client, _owner, _repo)

        public val organizationSecrets: OrganizationSecrets =
            OrganizationSecrets(__client, _owner, _repo)

        public val organizationVariables: OrganizationVariables =
            OrganizationVariables(__client, _owner, _repo)

        public val permissions: Permissions = Permissions(__client, _owner, _repo)

        public val runners: Runners = Runners(__client, _owner, _repo)

        public val runs: Runs = Runs(__client, _owner, _repo)

        public val secrets: Secrets = Secrets(__client, _owner, _repo)

        public val variables: Variables = Variables(__client, _owner, _repo)

        public val workflows: Workflows = Workflows(__client, _owner, _repo)

        public class Artifacts internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public val `get`: Get = Get(__client, _owner, _repo)

          public fun artifactId(artifactId: Long): ArtifactIdPath = ArtifactIdPath(__client, _owner, _repo, artifactId)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public suspend operator fun invoke(
              perPage: Long? = 30L,
              page: Long? = 1L,
              name: String? = null,
            ): Response {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/artifacts") {
                if (perPage != null) {
                  this.url.encodedParameters.append("per_page", perPage.toString())
                }
                if (page != null) {
                  this.url.encodedParameters.append("page", page.toString())
                }
                if (name != null) {
                  this.url.encodedParameters.append("name", name.encodeURLParameter())
                }
              }
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }

            @Serializable
            public data class Response(
              @SerialName("total_count")
              public val totalCount: Long,
              public val artifacts: List<Artifact>,
            )
          }

          public class ArtifactIdPath internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _artifactId: Long,
          ) {
            public val delete: Delete = Delete(__client, _owner, _repo, _artifactId)

            public val `get`: Get = Get(__client, _owner, _repo, _artifactId)

            public fun archiveFormat(archiveFormat: String): ArchiveFormatPath = ArchiveFormatPath(__client, _owner, _repo, _artifactId, archiveFormat)

            public class Delete internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _artifactId: Long,
            ) {
              public suspend operator fun invoke() {
                val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/artifacts/${this._artifactId}")
                if (response.status.value != 204) throw ResponseException(response, "")
              }
            }

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _artifactId: Long,
            ) {
              public suspend operator fun invoke(): Artifact {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/artifacts/${this._artifactId}")
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }
            }

            public class ArchiveFormatPath internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _artifactId: Long,
              private val _archiveFormat: String,
            ) {
              public val `get`: Get = Get(__client, _owner, _repo, _artifactId, _archiveFormat)

              public class Get internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _artifactId: Long,
                private val _archiveFormat: String,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/artifacts/${this._artifactId}/${this._archiveFormat.encodeURLParameter()}")
                  return when (response.status.value) {
                    302 -> Response.Found
                    410 -> Response.Gone(response.body())
                    else -> throw ResponseException(response, "")
                  }
                }

                public sealed interface Response {
                  public data object Found : Response

                  public data class Gone(
                    public val `value`: BasicError,
                  ) : Response
                }
              }
            }
          }
        }

        public class Cache internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public val retentionLimit: RetentionLimit = RetentionLimit(__client, _owner, _repo)

          public val storageLimit: StorageLimit = StorageLimit(__client, _owner, _repo)

          public val usage: Usage = Usage(__client, _owner, _repo)

          public class RetentionLimit internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo)

            public val put: Put = Put(__client, _owner, _repo)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/cache/retention-limit")
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: ActionsCacheRetentionLimitForRepository,
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
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
            ) {
              public suspend operator fun invoke(body: ActionsCacheRetentionLimitForRepository): Response {
                val response = __client.put("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/cache/retention-limit") {
                  contentType(ContentType.Application.Json)
                  setBody(body)
                }
                return when (response.status.value) {
                  204 -> Response.NoContent
                  400 -> Response.BadRequest(response.body())
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
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
              }
            }
          }

          public class StorageLimit internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo)

            public val put: Put = Put(__client, _owner, _repo)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/cache/storage-limit")
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: ActionsCacheStorageLimitForRepository,
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
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
            ) {
              public suspend operator fun invoke(body: ActionsCacheStorageLimitForRepository): Response {
                val response = __client.put("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/cache/storage-limit") {
                  contentType(ContentType.Application.Json)
                  setBody(body)
                }
                return when (response.status.value) {
                  204 -> Response.NoContent
                  400 -> Response.BadRequest(response.body())
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
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
              }
            }
          }

          public class Usage internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
            ) {
              public suspend operator fun invoke(): ActionsCacheUsageByRepository {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/cache/usage")
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }
            }
          }
        }

        public class Caches internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public val delete: Delete = Delete(__client, _owner, _repo)

          public val `get`: Get = Get(__client, _owner, _repo)

          public fun cacheId(cacheId: Long): CacheIdPath = CacheIdPath(__client, _owner, _repo, cacheId)

          public class Delete internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public suspend operator fun invoke(key: String, ref: String? = null): ActionsCacheList {
              val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/caches") {
                this.url.encodedParameters.append("key", key.encodeURLParameter())
                if (ref != null) {
                  this.url.encodedParameters.append("ref", ref.encodeURLParameter())
                }
              }
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }
          }

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public suspend operator fun invoke(
              perPage: Long? = 30L,
              page: Long? = 1L,
              ref: String? = null,
              key: String? = null,
              sort: Sort? = Sort.LastAccessedAt,
              direction: Direction? = Direction.Desc,
            ): ActionsCacheList {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/caches") {
                if (perPage != null) {
                  this.url.encodedParameters.append("per_page", perPage.toString())
                }
                if (page != null) {
                  this.url.encodedParameters.append("page", page.toString())
                }
                if (ref != null) {
                  this.url.encodedParameters.append("ref", ref.encodeURLParameter())
                }
                if (key != null) {
                  this.url.encodedParameters.append("key", key.encodeURLParameter())
                }
                if (sort != null) {
                  this.url.encodedParameters.append("sort", sort.value.encodeURLParameter())
                }
                if (direction != null) {
                  this.url.encodedParameters.append("direction", direction.value.encodeURLParameter())
                }
              }
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }

            @Serializable
            public enum class Sort(
              public val `value`: String,
            ) {
              @SerialName("created_at")
              CreatedAt("created_at"),
              @SerialName("last_accessed_at")
              LastAccessedAt("last_accessed_at"),
              @SerialName("size_in_bytes")
              SizeInBytes("size_in_bytes"),
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

          public class CacheIdPath internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _cacheId: Long,
          ) {
            public val delete: Delete = Delete(__client, _owner, _repo, _cacheId)

            public class Delete internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _cacheId: Long,
            ) {
              public suspend operator fun invoke() {
                val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/caches/${this._cacheId}")
                if (response.status.value != 204) throw ResponseException(response, "")
              }
            }
          }
        }

        public class Jobs internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public fun jobId(jobId: Long): JobIdPath = JobIdPath(__client, _owner, _repo, jobId)

          public class JobIdPath internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _jobId: Long,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo, _jobId)

            public val logs: Logs = Logs(__client, _owner, _repo, _jobId)

            public val rerun: Rerun = Rerun(__client, _owner, _repo, _jobId)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _jobId: Long,
            ) {
              public suspend operator fun invoke(): Job {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/jobs/${this._jobId}")
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }
            }

            public class Logs internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _jobId: Long,
            ) {
              public val `get`: Get = Get(__client, _owner, _repo, _jobId)

              public class Get internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _jobId: Long,
              ) {
                public suspend operator fun invoke() {
                  val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/jobs/${this._jobId}/logs")
                  if (response.status.value != 302) throw ResponseException(response, "")
                }
              }
            }

            public class Rerun internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _jobId: Long,
            ) {
              public val post: Post = Post(__client, _owner, _repo, _jobId)

              public class Post internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _jobId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/jobs/${this._jobId}/rerun")
                  return when (response.status.value) {
                    201 -> Response.Created(response.body())
                    403 -> Response.Forbidden(response.body())
                    else -> throw ResponseException(response, "")
                  }
                }

                public suspend operator fun invoke(enableDebugLogging: Boolean? = null): Response {
                  val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/jobs/${this._jobId}/rerun") {
                    contentType(ContentType.Application.Json)
                    setBody(Body(enableDebugLogging = enableDebugLogging))
                  }
                  return when (response.status.value) {
                    201 -> Response.Created(response.body())
                    403 -> Response.Forbidden(response.body())
                    else -> throw ResponseException(response, "")
                  }
                }

                @OptIn(ExperimentalSerializationApi::class)
                @Serializable
                internal data class Body(
                  @SerialName("enable_debug_logging")
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val enableDebugLogging: Boolean? = null,
                )

                public sealed interface Response {
                  public data class Created(
                    public val `value`: EmptyObject,
                  ) : Response

                  public data class Forbidden(
                    public val `value`: BasicError,
                  ) : Response
                }
              }
            }
          }
        }

        public class Oidc internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public val customization: Customization = Customization(__client, _owner, _repo)

          public class Customization internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public val sub: Sub = Sub(__client, _owner, _repo)

            public class Sub internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
            ) {
              public val `get`: Get = Get(__client, _owner, _repo)

              public val put: Put = Put(__client, _owner, _repo)

              public class Get internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/oidc/customization/sub")
                  return when (response.status.value) {
                    200 -> Response.Ok(response.body())
                    400 -> Response.BadRequest(response.body())
                    404 -> Response.NotFound(response.body())
                    else -> throw ResponseException(response, "")
                  }
                }

                public sealed interface Response {
                  public data class Ok(
                    public val `value`: OidcCustomSubRepo,
                  ) : Response

                  public data class BadRequest(
                    public val `value`: BasicError,
                  ) : Response

                  public data class NotFound(
                    public val `value`: BasicError,
                  ) : Response
                }
              }

              public class Put internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
              ) {
                public suspend operator fun invoke(useDefault: Boolean, includeClaimKeys: List<String>? = null): Response {
                  val response = __client.put("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/oidc/customization/sub") {
                    contentType(ContentType.Application.Json)
                    setBody(Body(useDefault = useDefault, includeClaimKeys = includeClaimKeys))
                  }
                  return when (response.status.value) {
                    201 -> Response.Created(response.body())
                    400 -> Response.BadRequest(response.body())
                    404 -> Response.NotFound(response.body())
                    422 -> Response.UnprocessableEntity(response.body())
                    else -> throw ResponseException(response, "")
                  }
                }

                /**
                 * Actions OIDC subject customization for a repository
                 */
                @OptIn(ExperimentalSerializationApi::class)
                @Serializable
                internal data class Body(
                  @SerialName("use_default")
                  public val useDefault: Boolean,
                  @SerialName("include_claim_keys")
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val includeClaimKeys: List<String>? = null,
                )

                public sealed interface Response {
                  public data class Created(
                    public val `value`: EmptyObject,
                  ) : Response

                  public data class BadRequest(
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
          }
        }

        public class OrganizationSecrets internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public val `get`: Get = Get(__client, _owner, _repo)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/organization-secrets") {
                if (perPage != null) {
                  this.url.encodedParameters.append("per_page", perPage.toString())
                }
                if (page != null) {
                  this.url.encodedParameters.append("page", page.toString())
                }
              }
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }

            @Serializable
            public data class Response(
              @SerialName("total_count")
              public val totalCount: Long,
              public val secrets: List<ActionsSecret>,
            )
          }
        }

        public class OrganizationVariables internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public val `get`: Get = Get(__client, _owner, _repo)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public suspend operator fun invoke(perPage: Long? = 10L, page: Long? = 1L): Response {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/organization-variables") {
                if (perPage != null) {
                  this.url.encodedParameters.append("per_page", perPage.toString())
                }
                if (page != null) {
                  this.url.encodedParameters.append("page", page.toString())
                }
              }
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }

            @Serializable
            public data class Response(
              @SerialName("total_count")
              public val totalCount: Long,
              public val variables: List<ActionsVariable>,
            )
          }
        }

        public class Permissions internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public val `get`: Get = Get(__client, _owner, _repo)

          public val put: Put = Put(__client, _owner, _repo)

          public val access: Access = Access(__client, _owner, _repo)

          public val artifactAndLogRetention: ArtifactAndLogRetention =
              ArtifactAndLogRetention(__client, _owner, _repo)

          public val forkPrContributorApproval: ForkPrContributorApproval =
              ForkPrContributorApproval(__client, _owner, _repo)

          public val forkPrWorkflowsPrivateRepos: ForkPrWorkflowsPrivateRepos =
              ForkPrWorkflowsPrivateRepos(__client, _owner, _repo)

          public val selectedActions: SelectedActions = SelectedActions(__client, _owner, _repo)

          public val workflow: Workflow = Workflow(__client, _owner, _repo)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public suspend operator fun invoke(): ActionsRepositoryPermissions {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/permissions")
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }
          }

          public class Put internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public suspend operator fun invoke(
              enabled: ActionsEnabled,
              allowedActions: AllowedActions? = null,
              shaPinningRequired: ShaPinningRequired? = null,
            ) {
              val response = __client.put("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/permissions") {
                contentType(ContentType.Application.Json)
                setBody(Body(enabled = enabled, allowedActions = allowedActions, shaPinningRequired = shaPinningRequired))
              }
              if (response.status.value != 204) throw ResponseException(response, "")
            }

            @OptIn(ExperimentalSerializationApi::class)
            @Serializable
            internal data class Body(
              public val enabled: ActionsEnabled,
              @SerialName("allowed_actions")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val allowedActions: AllowedActions? = null,
              @SerialName("sha_pinning_required")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val shaPinningRequired: ShaPinningRequired? = null,
            )
          }

          public class Access internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo)

            public val put: Put = Put(__client, _owner, _repo)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
            ) {
              public suspend operator fun invoke(): ActionsWorkflowAccessToRepository {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/permissions/access")
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }
            }

            public class Put internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
            ) {
              public suspend operator fun invoke(body: ActionsWorkflowAccessToRepository) {
                val response = __client.put("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/permissions/access") {
                  contentType(ContentType.Application.Json)
                  setBody(body)
                }
                if (response.status.value != 204) throw ResponseException(response, "")
              }
            }
          }

          public class ArtifactAndLogRetention internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo)

            public val put: Put = Put(__client, _owner, _repo)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/permissions/artifact-and-log-retention")
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  404 -> Response.NotFound(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: ActionsArtifactAndLogRetentionResponse,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response
              }
            }

            public class Put internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
            ) {
              public suspend operator fun invoke(body: ActionsArtifactAndLogRetention): Response {
                val response = __client.put("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/permissions/artifact-and-log-retention") {
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

          public class ForkPrContributorApproval internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo)

            public val put: Put = Put(__client, _owner, _repo)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/permissions/fork-pr-contributor-approval")
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
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
            ) {
              public suspend operator fun invoke(body: ActionsForkPrContributorApproval): Response {
                val response = __client.put("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/permissions/fork-pr-contributor-approval") {
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
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo)

            public val put: Put = Put(__client, _owner, _repo)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/permissions/fork-pr-workflows-private-repos")
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
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
            ) {
              public suspend operator fun invoke(body: ActionsForkPrWorkflowsPrivateReposRequest): Response {
                val response = __client.put("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/permissions/fork-pr-workflows-private-repos") {
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

          public class SelectedActions internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo)

            public val put: Put = Put(__client, _owner, _repo)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
            ) {
              public suspend operator fun invoke(): io.github.model.SelectedActions {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/permissions/selected-actions")
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }
            }

            public class Put internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
            ) {
              public suspend operator fun invoke(body: io.github.model.SelectedActions? = null) {
                val response = __client.put("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/permissions/selected-actions") {
                  body?.let {
                    contentType(ContentType.Application.Json)
                    setBody(it)
                  }
                }
                if (response.status.value != 204) throw ResponseException(response, "")
              }
            }
          }

          public class Workflow internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo)

            public val put: Put = Put(__client, _owner, _repo)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
            ) {
              public suspend operator fun invoke(): ActionsGetDefaultWorkflowPermissions {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/permissions/workflow")
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }
            }

            public class Put internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
            ) {
              public suspend operator fun invoke(body: ActionsSetDefaultWorkflowPermissions): Response {
                val response = __client.put("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/permissions/workflow") {
                  contentType(ContentType.Application.Json)
                  setBody(body)
                }
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

        public class Runners internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public val `get`: Get = Get(__client, _owner, _repo)

          public val downloads: Downloads = Downloads(__client, _owner, _repo)

          public val generateJitconfig: GenerateJitconfig =
              GenerateJitconfig(__client, _owner, _repo)

          public val registrationToken: RegistrationToken =
              RegistrationToken(__client, _owner, _repo)

          public val removeToken: RemoveToken = RemoveToken(__client, _owner, _repo)

          public fun runnerId(runnerId: Long): RunnerIdPath = RunnerIdPath(__client, _owner, _repo, runnerId)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public suspend operator fun invoke(
              name: String? = null,
              perPage: Long? = 30L,
              page: Long? = 1L,
            ): Response {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/runners") {
                if (name != null) {
                  this.url.encodedParameters.append("name", name.encodeURLParameter())
                }
                if (perPage != null) {
                  this.url.encodedParameters.append("per_page", perPage.toString())
                }
                if (page != null) {
                  this.url.encodedParameters.append("page", page.toString())
                }
              }
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }

            @Serializable
            public data class Response(
              @SerialName("total_count")
              public val totalCount: Long,
              public val runners: List<Runner>,
            )
          }

          public class Downloads internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
            ) {
              public suspend operator fun invoke(): List<RunnerApplication> {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/runners/downloads")
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }
            }
          }

          public class GenerateJitconfig internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public val post: Post = Post(__client, _owner, _repo)

            public class Post internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
            ) {
              public suspend operator fun invoke(
                name: String,
                runnerGroupId: Long,
                labels: List<String>,
                workFolder: String? = null,
              ): Response {
                val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/runners/generate-jitconfig") {
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

              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              internal data class Body(
                public val name: String,
                @SerialName("runner_group_id")
                public val runnerGroupId: Long,
                public val labels: List<String>,
                @SerialName("work_folder")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
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
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public val post: Post = Post(__client, _owner, _repo)

            public class Post internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
            ) {
              public suspend operator fun invoke(): AuthenticationToken {
                val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/runners/registration-token")
                if (response.status.value != 201) throw ResponseException(response, "")
                return response.body()
              }
            }
          }

          public class RemoveToken internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public val post: Post = Post(__client, _owner, _repo)

            public class Post internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
            ) {
              public suspend operator fun invoke(): AuthenticationToken {
                val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/runners/remove-token")
                if (response.status.value != 201) throw ResponseException(response, "")
                return response.body()
              }
            }
          }

          public class RunnerIdPath internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _runnerId: Long,
          ) {
            public val delete: Delete = Delete(__client, _owner, _repo, _runnerId)

            public val `get`: Get = Get(__client, _owner, _repo, _runnerId)

            public val labels: Labels = Labels(__client, _owner, _repo, _runnerId)

            public class Delete internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _runnerId: Long,
            ) {
              public suspend operator fun invoke(): Response {
                val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/runners/${this._runnerId}")
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
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _runnerId: Long,
            ) {
              public suspend operator fun invoke(): Runner {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/runners/${this._runnerId}")
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }
            }

            public class Labels internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _runnerId: Long,
            ) {
              public val delete: Delete = Delete(__client, _owner, _repo, _runnerId)

              public val `get`: Get = Get(__client, _owner, _repo, _runnerId)

              public val post: Post = Post(__client, _owner, _repo, _runnerId)

              public val put: Put = Put(__client, _owner, _repo, _runnerId)

              public fun name(name: String): NamePath = NamePath(__client, _owner, _repo, _runnerId, name)

              public class Delete internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _runnerId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/runners/${this._runnerId}/labels")
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
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _runnerId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/runners/${this._runnerId}/labels")
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
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _runnerId: Long,
              ) {
                public suspend operator fun invoke(labels: List<String>): Response {
                  val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/runners/${this._runnerId}/labels") {
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

                @Serializable
                internal data class Body(
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
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _runnerId: Long,
              ) {
                public suspend operator fun invoke(labels: List<String>): Response {
                  val response = __client.put("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/runners/${this._runnerId}/labels") {
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

                @Serializable
                internal data class Body(
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
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _runnerId: Long,
                private val _name: String,
              ) {
                public val delete: Delete = Delete(__client, _owner, _repo, _runnerId, _name)

                public class Delete internal constructor(
                  private val __client: HttpClient,
                  private val _owner: String,
                  private val _repo: String,
                  private val _runnerId: Long,
                  private val _name: String,
                ) {
                  public suspend operator fun invoke(): Response {
                    val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/runners/${this._runnerId}/labels/${this._name.encodeURLParameter()}")
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

        public class Runs internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public val `get`: Get = Get(__client, _owner, _repo)

          public fun runId(runId: Long): RunIdPath = RunIdPath(__client, _owner, _repo, runId)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public suspend operator fun invoke(
              actor: String? = null,
              branch: String? = null,
              event: String? = null,
              status: Status? = null,
              perPage: Long? = 30L,
              page: Long? = 1L,
              created: Instant? = null,
              excludePullRequests: Boolean? = false,
              checkSuiteId: Long? = null,
              headSha: String? = null,
            ): Response {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/runs") {
                if (actor != null) {
                  this.url.encodedParameters.append("actor", actor.encodeURLParameter())
                }
                if (branch != null) {
                  this.url.encodedParameters.append("branch", branch.encodeURLParameter())
                }
                if (event != null) {
                  this.url.encodedParameters.append("event", event.encodeURLParameter())
                }
                if (status != null) {
                  this.url.encodedParameters.append("status", status.value.encodeURLParameter())
                }
                if (perPage != null) {
                  this.url.encodedParameters.append("per_page", perPage.toString())
                }
                if (page != null) {
                  this.url.encodedParameters.append("page", page.toString())
                }
                if (created != null) {
                  this.url.encodedParameters.append("created", created.toString().encodeURLParameter())
                }
                if (excludePullRequests != null) {
                  this.url.encodedParameters.append("exclude_pull_requests", excludePullRequests.toString())
                }
                if (checkSuiteId != null) {
                  this.url.encodedParameters.append("check_suite_id", checkSuiteId.toString())
                }
                if (headSha != null) {
                  this.url.encodedParameters.append("head_sha", headSha.encodeURLParameter())
                }
              }
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }

            @Serializable
            public enum class Status(
              public val `value`: String,
            ) {
              @SerialName("completed")
              Completed("completed"),
              @SerialName("action_required")
              ActionRequired("action_required"),
              @SerialName("cancelled")
              Cancelled("cancelled"),
              @SerialName("failure")
              Failure("failure"),
              @SerialName("neutral")
              Neutral("neutral"),
              @SerialName("skipped")
              Skipped("skipped"),
              @SerialName("stale")
              Stale("stale"),
              @SerialName("success")
              Success("success"),
              @SerialName("timed_out")
              TimedOut("timed_out"),
              @SerialName("in_progress")
              InProgress("in_progress"),
              @SerialName("queued")
              Queued("queued"),
              @SerialName("requested")
              Requested("requested"),
              @SerialName("waiting")
              Waiting("waiting"),
              @SerialName("pending")
              Pending("pending"),
              ;
            }

            @Serializable
            public data class Response(
              @SerialName("total_count")
              public val totalCount: Long,
              @SerialName("workflow_runs")
              public val workflowRuns: List<WorkflowRun>,
            )
          }

          public class RunIdPath internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _runId: Long,
          ) {
            public val delete: Delete = Delete(__client, _owner, _repo, _runId)

            public val `get`: Get = Get(__client, _owner, _repo, _runId)

            public val approvals: Approvals = Approvals(__client, _owner, _repo, _runId)

            public val approve: Approve = Approve(__client, _owner, _repo, _runId)

            public val artifacts: Artifacts = Artifacts(__client, _owner, _repo, _runId)

            public val attempts: Attempts = Attempts(__client, _owner, _repo, _runId)

            public val cancel: Cancel = Cancel(__client, _owner, _repo, _runId)

            public val deploymentProtectionRule: DeploymentProtectionRule =
                DeploymentProtectionRule(__client, _owner, _repo, _runId)

            public val forceCancel: ForceCancel = ForceCancel(__client, _owner, _repo, _runId)

            public val jobs: Jobs = Jobs(__client, _owner, _repo, _runId)

            public val logs: Logs = Logs(__client, _owner, _repo, _runId)

            public val pendingDeployments: PendingDeployments =
                PendingDeployments(__client, _owner, _repo, _runId)

            public val rerun: Rerun = Rerun(__client, _owner, _repo, _runId)

            public val rerunFailedJobs: RerunFailedJobs =
                RerunFailedJobs(__client, _owner, _repo, _runId)

            public val timing: Timing = Timing(__client, _owner, _repo, _runId)

            public class Delete internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _runId: Long,
            ) {
              public suspend operator fun invoke() {
                val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/runs/${this._runId}")
                if (response.status.value != 204) throw ResponseException(response, "")
              }
            }

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _runId: Long,
            ) {
              public suspend operator fun invoke(excludePullRequests: Boolean? = false): WorkflowRun {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/runs/${this._runId}") {
                  if (excludePullRequests != null) {
                    this.url.encodedParameters.append("exclude_pull_requests", excludePullRequests.toString())
                  }
                }
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }
            }

            public class Approvals internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _runId: Long,
            ) {
              public val `get`: Get = Get(__client, _owner, _repo, _runId)

              public class Get internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _runId: Long,
              ) {
                public suspend operator fun invoke(): List<EnvironmentApprovals> {
                  val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/runs/${this._runId}/approvals")
                  if (response.status.value != 200) throw ResponseException(response, "")
                  return response.body()
                }
              }
            }

            public class Approve internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _runId: Long,
            ) {
              public val post: Post = Post(__client, _owner, _repo, _runId)

              public class Post internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _runId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/runs/${this._runId}/approve")
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

            public class Artifacts internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _runId: Long,
            ) {
              public val `get`: Get = Get(__client, _owner, _repo, _runId)

              public class Get internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _runId: Long,
              ) {
                public suspend operator fun invoke(
                  perPage: Long? = 30L,
                  page: Long? = 1L,
                  name: String? = null,
                  direction: Direction? = Direction.Desc,
                ): Response {
                  val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/runs/${this._runId}/artifacts") {
                    if (perPage != null) {
                      this.url.encodedParameters.append("per_page", perPage.toString())
                    }
                    if (page != null) {
                      this.url.encodedParameters.append("page", page.toString())
                    }
                    if (name != null) {
                      this.url.encodedParameters.append("name", name.encodeURLParameter())
                    }
                    if (direction != null) {
                      this.url.encodedParameters.append("direction", direction.value.encodeURLParameter())
                    }
                  }
                  if (response.status.value != 200) throw ResponseException(response, "")
                  return response.body()
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
                public data class Response(
                  @SerialName("total_count")
                  public val totalCount: Long,
                  public val artifacts: List<Artifact>,
                )
              }
            }

            public class Attempts internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _runId: Long,
            ) {
              public fun attemptNumber(attemptNumber: Long): AttemptNumberPath = AttemptNumberPath(__client, _owner, _repo, _runId, attemptNumber)

              public class AttemptNumberPath internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _runId: Long,
                private val _attemptNumber: Long,
              ) {
                public val `get`: Get = Get(__client, _owner, _repo, _runId, _attemptNumber)

                public val jobs: Jobs = Jobs(__client, _owner, _repo, _runId, _attemptNumber)

                public val logs: Logs = Logs(__client, _owner, _repo, _runId, _attemptNumber)

                public class Get internal constructor(
                  private val __client: HttpClient,
                  private val _owner: String,
                  private val _repo: String,
                  private val _runId: Long,
                  private val _attemptNumber: Long,
                ) {
                  public suspend operator fun invoke(excludePullRequests: Boolean? = false): WorkflowRun {
                    val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/runs/${this._runId}/attempts/${this._attemptNumber}") {
                      if (excludePullRequests != null) {
                        this.url.encodedParameters.append("exclude_pull_requests", excludePullRequests.toString())
                      }
                    }
                    if (response.status.value != 200) throw ResponseException(response, "")
                    return response.body()
                  }
                }

                public class Jobs internal constructor(
                  private val __client: HttpClient,
                  private val _owner: String,
                  private val _repo: String,
                  private val _runId: Long,
                  private val _attemptNumber: Long,
                ) {
                  public val `get`: Get = Get(__client, _owner, _repo, _runId, _attemptNumber)

                  public class Get internal constructor(
                    private val __client: HttpClient,
                    private val _owner: String,
                    private val _repo: String,
                    private val _runId: Long,
                    private val _attemptNumber: Long,
                  ) {
                    public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
                      val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/runs/${this._runId}/attempts/${this._attemptNumber}/jobs") {
                        if (perPage != null) {
                          this.url.encodedParameters.append("per_page", perPage.toString())
                        }
                        if (page != null) {
                          this.url.encodedParameters.append("page", page.toString())
                        }
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
                        public val jobs: List<Job>,
                      ) : Response

                      public data class NotFound(
                        public val `value`: BasicError,
                      ) : Response
                    }
                  }
                }

                public class Logs internal constructor(
                  private val __client: HttpClient,
                  private val _owner: String,
                  private val _repo: String,
                  private val _runId: Long,
                  private val _attemptNumber: Long,
                ) {
                  public val `get`: Get = Get(__client, _owner, _repo, _runId, _attemptNumber)

                  public class Get internal constructor(
                    private val __client: HttpClient,
                    private val _owner: String,
                    private val _repo: String,
                    private val _runId: Long,
                    private val _attemptNumber: Long,
                  ) {
                    public suspend operator fun invoke() {
                      val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/runs/${this._runId}/attempts/${this._attemptNumber}/logs")
                      if (response.status.value != 302) throw ResponseException(response, "")
                    }
                  }
                }
              }
            }

            public class Cancel internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _runId: Long,
            ) {
              public val post: Post = Post(__client, _owner, _repo, _runId)

              public class Post internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _runId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/runs/${this._runId}/cancel")
                  return when (response.status.value) {
                    202 -> Response.Accepted(response.body())
                    409 -> Response.Conflict(response.body())
                    else -> throw ResponseException(response, "")
                  }
                }

                public sealed interface Response {
                  public data class Accepted(
                    public val `value`: EmptyObject,
                  ) : Response

                  public data class Conflict(
                    public val `value`: BasicError,
                  ) : Response
                }
              }
            }

            public class DeploymentProtectionRule internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _runId: Long,
            ) {
              public val post: Post = Post(__client, _owner, _repo, _runId)

              public class Post internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _runId: Long,
              ) {
                public suspend operator fun invoke(body: ReviewCustomGatesCommentRequired) {
                  val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/runs/${this._runId}/deployment_protection_rule") {
                    contentType(ContentType.Application.Json)
                    setBody(body)
                  }
                  if (response.status.value != 204) throw ResponseException(response, "")
                }

                public suspend operator fun invoke(body: ReviewCustomGatesStateRequired) {
                  val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/runs/${this._runId}/deployment_protection_rule") {
                    contentType(ContentType.Application.Json)
                    setBody(body)
                  }
                  if (response.status.value != 204) throw ResponseException(response, "")
                }
              }
            }

            public class ForceCancel internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _runId: Long,
            ) {
              public val post: Post = Post(__client, _owner, _repo, _runId)

              public class Post internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _runId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/runs/${this._runId}/force-cancel")
                  return when (response.status.value) {
                    202 -> Response.Accepted(response.body())
                    409 -> Response.Conflict(response.body())
                    else -> throw ResponseException(response, "")
                  }
                }

                public sealed interface Response {
                  public data class Accepted(
                    public val `value`: EmptyObject,
                  ) : Response

                  public data class Conflict(
                    public val `value`: BasicError,
                  ) : Response
                }
              }
            }

            public class Jobs internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _runId: Long,
            ) {
              public val `get`: Get = Get(__client, _owner, _repo, _runId)

              public class Get internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _runId: Long,
              ) {
                public suspend operator fun invoke(
                  filter: Filter? = Filter.Latest,
                  perPage: Long? = 30L,
                  page: Long? = 1L,
                ): Response {
                  val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/runs/${this._runId}/jobs") {
                    if (filter != null) {
                      this.url.encodedParameters.append("filter", filter.value.encodeURLParameter())
                    }
                    if (perPage != null) {
                      this.url.encodedParameters.append("per_page", perPage.toString())
                    }
                    if (page != null) {
                      this.url.encodedParameters.append("page", page.toString())
                    }
                  }
                  if (response.status.value != 200) throw ResponseException(response, "")
                  return response.body()
                }

                @Serializable
                public enum class Filter(
                  public val `value`: String,
                ) {
                  @SerialName("latest")
                  Latest("latest"),
                  @SerialName("all")
                  All("all"),
                  ;
                }

                @Serializable
                public data class Response(
                  @SerialName("total_count")
                  public val totalCount: Long,
                  public val jobs: List<Job>,
                )
              }
            }

            public class Logs internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _runId: Long,
            ) {
              public val delete: Delete = Delete(__client, _owner, _repo, _runId)

              public val `get`: Get = Get(__client, _owner, _repo, _runId)

              public class Delete internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _runId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/runs/${this._runId}/logs")
                  return when (response.status.value) {
                    204 -> Response.NoContent
                    403 -> Response.Forbidden(response.body())
                    500 -> Response.InternalServerError(response.body())
                    else -> throw ResponseException(response, "")
                  }
                }

                public sealed interface Response {
                  public data object NoContent : Response

                  public data class Forbidden(
                    public val `value`: BasicError,
                  ) : Response

                  public data class InternalServerError(
                    public val `value`: BasicError,
                  ) : Response
                }
              }

              public class Get internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _runId: Long,
              ) {
                public suspend operator fun invoke() {
                  val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/runs/${this._runId}/logs")
                  if (response.status.value != 302) throw ResponseException(response, "")
                }
              }
            }

            public class PendingDeployments internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _runId: Long,
            ) {
              public val `get`: Get = Get(__client, _owner, _repo, _runId)

              public val post: Post = Post(__client, _owner, _repo, _runId)

              public class Get internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _runId: Long,
              ) {
                public suspend operator fun invoke(): List<PendingDeployment> {
                  val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/runs/${this._runId}/pending_deployments")
                  if (response.status.value != 200) throw ResponseException(response, "")
                  return response.body()
                }
              }

              public class Post internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _runId: Long,
              ) {
                public suspend operator fun invoke(
                  environmentIds: List<Long>,
                  state: State,
                  comment: String,
                ): List<Deployment> {
                  val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/runs/${this._runId}/pending_deployments") {
                    contentType(ContentType.Application.Json)
                    setBody(Body(environmentIds = environmentIds, state = state, comment = comment))
                  }
                  if (response.status.value != 200) throw ResponseException(response, "")
                  return response.body()
                }

                @Serializable
                public enum class State(
                  public val `value`: String,
                ) {
                  @SerialName("approved")
                  Approved("approved"),
                  @SerialName("rejected")
                  Rejected("rejected"),
                  ;
                }

                @Serializable
                internal data class Body(
                  @SerialName("environment_ids")
                  public val environmentIds: List<Long>,
                  public val state: State,
                  public val comment: String,
                )
              }
            }

            public class Rerun internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _runId: Long,
            ) {
              public val post: Post = Post(__client, _owner, _repo, _runId)

              public class Post internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _runId: Long,
              ) {
                public suspend operator fun invoke(): EmptyObject {
                  val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/runs/${this._runId}/rerun")
                  if (response.status.value != 201) throw ResponseException(response, "")
                  return response.body()
                }

                public suspend operator fun invoke(enableDebugLogging: Boolean? = null): EmptyObject {
                  val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/runs/${this._runId}/rerun") {
                    contentType(ContentType.Application.Json)
                    setBody(Body(enableDebugLogging = enableDebugLogging))
                  }
                  if (response.status.value != 201) throw ResponseException(response, "")
                  return response.body()
                }

                @OptIn(ExperimentalSerializationApi::class)
                @Serializable
                internal data class Body(
                  @SerialName("enable_debug_logging")
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val enableDebugLogging: Boolean? = null,
                )
              }
            }

            public class RerunFailedJobs internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _runId: Long,
            ) {
              public val post: Post = Post(__client, _owner, _repo, _runId)

              public class Post internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _runId: Long,
              ) {
                public suspend operator fun invoke(): EmptyObject {
                  val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/runs/${this._runId}/rerun-failed-jobs")
                  if (response.status.value != 201) throw ResponseException(response, "")
                  return response.body()
                }

                public suspend operator fun invoke(enableDebugLogging: Boolean? = null): EmptyObject {
                  val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/runs/${this._runId}/rerun-failed-jobs") {
                    contentType(ContentType.Application.Json)
                    setBody(Body(enableDebugLogging = enableDebugLogging))
                  }
                  if (response.status.value != 201) throw ResponseException(response, "")
                  return response.body()
                }

                @OptIn(ExperimentalSerializationApi::class)
                @Serializable
                internal data class Body(
                  @SerialName("enable_debug_logging")
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val enableDebugLogging: Boolean? = null,
                )
              }
            }

            public class Timing internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _runId: Long,
            ) {
              public val `get`: Get = Get(__client, _owner, _repo, _runId)

              public class Get internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _runId: Long,
              ) {
                public suspend operator fun invoke(): WorkflowRunUsage {
                  val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/runs/${this._runId}/timing")
                  if (response.status.value != 200) throw ResponseException(response, "")
                  return response.body()
                }
              }
            }
          }
        }

        public class Secrets internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public val `get`: Get = Get(__client, _owner, _repo)

          public val publicKey: PublicKey = PublicKey(__client, _owner, _repo)

          public fun secretName(secretName: String): SecretNamePath = SecretNamePath(__client, _owner, _repo, secretName)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/secrets") {
                if (perPage != null) {
                  this.url.encodedParameters.append("per_page", perPage.toString())
                }
                if (page != null) {
                  this.url.encodedParameters.append("page", page.toString())
                }
              }
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }

            @Serializable
            public data class Response(
              @SerialName("total_count")
              public val totalCount: Long,
              public val secrets: List<ActionsSecret>,
            )
          }

          public class PublicKey internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
            ) {
              public suspend operator fun invoke(): ActionsPublicKey {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/secrets/public-key")
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }
            }
          }

          public class SecretNamePath internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _secretName: String,
          ) {
            public val delete: Delete = Delete(__client, _owner, _repo, _secretName)

            public val `get`: Get = Get(__client, _owner, _repo, _secretName)

            public val put: Put = Put(__client, _owner, _repo, _secretName)

            public class Delete internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _secretName: String,
            ) {
              public suspend operator fun invoke() {
                val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/secrets/${this._secretName.encodeURLParameter()}")
                if (response.status.value != 204) throw ResponseException(response, "")
              }
            }

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _secretName: String,
            ) {
              public suspend operator fun invoke(): ActionsSecret {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/secrets/${this._secretName.encodeURLParameter()}")
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }
            }

            public class Put internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _secretName: String,
            ) {
              public suspend operator fun invoke(encryptedValue: String, keyId: String): Response {
                val response = __client.put("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/secrets/${this._secretName.encodeURLParameter()}") {
                  contentType(ContentType.Application.Json)
                  setBody(Body(encryptedValue = encryptedValue, keyId = keyId))
                }
                return when (response.status.value) {
                  201 -> Response.Created(response.body())
                  204 -> Response.NoContent
                  else -> throw ResponseException(response, "")
                }
              }

              @Serializable
              internal data class Body(
                @SerialName("encrypted_value")
                public val encryptedValue: String,
                @SerialName("key_id")
                public val keyId: String,
              )

              public sealed interface Response {
                public data class Created(
                  public val `value`: EmptyObject,
                ) : Response

                public data object NoContent : Response
              }
            }
          }
        }

        public class Variables internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public val `get`: Get = Get(__client, _owner, _repo)

          public val post: Post = Post(__client, _owner, _repo)

          public fun name(name: String): NamePath = NamePath(__client, _owner, _repo, name)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public suspend operator fun invoke(perPage: Long? = 10L, page: Long? = 1L): Response {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/variables") {
                if (perPage != null) {
                  this.url.encodedParameters.append("per_page", perPage.toString())
                }
                if (page != null) {
                  this.url.encodedParameters.append("page", page.toString())
                }
              }
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }

            @Serializable
            public data class Response(
              @SerialName("total_count")
              public val totalCount: Long,
              public val variables: List<ActionsVariable>,
            )
          }

          public class Post internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public suspend operator fun invoke(name: String, `value`: String): EmptyObject {
              val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/variables") {
                contentType(ContentType.Application.Json)
                setBody(Body(name = name, value = value))
              }
              if (response.status.value != 201) throw ResponseException(response, "")
              return response.body()
            }

            @Serializable
            internal data class Body(
              public val name: String,
              public val `value`: String,
            )
          }

          public class NamePath internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _name: String,
          ) {
            public val delete: Delete = Delete(__client, _owner, _repo, _name)

            public val `get`: Get = Get(__client, _owner, _repo, _name)

            public val patch: Patch = Patch(__client, _owner, _repo, _name)

            public class Delete internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _name: String,
            ) {
              public suspend operator fun invoke() {
                val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/variables/${this._name.encodeURLParameter()}")
                if (response.status.value != 204) throw ResponseException(response, "")
              }
            }

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _name: String,
            ) {
              public suspend operator fun invoke(): ActionsVariable {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/variables/${this._name.encodeURLParameter()}")
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }
            }

            public class Patch internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _name: String,
            ) {
              public suspend operator fun invoke(name: String? = null, `value`: String? = null) {
                val response = __client.patch("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/variables/${this._name.encodeURLParameter()}") {
                  contentType(ContentType.Application.Json)
                  setBody(Body(name = name, value = value))
                }
                if (response.status.value != 204) throw ResponseException(response, "")
              }

              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              internal data class Body(
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val name: String? = null,
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val `value`: String? = null,
              )
            }
          }
        }

        public class Workflows internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public val `get`: Get = Get(__client, _owner, _repo)

          public fun workflowId(workflowId: Long): WorkflowIdPath = WorkflowIdPath(__client, _owner, _repo, workflowId.toString())

          public fun workflowId(workflowId: String): WorkflowIdPath = WorkflowIdPath(__client, _owner, _repo, workflowId)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/workflows") {
                if (perPage != null) {
                  this.url.encodedParameters.append("per_page", perPage.toString())
                }
                if (page != null) {
                  this.url.encodedParameters.append("page", page.toString())
                }
              }
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }

            @Serializable
            public data class Response(
              @SerialName("total_count")
              public val totalCount: Long,
              public val workflows: List<Workflow>,
            )
          }

          public class WorkflowIdPath internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _workflowId: String,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo, _workflowId)

            public val disable: Disable = Disable(__client, _owner, _repo, _workflowId)

            public val dispatches: Dispatches = Dispatches(__client, _owner, _repo, _workflowId)

            public val enable: Enable = Enable(__client, _owner, _repo, _workflowId)

            public val runs: Runs = Runs(__client, _owner, _repo, _workflowId)

            public val timing: Timing = Timing(__client, _owner, _repo, _workflowId)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _workflowId: String,
            ) {
              public suspend operator fun invoke(): Workflow {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/workflows/${this._workflowId.encodeURLParameter()}")
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }
            }

            public class Disable internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _workflowId: String,
            ) {
              public val put: Put = Put(__client, _owner, _repo, _workflowId)

              public class Put internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _workflowId: String,
              ) {
                public suspend operator fun invoke() {
                  val response = __client.put("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/workflows/${this._workflowId.encodeURLParameter()}/disable")
                  if (response.status.value != 204) throw ResponseException(response, "")
                }
              }
            }

            public class Dispatches internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _workflowId: String,
            ) {
              public val post: Post = Post(__client, _owner, _repo, _workflowId)

              public class Post internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _workflowId: String,
              ) {
                public suspend operator fun invoke(
                  ref: String,
                  inputs: JsonElement? = null,
                  returnRunDetails: Boolean? = null,
                ): Response {
                  val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/workflows/${this._workflowId.encodeURLParameter()}/dispatches") {
                    contentType(ContentType.Application.Json)
                    setBody(Body(ref = ref, inputs = inputs, returnRunDetails = returnRunDetails))
                  }
                  return when (response.status.value) {
                    200 -> Response.Ok(response.body())
                    204 -> Response.NoContent
                    else -> throw ResponseException(response, "")
                  }
                }

                @OptIn(ExperimentalSerializationApi::class)
                @Serializable
                internal data class Body(
                  public val ref: String,
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val inputs: JsonElement? = null,
                  @SerialName("return_run_details")
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val returnRunDetails: Boolean? = null,
                )

                public sealed interface Response {
                  public data class Ok(
                    public val `value`: WorkflowDispatchResponse,
                  ) : Response

                  public data object NoContent : Response
                }
              }
            }

            public class Enable internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _workflowId: String,
            ) {
              public val put: Put = Put(__client, _owner, _repo, _workflowId)

              public class Put internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _workflowId: String,
              ) {
                public suspend operator fun invoke() {
                  val response = __client.put("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/workflows/${this._workflowId.encodeURLParameter()}/enable")
                  if (response.status.value != 204) throw ResponseException(response, "")
                }
              }
            }

            public class Runs internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _workflowId: String,
            ) {
              public val `get`: Get = Get(__client, _owner, _repo, _workflowId)

              public class Get internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _workflowId: String,
              ) {
                public suspend operator fun invoke(
                  actor: String? = null,
                  branch: String? = null,
                  event: String? = null,
                  status: Status? = null,
                  perPage: Long? = 30L,
                  page: Long? = 1L,
                  created: Instant? = null,
                  excludePullRequests: Boolean? = false,
                  checkSuiteId: Long? = null,
                  headSha: String? = null,
                ): Response {
                  val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/workflows/${this._workflowId.encodeURLParameter()}/runs") {
                    if (actor != null) {
                      this.url.encodedParameters.append("actor", actor.encodeURLParameter())
                    }
                    if (branch != null) {
                      this.url.encodedParameters.append("branch", branch.encodeURLParameter())
                    }
                    if (event != null) {
                      this.url.encodedParameters.append("event", event.encodeURLParameter())
                    }
                    if (status != null) {
                      this.url.encodedParameters.append("status", status.value.encodeURLParameter())
                    }
                    if (perPage != null) {
                      this.url.encodedParameters.append("per_page", perPage.toString())
                    }
                    if (page != null) {
                      this.url.encodedParameters.append("page", page.toString())
                    }
                    if (created != null) {
                      this.url.encodedParameters.append("created", created.toString().encodeURLParameter())
                    }
                    if (excludePullRequests != null) {
                      this.url.encodedParameters.append("exclude_pull_requests", excludePullRequests.toString())
                    }
                    if (checkSuiteId != null) {
                      this.url.encodedParameters.append("check_suite_id", checkSuiteId.toString())
                    }
                    if (headSha != null) {
                      this.url.encodedParameters.append("head_sha", headSha.encodeURLParameter())
                    }
                  }
                  if (response.status.value != 200) throw ResponseException(response, "")
                  return response.body()
                }

                @Serializable
                public enum class Status(
                  public val `value`: String,
                ) {
                  @SerialName("completed")
                  Completed("completed"),
                  @SerialName("action_required")
                  ActionRequired("action_required"),
                  @SerialName("cancelled")
                  Cancelled("cancelled"),
                  @SerialName("failure")
                  Failure("failure"),
                  @SerialName("neutral")
                  Neutral("neutral"),
                  @SerialName("skipped")
                  Skipped("skipped"),
                  @SerialName("stale")
                  Stale("stale"),
                  @SerialName("success")
                  Success("success"),
                  @SerialName("timed_out")
                  TimedOut("timed_out"),
                  @SerialName("in_progress")
                  InProgress("in_progress"),
                  @SerialName("queued")
                  Queued("queued"),
                  @SerialName("requested")
                  Requested("requested"),
                  @SerialName("waiting")
                  Waiting("waiting"),
                  @SerialName("pending")
                  Pending("pending"),
                  ;
                }

                @Serializable
                public data class Response(
                  @SerialName("total_count")
                  public val totalCount: Long,
                  @SerialName("workflow_runs")
                  public val workflowRuns: List<WorkflowRun>,
                )
              }
            }

            public class Timing internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _workflowId: String,
            ) {
              public val `get`: Get = Get(__client, _owner, _repo, _workflowId)

              public class Get internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _workflowId: String,
              ) {
                public suspend operator fun invoke(): WorkflowUsage {
                  val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/actions/workflows/${this._workflowId.encodeURLParameter()}/timing")
                  if (response.status.value != 200) throw ResponseException(response, "")
                  return response.body()
                }
              }
            }
          }
        }
      }

      public class Activity internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public val `get`: Get = Get(__client, _owner, _repo)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(
            direction: Direction? = Direction.Desc,
            perPage: Long? = 30L,
            before: String? = null,
            after: String? = null,
            ref: String? = null,
            actor: String? = null,
            timePeriod: TimePeriod? = null,
            activityType: ActivityType? = null,
          ): Response {
            val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/activity") {
              if (direction != null) {
                this.url.encodedParameters.append("direction", direction.value.encodeURLParameter())
              }
              if (perPage != null) {
                this.url.encodedParameters.append("per_page", perPage.toString())
              }
              if (before != null) {
                this.url.encodedParameters.append("before", before.encodeURLParameter())
              }
              if (after != null) {
                this.url.encodedParameters.append("after", after.encodeURLParameter())
              }
              if (ref != null) {
                this.url.encodedParameters.append("ref", ref.encodeURLParameter())
              }
              if (actor != null) {
                this.url.encodedParameters.append("actor", actor.encodeURLParameter())
              }
              if (timePeriod != null) {
                this.url.encodedParameters.append("time_period", timePeriod.value.encodeURLParameter())
              }
              if (activityType != null) {
                this.url.encodedParameters.append("activity_type", activityType.value.encodeURLParameter())
              }
            }
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              422 -> Response.UnprocessableEntity(response.body())
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
          public enum class TimePeriod(
            public val `value`: String,
          ) {
            @SerialName("day")
            Day("day"),
            @SerialName("week")
            Week("week"),
            @SerialName("month")
            Month("month"),
            @SerialName("quarter")
            Quarter("quarter"),
            @SerialName("year")
            Year("year"),
            ;
          }

          @Serializable
          public enum class ActivityType(
            public val `value`: String,
          ) {
            @SerialName("push")
            Push("push"),
            @SerialName("force_push")
            ForcePush("force_push"),
            @SerialName("branch_creation")
            BranchCreation("branch_creation"),
            @SerialName("branch_deletion")
            BranchDeletion("branch_deletion"),
            @SerialName("pr_merge")
            PrMerge("pr_merge"),
            @SerialName("merge_queue_merge")
            MergeQueueMerge("merge_queue_merge"),
            ;
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: List<io.github.model.Activity>,
            ) : Response

            public data class UnprocessableEntity(
              public val `value`: ValidationErrorSimple,
            ) : Response
          }
        }
      }

      public class Assignees internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public val `get`: Get = Get(__client, _owner, _repo)

        public fun assignee(assignee: String): AssigneePath = AssigneePath(__client, _owner, _repo, assignee)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
            val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/assignees") {
              if (perPage != null) {
                this.url.encodedParameters.append("per_page", perPage.toString())
              }
              if (page != null) {
                this.url.encodedParameters.append("page", page.toString())
              }
            }
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              404 -> Response.NotFound(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: List<SimpleUser>,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class AssigneePath internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
          private val _assignee: String,
        ) {
          public val `get`: Get = Get(__client, _owner, _repo, _assignee)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _assignee: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/assignees/${this._assignee.encodeURLParameter()}")
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

      public class Attestations internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public val post: Post = Post(__client, _owner, _repo)

        public fun subjectDigest(subjectDigest: String): SubjectDigestPath = SubjectDigestPath(__client, _owner, _repo, subjectDigest)

        public class Post internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(bundle: Bundle): Response {
            val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/attestations") {
              contentType(ContentType.Application.Json)
              setBody(Body(bundle = bundle))
            }
            return when (response.status.value) {
              201 -> response.body<Response.Created>()
              403 -> Response.Forbidden(response.body())
              422 -> Response.UnprocessableEntity(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          /**
           * The attestation's Sigstore Bundle.
           * Refer to the [Sigstore Bundle Specification](https://github.com/sigstore/protobuf-specs/blob/main/protos/sigstore_bundle.proto) for more information.
           */
          @OptIn(ExperimentalSerializationApi::class)
          @Serializable
          public data class Bundle(
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val mediaType: String? = null,
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val verificationMaterial: JsonElement? = null,
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val dsseEnvelope: JsonElement? = null,
          )

          @Serializable
          internal data class Body(
            public val bundle: Bundle,
          )

          public sealed interface Response {
            @OptIn(ExperimentalSerializationApi::class)
            @Serializable
            public data class Created(
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val id: Long? = null,
            ) : Response

            public data class Forbidden(
              public val `value`: BasicError,
            ) : Response

            public data class UnprocessableEntity(
              public val `value`: ValidationError,
            ) : Response
          }
        }

        public class SubjectDigestPath internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
          private val _subjectDigest: String,
        ) {
          public val `get`: Get = Get(__client, _owner, _repo, _subjectDigest)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _subjectDigest: String,
          ) {
            public suspend operator fun invoke(
              perPage: Long? = 30L,
              before: String? = null,
              after: String? = null,
              predicateType: String? = null,
            ): Response {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/attestations/${this._subjectDigest.encodeURLParameter()}") {
                if (perPage != null) {
                  this.url.encodedParameters.append("per_page", perPage.toString())
                }
                if (before != null) {
                  this.url.encodedParameters.append("before", before.encodeURLParameter())
                }
                if (after != null) {
                  this.url.encodedParameters.append("after", after.encodeURLParameter())
                }
                if (predicateType != null) {
                  this.url.encodedParameters.append("predicate_type", predicateType.encodeURLParameter())
                }
              }
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }

            @OptIn(ExperimentalSerializationApi::class)
            @Serializable
            public data class Response(
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val attestations: List<Attestations>? = null,
            ) {
              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              public data class Attestations(
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val bundle: Bundle? = null,
                @SerialName("repository_id")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val repositoryId: Long? = null,
                @SerialName("bundle_url")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val bundleUrl: String? = null,
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val initiator: String? = null,
              ) {
                /**
                 * The attestation's Sigstore Bundle.
                 * Refer to the [Sigstore Bundle Specification](https://github.com/sigstore/protobuf-specs/blob/main/protos/sigstore_bundle.proto) for more information.
                 */
                @OptIn(ExperimentalSerializationApi::class)
                @Serializable
                public data class Bundle(
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val mediaType: String? = null,
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val verificationMaterial: JsonElement? = null,
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val dsseEnvelope: JsonElement? = null,
                )
              }
            }
          }
        }
      }

      public class Autolinks internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public val `get`: Get = Get(__client, _owner, _repo)

        public val post: Post = Post(__client, _owner, _repo)

        public fun autolinkId(autolinkId: Long): AutolinkIdPath = AutolinkIdPath(__client, _owner, _repo, autolinkId)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(): List<Autolink> {
            val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/autolinks")
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class Post internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(
            keyPrefix: String,
            urlTemplate: String,
            isAlphanumeric: Boolean? = null,
          ): Response {
            val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/autolinks") {
              contentType(ContentType.Application.Json)
              setBody(Body(keyPrefix = keyPrefix, urlTemplate = urlTemplate, isAlphanumeric = isAlphanumeric))
            }
            return when (response.status.value) {
              201 -> Response.Created(response.body())
              422 -> Response.UnprocessableEntity(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          @OptIn(ExperimentalSerializationApi::class)
          @Serializable
          internal data class Body(
            @SerialName("key_prefix")
            public val keyPrefix: String,
            @SerialName("url_template")
            public val urlTemplate: String,
            @SerialName("is_alphanumeric")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val isAlphanumeric: Boolean? = null,
          )

          public sealed interface Response {
            public data class Created(
              public val `value`: Autolink,
            ) : Response

            public data class UnprocessableEntity(
              public val `value`: ValidationError,
            ) : Response
          }
        }

        public class AutolinkIdPath internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
          private val _autolinkId: Long,
        ) {
          public val delete: Delete = Delete(__client, _owner, _repo, _autolinkId)

          public val `get`: Get = Get(__client, _owner, _repo, _autolinkId)

          public class Delete internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _autolinkId: Long,
          ) {
            public suspend operator fun invoke(): Response {
              val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/autolinks/${this._autolinkId}")
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
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _autolinkId: Long,
          ) {
            public suspend operator fun invoke(): Response {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/autolinks/${this._autolinkId}")
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: Autolink,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response
            }
          }
        }
      }

      public class AutomatedSecurityFixes internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public val delete: Delete = Delete(__client, _owner, _repo)

        public val `get`: Get = Get(__client, _owner, _repo)

        public val put: Put = Put(__client, _owner, _repo)

        public class Delete internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke() {
            val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/automated-security-fixes")
            if (response.status.value != 204) throw ResponseException(response, "")
          }
        }

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/automated-security-fixes")
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              404 -> Response.NotFound
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: CheckAutomatedSecurityFixes,
            ) : Response

            public data object NotFound : Response
          }
        }

        public class Put internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke() {
            val response = __client.put("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/automated-security-fixes")
            if (response.status.value != 204) throw ResponseException(response, "")
          }
        }
      }

      public class Branches internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public val `get`: Get = Get(__client, _owner, _repo)

        public fun branch(branch: String): BranchPath = BranchPath(__client, _owner, _repo, branch)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(
            `protected`: Boolean? = null,
            perPage: Long? = 30L,
            page: Long? = 1L,
          ): Response {
            val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/branches") {
              if (protected != null) {
                this.url.encodedParameters.append("protected", protected.toString())
              }
              if (perPage != null) {
                this.url.encodedParameters.append("per_page", perPage.toString())
              }
              if (page != null) {
                this.url.encodedParameters.append("page", page.toString())
              }
            }
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              404 -> Response.NotFound(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: List<ShortBranch>,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class BranchPath internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
          private val _branch: String,
        ) {
          public val `get`: Get = Get(__client, _owner, _repo, _branch)

          public val protection: Protection = Protection(__client, _owner, _repo, _branch)

          public val rename: Rename = Rename(__client, _owner, _repo, _branch)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _branch: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/branches/${this._branch.encodeURLParameter()}")
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                301 -> Response.MovedPermanently(response.body())
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: BranchWithProtection,
              ) : Response

              public data class MovedPermanently(
                public val `value`: BasicError,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response
            }
          }

          public class Protection internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _branch: String,
          ) {
            public val delete: Delete = Delete(__client, _owner, _repo, _branch)

            public val `get`: Get = Get(__client, _owner, _repo, _branch)

            public val put: Put = Put(__client, _owner, _repo, _branch)

            public val enforceAdmins: EnforceAdmins =
                EnforceAdmins(__client, _owner, _repo, _branch)

            public val requiredPullRequestReviews: RequiredPullRequestReviews =
                RequiredPullRequestReviews(__client, _owner, _repo, _branch)

            public val requiredSignatures: RequiredSignatures =
                RequiredSignatures(__client, _owner, _repo, _branch)

            public val requiredStatusChecks: RequiredStatusChecks =
                RequiredStatusChecks(__client, _owner, _repo, _branch)

            public val restrictions: Restrictions = Restrictions(__client, _owner, _repo, _branch)

            public class Delete internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _branch: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/branches/${this._branch.encodeURLParameter()}/protection")
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
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _branch: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/branches/${this._branch.encodeURLParameter()}/protection")
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  404 -> Response.NotFound(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: BranchProtection,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response
              }
            }

            public class Put internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _branch: String,
            ) {
              public suspend operator fun invoke(
                requiredStatusChecks: RequiredStatusChecks?,
                enforceAdmins: Boolean?,
                requiredPullRequestReviews: RequiredPullRequestReviews?,
                restrictions: Restrictions?,
                requiredLinearHistory: Boolean? = null,
                allowForcePushes: Optional<Boolean> = Optional.Absent,
                allowDeletions: Boolean? = null,
                blockCreations: Boolean? = null,
                requiredConversationResolution: Boolean? = null,
                lockBranch: Boolean? = null,
                allowForkSyncing: Boolean? = null,
              ): Response {
                val response = __client.put("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/branches/${this._branch.encodeURLParameter()}/protection") {
                  contentType(ContentType.Application.Json)
                  setBody(Body(requiredStatusChecks = requiredStatusChecks, enforceAdmins = enforceAdmins, requiredPullRequestReviews = requiredPullRequestReviews, restrictions = restrictions, requiredLinearHistory = requiredLinearHistory, allowForcePushes = allowForcePushes, allowDeletions = allowDeletions, blockCreations = blockCreations, requiredConversationResolution = requiredConversationResolution, lockBranch = lockBranch, allowForkSyncing = allowForkSyncing))
                }
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  422 -> Response.UnprocessableEntity(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              /**
               * Require status checks to pass before merging. Set to `null` to disable.
               */
              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              public data class RequiredStatusChecks(
                public val strict: Boolean,
                public val contexts: List<String>,
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val checks: List<Checks>? = null,
              ) {
                @OptIn(ExperimentalSerializationApi::class)
                @Serializable
                public data class Checks(
                  public val context: String,
                  @SerialName("app_id")
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val appId: Long? = null,
                )
              }

              /**
               * Require at least one approving review on a pull request, before merging. Set to `null` to disable.
               */
              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              public data class RequiredPullRequestReviews(
                @SerialName("dismissal_restrictions")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val dismissalRestrictions: DismissalRestrictions? = null,
                @SerialName("dismiss_stale_reviews")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val dismissStaleReviews: Boolean? = null,
                @SerialName("require_code_owner_reviews")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val requireCodeOwnerReviews: Boolean? = null,
                @SerialName("required_approving_review_count")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val requiredApprovingReviewCount: Long? = null,
                @SerialName("require_last_push_approval")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val requireLastPushApproval: Boolean? = null,
                @SerialName("bypass_pull_request_allowances")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val bypassPullRequestAllowances: BypassPullRequestAllowances? = null,
              ) {
                /**
                 * Allow specific users, teams, or apps to bypass pull request requirements.
                 */
                @OptIn(ExperimentalSerializationApi::class)
                @Serializable
                public data class BypassPullRequestAllowances(
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val users: List<String>? = null,
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val teams: List<String>? = null,
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val apps: List<String>? = null,
                )

                /**
                 * Specify which users, teams, and apps can dismiss pull request reviews. Pass an empty `dismissal_restrictions` object to disable. User and team `dismissal_restrictions` are only available for organization-owned repositories. Omit this parameter for personal repositories.
                 */
                @OptIn(ExperimentalSerializationApi::class)
                @Serializable
                public data class DismissalRestrictions(
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val users: List<String>? = null,
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val teams: List<String>? = null,
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val apps: List<String>? = null,
                )
              }

              /**
               * Restrict who can push to the protected branch. User, app, and team `restrictions` are only available for organization-owned repositories. Set to `null` to disable.
               */
              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              public data class Restrictions(
                public val users: List<String>,
                public val teams: List<String>,
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val apps: List<String>? = null,
              )

              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              internal data class Body(
                @SerialName("required_status_checks")
                public val requiredStatusChecks: RequiredStatusChecks?,
                @SerialName("enforce_admins")
                public val enforceAdmins: Boolean?,
                @SerialName("required_pull_request_reviews")
                public val requiredPullRequestReviews: RequiredPullRequestReviews?,
                public val restrictions: Restrictions?,
                @SerialName("required_linear_history")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val requiredLinearHistory: Boolean? = null,
                @SerialName("allow_force_pushes")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val allowForcePushes: Optional<Boolean> = Optional.Absent,
                @SerialName("allow_deletions")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val allowDeletions: Boolean? = null,
                @SerialName("block_creations")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val blockCreations: Boolean? = null,
                @SerialName("required_conversation_resolution")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val requiredConversationResolution: Boolean? = null,
                @SerialName("lock_branch")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val lockBranch: Boolean? = null,
                @SerialName("allow_fork_syncing")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val allowForkSyncing: Boolean? = null,
              )

              public sealed interface Response {
                public data class Ok(
                  public val `value`: ProtectedBranch,
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

            public class EnforceAdmins internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _branch: String,
            ) {
              public val delete: Delete = Delete(__client, _owner, _repo, _branch)

              public val `get`: Get = Get(__client, _owner, _repo, _branch)

              public val post: Post = Post(__client, _owner, _repo, _branch)

              public class Delete internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _branch: String,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/branches/${this._branch.encodeURLParameter()}/protection/enforce_admins")
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
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _branch: String,
              ) {
                public suspend operator fun invoke(): ProtectedBranchAdminEnforced {
                  val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/branches/${this._branch.encodeURLParameter()}/protection/enforce_admins")
                  if (response.status.value != 200) throw ResponseException(response, "")
                  return response.body()
                }
              }

              public class Post internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _branch: String,
              ) {
                public suspend operator fun invoke(): ProtectedBranchAdminEnforced {
                  val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/branches/${this._branch.encodeURLParameter()}/protection/enforce_admins")
                  if (response.status.value != 200) throw ResponseException(response, "")
                  return response.body()
                }
              }
            }

            public class RequiredPullRequestReviews internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _branch: String,
            ) {
              public val delete: Delete = Delete(__client, _owner, _repo, _branch)

              public val `get`: Get = Get(__client, _owner, _repo, _branch)

              public val patch: Patch = Patch(__client, _owner, _repo, _branch)

              public class Delete internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _branch: String,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/branches/${this._branch.encodeURLParameter()}/protection/required_pull_request_reviews")
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
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _branch: String,
              ) {
                public suspend operator fun invoke(): ProtectedBranchPullRequestReview {
                  val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/branches/${this._branch.encodeURLParameter()}/protection/required_pull_request_reviews")
                  if (response.status.value != 200) throw ResponseException(response, "")
                  return response.body()
                }
              }

              public class Patch internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _branch: String,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = __client.patch("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/branches/${this._branch.encodeURLParameter()}/protection/required_pull_request_reviews")
                  return when (response.status.value) {
                    200 -> Response.Ok(response.body())
                    422 -> Response.UnprocessableEntity(response.body())
                    else -> throw ResponseException(response, "")
                  }
                }

                public suspend operator fun invoke(
                  dismissalRestrictions: DismissalRestrictions? = null,
                  dismissStaleReviews: Boolean? = null,
                  requireCodeOwnerReviews: Boolean? = null,
                  requiredApprovingReviewCount: Long? = null,
                  requireLastPushApproval: Boolean? = null,
                  bypassPullRequestAllowances: BypassPullRequestAllowances? = null,
                ): Response {
                  val response = __client.patch("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/branches/${this._branch.encodeURLParameter()}/protection/required_pull_request_reviews") {
                    contentType(ContentType.Application.Json)
                    setBody(Body(dismissalRestrictions = dismissalRestrictions, dismissStaleReviews = dismissStaleReviews, requireCodeOwnerReviews = requireCodeOwnerReviews, requiredApprovingReviewCount = requiredApprovingReviewCount, requireLastPushApproval = requireLastPushApproval, bypassPullRequestAllowances = bypassPullRequestAllowances))
                  }
                  return when (response.status.value) {
                    200 -> Response.Ok(response.body())
                    422 -> Response.UnprocessableEntity(response.body())
                    else -> throw ResponseException(response, "")
                  }
                }

                /**
                 * Specify which users, teams, and apps can dismiss pull request reviews. Pass an empty `dismissal_restrictions` object to disable. User and team `dismissal_restrictions` are only available for organization-owned repositories. Omit this parameter for personal repositories.
                 */
                @OptIn(ExperimentalSerializationApi::class)
                @Serializable
                public data class DismissalRestrictions(
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val users: List<String>? = null,
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val teams: List<String>? = null,
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val apps: List<String>? = null,
                )

                /**
                 * Allow specific users, teams, or apps to bypass pull request requirements.
                 */
                @OptIn(ExperimentalSerializationApi::class)
                @Serializable
                public data class BypassPullRequestAllowances(
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val users: List<String>? = null,
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val teams: List<String>? = null,
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val apps: List<String>? = null,
                )

                @OptIn(ExperimentalSerializationApi::class)
                @Serializable
                internal data class Body(
                  @SerialName("dismissal_restrictions")
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val dismissalRestrictions: DismissalRestrictions? = null,
                  @SerialName("dismiss_stale_reviews")
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val dismissStaleReviews: Boolean? = null,
                  @SerialName("require_code_owner_reviews")
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val requireCodeOwnerReviews: Boolean? = null,
                  @SerialName("required_approving_review_count")
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val requiredApprovingReviewCount: Long? = null,
                  @SerialName("require_last_push_approval")
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val requireLastPushApproval: Boolean? = null,
                  @SerialName("bypass_pull_request_allowances")
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val bypassPullRequestAllowances: BypassPullRequestAllowances? = null,
                )

                public sealed interface Response {
                  public data class Ok(
                    public val `value`: ProtectedBranchPullRequestReview,
                  ) : Response

                  public data class UnprocessableEntity(
                    public val `value`: ValidationError,
                  ) : Response
                }
              }
            }

            public class RequiredSignatures internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _branch: String,
            ) {
              public val delete: Delete = Delete(__client, _owner, _repo, _branch)

              public val `get`: Get = Get(__client, _owner, _repo, _branch)

              public val post: Post = Post(__client, _owner, _repo, _branch)

              public class Delete internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _branch: String,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/branches/${this._branch.encodeURLParameter()}/protection/required_signatures")
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
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _branch: String,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/branches/${this._branch.encodeURLParameter()}/protection/required_signatures")
                  return when (response.status.value) {
                    200 -> Response.Ok(response.body())
                    404 -> Response.NotFound(response.body())
                    else -> throw ResponseException(response, "")
                  }
                }

                public sealed interface Response {
                  public data class Ok(
                    public val `value`: ProtectedBranchAdminEnforced,
                  ) : Response

                  public data class NotFound(
                    public val `value`: BasicError,
                  ) : Response
                }
              }

              public class Post internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _branch: String,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/branches/${this._branch.encodeURLParameter()}/protection/required_signatures")
                  return when (response.status.value) {
                    200 -> Response.Ok(response.body())
                    404 -> Response.NotFound(response.body())
                    else -> throw ResponseException(response, "")
                  }
                }

                public sealed interface Response {
                  public data class Ok(
                    public val `value`: ProtectedBranchAdminEnforced,
                  ) : Response

                  public data class NotFound(
                    public val `value`: BasicError,
                  ) : Response
                }
              }
            }

            public class RequiredStatusChecks internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _branch: String,
            ) {
              public val delete: Delete = Delete(__client, _owner, _repo, _branch)

              public val `get`: Get = Get(__client, _owner, _repo, _branch)

              public val patch: Patch = Patch(__client, _owner, _repo, _branch)

              public val contexts: Contexts = Contexts(__client, _owner, _repo, _branch)

              public class Delete internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _branch: String,
              ) {
                public suspend operator fun invoke() {
                  val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/branches/${this._branch.encodeURLParameter()}/protection/required_status_checks")
                  if (response.status.value != 204) throw ResponseException(response, "")
                }
              }

              public class Get internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _branch: String,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/branches/${this._branch.encodeURLParameter()}/protection/required_status_checks")
                  return when (response.status.value) {
                    200 -> Response.Ok(response.body())
                    404 -> Response.NotFound(response.body())
                    else -> throw ResponseException(response, "")
                  }
                }

                public sealed interface Response {
                  public data class Ok(
                    public val `value`: StatusCheckPolicy,
                  ) : Response

                  public data class NotFound(
                    public val `value`: BasicError,
                  ) : Response
                }
              }

              public class Patch internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _branch: String,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = __client.patch("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/branches/${this._branch.encodeURLParameter()}/protection/required_status_checks")
                  return when (response.status.value) {
                    200 -> Response.Ok(response.body())
                    404 -> Response.NotFound(response.body())
                    422 -> Response.UnprocessableEntity(response.body())
                    else -> throw ResponseException(response, "")
                  }
                }

                public suspend operator fun invoke(
                  strict: Boolean? = null,
                  contexts: List<String>? = null,
                  checks: List<Checks>? = null,
                ): Response {
                  val response = __client.patch("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/branches/${this._branch.encodeURLParameter()}/protection/required_status_checks") {
                    contentType(ContentType.Application.Json)
                    setBody(Body(strict = strict, contexts = contexts, checks = checks))
                  }
                  return when (response.status.value) {
                    200 -> Response.Ok(response.body())
                    404 -> Response.NotFound(response.body())
                    422 -> Response.UnprocessableEntity(response.body())
                    else -> throw ResponseException(response, "")
                  }
                }

                @OptIn(ExperimentalSerializationApi::class)
                @Serializable
                public data class Checks(
                  public val context: String,
                  @SerialName("app_id")
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val appId: Long? = null,
                )

                @OptIn(ExperimentalSerializationApi::class)
                @Serializable
                internal data class Body(
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val strict: Boolean? = null,
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val contexts: List<String>? = null,
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val checks: List<Checks>? = null,
                )

                public sealed interface Response {
                  public data class Ok(
                    public val `value`: StatusCheckPolicy,
                  ) : Response

                  public data class NotFound(
                    public val `value`: BasicError,
                  ) : Response

                  public data class UnprocessableEntity(
                    public val `value`: ValidationError,
                  ) : Response
                }
              }

              public class Contexts internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _branch: String,
              ) {
                public val delete: Delete = Delete(__client, _owner, _repo, _branch)

                public val `get`: Get = Get(__client, _owner, _repo, _branch)

                public val post: Post = Post(__client, _owner, _repo, _branch)

                public val put: Put = Put(__client, _owner, _repo, _branch)

                public class Delete internal constructor(
                  private val __client: HttpClient,
                  private val _owner: String,
                  private val _repo: String,
                  private val _branch: String,
                ) {
                  public suspend operator fun invoke(): Response {
                    val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/branches/${this._branch.encodeURLParameter()}/protection/required_status_checks/contexts")
                    return when (response.status.value) {
                      200 -> Response.Ok(response.body())
                      404 -> Response.NotFound(response.body())
                      422 -> Response.UnprocessableEntity(response.body())
                      else -> throw ResponseException(response, "")
                    }
                  }

                  public suspend operator fun invoke(body: ContextsStrings): Response {
                    val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/branches/${this._branch.encodeURLParameter()}/protection/required_status_checks/contexts") {
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

                  public suspend operator fun invoke(body: List<String>): Response {
                    val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/branches/${this._branch.encodeURLParameter()}/protection/required_status_checks/contexts") {
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

                  @Serializable
                  public data class ContextsStrings(
                    public val contexts: List<String>,
                  )

                  public sealed interface Response {
                    public data class Ok(
                      public val `value`: List<String>,
                    ) : Response

                    public data class NotFound(
                      public val `value`: BasicError,
                    ) : Response

                    public data class UnprocessableEntity(
                      public val `value`: ValidationError,
                    ) : Response
                  }
                }

                public class Get internal constructor(
                  private val __client: HttpClient,
                  private val _owner: String,
                  private val _repo: String,
                  private val _branch: String,
                ) {
                  public suspend operator fun invoke(): Response {
                    val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/branches/${this._branch.encodeURLParameter()}/protection/required_status_checks/contexts")
                    return when (response.status.value) {
                      200 -> Response.Ok(response.body())
                      404 -> Response.NotFound(response.body())
                      else -> throw ResponseException(response, "")
                    }
                  }

                  public sealed interface Response {
                    public data class Ok(
                      public val `value`: List<String>,
                    ) : Response

                    public data class NotFound(
                      public val `value`: BasicError,
                    ) : Response
                  }
                }

                public class Post internal constructor(
                  private val __client: HttpClient,
                  private val _owner: String,
                  private val _repo: String,
                  private val _branch: String,
                ) {
                  public suspend operator fun invoke(): Response {
                    val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/branches/${this._branch.encodeURLParameter()}/protection/required_status_checks/contexts")
                    return when (response.status.value) {
                      200 -> Response.Ok(response.body())
                      403 -> Response.Forbidden(response.body())
                      404 -> Response.NotFound(response.body())
                      422 -> Response.UnprocessableEntity(response.body())
                      else -> throw ResponseException(response, "")
                    }
                  }

                  public suspend operator fun invoke(body: ContextsStrings): Response {
                    val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/branches/${this._branch.encodeURLParameter()}/protection/required_status_checks/contexts") {
                      contentType(ContentType.Application.Json)
                      setBody(body)
                    }
                    return when (response.status.value) {
                      200 -> Response.Ok(response.body())
                      403 -> Response.Forbidden(response.body())
                      404 -> Response.NotFound(response.body())
                      422 -> Response.UnprocessableEntity(response.body())
                      else -> throw ResponseException(response, "")
                    }
                  }

                  public suspend operator fun invoke(body: List<String>): Response {
                    val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/branches/${this._branch.encodeURLParameter()}/protection/required_status_checks/contexts") {
                      contentType(ContentType.Application.Json)
                      setBody(body)
                    }
                    return when (response.status.value) {
                      200 -> Response.Ok(response.body())
                      403 -> Response.Forbidden(response.body())
                      404 -> Response.NotFound(response.body())
                      422 -> Response.UnprocessableEntity(response.body())
                      else -> throw ResponseException(response, "")
                    }
                  }

                  @Serializable
                  public data class ContextsStrings(
                    public val contexts: List<String>,
                  )

                  public sealed interface Response {
                    public data class Ok(
                      public val `value`: List<String>,
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

                public class Put internal constructor(
                  private val __client: HttpClient,
                  private val _owner: String,
                  private val _repo: String,
                  private val _branch: String,
                ) {
                  public suspend operator fun invoke(): Response {
                    val response = __client.put("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/branches/${this._branch.encodeURLParameter()}/protection/required_status_checks/contexts")
                    return when (response.status.value) {
                      200 -> Response.Ok(response.body())
                      404 -> Response.NotFound(response.body())
                      422 -> Response.UnprocessableEntity(response.body())
                      else -> throw ResponseException(response, "")
                    }
                  }

                  public suspend operator fun invoke(body: ContextsStrings): Response {
                    val response = __client.put("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/branches/${this._branch.encodeURLParameter()}/protection/required_status_checks/contexts") {
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

                  public suspend operator fun invoke(body: List<String>): Response {
                    val response = __client.put("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/branches/${this._branch.encodeURLParameter()}/protection/required_status_checks/contexts") {
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

                  @Serializable
                  public data class ContextsStrings(
                    public val contexts: List<String>,
                  )

                  public sealed interface Response {
                    public data class Ok(
                      public val `value`: List<String>,
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

            public class Restrictions internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _branch: String,
            ) {
              public val delete: Delete = Delete(__client, _owner, _repo, _branch)

              public val `get`: Get = Get(__client, _owner, _repo, _branch)

              public val apps: Apps = Apps(__client, _owner, _repo, _branch)

              public val teams: Teams = Teams(__client, _owner, _repo, _branch)

              public val users: Users = Users(__client, _owner, _repo, _branch)

              public class Delete internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _branch: String,
              ) {
                public suspend operator fun invoke() {
                  val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/branches/${this._branch.encodeURLParameter()}/protection/restrictions")
                  if (response.status.value != 204) throw ResponseException(response, "")
                }
              }

              public class Get internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _branch: String,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/branches/${this._branch.encodeURLParameter()}/protection/restrictions")
                  return when (response.status.value) {
                    200 -> Response.Ok(response.body())
                    404 -> Response.NotFound(response.body())
                    else -> throw ResponseException(response, "")
                  }
                }

                public sealed interface Response {
                  public data class Ok(
                    public val `value`: BranchRestrictionPolicy,
                  ) : Response

                  public data class NotFound(
                    public val `value`: BasicError,
                  ) : Response
                }
              }

              public class Apps internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _branch: String,
              ) {
                public val delete: Delete = Delete(__client, _owner, _repo, _branch)

                public val `get`: Get = Get(__client, _owner, _repo, _branch)

                public val post: Post = Post(__client, _owner, _repo, _branch)

                public val put: Put = Put(__client, _owner, _repo, _branch)

                public class Delete internal constructor(
                  private val __client: HttpClient,
                  private val _owner: String,
                  private val _repo: String,
                  private val _branch: String,
                ) {
                  public suspend operator fun invoke(apps: List<String>): Response {
                    val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/branches/${this._branch.encodeURLParameter()}/protection/restrictions/apps") {
                      contentType(ContentType.Application.Json)
                      setBody(Body(apps = apps))
                    }
                    return when (response.status.value) {
                      200 -> Response.Ok(response.body())
                      422 -> Response.UnprocessableEntity(response.body())
                      else -> throw ResponseException(response, "")
                    }
                  }

                  @Serializable
                  internal data class Body(
                    public val apps: List<String>,
                  )

                  public sealed interface Response {
                    public data class Ok(
                      public val `value`: List<Integration?>,
                    ) : Response

                    public data class UnprocessableEntity(
                      public val `value`: ValidationError,
                    ) : Response
                  }
                }

                public class Get internal constructor(
                  private val __client: HttpClient,
                  private val _owner: String,
                  private val _repo: String,
                  private val _branch: String,
                ) {
                  public suspend operator fun invoke(): Response {
                    val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/branches/${this._branch.encodeURLParameter()}/protection/restrictions/apps")
                    return when (response.status.value) {
                      200 -> Response.Ok(response.body())
                      404 -> Response.NotFound(response.body())
                      else -> throw ResponseException(response, "")
                    }
                  }

                  public sealed interface Response {
                    public data class Ok(
                      public val `value`: List<Integration?>,
                    ) : Response

                    public data class NotFound(
                      public val `value`: BasicError,
                    ) : Response
                  }
                }

                public class Post internal constructor(
                  private val __client: HttpClient,
                  private val _owner: String,
                  private val _repo: String,
                  private val _branch: String,
                ) {
                  public suspend operator fun invoke(apps: List<String>): Response {
                    val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/branches/${this._branch.encodeURLParameter()}/protection/restrictions/apps") {
                      contentType(ContentType.Application.Json)
                      setBody(Body(apps = apps))
                    }
                    return when (response.status.value) {
                      200 -> Response.Ok(response.body())
                      422 -> Response.UnprocessableEntity(response.body())
                      else -> throw ResponseException(response, "")
                    }
                  }

                  @Serializable
                  internal data class Body(
                    public val apps: List<String>,
                  )

                  public sealed interface Response {
                    public data class Ok(
                      public val `value`: List<Integration?>,
                    ) : Response

                    public data class UnprocessableEntity(
                      public val `value`: ValidationError,
                    ) : Response
                  }
                }

                public class Put internal constructor(
                  private val __client: HttpClient,
                  private val _owner: String,
                  private val _repo: String,
                  private val _branch: String,
                ) {
                  public suspend operator fun invoke(apps: List<String>): Response {
                    val response = __client.put("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/branches/${this._branch.encodeURLParameter()}/protection/restrictions/apps") {
                      contentType(ContentType.Application.Json)
                      setBody(Body(apps = apps))
                    }
                    return when (response.status.value) {
                      200 -> Response.Ok(response.body())
                      422 -> Response.UnprocessableEntity(response.body())
                      else -> throw ResponseException(response, "")
                    }
                  }

                  @Serializable
                  internal data class Body(
                    public val apps: List<String>,
                  )

                  public sealed interface Response {
                    public data class Ok(
                      public val `value`: List<Integration?>,
                    ) : Response

                    public data class UnprocessableEntity(
                      public val `value`: ValidationError,
                    ) : Response
                  }
                }
              }

              public class Teams internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _branch: String,
              ) {
                public val delete: Delete = Delete(__client, _owner, _repo, _branch)

                public val `get`: Get = Get(__client, _owner, _repo, _branch)

                public val post: Post = Post(__client, _owner, _repo, _branch)

                public val put: Put = Put(__client, _owner, _repo, _branch)

                public class Delete internal constructor(
                  private val __client: HttpClient,
                  private val _owner: String,
                  private val _repo: String,
                  private val _branch: String,
                ) {
                  public suspend operator fun invoke(): Response {
                    val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/branches/${this._branch.encodeURLParameter()}/protection/restrictions/teams")
                    return when (response.status.value) {
                      200 -> Response.Ok(response.body())
                      422 -> Response.UnprocessableEntity(response.body())
                      else -> throw ResponseException(response, "")
                    }
                  }

                  public suspend operator fun invoke(body: TeamsStrings): Response {
                    val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/branches/${this._branch.encodeURLParameter()}/protection/restrictions/teams") {
                      contentType(ContentType.Application.Json)
                      setBody(body)
                    }
                    return when (response.status.value) {
                      200 -> Response.Ok(response.body())
                      422 -> Response.UnprocessableEntity(response.body())
                      else -> throw ResponseException(response, "")
                    }
                  }

                  public suspend operator fun invoke(body: List<String>): Response {
                    val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/branches/${this._branch.encodeURLParameter()}/protection/restrictions/teams") {
                      contentType(ContentType.Application.Json)
                      setBody(body)
                    }
                    return when (response.status.value) {
                      200 -> Response.Ok(response.body())
                      422 -> Response.UnprocessableEntity(response.body())
                      else -> throw ResponseException(response, "")
                    }
                  }

                  @Serializable
                  public data class TeamsStrings(
                    public val teams: List<String>,
                  )

                  public sealed interface Response {
                    public data class Ok(
                      public val `value`: List<Team>,
                    ) : Response

                    public data class UnprocessableEntity(
                      public val `value`: ValidationError,
                    ) : Response
                  }
                }

                public class Get internal constructor(
                  private val __client: HttpClient,
                  private val _owner: String,
                  private val _repo: String,
                  private val _branch: String,
                ) {
                  public suspend operator fun invoke(): Response {
                    val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/branches/${this._branch.encodeURLParameter()}/protection/restrictions/teams")
                    return when (response.status.value) {
                      200 -> Response.Ok(response.body())
                      404 -> Response.NotFound(response.body())
                      else -> throw ResponseException(response, "")
                    }
                  }

                  public sealed interface Response {
                    public data class Ok(
                      public val `value`: List<Team>,
                    ) : Response

                    public data class NotFound(
                      public val `value`: BasicError,
                    ) : Response
                  }
                }

                public class Post internal constructor(
                  private val __client: HttpClient,
                  private val _owner: String,
                  private val _repo: String,
                  private val _branch: String,
                ) {
                  public suspend operator fun invoke(): Response {
                    val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/branches/${this._branch.encodeURLParameter()}/protection/restrictions/teams")
                    return when (response.status.value) {
                      200 -> Response.Ok(response.body())
                      422 -> Response.UnprocessableEntity(response.body())
                      else -> throw ResponseException(response, "")
                    }
                  }

                  public suspend operator fun invoke(body: TeamsStrings): Response {
                    val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/branches/${this._branch.encodeURLParameter()}/protection/restrictions/teams") {
                      contentType(ContentType.Application.Json)
                      setBody(body)
                    }
                    return when (response.status.value) {
                      200 -> Response.Ok(response.body())
                      422 -> Response.UnprocessableEntity(response.body())
                      else -> throw ResponseException(response, "")
                    }
                  }

                  public suspend operator fun invoke(body: List<String>): Response {
                    val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/branches/${this._branch.encodeURLParameter()}/protection/restrictions/teams") {
                      contentType(ContentType.Application.Json)
                      setBody(body)
                    }
                    return when (response.status.value) {
                      200 -> Response.Ok(response.body())
                      422 -> Response.UnprocessableEntity(response.body())
                      else -> throw ResponseException(response, "")
                    }
                  }

                  @Serializable
                  public data class TeamsStrings(
                    public val teams: List<String>,
                  )

                  public sealed interface Response {
                    public data class Ok(
                      public val `value`: List<Team>,
                    ) : Response

                    public data class UnprocessableEntity(
                      public val `value`: ValidationError,
                    ) : Response
                  }
                }

                public class Put internal constructor(
                  private val __client: HttpClient,
                  private val _owner: String,
                  private val _repo: String,
                  private val _branch: String,
                ) {
                  public suspend operator fun invoke(): Response {
                    val response = __client.put("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/branches/${this._branch.encodeURLParameter()}/protection/restrictions/teams")
                    return when (response.status.value) {
                      200 -> Response.Ok(response.body())
                      422 -> Response.UnprocessableEntity(response.body())
                      else -> throw ResponseException(response, "")
                    }
                  }

                  public suspend operator fun invoke(body: TeamsStrings): Response {
                    val response = __client.put("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/branches/${this._branch.encodeURLParameter()}/protection/restrictions/teams") {
                      contentType(ContentType.Application.Json)
                      setBody(body)
                    }
                    return when (response.status.value) {
                      200 -> Response.Ok(response.body())
                      422 -> Response.UnprocessableEntity(response.body())
                      else -> throw ResponseException(response, "")
                    }
                  }

                  public suspend operator fun invoke(body: List<String>): Response {
                    val response = __client.put("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/branches/${this._branch.encodeURLParameter()}/protection/restrictions/teams") {
                      contentType(ContentType.Application.Json)
                      setBody(body)
                    }
                    return when (response.status.value) {
                      200 -> Response.Ok(response.body())
                      422 -> Response.UnprocessableEntity(response.body())
                      else -> throw ResponseException(response, "")
                    }
                  }

                  @Serializable
                  public data class TeamsStrings(
                    public val teams: List<String>,
                  )

                  public sealed interface Response {
                    public data class Ok(
                      public val `value`: List<Team>,
                    ) : Response

                    public data class UnprocessableEntity(
                      public val `value`: ValidationError,
                    ) : Response
                  }
                }
              }

              public class Users internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _branch: String,
              ) {
                public val delete: Delete = Delete(__client, _owner, _repo, _branch)

                public val `get`: Get = Get(__client, _owner, _repo, _branch)

                public val post: Post = Post(__client, _owner, _repo, _branch)

                public val put: Put = Put(__client, _owner, _repo, _branch)

                public class Delete internal constructor(
                  private val __client: HttpClient,
                  private val _owner: String,
                  private val _repo: String,
                  private val _branch: String,
                ) {
                  public suspend operator fun invoke(users: List<String>): Response {
                    val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/branches/${this._branch.encodeURLParameter()}/protection/restrictions/users") {
                      contentType(ContentType.Application.Json)
                      setBody(Body(users = users))
                    }
                    return when (response.status.value) {
                      200 -> Response.Ok(response.body())
                      422 -> Response.UnprocessableEntity(response.body())
                      else -> throw ResponseException(response, "")
                    }
                  }

                  @Serializable
                  internal data class Body(
                    public val users: List<String>,
                  )

                  public sealed interface Response {
                    public data class Ok(
                      public val `value`: List<SimpleUser>,
                    ) : Response

                    public data class UnprocessableEntity(
                      public val `value`: ValidationError,
                    ) : Response
                  }
                }

                public class Get internal constructor(
                  private val __client: HttpClient,
                  private val _owner: String,
                  private val _repo: String,
                  private val _branch: String,
                ) {
                  public suspend operator fun invoke(): Response {
                    val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/branches/${this._branch.encodeURLParameter()}/protection/restrictions/users")
                    return when (response.status.value) {
                      200 -> Response.Ok(response.body())
                      404 -> Response.NotFound(response.body())
                      else -> throw ResponseException(response, "")
                    }
                  }

                  public sealed interface Response {
                    public data class Ok(
                      public val `value`: List<SimpleUser>,
                    ) : Response

                    public data class NotFound(
                      public val `value`: BasicError,
                    ) : Response
                  }
                }

                public class Post internal constructor(
                  private val __client: HttpClient,
                  private val _owner: String,
                  private val _repo: String,
                  private val _branch: String,
                ) {
                  public suspend operator fun invoke(users: List<String>): Response {
                    val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/branches/${this._branch.encodeURLParameter()}/protection/restrictions/users") {
                      contentType(ContentType.Application.Json)
                      setBody(Body(users = users))
                    }
                    return when (response.status.value) {
                      200 -> Response.Ok(response.body())
                      422 -> Response.UnprocessableEntity(response.body())
                      else -> throw ResponseException(response, "")
                    }
                  }

                  @Serializable
                  internal data class Body(
                    public val users: List<String>,
                  )

                  public sealed interface Response {
                    public data class Ok(
                      public val `value`: List<SimpleUser>,
                    ) : Response

                    public data class UnprocessableEntity(
                      public val `value`: ValidationError,
                    ) : Response
                  }
                }

                public class Put internal constructor(
                  private val __client: HttpClient,
                  private val _owner: String,
                  private val _repo: String,
                  private val _branch: String,
                ) {
                  public suspend operator fun invoke(users: List<String>): Response {
                    val response = __client.put("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/branches/${this._branch.encodeURLParameter()}/protection/restrictions/users") {
                      contentType(ContentType.Application.Json)
                      setBody(Body(users = users))
                    }
                    return when (response.status.value) {
                      200 -> Response.Ok(response.body())
                      422 -> Response.UnprocessableEntity(response.body())
                      else -> throw ResponseException(response, "")
                    }
                  }

                  @Serializable
                  internal data class Body(
                    public val users: List<String>,
                  )

                  public sealed interface Response {
                    public data class Ok(
                      public val `value`: List<SimpleUser>,
                    ) : Response

                    public data class UnprocessableEntity(
                      public val `value`: ValidationError,
                    ) : Response
                  }
                }
              }
            }
          }

          public class Rename internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _branch: String,
          ) {
            public val post: Post = Post(__client, _owner, _repo, _branch)

            public class Post internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _branch: String,
            ) {
              public suspend operator fun invoke(newName: String): Response {
                val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/branches/${this._branch.encodeURLParameter()}/rename") {
                  contentType(ContentType.Application.Json)
                  setBody(Body(newName = newName))
                }
                return when (response.status.value) {
                  201 -> Response.Created(response.body())
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  422 -> Response.UnprocessableEntity(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              @Serializable
              internal data class Body(
                @SerialName("new_name")
                public val newName: String,
              )

              public sealed interface Response {
                public data class Created(
                  public val `value`: BranchWithProtection,
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
      }

      public class CheckRuns internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public val post: Post = Post(__client, _owner, _repo)

        public fun checkRunId(checkRunId: Long): CheckRunIdPath = CheckRunIdPath(__client, _owner, _repo, checkRunId)

        public class Post internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(
            name: String,
            headSha: String,
            detailsUrl: String? = null,
            externalId: String? = null,
            status: Status? = null,
            startedAt: Instant? = null,
            conclusion: Conclusion? = null,
            completedAt: Instant? = null,
            output: Output? = null,
            actions: List<Actions>? = null,
          ): CheckRun {
            val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/check-runs") {
              contentType(ContentType.Application.Json)
              setBody(Body(name = name, headSha = headSha, detailsUrl = detailsUrl, externalId = externalId, status = status, startedAt = startedAt, conclusion = conclusion, completedAt = completedAt, output = output, actions = actions))
            }
            if (response.status.value != 201) throw ResponseException(response, "")
            return response.body()
          }

          @Serializable
          public enum class Status(
            public val `value`: String,
          ) {
            @SerialName("queued")
            Queued("queued"),
            @SerialName("in_progress")
            InProgress("in_progress"),
            @SerialName("completed")
            Completed("completed"),
            @SerialName("waiting")
            Waiting("waiting"),
            @SerialName("requested")
            Requested("requested"),
            @SerialName("pending")
            Pending("pending"),
            ;
          }

          @Serializable
          public enum class Conclusion(
            public val `value`: String,
          ) {
            @SerialName("action_required")
            ActionRequired("action_required"),
            @SerialName("cancelled")
            Cancelled("cancelled"),
            @SerialName("failure")
            Failure("failure"),
            @SerialName("neutral")
            Neutral("neutral"),
            @SerialName("success")
            Success("success"),
            @SerialName("skipped")
            Skipped("skipped"),
            @SerialName("stale")
            Stale("stale"),
            @SerialName("timed_out")
            TimedOut("timed_out"),
            ;
          }

          /**
           * Check runs can accept a variety of data in the `output` object, including a `title` and `summary` and can optionally provide descriptive details about the run.
           */
          @OptIn(ExperimentalSerializationApi::class)
          @Serializable
          public data class Output(
            public val title: String,
            public val summary: String,
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val text: String? = null,
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val annotations: List<Annotations>? = null,
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val images: List<Images>? = null,
          ) {
            @OptIn(ExperimentalSerializationApi::class)
            @Serializable
            public data class Annotations(
              public val path: String,
              @SerialName("start_line")
              public val startLine: Long,
              @SerialName("end_line")
              public val endLine: Long,
              @SerialName("start_column")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val startColumn: Long? = null,
              @SerialName("end_column")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val endColumn: Long? = null,
              @SerialName("annotation_level")
              public val annotationLevel: AnnotationLevel,
              public val message: String,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val title: String? = null,
              @SerialName("raw_details")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val rawDetails: String? = null,
            ) {
              @Serializable
              public enum class AnnotationLevel(
                public val `value`: String,
              ) {
                @SerialName("notice")
                Notice("notice"),
                @SerialName("warning")
                Warning("warning"),
                @SerialName("failure")
                Failure("failure"),
                ;
              }
            }

            @OptIn(ExperimentalSerializationApi::class)
            @Serializable
            public data class Images(
              public val alt: String,
              @SerialName("image_url")
              public val imageUrl: String,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val caption: String? = null,
            )
          }

          @Serializable
          public data class Actions(
            public val label: String,
            public val description: String,
            public val identifier: String,
          )

          @OptIn(ExperimentalSerializationApi::class)
          @Serializable
          internal data class Body(
            public val name: String,
            @SerialName("head_sha")
            public val headSha: String,
            @SerialName("details_url")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val detailsUrl: String? = null,
            @SerialName("external_id")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val externalId: String? = null,
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val status: Status? = null,
            @SerialName("started_at")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val startedAt: Instant? = null,
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val conclusion: Conclusion? = null,
            @SerialName("completed_at")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val completedAt: Instant? = null,
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val output: Output? = null,
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val actions: List<Actions>? = null,
          )
        }

        public class CheckRunIdPath internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
          private val _checkRunId: Long,
        ) {
          public val `get`: Get = Get(__client, _owner, _repo, _checkRunId)

          public val patch: Patch = Patch(__client, _owner, _repo, _checkRunId)

          public val annotations: Annotations = Annotations(__client, _owner, _repo, _checkRunId)

          public val rerequest: Rerequest = Rerequest(__client, _owner, _repo, _checkRunId)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _checkRunId: Long,
          ) {
            public suspend operator fun invoke(): CheckRun {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/check-runs/${this._checkRunId}")
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }
          }

          public class Patch internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _checkRunId: Long,
          ) {
            public suspend operator fun invoke(
              name: String? = null,
              detailsUrl: String? = null,
              externalId: String? = null,
              startedAt: Instant? = null,
              status: Status? = null,
              conclusion: Conclusion? = null,
              completedAt: Instant? = null,
              output: Output? = null,
              actions: List<Actions>? = null,
            ): CheckRun {
              val response = __client.patch("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/check-runs/${this._checkRunId}") {
                contentType(ContentType.Application.Json)
                setBody(Body(name = name, detailsUrl = detailsUrl, externalId = externalId, startedAt = startedAt, status = status, conclusion = conclusion, completedAt = completedAt, output = output, actions = actions))
              }
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }

            @Serializable
            public enum class Status(
              public val `value`: String,
            ) {
              @SerialName("queued")
              Queued("queued"),
              @SerialName("in_progress")
              InProgress("in_progress"),
              @SerialName("completed")
              Completed("completed"),
              @SerialName("waiting")
              Waiting("waiting"),
              @SerialName("requested")
              Requested("requested"),
              @SerialName("pending")
              Pending("pending"),
              ;
            }

            @Serializable
            public enum class Conclusion(
              public val `value`: String,
            ) {
              @SerialName("action_required")
              ActionRequired("action_required"),
              @SerialName("cancelled")
              Cancelled("cancelled"),
              @SerialName("failure")
              Failure("failure"),
              @SerialName("neutral")
              Neutral("neutral"),
              @SerialName("success")
              Success("success"),
              @SerialName("skipped")
              Skipped("skipped"),
              @SerialName("stale")
              Stale("stale"),
              @SerialName("timed_out")
              TimedOut("timed_out"),
              ;
            }

            /**
             * Check runs can accept a variety of data in the `output` object, including a `title` and `summary` and can optionally provide descriptive details about the run.
             */
            @OptIn(ExperimentalSerializationApi::class)
            @Serializable
            public data class Output(
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val title: String? = null,
              public val summary: String,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val text: String? = null,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val annotations: List<Annotations>? = null,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val images: List<Images>? = null,
            ) {
              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              public data class Annotations(
                public val path: String,
                @SerialName("start_line")
                public val startLine: Long,
                @SerialName("end_line")
                public val endLine: Long,
                @SerialName("start_column")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val startColumn: Long? = null,
                @SerialName("end_column")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val endColumn: Long? = null,
                @SerialName("annotation_level")
                public val annotationLevel: AnnotationLevel,
                public val message: String,
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val title: String? = null,
                @SerialName("raw_details")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val rawDetails: String? = null,
              ) {
                @Serializable
                public enum class AnnotationLevel(
                  public val `value`: String,
                ) {
                  @SerialName("notice")
                  Notice("notice"),
                  @SerialName("warning")
                  Warning("warning"),
                  @SerialName("failure")
                  Failure("failure"),
                  ;
                }
              }

              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              public data class Images(
                public val alt: String,
                @SerialName("image_url")
                public val imageUrl: String,
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val caption: String? = null,
              )
            }

            @Serializable
            public data class Actions(
              public val label: String,
              public val description: String,
              public val identifier: String,
            )

            @OptIn(ExperimentalSerializationApi::class)
            @Serializable
            internal data class Body(
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val name: String? = null,
              @SerialName("details_url")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val detailsUrl: String? = null,
              @SerialName("external_id")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val externalId: String? = null,
              @SerialName("started_at")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val startedAt: Instant? = null,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val status: Status? = null,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val conclusion: Conclusion? = null,
              @SerialName("completed_at")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val completedAt: Instant? = null,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val output: Output? = null,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val actions: List<Actions>? = null,
            )
          }

          public class Annotations internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _checkRunId: Long,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo, _checkRunId)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _checkRunId: Long,
            ) {
              public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<CheckAnnotation> {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/check-runs/${this._checkRunId}/annotations") {
                  if (perPage != null) {
                    this.url.encodedParameters.append("per_page", perPage.toString())
                  }
                  if (page != null) {
                    this.url.encodedParameters.append("page", page.toString())
                  }
                }
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }
            }
          }

          public class Rerequest internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _checkRunId: Long,
          ) {
            public val post: Post = Post(__client, _owner, _repo, _checkRunId)

            public class Post internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _checkRunId: Long,
            ) {
              public suspend operator fun invoke(): Response {
                val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/check-runs/${this._checkRunId}/rerequest")
                return when (response.status.value) {
                  201 -> Response.Created(response.body())
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  422 -> Response.UnprocessableEntity(response.body())
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

                public data class UnprocessableEntity(
                  public val `value`: BasicError,
                ) : Response
              }
            }
          }
        }
      }

      public class CheckSuites internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public val post: Post = Post(__client, _owner, _repo)

        public val preferences: Preferences = Preferences(__client, _owner, _repo)

        public fun checkSuiteId(checkSuiteId: Long): CheckSuiteIdPath = CheckSuiteIdPath(__client, _owner, _repo, checkSuiteId)

        public class Post internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(headSha: String): Response {
            val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/check-suites") {
              contentType(ContentType.Application.Json)
              setBody(Body(headSha = headSha))
            }
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              201 -> Response.Created(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          @Serializable
          internal data class Body(
            @SerialName("head_sha")
            public val headSha: String,
          )

          public sealed interface Response {
            public data class Ok(
              public val `value`: CheckSuite,
            ) : Response

            public data class Created(
              public val `value`: CheckSuite,
            ) : Response
          }
        }

        public class Preferences internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public val patch: Patch = Patch(__client, _owner, _repo)

          public class Patch internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public suspend operator fun invoke(autoTriggerChecks: List<AutoTriggerChecks>? = null): CheckSuitePreference {
              val response = __client.patch("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/check-suites/preferences") {
                contentType(ContentType.Application.Json)
                setBody(Body(autoTriggerChecks = autoTriggerChecks))
              }
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }

            @Serializable
            public data class AutoTriggerChecks(
              @SerialName("app_id")
              public val appId: Long,
              @Required
              public val setting: Boolean = true,
            )

            @OptIn(ExperimentalSerializationApi::class)
            @Serializable
            internal data class Body(
              @SerialName("auto_trigger_checks")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val autoTriggerChecks: List<AutoTriggerChecks>? = null,
            )
          }
        }

        public class CheckSuiteIdPath internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
          private val _checkSuiteId: Long,
        ) {
          public val `get`: Get = Get(__client, _owner, _repo, _checkSuiteId)

          public val checkRuns: CheckRuns = CheckRuns(__client, _owner, _repo, _checkSuiteId)

          public val rerequest: Rerequest = Rerequest(__client, _owner, _repo, _checkSuiteId)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _checkSuiteId: Long,
          ) {
            public suspend operator fun invoke(): CheckSuite {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/check-suites/${this._checkSuiteId}")
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }
          }

          public class CheckRuns internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _checkSuiteId: Long,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo, _checkSuiteId)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _checkSuiteId: Long,
            ) {
              public suspend operator fun invoke(
                checkName: String? = null,
                status: Status? = null,
                filter: Filter? = Filter.Latest,
                perPage: Long? = 30L,
                page: Long? = 1L,
              ): Response {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/check-suites/${this._checkSuiteId}/check-runs") {
                  if (checkName != null) {
                    this.url.encodedParameters.append("check_name", checkName.encodeURLParameter())
                  }
                  if (status != null) {
                    this.url.encodedParameters.append("status", status.value.encodeURLParameter())
                  }
                  if (filter != null) {
                    this.url.encodedParameters.append("filter", filter.value.encodeURLParameter())
                  }
                  if (perPage != null) {
                    this.url.encodedParameters.append("per_page", perPage.toString())
                  }
                  if (page != null) {
                    this.url.encodedParameters.append("page", page.toString())
                  }
                }
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }

              @Serializable
              public enum class Status(
                public val `value`: String,
              ) {
                @SerialName("queued")
                Queued("queued"),
                @SerialName("in_progress")
                InProgress("in_progress"),
                @SerialName("completed")
                Completed("completed"),
                ;
              }

              @Serializable
              public enum class Filter(
                public val `value`: String,
              ) {
                @SerialName("latest")
                Latest("latest"),
                @SerialName("all")
                All("all"),
                ;
              }

              @Serializable
              public data class Response(
                @SerialName("total_count")
                public val totalCount: Long,
                @SerialName("check_runs")
                public val checkRuns: List<CheckRun>,
              )
            }
          }

          public class Rerequest internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _checkSuiteId: Long,
          ) {
            public val post: Post = Post(__client, _owner, _repo, _checkSuiteId)

            public class Post internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _checkSuiteId: Long,
            ) {
              public suspend operator fun invoke(): EmptyObject {
                val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/check-suites/${this._checkSuiteId}/rerequest")
                if (response.status.value != 201) throw ResponseException(response, "")
                return response.body()
              }
            }
          }
        }
      }

      public class CodeScanning internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public val alerts: Alerts = Alerts(__client, _owner, _repo)

        public val analyses: Analyses = Analyses(__client, _owner, _repo)

        public val codeql: Codeql = Codeql(__client, _owner, _repo)

        public val defaultSetup: DefaultSetup = DefaultSetup(__client, _owner, _repo)

        public val sarifs: Sarifs = Sarifs(__client, _owner, _repo)

        public class Alerts internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public val `get`: Get = Get(__client, _owner, _repo)

          public fun alertNumber(alertNumber: Long): AlertNumberPath = AlertNumberPath(__client, _owner, _repo, alertNumber)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public suspend operator fun invoke(
              toolName: String? = null,
              toolGuid: String? = null,
              page: Long? = 1L,
              perPage: Long? = 30L,
              ref: String? = null,
              pr: Long? = null,
              direction: Direction? = Direction.Desc,
              before: String? = null,
              after: String? = null,
              sort: Sort? = Sort.Created,
              state: CodeScanningAlertStateQuery? = null,
              severity: CodeScanningAlertSeverity? = null,
              assignees: String? = null,
            ): Response {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/code-scanning/alerts") {
                if (toolName != null) {
                  this.url.encodedParameters.append("tool_name", toolName.encodeURLParameter())
                }
                if (toolGuid != null) {
                  this.url.encodedParameters.append("tool_guid", toolGuid.encodeURLParameter())
                }
                if (page != null) {
                  this.url.encodedParameters.append("page", page.toString())
                }
                if (perPage != null) {
                  this.url.encodedParameters.append("per_page", perPage.toString())
                }
                if (ref != null) {
                  this.url.encodedParameters.append("ref", ref.encodeURLParameter())
                }
                if (pr != null) {
                  this.url.encodedParameters.append("pr", pr.toString())
                }
                if (direction != null) {
                  this.url.encodedParameters.append("direction", direction.value.encodeURLParameter())
                }
                if (before != null) {
                  this.url.encodedParameters.append("before", before.encodeURLParameter())
                }
                if (after != null) {
                  this.url.encodedParameters.append("after", after.encodeURLParameter())
                }
                if (sort != null) {
                  this.url.encodedParameters.append("sort", sort.value.encodeURLParameter())
                }
                if (state != null) {
                  this.url.encodedParameters.append("state", state.value.encodeURLParameter())
                }
                if (severity != null) {
                  this.url.encodedParameters.append("severity", severity.value.encodeURLParameter())
                }
                if (assignees != null) {
                  this.url.encodedParameters.append("assignees", assignees.encodeURLParameter())
                }
              }
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                304 -> Response.NotModified
                403 -> Response.Forbidden(response.body())
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
                public val `value`: List<CodeScanningAlertItems>,
              ) : Response

              public data object NotModified : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response

              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              public data class ServiceUnavailable(
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val code: String? = null,
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val message: String? = null,
                @SerialName("documentation_url")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val documentationUrl: String? = null,
              ) : Response
            }
          }

          public class AlertNumberPath internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _alertNumber: Long,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo, _alertNumber)

            public val patch: Patch = Patch(__client, _owner, _repo, _alertNumber)

            public val autofix: Autofix = Autofix(__client, _owner, _repo, _alertNumber)

            public val instances: Instances = Instances(__client, _owner, _repo, _alertNumber)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _alertNumber: Long,
            ) {
              public suspend operator fun invoke(): Response {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/code-scanning/alerts/${this._alertNumber}")
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  304 -> Response.NotModified
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  503 -> response.body<Response.ServiceUnavailable>()
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: CodeScanningAlert,
                ) : Response

                public data object NotModified : Response

                public data class Forbidden(
                  public val `value`: BasicError,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response

                @OptIn(ExperimentalSerializationApi::class)
                @Serializable
                public data class ServiceUnavailable(
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val code: String? = null,
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val message: String? = null,
                  @SerialName("documentation_url")
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val documentationUrl: String? = null,
                ) : Response
              }
            }

            public class Patch internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _alertNumber: Long,
            ) {
              public suspend operator fun invoke(
                state: CodeScanningAlertSetState? = null,
                dismissedReason: Optional<CodeScanningAlertDismissedReason> = Optional.Absent,
                dismissedComment: Optional<CodeScanningAlertDismissedComment> = Optional.Absent,
                createRequest: CodeScanningAlertCreateRequest? = null,
                assignees: CodeScanningAlertAssignees? = null,
              ): Response {
                val response = __client.patch("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/code-scanning/alerts/${this._alertNumber}") {
                  contentType(ContentType.Application.Json)
                  setBody(Body(state = state, dismissedReason = dismissedReason, dismissedComment = dismissedComment, createRequest = createRequest, assignees = assignees))
                }
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  400 -> Response.BadRequest(response.body())
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  503 -> response.body<Response.ServiceUnavailable>()
                  else -> throw ResponseException(response, "")
                }
              }

              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              internal data class Body(
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val state: CodeScanningAlertSetState? = null,
                @SerialName("dismissed_reason")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val dismissedReason:
                    Optional<CodeScanningAlertDismissedReason> = Optional.Absent,
                @SerialName("dismissed_comment")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val dismissedComment:
                    Optional<CodeScanningAlertDismissedComment> = Optional.Absent,
                @SerialName("create_request")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val createRequest: CodeScanningAlertCreateRequest? = null,
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val assignees: CodeScanningAlertAssignees? = null,
              )

              public sealed interface Response {
                public data class Ok(
                  public val `value`: CodeScanningAlert,
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

                @OptIn(ExperimentalSerializationApi::class)
                @Serializable
                public data class ServiceUnavailable(
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val code: String? = null,
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val message: String? = null,
                  @SerialName("documentation_url")
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val documentationUrl: String? = null,
                ) : Response
              }
            }

            public class Autofix internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _alertNumber: Long,
            ) {
              public val `get`: Get = Get(__client, _owner, _repo, _alertNumber)

              public val post: Post = Post(__client, _owner, _repo, _alertNumber)

              public val commits: Commits = Commits(__client, _owner, _repo, _alertNumber)

              public class Get internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _alertNumber: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/code-scanning/alerts/${this._alertNumber}/autofix")
                  return when (response.status.value) {
                    200 -> Response.Ok(response.body())
                    400 -> Response.BadRequest(response.body())
                    403 -> Response.Forbidden(response.body())
                    404 -> Response.NotFound(response.body())
                    503 -> response.body<Response.ServiceUnavailable>()
                    else -> throw ResponseException(response, "")
                  }
                }

                public sealed interface Response {
                  public data class Ok(
                    public val `value`: CodeScanningAutofix,
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

                  @OptIn(ExperimentalSerializationApi::class)
                  @Serializable
                  public data class ServiceUnavailable(
                    @EncodeDefault(EncodeDefault.Mode.NEVER)
                    public val code: String? = null,
                    @EncodeDefault(EncodeDefault.Mode.NEVER)
                    public val message: String? = null,
                    @SerialName("documentation_url")
                    @EncodeDefault(EncodeDefault.Mode.NEVER)
                    public val documentationUrl: String? = null,
                  ) : Response
                }
              }

              public class Post internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _alertNumber: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/code-scanning/alerts/${this._alertNumber}/autofix")
                  return when (response.status.value) {
                    200 -> Response.Ok(response.body())
                    202 -> Response.Accepted(response.body())
                    400 -> Response.BadRequest(response.body())
                    403 -> Response.Forbidden(response.body())
                    404 -> Response.NotFound(response.body())
                    422 -> Response.UnprocessableEntity
                    503 -> response.body<Response.ServiceUnavailable>()
                    else -> throw ResponseException(response, "")
                  }
                }

                public sealed interface Response {
                  public data class Ok(
                    public val `value`: CodeScanningAutofix,
                  ) : Response

                  public data class Accepted(
                    public val `value`: CodeScanningAutofix,
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

                  public data object UnprocessableEntity : Response

                  @OptIn(ExperimentalSerializationApi::class)
                  @Serializable
                  public data class ServiceUnavailable(
                    @EncodeDefault(EncodeDefault.Mode.NEVER)
                    public val code: String? = null,
                    @EncodeDefault(EncodeDefault.Mode.NEVER)
                    public val message: String? = null,
                    @SerialName("documentation_url")
                    @EncodeDefault(EncodeDefault.Mode.NEVER)
                    public val documentationUrl: String? = null,
                  ) : Response
                }
              }

              public class Commits internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _alertNumber: Long,
              ) {
                public val post: Post = Post(__client, _owner, _repo, _alertNumber)

                public class Post internal constructor(
                  private val __client: HttpClient,
                  private val _owner: String,
                  private val _repo: String,
                  private val _alertNumber: Long,
                ) {
                  public suspend operator fun invoke(body: CodeScanningAutofixCommits? = null): Response {
                    val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/code-scanning/alerts/${this._alertNumber}/autofix/commits") {
                      body?.let {
                        contentType(ContentType.Application.Json)
                        setBody(it)
                      }
                    }
                    return when (response.status.value) {
                      201 -> Response.Created(response.body())
                      400 -> Response.BadRequest(response.body())
                      403 -> Response.Forbidden(response.body())
                      404 -> Response.NotFound(response.body())
                      422 -> Response.UnprocessableEntity
                      503 -> response.body<Response.ServiceUnavailable>()
                      else -> throw ResponseException(response, "")
                    }
                  }

                  public sealed interface Response {
                    public data class Created(
                      public val `value`: CodeScanningAutofixCommitsResponse,
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

                    public data object UnprocessableEntity : Response

                    @OptIn(ExperimentalSerializationApi::class)
                    @Serializable
                    public data class ServiceUnavailable(
                      @EncodeDefault(EncodeDefault.Mode.NEVER)
                      public val code: String? = null,
                      @EncodeDefault(EncodeDefault.Mode.NEVER)
                      public val message: String? = null,
                      @SerialName("documentation_url")
                      @EncodeDefault(EncodeDefault.Mode.NEVER)
                      public val documentationUrl: String? = null,
                    ) : Response
                  }
                }
              }
            }

            public class Instances internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _alertNumber: Long,
            ) {
              public val `get`: Get = Get(__client, _owner, _repo, _alertNumber)

              public class Get internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _alertNumber: Long,
              ) {
                public suspend operator fun invoke(
                  page: Long? = 1L,
                  perPage: Long? = 30L,
                  ref: String? = null,
                  pr: Long? = null,
                ): Response {
                  val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/code-scanning/alerts/${this._alertNumber}/instances") {
                    if (page != null) {
                      this.url.encodedParameters.append("page", page.toString())
                    }
                    if (perPage != null) {
                      this.url.encodedParameters.append("per_page", perPage.toString())
                    }
                    if (ref != null) {
                      this.url.encodedParameters.append("ref", ref.encodeURLParameter())
                    }
                    if (pr != null) {
                      this.url.encodedParameters.append("pr", pr.toString())
                    }
                  }
                  return when (response.status.value) {
                    200 -> Response.Ok(response.body())
                    403 -> Response.Forbidden(response.body())
                    404 -> Response.NotFound(response.body())
                    503 -> response.body<Response.ServiceUnavailable>()
                    else -> throw ResponseException(response, "")
                  }
                }

                public sealed interface Response {
                  public data class Ok(
                    public val `value`: List<CodeScanningAlertInstanceList>,
                  ) : Response

                  public data class Forbidden(
                    public val `value`: BasicError,
                  ) : Response

                  public data class NotFound(
                    public val `value`: BasicError,
                  ) : Response

                  @OptIn(ExperimentalSerializationApi::class)
                  @Serializable
                  public data class ServiceUnavailable(
                    @EncodeDefault(EncodeDefault.Mode.NEVER)
                    public val code: String? = null,
                    @EncodeDefault(EncodeDefault.Mode.NEVER)
                    public val message: String? = null,
                    @SerialName("documentation_url")
                    @EncodeDefault(EncodeDefault.Mode.NEVER)
                    public val documentationUrl: String? = null,
                  ) : Response
                }
              }
            }
          }
        }

        public class Analyses internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public val `get`: Get = Get(__client, _owner, _repo)

          public fun analysisId(analysisId: Long): AnalysisIdPath = AnalysisIdPath(__client, _owner, _repo, analysisId)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public suspend operator fun invoke(
              toolName: String? = null,
              toolGuid: String? = null,
              page: Long? = 1L,
              perPage: Long? = 30L,
              pr: Long? = null,
              ref: String? = null,
              sarifId: String? = null,
              direction: Direction? = Direction.Desc,
              sort: Sort? = Sort.Created,
            ): Response {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/code-scanning/analyses") {
                if (toolName != null) {
                  this.url.encodedParameters.append("tool_name", toolName.encodeURLParameter())
                }
                if (toolGuid != null) {
                  this.url.encodedParameters.append("tool_guid", toolGuid.encodeURLParameter())
                }
                if (page != null) {
                  this.url.encodedParameters.append("page", page.toString())
                }
                if (perPage != null) {
                  this.url.encodedParameters.append("per_page", perPage.toString())
                }
                if (pr != null) {
                  this.url.encodedParameters.append("pr", pr.toString())
                }
                if (ref != null) {
                  this.url.encodedParameters.append("ref", ref.encodeURLParameter())
                }
                if (sarifId != null) {
                  this.url.encodedParameters.append("sarif_id", sarifId.encodeURLParameter())
                }
                if (direction != null) {
                  this.url.encodedParameters.append("direction", direction.value.encodeURLParameter())
                }
                if (sort != null) {
                  this.url.encodedParameters.append("sort", sort.value.encodeURLParameter())
                }
              }
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                403 -> Response.Forbidden(response.body())
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
              ;
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: List<CodeScanningAnalysis>,
              ) : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response

              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              public data class ServiceUnavailable(
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val code: String? = null,
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val message: String? = null,
                @SerialName("documentation_url")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val documentationUrl: String? = null,
              ) : Response
            }
          }

          public class AnalysisIdPath internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _analysisId: Long,
          ) {
            public val delete: Delete = Delete(__client, _owner, _repo, _analysisId)

            public val `get`: Get = Get(__client, _owner, _repo, _analysisId)

            public class Delete internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _analysisId: Long,
            ) {
              public suspend operator fun invoke(confirmDelete: String? = null): Response {
                val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/code-scanning/analyses/${this._analysisId}") {
                  if (confirmDelete != null) {
                    this.url.encodedParameters.append("confirm_delete", confirmDelete.encodeURLParameter())
                  }
                }
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  400 -> Response.BadRequest(response.body())
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  503 -> response.body<Response.ServiceUnavailable>()
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: CodeScanningAnalysisDeletion,
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

                @OptIn(ExperimentalSerializationApi::class)
                @Serializable
                public data class ServiceUnavailable(
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val code: String? = null,
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val message: String? = null,
                  @SerialName("documentation_url")
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val documentationUrl: String? = null,
                ) : Response
              }
            }

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _analysisId: Long,
            ) {
              public suspend fun json(): JsonResponse {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/code-scanning/analyses/${this._analysisId}") {
                  `header`(HttpHeaders.Accept, ContentType.Application.Json)
                }
                return when (response.status.value) {
                  200 -> JsonResponse.Ok(response.body())
                  403 -> Forbidden(response.body())
                  404 -> NotFound(response.body())
                  422 -> UnprocessableEntity(response.body())
                  503 -> response.body<ServiceUnavailable>()
                  else -> throw ResponseException(response, "")
                }
              }

              public suspend fun sarifJson(): SarifJsonResponse {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/code-scanning/analyses/${this._analysisId}") {
                  `header`(HttpHeaders.Accept, ContentType("application", "sarif+json"))
                }
                return when (response.status.value) {
                  200 -> SarifJsonResponse.Ok(response.body())
                  403 -> Forbidden(response.body())
                  404 -> NotFound(response.body())
                  422 -> UnprocessableEntity(response.body())
                  503 -> response.body<ServiceUnavailable>()
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface JsonResponse {
                public data class Ok(
                  public val `value`: CodeScanningAnalysis,
                ) : JsonResponse
              }

              public sealed interface SarifJsonResponse {
                public data class Ok(
                  public val `value`: JsonElement,
                ) : SarifJsonResponse
              }

              public data class Forbidden(
                public val `value`: BasicError,
              ) : JsonResponse,
                  SarifJsonResponse

              public data class NotFound(
                public val `value`: BasicError,
              ) : JsonResponse,
                  SarifJsonResponse

              public data class UnprocessableEntity(
                public val `value`: BasicError,
              ) : JsonResponse,
                  SarifJsonResponse

              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              public data class ServiceUnavailable(
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val code: String? = null,
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val message: String? = null,
                @SerialName("documentation_url")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val documentationUrl: String? = null,
              ) : JsonResponse,
                  SarifJsonResponse
            }
          }
        }

        public class Codeql internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public val databases: Databases = Databases(__client, _owner, _repo)

          public val variantAnalyses: VariantAnalyses = VariantAnalyses(__client, _owner, _repo)

          public class Databases internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo)

            public fun language(language: String): LanguagePath = LanguagePath(__client, _owner, _repo, language)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/code-scanning/codeql/databases")
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  503 -> response.body<Response.ServiceUnavailable>()
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: List<CodeScanningCodeqlDatabase>,
                ) : Response

                public data class Forbidden(
                  public val `value`: BasicError,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response

                @OptIn(ExperimentalSerializationApi::class)
                @Serializable
                public data class ServiceUnavailable(
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val code: String? = null,
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val message: String? = null,
                  @SerialName("documentation_url")
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val documentationUrl: String? = null,
                ) : Response
              }
            }

            public class LanguagePath internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _language: String,
            ) {
              public val delete: Delete = Delete(__client, _owner, _repo, _language)

              public val `get`: Get = Get(__client, _owner, _repo, _language)

              public class Delete internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _language: String,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/code-scanning/codeql/databases/${this._language.encodeURLParameter()}")
                  return when (response.status.value) {
                    204 -> Response.NoContent
                    403 -> Response.Forbidden(response.body())
                    404 -> Response.NotFound(response.body())
                    503 -> response.body<Response.ServiceUnavailable>()
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

                  @OptIn(ExperimentalSerializationApi::class)
                  @Serializable
                  public data class ServiceUnavailable(
                    @EncodeDefault(EncodeDefault.Mode.NEVER)
                    public val code: String? = null,
                    @EncodeDefault(EncodeDefault.Mode.NEVER)
                    public val message: String? = null,
                    @SerialName("documentation_url")
                    @EncodeDefault(EncodeDefault.Mode.NEVER)
                    public val documentationUrl: String? = null,
                  ) : Response
                }
              }

              public class Get internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _language: String,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/code-scanning/codeql/databases/${this._language.encodeURLParameter()}")
                  return when (response.status.value) {
                    200 -> Response.Ok(response.body())
                    302 -> Response.Found
                    403 -> Response.Forbidden(response.body())
                    404 -> Response.NotFound(response.body())
                    503 -> response.body<Response.ServiceUnavailable>()
                    else -> throw ResponseException(response, "")
                  }
                }

                public sealed interface Response {
                  public data class Ok(
                    public val `value`: CodeScanningCodeqlDatabase,
                  ) : Response

                  public data object Found : Response

                  public data class Forbidden(
                    public val `value`: BasicError,
                  ) : Response

                  public data class NotFound(
                    public val `value`: BasicError,
                  ) : Response

                  @OptIn(ExperimentalSerializationApi::class)
                  @Serializable
                  public data class ServiceUnavailable(
                    @EncodeDefault(EncodeDefault.Mode.NEVER)
                    public val code: String? = null,
                    @EncodeDefault(EncodeDefault.Mode.NEVER)
                    public val message: String? = null,
                    @SerialName("documentation_url")
                    @EncodeDefault(EncodeDefault.Mode.NEVER)
                    public val documentationUrl: String? = null,
                  ) : Response
                }
              }
            }
          }

          public class VariantAnalyses internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public val post: Post = Post(__client, _owner, _repo)

            public fun codeqlVariantAnalysisId(codeqlVariantAnalysisId: Long): CodeqlVariantAnalysisIdPath = CodeqlVariantAnalysisIdPath(__client, _owner, _repo, codeqlVariantAnalysisId)

            public class Post internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
            ) {
              public suspend operator fun invoke(
                language: CodeScanningVariantAnalysisLanguage,
                queryPack: String,
                repositories: List<String>? = null,
                repositoryLists: List<String>? = null,
                repositoryOwners: List<String>? = null,
              ): Response {
                val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/code-scanning/codeql/variant-analyses") {
                  contentType(ContentType.Application.Json)
                  setBody(Body(language = language, queryPack = queryPack, repositories = repositories, repositoryLists = repositoryLists, repositoryOwners = repositoryOwners))
                }
                return when (response.status.value) {
                  201 -> Response.Created(response.body())
                  404 -> Response.NotFound(response.body())
                  422 -> Response.UnprocessableEntity(response.body())
                  503 -> response.body<Response.ServiceUnavailable>()
                  else -> throw ResponseException(response, "")
                }
              }

              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              internal data class Body(
                public val language: CodeScanningVariantAnalysisLanguage,
                @SerialName("query_pack")
                public val queryPack: String,
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val repositories: List<String>? = null,
                @SerialName("repository_lists")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val repositoryLists: List<String>? = null,
                @SerialName("repository_owners")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val repositoryOwners: List<String>? = null,
              )

              public sealed interface Response {
                public data class Created(
                  public val `value`: CodeScanningVariantAnalysis,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response

                public data class UnprocessableEntity(
                  public val `value`: BasicError,
                ) : Response

                @OptIn(ExperimentalSerializationApi::class)
                @Serializable
                public data class ServiceUnavailable(
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val code: String? = null,
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val message: String? = null,
                  @SerialName("documentation_url")
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val documentationUrl: String? = null,
                ) : Response
              }
            }

            public class CodeqlVariantAnalysisIdPath internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _codeqlVariantAnalysisId: Long,
            ) {
              public val `get`: Get = Get(__client, _owner, _repo, _codeqlVariantAnalysisId)

              public val repos: Repos = Repos(__client, _owner, _repo, _codeqlVariantAnalysisId)

              public class Get internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _codeqlVariantAnalysisId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/code-scanning/codeql/variant-analyses/${this._codeqlVariantAnalysisId}")
                  return when (response.status.value) {
                    200 -> Response.Ok(response.body())
                    404 -> Response.NotFound(response.body())
                    503 -> response.body<Response.ServiceUnavailable>()
                    else -> throw ResponseException(response, "")
                  }
                }

                public sealed interface Response {
                  public data class Ok(
                    public val `value`: CodeScanningVariantAnalysis,
                  ) : Response

                  public data class NotFound(
                    public val `value`: BasicError,
                  ) : Response

                  @OptIn(ExperimentalSerializationApi::class)
                  @Serializable
                  public data class ServiceUnavailable(
                    @EncodeDefault(EncodeDefault.Mode.NEVER)
                    public val code: String? = null,
                    @EncodeDefault(EncodeDefault.Mode.NEVER)
                    public val message: String? = null,
                    @SerialName("documentation_url")
                    @EncodeDefault(EncodeDefault.Mode.NEVER)
                    public val documentationUrl: String? = null,
                  ) : Response
                }
              }

              public class Repos internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _codeqlVariantAnalysisId: Long,
              ) {
                public fun repoOwner(repoOwner: String): RepoOwnerPath = RepoOwnerPath(__client, _owner, _repo, _codeqlVariantAnalysisId, repoOwner)

                public class RepoOwnerPath internal constructor(
                  private val __client: HttpClient,
                  private val _owner: String,
                  private val _repo: String,
                  private val _codeqlVariantAnalysisId: Long,
                  private val _repoOwner: String,
                ) {
                  public fun repoName(repoName: String): RepoNamePath = RepoNamePath(__client, _owner, _repo, _codeqlVariantAnalysisId, _repoOwner, repoName)

                  public class RepoNamePath internal constructor(
                    private val __client: HttpClient,
                    private val _owner: String,
                    private val _repo: String,
                    private val _codeqlVariantAnalysisId: Long,
                    private val _repoOwner: String,
                    private val _repoName: String,
                  ) {
                    public val `get`: Get =
                        Get(__client, _owner, _repo, _codeqlVariantAnalysisId, _repoOwner, _repoName)

                    public class Get internal constructor(
                      private val __client: HttpClient,
                      private val _owner: String,
                      private val _repo: String,
                      private val _codeqlVariantAnalysisId: Long,
                      private val _repoOwner: String,
                      private val _repoName: String,
                    ) {
                      public suspend operator fun invoke(): Response {
                        val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/code-scanning/codeql/variant-analyses/${this._codeqlVariantAnalysisId}/repos/${this._repoOwner.encodeURLParameter()}/${this._repoName.encodeURLParameter()}")
                        return when (response.status.value) {
                          200 -> Response.Ok(response.body())
                          404 -> Response.NotFound(response.body())
                          503 -> response.body<Response.ServiceUnavailable>()
                          else -> throw ResponseException(response, "")
                        }
                      }

                      public sealed interface Response {
                        public data class Ok(
                          public val `value`: CodeScanningVariantAnalysisRepoTask,
                        ) : Response

                        public data class NotFound(
                          public val `value`: BasicError,
                        ) : Response

                        @OptIn(ExperimentalSerializationApi::class)
                        @Serializable
                        public data class ServiceUnavailable(
                          @EncodeDefault(EncodeDefault.Mode.NEVER)
                          public val code: String? = null,
                          @EncodeDefault(EncodeDefault.Mode.NEVER)
                          public val message: String? = null,
                          @SerialName("documentation_url")
                          @EncodeDefault(EncodeDefault.Mode.NEVER)
                          public val documentationUrl: String? = null,
                        ) : Response
                      }
                    }
                  }
                }
              }
            }
          }
        }

        public class DefaultSetup internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public val `get`: Get = Get(__client, _owner, _repo)

          public val patch: Patch = Patch(__client, _owner, _repo)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/code-scanning/default-setup")
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                503 -> response.body<Response.ServiceUnavailable>()
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: CodeScanningDefaultSetup,
              ) : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response

              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              public data class ServiceUnavailable(
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val code: String? = null,
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val message: String? = null,
                @SerialName("documentation_url")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val documentationUrl: String? = null,
              ) : Response
            }
          }

          public class Patch internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public suspend operator fun invoke(body: CodeScanningDefaultSetupUpdate): Response {
              val response = __client.patch("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/code-scanning/default-setup") {
                contentType(ContentType.Application.Json)
                setBody(body)
              }
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                202 -> Response.Accepted(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                409 -> Response.Conflict(response.body())
                422 -> Response.UnprocessableEntity(response.body())
                503 -> response.body<Response.ServiceUnavailable>()
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: EmptyObject,
              ) : Response

              public data class Accepted(
                public val `value`: CodeScanningDefaultSetupUpdateResponse,
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
                public val `value`: BasicError,
              ) : Response

              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              public data class ServiceUnavailable(
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val code: String? = null,
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val message: String? = null,
                @SerialName("documentation_url")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val documentationUrl: String? = null,
              ) : Response
            }
          }
        }

        public class Sarifs internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public val post: Post = Post(__client, _owner, _repo)

          public fun sarifId(sarifId: String): SarifIdPath = SarifIdPath(__client, _owner, _repo, sarifId)

          public class Post internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public suspend operator fun invoke(
              commitSha: CodeScanningAnalysisCommitSha,
              ref: CodeScanningRefFull,
              sarif: CodeScanningAnalysisSarifFile,
              checkoutUri: String? = null,
              startedAt: Instant? = null,
              toolName: String? = null,
              validate: Boolean? = null,
            ): Response {
              val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/code-scanning/sarifs") {
                contentType(ContentType.Application.Json)
                setBody(Body(commitSha = commitSha, ref = ref, sarif = sarif, checkoutUri = checkoutUri, startedAt = startedAt, toolName = toolName, validate = validate))
              }
              return when (response.status.value) {
                202 -> Response.Accepted(response.body())
                400 -> Response.BadRequest
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                413 -> Response.PayloadTooLarge
                503 -> response.body<Response.ServiceUnavailable>()
                else -> throw ResponseException(response, "")
              }
            }

            @OptIn(ExperimentalSerializationApi::class)
            @Serializable
            internal data class Body(
              @SerialName("commit_sha")
              public val commitSha: CodeScanningAnalysisCommitSha,
              public val ref: CodeScanningRefFull,
              public val sarif: CodeScanningAnalysisSarifFile,
              @SerialName("checkout_uri")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val checkoutUri: String? = null,
              @SerialName("started_at")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val startedAt: Instant? = null,
              @SerialName("tool_name")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val toolName: String? = null,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val validate: Boolean? = null,
            )

            public sealed interface Response {
              public data class Accepted(
                public val `value`: CodeScanningSarifsReceipt,
              ) : Response

              public data object BadRequest : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response

              public data object PayloadTooLarge : Response

              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              public data class ServiceUnavailable(
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val code: String? = null,
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val message: String? = null,
                @SerialName("documentation_url")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val documentationUrl: String? = null,
              ) : Response
            }
          }

          public class SarifIdPath internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _sarifId: String,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo, _sarifId)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _sarifId: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/code-scanning/sarifs/${this._sarifId.encodeURLParameter()}")
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound
                  503 -> response.body<Response.ServiceUnavailable>()
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: CodeScanningSarifsStatus,
                ) : Response

                public data class Forbidden(
                  public val `value`: BasicError,
                ) : Response

                public data object NotFound : Response

                @OptIn(ExperimentalSerializationApi::class)
                @Serializable
                public data class ServiceUnavailable(
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val code: String? = null,
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val message: String? = null,
                  @SerialName("documentation_url")
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val documentationUrl: String? = null,
                ) : Response
              }
            }
          }
        }
      }

      public class CodeSecurityConfiguration internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public val `get`: Get = Get(__client, _owner, _repo)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/code-security-configuration")
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              204 -> Response.NoContent
              304 -> Response.NotModified
              403 -> Response.Forbidden(response.body())
              404 -> Response.NotFound(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: CodeSecurityConfigurationForRepository,
            ) : Response

            public data object NoContent : Response

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

      public class Codeowners internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public val errors: Errors = Errors(__client, _owner, _repo)

        public class Errors internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public val `get`: Get = Get(__client, _owner, _repo)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public suspend operator fun invoke(ref: String? = null): Response {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/codeowners/errors") {
                if (ref != null) {
                  this.url.encodedParameters.append("ref", ref.encodeURLParameter())
                }
              }
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                404 -> Response.NotFound
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: CodeownersErrors,
              ) : Response

              public data object NotFound : Response
            }
          }
        }
      }

      public class Codespaces internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public val `get`: Get = Get(__client, _owner, _repo)

        public val post: Post = Post(__client, _owner, _repo)

        public val devcontainers: Devcontainers = Devcontainers(__client, _owner, _repo)

        public val machines: Machines = Machines(__client, _owner, _repo)

        public val new: New = New(__client, _owner, _repo)

        public val permissionsCheck: PermissionsCheck = PermissionsCheck(__client, _owner, _repo)

        public val secrets: Secrets = Secrets(__client, _owner, _repo)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
            val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/codespaces") {
              if (perPage != null) {
                this.url.encodedParameters.append("per_page", perPage.toString())
              }
              if (page != null) {
                this.url.encodedParameters.append("page", page.toString())
              }
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
              @SerialName("total_count")
              public val totalCount: Long,
              public val codespaces: List<Codespace>,
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

        public class Post internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(
            ref: String? = null,
            location: String? = null,
            geo: Geo? = null,
            clientIp: String? = null,
            machine: String? = null,
            devcontainerPath: String? = null,
            multiRepoPermissionsOptOut: Boolean? = null,
            workingDirectory: String? = null,
            idleTimeoutMinutes: Long? = null,
            displayName: String? = null,
            retentionPeriodMinutes: Long? = null,
          ): Response {
            val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/codespaces") {
              contentType(ContentType.Application.Json)
              setBody(Body(ref = ref, location = location, geo = geo, clientIp = clientIp, machine = machine, devcontainerPath = devcontainerPath, multiRepoPermissionsOptOut = multiRepoPermissionsOptOut, workingDirectory = workingDirectory, idleTimeoutMinutes = idleTimeoutMinutes, displayName = displayName, retentionPeriodMinutes = retentionPeriodMinutes))
            }
            return when (response.status.value) {
              201 -> Response.Created(response.body())
              202 -> Response.Accepted(response.body())
              400 -> Response.BadRequest(response.body())
              401 -> Response.Unauthorized(response.body())
              403 -> Response.Forbidden(response.body())
              404 -> Response.NotFound(response.body())
              503 -> response.body<Response.ServiceUnavailable>()
              else -> throw ResponseException(response, "")
            }
          }

          @Serializable
          public enum class Geo {
            EuropeWest,
            SoutheastAsia,
            UsEast,
            UsWest,
          }

          @OptIn(ExperimentalSerializationApi::class)
          @Serializable
          internal data class Body(
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val ref: String? = null,
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val location: String? = null,
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val geo: Geo? = null,
            @SerialName("client_ip")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val clientIp: String? = null,
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val machine: String? = null,
            @SerialName("devcontainer_path")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val devcontainerPath: String? = null,
            @SerialName("multi_repo_permissions_opt_out")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val multiRepoPermissionsOptOut: Boolean? = null,
            @SerialName("working_directory")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val workingDirectory: String? = null,
            @SerialName("idle_timeout_minutes")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val idleTimeoutMinutes: Long? = null,
            @SerialName("display_name")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val displayName: String? = null,
            @SerialName("retention_period_minutes")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val retentionPeriodMinutes: Long? = null,
          )

          public sealed interface Response {
            public data class Created(
              public val `value`: Codespace,
            ) : Response

            public data class Accepted(
              public val `value`: Codespace,
            ) : Response

            public data class BadRequest(
              public val `value`: BasicError,
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

            @OptIn(ExperimentalSerializationApi::class)
            @Serializable
            public data class ServiceUnavailable(
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val code: String? = null,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val message: String? = null,
              @SerialName("documentation_url")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val documentationUrl: String? = null,
            ) : Response
          }
        }

        public class Devcontainers internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public val `get`: Get = Get(__client, _owner, _repo)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/codespaces/devcontainers") {
                if (perPage != null) {
                  this.url.encodedParameters.append("per_page", perPage.toString())
                }
                if (page != null) {
                  this.url.encodedParameters.append("page", page.toString())
                }
              }
              return when (response.status.value) {
                200 -> response.body<Response.Ok>()
                400 -> Response.BadRequest(response.body())
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
                public val devcontainers: List<Devcontainers>,
              ) : Response {
                @OptIn(ExperimentalSerializationApi::class)
                @Serializable
                public data class Devcontainers(
                  public val path: String,
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val name: String? = null,
                  @SerialName("display_name")
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val displayName: String? = null,
                )
              }

              public data class BadRequest(
                public val `value`: BasicError,
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

        public class Machines internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public val `get`: Get = Get(__client, _owner, _repo)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public suspend operator fun invoke(
              location: String? = null,
              clientIp: String? = null,
              ref: String? = null,
            ): Response {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/codespaces/machines") {
                if (location != null) {
                  this.url.encodedParameters.append("location", location.encodeURLParameter())
                }
                if (clientIp != null) {
                  this.url.encodedParameters.append("client_ip", clientIp.encodeURLParameter())
                }
                if (ref != null) {
                  this.url.encodedParameters.append("ref", ref.encodeURLParameter())
                }
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
                public val machines: List<CodespaceMachine>,
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

        public class New internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public val `get`: Get = Get(__client, _owner, _repo)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public suspend operator fun invoke(ref: String? = null, clientIp: String? = null): Response {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/codespaces/new") {
                if (ref != null) {
                  this.url.encodedParameters.append("ref", ref.encodeURLParameter())
                }
                if (clientIp != null) {
                  this.url.encodedParameters.append("client_ip", clientIp.encodeURLParameter())
                }
              }
              return when (response.status.value) {
                200 -> response.body<Response.Ok>()
                401 -> Response.Unauthorized(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              public data class Ok(
                @SerialName("billable_owner")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val billableOwner: SimpleUser? = null,
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val defaults: Defaults? = null,
              ) : Response {
                @Serializable
                public data class Defaults(
                  public val location: String,
                  @SerialName("devcontainer_path")
                  public val devcontainerPath: String?,
                )
              }

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

        public class PermissionsCheck internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public val `get`: Get = Get(__client, _owner, _repo)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public suspend operator fun invoke(ref: String, devcontainerPath: String): Response {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/codespaces/permissions_check") {
                this.url.encodedParameters.append("ref", ref.encodeURLParameter())
                this.url.encodedParameters.append("devcontainer_path", devcontainerPath.encodeURLParameter())
              }
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                401 -> Response.Unauthorized(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                422 -> Response.UnprocessableEntity(response.body())
                503 -> response.body<Response.ServiceUnavailable>()
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: CodespacesPermissionsCheckForDevcontainer,
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

              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              public data class ServiceUnavailable(
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val code: String? = null,
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val message: String? = null,
                @SerialName("documentation_url")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val documentationUrl: String? = null,
              ) : Response
            }
          }
        }

        public class Secrets internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public val `get`: Get = Get(__client, _owner, _repo)

          public val publicKey: PublicKey = PublicKey(__client, _owner, _repo)

          public fun secretName(secretName: String): SecretNamePath = SecretNamePath(__client, _owner, _repo, secretName)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/codespaces/secrets") {
                if (perPage != null) {
                  this.url.encodedParameters.append("per_page", perPage.toString())
                }
                if (page != null) {
                  this.url.encodedParameters.append("page", page.toString())
                }
              }
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }

            @Serializable
            public data class Response(
              @SerialName("total_count")
              public val totalCount: Long,
              public val secrets: List<RepoCodespacesSecret>,
            )
          }

          public class PublicKey internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
            ) {
              public suspend operator fun invoke(): CodespacesPublicKey {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/codespaces/secrets/public-key")
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }
            }
          }

          public class SecretNamePath internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _secretName: String,
          ) {
            public val delete: Delete = Delete(__client, _owner, _repo, _secretName)

            public val `get`: Get = Get(__client, _owner, _repo, _secretName)

            public val put: Put = Put(__client, _owner, _repo, _secretName)

            public class Delete internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _secretName: String,
            ) {
              public suspend operator fun invoke() {
                val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/codespaces/secrets/${this._secretName.encodeURLParameter()}")
                if (response.status.value != 204) throw ResponseException(response, "")
              }
            }

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _secretName: String,
            ) {
              public suspend operator fun invoke(): RepoCodespacesSecret {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/codespaces/secrets/${this._secretName.encodeURLParameter()}")
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }
            }

            public class Put internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _secretName: String,
            ) {
              public suspend operator fun invoke(encryptedValue: String? = null, keyId: String? = null): Response {
                val response = __client.put("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/codespaces/secrets/${this._secretName.encodeURLParameter()}") {
                  contentType(ContentType.Application.Json)
                  setBody(Body(encryptedValue = encryptedValue, keyId = keyId))
                }
                return when (response.status.value) {
                  201 -> Response.Created(response.body())
                  204 -> Response.NoContent
                  else -> throw ResponseException(response, "")
                }
              }

              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              internal data class Body(
                @SerialName("encrypted_value")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val encryptedValue: String? = null,
                @SerialName("key_id")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val keyId: String? = null,
              )

              public sealed interface Response {
                public data class Created(
                  public val `value`: EmptyObject,
                ) : Response

                public data object NoContent : Response
              }
            }
          }
        }
      }

      public class Collaborators internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public val `get`: Get = Get(__client, _owner, _repo)

        public fun username(username: String): UsernamePath = UsernamePath(__client, _owner, _repo, username)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(
            affiliation: Affiliation? = Affiliation.All,
            permission: Permission? = null,
            perPage: Long? = 30L,
            page: Long? = 1L,
          ): Response {
            val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/collaborators") {
              if (affiliation != null) {
                this.url.encodedParameters.append("affiliation", affiliation.value.encodeURLParameter())
              }
              if (permission != null) {
                this.url.encodedParameters.append("permission", permission.value.encodeURLParameter())
              }
              if (perPage != null) {
                this.url.encodedParameters.append("per_page", perPage.toString())
              }
              if (page != null) {
                this.url.encodedParameters.append("page", page.toString())
              }
            }
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              404 -> Response.NotFound(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          @Serializable
          public enum class Affiliation(
            public val `value`: String,
          ) {
            @SerialName("outside")
            Outside("outside"),
            @SerialName("direct")
            Direct("direct"),
            @SerialName("all")
            All("all"),
            ;
          }

          @Serializable
          public enum class Permission(
            public val `value`: String,
          ) {
            @SerialName("pull")
            Pull("pull"),
            @SerialName("triage")
            Triage("triage"),
            @SerialName("push")
            Push("push"),
            @SerialName("maintain")
            Maintain("maintain"),
            @SerialName("admin")
            Admin("admin"),
            ;
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: List<Collaborator>,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class UsernamePath internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
          private val _username: String,
        ) {
          public val delete: Delete = Delete(__client, _owner, _repo, _username)

          public val `get`: Get = Get(__client, _owner, _repo, _username)

          public val put: Put = Put(__client, _owner, _repo, _username)

          public val permission: Permission = Permission(__client, _owner, _repo, _username)

          public class Delete internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _username: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/collaborators/${this._username.encodeURLParameter()}")
              return when (response.status.value) {
                204 -> Response.NoContent
                403 -> Response.Forbidden(response.body())
                422 -> Response.UnprocessableEntity(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data object NoContent : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response

              public data class UnprocessableEntity(
                public val `value`: ValidationError,
              ) : Response
            }
          }

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _username: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/collaborators/${this._username.encodeURLParameter()}")
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
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _username: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = __client.put("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/collaborators/${this._username.encodeURLParameter()}")
              return when (response.status.value) {
                201 -> Response.Created(response.body())
                204 -> Response.NoContent
                403 -> Response.Forbidden(response.body())
                422 -> Response.UnprocessableEntity(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public suspend operator fun invoke(permission: String? = null): Response {
              val response = __client.put("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/collaborators/${this._username.encodeURLParameter()}") {
                contentType(ContentType.Application.Json)
                setBody(Body(permission = permission))
              }
              return when (response.status.value) {
                201 -> Response.Created(response.body())
                204 -> Response.NoContent
                403 -> Response.Forbidden(response.body())
                422 -> Response.UnprocessableEntity(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            @OptIn(ExperimentalSerializationApi::class)
            @Serializable
            internal data class Body(
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val permission: String? = null,
            )

            public sealed interface Response {
              public data class Created(
                public val `value`: RepositoryInvitation,
              ) : Response

              public data object NoContent : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response

              public data class UnprocessableEntity(
                public val `value`: ValidationError,
              ) : Response
            }
          }

          public class Permission internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _username: String,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo, _username)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _username: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/collaborators/${this._username.encodeURLParameter()}/permission")
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  404 -> Response.NotFound(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: RepositoryCollaboratorPermission,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response
              }
            }
          }
        }
      }

      public class Comments internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public val `get`: Get = Get(__client, _owner, _repo)

        public fun commentId(commentId: Long): CommentIdPath = CommentIdPath(__client, _owner, _repo, commentId)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<CommitComment> {
            val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/comments") {
              if (perPage != null) {
                this.url.encodedParameters.append("per_page", perPage.toString())
              }
              if (page != null) {
                this.url.encodedParameters.append("page", page.toString())
              }
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class CommentIdPath internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
          private val _commentId: Long,
        ) {
          public val delete: Delete = Delete(__client, _owner, _repo, _commentId)

          public val `get`: Get = Get(__client, _owner, _repo, _commentId)

          public val patch: Patch = Patch(__client, _owner, _repo, _commentId)

          public val reactions: Reactions = Reactions(__client, _owner, _repo, _commentId)

          public class Delete internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _commentId: Long,
          ) {
            public suspend operator fun invoke(): Response {
              val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/comments/${this._commentId}")
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
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _commentId: Long,
          ) {
            public suspend operator fun invoke(): Response {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/comments/${this._commentId}")
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: CommitComment,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response
            }
          }

          public class Patch internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _commentId: Long,
          ) {
            public suspend operator fun invoke(body: String): Response {
              val response = __client.patch("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/comments/${this._commentId}") {
                contentType(ContentType.Application.Json)
                setBody(Body(body = body))
              }
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            @Serializable
            internal data class Body(
              public val body: String,
            )

            public sealed interface Response {
              public data class Ok(
                public val `value`: CommitComment,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response
            }
          }

          public class Reactions internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _commentId: Long,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo, _commentId)

            public val post: Post = Post(__client, _owner, _repo, _commentId)

            public fun reactionId(reactionId: Long): ReactionIdPath = ReactionIdPath(__client, _owner, _repo, _commentId, reactionId)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _commentId: Long,
            ) {
              public suspend operator fun invoke(
                content: Content? = null,
                perPage: Long? = 30L,
                page: Long? = 1L,
              ): Response {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/comments/${this._commentId}/reactions") {
                  if (content != null) {
                    this.url.encodedParameters.append("content", content.value.encodeURLParameter())
                  }
                  if (perPage != null) {
                    this.url.encodedParameters.append("per_page", perPage.toString())
                  }
                  if (page != null) {
                    this.url.encodedParameters.append("page", page.toString())
                  }
                }
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  404 -> Response.NotFound(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              @Serializable
              public enum class Content(
                public val `value`: String,
              ) {
                `+1`("+1"),
                `-1`("-1"),
                @SerialName("laugh")
                Laugh("laugh"),
                @SerialName("confused")
                Confused("confused"),
                @SerialName("heart")
                Heart("heart"),
                @SerialName("hooray")
                Hooray("hooray"),
                @SerialName("rocket")
                Rocket("rocket"),
                @SerialName("eyes")
                Eyes("eyes"),
                ;
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: List<Reaction>,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response
              }
            }

            public class Post internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _commentId: Long,
            ) {
              public suspend operator fun invoke(content: Content): Response {
                val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/comments/${this._commentId}/reactions") {
                  contentType(ContentType.Application.Json)
                  setBody(Body(content = content))
                }
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  201 -> Response.Created(response.body())
                  422 -> Response.UnprocessableEntity(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              @Serializable
              public enum class Content(
                public val `value`: String,
              ) {
                `+1`("+1"),
                `-1`("-1"),
                @SerialName("laugh")
                Laugh("laugh"),
                @SerialName("confused")
                Confused("confused"),
                @SerialName("heart")
                Heart("heart"),
                @SerialName("hooray")
                Hooray("hooray"),
                @SerialName("rocket")
                Rocket("rocket"),
                @SerialName("eyes")
                Eyes("eyes"),
                ;
              }

              @Serializable
              internal data class Body(
                public val content: Content,
              )

              public sealed interface Response {
                public data class Ok(
                  public val `value`: Reaction,
                ) : Response

                public data class Created(
                  public val `value`: Reaction,
                ) : Response

                public data class UnprocessableEntity(
                  public val `value`: ValidationError,
                ) : Response
              }
            }

            public class ReactionIdPath internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _commentId: Long,
              private val _reactionId: Long,
            ) {
              public val delete: Delete = Delete(__client, _owner, _repo, _commentId, _reactionId)

              public class Delete internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _commentId: Long,
                private val _reactionId: Long,
              ) {
                public suspend operator fun invoke() {
                  val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/comments/${this._commentId}/reactions/${this._reactionId}")
                  if (response.status.value != 204) throw ResponseException(response, "")
                }
              }
            }
          }
        }
      }

      public class Commits internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public val `get`: Get = Get(__client, _owner, _repo)

        public fun commitSha(commitSha: String): CommitShaPath = CommitShaPath(__client, _owner, _repo, commitSha)

        public fun ref(ref: String): RefPath = RefPath(__client, _owner, _repo, ref)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(
            sha: String? = null,
            path: String? = null,
            author: String? = null,
            committer: String? = null,
            since: Instant? = null,
            until: Instant? = null,
            perPage: Long? = 30L,
            page: Long? = 1L,
          ): Response {
            val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/commits") {
              if (sha != null) {
                this.url.encodedParameters.append("sha", sha.encodeURLParameter())
              }
              if (path != null) {
                this.url.encodedParameters.append("path", path.encodeURLParameter())
              }
              if (author != null) {
                this.url.encodedParameters.append("author", author.encodeURLParameter())
              }
              if (committer != null) {
                this.url.encodedParameters.append("committer", committer.encodeURLParameter())
              }
              if (since != null) {
                this.url.encodedParameters.append("since", since.toString().encodeURLParameter())
              }
              if (until != null) {
                this.url.encodedParameters.append("until", until.toString().encodeURLParameter())
              }
              if (perPage != null) {
                this.url.encodedParameters.append("per_page", perPage.toString())
              }
              if (page != null) {
                this.url.encodedParameters.append("page", page.toString())
              }
            }
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              400 -> Response.BadRequest(response.body())
              404 -> Response.NotFound(response.body())
              409 -> Response.Conflict(response.body())
              500 -> Response.InternalServerError(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: List<Commit>,
            ) : Response

            public data class BadRequest(
              public val `value`: BasicError,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response

            public data class Conflict(
              public val `value`: BasicError,
            ) : Response

            public data class InternalServerError(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class CommitShaPath internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
          private val _commitSha: String,
        ) {
          public val branchesWhereHead: BranchesWhereHead =
              BranchesWhereHead(__client, _owner, _repo, _commitSha)

          public val comments: Comments = Comments(__client, _owner, _repo, _commitSha)

          public val pulls: Pulls = Pulls(__client, _owner, _repo, _commitSha)

          public class BranchesWhereHead internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _commitSha: String,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo, _commitSha)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _commitSha: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/commits/${this._commitSha.encodeURLParameter()}/branches-where-head")
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  409 -> Response.Conflict(response.body())
                  422 -> Response.UnprocessableEntity(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: List<BranchShort>,
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

          public class Comments internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _commitSha: String,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo, _commitSha)

            public val post: Post = Post(__client, _owner, _repo, _commitSha)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _commitSha: String,
            ) {
              public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<CommitComment> {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/commits/${this._commitSha.encodeURLParameter()}/comments") {
                  if (perPage != null) {
                    this.url.encodedParameters.append("per_page", perPage.toString())
                  }
                  if (page != null) {
                    this.url.encodedParameters.append("page", page.toString())
                  }
                }
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }
            }

            public class Post internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _commitSha: String,
            ) {
              public suspend operator fun invoke(
                body: String,
                path: String? = null,
                position: Long? = null,
                line: Long? = null,
              ): Response {
                val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/commits/${this._commitSha.encodeURLParameter()}/comments") {
                  contentType(ContentType.Application.Json)
                  setBody(Body(body = body, path = path, position = position, line = line))
                }
                return when (response.status.value) {
                  201 -> Response.Created(response.body())
                  403 -> Response.Forbidden(response.body())
                  422 -> Response.UnprocessableEntity(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              internal data class Body(
                public val body: String,
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val path: String? = null,
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val position: Long? = null,
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val line: Long? = null,
              )

              public sealed interface Response {
                public data class Created(
                  public val `value`: CommitComment,
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

          public class Pulls internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _commitSha: String,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo, _commitSha)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _commitSha: String,
            ) {
              public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/commits/${this._commitSha.encodeURLParameter()}/pulls") {
                  if (perPage != null) {
                    this.url.encodedParameters.append("per_page", perPage.toString())
                  }
                  if (page != null) {
                    this.url.encodedParameters.append("page", page.toString())
                  }
                }
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  409 -> Response.Conflict(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: List<PullRequestSimple>,
                ) : Response

                public data class Conflict(
                  public val `value`: BasicError,
                ) : Response
              }
            }
          }
        }

        public class RefPath internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
          private val _ref: String,
        ) {
          public val `get`: Get = Get(__client, _owner, _repo, _ref)

          public val checkRuns: CheckRuns = CheckRuns(__client, _owner, _repo, _ref)

          public val checkSuites: CheckSuites = CheckSuites(__client, _owner, _repo, _ref)

          public val status: Status = Status(__client, _owner, _repo, _ref)

          public val statuses: Statuses = Statuses(__client, _owner, _repo, _ref)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _ref: String,
          ) {
            public suspend operator fun invoke(page: Long? = 1L, perPage: Long? = 30L): Response {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/commits/${this._ref.encodeURLParameter()}") {
                if (page != null) {
                  this.url.encodedParameters.append("page", page.toString())
                }
                if (perPage != null) {
                  this.url.encodedParameters.append("per_page", perPage.toString())
                }
              }
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                404 -> Response.NotFound(response.body())
                409 -> Response.Conflict(response.body())
                422 -> Response.UnprocessableEntity(response.body())
                500 -> Response.InternalServerError(response.body())
                503 -> response.body<Response.ServiceUnavailable>()
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: Commit,
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

              public data class InternalServerError(
                public val `value`: BasicError,
              ) : Response

              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              public data class ServiceUnavailable(
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val code: String? = null,
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val message: String? = null,
                @SerialName("documentation_url")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val documentationUrl: String? = null,
              ) : Response
            }
          }

          public class CheckRuns internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _ref: String,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo, _ref)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _ref: String,
            ) {
              public suspend operator fun invoke(
                checkName: String? = null,
                status: Status? = null,
                filter: Filter? = Filter.Latest,
                perPage: Long? = 30L,
                page: Long? = 1L,
                appId: Long? = null,
              ): Response {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/commits/${this._ref.encodeURLParameter()}/check-runs") {
                  if (checkName != null) {
                    this.url.encodedParameters.append("check_name", checkName.encodeURLParameter())
                  }
                  if (status != null) {
                    this.url.encodedParameters.append("status", status.value.encodeURLParameter())
                  }
                  if (filter != null) {
                    this.url.encodedParameters.append("filter", filter.value.encodeURLParameter())
                  }
                  if (perPage != null) {
                    this.url.encodedParameters.append("per_page", perPage.toString())
                  }
                  if (page != null) {
                    this.url.encodedParameters.append("page", page.toString())
                  }
                  if (appId != null) {
                    this.url.encodedParameters.append("app_id", appId.toString())
                  }
                }
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }

              @Serializable
              public enum class Status(
                public val `value`: String,
              ) {
                @SerialName("queued")
                Queued("queued"),
                @SerialName("in_progress")
                InProgress("in_progress"),
                @SerialName("completed")
                Completed("completed"),
                ;
              }

              @Serializable
              public enum class Filter(
                public val `value`: String,
              ) {
                @SerialName("latest")
                Latest("latest"),
                @SerialName("all")
                All("all"),
                ;
              }

              @Serializable
              public data class Response(
                @SerialName("total_count")
                public val totalCount: Long,
                @SerialName("check_runs")
                public val checkRuns: List<CheckRun>,
              )
            }
          }

          public class CheckSuites internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _ref: String,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo, _ref)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _ref: String,
            ) {
              public suspend operator fun invoke(
                appId: Long? = null,
                checkName: String? = null,
                perPage: Long? = 30L,
                page: Long? = 1L,
              ): Response {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/commits/${this._ref.encodeURLParameter()}/check-suites") {
                  if (appId != null) {
                    this.url.encodedParameters.append("app_id", appId.toString())
                  }
                  if (checkName != null) {
                    this.url.encodedParameters.append("check_name", checkName.encodeURLParameter())
                  }
                  if (perPage != null) {
                    this.url.encodedParameters.append("per_page", perPage.toString())
                  }
                  if (page != null) {
                    this.url.encodedParameters.append("page", page.toString())
                  }
                }
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }

              @Serializable
              public data class Response(
                @SerialName("total_count")
                public val totalCount: Long,
                @SerialName("check_suites")
                public val checkSuites: List<CheckSuite>,
              )
            }
          }

          public class Status internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _ref: String,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo, _ref)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _ref: String,
            ) {
              public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/commits/${this._ref.encodeURLParameter()}/status") {
                  if (perPage != null) {
                    this.url.encodedParameters.append("per_page", perPage.toString())
                  }
                  if (page != null) {
                    this.url.encodedParameters.append("page", page.toString())
                  }
                }
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  404 -> Response.NotFound(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: CombinedCommitStatus,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response
              }
            }
          }

          public class Statuses internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _ref: String,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo, _ref)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _ref: String,
            ) {
              public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/commits/${this._ref.encodeURLParameter()}/statuses") {
                  if (perPage != null) {
                    this.url.encodedParameters.append("per_page", perPage.toString())
                  }
                  if (page != null) {
                    this.url.encodedParameters.append("page", page.toString())
                  }
                }
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  301 -> Response.MovedPermanently(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: List<io.github.model.Status>,
                ) : Response

                public data class MovedPermanently(
                  public val `value`: BasicError,
                ) : Response
              }
            }
          }
        }
      }

      public class Community internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public val profile: Profile = Profile(__client, _owner, _repo)

        public class Profile internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public val `get`: Get = Get(__client, _owner, _repo)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public suspend operator fun invoke(): CommunityProfile {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/community/profile")
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }
          }
        }
      }

      public class Compare internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public fun basehead(basehead: String): BaseheadPath = BaseheadPath(__client, _owner, _repo, basehead)

        public class BaseheadPath internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
          private val _basehead: String,
        ) {
          public val `get`: Get = Get(__client, _owner, _repo, _basehead)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _basehead: String,
          ) {
            public suspend operator fun invoke(page: Long? = 1L, perPage: Long? = 30L): Response {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/compare/${this._basehead.encodeURLParameter()}") {
                if (page != null) {
                  this.url.encodedParameters.append("page", page.toString())
                }
                if (perPage != null) {
                  this.url.encodedParameters.append("per_page", perPage.toString())
                }
              }
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                404 -> Response.NotFound(response.body())
                500 -> Response.InternalServerError(response.body())
                503 -> response.body<Response.ServiceUnavailable>()
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: CommitComparison,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response

              public data class InternalServerError(
                public val `value`: BasicError,
              ) : Response

              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              public data class ServiceUnavailable(
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val code: String? = null,
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val message: String? = null,
                @SerialName("documentation_url")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val documentationUrl: String? = null,
              ) : Response
            }
          }
        }
      }

      public class Contents internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public fun path(path: String): PathPath = PathPath(__client, _owner, _repo, path)

        public class PathPath internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
          private val _path: String,
        ) {
          public val delete: Delete = Delete(__client, _owner, _repo, _path)

          public val `get`: Get = Get(__client, _owner, _repo, _path)

          public val put: Put = Put(__client, _owner, _repo, _path)

          public class Delete internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _path: String,
          ) {
            public suspend operator fun invoke(
              message: String,
              sha: String,
              branch: String? = null,
              committer: Committer? = null,
              author: Author? = null,
            ): Response {
              val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/contents/${this._path.encodeURLParameter()}") {
                contentType(ContentType.Application.Json)
                setBody(Body(message = message, sha = sha, branch = branch, committer = committer, author = author))
              }
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                404 -> Response.NotFound(response.body())
                409 -> Response.Conflict(response.body())
                422 -> Response.UnprocessableEntity(response.body())
                503 -> response.body<Response.ServiceUnavailable>()
                else -> throw ResponseException(response, "")
              }
            }

            /**
             * object containing information about the committer.
             */
            @OptIn(ExperimentalSerializationApi::class)
            @Serializable
            public data class Committer(
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val name: String? = null,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val email: String? = null,
            )

            /**
             * object containing information about the author.
             */
            @OptIn(ExperimentalSerializationApi::class)
            @Serializable
            public data class Author(
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val name: String? = null,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val email: String? = null,
            )

            @OptIn(ExperimentalSerializationApi::class)
            @Serializable
            internal data class Body(
              public val message: String,
              public val sha: String,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val branch: String? = null,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val committer: Committer? = null,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val author: Author? = null,
            )

            public sealed interface Response {
              public data class Ok(
                public val `value`: FileCommit,
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

              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              public data class ServiceUnavailable(
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val code: String? = null,
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val message: String? = null,
                @SerialName("documentation_url")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val documentationUrl: String? = null,
              ) : Response
            }
          }

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _path: String,
          ) {
            public suspend fun vndGithubObject(ref: String? = null, body: JsonElement? = null): VndGithubObjectResponse {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/contents/${this._path.encodeURLParameter()}") {
                `header`(HttpHeaders.Accept, ContentType("application", "vnd.github.object"))
                if (ref != null) {
                  this.url.encodedParameters.append("ref", ref.encodeURLParameter())
                }
                body?.let {
                  contentType(ContentType.Application.Json)
                  setBody(it)
                }
              }
              return when (response.status.value) {
                200 -> VndGithubObjectResponse.Ok(response.body())
                302 -> Found
                304 -> NotModified
                403 -> Forbidden(response.body())
                404 -> NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public suspend fun json(ref: String? = null, body: JsonElement? = null): JsonResponse {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/contents/${this._path.encodeURLParameter()}") {
                `header`(HttpHeaders.Accept, ContentType.Application.Json)
                if (ref != null) {
                  this.url.encodedParameters.append("ref", ref.encodeURLParameter())
                }
                body?.let {
                  contentType(ContentType.Application.Json)
                  setBody(it)
                }
              }
              return when (response.status.value) {
                200 -> response.body<JsonResponse.Ok>()
                302 -> Found
                304 -> NotModified
                403 -> Forbidden(response.body())
                404 -> NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface VndGithubObjectResponse {
              public data class Ok(
                public val `value`: ContentTree,
              ) : VndGithubObjectResponse
            }

            public sealed interface JsonResponse {
              @OptIn(ExperimentalSerializationApi::class)
              @JsonClassDiscriminator("type")
              @Serializable
              public sealed interface Ok : JsonResponse {
                @Serializable
                @JvmInline
                @SerialName("array")
                public value class Array(
                  public val `value`: List<Array>,
                ) : Ok

                /**
                 * Content File
                 */
                @SerialName("file")
                @OptIn(ExperimentalSerializationApi::class)
                @Serializable
                public data class File(
                  public val encoding: String,
                  public val size: Long,
                  public val name: String,
                  public val path: String,
                  public val content: String,
                  public val sha: String,
                  public val url: String,
                  @SerialName("git_url")
                  public val gitUrl: String?,
                  @SerialName("html_url")
                  public val htmlUrl: String?,
                  @SerialName("download_url")
                  public val downloadUrl: String?,
                  @SerialName("_links")
                  public val links: Links,
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val target: String? = null,
                  @SerialName("submodule_git_url")
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val submoduleGitUrl: String? = null,
                ) : Ok {
                  @Serializable
                  public data class Links(
                    public val git: String?,
                    public val html: String?,
                    public val self: String,
                  )
                }

                /**
                 * An object describing a symlink
                 */
                @SerialName("symlink")
                @Serializable
                public data class Symlink(
                  public val target: String,
                  public val size: Long,
                  public val name: String,
                  public val path: String,
                  public val sha: String,
                  public val url: String,
                  @SerialName("git_url")
                  public val gitUrl: String?,
                  @SerialName("html_url")
                  public val htmlUrl: String?,
                  @SerialName("download_url")
                  public val downloadUrl: String?,
                  @SerialName("_links")
                  public val links: Links,
                ) : Ok {
                  @Serializable
                  public data class Links(
                    public val git: String?,
                    public val html: String?,
                    public val self: String,
                  )
                }

                /**
                 * An object describing a submodule
                 */
                @SerialName("submodule")
                @Serializable
                public data class Submodule(
                  @SerialName("submodule_git_url")
                  public val submoduleGitUrl: String,
                  public val size: Long,
                  public val name: String,
                  public val path: String,
                  public val sha: String,
                  public val url: String,
                  @SerialName("git_url")
                  public val gitUrl: String?,
                  @SerialName("html_url")
                  public val htmlUrl: String?,
                  @SerialName("download_url")
                  public val downloadUrl: String?,
                  @SerialName("_links")
                  public val links: Links,
                ) : Ok {
                  @Serializable
                  public data class Links(
                    public val git: String?,
                    public val html: String?,
                    public val self: String,
                  )
                }
              }
            }

            public data object Found : VndGithubObjectResponse, JsonResponse

            public data object NotModified : VndGithubObjectResponse, JsonResponse

            public data class Forbidden(
              public val `value`: BasicError,
            ) : VndGithubObjectResponse,
                JsonResponse

            public data class NotFound(
              public val `value`: BasicError,
            ) : VndGithubObjectResponse,
                JsonResponse
          }

          public class Put internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _path: String,
          ) {
            public suspend operator fun invoke(
              message: String,
              content: String,
              sha: String? = null,
              branch: String? = null,
              committer: Committer? = null,
              author: Author? = null,
            ): Response {
              val response = __client.put("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/contents/${this._path.encodeURLParameter()}") {
                contentType(ContentType.Application.Json)
                setBody(Body(message = message, content = content, sha = sha, branch = branch, committer = committer, author = author))
              }
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                201 -> Response.Created(response.body())
                404 -> Response.NotFound(response.body())
                409 -> response.body<Response.Conflict>()
                422 -> Response.UnprocessableEntity(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            /**
             * The person that committed the file. Default: the authenticated user.
             */
            @OptIn(ExperimentalSerializationApi::class)
            @Serializable
            public data class Committer(
              public val name: String,
              public val email: String,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val date: String? = null,
            )

            /**
             * The author of the file. Default: The `committer` or the authenticated user if you omit `committer`.
             */
            @OptIn(ExperimentalSerializationApi::class)
            @Serializable
            public data class Author(
              public val name: String,
              public val email: String,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val date: String? = null,
            )

            @OptIn(ExperimentalSerializationApi::class)
            @Serializable
            internal data class Body(
              public val message: String,
              public val content: String,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val sha: String? = null,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val branch: String? = null,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val committer: Committer? = null,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val author: Author? = null,
            )

            public sealed interface Response {
              public data class Ok(
                public val `value`: FileCommit,
              ) : Response

              public data class Created(
                public val `value`: FileCommit,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response

              @Serializable(with = Conflict.Serializer::class)
              public sealed interface Conflict : Response {
                @Serializable
                @JvmInline
                public value class CaseBasicError(
                  public val `value`: BasicError,
                ) : Conflict

                @Serializable
                @JvmInline
                public value class CaseRepositoryRuleViolationError(
                  public val `value`: RepositoryRuleViolationError,
                ) : Conflict

                public object Serializer : KSerializer<Conflict> {
                  @OptIn(
                    InternalSerializationApi::class,
                    ExperimentalSerializationApi::class,
                  )
                  override val descriptor: SerialDescriptor =
                      buildSerialDescriptor("io.github.api.Repos.OwnerPath.RepoPath.Contents.PathPath.Put.Response.Conflict", PolymorphicKind.SEALED) {
                    element("CaseBasicError", BasicError.serializer().descriptor)
                    element("CaseRepositoryRuleViolationError", RepositoryRuleViolationError.serializer().descriptor)
                  }

                  override fun deserialize(decoder: Decoder): Conflict {
                    val value = decoder.decodeSerializableValue(JsonElement.serializer())
                    val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                    return json.attemptDeserialize(
                      value,
                      CaseBasicError::class to { CaseBasicError(decodeFromJsonElement(BasicError.serializer(), it)) },
                      CaseRepositoryRuleViolationError::class to { CaseRepositoryRuleViolationError(decodeFromJsonElement(RepositoryRuleViolationError.serializer(), it)) },
                    )
                  }

                  override fun serialize(encoder: Encoder, `value`: Conflict) {
                    when(value) {
                      is CaseBasicError -> encoder.encodeSerializableValue(BasicError.serializer(), value.value)
                      is CaseRepositoryRuleViolationError -> encoder.encodeSerializableValue(RepositoryRuleViolationError.serializer(), value.value)
                    }
                  }
                }
              }

              public data class UnprocessableEntity(
                public val `value`: ValidationError,
              ) : Response
            }
          }
        }
      }

      public class Contributors internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public val `get`: Get = Get(__client, _owner, _repo)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(
            anon: String? = null,
            perPage: Long? = 30L,
            page: Long? = 1L,
          ): Response {
            val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/contributors") {
              if (anon != null) {
                this.url.encodedParameters.append("anon", anon.encodeURLParameter())
              }
              if (perPage != null) {
                this.url.encodedParameters.append("per_page", perPage.toString())
              }
              if (page != null) {
                this.url.encodedParameters.append("page", page.toString())
              }
            }
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              204 -> Response.NoContent
              403 -> Response.Forbidden(response.body())
              404 -> Response.NotFound(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: List<Contributor>,
            ) : Response

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

      public class Dependabot internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public val alerts: Alerts = Alerts(__client, _owner, _repo)

        public val secrets: Secrets = Secrets(__client, _owner, _repo)

        public class Alerts internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public val `get`: Get = Get(__client, _owner, _repo)

          public fun alertNumber(alertNumber: Long): AlertNumberPath = AlertNumberPath(__client, _owner, _repo, alertNumber)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public suspend operator fun invoke(
              state: String? = null,
              severity: String? = null,
              ecosystem: String? = null,
              `package`: String? = null,
              manifest: String? = null,
              epssPercentage: String? = null,
              has: Has? = null,
              assignee: String? = null,
              scope: Scope? = null,
              sort: Sort? = Sort.Created,
              direction: Direction? = Direction.Desc,
              before: String? = null,
              after: String? = null,
              perPage: Long? = 30L,
            ): Response {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/dependabot/alerts") {
                if (state != null) {
                  this.url.encodedParameters.append("state", state.encodeURLParameter())
                }
                if (severity != null) {
                  this.url.encodedParameters.append("severity", severity.encodeURLParameter())
                }
                if (ecosystem != null) {
                  this.url.encodedParameters.append("ecosystem", ecosystem.encodeURLParameter())
                }
                if (`package` != null) {
                  this.url.encodedParameters.append("package", `package`.encodeURLParameter())
                }
                if (manifest != null) {
                  this.url.encodedParameters.append("manifest", manifest.encodeURLParameter())
                }
                if (epssPercentage != null) {
                  this.url.encodedParameters.append("epss_percentage", epssPercentage.encodeURLParameter())
                }
                if (has != null) {
                  when (has) {
                    is Has.CaseString -> {
                      this.url.encodedParameters.append("has", has.value.encodeURLParameter())
                    }
                    is Has.CasePatchList -> {
                      has.value.forEach { this.url.encodedParameters.append("has", it.value.encodeURLParameter()) }
                    }
                  }
                }
                if (assignee != null) {
                  this.url.encodedParameters.append("assignee", assignee.encodeURLParameter())
                }
                if (scope != null) {
                  this.url.encodedParameters.append("scope", scope.value.encodeURLParameter())
                }
                if (sort != null) {
                  this.url.encodedParameters.append("sort", sort.value.encodeURLParameter())
                }
                if (direction != null) {
                  this.url.encodedParameters.append("direction", direction.value.encodeURLParameter())
                }
                if (before != null) {
                  this.url.encodedParameters.append("before", before.encodeURLParameter())
                }
                if (after != null) {
                  this.url.encodedParameters.append("after", after.encodeURLParameter())
                }
                if (perPage != null) {
                  this.url.encodedParameters.append("per_page", perPage.toString())
                }
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
              public value class CasePatchList(
                public val `value`: List<Patch>,
              ) : Has

              @Serializable
              public enum class Patch(
                public val `value`: String,
              ) {
                @SerialName("patch")
                Patch("patch"),
                ;
              }

              public object Serializer : KSerializer<Has> {
                @OptIn(
                  InternalSerializationApi::class,
                  ExperimentalSerializationApi::class,
                )
                override val descriptor: SerialDescriptor =
                    buildSerialDescriptor("io.github.api.Repos.OwnerPath.RepoPath.Dependabot.Alerts.Get.Has", PolymorphicKind.SEALED) {
                  element("CaseString", String.serializer().descriptor)
                  element("CasePatchList", ListSerializer(Patch.serializer()).descriptor)
                }

                override fun deserialize(decoder: Decoder): Has {
                  val value = decoder.decodeSerializableValue(JsonElement.serializer())
                  val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                  return when {
                    value is JsonPrimitive && value.isString -> json.attemptDeserialize(
                      value,
                      CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                    )
                    value is JsonArray -> json.attemptDeserialize(
                      value,
                      CasePatchList::class to { CasePatchList(decodeFromJsonElement(ListSerializer(Patch.serializer()), it)) },
                    )
                    else -> throw UnionSerializationException(value, emptyMap())
                  }
                }

                override fun serialize(encoder: Encoder, `value`: Has) {
                  when(value) {
                    is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                    is CasePatchList -> encoder.encodeSerializableValue(ListSerializer(Patch.serializer()), value.value)
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
                public val `value`: List<DependabotAlert>,
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

          public class AlertNumberPath internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _alertNumber: Long,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo, _alertNumber)

            public val patch: Patch = Patch(__client, _owner, _repo, _alertNumber)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _alertNumber: Long,
            ) {
              public suspend operator fun invoke(): Response {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/dependabot/alerts/${this._alertNumber}")
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
                  public val `value`: DependabotAlert,
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
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _alertNumber: Long,
            ) {
              public suspend operator fun invoke(
                state: State? = null,
                dismissedReason: DismissedReason? = null,
                dismissedComment: String? = null,
                assignees: List<String>? = null,
              ): Response {
                val response = __client.patch("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/dependabot/alerts/${this._alertNumber}") {
                  contentType(ContentType.Application.Json)
                  setBody(Body(state = state, dismissedReason = dismissedReason, dismissedComment = dismissedComment, assignees = assignees))
                }
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  400 -> Response.BadRequest(response.body())
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  409 -> Response.Conflict(response.body())
                  422 -> Response.UnprocessableEntity(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              @Serializable
              public enum class State(
                public val `value`: String,
              ) {
                @SerialName("dismissed")
                Dismissed("dismissed"),
                @SerialName("open")
                Open("open"),
                ;
              }

              @Serializable
              public enum class DismissedReason(
                public val `value`: String,
              ) {
                @SerialName("fix_started")
                FixStarted("fix_started"),
                @SerialName("inaccurate")
                Inaccurate("inaccurate"),
                @SerialName("no_bandwidth")
                NoBandwidth("no_bandwidth"),
                @SerialName("not_used")
                NotUsed("not_used"),
                @SerialName("tolerable_risk")
                TolerableRisk("tolerable_risk"),
                ;
              }

              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              internal data class Body(
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val state: State? = null,
                @SerialName("dismissed_reason")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val dismissedReason: DismissedReason? = null,
                @SerialName("dismissed_comment")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val dismissedComment: String? = null,
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val assignees: List<String>? = null,
              )

              public sealed interface Response {
                public data class Ok(
                  public val `value`: DependabotAlert,
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
                  public val `value`: ValidationErrorSimple,
                ) : Response
              }
            }
          }
        }

        public class Secrets internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public val `get`: Get = Get(__client, _owner, _repo)

          public val publicKey: PublicKey = PublicKey(__client, _owner, _repo)

          public fun secretName(secretName: String): SecretNamePath = SecretNamePath(__client, _owner, _repo, secretName)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/dependabot/secrets") {
                if (perPage != null) {
                  this.url.encodedParameters.append("per_page", perPage.toString())
                }
                if (page != null) {
                  this.url.encodedParameters.append("page", page.toString())
                }
              }
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }

            @Serializable
            public data class Response(
              @SerialName("total_count")
              public val totalCount: Long,
              public val secrets: List<DependabotSecret>,
            )
          }

          public class PublicKey internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
            ) {
              public suspend operator fun invoke(): DependabotPublicKey {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/dependabot/secrets/public-key")
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }
            }
          }

          public class SecretNamePath internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _secretName: String,
          ) {
            public val delete: Delete = Delete(__client, _owner, _repo, _secretName)

            public val `get`: Get = Get(__client, _owner, _repo, _secretName)

            public val put: Put = Put(__client, _owner, _repo, _secretName)

            public class Delete internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _secretName: String,
            ) {
              public suspend operator fun invoke() {
                val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/dependabot/secrets/${this._secretName.encodeURLParameter()}")
                if (response.status.value != 204) throw ResponseException(response, "")
              }
            }

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _secretName: String,
            ) {
              public suspend operator fun invoke(): DependabotSecret {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/dependabot/secrets/${this._secretName.encodeURLParameter()}")
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }
            }

            public class Put internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _secretName: String,
            ) {
              public suspend operator fun invoke(encryptedValue: String? = null, keyId: String? = null): Response {
                val response = __client.put("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/dependabot/secrets/${this._secretName.encodeURLParameter()}") {
                  contentType(ContentType.Application.Json)
                  setBody(Body(encryptedValue = encryptedValue, keyId = keyId))
                }
                return when (response.status.value) {
                  201 -> Response.Created(response.body())
                  204 -> Response.NoContent
                  else -> throw ResponseException(response, "")
                }
              }

              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              internal data class Body(
                @SerialName("encrypted_value")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val encryptedValue: String? = null,
                @SerialName("key_id")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val keyId: String? = null,
              )

              public sealed interface Response {
                public data class Created(
                  public val `value`: EmptyObject,
                ) : Response

                public data object NoContent : Response
              }
            }
          }
        }
      }

      public class DependencyGraph internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public val compare: Compare = Compare(__client, _owner, _repo)

        public val sbom: Sbom = Sbom(__client, _owner, _repo)

        public val snapshots: Snapshots = Snapshots(__client, _owner, _repo)

        public class Compare internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public fun basehead(basehead: String): BaseheadPath = BaseheadPath(__client, _owner, _repo, basehead)

          public class BaseheadPath internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _basehead: String,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo, _basehead)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _basehead: String,
            ) {
              public suspend operator fun invoke(name: String? = null): Response {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/dependency-graph/compare/${this._basehead.encodeURLParameter()}") {
                  if (name != null) {
                    this.url.encodedParameters.append("name", name.encodeURLParameter())
                  }
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
                  public val `value`: DependencyGraphDiff,
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

        public class Sbom internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public val `get`: Get = Get(__client, _owner, _repo)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/dependency-graph/sbom")
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: DependencyGraphSpdxSbom,
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

        public class Snapshots internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public val post: Post = Post(__client, _owner, _repo)

          public class Post internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public suspend operator fun invoke(body: Snapshot): Response {
              val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/dependency-graph/snapshots") {
                contentType(ContentType.Application.Json)
                setBody(body)
              }
              if (response.status.value != 201) throw ResponseException(response, "")
              return response.body()
            }

            @Serializable
            public data class Response(
              public val id: Long,
              @SerialName("created_at")
              public val createdAt: String,
              public val result: String,
              public val message: String,
            )
          }
        }
      }

      public class Deployments internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public val `get`: Get = Get(__client, _owner, _repo)

        public val post: Post = Post(__client, _owner, _repo)

        public fun deploymentId(deploymentId: Long): DeploymentIdPath = DeploymentIdPath(__client, _owner, _repo, deploymentId)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(
            sha: String? = "none",
            ref: String? = "none",
            task: String? = "none",
            environment: String? = "none",
            perPage: Long? = 30L,
            page: Long? = 1L,
          ): List<Deployment> {
            val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/deployments") {
              if (sha != null) {
                this.url.encodedParameters.append("sha", sha.encodeURLParameter())
              }
              if (ref != null) {
                this.url.encodedParameters.append("ref", ref.encodeURLParameter())
              }
              if (task != null) {
                this.url.encodedParameters.append("task", task.encodeURLParameter())
              }
              if (environment != null) {
                this.url.encodedParameters.append("environment", environment.encodeURLParameter())
              }
              if (perPage != null) {
                this.url.encodedParameters.append("per_page", perPage.toString())
              }
              if (page != null) {
                this.url.encodedParameters.append("page", page.toString())
              }
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class Post internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(
            ref: String,
            task: String? = null,
            autoMerge: Boolean? = null,
            requiredContexts: List<String>? = null,
            payload: Payload? = null,
            environment: String? = null,
            description: Optional<String> = Optional.Absent,
            transientEnvironment: Boolean? = null,
            productionEnvironment: Boolean? = null,
          ): Response {
            val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/deployments") {
              contentType(ContentType.Application.Json)
              setBody(Body(ref = ref, task = task, autoMerge = autoMerge, requiredContexts = requiredContexts, payload = payload, environment = environment, description = description, transientEnvironment = transientEnvironment, productionEnvironment = productionEnvironment))
            }
            return when (response.status.value) {
              201 -> Response.Created(response.body())
              202 -> response.body<Response.Accepted>()
              409 -> Response.Conflict
              422 -> Response.UnprocessableEntity(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          @Serializable(with = Payload.Serializer::class)
          public sealed interface Payload {
            @Serializable
            @JvmInline
            public value class CaseJsonElement(
              public val `value`: JsonElement,
            ) : Payload

            @Serializable
            @JvmInline
            public value class CaseString(
              public val `value`: String,
            ) : Payload

            public object Serializer : KSerializer<Payload> {
              @OptIn(
                InternalSerializationApi::class,
                ExperimentalSerializationApi::class,
              )
              override val descriptor: SerialDescriptor =
                  buildSerialDescriptor("io.github.api.Repos.OwnerPath.RepoPath.Deployments.Post.Payload", PolymorphicKind.SEALED) {
                element("CaseJsonElement", JsonElement.serializer().descriptor)
                element("CaseString", String.serializer().descriptor)
              }

              override fun deserialize(decoder: Decoder): Payload {
                val value = decoder.decodeSerializableValue(JsonElement.serializer())
                val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                return when {
                  value is JsonPrimitive && value.isString -> json.attemptDeserialize(
                    value,
                    CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                    CaseJsonElement::class to { CaseJsonElement(decodeFromJsonElement(JsonElement.serializer(), it)) },
                  )
                  else -> json.attemptDeserialize(
                    value,
                    CaseJsonElement::class to { CaseJsonElement(decodeFromJsonElement(JsonElement.serializer(), it)) },
                  )
                }
              }

              override fun serialize(encoder: Encoder, `value`: Payload) {
                when(value) {
                  is CaseJsonElement -> encoder.encodeSerializableValue(JsonElement.serializer(), value.value)
                  is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                }
              }
            }
          }

          @OptIn(ExperimentalSerializationApi::class)
          @Serializable
          internal data class Body(
            public val ref: String,
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val task: String? = null,
            @SerialName("auto_merge")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val autoMerge: Boolean? = null,
            @SerialName("required_contexts")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val requiredContexts: List<String>? = null,
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val payload: Payload? = null,
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val environment: String? = null,
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val description: Optional<String> = Optional.Absent,
            @SerialName("transient_environment")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val transientEnvironment: Boolean? = null,
            @SerialName("production_environment")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val productionEnvironment: Boolean? = null,
          )

          public sealed interface Response {
            public data class Created(
              public val `value`: Deployment,
            ) : Response

            @OptIn(ExperimentalSerializationApi::class)
            @Serializable
            public data class Accepted(
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val message: String? = null,
            ) : Response

            public data object Conflict : Response

            public data class UnprocessableEntity(
              public val `value`: ValidationError,
            ) : Response
          }
        }

        public class DeploymentIdPath internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
          private val _deploymentId: Long,
        ) {
          public val delete: Delete = Delete(__client, _owner, _repo, _deploymentId)

          public val `get`: Get = Get(__client, _owner, _repo, _deploymentId)

          public val statuses: Statuses = Statuses(__client, _owner, _repo, _deploymentId)

          public class Delete internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _deploymentId: Long,
          ) {
            public suspend operator fun invoke(): Response {
              val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/deployments/${this._deploymentId}")
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

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _deploymentId: Long,
          ) {
            public suspend operator fun invoke(): Response {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/deployments/${this._deploymentId}")
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: Deployment,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response
            }
          }

          public class Statuses internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _deploymentId: Long,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo, _deploymentId)

            public val post: Post = Post(__client, _owner, _repo, _deploymentId)

            public fun statusId(statusId: Long): StatusIdPath = StatusIdPath(__client, _owner, _repo, _deploymentId, statusId)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _deploymentId: Long,
            ) {
              public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/deployments/${this._deploymentId}/statuses") {
                  if (perPage != null) {
                    this.url.encodedParameters.append("per_page", perPage.toString())
                  }
                  if (page != null) {
                    this.url.encodedParameters.append("page", page.toString())
                  }
                }
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  404 -> Response.NotFound(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: List<DeploymentStatus>,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response
              }
            }

            public class Post internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _deploymentId: Long,
            ) {
              public suspend operator fun invoke(
                state: State,
                targetUrl: String? = null,
                logUrl: String? = null,
                description: String? = null,
                environment: String? = null,
                environmentUrl: String? = null,
                autoInactive: Boolean? = null,
              ): Response {
                val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/deployments/${this._deploymentId}/statuses") {
                  contentType(ContentType.Application.Json)
                  setBody(Body(state = state, targetUrl = targetUrl, logUrl = logUrl, description = description, environment = environment, environmentUrl = environmentUrl, autoInactive = autoInactive))
                }
                return when (response.status.value) {
                  201 -> Response.Created(response.body())
                  422 -> Response.UnprocessableEntity(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              @Serializable
              public enum class State(
                public val `value`: String,
              ) {
                @SerialName("error")
                Error("error"),
                @SerialName("failure")
                Failure("failure"),
                @SerialName("inactive")
                Inactive("inactive"),
                @SerialName("in_progress")
                InProgress("in_progress"),
                @SerialName("queued")
                Queued("queued"),
                @SerialName("pending")
                Pending("pending"),
                @SerialName("success")
                Success("success"),
                ;
              }

              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              internal data class Body(
                public val state: State,
                @SerialName("target_url")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val targetUrl: String? = null,
                @SerialName("log_url")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val logUrl: String? = null,
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val description: String? = null,
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val environment: String? = null,
                @SerialName("environment_url")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val environmentUrl: String? = null,
                @SerialName("auto_inactive")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val autoInactive: Boolean? = null,
              )

              public sealed interface Response {
                public data class Created(
                  public val `value`: DeploymentStatus,
                ) : Response

                public data class UnprocessableEntity(
                  public val `value`: ValidationError,
                ) : Response
              }
            }

            public class StatusIdPath internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _deploymentId: Long,
              private val _statusId: Long,
            ) {
              public val `get`: Get = Get(__client, _owner, _repo, _deploymentId, _statusId)

              public class Get internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _deploymentId: Long,
                private val _statusId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/deployments/${this._deploymentId}/statuses/${this._statusId}")
                  return when (response.status.value) {
                    200 -> Response.Ok(response.body())
                    404 -> Response.NotFound(response.body())
                    else -> throw ResponseException(response, "")
                  }
                }

                public sealed interface Response {
                  public data class Ok(
                    public val `value`: DeploymentStatus,
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

      public class Dispatches internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public val post: Post = Post(__client, _owner, _repo)

        public class Post internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(eventType: String, clientPayload: JsonElement? = null): Response {
            val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/dispatches") {
              contentType(ContentType.Application.Json)
              setBody(Body(eventType = eventType, clientPayload = clientPayload))
            }
            return when (response.status.value) {
              204 -> Response.NoContent
              404 -> Response.NotFound(response.body())
              422 -> Response.UnprocessableEntity(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          @OptIn(ExperimentalSerializationApi::class)
          @Serializable
          internal data class Body(
            @SerialName("event_type")
            public val eventType: String,
            @SerialName("client_payload")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val clientPayload: JsonElement? = null,
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

      public class Environments internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public val `get`: Get = Get(__client, _owner, _repo)

        public fun environmentName(environmentName: String): EnvironmentNamePath = EnvironmentNamePath(__client, _owner, _repo, environmentName)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
            val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/environments") {
              if (perPage != null) {
                this.url.encodedParameters.append("per_page", perPage.toString())
              }
              if (page != null) {
                this.url.encodedParameters.append("page", page.toString())
              }
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }

          @OptIn(ExperimentalSerializationApi::class)
          @Serializable
          public data class Response(
            @SerialName("total_count")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val totalCount: Long? = null,
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val environments: List<Environment>? = null,
          )
        }

        public class EnvironmentNamePath internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
          private val _environmentName: String,
        ) {
          public val delete: Delete = Delete(__client, _owner, _repo, _environmentName)

          public val `get`: Get = Get(__client, _owner, _repo, _environmentName)

          public val put: Put = Put(__client, _owner, _repo, _environmentName)

          public val deploymentBranchPolicies: DeploymentBranchPolicies =
              DeploymentBranchPolicies(__client, _owner, _repo, _environmentName)

          public val deploymentProtectionRules: DeploymentProtectionRules =
              DeploymentProtectionRules(__client, _owner, _repo, _environmentName)

          public val secrets: Secrets = Secrets(__client, _owner, _repo, _environmentName)

          public val variables: Variables = Variables(__client, _owner, _repo, _environmentName)

          public class Delete internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _environmentName: String,
          ) {
            public suspend operator fun invoke() {
              val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/environments/${this._environmentName.encodeURLParameter()}")
              if (response.status.value != 204) throw ResponseException(response, "")
            }
          }

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _environmentName: String,
          ) {
            public suspend operator fun invoke(): Environment {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/environments/${this._environmentName.encodeURLParameter()}")
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }
          }

          public class Put internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _environmentName: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = __client.put("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/environments/${this._environmentName.encodeURLParameter()}")
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                422 -> Response.UnprocessableEntity(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public suspend operator fun invoke(
              waitTimer: WaitTimer? = null,
              preventSelfReview: PreventSelfReview? = null,
              reviewers: Optional<List<Reviewers>> = Optional.Absent,
              deploymentBranchPolicy: Optional<DeploymentBranchPolicySettings> = Optional.Absent,
            ): Response {
              val response = __client.put("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/environments/${this._environmentName.encodeURLParameter()}") {
                contentType(ContentType.Application.Json)
                setBody(Body(waitTimer = waitTimer, preventSelfReview = preventSelfReview, reviewers = reviewers, deploymentBranchPolicy = deploymentBranchPolicy))
              }
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                422 -> Response.UnprocessableEntity(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            @OptIn(ExperimentalSerializationApi::class)
            @Serializable
            public data class Reviewers(
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val type: DeploymentReviewerType? = null,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val id: Long? = null,
            )

            @OptIn(ExperimentalSerializationApi::class)
            @Serializable
            internal data class Body(
              @SerialName("wait_timer")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val waitTimer: WaitTimer? = null,
              @SerialName("prevent_self_review")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val preventSelfReview: PreventSelfReview? = null,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val reviewers: Optional<List<Reviewers>> = Optional.Absent,
              @SerialName("deployment_branch_policy")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val deploymentBranchPolicy:
                  Optional<DeploymentBranchPolicySettings> = Optional.Absent,
            )

            public sealed interface Response {
              public data class Ok(
                public val `value`: Environment,
              ) : Response

              public data class UnprocessableEntity(
                public val `value`: BasicError,
              ) : Response
            }
          }

          public class DeploymentBranchPolicies internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _environmentName: String,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo, _environmentName)

            public val post: Post = Post(__client, _owner, _repo, _environmentName)

            public fun branchPolicyId(branchPolicyId: Long): BranchPolicyIdPath = BranchPolicyIdPath(__client, _owner, _repo, _environmentName, branchPolicyId)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _environmentName: String,
            ) {
              public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/environments/${this._environmentName.encodeURLParameter()}/deployment-branch-policies") {
                  if (perPage != null) {
                    this.url.encodedParameters.append("per_page", perPage.toString())
                  }
                  if (page != null) {
                    this.url.encodedParameters.append("page", page.toString())
                  }
                }
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }

              @Serializable
              public data class Response(
                @SerialName("total_count")
                public val totalCount: Long,
                @SerialName("branch_policies")
                public val branchPolicies: List<DeploymentBranchPolicy>,
              )
            }

            public class Post internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _environmentName: String,
            ) {
              public suspend operator fun invoke(body: DeploymentBranchPolicyNamePatternWithType): Response {
                val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/environments/${this._environmentName.encodeURLParameter()}/deployment-branch-policies") {
                  contentType(ContentType.Application.Json)
                  setBody(body)
                }
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  303 -> Response.SeeOther
                  404 -> Response.NotFound
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: DeploymentBranchPolicy,
                ) : Response

                public data object SeeOther : Response

                public data object NotFound : Response
              }
            }

            public class BranchPolicyIdPath internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _environmentName: String,
              private val _branchPolicyId: Long,
            ) {
              public val delete: Delete =
                  Delete(__client, _owner, _repo, _environmentName, _branchPolicyId)

              public val `get`: Get =
                  Get(__client, _owner, _repo, _environmentName, _branchPolicyId)

              public val put: Put = Put(__client, _owner, _repo, _environmentName, _branchPolicyId)

              public class Delete internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _environmentName: String,
                private val _branchPolicyId: Long,
              ) {
                public suspend operator fun invoke() {
                  val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/environments/${this._environmentName.encodeURLParameter()}/deployment-branch-policies/${this._branchPolicyId}")
                  if (response.status.value != 204) throw ResponseException(response, "")
                }
              }

              public class Get internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _environmentName: String,
                private val _branchPolicyId: Long,
              ) {
                public suspend operator fun invoke(): DeploymentBranchPolicy {
                  val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/environments/${this._environmentName.encodeURLParameter()}/deployment-branch-policies/${this._branchPolicyId}")
                  if (response.status.value != 200) throw ResponseException(response, "")
                  return response.body()
                }
              }

              public class Put internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _environmentName: String,
                private val _branchPolicyId: Long,
              ) {
                public suspend operator fun invoke(body: DeploymentBranchPolicyNamePattern): DeploymentBranchPolicy {
                  val response = __client.put("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/environments/${this._environmentName.encodeURLParameter()}/deployment-branch-policies/${this._branchPolicyId}") {
                    contentType(ContentType.Application.Json)
                    setBody(body)
                  }
                  if (response.status.value != 200) throw ResponseException(response, "")
                  return response.body()
                }
              }
            }
          }

          public class DeploymentProtectionRules internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _environmentName: String,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo, _environmentName)

            public val post: Post = Post(__client, _owner, _repo, _environmentName)

            public val apps: Apps = Apps(__client, _owner, _repo, _environmentName)

            public fun protectionRuleId(protectionRuleId: Long): ProtectionRuleIdPath = ProtectionRuleIdPath(__client, _owner, _repo, _environmentName, protectionRuleId)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _environmentName: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/environments/${this._environmentName.encodeURLParameter()}/deployment_protection_rules")
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }

              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              public data class Response(
                @SerialName("total_count")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val totalCount: Long? = null,
                @SerialName("custom_deployment_protection_rules")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val customDeploymentProtectionRules: List<DeploymentProtectionRule>? = null,
              )
            }

            public class Post internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _environmentName: String,
            ) {
              public suspend operator fun invoke(integrationId: Long? = null): DeploymentProtectionRule {
                val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/environments/${this._environmentName.encodeURLParameter()}/deployment_protection_rules") {
                  contentType(ContentType.Application.Json)
                  setBody(Body(integrationId = integrationId))
                }
                if (response.status.value != 201) throw ResponseException(response, "")
                return response.body()
              }

              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              internal data class Body(
                @SerialName("integration_id")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val integrationId: Long? = null,
              )
            }

            public class Apps internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _environmentName: String,
            ) {
              public val `get`: Get = Get(__client, _owner, _repo, _environmentName)

              public class Get internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _environmentName: String,
              ) {
                public suspend operator fun invoke(page: Long? = 1L, perPage: Long? = 30L): Response {
                  val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/environments/${this._environmentName.encodeURLParameter()}/deployment_protection_rules/apps") {
                    if (page != null) {
                      this.url.encodedParameters.append("page", page.toString())
                    }
                    if (perPage != null) {
                      this.url.encodedParameters.append("per_page", perPage.toString())
                    }
                  }
                  if (response.status.value != 200) throw ResponseException(response, "")
                  return response.body()
                }

                @OptIn(ExperimentalSerializationApi::class)
                @Serializable
                public data class Response(
                  @SerialName("total_count")
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val totalCount: Long? = null,
                  @SerialName("available_custom_deployment_protection_rule_integrations")
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val availableCustomDeploymentProtectionRuleIntegrations:
                      List<CustomDeploymentRuleApp>? = null,
                )
              }
            }

            public class ProtectionRuleIdPath internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _environmentName: String,
              private val _protectionRuleId: Long,
            ) {
              public val delete: Delete =
                  Delete(__client, _owner, _repo, _environmentName, _protectionRuleId)

              public val `get`: Get =
                  Get(__client, _owner, _repo, _environmentName, _protectionRuleId)

              public class Delete internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _environmentName: String,
                private val _protectionRuleId: Long,
              ) {
                public suspend operator fun invoke() {
                  val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/environments/${this._environmentName.encodeURLParameter()}/deployment_protection_rules/${this._protectionRuleId}")
                  if (response.status.value != 204) throw ResponseException(response, "")
                }
              }

              public class Get internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _environmentName: String,
                private val _protectionRuleId: Long,
              ) {
                public suspend operator fun invoke(): DeploymentProtectionRule {
                  val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/environments/${this._environmentName.encodeURLParameter()}/deployment_protection_rules/${this._protectionRuleId}")
                  if (response.status.value != 200) throw ResponseException(response, "")
                  return response.body()
                }
              }
            }
          }

          public class Secrets internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _environmentName: String,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo, _environmentName)

            public val publicKey: PublicKey = PublicKey(__client, _owner, _repo, _environmentName)

            public fun secretName(secretName: String): SecretNamePath = SecretNamePath(__client, _owner, _repo, _environmentName, secretName)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _environmentName: String,
            ) {
              public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/environments/${this._environmentName.encodeURLParameter()}/secrets") {
                  if (perPage != null) {
                    this.url.encodedParameters.append("per_page", perPage.toString())
                  }
                  if (page != null) {
                    this.url.encodedParameters.append("page", page.toString())
                  }
                }
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }

              @Serializable
              public data class Response(
                @SerialName("total_count")
                public val totalCount: Long,
                public val secrets: List<ActionsSecret>,
              )
            }

            public class PublicKey internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _environmentName: String,
            ) {
              public val `get`: Get = Get(__client, _owner, _repo, _environmentName)

              public class Get internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _environmentName: String,
              ) {
                public suspend operator fun invoke(): ActionsPublicKey {
                  val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/environments/${this._environmentName.encodeURLParameter()}/secrets/public-key")
                  if (response.status.value != 200) throw ResponseException(response, "")
                  return response.body()
                }
              }
            }

            public class SecretNamePath internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _environmentName: String,
              private val _secretName: String,
            ) {
              public val delete: Delete =
                  Delete(__client, _owner, _repo, _environmentName, _secretName)

              public val `get`: Get = Get(__client, _owner, _repo, _environmentName, _secretName)

              public val put: Put = Put(__client, _owner, _repo, _environmentName, _secretName)

              public class Delete internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _environmentName: String,
                private val _secretName: String,
              ) {
                public suspend operator fun invoke() {
                  val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/environments/${this._environmentName.encodeURLParameter()}/secrets/${this._secretName.encodeURLParameter()}")
                  if (response.status.value != 204) throw ResponseException(response, "")
                }
              }

              public class Get internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _environmentName: String,
                private val _secretName: String,
              ) {
                public suspend operator fun invoke(): ActionsSecret {
                  val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/environments/${this._environmentName.encodeURLParameter()}/secrets/${this._secretName.encodeURLParameter()}")
                  if (response.status.value != 200) throw ResponseException(response, "")
                  return response.body()
                }
              }

              public class Put internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _environmentName: String,
                private val _secretName: String,
              ) {
                public suspend operator fun invoke(encryptedValue: String, keyId: String): Response {
                  val response = __client.put("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/environments/${this._environmentName.encodeURLParameter()}/secrets/${this._secretName.encodeURLParameter()}") {
                    contentType(ContentType.Application.Json)
                    setBody(Body(encryptedValue = encryptedValue, keyId = keyId))
                  }
                  return when (response.status.value) {
                    201 -> Response.Created(response.body())
                    204 -> Response.NoContent
                    else -> throw ResponseException(response, "")
                  }
                }

                @Serializable
                internal data class Body(
                  @SerialName("encrypted_value")
                  public val encryptedValue: String,
                  @SerialName("key_id")
                  public val keyId: String,
                )

                public sealed interface Response {
                  public data class Created(
                    public val `value`: EmptyObject,
                  ) : Response

                  public data object NoContent : Response
                }
              }
            }
          }

          public class Variables internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _environmentName: String,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo, _environmentName)

            public val post: Post = Post(__client, _owner, _repo, _environmentName)

            public fun name(name: String): NamePath = NamePath(__client, _owner, _repo, _environmentName, name)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _environmentName: String,
            ) {
              public suspend operator fun invoke(perPage: Long? = 10L, page: Long? = 1L): Response {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/environments/${this._environmentName.encodeURLParameter()}/variables") {
                  if (perPage != null) {
                    this.url.encodedParameters.append("per_page", perPage.toString())
                  }
                  if (page != null) {
                    this.url.encodedParameters.append("page", page.toString())
                  }
                }
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }

              @Serializable
              public data class Response(
                @SerialName("total_count")
                public val totalCount: Long,
                public val variables: List<ActionsVariable>,
              )
            }

            public class Post internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _environmentName: String,
            ) {
              public suspend operator fun invoke(name: String, `value`: String): EmptyObject {
                val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/environments/${this._environmentName.encodeURLParameter()}/variables") {
                  contentType(ContentType.Application.Json)
                  setBody(Body(name = name, value = value))
                }
                if (response.status.value != 201) throw ResponseException(response, "")
                return response.body()
              }

              @Serializable
              internal data class Body(
                public val name: String,
                public val `value`: String,
              )
            }

            public class NamePath internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _environmentName: String,
              private val _name: String,
            ) {
              public val delete: Delete = Delete(__client, _owner, _repo, _environmentName, _name)

              public val `get`: Get = Get(__client, _owner, _repo, _environmentName, _name)

              public val patch: Patch = Patch(__client, _owner, _repo, _environmentName, _name)

              public class Delete internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _environmentName: String,
                private val _name: String,
              ) {
                public suspend operator fun invoke() {
                  val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/environments/${this._environmentName.encodeURLParameter()}/variables/${this._name.encodeURLParameter()}")
                  if (response.status.value != 204) throw ResponseException(response, "")
                }
              }

              public class Get internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _environmentName: String,
                private val _name: String,
              ) {
                public suspend operator fun invoke(): ActionsVariable {
                  val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/environments/${this._environmentName.encodeURLParameter()}/variables/${this._name.encodeURLParameter()}")
                  if (response.status.value != 200) throw ResponseException(response, "")
                  return response.body()
                }
              }

              public class Patch internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _environmentName: String,
                private val _name: String,
              ) {
                public suspend operator fun invoke(name: String? = null, `value`: String? = null) {
                  val response = __client.patch("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/environments/${this._environmentName.encodeURLParameter()}/variables/${this._name.encodeURLParameter()}") {
                    contentType(ContentType.Application.Json)
                    setBody(Body(name = name, value = value))
                  }
                  if (response.status.value != 204) throw ResponseException(response, "")
                }

                @OptIn(ExperimentalSerializationApi::class)
                @Serializable
                internal data class Body(
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val name: String? = null,
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val `value`: String? = null,
                )
              }
            }
          }
        }
      }

      public class Events internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public val `get`: Get = Get(__client, _owner, _repo)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<Event> {
            val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/events") {
              if (perPage != null) {
                this.url.encodedParameters.append("per_page", perPage.toString())
              }
              if (page != null) {
                this.url.encodedParameters.append("page", page.toString())
              }
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }
      }

      public class Forks internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public val `get`: Get = Get(__client, _owner, _repo)

        public val post: Post = Post(__client, _owner, _repo)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(
            sort: Sort? = Sort.Newest,
            perPage: Long? = 30L,
            page: Long? = 1L,
          ): Response {
            val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/forks") {
              if (sort != null) {
                this.url.encodedParameters.append("sort", sort.value.encodeURLParameter())
              }
              if (perPage != null) {
                this.url.encodedParameters.append("per_page", perPage.toString())
              }
              if (page != null) {
                this.url.encodedParameters.append("page", page.toString())
              }
            }
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              400 -> Response.BadRequest(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          @Serializable
          public enum class Sort(
            public val `value`: String,
          ) {
            @SerialName("newest")
            Newest("newest"),
            @SerialName("oldest")
            Oldest("oldest"),
            @SerialName("stargazers")
            Stargazers("stargazers"),
            @SerialName("watchers")
            Watchers("watchers"),
            ;
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: List<MinimalRepository>,
            ) : Response

            public data class BadRequest(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class Post internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/forks")
            return when (response.status.value) {
              202 -> Response.Accepted(response.body())
              400 -> Response.BadRequest(response.body())
              403 -> Response.Forbidden(response.body())
              404 -> Response.NotFound(response.body())
              422 -> Response.UnprocessableEntity(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public suspend operator fun invoke(
            organization: String? = null,
            name: String? = null,
            defaultBranchOnly: Boolean? = null,
          ): Response {
            val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/forks") {
              contentType(ContentType.Application.Json)
              setBody(Body(organization = organization, name = name, defaultBranchOnly = defaultBranchOnly))
            }
            return when (response.status.value) {
              202 -> Response.Accepted(response.body())
              400 -> Response.BadRequest(response.body())
              403 -> Response.Forbidden(response.body())
              404 -> Response.NotFound(response.body())
              422 -> Response.UnprocessableEntity(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          @OptIn(ExperimentalSerializationApi::class)
          @Serializable
          internal data class Body(
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val organization: String? = null,
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val name: String? = null,
            @SerialName("default_branch_only")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val defaultBranchOnly: Boolean? = null,
          )

          public sealed interface Response {
            public data class Accepted(
              public val `value`: FullRepository,
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

            public data class UnprocessableEntity(
              public val `value`: ValidationError,
            ) : Response
          }
        }
      }

      public class Git internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public val blobs: Blobs = Blobs(__client, _owner, _repo)

        public val commits: Commits = Commits(__client, _owner, _repo)

        public val matchingRefs: MatchingRefs = MatchingRefs(__client, _owner, _repo)

        public val ref: Ref = Ref(__client, _owner, _repo)

        public val refs: Refs = Refs(__client, _owner, _repo)

        public val tags: Tags = Tags(__client, _owner, _repo)

        public val trees: Trees = Trees(__client, _owner, _repo)

        public class Blobs internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public val post: Post = Post(__client, _owner, _repo)

          public fun fileSha(fileSha: String): FileShaPath = FileShaPath(__client, _owner, _repo, fileSha)

          public class Post internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public suspend operator fun invoke(content: String, encoding: String? = null): Response {
              val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/git/blobs") {
                contentType(ContentType.Application.Json)
                setBody(Body(content = content, encoding = encoding))
              }
              return when (response.status.value) {
                201 -> Response.Created(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                409 -> Response.Conflict(response.body())
                422 -> response.body<Response.UnprocessableEntity>()
                else -> throw ResponseException(response, "")
              }
            }

            @OptIn(ExperimentalSerializationApi::class)
            @Serializable
            internal data class Body(
              public val content: String,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val encoding: String? = null,
            )

            public sealed interface Response {
              public data class Created(
                public val `value`: ShortBlob,
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

              @Serializable(with = UnprocessableEntity.Serializer::class)
              public sealed interface UnprocessableEntity : Response {
                @Serializable
                @JvmInline
                public value class CaseValidationError(
                  public val `value`: ValidationError,
                ) : UnprocessableEntity

                @Serializable
                @JvmInline
                public value class CaseRepositoryRuleViolationError(
                  public val `value`: RepositoryRuleViolationError,
                ) : UnprocessableEntity

                public object Serializer : KSerializer<UnprocessableEntity> {
                  @OptIn(
                    InternalSerializationApi::class,
                    ExperimentalSerializationApi::class,
                  )
                  override val descriptor: SerialDescriptor =
                      buildSerialDescriptor("io.github.api.Repos.OwnerPath.RepoPath.Git.Blobs.Post.Response.UnprocessableEntity", PolymorphicKind.SEALED) {
                    element("CaseValidationError", ValidationError.serializer().descriptor)
                    element("CaseRepositoryRuleViolationError", RepositoryRuleViolationError.serializer().descriptor)
                  }

                  override fun deserialize(decoder: Decoder): UnprocessableEntity {
                    val value = decoder.decodeSerializableValue(JsonElement.serializer())
                    val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                    return json.attemptDeserialize(
                      value,
                      CaseValidationError::class to { CaseValidationError(decodeFromJsonElement(ValidationError.serializer(), it)) },
                      CaseRepositoryRuleViolationError::class to { CaseRepositoryRuleViolationError(decodeFromJsonElement(RepositoryRuleViolationError.serializer(), it)) },
                    )
                  }

                  override fun serialize(encoder: Encoder, `value`: UnprocessableEntity) {
                    when(value) {
                      is CaseValidationError -> encoder.encodeSerializableValue(ValidationError.serializer(), value.value)
                      is CaseRepositoryRuleViolationError -> encoder.encodeSerializableValue(RepositoryRuleViolationError.serializer(), value.value)
                    }
                  }
                }
              }
            }
          }

          public class FileShaPath internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _fileSha: String,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo, _fileSha)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _fileSha: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/git/blobs/${this._fileSha.encodeURLParameter()}")
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  409 -> Response.Conflict(response.body())
                  422 -> Response.UnprocessableEntity(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: Blob,
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
        }

        public class Commits internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public val post: Post = Post(__client, _owner, _repo)

          public fun commitSha(commitSha: String): CommitShaPath = CommitShaPath(__client, _owner, _repo, commitSha)

          public class Post internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public suspend operator fun invoke(
              message: String,
              tree: String,
              parents: List<String>? = null,
              author: Author? = null,
              committer: Committer? = null,
              signature: String? = null,
            ): Response {
              val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/git/commits") {
                contentType(ContentType.Application.Json)
                setBody(Body(message = message, tree = tree, parents = parents, author = author, committer = committer, signature = signature))
              }
              return when (response.status.value) {
                201 -> Response.Created(response.body())
                404 -> Response.NotFound(response.body())
                409 -> Response.Conflict(response.body())
                422 -> Response.UnprocessableEntity(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            /**
             * Information about the author of the commit. By default, the `author` will be the authenticated user and the current date. See the `author` and `committer` object below for details.
             */
            @OptIn(ExperimentalSerializationApi::class)
            @Serializable
            public data class Author(
              public val name: String,
              public val email: String,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val date: Instant? = null,
            )

            /**
             * Information about the person who is making the commit. By default, `committer` will use the information set in `author`. See the `author` and `committer` object below for details.
             */
            @OptIn(ExperimentalSerializationApi::class)
            @Serializable
            public data class Committer(
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val name: String? = null,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val email: String? = null,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val date: Instant? = null,
            )

            @OptIn(ExperimentalSerializationApi::class)
            @Serializable
            internal data class Body(
              public val message: String,
              public val tree: String,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val parents: List<String>? = null,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val author: Author? = null,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val committer: Committer? = null,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val signature: String? = null,
            )

            public sealed interface Response {
              public data class Created(
                public val `value`: GitCommit,
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

          public class CommitShaPath internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _commitSha: String,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo, _commitSha)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _commitSha: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/git/commits/${this._commitSha.encodeURLParameter()}")
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  404 -> Response.NotFound(response.body())
                  409 -> Response.Conflict(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: GitCommit,
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
        }

        public class MatchingRefs internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public fun ref(ref: String): RefPath = RefPath(__client, _owner, _repo, ref)

          public class RefPath internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _ref: String,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo, _ref)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _ref: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/git/matching-refs/${this._ref.encodeURLParameter()}")
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  409 -> Response.Conflict(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: List<GitRef>,
                ) : Response

                public data class Conflict(
                  public val `value`: BasicError,
                ) : Response
              }
            }
          }
        }

        public class Ref internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public fun ref(ref: String): RefPath = RefPath(__client, _owner, _repo, ref)

          public class RefPath internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _ref: String,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo, _ref)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _ref: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/git/ref/${this._ref.encodeURLParameter()}")
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  404 -> Response.NotFound(response.body())
                  409 -> Response.Conflict(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: GitRef,
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
        }

        public class Refs internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public val post: Post = Post(__client, _owner, _repo)

          public fun ref(ref: String): RefPath = RefPath(__client, _owner, _repo, ref)

          public class Post internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public suspend operator fun invoke(ref: String, sha: String): Response {
              val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/git/refs") {
                contentType(ContentType.Application.Json)
                setBody(Body(ref = ref, sha = sha))
              }
              return when (response.status.value) {
                201 -> Response.Created(response.body())
                409 -> Response.Conflict(response.body())
                422 -> Response.UnprocessableEntity(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            @Serializable
            internal data class Body(
              public val ref: String,
              public val sha: String,
            )

            public sealed interface Response {
              public data class Created(
                public val `value`: GitRef,
              ) : Response

              public data class Conflict(
                public val `value`: BasicError,
              ) : Response

              public data class UnprocessableEntity(
                public val `value`: ValidationError,
              ) : Response
            }
          }

          public class RefPath internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _ref: String,
          ) {
            public val delete: Delete = Delete(__client, _owner, _repo, _ref)

            public val patch: Patch = Patch(__client, _owner, _repo, _ref)

            public class Delete internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _ref: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/git/refs/${this._ref.encodeURLParameter()}")
                return when (response.status.value) {
                  204 -> Response.NoContent
                  409 -> Response.Conflict(response.body())
                  422 -> Response.UnprocessableEntity
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data object NoContent : Response

                public data class Conflict(
                  public val `value`: BasicError,
                ) : Response

                public data object UnprocessableEntity : Response
              }
            }

            public class Patch internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _ref: String,
            ) {
              public suspend operator fun invoke(sha: String, force: Boolean? = null): Response {
                val response = __client.patch("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/git/refs/${this._ref.encodeURLParameter()}") {
                  contentType(ContentType.Application.Json)
                  setBody(Body(sha = sha, force = force))
                }
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  409 -> Response.Conflict(response.body())
                  422 -> Response.UnprocessableEntity(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              internal data class Body(
                public val sha: String,
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val force: Boolean? = null,
              )

              public sealed interface Response {
                public data class Ok(
                  public val `value`: GitRef,
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

        public class Tags internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public val post: Post = Post(__client, _owner, _repo)

          public fun tagSha(tagSha: String): TagShaPath = TagShaPath(__client, _owner, _repo, tagSha)

          public class Post internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public suspend operator fun invoke(
              tag: String,
              message: String,
              `object`: String,
              type: Type,
              tagger: Tagger? = null,
            ): Response {
              val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/git/tags") {
                contentType(ContentType.Application.Json)
                setBody(Body(tag = tag, message = message, `object` = `object`, type = type, tagger = tagger))
              }
              return when (response.status.value) {
                201 -> Response.Created(response.body())
                409 -> Response.Conflict(response.body())
                422 -> Response.UnprocessableEntity(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            @Serializable
            public enum class Type(
              public val `value`: String,
            ) {
              @SerialName("commit")
              Commit("commit"),
              @SerialName("tree")
              Tree("tree"),
              @SerialName("blob")
              Blob("blob"),
              ;
            }

            /**
             * An object with information about the individual creating the tag.
             */
            @OptIn(ExperimentalSerializationApi::class)
            @Serializable
            public data class Tagger(
              public val name: String,
              public val email: String,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val date: Instant? = null,
            )

            @OptIn(ExperimentalSerializationApi::class)
            @Serializable
            internal data class Body(
              public val tag: String,
              public val message: String,
              public val `object`: String,
              public val type: Type,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val tagger: Tagger? = null,
            )

            public sealed interface Response {
              public data class Created(
                public val `value`: GitTag,
              ) : Response

              public data class Conflict(
                public val `value`: BasicError,
              ) : Response

              public data class UnprocessableEntity(
                public val `value`: ValidationError,
              ) : Response
            }
          }

          public class TagShaPath internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _tagSha: String,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo, _tagSha)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _tagSha: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/git/tags/${this._tagSha.encodeURLParameter()}")
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  404 -> Response.NotFound(response.body())
                  409 -> Response.Conflict(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: GitTag,
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
        }

        public class Trees internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public val post: Post = Post(__client, _owner, _repo)

          public fun treeSha(treeSha: String): TreeShaPath = TreeShaPath(__client, _owner, _repo, treeSha)

          public class Post internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public suspend operator fun invoke(tree: List<Tree>, baseTree: String? = null): Response {
              val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/git/trees") {
                contentType(ContentType.Application.Json)
                setBody(Body(tree = tree, baseTree = baseTree))
              }
              return when (response.status.value) {
                201 -> Response.Created(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                409 -> Response.Conflict(response.body())
                422 -> Response.UnprocessableEntity(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            @OptIn(ExperimentalSerializationApi::class)
            @Serializable
            public data class Tree(
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val path: String? = null,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val mode: Mode? = null,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val type: Type? = null,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val sha: Optional<String> = Optional.Absent,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val content: String? = null,
            ) {
              @Serializable
              public enum class Mode {
                `100644`,
                `100755`,
                `040000`,
                `160000`,
                `120000`,
              }

              @Serializable
              public enum class Type(
                public val `value`: String,
              ) {
                @SerialName("blob")
                Blob("blob"),
                @SerialName("tree")
                Tree("tree"),
                @SerialName("commit")
                Commit("commit"),
                ;
              }
            }

            @OptIn(ExperimentalSerializationApi::class)
            @Serializable
            internal data class Body(
              public val tree: List<Tree>,
              @SerialName("base_tree")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val baseTree: String? = null,
            )

            public sealed interface Response {
              public data class Created(
                public val `value`: GitTree,
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

          public class TreeShaPath internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _treeSha: String,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo, _treeSha)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _treeSha: String,
            ) {
              public suspend operator fun invoke(recursive: String? = null): Response {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/git/trees/${this._treeSha.encodeURLParameter()}") {
                  if (recursive != null) {
                    this.url.encodedParameters.append("recursive", recursive.encodeURLParameter())
                  }
                }
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  404 -> Response.NotFound(response.body())
                  409 -> Response.Conflict(response.body())
                  422 -> Response.UnprocessableEntity(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: GitTree,
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

      public class Hooks internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public val `get`: Get = Get(__client, _owner, _repo)

        public val post: Post = Post(__client, _owner, _repo)

        public fun hookId(hookId: Long): HookIdPath = HookIdPath(__client, _owner, _repo, hookId)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
            val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/hooks") {
              if (perPage != null) {
                this.url.encodedParameters.append("per_page", perPage.toString())
              }
              if (page != null) {
                this.url.encodedParameters.append("page", page.toString())
              }
            }
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              404 -> Response.NotFound(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: List<Hook>,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class Post internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/hooks")
            return when (response.status.value) {
              201 -> Response.Created(response.body())
              403 -> Response.Forbidden(response.body())
              404 -> Response.NotFound(response.body())
              422 -> Response.UnprocessableEntity(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public suspend operator fun invoke(
            name: String? = null,
            config: Config? = null,
            events: List<String>? = null,
            active: Boolean? = null,
          ): Response {
            val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/hooks") {
              contentType(ContentType.Application.Json)
              setBody(Body(name = name, config = config, events = events, active = active))
            }
            return when (response.status.value) {
              201 -> Response.Created(response.body())
              403 -> Response.Forbidden(response.body())
              404 -> Response.NotFound(response.body())
              422 -> Response.UnprocessableEntity(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          /**
           * Key/value pairs to provide settings for this webhook.
           */
          @OptIn(ExperimentalSerializationApi::class)
          @Serializable
          public data class Config(
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val url: WebhookConfigUrl? = null,
            @SerialName("content_type")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val contentType: WebhookConfigContentType? = null,
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val secret: WebhookConfigSecret? = null,
            @SerialName("insecure_ssl")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val insecureSsl: WebhookConfigInsecureSsl? = null,
          )

          @OptIn(ExperimentalSerializationApi::class)
          @Serializable
          internal data class Body(
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val name: String? = null,
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val config: Config? = null,
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val events: List<String>? = null,
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val active: Boolean? = null,
          )

          public sealed interface Response {
            public data class Created(
              public val `value`: Hook,
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

        public class HookIdPath internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
          private val _hookId: Long,
        ) {
          public val delete: Delete = Delete(__client, _owner, _repo, _hookId)

          public val `get`: Get = Get(__client, _owner, _repo, _hookId)

          public val patch: Patch = Patch(__client, _owner, _repo, _hookId)

          public val config: Config = Config(__client, _owner, _repo, _hookId)

          public val deliveries: Deliveries = Deliveries(__client, _owner, _repo, _hookId)

          public val pings: Pings = Pings(__client, _owner, _repo, _hookId)

          public val tests: Tests = Tests(__client, _owner, _repo, _hookId)

          public class Delete internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _hookId: Long,
          ) {
            public suspend operator fun invoke(): Response {
              val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/hooks/${this._hookId}")
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
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _hookId: Long,
          ) {
            public suspend operator fun invoke(): Response {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/hooks/${this._hookId}")
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: Hook,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response
            }
          }

          public class Patch internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _hookId: Long,
          ) {
            public suspend operator fun invoke(
              config: WebhookConfig? = null,
              events: List<String>? = null,
              addEvents: List<String>? = null,
              removeEvents: List<String>? = null,
              active: Boolean? = null,
            ): Response {
              val response = __client.patch("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/hooks/${this._hookId}") {
                contentType(ContentType.Application.Json)
                setBody(Body(config = config, events = events, addEvents = addEvents, removeEvents = removeEvents, active = active))
              }
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                404 -> Response.NotFound(response.body())
                422 -> Response.UnprocessableEntity(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            @OptIn(ExperimentalSerializationApi::class)
            @Serializable
            internal data class Body(
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val config: WebhookConfig? = null,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val events: List<String>? = null,
              @SerialName("add_events")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val addEvents: List<String>? = null,
              @SerialName("remove_events")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val removeEvents: List<String>? = null,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val active: Boolean? = null,
            )

            public sealed interface Response {
              public data class Ok(
                public val `value`: Hook,
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
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _hookId: Long,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo, _hookId)

            public val patch: Patch = Patch(__client, _owner, _repo, _hookId)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _hookId: Long,
            ) {
              public suspend operator fun invoke(): WebhookConfig {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/hooks/${this._hookId}/config")
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }
            }

            public class Patch internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _hookId: Long,
            ) {
              public suspend operator fun invoke(): WebhookConfig {
                val response = __client.patch("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/hooks/${this._hookId}/config")
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }

              public suspend operator fun invoke(
                url: WebhookConfigUrl? = null,
                contentType: WebhookConfigContentType? = null,
                secret: WebhookConfigSecret? = null,
                insecureSsl: WebhookConfigInsecureSsl? = null,
              ): WebhookConfig {
                val response = __client.patch("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/hooks/${this._hookId}/config") {
                  contentType(ContentType.Application.Json)
                  setBody(Body(url = url, contentType = contentType, secret = secret, insecureSsl = insecureSsl))
                }
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }

              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              internal data class Body(
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val url: WebhookConfigUrl? = null,
                @SerialName("content_type")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val contentType: WebhookConfigContentType? = null,
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val secret: WebhookConfigSecret? = null,
                @SerialName("insecure_ssl")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val insecureSsl: WebhookConfigInsecureSsl? = null,
              )
            }
          }

          public class Deliveries internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _hookId: Long,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo, _hookId)

            public fun deliveryId(deliveryId: Long): DeliveryIdPath = DeliveryIdPath(__client, _owner, _repo, _hookId, deliveryId)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _hookId: Long,
            ) {
              public suspend operator fun invoke(perPage: Long? = 30L, cursor: String? = null): Response {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/hooks/${this._hookId}/deliveries") {
                  if (perPage != null) {
                    this.url.encodedParameters.append("per_page", perPage.toString())
                  }
                  if (cursor != null) {
                    this.url.encodedParameters.append("cursor", cursor.encodeURLParameter())
                  }
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
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _hookId: Long,
              private val _deliveryId: Long,
            ) {
              public val `get`: Get = Get(__client, _owner, _repo, _hookId, _deliveryId)

              public val attempts: Attempts =
                  Attempts(__client, _owner, _repo, _hookId, _deliveryId)

              public class Get internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _hookId: Long,
                private val _deliveryId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/hooks/${this._hookId}/deliveries/${this._deliveryId}")
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
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _hookId: Long,
                private val _deliveryId: Long,
              ) {
                public val post: Post = Post(__client, _owner, _repo, _hookId, _deliveryId)

                public class Post internal constructor(
                  private val __client: HttpClient,
                  private val _owner: String,
                  private val _repo: String,
                  private val _hookId: Long,
                  private val _deliveryId: Long,
                ) {
                  public suspend operator fun invoke(): Response {
                    val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/hooks/${this._hookId}/deliveries/${this._deliveryId}/attempts")
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
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _hookId: Long,
          ) {
            public val post: Post = Post(__client, _owner, _repo, _hookId)

            public class Post internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _hookId: Long,
            ) {
              public suspend operator fun invoke(): Response {
                val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/hooks/${this._hookId}/pings")
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

          public class Tests internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _hookId: Long,
          ) {
            public val post: Post = Post(__client, _owner, _repo, _hookId)

            public class Post internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _hookId: Long,
            ) {
              public suspend operator fun invoke(): Response {
                val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/hooks/${this._hookId}/tests")
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

      public class ImmutableReleases internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public val delete: Delete = Delete(__client, _owner, _repo)

        public val `get`: Get = Get(__client, _owner, _repo)

        public val put: Put = Put(__client, _owner, _repo)

        public class Delete internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/immutable-releases")
            return when (response.status.value) {
              204 -> Response.NoContent
              409 -> Response.Conflict(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data object NoContent : Response

            public data class Conflict(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/immutable-releases")
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              404 -> Response.NotFound
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: CheckImmutableReleases,
            ) : Response

            public data object NotFound : Response
          }
        }

        public class Put internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = __client.put("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/immutable-releases")
            return when (response.status.value) {
              204 -> Response.NoContent
              409 -> Response.Conflict(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data object NoContent : Response

            public data class Conflict(
              public val `value`: BasicError,
            ) : Response
          }
        }
      }

      public class Import internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        @Deprecated("Deprecated by the API provider")
        public val delete: Delete = Delete(__client, _owner, _repo)

        @Deprecated("Deprecated by the API provider")
        public val `get`: Get = Get(__client, _owner, _repo)

        @Deprecated("Deprecated by the API provider")
        public val patch: Patch = Patch(__client, _owner, _repo)

        @Deprecated("Deprecated by the API provider")
        public val put: Put = Put(__client, _owner, _repo)

        public val authors: Authors = Authors(__client, _owner, _repo)

        public val largeFiles: LargeFiles = LargeFiles(__client, _owner, _repo)

        public val lfs: Lfs = Lfs(__client, _owner, _repo)

        @Deprecated("Deprecated by the API provider")
        public class Delete internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          @Deprecated("Deprecated by the API provider")
          public suspend operator fun invoke(): Response {
            val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/import")
            return when (response.status.value) {
              204 -> Response.NoContent
              503 -> Response.ServiceUnavailable(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data object NoContent : Response

            public data class ServiceUnavailable(
              public val `value`: BasicError,
            ) : Response
          }
        }

        @Deprecated("Deprecated by the API provider")
        public class Get internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          @Deprecated("Deprecated by the API provider")
          public suspend operator fun invoke(): Response {
            val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/import")
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              404 -> Response.NotFound(response.body())
              503 -> Response.ServiceUnavailable(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: io.github.model.Import,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response

            public data class ServiceUnavailable(
              public val `value`: BasicError,
            ) : Response
          }
        }

        @Deprecated("Deprecated by the API provider")
        public class Patch internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          @Deprecated("Deprecated by the API provider")
          public suspend operator fun invoke(): Response {
            val response = __client.patch("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/import")
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              503 -> Response.ServiceUnavailable(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          @Deprecated("Deprecated by the API provider")
          public suspend operator fun invoke(
            vcsUsername: String? = null,
            vcsPassword: String? = null,
            vcs: Vcs? = null,
            tfvcProject: String? = null,
          ): Response {
            val response = __client.patch("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/import") {
              contentType(ContentType.Application.Json)
              setBody(Body(vcsUsername = vcsUsername, vcsPassword = vcsPassword, vcs = vcs, tfvcProject = tfvcProject))
            }
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              503 -> Response.ServiceUnavailable(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          @Serializable
          public enum class Vcs(
            public val `value`: String,
          ) {
            @SerialName("subversion")
            Subversion("subversion"),
            @SerialName("tfvc")
            Tfvc("tfvc"),
            @SerialName("git")
            Git("git"),
            @SerialName("mercurial")
            Mercurial("mercurial"),
            ;
          }

          @OptIn(ExperimentalSerializationApi::class)
          @Serializable
          internal data class Body(
            @SerialName("vcs_username")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val vcsUsername: String? = null,
            @SerialName("vcs_password")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val vcsPassword: String? = null,
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val vcs: Vcs? = null,
            @SerialName("tfvc_project")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val tfvcProject: String? = null,
          )

          public sealed interface Response {
            public data class Ok(
              public val `value`: io.github.model.Import,
            ) : Response

            public data class ServiceUnavailable(
              public val `value`: BasicError,
            ) : Response
          }
        }

        @Deprecated("Deprecated by the API provider")
        public class Put internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          @Deprecated("Deprecated by the API provider")
          public suspend operator fun invoke(
            vcsUrl: String,
            vcs: Vcs? = null,
            vcsUsername: String? = null,
            vcsPassword: String? = null,
            tfvcProject: String? = null,
          ): Response {
            val response = __client.put("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/import") {
              contentType(ContentType.Application.Json)
              setBody(Body(vcsUrl = vcsUrl, vcs = vcs, vcsUsername = vcsUsername, vcsPassword = vcsPassword, tfvcProject = tfvcProject))
            }
            return when (response.status.value) {
              201 -> Response.Created(response.body())
              404 -> Response.NotFound(response.body())
              422 -> Response.UnprocessableEntity(response.body())
              503 -> Response.ServiceUnavailable(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          @Serializable
          public enum class Vcs(
            public val `value`: String,
          ) {
            @SerialName("subversion")
            Subversion("subversion"),
            @SerialName("git")
            Git("git"),
            @SerialName("mercurial")
            Mercurial("mercurial"),
            @SerialName("tfvc")
            Tfvc("tfvc"),
            ;
          }

          @OptIn(ExperimentalSerializationApi::class)
          @Serializable
          internal data class Body(
            @SerialName("vcs_url")
            public val vcsUrl: String,
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val vcs: Vcs? = null,
            @SerialName("vcs_username")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val vcsUsername: String? = null,
            @SerialName("vcs_password")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val vcsPassword: String? = null,
            @SerialName("tfvc_project")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val tfvcProject: String? = null,
          )

          public sealed interface Response {
            public data class Created(
              public val `value`: io.github.model.Import,
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

        public class Authors internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          @Deprecated("Deprecated by the API provider")
          public val `get`: Get = Get(__client, _owner, _repo)

          public fun authorId(authorId: Long): AuthorIdPath = AuthorIdPath(__client, _owner, _repo, authorId)

          @Deprecated("Deprecated by the API provider")
          public class Get internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            @Deprecated("Deprecated by the API provider")
            public suspend operator fun invoke(since: Long? = null): Response {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/import/authors") {
                if (since != null) {
                  this.url.encodedParameters.append("since", since.toString())
                }
              }
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                404 -> Response.NotFound(response.body())
                503 -> Response.ServiceUnavailable(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: List<PorterAuthor>,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response

              public data class ServiceUnavailable(
                public val `value`: BasicError,
              ) : Response
            }
          }

          public class AuthorIdPath internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _authorId: Long,
          ) {
            @Deprecated("Deprecated by the API provider")
            public val patch: Patch = Patch(__client, _owner, _repo, _authorId)

            @Deprecated("Deprecated by the API provider")
            public class Patch internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _authorId: Long,
            ) {
              @Deprecated("Deprecated by the API provider")
              public suspend operator fun invoke(): Response {
                val response = __client.patch("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/import/authors/${this._authorId}")
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  404 -> Response.NotFound(response.body())
                  422 -> Response.UnprocessableEntity(response.body())
                  503 -> Response.ServiceUnavailable(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              @Deprecated("Deprecated by the API provider")
              public suspend operator fun invoke(email: String? = null, name: String? = null): Response {
                val response = __client.patch("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/import/authors/${this._authorId}") {
                  contentType(ContentType.Application.Json)
                  setBody(Body(email = email, name = name))
                }
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  404 -> Response.NotFound(response.body())
                  422 -> Response.UnprocessableEntity(response.body())
                  503 -> Response.ServiceUnavailable(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              internal data class Body(
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val email: String? = null,
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val name: String? = null,
              )

              public sealed interface Response {
                public data class Ok(
                  public val `value`: PorterAuthor,
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
          }
        }

        public class LargeFiles internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          @Deprecated("Deprecated by the API provider")
          public val `get`: Get = Get(__client, _owner, _repo)

          @Deprecated("Deprecated by the API provider")
          public class Get internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            @Deprecated("Deprecated by the API provider")
            public suspend operator fun invoke(): Response {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/import/large_files")
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                503 -> Response.ServiceUnavailable(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: List<PorterLargeFile>,
              ) : Response

              public data class ServiceUnavailable(
                public val `value`: BasicError,
              ) : Response
            }
          }
        }

        public class Lfs internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          @Deprecated("Deprecated by the API provider")
          public val patch: Patch = Patch(__client, _owner, _repo)

          @Deprecated("Deprecated by the API provider")
          public class Patch internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            @Deprecated("Deprecated by the API provider")
            public suspend operator fun invoke(useLfs: UseLfs): Response {
              val response = __client.patch("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/import/lfs") {
                contentType(ContentType.Application.Json)
                setBody(Body(useLfs = useLfs))
              }
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                422 -> Response.UnprocessableEntity(response.body())
                503 -> Response.ServiceUnavailable(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            @Serializable
            public enum class UseLfs(
              public val `value`: String,
            ) {
              @SerialName("opt_in")
              OptIn("opt_in"),
              @SerialName("opt_out")
              OptOut("opt_out"),
              ;
            }

            @Serializable
            internal data class Body(
              @SerialName("use_lfs")
              public val useLfs: UseLfs,
            )

            public sealed interface Response {
              public data class Ok(
                public val `value`: io.github.model.Import,
              ) : Response

              public data class UnprocessableEntity(
                public val `value`: ValidationError,
              ) : Response

              public data class ServiceUnavailable(
                public val `value`: BasicError,
              ) : Response
            }
          }
        }
      }

      public class Installation internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public val `get`: Get = Get(__client, _owner, _repo)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/installation")
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              301 -> Response.MovedPermanently(response.body())
              404 -> Response.NotFound(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: io.github.model.Installation,
            ) : Response

            public data class MovedPermanently(
              public val `value`: BasicError,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response
          }
        }
      }

      public class InteractionLimits internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public val delete: Delete = Delete(__client, _owner, _repo)

        public val `get`: Get = Get(__client, _owner, _repo)

        public val put: Put = Put(__client, _owner, _repo)

        public class Delete internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/interaction-limits")
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

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/interaction-limits")
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }

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
                  buildSerialDescriptor("io.github.api.Repos.OwnerPath.RepoPath.InteractionLimits.Get.Response", PolymorphicKind.SEALED) {
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
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(body: InteractionLimit): Response {
            val response = __client.put("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/interaction-limits") {
              contentType(ContentType.Application.Json)
              setBody(body)
            }
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              409 -> Response.Conflict
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: InteractionLimitResponse,
            ) : Response

            public data object Conflict : Response
          }
        }
      }

      public class Invitations internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public val `get`: Get = Get(__client, _owner, _repo)

        public fun invitationId(invitationId: Long): InvitationIdPath = InvitationIdPath(__client, _owner, _repo, invitationId)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<RepositoryInvitation> {
            val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/invitations") {
              if (perPage != null) {
                this.url.encodedParameters.append("per_page", perPage.toString())
              }
              if (page != null) {
                this.url.encodedParameters.append("page", page.toString())
              }
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class InvitationIdPath internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
          private val _invitationId: Long,
        ) {
          public val delete: Delete = Delete(__client, _owner, _repo, _invitationId)

          public val patch: Patch = Patch(__client, _owner, _repo, _invitationId)

          public class Delete internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _invitationId: Long,
          ) {
            public suspend operator fun invoke() {
              val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/invitations/${this._invitationId}")
              if (response.status.value != 204) throw ResponseException(response, "")
            }
          }

          public class Patch internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _invitationId: Long,
          ) {
            public suspend operator fun invoke(): RepositoryInvitation {
              val response = __client.patch("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/invitations/${this._invitationId}")
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }

            public suspend operator fun invoke(permissions: Permissions? = null): RepositoryInvitation {
              val response = __client.patch("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/invitations/${this._invitationId}") {
                contentType(ContentType.Application.Json)
                setBody(Body(permissions = permissions))
              }
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }

            @Serializable
            public enum class Permissions(
              public val `value`: String,
            ) {
              @SerialName("read")
              Read("read"),
              @SerialName("write")
              Write("write"),
              @SerialName("maintain")
              Maintain("maintain"),
              @SerialName("triage")
              Triage("triage"),
              @SerialName("admin")
              Admin("admin"),
              ;
            }

            @OptIn(ExperimentalSerializationApi::class)
            @Serializable
            internal data class Body(
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val permissions: Permissions? = null,
            )
          }
        }
      }

      public class Issues internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public val `get`: Get = Get(__client, _owner, _repo)

        public val post: Post = Post(__client, _owner, _repo)

        public val comments: Comments = Comments(__client, _owner, _repo)

        public val events: Events = Events(__client, _owner, _repo)

        public fun issueNumber(issueNumber: Long): IssueNumberPath = IssueNumberPath(__client, _owner, _repo, issueNumber)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(
            milestone: String? = null,
            state: State? = State.Open,
            assignee: String? = null,
            type: String? = null,
            creator: String? = null,
            mentioned: String? = null,
            labels: String? = null,
            sort: Sort? = Sort.Created,
            direction: Direction? = Direction.Desc,
            since: Instant? = null,
            perPage: Long? = 30L,
            page: Long? = 1L,
          ): Response {
            val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/issues") {
              if (milestone != null) {
                this.url.encodedParameters.append("milestone", milestone.encodeURLParameter())
              }
              if (state != null) {
                this.url.encodedParameters.append("state", state.value.encodeURLParameter())
              }
              if (assignee != null) {
                this.url.encodedParameters.append("assignee", assignee.encodeURLParameter())
              }
              if (type != null) {
                this.url.encodedParameters.append("type", type.encodeURLParameter())
              }
              if (creator != null) {
                this.url.encodedParameters.append("creator", creator.encodeURLParameter())
              }
              if (mentioned != null) {
                this.url.encodedParameters.append("mentioned", mentioned.encodeURLParameter())
              }
              if (labels != null) {
                this.url.encodedParameters.append("labels", labels.encodeURLParameter())
              }
              if (sort != null) {
                this.url.encodedParameters.append("sort", sort.value.encodeURLParameter())
              }
              if (direction != null) {
                this.url.encodedParameters.append("direction", direction.value.encodeURLParameter())
              }
              if (since != null) {
                this.url.encodedParameters.append("since", since.toString().encodeURLParameter())
              }
              if (perPage != null) {
                this.url.encodedParameters.append("per_page", perPage.toString())
              }
              if (page != null) {
                this.url.encodedParameters.append("page", page.toString())
              }
            }
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              301 -> Response.MovedPermanently(response.body())
              404 -> Response.NotFound(response.body())
              422 -> Response.UnprocessableEntity(response.body())
              else -> throw ResponseException(response, "")
            }
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

            public data class MovedPermanently(
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

        public class Post internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(
            title: String,
            body: String? = null,
            assignee: Optional<String> = Optional.Absent,
            milestone: Optional<Milestone> = Optional.Absent,
            labels: List<Labels>? = null,
            assignees: List<String>? = null,
            type: Optional<String> = Optional.Absent,
          ): Response {
            val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/issues") {
              contentType(ContentType.Application.Json)
              setBody(Body(title = Body.Title.CaseString(title), body = body, assignee = assignee, milestone = milestone, labels = labels, assignees = assignees, type = type))
            }
            return when (response.status.value) {
              201 -> Response.Created(response.body())
              400 -> Response.BadRequest(response.body())
              403 -> Response.Forbidden(response.body())
              404 -> Response.NotFound(response.body())
              410 -> Response.Gone(response.body())
              422 -> Response.UnprocessableEntity(response.body())
              503 -> response.body<Response.ServiceUnavailable>()
              else -> throw ResponseException(response, "")
            }
          }

          public suspend operator fun invoke(
            title: Long,
            body: String? = null,
            assignee: Optional<String> = Optional.Absent,
            milestone: Optional<Milestone> = Optional.Absent,
            labels: List<Labels>? = null,
            assignees: List<String>? = null,
            type: Optional<String> = Optional.Absent,
          ): Response {
            val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/issues") {
              contentType(ContentType.Application.Json)
              setBody(Body(title = Body.Title.CaseLong(title), body = body, assignee = assignee, milestone = milestone, labels = labels, assignees = assignees, type = type))
            }
            return when (response.status.value) {
              201 -> Response.Created(response.body())
              400 -> Response.BadRequest(response.body())
              403 -> Response.Forbidden(response.body())
              404 -> Response.NotFound(response.body())
              410 -> Response.Gone(response.body())
              422 -> Response.UnprocessableEntity(response.body())
              503 -> response.body<Response.ServiceUnavailable>()
              else -> throw ResponseException(response, "")
            }
          }

          @Serializable(with = Milestone.Serializer::class)
          public sealed interface Milestone {
            @Serializable
            @JvmInline
            public value class CaseString(
              public val `value`: String,
            ) : Milestone

            @Serializable
            @JvmInline
            public value class CaseLong(
              public val `value`: Long,
            ) : Milestone

            public object Serializer : KSerializer<Milestone> {
              @OptIn(
                InternalSerializationApi::class,
                ExperimentalSerializationApi::class,
              )
              override val descriptor: SerialDescriptor =
                  buildSerialDescriptor("io.github.api.Repos.OwnerPath.RepoPath.Issues.Post.Milestone", PolymorphicKind.SEALED) {
                element("CaseString", String.serializer().descriptor)
                element("CaseLong", Long.serializer().descriptor)
              }

              override fun deserialize(decoder: Decoder): Milestone {
                val value = decoder.decodeSerializableValue(JsonElement.serializer())
                val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                return when {
                  value is JsonPrimitive && value.isString -> json.attemptDeserialize(
                    value,
                    CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                  )
                  value is JsonPrimitive && !value.isString && value.doubleOrNull != null -> json.attemptDeserialize(
                    value,
                    CaseLong::class to { CaseLong(decodeFromJsonElement(Long.serializer(), it)) },
                  )
                  else -> throw UnionSerializationException(value, emptyMap())
                }
              }

              override fun serialize(encoder: Encoder, `value`: Milestone) {
                when(value) {
                  is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                  is CaseLong -> encoder.encodeSerializableValue(Long.serializer(), value.value)
                }
              }
            }
          }

          @Serializable(with = Labels.Serializer::class)
          public sealed interface Labels {
            @Serializable
            @JvmInline
            public value class CaseString(
              public val `value`: String,
            ) : Labels

            @OptIn(ExperimentalSerializationApi::class)
            @Serializable
            public data class IdAndNameAndDescriptionAndColor(
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val id: Long? = null,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val name: String? = null,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val description: Optional<String> = Optional.Absent,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val color: Optional<String> = Optional.Absent,
            ) : Labels

            public object Serializer : KSerializer<Labels> {
              @OptIn(
                InternalSerializationApi::class,
                ExperimentalSerializationApi::class,
              )
              override val descriptor: SerialDescriptor =
                  buildSerialDescriptor("io.github.api.Repos.OwnerPath.RepoPath.Issues.Post.Labels", PolymorphicKind.SEALED) {
                element("CaseString", String.serializer().descriptor)
                element("IdAndNameAndDescriptionAndColor", IdAndNameAndDescriptionAndColor.serializer().descriptor)
              }

              override fun deserialize(decoder: Decoder): Labels {
                val value = decoder.decodeSerializableValue(JsonElement.serializer())
                val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                return when {
                  value is JsonPrimitive && value.isString -> json.attemptDeserialize(
                    value,
                    CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                  )
                  value is JsonObject -> json.attemptDeserialize(
                    value,
                    IdAndNameAndDescriptionAndColor::class to { decodeFromJsonElement(IdAndNameAndDescriptionAndColor.serializer(), it) },
                  )
                  else -> throw UnionSerializationException(value, emptyMap())
                }
              }

              override fun serialize(encoder: Encoder, `value`: Labels) {
                when(value) {
                  is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                  is IdAndNameAndDescriptionAndColor -> encoder.encodeSerializableValue(IdAndNameAndDescriptionAndColor.serializer(), value)
                }
              }
            }
          }

          @OptIn(ExperimentalSerializationApi::class)
          @Serializable
          internal data class Body(
            public val title: Title,
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val body: String? = null,
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val assignee: Optional<String> = Optional.Absent,
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val milestone: Optional<Milestone> = Optional.Absent,
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val labels: List<Labels>? = null,
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val assignees: List<String>? = null,
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val type: Optional<String> = Optional.Absent,
          ) {
            /**
             * The title of the issue.
             */
            @Serializable(with = Title.Serializer::class)
            public sealed interface Title {
              @Serializable
              @JvmInline
              public value class CaseString(
                public val `value`: String,
              ) : Title

              @Serializable
              @JvmInline
              public value class CaseLong(
                public val `value`: Long,
              ) : Title

              public object Serializer : KSerializer<Title> {
                @OptIn(
                  InternalSerializationApi::class,
                  ExperimentalSerializationApi::class,
                )
                override val descriptor: SerialDescriptor =
                    buildSerialDescriptor("io.github.api.Repos.OwnerPath.RepoPath.Issues.Post.Body.Title", PolymorphicKind.SEALED) {
                  element("CaseString", String.serializer().descriptor)
                  element("CaseLong", Long.serializer().descriptor)
                }

                override fun deserialize(decoder: Decoder): Title {
                  val value = decoder.decodeSerializableValue(JsonElement.serializer())
                  val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                  return when {
                    value is JsonPrimitive && value.isString -> json.attemptDeserialize(
                      value,
                      CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                    )
                    value is JsonPrimitive && !value.isString && value.doubleOrNull != null -> json.attemptDeserialize(
                      value,
                      CaseLong::class to { CaseLong(decodeFromJsonElement(Long.serializer(), it)) },
                    )
                    else -> throw UnionSerializationException(value, emptyMap())
                  }
                }

                override fun serialize(encoder: Encoder, `value`: Title) {
                  when(value) {
                    is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                    is CaseLong -> encoder.encodeSerializableValue(Long.serializer(), value.value)
                  }
                }
              }
            }
          }

          public sealed interface Response {
            public data class Created(
              public val `value`: Issue,
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

            public data class Gone(
              public val `value`: BasicError,
            ) : Response

            public data class UnprocessableEntity(
              public val `value`: ValidationError,
            ) : Response

            @OptIn(ExperimentalSerializationApi::class)
            @Serializable
            public data class ServiceUnavailable(
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val code: String? = null,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val message: String? = null,
              @SerialName("documentation_url")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val documentationUrl: String? = null,
            ) : Response
          }
        }

        public class Comments internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public val `get`: Get = Get(__client, _owner, _repo)

          public fun commentId(commentId: Long): CommentIdPath = CommentIdPath(__client, _owner, _repo, commentId)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public suspend operator fun invoke(
              sort: Sort? = Sort.Created,
              direction: Direction? = null,
              since: Instant? = null,
              perPage: Long? = 30L,
              page: Long? = 1L,
            ): Response {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/issues/comments") {
                if (sort != null) {
                  this.url.encodedParameters.append("sort", sort.value.encodeURLParameter())
                }
                if (direction != null) {
                  this.url.encodedParameters.append("direction", direction.value.encodeURLParameter())
                }
                if (since != null) {
                  this.url.encodedParameters.append("since", since.toString().encodeURLParameter())
                }
                if (perPage != null) {
                  this.url.encodedParameters.append("per_page", perPage.toString())
                }
                if (page != null) {
                  this.url.encodedParameters.append("page", page.toString())
                }
              }
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                404 -> Response.NotFound(response.body())
                422 -> Response.UnprocessableEntity(response.body())
                else -> throw ResponseException(response, "")
              }
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
                public val `value`: List<IssueComment>,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response

              public data class UnprocessableEntity(
                public val `value`: ValidationError,
              ) : Response
            }
          }

          public class CommentIdPath internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _commentId: Long,
          ) {
            public val delete: Delete = Delete(__client, _owner, _repo, _commentId)

            public val `get`: Get = Get(__client, _owner, _repo, _commentId)

            public val patch: Patch = Patch(__client, _owner, _repo, _commentId)

            public val pin: Pin = Pin(__client, _owner, _repo, _commentId)

            public val reactions: Reactions = Reactions(__client, _owner, _repo, _commentId)

            public class Delete internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _commentId: Long,
            ) {
              public suspend operator fun invoke() {
                val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/issues/comments/${this._commentId}")
                if (response.status.value != 204) throw ResponseException(response, "")
              }
            }

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _commentId: Long,
            ) {
              public suspend operator fun invoke(): Response {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/issues/comments/${this._commentId}")
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  404 -> Response.NotFound(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: IssueComment,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response
              }
            }

            public class Patch internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _commentId: Long,
            ) {
              public suspend operator fun invoke(body: String): Response {
                val response = __client.patch("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/issues/comments/${this._commentId}") {
                  contentType(ContentType.Application.Json)
                  setBody(Body(body = body))
                }
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  422 -> Response.UnprocessableEntity(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              @Serializable
              internal data class Body(
                public val body: String,
              )

              public sealed interface Response {
                public data class Ok(
                  public val `value`: IssueComment,
                ) : Response

                public data class UnprocessableEntity(
                  public val `value`: ValidationError,
                ) : Response
              }
            }

            public class Pin internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _commentId: Long,
            ) {
              public val delete: Delete = Delete(__client, _owner, _repo, _commentId)

              public val put: Put = Put(__client, _owner, _repo, _commentId)

              public class Delete internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _commentId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/issues/comments/${this._commentId}/pin")
                  return when (response.status.value) {
                    204 -> Response.NoContent
                    401 -> Response.Unauthorized(response.body())
                    403 -> Response.Forbidden(response.body())
                    404 -> Response.NotFound(response.body())
                    410 -> Response.Gone(response.body())
                    503 -> response.body<Response.ServiceUnavailable>()
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

                  public data class Gone(
                    public val `value`: BasicError,
                  ) : Response

                  @OptIn(ExperimentalSerializationApi::class)
                  @Serializable
                  public data class ServiceUnavailable(
                    @EncodeDefault(EncodeDefault.Mode.NEVER)
                    public val code: String? = null,
                    @EncodeDefault(EncodeDefault.Mode.NEVER)
                    public val message: String? = null,
                    @SerialName("documentation_url")
                    @EncodeDefault(EncodeDefault.Mode.NEVER)
                    public val documentationUrl: String? = null,
                  ) : Response
                }
              }

              public class Put internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _commentId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = __client.put("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/issues/comments/${this._commentId}/pin")
                  return when (response.status.value) {
                    200 -> Response.Ok(response.body())
                    401 -> Response.Unauthorized(response.body())
                    403 -> Response.Forbidden(response.body())
                    404 -> Response.NotFound(response.body())
                    410 -> Response.Gone(response.body())
                    422 -> Response.UnprocessableEntity(response.body())
                    else -> throw ResponseException(response, "")
                  }
                }

                public sealed interface Response {
                  public data class Ok(
                    public val `value`: IssueComment,
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

                  public data class Gone(
                    public val `value`: BasicError,
                  ) : Response

                  public data class UnprocessableEntity(
                    public val `value`: ValidationError,
                  ) : Response
                }
              }
            }

            public class Reactions internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _commentId: Long,
            ) {
              public val `get`: Get = Get(__client, _owner, _repo, _commentId)

              public val post: Post = Post(__client, _owner, _repo, _commentId)

              public fun reactionId(reactionId: Long): ReactionIdPath = ReactionIdPath(__client, _owner, _repo, _commentId, reactionId)

              public class Get internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _commentId: Long,
              ) {
                public suspend operator fun invoke(
                  content: Content? = null,
                  perPage: Long? = 30L,
                  page: Long? = 1L,
                ): Response {
                  val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/issues/comments/${this._commentId}/reactions") {
                    if (content != null) {
                      this.url.encodedParameters.append("content", content.value.encodeURLParameter())
                    }
                    if (perPage != null) {
                      this.url.encodedParameters.append("per_page", perPage.toString())
                    }
                    if (page != null) {
                      this.url.encodedParameters.append("page", page.toString())
                    }
                  }
                  return when (response.status.value) {
                    200 -> Response.Ok(response.body())
                    404 -> Response.NotFound(response.body())
                    else -> throw ResponseException(response, "")
                  }
                }

                @Serializable
                public enum class Content(
                  public val `value`: String,
                ) {
                  `+1`("+1"),
                  `-1`("-1"),
                  @SerialName("laugh")
                  Laugh("laugh"),
                  @SerialName("confused")
                  Confused("confused"),
                  @SerialName("heart")
                  Heart("heart"),
                  @SerialName("hooray")
                  Hooray("hooray"),
                  @SerialName("rocket")
                  Rocket("rocket"),
                  @SerialName("eyes")
                  Eyes("eyes"),
                  ;
                }

                public sealed interface Response {
                  public data class Ok(
                    public val `value`: List<Reaction>,
                  ) : Response

                  public data class NotFound(
                    public val `value`: BasicError,
                  ) : Response
                }
              }

              public class Post internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _commentId: Long,
              ) {
                public suspend operator fun invoke(content: Content): Response {
                  val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/issues/comments/${this._commentId}/reactions") {
                    contentType(ContentType.Application.Json)
                    setBody(Body(content = content))
                  }
                  return when (response.status.value) {
                    200 -> Response.Ok(response.body())
                    201 -> Response.Created(response.body())
                    422 -> Response.UnprocessableEntity(response.body())
                    else -> throw ResponseException(response, "")
                  }
                }

                @Serializable
                public enum class Content(
                  public val `value`: String,
                ) {
                  `+1`("+1"),
                  `-1`("-1"),
                  @SerialName("laugh")
                  Laugh("laugh"),
                  @SerialName("confused")
                  Confused("confused"),
                  @SerialName("heart")
                  Heart("heart"),
                  @SerialName("hooray")
                  Hooray("hooray"),
                  @SerialName("rocket")
                  Rocket("rocket"),
                  @SerialName("eyes")
                  Eyes("eyes"),
                  ;
                }

                @Serializable
                internal data class Body(
                  public val content: Content,
                )

                public sealed interface Response {
                  public data class Ok(
                    public val `value`: Reaction,
                  ) : Response

                  public data class Created(
                    public val `value`: Reaction,
                  ) : Response

                  public data class UnprocessableEntity(
                    public val `value`: ValidationError,
                  ) : Response
                }
              }

              public class ReactionIdPath internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _commentId: Long,
                private val _reactionId: Long,
              ) {
                public val delete: Delete = Delete(__client, _owner, _repo, _commentId, _reactionId)

                public class Delete internal constructor(
                  private val __client: HttpClient,
                  private val _owner: String,
                  private val _repo: String,
                  private val _commentId: Long,
                  private val _reactionId: Long,
                ) {
                  public suspend operator fun invoke() {
                    val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/issues/comments/${this._commentId}/reactions/${this._reactionId}")
                    if (response.status.value != 204) throw ResponseException(response, "")
                  }
                }
              }
            }
          }
        }

        public class Events internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public val `get`: Get = Get(__client, _owner, _repo)

          public fun eventId(eventId: Long): EventIdPath = EventIdPath(__client, _owner, _repo, eventId)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/issues/events") {
                if (perPage != null) {
                  this.url.encodedParameters.append("per_page", perPage.toString())
                }
                if (page != null) {
                  this.url.encodedParameters.append("page", page.toString())
                }
              }
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                422 -> Response.UnprocessableEntity(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: List<IssueEvent>,
              ) : Response

              public data class UnprocessableEntity(
                public val `value`: ValidationError,
              ) : Response
            }
          }

          public class EventIdPath internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _eventId: Long,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo, _eventId)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _eventId: Long,
            ) {
              public suspend operator fun invoke(): Response {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/issues/events/${this._eventId}")
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  410 -> Response.Gone(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: IssueEvent,
                ) : Response

                public data class Forbidden(
                  public val `value`: BasicError,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response

                public data class Gone(
                  public val `value`: BasicError,
                ) : Response
              }
            }
          }
        }

        public class IssueNumberPath internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
          private val _issueNumber: Long,
        ) {
          public val `get`: Get = Get(__client, _owner, _repo, _issueNumber)

          public val patch: Patch = Patch(__client, _owner, _repo, _issueNumber)

          public val assignees: Assignees = Assignees(__client, _owner, _repo, _issueNumber)

          public val comments: Comments = Comments(__client, _owner, _repo, _issueNumber)

          public val dependencies: Dependencies =
              Dependencies(__client, _owner, _repo, _issueNumber)

          public val events: Events = Events(__client, _owner, _repo, _issueNumber)

          public val issueFieldValues: IssueFieldValues =
              IssueFieldValues(__client, _owner, _repo, _issueNumber)

          public val labels: Labels = Labels(__client, _owner, _repo, _issueNumber)

          public val lock: Lock = Lock(__client, _owner, _repo, _issueNumber)

          public val parent: Parent = Parent(__client, _owner, _repo, _issueNumber)

          public val reactions: Reactions = Reactions(__client, _owner, _repo, _issueNumber)

          public val subIssue: SubIssue = SubIssue(__client, _owner, _repo, _issueNumber)

          public val subIssues: SubIssues = SubIssues(__client, _owner, _repo, _issueNumber)

          public val timeline: Timeline = Timeline(__client, _owner, _repo, _issueNumber)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _issueNumber: Long,
          ) {
            public suspend operator fun invoke(): Response {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/issues/${this._issueNumber}")
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                301 -> Response.MovedPermanently(response.body())
                304 -> Response.NotModified
                404 -> Response.NotFound(response.body())
                410 -> Response.Gone(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: Issue,
              ) : Response

              public data class MovedPermanently(
                public val `value`: BasicError,
              ) : Response

              public data object NotModified : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response

              public data class Gone(
                public val `value`: BasicError,
              ) : Response
            }
          }

          public class Patch internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _issueNumber: Long,
          ) {
            public suspend operator fun invoke(): Response {
              val response = __client.patch("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/issues/${this._issueNumber}")
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                301 -> Response.MovedPermanently(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                410 -> Response.Gone(response.body())
                422 -> Response.UnprocessableEntity(response.body())
                503 -> response.body<Response.ServiceUnavailable>()
                else -> throw ResponseException(response, "")
              }
            }

            public suspend operator fun invoke(
              title: Optional<Title> = Optional.Absent,
              body: Optional<String> = Optional.Absent,
              assignee: Optional<String> = Optional.Absent,
              state: State? = null,
              stateReason: Optional<StateReason> = Optional.Absent,
              milestone: Optional<Milestone> = Optional.Absent,
              labels: List<Labels>? = null,
              assignees: List<String>? = null,
              issueFieldValues: List<IssueFieldValues>? = null,
              type: Optional<String> = Optional.Absent,
            ): Response {
              val response = __client.patch("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/issues/${this._issueNumber}") {
                contentType(ContentType.Application.Json)
                setBody(Body(title = title, body = body, assignee = assignee, state = state, stateReason = stateReason, milestone = milestone, labels = labels, assignees = assignees, issueFieldValues = issueFieldValues, type = type))
              }
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                301 -> Response.MovedPermanently(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                410 -> Response.Gone(response.body())
                422 -> Response.UnprocessableEntity(response.body())
                503 -> response.body<Response.ServiceUnavailable>()
                else -> throw ResponseException(response, "")
              }
            }

            /**
             * The title of the issue.
             */
            @Serializable(with = Title.Serializer::class)
            public sealed interface Title {
              @Serializable
              @JvmInline
              public value class CaseString(
                public val `value`: String,
              ) : Title

              @Serializable
              @JvmInline
              public value class CaseLong(
                public val `value`: Long,
              ) : Title

              public object Serializer : KSerializer<Title> {
                @OptIn(
                  InternalSerializationApi::class,
                  ExperimentalSerializationApi::class,
                )
                override val descriptor: SerialDescriptor =
                    buildSerialDescriptor("io.github.api.Repos.OwnerPath.RepoPath.Issues.IssueNumberPath.Patch.Title", PolymorphicKind.SEALED) {
                  element("CaseString", String.serializer().descriptor)
                  element("CaseLong", Long.serializer().descriptor)
                }

                override fun deserialize(decoder: Decoder): Title {
                  val value = decoder.decodeSerializableValue(JsonElement.serializer())
                  val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                  return when {
                    value is JsonPrimitive && value.isString -> json.attemptDeserialize(
                      value,
                      CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                    )
                    value is JsonPrimitive && !value.isString && value.doubleOrNull != null -> json.attemptDeserialize(
                      value,
                      CaseLong::class to { CaseLong(decodeFromJsonElement(Long.serializer(), it)) },
                    )
                    else -> throw UnionSerializationException(value, emptyMap())
                  }
                }

                override fun serialize(encoder: Encoder, `value`: Title) {
                  when(value) {
                    is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                    is CaseLong -> encoder.encodeSerializableValue(Long.serializer(), value.value)
                  }
                }
              }
            }

            @Serializable
            public enum class State(
              public val `value`: String,
            ) {
              @SerialName("open")
              Open("open"),
              @SerialName("closed")
              Closed("closed"),
              ;
            }

            @Serializable
            public enum class StateReason(
              public val `value`: String,
            ) {
              @SerialName("completed")
              Completed("completed"),
              @SerialName("not_planned")
              NotPlanned("not_planned"),
              @SerialName("duplicate")
              Duplicate("duplicate"),
              @SerialName("reopened")
              Reopened("reopened"),
              ;
            }

            @Serializable(with = Milestone.Serializer::class)
            public sealed interface Milestone {
              @Serializable
              @JvmInline
              public value class CaseString(
                public val `value`: String,
              ) : Milestone

              @Serializable
              @JvmInline
              public value class CaseLong(
                public val `value`: Long,
              ) : Milestone

              public object Serializer : KSerializer<Milestone> {
                @OptIn(
                  InternalSerializationApi::class,
                  ExperimentalSerializationApi::class,
                )
                override val descriptor: SerialDescriptor =
                    buildSerialDescriptor("io.github.api.Repos.OwnerPath.RepoPath.Issues.IssueNumberPath.Patch.Milestone", PolymorphicKind.SEALED) {
                  element("CaseString", String.serializer().descriptor)
                  element("CaseLong", Long.serializer().descriptor)
                }

                override fun deserialize(decoder: Decoder): Milestone {
                  val value = decoder.decodeSerializableValue(JsonElement.serializer())
                  val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                  return when {
                    value is JsonPrimitive && value.isString -> json.attemptDeserialize(
                      value,
                      CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                    )
                    value is JsonPrimitive && !value.isString && value.doubleOrNull != null -> json.attemptDeserialize(
                      value,
                      CaseLong::class to { CaseLong(decodeFromJsonElement(Long.serializer(), it)) },
                    )
                    else -> throw UnionSerializationException(value, emptyMap())
                  }
                }

                override fun serialize(encoder: Encoder, `value`: Milestone) {
                  when(value) {
                    is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                    is CaseLong -> encoder.encodeSerializableValue(Long.serializer(), value.value)
                  }
                }
              }
            }

            @Serializable(with = Labels.Serializer::class)
            public sealed interface Labels {
              @Serializable
              @JvmInline
              public value class CaseString(
                public val `value`: String,
              ) : Labels

              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              public data class IdAndNameAndDescriptionAndColor(
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val id: Long? = null,
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val name: String? = null,
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val description: Optional<String> = Optional.Absent,
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val color: Optional<String> = Optional.Absent,
              ) : Labels

              public object Serializer : KSerializer<Labels> {
                @OptIn(
                  InternalSerializationApi::class,
                  ExperimentalSerializationApi::class,
                )
                override val descriptor: SerialDescriptor =
                    buildSerialDescriptor("io.github.api.Repos.OwnerPath.RepoPath.Issues.IssueNumberPath.Patch.Labels", PolymorphicKind.SEALED) {
                  element("CaseString", String.serializer().descriptor)
                  element("IdAndNameAndDescriptionAndColor", IdAndNameAndDescriptionAndColor.serializer().descriptor)
                }

                override fun deserialize(decoder: Decoder): Labels {
                  val value = decoder.decodeSerializableValue(JsonElement.serializer())
                  val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                  return when {
                    value is JsonPrimitive && value.isString -> json.attemptDeserialize(
                      value,
                      CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                    )
                    value is JsonObject -> json.attemptDeserialize(
                      value,
                      IdAndNameAndDescriptionAndColor::class to { decodeFromJsonElement(IdAndNameAndDescriptionAndColor.serializer(), it) },
                    )
                    else -> throw UnionSerializationException(value, emptyMap())
                  }
                }

                override fun serialize(encoder: Encoder, `value`: Labels) {
                  when(value) {
                    is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                    is IdAndNameAndDescriptionAndColor -> encoder.encodeSerializableValue(IdAndNameAndDescriptionAndColor.serializer(), value)
                  }
                }
              }
            }

            @Serializable
            public data class IssueFieldValues(
              @SerialName("field_id")
              public val fieldId: Long,
              public val `value`: Value,
            ) {
              /**
               * The value to set for the field
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
                      buildSerialDescriptor("io.github.api.Repos.OwnerPath.RepoPath.Issues.IssueNumberPath.Patch.IssueFieldValues.Value", PolymorphicKind.SEALED) {
                    element("CaseString", String.serializer().descriptor)
                    element("CaseDouble", Double.serializer().descriptor)
                  }

                  override fun deserialize(decoder: Decoder): Value {
                    val value = decoder.decodeSerializableValue(JsonElement.serializer())
                    val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                    return when {
                      value is JsonPrimitive && value.isString -> json.attemptDeserialize(
                        value,
                        CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                      )
                      value is JsonPrimitive && !value.isString && value.doubleOrNull != null -> json.attemptDeserialize(
                        value,
                        CaseDouble::class to { CaseDouble(decodeFromJsonElement(Double.serializer(), it)) },
                      )
                      else -> throw UnionSerializationException(value, emptyMap())
                    }
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

            @OptIn(ExperimentalSerializationApi::class)
            @Serializable
            internal data class Body(
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val title: Optional<Title> = Optional.Absent,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val body: Optional<String> = Optional.Absent,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val assignee: Optional<String> = Optional.Absent,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val state: State? = null,
              @SerialName("state_reason")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val stateReason: Optional<StateReason> = Optional.Absent,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val milestone: Optional<Milestone> = Optional.Absent,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val labels: List<Labels>? = null,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val assignees: List<String>? = null,
              @SerialName("issue_field_values")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val issueFieldValues: List<IssueFieldValues>? = null,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val type: Optional<String> = Optional.Absent,
            )

            public sealed interface Response {
              public data class Ok(
                public val `value`: Issue,
              ) : Response

              public data class MovedPermanently(
                public val `value`: BasicError,
              ) : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response

              public data class Gone(
                public val `value`: BasicError,
              ) : Response

              public data class UnprocessableEntity(
                public val `value`: ValidationError,
              ) : Response

              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              public data class ServiceUnavailable(
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val code: String? = null,
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val message: String? = null,
                @SerialName("documentation_url")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val documentationUrl: String? = null,
              ) : Response
            }
          }

          public class Assignees internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _issueNumber: Long,
          ) {
            public val delete: Delete = Delete(__client, _owner, _repo, _issueNumber)

            public val post: Post = Post(__client, _owner, _repo, _issueNumber)

            public fun assignee(assignee: String): AssigneePath = AssigneePath(__client, _owner, _repo, _issueNumber, assignee)

            public class Delete internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _issueNumber: Long,
            ) {
              public suspend operator fun invoke(): Issue {
                val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/issues/${this._issueNumber}/assignees")
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }

              public suspend operator fun invoke(assignees: List<String>? = null): Issue {
                val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/issues/${this._issueNumber}/assignees") {
                  contentType(ContentType.Application.Json)
                  setBody(Body(assignees = assignees))
                }
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }

              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              internal data class Body(
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val assignees: List<String>? = null,
              )
            }

            public class Post internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _issueNumber: Long,
            ) {
              public suspend operator fun invoke(): Issue {
                val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/issues/${this._issueNumber}/assignees")
                if (response.status.value != 201) throw ResponseException(response, "")
                return response.body()
              }

              public suspend operator fun invoke(assignees: List<String>? = null): Issue {
                val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/issues/${this._issueNumber}/assignees") {
                  contentType(ContentType.Application.Json)
                  setBody(Body(assignees = assignees))
                }
                if (response.status.value != 201) throw ResponseException(response, "")
                return response.body()
              }

              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              internal data class Body(
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val assignees: List<String>? = null,
              )
            }

            public class AssigneePath internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _issueNumber: Long,
              private val _assignee: String,
            ) {
              public val `get`: Get = Get(__client, _owner, _repo, _issueNumber, _assignee)

              public class Get internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _issueNumber: Long,
                private val _assignee: String,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/issues/${this._issueNumber}/assignees/${this._assignee.encodeURLParameter()}")
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

          public class Comments internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _issueNumber: Long,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo, _issueNumber)

            public val post: Post = Post(__client, _owner, _repo, _issueNumber)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _issueNumber: Long,
            ) {
              public suspend operator fun invoke(
                since: Instant? = null,
                perPage: Long? = 30L,
                page: Long? = 1L,
              ): Response {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/issues/${this._issueNumber}/comments") {
                  if (since != null) {
                    this.url.encodedParameters.append("since", since.toString().encodeURLParameter())
                  }
                  if (perPage != null) {
                    this.url.encodedParameters.append("per_page", perPage.toString())
                  }
                  if (page != null) {
                    this.url.encodedParameters.append("page", page.toString())
                  }
                }
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  404 -> Response.NotFound(response.body())
                  410 -> Response.Gone(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: List<IssueComment>,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response

                public data class Gone(
                  public val `value`: BasicError,
                ) : Response
              }
            }

            public class Post internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _issueNumber: Long,
            ) {
              public suspend operator fun invoke(body: String): Response {
                val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/issues/${this._issueNumber}/comments") {
                  contentType(ContentType.Application.Json)
                  setBody(Body(body = body))
                }
                return when (response.status.value) {
                  201 -> Response.Created(response.body())
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  410 -> Response.Gone(response.body())
                  422 -> Response.UnprocessableEntity(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              @Serializable
              internal data class Body(
                public val body: String,
              )

              public sealed interface Response {
                public data class Created(
                  public val `value`: IssueComment,
                ) : Response

                public data class Forbidden(
                  public val `value`: BasicError,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response

                public data class Gone(
                  public val `value`: BasicError,
                ) : Response

                public data class UnprocessableEntity(
                  public val `value`: ValidationError,
                ) : Response
              }
            }
          }

          public class Dependencies internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _issueNumber: Long,
          ) {
            public val blockedBy: BlockedBy = BlockedBy(__client, _owner, _repo, _issueNumber)

            public val blocking: Blocking = Blocking(__client, _owner, _repo, _issueNumber)

            public class BlockedBy internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _issueNumber: Long,
            ) {
              public val `get`: Get = Get(__client, _owner, _repo, _issueNumber)

              public val post: Post = Post(__client, _owner, _repo, _issueNumber)

              public fun issueId(issueId: Long): IssueIdPath = IssueIdPath(__client, _owner, _repo, _issueNumber, issueId)

              public class Get internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _issueNumber: Long,
              ) {
                public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
                  val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/issues/${this._issueNumber}/dependencies/blocked_by") {
                    if (perPage != null) {
                      this.url.encodedParameters.append("per_page", perPage.toString())
                    }
                    if (page != null) {
                      this.url.encodedParameters.append("page", page.toString())
                    }
                  }
                  return when (response.status.value) {
                    200 -> Response.Ok(response.body())
                    301 -> Response.MovedPermanently(response.body())
                    404 -> Response.NotFound(response.body())
                    410 -> Response.Gone(response.body())
                    else -> throw ResponseException(response, "")
                  }
                }

                public sealed interface Response {
                  public data class Ok(
                    public val `value`: List<Issue>,
                  ) : Response

                  public data class MovedPermanently(
                    public val `value`: BasicError,
                  ) : Response

                  public data class NotFound(
                    public val `value`: BasicError,
                  ) : Response

                  public data class Gone(
                    public val `value`: BasicError,
                  ) : Response
                }
              }

              public class Post internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _issueNumber: Long,
              ) {
                public suspend operator fun invoke(issueId: Long): Response {
                  val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/issues/${this._issueNumber}/dependencies/blocked_by") {
                    contentType(ContentType.Application.Json)
                    setBody(Body(issueId = issueId))
                  }
                  return when (response.status.value) {
                    201 -> Response.Created(response.body())
                    301 -> Response.MovedPermanently(response.body())
                    403 -> Response.Forbidden(response.body())
                    404 -> Response.NotFound(response.body())
                    410 -> Response.Gone(response.body())
                    422 -> Response.UnprocessableEntity(response.body())
                    else -> throw ResponseException(response, "")
                  }
                }

                @Serializable
                internal data class Body(
                  @SerialName("issue_id")
                  public val issueId: Long,
                )

                public sealed interface Response {
                  public data class Created(
                    public val `value`: Issue,
                  ) : Response

                  public data class MovedPermanently(
                    public val `value`: BasicError,
                  ) : Response

                  public data class Forbidden(
                    public val `value`: BasicError,
                  ) : Response

                  public data class NotFound(
                    public val `value`: BasicError,
                  ) : Response

                  public data class Gone(
                    public val `value`: BasicError,
                  ) : Response

                  public data class UnprocessableEntity(
                    public val `value`: ValidationError,
                  ) : Response
                }
              }

              public class IssueIdPath internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _issueNumber: Long,
                private val _issueId: Long,
              ) {
                public val delete: Delete = Delete(__client, _owner, _repo, _issueNumber, _issueId)

                public class Delete internal constructor(
                  private val __client: HttpClient,
                  private val _owner: String,
                  private val _repo: String,
                  private val _issueNumber: Long,
                  private val _issueId: Long,
                ) {
                  public suspend operator fun invoke(): Response {
                    val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/issues/${this._issueNumber}/dependencies/blocked_by/${this._issueId}")
                    return when (response.status.value) {
                      200 -> Response.Ok(response.body())
                      301 -> Response.MovedPermanently(response.body())
                      400 -> Response.BadRequest(response.body())
                      401 -> Response.Unauthorized(response.body())
                      403 -> Response.Forbidden(response.body())
                      404 -> Response.NotFound(response.body())
                      410 -> Response.Gone(response.body())
                      else -> throw ResponseException(response, "")
                    }
                  }

                  public sealed interface Response {
                    public data class Ok(
                      public val `value`: Issue,
                    ) : Response

                    public data class MovedPermanently(
                      public val `value`: BasicError,
                    ) : Response

                    public data class BadRequest(
                      public val `value`: BasicError,
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

                    public data class Gone(
                      public val `value`: BasicError,
                    ) : Response
                  }
                }
              }
            }

            public class Blocking internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _issueNumber: Long,
            ) {
              public val `get`: Get = Get(__client, _owner, _repo, _issueNumber)

              public class Get internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _issueNumber: Long,
              ) {
                public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
                  val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/issues/${this._issueNumber}/dependencies/blocking") {
                    if (perPage != null) {
                      this.url.encodedParameters.append("per_page", perPage.toString())
                    }
                    if (page != null) {
                      this.url.encodedParameters.append("page", page.toString())
                    }
                  }
                  return when (response.status.value) {
                    200 -> Response.Ok(response.body())
                    301 -> Response.MovedPermanently(response.body())
                    404 -> Response.NotFound(response.body())
                    410 -> Response.Gone(response.body())
                    else -> throw ResponseException(response, "")
                  }
                }

                public sealed interface Response {
                  public data class Ok(
                    public val `value`: List<Issue>,
                  ) : Response

                  public data class MovedPermanently(
                    public val `value`: BasicError,
                  ) : Response

                  public data class NotFound(
                    public val `value`: BasicError,
                  ) : Response

                  public data class Gone(
                    public val `value`: BasicError,
                  ) : Response
                }
              }
            }
          }

          public class Events internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _issueNumber: Long,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo, _issueNumber)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _issueNumber: Long,
            ) {
              public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/issues/${this._issueNumber}/events") {
                  if (perPage != null) {
                    this.url.encodedParameters.append("per_page", perPage.toString())
                  }
                  if (page != null) {
                    this.url.encodedParameters.append("page", page.toString())
                  }
                }
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  410 -> Response.Gone(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: List<IssueEventForIssue>,
                ) : Response

                public data class Gone(
                  public val `value`: BasicError,
                ) : Response
              }
            }
          }

          public class IssueFieldValues internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _issueNumber: Long,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo, _issueNumber)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _issueNumber: Long,
            ) {
              public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/issues/${this._issueNumber}/issue-field-values") {
                  if (perPage != null) {
                    this.url.encodedParameters.append("per_page", perPage.toString())
                  }
                  if (page != null) {
                    this.url.encodedParameters.append("page", page.toString())
                  }
                }
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  301 -> Response.MovedPermanently(response.body())
                  404 -> Response.NotFound(response.body())
                  410 -> Response.Gone(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: List<IssueFieldValue>,
                ) : Response

                public data class MovedPermanently(
                  public val `value`: BasicError,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response

                public data class Gone(
                  public val `value`: BasicError,
                ) : Response
              }
            }
          }

          public class Labels internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _issueNumber: Long,
          ) {
            public val delete: Delete = Delete(__client, _owner, _repo, _issueNumber)

            public val `get`: Get = Get(__client, _owner, _repo, _issueNumber)

            public val post: Post = Post(__client, _owner, _repo, _issueNumber)

            public val put: Put = Put(__client, _owner, _repo, _issueNumber)

            public fun name(name: String): NamePath = NamePath(__client, _owner, _repo, _issueNumber, name)

            public class Delete internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _issueNumber: Long,
            ) {
              public suspend operator fun invoke(): Response {
                val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/issues/${this._issueNumber}/labels")
                return when (response.status.value) {
                  204 -> Response.NoContent
                  301 -> Response.MovedPermanently(response.body())
                  404 -> Response.NotFound(response.body())
                  410 -> Response.Gone(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data object NoContent : Response

                public data class MovedPermanently(
                  public val `value`: BasicError,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response

                public data class Gone(
                  public val `value`: BasicError,
                ) : Response
              }
            }

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _issueNumber: Long,
            ) {
              public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/issues/${this._issueNumber}/labels") {
                  if (perPage != null) {
                    this.url.encodedParameters.append("per_page", perPage.toString())
                  }
                  if (page != null) {
                    this.url.encodedParameters.append("page", page.toString())
                  }
                }
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  301 -> Response.MovedPermanently(response.body())
                  404 -> Response.NotFound(response.body())
                  410 -> Response.Gone(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: List<Label>,
                ) : Response

                public data class MovedPermanently(
                  public val `value`: BasicError,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response

                public data class Gone(
                  public val `value`: BasicError,
                ) : Response
              }
            }

            public class Post internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _issueNumber: Long,
            ) {
              public suspend operator fun invoke(): Response {
                val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/issues/${this._issueNumber}/labels")
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  301 -> Response.MovedPermanently(response.body())
                  404 -> Response.NotFound(response.body())
                  410 -> Response.Gone(response.body())
                  422 -> Response.UnprocessableEntity(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public suspend operator fun invoke(body: LabelsStrings): Response {
                val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/issues/${this._issueNumber}/labels") {
                  contentType(ContentType.Application.Json)
                  setBody(body)
                }
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  301 -> Response.MovedPermanently(response.body())
                  404 -> Response.NotFound(response.body())
                  410 -> Response.Gone(response.body())
                  422 -> Response.UnprocessableEntity(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              @JvmName("StringList")
              public suspend operator fun invoke(body: List<String>): Response {
                val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/issues/${this._issueNumber}/labels") {
                  contentType(ContentType.Application.Json)
                  setBody(body)
                }
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  301 -> Response.MovedPermanently(response.body())
                  404 -> Response.NotFound(response.body())
                  410 -> Response.Gone(response.body())
                  422 -> Response.UnprocessableEntity(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              @JvmName("NameList")
              public suspend operator fun invoke(body: List<Name>): Response {
                val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/issues/${this._issueNumber}/labels") {
                  contentType(ContentType.Application.Json)
                  setBody(body)
                }
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  301 -> Response.MovedPermanently(response.body())
                  404 -> Response.NotFound(response.body())
                  410 -> Response.Gone(response.body())
                  422 -> Response.UnprocessableEntity(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              public data class LabelsStrings(
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val labels: List<String>? = null,
              )

              @Serializable
              public data class Name(
                public val name: String,
              )

              public sealed interface Response {
                public data class Ok(
                  public val `value`: List<Label>,
                ) : Response

                public data class MovedPermanently(
                  public val `value`: BasicError,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response

                public data class Gone(
                  public val `value`: BasicError,
                ) : Response

                public data class UnprocessableEntity(
                  public val `value`: ValidationError,
                ) : Response
              }
            }

            public class Put internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _issueNumber: Long,
            ) {
              public suspend operator fun invoke(): Response {
                val response = __client.put("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/issues/${this._issueNumber}/labels")
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  301 -> Response.MovedPermanently(response.body())
                  404 -> Response.NotFound(response.body())
                  410 -> Response.Gone(response.body())
                  422 -> Response.UnprocessableEntity(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public suspend operator fun invoke(body: LabelsStrings): Response {
                val response = __client.put("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/issues/${this._issueNumber}/labels") {
                  contentType(ContentType.Application.Json)
                  setBody(body)
                }
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  301 -> Response.MovedPermanently(response.body())
                  404 -> Response.NotFound(response.body())
                  410 -> Response.Gone(response.body())
                  422 -> Response.UnprocessableEntity(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              @JvmName("StringList")
              public suspend operator fun invoke(body: List<String>): Response {
                val response = __client.put("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/issues/${this._issueNumber}/labels") {
                  contentType(ContentType.Application.Json)
                  setBody(body)
                }
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  301 -> Response.MovedPermanently(response.body())
                  404 -> Response.NotFound(response.body())
                  410 -> Response.Gone(response.body())
                  422 -> Response.UnprocessableEntity(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public suspend operator fun invoke(body: LabelsNames): Response {
                val response = __client.put("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/issues/${this._issueNumber}/labels") {
                  contentType(ContentType.Application.Json)
                  setBody(body)
                }
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  301 -> Response.MovedPermanently(response.body())
                  404 -> Response.NotFound(response.body())
                  410 -> Response.Gone(response.body())
                  422 -> Response.UnprocessableEntity(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              @JvmName("NameList")
              public suspend operator fun invoke(body: List<Name>): Response {
                val response = __client.put("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/issues/${this._issueNumber}/labels") {
                  contentType(ContentType.Application.Json)
                  setBody(body)
                }
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  301 -> Response.MovedPermanently(response.body())
                  404 -> Response.NotFound(response.body())
                  410 -> Response.Gone(response.body())
                  422 -> Response.UnprocessableEntity(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public suspend operator fun invoke(body: String): Response {
                val response = __client.put("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/issues/${this._issueNumber}/labels") {
                  contentType(ContentType.Application.Json)
                  setBody(JsonPrimitive(body))
                }
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  301 -> Response.MovedPermanently(response.body())
                  404 -> Response.NotFound(response.body())
                  410 -> Response.Gone(response.body())
                  422 -> Response.UnprocessableEntity(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              public data class LabelsStrings(
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val labels: List<String>? = null,
              )

              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              public data class LabelsNames(
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val labels: List<Name>? = null,
              )

              @Serializable
              public data class Name(
                public val name: String,
              )

              public sealed interface Response {
                public data class Ok(
                  public val `value`: List<Label>,
                ) : Response

                public data class MovedPermanently(
                  public val `value`: BasicError,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response

                public data class Gone(
                  public val `value`: BasicError,
                ) : Response

                public data class UnprocessableEntity(
                  public val `value`: ValidationError,
                ) : Response
              }
            }

            public class NamePath internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _issueNumber: Long,
              private val _name: String,
            ) {
              public val delete: Delete = Delete(__client, _owner, _repo, _issueNumber, _name)

              public class Delete internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _issueNumber: Long,
                private val _name: String,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/issues/${this._issueNumber}/labels/${this._name.encodeURLParameter()}")
                  return when (response.status.value) {
                    200 -> Response.Ok(response.body())
                    301 -> Response.MovedPermanently(response.body())
                    404 -> Response.NotFound(response.body())
                    410 -> Response.Gone(response.body())
                    else -> throw ResponseException(response, "")
                  }
                }

                public sealed interface Response {
                  public data class Ok(
                    public val `value`: List<Label>,
                  ) : Response

                  public data class MovedPermanently(
                    public val `value`: BasicError,
                  ) : Response

                  public data class NotFound(
                    public val `value`: BasicError,
                  ) : Response

                  public data class Gone(
                    public val `value`: BasicError,
                  ) : Response
                }
              }
            }
          }

          public class Lock internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _issueNumber: Long,
          ) {
            public val delete: Delete = Delete(__client, _owner, _repo, _issueNumber)

            public val put: Put = Put(__client, _owner, _repo, _issueNumber)

            public class Delete internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _issueNumber: Long,
            ) {
              public suspend operator fun invoke(): Response {
                val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/issues/${this._issueNumber}/lock")
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

            public class Put internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _issueNumber: Long,
            ) {
              public suspend operator fun invoke(): Response {
                val response = __client.put("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/issues/${this._issueNumber}/lock")
                return when (response.status.value) {
                  204 -> Response.NoContent
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  410 -> Response.Gone(response.body())
                  422 -> Response.UnprocessableEntity(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public suspend operator fun invoke(lockReason: LockReason? = null): Response {
                val response = __client.put("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/issues/${this._issueNumber}/lock") {
                  contentType(ContentType.Application.Json)
                  setBody(Body(lockReason = lockReason))
                }
                return when (response.status.value) {
                  204 -> Response.NoContent
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  410 -> Response.Gone(response.body())
                  422 -> Response.UnprocessableEntity(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              @Serializable
              public enum class LockReason(
                public val `value`: String,
              ) {
                @SerialName("off-topic")
                OffTopic("off-topic"),
                @SerialName("too heated")
                TooHeated("too heated"),
                @SerialName("resolved")
                Resolved("resolved"),
                @SerialName("spam")
                Spam("spam"),
                ;
              }

              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              internal data class Body(
                @SerialName("lock_reason")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val lockReason: LockReason? = null,
              )

              public sealed interface Response {
                public data object NoContent : Response

                public data class Forbidden(
                  public val `value`: BasicError,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response

                public data class Gone(
                  public val `value`: BasicError,
                ) : Response

                public data class UnprocessableEntity(
                  public val `value`: ValidationError,
                ) : Response
              }
            }
          }

          public class Parent internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _issueNumber: Long,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo, _issueNumber)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _issueNumber: Long,
            ) {
              public suspend operator fun invoke(): Response {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/issues/${this._issueNumber}/parent")
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  301 -> Response.MovedPermanently(response.body())
                  404 -> Response.NotFound(response.body())
                  410 -> Response.Gone(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: Issue,
                ) : Response

                public data class MovedPermanently(
                  public val `value`: BasicError,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response

                public data class Gone(
                  public val `value`: BasicError,
                ) : Response
              }
            }
          }

          public class Reactions internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _issueNumber: Long,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo, _issueNumber)

            public val post: Post = Post(__client, _owner, _repo, _issueNumber)

            public fun reactionId(reactionId: Long): ReactionIdPath = ReactionIdPath(__client, _owner, _repo, _issueNumber, reactionId)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _issueNumber: Long,
            ) {
              public suspend operator fun invoke(
                content: Content? = null,
                perPage: Long? = 30L,
                page: Long? = 1L,
              ): Response {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/issues/${this._issueNumber}/reactions") {
                  if (content != null) {
                    this.url.encodedParameters.append("content", content.value.encodeURLParameter())
                  }
                  if (perPage != null) {
                    this.url.encodedParameters.append("per_page", perPage.toString())
                  }
                  if (page != null) {
                    this.url.encodedParameters.append("page", page.toString())
                  }
                }
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  404 -> Response.NotFound(response.body())
                  410 -> Response.Gone(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              @Serializable
              public enum class Content(
                public val `value`: String,
              ) {
                `+1`("+1"),
                `-1`("-1"),
                @SerialName("laugh")
                Laugh("laugh"),
                @SerialName("confused")
                Confused("confused"),
                @SerialName("heart")
                Heart("heart"),
                @SerialName("hooray")
                Hooray("hooray"),
                @SerialName("rocket")
                Rocket("rocket"),
                @SerialName("eyes")
                Eyes("eyes"),
                ;
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: List<Reaction>,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response

                public data class Gone(
                  public val `value`: BasicError,
                ) : Response
              }
            }

            public class Post internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _issueNumber: Long,
            ) {
              public suspend operator fun invoke(content: Content): Response {
                val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/issues/${this._issueNumber}/reactions") {
                  contentType(ContentType.Application.Json)
                  setBody(Body(content = content))
                }
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  201 -> Response.Created(response.body())
                  422 -> Response.UnprocessableEntity(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              @Serializable
              public enum class Content(
                public val `value`: String,
              ) {
                `+1`("+1"),
                `-1`("-1"),
                @SerialName("laugh")
                Laugh("laugh"),
                @SerialName("confused")
                Confused("confused"),
                @SerialName("heart")
                Heart("heart"),
                @SerialName("hooray")
                Hooray("hooray"),
                @SerialName("rocket")
                Rocket("rocket"),
                @SerialName("eyes")
                Eyes("eyes"),
                ;
              }

              @Serializable
              internal data class Body(
                public val content: Content,
              )

              public sealed interface Response {
                public data class Ok(
                  public val `value`: Reaction,
                ) : Response

                public data class Created(
                  public val `value`: Reaction,
                ) : Response

                public data class UnprocessableEntity(
                  public val `value`: ValidationError,
                ) : Response
              }
            }

            public class ReactionIdPath internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _issueNumber: Long,
              private val _reactionId: Long,
            ) {
              public val delete: Delete = Delete(__client, _owner, _repo, _issueNumber, _reactionId)

              public class Delete internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _issueNumber: Long,
                private val _reactionId: Long,
              ) {
                public suspend operator fun invoke() {
                  val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/issues/${this._issueNumber}/reactions/${this._reactionId}")
                  if (response.status.value != 204) throw ResponseException(response, "")
                }
              }
            }
          }

          public class SubIssue internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _issueNumber: Long,
          ) {
            public val delete: Delete = Delete(__client, _owner, _repo, _issueNumber)

            public class Delete internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _issueNumber: Long,
            ) {
              public suspend operator fun invoke(subIssueId: Long): Response {
                val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/issues/${this._issueNumber}/sub_issue") {
                  contentType(ContentType.Application.Json)
                  setBody(Body(subIssueId = subIssueId))
                }
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  400 -> Response.BadRequest(response.body())
                  404 -> Response.NotFound(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              @Serializable
              internal data class Body(
                @SerialName("sub_issue_id")
                public val subIssueId: Long,
              )

              public sealed interface Response {
                public data class Ok(
                  public val `value`: Issue,
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

          public class SubIssues internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _issueNumber: Long,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo, _issueNumber)

            public val post: Post = Post(__client, _owner, _repo, _issueNumber)

            public val priority: Priority = Priority(__client, _owner, _repo, _issueNumber)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _issueNumber: Long,
            ) {
              public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/issues/${this._issueNumber}/sub_issues") {
                  if (perPage != null) {
                    this.url.encodedParameters.append("per_page", perPage.toString())
                  }
                  if (page != null) {
                    this.url.encodedParameters.append("page", page.toString())
                  }
                }
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  404 -> Response.NotFound(response.body())
                  410 -> Response.Gone(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: List<Issue>,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response

                public data class Gone(
                  public val `value`: BasicError,
                ) : Response
              }
            }

            public class Post internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _issueNumber: Long,
            ) {
              public suspend operator fun invoke(subIssueId: Long, replaceParent: Boolean? = null): Response {
                val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/issues/${this._issueNumber}/sub_issues") {
                  contentType(ContentType.Application.Json)
                  setBody(Body(subIssueId = subIssueId, replaceParent = replaceParent))
                }
                return when (response.status.value) {
                  201 -> Response.Created(response.body())
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  410 -> Response.Gone(response.body())
                  422 -> Response.UnprocessableEntity(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              internal data class Body(
                @SerialName("sub_issue_id")
                public val subIssueId: Long,
                @SerialName("replace_parent")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val replaceParent: Boolean? = null,
              )

              public sealed interface Response {
                public data class Created(
                  public val `value`: Issue,
                ) : Response

                public data class Forbidden(
                  public val `value`: BasicError,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response

                public data class Gone(
                  public val `value`: BasicError,
                ) : Response

                public data class UnprocessableEntity(
                  public val `value`: ValidationError,
                ) : Response
              }
            }

            public class Priority internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _issueNumber: Long,
            ) {
              public val patch: Patch = Patch(__client, _owner, _repo, _issueNumber)

              public class Patch internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _issueNumber: Long,
              ) {
                public suspend operator fun invoke(
                  subIssueId: Long,
                  afterId: Long? = null,
                  beforeId: Long? = null,
                ): Response {
                  val response = __client.patch("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/issues/${this._issueNumber}/sub_issues/priority") {
                    contentType(ContentType.Application.Json)
                    setBody(Body(subIssueId = subIssueId, afterId = afterId, beforeId = beforeId))
                  }
                  return when (response.status.value) {
                    200 -> Response.Ok(response.body())
                    403 -> Response.Forbidden(response.body())
                    404 -> Response.NotFound(response.body())
                    422 -> Response.UnprocessableEntity(response.body())
                    503 -> response.body<Response.ServiceUnavailable>()
                    else -> throw ResponseException(response, "")
                  }
                }

                @OptIn(ExperimentalSerializationApi::class)
                @Serializable
                internal data class Body(
                  @SerialName("sub_issue_id")
                  public val subIssueId: Long,
                  @SerialName("after_id")
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val afterId: Long? = null,
                  @SerialName("before_id")
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val beforeId: Long? = null,
                )

                public sealed interface Response {
                  public data class Ok(
                    public val `value`: Issue,
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

                  @OptIn(ExperimentalSerializationApi::class)
                  @Serializable
                  public data class ServiceUnavailable(
                    @EncodeDefault(EncodeDefault.Mode.NEVER)
                    public val code: String? = null,
                    @EncodeDefault(EncodeDefault.Mode.NEVER)
                    public val message: String? = null,
                    @SerialName("documentation_url")
                    @EncodeDefault(EncodeDefault.Mode.NEVER)
                    public val documentationUrl: String? = null,
                  ) : Response
                }
              }
            }
          }

          public class Timeline internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _issueNumber: Long,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo, _issueNumber)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _issueNumber: Long,
            ) {
              public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/issues/${this._issueNumber}/timeline") {
                  if (perPage != null) {
                    this.url.encodedParameters.append("per_page", perPage.toString())
                  }
                  if (page != null) {
                    this.url.encodedParameters.append("page", page.toString())
                  }
                }
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  404 -> Response.NotFound(response.body())
                  410 -> Response.Gone(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: List<TimelineIssueEvents>,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response

                public data class Gone(
                  public val `value`: BasicError,
                ) : Response
              }
            }
          }
        }
      }

      public class Keys internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public val `get`: Get = Get(__client, _owner, _repo)

        public val post: Post = Post(__client, _owner, _repo)

        public fun keyId(keyId: Long): KeyIdPath = KeyIdPath(__client, _owner, _repo, keyId)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<DeployKey> {
            val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/keys") {
              if (perPage != null) {
                this.url.encodedParameters.append("per_page", perPage.toString())
              }
              if (page != null) {
                this.url.encodedParameters.append("page", page.toString())
              }
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class Post internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(
            title: String? = null,
            key: String,
            readOnly: Boolean? = null,
          ): Response {
            val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/keys") {
              contentType(ContentType.Application.Json)
              setBody(Body(title = title, key = key, readOnly = readOnly))
            }
            return when (response.status.value) {
              201 -> Response.Created(response.body())
              422 -> Response.UnprocessableEntity(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          @OptIn(ExperimentalSerializationApi::class)
          @Serializable
          internal data class Body(
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val title: String? = null,
            public val key: String,
            @SerialName("read_only")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val readOnly: Boolean? = null,
          )

          public sealed interface Response {
            public data class Created(
              public val `value`: DeployKey,
            ) : Response

            public data class UnprocessableEntity(
              public val `value`: ValidationError,
            ) : Response
          }
        }

        public class KeyIdPath internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
          private val _keyId: Long,
        ) {
          public val delete: Delete = Delete(__client, _owner, _repo, _keyId)

          public val `get`: Get = Get(__client, _owner, _repo, _keyId)

          public class Delete internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _keyId: Long,
          ) {
            public suspend operator fun invoke() {
              val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/keys/${this._keyId}")
              if (response.status.value != 204) throw ResponseException(response, "")
            }
          }

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _keyId: Long,
          ) {
            public suspend operator fun invoke(): Response {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/keys/${this._keyId}")
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: DeployKey,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response
            }
          }
        }
      }

      public class Labels internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public val `get`: Get = Get(__client, _owner, _repo)

        public val post: Post = Post(__client, _owner, _repo)

        public fun name(name: String): NamePath = NamePath(__client, _owner, _repo, name)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
            val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/labels") {
              if (perPage != null) {
                this.url.encodedParameters.append("per_page", perPage.toString())
              }
              if (page != null) {
                this.url.encodedParameters.append("page", page.toString())
              }
            }
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              404 -> Response.NotFound(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: List<Label>,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class Post internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(
            name: String,
            color: String? = null,
            description: String? = null,
          ): Response {
            val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/labels") {
              contentType(ContentType.Application.Json)
              setBody(Body(name = name, color = color, description = description))
            }
            return when (response.status.value) {
              201 -> Response.Created(response.body())
              404 -> Response.NotFound(response.body())
              422 -> Response.UnprocessableEntity(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          @OptIn(ExperimentalSerializationApi::class)
          @Serializable
          internal data class Body(
            public val name: String,
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val color: String? = null,
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val description: String? = null,
          )

          public sealed interface Response {
            public data class Created(
              public val `value`: Label,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response

            public data class UnprocessableEntity(
              public val `value`: ValidationError,
            ) : Response
          }
        }

        public class NamePath internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
          private val _name: String,
        ) {
          public val delete: Delete = Delete(__client, _owner, _repo, _name)

          public val `get`: Get = Get(__client, _owner, _repo, _name)

          public val patch: Patch = Patch(__client, _owner, _repo, _name)

          public class Delete internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _name: String,
          ) {
            public suspend operator fun invoke() {
              val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/labels/${this._name.encodeURLParameter()}")
              if (response.status.value != 204) throw ResponseException(response, "")
            }
          }

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _name: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/labels/${this._name.encodeURLParameter()}")
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: Label,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response
            }
          }

          public class Patch internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _name: String,
          ) {
            public suspend operator fun invoke(): Label {
              val response = __client.patch("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/labels/${this._name.encodeURLParameter()}")
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }

            public suspend operator fun invoke(
              newName: String? = null,
              color: String? = null,
              description: String? = null,
            ): Label {
              val response = __client.patch("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/labels/${this._name.encodeURLParameter()}") {
                contentType(ContentType.Application.Json)
                setBody(Body(newName = newName, color = color, description = description))
              }
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }

            @OptIn(ExperimentalSerializationApi::class)
            @Serializable
            internal data class Body(
              @SerialName("new_name")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val newName: String? = null,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val color: String? = null,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val description: String? = null,
            )
          }
        }
      }

      public class Languages internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public val `get`: Get = Get(__client, _owner, _repo)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(): Language {
            val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/languages")
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }
      }

      public class License internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public val `get`: Get = Get(__client, _owner, _repo)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(ref: String? = null): Response {
            val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/license") {
              if (ref != null) {
                this.url.encodedParameters.append("ref", ref.encodeURLParameter())
              }
            }
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              404 -> Response.NotFound(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: LicenseContent,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response
          }
        }
      }

      public class MergeUpstream internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public val post: Post = Post(__client, _owner, _repo)

        public class Post internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(branch: String): Response {
            val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/merge-upstream") {
              contentType(ContentType.Application.Json)
              setBody(Body(branch = branch))
            }
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              409 -> Response.Conflict
              422 -> Response.UnprocessableEntity
              else -> throw ResponseException(response, "")
            }
          }

          @Serializable
          internal data class Body(
            public val branch: String,
          )

          public sealed interface Response {
            public data class Ok(
              public val `value`: MergedUpstream,
            ) : Response

            public data object Conflict : Response

            public data object UnprocessableEntity : Response
          }
        }
      }

      public class Merges internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public val post: Post = Post(__client, _owner, _repo)

        public class Post internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(
            base: String,
            head: String,
            commitMessage: String? = null,
          ): Response {
            val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/merges") {
              contentType(ContentType.Application.Json)
              setBody(Body(base = base, head = head, commitMessage = commitMessage))
            }
            return when (response.status.value) {
              201 -> Response.Created(response.body())
              204 -> Response.NoContent
              403 -> Response.Forbidden(response.body())
              404 -> Response.NotFound
              409 -> Response.Conflict
              422 -> Response.UnprocessableEntity(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          @OptIn(ExperimentalSerializationApi::class)
          @Serializable
          internal data class Body(
            public val base: String,
            public val head: String,
            @SerialName("commit_message")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val commitMessage: String? = null,
          )

          public sealed interface Response {
            public data class Created(
              public val `value`: Commit,
            ) : Response

            public data object NoContent : Response

            public data class Forbidden(
              public val `value`: BasicError,
            ) : Response

            public data object NotFound : Response

            public data object Conflict : Response

            public data class UnprocessableEntity(
              public val `value`: ValidationError,
            ) : Response
          }
        }
      }

      public class Milestones internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public val `get`: Get = Get(__client, _owner, _repo)

        public val post: Post = Post(__client, _owner, _repo)

        public fun milestoneNumber(milestoneNumber: Long): MilestoneNumberPath = MilestoneNumberPath(__client, _owner, _repo, milestoneNumber)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(
            state: State? = State.Open,
            sort: Sort? = Sort.DueOn,
            direction: Direction? = Direction.Asc,
            perPage: Long? = 30L,
            page: Long? = 1L,
          ): Response {
            val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/milestones") {
              if (state != null) {
                this.url.encodedParameters.append("state", state.value.encodeURLParameter())
              }
              if (sort != null) {
                this.url.encodedParameters.append("sort", sort.value.encodeURLParameter())
              }
              if (direction != null) {
                this.url.encodedParameters.append("direction", direction.value.encodeURLParameter())
              }
              if (perPage != null) {
                this.url.encodedParameters.append("per_page", perPage.toString())
              }
              if (page != null) {
                this.url.encodedParameters.append("page", page.toString())
              }
            }
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              404 -> Response.NotFound(response.body())
              else -> throw ResponseException(response, "")
            }
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
            @SerialName("due_on")
            DueOn("due_on"),
            @SerialName("completeness")
            Completeness("completeness"),
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
              public val `value`: List<Milestone>,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class Post internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(
            title: String,
            state: State? = null,
            description: String? = null,
            dueOn: Instant? = null,
          ): Response {
            val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/milestones") {
              contentType(ContentType.Application.Json)
              setBody(Body(title = title, state = state, description = description, dueOn = dueOn))
            }
            return when (response.status.value) {
              201 -> Response.Created(response.body())
              404 -> Response.NotFound(response.body())
              422 -> Response.UnprocessableEntity(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          @Serializable
          public enum class State(
            public val `value`: String,
          ) {
            @SerialName("open")
            Open("open"),
            @SerialName("closed")
            Closed("closed"),
            ;
          }

          @OptIn(ExperimentalSerializationApi::class)
          @Serializable
          internal data class Body(
            public val title: String,
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val state: State? = null,
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val description: String? = null,
            @SerialName("due_on")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val dueOn: Instant? = null,
          )

          public sealed interface Response {
            public data class Created(
              public val `value`: Milestone,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response

            public data class UnprocessableEntity(
              public val `value`: ValidationError,
            ) : Response
          }
        }

        public class MilestoneNumberPath internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
          private val _milestoneNumber: Long,
        ) {
          public val delete: Delete = Delete(__client, _owner, _repo, _milestoneNumber)

          public val `get`: Get = Get(__client, _owner, _repo, _milestoneNumber)

          public val patch: Patch = Patch(__client, _owner, _repo, _milestoneNumber)

          public val labels: Labels = Labels(__client, _owner, _repo, _milestoneNumber)

          public class Delete internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _milestoneNumber: Long,
          ) {
            public suspend operator fun invoke(): Response {
              val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/milestones/${this._milestoneNumber}")
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
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _milestoneNumber: Long,
          ) {
            public suspend operator fun invoke(): Response {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/milestones/${this._milestoneNumber}")
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: Milestone,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response
            }
          }

          public class Patch internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _milestoneNumber: Long,
          ) {
            public suspend operator fun invoke(): Milestone {
              val response = __client.patch("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/milestones/${this._milestoneNumber}")
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }

            public suspend operator fun invoke(
              title: String? = null,
              state: State? = null,
              description: String? = null,
              dueOn: Instant? = null,
            ): Milestone {
              val response = __client.patch("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/milestones/${this._milestoneNumber}") {
                contentType(ContentType.Application.Json)
                setBody(Body(title = title, state = state, description = description, dueOn = dueOn))
              }
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }

            @Serializable
            public enum class State(
              public val `value`: String,
            ) {
              @SerialName("open")
              Open("open"),
              @SerialName("closed")
              Closed("closed"),
              ;
            }

            @OptIn(ExperimentalSerializationApi::class)
            @Serializable
            internal data class Body(
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val title: String? = null,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val state: State? = null,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val description: String? = null,
              @SerialName("due_on")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val dueOn: Instant? = null,
            )
          }

          public class Labels internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _milestoneNumber: Long,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo, _milestoneNumber)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _milestoneNumber: Long,
            ) {
              public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<Label> {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/milestones/${this._milestoneNumber}/labels") {
                  if (perPage != null) {
                    this.url.encodedParameters.append("per_page", perPage.toString())
                  }
                  if (page != null) {
                    this.url.encodedParameters.append("page", page.toString())
                  }
                }
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }
            }
          }
        }
      }

      public class Notifications internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public val `get`: Get = Get(__client, _owner, _repo)

        public val put: Put = Put(__client, _owner, _repo)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(
            all: Boolean? = false,
            participating: Boolean? = false,
            since: Instant? = null,
            before: Instant? = null,
            perPage: Long? = 30L,
            page: Long? = 1L,
          ): List<Thread> {
            val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/notifications") {
              if (all != null) {
                this.url.encodedParameters.append("all", all.toString())
              }
              if (participating != null) {
                this.url.encodedParameters.append("participating", participating.toString())
              }
              if (since != null) {
                this.url.encodedParameters.append("since", since.toString().encodeURLParameter())
              }
              if (before != null) {
                this.url.encodedParameters.append("before", before.toString().encodeURLParameter())
              }
              if (perPage != null) {
                this.url.encodedParameters.append("per_page", perPage.toString())
              }
              if (page != null) {
                this.url.encodedParameters.append("page", page.toString())
              }
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class Put internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = __client.put("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/notifications")
            return when (response.status.value) {
              202 -> response.body<Response.Accepted>()
              205 -> Response.ResetContent
              else -> throw ResponseException(response, "")
            }
          }

          public suspend operator fun invoke(lastReadAt: Instant? = null): Response {
            val response = __client.put("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/notifications") {
              contentType(ContentType.Application.Json)
              setBody(Body(lastReadAt = lastReadAt))
            }
            return when (response.status.value) {
              202 -> response.body<Response.Accepted>()
              205 -> Response.ResetContent
              else -> throw ResponseException(response, "")
            }
          }

          @OptIn(ExperimentalSerializationApi::class)
          @Serializable
          internal data class Body(
            @SerialName("last_read_at")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val lastReadAt: Instant? = null,
          )

          public sealed interface Response {
            @OptIn(ExperimentalSerializationApi::class)
            @Serializable
            public data class Accepted(
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val message: String? = null,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val url: String? = null,
            ) : Response

            public data object ResetContent : Response
          }
        }
      }

      public class Pages internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public val delete: Delete = Delete(__client, _owner, _repo)

        public val `get`: Get = Get(__client, _owner, _repo)

        public val post: Post = Post(__client, _owner, _repo)

        public val put: Put = Put(__client, _owner, _repo)

        public val builds: Builds = Builds(__client, _owner, _repo)

        public val deployments: Deployments = Deployments(__client, _owner, _repo)

        public val health: Health = Health(__client, _owner, _repo)

        public class Delete internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/pages")
            return when (response.status.value) {
              204 -> Response.NoContent
              404 -> Response.NotFound(response.body())
              409 -> Response.Conflict(response.body())
              422 -> Response.UnprocessableEntity(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data object NoContent : Response

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

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/pages")
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              404 -> Response.NotFound(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: Page,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class Post internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(buildType: BuildType? = null, source: Source? = null): Response {
            val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/pages") {
              contentType(ContentType.Application.Json)
              setBody(Body(buildType = buildType, source = source))
            }
            return when (response.status.value) {
              201 -> Response.Created(response.body())
              409 -> Response.Conflict(response.body())
              422 -> Response.UnprocessableEntity(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          @Serializable
          public enum class BuildType(
            public val `value`: String,
          ) {
            @SerialName("legacy")
            Legacy("legacy"),
            @SerialName("workflow")
            Workflow("workflow"),
            ;
          }

          /**
           * The source branch and directory used to publish your Pages site.
           */
          @OptIn(ExperimentalSerializationApi::class)
          @Serializable
          public data class Source(
            public val branch: String,
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val path: Path? = null,
          ) {
            @Serializable
            public enum class Path(
              public val `value`: String,
            ) {
              @SerialName("/")
              Slash("/"),
              @SerialName("/docs")
              Docs("/docs"),
              ;
            }
          }

          /**
           * The source branch and directory used to publish your Pages site.
           */
          @OptIn(ExperimentalSerializationApi::class)
          @Serializable
          internal data class Body(
            @SerialName("build_type")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val buildType: BuildType? = null,
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val source: Source? = null,
          )

          public sealed interface Response {
            public data class Created(
              public val `value`: Page,
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
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(
            cname: Optional<String> = Optional.Absent,
            httpsEnforced: Boolean? = null,
            buildType: BuildType? = null,
            source: Source? = null,
          ): Response {
            val response = __client.put("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/pages") {
              contentType(ContentType.Application.Json)
              setBody(Body(cname = cname, httpsEnforced = httpsEnforced, buildType = buildType, source = source))
            }
            return when (response.status.value) {
              204 -> Response.NoContent
              400 -> Response.BadRequest(response.body())
              409 -> Response.Conflict(response.body())
              422 -> Response.UnprocessableEntity(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          @Serializable
          public enum class BuildType(
            public val `value`: String,
          ) {
            @SerialName("legacy")
            Legacy("legacy"),
            @SerialName("workflow")
            Workflow("workflow"),
            ;
          }

          @Serializable(with = Source.Serializer::class)
          public sealed interface Source {
            @Serializable
            public enum class GhPagesOrMasterOrMasterDocs(
              public val `value`: String,
            ) : Source {
              @SerialName("gh-pages")
              GhPages("gh-pages"),
              @SerialName("master")
              Master("master"),
              @SerialName("master /docs")
              MasterDocs("master /docs"),
              ;
            }

            /**
             * Update the source for the repository. Must include the branch name and path.
             */
            @Serializable
            public data class BranchAndPath(
              public val branch: String,
              public val path: Path,
            ) : Source {
              @Serializable
              public enum class Path(
                public val `value`: String,
              ) {
                @SerialName("/")
                Slash("/"),
                @SerialName("/docs")
                Docs("/docs"),
                ;
              }
            }

            public object Serializer : KSerializer<Source> {
              @OptIn(
                InternalSerializationApi::class,
                ExperimentalSerializationApi::class,
              )
              override val descriptor: SerialDescriptor =
                  buildSerialDescriptor("io.github.api.Repos.OwnerPath.RepoPath.Pages.Put.Source", PolymorphicKind.SEALED) {
                element("GhPagesOrMasterOrMasterDocs", GhPagesOrMasterOrMasterDocs.serializer().descriptor)
                element("BranchAndPath", BranchAndPath.serializer().descriptor)
              }

              override fun deserialize(decoder: Decoder): Source {
                val value = decoder.decodeSerializableValue(JsonElement.serializer())
                val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                return when {
                  value is JsonPrimitive && value.isString -> json.attemptDeserialize(
                    value,
                    GhPagesOrMasterOrMasterDocs::class to { decodeFromJsonElement(GhPagesOrMasterOrMasterDocs.serializer(), it) },
                  )
                  value is JsonObject -> json.attemptDeserialize(
                    value,
                    BranchAndPath::class to { decodeFromJsonElement(BranchAndPath.serializer(), it) },
                  )
                  else -> throw UnionSerializationException(value, emptyMap())
                }
              }

              override fun serialize(encoder: Encoder, `value`: Source) {
                when(value) {
                  is GhPagesOrMasterOrMasterDocs -> encoder.encodeSerializableValue(GhPagesOrMasterOrMasterDocs.serializer(), value)
                  is BranchAndPath -> encoder.encodeSerializableValue(BranchAndPath.serializer(), value)
                }
              }
            }
          }

          @OptIn(ExperimentalSerializationApi::class)
          @Serializable
          internal data class Body(
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val cname: Optional<String> = Optional.Absent,
            @SerialName("https_enforced")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val httpsEnforced: Boolean? = null,
            @SerialName("build_type")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val buildType: BuildType? = null,
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val source: Source? = null,
          )

          public sealed interface Response {
            public data object NoContent : Response

            public data class BadRequest(
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

        public class Builds internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public val `get`: Get = Get(__client, _owner, _repo)

          public val post: Post = Post(__client, _owner, _repo)

          public val latest: Latest = Latest(__client, _owner, _repo)

          public fun buildId(buildId: Long): BuildIdPath = BuildIdPath(__client, _owner, _repo, buildId)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<PageBuild> {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/pages/builds") {
                if (perPage != null) {
                  this.url.encodedParameters.append("per_page", perPage.toString())
                }
                if (page != null) {
                  this.url.encodedParameters.append("page", page.toString())
                }
              }
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }
          }

          public class Post internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public suspend operator fun invoke(): PageBuildStatus {
              val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/pages/builds")
              if (response.status.value != 201) throw ResponseException(response, "")
              return response.body()
            }
          }

          public class Latest internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
            ) {
              public suspend operator fun invoke(): PageBuild {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/pages/builds/latest")
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }
            }
          }

          public class BuildIdPath internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _buildId: Long,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo, _buildId)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _buildId: Long,
            ) {
              public suspend operator fun invoke(): PageBuild {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/pages/builds/${this._buildId}")
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }
            }
          }
        }

        public class Deployments internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public val post: Post = Post(__client, _owner, _repo)

          public fun pagesDeploymentId(pagesDeploymentId: Long): PagesDeploymentIdPath = PagesDeploymentIdPath(__client, _owner, _repo, pagesDeploymentId.toString())

          public fun pagesDeploymentId(pagesDeploymentId: String): PagesDeploymentIdPath = PagesDeploymentIdPath(__client, _owner, _repo, pagesDeploymentId)

          public class Post internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public suspend operator fun invoke(
              artifactId: Double? = null,
              artifactUrl: String? = null,
              environment: String? = null,
              pagesBuildVersion: String,
              oidcToken: String,
            ): Response {
              val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/pages/deployments") {
                contentType(ContentType.Application.Json)
                setBody(Body(artifactId = artifactId, artifactUrl = artifactUrl, environment = environment, pagesBuildVersion = pagesBuildVersion, oidcToken = oidcToken))
              }
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                400 -> Response.BadRequest(response.body())
                404 -> Response.NotFound(response.body())
                422 -> Response.UnprocessableEntity(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            /**
             * The object used to create GitHub Pages deployment
             */
            @OptIn(ExperimentalSerializationApi::class)
            @Serializable
            internal data class Body(
              @SerialName("artifact_id")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val artifactId: Double? = null,
              @SerialName("artifact_url")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val artifactUrl: String? = null,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val environment: String? = null,
              @SerialName("pages_build_version")
              @Required
              public val pagesBuildVersion: String = "GITHUB_SHA",
              @SerialName("oidc_token")
              public val oidcToken: String,
            )

            public sealed interface Response {
              public data class Ok(
                public val `value`: PageDeployment,
              ) : Response

              public data class BadRequest(
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

          public class PagesDeploymentIdPath internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _pagesDeploymentId: String,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo, _pagesDeploymentId)

            public val cancel: Cancel = Cancel(__client, _owner, _repo, _pagesDeploymentId)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _pagesDeploymentId: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/pages/deployments/${this._pagesDeploymentId.encodeURLParameter()}")
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  404 -> Response.NotFound(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: PagesDeploymentStatus,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response
              }
            }

            public class Cancel internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _pagesDeploymentId: String,
            ) {
              public val post: Post = Post(__client, _owner, _repo, _pagesDeploymentId)

              public class Post internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _pagesDeploymentId: String,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/pages/deployments/${this._pagesDeploymentId.encodeURLParameter()}/cancel")
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

        public class Health internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public val `get`: Get = Get(__client, _owner, _repo)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/pages/health")
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                202 -> Response.Accepted(response.body())
                400 -> Response.BadRequest
                404 -> Response.NotFound(response.body())
                422 -> Response.UnprocessableEntity
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: PagesHealthCheck,
              ) : Response

              public data class Accepted(
                public val `value`: EmptyObject,
              ) : Response

              public data object BadRequest : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response

              public data object UnprocessableEntity : Response
            }
          }
        }
      }

      public class PrivateVulnerabilityReporting internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public val delete: Delete = Delete(__client, _owner, _repo)

        public val `get`: Get = Get(__client, _owner, _repo)

        public val put: Put = Put(__client, _owner, _repo)

        public class Delete internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/private-vulnerability-reporting")
            return when (response.status.value) {
              204 -> Response.NoContent
              422 -> Response.UnprocessableEntity(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data object NoContent : Response

            public data class UnprocessableEntity(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/private-vulnerability-reporting")
            return when (response.status.value) {
              200 -> response.body<Response.Ok>()
              422 -> Response.UnprocessableEntity(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            @Serializable
            public data class Ok(
              public val enabled: Boolean,
            ) : Response

            public data class UnprocessableEntity(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class Put internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = __client.put("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/private-vulnerability-reporting")
            return when (response.status.value) {
              204 -> Response.NoContent
              422 -> Response.UnprocessableEntity(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data object NoContent : Response

            public data class UnprocessableEntity(
              public val `value`: BasicError,
            ) : Response
          }
        }
      }

      public class Properties internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public val values: Values = Values(__client, _owner, _repo)

        public class Values internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public val `get`: Get = Get(__client, _owner, _repo)

          public val patch: Patch = Patch(__client, _owner, _repo)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/properties/values")
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: List<CustomPropertyValue>,
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
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public suspend operator fun invoke(properties: List<CustomPropertyValue>): Response {
              val response = __client.patch("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/properties/values") {
                contentType(ContentType.Application.Json)
                setBody(Body(properties = properties))
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

      public class Pulls internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public val `get`: Get = Get(__client, _owner, _repo)

        public val post: Post = Post(__client, _owner, _repo)

        public val comments: Comments = Comments(__client, _owner, _repo)

        public fun pullNumber(pullNumber: Long): PullNumberPath = PullNumberPath(__client, _owner, _repo, pullNumber)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(
            state: State? = State.Open,
            head: String? = null,
            base: String? = null,
            sort: Sort? = Sort.Created,
            direction: Direction? = null,
            perPage: Long? = 30L,
            page: Long? = 1L,
          ): Response {
            val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/pulls") {
              if (state != null) {
                this.url.encodedParameters.append("state", state.value.encodeURLParameter())
              }
              if (head != null) {
                this.url.encodedParameters.append("head", head.encodeURLParameter())
              }
              if (base != null) {
                this.url.encodedParameters.append("base", base.encodeURLParameter())
              }
              if (sort != null) {
                this.url.encodedParameters.append("sort", sort.value.encodeURLParameter())
              }
              if (direction != null) {
                this.url.encodedParameters.append("direction", direction.value.encodeURLParameter())
              }
              if (perPage != null) {
                this.url.encodedParameters.append("per_page", perPage.toString())
              }
              if (page != null) {
                this.url.encodedParameters.append("page", page.toString())
              }
            }
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              304 -> Response.NotModified
              422 -> Response.UnprocessableEntity(response.body())
              else -> throw ResponseException(response, "")
            }
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
            @SerialName("popularity")
            Popularity("popularity"),
            @SerialName("long-running")
            LongRunning("long-running"),
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
              public val `value`: List<PullRequestSimple>,
            ) : Response

            public data object NotModified : Response

            public data class UnprocessableEntity(
              public val `value`: ValidationError,
            ) : Response
          }
        }

        public class Post internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(
            title: String? = null,
            head: String,
            headRepo: String? = null,
            base: String,
            body: String? = null,
            maintainerCanModify: Boolean? = null,
            draft: Boolean? = null,
            issue: Long? = null,
          ): Response {
            val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/pulls") {
              contentType(ContentType.Application.Json)
              setBody(Body(title = title, head = head, headRepo = headRepo, base = base, body = body, maintainerCanModify = maintainerCanModify, draft = draft, issue = issue))
            }
            return when (response.status.value) {
              201 -> Response.Created(response.body())
              403 -> Response.Forbidden(response.body())
              422 -> Response.UnprocessableEntity(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          @OptIn(ExperimentalSerializationApi::class)
          @Serializable
          internal data class Body(
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val title: String? = null,
            public val head: String,
            @SerialName("head_repo")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val headRepo: String? = null,
            public val base: String,
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val body: String? = null,
            @SerialName("maintainer_can_modify")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val maintainerCanModify: Boolean? = null,
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val draft: Boolean? = null,
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val issue: Long? = null,
          )

          public sealed interface Response {
            public data class Created(
              public val `value`: PullRequest,
            ) : Response

            public data class Forbidden(
              public val `value`: BasicError,
            ) : Response

            public data class UnprocessableEntity(
              public val `value`: ValidationError,
            ) : Response
          }
        }

        public class Comments internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public val `get`: Get = Get(__client, _owner, _repo)

          public fun commentId(commentId: Long): CommentIdPath = CommentIdPath(__client, _owner, _repo, commentId)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public suspend operator fun invoke(
              sort: Sort? = null,
              direction: Direction? = null,
              since: Instant? = null,
              perPage: Long? = 30L,
              page: Long? = 1L,
            ): List<PullRequestReviewComment> {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/pulls/comments") {
                if (sort != null) {
                  this.url.encodedParameters.append("sort", sort.value.encodeURLParameter())
                }
                if (direction != null) {
                  this.url.encodedParameters.append("direction", direction.value.encodeURLParameter())
                }
                if (since != null) {
                  this.url.encodedParameters.append("since", since.toString().encodeURLParameter())
                }
                if (perPage != null) {
                  this.url.encodedParameters.append("per_page", perPage.toString())
                }
                if (page != null) {
                  this.url.encodedParameters.append("page", page.toString())
                }
              }
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }

            @Serializable
            public enum class Sort(
              public val `value`: String,
            ) {
              @SerialName("created")
              Created("created"),
              @SerialName("updated")
              Updated("updated"),
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
          }

          public class CommentIdPath internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _commentId: Long,
          ) {
            public val delete: Delete = Delete(__client, _owner, _repo, _commentId)

            public val `get`: Get = Get(__client, _owner, _repo, _commentId)

            public val patch: Patch = Patch(__client, _owner, _repo, _commentId)

            public val reactions: Reactions = Reactions(__client, _owner, _repo, _commentId)

            public class Delete internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _commentId: Long,
            ) {
              public suspend operator fun invoke(): Response {
                val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/pulls/comments/${this._commentId}")
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
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _commentId: Long,
            ) {
              public suspend operator fun invoke(): Response {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/pulls/comments/${this._commentId}")
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  404 -> Response.NotFound(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: PullRequestReviewComment,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response
              }
            }

            public class Patch internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _commentId: Long,
            ) {
              public suspend operator fun invoke(body: String): PullRequestReviewComment {
                val response = __client.patch("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/pulls/comments/${this._commentId}") {
                  contentType(ContentType.Application.Json)
                  setBody(Body(body = body))
                }
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }

              @Serializable
              internal data class Body(
                public val body: String,
              )
            }

            public class Reactions internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _commentId: Long,
            ) {
              public val `get`: Get = Get(__client, _owner, _repo, _commentId)

              public val post: Post = Post(__client, _owner, _repo, _commentId)

              public fun reactionId(reactionId: Long): ReactionIdPath = ReactionIdPath(__client, _owner, _repo, _commentId, reactionId)

              public class Get internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _commentId: Long,
              ) {
                public suspend operator fun invoke(
                  content: Content? = null,
                  perPage: Long? = 30L,
                  page: Long? = 1L,
                ): Response {
                  val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/pulls/comments/${this._commentId}/reactions") {
                    if (content != null) {
                      this.url.encodedParameters.append("content", content.value.encodeURLParameter())
                    }
                    if (perPage != null) {
                      this.url.encodedParameters.append("per_page", perPage.toString())
                    }
                    if (page != null) {
                      this.url.encodedParameters.append("page", page.toString())
                    }
                  }
                  return when (response.status.value) {
                    200 -> Response.Ok(response.body())
                    404 -> Response.NotFound(response.body())
                    else -> throw ResponseException(response, "")
                  }
                }

                @Serializable
                public enum class Content(
                  public val `value`: String,
                ) {
                  `+1`("+1"),
                  `-1`("-1"),
                  @SerialName("laugh")
                  Laugh("laugh"),
                  @SerialName("confused")
                  Confused("confused"),
                  @SerialName("heart")
                  Heart("heart"),
                  @SerialName("hooray")
                  Hooray("hooray"),
                  @SerialName("rocket")
                  Rocket("rocket"),
                  @SerialName("eyes")
                  Eyes("eyes"),
                  ;
                }

                public sealed interface Response {
                  public data class Ok(
                    public val `value`: List<Reaction>,
                  ) : Response

                  public data class NotFound(
                    public val `value`: BasicError,
                  ) : Response
                }
              }

              public class Post internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _commentId: Long,
              ) {
                public suspend operator fun invoke(content: Content): Response {
                  val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/pulls/comments/${this._commentId}/reactions") {
                    contentType(ContentType.Application.Json)
                    setBody(Body(content = content))
                  }
                  return when (response.status.value) {
                    200 -> Response.Ok(response.body())
                    201 -> Response.Created(response.body())
                    422 -> Response.UnprocessableEntity(response.body())
                    else -> throw ResponseException(response, "")
                  }
                }

                @Serializable
                public enum class Content(
                  public val `value`: String,
                ) {
                  `+1`("+1"),
                  `-1`("-1"),
                  @SerialName("laugh")
                  Laugh("laugh"),
                  @SerialName("confused")
                  Confused("confused"),
                  @SerialName("heart")
                  Heart("heart"),
                  @SerialName("hooray")
                  Hooray("hooray"),
                  @SerialName("rocket")
                  Rocket("rocket"),
                  @SerialName("eyes")
                  Eyes("eyes"),
                  ;
                }

                @Serializable
                internal data class Body(
                  public val content: Content,
                )

                public sealed interface Response {
                  public data class Ok(
                    public val `value`: Reaction,
                  ) : Response

                  public data class Created(
                    public val `value`: Reaction,
                  ) : Response

                  public data class UnprocessableEntity(
                    public val `value`: ValidationError,
                  ) : Response
                }
              }

              public class ReactionIdPath internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _commentId: Long,
                private val _reactionId: Long,
              ) {
                public val delete: Delete = Delete(__client, _owner, _repo, _commentId, _reactionId)

                public class Delete internal constructor(
                  private val __client: HttpClient,
                  private val _owner: String,
                  private val _repo: String,
                  private val _commentId: Long,
                  private val _reactionId: Long,
                ) {
                  public suspend operator fun invoke() {
                    val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/pulls/comments/${this._commentId}/reactions/${this._reactionId}")
                    if (response.status.value != 204) throw ResponseException(response, "")
                  }
                }
              }
            }
          }
        }

        public class PullNumberPath internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
          private val _pullNumber: Long,
        ) {
          public val `get`: Get = Get(__client, _owner, _repo, _pullNumber)

          public val patch: Patch = Patch(__client, _owner, _repo, _pullNumber)

          public val codespaces: Codespaces = Codespaces(__client, _owner, _repo, _pullNumber)

          public val comments: Comments = Comments(__client, _owner, _repo, _pullNumber)

          public val commits: Commits = Commits(__client, _owner, _repo, _pullNumber)

          public val files: Files = Files(__client, _owner, _repo, _pullNumber)

          public val merge: Merge = Merge(__client, _owner, _repo, _pullNumber)

          public val requestedReviewers: RequestedReviewers =
              RequestedReviewers(__client, _owner, _repo, _pullNumber)

          public val reviews: Reviews = Reviews(__client, _owner, _repo, _pullNumber)

          public val updateBranch: UpdateBranch = UpdateBranch(__client, _owner, _repo, _pullNumber)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _pullNumber: Long,
          ) {
            public suspend operator fun invoke(): Response {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/pulls/${this._pullNumber}")
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                304 -> Response.NotModified
                404 -> Response.NotFound(response.body())
                406 -> Response.NotAcceptable(response.body())
                500 -> Response.InternalServerError(response.body())
                503 -> response.body<Response.ServiceUnavailable>()
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: PullRequest,
              ) : Response

              public data object NotModified : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response

              public data class NotAcceptable(
                public val `value`: BasicError,
              ) : Response

              public data class InternalServerError(
                public val `value`: BasicError,
              ) : Response

              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              public data class ServiceUnavailable(
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val code: String? = null,
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val message: String? = null,
                @SerialName("documentation_url")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val documentationUrl: String? = null,
              ) : Response
            }
          }

          public class Patch internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _pullNumber: Long,
          ) {
            public suspend operator fun invoke(): Response {
              val response = __client.patch("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/pulls/${this._pullNumber}")
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                403 -> Response.Forbidden(response.body())
                422 -> Response.UnprocessableEntity(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public suspend operator fun invoke(
              title: String? = null,
              body: String? = null,
              state: State? = null,
              base: String? = null,
              maintainerCanModify: Boolean? = null,
            ): Response {
              val response = __client.patch("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/pulls/${this._pullNumber}") {
                contentType(ContentType.Application.Json)
                setBody(Body(title = title, body = body, state = state, base = base, maintainerCanModify = maintainerCanModify))
              }
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                403 -> Response.Forbidden(response.body())
                422 -> Response.UnprocessableEntity(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            @Serializable
            public enum class State(
              public val `value`: String,
            ) {
              @SerialName("open")
              Open("open"),
              @SerialName("closed")
              Closed("closed"),
              ;
            }

            @OptIn(ExperimentalSerializationApi::class)
            @Serializable
            internal data class Body(
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val title: String? = null,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val body: String? = null,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val state: State? = null,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val base: String? = null,
              @SerialName("maintainer_can_modify")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val maintainerCanModify: Boolean? = null,
            )

            public sealed interface Response {
              public data class Ok(
                public val `value`: PullRequest,
              ) : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response

              public data class UnprocessableEntity(
                public val `value`: ValidationError,
              ) : Response
            }
          }

          public class Codespaces internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _pullNumber: Long,
          ) {
            public val post: Post = Post(__client, _owner, _repo, _pullNumber)

            public class Post internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _pullNumber: Long,
            ) {
              public suspend operator fun invoke(
                location: String? = null,
                geo: Geo? = null,
                clientIp: String? = null,
                machine: String? = null,
                devcontainerPath: String? = null,
                multiRepoPermissionsOptOut: Boolean? = null,
                workingDirectory: String? = null,
                idleTimeoutMinutes: Long? = null,
                displayName: String? = null,
                retentionPeriodMinutes: Long? = null,
              ): Response {
                val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/pulls/${this._pullNumber}/codespaces") {
                  contentType(ContentType.Application.Json)
                  setBody(Body(location = location, geo = geo, clientIp = clientIp, machine = machine, devcontainerPath = devcontainerPath, multiRepoPermissionsOptOut = multiRepoPermissionsOptOut, workingDirectory = workingDirectory, idleTimeoutMinutes = idleTimeoutMinutes, displayName = displayName, retentionPeriodMinutes = retentionPeriodMinutes))
                }
                return when (response.status.value) {
                  201 -> Response.Created(response.body())
                  202 -> Response.Accepted(response.body())
                  401 -> Response.Unauthorized(response.body())
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  503 -> response.body<Response.ServiceUnavailable>()
                  else -> throw ResponseException(response, "")
                }
              }

              @Serializable
              public enum class Geo {
                EuropeWest,
                SoutheastAsia,
                UsEast,
                UsWest,
              }

              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              internal data class Body(
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val location: String? = null,
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val geo: Geo? = null,
                @SerialName("client_ip")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val clientIp: String? = null,
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val machine: String? = null,
                @SerialName("devcontainer_path")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val devcontainerPath: String? = null,
                @SerialName("multi_repo_permissions_opt_out")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val multiRepoPermissionsOptOut: Boolean? = null,
                @SerialName("working_directory")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val workingDirectory: String? = null,
                @SerialName("idle_timeout_minutes")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val idleTimeoutMinutes: Long? = null,
                @SerialName("display_name")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val displayName: String? = null,
                @SerialName("retention_period_minutes")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val retentionPeriodMinutes: Long? = null,
              )

              public sealed interface Response {
                public data class Created(
                  public val `value`: Codespace,
                ) : Response

                public data class Accepted(
                  public val `value`: Codespace,
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

                @OptIn(ExperimentalSerializationApi::class)
                @Serializable
                public data class ServiceUnavailable(
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val code: String? = null,
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val message: String? = null,
                  @SerialName("documentation_url")
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val documentationUrl: String? = null,
                ) : Response
              }
            }
          }

          public class Comments internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _pullNumber: Long,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo, _pullNumber)

            public val post: Post = Post(__client, _owner, _repo, _pullNumber)

            public fun commentId(commentId: Long): CommentIdPath = CommentIdPath(__client, _owner, _repo, _pullNumber, commentId)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _pullNumber: Long,
            ) {
              public suspend operator fun invoke(
                sort: Sort? = Sort.Created,
                direction: Direction? = null,
                since: Instant? = null,
                perPage: Long? = 30L,
                page: Long? = 1L,
              ): List<PullRequestReviewComment> {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/pulls/${this._pullNumber}/comments") {
                  if (sort != null) {
                    this.url.encodedParameters.append("sort", sort.value.encodeURLParameter())
                  }
                  if (direction != null) {
                    this.url.encodedParameters.append("direction", direction.value.encodeURLParameter())
                  }
                  if (since != null) {
                    this.url.encodedParameters.append("since", since.toString().encodeURLParameter())
                  }
                  if (perPage != null) {
                    this.url.encodedParameters.append("per_page", perPage.toString())
                  }
                  if (page != null) {
                    this.url.encodedParameters.append("page", page.toString())
                  }
                }
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
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
            }

            public class Post internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _pullNumber: Long,
            ) {
              public suspend operator fun invoke(
                body: String,
                commitId: String,
                path: String,
                position: Long? = null,
                side: Side? = null,
                line: Long? = null,
                startLine: Long? = null,
                startSide: StartSide? = null,
                inReplyTo: Long? = null,
                subjectType: SubjectType? = null,
              ): Response {
                val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/pulls/${this._pullNumber}/comments") {
                  contentType(ContentType.Application.Json)
                  setBody(Body(body = body, commitId = commitId, path = path, position = position, side = side, line = line, startLine = startLine, startSide = startSide, inReplyTo = inReplyTo, subjectType = subjectType))
                }
                return when (response.status.value) {
                  201 -> Response.Created(response.body())
                  403 -> Response.Forbidden(response.body())
                  422 -> Response.UnprocessableEntity(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              @Serializable
              public enum class Side {
                LEFT,
                RIGHT,
              }

              @Serializable
              public enum class StartSide(
                public val `value`: String,
              ) {
                LEFT("LEFT"),
                RIGHT("RIGHT"),
                @SerialName("side")
                Side("side"),
                ;
              }

              @Serializable
              public enum class SubjectType(
                public val `value`: String,
              ) {
                @SerialName("line")
                Line("line"),
                @SerialName("file")
                File("file"),
                ;
              }

              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              internal data class Body(
                public val body: String,
                @SerialName("commit_id")
                public val commitId: String,
                public val path: String,
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val position: Long? = null,
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val side: Side? = null,
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val line: Long? = null,
                @SerialName("start_line")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val startLine: Long? = null,
                @SerialName("start_side")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val startSide: StartSide? = null,
                @SerialName("in_reply_to")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val inReplyTo: Long? = null,
                @SerialName("subject_type")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val subjectType: SubjectType? = null,
              )

              public sealed interface Response {
                public data class Created(
                  public val `value`: PullRequestReviewComment,
                ) : Response

                public data class Forbidden(
                  public val `value`: BasicError,
                ) : Response

                public data class UnprocessableEntity(
                  public val `value`: ValidationError,
                ) : Response
              }
            }

            public class CommentIdPath internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _pullNumber: Long,
              private val _commentId: Long,
            ) {
              public val replies: Replies =
                  Replies(__client, _owner, _repo, _pullNumber, _commentId)

              public class Replies internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _pullNumber: Long,
                private val _commentId: Long,
              ) {
                public val post: Post = Post(__client, _owner, _repo, _pullNumber, _commentId)

                public class Post internal constructor(
                  private val __client: HttpClient,
                  private val _owner: String,
                  private val _repo: String,
                  private val _pullNumber: Long,
                  private val _commentId: Long,
                ) {
                  public suspend operator fun invoke(body: String): Response {
                    val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/pulls/${this._pullNumber}/comments/${this._commentId}/replies") {
                      contentType(ContentType.Application.Json)
                      setBody(Body(body = body))
                    }
                    return when (response.status.value) {
                      201 -> Response.Created(response.body())
                      404 -> Response.NotFound(response.body())
                      else -> throw ResponseException(response, "")
                    }
                  }

                  @Serializable
                  internal data class Body(
                    public val body: String,
                  )

                  public sealed interface Response {
                    public data class Created(
                      public val `value`: PullRequestReviewComment,
                    ) : Response

                    public data class NotFound(
                      public val `value`: BasicError,
                    ) : Response
                  }
                }
              }
            }
          }

          public class Commits internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _pullNumber: Long,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo, _pullNumber)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _pullNumber: Long,
            ) {
              public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<Commit> {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/pulls/${this._pullNumber}/commits") {
                  if (perPage != null) {
                    this.url.encodedParameters.append("per_page", perPage.toString())
                  }
                  if (page != null) {
                    this.url.encodedParameters.append("page", page.toString())
                  }
                }
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }
            }
          }

          public class Files internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _pullNumber: Long,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo, _pullNumber)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _pullNumber: Long,
            ) {
              public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/pulls/${this._pullNumber}/files") {
                  if (perPage != null) {
                    this.url.encodedParameters.append("per_page", perPage.toString())
                  }
                  if (page != null) {
                    this.url.encodedParameters.append("page", page.toString())
                  }
                }
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  422 -> Response.UnprocessableEntity(response.body())
                  500 -> Response.InternalServerError(response.body())
                  503 -> response.body<Response.ServiceUnavailable>()
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: List<DiffEntry>,
                ) : Response

                public data class UnprocessableEntity(
                  public val `value`: ValidationError,
                ) : Response

                public data class InternalServerError(
                  public val `value`: BasicError,
                ) : Response

                @OptIn(ExperimentalSerializationApi::class)
                @Serializable
                public data class ServiceUnavailable(
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val code: String? = null,
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val message: String? = null,
                  @SerialName("documentation_url")
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val documentationUrl: String? = null,
                ) : Response
              }
            }
          }

          public class Merge internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _pullNumber: Long,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo, _pullNumber)

            public val put: Put = Put(__client, _owner, _repo, _pullNumber)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _pullNumber: Long,
            ) {
              public suspend operator fun invoke(): Response {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/pulls/${this._pullNumber}/merge")
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
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _pullNumber: Long,
            ) {
              public suspend operator fun invoke(): Response {
                val response = __client.put("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/pulls/${this._pullNumber}/merge")
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  405 -> response.body<Response.MethodNotAllowed>()
                  409 -> response.body<Response.Conflict>()
                  422 -> Response.UnprocessableEntity(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public suspend operator fun invoke(
                commitTitle: String? = null,
                commitMessage: String? = null,
                sha: String? = null,
                mergeMethod: MergeMethod? = null,
              ): Response {
                val response = __client.put("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/pulls/${this._pullNumber}/merge") {
                  contentType(ContentType.Application.Json)
                  setBody(Body(commitTitle = commitTitle, commitMessage = commitMessage, sha = sha, mergeMethod = mergeMethod))
                }
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  405 -> response.body<Response.MethodNotAllowed>()
                  409 -> response.body<Response.Conflict>()
                  422 -> Response.UnprocessableEntity(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              @Serializable
              public enum class MergeMethod(
                public val `value`: String,
              ) {
                @SerialName("merge")
                Merge("merge"),
                @SerialName("squash")
                Squash("squash"),
                @SerialName("rebase")
                Rebase("rebase"),
                ;
              }

              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              internal data class Body(
                @SerialName("commit_title")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val commitTitle: String? = null,
                @SerialName("commit_message")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val commitMessage: String? = null,
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val sha: String? = null,
                @SerialName("merge_method")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val mergeMethod: MergeMethod? = null,
              )

              public sealed interface Response {
                public data class Ok(
                  public val `value`: PullRequestMergeResult,
                ) : Response

                public data class Forbidden(
                  public val `value`: BasicError,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response

                @OptIn(ExperimentalSerializationApi::class)
                @Serializable
                public data class MethodNotAllowed(
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val message: String? = null,
                  @SerialName("documentation_url")
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val documentationUrl: String? = null,
                ) : Response

                @OptIn(ExperimentalSerializationApi::class)
                @Serializable
                public data class Conflict(
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val message: String? = null,
                  @SerialName("documentation_url")
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val documentationUrl: String? = null,
                ) : Response

                public data class UnprocessableEntity(
                  public val `value`: ValidationError,
                ) : Response
              }
            }
          }

          public class RequestedReviewers internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _pullNumber: Long,
          ) {
            public val delete: Delete = Delete(__client, _owner, _repo, _pullNumber)

            public val `get`: Get = Get(__client, _owner, _repo, _pullNumber)

            public val post: Post = Post(__client, _owner, _repo, _pullNumber)

            public class Delete internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _pullNumber: Long,
            ) {
              public suspend operator fun invoke(reviewers: List<String>, teamReviewers: List<String>? = null): Response {
                val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/pulls/${this._pullNumber}/requested_reviewers") {
                  contentType(ContentType.Application.Json)
                  setBody(Body(reviewers = reviewers, teamReviewers = teamReviewers))
                }
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  422 -> Response.UnprocessableEntity(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              internal data class Body(
                public val reviewers: List<String>,
                @SerialName("team_reviewers")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val teamReviewers: List<String>? = null,
              )

              public sealed interface Response {
                public data class Ok(
                  public val `value`: PullRequestSimple,
                ) : Response

                public data class UnprocessableEntity(
                  public val `value`: ValidationError,
                ) : Response
              }
            }

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _pullNumber: Long,
            ) {
              public suspend operator fun invoke(): PullRequestReviewRequest {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/pulls/${this._pullNumber}/requested_reviewers")
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }
            }

            public class Post internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _pullNumber: Long,
            ) {
              public suspend operator fun invoke(): Response {
                val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/pulls/${this._pullNumber}/requested_reviewers")
                return when (response.status.value) {
                  201 -> Response.Created(response.body())
                  403 -> Response.Forbidden(response.body())
                  422 -> Response.UnprocessableEntity
                  else -> throw ResponseException(response, "")
                }
              }

              public suspend operator fun invoke(reviewers: List<String>? = null, teamReviewers: List<String>? = null): Response {
                val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/pulls/${this._pullNumber}/requested_reviewers") {
                  contentType(ContentType.Application.Json)
                  setBody(Body(reviewers = reviewers, teamReviewers = teamReviewers))
                }
                return when (response.status.value) {
                  201 -> Response.Created(response.body())
                  403 -> Response.Forbidden(response.body())
                  422 -> Response.UnprocessableEntity
                  else -> throw ResponseException(response, "")
                }
              }

              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              internal data class Body(
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val reviewers: List<String>? = null,
                @SerialName("team_reviewers")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val teamReviewers: List<String>? = null,
              )

              public sealed interface Response {
                public data class Created(
                  public val `value`: PullRequestSimple,
                ) : Response

                public data class Forbidden(
                  public val `value`: BasicError,
                ) : Response

                public data object UnprocessableEntity : Response
              }
            }
          }

          public class Reviews internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _pullNumber: Long,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo, _pullNumber)

            public val post: Post = Post(__client, _owner, _repo, _pullNumber)

            public fun reviewId(reviewId: Long): ReviewIdPath = ReviewIdPath(__client, _owner, _repo, _pullNumber, reviewId)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _pullNumber: Long,
            ) {
              public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<PullRequestReview> {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/pulls/${this._pullNumber}/reviews") {
                  if (perPage != null) {
                    this.url.encodedParameters.append("per_page", perPage.toString())
                  }
                  if (page != null) {
                    this.url.encodedParameters.append("page", page.toString())
                  }
                }
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }
            }

            public class Post internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _pullNumber: Long,
            ) {
              public suspend operator fun invoke(): Response {
                val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/pulls/${this._pullNumber}/reviews")
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  403 -> Response.Forbidden(response.body())
                  422 -> Response.UnprocessableEntity(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public suspend operator fun invoke(
                commitId: String? = null,
                body: String? = null,
                event: Event? = null,
                comments: List<Comments>? = null,
              ): Response {
                val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/pulls/${this._pullNumber}/reviews") {
                  contentType(ContentType.Application.Json)
                  setBody(Body(commitId = commitId, body = body, event = event, comments = comments))
                }
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  403 -> Response.Forbidden(response.body())
                  422 -> Response.UnprocessableEntity(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              @Serializable
              public enum class Event(
                public val `value`: String,
              ) {
                APPROVE("APPROVE"),
                @SerialName("REQUEST_CHANGES")
                REQUESTCHANGES("REQUEST_CHANGES"),
                COMMENT("COMMENT"),
                ;
              }

              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              public data class Comments(
                public val path: String,
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val position: Long? = null,
                public val body: String,
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val line: Long? = null,
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val side: String? = null,
                @SerialName("start_line")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val startLine: Long? = null,
                @SerialName("start_side")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val startSide: String? = null,
              )

              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              internal data class Body(
                @SerialName("commit_id")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val commitId: String? = null,
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val body: String? = null,
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val event: Event? = null,
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val comments: List<Comments>? = null,
              )

              public sealed interface Response {
                public data class Ok(
                  public val `value`: PullRequestReview,
                ) : Response

                public data class Forbidden(
                  public val `value`: BasicError,
                ) : Response

                public data class UnprocessableEntity(
                  public val `value`: ValidationErrorSimple,
                ) : Response
              }
            }

            public class ReviewIdPath internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _pullNumber: Long,
              private val _reviewId: Long,
            ) {
              public val delete: Delete = Delete(__client, _owner, _repo, _pullNumber, _reviewId)

              public val `get`: Get = Get(__client, _owner, _repo, _pullNumber, _reviewId)

              public val put: Put = Put(__client, _owner, _repo, _pullNumber, _reviewId)

              public val comments: Comments =
                  Comments(__client, _owner, _repo, _pullNumber, _reviewId)

              public val dismissals: Dismissals =
                  Dismissals(__client, _owner, _repo, _pullNumber, _reviewId)

              public val events: Events = Events(__client, _owner, _repo, _pullNumber, _reviewId)

              public class Delete internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _pullNumber: Long,
                private val _reviewId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/pulls/${this._pullNumber}/reviews/${this._reviewId}")
                  return when (response.status.value) {
                    200 -> Response.Ok(response.body())
                    404 -> Response.NotFound(response.body())
                    422 -> Response.UnprocessableEntity(response.body())
                    else -> throw ResponseException(response, "")
                  }
                }

                public sealed interface Response {
                  public data class Ok(
                    public val `value`: PullRequestReview,
                  ) : Response

                  public data class NotFound(
                    public val `value`: BasicError,
                  ) : Response

                  public data class UnprocessableEntity(
                    public val `value`: ValidationErrorSimple,
                  ) : Response
                }
              }

              public class Get internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _pullNumber: Long,
                private val _reviewId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/pulls/${this._pullNumber}/reviews/${this._reviewId}")
                  return when (response.status.value) {
                    200 -> Response.Ok(response.body())
                    404 -> Response.NotFound(response.body())
                    else -> throw ResponseException(response, "")
                  }
                }

                public sealed interface Response {
                  public data class Ok(
                    public val `value`: PullRequestReview,
                  ) : Response

                  public data class NotFound(
                    public val `value`: BasicError,
                  ) : Response
                }
              }

              public class Put internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _pullNumber: Long,
                private val _reviewId: Long,
              ) {
                public suspend operator fun invoke(body: String): Response {
                  val response = __client.put("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/pulls/${this._pullNumber}/reviews/${this._reviewId}") {
                    contentType(ContentType.Application.Json)
                    setBody(Body(body = body))
                  }
                  return when (response.status.value) {
                    200 -> Response.Ok(response.body())
                    422 -> Response.UnprocessableEntity(response.body())
                    else -> throw ResponseException(response, "")
                  }
                }

                @Serializable
                internal data class Body(
                  public val body: String,
                )

                public sealed interface Response {
                  public data class Ok(
                    public val `value`: PullRequestReview,
                  ) : Response

                  public data class UnprocessableEntity(
                    public val `value`: ValidationErrorSimple,
                  ) : Response
                }
              }

              public class Comments internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _pullNumber: Long,
                private val _reviewId: Long,
              ) {
                public val `get`: Get = Get(__client, _owner, _repo, _pullNumber, _reviewId)

                public class Get internal constructor(
                  private val __client: HttpClient,
                  private val _owner: String,
                  private val _repo: String,
                  private val _pullNumber: Long,
                  private val _reviewId: Long,
                ) {
                  public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
                    val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/pulls/${this._pullNumber}/reviews/${this._reviewId}/comments") {
                      if (perPage != null) {
                        this.url.encodedParameters.append("per_page", perPage.toString())
                      }
                      if (page != null) {
                        this.url.encodedParameters.append("page", page.toString())
                      }
                    }
                    return when (response.status.value) {
                      200 -> Response.Ok(response.body())
                      404 -> Response.NotFound(response.body())
                      else -> throw ResponseException(response, "")
                    }
                  }

                  public sealed interface Response {
                    public data class Ok(
                      public val `value`: List<ReviewComment>,
                    ) : Response

                    public data class NotFound(
                      public val `value`: BasicError,
                    ) : Response
                  }
                }
              }

              public class Dismissals internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _pullNumber: Long,
                private val _reviewId: Long,
              ) {
                public val put: Put = Put(__client, _owner, _repo, _pullNumber, _reviewId)

                public class Put internal constructor(
                  private val __client: HttpClient,
                  private val _owner: String,
                  private val _repo: String,
                  private val _pullNumber: Long,
                  private val _reviewId: Long,
                ) {
                  public suspend operator fun invoke(message: String, event: Event? = null): Response {
                    val response = __client.put("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/pulls/${this._pullNumber}/reviews/${this._reviewId}/dismissals") {
                      contentType(ContentType.Application.Json)
                      setBody(Body(message = message, event = event))
                    }
                    return when (response.status.value) {
                      200 -> Response.Ok(response.body())
                      404 -> Response.NotFound(response.body())
                      422 -> Response.UnprocessableEntity(response.body())
                      else -> throw ResponseException(response, "")
                    }
                  }

                  @Serializable
                  public enum class Event {
                    DISMISS,
                  }

                  @OptIn(ExperimentalSerializationApi::class)
                  @Serializable
                  internal data class Body(
                    public val message: String,
                    @EncodeDefault(EncodeDefault.Mode.NEVER)
                    public val event: Event? = null,
                  )

                  public sealed interface Response {
                    public data class Ok(
                      public val `value`: PullRequestReview,
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

              public class Events internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _pullNumber: Long,
                private val _reviewId: Long,
              ) {
                public val post: Post = Post(__client, _owner, _repo, _pullNumber, _reviewId)

                public class Post internal constructor(
                  private val __client: HttpClient,
                  private val _owner: String,
                  private val _repo: String,
                  private val _pullNumber: Long,
                  private val _reviewId: Long,
                ) {
                  public suspend operator fun invoke(body: String? = null, event: Event): Response {
                    val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/pulls/${this._pullNumber}/reviews/${this._reviewId}/events") {
                      contentType(ContentType.Application.Json)
                      setBody(Body(body = body, event = event))
                    }
                    return when (response.status.value) {
                      200 -> Response.Ok(response.body())
                      403 -> Response.Forbidden(response.body())
                      404 -> Response.NotFound(response.body())
                      422 -> Response.UnprocessableEntity(response.body())
                      else -> throw ResponseException(response, "")
                    }
                  }

                  @Serializable
                  public enum class Event(
                    public val `value`: String,
                  ) {
                    APPROVE("APPROVE"),
                    @SerialName("REQUEST_CHANGES")
                    REQUESTCHANGES("REQUEST_CHANGES"),
                    COMMENT("COMMENT"),
                    ;
                  }

                  @OptIn(ExperimentalSerializationApi::class)
                  @Serializable
                  internal data class Body(
                    @EncodeDefault(EncodeDefault.Mode.NEVER)
                    public val body: String? = null,
                    public val event: Event,
                  )

                  public sealed interface Response {
                    public data class Ok(
                      public val `value`: PullRequestReview,
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
            }
          }

          public class UpdateBranch internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _pullNumber: Long,
          ) {
            public val put: Put = Put(__client, _owner, _repo, _pullNumber)

            public class Put internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _pullNumber: Long,
            ) {
              public suspend operator fun invoke(): Response {
                val response = __client.put("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/pulls/${this._pullNumber}/update-branch")
                return when (response.status.value) {
                  202 -> response.body<Response.Accepted>()
                  403 -> Response.Forbidden(response.body())
                  422 -> Response.UnprocessableEntity(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public suspend operator fun invoke(expectedHeadSha: String? = null): Response {
                val response = __client.put("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/pulls/${this._pullNumber}/update-branch") {
                  contentType(ContentType.Application.Json)
                  setBody(Body(expectedHeadSha = expectedHeadSha))
                }
                return when (response.status.value) {
                  202 -> response.body<Response.Accepted>()
                  403 -> Response.Forbidden(response.body())
                  422 -> Response.UnprocessableEntity(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              internal data class Body(
                @SerialName("expected_head_sha")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val expectedHeadSha: String? = null,
              )

              public sealed interface Response {
                @OptIn(ExperimentalSerializationApi::class)
                @Serializable
                public data class Accepted(
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val message: String? = null,
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val url: String? = null,
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
      }

      public class Readme internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public val `get`: Get = Get(__client, _owner, _repo)

        public fun dir(dir: String): DirPath = DirPath(__client, _owner, _repo, dir)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(ref: String? = null): Response {
            val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/readme") {
              if (ref != null) {
                this.url.encodedParameters.append("ref", ref.encodeURLParameter())
              }
            }
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              304 -> Response.NotModified
              404 -> Response.NotFound(response.body())
              422 -> Response.UnprocessableEntity(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: ContentFile,
            ) : Response

            public data object NotModified : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response

            public data class UnprocessableEntity(
              public val `value`: ValidationError,
            ) : Response
          }
        }

        public class DirPath internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
          private val _dir: String,
        ) {
          public val `get`: Get = Get(__client, _owner, _repo, _dir)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _dir: String,
          ) {
            public suspend operator fun invoke(ref: String? = null): Response {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/readme/${this._dir.encodeURLParameter()}") {
                if (ref != null) {
                  this.url.encodedParameters.append("ref", ref.encodeURLParameter())
                }
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
                public val `value`: ContentFile,
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

      public class Releases internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public val `get`: Get = Get(__client, _owner, _repo)

        public val post: Post = Post(__client, _owner, _repo)

        public val assets: Assets = Assets(__client, _owner, _repo)

        public val generateNotes: GenerateNotes = GenerateNotes(__client, _owner, _repo)

        public val latest: Latest = Latest(__client, _owner, _repo)

        public val tags: Tags = Tags(__client, _owner, _repo)

        public fun releaseId(releaseId: Long): ReleaseIdPath = ReleaseIdPath(__client, _owner, _repo, releaseId)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
            val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/releases") {
              if (perPage != null) {
                this.url.encodedParameters.append("per_page", perPage.toString())
              }
              if (page != null) {
                this.url.encodedParameters.append("page", page.toString())
              }
            }
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              404 -> Response.NotFound(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: List<Release>,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class Post internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(
            tagName: String,
            targetCommitish: String? = null,
            name: String? = null,
            body: String? = null,
            draft: Boolean? = null,
            prerelease: Boolean? = null,
            discussionCategoryName: String? = null,
            generateReleaseNotes: Boolean? = null,
            makeLatest: MakeLatest? = null,
          ): Response {
            val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/releases") {
              contentType(ContentType.Application.Json)
              setBody(Body(tagName = tagName, targetCommitish = targetCommitish, name = name, body = body, draft = draft, prerelease = prerelease, discussionCategoryName = discussionCategoryName, generateReleaseNotes = generateReleaseNotes, makeLatest = makeLatest))
            }
            return when (response.status.value) {
              201 -> Response.Created(response.body())
              404 -> Response.NotFound(response.body())
              422 -> Response.UnprocessableEntity(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          @Serializable
          public enum class MakeLatest(
            public val `value`: String,
          ) {
            @SerialName("true")
            True("true"),
            @SerialName("false")
            False("false"),
            @SerialName("legacy")
            Legacy("legacy"),
            ;
          }

          @OptIn(ExperimentalSerializationApi::class)
          @Serializable
          internal data class Body(
            @SerialName("tag_name")
            public val tagName: String,
            @SerialName("target_commitish")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val targetCommitish: String? = null,
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val name: String? = null,
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val body: String? = null,
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val draft: Boolean? = null,
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val prerelease: Boolean? = null,
            @SerialName("discussion_category_name")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val discussionCategoryName: String? = null,
            @SerialName("generate_release_notes")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val generateReleaseNotes: Boolean? = null,
            @SerialName("make_latest")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val makeLatest: MakeLatest? = null,
          )

          public sealed interface Response {
            public data class Created(
              public val `value`: Release,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response

            public data class UnprocessableEntity(
              public val `value`: ValidationError,
            ) : Response
          }
        }

        public class Assets internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public fun assetId(assetId: Long): AssetIdPath = AssetIdPath(__client, _owner, _repo, assetId)

          public class AssetIdPath internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _assetId: Long,
          ) {
            public val delete: Delete = Delete(__client, _owner, _repo, _assetId)

            public val `get`: Get = Get(__client, _owner, _repo, _assetId)

            public val patch: Patch = Patch(__client, _owner, _repo, _assetId)

            public class Delete internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _assetId: Long,
            ) {
              public suspend operator fun invoke() {
                val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/releases/assets/${this._assetId}")
                if (response.status.value != 204) throw ResponseException(response, "")
              }
            }

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _assetId: Long,
            ) {
              public suspend operator fun invoke(): Response {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/releases/assets/${this._assetId}")
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  302 -> Response.Found
                  404 -> Response.NotFound(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: ReleaseAsset,
                ) : Response

                public data object Found : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response
              }
            }

            public class Patch internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _assetId: Long,
            ) {
              public suspend operator fun invoke(): ReleaseAsset {
                val response = __client.patch("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/releases/assets/${this._assetId}")
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }

              public suspend operator fun invoke(
                name: String? = null,
                label: String? = null,
                state: String? = null,
              ): ReleaseAsset {
                val response = __client.patch("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/releases/assets/${this._assetId}") {
                  contentType(ContentType.Application.Json)
                  setBody(Body(name = name, label = label, state = state))
                }
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }

              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              internal data class Body(
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val name: String? = null,
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val label: String? = null,
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val state: String? = null,
              )
            }
          }
        }

        public class GenerateNotes internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public val post: Post = Post(__client, _owner, _repo)

          public class Post internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public suspend operator fun invoke(
              tagName: String,
              targetCommitish: String? = null,
              previousTagName: String? = null,
              configurationFilePath: String? = null,
            ): Response {
              val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/releases/generate-notes") {
                contentType(ContentType.Application.Json)
                setBody(Body(tagName = tagName, targetCommitish = targetCommitish, previousTagName = previousTagName, configurationFilePath = configurationFilePath))
              }
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            @OptIn(ExperimentalSerializationApi::class)
            @Serializable
            internal data class Body(
              @SerialName("tag_name")
              public val tagName: String,
              @SerialName("target_commitish")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val targetCommitish: String? = null,
              @SerialName("previous_tag_name")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val previousTagName: String? = null,
              @SerialName("configuration_file_path")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val configurationFilePath: String? = null,
            )

            public sealed interface Response {
              public data class Ok(
                public val `value`: ReleaseNotesContent,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response
            }
          }
        }

        public class Latest internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public val `get`: Get = Get(__client, _owner, _repo)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public suspend operator fun invoke(): Release {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/releases/latest")
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }
          }
        }

        public class Tags internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public fun tag(tag: String): TagPath = TagPath(__client, _owner, _repo, tag)

          public class TagPath internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _tag: String,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo, _tag)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _tag: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/releases/tags/${this._tag.encodeURLParameter()}")
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  404 -> Response.NotFound(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: Release,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response
              }
            }
          }
        }

        public class ReleaseIdPath internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
          private val _releaseId: Long,
        ) {
          public val delete: Delete = Delete(__client, _owner, _repo, _releaseId)

          public val `get`: Get = Get(__client, _owner, _repo, _releaseId)

          public val patch: Patch = Patch(__client, _owner, _repo, _releaseId)

          public val assets: Assets = Assets(__client, _owner, _repo, _releaseId)

          public val reactions: Reactions = Reactions(__client, _owner, _repo, _releaseId)

          public class Delete internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _releaseId: Long,
          ) {
            public suspend operator fun invoke() {
              val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/releases/${this._releaseId}")
              if (response.status.value != 204) throw ResponseException(response, "")
            }
          }

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _releaseId: Long,
          ) {
            public suspend operator fun invoke(): Response {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/releases/${this._releaseId}")
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                401 -> Response.Unauthorized
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: Release,
              ) : Response

              public data object Unauthorized : Response
            }
          }

          public class Patch internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _releaseId: Long,
          ) {
            public suspend operator fun invoke(): Response {
              val response = __client.patch("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/releases/${this._releaseId}")
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public suspend operator fun invoke(
              tagName: String? = null,
              targetCommitish: String? = null,
              name: String? = null,
              body: String? = null,
              draft: Boolean? = null,
              prerelease: Boolean? = null,
              makeLatest: MakeLatest? = null,
              discussionCategoryName: String? = null,
            ): Response {
              val response = __client.patch("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/releases/${this._releaseId}") {
                contentType(ContentType.Application.Json)
                setBody(Body(tagName = tagName, targetCommitish = targetCommitish, name = name, body = body, draft = draft, prerelease = prerelease, makeLatest = makeLatest, discussionCategoryName = discussionCategoryName))
              }
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            @Serializable
            public enum class MakeLatest(
              public val `value`: String,
            ) {
              @SerialName("true")
              True("true"),
              @SerialName("false")
              False("false"),
              @SerialName("legacy")
              Legacy("legacy"),
              ;
            }

            @OptIn(ExperimentalSerializationApi::class)
            @Serializable
            internal data class Body(
              @SerialName("tag_name")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val tagName: String? = null,
              @SerialName("target_commitish")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val targetCommitish: String? = null,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val name: String? = null,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val body: String? = null,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val draft: Boolean? = null,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val prerelease: Boolean? = null,
              @SerialName("make_latest")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val makeLatest: MakeLatest? = null,
              @SerialName("discussion_category_name")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val discussionCategoryName: String? = null,
            )

            public sealed interface Response {
              public data class Ok(
                public val `value`: Release,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response
            }
          }

          public class Assets internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _releaseId: Long,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo, _releaseId)

            public val post: Post = Post(__client, _owner, _repo, _releaseId)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _releaseId: Long,
            ) {
              public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<ReleaseAsset> {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/releases/${this._releaseId}/assets") {
                  if (perPage != null) {
                    this.url.encodedParameters.append("per_page", perPage.toString())
                  }
                  if (page != null) {
                    this.url.encodedParameters.append("page", page.toString())
                  }
                }
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }
            }

            public class Post internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _releaseId: Long,
            ) {
              public suspend operator fun invoke(
                name: String,
                label: String? = null,
                body: ByteArray? = null,
              ): Response {
                val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/releases/${this._releaseId}/assets") {
                  this.url.encodedParameters.append("name", name.encodeURLParameter())
                  if (label != null) {
                    this.url.encodedParameters.append("label", label.encodeURLParameter())
                  }
                  body?.let {
                    contentType(ContentType.Application.OctetStream)
                    setBody(it)
                  }
                }
                return when (response.status.value) {
                  201 -> Response.Created(response.body())
                  422 -> Response.UnprocessableEntity
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data class Created(
                  public val `value`: ReleaseAsset,
                ) : Response

                public data object UnprocessableEntity : Response
              }
            }
          }

          public class Reactions internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _releaseId: Long,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo, _releaseId)

            public val post: Post = Post(__client, _owner, _repo, _releaseId)

            public fun reactionId(reactionId: Long): ReactionIdPath = ReactionIdPath(__client, _owner, _repo, _releaseId, reactionId)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _releaseId: Long,
            ) {
              public suspend operator fun invoke(
                content: Content? = null,
                perPage: Long? = 30L,
                page: Long? = 1L,
              ): Response {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/releases/${this._releaseId}/reactions") {
                  if (content != null) {
                    this.url.encodedParameters.append("content", content.value.encodeURLParameter())
                  }
                  if (perPage != null) {
                    this.url.encodedParameters.append("per_page", perPage.toString())
                  }
                  if (page != null) {
                    this.url.encodedParameters.append("page", page.toString())
                  }
                }
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  404 -> Response.NotFound(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              @Serializable
              public enum class Content(
                public val `value`: String,
              ) {
                `+1`("+1"),
                @SerialName("laugh")
                Laugh("laugh"),
                @SerialName("heart")
                Heart("heart"),
                @SerialName("hooray")
                Hooray("hooray"),
                @SerialName("rocket")
                Rocket("rocket"),
                @SerialName("eyes")
                Eyes("eyes"),
                ;
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: List<Reaction>,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response
              }
            }

            public class Post internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _releaseId: Long,
            ) {
              public suspend operator fun invoke(content: Content): Response {
                val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/releases/${this._releaseId}/reactions") {
                  contentType(ContentType.Application.Json)
                  setBody(Body(content = content))
                }
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  201 -> Response.Created(response.body())
                  422 -> Response.UnprocessableEntity(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              @Serializable
              public enum class Content(
                public val `value`: String,
              ) {
                `+1`("+1"),
                @SerialName("laugh")
                Laugh("laugh"),
                @SerialName("heart")
                Heart("heart"),
                @SerialName("hooray")
                Hooray("hooray"),
                @SerialName("rocket")
                Rocket("rocket"),
                @SerialName("eyes")
                Eyes("eyes"),
                ;
              }

              @Serializable
              internal data class Body(
                public val content: Content,
              )

              public sealed interface Response {
                public data class Ok(
                  public val `value`: Reaction,
                ) : Response

                public data class Created(
                  public val `value`: Reaction,
                ) : Response

                public data class UnprocessableEntity(
                  public val `value`: ValidationError,
                ) : Response
              }
            }

            public class ReactionIdPath internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _releaseId: Long,
              private val _reactionId: Long,
            ) {
              public val delete: Delete = Delete(__client, _owner, _repo, _releaseId, _reactionId)

              public class Delete internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _releaseId: Long,
                private val _reactionId: Long,
              ) {
                public suspend operator fun invoke() {
                  val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/releases/${this._releaseId}/reactions/${this._reactionId}")
                  if (response.status.value != 204) throw ResponseException(response, "")
                }
              }
            }
          }
        }
      }

      public class Rules internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public val branches: Branches = Branches(__client, _owner, _repo)

        public class Branches internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public fun branch(branch: String): BranchPath = BranchPath(__client, _owner, _repo, branch)

          public class BranchPath internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _branch: String,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo, _branch)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _branch: String,
            ) {
              public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<RepositoryRuleDetailed> {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/rules/branches/${this._branch.encodeURLParameter()}") {
                  if (perPage != null) {
                    this.url.encodedParameters.append("per_page", perPage.toString())
                  }
                  if (page != null) {
                    this.url.encodedParameters.append("page", page.toString())
                  }
                }
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }
            }
          }
        }
      }

      public class Rulesets internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public val `get`: Get = Get(__client, _owner, _repo)

        public val post: Post = Post(__client, _owner, _repo)

        public val ruleSuites: RuleSuites = RuleSuites(__client, _owner, _repo)

        public fun rulesetId(rulesetId: Long): RulesetIdPath = RulesetIdPath(__client, _owner, _repo, rulesetId)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(
            perPage: Long? = 30L,
            page: Long? = 1L,
            includesParents: Boolean? = true,
            targets: String? = null,
          ): Response {
            val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/rulesets") {
              if (perPage != null) {
                this.url.encodedParameters.append("per_page", perPage.toString())
              }
              if (page != null) {
                this.url.encodedParameters.append("page", page.toString())
              }
              if (includesParents != null) {
                this.url.encodedParameters.append("includes_parents", includesParents.toString())
              }
              if (targets != null) {
                this.url.encodedParameters.append("targets", targets.encodeURLParameter())
              }
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
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(
            name: String,
            target: Target? = null,
            enforcement: RepositoryRuleEnforcement,
            bypassActors: List<RepositoryRulesetBypassActor>? = null,
            conditions: RepositoryRulesetConditions? = null,
            rules: List<RepositoryRule>? = null,
          ): Response {
            val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/rulesets") {
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
            ;
          }

          @OptIn(ExperimentalSerializationApi::class)
          @Serializable
          internal data class Body(
            public val name: String,
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val target: Target? = null,
            public val enforcement: RepositoryRuleEnforcement,
            @SerialName("bypass_actors")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val bypassActors: List<RepositoryRulesetBypassActor>? = null,
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val conditions: RepositoryRulesetConditions? = null,
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val rules: List<RepositoryRule>? = null,
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
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public val `get`: Get = Get(__client, _owner, _repo)

          public fun ruleSuiteId(ruleSuiteId: Long): RuleSuiteIdPath = RuleSuiteIdPath(__client, _owner, _repo, ruleSuiteId)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public suspend operator fun invoke(
              ref: String? = null,
              timePeriod: TimePeriod? = TimePeriod.Day,
              actorName: String? = null,
              ruleSuiteResult: RuleSuiteResult? = RuleSuiteResult.All,
              perPage: Long? = 30L,
              page: Long? = 1L,
            ): Response {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/rulesets/rule-suites") {
                if (ref != null) {
                  this.url.encodedParameters.append("ref", ref.encodeURLParameter())
                }
                if (timePeriod != null) {
                  this.url.encodedParameters.append("time_period", timePeriod.value.encodeURLParameter())
                }
                if (actorName != null) {
                  this.url.encodedParameters.append("actor_name", actorName.encodeURLParameter())
                }
                if (ruleSuiteResult != null) {
                  this.url.encodedParameters.append("rule_suite_result", ruleSuiteResult.value.encodeURLParameter())
                }
                if (perPage != null) {
                  this.url.encodedParameters.append("per_page", perPage.toString())
                }
                if (page != null) {
                  this.url.encodedParameters.append("page", page.toString())
                }
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
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _ruleSuiteId: Long,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo, _ruleSuiteId)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _ruleSuiteId: Long,
            ) {
              public suspend operator fun invoke(): Response {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/rulesets/rule-suites/${this._ruleSuiteId}")
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
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
          private val _rulesetId: Long,
        ) {
          public val delete: Delete = Delete(__client, _owner, _repo, _rulesetId)

          public val `get`: Get = Get(__client, _owner, _repo, _rulesetId)

          public val put: Put = Put(__client, _owner, _repo, _rulesetId)

          public val history: History = History(__client, _owner, _repo, _rulesetId)

          public class Delete internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _rulesetId: Long,
          ) {
            public suspend operator fun invoke(): Response {
              val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/rulesets/${this._rulesetId}")
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
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _rulesetId: Long,
          ) {
            public suspend operator fun invoke(includesParents: Boolean? = true): Response {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/rulesets/${this._rulesetId}") {
                if (includesParents != null) {
                  this.url.encodedParameters.append("includes_parents", includesParents.toString())
                }
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
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _rulesetId: Long,
          ) {
            public suspend operator fun invoke(): Response {
              val response = __client.put("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/rulesets/${this._rulesetId}")
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                404 -> Response.NotFound(response.body())
                422 -> Response.UnprocessableEntity(response.body())
                500 -> Response.InternalServerError(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public suspend operator fun invoke(
              name: String? = null,
              target: Target? = null,
              enforcement: RepositoryRuleEnforcement? = null,
              bypassActors: List<RepositoryRulesetBypassActor>? = null,
              conditions: RepositoryRulesetConditions? = null,
              rules: List<RepositoryRule>? = null,
            ): Response {
              val response = __client.put("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/rulesets/${this._rulesetId}") {
                contentType(ContentType.Application.Json)
                setBody(Body(name = name, target = target, enforcement = enforcement, bypassActors = bypassActors, conditions = conditions, rules = rules))
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
              ;
            }

            @OptIn(ExperimentalSerializationApi::class)
            @Serializable
            internal data class Body(
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val name: String? = null,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val target: Target? = null,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val enforcement: RepositoryRuleEnforcement? = null,
              @SerialName("bypass_actors")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val bypassActors: List<RepositoryRulesetBypassActor>? = null,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val conditions: RepositoryRulesetConditions? = null,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val rules: List<RepositoryRule>? = null,
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
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _rulesetId: Long,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo, _rulesetId)

            public fun versionId(versionId: Long): VersionIdPath = VersionIdPath(__client, _owner, _repo, _rulesetId, versionId)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _rulesetId: Long,
            ) {
              public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/rulesets/${this._rulesetId}/history") {
                  if (perPage != null) {
                    this.url.encodedParameters.append("per_page", perPage.toString())
                  }
                  if (page != null) {
                    this.url.encodedParameters.append("page", page.toString())
                  }
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
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _rulesetId: Long,
              private val _versionId: Long,
            ) {
              public val `get`: Get = Get(__client, _owner, _repo, _rulesetId, _versionId)

              public class Get internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _rulesetId: Long,
                private val _versionId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/rulesets/${this._rulesetId}/history/${this._versionId}")
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
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public val alerts: Alerts = Alerts(__client, _owner, _repo)

        public val pushProtectionBypasses: PushProtectionBypasses =
            PushProtectionBypasses(__client, _owner, _repo)

        public val scanHistory: ScanHistory = ScanHistory(__client, _owner, _repo)

        public class Alerts internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public val `get`: Get = Get(__client, _owner, _repo)

          public fun alertNumber(alertNumber: Long): AlertNumberPath = AlertNumberPath(__client, _owner, _repo, alertNumber)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
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
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/secret-scanning/alerts") {
                if (state != null) {
                  this.url.encodedParameters.append("state", state.value.encodeURLParameter())
                }
                if (secretType != null) {
                  this.url.encodedParameters.append("secret_type", secretType.encodeURLParameter())
                }
                if (resolution != null) {
                  this.url.encodedParameters.append("resolution", resolution.encodeURLParameter())
                }
                if (assignee != null) {
                  this.url.encodedParameters.append("assignee", assignee.encodeURLParameter())
                }
                if (sort != null) {
                  this.url.encodedParameters.append("sort", sort.value.encodeURLParameter())
                }
                if (direction != null) {
                  this.url.encodedParameters.append("direction", direction.value.encodeURLParameter())
                }
                if (page != null) {
                  this.url.encodedParameters.append("page", page.toString())
                }
                if (perPage != null) {
                  this.url.encodedParameters.append("per_page", perPage.toString())
                }
                if (before != null) {
                  this.url.encodedParameters.append("before", before.encodeURLParameter())
                }
                if (after != null) {
                  this.url.encodedParameters.append("after", after.encodeURLParameter())
                }
                if (validity != null) {
                  this.url.encodedParameters.append("validity", validity.encodeURLParameter())
                }
                if (isPubliclyLeaked != null) {
                  this.url.encodedParameters.append("is_publicly_leaked", isPubliclyLeaked.toString())
                }
                if (isMultiRepo != null) {
                  this.url.encodedParameters.append("is_multi_repo", isMultiRepo.toString())
                }
                if (hideSecret != null) {
                  this.url.encodedParameters.append("hide_secret", hideSecret.toString())
                }
              }
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                404 -> Response.NotFound
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
                public val `value`: List<SecretScanningAlert>,
              ) : Response

              public data object NotFound : Response

              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              public data class ServiceUnavailable(
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val code: String? = null,
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val message: String? = null,
                @SerialName("documentation_url")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val documentationUrl: String? = null,
              ) : Response
            }
          }

          public class AlertNumberPath internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _alertNumber: Long,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo, _alertNumber)

            public val patch: Patch = Patch(__client, _owner, _repo, _alertNumber)

            public val locations: Locations = Locations(__client, _owner, _repo, _alertNumber)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _alertNumber: Long,
            ) {
              public suspend operator fun invoke(hideSecret: Boolean? = false): Response {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/secret-scanning/alerts/${this._alertNumber}") {
                  if (hideSecret != null) {
                    this.url.encodedParameters.append("hide_secret", hideSecret.toString())
                  }
                }
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  304 -> Response.NotModified
                  404 -> Response.NotFound
                  503 -> response.body<Response.ServiceUnavailable>()
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: SecretScanningAlert,
                ) : Response

                public data object NotModified : Response

                public data object NotFound : Response

                @OptIn(ExperimentalSerializationApi::class)
                @Serializable
                public data class ServiceUnavailable(
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val code: String? = null,
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val message: String? = null,
                  @SerialName("documentation_url")
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val documentationUrl: String? = null,
                ) : Response
              }
            }

            public class Patch internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _alertNumber: Long,
            ) {
              public suspend operator fun invoke(
                state: SecretScanningAlertState? = null,
                resolution: Optional<SecretScanningAlertResolution> = Optional.Absent,
                resolutionComment: Optional<SecretScanningAlertResolutionComment> = Optional.Absent,
                assignee: Optional<SecretScanningAlertAssignee> = Optional.Absent,
              ): Response {
                val response = __client.patch("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/secret-scanning/alerts/${this._alertNumber}") {
                  contentType(ContentType.Application.Json)
                  setBody(Body(state = state, resolution = resolution, resolutionComment = resolutionComment, assignee = assignee))
                }
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  400 -> Response.BadRequest
                  404 -> Response.NotFound
                  422 -> Response.UnprocessableEntity
                  503 -> response.body<Response.ServiceUnavailable>()
                  else -> throw ResponseException(response, "")
                }
              }

              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              internal data class Body(
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val state: SecretScanningAlertState? = null,
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val resolution: Optional<SecretScanningAlertResolution> = Optional.Absent,
                @SerialName("resolution_comment")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val resolutionComment:
                    Optional<SecretScanningAlertResolutionComment> = Optional.Absent,
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val assignee: Optional<SecretScanningAlertAssignee> = Optional.Absent,
              )

              public sealed interface Response {
                public data class Ok(
                  public val `value`: SecretScanningAlert,
                ) : Response

                public data object BadRequest : Response

                public data object NotFound : Response

                public data object UnprocessableEntity : Response

                @OptIn(ExperimentalSerializationApi::class)
                @Serializable
                public data class ServiceUnavailable(
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val code: String? = null,
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val message: String? = null,
                  @SerialName("documentation_url")
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val documentationUrl: String? = null,
                ) : Response
              }
            }

            public class Locations internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _alertNumber: Long,
            ) {
              public val `get`: Get = Get(__client, _owner, _repo, _alertNumber)

              public class Get internal constructor(
                private val __client: HttpClient,
                private val _owner: String,
                private val _repo: String,
                private val _alertNumber: Long,
              ) {
                public suspend operator fun invoke(page: Long? = 1L, perPage: Long? = 30L): Response {
                  val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/secret-scanning/alerts/${this._alertNumber}/locations") {
                    if (page != null) {
                      this.url.encodedParameters.append("page", page.toString())
                    }
                    if (perPage != null) {
                      this.url.encodedParameters.append("per_page", perPage.toString())
                    }
                  }
                  return when (response.status.value) {
                    200 -> Response.Ok(response.body())
                    404 -> Response.NotFound
                    503 -> response.body<Response.ServiceUnavailable>()
                    else -> throw ResponseException(response, "")
                  }
                }

                public sealed interface Response {
                  public data class Ok(
                    public val `value`: List<SecretScanningLocation>,
                  ) : Response

                  public data object NotFound : Response

                  @OptIn(ExperimentalSerializationApi::class)
                  @Serializable
                  public data class ServiceUnavailable(
                    @EncodeDefault(EncodeDefault.Mode.NEVER)
                    public val code: String? = null,
                    @EncodeDefault(EncodeDefault.Mode.NEVER)
                    public val message: String? = null,
                    @SerialName("documentation_url")
                    @EncodeDefault(EncodeDefault.Mode.NEVER)
                    public val documentationUrl: String? = null,
                  ) : Response
                }
              }
            }
          }
        }

        public class PushProtectionBypasses internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public val post: Post = Post(__client, _owner, _repo)

          public class Post internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public suspend operator fun invoke(reason: SecretScanningPushProtectionBypassReason, placeholderId: SecretScanningPushProtectionBypassPlaceholderId): Response {
              val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/secret-scanning/push-protection-bypasses") {
                contentType(ContentType.Application.Json)
                setBody(Body(reason = reason, placeholderId = placeholderId))
              }
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                403 -> Response.Forbidden
                404 -> Response.NotFound
                422 -> Response.UnprocessableEntity
                503 -> response.body<Response.ServiceUnavailable>()
                else -> throw ResponseException(response, "")
              }
            }

            @Serializable
            internal data class Body(
              public val reason: SecretScanningPushProtectionBypassReason,
              @SerialName("placeholder_id")
              public val placeholderId: SecretScanningPushProtectionBypassPlaceholderId,
            )

            public sealed interface Response {
              public data class Ok(
                public val `value`: SecretScanningPushProtectionBypass,
              ) : Response

              public data object Forbidden : Response

              public data object NotFound : Response

              public data object UnprocessableEntity : Response

              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              public data class ServiceUnavailable(
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val code: String? = null,
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val message: String? = null,
                @SerialName("documentation_url")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val documentationUrl: String? = null,
              ) : Response
            }
          }
        }

        public class ScanHistory internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public val `get`: Get = Get(__client, _owner, _repo)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/secret-scanning/scan-history")
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                404 -> Response.NotFound
                503 -> response.body<Response.ServiceUnavailable>()
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: SecretScanningScanHistory,
              ) : Response

              public data object NotFound : Response

              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              public data class ServiceUnavailable(
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val code: String? = null,
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val message: String? = null,
                @SerialName("documentation_url")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val documentationUrl: String? = null,
              ) : Response
            }
          }
        }
      }

      public class SecurityAdvisories internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public val `get`: Get = Get(__client, _owner, _repo)

        public val post: Post = Post(__client, _owner, _repo)

        public val reports: Reports = Reports(__client, _owner, _repo)

        public fun ghsaId(ghsaId: String): GhsaIdPath = GhsaIdPath(__client, _owner, _repo, ghsaId)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(
            direction: Direction? = Direction.Desc,
            sort: Sort? = Sort.Created,
            before: String? = null,
            after: String? = null,
            perPage: Long? = 30L,
            state: State? = null,
          ): Response {
            val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/security-advisories") {
              if (direction != null) {
                this.url.encodedParameters.append("direction", direction.value.encodeURLParameter())
              }
              if (sort != null) {
                this.url.encodedParameters.append("sort", sort.value.encodeURLParameter())
              }
              if (before != null) {
                this.url.encodedParameters.append("before", before.encodeURLParameter())
              }
              if (after != null) {
                this.url.encodedParameters.append("after", after.encodeURLParameter())
              }
              if (perPage != null) {
                this.url.encodedParameters.append("per_page", perPage.toString())
              }
              if (state != null) {
                this.url.encodedParameters.append("state", state.value.encodeURLParameter())
              }
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

        public class Post internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(body: RepositoryAdvisoryCreate): Response {
            val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/security-advisories") {
              contentType(ContentType.Application.Json)
              setBody(body)
            }
            return when (response.status.value) {
              201 -> Response.Created(response.body())
              403 -> Response.Forbidden(response.body())
              404 -> Response.NotFound(response.body())
              422 -> Response.UnprocessableEntity(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data class Created(
              public val `value`: RepositoryAdvisory,
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

        public class Reports internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public val post: Post = Post(__client, _owner, _repo)

          public class Post internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public suspend operator fun invoke(body: PrivateVulnerabilityReportCreate): Response {
              val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/security-advisories/reports") {
                contentType(ContentType.Application.Json)
                setBody(body)
              }
              return when (response.status.value) {
                201 -> Response.Created(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                422 -> Response.UnprocessableEntity(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Created(
                public val `value`: RepositoryAdvisory,
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

        public class GhsaIdPath internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
          private val _ghsaId: String,
        ) {
          public val `get`: Get = Get(__client, _owner, _repo, _ghsaId)

          public val patch: Patch = Patch(__client, _owner, _repo, _ghsaId)

          public val cve: Cve = Cve(__client, _owner, _repo, _ghsaId)

          public val forks: Forks = Forks(__client, _owner, _repo, _ghsaId)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _ghsaId: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/security-advisories/${this._ghsaId.encodeURLParameter()}")
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: RepositoryAdvisory,
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
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _ghsaId: String,
          ) {
            public suspend operator fun invoke(body: RepositoryAdvisoryUpdate): Response {
              val response = __client.patch("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/security-advisories/${this._ghsaId.encodeURLParameter()}") {
                contentType(ContentType.Application.Json)
                setBody(body)
              }
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                422 -> Response.UnprocessableEntity(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: RepositoryAdvisory,
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

          public class Cve internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _ghsaId: String,
          ) {
            public val post: Post = Post(__client, _owner, _repo, _ghsaId)

            public class Post internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _ghsaId: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/security-advisories/${this._ghsaId.encodeURLParameter()}/cve")
                return when (response.status.value) {
                  202 -> Response.Accepted(response.body())
                  400 -> Response.BadRequest(response.body())
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
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

          public class Forks internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _ghsaId: String,
          ) {
            public val post: Post = Post(__client, _owner, _repo, _ghsaId)

            public class Post internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
              private val _ghsaId: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/security-advisories/${this._ghsaId.encodeURLParameter()}/forks")
                return when (response.status.value) {
                  202 -> Response.Accepted(response.body())
                  400 -> Response.BadRequest(response.body())
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  422 -> Response.UnprocessableEntity(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data class Accepted(
                  public val `value`: FullRepository,
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

                public data class UnprocessableEntity(
                  public val `value`: ValidationError,
                ) : Response
              }
            }
          }
        }
      }

      public class Stargazers internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public val `get`: Get = Get(__client, _owner, _repo)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
            val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/stargazers") {
              if (perPage != null) {
                this.url.encodedParameters.append("per_page", perPage.toString())
              }
              if (page != null) {
                this.url.encodedParameters.append("page", page.toString())
              }
            }
            return when (response.status.value) {
              200 -> response.body<Response.Ok>()
              422 -> Response.UnprocessableEntity(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            @Serializable(with = Ok.Serializer::class)
            public sealed interface Ok : Response {
              @Serializable
              @JvmInline
              public value class CaseSimpleUserList(
                public val `value`: List<SimpleUser>,
              ) : Ok

              @Serializable
              @JvmInline
              public value class CaseStargazerList(
                public val `value`: List<Stargazer>,
              ) : Ok

              public object Serializer : KSerializer<Ok> {
                @OptIn(
                  InternalSerializationApi::class,
                  ExperimentalSerializationApi::class,
                )
                override val descriptor: SerialDescriptor =
                    buildSerialDescriptor("io.github.api.Repos.OwnerPath.RepoPath.Stargazers.Get.Response.Ok", PolymorphicKind.SEALED) {
                  element("CaseSimpleUserList", ListSerializer(SimpleUser.serializer()).descriptor)
                  element("CaseStargazerList", ListSerializer(Stargazer.serializer()).descriptor)
                }

                override fun deserialize(decoder: Decoder): Ok {
                  val value = decoder.decodeSerializableValue(JsonElement.serializer())
                  val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                  return json.attemptDeserialize(
                    value,
                    CaseSimpleUserList::class to { CaseSimpleUserList(decodeFromJsonElement(ListSerializer(SimpleUser.serializer()), it)) },
                    CaseStargazerList::class to { CaseStargazerList(decodeFromJsonElement(ListSerializer(Stargazer.serializer()), it)) },
                  )
                }

                override fun serialize(encoder: Encoder, `value`: Ok) {
                  when(value) {
                    is CaseSimpleUserList -> encoder.encodeSerializableValue(ListSerializer(SimpleUser.serializer()), value.value)
                    is CaseStargazerList -> encoder.encodeSerializableValue(ListSerializer(Stargazer.serializer()), value.value)
                  }
                }
              }
            }

            public data class UnprocessableEntity(
              public val `value`: ValidationError,
            ) : Response
          }
        }
      }

      public class Stats internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public val codeFrequency: CodeFrequency = CodeFrequency(__client, _owner, _repo)

        public val commitActivity: CommitActivity = CommitActivity(__client, _owner, _repo)

        public val contributors: Contributors = Contributors(__client, _owner, _repo)

        public val participation: Participation = Participation(__client, _owner, _repo)

        public val punchCard: PunchCard = PunchCard(__client, _owner, _repo)

        public class CodeFrequency internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public val `get`: Get = Get(__client, _owner, _repo)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/stats/code_frequency")
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                202 -> Response.Accepted(response.body())
                204 -> Response.NoContent
                422 -> Response.UnprocessableEntity
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: List<CodeFrequencyStat>,
              ) : Response

              public data class Accepted(
                public val `value`: JsonElement,
              ) : Response

              public data object NoContent : Response

              public data object UnprocessableEntity : Response
            }
          }
        }

        public class CommitActivity internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public val `get`: Get = Get(__client, _owner, _repo)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/stats/commit_activity")
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                202 -> Response.Accepted(response.body())
                204 -> Response.NoContent
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: List<io.github.model.CommitActivity>,
              ) : Response

              public data class Accepted(
                public val `value`: JsonElement,
              ) : Response

              public data object NoContent : Response
            }
          }
        }

        public class Contributors internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public val `get`: Get = Get(__client, _owner, _repo)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/stats/contributors")
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                202 -> Response.Accepted(response.body())
                204 -> Response.NoContent
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: List<ContributorActivity>,
              ) : Response

              public data class Accepted(
                public val `value`: JsonElement,
              ) : Response

              public data object NoContent : Response
            }
          }
        }

        public class Participation internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public val `get`: Get = Get(__client, _owner, _repo)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/stats/participation")
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: ParticipationStats,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response
            }
          }
        }

        public class PunchCard internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public val `get`: Get = Get(__client, _owner, _repo)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/stats/punch_card")
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                204 -> Response.NoContent
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: List<CodeFrequencyStat>,
              ) : Response

              public data object NoContent : Response
            }
          }
        }
      }

      public class Statuses internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public fun sha(sha: String): ShaPath = ShaPath(__client, _owner, _repo, sha)

        public class ShaPath internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
          private val _sha: String,
        ) {
          public val post: Post = Post(__client, _owner, _repo, _sha)

          public class Post internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _sha: String,
          ) {
            public suspend operator fun invoke(
              state: State,
              targetUrl: Optional<String> = Optional.Absent,
              description: Optional<String> = Optional.Absent,
              context: String? = null,
            ): Status {
              val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/statuses/${this._sha.encodeURLParameter()}") {
                contentType(ContentType.Application.Json)
                setBody(Body(state = state, targetUrl = targetUrl, description = description, context = context))
              }
              if (response.status.value != 201) throw ResponseException(response, "")
              return response.body()
            }

            @Serializable
            public enum class State(
              public val `value`: String,
            ) {
              @SerialName("error")
              Error("error"),
              @SerialName("failure")
              Failure("failure"),
              @SerialName("pending")
              Pending("pending"),
              @SerialName("success")
              Success("success"),
              ;
            }

            @OptIn(ExperimentalSerializationApi::class)
            @Serializable
            internal data class Body(
              public val state: State,
              @SerialName("target_url")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val targetUrl: Optional<String> = Optional.Absent,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val description: Optional<String> = Optional.Absent,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val context: String? = null,
            )
          }
        }
      }

      public class Subscribers internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public val `get`: Get = Get(__client, _owner, _repo)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<SimpleUser> {
            val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/subscribers") {
              if (perPage != null) {
                this.url.encodedParameters.append("per_page", perPage.toString())
              }
              if (page != null) {
                this.url.encodedParameters.append("page", page.toString())
              }
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }
      }

      public class Subscription internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public val delete: Delete = Delete(__client, _owner, _repo)

        public val `get`: Get = Get(__client, _owner, _repo)

        public val put: Put = Put(__client, _owner, _repo)

        public class Delete internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke() {
            val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/subscription")
            if (response.status.value != 204) throw ResponseException(response, "")
          }
        }

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/subscription")
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              403 -> Response.Forbidden(response.body())
              404 -> Response.NotFound
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: RepositorySubscription,
            ) : Response

            public data class Forbidden(
              public val `value`: BasicError,
            ) : Response

            public data object NotFound : Response
          }
        }

        public class Put internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(): RepositorySubscription {
            val response = __client.put("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/subscription")
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }

          public suspend operator fun invoke(subscribed: Boolean? = null, ignored: Boolean? = null): RepositorySubscription {
            val response = __client.put("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/subscription") {
              contentType(ContentType.Application.Json)
              setBody(Body(subscribed = subscribed, ignored = ignored))
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }

          @OptIn(ExperimentalSerializationApi::class)
          @Serializable
          internal data class Body(
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val subscribed: Boolean? = null,
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val ignored: Boolean? = null,
          )
        }
      }

      public class Tags internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public val `get`: Get = Get(__client, _owner, _repo)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<Tag> {
            val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/tags") {
              if (perPage != null) {
                this.url.encodedParameters.append("per_page", perPage.toString())
              }
              if (page != null) {
                this.url.encodedParameters.append("page", page.toString())
              }
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }
      }

      public class Tarball internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public fun ref(ref: String): RefPath = RefPath(__client, _owner, _repo, ref)

        public class RefPath internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
          private val _ref: String,
        ) {
          public val `get`: Get = Get(__client, _owner, _repo, _ref)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _ref: String,
          ) {
            public suspend operator fun invoke() {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/tarball/${this._ref.encodeURLParameter()}")
              if (response.status.value != 302) throw ResponseException(response, "")
            }
          }
        }
      }

      public class Teams internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public val `get`: Get = Get(__client, _owner, _repo)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
            val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/teams") {
              if (perPage != null) {
                this.url.encodedParameters.append("per_page", perPage.toString())
              }
              if (page != null) {
                this.url.encodedParameters.append("page", page.toString())
              }
            }
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              404 -> Response.NotFound(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: List<Team>,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response
          }
        }
      }

      public class Topics internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public val `get`: Get = Get(__client, _owner, _repo)

        public val put: Put = Put(__client, _owner, _repo)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(page: Long? = 1L, perPage: Long? = 30L): Response {
            val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/topics") {
              if (page != null) {
                this.url.encodedParameters.append("page", page.toString())
              }
              if (perPage != null) {
                this.url.encodedParameters.append("per_page", perPage.toString())
              }
            }
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              404 -> Response.NotFound(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: Topic,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class Put internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(names: List<String>): Response {
            val response = __client.put("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/topics") {
              contentType(ContentType.Application.Json)
              setBody(Body(names = names))
            }
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              404 -> Response.NotFound(response.body())
              422 -> Response.UnprocessableEntity(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          @Serializable
          internal data class Body(
            public val names: List<String>,
          )

          public sealed interface Response {
            public data class Ok(
              public val `value`: Topic,
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

      public class Traffic internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public val clones: Clones = Clones(__client, _owner, _repo)

        public val popular: Popular = Popular(__client, _owner, _repo)

        public val views: Views = Views(__client, _owner, _repo)

        public class Clones internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public val `get`: Get = Get(__client, _owner, _repo)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public suspend operator fun invoke(per: Per? = Per.Day): Response {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/traffic/clones") {
                if (per != null) {
                  this.url.encodedParameters.append("per", per.value.encodeURLParameter())
                }
              }
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                403 -> Response.Forbidden(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            @Serializable
            public enum class Per(
              public val `value`: String,
            ) {
              @SerialName("day")
              Day("day"),
              @SerialName("week")
              Week("week"),
              ;
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: CloneTraffic,
              ) : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response
            }
          }
        }

        public class Popular internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public val paths: Paths = Paths(__client, _owner, _repo)

          public val referrers: Referrers = Referrers(__client, _owner, _repo)

          public class Paths internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/traffic/popular/paths")
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  403 -> Response.Forbidden(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: List<ContentTraffic>,
                ) : Response

                public data class Forbidden(
                  public val `value`: BasicError,
                ) : Response
              }
            }
          }

          public class Referrers internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public val `get`: Get = Get(__client, _owner, _repo)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _owner: String,
              private val _repo: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/traffic/popular/referrers")
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  403 -> Response.Forbidden(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: List<ReferrerTraffic>,
                ) : Response

                public data class Forbidden(
                  public val `value`: BasicError,
                ) : Response
              }
            }
          }
        }

        public class Views internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public val `get`: Get = Get(__client, _owner, _repo)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
          ) {
            public suspend operator fun invoke(per: Per? = Per.Day): Response {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/traffic/views") {
                if (per != null) {
                  this.url.encodedParameters.append("per", per.value.encodeURLParameter())
                }
              }
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                403 -> Response.Forbidden(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            @Serializable
            public enum class Per(
              public val `value`: String,
            ) {
              @SerialName("day")
              Day("day"),
              @SerialName("week")
              Week("week"),
              ;
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: ViewTraffic,
              ) : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response
            }
          }
        }
      }

      public class Transfer internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public val post: Post = Post(__client, _owner, _repo)

        public class Post internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(
            newOwner: String,
            newName: String? = null,
            teamIds: List<Long>? = null,
          ): MinimalRepository {
            val response = __client.post("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/transfer") {
              contentType(ContentType.Application.Json)
              setBody(Body(newOwner = newOwner, newName = newName, teamIds = teamIds))
            }
            if (response.status.value != 202) throw ResponseException(response, "")
            return response.body()
          }

          @OptIn(ExperimentalSerializationApi::class)
          @Serializable
          internal data class Body(
            @SerialName("new_owner")
            public val newOwner: String,
            @SerialName("new_name")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val newName: String? = null,
            @SerialName("team_ids")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val teamIds: List<Long>? = null,
          )
        }
      }

      public class VulnerabilityAlerts internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public val delete: Delete = Delete(__client, _owner, _repo)

        public val `get`: Get = Get(__client, _owner, _repo)

        public val put: Put = Put(__client, _owner, _repo)

        public class Delete internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke() {
            val response = __client.delete("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/vulnerability-alerts")
            if (response.status.value != 204) throw ResponseException(response, "")
          }
        }

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/vulnerability-alerts")
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
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke() {
            val response = __client.put("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/vulnerability-alerts")
            if (response.status.value != 204) throw ResponseException(response, "")
          }
        }
      }

      public class Zipball internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public fun ref(ref: String): RefPath = RefPath(__client, _owner, _repo, ref)

        public class RefPath internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
          private val _ref: String,
        ) {
          public val `get`: Get = Get(__client, _owner, _repo, _ref)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _owner: String,
            private val _repo: String,
            private val _ref: String,
          ) {
            public suspend operator fun invoke() {
              val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/zipball/${this._ref.encodeURLParameter()}")
              if (response.status.value != 302) throw ResponseException(response, "")
            }
          }
        }
      }
    }
  }

  public class TemplateOwnerPath internal constructor(
    private val __client: HttpClient,
    private val _templateOwner: String,
  ) {
    public fun templateRepo(templateRepo: String): TemplateRepoPath = TemplateRepoPath(__client, _templateOwner, templateRepo)

    public class TemplateRepoPath internal constructor(
      private val __client: HttpClient,
      private val _templateOwner: String,
      private val _templateRepo: String,
    ) {
      public val generate: Generate = Generate(__client, _templateOwner, _templateRepo)

      public class Generate internal constructor(
        private val __client: HttpClient,
        private val _templateOwner: String,
        private val _templateRepo: String,
      ) {
        public val post: Post = Post(__client, _templateOwner, _templateRepo)

        public class Post internal constructor(
          private val __client: HttpClient,
          private val _templateOwner: String,
          private val _templateRepo: String,
        ) {
          public suspend operator fun invoke(
            owner: String? = null,
            name: String,
            description: String? = null,
            includeAllBranches: Boolean? = null,
            `private`: Boolean? = null,
          ): FullRepository {
            val response = __client.post("repos/${this._templateOwner.encodeURLParameter()}/${this._templateRepo.encodeURLParameter()}/generate") {
              contentType(ContentType.Application.Json)
              setBody(Body(owner = owner, name = name, description = description, includeAllBranches = includeAllBranches, private = private))
            }
            if (response.status.value != 201) throw ResponseException(response, "")
            return response.body()
          }

          @OptIn(ExperimentalSerializationApi::class)
          @Serializable
          internal data class Body(
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val owner: String? = null,
            public val name: String,
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val description: String? = null,
            @SerialName("include_all_branches")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val includeAllBranches: Boolean? = null,
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val `private`: Boolean? = null,
          )
        }
      }
    }
  }
}
