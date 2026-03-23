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
import io.github.model.ContentDirectory
import io.github.model.ContentFile
import io.github.model.ContentSubmodule
import io.github.model.ContentSymlink
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
import io.ktor.client.request.delete
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
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
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement

public class Repos internal constructor(
  private val client: HttpClient,
) {
  public fun owner(owner: String): OwnerPath = OwnerPath(client, owner)

  public fun templateOwner(templateOwner: String): TemplateOwnerPath = TemplateOwnerPath(client, templateOwner)

  public class OwnerPath internal constructor(
    private val client: HttpClient,
    private val owner: String,
  ) {
    public fun repo(repo: String): RepoPath = RepoPath(client, owner, repo)

    public class RepoPath internal constructor(
      private val client: HttpClient,
      private val owner: String,
      private val repo: String,
    ) {
      public val delete: Delete = Delete(client, owner, repo)

      public val `get`: Get = Get(client, owner, repo)

      public val patch: Patch = Patch(client, owner, repo)

      public val actions: Actions = Actions(client, owner, repo)

      public val activity: Activity = Activity(client, owner, repo)

      public val assignees: Assignees = Assignees(client, owner, repo)

      public val attestations: Attestations = Attestations(client, owner, repo)

      public val autolinks: Autolinks = Autolinks(client, owner, repo)

      public val automatedSecurityFixes: AutomatedSecurityFixes =
          AutomatedSecurityFixes(client, owner, repo)

      public val branches: Branches = Branches(client, owner, repo)

      public val checkRuns: CheckRuns = CheckRuns(client, owner, repo)

      public val checkSuites: CheckSuites = CheckSuites(client, owner, repo)

      public val codeScanning: CodeScanning = CodeScanning(client, owner, repo)

      public val codeSecurityConfiguration: CodeSecurityConfiguration =
          CodeSecurityConfiguration(client, owner, repo)

      public val codeowners: Codeowners = Codeowners(client, owner, repo)

      public val codespaces: Codespaces = Codespaces(client, owner, repo)

      public val collaborators: Collaborators = Collaborators(client, owner, repo)

      public val comments: Comments = Comments(client, owner, repo)

      public val commits: Commits = Commits(client, owner, repo)

      public val community: Community = Community(client, owner, repo)

      public val compare: Compare = Compare(client, owner, repo)

      public val contents: Contents = Contents(client, owner, repo)

      public val contributors: Contributors = Contributors(client, owner, repo)

      public val dependabot: Dependabot = Dependabot(client, owner, repo)

      public val dependencyGraph: DependencyGraph = DependencyGraph(client, owner, repo)

      public val deployments: Deployments = Deployments(client, owner, repo)

      public val dispatches: Dispatches = Dispatches(client, owner, repo)

      public val environments: Environments = Environments(client, owner, repo)

      public val events: Events = Events(client, owner, repo)

      public val forks: Forks = Forks(client, owner, repo)

      public val git: Git = Git(client, owner, repo)

      public val hooks: Hooks = Hooks(client, owner, repo)

      public val immutableReleases: ImmutableReleases = ImmutableReleases(client, owner, repo)

      public val `import`: Import = Import(client, owner, repo)

      public val installation: Installation = Installation(client, owner, repo)

      public val interactionLimits: InteractionLimits = InteractionLimits(client, owner, repo)

      public val invitations: Invitations = Invitations(client, owner, repo)

      public val issues: Issues = Issues(client, owner, repo)

      public val keys: Keys = Keys(client, owner, repo)

      public val labels: Labels = Labels(client, owner, repo)

      public val languages: Languages = Languages(client, owner, repo)

      public val license: License = License(client, owner, repo)

      public val mergeUpstream: MergeUpstream = MergeUpstream(client, owner, repo)

      public val merges: Merges = Merges(client, owner, repo)

      public val milestones: Milestones = Milestones(client, owner, repo)

      public val notifications: Notifications = Notifications(client, owner, repo)

      public val pages: Pages = Pages(client, owner, repo)

      public val privateVulnerabilityReporting: PrivateVulnerabilityReporting =
          PrivateVulnerabilityReporting(client, owner, repo)

      public val properties: Properties = Properties(client, owner, repo)

      public val pulls: Pulls = Pulls(client, owner, repo)

      public val readme: Readme = Readme(client, owner, repo)

      public val releases: Releases = Releases(client, owner, repo)

      public val rules: Rules = Rules(client, owner, repo)

      public val rulesets: Rulesets = Rulesets(client, owner, repo)

      public val secretScanning: SecretScanning = SecretScanning(client, owner, repo)

      public val securityAdvisories: SecurityAdvisories = SecurityAdvisories(client, owner, repo)

      public val stargazers: Stargazers = Stargazers(client, owner, repo)

      public val stats: Stats = Stats(client, owner, repo)

      public val statuses: Statuses = Statuses(client, owner, repo)

      public val subscribers: Subscribers = Subscribers(client, owner, repo)

      public val subscription: Subscription = Subscription(client, owner, repo)

      public val tags: Tags = Tags(client, owner, repo)

      public val tarball: Tarball = Tarball(client, owner, repo)

      public val teams: Teams = Teams(client, owner, repo)

      public val topics: Topics = Topics(client, owner, repo)

      public val traffic: Traffic = Traffic(client, owner, repo)

      public val transfer: Transfer = Transfer(client, owner, repo)

      public val vulnerabilityAlerts: VulnerabilityAlerts = VulnerabilityAlerts(client, owner, repo)

      public val zipball: Zipball = Zipball(client, owner, repo)

      public class Delete internal constructor(
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public suspend operator fun invoke(): Response {
          val response = client.delete("/repos/$owner/$repo")
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

          @Serializable
          public data class Forbidden(
            public val message: String? = null,
            @SerialName("documentation_url")
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
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public suspend operator fun invoke(): Response {
          val response = client.get("/repos/$owner/$repo")
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
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public suspend operator fun invoke(
          name: String? = null,
          description: String? = null,
          homepage: String? = null,
          `private`: Boolean? = null,
          visibility: Visibility? = null,
          securityAndAnalysis: SecurityAndAnalysis? = null,
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
          val response = client.patch("/repos/$owner/$repo") {
            if (name != null || description != null || homepage != null || private != null || visibility != null || securityAndAnalysis != null || hasIssues != null || hasProjects != null || hasWiki != null || isTemplate != null || defaultBranch != null || allowSquashMerge != null || allowMergeCommit != null || allowRebaseMerge != null || allowAutoMerge != null || deleteBranchOnMerge != null || allowUpdateBranch != null || useSquashPrTitleAsDefault != null || squashMergeCommitTitle != null || squashMergeCommitMessage != null || mergeCommitTitle != null || mergeCommitMessage != null || archived != null || allowForking != null || webCommitSignoffRequired != null) {
              contentType(ContentType.Application.Json)
              setBody(Body(name = name, description = description, homepage = homepage, private = private, visibility = visibility, securityAndAnalysis = securityAndAnalysis, hasIssues = hasIssues, hasProjects = hasProjects, hasWiki = hasWiki, isTemplate = isTemplate, defaultBranch = defaultBranch, allowSquashMerge = allowSquashMerge, allowMergeCommit = allowMergeCommit, allowRebaseMerge = allowRebaseMerge, allowAutoMerge = allowAutoMerge, deleteBranchOnMerge = deleteBranchOnMerge, allowUpdateBranch = allowUpdateBranch, useSquashPrTitleAsDefault = useSquashPrTitleAsDefault, squashMergeCommitTitle = squashMergeCommitTitle, squashMergeCommitMessage = squashMergeCommitMessage, mergeCommitTitle = mergeCommitTitle, mergeCommitMessage = mergeCommitMessage, archived = archived, allowForking = allowForking, webCommitSignoffRequired = webCommitSignoffRequired))
            }
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
        @Serializable
        public data class SecurityAndAnalysis(
          @SerialName("advanced_security")
          public val advancedSecurity: AdvancedSecurity? = null,
          @SerialName("code_security")
          public val codeSecurity: CodeSecurity? = null,
          @SerialName("secret_scanning")
          public val secretScanning: SecretScanning? = null,
          @SerialName("secret_scanning_push_protection")
          public val secretScanningPushProtection: SecretScanningPushProtection? = null,
          @SerialName("secret_scanning_ai_detection")
          public val secretScanningAiDetection: SecretScanningAiDetection? = null,
          @SerialName("secret_scanning_non_provider_patterns")
          public val secretScanningNonProviderPatterns: SecretScanningNonProviderPatterns? = null,
          @SerialName("secret_scanning_delegated_alert_dismissal")
          public val secretScanningDelegatedAlertDismissal:
              SecretScanningDelegatedAlertDismissal? = null,
          @SerialName("secret_scanning_delegated_bypass")
          public val secretScanningDelegatedBypass: SecretScanningDelegatedBypass? = null,
          @SerialName("secret_scanning_delegated_bypass_options")
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
          @JvmInline
          @Serializable
          public value class AdvancedSecurity(
            public val status: String? = null,
          )

          /**
           * Use the `status` property to enable or disable GitHub Code Security for this repository.
           */
          @JvmInline
          @Serializable
          public value class CodeSecurity(
            public val status: String? = null,
          )

          /**
           * Use the `status` property to enable or disable secret scanning for this repository. For more information, see "[About secret scanning](/code-security/secret-security/about-secret-scanning)."
           */
          @JvmInline
          @Serializable
          public value class SecretScanning(
            public val status: String? = null,
          )

          /**
           * Use the `status` property to enable or disable secret scanning AI detection for this repository. For more information, see "[Responsible detection of generic secrets with AI](https://docs.github.com/code-security/secret-scanning/using-advanced-secret-scanning-and-push-protection-features/generic-secret-detection/responsible-ai-generic-secrets)."
           */
          @JvmInline
          @Serializable
          public value class SecretScanningAiDetection(
            public val status: String? = null,
          )

          /**
           * Use the `status` property to enable or disable secret scanning delegated alert dismissal for this repository.
           */
          @JvmInline
          @Serializable
          public value class SecretScanningDelegatedAlertDismissal(
            public val status: String? = null,
          )

          /**
           * Use the `status` property to enable or disable secret scanning delegated bypass for this repository.
           */
          @JvmInline
          @Serializable
          public value class SecretScanningDelegatedBypass(
            public val status: String? = null,
          )

          /**
           * Feature options for secret scanning delegated bypass.
           * This object is only honored when `security_and_analysis.secret_scanning_delegated_bypass.status` is set to `enabled`.
           * You can send this object in the same request as `secret_scanning_delegated_bypass`, or update just the options in a separate request.
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

          /**
           * Use the `status` property to enable or disable secret scanning non-provider patterns for this repository. For more information, see "[Supported secret scanning patterns](/code-security/secret-scanning/introduction/supported-secret-scanning-patterns#supported-secrets)."
           */
          @JvmInline
          @Serializable
          public value class SecretScanningNonProviderPatterns(
            public val status: String? = null,
          )

          /**
           * Use the `status` property to enable or disable secret scanning push protection for this repository. For more information, see "[Protecting pushes with secret scanning](/code-security/secret-scanning/protecting-pushes-with-secret-scanning)."
           */
          @JvmInline
          @Serializable
          public value class SecretScanningPushProtection(
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

        @Serializable
        internal data class Body(
          public val name: String? = null,
          public val description: String? = null,
          public val homepage: String? = null,
          public val `private`: Boolean? = null,
          public val visibility: Visibility? = null,
          @SerialName("security_and_analysis")
          public val securityAndAnalysis: SecurityAndAnalysis? = null,
          @SerialName("has_issues")
          public val hasIssues: Boolean? = null,
          @SerialName("has_projects")
          public val hasProjects: Boolean? = null,
          @SerialName("has_wiki")
          public val hasWiki: Boolean? = null,
          @SerialName("is_template")
          public val isTemplate: Boolean? = null,
          @SerialName("default_branch")
          public val defaultBranch: String? = null,
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
          @SerialName("allow_update_branch")
          public val allowUpdateBranch: Boolean? = null,
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
          public val archived: Boolean? = null,
          @SerialName("allow_forking")
          public val allowForking: Boolean? = null,
          @SerialName("web_commit_signoff_required")
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
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val artifacts: Artifacts = Artifacts(client, owner, repo)

        public val cache: Cache = Cache(client, owner, repo)

        public val caches: Caches = Caches(client, owner, repo)

        public val jobs: Jobs = Jobs(client, owner, repo)

        public val oidc: Oidc = Oidc(client, owner, repo)

        public val organizationSecrets: OrganizationSecrets =
            OrganizationSecrets(client, owner, repo)

        public val organizationVariables: OrganizationVariables =
            OrganizationVariables(client, owner, repo)

        public val permissions: Permissions = Permissions(client, owner, repo)

        public val runners: Runners = Runners(client, owner, repo)

        public val runs: Runs = Runs(client, owner, repo)

        public val secrets: Secrets = Secrets(client, owner, repo)

        public val variables: Variables = Variables(client, owner, repo)

        public val workflows: Workflows = Workflows(client, owner, repo)

        public class Artifacts internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public val `get`: Get = Get(client, owner, repo)

          public fun artifactId(artifactId: Long): ArtifactIdPath = ArtifactIdPath(client, owner, repo, artifactId)

          public class Get internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public suspend operator fun invoke(
              perPage: Long? = 30L,
              page: Long? = 1L,
              name: String? = null,
            ): Response = client.get("/repos/$owner/$repo/actions/artifacts") {
              perPage?.let { parameter("per_page", it) }
              page?.let { parameter("page", it) }
              name?.let { parameter("name", it) }
            }.body()

            @Serializable
            public data class Response(
              @SerialName("total_count")
              public val totalCount: Long,
              public val artifacts: List<Artifact>,
            )
          }

          public class ArtifactIdPath internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val artifactId: Long,
          ) {
            public val delete: Delete = Delete(client, owner, repo, artifactId)

            public val `get`: Get = Get(client, owner, repo, artifactId)

            public fun archiveFormat(archiveFormat: String): ArchiveFormatPath = ArchiveFormatPath(client, owner, repo, artifactId, archiveFormat)

            public class Delete internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val artifactId: Long,
            ) {
              public suspend operator fun invoke() {
                client.delete("/repos/$owner/$repo/actions/artifacts/$artifactId")
              }
            }

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val artifactId: Long,
            ) {
              public suspend operator fun invoke(): Artifact = client.get("/repos/$owner/$repo/actions/artifacts/$artifactId").body()
            }

            public class ArchiveFormatPath internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val artifactId: Long,
              private val archiveFormat: String,
            ) {
              public val `get`: Get = Get(client, owner, repo, artifactId, archiveFormat)

              public class Get internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val artifactId: Long,
                private val archiveFormat: String,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.get("/repos/$owner/$repo/actions/artifacts/$artifactId/$archiveFormat")
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public val retentionLimit: RetentionLimit = RetentionLimit(client, owner, repo)

          public val storageLimit: StorageLimit = StorageLimit(client, owner, repo)

          public val usage: Usage = Usage(client, owner, repo)

          public class RetentionLimit internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public val `get`: Get = Get(client, owner, repo)

            public val put: Put = Put(client, owner, repo)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.get("/repos/$owner/$repo/actions/cache/retention-limit")
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
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
            ) {
              public suspend operator fun invoke(body: ActionsCacheRetentionLimitForRepository): Response {
                val response = client.put("/repos/$owner/$repo/actions/cache/retention-limit") {
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
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public val `get`: Get = Get(client, owner, repo)

            public val put: Put = Put(client, owner, repo)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.get("/repos/$owner/$repo/actions/cache/storage-limit")
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
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
            ) {
              public suspend operator fun invoke(body: ActionsCacheStorageLimitForRepository): Response {
                val response = client.put("/repos/$owner/$repo/actions/cache/storage-limit") {
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
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public val `get`: Get = Get(client, owner, repo)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
            ) {
              public suspend operator fun invoke(): ActionsCacheUsageByRepository = client.get("/repos/$owner/$repo/actions/cache/usage").body()
            }
          }
        }

        public class Caches internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public val delete: Delete = Delete(client, owner, repo)

          public val `get`: Get = Get(client, owner, repo)

          public fun cacheId(cacheId: Long): CacheIdPath = CacheIdPath(client, owner, repo, cacheId)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public suspend operator fun invoke(key: String, ref: String? = null): ActionsCacheList = client.delete("/repos/$owner/$repo/actions/caches") {
              parameter("key", key)
              ref?.let { parameter("ref", it) }
            }.body()
          }

          public class Get internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public suspend operator fun invoke(
              perPage: Long? = 30L,
              page: Long? = 1L,
              ref: String? = null,
              key: String? = null,
              sort: Sort? = Sort.LastAccessedAt,
              direction: Direction? = Direction.Desc,
            ): ActionsCacheList = client.get("/repos/$owner/$repo/actions/caches") {
              perPage?.let { parameter("per_page", it) }
              page?.let { parameter("page", it) }
              ref?.let { parameter("ref", it) }
              key?.let { parameter("key", it) }
              sort?.let { parameter("sort", it.value) }
              direction?.let { parameter("direction", it.value) }
            }.body()

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
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val cacheId: Long,
          ) {
            public val delete: Delete = Delete(client, owner, repo, cacheId)

            public class Delete internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val cacheId: Long,
            ) {
              public suspend operator fun invoke() {
                client.delete("/repos/$owner/$repo/actions/caches/$cacheId")
              }
            }
          }
        }

        public class Jobs internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public fun jobId(jobId: Long): JobIdPath = JobIdPath(client, owner, repo, jobId)

          public class JobIdPath internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val jobId: Long,
          ) {
            public val `get`: Get = Get(client, owner, repo, jobId)

            public val logs: Logs = Logs(client, owner, repo, jobId)

            public val rerun: Rerun = Rerun(client, owner, repo, jobId)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val jobId: Long,
            ) {
              public suspend operator fun invoke(): Job = client.get("/repos/$owner/$repo/actions/jobs/$jobId").body()
            }

            public class Logs internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val jobId: Long,
            ) {
              public val `get`: Get = Get(client, owner, repo, jobId)

              public class Get internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val jobId: Long,
              ) {
                public suspend operator fun invoke() {
                  client.get("/repos/$owner/$repo/actions/jobs/$jobId/logs")
                }
              }
            }

            public class Rerun internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val jobId: Long,
            ) {
              public val post: Post = Post(client, owner, repo, jobId)

              public class Post internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val jobId: Long,
              ) {
                public suspend operator fun invoke(enableDebugLogging: Boolean? = null): Response {
                  val response = client.post("/repos/$owner/$repo/actions/jobs/$jobId/rerun") {
                    if (enableDebugLogging != null) {
                      contentType(ContentType.Application.Json)
                      setBody(Body(enableDebugLogging = enableDebugLogging))
                    }
                  }
                  return when (response.status.value) {
                    201 -> Response.Created(response.body())
                    403 -> Response.Forbidden(response.body())
                    else -> throw ResponseException(response, "")
                  }
                }

                @JvmInline
                @Serializable
                internal value class Body(
                  @SerialName("enable_debug_logging")
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public val customization: Customization = Customization(client, owner, repo)

          public class Customization internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public val sub: Sub = Sub(client, owner, repo)

            public class Sub internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
            ) {
              public val `get`: Get = Get(client, owner, repo)

              public val put: Put = Put(client, owner, repo)

              public class Get internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.get("/repos/$owner/$repo/actions/oidc/customization/sub")
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
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
              ) {
                public suspend operator fun invoke(useDefault: Boolean, includeClaimKeys: List<String>? = null): Response {
                  val response = client.put("/repos/$owner/$repo/actions/oidc/customization/sub") {
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
                @Serializable
                internal data class Body(
                  @SerialName("use_default")
                  public val useDefault: Boolean,
                  @SerialName("include_claim_keys")
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public val `get`: Get = Get(client, owner, repo)

          public class Get internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response = client.get("/repos/$owner/$repo/actions/organization-secrets") {
              perPage?.let { parameter("per_page", it) }
              page?.let { parameter("page", it) }
            }.body()

            @Serializable
            public data class Response(
              @SerialName("total_count")
              public val totalCount: Long,
              public val secrets: List<ActionsSecret>,
            )
          }
        }

        public class OrganizationVariables internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public val `get`: Get = Get(client, owner, repo)

          public class Get internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public suspend operator fun invoke(perPage: Long? = 10L, page: Long? = 1L): Response = client.get("/repos/$owner/$repo/actions/organization-variables") {
              perPage?.let { parameter("per_page", it) }
              page?.let { parameter("page", it) }
            }.body()

            @Serializable
            public data class Response(
              @SerialName("total_count")
              public val totalCount: Long,
              public val variables: List<ActionsVariable>,
            )
          }
        }

        public class Permissions internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public val `get`: Get = Get(client, owner, repo)

          public val put: Put = Put(client, owner, repo)

          public val access: Access = Access(client, owner, repo)

          public val artifactAndLogRetention: ArtifactAndLogRetention =
              ArtifactAndLogRetention(client, owner, repo)

          public val forkPrContributorApproval: ForkPrContributorApproval =
              ForkPrContributorApproval(client, owner, repo)

          public val forkPrWorkflowsPrivateRepos: ForkPrWorkflowsPrivateRepos =
              ForkPrWorkflowsPrivateRepos(client, owner, repo)

          public val selectedActions: SelectedActions = SelectedActions(client, owner, repo)

          public val workflow: Workflow = Workflow(client, owner, repo)

          public class Get internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public suspend operator fun invoke(): ActionsRepositoryPermissions = client.get("/repos/$owner/$repo/actions/permissions").body()
          }

          public class Put internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public suspend operator fun invoke(
              enabled: ActionsEnabled,
              allowedActions: AllowedActions? = null,
              shaPinningRequired: ShaPinningRequired? = null,
            ) {
              client.put("/repos/$owner/$repo/actions/permissions") {
                contentType(ContentType.Application.Json)
                setBody(Body(enabled = enabled, allowedActions = allowedActions, shaPinningRequired = shaPinningRequired))
              }
            }

            @Serializable
            internal data class Body(
              public val enabled: ActionsEnabled,
              @SerialName("allowed_actions")
              public val allowedActions: AllowedActions? = null,
              @SerialName("sha_pinning_required")
              public val shaPinningRequired: ShaPinningRequired? = null,
            )
          }

          public class Access internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public val `get`: Get = Get(client, owner, repo)

            public val put: Put = Put(client, owner, repo)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
            ) {
              public suspend operator fun invoke(): ActionsWorkflowAccessToRepository = client.get("/repos/$owner/$repo/actions/permissions/access").body()
            }

            public class Put internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
            ) {
              public suspend operator fun invoke(body: ActionsWorkflowAccessToRepository) {
                client.put("/repos/$owner/$repo/actions/permissions/access") {
                  contentType(ContentType.Application.Json)
                  setBody(body)
                }
              }
            }
          }

          public class ArtifactAndLogRetention internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public val `get`: Get = Get(client, owner, repo)

            public val put: Put = Put(client, owner, repo)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.get("/repos/$owner/$repo/actions/permissions/artifact-and-log-retention")
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
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
            ) {
              public suspend operator fun invoke(body: ActionsArtifactAndLogRetention): Response {
                val response = client.put("/repos/$owner/$repo/actions/permissions/artifact-and-log-retention") {
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
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public val `get`: Get = Get(client, owner, repo)

            public val put: Put = Put(client, owner, repo)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.get("/repos/$owner/$repo/actions/permissions/fork-pr-contributor-approval")
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
              private val owner: String,
              private val repo: String,
            ) {
              public suspend operator fun invoke(body: ActionsForkPrContributorApproval): Response {
                val response = client.put("/repos/$owner/$repo/actions/permissions/fork-pr-contributor-approval") {
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
            private val owner: String,
            private val repo: String,
          ) {
            public val `get`: Get = Get(client, owner, repo)

            public val put: Put = Put(client, owner, repo)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.get("/repos/$owner/$repo/actions/permissions/fork-pr-workflows-private-repos")
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
              private val owner: String,
              private val repo: String,
            ) {
              public suspend operator fun invoke(body: ActionsForkPrWorkflowsPrivateReposRequest): Response {
                val response = client.put("/repos/$owner/$repo/actions/permissions/fork-pr-workflows-private-repos") {
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
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public val `get`: Get = Get(client, owner, repo)

            public val put: Put = Put(client, owner, repo)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
            ) {
              public suspend operator fun invoke(): io.github.model.SelectedActions = client.get("/repos/$owner/$repo/actions/permissions/selected-actions").body()
            }

            public class Put internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
            ) {
              public suspend operator fun invoke(body: io.github.model.SelectedActions? = null) {
                client.put("/repos/$owner/$repo/actions/permissions/selected-actions") {
                  body?.let {
                    contentType(ContentType.Application.Json)
                    setBody(it)
                  }
                }
              }
            }
          }

          public class Workflow internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public val `get`: Get = Get(client, owner, repo)

            public val put: Put = Put(client, owner, repo)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
            ) {
              public suspend operator fun invoke(): ActionsGetDefaultWorkflowPermissions = client.get("/repos/$owner/$repo/actions/permissions/workflow").body()
            }

            public class Put internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
            ) {
              public suspend operator fun invoke(body: ActionsSetDefaultWorkflowPermissions): Response {
                val response = client.put("/repos/$owner/$repo/actions/permissions/workflow") {
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public val `get`: Get = Get(client, owner, repo)

          public val downloads: Downloads = Downloads(client, owner, repo)

          public val generateJitconfig: GenerateJitconfig = GenerateJitconfig(client, owner, repo)

          public val registrationToken: RegistrationToken = RegistrationToken(client, owner, repo)

          public val removeToken: RemoveToken = RemoveToken(client, owner, repo)

          public fun runnerId(runnerId: Long): RunnerIdPath = RunnerIdPath(client, owner, repo, runnerId)

          public class Get internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public suspend operator fun invoke(
              name: String? = null,
              perPage: Long? = 30L,
              page: Long? = 1L,
            ): Response = client.get("/repos/$owner/$repo/actions/runners") {
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
            private val owner: String,
            private val repo: String,
          ) {
            public val `get`: Get = Get(client, owner, repo)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
            ) {
              public suspend operator fun invoke(): List<RunnerApplication> = client.get("/repos/$owner/$repo/actions/runners/downloads").body()
            }
          }

          public class GenerateJitconfig internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public val post: Post = Post(client, owner, repo)

            public class Post internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
            ) {
              public suspend operator fun invoke(
                name: String,
                runnerGroupId: Long,
                labels: List<String>,
                workFolder: String? = null,
              ): Response {
                val response = client.post("/repos/$owner/$repo/actions/runners/generate-jitconfig") {
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
            private val owner: String,
            private val repo: String,
          ) {
            public val post: Post = Post(client, owner, repo)

            public class Post internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
            ) {
              public suspend operator fun invoke(): AuthenticationToken = client.post("/repos/$owner/$repo/actions/runners/registration-token").body()
            }
          }

          public class RemoveToken internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public val post: Post = Post(client, owner, repo)

            public class Post internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
            ) {
              public suspend operator fun invoke(): AuthenticationToken = client.post("/repos/$owner/$repo/actions/runners/remove-token").body()
            }
          }

          public class RunnerIdPath internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val runnerId: Long,
          ) {
            public val delete: Delete = Delete(client, owner, repo, runnerId)

            public val `get`: Get = Get(client, owner, repo, runnerId)

            public val labels: Labels = Labels(client, owner, repo, runnerId)

            public class Delete internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val runnerId: Long,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.delete("/repos/$owner/$repo/actions/runners/$runnerId")
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
              private val owner: String,
              private val repo: String,
              private val runnerId: Long,
            ) {
              public suspend operator fun invoke(): Runner = client.get("/repos/$owner/$repo/actions/runners/$runnerId").body()
            }

            public class Labels internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val runnerId: Long,
            ) {
              public val delete: Delete = Delete(client, owner, repo, runnerId)

              public val `get`: Get = Get(client, owner, repo, runnerId)

              public val post: Post = Post(client, owner, repo, runnerId)

              public val put: Put = Put(client, owner, repo, runnerId)

              public fun name(name: String): NamePath = NamePath(client, owner, repo, runnerId, name)

              public class Delete internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val runnerId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.delete("/repos/$owner/$repo/actions/runners/$runnerId/labels")
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
                private val owner: String,
                private val repo: String,
                private val runnerId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.get("/repos/$owner/$repo/actions/runners/$runnerId/labels")
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
                private val owner: String,
                private val repo: String,
                private val runnerId: Long,
              ) {
                public suspend operator fun invoke(labels: List<String>): Response {
                  val response = client.post("/repos/$owner/$repo/actions/runners/$runnerId/labels") {
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
                private val owner: String,
                private val repo: String,
                private val runnerId: Long,
              ) {
                public suspend operator fun invoke(labels: List<String>): Response {
                  val response = client.put("/repos/$owner/$repo/actions/runners/$runnerId/labels") {
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
                private val owner: String,
                private val repo: String,
                private val runnerId: Long,
                private val name: String,
              ) {
                public val delete: Delete = Delete(client, owner, repo, runnerId, name)

                public class Delete internal constructor(
                  private val client: HttpClient,
                  private val owner: String,
                  private val repo: String,
                  private val runnerId: Long,
                  private val name: String,
                ) {
                  public suspend operator fun invoke(): Response {
                    val response = client.delete("/repos/$owner/$repo/actions/runners/$runnerId/labels/$name")
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public val `get`: Get = Get(client, owner, repo)

          public fun runId(runId: Long): RunIdPath = RunIdPath(client, owner, repo, runId)

          public class Get internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
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
            ): Response = client.get("/repos/$owner/$repo/actions/runs") {
              actor?.let { parameter("actor", it) }
              branch?.let { parameter("branch", it) }
              event?.let { parameter("event", it) }
              status?.let { parameter("status", it.value) }
              perPage?.let { parameter("per_page", it) }
              page?.let { parameter("page", it) }
              created?.let { parameter("created", it.toString()) }
              excludePullRequests?.let { parameter("exclude_pull_requests", it) }
              checkSuiteId?.let { parameter("check_suite_id", it) }
              headSha?.let { parameter("head_sha", it) }
            }.body()

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
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val runId: Long,
          ) {
            public val delete: Delete = Delete(client, owner, repo, runId)

            public val `get`: Get = Get(client, owner, repo, runId)

            public val approvals: Approvals = Approvals(client, owner, repo, runId)

            public val approve: Approve = Approve(client, owner, repo, runId)

            public val artifacts: Artifacts = Artifacts(client, owner, repo, runId)

            public val attempts: Attempts = Attempts(client, owner, repo, runId)

            public val cancel: Cancel = Cancel(client, owner, repo, runId)

            public val deploymentProtectionRule: DeploymentProtectionRule =
                DeploymentProtectionRule(client, owner, repo, runId)

            public val forceCancel: ForceCancel = ForceCancel(client, owner, repo, runId)

            public val jobs: Jobs = Jobs(client, owner, repo, runId)

            public val logs: Logs = Logs(client, owner, repo, runId)

            public val pendingDeployments: PendingDeployments =
                PendingDeployments(client, owner, repo, runId)

            public val rerun: Rerun = Rerun(client, owner, repo, runId)

            public val rerunFailedJobs: RerunFailedJobs =
                RerunFailedJobs(client, owner, repo, runId)

            public val timing: Timing = Timing(client, owner, repo, runId)

            public class Delete internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val runId: Long,
            ) {
              public suspend operator fun invoke() {
                client.delete("/repos/$owner/$repo/actions/runs/$runId")
              }
            }

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val runId: Long,
            ) {
              public suspend operator fun invoke(excludePullRequests: Boolean? = false): WorkflowRun = client.get("/repos/$owner/$repo/actions/runs/$runId") {
                excludePullRequests?.let { parameter("exclude_pull_requests", it) }
              }.body()
            }

            public class Approvals internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val runId: Long,
            ) {
              public val `get`: Get = Get(client, owner, repo, runId)

              public class Get internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val runId: Long,
              ) {
                public suspend operator fun invoke(): List<EnvironmentApprovals> = client.get("/repos/$owner/$repo/actions/runs/$runId/approvals").body()
              }
            }

            public class Approve internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val runId: Long,
            ) {
              public val post: Post = Post(client, owner, repo, runId)

              public class Post internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val runId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.post("/repos/$owner/$repo/actions/runs/$runId/approve")
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
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val runId: Long,
            ) {
              public val `get`: Get = Get(client, owner, repo, runId)

              public class Get internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val runId: Long,
              ) {
                public suspend operator fun invoke(
                  perPage: Long? = 30L,
                  page: Long? = 1L,
                  name: String? = null,
                  direction: Direction? = Direction.Desc,
                ): Response = client.get("/repos/$owner/$repo/actions/runs/$runId/artifacts") {
                  perPage?.let { parameter("per_page", it) }
                  page?.let { parameter("page", it) }
                  name?.let { parameter("name", it) }
                  direction?.let { parameter("direction", it.value) }
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
                public data class Response(
                  @SerialName("total_count")
                  public val totalCount: Long,
                  public val artifacts: List<Artifact>,
                )
              }
            }

            public class Attempts internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val runId: Long,
            ) {
              public fun attemptNumber(attemptNumber: Long): AttemptNumberPath = AttemptNumberPath(client, owner, repo, runId, attemptNumber)

              public class AttemptNumberPath internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val runId: Long,
                private val attemptNumber: Long,
              ) {
                public val `get`: Get = Get(client, owner, repo, runId, attemptNumber)

                public val jobs: Jobs = Jobs(client, owner, repo, runId, attemptNumber)

                public val logs: Logs = Logs(client, owner, repo, runId, attemptNumber)

                public class Get internal constructor(
                  private val client: HttpClient,
                  private val owner: String,
                  private val repo: String,
                  private val runId: Long,
                  private val attemptNumber: Long,
                ) {
                  public suspend operator fun invoke(excludePullRequests: Boolean? = false): WorkflowRun = client.get("/repos/$owner/$repo/actions/runs/$runId/attempts/$attemptNumber") {
                    excludePullRequests?.let { parameter("exclude_pull_requests", it) }
                  }.body()
                }

                public class Jobs internal constructor(
                  private val client: HttpClient,
                  private val owner: String,
                  private val repo: String,
                  private val runId: Long,
                  private val attemptNumber: Long,
                ) {
                  public val `get`: Get = Get(client, owner, repo, runId, attemptNumber)

                  public class Get internal constructor(
                    private val client: HttpClient,
                    private val owner: String,
                    private val repo: String,
                    private val runId: Long,
                    private val attemptNumber: Long,
                  ) {
                    public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
                      val response = client.get("/repos/$owner/$repo/actions/runs/$runId/attempts/$attemptNumber/jobs") {
                        perPage?.let { parameter("per_page", it) }
                        page?.let { parameter("page", it) }
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
                  private val client: HttpClient,
                  private val owner: String,
                  private val repo: String,
                  private val runId: Long,
                  private val attemptNumber: Long,
                ) {
                  public val `get`: Get = Get(client, owner, repo, runId, attemptNumber)

                  public class Get internal constructor(
                    private val client: HttpClient,
                    private val owner: String,
                    private val repo: String,
                    private val runId: Long,
                    private val attemptNumber: Long,
                  ) {
                    public suspend operator fun invoke() {
                      client.get("/repos/$owner/$repo/actions/runs/$runId/attempts/$attemptNumber/logs")
                    }
                  }
                }
              }
            }

            public class Cancel internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val runId: Long,
            ) {
              public val post: Post = Post(client, owner, repo, runId)

              public class Post internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val runId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.post("/repos/$owner/$repo/actions/runs/$runId/cancel")
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
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val runId: Long,
            ) {
              public val post: Post = Post(client, owner, repo, runId)

              public class Post internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val runId: Long,
              ) {
                public suspend operator fun invoke(body: ReviewCustomGatesCommentRequired) {
                  client.post("/repos/$owner/$repo/actions/runs/$runId/deployment_protection_rule") {
                    contentType(ContentType.Application.Json)
                    setBody(body)
                  }
                }

                public suspend operator fun invoke(body: ReviewCustomGatesStateRequired) {
                  client.post("/repos/$owner/$repo/actions/runs/$runId/deployment_protection_rule") {
                    contentType(ContentType.Application.Json)
                    setBody(body)
                  }
                }
              }
            }

            public class ForceCancel internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val runId: Long,
            ) {
              public val post: Post = Post(client, owner, repo, runId)

              public class Post internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val runId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.post("/repos/$owner/$repo/actions/runs/$runId/force-cancel")
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
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val runId: Long,
            ) {
              public val `get`: Get = Get(client, owner, repo, runId)

              public class Get internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val runId: Long,
              ) {
                public suspend operator fun invoke(
                  filter: Filter? = Filter.Latest,
                  perPage: Long? = 30L,
                  page: Long? = 1L,
                ): Response = client.get("/repos/$owner/$repo/actions/runs/$runId/jobs") {
                  filter?.let { parameter("filter", it.value) }
                  perPage?.let { parameter("per_page", it) }
                  page?.let { parameter("page", it) }
                }.body()

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
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val runId: Long,
            ) {
              public val delete: Delete = Delete(client, owner, repo, runId)

              public val `get`: Get = Get(client, owner, repo, runId)

              public class Delete internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val runId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.delete("/repos/$owner/$repo/actions/runs/$runId/logs")
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
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val runId: Long,
              ) {
                public suspend operator fun invoke() {
                  client.get("/repos/$owner/$repo/actions/runs/$runId/logs")
                }
              }
            }

            public class PendingDeployments internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val runId: Long,
            ) {
              public val `get`: Get = Get(client, owner, repo, runId)

              public val post: Post = Post(client, owner, repo, runId)

              public class Get internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val runId: Long,
              ) {
                public suspend operator fun invoke(): List<PendingDeployment> = client.get("/repos/$owner/$repo/actions/runs/$runId/pending_deployments").body()
              }

              public class Post internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val runId: Long,
              ) {
                public suspend operator fun invoke(
                  environmentIds: List<Long>,
                  state: State,
                  comment: String,
                ): List<Deployment> = client.post("/repos/$owner/$repo/actions/runs/$runId/pending_deployments") {
                  contentType(ContentType.Application.Json)
                  setBody(Body(environmentIds = environmentIds, state = state, comment = comment))
                }.body()

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
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val runId: Long,
            ) {
              public val post: Post = Post(client, owner, repo, runId)

              public class Post internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val runId: Long,
              ) {
                public suspend operator fun invoke(enableDebugLogging: Boolean? = null): EmptyObject = client.post("/repos/$owner/$repo/actions/runs/$runId/rerun") {
                  if (enableDebugLogging != null) {
                    contentType(ContentType.Application.Json)
                    setBody(Body(enableDebugLogging = enableDebugLogging))
                  }
                }.body()

                @JvmInline
                @Serializable
                internal value class Body(
                  @SerialName("enable_debug_logging")
                  public val enableDebugLogging: Boolean? = null,
                )
              }
            }

            public class RerunFailedJobs internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val runId: Long,
            ) {
              public val post: Post = Post(client, owner, repo, runId)

              public class Post internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val runId: Long,
              ) {
                public suspend operator fun invoke(enableDebugLogging: Boolean? = null): EmptyObject = client.post("/repos/$owner/$repo/actions/runs/$runId/rerun-failed-jobs") {
                  if (enableDebugLogging != null) {
                    contentType(ContentType.Application.Json)
                    setBody(Body(enableDebugLogging = enableDebugLogging))
                  }
                }.body()

                @JvmInline
                @Serializable
                internal value class Body(
                  @SerialName("enable_debug_logging")
                  public val enableDebugLogging: Boolean? = null,
                )
              }
            }

            public class Timing internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val runId: Long,
            ) {
              public val `get`: Get = Get(client, owner, repo, runId)

              public class Get internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val runId: Long,
              ) {
                public suspend operator fun invoke(): WorkflowRunUsage = client.get("/repos/$owner/$repo/actions/runs/$runId/timing").body()
              }
            }
          }
        }

        public class Secrets internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public val `get`: Get = Get(client, owner, repo)

          public val publicKey: PublicKey = PublicKey(client, owner, repo)

          public fun secretName(secretName: String): SecretNamePath = SecretNamePath(client, owner, repo, secretName)

          public class Get internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response = client.get("/repos/$owner/$repo/actions/secrets") {
              perPage?.let { parameter("per_page", it) }
              page?.let { parameter("page", it) }
            }.body()

            @Serializable
            public data class Response(
              @SerialName("total_count")
              public val totalCount: Long,
              public val secrets: List<ActionsSecret>,
            )
          }

          public class PublicKey internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public val `get`: Get = Get(client, owner, repo)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
            ) {
              public suspend operator fun invoke(): ActionsPublicKey = client.get("/repos/$owner/$repo/actions/secrets/public-key").body()
            }
          }

          public class SecretNamePath internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val secretName: String,
          ) {
            public val delete: Delete = Delete(client, owner, repo, secretName)

            public val `get`: Get = Get(client, owner, repo, secretName)

            public val put: Put = Put(client, owner, repo, secretName)

            public class Delete internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val secretName: String,
            ) {
              public suspend operator fun invoke() {
                client.delete("/repos/$owner/$repo/actions/secrets/$secretName")
              }
            }

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val secretName: String,
            ) {
              public suspend operator fun invoke(): ActionsSecret = client.get("/repos/$owner/$repo/actions/secrets/$secretName").body()
            }

            public class Put internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val secretName: String,
            ) {
              public suspend operator fun invoke(encryptedValue: String, keyId: String): Response {
                val response = client.put("/repos/$owner/$repo/actions/secrets/$secretName") {
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public val `get`: Get = Get(client, owner, repo)

          public val post: Post = Post(client, owner, repo)

          public fun name(name: String): NamePath = NamePath(client, owner, repo, name)

          public class Get internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public suspend operator fun invoke(perPage: Long? = 10L, page: Long? = 1L): Response = client.get("/repos/$owner/$repo/actions/variables") {
              perPage?.let { parameter("per_page", it) }
              page?.let { parameter("page", it) }
            }.body()

            @Serializable
            public data class Response(
              @SerialName("total_count")
              public val totalCount: Long,
              public val variables: List<ActionsVariable>,
            )
          }

          public class Post internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public suspend operator fun invoke(name: String, `value`: String): EmptyObject = client.post("/repos/$owner/$repo/actions/variables") {
              contentType(ContentType.Application.Json)
              setBody(Body(name = name, value = value))
            }.body()

            @Serializable
            internal data class Body(
              public val name: String,
              public val `value`: String,
            )
          }

          public class NamePath internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val name: String,
          ) {
            public val delete: Delete = Delete(client, owner, repo, name)

            public val `get`: Get = Get(client, owner, repo, name)

            public val patch: Patch = Patch(client, owner, repo, name)

            public class Delete internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val name: String,
            ) {
              public suspend operator fun invoke() {
                client.delete("/repos/$owner/$repo/actions/variables/$name")
              }
            }

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val name: String,
            ) {
              public suspend operator fun invoke(): ActionsVariable = client.get("/repos/$owner/$repo/actions/variables/$name").body()
            }

            public class Patch internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val name: String,
            ) {
              public suspend operator fun invoke(name: String? = null, `value`: String? = null) {
                client.patch("/repos/$owner/$repo/actions/variables/$name") {
                  contentType(ContentType.Application.Json)
                  setBody(Body(name = name, value = value))
                }
              }

              @Serializable
              internal data class Body(
                public val name: String? = null,
                public val `value`: String? = null,
              )
            }
          }
        }

        public class Workflows internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public val `get`: Get = Get(client, owner, repo)

          public fun workflowId(workflowId: Long): WorkflowIdPath = WorkflowIdPath(client, owner, repo, workflowId.toString())

          public fun workflowId(workflowId: String): WorkflowIdPath = WorkflowIdPath(client, owner, repo, workflowId)

          public class Get internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response = client.get("/repos/$owner/$repo/actions/workflows") {
              perPage?.let { parameter("per_page", it) }
              page?.let { parameter("page", it) }
            }.body()

            @Serializable
            public data class Response(
              @SerialName("total_count")
              public val totalCount: Long,
              public val workflows: List<Workflow>,
            )
          }

          public class WorkflowIdPath internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val workflowId: String,
          ) {
            public val `get`: Get = Get(client, owner, repo, workflowId)

            public val disable: Disable = Disable(client, owner, repo, workflowId)

            public val dispatches: Dispatches = Dispatches(client, owner, repo, workflowId)

            public val enable: Enable = Enable(client, owner, repo, workflowId)

            public val runs: Runs = Runs(client, owner, repo, workflowId)

            public val timing: Timing = Timing(client, owner, repo, workflowId)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val workflowId: String,
            ) {
              public suspend operator fun invoke(): Workflow = client.get("/repos/$owner/$repo/actions/workflows/$workflowId").body()
            }

            public class Disable internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val workflowId: String,
            ) {
              public val put: Put = Put(client, owner, repo, workflowId)

              public class Put internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val workflowId: String,
              ) {
                public suspend operator fun invoke() {
                  client.put("/repos/$owner/$repo/actions/workflows/$workflowId/disable")
                }
              }
            }

            public class Dispatches internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val workflowId: String,
            ) {
              public val post: Post = Post(client, owner, repo, workflowId)

              public class Post internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val workflowId: String,
              ) {
                public suspend operator fun invoke(
                  ref: String,
                  inputs: JsonElement? = null,
                  returnRunDetails: Boolean? = null,
                ): Response {
                  val response = client.post("/repos/$owner/$repo/actions/workflows/$workflowId/dispatches") {
                    contentType(ContentType.Application.Json)
                    setBody(Body(ref = ref, inputs = inputs, returnRunDetails = returnRunDetails))
                  }
                  return when (response.status.value) {
                    200 -> Response.Ok(response.body())
                    204 -> Response.NoContent
                    else -> throw ResponseException(response, "")
                  }
                }

                @Serializable
                internal data class Body(
                  public val ref: String,
                  public val inputs: JsonElement? = null,
                  @SerialName("return_run_details")
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
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val workflowId: String,
            ) {
              public val put: Put = Put(client, owner, repo, workflowId)

              public class Put internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val workflowId: String,
              ) {
                public suspend operator fun invoke() {
                  client.put("/repos/$owner/$repo/actions/workflows/$workflowId/enable")
                }
              }
            }

            public class Runs internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val workflowId: String,
            ) {
              public val `get`: Get = Get(client, owner, repo, workflowId)

              public class Get internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val workflowId: String,
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
                ): Response = client.get("/repos/$owner/$repo/actions/workflows/$workflowId/runs") {
                  actor?.let { parameter("actor", it) }
                  branch?.let { parameter("branch", it) }
                  event?.let { parameter("event", it) }
                  status?.let { parameter("status", it.value) }
                  perPage?.let { parameter("per_page", it) }
                  page?.let { parameter("page", it) }
                  created?.let { parameter("created", it.toString()) }
                  excludePullRequests?.let { parameter("exclude_pull_requests", it) }
                  checkSuiteId?.let { parameter("check_suite_id", it) }
                  headSha?.let { parameter("head_sha", it) }
                }.body()

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
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val workflowId: String,
            ) {
              public val `get`: Get = Get(client, owner, repo, workflowId)

              public class Get internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val workflowId: String,
              ) {
                public suspend operator fun invoke(): WorkflowUsage = client.get("/repos/$owner/$repo/actions/workflows/$workflowId/timing").body()
              }
            }
          }
        }
      }

      public class Activity internal constructor(
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val `get`: Get = Get(client, owner, repo)

        public class Get internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
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
            val response = client.get("/repos/$owner/$repo/activity") {
              direction?.let { parameter("direction", it.value) }
              perPage?.let { parameter("per_page", it) }
              before?.let { parameter("before", it) }
              after?.let { parameter("after", it) }
              ref?.let { parameter("ref", it) }
              actor?.let { parameter("actor", it) }
              timePeriod?.let { parameter("time_period", it.value) }
              activityType?.let { parameter("activity_type", it.value) }
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
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val `get`: Get = Get(client, owner, repo)

        public fun assignee(assignee: String): AssigneePath = AssigneePath(client, owner, repo, assignee)

        public class Get internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
            val response = client.get("/repos/$owner/$repo/assignees") {
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
              public val `value`: List<SimpleUser>,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class AssigneePath internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
          private val assignee: String,
        ) {
          public val `get`: Get = Get(client, owner, repo, assignee)

          public class Get internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val assignee: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.get("/repos/$owner/$repo/assignees/$assignee")
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
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val post: Post = Post(client, owner, repo)

        public fun subjectDigest(subjectDigest: String): SubjectDigestPath = SubjectDigestPath(client, owner, repo, subjectDigest)

        public class Post internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(bundle: Bundle): Response {
            val response = client.post("/repos/$owner/$repo/attestations") {
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
          @Serializable
          public data class Bundle(
            public val mediaType: String? = null,
            public val verificationMaterial: JsonElement? = null,
            public val dsseEnvelope: JsonElement? = null,
          )

          @JvmInline
          @Serializable
          internal value class Body(
            public val bundle: Bundle,
          )

          public sealed interface Response {
            @JvmInline
            @Serializable
            public value class Created(
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
          private val subjectDigest: String,
        ) {
          public val `get`: Get = Get(client, owner, repo, subjectDigest)

          public class Get internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val subjectDigest: String,
          ) {
            public suspend operator fun invoke(
              perPage: Long? = 30L,
              before: String? = null,
              after: String? = null,
              predicateType: String? = null,
            ): Response = client.get("/repos/$owner/$repo/attestations/$subjectDigest") {
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

      public class Autolinks internal constructor(
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val `get`: Get = Get(client, owner, repo)

        public val post: Post = Post(client, owner, repo)

        public fun autolinkId(autolinkId: Long): AutolinkIdPath = AutolinkIdPath(client, owner, repo, autolinkId)

        public class Get internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(): List<Autolink> = client.get("/repos/$owner/$repo/autolinks").body()
        }

        public class Post internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(
            keyPrefix: String,
            urlTemplate: String,
            isAlphanumeric: Boolean? = null,
          ): Response {
            val response = client.post("/repos/$owner/$repo/autolinks") {
              contentType(ContentType.Application.Json)
              setBody(Body(keyPrefix = keyPrefix, urlTemplate = urlTemplate, isAlphanumeric = isAlphanumeric))
            }
            return when (response.status.value) {
              201 -> Response.Created(response.body())
              422 -> Response.UnprocessableEntity(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          @Serializable
          internal data class Body(
            @SerialName("key_prefix")
            public val keyPrefix: String,
            @SerialName("url_template")
            public val urlTemplate: String,
            @SerialName("is_alphanumeric")
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
          private val autolinkId: Long,
        ) {
          public val delete: Delete = Delete(client, owner, repo, autolinkId)

          public val `get`: Get = Get(client, owner, repo, autolinkId)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val autolinkId: Long,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.delete("/repos/$owner/$repo/autolinks/$autolinkId")
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
            private val owner: String,
            private val repo: String,
            private val autolinkId: Long,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.get("/repos/$owner/$repo/autolinks/$autolinkId")
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
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val delete: Delete = Delete(client, owner, repo)

        public val `get`: Get = Get(client, owner, repo)

        public val put: Put = Put(client, owner, repo)

        public class Delete internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke() {
            client.delete("/repos/$owner/$repo/automated-security-fixes")
          }
        }

        public class Get internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.get("/repos/$owner/$repo/automated-security-fixes")
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke() {
            client.put("/repos/$owner/$repo/automated-security-fixes")
          }
        }
      }

      public class Branches internal constructor(
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val `get`: Get = Get(client, owner, repo)

        public fun branch(branch: String): BranchPath = BranchPath(client, owner, repo, branch)

        public class Get internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(
            `protected`: Boolean? = null,
            perPage: Long? = 30L,
            page: Long? = 1L,
          ): Response {
            val response = client.get("/repos/$owner/$repo/branches") {
              protected?.let { parameter("protected", it) }
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
              public val `value`: List<ShortBranch>,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class BranchPath internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
          private val branch: String,
        ) {
          public val `get`: Get = Get(client, owner, repo, branch)

          public val protection: Protection = Protection(client, owner, repo, branch)

          public val rename: Rename = Rename(client, owner, repo, branch)

          public class Get internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val branch: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.get("/repos/$owner/$repo/branches/$branch")
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
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val branch: String,
          ) {
            public val delete: Delete = Delete(client, owner, repo, branch)

            public val `get`: Get = Get(client, owner, repo, branch)

            public val put: Put = Put(client, owner, repo, branch)

            public val enforceAdmins: EnforceAdmins = EnforceAdmins(client, owner, repo, branch)

            public val requiredPullRequestReviews: RequiredPullRequestReviews =
                RequiredPullRequestReviews(client, owner, repo, branch)

            public val requiredSignatures: RequiredSignatures =
                RequiredSignatures(client, owner, repo, branch)

            public val requiredStatusChecks: RequiredStatusChecks =
                RequiredStatusChecks(client, owner, repo, branch)

            public val restrictions: Restrictions = Restrictions(client, owner, repo, branch)

            public class Delete internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val branch: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.delete("/repos/$owner/$repo/branches/$branch/protection")
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
              private val owner: String,
              private val repo: String,
              private val branch: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.get("/repos/$owner/$repo/branches/$branch/protection")
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
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val branch: String,
            ) {
              public suspend operator fun invoke(
                requiredStatusChecks: RequiredStatusChecks?,
                enforceAdmins: Boolean?,
                requiredPullRequestReviews: RequiredPullRequestReviews?,
                restrictions: Restrictions?,
                requiredLinearHistory: Boolean? = null,
                allowForcePushes: Boolean? = null,
                allowDeletions: Boolean? = null,
                blockCreations: Boolean? = null,
                requiredConversationResolution: Boolean? = null,
                lockBranch: Boolean? = null,
                allowForkSyncing: Boolean? = null,
              ): Response {
                val response = client.put("/repos/$owner/$repo/branches/$branch/protection") {
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
              @Serializable
              public data class RequiredStatusChecks(
                public val strict: Boolean,
                public val contexts: List<String>,
                public val checks: List<Checks>? = null,
              ) {
                @Serializable
                public data class Checks(
                  public val context: String,
                  @SerialName("app_id")
                  public val appId: Long? = null,
                )
              }

              /**
               * Require at least one approving review on a pull request, before merging. Set to `null` to disable.
               */
              @Serializable
              public data class RequiredPullRequestReviews(
                @SerialName("dismissal_restrictions")
                public val dismissalRestrictions: DismissalRestrictions? = null,
                @SerialName("dismiss_stale_reviews")
                public val dismissStaleReviews: Boolean? = null,
                @SerialName("require_code_owner_reviews")
                public val requireCodeOwnerReviews: Boolean? = null,
                @SerialName("required_approving_review_count")
                public val requiredApprovingReviewCount: Long? = null,
                @SerialName("require_last_push_approval")
                public val requireLastPushApproval: Boolean? = null,
                @SerialName("bypass_pull_request_allowances")
                public val bypassPullRequestAllowances: BypassPullRequestAllowances? = null,
              ) {
                /**
                 * Allow specific users, teams, or apps to bypass pull request requirements.
                 */
                @Serializable
                public data class BypassPullRequestAllowances(
                  public val users: List<String>? = null,
                  public val teams: List<String>? = null,
                  public val apps: List<String>? = null,
                )

                /**
                 * Specify which users, teams, and apps can dismiss pull request reviews. Pass an empty `dismissal_restrictions` object to disable. User and team `dismissal_restrictions` are only available for organization-owned repositories. Omit this parameter for personal repositories.
                 */
                @Serializable
                public data class DismissalRestrictions(
                  public val users: List<String>? = null,
                  public val teams: List<String>? = null,
                  public val apps: List<String>? = null,
                )
              }

              /**
               * Restrict who can push to the protected branch. User, app, and team `restrictions` are only available for organization-owned repositories. Set to `null` to disable.
               */
              @Serializable
              public data class Restrictions(
                public val users: List<String>,
                public val teams: List<String>,
                public val apps: List<String>? = null,
              )

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
                public val requiredLinearHistory: Boolean? = null,
                @SerialName("allow_force_pushes")
                public val allowForcePushes: Boolean? = null,
                @SerialName("allow_deletions")
                public val allowDeletions: Boolean? = null,
                @SerialName("block_creations")
                public val blockCreations: Boolean? = null,
                @SerialName("required_conversation_resolution")
                public val requiredConversationResolution: Boolean? = null,
                @SerialName("lock_branch")
                public val lockBranch: Boolean? = null,
                @SerialName("allow_fork_syncing")
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
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val branch: String,
            ) {
              public val delete: Delete = Delete(client, owner, repo, branch)

              public val `get`: Get = Get(client, owner, repo, branch)

              public val post: Post = Post(client, owner, repo, branch)

              public class Delete internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val branch: String,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.delete("/repos/$owner/$repo/branches/$branch/protection/enforce_admins")
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
                private val owner: String,
                private val repo: String,
                private val branch: String,
              ) {
                public suspend operator fun invoke(): ProtectedBranchAdminEnforced = client.get("/repos/$owner/$repo/branches/$branch/protection/enforce_admins").body()
              }

              public class Post internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val branch: String,
              ) {
                public suspend operator fun invoke(): ProtectedBranchAdminEnforced = client.post("/repos/$owner/$repo/branches/$branch/protection/enforce_admins").body()
              }
            }

            public class RequiredPullRequestReviews internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val branch: String,
            ) {
              public val delete: Delete = Delete(client, owner, repo, branch)

              public val `get`: Get = Get(client, owner, repo, branch)

              public val patch: Patch = Patch(client, owner, repo, branch)

              public class Delete internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val branch: String,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.delete("/repos/$owner/$repo/branches/$branch/protection/required_pull_request_reviews")
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
                private val owner: String,
                private val repo: String,
                private val branch: String,
              ) {
                public suspend operator fun invoke(): ProtectedBranchPullRequestReview = client.get("/repos/$owner/$repo/branches/$branch/protection/required_pull_request_reviews").body()
              }

              public class Patch internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val branch: String,
              ) {
                public suspend operator fun invoke(
                  dismissalRestrictions: DismissalRestrictions? = null,
                  dismissStaleReviews: Boolean? = null,
                  requireCodeOwnerReviews: Boolean? = null,
                  requiredApprovingReviewCount: Long? = null,
                  requireLastPushApproval: Boolean? = null,
                  bypassPullRequestAllowances: BypassPullRequestAllowances? = null,
                ): Response {
                  val response = client.patch("/repos/$owner/$repo/branches/$branch/protection/required_pull_request_reviews") {
                    if (dismissalRestrictions != null || dismissStaleReviews != null || requireCodeOwnerReviews != null || requiredApprovingReviewCount != null || requireLastPushApproval != null || bypassPullRequestAllowances != null) {
                      contentType(ContentType.Application.Json)
                      setBody(Body(dismissalRestrictions = dismissalRestrictions, dismissStaleReviews = dismissStaleReviews, requireCodeOwnerReviews = requireCodeOwnerReviews, requiredApprovingReviewCount = requiredApprovingReviewCount, requireLastPushApproval = requireLastPushApproval, bypassPullRequestAllowances = bypassPullRequestAllowances))
                    }
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
                @Serializable
                public data class DismissalRestrictions(
                  public val users: List<String>? = null,
                  public val teams: List<String>? = null,
                  public val apps: List<String>? = null,
                )

                /**
                 * Allow specific users, teams, or apps to bypass pull request requirements.
                 */
                @Serializable
                public data class BypassPullRequestAllowances(
                  public val users: List<String>? = null,
                  public val teams: List<String>? = null,
                  public val apps: List<String>? = null,
                )

                @Serializable
                internal data class Body(
                  @SerialName("dismissal_restrictions")
                  public val dismissalRestrictions: DismissalRestrictions? = null,
                  @SerialName("dismiss_stale_reviews")
                  public val dismissStaleReviews: Boolean? = null,
                  @SerialName("require_code_owner_reviews")
                  public val requireCodeOwnerReviews: Boolean? = null,
                  @SerialName("required_approving_review_count")
                  public val requiredApprovingReviewCount: Long? = null,
                  @SerialName("require_last_push_approval")
                  public val requireLastPushApproval: Boolean? = null,
                  @SerialName("bypass_pull_request_allowances")
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
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val branch: String,
            ) {
              public val delete: Delete = Delete(client, owner, repo, branch)

              public val `get`: Get = Get(client, owner, repo, branch)

              public val post: Post = Post(client, owner, repo, branch)

              public class Delete internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val branch: String,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.delete("/repos/$owner/$repo/branches/$branch/protection/required_signatures")
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
                private val owner: String,
                private val repo: String,
                private val branch: String,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.get("/repos/$owner/$repo/branches/$branch/protection/required_signatures")
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
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val branch: String,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.post("/repos/$owner/$repo/branches/$branch/protection/required_signatures")
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
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val branch: String,
            ) {
              public val delete: Delete = Delete(client, owner, repo, branch)

              public val `get`: Get = Get(client, owner, repo, branch)

              public val patch: Patch = Patch(client, owner, repo, branch)

              public val contexts: Contexts = Contexts(client, owner, repo, branch)

              public class Delete internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val branch: String,
              ) {
                public suspend operator fun invoke() {
                  client.delete("/repos/$owner/$repo/branches/$branch/protection/required_status_checks")
                }
              }

              public class Get internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val branch: String,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.get("/repos/$owner/$repo/branches/$branch/protection/required_status_checks")
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
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val branch: String,
              ) {
                public suspend operator fun invoke(
                  strict: Boolean? = null,
                  contexts: List<String>? = null,
                  checks: List<Checks>? = null,
                ): Response {
                  val response = client.patch("/repos/$owner/$repo/branches/$branch/protection/required_status_checks") {
                    if (strict != null || contexts != null || checks != null) {
                      contentType(ContentType.Application.Json)
                      setBody(Body(strict = strict, contexts = contexts, checks = checks))
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
                public data class Checks(
                  public val context: String,
                  @SerialName("app_id")
                  public val appId: Long? = null,
                )

                @Serializable
                internal data class Body(
                  public val strict: Boolean? = null,
                  public val contexts: List<String>? = null,
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
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val branch: String,
              ) {
                public val delete: Delete = Delete(client, owner, repo, branch)

                public val `get`: Get = Get(client, owner, repo, branch)

                public val post: Post = Post(client, owner, repo, branch)

                public val put: Put = Put(client, owner, repo, branch)

                public class Delete internal constructor(
                  private val client: HttpClient,
                  private val owner: String,
                  private val repo: String,
                  private val branch: String,
                ) {
                  public suspend operator fun invoke(): Response {
                    val response = client.delete("/repos/$owner/$repo/branches/$branch/protection/required_status_checks/contexts")
                    return when (response.status.value) {
                      200 -> Response.Ok(response.body())
                      404 -> Response.NotFound(response.body())
                      422 -> Response.UnprocessableEntity(response.body())
                      else -> throw ResponseException(response, "")
                    }
                  }

                  public suspend operator fun invoke(body: ContextsStrings): Response {
                    val response = client.delete("/repos/$owner/$repo/branches/$branch/protection/required_status_checks/contexts") {
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
                    val response = client.delete("/repos/$owner/$repo/branches/$branch/protection/required_status_checks/contexts") {
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

                  @JvmInline
                  @Serializable
                  public value class ContextsStrings(
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
                  private val client: HttpClient,
                  private val owner: String,
                  private val repo: String,
                  private val branch: String,
                ) {
                  public suspend operator fun invoke(): Response {
                    val response = client.get("/repos/$owner/$repo/branches/$branch/protection/required_status_checks/contexts")
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
                  private val client: HttpClient,
                  private val owner: String,
                  private val repo: String,
                  private val branch: String,
                ) {
                  public suspend operator fun invoke(): Response {
                    val response = client.post("/repos/$owner/$repo/branches/$branch/protection/required_status_checks/contexts")
                    return when (response.status.value) {
                      200 -> Response.Ok(response.body())
                      403 -> Response.Forbidden(response.body())
                      404 -> Response.NotFound(response.body())
                      422 -> Response.UnprocessableEntity(response.body())
                      else -> throw ResponseException(response, "")
                    }
                  }

                  public suspend operator fun invoke(body: ContextsStrings): Response {
                    val response = client.post("/repos/$owner/$repo/branches/$branch/protection/required_status_checks/contexts") {
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
                    val response = client.post("/repos/$owner/$repo/branches/$branch/protection/required_status_checks/contexts") {
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

                  @JvmInline
                  @Serializable
                  public value class ContextsStrings(
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
                  private val client: HttpClient,
                  private val owner: String,
                  private val repo: String,
                  private val branch: String,
                ) {
                  public suspend operator fun invoke(): Response {
                    val response = client.put("/repos/$owner/$repo/branches/$branch/protection/required_status_checks/contexts")
                    return when (response.status.value) {
                      200 -> Response.Ok(response.body())
                      404 -> Response.NotFound(response.body())
                      422 -> Response.UnprocessableEntity(response.body())
                      else -> throw ResponseException(response, "")
                    }
                  }

                  public suspend operator fun invoke(body: ContextsStrings): Response {
                    val response = client.put("/repos/$owner/$repo/branches/$branch/protection/required_status_checks/contexts") {
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
                    val response = client.put("/repos/$owner/$repo/branches/$branch/protection/required_status_checks/contexts") {
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

                  @JvmInline
                  @Serializable
                  public value class ContextsStrings(
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
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val branch: String,
            ) {
              public val delete: Delete = Delete(client, owner, repo, branch)

              public val `get`: Get = Get(client, owner, repo, branch)

              public val apps: Apps = Apps(client, owner, repo, branch)

              public val teams: Teams = Teams(client, owner, repo, branch)

              public val users: Users = Users(client, owner, repo, branch)

              public class Delete internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val branch: String,
              ) {
                public suspend operator fun invoke() {
                  client.delete("/repos/$owner/$repo/branches/$branch/protection/restrictions")
                }
              }

              public class Get internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val branch: String,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.get("/repos/$owner/$repo/branches/$branch/protection/restrictions")
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
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val branch: String,
              ) {
                public val delete: Delete = Delete(client, owner, repo, branch)

                public val `get`: Get = Get(client, owner, repo, branch)

                public val post: Post = Post(client, owner, repo, branch)

                public val put: Put = Put(client, owner, repo, branch)

                public class Delete internal constructor(
                  private val client: HttpClient,
                  private val owner: String,
                  private val repo: String,
                  private val branch: String,
                ) {
                  public suspend operator fun invoke(apps: List<String>): Response {
                    val response = client.delete("/repos/$owner/$repo/branches/$branch/protection/restrictions/apps") {
                      contentType(ContentType.Application.Json)
                      setBody(Body(apps = apps))
                    }
                    return when (response.status.value) {
                      200 -> Response.Ok(response.body())
                      422 -> Response.UnprocessableEntity(response.body())
                      else -> throw ResponseException(response, "")
                    }
                  }

                  @JvmInline
                  @Serializable
                  internal value class Body(
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
                  private val client: HttpClient,
                  private val owner: String,
                  private val repo: String,
                  private val branch: String,
                ) {
                  public suspend operator fun invoke(): Response {
                    val response = client.get("/repos/$owner/$repo/branches/$branch/protection/restrictions/apps")
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
                  private val client: HttpClient,
                  private val owner: String,
                  private val repo: String,
                  private val branch: String,
                ) {
                  public suspend operator fun invoke(apps: List<String>): Response {
                    val response = client.post("/repos/$owner/$repo/branches/$branch/protection/restrictions/apps") {
                      contentType(ContentType.Application.Json)
                      setBody(Body(apps = apps))
                    }
                    return when (response.status.value) {
                      200 -> Response.Ok(response.body())
                      422 -> Response.UnprocessableEntity(response.body())
                      else -> throw ResponseException(response, "")
                    }
                  }

                  @JvmInline
                  @Serializable
                  internal value class Body(
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
                  private val client: HttpClient,
                  private val owner: String,
                  private val repo: String,
                  private val branch: String,
                ) {
                  public suspend operator fun invoke(apps: List<String>): Response {
                    val response = client.put("/repos/$owner/$repo/branches/$branch/protection/restrictions/apps") {
                      contentType(ContentType.Application.Json)
                      setBody(Body(apps = apps))
                    }
                    return when (response.status.value) {
                      200 -> Response.Ok(response.body())
                      422 -> Response.UnprocessableEntity(response.body())
                      else -> throw ResponseException(response, "")
                    }
                  }

                  @JvmInline
                  @Serializable
                  internal value class Body(
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
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val branch: String,
              ) {
                public val delete: Delete = Delete(client, owner, repo, branch)

                public val `get`: Get = Get(client, owner, repo, branch)

                public val post: Post = Post(client, owner, repo, branch)

                public val put: Put = Put(client, owner, repo, branch)

                public class Delete internal constructor(
                  private val client: HttpClient,
                  private val owner: String,
                  private val repo: String,
                  private val branch: String,
                ) {
                  public suspend operator fun invoke(): Response {
                    val response = client.delete("/repos/$owner/$repo/branches/$branch/protection/restrictions/teams")
                    return when (response.status.value) {
                      200 -> Response.Ok(response.body())
                      422 -> Response.UnprocessableEntity(response.body())
                      else -> throw ResponseException(response, "")
                    }
                  }

                  public suspend operator fun invoke(body: TeamsStrings): Response {
                    val response = client.delete("/repos/$owner/$repo/branches/$branch/protection/restrictions/teams") {
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
                    val response = client.delete("/repos/$owner/$repo/branches/$branch/protection/restrictions/teams") {
                      contentType(ContentType.Application.Json)
                      setBody(body)
                    }
                    return when (response.status.value) {
                      200 -> Response.Ok(response.body())
                      422 -> Response.UnprocessableEntity(response.body())
                      else -> throw ResponseException(response, "")
                    }
                  }

                  @JvmInline
                  @Serializable
                  public value class TeamsStrings(
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
                  private val client: HttpClient,
                  private val owner: String,
                  private val repo: String,
                  private val branch: String,
                ) {
                  public suspend operator fun invoke(): Response {
                    val response = client.get("/repos/$owner/$repo/branches/$branch/protection/restrictions/teams")
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
                  private val client: HttpClient,
                  private val owner: String,
                  private val repo: String,
                  private val branch: String,
                ) {
                  public suspend operator fun invoke(): Response {
                    val response = client.post("/repos/$owner/$repo/branches/$branch/protection/restrictions/teams")
                    return when (response.status.value) {
                      200 -> Response.Ok(response.body())
                      422 -> Response.UnprocessableEntity(response.body())
                      else -> throw ResponseException(response, "")
                    }
                  }

                  public suspend operator fun invoke(body: TeamsStrings): Response {
                    val response = client.post("/repos/$owner/$repo/branches/$branch/protection/restrictions/teams") {
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
                    val response = client.post("/repos/$owner/$repo/branches/$branch/protection/restrictions/teams") {
                      contentType(ContentType.Application.Json)
                      setBody(body)
                    }
                    return when (response.status.value) {
                      200 -> Response.Ok(response.body())
                      422 -> Response.UnprocessableEntity(response.body())
                      else -> throw ResponseException(response, "")
                    }
                  }

                  @JvmInline
                  @Serializable
                  public value class TeamsStrings(
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
                  private val client: HttpClient,
                  private val owner: String,
                  private val repo: String,
                  private val branch: String,
                ) {
                  public suspend operator fun invoke(): Response {
                    val response = client.put("/repos/$owner/$repo/branches/$branch/protection/restrictions/teams")
                    return when (response.status.value) {
                      200 -> Response.Ok(response.body())
                      422 -> Response.UnprocessableEntity(response.body())
                      else -> throw ResponseException(response, "")
                    }
                  }

                  public suspend operator fun invoke(body: TeamsStrings): Response {
                    val response = client.put("/repos/$owner/$repo/branches/$branch/protection/restrictions/teams") {
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
                    val response = client.put("/repos/$owner/$repo/branches/$branch/protection/restrictions/teams") {
                      contentType(ContentType.Application.Json)
                      setBody(body)
                    }
                    return when (response.status.value) {
                      200 -> Response.Ok(response.body())
                      422 -> Response.UnprocessableEntity(response.body())
                      else -> throw ResponseException(response, "")
                    }
                  }

                  @JvmInline
                  @Serializable
                  public value class TeamsStrings(
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
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val branch: String,
              ) {
                public val delete: Delete = Delete(client, owner, repo, branch)

                public val `get`: Get = Get(client, owner, repo, branch)

                public val post: Post = Post(client, owner, repo, branch)

                public val put: Put = Put(client, owner, repo, branch)

                public class Delete internal constructor(
                  private val client: HttpClient,
                  private val owner: String,
                  private val repo: String,
                  private val branch: String,
                ) {
                  public suspend operator fun invoke(users: List<String>): Response {
                    val response = client.delete("/repos/$owner/$repo/branches/$branch/protection/restrictions/users") {
                      contentType(ContentType.Application.Json)
                      setBody(Body(users = users))
                    }
                    return when (response.status.value) {
                      200 -> Response.Ok(response.body())
                      422 -> Response.UnprocessableEntity(response.body())
                      else -> throw ResponseException(response, "")
                    }
                  }

                  @JvmInline
                  @Serializable
                  internal value class Body(
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
                  private val client: HttpClient,
                  private val owner: String,
                  private val repo: String,
                  private val branch: String,
                ) {
                  public suspend operator fun invoke(): Response {
                    val response = client.get("/repos/$owner/$repo/branches/$branch/protection/restrictions/users")
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
                  private val client: HttpClient,
                  private val owner: String,
                  private val repo: String,
                  private val branch: String,
                ) {
                  public suspend operator fun invoke(users: List<String>): Response {
                    val response = client.post("/repos/$owner/$repo/branches/$branch/protection/restrictions/users") {
                      contentType(ContentType.Application.Json)
                      setBody(Body(users = users))
                    }
                    return when (response.status.value) {
                      200 -> Response.Ok(response.body())
                      422 -> Response.UnprocessableEntity(response.body())
                      else -> throw ResponseException(response, "")
                    }
                  }

                  @JvmInline
                  @Serializable
                  internal value class Body(
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
                  private val client: HttpClient,
                  private val owner: String,
                  private val repo: String,
                  private val branch: String,
                ) {
                  public suspend operator fun invoke(users: List<String>): Response {
                    val response = client.put("/repos/$owner/$repo/branches/$branch/protection/restrictions/users") {
                      contentType(ContentType.Application.Json)
                      setBody(Body(users = users))
                    }
                    return when (response.status.value) {
                      200 -> Response.Ok(response.body())
                      422 -> Response.UnprocessableEntity(response.body())
                      else -> throw ResponseException(response, "")
                    }
                  }

                  @JvmInline
                  @Serializable
                  internal value class Body(
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
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val branch: String,
          ) {
            public val post: Post = Post(client, owner, repo, branch)

            public class Post internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val branch: String,
            ) {
              public suspend operator fun invoke(newName: String): Response {
                val response = client.post("/repos/$owner/$repo/branches/$branch/rename") {
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

              @JvmInline
              @Serializable
              internal value class Body(
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
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val post: Post = Post(client, owner, repo)

        public fun checkRunId(checkRunId: Long): CheckRunIdPath = CheckRunIdPath(client, owner, repo, checkRunId)

        public class Post internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
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
          ): CheckRun = client.post("/repos/$owner/$repo/check-runs") {
            contentType(ContentType.Application.Json)
            setBody(Body(name = name, headSha = headSha, detailsUrl = detailsUrl, externalId = externalId, status = status, startedAt = startedAt, conclusion = conclusion, completedAt = completedAt, output = output, actions = actions))
          }.body()

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
          @Serializable
          public data class Output(
            public val title: String,
            public val summary: String,
            public val text: String? = null,
            public val annotations: List<Annotations>? = null,
            public val images: List<Images>? = null,
          ) {
            @Serializable
            public data class Annotations(
              public val path: String,
              @SerialName("start_line")
              public val startLine: Long,
              @SerialName("end_line")
              public val endLine: Long,
              @SerialName("start_column")
              public val startColumn: Long? = null,
              @SerialName("end_column")
              public val endColumn: Long? = null,
              @SerialName("annotation_level")
              public val annotationLevel: AnnotationLevel,
              public val message: String,
              public val title: String? = null,
              @SerialName("raw_details")
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

            @Serializable
            public data class Images(
              public val alt: String,
              @SerialName("image_url")
              public val imageUrl: String,
              public val caption: String? = null,
            )
          }

          @Serializable
          public data class Actions(
            public val label: String,
            public val description: String,
            public val identifier: String,
          )

          @Serializable
          internal data class Body(
            public val name: String,
            @SerialName("head_sha")
            public val headSha: String,
            @SerialName("details_url")
            public val detailsUrl: String? = null,
            @SerialName("external_id")
            public val externalId: String? = null,
            public val status: Status? = null,
            @SerialName("started_at")
            public val startedAt: Instant? = null,
            public val conclusion: Conclusion? = null,
            @SerialName("completed_at")
            public val completedAt: Instant? = null,
            public val output: Output? = null,
            public val actions: List<Actions>? = null,
          )
        }

        public class CheckRunIdPath internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
          private val checkRunId: Long,
        ) {
          public val `get`: Get = Get(client, owner, repo, checkRunId)

          public val patch: Patch = Patch(client, owner, repo, checkRunId)

          public val annotations: Annotations = Annotations(client, owner, repo, checkRunId)

          public val rerequest: Rerequest = Rerequest(client, owner, repo, checkRunId)

          public class Get internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val checkRunId: Long,
          ) {
            public suspend operator fun invoke(): CheckRun = client.get("/repos/$owner/$repo/check-runs/$checkRunId").body()
          }

          public class Patch internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val checkRunId: Long,
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
            ): CheckRun = client.patch("/repos/$owner/$repo/check-runs/$checkRunId") {
              contentType(ContentType.Application.Json)
              setBody(Body(name = name, detailsUrl = detailsUrl, externalId = externalId, startedAt = startedAt, status = status, conclusion = conclusion, completedAt = completedAt, output = output, actions = actions))
            }.body()

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
            @Serializable
            public data class Output(
              public val title: String? = null,
              public val summary: String,
              public val text: String? = null,
              public val annotations: List<Annotations>? = null,
              public val images: List<Images>? = null,
            ) {
              @Serializable
              public data class Annotations(
                public val path: String,
                @SerialName("start_line")
                public val startLine: Long,
                @SerialName("end_line")
                public val endLine: Long,
                @SerialName("start_column")
                public val startColumn: Long? = null,
                @SerialName("end_column")
                public val endColumn: Long? = null,
                @SerialName("annotation_level")
                public val annotationLevel: AnnotationLevel,
                public val message: String,
                public val title: String? = null,
                @SerialName("raw_details")
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

              @Serializable
              public data class Images(
                public val alt: String,
                @SerialName("image_url")
                public val imageUrl: String,
                public val caption: String? = null,
              )
            }

            @Serializable
            public data class Actions(
              public val label: String,
              public val description: String,
              public val identifier: String,
            )

            @Serializable
            internal data class Body(
              public val name: String? = null,
              @SerialName("details_url")
              public val detailsUrl: String? = null,
              @SerialName("external_id")
              public val externalId: String? = null,
              @SerialName("started_at")
              public val startedAt: Instant? = null,
              public val status: Status? = null,
              public val conclusion: Conclusion? = null,
              @SerialName("completed_at")
              public val completedAt: Instant? = null,
              public val output: Output? = null,
              public val actions: List<Actions>? = null,
            )
          }

          public class Annotations internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val checkRunId: Long,
          ) {
            public val `get`: Get = Get(client, owner, repo, checkRunId)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val checkRunId: Long,
            ) {
              public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<CheckAnnotation> = client.get("/repos/$owner/$repo/check-runs/$checkRunId/annotations") {
                perPage?.let { parameter("per_page", it) }
                page?.let { parameter("page", it) }
              }.body()
            }
          }

          public class Rerequest internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val checkRunId: Long,
          ) {
            public val post: Post = Post(client, owner, repo, checkRunId)

            public class Post internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val checkRunId: Long,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.post("/repos/$owner/$repo/check-runs/$checkRunId/rerequest")
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
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val post: Post = Post(client, owner, repo)

        public val preferences: Preferences = Preferences(client, owner, repo)

        public fun checkSuiteId(checkSuiteId: Long): CheckSuiteIdPath = CheckSuiteIdPath(client, owner, repo, checkSuiteId)

        public class Post internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(headSha: String): Response {
            val response = client.post("/repos/$owner/$repo/check-suites") {
              contentType(ContentType.Application.Json)
              setBody(Body(headSha = headSha))
            }
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              201 -> Response.Created(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          @JvmInline
          @Serializable
          internal value class Body(
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public val patch: Patch = Patch(client, owner, repo)

          public class Patch internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public suspend operator fun invoke(autoTriggerChecks: List<AutoTriggerChecks>? = null): CheckSuitePreference = client.patch("/repos/$owner/$repo/check-suites/preferences") {
              contentType(ContentType.Application.Json)
              setBody(Body(autoTriggerChecks = autoTriggerChecks))
            }.body()

            @Serializable
            public data class AutoTriggerChecks(
              @SerialName("app_id")
              public val appId: Long,
              @Required
              public val setting: Boolean = true,
            )

            @JvmInline
            @Serializable
            internal value class Body(
              @SerialName("auto_trigger_checks")
              public val autoTriggerChecks: List<AutoTriggerChecks>? = null,
            )
          }
        }

        public class CheckSuiteIdPath internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
          private val checkSuiteId: Long,
        ) {
          public val `get`: Get = Get(client, owner, repo, checkSuiteId)

          public val checkRuns: CheckRuns = CheckRuns(client, owner, repo, checkSuiteId)

          public val rerequest: Rerequest = Rerequest(client, owner, repo, checkSuiteId)

          public class Get internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val checkSuiteId: Long,
          ) {
            public suspend operator fun invoke(): CheckSuite = client.get("/repos/$owner/$repo/check-suites/$checkSuiteId").body()
          }

          public class CheckRuns internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val checkSuiteId: Long,
          ) {
            public val `get`: Get = Get(client, owner, repo, checkSuiteId)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val checkSuiteId: Long,
            ) {
              public suspend operator fun invoke(
                checkName: String? = null,
                status: Status? = null,
                filter: Filter? = Filter.Latest,
                perPage: Long? = 30L,
                page: Long? = 1L,
              ): Response = client.get("/repos/$owner/$repo/check-suites/$checkSuiteId/check-runs") {
                checkName?.let { parameter("check_name", it) }
                status?.let { parameter("status", it.value) }
                filter?.let { parameter("filter", it.value) }
                perPage?.let { parameter("per_page", it) }
                page?.let { parameter("page", it) }
              }.body()

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
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val checkSuiteId: Long,
          ) {
            public val post: Post = Post(client, owner, repo, checkSuiteId)

            public class Post internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val checkSuiteId: Long,
            ) {
              public suspend operator fun invoke(): EmptyObject = client.post("/repos/$owner/$repo/check-suites/$checkSuiteId/rerequest").body()
            }
          }
        }
      }

      public class CodeScanning internal constructor(
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val alerts: Alerts = Alerts(client, owner, repo)

        public val analyses: Analyses = Analyses(client, owner, repo)

        public val codeql: Codeql = Codeql(client, owner, repo)

        public val defaultSetup: DefaultSetup = DefaultSetup(client, owner, repo)

        public val sarifs: Sarifs = Sarifs(client, owner, repo)

        public class Alerts internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public val `get`: Get = Get(client, owner, repo)

          public fun alertNumber(alertNumber: Long): AlertNumberPath = AlertNumberPath(client, owner, repo, alertNumber)

          public class Get internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
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
              val response = client.get("/repos/$owner/$repo/code-scanning/alerts") {
                toolName?.let { parameter("tool_name", it) }
                toolGuid?.let { parameter("tool_guid", it) }
                page?.let { parameter("page", it) }
                perPage?.let { parameter("per_page", it) }
                ref?.let { parameter("ref", it) }
                pr?.let { parameter("pr", it) }
                direction?.let { parameter("direction", it.value) }
                before?.let { parameter("before", it) }
                after?.let { parameter("after", it) }
                sort?.let { parameter("sort", it.value) }
                state?.let { parameter("state", it.value) }
                severity?.let { parameter("severity", it.value) }
                assignees?.let { parameter("assignees", it) }
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

              @Serializable
              public data class ServiceUnavailable(
                public val code: String? = null,
                public val message: String? = null,
                @SerialName("documentation_url")
                public val documentationUrl: String? = null,
              ) : Response
            }
          }

          public class AlertNumberPath internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val alertNumber: Long,
          ) {
            public val `get`: Get = Get(client, owner, repo, alertNumber)

            public val patch: Patch = Patch(client, owner, repo, alertNumber)

            public val autofix: Autofix = Autofix(client, owner, repo, alertNumber)

            public val instances: Instances = Instances(client, owner, repo, alertNumber)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val alertNumber: Long,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.get("/repos/$owner/$repo/code-scanning/alerts/$alertNumber")
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
              private val owner: String,
              private val repo: String,
              private val alertNumber: Long,
            ) {
              public suspend operator fun invoke(
                state: CodeScanningAlertSetState? = null,
                dismissedReason: CodeScanningAlertDismissedReason? = null,
                dismissedComment: CodeScanningAlertDismissedComment? = null,
                createRequest: CodeScanningAlertCreateRequest? = null,
                assignees: CodeScanningAlertAssignees? = null,
              ): Response {
                val response = client.patch("/repos/$owner/$repo/code-scanning/alerts/$alertNumber") {
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

              @Serializable
              internal data class Body(
                public val state: CodeScanningAlertSetState? = null,
                @SerialName("dismissed_reason")
                public val dismissedReason: CodeScanningAlertDismissedReason? = null,
                @SerialName("dismissed_comment")
                public val dismissedComment: CodeScanningAlertDismissedComment? = null,
                @SerialName("create_request")
                public val createRequest: CodeScanningAlertCreateRequest? = null,
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

                @Serializable
                public data class ServiceUnavailable(
                  public val code: String? = null,
                  public val message: String? = null,
                  @SerialName("documentation_url")
                  public val documentationUrl: String? = null,
                ) : Response
              }
            }

            public class Autofix internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val alertNumber: Long,
            ) {
              public val `get`: Get = Get(client, owner, repo, alertNumber)

              public val post: Post = Post(client, owner, repo, alertNumber)

              public val commits: Commits = Commits(client, owner, repo, alertNumber)

              public class Get internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val alertNumber: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.get("/repos/$owner/$repo/code-scanning/alerts/$alertNumber/autofix")
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
                private val owner: String,
                private val repo: String,
                private val alertNumber: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.post("/repos/$owner/$repo/code-scanning/alerts/$alertNumber/autofix")
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

                  @Serializable
                  public data class ServiceUnavailable(
                    public val code: String? = null,
                    public val message: String? = null,
                    @SerialName("documentation_url")
                    public val documentationUrl: String? = null,
                  ) : Response
                }
              }

              public class Commits internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val alertNumber: Long,
              ) {
                public val post: Post = Post(client, owner, repo, alertNumber)

                public class Post internal constructor(
                  private val client: HttpClient,
                  private val owner: String,
                  private val repo: String,
                  private val alertNumber: Long,
                ) {
                  public suspend operator fun invoke(body: CodeScanningAutofixCommits? = null): Response {
                    val response = client.post("/repos/$owner/$repo/code-scanning/alerts/$alertNumber/autofix/commits") {
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

            public class Instances internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val alertNumber: Long,
            ) {
              public val `get`: Get = Get(client, owner, repo, alertNumber)

              public class Get internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val alertNumber: Long,
              ) {
                public suspend operator fun invoke(
                  page: Long? = 1L,
                  perPage: Long? = 30L,
                  ref: String? = null,
                  pr: Long? = null,
                ): Response {
                  val response = client.get("/repos/$owner/$repo/code-scanning/alerts/$alertNumber/instances") {
                    page?.let { parameter("page", it) }
                    perPage?.let { parameter("per_page", it) }
                    ref?.let { parameter("ref", it) }
                    pr?.let { parameter("pr", it) }
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
        }

        public class Analyses internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public val `get`: Get = Get(client, owner, repo)

          public fun analysisId(analysisId: Long): AnalysisIdPath = AnalysisIdPath(client, owner, repo, analysisId)

          public class Get internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
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
              val response = client.get("/repos/$owner/$repo/code-scanning/analyses") {
                toolName?.let { parameter("tool_name", it) }
                toolGuid?.let { parameter("tool_guid", it) }
                page?.let { parameter("page", it) }
                perPage?.let { parameter("per_page", it) }
                pr?.let { parameter("pr", it) }
                ref?.let { parameter("ref", it) }
                sarifId?.let { parameter("sarif_id", it) }
                direction?.let { parameter("direction", it.value) }
                sort?.let { parameter("sort", it.value) }
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

              @Serializable
              public data class ServiceUnavailable(
                public val code: String? = null,
                public val message: String? = null,
                @SerialName("documentation_url")
                public val documentationUrl: String? = null,
              ) : Response
            }
          }

          public class AnalysisIdPath internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val analysisId: Long,
          ) {
            public val delete: Delete = Delete(client, owner, repo, analysisId)

            public val `get`: Get = Get(client, owner, repo, analysisId)

            public class Delete internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val analysisId: Long,
            ) {
              public suspend operator fun invoke(confirmDelete: String? = null): Response {
                val response = client.delete("/repos/$owner/$repo/code-scanning/analyses/$analysisId") {
                  confirmDelete?.let { parameter("confirm_delete", it) }
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
              private val owner: String,
              private val repo: String,
              private val analysisId: Long,
            ) {
              public suspend fun json(): JsonResponse {
                val response = client.get("/repos/$owner/$repo/code-scanning/analyses/$analysisId")
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
                val response = client.get("/repos/$owner/$repo/code-scanning/analyses/$analysisId")
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

              @Serializable
              public data class ServiceUnavailable(
                public val code: String? = null,
                public val message: String? = null,
                @SerialName("documentation_url")
                public val documentationUrl: String? = null,
              ) : JsonResponse,
                  SarifJsonResponse
            }
          }
        }

        public class Codeql internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public val databases: Databases = Databases(client, owner, repo)

          public val variantAnalyses: VariantAnalyses = VariantAnalyses(client, owner, repo)

          public class Databases internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public val `get`: Get = Get(client, owner, repo)

            public fun language(language: String): LanguagePath = LanguagePath(client, owner, repo, language)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.get("/repos/$owner/$repo/code-scanning/codeql/databases")
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

                @Serializable
                public data class ServiceUnavailable(
                  public val code: String? = null,
                  public val message: String? = null,
                  @SerialName("documentation_url")
                  public val documentationUrl: String? = null,
                ) : Response
              }
            }

            public class LanguagePath internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val language: String,
            ) {
              public val delete: Delete = Delete(client, owner, repo, language)

              public val `get`: Get = Get(client, owner, repo, language)

              public class Delete internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val language: String,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.delete("/repos/$owner/$repo/code-scanning/codeql/databases/$language")
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
                private val owner: String,
                private val repo: String,
                private val language: String,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.get("/repos/$owner/$repo/code-scanning/codeql/databases/$language")
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

          public class VariantAnalyses internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public val post: Post = Post(client, owner, repo)

            public fun codeqlVariantAnalysisId(codeqlVariantAnalysisId: Long): CodeqlVariantAnalysisIdPath = CodeqlVariantAnalysisIdPath(client, owner, repo, codeqlVariantAnalysisId)

            public class Post internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
            ) {
              public suspend operator fun invoke(
                language: CodeScanningVariantAnalysisLanguage,
                queryPack: String,
                repositories: List<String>? = null,
                repositoryLists: List<String>? = null,
                repositoryOwners: List<String>? = null,
              ): Response {
                val response = client.post("/repos/$owner/$repo/code-scanning/codeql/variant-analyses") {
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

              @Serializable
              internal data class Body(
                public val language: CodeScanningVariantAnalysisLanguage,
                @SerialName("query_pack")
                public val queryPack: String,
                public val repositories: List<String>? = null,
                @SerialName("repository_lists")
                public val repositoryLists: List<String>? = null,
                @SerialName("repository_owners")
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

                @Serializable
                public data class ServiceUnavailable(
                  public val code: String? = null,
                  public val message: String? = null,
                  @SerialName("documentation_url")
                  public val documentationUrl: String? = null,
                ) : Response
              }
            }

            public class CodeqlVariantAnalysisIdPath internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val codeqlVariantAnalysisId: Long,
            ) {
              public val `get`: Get = Get(client, owner, repo, codeqlVariantAnalysisId)

              public val repos: Repos = Repos(client, owner, repo, codeqlVariantAnalysisId)

              public class Get internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val codeqlVariantAnalysisId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.get("/repos/$owner/$repo/code-scanning/codeql/variant-analyses/$codeqlVariantAnalysisId")
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

                  @Serializable
                  public data class ServiceUnavailable(
                    public val code: String? = null,
                    public val message: String? = null,
                    @SerialName("documentation_url")
                    public val documentationUrl: String? = null,
                  ) : Response
                }
              }

              public class Repos internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val codeqlVariantAnalysisId: Long,
              ) {
                public fun repoOwner(repoOwner: String): RepoOwnerPath = RepoOwnerPath(client, owner, repo, codeqlVariantAnalysisId, repoOwner)

                public class RepoOwnerPath internal constructor(
                  private val client: HttpClient,
                  private val owner: String,
                  private val repo: String,
                  private val codeqlVariantAnalysisId: Long,
                  private val repoOwner: String,
                ) {
                  public fun repoName(repoName: String): RepoNamePath = RepoNamePath(client, owner, repo, codeqlVariantAnalysisId, repoOwner, repoName)

                  public class RepoNamePath internal constructor(
                    private val client: HttpClient,
                    private val owner: String,
                    private val repo: String,
                    private val codeqlVariantAnalysisId: Long,
                    private val repoOwner: String,
                    private val repoName: String,
                  ) {
                    public val `get`: Get =
                        Get(client, owner, repo, codeqlVariantAnalysisId, repoOwner, repoName)

                    public class Get internal constructor(
                      private val client: HttpClient,
                      private val owner: String,
                      private val repo: String,
                      private val codeqlVariantAnalysisId: Long,
                      private val repoOwner: String,
                      private val repoName: String,
                    ) {
                      public suspend operator fun invoke(): Response {
                        val response = client.get("/repos/$owner/$repo/code-scanning/codeql/variant-analyses/$codeqlVariantAnalysisId/repos/$repoOwner/$repoName")
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
              }
            }
          }
        }

        public class DefaultSetup internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public val `get`: Get = Get(client, owner, repo)

          public val patch: Patch = Patch(client, owner, repo)

          public class Get internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.get("/repos/$owner/$repo/code-scanning/default-setup")
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
            private val owner: String,
            private val repo: String,
          ) {
            public suspend operator fun invoke(body: CodeScanningDefaultSetupUpdate): Response {
              val response = client.patch("/repos/$owner/$repo/code-scanning/default-setup") {
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

        public class Sarifs internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public val post: Post = Post(client, owner, repo)

          public fun sarifId(sarifId: String): SarifIdPath = SarifIdPath(client, owner, repo, sarifId)

          public class Post internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
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
              val response = client.post("/repos/$owner/$repo/code-scanning/sarifs") {
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

            @Serializable
            internal data class Body(
              @SerialName("commit_sha")
              public val commitSha: CodeScanningAnalysisCommitSha,
              public val ref: CodeScanningRefFull,
              public val sarif: CodeScanningAnalysisSarifFile,
              @SerialName("checkout_uri")
              public val checkoutUri: String? = null,
              @SerialName("started_at")
              public val startedAt: Instant? = null,
              @SerialName("tool_name")
              public val toolName: String? = null,
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

              @Serializable
              public data class ServiceUnavailable(
                public val code: String? = null,
                public val message: String? = null,
                @SerialName("documentation_url")
                public val documentationUrl: String? = null,
              ) : Response
            }
          }

          public class SarifIdPath internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val sarifId: String,
          ) {
            public val `get`: Get = Get(client, owner, repo, sarifId)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val sarifId: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.get("/repos/$owner/$repo/code-scanning/sarifs/$sarifId")
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
      }

      public class CodeSecurityConfiguration internal constructor(
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val `get`: Get = Get(client, owner, repo)

        public class Get internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.get("/repos/$owner/$repo/code-security-configuration")
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
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val errors: Errors = Errors(client, owner, repo)

        public class Errors internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public val `get`: Get = Get(client, owner, repo)

          public class Get internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public suspend operator fun invoke(ref: String? = null): Response {
              val response = client.get("/repos/$owner/$repo/codeowners/errors") {
                ref?.let { parameter("ref", it) }
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
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val `get`: Get = Get(client, owner, repo)

        public val post: Post = Post(client, owner, repo)

        public val devcontainers: Devcontainers = Devcontainers(client, owner, repo)

        public val machines: Machines = Machines(client, owner, repo)

        public val new: New = New(client, owner, repo)

        public val permissionsCheck: PermissionsCheck = PermissionsCheck(client, owner, repo)

        public val secrets: Secrets = Secrets(client, owner, repo)

        public class Get internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
            val response = client.get("/repos/$owner/$repo/codespaces") {
              perPage?.let { parameter("per_page", it) }
              page?.let { parameter("page", it) }
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
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
            val response = client.post("/repos/$owner/$repo/codespaces") {
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

          @Serializable
          internal data class Body(
            public val ref: String? = null,
            public val location: String? = null,
            public val geo: Geo? = null,
            @SerialName("client_ip")
            public val clientIp: String? = null,
            public val machine: String? = null,
            @SerialName("devcontainer_path")
            public val devcontainerPath: String? = null,
            @SerialName("multi_repo_permissions_opt_out")
            public val multiRepoPermissionsOptOut: Boolean? = null,
            @SerialName("working_directory")
            public val workingDirectory: String? = null,
            @SerialName("idle_timeout_minutes")
            public val idleTimeoutMinutes: Long? = null,
            @SerialName("display_name")
            public val displayName: String? = null,
            @SerialName("retention_period_minutes")
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

            @Serializable
            public data class ServiceUnavailable(
              public val code: String? = null,
              public val message: String? = null,
              @SerialName("documentation_url")
              public val documentationUrl: String? = null,
            ) : Response
          }
        }

        public class Devcontainers internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public val `get`: Get = Get(client, owner, repo)

          public class Get internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
              val response = client.get("/repos/$owner/$repo/codespaces/devcontainers") {
                perPage?.let { parameter("per_page", it) }
                page?.let { parameter("page", it) }
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
                @Serializable
                public data class Devcontainers(
                  public val path: String,
                  public val name: String? = null,
                  @SerialName("display_name")
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public val `get`: Get = Get(client, owner, repo)

          public class Get internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public suspend operator fun invoke(
              location: String? = null,
              clientIp: String? = null,
              ref: String? = null,
            ): Response {
              val response = client.get("/repos/$owner/$repo/codespaces/machines") {
                location?.let { parameter("location", it) }
                clientIp?.let { parameter("client_ip", it) }
                ref?.let { parameter("ref", it) }
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public val `get`: Get = Get(client, owner, repo)

          public class Get internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public suspend operator fun invoke(ref: String? = null, clientIp: String? = null): Response {
              val response = client.get("/repos/$owner/$repo/codespaces/new") {
                ref?.let { parameter("ref", it) }
                clientIp?.let { parameter("client_ip", it) }
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
              @Serializable
              public data class Ok(
                @SerialName("billable_owner")
                public val billableOwner: SimpleUser? = null,
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public val `get`: Get = Get(client, owner, repo)

          public class Get internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public suspend operator fun invoke(ref: String, devcontainerPath: String): Response {
              val response = client.get("/repos/$owner/$repo/codespaces/permissions_check") {
                parameter("ref", ref)
                parameter("devcontainer_path", devcontainerPath)
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

        public class Secrets internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public val `get`: Get = Get(client, owner, repo)

          public val publicKey: PublicKey = PublicKey(client, owner, repo)

          public fun secretName(secretName: String): SecretNamePath = SecretNamePath(client, owner, repo, secretName)

          public class Get internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response = client.get("/repos/$owner/$repo/codespaces/secrets") {
              perPage?.let { parameter("per_page", it) }
              page?.let { parameter("page", it) }
            }.body()

            @Serializable
            public data class Response(
              @SerialName("total_count")
              public val totalCount: Long,
              public val secrets: List<RepoCodespacesSecret>,
            )
          }

          public class PublicKey internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public val `get`: Get = Get(client, owner, repo)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
            ) {
              public suspend operator fun invoke(): CodespacesPublicKey = client.get("/repos/$owner/$repo/codespaces/secrets/public-key").body()
            }
          }

          public class SecretNamePath internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val secretName: String,
          ) {
            public val delete: Delete = Delete(client, owner, repo, secretName)

            public val `get`: Get = Get(client, owner, repo, secretName)

            public val put: Put = Put(client, owner, repo, secretName)

            public class Delete internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val secretName: String,
            ) {
              public suspend operator fun invoke() {
                client.delete("/repos/$owner/$repo/codespaces/secrets/$secretName")
              }
            }

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val secretName: String,
            ) {
              public suspend operator fun invoke(): RepoCodespacesSecret = client.get("/repos/$owner/$repo/codespaces/secrets/$secretName").body()
            }

            public class Put internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val secretName: String,
            ) {
              public suspend operator fun invoke(encryptedValue: String? = null, keyId: String? = null): Response {
                val response = client.put("/repos/$owner/$repo/codespaces/secrets/$secretName") {
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
                public val encryptedValue: String? = null,
                @SerialName("key_id")
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
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val `get`: Get = Get(client, owner, repo)

        public fun username(username: String): UsernamePath = UsernamePath(client, owner, repo, username)

        public class Get internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(
            affiliation: Affiliation? = Affiliation.All,
            permission: Permission? = null,
            perPage: Long? = 30L,
            page: Long? = 1L,
          ): Response {
            val response = client.get("/repos/$owner/$repo/collaborators") {
              affiliation?.let { parameter("affiliation", it.value) }
              permission?.let { parameter("permission", it.value) }
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
          private val username: String,
        ) {
          public val delete: Delete = Delete(client, owner, repo, username)

          public val `get`: Get = Get(client, owner, repo, username)

          public val put: Put = Put(client, owner, repo, username)

          public val permission: Permission = Permission(client, owner, repo, username)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val username: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.delete("/repos/$owner/$repo/collaborators/$username")
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
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val username: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.get("/repos/$owner/$repo/collaborators/$username")
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
            private val owner: String,
            private val repo: String,
            private val username: String,
          ) {
            public suspend operator fun invoke(permission: String? = null): Response {
              val response = client.put("/repos/$owner/$repo/collaborators/$username") {
                if (permission != null) {
                  contentType(ContentType.Application.Json)
                  setBody(Body(permission = permission))
                }
              }
              return when (response.status.value) {
                201 -> Response.Created(response.body())
                204 -> Response.NoContent
                403 -> Response.Forbidden(response.body())
                422 -> Response.UnprocessableEntity(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            @JvmInline
            @Serializable
            internal value class Body(
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
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val username: String,
          ) {
            public val `get`: Get = Get(client, owner, repo, username)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val username: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.get("/repos/$owner/$repo/collaborators/$username/permission")
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
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val `get`: Get = Get(client, owner, repo)

        public fun commentId(commentId: Long): CommentIdPath = CommentIdPath(client, owner, repo, commentId)

        public class Get internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<CommitComment> = client.get("/repos/$owner/$repo/comments") {
            perPage?.let { parameter("per_page", it) }
            page?.let { parameter("page", it) }
          }.body()
        }

        public class CommentIdPath internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
          private val commentId: Long,
        ) {
          public val delete: Delete = Delete(client, owner, repo, commentId)

          public val `get`: Get = Get(client, owner, repo, commentId)

          public val patch: Patch = Patch(client, owner, repo, commentId)

          public val reactions: Reactions = Reactions(client, owner, repo, commentId)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val commentId: Long,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.delete("/repos/$owner/$repo/comments/$commentId")
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
            private val owner: String,
            private val repo: String,
            private val commentId: Long,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.get("/repos/$owner/$repo/comments/$commentId")
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
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val commentId: Long,
          ) {
            public suspend operator fun invoke(body: String): Response {
              val response = client.patch("/repos/$owner/$repo/comments/$commentId") {
                contentType(ContentType.Application.Json)
                setBody(Body(body = body))
              }
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            @JvmInline
            @Serializable
            internal value class Body(
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
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val commentId: Long,
          ) {
            public val `get`: Get = Get(client, owner, repo, commentId)

            public val post: Post = Post(client, owner, repo, commentId)

            public fun reactionId(reactionId: Long): ReactionIdPath = ReactionIdPath(client, owner, repo, commentId, reactionId)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val commentId: Long,
            ) {
              public suspend operator fun invoke(
                content: Content? = null,
                perPage: Long? = 30L,
                page: Long? = 1L,
              ): Response {
                val response = client.get("/repos/$owner/$repo/comments/$commentId/reactions") {
                  content?.let { parameter("content", it.value) }
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
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val commentId: Long,
            ) {
              public suspend operator fun invoke(content: Content): Response {
                val response = client.post("/repos/$owner/$repo/comments/$commentId/reactions") {
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

              @JvmInline
              @Serializable
              internal value class Body(
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
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val commentId: Long,
              private val reactionId: Long,
            ) {
              public val delete: Delete = Delete(client, owner, repo, commentId, reactionId)

              public class Delete internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val commentId: Long,
                private val reactionId: Long,
              ) {
                public suspend operator fun invoke() {
                  client.delete("/repos/$owner/$repo/comments/$commentId/reactions/$reactionId")
                }
              }
            }
          }
        }
      }

      public class Commits internal constructor(
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val `get`: Get = Get(client, owner, repo)

        public fun commitSha(commitSha: String): CommitShaPath = CommitShaPath(client, owner, repo, commitSha)

        public fun ref(ref: String): RefPath = RefPath(client, owner, repo, ref)

        public class Get internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
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
            val response = client.get("/repos/$owner/$repo/commits") {
              sha?.let { parameter("sha", it) }
              path?.let { parameter("path", it) }
              author?.let { parameter("author", it) }
              committer?.let { parameter("committer", it) }
              since?.let { parameter("since", it.toString()) }
              until?.let { parameter("until", it.toString()) }
              perPage?.let { parameter("per_page", it) }
              page?.let { parameter("page", it) }
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
          private val commitSha: String,
        ) {
          public val branchesWhereHead: BranchesWhereHead =
              BranchesWhereHead(client, owner, repo, commitSha)

          public val comments: Comments = Comments(client, owner, repo, commitSha)

          public val pulls: Pulls = Pulls(client, owner, repo, commitSha)

          public class BranchesWhereHead internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val commitSha: String,
          ) {
            public val `get`: Get = Get(client, owner, repo, commitSha)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val commitSha: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.get("/repos/$owner/$repo/commits/$commitSha/branches-where-head")
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
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val commitSha: String,
          ) {
            public val `get`: Get = Get(client, owner, repo, commitSha)

            public val post: Post = Post(client, owner, repo, commitSha)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val commitSha: String,
            ) {
              public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<CommitComment> = client.get("/repos/$owner/$repo/commits/$commitSha/comments") {
                perPage?.let { parameter("per_page", it) }
                page?.let { parameter("page", it) }
              }.body()
            }

            public class Post internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val commitSha: String,
            ) {
              public suspend operator fun invoke(
                body: String,
                path: String? = null,
                position: Long? = null,
                line: Long? = null,
              ): Response {
                val response = client.post("/repos/$owner/$repo/commits/$commitSha/comments") {
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

              @Serializable
              internal data class Body(
                public val body: String,
                public val path: String? = null,
                public val position: Long? = null,
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
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val commitSha: String,
          ) {
            public val `get`: Get = Get(client, owner, repo, commitSha)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val commitSha: String,
            ) {
              public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
                val response = client.get("/repos/$owner/$repo/commits/$commitSha/pulls") {
                  perPage?.let { parameter("per_page", it) }
                  page?.let { parameter("page", it) }
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
          private val ref: String,
        ) {
          public val `get`: Get = Get(client, owner, repo, ref)

          public val checkRuns: CheckRuns = CheckRuns(client, owner, repo, ref)

          public val checkSuites: CheckSuites = CheckSuites(client, owner, repo, ref)

          public val status: Status = Status(client, owner, repo, ref)

          public val statuses: Statuses = Statuses(client, owner, repo, ref)

          public class Get internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val ref: String,
          ) {
            public suspend operator fun invoke(page: Long? = 1L, perPage: Long? = 30L): Response {
              val response = client.get("/repos/$owner/$repo/commits/$ref") {
                page?.let { parameter("page", it) }
                perPage?.let { parameter("per_page", it) }
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

              @Serializable
              public data class ServiceUnavailable(
                public val code: String? = null,
                public val message: String? = null,
                @SerialName("documentation_url")
                public val documentationUrl: String? = null,
              ) : Response
            }
          }

          public class CheckRuns internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val ref: String,
          ) {
            public val `get`: Get = Get(client, owner, repo, ref)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val ref: String,
            ) {
              public suspend operator fun invoke(
                checkName: String? = null,
                status: Status? = null,
                filter: Filter? = Filter.Latest,
                perPage: Long? = 30L,
                page: Long? = 1L,
                appId: Long? = null,
              ): Response = client.get("/repos/$owner/$repo/commits/$ref/check-runs") {
                checkName?.let { parameter("check_name", it) }
                status?.let { parameter("status", it.value) }
                filter?.let { parameter("filter", it.value) }
                perPage?.let { parameter("per_page", it) }
                page?.let { parameter("page", it) }
                appId?.let { parameter("app_id", it) }
              }.body()

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
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val ref: String,
          ) {
            public val `get`: Get = Get(client, owner, repo, ref)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val ref: String,
            ) {
              public suspend operator fun invoke(
                appId: Long? = null,
                checkName: String? = null,
                perPage: Long? = 30L,
                page: Long? = 1L,
              ): Response = client.get("/repos/$owner/$repo/commits/$ref/check-suites") {
                appId?.let { parameter("app_id", it) }
                checkName?.let { parameter("check_name", it) }
                perPage?.let { parameter("per_page", it) }
                page?.let { parameter("page", it) }
              }.body()

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
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val ref: String,
          ) {
            public val `get`: Get = Get(client, owner, repo, ref)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val ref: String,
            ) {
              public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
                val response = client.get("/repos/$owner/$repo/commits/$ref/status") {
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
                  public val `value`: CombinedCommitStatus,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response
              }
            }
          }

          public class Statuses internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val ref: String,
          ) {
            public val `get`: Get = Get(client, owner, repo, ref)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val ref: String,
            ) {
              public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
                val response = client.get("/repos/$owner/$repo/commits/$ref/statuses") {
                  perPage?.let { parameter("per_page", it) }
                  page?.let { parameter("page", it) }
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
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val profile: Profile = Profile(client, owner, repo)

        public class Profile internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public val `get`: Get = Get(client, owner, repo)

          public class Get internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public suspend operator fun invoke(): CommunityProfile = client.get("/repos/$owner/$repo/community/profile").body()
          }
        }
      }

      public class Compare internal constructor(
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public fun basehead(basehead: String): BaseheadPath = BaseheadPath(client, owner, repo, basehead)

        public class BaseheadPath internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
          private val basehead: String,
        ) {
          public val `get`: Get = Get(client, owner, repo, basehead)

          public class Get internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val basehead: String,
          ) {
            public suspend operator fun invoke(page: Long? = 1L, perPage: Long? = 30L): Response {
              val response = client.get("/repos/$owner/$repo/compare/$basehead") {
                page?.let { parameter("page", it) }
                perPage?.let { parameter("per_page", it) }
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

      public class Contents internal constructor(
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public fun path(path: String): PathPath = PathPath(client, owner, repo, path)

        public class PathPath internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
          private val path: String,
        ) {
          public val delete: Delete = Delete(client, owner, repo, path)

          public val `get`: Get = Get(client, owner, repo, path)

          public val put: Put = Put(client, owner, repo, path)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val path: String,
          ) {
            public suspend operator fun invoke(
              message: String,
              sha: String,
              branch: String? = null,
              committer: Committer? = null,
              author: Author? = null,
            ): Response {
              val response = client.delete("/repos/$owner/$repo/contents/$path") {
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
            @Serializable
            public data class Committer(
              public val name: String? = null,
              public val email: String? = null,
            )

            /**
             * object containing information about the author.
             */
            @Serializable
            public data class Author(
              public val name: String? = null,
              public val email: String? = null,
            )

            @Serializable
            internal data class Body(
              public val message: String,
              public val sha: String,
              public val branch: String? = null,
              public val committer: Committer? = null,
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
            private val owner: String,
            private val repo: String,
            private val path: String,
          ) {
            public suspend fun vndGithubObject(ref: String? = null): VndGithubObjectResponse {
              val response = client.get("/repos/$owner/$repo/contents/$path") {
                ref?.let { parameter("ref", it) }
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

            public suspend fun json(ref: String? = null): JsonResponse {
              val response = client.get("/repos/$owner/$repo/contents/$path") {
                ref?.let { parameter("ref", it) }
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
                  public val `value`: ContentDirectory,
                ) : Ok

                @Serializable
                @JvmInline
                @SerialName("file")
                public value class File(
                  public val `value`: ContentFile,
                ) : Ok

                @Serializable
                @JvmInline
                @SerialName("symlink")
                public value class Symlink(
                  public val `value`: ContentSymlink,
                ) : Ok

                @Serializable
                @JvmInline
                @SerialName("submodule")
                public value class Submodule(
                  public val `value`: ContentSubmodule,
                ) : Ok
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
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val path: String,
          ) {
            public suspend operator fun invoke(
              message: String,
              content: String,
              sha: String? = null,
              branch: String? = null,
              committer: Committer? = null,
              author: Author? = null,
            ): Response {
              val response = client.put("/repos/$owner/$repo/contents/$path") {
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
            @Serializable
            public data class Committer(
              public val name: String,
              public val email: String,
              public val date: String? = null,
            )

            /**
             * The author of the file. Default: The `committer` or the authenticated user if you omit `committer`.
             */
            @Serializable
            public data class Author(
              public val name: String,
              public val email: String,
              public val date: String? = null,
            )

            @Serializable
            internal data class Body(
              public val message: String,
              public val content: String,
              public val sha: String? = null,
              public val branch: String? = null,
              public val committer: Committer? = null,
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
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val `get`: Get = Get(client, owner, repo)

        public class Get internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(
            anon: String? = null,
            perPage: Long? = 30L,
            page: Long? = 1L,
          ): Response {
            val response = client.get("/repos/$owner/$repo/contributors") {
              anon?.let { parameter("anon", it) }
              perPage?.let { parameter("per_page", it) }
              page?.let { parameter("page", it) }
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
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val alerts: Alerts = Alerts(client, owner, repo)

        public val secrets: Secrets = Secrets(client, owner, repo)

        public class Alerts internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public val `get`: Get = Get(client, owner, repo)

          public fun alertNumber(alertNumber: Long): AlertNumberPath = AlertNumberPath(client, owner, repo, alertNumber)

          public class Get internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
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
              val response = client.get("/repos/$owner/$repo/dependabot/alerts") {
                state?.let { parameter("state", it) }
                severity?.let { parameter("severity", it) }
                ecosystem?.let { parameter("ecosystem", it) }
                `package`?.let { parameter("package", it) }
                manifest?.let { parameter("manifest", it) }
                epssPercentage?.let { parameter("epss_percentage", it) }
                has?.let { parameter("has", it) }
                assignee?.let { parameter("assignee", it) }
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
                  return json.attemptDeserialize(
                    value,
                    CasePatchList::class to { CasePatchList(decodeFromJsonElement(ListSerializer(Patch.serializer()), it)) },
                    CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                  )
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
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val alertNumber: Long,
          ) {
            public val `get`: Get = Get(client, owner, repo, alertNumber)

            public val patch: Patch = Patch(client, owner, repo, alertNumber)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val alertNumber: Long,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.get("/repos/$owner/$repo/dependabot/alerts/$alertNumber")
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
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val alertNumber: Long,
            ) {
              public suspend operator fun invoke(
                state: State? = null,
                dismissedReason: DismissedReason? = null,
                dismissedComment: String? = null,
                assignees: List<String>? = null,
              ): Response {
                val response = client.patch("/repos/$owner/$repo/dependabot/alerts/$alertNumber") {
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

              @Serializable
              internal data class Body(
                public val state: State? = null,
                @SerialName("dismissed_reason")
                public val dismissedReason: DismissedReason? = null,
                @SerialName("dismissed_comment")
                public val dismissedComment: String? = null,
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public val `get`: Get = Get(client, owner, repo)

          public val publicKey: PublicKey = PublicKey(client, owner, repo)

          public fun secretName(secretName: String): SecretNamePath = SecretNamePath(client, owner, repo, secretName)

          public class Get internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response = client.get("/repos/$owner/$repo/dependabot/secrets") {
              perPage?.let { parameter("per_page", it) }
              page?.let { parameter("page", it) }
            }.body()

            @Serializable
            public data class Response(
              @SerialName("total_count")
              public val totalCount: Long,
              public val secrets: List<DependabotSecret>,
            )
          }

          public class PublicKey internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public val `get`: Get = Get(client, owner, repo)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
            ) {
              public suspend operator fun invoke(): DependabotPublicKey = client.get("/repos/$owner/$repo/dependabot/secrets/public-key").body()
            }
          }

          public class SecretNamePath internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val secretName: String,
          ) {
            public val delete: Delete = Delete(client, owner, repo, secretName)

            public val `get`: Get = Get(client, owner, repo, secretName)

            public val put: Put = Put(client, owner, repo, secretName)

            public class Delete internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val secretName: String,
            ) {
              public suspend operator fun invoke() {
                client.delete("/repos/$owner/$repo/dependabot/secrets/$secretName")
              }
            }

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val secretName: String,
            ) {
              public suspend operator fun invoke(): DependabotSecret = client.get("/repos/$owner/$repo/dependabot/secrets/$secretName").body()
            }

            public class Put internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val secretName: String,
            ) {
              public suspend operator fun invoke(encryptedValue: String? = null, keyId: String? = null): Response {
                val response = client.put("/repos/$owner/$repo/dependabot/secrets/$secretName") {
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
                public val encryptedValue: String? = null,
                @SerialName("key_id")
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
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val compare: Compare = Compare(client, owner, repo)

        public val sbom: Sbom = Sbom(client, owner, repo)

        public val snapshots: Snapshots = Snapshots(client, owner, repo)

        public class Compare internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public fun basehead(basehead: String): BaseheadPath = BaseheadPath(client, owner, repo, basehead)

          public class BaseheadPath internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val basehead: String,
          ) {
            public val `get`: Get = Get(client, owner, repo, basehead)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val basehead: String,
            ) {
              public suspend operator fun invoke(name: String? = null): Response {
                val response = client.get("/repos/$owner/$repo/dependency-graph/compare/$basehead") {
                  name?.let { parameter("name", it) }
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public val `get`: Get = Get(client, owner, repo)

          public class Get internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.get("/repos/$owner/$repo/dependency-graph/sbom")
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public val post: Post = Post(client, owner, repo)

          public class Post internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public suspend operator fun invoke(body: Snapshot): Response = client.post("/repos/$owner/$repo/dependency-graph/snapshots") {
              contentType(ContentType.Application.Json)
              setBody(body)
            }.body()

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
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val `get`: Get = Get(client, owner, repo)

        public val post: Post = Post(client, owner, repo)

        public fun deploymentId(deploymentId: Long): DeploymentIdPath = DeploymentIdPath(client, owner, repo, deploymentId)

        public class Get internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(
            sha: String? = "none",
            ref: String? = "none",
            task: String? = "none",
            environment: String? = "none",
            perPage: Long? = 30L,
            page: Long? = 1L,
          ): List<Deployment> = client.get("/repos/$owner/$repo/deployments") {
            sha?.let { parameter("sha", it) }
            ref?.let { parameter("ref", it) }
            task?.let { parameter("task", it) }
            environment?.let { parameter("environment", it) }
            perPage?.let { parameter("per_page", it) }
            page?.let { parameter("page", it) }
          }.body()
        }

        public class Post internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(
            ref: String,
            task: String? = null,
            autoMerge: Boolean? = null,
            requiredContexts: List<String>? = null,
            payload: Payload? = null,
            environment: String? = null,
            description: String? = null,
            transientEnvironment: Boolean? = null,
            productionEnvironment: Boolean? = null,
          ): Response {
            val response = client.post("/repos/$owner/$repo/deployments") {
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
                return json.attemptDeserialize(
                  value,
                  CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                  CaseJsonElement::class to { CaseJsonElement(decodeFromJsonElement(JsonElement.serializer(), it)) },
                )
              }

              override fun serialize(encoder: Encoder, `value`: Payload) {
                when(value) {
                  is CaseJsonElement -> encoder.encodeSerializableValue(JsonElement.serializer(), value.value)
                  is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                }
              }
            }
          }

          @Serializable
          internal data class Body(
            public val ref: String,
            public val task: String? = null,
            @SerialName("auto_merge")
            public val autoMerge: Boolean? = null,
            @SerialName("required_contexts")
            public val requiredContexts: List<String>? = null,
            public val payload: Payload? = null,
            public val environment: String? = null,
            public val description: String? = null,
            @SerialName("transient_environment")
            public val transientEnvironment: Boolean? = null,
            @SerialName("production_environment")
            public val productionEnvironment: Boolean? = null,
          )

          public sealed interface Response {
            public data class Created(
              public val `value`: Deployment,
            ) : Response

            @JvmInline
            @Serializable
            public value class Accepted(
              public val message: String? = null,
            ) : Response

            public data object Conflict : Response

            public data class UnprocessableEntity(
              public val `value`: ValidationError,
            ) : Response
          }
        }

        public class DeploymentIdPath internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
          private val deploymentId: Long,
        ) {
          public val delete: Delete = Delete(client, owner, repo, deploymentId)

          public val `get`: Get = Get(client, owner, repo, deploymentId)

          public val statuses: Statuses = Statuses(client, owner, repo, deploymentId)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val deploymentId: Long,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.delete("/repos/$owner/$repo/deployments/$deploymentId")
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
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val deploymentId: Long,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.get("/repos/$owner/$repo/deployments/$deploymentId")
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
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val deploymentId: Long,
          ) {
            public val `get`: Get = Get(client, owner, repo, deploymentId)

            public val post: Post = Post(client, owner, repo, deploymentId)

            public fun statusId(statusId: Long): StatusIdPath = StatusIdPath(client, owner, repo, deploymentId, statusId)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val deploymentId: Long,
            ) {
              public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
                val response = client.get("/repos/$owner/$repo/deployments/$deploymentId/statuses") {
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
                  public val `value`: List<DeploymentStatus>,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response
              }
            }

            public class Post internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val deploymentId: Long,
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
                val response = client.post("/repos/$owner/$repo/deployments/$deploymentId/statuses") {
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

              @Serializable
              internal data class Body(
                public val state: State,
                @SerialName("target_url")
                public val targetUrl: String? = null,
                @SerialName("log_url")
                public val logUrl: String? = null,
                public val description: String? = null,
                public val environment: String? = null,
                @SerialName("environment_url")
                public val environmentUrl: String? = null,
                @SerialName("auto_inactive")
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
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val deploymentId: Long,
              private val statusId: Long,
            ) {
              public val `get`: Get = Get(client, owner, repo, deploymentId, statusId)

              public class Get internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val deploymentId: Long,
                private val statusId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.get("/repos/$owner/$repo/deployments/$deploymentId/statuses/$statusId")
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
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val post: Post = Post(client, owner, repo)

        public class Post internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(eventType: String, clientPayload: JsonElement? = null): Response {
            val response = client.post("/repos/$owner/$repo/dispatches") {
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

          @Serializable
          internal data class Body(
            @SerialName("event_type")
            public val eventType: String,
            @SerialName("client_payload")
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
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val `get`: Get = Get(client, owner, repo)

        public fun environmentName(environmentName: String): EnvironmentNamePath = EnvironmentNamePath(client, owner, repo, environmentName)

        public class Get internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response = client.get("/repos/$owner/$repo/environments") {
            perPage?.let { parameter("per_page", it) }
            page?.let { parameter("page", it) }
          }.body()

          @Serializable
          public data class Response(
            @SerialName("total_count")
            public val totalCount: Long? = null,
            public val environments: List<Environment>? = null,
          )
        }

        public class EnvironmentNamePath internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
          private val environmentName: String,
        ) {
          public val delete: Delete = Delete(client, owner, repo, environmentName)

          public val `get`: Get = Get(client, owner, repo, environmentName)

          public val put: Put = Put(client, owner, repo, environmentName)

          public val deploymentBranchPolicies: DeploymentBranchPolicies =
              DeploymentBranchPolicies(client, owner, repo, environmentName)

          public val deploymentProtectionRules: DeploymentProtectionRules =
              DeploymentProtectionRules(client, owner, repo, environmentName)

          public val secrets: Secrets = Secrets(client, owner, repo, environmentName)

          public val variables: Variables = Variables(client, owner, repo, environmentName)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val environmentName: String,
          ) {
            public suspend operator fun invoke() {
              client.delete("/repos/$owner/$repo/environments/$environmentName")
            }
          }

          public class Get internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val environmentName: String,
          ) {
            public suspend operator fun invoke(): Environment = client.get("/repos/$owner/$repo/environments/$environmentName").body()
          }

          public class Put internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val environmentName: String,
          ) {
            public suspend operator fun invoke(
              waitTimer: WaitTimer? = null,
              preventSelfReview: PreventSelfReview? = null,
              reviewers: List<Reviewers>? = null,
              deploymentBranchPolicy: DeploymentBranchPolicySettings? = null,
            ): Response {
              val response = client.put("/repos/$owner/$repo/environments/$environmentName") {
                if (waitTimer != null || preventSelfReview != null || reviewers != null || deploymentBranchPolicy != null) {
                  contentType(ContentType.Application.Json)
                  setBody(Body(waitTimer = waitTimer, preventSelfReview = preventSelfReview, reviewers = reviewers, deploymentBranchPolicy = deploymentBranchPolicy))
                }
              }
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                422 -> Response.UnprocessableEntity(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            @Serializable
            public data class Reviewers(
              public val type: DeploymentReviewerType? = null,
              public val id: Long? = null,
            )

            @Serializable
            internal data class Body(
              @SerialName("wait_timer")
              public val waitTimer: WaitTimer? = null,
              @SerialName("prevent_self_review")
              public val preventSelfReview: PreventSelfReview? = null,
              public val reviewers: List<Reviewers>? = null,
              @SerialName("deployment_branch_policy")
              public val deploymentBranchPolicy: DeploymentBranchPolicySettings? = null,
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
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val environmentName: String,
          ) {
            public val `get`: Get = Get(client, owner, repo, environmentName)

            public val post: Post = Post(client, owner, repo, environmentName)

            public fun branchPolicyId(branchPolicyId: Long): BranchPolicyIdPath = BranchPolicyIdPath(client, owner, repo, environmentName, branchPolicyId)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val environmentName: String,
            ) {
              public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response = client.get("/repos/$owner/$repo/environments/$environmentName/deployment-branch-policies") {
                perPage?.let { parameter("per_page", it) }
                page?.let { parameter("page", it) }
              }.body()

              @Serializable
              public data class Response(
                @SerialName("total_count")
                public val totalCount: Long,
                @SerialName("branch_policies")
                public val branchPolicies: List<DeploymentBranchPolicy>,
              )
            }

            public class Post internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val environmentName: String,
            ) {
              public suspend operator fun invoke(body: DeploymentBranchPolicyNamePatternWithType): Response {
                val response = client.post("/repos/$owner/$repo/environments/$environmentName/deployment-branch-policies") {
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
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val environmentName: String,
              private val branchPolicyId: Long,
            ) {
              public val delete: Delete =
                  Delete(client, owner, repo, environmentName, branchPolicyId)

              public val `get`: Get = Get(client, owner, repo, environmentName, branchPolicyId)

              public val put: Put = Put(client, owner, repo, environmentName, branchPolicyId)

              public class Delete internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val environmentName: String,
                private val branchPolicyId: Long,
              ) {
                public suspend operator fun invoke() {
                  client.delete("/repos/$owner/$repo/environments/$environmentName/deployment-branch-policies/$branchPolicyId")
                }
              }

              public class Get internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val environmentName: String,
                private val branchPolicyId: Long,
              ) {
                public suspend operator fun invoke(): DeploymentBranchPolicy = client.get("/repos/$owner/$repo/environments/$environmentName/deployment-branch-policies/$branchPolicyId").body()
              }

              public class Put internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val environmentName: String,
                private val branchPolicyId: Long,
              ) {
                public suspend operator fun invoke(body: DeploymentBranchPolicyNamePattern): DeploymentBranchPolicy = client.put("/repos/$owner/$repo/environments/$environmentName/deployment-branch-policies/$branchPolicyId") {
                  contentType(ContentType.Application.Json)
                  setBody(body)
                }.body()
              }
            }
          }

          public class DeploymentProtectionRules internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val environmentName: String,
          ) {
            public val `get`: Get = Get(client, owner, repo, environmentName)

            public val post: Post = Post(client, owner, repo, environmentName)

            public val apps: Apps = Apps(client, owner, repo, environmentName)

            public fun protectionRuleId(protectionRuleId: Long): ProtectionRuleIdPath = ProtectionRuleIdPath(client, owner, repo, environmentName, protectionRuleId)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val environmentName: String,
            ) {
              public suspend operator fun invoke(): Response = client.get("/repos/$owner/$repo/environments/$environmentName/deployment_protection_rules").body()

              @Serializable
              public data class Response(
                @SerialName("total_count")
                public val totalCount: Long? = null,
                @SerialName("custom_deployment_protection_rules")
                public val customDeploymentProtectionRules: List<DeploymentProtectionRule>? = null,
              )
            }

            public class Post internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val environmentName: String,
            ) {
              public suspend operator fun invoke(integrationId: Long? = null): DeploymentProtectionRule = client.post("/repos/$owner/$repo/environments/$environmentName/deployment_protection_rules") {
                contentType(ContentType.Application.Json)
                setBody(Body(integrationId = integrationId))
              }.body()

              @JvmInline
              @Serializable
              internal value class Body(
                @SerialName("integration_id")
                public val integrationId: Long? = null,
              )
            }

            public class Apps internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val environmentName: String,
            ) {
              public val `get`: Get = Get(client, owner, repo, environmentName)

              public class Get internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val environmentName: String,
              ) {
                public suspend operator fun invoke(page: Long? = 1L, perPage: Long? = 30L): Response = client.get("/repos/$owner/$repo/environments/$environmentName/deployment_protection_rules/apps") {
                  page?.let { parameter("page", it) }
                  perPage?.let { parameter("per_page", it) }
                }.body()

                @Serializable
                public data class Response(
                  @SerialName("total_count")
                  public val totalCount: Long? = null,
                  @SerialName("available_custom_deployment_protection_rule_integrations")
                  public val availableCustomDeploymentProtectionRuleIntegrations:
                      List<CustomDeploymentRuleApp>? = null,
                )
              }
            }

            public class ProtectionRuleIdPath internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val environmentName: String,
              private val protectionRuleId: Long,
            ) {
              public val delete: Delete =
                  Delete(client, owner, repo, environmentName, protectionRuleId)

              public val `get`: Get = Get(client, owner, repo, environmentName, protectionRuleId)

              public class Delete internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val environmentName: String,
                private val protectionRuleId: Long,
              ) {
                public suspend operator fun invoke() {
                  client.delete("/repos/$owner/$repo/environments/$environmentName/deployment_protection_rules/$protectionRuleId")
                }
              }

              public class Get internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val environmentName: String,
                private val protectionRuleId: Long,
              ) {
                public suspend operator fun invoke(): DeploymentProtectionRule = client.get("/repos/$owner/$repo/environments/$environmentName/deployment_protection_rules/$protectionRuleId").body()
              }
            }
          }

          public class Secrets internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val environmentName: String,
          ) {
            public val `get`: Get = Get(client, owner, repo, environmentName)

            public val publicKey: PublicKey = PublicKey(client, owner, repo, environmentName)

            public fun secretName(secretName: String): SecretNamePath = SecretNamePath(client, owner, repo, environmentName, secretName)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val environmentName: String,
            ) {
              public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response = client.get("/repos/$owner/$repo/environments/$environmentName/secrets") {
                perPage?.let { parameter("per_page", it) }
                page?.let { parameter("page", it) }
              }.body()

              @Serializable
              public data class Response(
                @SerialName("total_count")
                public val totalCount: Long,
                public val secrets: List<ActionsSecret>,
              )
            }

            public class PublicKey internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val environmentName: String,
            ) {
              public val `get`: Get = Get(client, owner, repo, environmentName)

              public class Get internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val environmentName: String,
              ) {
                public suspend operator fun invoke(): ActionsPublicKey = client.get("/repos/$owner/$repo/environments/$environmentName/secrets/public-key").body()
              }
            }

            public class SecretNamePath internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val environmentName: String,
              private val secretName: String,
            ) {
              public val delete: Delete = Delete(client, owner, repo, environmentName, secretName)

              public val `get`: Get = Get(client, owner, repo, environmentName, secretName)

              public val put: Put = Put(client, owner, repo, environmentName, secretName)

              public class Delete internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val environmentName: String,
                private val secretName: String,
              ) {
                public suspend operator fun invoke() {
                  client.delete("/repos/$owner/$repo/environments/$environmentName/secrets/$secretName")
                }
              }

              public class Get internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val environmentName: String,
                private val secretName: String,
              ) {
                public suspend operator fun invoke(): ActionsSecret = client.get("/repos/$owner/$repo/environments/$environmentName/secrets/$secretName").body()
              }

              public class Put internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val environmentName: String,
                private val secretName: String,
              ) {
                public suspend operator fun invoke(encryptedValue: String, keyId: String): Response {
                  val response = client.put("/repos/$owner/$repo/environments/$environmentName/secrets/$secretName") {
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
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val environmentName: String,
          ) {
            public val `get`: Get = Get(client, owner, repo, environmentName)

            public val post: Post = Post(client, owner, repo, environmentName)

            public fun name(name: String): NamePath = NamePath(client, owner, repo, environmentName, name)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val environmentName: String,
            ) {
              public suspend operator fun invoke(perPage: Long? = 10L, page: Long? = 1L): Response = client.get("/repos/$owner/$repo/environments/$environmentName/variables") {
                perPage?.let { parameter("per_page", it) }
                page?.let { parameter("page", it) }
              }.body()

              @Serializable
              public data class Response(
                @SerialName("total_count")
                public val totalCount: Long,
                public val variables: List<ActionsVariable>,
              )
            }

            public class Post internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val environmentName: String,
            ) {
              public suspend operator fun invoke(name: String, `value`: String): EmptyObject = client.post("/repos/$owner/$repo/environments/$environmentName/variables") {
                contentType(ContentType.Application.Json)
                setBody(Body(name = name, value = value))
              }.body()

              @Serializable
              internal data class Body(
                public val name: String,
                public val `value`: String,
              )
            }

            public class NamePath internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val environmentName: String,
              private val name: String,
            ) {
              public val delete: Delete = Delete(client, owner, repo, environmentName, name)

              public val `get`: Get = Get(client, owner, repo, environmentName, name)

              public val patch: Patch = Patch(client, owner, repo, environmentName, name)

              public class Delete internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val environmentName: String,
                private val name: String,
              ) {
                public suspend operator fun invoke() {
                  client.delete("/repos/$owner/$repo/environments/$environmentName/variables/$name")
                }
              }

              public class Get internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val environmentName: String,
                private val name: String,
              ) {
                public suspend operator fun invoke(): ActionsVariable = client.get("/repos/$owner/$repo/environments/$environmentName/variables/$name").body()
              }

              public class Patch internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val environmentName: String,
                private val name: String,
              ) {
                public suspend operator fun invoke(name: String? = null, `value`: String? = null) {
                  client.patch("/repos/$owner/$repo/environments/$environmentName/variables/$name") {
                    contentType(ContentType.Application.Json)
                    setBody(Body(name = name, value = value))
                  }
                }

                @Serializable
                internal data class Body(
                  public val name: String? = null,
                  public val `value`: String? = null,
                )
              }
            }
          }
        }
      }

      public class Events internal constructor(
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val `get`: Get = Get(client, owner, repo)

        public class Get internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<Event> = client.get("/repos/$owner/$repo/events") {
            perPage?.let { parameter("per_page", it) }
            page?.let { parameter("page", it) }
          }.body()
        }
      }

      public class Forks internal constructor(
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val `get`: Get = Get(client, owner, repo)

        public val post: Post = Post(client, owner, repo)

        public class Get internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(
            sort: Sort? = Sort.Newest,
            perPage: Long? = 30L,
            page: Long? = 1L,
          ): Response {
            val response = client.get("/repos/$owner/$repo/forks") {
              sort?.let { parameter("sort", it.value) }
              perPage?.let { parameter("per_page", it) }
              page?.let { parameter("page", it) }
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(
            organization: String? = null,
            name: String? = null,
            defaultBranchOnly: Boolean? = null,
          ): Response {
            val response = client.post("/repos/$owner/$repo/forks") {
              if (organization != null || name != null || defaultBranchOnly != null) {
                contentType(ContentType.Application.Json)
                setBody(Body(organization = organization, name = name, defaultBranchOnly = defaultBranchOnly))
              }
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

          @Serializable
          internal data class Body(
            public val organization: String? = null,
            public val name: String? = null,
            @SerialName("default_branch_only")
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
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val blobs: Blobs = Blobs(client, owner, repo)

        public val commits: Commits = Commits(client, owner, repo)

        public val matchingRefs: MatchingRefs = MatchingRefs(client, owner, repo)

        public val ref: Ref = Ref(client, owner, repo)

        public val refs: Refs = Refs(client, owner, repo)

        public val tags: Tags = Tags(client, owner, repo)

        public val trees: Trees = Trees(client, owner, repo)

        public class Blobs internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public val post: Post = Post(client, owner, repo)

          public fun fileSha(fileSha: String): FileShaPath = FileShaPath(client, owner, repo, fileSha)

          public class Post internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public suspend operator fun invoke(content: String, encoding: String? = null): Response {
              val response = client.post("/repos/$owner/$repo/git/blobs") {
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

            @Serializable
            internal data class Body(
              public val content: String,
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
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val fileSha: String,
          ) {
            public val `get`: Get = Get(client, owner, repo, fileSha)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val fileSha: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.get("/repos/$owner/$repo/git/blobs/$fileSha")
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public val post: Post = Post(client, owner, repo)

          public fun commitSha(commitSha: String): CommitShaPath = CommitShaPath(client, owner, repo, commitSha)

          public class Post internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public suspend operator fun invoke(
              message: String,
              tree: String,
              parents: List<String>? = null,
              author: Author? = null,
              committer: Committer? = null,
              signature: String? = null,
            ): Response {
              val response = client.post("/repos/$owner/$repo/git/commits") {
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
            @Serializable
            public data class Author(
              public val name: String,
              public val email: String,
              public val date: Instant? = null,
            )

            /**
             * Information about the person who is making the commit. By default, `committer` will use the information set in `author`. See the `author` and `committer` object below for details.
             */
            @Serializable
            public data class Committer(
              public val name: String? = null,
              public val email: String? = null,
              public val date: Instant? = null,
            )

            @Serializable
            internal data class Body(
              public val message: String,
              public val tree: String,
              public val parents: List<String>? = null,
              public val author: Author? = null,
              public val committer: Committer? = null,
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
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val commitSha: String,
          ) {
            public val `get`: Get = Get(client, owner, repo, commitSha)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val commitSha: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.get("/repos/$owner/$repo/git/commits/$commitSha")
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public fun ref(ref: String): RefPath = RefPath(client, owner, repo, ref)

          public class RefPath internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val ref: String,
          ) {
            public val `get`: Get = Get(client, owner, repo, ref)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val ref: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.get("/repos/$owner/$repo/git/matching-refs/$ref")
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public fun ref(ref: String): RefPath = RefPath(client, owner, repo, ref)

          public class RefPath internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val ref: String,
          ) {
            public val `get`: Get = Get(client, owner, repo, ref)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val ref: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.get("/repos/$owner/$repo/git/ref/$ref")
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public val post: Post = Post(client, owner, repo)

          public fun ref(ref: String): RefPath = RefPath(client, owner, repo, ref)

          public class Post internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public suspend operator fun invoke(ref: String, sha: String): Response {
              val response = client.post("/repos/$owner/$repo/git/refs") {
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
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val ref: String,
          ) {
            public val delete: Delete = Delete(client, owner, repo, ref)

            public val patch: Patch = Patch(client, owner, repo, ref)

            public class Delete internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val ref: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.delete("/repos/$owner/$repo/git/refs/$ref")
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
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val ref: String,
            ) {
              public suspend operator fun invoke(sha: String, force: Boolean? = null): Response {
                val response = client.patch("/repos/$owner/$repo/git/refs/$ref") {
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

              @Serializable
              internal data class Body(
                public val sha: String,
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public val post: Post = Post(client, owner, repo)

          public fun tagSha(tagSha: String): TagShaPath = TagShaPath(client, owner, repo, tagSha)

          public class Post internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public suspend operator fun invoke(
              tag: String,
              message: String,
              `object`: String,
              type: Type,
              tagger: Tagger? = null,
            ): Response {
              val response = client.post("/repos/$owner/$repo/git/tags") {
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
            @Serializable
            public data class Tagger(
              public val name: String,
              public val email: String,
              public val date: Instant? = null,
            )

            @Serializable
            internal data class Body(
              public val tag: String,
              public val message: String,
              public val `object`: String,
              public val type: Type,
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
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val tagSha: String,
          ) {
            public val `get`: Get = Get(client, owner, repo, tagSha)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val tagSha: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.get("/repos/$owner/$repo/git/tags/$tagSha")
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public val post: Post = Post(client, owner, repo)

          public fun treeSha(treeSha: String): TreeShaPath = TreeShaPath(client, owner, repo, treeSha)

          public class Post internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public suspend operator fun invoke(tree: List<Tree>, baseTree: String? = null): Response {
              val response = client.post("/repos/$owner/$repo/git/trees") {
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

            @Serializable
            public data class Tree(
              public val path: String? = null,
              public val mode: Mode? = null,
              public val type: Type? = null,
              public val sha: String? = null,
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

            @Serializable
            internal data class Body(
              public val tree: List<Tree>,
              @SerialName("base_tree")
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
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val treeSha: String,
          ) {
            public val `get`: Get = Get(client, owner, repo, treeSha)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val treeSha: String,
            ) {
              public suspend operator fun invoke(recursive: String? = null): Response {
                val response = client.get("/repos/$owner/$repo/git/trees/$treeSha") {
                  recursive?.let { parameter("recursive", it) }
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
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val `get`: Get = Get(client, owner, repo)

        public val post: Post = Post(client, owner, repo)

        public fun hookId(hookId: Long): HookIdPath = HookIdPath(client, owner, repo, hookId)

        public class Get internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
            val response = client.get("/repos/$owner/$repo/hooks") {
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
              public val `value`: List<Hook>,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class Post internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(
            name: String? = null,
            config: Config? = null,
            events: List<String>? = null,
            active: Boolean? = null,
          ): Response {
            val response = client.post("/repos/$owner/$repo/hooks") {
              if (name != null || config != null || events != null || active != null) {
                contentType(ContentType.Application.Json)
                setBody(Body(name = name, config = config, events = events, active = active))
              }
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
          @Serializable
          public data class Config(
            public val url: WebhookConfigUrl? = null,
            @SerialName("content_type")
            public val contentType: WebhookConfigContentType? = null,
            public val secret: WebhookConfigSecret? = null,
            @SerialName("insecure_ssl")
            public val insecureSsl: WebhookConfigInsecureSsl? = null,
          )

          @Serializable
          internal data class Body(
            public val name: String? = null,
            public val config: Config? = null,
            public val events: List<String>? = null,
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
          private val hookId: Long,
        ) {
          public val delete: Delete = Delete(client, owner, repo, hookId)

          public val `get`: Get = Get(client, owner, repo, hookId)

          public val patch: Patch = Patch(client, owner, repo, hookId)

          public val config: Config = Config(client, owner, repo, hookId)

          public val deliveries: Deliveries = Deliveries(client, owner, repo, hookId)

          public val pings: Pings = Pings(client, owner, repo, hookId)

          public val tests: Tests = Tests(client, owner, repo, hookId)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val hookId: Long,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.delete("/repos/$owner/$repo/hooks/$hookId")
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
            private val owner: String,
            private val repo: String,
            private val hookId: Long,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.get("/repos/$owner/$repo/hooks/$hookId")
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
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val hookId: Long,
          ) {
            public suspend operator fun invoke(
              config: WebhookConfig? = null,
              events: List<String>? = null,
              addEvents: List<String>? = null,
              removeEvents: List<String>? = null,
              active: Boolean? = null,
            ): Response {
              val response = client.patch("/repos/$owner/$repo/hooks/$hookId") {
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

            @Serializable
            internal data class Body(
              public val config: WebhookConfig? = null,
              public val events: List<String>? = null,
              @SerialName("add_events")
              public val addEvents: List<String>? = null,
              @SerialName("remove_events")
              public val removeEvents: List<String>? = null,
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
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val hookId: Long,
          ) {
            public val `get`: Get = Get(client, owner, repo, hookId)

            public val patch: Patch = Patch(client, owner, repo, hookId)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val hookId: Long,
            ) {
              public suspend operator fun invoke(): WebhookConfig = client.get("/repos/$owner/$repo/hooks/$hookId/config").body()
            }

            public class Patch internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val hookId: Long,
            ) {
              public suspend operator fun invoke(
                url: WebhookConfigUrl? = null,
                contentType: WebhookConfigContentType? = null,
                secret: WebhookConfigSecret? = null,
                insecureSsl: WebhookConfigInsecureSsl? = null,
              ): WebhookConfig = client.patch("/repos/$owner/$repo/hooks/$hookId/config") {
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
            private val owner: String,
            private val repo: String,
            private val hookId: Long,
          ) {
            public val `get`: Get = Get(client, owner, repo, hookId)

            public fun deliveryId(deliveryId: Long): DeliveryIdPath = DeliveryIdPath(client, owner, repo, hookId, deliveryId)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val hookId: Long,
            ) {
              public suspend operator fun invoke(perPage: Long? = 30L, cursor: String? = null): Response {
                val response = client.get("/repos/$owner/$repo/hooks/$hookId/deliveries") {
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
              private val owner: String,
              private val repo: String,
              private val hookId: Long,
              private val deliveryId: Long,
            ) {
              public val `get`: Get = Get(client, owner, repo, hookId, deliveryId)

              public val attempts: Attempts = Attempts(client, owner, repo, hookId, deliveryId)

              public class Get internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val hookId: Long,
                private val deliveryId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.get("/repos/$owner/$repo/hooks/$hookId/deliveries/$deliveryId")
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
                private val owner: String,
                private val repo: String,
                private val hookId: Long,
                private val deliveryId: Long,
              ) {
                public val post: Post = Post(client, owner, repo, hookId, deliveryId)

                public class Post internal constructor(
                  private val client: HttpClient,
                  private val owner: String,
                  private val repo: String,
                  private val hookId: Long,
                  private val deliveryId: Long,
                ) {
                  public suspend operator fun invoke(): Response {
                    val response = client.post("/repos/$owner/$repo/hooks/$hookId/deliveries/$deliveryId/attempts")
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
            private val owner: String,
            private val repo: String,
            private val hookId: Long,
          ) {
            public val post: Post = Post(client, owner, repo, hookId)

            public class Post internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val hookId: Long,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.post("/repos/$owner/$repo/hooks/$hookId/pings")
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
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val hookId: Long,
          ) {
            public val post: Post = Post(client, owner, repo, hookId)

            public class Post internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val hookId: Long,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.post("/repos/$owner/$repo/hooks/$hookId/tests")
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
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val delete: Delete = Delete(client, owner, repo)

        public val `get`: Get = Get(client, owner, repo)

        public val put: Put = Put(client, owner, repo)

        public class Delete internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.delete("/repos/$owner/$repo/immutable-releases")
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.get("/repos/$owner/$repo/immutable-releases")
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.put("/repos/$owner/$repo/immutable-releases")
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
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        @Deprecated("Deprecated by the API provider")
        public val delete: Delete = Delete(client, owner, repo)

        @Deprecated("Deprecated by the API provider")
        public val `get`: Get = Get(client, owner, repo)

        @Deprecated("Deprecated by the API provider")
        public val patch: Patch = Patch(client, owner, repo)

        @Deprecated("Deprecated by the API provider")
        public val put: Put = Put(client, owner, repo)

        public val authors: Authors = Authors(client, owner, repo)

        public val largeFiles: LargeFiles = LargeFiles(client, owner, repo)

        public val lfs: Lfs = Lfs(client, owner, repo)

        @Deprecated("Deprecated by the API provider")
        public class Delete internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          @Deprecated("Deprecated by the API provider")
          public suspend operator fun invoke(): Response {
            val response = client.delete("/repos/$owner/$repo/import")
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          @Deprecated("Deprecated by the API provider")
          public suspend operator fun invoke(): Response {
            val response = client.get("/repos/$owner/$repo/import")
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          @Deprecated("Deprecated by the API provider")
          public suspend operator fun invoke(
            vcsUsername: String? = null,
            vcsPassword: String? = null,
            vcs: Vcs? = null,
            tfvcProject: String? = null,
          ): Response {
            val response = client.patch("/repos/$owner/$repo/import") {
              if (vcsUsername != null || vcsPassword != null || vcs != null || tfvcProject != null) {
                contentType(ContentType.Application.Json)
                setBody(Body(vcsUsername = vcsUsername, vcsPassword = vcsPassword, vcs = vcs, tfvcProject = tfvcProject))
              }
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

          @Serializable
          internal data class Body(
            @SerialName("vcs_username")
            public val vcsUsername: String? = null,
            @SerialName("vcs_password")
            public val vcsPassword: String? = null,
            public val vcs: Vcs? = null,
            @SerialName("tfvc_project")
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          @Deprecated("Deprecated by the API provider")
          public suspend operator fun invoke(
            vcsUrl: String,
            vcs: Vcs? = null,
            vcsUsername: String? = null,
            vcsPassword: String? = null,
            tfvcProject: String? = null,
          ): Response {
            val response = client.put("/repos/$owner/$repo/import") {
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

          @Serializable
          internal data class Body(
            @SerialName("vcs_url")
            public val vcsUrl: String,
            public val vcs: Vcs? = null,
            @SerialName("vcs_username")
            public val vcsUsername: String? = null,
            @SerialName("vcs_password")
            public val vcsPassword: String? = null,
            @SerialName("tfvc_project")
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          @Deprecated("Deprecated by the API provider")
          public val `get`: Get = Get(client, owner, repo)

          public fun authorId(authorId: Long): AuthorIdPath = AuthorIdPath(client, owner, repo, authorId)

          @Deprecated("Deprecated by the API provider")
          public class Get internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            @Deprecated("Deprecated by the API provider")
            public suspend operator fun invoke(since: Long? = null): Response {
              val response = client.get("/repos/$owner/$repo/import/authors") {
                since?.let { parameter("since", it) }
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
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val authorId: Long,
          ) {
            @Deprecated("Deprecated by the API provider")
            public val patch: Patch = Patch(client, owner, repo, authorId)

            @Deprecated("Deprecated by the API provider")
            public class Patch internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val authorId: Long,
            ) {
              @Deprecated("Deprecated by the API provider")
              public suspend operator fun invoke(email: String? = null, name: String? = null): Response {
                val response = client.patch("/repos/$owner/$repo/import/authors/$authorId") {
                  if (email != null || name != null) {
                    contentType(ContentType.Application.Json)
                    setBody(Body(email = email, name = name))
                  }
                }
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  404 -> Response.NotFound(response.body())
                  422 -> Response.UnprocessableEntity(response.body())
                  503 -> Response.ServiceUnavailable(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              @Serializable
              internal data class Body(
                public val email: String? = null,
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          @Deprecated("Deprecated by the API provider")
          public val `get`: Get = Get(client, owner, repo)

          @Deprecated("Deprecated by the API provider")
          public class Get internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            @Deprecated("Deprecated by the API provider")
            public suspend operator fun invoke(): Response {
              val response = client.get("/repos/$owner/$repo/import/large_files")
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          @Deprecated("Deprecated by the API provider")
          public val patch: Patch = Patch(client, owner, repo)

          @Deprecated("Deprecated by the API provider")
          public class Patch internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            @Deprecated("Deprecated by the API provider")
            public suspend operator fun invoke(useLfs: UseLfs): Response {
              val response = client.patch("/repos/$owner/$repo/import/lfs") {
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

            @JvmInline
            @Serializable
            internal value class Body(
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
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val `get`: Get = Get(client, owner, repo)

        public class Get internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.get("/repos/$owner/$repo/installation")
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
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val delete: Delete = Delete(client, owner, repo)

        public val `get`: Get = Get(client, owner, repo)

        public val put: Put = Put(client, owner, repo)

        public class Delete internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.delete("/repos/$owner/$repo/interaction-limits")
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(): Response = client.get("/repos/$owner/$repo/interaction-limits").body()

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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(body: InteractionLimit): Response {
            val response = client.put("/repos/$owner/$repo/interaction-limits") {
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
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val `get`: Get = Get(client, owner, repo)

        public fun invitationId(invitationId: Long): InvitationIdPath = InvitationIdPath(client, owner, repo, invitationId)

        public class Get internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<RepositoryInvitation> = client.get("/repos/$owner/$repo/invitations") {
            perPage?.let { parameter("per_page", it) }
            page?.let { parameter("page", it) }
          }.body()
        }

        public class InvitationIdPath internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
          private val invitationId: Long,
        ) {
          public val delete: Delete = Delete(client, owner, repo, invitationId)

          public val patch: Patch = Patch(client, owner, repo, invitationId)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val invitationId: Long,
          ) {
            public suspend operator fun invoke() {
              client.delete("/repos/$owner/$repo/invitations/$invitationId")
            }
          }

          public class Patch internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val invitationId: Long,
          ) {
            public suspend operator fun invoke(permissions: Permissions? = null): RepositoryInvitation = client.patch("/repos/$owner/$repo/invitations/$invitationId") {
              if (permissions != null) {
                contentType(ContentType.Application.Json)
                setBody(Body(permissions = permissions))
              }
            }.body()

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

            @JvmInline
            @Serializable
            internal value class Body(
              public val permissions: Permissions? = null,
            )
          }
        }
      }

      public class Issues internal constructor(
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val `get`: Get = Get(client, owner, repo)

        public val post: Post = Post(client, owner, repo)

        public val comments: Comments = Comments(client, owner, repo)

        public val events: Events = Events(client, owner, repo)

        public fun issueNumber(issueNumber: Long): IssueNumberPath = IssueNumberPath(client, owner, repo, issueNumber)

        public class Get internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
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
            val response = client.get("/repos/$owner/$repo/issues") {
              milestone?.let { parameter("milestone", it) }
              state?.let { parameter("state", it.value) }
              assignee?.let { parameter("assignee", it) }
              type?.let { parameter("type", it) }
              creator?.let { parameter("creator", it) }
              mentioned?.let { parameter("mentioned", it) }
              labels?.let { parameter("labels", it) }
              sort?.let { parameter("sort", it.value) }
              direction?.let { parameter("direction", it.value) }
              since?.let { parameter("since", it.toString()) }
              perPage?.let { parameter("per_page", it) }
              page?.let { parameter("page", it) }
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(
            title: String,
            body: String? = null,
            assignee: String? = null,
            milestone: Milestone? = null,
            labels: List<Labels>? = null,
            assignees: List<String>? = null,
            type: String? = null,
          ): Response {
            val response = client.post("/repos/$owner/$repo/issues") {
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
            assignee: String? = null,
            milestone: Milestone? = null,
            labels: List<Labels>? = null,
            assignees: List<String>? = null,
            type: String? = null,
          ): Response {
            val response = client.post("/repos/$owner/$repo/issues") {
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
                return json.attemptDeserialize(
                  value,
                  CaseLong::class to { CaseLong(decodeFromJsonElement(Long.serializer(), it)) },
                  CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                )
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

            @Serializable
            public data class IdAndNameAndDescriptionAndColor(
              public val id: Long? = null,
              public val name: String? = null,
              public val description: String? = null,
              public val color: String? = null,
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
                return json.attemptDeserialize(
                  value,
                  IdAndNameAndDescriptionAndColor::class to { decodeFromJsonElement(IdAndNameAndDescriptionAndColor.serializer(), it) },
                  CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                )
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
          internal data class Body(
            public val title: Title,
            public val body: String? = null,
            public val assignee: String? = null,
            public val milestone: Milestone? = null,
            public val labels: List<Labels>? = null,
            public val assignees: List<String>? = null,
            public val type: String? = null,
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
                  return json.attemptDeserialize(
                    value,
                    CaseLong::class to { CaseLong(decodeFromJsonElement(Long.serializer(), it)) },
                    CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                  )
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

            @Serializable
            public data class ServiceUnavailable(
              public val code: String? = null,
              public val message: String? = null,
              @SerialName("documentation_url")
              public val documentationUrl: String? = null,
            ) : Response
          }
        }

        public class Comments internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public val `get`: Get = Get(client, owner, repo)

          public fun commentId(commentId: Long): CommentIdPath = CommentIdPath(client, owner, repo, commentId)

          public class Get internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public suspend operator fun invoke(
              sort: Sort? = Sort.Created,
              direction: Direction? = null,
              since: Instant? = null,
              perPage: Long? = 30L,
              page: Long? = 1L,
            ): Response {
              val response = client.get("/repos/$owner/$repo/issues/comments") {
                sort?.let { parameter("sort", it.value) }
                direction?.let { parameter("direction", it.value) }
                since?.let { parameter("since", it.toString()) }
                perPage?.let { parameter("per_page", it) }
                page?.let { parameter("page", it) }
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
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val commentId: Long,
          ) {
            public val delete: Delete = Delete(client, owner, repo, commentId)

            public val `get`: Get = Get(client, owner, repo, commentId)

            public val patch: Patch = Patch(client, owner, repo, commentId)

            public val pin: Pin = Pin(client, owner, repo, commentId)

            public val reactions: Reactions = Reactions(client, owner, repo, commentId)

            public class Delete internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val commentId: Long,
            ) {
              public suspend operator fun invoke() {
                client.delete("/repos/$owner/$repo/issues/comments/$commentId")
              }
            }

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val commentId: Long,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.get("/repos/$owner/$repo/issues/comments/$commentId")
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
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val commentId: Long,
            ) {
              public suspend operator fun invoke(body: String): Response {
                val response = client.patch("/repos/$owner/$repo/issues/comments/$commentId") {
                  contentType(ContentType.Application.Json)
                  setBody(Body(body = body))
                }
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  422 -> Response.UnprocessableEntity(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              @JvmInline
              @Serializable
              internal value class Body(
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
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val commentId: Long,
            ) {
              public val delete: Delete = Delete(client, owner, repo, commentId)

              public val put: Put = Put(client, owner, repo, commentId)

              public class Delete internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val commentId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.delete("/repos/$owner/$repo/issues/comments/$commentId/pin")
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

                  @Serializable
                  public data class ServiceUnavailable(
                    public val code: String? = null,
                    public val message: String? = null,
                    @SerialName("documentation_url")
                    public val documentationUrl: String? = null,
                  ) : Response
                }
              }

              public class Put internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val commentId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.put("/repos/$owner/$repo/issues/comments/$commentId/pin")
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
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val commentId: Long,
            ) {
              public val `get`: Get = Get(client, owner, repo, commentId)

              public val post: Post = Post(client, owner, repo, commentId)

              public fun reactionId(reactionId: Long): ReactionIdPath = ReactionIdPath(client, owner, repo, commentId, reactionId)

              public class Get internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val commentId: Long,
              ) {
                public suspend operator fun invoke(
                  content: Content? = null,
                  perPage: Long? = 30L,
                  page: Long? = 1L,
                ): Response {
                  val response = client.get("/repos/$owner/$repo/issues/comments/$commentId/reactions") {
                    content?.let { parameter("content", it.value) }
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
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val commentId: Long,
              ) {
                public suspend operator fun invoke(content: Content): Response {
                  val response = client.post("/repos/$owner/$repo/issues/comments/$commentId/reactions") {
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

                @JvmInline
                @Serializable
                internal value class Body(
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
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val commentId: Long,
                private val reactionId: Long,
              ) {
                public val delete: Delete = Delete(client, owner, repo, commentId, reactionId)

                public class Delete internal constructor(
                  private val client: HttpClient,
                  private val owner: String,
                  private val repo: String,
                  private val commentId: Long,
                  private val reactionId: Long,
                ) {
                  public suspend operator fun invoke() {
                    client.delete("/repos/$owner/$repo/issues/comments/$commentId/reactions/$reactionId")
                  }
                }
              }
            }
          }
        }

        public class Events internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public val `get`: Get = Get(client, owner, repo)

          public fun eventId(eventId: Long): EventIdPath = EventIdPath(client, owner, repo, eventId)

          public class Get internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
              val response = client.get("/repos/$owner/$repo/issues/events") {
                perPage?.let { parameter("per_page", it) }
                page?.let { parameter("page", it) }
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
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val eventId: Long,
          ) {
            public val `get`: Get = Get(client, owner, repo, eventId)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val eventId: Long,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.get("/repos/$owner/$repo/issues/events/$eventId")
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
          private val issueNumber: Long,
        ) {
          public val `get`: Get = Get(client, owner, repo, issueNumber)

          public val patch: Patch = Patch(client, owner, repo, issueNumber)

          public val assignees: Assignees = Assignees(client, owner, repo, issueNumber)

          public val comments: Comments = Comments(client, owner, repo, issueNumber)

          public val dependencies: Dependencies = Dependencies(client, owner, repo, issueNumber)

          public val events: Events = Events(client, owner, repo, issueNumber)

          public val issueFieldValues: IssueFieldValues =
              IssueFieldValues(client, owner, repo, issueNumber)

          public val labels: Labels = Labels(client, owner, repo, issueNumber)

          public val lock: Lock = Lock(client, owner, repo, issueNumber)

          public val parent: Parent = Parent(client, owner, repo, issueNumber)

          public val reactions: Reactions = Reactions(client, owner, repo, issueNumber)

          public val subIssue: SubIssue = SubIssue(client, owner, repo, issueNumber)

          public val subIssues: SubIssues = SubIssues(client, owner, repo, issueNumber)

          public val timeline: Timeline = Timeline(client, owner, repo, issueNumber)

          public class Get internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val issueNumber: Long,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.get("/repos/$owner/$repo/issues/$issueNumber")
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
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val issueNumber: Long,
          ) {
            public suspend operator fun invoke(
              title: Title? = null,
              body: String? = null,
              assignee: String? = null,
              state: State? = null,
              stateReason: StateReason? = null,
              milestone: Milestone? = null,
              labels: List<Labels>? = null,
              assignees: List<String>? = null,
              issueFieldValues: List<IssueFieldValues>? = null,
              type: String? = null,
            ): Response {
              val response = client.patch("/repos/$owner/$repo/issues/$issueNumber") {
                if (title != null || body != null || assignee != null || state != null || stateReason != null || milestone != null || labels != null || assignees != null || issueFieldValues != null || type != null) {
                  contentType(ContentType.Application.Json)
                  setBody(Body(title = title, body = body, assignee = assignee, state = state, stateReason = stateReason, milestone = milestone, labels = labels, assignees = assignees, issueFieldValues = issueFieldValues, type = type))
                }
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
                  return json.attemptDeserialize(
                    value,
                    CaseLong::class to { CaseLong(decodeFromJsonElement(Long.serializer(), it)) },
                    CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                  )
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
                  return json.attemptDeserialize(
                    value,
                    CaseLong::class to { CaseLong(decodeFromJsonElement(Long.serializer(), it)) },
                    CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                  )
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

              @Serializable
              public data class IdAndNameAndDescriptionAndColor(
                public val id: Long? = null,
                public val name: String? = null,
                public val description: String? = null,
                public val color: String? = null,
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
                  return json.attemptDeserialize(
                    value,
                    IdAndNameAndDescriptionAndColor::class to { decodeFromJsonElement(IdAndNameAndDescriptionAndColor.serializer(), it) },
                    CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                  )
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

            @Serializable
            internal data class Body(
              public val title: Title? = null,
              public val body: String? = null,
              public val assignee: String? = null,
              public val state: State? = null,
              @SerialName("state_reason")
              public val stateReason: StateReason? = null,
              public val milestone: Milestone? = null,
              public val labels: List<Labels>? = null,
              public val assignees: List<String>? = null,
              @SerialName("issue_field_values")
              public val issueFieldValues: List<IssueFieldValues>? = null,
              public val type: String? = null,
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

              @Serializable
              public data class ServiceUnavailable(
                public val code: String? = null,
                public val message: String? = null,
                @SerialName("documentation_url")
                public val documentationUrl: String? = null,
              ) : Response
            }
          }

          public class Assignees internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val issueNumber: Long,
          ) {
            public val delete: Delete = Delete(client, owner, repo, issueNumber)

            public val post: Post = Post(client, owner, repo, issueNumber)

            public fun assignee(assignee: String): AssigneePath = AssigneePath(client, owner, repo, issueNumber, assignee)

            public class Delete internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val issueNumber: Long,
            ) {
              public suspend operator fun invoke(assignees: List<String>? = null): Issue = client.delete("/repos/$owner/$repo/issues/$issueNumber/assignees") {
                if (assignees != null) {
                  contentType(ContentType.Application.Json)
                  setBody(Body(assignees = assignees))
                }
              }.body()

              @JvmInline
              @Serializable
              internal value class Body(
                public val assignees: List<String>? = null,
              )
            }

            public class Post internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val issueNumber: Long,
            ) {
              public suspend operator fun invoke(assignees: List<String>? = null): Issue = client.post("/repos/$owner/$repo/issues/$issueNumber/assignees") {
                if (assignees != null) {
                  contentType(ContentType.Application.Json)
                  setBody(Body(assignees = assignees))
                }
              }.body()

              @JvmInline
              @Serializable
              internal value class Body(
                public val assignees: List<String>? = null,
              )
            }

            public class AssigneePath internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val issueNumber: Long,
              private val assignee: String,
            ) {
              public val `get`: Get = Get(client, owner, repo, issueNumber, assignee)

              public class Get internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val issueNumber: Long,
                private val assignee: String,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.get("/repos/$owner/$repo/issues/$issueNumber/assignees/$assignee")
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
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val issueNumber: Long,
          ) {
            public val `get`: Get = Get(client, owner, repo, issueNumber)

            public val post: Post = Post(client, owner, repo, issueNumber)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val issueNumber: Long,
            ) {
              public suspend operator fun invoke(
                since: Instant? = null,
                perPage: Long? = 30L,
                page: Long? = 1L,
              ): Response {
                val response = client.get("/repos/$owner/$repo/issues/$issueNumber/comments") {
                  since?.let { parameter("since", it.toString()) }
                  perPage?.let { parameter("per_page", it) }
                  page?.let { parameter("page", it) }
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
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val issueNumber: Long,
            ) {
              public suspend operator fun invoke(body: String): Response {
                val response = client.post("/repos/$owner/$repo/issues/$issueNumber/comments") {
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

              @JvmInline
              @Serializable
              internal value class Body(
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
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val issueNumber: Long,
          ) {
            public val blockedBy: BlockedBy = BlockedBy(client, owner, repo, issueNumber)

            public val blocking: Blocking = Blocking(client, owner, repo, issueNumber)

            public class BlockedBy internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val issueNumber: Long,
            ) {
              public val `get`: Get = Get(client, owner, repo, issueNumber)

              public val post: Post = Post(client, owner, repo, issueNumber)

              public fun issueId(issueId: Long): IssueIdPath = IssueIdPath(client, owner, repo, issueNumber, issueId)

              public class Get internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val issueNumber: Long,
              ) {
                public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
                  val response = client.get("/repos/$owner/$repo/issues/$issueNumber/dependencies/blocked_by") {
                    perPage?.let { parameter("per_page", it) }
                    page?.let { parameter("page", it) }
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
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val issueNumber: Long,
              ) {
                public suspend operator fun invoke(issueId: Long): Response {
                  val response = client.post("/repos/$owner/$repo/issues/$issueNumber/dependencies/blocked_by") {
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

                @JvmInline
                @Serializable
                internal value class Body(
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
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val issueNumber: Long,
                private val issueId: Long,
              ) {
                public val delete: Delete = Delete(client, owner, repo, issueNumber, issueId)

                public class Delete internal constructor(
                  private val client: HttpClient,
                  private val owner: String,
                  private val repo: String,
                  private val issueNumber: Long,
                  private val issueId: Long,
                ) {
                  public suspend operator fun invoke(): Response {
                    val response = client.delete("/repos/$owner/$repo/issues/$issueNumber/dependencies/blocked_by/$issueId")
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
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val issueNumber: Long,
            ) {
              public val `get`: Get = Get(client, owner, repo, issueNumber)

              public class Get internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val issueNumber: Long,
              ) {
                public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
                  val response = client.get("/repos/$owner/$repo/issues/$issueNumber/dependencies/blocking") {
                    perPage?.let { parameter("per_page", it) }
                    page?.let { parameter("page", it) }
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
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val issueNumber: Long,
          ) {
            public val `get`: Get = Get(client, owner, repo, issueNumber)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val issueNumber: Long,
            ) {
              public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
                val response = client.get("/repos/$owner/$repo/issues/$issueNumber/events") {
                  perPage?.let { parameter("per_page", it) }
                  page?.let { parameter("page", it) }
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
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val issueNumber: Long,
          ) {
            public val `get`: Get = Get(client, owner, repo, issueNumber)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val issueNumber: Long,
            ) {
              public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
                val response = client.get("/repos/$owner/$repo/issues/$issueNumber/issue-field-values") {
                  perPage?.let { parameter("per_page", it) }
                  page?.let { parameter("page", it) }
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
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val issueNumber: Long,
          ) {
            public val delete: Delete = Delete(client, owner, repo, issueNumber)

            public val `get`: Get = Get(client, owner, repo, issueNumber)

            public val post: Post = Post(client, owner, repo, issueNumber)

            public val put: Put = Put(client, owner, repo, issueNumber)

            public fun name(name: String): NamePath = NamePath(client, owner, repo, issueNumber, name)

            public class Delete internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val issueNumber: Long,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.delete("/repos/$owner/$repo/issues/$issueNumber/labels")
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
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val issueNumber: Long,
            ) {
              public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
                val response = client.get("/repos/$owner/$repo/issues/$issueNumber/labels") {
                  perPage?.let { parameter("per_page", it) }
                  page?.let { parameter("page", it) }
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
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val issueNumber: Long,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.post("/repos/$owner/$repo/issues/$issueNumber/labels")
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
                val response = client.post("/repos/$owner/$repo/issues/$issueNumber/labels") {
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
                val response = client.post("/repos/$owner/$repo/issues/$issueNumber/labels") {
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
                val response = client.post("/repos/$owner/$repo/issues/$issueNumber/labels") {
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

              @JvmInline
              @Serializable
              public value class LabelsStrings(
                public val labels: List<String>? = null,
              )

              @JvmInline
              @Serializable
              public value class Name(
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
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val issueNumber: Long,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.put("/repos/$owner/$repo/issues/$issueNumber/labels")
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
                val response = client.put("/repos/$owner/$repo/issues/$issueNumber/labels") {
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
                val response = client.put("/repos/$owner/$repo/issues/$issueNumber/labels") {
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
                val response = client.put("/repos/$owner/$repo/issues/$issueNumber/labels") {
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
                val response = client.put("/repos/$owner/$repo/issues/$issueNumber/labels") {
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
                val response = client.put("/repos/$owner/$repo/issues/$issueNumber/labels") {
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

              @JvmInline
              @Serializable
              public value class LabelsStrings(
                public val labels: List<String>? = null,
              )

              @JvmInline
              @Serializable
              public value class LabelsNames(
                public val labels: List<Name>? = null,
              )

              @JvmInline
              @Serializable
              public value class Name(
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
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val issueNumber: Long,
              private val name: String,
            ) {
              public val delete: Delete = Delete(client, owner, repo, issueNumber, name)

              public class Delete internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val issueNumber: Long,
                private val name: String,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.delete("/repos/$owner/$repo/issues/$issueNumber/labels/$name")
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
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val issueNumber: Long,
          ) {
            public val delete: Delete = Delete(client, owner, repo, issueNumber)

            public val put: Put = Put(client, owner, repo, issueNumber)

            public class Delete internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val issueNumber: Long,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.delete("/repos/$owner/$repo/issues/$issueNumber/lock")
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
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val issueNumber: Long,
            ) {
              public suspend operator fun invoke(lockReason: LockReason? = null): Response {
                val response = client.put("/repos/$owner/$repo/issues/$issueNumber/lock") {
                  if (lockReason != null) {
                    contentType(ContentType.Application.Json)
                    setBody(Body(lockReason = lockReason))
                  }
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

              @JvmInline
              @Serializable
              internal value class Body(
                @SerialName("lock_reason")
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
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val issueNumber: Long,
          ) {
            public val `get`: Get = Get(client, owner, repo, issueNumber)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val issueNumber: Long,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.get("/repos/$owner/$repo/issues/$issueNumber/parent")
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
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val issueNumber: Long,
          ) {
            public val `get`: Get = Get(client, owner, repo, issueNumber)

            public val post: Post = Post(client, owner, repo, issueNumber)

            public fun reactionId(reactionId: Long): ReactionIdPath = ReactionIdPath(client, owner, repo, issueNumber, reactionId)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val issueNumber: Long,
            ) {
              public suspend operator fun invoke(
                content: Content? = null,
                perPage: Long? = 30L,
                page: Long? = 1L,
              ): Response {
                val response = client.get("/repos/$owner/$repo/issues/$issueNumber/reactions") {
                  content?.let { parameter("content", it.value) }
                  perPage?.let { parameter("per_page", it) }
                  page?.let { parameter("page", it) }
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
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val issueNumber: Long,
            ) {
              public suspend operator fun invoke(content: Content): Response {
                val response = client.post("/repos/$owner/$repo/issues/$issueNumber/reactions") {
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

              @JvmInline
              @Serializable
              internal value class Body(
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
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val issueNumber: Long,
              private val reactionId: Long,
            ) {
              public val delete: Delete = Delete(client, owner, repo, issueNumber, reactionId)

              public class Delete internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val issueNumber: Long,
                private val reactionId: Long,
              ) {
                public suspend operator fun invoke() {
                  client.delete("/repos/$owner/$repo/issues/$issueNumber/reactions/$reactionId")
                }
              }
            }
          }

          public class SubIssue internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val issueNumber: Long,
          ) {
            public val delete: Delete = Delete(client, owner, repo, issueNumber)

            public class Delete internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val issueNumber: Long,
            ) {
              public suspend operator fun invoke(subIssueId: Long): Response {
                val response = client.delete("/repos/$owner/$repo/issues/$issueNumber/sub_issue") {
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

              @JvmInline
              @Serializable
              internal value class Body(
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
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val issueNumber: Long,
          ) {
            public val `get`: Get = Get(client, owner, repo, issueNumber)

            public val post: Post = Post(client, owner, repo, issueNumber)

            public val priority: Priority = Priority(client, owner, repo, issueNumber)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val issueNumber: Long,
            ) {
              public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
                val response = client.get("/repos/$owner/$repo/issues/$issueNumber/sub_issues") {
                  perPage?.let { parameter("per_page", it) }
                  page?.let { parameter("page", it) }
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
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val issueNumber: Long,
            ) {
              public suspend operator fun invoke(subIssueId: Long, replaceParent: Boolean? = null): Response {
                val response = client.post("/repos/$owner/$repo/issues/$issueNumber/sub_issues") {
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

              @Serializable
              internal data class Body(
                @SerialName("sub_issue_id")
                public val subIssueId: Long,
                @SerialName("replace_parent")
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
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val issueNumber: Long,
            ) {
              public val patch: Patch = Patch(client, owner, repo, issueNumber)

              public class Patch internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val issueNumber: Long,
              ) {
                public suspend operator fun invoke(
                  subIssueId: Long,
                  afterId: Long? = null,
                  beforeId: Long? = null,
                ): Response {
                  val response = client.patch("/repos/$owner/$repo/issues/$issueNumber/sub_issues/priority") {
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

                @Serializable
                internal data class Body(
                  @SerialName("sub_issue_id")
                  public val subIssueId: Long,
                  @SerialName("after_id")
                  public val afterId: Long? = null,
                  @SerialName("before_id")
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

          public class Timeline internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val issueNumber: Long,
          ) {
            public val `get`: Get = Get(client, owner, repo, issueNumber)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val issueNumber: Long,
            ) {
              public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
                val response = client.get("/repos/$owner/$repo/issues/$issueNumber/timeline") {
                  perPage?.let { parameter("per_page", it) }
                  page?.let { parameter("page", it) }
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
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val `get`: Get = Get(client, owner, repo)

        public val post: Post = Post(client, owner, repo)

        public fun keyId(keyId: Long): KeyIdPath = KeyIdPath(client, owner, repo, keyId)

        public class Get internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<DeployKey> = client.get("/repos/$owner/$repo/keys") {
            perPage?.let { parameter("per_page", it) }
            page?.let { parameter("page", it) }
          }.body()
        }

        public class Post internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(
            title: String? = null,
            key: String,
            readOnly: Boolean? = null,
          ): Response {
            val response = client.post("/repos/$owner/$repo/keys") {
              contentType(ContentType.Application.Json)
              setBody(Body(title = title, key = key, readOnly = readOnly))
            }
            return when (response.status.value) {
              201 -> Response.Created(response.body())
              422 -> Response.UnprocessableEntity(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          @Serializable
          internal data class Body(
            public val title: String? = null,
            public val key: String,
            @SerialName("read_only")
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
          private val keyId: Long,
        ) {
          public val delete: Delete = Delete(client, owner, repo, keyId)

          public val `get`: Get = Get(client, owner, repo, keyId)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val keyId: Long,
          ) {
            public suspend operator fun invoke() {
              client.delete("/repos/$owner/$repo/keys/$keyId")
            }
          }

          public class Get internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val keyId: Long,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.get("/repos/$owner/$repo/keys/$keyId")
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
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val `get`: Get = Get(client, owner, repo)

        public val post: Post = Post(client, owner, repo)

        public fun name(name: String): NamePath = NamePath(client, owner, repo, name)

        public class Get internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
            val response = client.get("/repos/$owner/$repo/labels") {
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
              public val `value`: List<Label>,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class Post internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(
            name: String,
            color: String? = null,
            description: String? = null,
          ): Response {
            val response = client.post("/repos/$owner/$repo/labels") {
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

          @Serializable
          internal data class Body(
            public val name: String,
            public val color: String? = null,
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
          private val name: String,
        ) {
          public val delete: Delete = Delete(client, owner, repo, name)

          public val `get`: Get = Get(client, owner, repo, name)

          public val patch: Patch = Patch(client, owner, repo, name)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val name: String,
          ) {
            public suspend operator fun invoke() {
              client.delete("/repos/$owner/$repo/labels/$name")
            }
          }

          public class Get internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val name: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.get("/repos/$owner/$repo/labels/$name")
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
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val name: String,
          ) {
            public suspend operator fun invoke(
              newName: String? = null,
              color: String? = null,
              description: String? = null,
            ): Label = client.patch("/repos/$owner/$repo/labels/$name") {
              if (newName != null || color != null || description != null) {
                contentType(ContentType.Application.Json)
                setBody(Body(newName = newName, color = color, description = description))
              }
            }.body()

            @Serializable
            internal data class Body(
              @SerialName("new_name")
              public val newName: String? = null,
              public val color: String? = null,
              public val description: String? = null,
            )
          }
        }
      }

      public class Languages internal constructor(
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val `get`: Get = Get(client, owner, repo)

        public class Get internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(): Language = client.get("/repos/$owner/$repo/languages").body()
        }
      }

      public class License internal constructor(
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val `get`: Get = Get(client, owner, repo)

        public class Get internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(ref: String? = null): Response {
            val response = client.get("/repos/$owner/$repo/license") {
              ref?.let { parameter("ref", it) }
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
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val post: Post = Post(client, owner, repo)

        public class Post internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(branch: String): Response {
            val response = client.post("/repos/$owner/$repo/merge-upstream") {
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

          @JvmInline
          @Serializable
          internal value class Body(
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
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val post: Post = Post(client, owner, repo)

        public class Post internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(
            base: String,
            head: String,
            commitMessage: String? = null,
          ): Response {
            val response = client.post("/repos/$owner/$repo/merges") {
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

          @Serializable
          internal data class Body(
            public val base: String,
            public val head: String,
            @SerialName("commit_message")
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
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val `get`: Get = Get(client, owner, repo)

        public val post: Post = Post(client, owner, repo)

        public fun milestoneNumber(milestoneNumber: Long): MilestoneNumberPath = MilestoneNumberPath(client, owner, repo, milestoneNumber)

        public class Get internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(
            state: State? = State.Open,
            sort: Sort? = Sort.DueOn,
            direction: Direction? = Direction.Asc,
            perPage: Long? = 30L,
            page: Long? = 1L,
          ): Response {
            val response = client.get("/repos/$owner/$repo/milestones") {
              state?.let { parameter("state", it.value) }
              sort?.let { parameter("sort", it.value) }
              direction?.let { parameter("direction", it.value) }
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(
            title: String,
            state: State? = null,
            description: String? = null,
            dueOn: Instant? = null,
          ): Response {
            val response = client.post("/repos/$owner/$repo/milestones") {
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

          @Serializable
          internal data class Body(
            public val title: String,
            public val state: State? = null,
            public val description: String? = null,
            @SerialName("due_on")
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
          private val milestoneNumber: Long,
        ) {
          public val delete: Delete = Delete(client, owner, repo, milestoneNumber)

          public val `get`: Get = Get(client, owner, repo, milestoneNumber)

          public val patch: Patch = Patch(client, owner, repo, milestoneNumber)

          public val labels: Labels = Labels(client, owner, repo, milestoneNumber)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val milestoneNumber: Long,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.delete("/repos/$owner/$repo/milestones/$milestoneNumber")
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
            private val owner: String,
            private val repo: String,
            private val milestoneNumber: Long,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.get("/repos/$owner/$repo/milestones/$milestoneNumber")
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
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val milestoneNumber: Long,
          ) {
            public suspend operator fun invoke(
              title: String? = null,
              state: State? = null,
              description: String? = null,
              dueOn: Instant? = null,
            ): Milestone = client.patch("/repos/$owner/$repo/milestones/$milestoneNumber") {
              if (title != null || state != null || description != null || dueOn != null) {
                contentType(ContentType.Application.Json)
                setBody(Body(title = title, state = state, description = description, dueOn = dueOn))
              }
            }.body()

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
            internal data class Body(
              public val title: String? = null,
              public val state: State? = null,
              public val description: String? = null,
              @SerialName("due_on")
              public val dueOn: Instant? = null,
            )
          }

          public class Labels internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val milestoneNumber: Long,
          ) {
            public val `get`: Get = Get(client, owner, repo, milestoneNumber)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val milestoneNumber: Long,
            ) {
              public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<Label> = client.get("/repos/$owner/$repo/milestones/$milestoneNumber/labels") {
                perPage?.let { parameter("per_page", it) }
                page?.let { parameter("page", it) }
              }.body()
            }
          }
        }
      }

      public class Notifications internal constructor(
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val `get`: Get = Get(client, owner, repo)

        public val put: Put = Put(client, owner, repo)

        public class Get internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(
            all: Boolean? = false,
            participating: Boolean? = false,
            since: Instant? = null,
            before: Instant? = null,
            perPage: Long? = 30L,
            page: Long? = 1L,
          ): List<Thread> = client.get("/repos/$owner/$repo/notifications") {
            all?.let { parameter("all", it) }
            participating?.let { parameter("participating", it) }
            since?.let { parameter("since", it.toString()) }
            before?.let { parameter("before", it.toString()) }
            perPage?.let { parameter("per_page", it) }
            page?.let { parameter("page", it) }
          }.body()
        }

        public class Put internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(lastReadAt: Instant? = null): Response {
            val response = client.put("/repos/$owner/$repo/notifications") {
              if (lastReadAt != null) {
                contentType(ContentType.Application.Json)
                setBody(Body(lastReadAt = lastReadAt))
              }
            }
            return when (response.status.value) {
              202 -> response.body<Response.Accepted>()
              205 -> Response.ResetContent
              else -> throw ResponseException(response, "")
            }
          }

          @JvmInline
          @Serializable
          internal value class Body(
            @SerialName("last_read_at")
            public val lastReadAt: Instant? = null,
          )

          public sealed interface Response {
            @Serializable
            public data class Accepted(
              public val message: String? = null,
              public val url: String? = null,
            ) : Response

            public data object ResetContent : Response
          }
        }
      }

      public class Pages internal constructor(
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val delete: Delete = Delete(client, owner, repo)

        public val `get`: Get = Get(client, owner, repo)

        public val post: Post = Post(client, owner, repo)

        public val put: Put = Put(client, owner, repo)

        public val builds: Builds = Builds(client, owner, repo)

        public val deployments: Deployments = Deployments(client, owner, repo)

        public val health: Health = Health(client, owner, repo)

        public class Delete internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.delete("/repos/$owner/$repo/pages")
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.get("/repos/$owner/$repo/pages")
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(buildType: BuildType? = null, source: Source? = null): Response {
            val response = client.post("/repos/$owner/$repo/pages") {
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
          @Serializable
          public data class Source(
            public val branch: String,
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
          @Serializable
          internal data class Body(
            @SerialName("build_type")
            public val buildType: BuildType? = null,
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(
            cname: String? = null,
            httpsEnforced: Boolean? = null,
            buildType: BuildType? = null,
            source: Source? = null,
          ): Response {
            val response = client.put("/repos/$owner/$repo/pages") {
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
                return json.attemptDeserialize(
                  value,
                  BranchAndPath::class to { decodeFromJsonElement(BranchAndPath.serializer(), it) },
                  GhPagesOrMasterOrMasterDocs::class to { decodeFromJsonElement(GhPagesOrMasterOrMasterDocs.serializer(), it) },
                )
              }

              override fun serialize(encoder: Encoder, `value`: Source) {
                when(value) {
                  is GhPagesOrMasterOrMasterDocs -> encoder.encodeSerializableValue(GhPagesOrMasterOrMasterDocs.serializer(), value)
                  is BranchAndPath -> encoder.encodeSerializableValue(BranchAndPath.serializer(), value)
                }
              }
            }
          }

          @Serializable
          internal data class Body(
            public val cname: String? = null,
            @SerialName("https_enforced")
            public val httpsEnforced: Boolean? = null,
            @SerialName("build_type")
            public val buildType: BuildType? = null,
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public val `get`: Get = Get(client, owner, repo)

          public val post: Post = Post(client, owner, repo)

          public val latest: Latest = Latest(client, owner, repo)

          public fun buildId(buildId: Long): BuildIdPath = BuildIdPath(client, owner, repo, buildId)

          public class Get internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<PageBuild> = client.get("/repos/$owner/$repo/pages/builds") {
              perPage?.let { parameter("per_page", it) }
              page?.let { parameter("page", it) }
            }.body()
          }

          public class Post internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public suspend operator fun invoke(): PageBuildStatus = client.post("/repos/$owner/$repo/pages/builds").body()
          }

          public class Latest internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public val `get`: Get = Get(client, owner, repo)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
            ) {
              public suspend operator fun invoke(): PageBuild = client.get("/repos/$owner/$repo/pages/builds/latest").body()
            }
          }

          public class BuildIdPath internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val buildId: Long,
          ) {
            public val `get`: Get = Get(client, owner, repo, buildId)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val buildId: Long,
            ) {
              public suspend operator fun invoke(): PageBuild = client.get("/repos/$owner/$repo/pages/builds/$buildId").body()
            }
          }
        }

        public class Deployments internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public val post: Post = Post(client, owner, repo)

          public fun pagesDeploymentId(pagesDeploymentId: Long): PagesDeploymentIdPath = PagesDeploymentIdPath(client, owner, repo, pagesDeploymentId.toString())

          public fun pagesDeploymentId(pagesDeploymentId: String): PagesDeploymentIdPath = PagesDeploymentIdPath(client, owner, repo, pagesDeploymentId)

          public class Post internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public suspend operator fun invoke(
              artifactId: Double? = null,
              artifactUrl: String? = null,
              environment: String? = null,
              pagesBuildVersion: String,
              oidcToken: String,
            ): Response {
              val response = client.post("/repos/$owner/$repo/pages/deployments") {
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
            @Serializable
            internal data class Body(
              @SerialName("artifact_id")
              public val artifactId: Double? = null,
              @SerialName("artifact_url")
              public val artifactUrl: String? = null,
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
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val pagesDeploymentId: String,
          ) {
            public val `get`: Get = Get(client, owner, repo, pagesDeploymentId)

            public val cancel: Cancel = Cancel(client, owner, repo, pagesDeploymentId)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val pagesDeploymentId: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.get("/repos/$owner/$repo/pages/deployments/$pagesDeploymentId")
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
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val pagesDeploymentId: String,
            ) {
              public val post: Post = Post(client, owner, repo, pagesDeploymentId)

              public class Post internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val pagesDeploymentId: String,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.post("/repos/$owner/$repo/pages/deployments/$pagesDeploymentId/cancel")
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public val `get`: Get = Get(client, owner, repo)

          public class Get internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.get("/repos/$owner/$repo/pages/health")
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
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val delete: Delete = Delete(client, owner, repo)

        public val `get`: Get = Get(client, owner, repo)

        public val put: Put = Put(client, owner, repo)

        public class Delete internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.delete("/repos/$owner/$repo/private-vulnerability-reporting")
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.get("/repos/$owner/$repo/private-vulnerability-reporting")
            return when (response.status.value) {
              200 -> response.body<Response.Ok>()
              422 -> Response.UnprocessableEntity(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            @JvmInline
            @Serializable
            public value class Ok(
              public val enabled: Boolean,
            ) : Response

            public data class UnprocessableEntity(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class Put internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.put("/repos/$owner/$repo/private-vulnerability-reporting")
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
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val values: Values = Values(client, owner, repo)

        public class Values internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public val `get`: Get = Get(client, owner, repo)

          public val patch: Patch = Patch(client, owner, repo)

          public class Get internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.get("/repos/$owner/$repo/properties/values")
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
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public suspend operator fun invoke(properties: List<CustomPropertyValue>): Response {
              val response = client.patch("/repos/$owner/$repo/properties/values") {
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

            @JvmInline
            @Serializable
            internal value class Body(
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
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val `get`: Get = Get(client, owner, repo)

        public val post: Post = Post(client, owner, repo)

        public val comments: Comments = Comments(client, owner, repo)

        public fun pullNumber(pullNumber: Long): PullNumberPath = PullNumberPath(client, owner, repo, pullNumber)

        public class Get internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
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
            val response = client.get("/repos/$owner/$repo/pulls") {
              state?.let { parameter("state", it.value) }
              head?.let { parameter("head", it) }
              base?.let { parameter("base", it) }
              sort?.let { parameter("sort", it.value) }
              direction?.let { parameter("direction", it.value) }
              perPage?.let { parameter("per_page", it) }
              page?.let { parameter("page", it) }
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
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
            val response = client.post("/repos/$owner/$repo/pulls") {
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

          @Serializable
          internal data class Body(
            public val title: String? = null,
            public val head: String,
            @SerialName("head_repo")
            public val headRepo: String? = null,
            public val base: String,
            public val body: String? = null,
            @SerialName("maintainer_can_modify")
            public val maintainerCanModify: Boolean? = null,
            public val draft: Boolean? = null,
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public val `get`: Get = Get(client, owner, repo)

          public fun commentId(commentId: Long): CommentIdPath = CommentIdPath(client, owner, repo, commentId)

          public class Get internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public suspend operator fun invoke(
              sort: Sort? = null,
              direction: Direction? = null,
              since: Instant? = null,
              perPage: Long? = 30L,
              page: Long? = 1L,
            ): List<PullRequestReviewComment> = client.get("/repos/$owner/$repo/pulls/comments") {
              sort?.let { parameter("sort", it.value) }
              direction?.let { parameter("direction", it.value) }
              since?.let { parameter("since", it.toString()) }
              perPage?.let { parameter("per_page", it) }
              page?.let { parameter("page", it) }
            }.body()

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
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val commentId: Long,
          ) {
            public val delete: Delete = Delete(client, owner, repo, commentId)

            public val `get`: Get = Get(client, owner, repo, commentId)

            public val patch: Patch = Patch(client, owner, repo, commentId)

            public val reactions: Reactions = Reactions(client, owner, repo, commentId)

            public class Delete internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val commentId: Long,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.delete("/repos/$owner/$repo/pulls/comments/$commentId")
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
              private val owner: String,
              private val repo: String,
              private val commentId: Long,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.get("/repos/$owner/$repo/pulls/comments/$commentId")
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
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val commentId: Long,
            ) {
              public suspend operator fun invoke(body: String): PullRequestReviewComment = client.patch("/repos/$owner/$repo/pulls/comments/$commentId") {
                contentType(ContentType.Application.Json)
                setBody(Body(body = body))
              }.body()

              @JvmInline
              @Serializable
              internal value class Body(
                public val body: String,
              )
            }

            public class Reactions internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val commentId: Long,
            ) {
              public val `get`: Get = Get(client, owner, repo, commentId)

              public val post: Post = Post(client, owner, repo, commentId)

              public fun reactionId(reactionId: Long): ReactionIdPath = ReactionIdPath(client, owner, repo, commentId, reactionId)

              public class Get internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val commentId: Long,
              ) {
                public suspend operator fun invoke(
                  content: Content? = null,
                  perPage: Long? = 30L,
                  page: Long? = 1L,
                ): Response {
                  val response = client.get("/repos/$owner/$repo/pulls/comments/$commentId/reactions") {
                    content?.let { parameter("content", it.value) }
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
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val commentId: Long,
              ) {
                public suspend operator fun invoke(content: Content): Response {
                  val response = client.post("/repos/$owner/$repo/pulls/comments/$commentId/reactions") {
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

                @JvmInline
                @Serializable
                internal value class Body(
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
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val commentId: Long,
                private val reactionId: Long,
              ) {
                public val delete: Delete = Delete(client, owner, repo, commentId, reactionId)

                public class Delete internal constructor(
                  private val client: HttpClient,
                  private val owner: String,
                  private val repo: String,
                  private val commentId: Long,
                  private val reactionId: Long,
                ) {
                  public suspend operator fun invoke() {
                    client.delete("/repos/$owner/$repo/pulls/comments/$commentId/reactions/$reactionId")
                  }
                }
              }
            }
          }
        }

        public class PullNumberPath internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
          private val pullNumber: Long,
        ) {
          public val `get`: Get = Get(client, owner, repo, pullNumber)

          public val patch: Patch = Patch(client, owner, repo, pullNumber)

          public val codespaces: Codespaces = Codespaces(client, owner, repo, pullNumber)

          public val comments: Comments = Comments(client, owner, repo, pullNumber)

          public val commits: Commits = Commits(client, owner, repo, pullNumber)

          public val files: Files = Files(client, owner, repo, pullNumber)

          public val merge: Merge = Merge(client, owner, repo, pullNumber)

          public val requestedReviewers: RequestedReviewers =
              RequestedReviewers(client, owner, repo, pullNumber)

          public val reviews: Reviews = Reviews(client, owner, repo, pullNumber)

          public val updateBranch: UpdateBranch = UpdateBranch(client, owner, repo, pullNumber)

          public class Get internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val pullNumber: Long,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.get("/repos/$owner/$repo/pulls/$pullNumber")
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
            private val owner: String,
            private val repo: String,
            private val pullNumber: Long,
          ) {
            public suspend operator fun invoke(
              title: String? = null,
              body: String? = null,
              state: State? = null,
              base: String? = null,
              maintainerCanModify: Boolean? = null,
            ): Response {
              val response = client.patch("/repos/$owner/$repo/pulls/$pullNumber") {
                if (title != null || body != null || state != null || base != null || maintainerCanModify != null) {
                  contentType(ContentType.Application.Json)
                  setBody(Body(title = title, body = body, state = state, base = base, maintainerCanModify = maintainerCanModify))
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
            internal data class Body(
              public val title: String? = null,
              public val body: String? = null,
              public val state: State? = null,
              public val base: String? = null,
              @SerialName("maintainer_can_modify")
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
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val pullNumber: Long,
          ) {
            public val post: Post = Post(client, owner, repo, pullNumber)

            public class Post internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val pullNumber: Long,
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
                val response = client.post("/repos/$owner/$repo/pulls/$pullNumber/codespaces") {
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

              @Serializable
              internal data class Body(
                public val location: String? = null,
                public val geo: Geo? = null,
                @SerialName("client_ip")
                public val clientIp: String? = null,
                public val machine: String? = null,
                @SerialName("devcontainer_path")
                public val devcontainerPath: String? = null,
                @SerialName("multi_repo_permissions_opt_out")
                public val multiRepoPermissionsOptOut: Boolean? = null,
                @SerialName("working_directory")
                public val workingDirectory: String? = null,
                @SerialName("idle_timeout_minutes")
                public val idleTimeoutMinutes: Long? = null,
                @SerialName("display_name")
                public val displayName: String? = null,
                @SerialName("retention_period_minutes")
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

          public class Comments internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val pullNumber: Long,
          ) {
            public val `get`: Get = Get(client, owner, repo, pullNumber)

            public val post: Post = Post(client, owner, repo, pullNumber)

            public fun commentId(commentId: Long): CommentIdPath = CommentIdPath(client, owner, repo, pullNumber, commentId)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val pullNumber: Long,
            ) {
              public suspend operator fun invoke(
                sort: Sort? = Sort.Created,
                direction: Direction? = null,
                since: Instant? = null,
                perPage: Long? = 30L,
                page: Long? = 1L,
              ): List<PullRequestReviewComment> = client.get("/repos/$owner/$repo/pulls/$pullNumber/comments") {
                sort?.let { parameter("sort", it.value) }
                direction?.let { parameter("direction", it.value) }
                since?.let { parameter("since", it.toString()) }
                perPage?.let { parameter("per_page", it) }
                page?.let { parameter("page", it) }
              }.body()

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
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val pullNumber: Long,
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
                val response = client.post("/repos/$owner/$repo/pulls/$pullNumber/comments") {
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

              @Serializable
              internal data class Body(
                public val body: String,
                @SerialName("commit_id")
                public val commitId: String,
                public val path: String,
                public val position: Long? = null,
                public val side: Side? = null,
                public val line: Long? = null,
                @SerialName("start_line")
                public val startLine: Long? = null,
                @SerialName("start_side")
                public val startSide: StartSide? = null,
                @SerialName("in_reply_to")
                public val inReplyTo: Long? = null,
                @SerialName("subject_type")
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
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val pullNumber: Long,
              private val commentId: Long,
            ) {
              public val replies: Replies = Replies(client, owner, repo, pullNumber, commentId)

              public class Replies internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val pullNumber: Long,
                private val commentId: Long,
              ) {
                public val post: Post = Post(client, owner, repo, pullNumber, commentId)

                public class Post internal constructor(
                  private val client: HttpClient,
                  private val owner: String,
                  private val repo: String,
                  private val pullNumber: Long,
                  private val commentId: Long,
                ) {
                  public suspend operator fun invoke(body: String): Response {
                    val response = client.post("/repos/$owner/$repo/pulls/$pullNumber/comments/$commentId/replies") {
                      contentType(ContentType.Application.Json)
                      setBody(Body(body = body))
                    }
                    return when (response.status.value) {
                      201 -> Response.Created(response.body())
                      404 -> Response.NotFound(response.body())
                      else -> throw ResponseException(response, "")
                    }
                  }

                  @JvmInline
                  @Serializable
                  internal value class Body(
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
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val pullNumber: Long,
          ) {
            public val `get`: Get = Get(client, owner, repo, pullNumber)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val pullNumber: Long,
            ) {
              public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<Commit> = client.get("/repos/$owner/$repo/pulls/$pullNumber/commits") {
                perPage?.let { parameter("per_page", it) }
                page?.let { parameter("page", it) }
              }.body()
            }
          }

          public class Files internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val pullNumber: Long,
          ) {
            public val `get`: Get = Get(client, owner, repo, pullNumber)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val pullNumber: Long,
            ) {
              public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
                val response = client.get("/repos/$owner/$repo/pulls/$pullNumber/files") {
                  perPage?.let { parameter("per_page", it) }
                  page?.let { parameter("page", it) }
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

          public class Merge internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val pullNumber: Long,
          ) {
            public val `get`: Get = Get(client, owner, repo, pullNumber)

            public val put: Put = Put(client, owner, repo, pullNumber)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val pullNumber: Long,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.get("/repos/$owner/$repo/pulls/$pullNumber/merge")
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
              private val owner: String,
              private val repo: String,
              private val pullNumber: Long,
            ) {
              public suspend operator fun invoke(
                commitTitle: String? = null,
                commitMessage: String? = null,
                sha: String? = null,
                mergeMethod: MergeMethod? = null,
              ): Response {
                val response = client.put("/repos/$owner/$repo/pulls/$pullNumber/merge") {
                  if (commitTitle != null || commitMessage != null || sha != null || mergeMethod != null) {
                    contentType(ContentType.Application.Json)
                    setBody(Body(commitTitle = commitTitle, commitMessage = commitMessage, sha = sha, mergeMethod = mergeMethod))
                  }
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

              @Serializable
              internal data class Body(
                @SerialName("commit_title")
                public val commitTitle: String? = null,
                @SerialName("commit_message")
                public val commitMessage: String? = null,
                public val sha: String? = null,
                @SerialName("merge_method")
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

                @Serializable
                public data class MethodNotAllowed(
                  public val message: String? = null,
                  @SerialName("documentation_url")
                  public val documentationUrl: String? = null,
                ) : Response

                @Serializable
                public data class Conflict(
                  public val message: String? = null,
                  @SerialName("documentation_url")
                  public val documentationUrl: String? = null,
                ) : Response

                public data class UnprocessableEntity(
                  public val `value`: ValidationError,
                ) : Response
              }
            }
          }

          public class RequestedReviewers internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val pullNumber: Long,
          ) {
            public val delete: Delete = Delete(client, owner, repo, pullNumber)

            public val `get`: Get = Get(client, owner, repo, pullNumber)

            public val post: Post = Post(client, owner, repo, pullNumber)

            public class Delete internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val pullNumber: Long,
            ) {
              public suspend operator fun invoke(reviewers: List<String>, teamReviewers: List<String>? = null): Response {
                val response = client.delete("/repos/$owner/$repo/pulls/$pullNumber/requested_reviewers") {
                  contentType(ContentType.Application.Json)
                  setBody(Body(reviewers = reviewers, teamReviewers = teamReviewers))
                }
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  422 -> Response.UnprocessableEntity(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              @Serializable
              internal data class Body(
                public val reviewers: List<String>,
                @SerialName("team_reviewers")
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
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val pullNumber: Long,
            ) {
              public suspend operator fun invoke(): PullRequestReviewRequest = client.get("/repos/$owner/$repo/pulls/$pullNumber/requested_reviewers").body()
            }

            public class Post internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val pullNumber: Long,
            ) {
              public suspend operator fun invoke(reviewers: List<String>? = null, teamReviewers: List<String>? = null): Response {
                val response = client.post("/repos/$owner/$repo/pulls/$pullNumber/requested_reviewers") {
                  if (reviewers != null || teamReviewers != null) {
                    contentType(ContentType.Application.Json)
                    setBody(Body(reviewers = reviewers, teamReviewers = teamReviewers))
                  }
                }
                return when (response.status.value) {
                  201 -> Response.Created(response.body())
                  403 -> Response.Forbidden(response.body())
                  422 -> Response.UnprocessableEntity
                  else -> throw ResponseException(response, "")
                }
              }

              @Serializable
              internal data class Body(
                public val reviewers: List<String>? = null,
                @SerialName("team_reviewers")
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
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val pullNumber: Long,
          ) {
            public val `get`: Get = Get(client, owner, repo, pullNumber)

            public val post: Post = Post(client, owner, repo, pullNumber)

            public fun reviewId(reviewId: Long): ReviewIdPath = ReviewIdPath(client, owner, repo, pullNumber, reviewId)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val pullNumber: Long,
            ) {
              public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<PullRequestReview> = client.get("/repos/$owner/$repo/pulls/$pullNumber/reviews") {
                perPage?.let { parameter("per_page", it) }
                page?.let { parameter("page", it) }
              }.body()
            }

            public class Post internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val pullNumber: Long,
            ) {
              public suspend operator fun invoke(
                commitId: String? = null,
                body: String? = null,
                event: Event? = null,
                comments: List<Comments>? = null,
              ): Response {
                val response = client.post("/repos/$owner/$repo/pulls/$pullNumber/reviews") {
                  if (commitId != null || body != null || event != null || comments != null) {
                    contentType(ContentType.Application.Json)
                    setBody(Body(commitId = commitId, body = body, event = event, comments = comments))
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
              public enum class Event(
                public val `value`: String,
              ) {
                APPROVE("APPROVE"),
                @SerialName("REQUEST_CHANGES")
                REQUESTCHANGES("REQUEST_CHANGES"),
                COMMENT("COMMENT"),
                ;
              }

              @Serializable
              public data class Comments(
                public val path: String,
                public val position: Long? = null,
                public val body: String,
                public val line: Long? = null,
                public val side: String? = null,
                @SerialName("start_line")
                public val startLine: Long? = null,
                @SerialName("start_side")
                public val startSide: String? = null,
              )

              @Serializable
              internal data class Body(
                @SerialName("commit_id")
                public val commitId: String? = null,
                public val body: String? = null,
                public val event: Event? = null,
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
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val pullNumber: Long,
              private val reviewId: Long,
            ) {
              public val delete: Delete = Delete(client, owner, repo, pullNumber, reviewId)

              public val `get`: Get = Get(client, owner, repo, pullNumber, reviewId)

              public val put: Put = Put(client, owner, repo, pullNumber, reviewId)

              public val comments: Comments = Comments(client, owner, repo, pullNumber, reviewId)

              public val dismissals: Dismissals =
                  Dismissals(client, owner, repo, pullNumber, reviewId)

              public val events: Events = Events(client, owner, repo, pullNumber, reviewId)

              public class Delete internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val pullNumber: Long,
                private val reviewId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.delete("/repos/$owner/$repo/pulls/$pullNumber/reviews/$reviewId")
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
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val pullNumber: Long,
                private val reviewId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.get("/repos/$owner/$repo/pulls/$pullNumber/reviews/$reviewId")
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
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val pullNumber: Long,
                private val reviewId: Long,
              ) {
                public suspend operator fun invoke(body: String): Response {
                  val response = client.put("/repos/$owner/$repo/pulls/$pullNumber/reviews/$reviewId") {
                    contentType(ContentType.Application.Json)
                    setBody(Body(body = body))
                  }
                  return when (response.status.value) {
                    200 -> Response.Ok(response.body())
                    422 -> Response.UnprocessableEntity(response.body())
                    else -> throw ResponseException(response, "")
                  }
                }

                @JvmInline
                @Serializable
                internal value class Body(
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
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val pullNumber: Long,
                private val reviewId: Long,
              ) {
                public val `get`: Get = Get(client, owner, repo, pullNumber, reviewId)

                public class Get internal constructor(
                  private val client: HttpClient,
                  private val owner: String,
                  private val repo: String,
                  private val pullNumber: Long,
                  private val reviewId: Long,
                ) {
                  public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
                    val response = client.get("/repos/$owner/$repo/pulls/$pullNumber/reviews/$reviewId/comments") {
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
                      public val `value`: List<ReviewComment>,
                    ) : Response

                    public data class NotFound(
                      public val `value`: BasicError,
                    ) : Response
                  }
                }
              }

              public class Dismissals internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val pullNumber: Long,
                private val reviewId: Long,
              ) {
                public val put: Put = Put(client, owner, repo, pullNumber, reviewId)

                public class Put internal constructor(
                  private val client: HttpClient,
                  private val owner: String,
                  private val repo: String,
                  private val pullNumber: Long,
                  private val reviewId: Long,
                ) {
                  public suspend operator fun invoke(message: String, event: Event? = null): Response {
                    val response = client.put("/repos/$owner/$repo/pulls/$pullNumber/reviews/$reviewId/dismissals") {
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

                  @Serializable
                  internal data class Body(
                    public val message: String,
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
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val pullNumber: Long,
                private val reviewId: Long,
              ) {
                public val post: Post = Post(client, owner, repo, pullNumber, reviewId)

                public class Post internal constructor(
                  private val client: HttpClient,
                  private val owner: String,
                  private val repo: String,
                  private val pullNumber: Long,
                  private val reviewId: Long,
                ) {
                  public suspend operator fun invoke(body: String? = null, event: Event): Response {
                    val response = client.post("/repos/$owner/$repo/pulls/$pullNumber/reviews/$reviewId/events") {
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

                  @Serializable
                  internal data class Body(
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
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val pullNumber: Long,
          ) {
            public val put: Put = Put(client, owner, repo, pullNumber)

            public class Put internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val pullNumber: Long,
            ) {
              public suspend operator fun invoke(expectedHeadSha: String? = null): Response {
                val response = client.put("/repos/$owner/$repo/pulls/$pullNumber/update-branch") {
                  if (expectedHeadSha != null) {
                    contentType(ContentType.Application.Json)
                    setBody(Body(expectedHeadSha = expectedHeadSha))
                  }
                }
                return when (response.status.value) {
                  202 -> response.body<Response.Accepted>()
                  403 -> Response.Forbidden(response.body())
                  422 -> Response.UnprocessableEntity(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              @JvmInline
              @Serializable
              internal value class Body(
                @SerialName("expected_head_sha")
                public val expectedHeadSha: String? = null,
              )

              public sealed interface Response {
                @Serializable
                public data class Accepted(
                  public val message: String? = null,
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
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val `get`: Get = Get(client, owner, repo)

        public fun dir(dir: String): DirPath = DirPath(client, owner, repo, dir)

        public class Get internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(ref: String? = null): Response {
            val response = client.get("/repos/$owner/$repo/readme") {
              ref?.let { parameter("ref", it) }
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
          private val dir: String,
        ) {
          public val `get`: Get = Get(client, owner, repo, dir)

          public class Get internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val dir: String,
          ) {
            public suspend operator fun invoke(ref: String? = null): Response {
              val response = client.get("/repos/$owner/$repo/readme/$dir") {
                ref?.let { parameter("ref", it) }
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
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val `get`: Get = Get(client, owner, repo)

        public val post: Post = Post(client, owner, repo)

        public val assets: Assets = Assets(client, owner, repo)

        public val generateNotes: GenerateNotes = GenerateNotes(client, owner, repo)

        public val latest: Latest = Latest(client, owner, repo)

        public val tags: Tags = Tags(client, owner, repo)

        public fun releaseId(releaseId: Long): ReleaseIdPath = ReleaseIdPath(client, owner, repo, releaseId)

        public class Get internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
            val response = client.get("/repos/$owner/$repo/releases") {
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
              public val `value`: List<Release>,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class Post internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
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
            val response = client.post("/repos/$owner/$repo/releases") {
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

          @Serializable
          internal data class Body(
            @SerialName("tag_name")
            public val tagName: String,
            @SerialName("target_commitish")
            public val targetCommitish: String? = null,
            public val name: String? = null,
            public val body: String? = null,
            public val draft: Boolean? = null,
            public val prerelease: Boolean? = null,
            @SerialName("discussion_category_name")
            public val discussionCategoryName: String? = null,
            @SerialName("generate_release_notes")
            public val generateReleaseNotes: Boolean? = null,
            @SerialName("make_latest")
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public fun assetId(assetId: Long): AssetIdPath = AssetIdPath(client, owner, repo, assetId)

          public class AssetIdPath internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val assetId: Long,
          ) {
            public val delete: Delete = Delete(client, owner, repo, assetId)

            public val `get`: Get = Get(client, owner, repo, assetId)

            public val patch: Patch = Patch(client, owner, repo, assetId)

            public class Delete internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val assetId: Long,
            ) {
              public suspend operator fun invoke() {
                client.delete("/repos/$owner/$repo/releases/assets/$assetId")
              }
            }

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val assetId: Long,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.get("/repos/$owner/$repo/releases/assets/$assetId")
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
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val assetId: Long,
            ) {
              public suspend operator fun invoke(
                name: String? = null,
                label: String? = null,
                state: String? = null,
              ): ReleaseAsset = client.patch("/repos/$owner/$repo/releases/assets/$assetId") {
                if (name != null || label != null || state != null) {
                  contentType(ContentType.Application.Json)
                  setBody(Body(name = name, label = label, state = state))
                }
              }.body()

              @Serializable
              internal data class Body(
                public val name: String? = null,
                public val label: String? = null,
                public val state: String? = null,
              )
            }
          }
        }

        public class GenerateNotes internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public val post: Post = Post(client, owner, repo)

          public class Post internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public suspend operator fun invoke(
              tagName: String,
              targetCommitish: String? = null,
              previousTagName: String? = null,
              configurationFilePath: String? = null,
            ): Response {
              val response = client.post("/repos/$owner/$repo/releases/generate-notes") {
                contentType(ContentType.Application.Json)
                setBody(Body(tagName = tagName, targetCommitish = targetCommitish, previousTagName = previousTagName, configurationFilePath = configurationFilePath))
              }
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            @Serializable
            internal data class Body(
              @SerialName("tag_name")
              public val tagName: String,
              @SerialName("target_commitish")
              public val targetCommitish: String? = null,
              @SerialName("previous_tag_name")
              public val previousTagName: String? = null,
              @SerialName("configuration_file_path")
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public val `get`: Get = Get(client, owner, repo)

          public class Get internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public suspend operator fun invoke(): Release = client.get("/repos/$owner/$repo/releases/latest").body()
          }
        }

        public class Tags internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public fun tag(tag: String): TagPath = TagPath(client, owner, repo, tag)

          public class TagPath internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val tag: String,
          ) {
            public val `get`: Get = Get(client, owner, repo, tag)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val tag: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.get("/repos/$owner/$repo/releases/tags/$tag")
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
          private val releaseId: Long,
        ) {
          public val delete: Delete = Delete(client, owner, repo, releaseId)

          public val `get`: Get = Get(client, owner, repo, releaseId)

          public val patch: Patch = Patch(client, owner, repo, releaseId)

          public val assets: Assets = Assets(client, owner, repo, releaseId)

          public val reactions: Reactions = Reactions(client, owner, repo, releaseId)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val releaseId: Long,
          ) {
            public suspend operator fun invoke() {
              client.delete("/repos/$owner/$repo/releases/$releaseId")
            }
          }

          public class Get internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val releaseId: Long,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.get("/repos/$owner/$repo/releases/$releaseId")
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
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val releaseId: Long,
          ) {
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
              val response = client.patch("/repos/$owner/$repo/releases/$releaseId") {
                if (tagName != null || targetCommitish != null || name != null || body != null || draft != null || prerelease != null || makeLatest != null || discussionCategoryName != null) {
                  contentType(ContentType.Application.Json)
                  setBody(Body(tagName = tagName, targetCommitish = targetCommitish, name = name, body = body, draft = draft, prerelease = prerelease, makeLatest = makeLatest, discussionCategoryName = discussionCategoryName))
                }
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

            @Serializable
            internal data class Body(
              @SerialName("tag_name")
              public val tagName: String? = null,
              @SerialName("target_commitish")
              public val targetCommitish: String? = null,
              public val name: String? = null,
              public val body: String? = null,
              public val draft: Boolean? = null,
              public val prerelease: Boolean? = null,
              @SerialName("make_latest")
              public val makeLatest: MakeLatest? = null,
              @SerialName("discussion_category_name")
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
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val releaseId: Long,
          ) {
            public val `get`: Get = Get(client, owner, repo, releaseId)

            public val post: Post = Post(client, owner, repo, releaseId)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val releaseId: Long,
            ) {
              public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<ReleaseAsset> = client.get("/repos/$owner/$repo/releases/$releaseId/assets") {
                perPage?.let { parameter("per_page", it) }
                page?.let { parameter("page", it) }
              }.body()
            }

            public class Post internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val releaseId: Long,
            ) {
              public suspend operator fun invoke(
                name: String,
                label: String? = null,
                body: ByteArray? = null,
              ): Response {
                val response = client.post("/repos/$owner/$repo/releases/$releaseId/assets") {
                  parameter("name", name)
                  label?.let { parameter("label", it) }
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
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val releaseId: Long,
          ) {
            public val `get`: Get = Get(client, owner, repo, releaseId)

            public val post: Post = Post(client, owner, repo, releaseId)

            public fun reactionId(reactionId: Long): ReactionIdPath = ReactionIdPath(client, owner, repo, releaseId, reactionId)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val releaseId: Long,
            ) {
              public suspend operator fun invoke(
                content: Content? = null,
                perPage: Long? = 30L,
                page: Long? = 1L,
              ): Response {
                val response = client.get("/repos/$owner/$repo/releases/$releaseId/reactions") {
                  content?.let { parameter("content", it.value) }
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
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val releaseId: Long,
            ) {
              public suspend operator fun invoke(content: Content): Response {
                val response = client.post("/repos/$owner/$repo/releases/$releaseId/reactions") {
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

              @JvmInline
              @Serializable
              internal value class Body(
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
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val releaseId: Long,
              private val reactionId: Long,
            ) {
              public val delete: Delete = Delete(client, owner, repo, releaseId, reactionId)

              public class Delete internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val releaseId: Long,
                private val reactionId: Long,
              ) {
                public suspend operator fun invoke() {
                  client.delete("/repos/$owner/$repo/releases/$releaseId/reactions/$reactionId")
                }
              }
            }
          }
        }
      }

      public class Rules internal constructor(
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val branches: Branches = Branches(client, owner, repo)

        public class Branches internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public fun branch(branch: String): BranchPath = BranchPath(client, owner, repo, branch)

          public class BranchPath internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val branch: String,
          ) {
            public val `get`: Get = Get(client, owner, repo, branch)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val branch: String,
            ) {
              public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<RepositoryRuleDetailed> = client.get("/repos/$owner/$repo/rules/branches/$branch") {
                perPage?.let { parameter("per_page", it) }
                page?.let { parameter("page", it) }
              }.body()
            }
          }
        }
      }

      public class Rulesets internal constructor(
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val `get`: Get = Get(client, owner, repo)

        public val post: Post = Post(client, owner, repo)

        public val ruleSuites: RuleSuites = RuleSuites(client, owner, repo)

        public fun rulesetId(rulesetId: Long): RulesetIdPath = RulesetIdPath(client, owner, repo, rulesetId)

        public class Get internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(
            perPage: Long? = 30L,
            page: Long? = 1L,
            includesParents: Boolean? = true,
            targets: String? = null,
          ): Response {
            val response = client.get("/repos/$owner/$repo/rulesets") {
              perPage?.let { parameter("per_page", it) }
              page?.let { parameter("page", it) }
              includesParents?.let { parameter("includes_parents", it) }
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
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(
            name: String,
            target: Target? = null,
            enforcement: RepositoryRuleEnforcement,
            bypassActors: List<RepositoryRulesetBypassActor>? = null,
            conditions: RepositoryRulesetConditions? = null,
            rules: List<RepositoryRule>? = null,
          ): Response {
            val response = client.post("/repos/$owner/$repo/rulesets") {
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

          @Serializable
          internal data class Body(
            public val name: String,
            public val target: Target? = null,
            public val enforcement: RepositoryRuleEnforcement,
            @SerialName("bypass_actors")
            public val bypassActors: List<RepositoryRulesetBypassActor>? = null,
            public val conditions: RepositoryRulesetConditions? = null,
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public val `get`: Get = Get(client, owner, repo)

          public fun ruleSuiteId(ruleSuiteId: Long): RuleSuiteIdPath = RuleSuiteIdPath(client, owner, repo, ruleSuiteId)

          public class Get internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public suspend operator fun invoke(
              ref: String? = null,
              timePeriod: TimePeriod? = TimePeriod.Day,
              actorName: String? = null,
              ruleSuiteResult: RuleSuiteResult? = RuleSuiteResult.All,
              perPage: Long? = 30L,
              page: Long? = 1L,
            ): Response {
              val response = client.get("/repos/$owner/$repo/rulesets/rule-suites") {
                ref?.let { parameter("ref", it) }
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
            private val owner: String,
            private val repo: String,
            private val ruleSuiteId: Long,
          ) {
            public val `get`: Get = Get(client, owner, repo, ruleSuiteId)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val ruleSuiteId: Long,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.get("/repos/$owner/$repo/rulesets/rule-suites/$ruleSuiteId")
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
          private val owner: String,
          private val repo: String,
          private val rulesetId: Long,
        ) {
          public val delete: Delete = Delete(client, owner, repo, rulesetId)

          public val `get`: Get = Get(client, owner, repo, rulesetId)

          public val put: Put = Put(client, owner, repo, rulesetId)

          public val history: History = History(client, owner, repo, rulesetId)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val rulesetId: Long,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.delete("/repos/$owner/$repo/rulesets/$rulesetId")
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
            private val owner: String,
            private val repo: String,
            private val rulesetId: Long,
          ) {
            public suspend operator fun invoke(includesParents: Boolean? = true): Response {
              val response = client.get("/repos/$owner/$repo/rulesets/$rulesetId") {
                includesParents?.let { parameter("includes_parents", it) }
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
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val rulesetId: Long,
          ) {
            public suspend operator fun invoke(
              name: String? = null,
              target: Target? = null,
              enforcement: RepositoryRuleEnforcement? = null,
              bypassActors: List<RepositoryRulesetBypassActor>? = null,
              conditions: RepositoryRulesetConditions? = null,
              rules: List<RepositoryRule>? = null,
            ): Response {
              val response = client.put("/repos/$owner/$repo/rulesets/$rulesetId") {
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
              ;
            }

            @Serializable
            internal data class Body(
              public val name: String? = null,
              public val target: Target? = null,
              public val enforcement: RepositoryRuleEnforcement? = null,
              @SerialName("bypass_actors")
              public val bypassActors: List<RepositoryRulesetBypassActor>? = null,
              public val conditions: RepositoryRulesetConditions? = null,
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
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val rulesetId: Long,
          ) {
            public val `get`: Get = Get(client, owner, repo, rulesetId)

            public fun versionId(versionId: Long): VersionIdPath = VersionIdPath(client, owner, repo, rulesetId, versionId)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val rulesetId: Long,
            ) {
              public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
                val response = client.get("/repos/$owner/$repo/rulesets/$rulesetId/history") {
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
              private val owner: String,
              private val repo: String,
              private val rulesetId: Long,
              private val versionId: Long,
            ) {
              public val `get`: Get = Get(client, owner, repo, rulesetId, versionId)

              public class Get internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val rulesetId: Long,
                private val versionId: Long,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.get("/repos/$owner/$repo/rulesets/$rulesetId/history/$versionId")
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
        private val owner: String,
        private val repo: String,
      ) {
        public val alerts: Alerts = Alerts(client, owner, repo)

        public val pushProtectionBypasses: PushProtectionBypasses =
            PushProtectionBypasses(client, owner, repo)

        public val scanHistory: ScanHistory = ScanHistory(client, owner, repo)

        public class Alerts internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public val `get`: Get = Get(client, owner, repo)

          public fun alertNumber(alertNumber: Long): AlertNumberPath = AlertNumberPath(client, owner, repo, alertNumber)

          public class Get internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
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
              val response = client.get("/repos/$owner/$repo/secret-scanning/alerts") {
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

              @Serializable
              public data class ServiceUnavailable(
                public val code: String? = null,
                public val message: String? = null,
                @SerialName("documentation_url")
                public val documentationUrl: String? = null,
              ) : Response
            }
          }

          public class AlertNumberPath internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val alertNumber: Long,
          ) {
            public val `get`: Get = Get(client, owner, repo, alertNumber)

            public val patch: Patch = Patch(client, owner, repo, alertNumber)

            public val locations: Locations = Locations(client, owner, repo, alertNumber)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val alertNumber: Long,
            ) {
              public suspend operator fun invoke(hideSecret: Boolean? = false): Response {
                val response = client.get("/repos/$owner/$repo/secret-scanning/alerts/$alertNumber") {
                  hideSecret?.let { parameter("hide_secret", it) }
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
              private val owner: String,
              private val repo: String,
              private val alertNumber: Long,
            ) {
              public suspend operator fun invoke(
                state: SecretScanningAlertState? = null,
                resolution: SecretScanningAlertResolution? = null,
                resolutionComment: SecretScanningAlertResolutionComment? = null,
                assignee: SecretScanningAlertAssignee? = null,
              ): Response {
                val response = client.patch("/repos/$owner/$repo/secret-scanning/alerts/$alertNumber") {
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

              @Serializable
              internal data class Body(
                public val state: SecretScanningAlertState? = null,
                public val resolution: SecretScanningAlertResolution? = null,
                @SerialName("resolution_comment")
                public val resolutionComment: SecretScanningAlertResolutionComment? = null,
                public val assignee: SecretScanningAlertAssignee? = null,
              )

              public sealed interface Response {
                public data class Ok(
                  public val `value`: SecretScanningAlert,
                ) : Response

                public data object BadRequest : Response

                public data object NotFound : Response

                public data object UnprocessableEntity : Response

                @Serializable
                public data class ServiceUnavailable(
                  public val code: String? = null,
                  public val message: String? = null,
                  @SerialName("documentation_url")
                  public val documentationUrl: String? = null,
                ) : Response
              }
            }

            public class Locations internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val alertNumber: Long,
            ) {
              public val `get`: Get = Get(client, owner, repo, alertNumber)

              public class Get internal constructor(
                private val client: HttpClient,
                private val owner: String,
                private val repo: String,
                private val alertNumber: Long,
              ) {
                public suspend operator fun invoke(page: Long? = 1L, perPage: Long? = 30L): Response {
                  val response = client.get("/repos/$owner/$repo/secret-scanning/alerts/$alertNumber/locations") {
                    page?.let { parameter("page", it) }
                    perPage?.let { parameter("per_page", it) }
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
        }

        public class PushProtectionBypasses internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public val post: Post = Post(client, owner, repo)

          public class Post internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public suspend operator fun invoke(reason: SecretScanningPushProtectionBypassReason, placeholderId: SecretScanningPushProtectionBypassPlaceholderId): Response {
              val response = client.post("/repos/$owner/$repo/secret-scanning/push-protection-bypasses") {
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

        public class ScanHistory internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public val `get`: Get = Get(client, owner, repo)

          public class Get internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.get("/repos/$owner/$repo/secret-scanning/scan-history")
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

      public class SecurityAdvisories internal constructor(
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val `get`: Get = Get(client, owner, repo)

        public val post: Post = Post(client, owner, repo)

        public val reports: Reports = Reports(client, owner, repo)

        public fun ghsaId(ghsaId: String): GhsaIdPath = GhsaIdPath(client, owner, repo, ghsaId)

        public class Get internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(
            direction: Direction? = Direction.Desc,
            sort: Sort? = Sort.Created,
            before: String? = null,
            after: String? = null,
            perPage: Long? = 30L,
            state: State? = null,
          ): Response {
            val response = client.get("/repos/$owner/$repo/security-advisories") {
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

        public class Post internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(body: RepositoryAdvisoryCreate): Response {
            val response = client.post("/repos/$owner/$repo/security-advisories") {
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public val post: Post = Post(client, owner, repo)

          public class Post internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public suspend operator fun invoke(body: PrivateVulnerabilityReportCreate): Response {
              val response = client.post("/repos/$owner/$repo/security-advisories/reports") {
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
          private val ghsaId: String,
        ) {
          public val `get`: Get = Get(client, owner, repo, ghsaId)

          public val patch: Patch = Patch(client, owner, repo, ghsaId)

          public val cve: Cve = Cve(client, owner, repo, ghsaId)

          public val forks: Forks = Forks(client, owner, repo, ghsaId)

          public class Get internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val ghsaId: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.get("/repos/$owner/$repo/security-advisories/$ghsaId")
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
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val ghsaId: String,
          ) {
            public suspend operator fun invoke(body: RepositoryAdvisoryUpdate): Response {
              val response = client.patch("/repos/$owner/$repo/security-advisories/$ghsaId") {
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
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val ghsaId: String,
          ) {
            public val post: Post = Post(client, owner, repo, ghsaId)

            public class Post internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val ghsaId: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.post("/repos/$owner/$repo/security-advisories/$ghsaId/cve")
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
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val ghsaId: String,
          ) {
            public val post: Post = Post(client, owner, repo, ghsaId)

            public class Post internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
              private val ghsaId: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.post("/repos/$owner/$repo/security-advisories/$ghsaId/forks")
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
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val `get`: Get = Get(client, owner, repo)

        public class Get internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
            val response = client.get("/repos/$owner/$repo/stargazers") {
              perPage?.let { parameter("per_page", it) }
              page?.let { parameter("page", it) }
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
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val codeFrequency: CodeFrequency = CodeFrequency(client, owner, repo)

        public val commitActivity: CommitActivity = CommitActivity(client, owner, repo)

        public val contributors: Contributors = Contributors(client, owner, repo)

        public val participation: Participation = Participation(client, owner, repo)

        public val punchCard: PunchCard = PunchCard(client, owner, repo)

        public class CodeFrequency internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public val `get`: Get = Get(client, owner, repo)

          public class Get internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.get("/repos/$owner/$repo/stats/code_frequency")
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public val `get`: Get = Get(client, owner, repo)

          public class Get internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.get("/repos/$owner/$repo/stats/commit_activity")
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public val `get`: Get = Get(client, owner, repo)

          public class Get internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.get("/repos/$owner/$repo/stats/contributors")
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public val `get`: Get = Get(client, owner, repo)

          public class Get internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.get("/repos/$owner/$repo/stats/participation")
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public val `get`: Get = Get(client, owner, repo)

          public class Get internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.get("/repos/$owner/$repo/stats/punch_card")
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
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public fun sha(sha: String): ShaPath = ShaPath(client, owner, repo, sha)

        public class ShaPath internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
          private val sha: String,
        ) {
          public val post: Post = Post(client, owner, repo, sha)

          public class Post internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val sha: String,
          ) {
            public suspend operator fun invoke(
              state: State,
              targetUrl: String? = null,
              description: String? = null,
              context: String? = null,
            ): Status = client.post("/repos/$owner/$repo/statuses/$sha") {
              contentType(ContentType.Application.Json)
              setBody(Body(state = state, targetUrl = targetUrl, description = description, context = context))
            }.body()

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

            @Serializable
            internal data class Body(
              public val state: State,
              @SerialName("target_url")
              public val targetUrl: String? = null,
              public val description: String? = null,
              public val context: String? = null,
            )
          }
        }
      }

      public class Subscribers internal constructor(
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val `get`: Get = Get(client, owner, repo)

        public class Get internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<SimpleUser> = client.get("/repos/$owner/$repo/subscribers") {
            perPage?.let { parameter("per_page", it) }
            page?.let { parameter("page", it) }
          }.body()
        }
      }

      public class Subscription internal constructor(
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val delete: Delete = Delete(client, owner, repo)

        public val `get`: Get = Get(client, owner, repo)

        public val put: Put = Put(client, owner, repo)

        public class Delete internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke() {
            client.delete("/repos/$owner/$repo/subscription")
          }
        }

        public class Get internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.get("/repos/$owner/$repo/subscription")
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(subscribed: Boolean? = null, ignored: Boolean? = null): RepositorySubscription = client.put("/repos/$owner/$repo/subscription") {
            if (subscribed != null || ignored != null) {
              contentType(ContentType.Application.Json)
              setBody(Body(subscribed = subscribed, ignored = ignored))
            }
          }.body()

          @Serializable
          internal data class Body(
            public val subscribed: Boolean? = null,
            public val ignored: Boolean? = null,
          )
        }
      }

      public class Tags internal constructor(
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val `get`: Get = Get(client, owner, repo)

        public class Get internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<Tag> = client.get("/repos/$owner/$repo/tags") {
            perPage?.let { parameter("per_page", it) }
            page?.let { parameter("page", it) }
          }.body()
        }
      }

      public class Tarball internal constructor(
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public fun ref(ref: String): RefPath = RefPath(client, owner, repo, ref)

        public class RefPath internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
          private val ref: String,
        ) {
          public val `get`: Get = Get(client, owner, repo, ref)

          public class Get internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val ref: String,
          ) {
            public suspend operator fun invoke() {
              client.get("/repos/$owner/$repo/tarball/$ref")
            }
          }
        }
      }

      public class Teams internal constructor(
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val `get`: Get = Get(client, owner, repo)

        public class Get internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
            val response = client.get("/repos/$owner/$repo/teams") {
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
              public val `value`: List<Team>,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response
          }
        }
      }

      public class Topics internal constructor(
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val `get`: Get = Get(client, owner, repo)

        public val put: Put = Put(client, owner, repo)

        public class Get internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(page: Long? = 1L, perPage: Long? = 30L): Response {
            val response = client.get("/repos/$owner/$repo/topics") {
              page?.let { parameter("page", it) }
              perPage?.let { parameter("per_page", it) }
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(names: List<String>): Response {
            val response = client.put("/repos/$owner/$repo/topics") {
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

          @JvmInline
          @Serializable
          internal value class Body(
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
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val clones: Clones = Clones(client, owner, repo)

        public val popular: Popular = Popular(client, owner, repo)

        public val views: Views = Views(client, owner, repo)

        public class Clones internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public val `get`: Get = Get(client, owner, repo)

          public class Get internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public suspend operator fun invoke(per: Per? = Per.Day): Response {
              val response = client.get("/repos/$owner/$repo/traffic/clones") {
                per?.let { parameter("per", it.value) }
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public val paths: Paths = Paths(client, owner, repo)

          public val referrers: Referrers = Referrers(client, owner, repo)

          public class Paths internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public val `get`: Get = Get(client, owner, repo)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.get("/repos/$owner/$repo/traffic/popular/paths")
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
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public val `get`: Get = Get(client, owner, repo)

            public class Get internal constructor(
              private val client: HttpClient,
              private val owner: String,
              private val repo: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.get("/repos/$owner/$repo/traffic/popular/referrers")
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
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public val `get`: Get = Get(client, owner, repo)

          public class Get internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
          ) {
            public suspend operator fun invoke(per: Per? = Per.Day): Response {
              val response = client.get("/repos/$owner/$repo/traffic/views") {
                per?.let { parameter("per", it.value) }
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
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val post: Post = Post(client, owner, repo)

        public class Post internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(
            newOwner: String,
            newName: String? = null,
            teamIds: List<Long>? = null,
          ): MinimalRepository = client.post("/repos/$owner/$repo/transfer") {
            contentType(ContentType.Application.Json)
            setBody(Body(newOwner = newOwner, newName = newName, teamIds = teamIds))
          }.body()

          @Serializable
          internal data class Body(
            @SerialName("new_owner")
            public val newOwner: String,
            @SerialName("new_name")
            public val newName: String? = null,
            @SerialName("team_ids")
            public val teamIds: List<Long>? = null,
          )
        }
      }

      public class VulnerabilityAlerts internal constructor(
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val delete: Delete = Delete(client, owner, repo)

        public val `get`: Get = Get(client, owner, repo)

        public val put: Put = Put(client, owner, repo)

        public class Delete internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke() {
            client.delete("/repos/$owner/$repo/vulnerability-alerts")
          }
        }

        public class Get internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.get("/repos/$owner/$repo/vulnerability-alerts")
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
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke() {
            client.put("/repos/$owner/$repo/vulnerability-alerts")
          }
        }
      }

      public class Zipball internal constructor(
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public fun ref(ref: String): RefPath = RefPath(client, owner, repo, ref)

        public class RefPath internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
          private val ref: String,
        ) {
          public val `get`: Get = Get(client, owner, repo, ref)

          public class Get internal constructor(
            private val client: HttpClient,
            private val owner: String,
            private val repo: String,
            private val ref: String,
          ) {
            public suspend operator fun invoke() {
              client.get("/repos/$owner/$repo/zipball/$ref")
            }
          }
        }
      }
    }
  }

  public class TemplateOwnerPath internal constructor(
    private val client: HttpClient,
    private val templateOwner: String,
  ) {
    public fun templateRepo(templateRepo: String): TemplateRepoPath = TemplateRepoPath(client, templateOwner, templateRepo)

    public class TemplateRepoPath internal constructor(
      private val client: HttpClient,
      private val templateOwner: String,
      private val templateRepo: String,
    ) {
      public val generate: Generate = Generate(client, templateOwner, templateRepo)

      public class Generate internal constructor(
        private val client: HttpClient,
        private val templateOwner: String,
        private val templateRepo: String,
      ) {
        public val post: Post = Post(client, templateOwner, templateRepo)

        public class Post internal constructor(
          private val client: HttpClient,
          private val templateOwner: String,
          private val templateRepo: String,
        ) {
          public suspend operator fun invoke(
            owner: String? = null,
            name: String,
            description: String? = null,
            includeAllBranches: Boolean? = null,
            `private`: Boolean? = null,
          ): FullRepository = client.post("/repos/$templateOwner/$templateRepo/generate") {
            contentType(ContentType.Application.Json)
            setBody(Body(owner = owner, name = name, description = description, includeAllBranches = includeAllBranches, private = private))
          }.body()

          @Serializable
          internal data class Body(
            public val owner: String? = null,
            public val name: String,
            public val description: String? = null,
            @SerialName("include_all_branches")
            public val includeAllBranches: Boolean? = null,
            public val `private`: Boolean? = null,
          )
        }
      }
    }
  }
}
