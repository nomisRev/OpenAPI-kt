package io.github.nomisrev.api

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlin.jvm.JvmInline
import io.github.nomisrev.model.FullRepository
import io.github.nomisrev.model.BasicError
import io.github.nomisrev.model.ValidationError
import io.github.nomisrev.model.Artifact
import io.github.nomisrev.model.ActionsCacheRetentionLimitForRepository
import io.github.nomisrev.model.ActionsCacheStorageLimitForRepository
import io.github.nomisrev.model.ActionsCacheUsageByRepository
import io.github.nomisrev.model.ActionsCacheList
import io.github.nomisrev.model.Job
import io.github.nomisrev.model.EmptyObject
import io.github.nomisrev.model.OidcCustomSubRepo
import io.github.nomisrev.model.ValidationErrorSimple
import io.github.nomisrev.model.ActionsSecret
import io.github.nomisrev.model.ActionsVariable
import io.github.nomisrev.model.ActionsEnabled
import io.github.nomisrev.model.AllowedActions
import io.github.nomisrev.model.ShaPinningRequired
import io.github.nomisrev.model.ActionsRepositoryPermissions
import io.github.nomisrev.model.ActionsWorkflowAccessToRepository
import io.github.nomisrev.model.ActionsArtifactAndLogRetentionResponse
import io.github.nomisrev.model.ActionsArtifactAndLogRetention
import io.github.nomisrev.model.ActionsForkPrContributorApproval
import io.github.nomisrev.model.ActionsForkPrWorkflowsPrivateRepos
import io.github.nomisrev.model.ActionsForkPrWorkflowsPrivateReposRequest
import io.github.nomisrev.model.SelectedActions
import io.github.nomisrev.model.ActionsGetDefaultWorkflowPermissions
import io.github.nomisrev.model.ActionsSetDefaultWorkflowPermissions
import io.github.nomisrev.model.Runner
import io.github.nomisrev.model.RunnerApplication
import io.github.nomisrev.model.AuthenticationToken
import io.github.nomisrev.model.RunnerLabel
import io.github.nomisrev.model.WorkflowRun
import kotlinx.datetime.LocalDateTime
import io.github.nomisrev.model.EnvironmentApprovals
import io.github.nomisrev.model.ReviewCustomGatesCommentRequired
import io.github.nomisrev.model.ReviewCustomGatesStateRequired
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
import io.github.nomisrev.model.PendingDeployment
import io.github.nomisrev.model.Deployment
import io.github.nomisrev.model.WorkflowRunUsage
import io.github.nomisrev.model.ActionsPublicKey
import io.github.nomisrev.model.Workflow
import kotlinx.serialization.builtins.serializer
import io.github.nomisrev.model.WorkflowDispatchResponse
import io.github.nomisrev.model.WorkflowUsage
import io.github.nomisrev.model.Activity
import io.github.nomisrev.model.SimpleUser
import io.github.nomisrev.model.Autolink
import io.github.nomisrev.model.CheckAutomatedSecurityFixes
import io.github.nomisrev.model.ShortBranch
import io.github.nomisrev.model.BranchWithProtection
import io.github.nomisrev.model.BranchProtection
import io.github.nomisrev.model.ProtectedBranch
import io.github.nomisrev.model.ProtectedBranchAdminEnforced
import io.github.nomisrev.model.ProtectedBranchPullRequestReview
import io.github.nomisrev.model.StatusCheckPolicy
import kotlinx.serialization.builtins.ListSerializer
import io.github.nomisrev.model.BranchRestrictionPolicy
import io.github.nomisrev.model.Integration
import io.github.nomisrev.model.Team
import io.github.nomisrev.model.CheckRun
import io.github.nomisrev.model.CheckAnnotation
import io.github.nomisrev.model.CheckSuite
import kotlinx.serialization.Required
import io.github.nomisrev.model.CheckSuitePreference
import io.github.nomisrev.model.CodeScanningAlertSetState
import io.github.nomisrev.model.CodeScanningAlertDismissedReason
import io.github.nomisrev.model.CodeScanningAlertDismissedComment
import io.github.nomisrev.model.CodeScanningAlertCreateRequest
import io.github.nomisrev.model.CodeScanningAlertAssignees
import io.github.nomisrev.model.CodeScanningAlertItemsResponse
import io.github.nomisrev.model.CodeScanningRef
import io.github.nomisrev.model.CodeScanningAlertSeverity
import io.github.nomisrev.model.CodeScanningAlertStateQuery
import io.github.nomisrev.model.CodeScanningAnalysisToolGuid
import io.github.nomisrev.model.CodeScanningAnalysisToolName
import io.github.nomisrev.model.CodeScanningAlertResponse
import io.github.nomisrev.model.AlertNumberRequest
import io.github.nomisrev.model.CodeScanningAutofixResponse
import io.github.nomisrev.model.CodeScanningAutofixCommitsResponse
import io.github.nomisrev.model.CodeScanningAutofixCommits
import io.github.nomisrev.model.CodeScanningAlertInstanceList
import io.github.nomisrev.model.CodeScanningAnalysisResponse
import io.github.nomisrev.model.CodeScanningAnalysisSarifId
import io.github.nomisrev.model.CodeScanningAnalysisDeletionResponse
import io.github.nomisrev.model.CodeScanningCodeqlDatabase
import io.github.nomisrev.model.CodeScanningVariantAnalysisLanguage
import io.github.nomisrev.model.CodeScanningVariantAnalysis
import io.github.nomisrev.model.CodeScanningVariantAnalysisRepoTask
import io.github.nomisrev.model.CodeScanningDefaultSetup
import io.github.nomisrev.model.CodeScanningDefaultSetupUpdateResponse
import io.github.nomisrev.model.CodeScanningDefaultSetupUpdate
import io.github.nomisrev.model.CodeScanningAnalysisCommitSha
import io.github.nomisrev.model.CodeScanningRefFull
import io.github.nomisrev.model.CodeScanningAnalysisSarifFile
import io.github.nomisrev.model.CodeScanningSarifsReceiptResponse
import io.github.nomisrev.model.CodeScanningSarifsStatusResponse
import io.github.nomisrev.model.CodeSecurityConfigurationForRepository
import io.github.nomisrev.model.CodeownersErrors
import io.github.nomisrev.model.Codespace
import io.github.nomisrev.model.CodespaceMachine
import io.github.nomisrev.model.CodespacesPermissionsCheckForDevcontainer
import io.github.nomisrev.model.RepoCodespacesSecret
import io.github.nomisrev.model.CodespacesPublicKey
import io.github.nomisrev.model.Collaborator
import io.github.nomisrev.model.RepositoryInvitation
import io.github.nomisrev.model.RepositoryCollaboratorPermission
import io.github.nomisrev.model.CommitComment
import io.github.nomisrev.model.Reaction
import io.github.nomisrev.model.Commit
import io.github.nomisrev.model.BranchShort
import io.github.nomisrev.model.PullRequestSimple
import io.github.nomisrev.model.CombinedCommitStatus
import io.github.nomisrev.model.Status
import io.github.nomisrev.model.CommunityProfile
import io.github.nomisrev.model.CommitComparison
import io.github.nomisrev.model.RepositoryRuleViolationError
import kotlinx.serialization.json.JsonClassDiscriminator
import io.github.nomisrev.model.ContentDirectory
import io.github.nomisrev.model.ContentFile
import io.github.nomisrev.model.ContentSymlink
import io.github.nomisrev.model.ContentSubmodule
import io.github.nomisrev.model.FileCommit
import io.github.nomisrev.model.Contributor
import io.github.nomisrev.model.DependabotAlertResponse
import io.github.nomisrev.model.DependabotSecret
import io.github.nomisrev.model.DependabotPublicKey
import io.github.nomisrev.model.DependencyGraphDiff
import io.github.nomisrev.model.DependencyGraphSpdxSbom
import io.github.nomisrev.model.Snapshot
import io.github.nomisrev.model.DeploymentStatus
import io.github.nomisrev.model.WaitTimer
import io.github.nomisrev.model.PreventSelfReview
import io.github.nomisrev.model.DeploymentBranchPolicySettings
import io.github.nomisrev.model.DeploymentReviewerType
import io.github.nomisrev.model.Environment
import io.github.nomisrev.model.DeploymentBranchPolicy
import io.github.nomisrev.model.DeploymentBranchPolicyNamePatternWithType
import io.github.nomisrev.model.DeploymentBranchPolicyNamePattern
import io.github.nomisrev.model.DeploymentProtectionRule
import io.github.nomisrev.model.CustomDeploymentRuleApp
import io.github.nomisrev.model.Event
import io.github.nomisrev.model.MinimalRepository
import io.github.nomisrev.model.ShortBlob
import io.github.nomisrev.model.Blob
import io.github.nomisrev.model.GitCommit
import io.github.nomisrev.model.GitRef
import io.github.nomisrev.model.GitTag
import kotlin.js.JsName
import io.github.nomisrev.model.GitTree
import io.github.nomisrev.model.WebhookConfigUrl
import io.github.nomisrev.model.WebhookConfigContentType
import io.github.nomisrev.model.WebhookConfigSecret
import io.github.nomisrev.model.WebhookConfigInsecureSsl
import io.github.nomisrev.model.WebhookConfig
import io.github.nomisrev.model.Hook
import io.github.nomisrev.model.HookDeliveryItem
import io.github.nomisrev.model.HookDelivery
import io.github.nomisrev.model.CheckImmutableReleases
import io.github.nomisrev.model.Import
import io.github.nomisrev.model.PorterAuthor
import io.github.nomisrev.model.PorterLargeFile
import io.github.nomisrev.model.Installation
import io.github.nomisrev.model.InteractionLimitResponse
import io.github.nomisrev.model.InteractionLimit
import io.github.nomisrev.model.Issue
import io.github.nomisrev.model.IssueComment
import io.github.nomisrev.model.IssueEvent
import io.github.nomisrev.model.IssueEventForIssue
import io.github.nomisrev.model.IssueFieldValue
import io.github.nomisrev.model.Label
import io.github.nomisrev.model.TimelineIssueEvents
import io.github.nomisrev.model.DeployKey
import io.github.nomisrev.model.Language
import io.github.nomisrev.model.LicenseContent
import io.github.nomisrev.model.MergedUpstream
import io.github.nomisrev.model.Milestone
import io.github.nomisrev.model.Thread
import io.github.nomisrev.model.Page
import io.github.nomisrev.model.PageBuild
import io.github.nomisrev.model.PageBuildStatus
import io.github.nomisrev.model.PageDeployment
import io.github.nomisrev.model.PagesDeploymentStatus
import io.github.nomisrev.model.PagesHealthCheck
import io.github.nomisrev.model.CustomPropertyValue
import io.github.nomisrev.model.PullRequest
import io.github.nomisrev.model.PullRequestReviewComment
import io.github.nomisrev.model.DiffEntry
import io.github.nomisrev.model.PullRequestMergeResult
import io.github.nomisrev.model.PullRequestReviewRequest
import io.github.nomisrev.model.PullRequestReview
import io.github.nomisrev.model.ReviewComment
import io.github.nomisrev.model.Release
import io.github.nomisrev.model.ReleaseAsset
import io.github.nomisrev.model.ReleaseNotesContent
import io.github.nomisrev.model.RepositoryRuleDetailed
import io.github.nomisrev.model.RepositoryRuleEnforcement
import io.github.nomisrev.model.RepositoryRulesetBypassActor
import io.github.nomisrev.model.RepositoryRulesetConditions
import io.github.nomisrev.model.RepositoryRule
import io.github.nomisrev.model.RepositoryRuleset
import io.github.nomisrev.model.RuleSuites
import io.github.nomisrev.model.RuleSuite
import io.github.nomisrev.model.RulesetVersion
import io.github.nomisrev.model.RulesetVersionWithState
import io.github.nomisrev.model.SecretScanningAlertState
import io.github.nomisrev.model.SecretScanningAlertResolution
import io.github.nomisrev.model.SecretScanningAlertResolutionComment
import io.github.nomisrev.model.SecretScanningAlertAssignee
import io.github.nomisrev.model.SecretScanningAlertResponse
import io.github.nomisrev.model.SecretScanningLocation
import io.github.nomisrev.model.SecretScanningPushProtectionBypassReason
import io.github.nomisrev.model.SecretScanningPushProtectionBypassPlaceholderId
import io.github.nomisrev.model.SecretScanningPushProtectionBypass
import io.github.nomisrev.model.SecretScanningScanHistory
import io.github.nomisrev.model.RepositoryAdvisoryResponse
import io.github.nomisrev.model.RepositoryAdvisoryCreate
import io.github.nomisrev.model.RepositoryAdvisoryUpdate
import io.github.nomisrev.model.PrivateVulnerabilityReportCreate
import io.github.nomisrev.model.Stargazer
import io.github.nomisrev.model.CodeFrequencyStat
import io.github.nomisrev.model.CommitActivity
import io.github.nomisrev.model.ContributorActivity
import io.github.nomisrev.model.ParticipationStats
import io.github.nomisrev.model.RepositorySubscription
import io.github.nomisrev.model.Tag
import io.github.nomisrev.model.Topic
import io.github.nomisrev.model.CloneTraffic
import io.github.nomisrev.model.ContentTraffic
import io.github.nomisrev.model.ReferrerTraffic
import io.github.nomisrev.model.ViewTraffic
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

interface Repos {
    val actions: Repos.Actions

    val activity: Repos.Activity

    val assignees: Repos.Assignees

    val attestations: Repos.Attestations

    val autolinks: Repos.Autolinks

    val automatedSecurityFixes: Repos.AutomatedSecurityFixes

    val branches: Repos.Branches

    val checkRuns: Repos.CheckRuns

    val checkSuites: Repos.CheckSuites

    val codeScanning: Repos.CodeScanning

    val codeSecurityConfiguration: Repos.CodeSecurityConfiguration

    val codeowners: Repos.Codeowners

    val codespaces: Repos.Codespaces

    val collaborators: Repos.Collaborators

    val comments: Repos.Comments

    val commits: Repos.Commits

    val community: Repos.Community

    val compare: Repos.Compare

    val contents: Repos.Contents

    val contributors: Repos.Contributors

    val dependabot: Repos.Dependabot

    val dependencyGraph: Repos.DependencyGraph

    val deployments: Repos.Deployments

    val dispatches: Repos.Dispatches

    val environments: Repos.Environments

    val events: Repos.Events

    val forks: Repos.Forks

    val git: Repos.Git

    val hooks: Repos.Hooks

    val immutableReleases: Repos.ImmutableReleases

    val import: Repos.Import

    val installation: Repos.Installation

    val interactionLimits: Repos.InteractionLimits

    val invitations: Repos.Invitations

    val issues: Repos.Issues

    val keys: Repos.Keys

    val labels: Repos.Labels

    val languages: Repos.Languages

    val license: Repos.License

    val mergeUpstream: Repos.MergeUpstream

    val merges: Repos.Merges

    val milestones: Repos.Milestones

    val notifications: Repos.Notifications

    val pages: Repos.Pages

    val privateVulnerabilityReporting: Repos.PrivateVulnerabilityReporting

    val properties: Repos.Properties

    val pulls: Repos.Pulls

    val readme: Repos.Readme

    val releases: Repos.Releases

    val rules: Repos.Rules

    val rulesets: Repos.Rulesets

    val secretScanning: Repos.SecretScanning

    val securityAdvisories: Repos.SecurityAdvisories

    val stargazers: Repos.Stargazers

    val stats: Repos.Stats

    val statuses: Repos.Statuses

    val subscribers: Repos.Subscribers

    val subscription: Repos.Subscription

    val tags: Repos.Tags

    val tarball: Repos.Tarball

    val teams: Repos.Teams

    val topics: Repos.Topics

    val traffic: Repos.Traffic

    val transfer: Repos.Transfer

    val vulnerabilityAlerts: Repos.VulnerabilityAlerts

    val zipball: Repos.Zipball

    val generate: Repos.Generate

    @Serializable
    data class ReposDeleteResponse(
        val message: String? = null,
        @SerialName("documentation_url") val documentationUrl: String? = null,
    )


    @Serializable
    data class ReposUpdateBody(
        val name: String? = null,
        val description: String? = null,
        val homepage: String? = null,
        val private: Boolean? = null,
        val visibility: Visibility? = null,
        @SerialName("security_and_analysis") val securityAndAnalysis: SecurityAndAnalysis? = null,
        @SerialName("has_issues") val hasIssues: Boolean? = null,
        @SerialName("has_projects") val hasProjects: Boolean? = null,
        @SerialName("has_wiki") val hasWiki: Boolean? = null,
        @SerialName("is_template") val isTemplate: Boolean? = null,
        @SerialName("default_branch") val defaultBranch: String? = null,
        @SerialName("allow_squash_merge") val allowSquashMerge: Boolean? = null,
        @SerialName("allow_merge_commit") val allowMergeCommit: Boolean? = null,
        @SerialName("allow_rebase_merge") val allowRebaseMerge: Boolean? = null,
        @SerialName("allow_auto_merge") val allowAutoMerge: Boolean? = null,
        @SerialName("delete_branch_on_merge") val deleteBranchOnMerge: Boolean? = null,
        @SerialName("allow_update_branch") val allowUpdateBranch: Boolean? = null,
        @SerialName("use_squash_pr_title_as_default") val useSquashPrTitleAsDefault: Boolean? = null,
        @SerialName("squash_merge_commit_title") val squashMergeCommitTitle: SquashMergeCommitTitle? = null,
        @SerialName("squash_merge_commit_message") val squashMergeCommitMessage: SquashMergeCommitMessage? = null,
        @SerialName("merge_commit_title") val mergeCommitTitle: MergeCommitTitle? = null,
        @SerialName("merge_commit_message") val mergeCommitMessage: MergeCommitMessage? = null,
        val archived: Boolean? = null,
        @SerialName("allow_forking") val allowForking: Boolean? = null,
        @SerialName("web_commit_signoff_required") val webCommitSignoffRequired: Boolean? = null,
    ) {
        @Serializable
        enum class Visibility {
            @SerialName("public") Public, @SerialName("private") Private;
        }

        @Serializable
        data class SecurityAndAnalysis(
            @SerialName("advanced_security") val advancedSecurity: AdvancedSecurity? = null,
            @SerialName("code_security") val codeSecurity: CodeSecurity? = null,
            @SerialName("secret_scanning") val secretScanning: SecretScanning? = null,
            @SerialName("secret_scanning_push_protection") val secretScanningPushProtection: SecretScanningPushProtection? = null,
            @SerialName("secret_scanning_ai_detection") val secretScanningAiDetection: SecretScanningAiDetection? = null,
            @SerialName("secret_scanning_non_provider_patterns") val secretScanningNonProviderPatterns: SecretScanningNonProviderPatterns? = null,
            @SerialName("secret_scanning_delegated_alert_dismissal") val secretScanningDelegatedAlertDismissal: SecretScanningDelegatedAlertDismissal? = null,
            @SerialName("secret_scanning_delegated_bypass") val secretScanningDelegatedBypass: SecretScanningDelegatedBypass? = null,
            @SerialName("secret_scanning_delegated_bypass_options") val secretScanningDelegatedBypassOptions: SecretScanningDelegatedBypassOptions? = null,
        ) {
            @Serializable
            @JvmInline
            value class AdvancedSecurity(val status: String? = null)

            @Serializable
            @JvmInline
            value class CodeSecurity(val status: String? = null)

            @Serializable
            @JvmInline
            value class SecretScanning(val status: String? = null)

            @Serializable
            @JvmInline
            value class SecretScanningPushProtection(val status: String? = null)

            @Serializable
            @JvmInline
            value class SecretScanningAiDetection(val status: String? = null)

            @Serializable
            @JvmInline
            value class SecretScanningNonProviderPatterns(val status: String? = null)

            @Serializable
            @JvmInline
            value class SecretScanningDelegatedAlertDismissal(val status: String? = null)

            @Serializable
            @JvmInline
            value class SecretScanningDelegatedBypass(val status: String? = null)

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

    sealed interface ReposGetResult {
        data class OK(val value: FullRepository) : ReposGetResult

        data class MovedPermanently(val value: BasicError) : ReposGetResult

        data class Forbidden(val value: BasicError) : ReposGetResult

        data class NotFound(val value: BasicError) : ReposGetResult
    }

    suspend fun reposGet(
        owner: String,
        repo: String,
    ): ReposGetResult

    sealed interface ReposDeleteResult {
        data object NoContent : ReposDeleteResult

        data class TemporaryRedirect(val value: BasicError) : ReposDeleteResult

        data class Forbidden(val value: ReposDeleteResponse) : ReposDeleteResult

        data class NotFound(val value: BasicError) : ReposDeleteResult

        data class Conflict(val value: BasicError) : ReposDeleteResult
    }

    suspend fun reposDelete(
        owner: String,
        repo: String,
    ): ReposDeleteResult

    sealed interface ReposUpdateResult {
        data class OK(val value: FullRepository) : ReposUpdateResult

        data class TemporaryRedirect(val value: BasicError) : ReposUpdateResult

        data class Forbidden(val value: BasicError) : ReposUpdateResult

        data class NotFound(val value: BasicError) : ReposUpdateResult

        data class UnprocessableEntity(val value: ValidationError) : ReposUpdateResult
    }

    suspend fun reposUpdate(
        owner: String,
        repo: String,
        body: ReposUpdateBody? = null,
    ): ReposUpdateResult

    interface Actions {
        val artifacts: Repos.Actions.Artifacts

        val cache: Repos.Actions.Cache

        val caches: Repos.Actions.Caches

        val jobs: Repos.Actions.Jobs

        val oidc: Repos.Actions.Oidc

        val organizationSecrets: Repos.Actions.OrganizationSecrets

        val organizationVariables: Repos.Actions.OrganizationVariables

        val permissions: Repos.Actions.Permissions

        val runners: Repos.Actions.Runners

        val runs: Repos.Actions.Runs

        val secrets: Repos.Actions.Secrets

        val variables: Repos.Actions.Variables

        val workflows: Repos.Actions.Workflows

        interface Artifacts {
            @Serializable
            data class ActionsListArtifactsForRepoResponse(
                @SerialName("total_count") val totalCount: Long,
                val artifacts: List<Artifact>,
            )

            suspend fun actionsListArtifactsForRepo(
                owner: String,
                repo: String,
                page: Long = 1L,
                perPage: Long = 30L,
                name: String? = null,
            ): ActionsListArtifactsForRepoResponse

            suspend fun actionsGetArtifact(
                owner: String,
                repo: String,
                artifactId: Long,
            ): Artifact

            suspend fun actionsDeleteArtifact(
                owner: String,
                repo: String,
                artifactId: Long,
            ): Unit

            sealed interface ActionsDownloadArtifactResult {
                data object Found : ActionsDownloadArtifactResult

                data class Gone(val value: BasicError) : ActionsDownloadArtifactResult
            }

            suspend fun actionsDownloadArtifact(
                owner: String,
                repo: String,
                artifactId: Long,
                archiveFormat: String,
            ): ActionsDownloadArtifactResult
        }

        interface Cache {
            val retentionLimit: Repos.Actions.Cache.RetentionLimit

            val storageLimit: Repos.Actions.Cache.StorageLimit

            val usage: Repos.Actions.Cache.Usage

            interface RetentionLimit {
                sealed interface ActionsGetActionsCacheRetentionLimitForRepositoryResult {
                    data class OK(val value: ActionsCacheRetentionLimitForRepository) : ActionsGetActionsCacheRetentionLimitForRepositoryResult

                    data class Forbidden(val value: BasicError) : ActionsGetActionsCacheRetentionLimitForRepositoryResult

                    data class NotFound(val value: BasicError) : ActionsGetActionsCacheRetentionLimitForRepositoryResult
                }

                suspend fun actionsGetActionsCacheRetentionLimitForRepository(
                    owner: String,
                    repo: String,
                ): ActionsGetActionsCacheRetentionLimitForRepositoryResult

                sealed interface ActionsSetActionsCacheRetentionLimitForRepositoryResult {
                    data object NoContent : ActionsSetActionsCacheRetentionLimitForRepositoryResult

                    data class BadRequest(val value: BasicError) : ActionsSetActionsCacheRetentionLimitForRepositoryResult

                    data class Forbidden(val value: BasicError) : ActionsSetActionsCacheRetentionLimitForRepositoryResult

                    data class NotFound(val value: BasicError) : ActionsSetActionsCacheRetentionLimitForRepositoryResult
                }

                suspend fun actionsSetActionsCacheRetentionLimitForRepository(
                    owner: String,
                    repo: String,
                    body: ActionsCacheRetentionLimitForRepository,
                ): ActionsSetActionsCacheRetentionLimitForRepositoryResult
            }

            interface StorageLimit {
                sealed interface ActionsGetActionsCacheStorageLimitForRepositoryResult {
                    data class OK(val value: ActionsCacheStorageLimitForRepository) : ActionsGetActionsCacheStorageLimitForRepositoryResult

                    data class Forbidden(val value: BasicError) : ActionsGetActionsCacheStorageLimitForRepositoryResult

                    data class NotFound(val value: BasicError) : ActionsGetActionsCacheStorageLimitForRepositoryResult
                }

                suspend fun actionsGetActionsCacheStorageLimitForRepository(
                    owner: String,
                    repo: String,
                ): ActionsGetActionsCacheStorageLimitForRepositoryResult

                sealed interface ActionsSetActionsCacheStorageLimitForRepositoryResult {
                    data object NoContent : ActionsSetActionsCacheStorageLimitForRepositoryResult

                    data class BadRequest(val value: BasicError) : ActionsSetActionsCacheStorageLimitForRepositoryResult

                    data class Forbidden(val value: BasicError) : ActionsSetActionsCacheStorageLimitForRepositoryResult

                    data class NotFound(val value: BasicError) : ActionsSetActionsCacheStorageLimitForRepositoryResult
                }

                suspend fun actionsSetActionsCacheStorageLimitForRepository(
                    owner: String,
                    repo: String,
                    body: ActionsCacheStorageLimitForRepository,
                ): ActionsSetActionsCacheStorageLimitForRepositoryResult
            }

            interface Usage {
                suspend fun actionsGetActionsCacheUsage(
                    owner: String,
                    repo: String,
                ): ActionsCacheUsageByRepository
            }
        }

        interface Caches {
            @Serializable
            enum class Direction {
                @SerialName("asc") Asc, @SerialName("desc") Desc;
            }


            @Serializable
            enum class Sort {
                @SerialName("created_at")
                CreatedAt,
                @SerialName("last_accessed_at")
                LastAccessedAt,
                @SerialName("size_in_bytes")
                SizeInBytes;
            }

            suspend fun actionsGetActionsCacheList(
                owner: String,
                repo: String,
                direction: Direction = Direction.Desc,
                page: Long = 1L,
                perPage: Long = 30L,
                sort: Sort = Sort.LastAccessedAt,
                key: String? = null,
                ref: String? = null,
            ): ActionsCacheList

            suspend fun actionsDeleteActionsCacheByKey(
                owner: String,
                repo: String,
                key: String,
                ref: String? = null,
            ): ActionsCacheList

            suspend fun actionsDeleteActionsCacheById(
                owner: String,
                repo: String,
                cacheId: Long,
            ): Unit
        }

        interface Jobs {
            val logs: Repos.Actions.Jobs.Logs

            val rerun: Repos.Actions.Jobs.Rerun

            suspend fun actionsGetJobForWorkflowRun(
                owner: String,
                repo: String,
                jobId: Long,
            ): Job

            interface Logs {
                suspend fun actionsDownloadJobLogsForWorkflowRun(
                    owner: String,
                    repo: String,
                    jobId: Long,
                ): Unit
            }

            interface Rerun {
                @Serializable
                @JvmInline
                value class ActionsReRunJobForWorkflowRunBody(@SerialName("enable_debug_logging") val enableDebugLogging: Boolean? = null)

                sealed interface ActionsReRunJobForWorkflowRunResult {
                    data class Created(val value: EmptyObject) : ActionsReRunJobForWorkflowRunResult

                    data class Forbidden(val value: BasicError) : ActionsReRunJobForWorkflowRunResult
                }

                suspend fun actionsReRunJobForWorkflowRun(
                    owner: String,
                    repo: String,
                    jobId: Long,
                    body: ActionsReRunJobForWorkflowRunBody? = null,
                ): ActionsReRunJobForWorkflowRunResult
            }
        }

        interface Oidc {
            val customization: Repos.Actions.Oidc.Customization

            interface Customization {
                val sub: Repos.Actions.Oidc.Customization.Sub

                interface Sub {
                    @Serializable
                    data class ActionsSetCustomOidcSubClaimForRepoBody(
                        @SerialName("use_default") val useDefault: Boolean,
                        @SerialName("include_claim_keys") val includeClaimKeys: List<String>? = null,
                    )

                    sealed interface ActionsGetCustomOidcSubClaimForRepoResult {
                        data class OK(val value: OidcCustomSubRepo) : ActionsGetCustomOidcSubClaimForRepoResult

                        data class BadRequest(val value: BasicError) : ActionsGetCustomOidcSubClaimForRepoResult

                        data class NotFound(val value: BasicError) : ActionsGetCustomOidcSubClaimForRepoResult
                    }

                    suspend fun actionsGetCustomOidcSubClaimForRepo(
                        owner: String,
                        repo: String,
                    ): ActionsGetCustomOidcSubClaimForRepoResult

                    sealed interface ActionsSetCustomOidcSubClaimForRepoResult {
                        data class Created(val value: EmptyObject) : ActionsSetCustomOidcSubClaimForRepoResult

                        data class BadRequest(val value: BasicError) : ActionsSetCustomOidcSubClaimForRepoResult

                        data class NotFound(val value: BasicError) : ActionsSetCustomOidcSubClaimForRepoResult

                        data class UnprocessableEntity(val value: ValidationErrorSimple) : ActionsSetCustomOidcSubClaimForRepoResult
                    }

                    suspend fun actionsSetCustomOidcSubClaimForRepo(
                        owner: String,
                        repo: String,
                        body: ActionsSetCustomOidcSubClaimForRepoBody,
                    ): ActionsSetCustomOidcSubClaimForRepoResult
                }
            }
        }

        interface OrganizationSecrets {
            @Serializable
            data class ActionsListRepoOrganizationSecretsResponse(
                @SerialName("total_count") val totalCount: Long,
                val secrets: List<ActionsSecret>,
            )

            suspend fun actionsListRepoOrganizationSecrets(
                owner: String,
                repo: String,
                page: Long = 1L,
                perPage: Long = 30L,
            ): ActionsListRepoOrganizationSecretsResponse
        }

        interface OrganizationVariables {
            @Serializable
            data class ActionsListRepoOrganizationVariablesResponse(
                @SerialName("total_count") val totalCount: Long,
                val variables: List<ActionsVariable>,
            )

            suspend fun actionsListRepoOrganizationVariables(
                owner: String,
                repo: String,
                page: Long = 1L,
                perPage: Long = 10L,
            ): ActionsListRepoOrganizationVariablesResponse
        }

        interface Permissions {
            val access: Repos.Actions.Permissions.Access

            val artifactAndLogRetention: Repos.Actions.Permissions.ArtifactAndLogRetention

            val forkPrContributorApproval: Repos.Actions.Permissions.ForkPrContributorApproval

            val forkPrWorkflowsPrivateRepos: Repos.Actions.Permissions.ForkPrWorkflowsPrivateRepos

            val selectedActions: Repos.Actions.Permissions.SelectedActions

            val workflow: Repos.Actions.Permissions.Workflow

            @Serializable
            data class ActionsSetGithubActionsPermissionsRepositoryBody(
                val enabled: ActionsEnabled,
                @SerialName("allowed_actions") val allowedActions: AllowedActions? = null,
                @SerialName("sha_pinning_required") val shaPinningRequired: ShaPinningRequired? = null,
            )

            suspend fun actionsGetGithubActionsPermissionsRepository(
                owner: String,
                repo: String,
            ): ActionsRepositoryPermissions

            suspend fun actionsSetGithubActionsPermissionsRepository(
                owner: String,
                repo: String,
                body: ActionsSetGithubActionsPermissionsRepositoryBody,
            ): Unit

            interface Access {
                suspend fun actionsGetWorkflowAccessToRepository(
                    owner: String,
                    repo: String,
                ): ActionsWorkflowAccessToRepository

                suspend fun actionsSetWorkflowAccessToRepository(
                    owner: String,
                    repo: String,
                    body: ActionsWorkflowAccessToRepository,
                ): Unit
            }

            interface ArtifactAndLogRetention {
                sealed interface ActionsGetArtifactAndLogRetentionSettingsRepositoryResult {
                    data class OK(val value: ActionsArtifactAndLogRetentionResponse) : ActionsGetArtifactAndLogRetentionSettingsRepositoryResult

                    data class NotFound(val value: BasicError) : ActionsGetArtifactAndLogRetentionSettingsRepositoryResult
                }

                suspend fun actionsGetArtifactAndLogRetentionSettingsRepository(
                    owner: String,
                    repo: String,
                ): ActionsGetArtifactAndLogRetentionSettingsRepositoryResult

                sealed interface ActionsSetArtifactAndLogRetentionSettingsRepositoryResult {
                    data object NoContent : ActionsSetArtifactAndLogRetentionSettingsRepositoryResult

                    data class NotFound(val value: BasicError) : ActionsSetArtifactAndLogRetentionSettingsRepositoryResult

                    data class UnprocessableEntity(val value: ValidationError) : ActionsSetArtifactAndLogRetentionSettingsRepositoryResult
                }

                suspend fun actionsSetArtifactAndLogRetentionSettingsRepository(
                    owner: String,
                    repo: String,
                    body: ActionsArtifactAndLogRetention,
                ): ActionsSetArtifactAndLogRetentionSettingsRepositoryResult
            }

            interface ForkPrContributorApproval {
                sealed interface ActionsGetForkPrContributorApprovalPermissionsRepositoryResult {
                    data class OK(val value: ActionsForkPrContributorApproval) : ActionsGetForkPrContributorApprovalPermissionsRepositoryResult

                    data class NotFound(val value: BasicError) : ActionsGetForkPrContributorApprovalPermissionsRepositoryResult
                }

                suspend fun actionsGetForkPrContributorApprovalPermissionsRepository(
                    owner: String,
                    repo: String,
                ): ActionsGetForkPrContributorApprovalPermissionsRepositoryResult

                sealed interface ActionsSetForkPrContributorApprovalPermissionsRepositoryResult {
                    data object NoContent : ActionsSetForkPrContributorApprovalPermissionsRepositoryResult

                    data class NotFound(val value: BasicError) : ActionsSetForkPrContributorApprovalPermissionsRepositoryResult

                    data class UnprocessableEntity(val value: ValidationError) : ActionsSetForkPrContributorApprovalPermissionsRepositoryResult
                }

                suspend fun actionsSetForkPrContributorApprovalPermissionsRepository(
                    owner: String,
                    repo: String,
                    body: ActionsForkPrContributorApproval,
                ): ActionsSetForkPrContributorApprovalPermissionsRepositoryResult
            }

            interface ForkPrWorkflowsPrivateRepos {
                sealed interface ActionsGetPrivateRepoForkPrWorkflowsSettingsRepositoryResult {
                    data class OK(val value: ActionsForkPrWorkflowsPrivateRepos) : ActionsGetPrivateRepoForkPrWorkflowsSettingsRepositoryResult

                    data class Forbidden(val value: BasicError) : ActionsGetPrivateRepoForkPrWorkflowsSettingsRepositoryResult

                    data class NotFound(val value: BasicError) : ActionsGetPrivateRepoForkPrWorkflowsSettingsRepositoryResult
                }

                suspend fun actionsGetPrivateRepoForkPrWorkflowsSettingsRepository(
                    owner: String,
                    repo: String,
                ): ActionsGetPrivateRepoForkPrWorkflowsSettingsRepositoryResult

                sealed interface ActionsSetPrivateRepoForkPrWorkflowsSettingsRepositoryResult {
                    data object NoContent : ActionsSetPrivateRepoForkPrWorkflowsSettingsRepositoryResult

                    data class NotFound(val value: BasicError) : ActionsSetPrivateRepoForkPrWorkflowsSettingsRepositoryResult

                    data class UnprocessableEntity(val value: ValidationError) : ActionsSetPrivateRepoForkPrWorkflowsSettingsRepositoryResult
                }

                suspend fun actionsSetPrivateRepoForkPrWorkflowsSettingsRepository(
                    owner: String,
                    repo: String,
                    body: ActionsForkPrWorkflowsPrivateReposRequest,
                ): ActionsSetPrivateRepoForkPrWorkflowsSettingsRepositoryResult
            }

            interface SelectedActions {
                suspend fun actionsGetAllowedActionsRepository(
                    owner: String,
                    repo: String,
                ): SelectedActions

                suspend fun actionsSetAllowedActionsRepository(
                    owner: String,
                    repo: String,
                    body: SelectedActions? = null,
                ): Unit
            }

            interface Workflow {
                suspend fun actionsGetGithubActionsDefaultWorkflowPermissionsRepository(
                    owner: String,
                    repo: String,
                ): ActionsGetDefaultWorkflowPermissions

                sealed interface ActionsSetGithubActionsDefaultWorkflowPermissionsRepositoryResult {
                    data object NoContent : ActionsSetGithubActionsDefaultWorkflowPermissionsRepositoryResult

                    data object Conflict : ActionsSetGithubActionsDefaultWorkflowPermissionsRepositoryResult
                }

                suspend fun actionsSetGithubActionsDefaultWorkflowPermissionsRepository(
                    owner: String,
                    repo: String,
                    body: ActionsSetDefaultWorkflowPermissions,
                ): ActionsSetGithubActionsDefaultWorkflowPermissionsRepositoryResult
            }
        }

        interface Runners {
            val downloads: Repos.Actions.Runners.Downloads

            val generateJitconfig: Repos.Actions.Runners.GenerateJitconfig

            val registrationToken: Repos.Actions.Runners.RegistrationToken

            val removeToken: Repos.Actions.Runners.RemoveToken

            val labels: Repos.Actions.Runners.LabelsApi

            @Serializable
            data class ActionsListSelfHostedRunnersForRepoResponse(
                @SerialName("total_count") val totalCount: Long,
                val runners: List<Runner>,
            )

            suspend fun actionsListSelfHostedRunnersForRepo(
                owner: String,
                repo: String,
                page: Long = 1L,
                perPage: Long = 30L,
                name: String? = null,
            ): ActionsListSelfHostedRunnersForRepoResponse

            suspend fun actionsGetSelfHostedRunnerForRepo(
                owner: String,
                repo: String,
                runnerId: Long,
            ): Runner

            sealed interface ActionsDeleteSelfHostedRunnerFromRepoResult {
                data object NoContent : ActionsDeleteSelfHostedRunnerFromRepoResult

                data class UnprocessableEntity(val value: ValidationErrorSimple) : ActionsDeleteSelfHostedRunnerFromRepoResult
            }

            suspend fun actionsDeleteSelfHostedRunnerFromRepo(
                owner: String,
                repo: String,
                runnerId: Long,
            ): ActionsDeleteSelfHostedRunnerFromRepoResult

            interface Downloads {
                suspend fun actionsListRunnerApplicationsForRepo(
                    owner: String,
                    repo: String,
                ): List<RunnerApplication>
            }

            interface GenerateJitconfig {
                @Serializable
                data class ActionsGenerateRunnerJitconfigForRepoBody(
                    val name: String,
                    @SerialName("runner_group_id") val runnerGroupId: Long,
                    val labels: List<String>,
                    @SerialName("work_folder") val workFolder: String? = null,
                )


                @Serializable
                data class ActionsGenerateRunnerJitconfigForRepoResponse(
                    val runner: Runner,
                    @SerialName("encoded_jit_config") val encodedJitConfig: String,
                )

                sealed interface ActionsGenerateRunnerJitconfigForRepoResult {
                    data class Created(val value: ActionsGenerateRunnerJitconfigForRepoResponse) : ActionsGenerateRunnerJitconfigForRepoResult

                    data class NotFound(val value: BasicError) : ActionsGenerateRunnerJitconfigForRepoResult

                    data class Conflict(val value: BasicError) : ActionsGenerateRunnerJitconfigForRepoResult

                    data class UnprocessableEntity(val value: ValidationErrorSimple) : ActionsGenerateRunnerJitconfigForRepoResult
                }

                suspend fun actionsGenerateRunnerJitconfigForRepo(
                    owner: String,
                    repo: String,
                    body: ActionsGenerateRunnerJitconfigForRepoBody,
                ): ActionsGenerateRunnerJitconfigForRepoResult
            }

            interface RegistrationToken {
                suspend fun actionsCreateRegistrationTokenForRepo(
                    owner: String,
                    repo: String,
                ): AuthenticationToken
            }

            interface RemoveToken {
                suspend fun actionsCreateRemoveTokenForRepo(
                    owner: String,
                    repo: String,
                ): AuthenticationToken
            }

            interface LabelsApi {
                @Serializable
                @JvmInline
                value class ActionsAddCustomLabelsToSelfHostedRunnerForRepoBody(val labels: List<String>)


                @Serializable
                data class ActionsAddCustomLabelsToSelfHostedRunnerForRepoResponse(
                    @SerialName("total_count") val totalCount: Long,
                    val labels: List<RunnerLabel>,
                )


                @Serializable
                data class ActionsListLabelsForSelfHostedRunnerForRepoResponse(
                    @SerialName("total_count") val totalCount: Long,
                    val labels: List<RunnerLabel>,
                )


                @Serializable
                data class ActionsRemoveAllCustomLabelsFromSelfHostedRunnerForRepoResponse(
                    @SerialName("total_count") val totalCount: Long,
                    val labels: List<RunnerLabel>,
                )


                @Serializable
                data class ActionsRemoveCustomLabelFromSelfHostedRunnerForRepoResponse(
                    @SerialName("total_count") val totalCount: Long,
                    val labels: List<RunnerLabel>,
                )


                @Serializable
                @JvmInline
                value class ActionsSetCustomLabelsForSelfHostedRunnerForRepoBody(val labels: List<String>)


                @Serializable
                data class ActionsSetCustomLabelsForSelfHostedRunnerForRepoResponse(
                    @SerialName("total_count") val totalCount: Long,
                    val labels: List<RunnerLabel>,
                )

                sealed interface ActionsListLabelsForSelfHostedRunnerForRepoResult {
                    data class OK(val value: ActionsListLabelsForSelfHostedRunnerForRepoResponse) : ActionsListLabelsForSelfHostedRunnerForRepoResult

                    data class NotFound(val value: BasicError) : ActionsListLabelsForSelfHostedRunnerForRepoResult
                }

                suspend fun actionsListLabelsForSelfHostedRunnerForRepo(
                    owner: String,
                    repo: String,
                    runnerId: Long,
                ): ActionsListLabelsForSelfHostedRunnerForRepoResult

                sealed interface ActionsSetCustomLabelsForSelfHostedRunnerForRepoResult {
                    data class OK(val value: ActionsSetCustomLabelsForSelfHostedRunnerForRepoResponse) : ActionsSetCustomLabelsForSelfHostedRunnerForRepoResult

                    data class NotFound(val value: BasicError) : ActionsSetCustomLabelsForSelfHostedRunnerForRepoResult

                    data class UnprocessableEntity(val value: ValidationErrorSimple) : ActionsSetCustomLabelsForSelfHostedRunnerForRepoResult
                }

                suspend fun actionsSetCustomLabelsForSelfHostedRunnerForRepo(
                    owner: String,
                    repo: String,
                    runnerId: Long,
                    body: ActionsSetCustomLabelsForSelfHostedRunnerForRepoBody,
                ): ActionsSetCustomLabelsForSelfHostedRunnerForRepoResult

                sealed interface ActionsAddCustomLabelsToSelfHostedRunnerForRepoResult {
                    data class OK(val value: ActionsAddCustomLabelsToSelfHostedRunnerForRepoResponse) : ActionsAddCustomLabelsToSelfHostedRunnerForRepoResult

                    data class NotFound(val value: BasicError) : ActionsAddCustomLabelsToSelfHostedRunnerForRepoResult

                    data class UnprocessableEntity(val value: ValidationErrorSimple) : ActionsAddCustomLabelsToSelfHostedRunnerForRepoResult
                }

                suspend fun actionsAddCustomLabelsToSelfHostedRunnerForRepo(
                    owner: String,
                    repo: String,
                    runnerId: Long,
                    body: ActionsAddCustomLabelsToSelfHostedRunnerForRepoBody,
                ): ActionsAddCustomLabelsToSelfHostedRunnerForRepoResult

                sealed interface ActionsRemoveAllCustomLabelsFromSelfHostedRunnerForRepoResult {
                    data class OK(val value: ActionsRemoveAllCustomLabelsFromSelfHostedRunnerForRepoResponse) : ActionsRemoveAllCustomLabelsFromSelfHostedRunnerForRepoResult

                    data class NotFound(val value: BasicError) : ActionsRemoveAllCustomLabelsFromSelfHostedRunnerForRepoResult
                }

                suspend fun actionsRemoveAllCustomLabelsFromSelfHostedRunnerForRepo(
                    owner: String,
                    repo: String,
                    runnerId: Long,
                ): ActionsRemoveAllCustomLabelsFromSelfHostedRunnerForRepoResult

                sealed interface ActionsRemoveCustomLabelFromSelfHostedRunnerForRepoResult {
                    data class OK(val value: ActionsRemoveCustomLabelFromSelfHostedRunnerForRepoResponse) : ActionsRemoveCustomLabelFromSelfHostedRunnerForRepoResult

                    data class NotFound(val value: BasicError) : ActionsRemoveCustomLabelFromSelfHostedRunnerForRepoResult

                    data class UnprocessableEntity(val value: ValidationErrorSimple) : ActionsRemoveCustomLabelFromSelfHostedRunnerForRepoResult
                }

                suspend fun actionsRemoveCustomLabelFromSelfHostedRunnerForRepo(
                    owner: String,
                    repo: String,
                    runnerId: Long,
                    name: String,
                ): ActionsRemoveCustomLabelFromSelfHostedRunnerForRepoResult
            }
        }

        interface Runs {
            val approvals: Repos.Actions.Runs.Approvals

            val approve: Repos.Actions.Runs.Approve

            val artifacts: Repos.Actions.Runs.ArtifactsApi

            val attempts: Repos.Actions.Runs.Attempts

            val cancel: Repos.Actions.Runs.Cancel

            val deploymentProtectionRule: Repos.Actions.Runs.DeploymentProtectionRule

            val forceCancel: Repos.Actions.Runs.ForceCancel

            val jobs: Repos.Actions.Runs.JobsApi

            val logs: Repos.Actions.Runs.Logs

            val pendingDeployments: Repos.Actions.Runs.PendingDeployments

            val rerun: Repos.Actions.Runs.Rerun

            val rerunFailedJobs: Repos.Actions.Runs.RerunFailedJobs

            val timing: Repos.Actions.Runs.Timing

            @Serializable
            data class ActionsListWorkflowRunsForRepoResponse(
                @SerialName("total_count") val totalCount: Long,
                @SerialName("workflow_runs") val workflowRuns: List<WorkflowRun>,
            )


            @Serializable
            enum class Status {
                @SerialName("completed")
                Completed,
                @SerialName("action_required")
                ActionRequired,
                @SerialName("cancelled")
                Cancelled,
                @SerialName("failure")
                Failure,
                @SerialName("neutral")
                Neutral,
                @SerialName("skipped")
                Skipped,
                @SerialName("stale")
                Stale,
                @SerialName("success")
                Success,
                @SerialName("timed_out")
                TimedOut,
                @SerialName("in_progress")
                InProgress,
                @SerialName("queued")
                Queued,
                @SerialName("requested")
                Requested,
                @SerialName("waiting")
                Waiting,
                @SerialName("pending")
                Pending;
            }

            suspend fun actionsListWorkflowRunsForRepo(
                owner: String,
                repo: String,
                excludePullRequests: Boolean = false,
                page: Long = 1L,
                perPage: Long = 30L,
                actor: String? = null,
                branch: String? = null,
                checkSuiteId: Long? = null,
                created: LocalDateTime? = null,
                event: String? = null,
                headSha: String? = null,
                status: Status? = null,
            ): ActionsListWorkflowRunsForRepoResponse

            suspend fun actionsGetWorkflowRun(
                owner: String,
                repo: String,
                runId: Long,
                excludePullRequests: Boolean = false,
            ): WorkflowRun

            suspend fun actionsDeleteWorkflowRun(
                owner: String,
                repo: String,
                runId: Long,
            ): Unit

            interface Approvals {
                suspend fun actionsGetReviewsForRun(
                    owner: String,
                    repo: String,
                    runId: Long,
                ): List<EnvironmentApprovals>
            }

            interface Approve {
                sealed interface ActionsApproveWorkflowRunResult {
                    data class Created(val value: EmptyObject) : ActionsApproveWorkflowRunResult

                    data class Forbidden(val value: BasicError) : ActionsApproveWorkflowRunResult

                    data class NotFound(val value: BasicError) : ActionsApproveWorkflowRunResult
                }

                suspend fun actionsApproveWorkflowRun(
                    owner: String,
                    repo: String,
                    runId: Long,
                ): ActionsApproveWorkflowRunResult
            }

            interface ArtifactsApi {
                @Serializable
                data class ActionsListWorkflowRunArtifactsResponse(
                    @SerialName("total_count") val totalCount: Long,
                    val artifacts: List<Artifact>,
                )


                @Serializable
                enum class Direction {
                    @SerialName("asc") Asc, @SerialName("desc") Desc;
                }

                suspend fun actionsListWorkflowRunArtifacts(
                    owner: String,
                    repo: String,
                    runId: Long,
                    direction: Direction = Direction.Desc,
                    page: Long = 1L,
                    perPage: Long = 30L,
                    name: String? = null,
                ): ActionsListWorkflowRunArtifactsResponse
            }

            interface Attempts {
                val jobs: Repos.Actions.Runs.Attempts.JobsApi2

                val logs: Repos.Actions.Runs.Attempts.LogsApi

                suspend fun actionsGetWorkflowRunAttempt(
                    owner: String,
                    repo: String,
                    runId: Long,
                    attemptNumber: Long,
                    excludePullRequests: Boolean = false,
                ): WorkflowRun

                interface JobsApi2 {
                    @Serializable
                    data class ActionsListJobsForWorkflowRunAttemptResponse(
                        @SerialName("total_count") val totalCount: Long,
                        val jobs: List<Job>,
                    )

                    sealed interface ActionsListJobsForWorkflowRunAttemptResult {
                        data class OK(val value: ActionsListJobsForWorkflowRunAttemptResponse) : ActionsListJobsForWorkflowRunAttemptResult

                        data class NotFound(val value: BasicError) : ActionsListJobsForWorkflowRunAttemptResult
                    }

                    suspend fun actionsListJobsForWorkflowRunAttempt(
                        owner: String,
                        repo: String,
                        runId: Long,
                        attemptNumber: Long,
                        page: Long = 1L,
                        perPage: Long = 30L,
                    ): ActionsListJobsForWorkflowRunAttemptResult
                }

                interface LogsApi {
                    suspend fun actionsDownloadWorkflowRunAttemptLogs(
                        owner: String,
                        repo: String,
                        runId: Long,
                        attemptNumber: Long,
                    ): Unit
                }
            }

            interface Cancel {
                sealed interface ActionsCancelWorkflowRunResult {
                    data class Accepted(val value: EmptyObject) : ActionsCancelWorkflowRunResult

                    data class Conflict(val value: BasicError) : ActionsCancelWorkflowRunResult
                }

                suspend fun actionsCancelWorkflowRun(
                    owner: String,
                    repo: String,
                    runId: Long,
                ): ActionsCancelWorkflowRunResult
            }

            interface DeploymentProtectionRule {
                @Serializable(with = ActionsReviewCustomGatesForRunBody.Serializer::class)
                sealed interface ActionsReviewCustomGatesForRunBody {
                    @Serializable
                    @JvmInline
                    value class CaseReviewCustomGatesCommentRequired(val value: ReviewCustomGatesCommentRequired) : ActionsReviewCustomGatesForRunBody

                    @Serializable
                    @JvmInline
                    value class CaseReviewCustomGatesStateRequired(val value: ReviewCustomGatesStateRequired) : ActionsReviewCustomGatesForRunBody

                    object Serializer : KSerializer<ActionsReviewCustomGatesForRunBody> {
                        @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
                        override val descriptor: SerialDescriptor =
                            buildSerialDescriptor("io.github.nomisrev.api.Repos.Actions.Runs.DeploymentProtectionRule.ActionsReviewCustomGatesForRunBody", PolymorphicKind.SEALED) {
                                element("CaseReviewCustomGatesCommentRequired", ReviewCustomGatesCommentRequired.serializer().descriptor)
                                element("CaseReviewCustomGatesStateRequired", ReviewCustomGatesStateRequired.serializer().descriptor)
                            }

                        override fun deserialize(decoder: Decoder): ActionsReviewCustomGatesForRunBody {
                            val value = decoder.decodeSerializableValue(JsonElement.serializer())
                            val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                            return json.attemptDeserialize(
                                value,
                                CaseReviewCustomGatesCommentRequired::class to { CaseReviewCustomGatesCommentRequired(decodeFromJsonElement(ReviewCustomGatesCommentRequired.serializer(), it)) },
                                CaseReviewCustomGatesStateRequired::class to { CaseReviewCustomGatesStateRequired(decodeFromJsonElement(ReviewCustomGatesStateRequired.serializer(), it)) },
                            )
                        }

                        override fun serialize(encoder: Encoder, value: ActionsReviewCustomGatesForRunBody) = when(value) {
                            is CaseReviewCustomGatesCommentRequired -> encoder.encodeSerializableValue(ReviewCustomGatesCommentRequired.serializer(), value.value)
                            is CaseReviewCustomGatesStateRequired -> encoder.encodeSerializableValue(ReviewCustomGatesStateRequired.serializer(), value.value)
                        }
                    }
                }

                suspend fun actionsReviewCustomGatesForRun(
                    owner: String,
                    repo: String,
                    runId: Long,
                    body: ActionsReviewCustomGatesForRunBody,
                ): Unit
            }

            interface ForceCancel {
                sealed interface ActionsForceCancelWorkflowRunResult {
                    data class Accepted(val value: EmptyObject) : ActionsForceCancelWorkflowRunResult

                    data class Conflict(val value: BasicError) : ActionsForceCancelWorkflowRunResult
                }

                suspend fun actionsForceCancelWorkflowRun(
                    owner: String,
                    repo: String,
                    runId: Long,
                ): ActionsForceCancelWorkflowRunResult
            }

            interface JobsApi {
                @Serializable
                data class ActionsListJobsForWorkflowRunResponse(@SerialName("total_count") val totalCount: Long, val jobs: List<Job>)


                @Serializable
                enum class Filter {
                    @SerialName("latest") Latest, @SerialName("all") All;
                }

                suspend fun actionsListJobsForWorkflowRun(
                    owner: String,
                    repo: String,
                    runId: Long,
                    filter: Filter = Filter.Latest,
                    page: Long = 1L,
                    perPage: Long = 30L,
                ): ActionsListJobsForWorkflowRunResponse
            }

            interface Logs {
                suspend fun actionsDownloadWorkflowRunLogs(
                    owner: String,
                    repo: String,
                    runId: Long,
                ): Unit

                sealed interface ActionsDeleteWorkflowRunLogsResult {
                    data object NoContent : ActionsDeleteWorkflowRunLogsResult

                    data class Forbidden(val value: BasicError) : ActionsDeleteWorkflowRunLogsResult

                    data class InternalServerError(val value: BasicError) : ActionsDeleteWorkflowRunLogsResult
                }

                suspend fun actionsDeleteWorkflowRunLogs(
                    owner: String,
                    repo: String,
                    runId: Long,
                ): ActionsDeleteWorkflowRunLogsResult
            }

            interface PendingDeployments {
                @Serializable
                data class ActionsReviewPendingDeploymentsForRunBody(
                    @SerialName("environment_ids") val environmentIds: List<Long>,
                    val state: State,
                    val comment: String,
                ) {
                    @Serializable
                    enum class State {
                        @SerialName("approved") Approved, @SerialName("rejected") Rejected;
                    }
                }

                suspend fun actionsGetPendingDeploymentsForRun(
                    owner: String,
                    repo: String,
                    runId: Long,
                ): List<PendingDeployment>

                suspend fun actionsReviewPendingDeploymentsForRun(
                    owner: String,
                    repo: String,
                    runId: Long,
                    body: ActionsReviewPendingDeploymentsForRunBody,
                ): List<Deployment>
            }

            interface Rerun {
                @Serializable
                @JvmInline
                value class ActionsReRunWorkflowBody(@SerialName("enable_debug_logging") val enableDebugLogging: Boolean? = null)

                suspend fun actionsReRunWorkflow(
                    owner: String,
                    repo: String,
                    runId: Long,
                    body: ActionsReRunWorkflowBody? = null,
                ): EmptyObject
            }

            interface RerunFailedJobs {
                @Serializable
                @JvmInline
                value class ActionsReRunWorkflowFailedJobsBody(@SerialName("enable_debug_logging") val enableDebugLogging: Boolean? = null)

                suspend fun actionsReRunWorkflowFailedJobs(
                    owner: String,
                    repo: String,
                    runId: Long,
                    body: ActionsReRunWorkflowFailedJobsBody? = null,
                ): EmptyObject
            }

            interface Timing {
                suspend fun actionsGetWorkflowRunUsage(
                    owner: String,
                    repo: String,
                    runId: Long,
                ): WorkflowRunUsage
            }
        }

        interface Secrets {
            val publicKey: Repos.Actions.Secrets.PublicKey

            @Serializable
            data class ActionsCreateOrUpdateRepoSecretBody(
                @SerialName("encrypted_value") val encryptedValue: String,
                @SerialName("key_id") val keyId: String,
            )


            @Serializable
            data class ActionsListRepoSecretsResponse(
                @SerialName("total_count") val totalCount: Long,
                val secrets: List<ActionsSecret>,
            )

            suspend fun actionsListRepoSecrets(
                owner: String,
                repo: String,
                page: Long = 1L,
                perPage: Long = 30L,
            ): ActionsListRepoSecretsResponse

            suspend fun actionsGetRepoSecret(
                owner: String,
                repo: String,
                secretName: String,
            ): ActionsSecret

            sealed interface ActionsCreateOrUpdateRepoSecretResult {
                data class Created(val value: EmptyObject) : ActionsCreateOrUpdateRepoSecretResult

                data object NoContent : ActionsCreateOrUpdateRepoSecretResult
            }

            suspend fun actionsCreateOrUpdateRepoSecret(
                owner: String,
                repo: String,
                secretName: String,
                body: ActionsCreateOrUpdateRepoSecretBody,
            ): ActionsCreateOrUpdateRepoSecretResult

            suspend fun actionsDeleteRepoSecret(
                owner: String,
                repo: String,
                secretName: String,
            ): Unit

            interface PublicKey {
                suspend fun actionsGetRepoPublicKey(
                    owner: String,
                    repo: String,
                ): ActionsPublicKey
            }
        }

        interface Variables {
            @Serializable
            data class ActionsCreateRepoVariableBody(val name: String, val value: String)


            @Serializable
            data class ActionsListRepoVariablesResponse(
                @SerialName("total_count") val totalCount: Long,
                val variables: List<ActionsVariable>,
            )


            @Serializable
            data class ActionsUpdateRepoVariableBody(val name: String? = null, val value: String? = null)

            suspend fun actionsListRepoVariables(
                owner: String,
                repo: String,
                page: Long = 1L,
                perPage: Long = 10L,
            ): ActionsListRepoVariablesResponse

            suspend fun actionsCreateRepoVariable(
                owner: String,
                repo: String,
                body: ActionsCreateRepoVariableBody,
            ): EmptyObject

            suspend fun actionsGetRepoVariable(
                owner: String,
                repo: String,
                name: String,
            ): ActionsVariable

            suspend fun actionsDeleteRepoVariable(
                owner: String,
                repo: String,
                name: String,
            ): Unit

            suspend fun actionsUpdateRepoVariable(
                owner: String,
                repo: String,
                name: String,
                body: ActionsUpdateRepoVariableBody,
            ): Unit
        }

        interface Workflows {
            val disable: Repos.Actions.Workflows.Disable

            val dispatches: Repos.Actions.Workflows.DispatchesApi

            val enable: Repos.Actions.Workflows.Enable

            val runs: Repos.Actions.Workflows.RunsApi

            val timing: Repos.Actions.Workflows.Timing

            @Serializable
            data class ActionsListRepoWorkflowsResponse(
                @SerialName("total_count") val totalCount: Long,
                val workflows: List<Workflow>,
            )


            @Serializable(with = WorkflowId.Serializer::class)
            sealed interface WorkflowId {
                @Serializable
                @JvmInline
                value class CaseLong(val value: Long) : WorkflowId

                @Serializable
                @JvmInline
                value class CaseString(val value: String) : WorkflowId

                object Serializer : KSerializer<WorkflowId> {
                    @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
                    override val descriptor: SerialDescriptor =
                        buildSerialDescriptor("io.github.nomisrev.api.Repos.Actions.Workflows.WorkflowId", PolymorphicKind.SEALED) {
                            element("CaseLong", Long.serializer().descriptor)
                            element("CaseString", String.serializer().descriptor)
                        }

                    override fun deserialize(decoder: Decoder): WorkflowId {
                        val value = decoder.decodeSerializableValue(JsonElement.serializer())
                        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                        return json.attemptDeserialize(
                            value,
                            CaseLong::class to { CaseLong(decodeFromJsonElement(Long.serializer(), it)) },
                            CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                        )
                    }

                    override fun serialize(encoder: Encoder, value: WorkflowId) = when(value) {
                        is CaseLong -> encoder.encodeSerializableValue(Long.serializer(), value.value)
                        is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                    }
                }
            }

            suspend fun actionsListRepoWorkflows(
                owner: String,
                repo: String,
                page: Long = 1L,
                perPage: Long = 30L,
            ): ActionsListRepoWorkflowsResponse

            suspend fun actionsGetWorkflow(
                owner: String,
                repo: String,
                workflowId: WorkflowId,
            ): Workflow

            interface Disable {
                @Serializable(with = ActionsDisableWorkflowWorkflowId.Serializer::class)
                sealed interface ActionsDisableWorkflowWorkflowId {
                    @Serializable
                    @JvmInline
                    value class CaseLong(val value: Long) : ActionsDisableWorkflowWorkflowId

                    @Serializable
                    @JvmInline
                    value class CaseString(val value: String) : ActionsDisableWorkflowWorkflowId

                    object Serializer : KSerializer<ActionsDisableWorkflowWorkflowId> {
                        @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
                        override val descriptor: SerialDescriptor =
                            buildSerialDescriptor("io.github.nomisrev.api.Repos.Actions.Workflows.Disable.ActionsDisableWorkflowWorkflowId", PolymorphicKind.SEALED) {
                                element("CaseLong", Long.serializer().descriptor)
                                element("CaseString", String.serializer().descriptor)
                            }

                        override fun deserialize(decoder: Decoder): ActionsDisableWorkflowWorkflowId {
                            val value = decoder.decodeSerializableValue(JsonElement.serializer())
                            val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                            return json.attemptDeserialize(
                                value,
                                CaseLong::class to { CaseLong(decodeFromJsonElement(Long.serializer(), it)) },
                                CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                            )
                        }

                        override fun serialize(encoder: Encoder, value: ActionsDisableWorkflowWorkflowId) = when(value) {
                            is CaseLong -> encoder.encodeSerializableValue(Long.serializer(), value.value)
                            is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                        }
                    }
                }

                suspend fun actionsDisableWorkflow(
                    owner: String,
                    repo: String,
                    workflowId: ActionsDisableWorkflowWorkflowId,
                ): Unit
            }

            interface DispatchesApi {
                @Serializable
                data class ActionsCreateWorkflowDispatchBody(
                    val ref: String,
                    val inputs: JsonElement? = null,
                    @SerialName("return_run_details") val returnRunDetails: Boolean? = null,
                )


                @Serializable(with = ActionsCreateWorkflowDispatchWorkflowId.Serializer::class)
                sealed interface ActionsCreateWorkflowDispatchWorkflowId {
                    @Serializable
                    @JvmInline
                    value class CaseLong(val value: Long) : ActionsCreateWorkflowDispatchWorkflowId

                    @Serializable
                    @JvmInline
                    value class CaseString(val value: String) : ActionsCreateWorkflowDispatchWorkflowId

                    object Serializer : KSerializer<ActionsCreateWorkflowDispatchWorkflowId> {
                        @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
                        override val descriptor: SerialDescriptor =
                            buildSerialDescriptor("io.github.nomisrev.api.Repos.Actions.Workflows.DispatchesApi.ActionsCreateWorkflowDispatchWorkflowId", PolymorphicKind.SEALED) {
                                element("CaseLong", Long.serializer().descriptor)
                                element("CaseString", String.serializer().descriptor)
                            }

                        override fun deserialize(decoder: Decoder): ActionsCreateWorkflowDispatchWorkflowId {
                            val value = decoder.decodeSerializableValue(JsonElement.serializer())
                            val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                            return json.attemptDeserialize(
                                value,
                                CaseLong::class to { CaseLong(decodeFromJsonElement(Long.serializer(), it)) },
                                CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                            )
                        }

                        override fun serialize(encoder: Encoder, value: ActionsCreateWorkflowDispatchWorkflowId) = when(value) {
                            is CaseLong -> encoder.encodeSerializableValue(Long.serializer(), value.value)
                            is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                        }
                    }
                }

                sealed interface ActionsCreateWorkflowDispatchResult {
                    data class OK(val value: WorkflowDispatchResponse) : ActionsCreateWorkflowDispatchResult

                    data object NoContent : ActionsCreateWorkflowDispatchResult
                }

                suspend fun actionsCreateWorkflowDispatch(
                    owner: String,
                    repo: String,
                    workflowId: ActionsCreateWorkflowDispatchWorkflowId,
                    body: ActionsCreateWorkflowDispatchBody,
                ): ActionsCreateWorkflowDispatchResult
            }

            interface Enable {
                @Serializable(with = ActionsEnableWorkflowWorkflowId.Serializer::class)
                sealed interface ActionsEnableWorkflowWorkflowId {
                    @Serializable
                    @JvmInline
                    value class CaseLong(val value: Long) : ActionsEnableWorkflowWorkflowId

                    @Serializable
                    @JvmInline
                    value class CaseString(val value: String) : ActionsEnableWorkflowWorkflowId

                    object Serializer : KSerializer<ActionsEnableWorkflowWorkflowId> {
                        @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
                        override val descriptor: SerialDescriptor =
                            buildSerialDescriptor("io.github.nomisrev.api.Repos.Actions.Workflows.Enable.ActionsEnableWorkflowWorkflowId", PolymorphicKind.SEALED) {
                                element("CaseLong", Long.serializer().descriptor)
                                element("CaseString", String.serializer().descriptor)
                            }

                        override fun deserialize(decoder: Decoder): ActionsEnableWorkflowWorkflowId {
                            val value = decoder.decodeSerializableValue(JsonElement.serializer())
                            val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                            return json.attemptDeserialize(
                                value,
                                CaseLong::class to { CaseLong(decodeFromJsonElement(Long.serializer(), it)) },
                                CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                            )
                        }

                        override fun serialize(encoder: Encoder, value: ActionsEnableWorkflowWorkflowId) = when(value) {
                            is CaseLong -> encoder.encodeSerializableValue(Long.serializer(), value.value)
                            is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                        }
                    }
                }

                suspend fun actionsEnableWorkflow(
                    owner: String,
                    repo: String,
                    workflowId: ActionsEnableWorkflowWorkflowId,
                ): Unit
            }

            interface RunsApi {
                @Serializable
                data class ActionsListWorkflowRunsResponse(
                    @SerialName("total_count") val totalCount: Long,
                    @SerialName("workflow_runs") val workflowRuns: List<WorkflowRun>,
                )


                @Serializable(with = ActionsListWorkflowRunsWorkflowId.Serializer::class)
                sealed interface ActionsListWorkflowRunsWorkflowId {
                    @Serializable
                    @JvmInline
                    value class CaseLong(val value: Long) : ActionsListWorkflowRunsWorkflowId

                    @Serializable
                    @JvmInline
                    value class CaseString(val value: String) : ActionsListWorkflowRunsWorkflowId

                    object Serializer : KSerializer<ActionsListWorkflowRunsWorkflowId> {
                        @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
                        override val descriptor: SerialDescriptor =
                            buildSerialDescriptor("io.github.nomisrev.api.Repos.Actions.Workflows.RunsApi.ActionsListWorkflowRunsWorkflowId", PolymorphicKind.SEALED) {
                                element("CaseLong", Long.serializer().descriptor)
                                element("CaseString", String.serializer().descriptor)
                            }

                        override fun deserialize(decoder: Decoder): ActionsListWorkflowRunsWorkflowId {
                            val value = decoder.decodeSerializableValue(JsonElement.serializer())
                            val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                            return json.attemptDeserialize(
                                value,
                                CaseLong::class to { CaseLong(decodeFromJsonElement(Long.serializer(), it)) },
                                CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                            )
                        }

                        override fun serialize(encoder: Encoder, value: ActionsListWorkflowRunsWorkflowId) = when(value) {
                            is CaseLong -> encoder.encodeSerializableValue(Long.serializer(), value.value)
                            is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                        }
                    }
                }


                @Serializable
                enum class Status {
                    @SerialName("completed")
                    Completed,
                    @SerialName("action_required")
                    ActionRequired,
                    @SerialName("cancelled")
                    Cancelled,
                    @SerialName("failure")
                    Failure,
                    @SerialName("neutral")
                    Neutral,
                    @SerialName("skipped")
                    Skipped,
                    @SerialName("stale")
                    Stale,
                    @SerialName("success")
                    Success,
                    @SerialName("timed_out")
                    TimedOut,
                    @SerialName("in_progress")
                    InProgress,
                    @SerialName("queued")
                    Queued,
                    @SerialName("requested")
                    Requested,
                    @SerialName("waiting")
                    Waiting,
                    @SerialName("pending")
                    Pending;
                }

                suspend fun actionsListWorkflowRuns(
                    owner: String,
                    repo: String,
                    workflowId: ActionsListWorkflowRunsWorkflowId,
                    excludePullRequests: Boolean = false,
                    page: Long = 1L,
                    perPage: Long = 30L,
                    actor: String? = null,
                    branch: String? = null,
                    checkSuiteId: Long? = null,
                    created: LocalDateTime? = null,
                    event: String? = null,
                    headSha: String? = null,
                    status: Status? = null,
                ): ActionsListWorkflowRunsResponse
            }

            interface Timing {
                @Serializable(with = ActionsGetWorkflowUsageWorkflowId.Serializer::class)
                sealed interface ActionsGetWorkflowUsageWorkflowId {
                    @Serializable
                    @JvmInline
                    value class CaseLong(val value: Long) : ActionsGetWorkflowUsageWorkflowId

                    @Serializable
                    @JvmInline
                    value class CaseString(val value: String) : ActionsGetWorkflowUsageWorkflowId

                    object Serializer : KSerializer<ActionsGetWorkflowUsageWorkflowId> {
                        @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
                        override val descriptor: SerialDescriptor =
                            buildSerialDescriptor("io.github.nomisrev.api.Repos.Actions.Workflows.Timing.ActionsGetWorkflowUsageWorkflowId", PolymorphicKind.SEALED) {
                                element("CaseLong", Long.serializer().descriptor)
                                element("CaseString", String.serializer().descriptor)
                            }

                        override fun deserialize(decoder: Decoder): ActionsGetWorkflowUsageWorkflowId {
                            val value = decoder.decodeSerializableValue(JsonElement.serializer())
                            val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                            return json.attemptDeserialize(
                                value,
                                CaseLong::class to { CaseLong(decodeFromJsonElement(Long.serializer(), it)) },
                                CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                            )
                        }

                        override fun serialize(encoder: Encoder, value: ActionsGetWorkflowUsageWorkflowId) = when(value) {
                            is CaseLong -> encoder.encodeSerializableValue(Long.serializer(), value.value)
                            is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                        }
                    }
                }

                suspend fun actionsGetWorkflowUsage(
                    owner: String,
                    repo: String,
                    workflowId: ActionsGetWorkflowUsageWorkflowId,
                ): WorkflowUsage
            }
        }
    }

    interface Activity {
        @Serializable
        enum class ActivityType {
            @SerialName("push")
            Push,
            @SerialName("force_push")
            ForcePush,
            @SerialName("branch_creation")
            BranchCreation,
            @SerialName("branch_deletion")
            BranchDeletion,
            @SerialName("pr_merge")
            PrMerge,
            @SerialName("merge_queue_merge")
            MergeQueueMerge;
        }


        @Serializable
        enum class Direction {
            @SerialName("asc") Asc, @SerialName("desc") Desc;
        }


        @Serializable
        enum class TimePeriod {
            @SerialName("day")
            Day,
            @SerialName("week")
            Week,
            @SerialName("month")
            Month,
            @SerialName("quarter")
            Quarter,
            @SerialName("year")
            Year;
        }

        sealed interface ReposListActivitiesResult {
            data class OK(val value: List<Activity>) : ReposListActivitiesResult

            data class UnprocessableEntity(val value: ValidationErrorSimple) : ReposListActivitiesResult
        }

        suspend fun reposListActivities(
            owner: String,
            repo: String,
            direction: Direction = Direction.Desc,
            perPage: Long = 30L,
            activityType: ActivityType? = null,
            actor: String? = null,
            after: String? = null,
            before: String? = null,
            ref: String? = null,
            timePeriod: TimePeriod? = null,
        ): ReposListActivitiesResult
    }

    interface Assignees {
        sealed interface IssuesListAssigneesResult {
            data class OK(val value: List<SimpleUser>) : IssuesListAssigneesResult

            data class NotFound(val value: BasicError) : IssuesListAssigneesResult
        }

        suspend fun issuesListAssignees(
            owner: String,
            repo: String,
            page: Long = 1L,
            perPage: Long = 30L,
        ): IssuesListAssigneesResult

        sealed interface IssuesCheckUserCanBeAssignedResult {
            data object NoContent : IssuesCheckUserCanBeAssignedResult

            data class NotFound(val value: BasicError) : IssuesCheckUserCanBeAssignedResult
        }

        suspend fun issuesCheckUserCanBeAssigned(
            owner: String,
            repo: String,
            assignee: String,
        ): IssuesCheckUserCanBeAssignedResult
    }

    interface Attestations {
        @Serializable
        @JvmInline
        value class ReposCreateAttestationBody(val bundle: Bundle) {
            @Serializable
            data class Bundle(
                val mediaType: String? = null,
                val verificationMaterial: JsonElement? = null,
                val dsseEnvelope: JsonElement? = null,
            )
        }


        @Serializable
        @JvmInline
        value class ReposCreateAttestationResponse(val id: Long? = null)


        @Serializable
        @JvmInline
        value class ReposListAttestationsResponse(val attestations: List<Attestations>? = null) {
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

        sealed interface ReposCreateAttestationResult {
            data class Created(val value: ReposCreateAttestationResponse) : ReposCreateAttestationResult

            data class Forbidden(val value: BasicError) : ReposCreateAttestationResult

            data class UnprocessableEntity(val value: ValidationError) : ReposCreateAttestationResult
        }

        suspend fun reposCreateAttestation(
            owner: String,
            repo: String,
            body: ReposCreateAttestationBody,
        ): ReposCreateAttestationResult

        suspend fun reposListAttestations(
            owner: String,
            repo: String,
            subjectDigest: String,
            perPage: Long = 30L,
            after: String? = null,
            before: String? = null,
            predicateType: String? = null,
        ): ReposListAttestationsResponse
    }

    interface Autolinks {
        @Serializable
        data class ReposCreateAutolinkBody(
            @SerialName("key_prefix") val keyPrefix: String,
            @SerialName("url_template") val urlTemplate: String,
            @SerialName("is_alphanumeric") val isAlphanumeric: Boolean? = null,
        )

        suspend fun reposListAutolinks(
            owner: String,
            repo: String,
        ): List<Autolink>

        sealed interface ReposCreateAutolinkResult {
            data class Created(val value: Autolink) : ReposCreateAutolinkResult

            data class UnprocessableEntity(val value: ValidationError) : ReposCreateAutolinkResult
        }

        suspend fun reposCreateAutolink(
            owner: String,
            repo: String,
            body: ReposCreateAutolinkBody,
        ): ReposCreateAutolinkResult

        sealed interface ReposGetAutolinkResult {
            data class OK(val value: Autolink) : ReposGetAutolinkResult

            data class NotFound(val value: BasicError) : ReposGetAutolinkResult
        }

        suspend fun reposGetAutolink(
            owner: String,
            repo: String,
            autolinkId: Long,
        ): ReposGetAutolinkResult

        sealed interface ReposDeleteAutolinkResult {
            data object NoContent : ReposDeleteAutolinkResult

            data class NotFound(val value: BasicError) : ReposDeleteAutolinkResult
        }

        suspend fun reposDeleteAutolink(
            owner: String,
            repo: String,
            autolinkId: Long,
        ): ReposDeleteAutolinkResult
    }

    interface AutomatedSecurityFixes {
        sealed interface ReposCheckAutomatedSecurityFixesResult {
            data class OK(val value: CheckAutomatedSecurityFixes) : ReposCheckAutomatedSecurityFixesResult

            data object NotFound : ReposCheckAutomatedSecurityFixesResult
        }

        suspend fun reposCheckAutomatedSecurityFixes(
            owner: String,
            repo: String,
        ): ReposCheckAutomatedSecurityFixesResult

        suspend fun reposEnableAutomatedSecurityFixes(
            owner: String,
            repo: String,
        ): Unit

        suspend fun reposDisableAutomatedSecurityFixes(
            owner: String,
            repo: String,
        ): Unit
    }

    interface Branches {
        val protection: Repos.Branches.Protection

        val rename: Repos.Branches.Rename

        sealed interface ReposListBranchesResult {
            data class OK(val value: List<ShortBranch>) : ReposListBranchesResult

            data class NotFound(val value: BasicError) : ReposListBranchesResult
        }

        suspend fun reposListBranches(
            owner: String,
            repo: String,
            page: Long = 1L,
            perPage: Long = 30L,
            protected: Boolean? = null,
        ): ReposListBranchesResult

        sealed interface ReposGetBranchResult {
            data class OK(val value: BranchWithProtection) : ReposGetBranchResult

            data class MovedPermanently(val value: BasicError) : ReposGetBranchResult

            data class NotFound(val value: BasicError) : ReposGetBranchResult
        }

        suspend fun reposGetBranch(
            owner: String,
            repo: String,
            branch: String,
        ): ReposGetBranchResult

        interface Protection {
            val enforceAdmins: Repos.Branches.Protection.EnforceAdmins

            val requiredPullRequestReviews: Repos.Branches.Protection.RequiredPullRequestReviews

            val requiredSignatures: Repos.Branches.Protection.RequiredSignatures

            val requiredStatusChecks: Repos.Branches.Protection.RequiredStatusChecks

            val restrictions: Repos.Branches.Protection.Restrictions

            @Serializable
            data class ReposUpdateBranchProtectionBody(
                @SerialName("required_status_checks") val requiredStatusChecks: RequiredStatusChecks?,
                @SerialName("enforce_admins") val enforceAdmins: Boolean?,
                @SerialName("required_pull_request_reviews") val requiredPullRequestReviews: RequiredPullRequestReviews?,
                val restrictions: Restrictions?,
                @SerialName("required_linear_history") val requiredLinearHistory: Boolean? = null,
                @SerialName("allow_force_pushes") val allowForcePushes: Boolean? = null,
                @SerialName("allow_deletions") val allowDeletions: Boolean? = null,
                @SerialName("block_creations") val blockCreations: Boolean? = null,
                @SerialName("required_conversation_resolution") val requiredConversationResolution: Boolean? = null,
                @SerialName("lock_branch") val lockBranch: Boolean? = null,
                @SerialName("allow_fork_syncing") val allowForkSyncing: Boolean? = null,
            ) {
                @Serializable
                data class RequiredStatusChecks(val strict: Boolean, val contexts: List<String>, val checks: List<Checks>? = null) {
                    @Serializable
                    data class Checks(val context: String, @SerialName("app_id") val appId: Long? = null)
                }

                @Serializable
                data class RequiredPullRequestReviews(
                    @SerialName("dismissal_restrictions") val dismissalRestrictions: DismissalRestrictions? = null,
                    @SerialName("dismiss_stale_reviews") val dismissStaleReviews: Boolean? = null,
                    @SerialName("require_code_owner_reviews") val requireCodeOwnerReviews: Boolean? = null,
                    @SerialName("required_approving_review_count") val requiredApprovingReviewCount: Long? = null,
                    @SerialName("require_last_push_approval") val requireLastPushApproval: Boolean? = null,
                    @SerialName("bypass_pull_request_allowances") val bypassPullRequestAllowances: BypassPullRequestAllowances? = null,
                ) {
                    @Serializable
                    data class DismissalRestrictions(
                        val users: List<String>? = null,
                        val teams: List<String>? = null,
                        val apps: List<String>? = null,
                    )

                    @Serializable
                    data class BypassPullRequestAllowances(
                        val users: List<String>? = null,
                        val teams: List<String>? = null,
                        val apps: List<String>? = null,
                    )
                }

                @Serializable
                data class Restrictions(val users: List<String>, val teams: List<String>, val apps: List<String>? = null)
            }

            sealed interface ReposGetBranchProtectionResult {
                data class OK(val value: BranchProtection) : ReposGetBranchProtectionResult

                data class NotFound(val value: BasicError) : ReposGetBranchProtectionResult
            }

            suspend fun reposGetBranchProtection(
                owner: String,
                repo: String,
                branch: String,
            ): ReposGetBranchProtectionResult

            sealed interface ReposUpdateBranchProtectionResult {
                data class OK(val value: ProtectedBranch) : ReposUpdateBranchProtectionResult

                data class Forbidden(val value: BasicError) : ReposUpdateBranchProtectionResult

                data class NotFound(val value: BasicError) : ReposUpdateBranchProtectionResult

                data class UnprocessableEntity(val value: ValidationErrorSimple) : ReposUpdateBranchProtectionResult
            }

            suspend fun reposUpdateBranchProtection(
                owner: String,
                repo: String,
                branch: String,
                body: ReposUpdateBranchProtectionBody,
            ): ReposUpdateBranchProtectionResult

            sealed interface ReposDeleteBranchProtectionResult {
                data object NoContent : ReposDeleteBranchProtectionResult

                data class Forbidden(val value: BasicError) : ReposDeleteBranchProtectionResult
            }

            suspend fun reposDeleteBranchProtection(
                owner: String,
                repo: String,
                branch: String,
            ): ReposDeleteBranchProtectionResult

            interface EnforceAdmins {
                suspend fun reposGetAdminBranchProtection(
                    owner: String,
                    repo: String,
                    branch: String,
                ): ProtectedBranchAdminEnforced

                suspend fun reposSetAdminBranchProtection(
                    owner: String,
                    repo: String,
                    branch: String,
                ): ProtectedBranchAdminEnforced

                sealed interface ReposDeleteAdminBranchProtectionResult {
                    data object NoContent : ReposDeleteAdminBranchProtectionResult

                    data class NotFound(val value: BasicError) : ReposDeleteAdminBranchProtectionResult
                }

                suspend fun reposDeleteAdminBranchProtection(
                    owner: String,
                    repo: String,
                    branch: String,
                ): ReposDeleteAdminBranchProtectionResult
            }

            interface RequiredPullRequestReviews {
                @Serializable
                data class ReposUpdatePullRequestReviewProtectionBody(
                    @SerialName("dismissal_restrictions") val dismissalRestrictions: DismissalRestrictions? = null,
                    @SerialName("dismiss_stale_reviews") val dismissStaleReviews: Boolean? = null,
                    @SerialName("require_code_owner_reviews") val requireCodeOwnerReviews: Boolean? = null,
                    @SerialName("required_approving_review_count") val requiredApprovingReviewCount: Long? = null,
                    @SerialName("require_last_push_approval") val requireLastPushApproval: Boolean? = null,
                    @SerialName("bypass_pull_request_allowances") val bypassPullRequestAllowances: BypassPullRequestAllowances? = null,
                ) {
                    @Serializable
                    data class DismissalRestrictions(
                        val users: List<String>? = null,
                        val teams: List<String>? = null,
                        val apps: List<String>? = null,
                    )

                    @Serializable
                    data class BypassPullRequestAllowances(
                        val users: List<String>? = null,
                        val teams: List<String>? = null,
                        val apps: List<String>? = null,
                    )
                }

                suspend fun reposGetPullRequestReviewProtection(
                    owner: String,
                    repo: String,
                    branch: String,
                ): ProtectedBranchPullRequestReview

                sealed interface ReposDeletePullRequestReviewProtectionResult {
                    data object NoContent : ReposDeletePullRequestReviewProtectionResult

                    data class NotFound(val value: BasicError) : ReposDeletePullRequestReviewProtectionResult
                }

                suspend fun reposDeletePullRequestReviewProtection(
                    owner: String,
                    repo: String,
                    branch: String,
                ): ReposDeletePullRequestReviewProtectionResult

                sealed interface ReposUpdatePullRequestReviewProtectionResult {
                    data class OK(val value: ProtectedBranchPullRequestReview) : ReposUpdatePullRequestReviewProtectionResult

                    data class UnprocessableEntity(val value: ValidationError) : ReposUpdatePullRequestReviewProtectionResult
                }

                suspend fun reposUpdatePullRequestReviewProtection(
                    owner: String,
                    repo: String,
                    branch: String,
                    body: ReposUpdatePullRequestReviewProtectionBody? = null,
                ): ReposUpdatePullRequestReviewProtectionResult
            }

            interface RequiredSignatures {
                sealed interface ReposGetCommitSignatureProtectionResult {
                    data class OK(val value: ProtectedBranchAdminEnforced) : ReposGetCommitSignatureProtectionResult

                    data class NotFound(val value: BasicError) : ReposGetCommitSignatureProtectionResult
                }

                suspend fun reposGetCommitSignatureProtection(
                    owner: String,
                    repo: String,
                    branch: String,
                ): ReposGetCommitSignatureProtectionResult

                sealed interface ReposCreateCommitSignatureProtectionResult {
                    data class OK(val value: ProtectedBranchAdminEnforced) : ReposCreateCommitSignatureProtectionResult

                    data class NotFound(val value: BasicError) : ReposCreateCommitSignatureProtectionResult
                }

                suspend fun reposCreateCommitSignatureProtection(
                    owner: String,
                    repo: String,
                    branch: String,
                ): ReposCreateCommitSignatureProtectionResult

                sealed interface ReposDeleteCommitSignatureProtectionResult {
                    data object NoContent : ReposDeleteCommitSignatureProtectionResult

                    data class NotFound(val value: BasicError) : ReposDeleteCommitSignatureProtectionResult
                }

                suspend fun reposDeleteCommitSignatureProtection(
                    owner: String,
                    repo: String,
                    branch: String,
                ): ReposDeleteCommitSignatureProtectionResult
            }

            interface RequiredStatusChecks {
                val contexts: Repos.Branches.Protection.RequiredStatusChecks.Contexts

                @Serializable
                data class ReposUpdateStatusCheckProtectionBody(
                    val strict: Boolean? = null,
                    val contexts: List<String>? = null,
                    val checks: List<Checks>? = null,
                ) {
                    @Serializable
                    data class Checks(val context: String, @SerialName("app_id") val appId: Long? = null)
                }

                sealed interface ReposGetStatusChecksProtectionResult {
                    data class OK(val value: StatusCheckPolicy) : ReposGetStatusChecksProtectionResult

                    data class NotFound(val value: BasicError) : ReposGetStatusChecksProtectionResult
                }

                suspend fun reposGetStatusChecksProtection(
                    owner: String,
                    repo: String,
                    branch: String,
                ): ReposGetStatusChecksProtectionResult

                suspend fun reposRemoveStatusCheckProtection(
                    owner: String,
                    repo: String,
                    branch: String,
                ): Unit

                sealed interface ReposUpdateStatusCheckProtectionResult {
                    data class OK(val value: StatusCheckPolicy) : ReposUpdateStatusCheckProtectionResult

                    data class NotFound(val value: BasicError) : ReposUpdateStatusCheckProtectionResult

                    data class UnprocessableEntity(val value: ValidationError) : ReposUpdateStatusCheckProtectionResult
                }

                suspend fun reposUpdateStatusCheckProtection(
                    owner: String,
                    repo: String,
                    branch: String,
                    body: ReposUpdateStatusCheckProtectionBody? = null,
                ): ReposUpdateStatusCheckProtectionResult

                interface Contexts {
                    @Serializable(with = ReposAddStatusCheckContextsBody.Serializer::class)
                    sealed interface ReposAddStatusCheckContextsBody {
                        @Serializable
                        @JvmInline
                        value class Contexts(val contexts: List<String>) : ReposAddStatusCheckContextsBody

                        @Serializable
                        @JvmInline
                        value class CaseStrings(val value: List<String>) : ReposAddStatusCheckContextsBody

                        object Serializer : KSerializer<ReposAddStatusCheckContextsBody> {
                            @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
                            override val descriptor: SerialDescriptor =
                                buildSerialDescriptor("io.github.nomisrev.api.Repos.Branches.Protection.RequiredStatusChecks.Contexts.ReposAddStatusCheckContextsBody", PolymorphicKind.SEALED) {
                                    element("Contexts", ReposAddStatusCheckContextsBody.Contexts.serializer().descriptor)
                                    element("CaseStrings", ListSerializer(String.serializer()).descriptor)
                                }

                            override fun deserialize(decoder: Decoder): ReposAddStatusCheckContextsBody {
                                val value = decoder.decodeSerializableValue(JsonElement.serializer())
                                val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                                return json.attemptDeserialize(
                                    value,
                                    Contexts::class to { decodeFromJsonElement(ReposAddStatusCheckContextsBody.Contexts.serializer(), it) },
                                    CaseStrings::class to { CaseStrings(decodeFromJsonElement(ListSerializer(String.serializer()), it)) },
                                )
                            }

                            override fun serialize(encoder: Encoder, value: ReposAddStatusCheckContextsBody) = when(value) {
                                is Contexts -> encoder.encodeSerializableValue(ReposAddStatusCheckContextsBody.Contexts.serializer(), value)
                                is CaseStrings -> encoder.encodeSerializableValue(ListSerializer(String.serializer()), value.value)
                            }
                        }
                    }


                    @Serializable(with = ReposRemoveStatusCheckContextsBody.Serializer::class)
                    sealed interface ReposRemoveStatusCheckContextsBody {
                        @Serializable
                        @JvmInline
                        value class Contexts(val contexts: List<String>) : ReposRemoveStatusCheckContextsBody

                        @Serializable
                        @JvmInline
                        value class CaseStrings(val value: List<String>) : ReposRemoveStatusCheckContextsBody

                        object Serializer : KSerializer<ReposRemoveStatusCheckContextsBody> {
                            @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
                            override val descriptor: SerialDescriptor =
                                buildSerialDescriptor("io.github.nomisrev.api.Repos.Branches.Protection.RequiredStatusChecks.Contexts.ReposRemoveStatusCheckContextsBody", PolymorphicKind.SEALED) {
                                    element("Contexts", ReposRemoveStatusCheckContextsBody.Contexts.serializer().descriptor)
                                    element("CaseStrings", ListSerializer(String.serializer()).descriptor)
                                }

                            override fun deserialize(decoder: Decoder): ReposRemoveStatusCheckContextsBody {
                                val value = decoder.decodeSerializableValue(JsonElement.serializer())
                                val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                                return json.attemptDeserialize(
                                    value,
                                    Contexts::class to { decodeFromJsonElement(ReposRemoveStatusCheckContextsBody.Contexts.serializer(), it) },
                                    CaseStrings::class to { CaseStrings(decodeFromJsonElement(ListSerializer(String.serializer()), it)) },
                                )
                            }

                            override fun serialize(encoder: Encoder, value: ReposRemoveStatusCheckContextsBody) = when(value) {
                                is Contexts -> encoder.encodeSerializableValue(ReposRemoveStatusCheckContextsBody.Contexts.serializer(), value)
                                is CaseStrings -> encoder.encodeSerializableValue(ListSerializer(String.serializer()), value.value)
                            }
                        }
                    }


                    @Serializable(with = ReposSetStatusCheckContextsBody.Serializer::class)
                    sealed interface ReposSetStatusCheckContextsBody {
                        @Serializable
                        @JvmInline
                        value class Contexts(val contexts: List<String>) : ReposSetStatusCheckContextsBody

                        @Serializable
                        @JvmInline
                        value class CaseStrings(val value: List<String>) : ReposSetStatusCheckContextsBody

                        object Serializer : KSerializer<ReposSetStatusCheckContextsBody> {
                            @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
                            override val descriptor: SerialDescriptor =
                                buildSerialDescriptor("io.github.nomisrev.api.Repos.Branches.Protection.RequiredStatusChecks.Contexts.ReposSetStatusCheckContextsBody", PolymorphicKind.SEALED) {
                                    element("Contexts", ReposSetStatusCheckContextsBody.Contexts.serializer().descriptor)
                                    element("CaseStrings", ListSerializer(String.serializer()).descriptor)
                                }

                            override fun deserialize(decoder: Decoder): ReposSetStatusCheckContextsBody {
                                val value = decoder.decodeSerializableValue(JsonElement.serializer())
                                val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                                return json.attemptDeserialize(
                                    value,
                                    Contexts::class to { decodeFromJsonElement(ReposSetStatusCheckContextsBody.Contexts.serializer(), it) },
                                    CaseStrings::class to { CaseStrings(decodeFromJsonElement(ListSerializer(String.serializer()), it)) },
                                )
                            }

                            override fun serialize(encoder: Encoder, value: ReposSetStatusCheckContextsBody) = when(value) {
                                is Contexts -> encoder.encodeSerializableValue(ReposSetStatusCheckContextsBody.Contexts.serializer(), value)
                                is CaseStrings -> encoder.encodeSerializableValue(ListSerializer(String.serializer()), value.value)
                            }
                        }
                    }

                    sealed interface ReposGetAllStatusCheckContextsResult {
                        data class OK(val value: List<String>) : ReposGetAllStatusCheckContextsResult

                        data class NotFound(val value: BasicError) : ReposGetAllStatusCheckContextsResult
                    }

                    suspend fun reposGetAllStatusCheckContexts(
                        owner: String,
                        repo: String,
                        branch: String,
                    ): ReposGetAllStatusCheckContextsResult

                    sealed interface ReposSetStatusCheckContextsResult {
                        data class OK(val value: List<String>) : ReposSetStatusCheckContextsResult

                        data class NotFound(val value: BasicError) : ReposSetStatusCheckContextsResult

                        data class UnprocessableEntity(val value: ValidationError) : ReposSetStatusCheckContextsResult
                    }

                    suspend fun reposSetStatusCheckContexts(
                        owner: String,
                        repo: String,
                        branch: String,
                        body: ReposSetStatusCheckContextsBody? = null,
                    ): ReposSetStatusCheckContextsResult

                    sealed interface ReposAddStatusCheckContextsResult {
                        data class OK(val value: List<String>) : ReposAddStatusCheckContextsResult

                        data class Forbidden(val value: BasicError) : ReposAddStatusCheckContextsResult

                        data class NotFound(val value: BasicError) : ReposAddStatusCheckContextsResult

                        data class UnprocessableEntity(val value: ValidationError) : ReposAddStatusCheckContextsResult
                    }

                    suspend fun reposAddStatusCheckContexts(
                        owner: String,
                        repo: String,
                        branch: String,
                        body: ReposAddStatusCheckContextsBody? = null,
                    ): ReposAddStatusCheckContextsResult

                    sealed interface ReposRemoveStatusCheckContextsResult {
                        data class OK(val value: List<String>) : ReposRemoveStatusCheckContextsResult

                        data class NotFound(val value: BasicError) : ReposRemoveStatusCheckContextsResult

                        data class UnprocessableEntity(val value: ValidationError) : ReposRemoveStatusCheckContextsResult
                    }

                    suspend fun reposRemoveStatusCheckContexts(
                        owner: String,
                        repo: String,
                        branch: String,
                        body: ReposRemoveStatusCheckContextsBody? = null,
                    ): ReposRemoveStatusCheckContextsResult
                }
            }

            interface Restrictions {
                val apps: Repos.Branches.Protection.Restrictions.Apps

                val teams: Repos.Branches.Protection.Restrictions.TeamsApi

                val users: Repos.Branches.Protection.Restrictions.Users

                sealed interface ReposGetAccessRestrictionsResult {
                    data class OK(val value: BranchRestrictionPolicy) : ReposGetAccessRestrictionsResult

                    data class NotFound(val value: BasicError) : ReposGetAccessRestrictionsResult
                }

                suspend fun reposGetAccessRestrictions(
                    owner: String,
                    repo: String,
                    branch: String,
                ): ReposGetAccessRestrictionsResult

                suspend fun reposDeleteAccessRestrictions(
                    owner: String,
                    repo: String,
                    branch: String,
                ): Unit

                interface Apps {
                    @Serializable
                    @JvmInline
                    value class ReposAddAppAccessRestrictionsBody(val apps: List<String>)


                    @Serializable
                    @JvmInline
                    value class ReposRemoveAppAccessRestrictionsBody(val apps: List<String>)


                    @Serializable
                    @JvmInline
                    value class ReposSetAppAccessRestrictionsBody(val apps: List<String>)

                    sealed interface ReposGetAppsWithAccessToProtectedBranchResult {
                        data class OK(val value: List<Integration>) : ReposGetAppsWithAccessToProtectedBranchResult

                        data class NotFound(val value: BasicError) : ReposGetAppsWithAccessToProtectedBranchResult
                    }

                    suspend fun reposGetAppsWithAccessToProtectedBranch(
                        owner: String,
                        repo: String,
                        branch: String,
                    ): ReposGetAppsWithAccessToProtectedBranchResult

                    sealed interface ReposSetAppAccessRestrictionsResult {
                        data class OK(val value: List<Integration>) : ReposSetAppAccessRestrictionsResult

                        data class UnprocessableEntity(val value: ValidationError) : ReposSetAppAccessRestrictionsResult
                    }

                    suspend fun reposSetAppAccessRestrictions(
                        owner: String,
                        repo: String,
                        branch: String,
                        body: ReposSetAppAccessRestrictionsBody,
                    ): ReposSetAppAccessRestrictionsResult

                    sealed interface ReposAddAppAccessRestrictionsResult {
                        data class OK(val value: List<Integration>) : ReposAddAppAccessRestrictionsResult

                        data class UnprocessableEntity(val value: ValidationError) : ReposAddAppAccessRestrictionsResult
                    }

                    suspend fun reposAddAppAccessRestrictions(
                        owner: String,
                        repo: String,
                        branch: String,
                        body: ReposAddAppAccessRestrictionsBody,
                    ): ReposAddAppAccessRestrictionsResult

                    sealed interface ReposRemoveAppAccessRestrictionsResult {
                        data class OK(val value: List<Integration>) : ReposRemoveAppAccessRestrictionsResult

                        data class UnprocessableEntity(val value: ValidationError) : ReposRemoveAppAccessRestrictionsResult
                    }

                    suspend fun reposRemoveAppAccessRestrictions(
                        owner: String,
                        repo: String,
                        branch: String,
                        body: ReposRemoveAppAccessRestrictionsBody,
                    ): ReposRemoveAppAccessRestrictionsResult
                }

                interface TeamsApi {
                    @Serializable(with = ReposAddTeamAccessRestrictionsBody.Serializer::class)
                    sealed interface ReposAddTeamAccessRestrictionsBody {
                        @Serializable
                        @JvmInline
                        value class Teams(val teams: List<String>) : ReposAddTeamAccessRestrictionsBody

                        @Serializable
                        @JvmInline
                        value class CaseStrings(val value: List<String>) : ReposAddTeamAccessRestrictionsBody

                        object Serializer : KSerializer<ReposAddTeamAccessRestrictionsBody> {
                            @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
                            override val descriptor: SerialDescriptor =
                                buildSerialDescriptor("io.github.nomisrev.api.Repos.Branches.Protection.Restrictions.TeamsApi.ReposAddTeamAccessRestrictionsBody", PolymorphicKind.SEALED) {
                                    element("Teams", ReposAddTeamAccessRestrictionsBody.Teams.serializer().descriptor)
                                    element("CaseStrings", ListSerializer(String.serializer()).descriptor)
                                }

                            override fun deserialize(decoder: Decoder): ReposAddTeamAccessRestrictionsBody {
                                val value = decoder.decodeSerializableValue(JsonElement.serializer())
                                val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                                return json.attemptDeserialize(
                                    value,
                                    Teams::class to { decodeFromJsonElement(ReposAddTeamAccessRestrictionsBody.Teams.serializer(), it) },
                                    CaseStrings::class to { CaseStrings(decodeFromJsonElement(ListSerializer(String.serializer()), it)) },
                                )
                            }

                            override fun serialize(encoder: Encoder, value: ReposAddTeamAccessRestrictionsBody) = when(value) {
                                is Teams -> encoder.encodeSerializableValue(ReposAddTeamAccessRestrictionsBody.Teams.serializer(), value)
                                is CaseStrings -> encoder.encodeSerializableValue(ListSerializer(String.serializer()), value.value)
                            }
                        }
                    }


                    @Serializable(with = ReposRemoveTeamAccessRestrictionsBody.Serializer::class)
                    sealed interface ReposRemoveTeamAccessRestrictionsBody {
                        @Serializable
                        @JvmInline
                        value class Teams(val teams: List<String>) : ReposRemoveTeamAccessRestrictionsBody

                        @Serializable
                        @JvmInline
                        value class CaseStrings(val value: List<String>) : ReposRemoveTeamAccessRestrictionsBody

                        object Serializer : KSerializer<ReposRemoveTeamAccessRestrictionsBody> {
                            @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
                            override val descriptor: SerialDescriptor =
                                buildSerialDescriptor("io.github.nomisrev.api.Repos.Branches.Protection.Restrictions.TeamsApi.ReposRemoveTeamAccessRestrictionsBody", PolymorphicKind.SEALED) {
                                    element("Teams", ReposRemoveTeamAccessRestrictionsBody.Teams.serializer().descriptor)
                                    element("CaseStrings", ListSerializer(String.serializer()).descriptor)
                                }

                            override fun deserialize(decoder: Decoder): ReposRemoveTeamAccessRestrictionsBody {
                                val value = decoder.decodeSerializableValue(JsonElement.serializer())
                                val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                                return json.attemptDeserialize(
                                    value,
                                    Teams::class to { decodeFromJsonElement(ReposRemoveTeamAccessRestrictionsBody.Teams.serializer(), it) },
                                    CaseStrings::class to { CaseStrings(decodeFromJsonElement(ListSerializer(String.serializer()), it)) },
                                )
                            }

                            override fun serialize(encoder: Encoder, value: ReposRemoveTeamAccessRestrictionsBody) = when(value) {
                                is Teams -> encoder.encodeSerializableValue(ReposRemoveTeamAccessRestrictionsBody.Teams.serializer(), value)
                                is CaseStrings -> encoder.encodeSerializableValue(ListSerializer(String.serializer()), value.value)
                            }
                        }
                    }


                    @Serializable(with = ReposSetTeamAccessRestrictionsBody.Serializer::class)
                    sealed interface ReposSetTeamAccessRestrictionsBody {
                        @Serializable
                        @JvmInline
                        value class Teams(val teams: List<String>) : ReposSetTeamAccessRestrictionsBody

                        @Serializable
                        @JvmInline
                        value class CaseStrings(val value: List<String>) : ReposSetTeamAccessRestrictionsBody

                        object Serializer : KSerializer<ReposSetTeamAccessRestrictionsBody> {
                            @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
                            override val descriptor: SerialDescriptor =
                                buildSerialDescriptor("io.github.nomisrev.api.Repos.Branches.Protection.Restrictions.TeamsApi.ReposSetTeamAccessRestrictionsBody", PolymorphicKind.SEALED) {
                                    element("Teams", ReposSetTeamAccessRestrictionsBody.Teams.serializer().descriptor)
                                    element("CaseStrings", ListSerializer(String.serializer()).descriptor)
                                }

                            override fun deserialize(decoder: Decoder): ReposSetTeamAccessRestrictionsBody {
                                val value = decoder.decodeSerializableValue(JsonElement.serializer())
                                val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                                return json.attemptDeserialize(
                                    value,
                                    Teams::class to { decodeFromJsonElement(ReposSetTeamAccessRestrictionsBody.Teams.serializer(), it) },
                                    CaseStrings::class to { CaseStrings(decodeFromJsonElement(ListSerializer(String.serializer()), it)) },
                                )
                            }

                            override fun serialize(encoder: Encoder, value: ReposSetTeamAccessRestrictionsBody) = when(value) {
                                is Teams -> encoder.encodeSerializableValue(ReposSetTeamAccessRestrictionsBody.Teams.serializer(), value)
                                is CaseStrings -> encoder.encodeSerializableValue(ListSerializer(String.serializer()), value.value)
                            }
                        }
                    }

                    sealed interface ReposGetTeamsWithAccessToProtectedBranchResult {
                        data class OK(val value: List<Team>) : ReposGetTeamsWithAccessToProtectedBranchResult

                        data class NotFound(val value: BasicError) : ReposGetTeamsWithAccessToProtectedBranchResult
                    }

                    suspend fun reposGetTeamsWithAccessToProtectedBranch(
                        owner: String,
                        repo: String,
                        branch: String,
                    ): ReposGetTeamsWithAccessToProtectedBranchResult

                    sealed interface ReposSetTeamAccessRestrictionsResult {
                        data class OK(val value: List<Team>) : ReposSetTeamAccessRestrictionsResult

                        data class UnprocessableEntity(val value: ValidationError) : ReposSetTeamAccessRestrictionsResult
                    }

                    suspend fun reposSetTeamAccessRestrictions(
                        owner: String,
                        repo: String,
                        branch: String,
                        body: ReposSetTeamAccessRestrictionsBody? = null,
                    ): ReposSetTeamAccessRestrictionsResult

                    sealed interface ReposAddTeamAccessRestrictionsResult {
                        data class OK(val value: List<Team>) : ReposAddTeamAccessRestrictionsResult

                        data class UnprocessableEntity(val value: ValidationError) : ReposAddTeamAccessRestrictionsResult
                    }

                    suspend fun reposAddTeamAccessRestrictions(
                        owner: String,
                        repo: String,
                        branch: String,
                        body: ReposAddTeamAccessRestrictionsBody? = null,
                    ): ReposAddTeamAccessRestrictionsResult

                    sealed interface ReposRemoveTeamAccessRestrictionsResult {
                        data class OK(val value: List<Team>) : ReposRemoveTeamAccessRestrictionsResult

                        data class UnprocessableEntity(val value: ValidationError) : ReposRemoveTeamAccessRestrictionsResult
                    }

                    suspend fun reposRemoveTeamAccessRestrictions(
                        owner: String,
                        repo: String,
                        branch: String,
                        body: ReposRemoveTeamAccessRestrictionsBody? = null,
                    ): ReposRemoveTeamAccessRestrictionsResult
                }

                interface Users {
                    @Serializable
                    @JvmInline
                    value class ReposAddUserAccessRestrictionsBody(val users: List<String>)


                    @Serializable
                    @JvmInline
                    value class ReposRemoveUserAccessRestrictionsBody(val users: List<String>)


                    @Serializable
                    @JvmInline
                    value class ReposSetUserAccessRestrictionsBody(val users: List<String>)

                    sealed interface ReposGetUsersWithAccessToProtectedBranchResult {
                        data class OK(val value: List<SimpleUser>) : ReposGetUsersWithAccessToProtectedBranchResult

                        data class NotFound(val value: BasicError) : ReposGetUsersWithAccessToProtectedBranchResult
                    }

                    suspend fun reposGetUsersWithAccessToProtectedBranch(
                        owner: String,
                        repo: String,
                        branch: String,
                    ): ReposGetUsersWithAccessToProtectedBranchResult

                    sealed interface ReposSetUserAccessRestrictionsResult {
                        data class OK(val value: List<SimpleUser>) : ReposSetUserAccessRestrictionsResult

                        data class UnprocessableEntity(val value: ValidationError) : ReposSetUserAccessRestrictionsResult
                    }

                    suspend fun reposSetUserAccessRestrictions(
                        owner: String,
                        repo: String,
                        branch: String,
                        body: ReposSetUserAccessRestrictionsBody,
                    ): ReposSetUserAccessRestrictionsResult

                    sealed interface ReposAddUserAccessRestrictionsResult {
                        data class OK(val value: List<SimpleUser>) : ReposAddUserAccessRestrictionsResult

                        data class UnprocessableEntity(val value: ValidationError) : ReposAddUserAccessRestrictionsResult
                    }

                    suspend fun reposAddUserAccessRestrictions(
                        owner: String,
                        repo: String,
                        branch: String,
                        body: ReposAddUserAccessRestrictionsBody,
                    ): ReposAddUserAccessRestrictionsResult

                    sealed interface ReposRemoveUserAccessRestrictionsResult {
                        data class OK(val value: List<SimpleUser>) : ReposRemoveUserAccessRestrictionsResult

                        data class UnprocessableEntity(val value: ValidationError) : ReposRemoveUserAccessRestrictionsResult
                    }

                    suspend fun reposRemoveUserAccessRestrictions(
                        owner: String,
                        repo: String,
                        branch: String,
                        body: ReposRemoveUserAccessRestrictionsBody,
                    ): ReposRemoveUserAccessRestrictionsResult
                }
            }
        }

        interface Rename {
            @Serializable
            @JvmInline
            value class ReposRenameBranchBody(@SerialName("new_name") val newName: String)

            sealed interface ReposRenameBranchResult {
                data class Created(val value: BranchWithProtection) : ReposRenameBranchResult

                data class Forbidden(val value: BasicError) : ReposRenameBranchResult

                data class NotFound(val value: BasicError) : ReposRenameBranchResult

                data class UnprocessableEntity(val value: ValidationError) : ReposRenameBranchResult
            }

            suspend fun reposRenameBranch(
                owner: String,
                repo: String,
                branch: String,
                body: ReposRenameBranchBody,
            ): ReposRenameBranchResult
        }
    }

    interface CheckRuns {
        val annotations: Repos.CheckRuns.Annotations

        val rerequest: Repos.CheckRuns.Rerequest

        @Serializable
        data class ChecksCreateBody(
            val name: String,
            @SerialName("head_sha") val headSha: String,
            @SerialName("details_url") val detailsUrl: String? = null,
            @SerialName("external_id") val externalId: String? = null,
            val status: Status? = null,
            @SerialName("started_at") val startedAt: LocalDateTime? = null,
            val conclusion: Conclusion? = null,
            @SerialName("completed_at") val completedAt: LocalDateTime? = null,
            val output: Output? = null,
            val actions: List<Actions>? = null,
        ) {
            @Serializable
            enum class Status {
                @SerialName("queued")
                Queued,
                @SerialName("in_progress")
                InProgress,
                @SerialName("completed")
                Completed,
                @SerialName("waiting")
                Waiting,
                @SerialName("requested")
                Requested,
                @SerialName("pending")
                Pending;
            }

            @Serializable
            enum class Conclusion {
                @SerialName("action_required")
                ActionRequired,
                @SerialName("cancelled")
                Cancelled,
                @SerialName("failure")
                Failure,
                @SerialName("neutral")
                Neutral,
                @SerialName("success")
                Success,
                @SerialName("skipped")
                Skipped,
                @SerialName("stale")
                Stale,
                @SerialName("timed_out")
                TimedOut;
            }

            @Serializable
            data class Output(
                val title: String,
                val summary: String,
                val text: String? = null,
                val annotations: List<Annotations>? = null,
                val images: List<Images>? = null,
            ) {
                @Serializable
                data class Annotations(
                    val path: String,
                    @SerialName("start_line") val startLine: Long,
                    @SerialName("end_line") val endLine: Long,
                    @SerialName("start_column") val startColumn: Long? = null,
                    @SerialName("end_column") val endColumn: Long? = null,
                    @SerialName("annotation_level") val annotationLevel: AnnotationLevel,
                    val message: String,
                    val title: String? = null,
                    @SerialName("raw_details") val rawDetails: String? = null,
                ) {
                    @Serializable
                    enum class AnnotationLevel {
                        @SerialName("notice") Notice, @SerialName("warning") Warning, @SerialName("failure") Failure;
                    }
                }

                @Serializable
                data class Images(val alt: String, @SerialName("image_url") val imageUrl: String, val caption: String? = null)
            }

            @Serializable
            data class Actions(val label: String, val description: String, val identifier: String)
        }


        @Serializable
        data class ChecksUpdateBody(
            val name: String? = null,
            @SerialName("details_url") val detailsUrl: String? = null,
            @SerialName("external_id") val externalId: String? = null,
            @SerialName("started_at") val startedAt: LocalDateTime? = null,
            val status: Status? = null,
            val conclusion: Conclusion? = null,
            @SerialName("completed_at") val completedAt: LocalDateTime? = null,
            val output: Output? = null,
            val actions: List<Actions>? = null,
        ) {
            @Serializable
            enum class Status {
                @SerialName("queued")
                Queued,
                @SerialName("in_progress")
                InProgress,
                @SerialName("completed")
                Completed,
                @SerialName("waiting")
                Waiting,
                @SerialName("requested")
                Requested,
                @SerialName("pending")
                Pending;
            }

            @Serializable
            enum class Conclusion {
                @SerialName("action_required")
                ActionRequired,
                @SerialName("cancelled")
                Cancelled,
                @SerialName("failure")
                Failure,
                @SerialName("neutral")
                Neutral,
                @SerialName("success")
                Success,
                @SerialName("skipped")
                Skipped,
                @SerialName("stale")
                Stale,
                @SerialName("timed_out")
                TimedOut;
            }

            @Serializable
            data class Output(
                val title: String? = null,
                val summary: String,
                val text: String? = null,
                val annotations: List<Annotations>? = null,
                val images: List<Images>? = null,
            ) {
                @Serializable
                data class Annotations(
                    val path: String,
                    @SerialName("start_line") val startLine: Long,
                    @SerialName("end_line") val endLine: Long,
                    @SerialName("start_column") val startColumn: Long? = null,
                    @SerialName("end_column") val endColumn: Long? = null,
                    @SerialName("annotation_level") val annotationLevel: AnnotationLevel,
                    val message: String,
                    val title: String? = null,
                    @SerialName("raw_details") val rawDetails: String? = null,
                ) {
                    @Serializable
                    enum class AnnotationLevel {
                        @SerialName("notice") Notice, @SerialName("warning") Warning, @SerialName("failure") Failure;
                    }
                }

                @Serializable
                data class Images(val alt: String, @SerialName("image_url") val imageUrl: String, val caption: String? = null)
            }

            @Serializable
            data class Actions(val label: String, val description: String, val identifier: String)
        }

        suspend fun checksCreate(
            owner: String,
            repo: String,
            body: ChecksCreateBody,
        ): CheckRun

        suspend fun checksGet(
            owner: String,
            repo: String,
            checkRunId: Long,
        ): CheckRun

        suspend fun checksUpdate(
            owner: String,
            repo: String,
            checkRunId: Long,
            body: ChecksUpdateBody,
        ): CheckRun

        interface Annotations {
            suspend fun checksListAnnotations(
                owner: String,
                repo: String,
                checkRunId: Long,
                page: Long = 1L,
                perPage: Long = 30L,
            ): List<CheckAnnotation>
        }

        interface Rerequest {
            sealed interface ChecksRerequestRunResult {
                data class Created(val value: EmptyObject) : ChecksRerequestRunResult

                data class Forbidden(val value: BasicError) : ChecksRerequestRunResult

                data class NotFound(val value: BasicError) : ChecksRerequestRunResult

                data class UnprocessableEntity(val value: BasicError) : ChecksRerequestRunResult
            }

            suspend fun checksRerequestRun(
                owner: String,
                repo: String,
                checkRunId: Long,
            ): ChecksRerequestRunResult
        }
    }

    interface CheckSuites {
        val preferences: Repos.CheckSuites.Preferences

        val checkRuns: Repos.CheckSuites.CheckRunsApi

        val rerequest: Repos.CheckSuites.Rerequest

        @Serializable
        @JvmInline
        value class ChecksCreateSuiteBody(@SerialName("head_sha") val headSha: String)

        sealed interface ChecksCreateSuiteResult {
            data class OK(val value: CheckSuite) : ChecksCreateSuiteResult

            data class Created(val value: CheckSuite) : ChecksCreateSuiteResult
        }

        suspend fun checksCreateSuite(
            owner: String,
            repo: String,
            body: ChecksCreateSuiteBody,
        ): ChecksCreateSuiteResult

        suspend fun checksGetSuite(
            owner: String,
            repo: String,
            checkSuiteId: Long,
        ): CheckSuite

        interface Preferences {
            @Serializable
            @JvmInline
            value class ChecksSetSuitesPreferencesBody(@SerialName("auto_trigger_checks") val autoTriggerChecks: List<AutoTriggerChecks>? = null) {
                @Serializable
                data class AutoTriggerChecks(@SerialName("app_id") val appId: Long, @Required val setting: Boolean)
            }

            suspend fun checksSetSuitesPreferences(
                owner: String,
                repo: String,
                body: ChecksSetSuitesPreferencesBody,
            ): CheckSuitePreference
        }

        interface CheckRunsApi {
            @Serializable
            data class ChecksListForSuiteResponse(
                @SerialName("total_count") val totalCount: Long,
                @SerialName("check_runs") val checkRuns: List<CheckRun>,
            )


            @Serializable
            enum class Filter {
                @SerialName("latest") Latest, @SerialName("all") All;
            }


            @Serializable
            enum class Status {
                @SerialName("queued") Queued, @SerialName("in_progress") InProgress, @SerialName("completed") Completed;
            }

            suspend fun checksListForSuite(
                owner: String,
                repo: String,
                checkSuiteId: Long,
                filter: Filter = Filter.Latest,
                page: Long = 1L,
                perPage: Long = 30L,
                checkName: String? = null,
                status: Status? = null,
            ): ChecksListForSuiteResponse
        }

        interface Rerequest {
            suspend fun checksRerequestSuite(
                owner: String,
                repo: String,
                checkSuiteId: Long,
            ): EmptyObject
        }
    }

    interface CodeScanning {
        val alerts: Repos.CodeScanning.Alerts

        val analyses: Repos.CodeScanning.Analyses

        val codeql: Repos.CodeScanning.Codeql

        val defaultSetup: Repos.CodeScanning.DefaultSetup

        val sarifs: Repos.CodeScanning.Sarifs

        interface Alerts {
            val autofix: Repos.CodeScanning.Alerts.Autofix

            val instances: Repos.CodeScanning.Alerts.Instances

            @Serializable
            data class CodeScanningGetAlertResponse(
                val code: String? = null,
                val message: String? = null,
                @SerialName("documentation_url") val documentationUrl: String? = null,
            )


            @Serializable
            data class CodeScanningListAlertsForRepoResponse(
                val code: String? = null,
                val message: String? = null,
                @SerialName("documentation_url") val documentationUrl: String? = null,
            )


            @Serializable
            data class CodeScanningUpdateAlertBody(
                val state: CodeScanningAlertSetState? = null,
                @SerialName("dismissed_reason") val dismissedReason: CodeScanningAlertDismissedReason? = null,
                @SerialName("dismissed_comment") val dismissedComment: CodeScanningAlertDismissedComment? = null,
                @SerialName("create_request") val createRequest: CodeScanningAlertCreateRequest? = null,
                val assignees: CodeScanningAlertAssignees? = null,
            )


            @Serializable
            data class CodeScanningUpdateAlertResponse(
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

            sealed interface CodeScanningListAlertsForRepoResult {
                data class OK(val value: List<CodeScanningAlertItemsResponse>) : CodeScanningListAlertsForRepoResult

                data object NotModified : CodeScanningListAlertsForRepoResult

                data class Forbidden(val value: BasicError) : CodeScanningListAlertsForRepoResult

                data class NotFound(val value: BasicError) : CodeScanningListAlertsForRepoResult

                data class ServiceUnavailable(val value: CodeScanningListAlertsForRepoResponse) : CodeScanningListAlertsForRepoResult
            }

            suspend fun codeScanningListAlertsForRepo(
                owner: String,
                repo: String,
                direction: Direction = Direction.Desc,
                page: Long = 1L,
                perPage: Long = 30L,
                sort: Sort = Sort.Created,
                after: String? = null,
                assignees: String? = null,
                before: String? = null,
                pr: Long? = null,
                ref: CodeScanningRef? = null,
                severity: CodeScanningAlertSeverity? = null,
                state: CodeScanningAlertStateQuery? = null,
                toolGuid: CodeScanningAnalysisToolGuid? = null,
                toolName: CodeScanningAnalysisToolName? = null,
            ): CodeScanningListAlertsForRepoResult

            sealed interface CodeScanningGetAlertResult {
                data class OK(val value: CodeScanningAlertResponse) : CodeScanningGetAlertResult

                data object NotModified : CodeScanningGetAlertResult

                data class Forbidden(val value: BasicError) : CodeScanningGetAlertResult

                data class NotFound(val value: BasicError) : CodeScanningGetAlertResult

                data class ServiceUnavailable(val value: CodeScanningGetAlertResponse) : CodeScanningGetAlertResult
            }

            suspend fun codeScanningGetAlert(
                owner: String,
                repo: String,
                alertNumber: AlertNumberRequest,
            ): CodeScanningGetAlertResult

            sealed interface CodeScanningUpdateAlertResult {
                data class OK(val value: CodeScanningAlertResponse) : CodeScanningUpdateAlertResult

                data class BadRequest(val value: BasicError) : CodeScanningUpdateAlertResult

                data class Forbidden(val value: BasicError) : CodeScanningUpdateAlertResult

                data class NotFound(val value: BasicError) : CodeScanningUpdateAlertResult

                data class ServiceUnavailable(val value: CodeScanningUpdateAlertResponse) : CodeScanningUpdateAlertResult
            }

            suspend fun codeScanningUpdateAlert(
                owner: String,
                repo: String,
                alertNumber: AlertNumberRequest,
                body: CodeScanningUpdateAlertBody,
            ): CodeScanningUpdateAlertResult

            interface Autofix {
                val commits: Repos.CodeScanning.Alerts.Autofix.CommitsApi

                @Serializable
                data class CodeScanningCreateAutofixResponse(
                    val code: String? = null,
                    val message: String? = null,
                    @SerialName("documentation_url") val documentationUrl: String? = null,
                )


                @Serializable
                data class CodeScanningGetAutofixResponse(
                    val code: String? = null,
                    val message: String? = null,
                    @SerialName("documentation_url") val documentationUrl: String? = null,
                )

                sealed interface CodeScanningGetAutofixResult {
                    data class OK(val value: CodeScanningAutofixResponse) : CodeScanningGetAutofixResult

                    data class BadRequest(val value: BasicError) : CodeScanningGetAutofixResult

                    data class Forbidden(val value: BasicError) : CodeScanningGetAutofixResult

                    data class NotFound(val value: BasicError) : CodeScanningGetAutofixResult

                    data class ServiceUnavailable(val value: CodeScanningGetAutofixResponse) : CodeScanningGetAutofixResult
                }

                suspend fun codeScanningGetAutofix(
                    owner: String,
                    repo: String,
                    alertNumber: AlertNumberRequest,
                ): CodeScanningGetAutofixResult

                sealed interface CodeScanningCreateAutofixResult {
                    data class OK(val value: CodeScanningAutofixResponse) : CodeScanningCreateAutofixResult

                    data class Accepted(val value: CodeScanningAutofixResponse) : CodeScanningCreateAutofixResult

                    data class BadRequest(val value: BasicError) : CodeScanningCreateAutofixResult

                    data class Forbidden(val value: BasicError) : CodeScanningCreateAutofixResult

                    data class NotFound(val value: BasicError) : CodeScanningCreateAutofixResult

                    data object UnprocessableEntity : CodeScanningCreateAutofixResult

                    data class ServiceUnavailable(val value: CodeScanningCreateAutofixResponse) : CodeScanningCreateAutofixResult
                }

                suspend fun codeScanningCreateAutofix(
                    owner: String,
                    repo: String,
                    alertNumber: AlertNumberRequest,
                ): CodeScanningCreateAutofixResult

                interface CommitsApi {
                    @Serializable
                    data class CodeScanningCommitAutofixResponse(
                        val code: String? = null,
                        val message: String? = null,
                        @SerialName("documentation_url") val documentationUrl: String? = null,
                    )

                    sealed interface CodeScanningCommitAutofixResult {
                        data class Created(val value: CodeScanningAutofixCommitsResponse) : CodeScanningCommitAutofixResult

                        data class BadRequest(val value: BasicError) : CodeScanningCommitAutofixResult

                        data class Forbidden(val value: BasicError) : CodeScanningCommitAutofixResult

                        data class NotFound(val value: BasicError) : CodeScanningCommitAutofixResult

                        data object UnprocessableEntity : CodeScanningCommitAutofixResult

                        data class ServiceUnavailable(val value: CodeScanningCommitAutofixResponse) : CodeScanningCommitAutofixResult
                    }

                    suspend fun codeScanningCommitAutofix(
                        owner: String,
                        repo: String,
                        alertNumber: AlertNumberRequest,
                        body: CodeScanningAutofixCommits? = null,
                    ): CodeScanningCommitAutofixResult
                }
            }

            interface Instances {
                @Serializable
                data class CodeScanningListAlertInstancesResponse(
                    val code: String? = null,
                    val message: String? = null,
                    @SerialName("documentation_url") val documentationUrl: String? = null,
                )

                sealed interface CodeScanningListAlertInstancesResult {
                    data class OK(val value: List<CodeScanningAlertInstanceList>) : CodeScanningListAlertInstancesResult

                    data class Forbidden(val value: BasicError) : CodeScanningListAlertInstancesResult

                    data class NotFound(val value: BasicError) : CodeScanningListAlertInstancesResult

                    data class ServiceUnavailable(val value: CodeScanningListAlertInstancesResponse) : CodeScanningListAlertInstancesResult
                }

                suspend fun codeScanningListAlertInstances(
                    owner: String,
                    repo: String,
                    alertNumber: AlertNumberRequest,
                    page: Long = 1L,
                    perPage: Long = 30L,
                    pr: Long? = null,
                    ref: CodeScanningRef? = null,
                ): CodeScanningListAlertInstancesResult
            }
        }

        interface Analyses {
            @Serializable
            data class CodeScanningDeleteAnalysisResponse(
                val code: String? = null,
                val message: String? = null,
                @SerialName("documentation_url") val documentationUrl: String? = null,
            )


            @Serializable
            data class CodeScanningGetAnalysisResponse(
                val code: String? = null,
                val message: String? = null,
                @SerialName("documentation_url") val documentationUrl: String? = null,
            )


            @Serializable
            data class CodeScanningListRecentAnalysesResponse(
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
                @SerialName("created") Created;
            }

            sealed interface CodeScanningListRecentAnalysesResult {
                data class OK(val value: List<CodeScanningAnalysisResponse>) : CodeScanningListRecentAnalysesResult

                data class Forbidden(val value: BasicError) : CodeScanningListRecentAnalysesResult

                data class NotFound(val value: BasicError) : CodeScanningListRecentAnalysesResult

                data class ServiceUnavailable(val value: CodeScanningListRecentAnalysesResponse) : CodeScanningListRecentAnalysesResult
            }

            suspend fun codeScanningListRecentAnalyses(
                owner: String,
                repo: String,
                direction: Direction = Direction.Desc,
                page: Long = 1L,
                perPage: Long = 30L,
                sort: Sort = Sort.Created,
                pr: Long? = null,
                ref: CodeScanningRef? = null,
                sarifId: CodeScanningAnalysisSarifId? = null,
                toolGuid: CodeScanningAnalysisToolGuid? = null,
                toolName: CodeScanningAnalysisToolName? = null,
            ): CodeScanningListRecentAnalysesResult

            sealed interface CodeScanningGetAnalysisResult {
                data class OK(val value: CodeScanningAnalysisResponse) : CodeScanningGetAnalysisResult

                data class Forbidden(val value: BasicError) : CodeScanningGetAnalysisResult

                data class NotFound(val value: BasicError) : CodeScanningGetAnalysisResult

                data class UnprocessableEntity(val value: BasicError) : CodeScanningGetAnalysisResult

                data class ServiceUnavailable(val value: CodeScanningGetAnalysisResponse) : CodeScanningGetAnalysisResult
            }

            suspend fun codeScanningGetAnalysis(
                owner: String,
                repo: String,
                analysisId: Long,
            ): CodeScanningGetAnalysisResult

            sealed interface CodeScanningDeleteAnalysisResult {
                data class OK(val value: CodeScanningAnalysisDeletionResponse) : CodeScanningDeleteAnalysisResult

                data class BadRequest(val value: BasicError) : CodeScanningDeleteAnalysisResult

                data class Forbidden(val value: BasicError) : CodeScanningDeleteAnalysisResult

                data class NotFound(val value: BasicError) : CodeScanningDeleteAnalysisResult

                data class ServiceUnavailable(val value: CodeScanningDeleteAnalysisResponse) : CodeScanningDeleteAnalysisResult
            }

            suspend fun codeScanningDeleteAnalysis(
                owner: String,
                repo: String,
                analysisId: Long,
                confirmDelete: String? = null,
            ): CodeScanningDeleteAnalysisResult
        }

        interface Codeql {
            val databases: Repos.CodeScanning.Codeql.Databases

            val variantAnalyses: Repos.CodeScanning.Codeql.VariantAnalyses

            interface Databases {
                @Serializable
                data class CodeScanningDeleteCodeqlDatabaseResponse(
                    val code: String? = null,
                    val message: String? = null,
                    @SerialName("documentation_url") val documentationUrl: String? = null,
                )


                @Serializable
                data class CodeScanningGetCodeqlDatabaseResponse(
                    val code: String? = null,
                    val message: String? = null,
                    @SerialName("documentation_url") val documentationUrl: String? = null,
                )


                @Serializable
                data class CodeScanningListCodeqlDatabasesResponse(
                    val code: String? = null,
                    val message: String? = null,
                    @SerialName("documentation_url") val documentationUrl: String? = null,
                )

                sealed interface CodeScanningListCodeqlDatabasesResult {
                    data class OK(val value: List<CodeScanningCodeqlDatabase>) : CodeScanningListCodeqlDatabasesResult

                    data class Forbidden(val value: BasicError) : CodeScanningListCodeqlDatabasesResult

                    data class NotFound(val value: BasicError) : CodeScanningListCodeqlDatabasesResult

                    data class ServiceUnavailable(val value: CodeScanningListCodeqlDatabasesResponse) : CodeScanningListCodeqlDatabasesResult
                }

                suspend fun codeScanningListCodeqlDatabases(
                    owner: String,
                    repo: String,
                ): CodeScanningListCodeqlDatabasesResult

                sealed interface CodeScanningGetCodeqlDatabaseResult {
                    data class OK(val value: CodeScanningCodeqlDatabase) : CodeScanningGetCodeqlDatabaseResult

                    data object Found : CodeScanningGetCodeqlDatabaseResult

                    data class Forbidden(val value: BasicError) : CodeScanningGetCodeqlDatabaseResult

                    data class NotFound(val value: BasicError) : CodeScanningGetCodeqlDatabaseResult

                    data class ServiceUnavailable(val value: CodeScanningGetCodeqlDatabaseResponse) : CodeScanningGetCodeqlDatabaseResult
                }

                suspend fun codeScanningGetCodeqlDatabase(
                    owner: String,
                    repo: String,
                    language: String,
                ): CodeScanningGetCodeqlDatabaseResult

                sealed interface CodeScanningDeleteCodeqlDatabaseResult {
                    data object NoContent : CodeScanningDeleteCodeqlDatabaseResult

                    data class Forbidden(val value: BasicError) : CodeScanningDeleteCodeqlDatabaseResult

                    data class NotFound(val value: BasicError) : CodeScanningDeleteCodeqlDatabaseResult

                    data class ServiceUnavailable(val value: CodeScanningDeleteCodeqlDatabaseResponse) : CodeScanningDeleteCodeqlDatabaseResult
                }

                suspend fun codeScanningDeleteCodeqlDatabase(
                    owner: String,
                    repo: String,
                    language: String,
                ): CodeScanningDeleteCodeqlDatabaseResult
            }

            interface VariantAnalyses {
                val repos: Repos.CodeScanning.Codeql.VariantAnalyses.ReposApi

                @Serializable
                data class CodeScanningCreateVariantAnalysisBody(
                    val language: CodeScanningVariantAnalysisLanguage,
                    @SerialName("query_pack") val queryPack: String,
                    val repositories: List<String>? = null,
                    @SerialName("repository_lists") val repositoryLists: List<String>? = null,
                    @SerialName("repository_owners") val repositoryOwners: List<String>? = null,
                )


                @Serializable
                data class CodeScanningCreateVariantAnalysisResponse(
                    val code: String? = null,
                    val message: String? = null,
                    @SerialName("documentation_url") val documentationUrl: String? = null,
                )


                @Serializable
                data class CodeScanningGetVariantAnalysisResponse(
                    val code: String? = null,
                    val message: String? = null,
                    @SerialName("documentation_url") val documentationUrl: String? = null,
                )

                sealed interface CodeScanningCreateVariantAnalysisResult {
                    data class Created(val value: CodeScanningVariantAnalysis) : CodeScanningCreateVariantAnalysisResult

                    data class NotFound(val value: BasicError) : CodeScanningCreateVariantAnalysisResult

                    data class UnprocessableEntity(val value: BasicError) : CodeScanningCreateVariantAnalysisResult

                    data class ServiceUnavailable(val value: CodeScanningCreateVariantAnalysisResponse) : CodeScanningCreateVariantAnalysisResult
                }

                suspend fun codeScanningCreateVariantAnalysis(
                    owner: String,
                    repo: String,
                    body: CodeScanningCreateVariantAnalysisBody,
                ): CodeScanningCreateVariantAnalysisResult

                sealed interface CodeScanningGetVariantAnalysisResult {
                    data class OK(val value: CodeScanningVariantAnalysis) : CodeScanningGetVariantAnalysisResult

                    data class NotFound(val value: BasicError) : CodeScanningGetVariantAnalysisResult

                    data class ServiceUnavailable(val value: CodeScanningGetVariantAnalysisResponse) : CodeScanningGetVariantAnalysisResult
                }

                suspend fun codeScanningGetVariantAnalysis(
                    owner: String,
                    repo: String,
                    codeqlVariantAnalysisId: Long,
                ): CodeScanningGetVariantAnalysisResult

                interface ReposApi {
                    @Serializable
                    data class CodeScanningGetVariantAnalysisRepoTaskResponse(
                        val code: String? = null,
                        val message: String? = null,
                        @SerialName("documentation_url") val documentationUrl: String? = null,
                    )

                    sealed interface CodeScanningGetVariantAnalysisRepoTaskResult {
                        data class OK(val value: CodeScanningVariantAnalysisRepoTask) : CodeScanningGetVariantAnalysisRepoTaskResult

                        data class NotFound(val value: BasicError) : CodeScanningGetVariantAnalysisRepoTaskResult

                        data class ServiceUnavailable(val value: CodeScanningGetVariantAnalysisRepoTaskResponse) : CodeScanningGetVariantAnalysisRepoTaskResult
                    }

                    suspend fun codeScanningGetVariantAnalysisRepoTask(
                        owner: String,
                        repo: String,
                        codeqlVariantAnalysisId: Long,
                        repoOwner: String,
                        repoName: String,
                    ): CodeScanningGetVariantAnalysisRepoTaskResult
                }
            }
        }

        interface DefaultSetup {
            @Serializable
            data class CodeScanningGetDefaultSetupResponse(
                val code: String? = null,
                val message: String? = null,
                @SerialName("documentation_url") val documentationUrl: String? = null,
            )


            @Serializable
            data class CodeScanningUpdateDefaultSetupResponse(
                val code: String? = null,
                val message: String? = null,
                @SerialName("documentation_url") val documentationUrl: String? = null,
            )

            sealed interface CodeScanningGetDefaultSetupResult {
                data class OK(val value: CodeScanningDefaultSetup) : CodeScanningGetDefaultSetupResult

                data class Forbidden(val value: BasicError) : CodeScanningGetDefaultSetupResult

                data class NotFound(val value: BasicError) : CodeScanningGetDefaultSetupResult

                data class ServiceUnavailable(val value: CodeScanningGetDefaultSetupResponse) : CodeScanningGetDefaultSetupResult
            }

            suspend fun codeScanningGetDefaultSetup(
                owner: String,
                repo: String,
            ): CodeScanningGetDefaultSetupResult

            sealed interface CodeScanningUpdateDefaultSetupResult {
                data class OK(val value: EmptyObject) : CodeScanningUpdateDefaultSetupResult

                data class Accepted(val value: CodeScanningDefaultSetupUpdateResponse) : CodeScanningUpdateDefaultSetupResult

                data class Forbidden(val value: BasicError) : CodeScanningUpdateDefaultSetupResult

                data class NotFound(val value: BasicError) : CodeScanningUpdateDefaultSetupResult

                data class Conflict(val value: BasicError) : CodeScanningUpdateDefaultSetupResult

                data class UnprocessableEntity(val value: BasicError) : CodeScanningUpdateDefaultSetupResult

                data class ServiceUnavailable(val value: CodeScanningUpdateDefaultSetupResponse) : CodeScanningUpdateDefaultSetupResult
            }

            suspend fun codeScanningUpdateDefaultSetup(
                owner: String,
                repo: String,
                body: CodeScanningDefaultSetupUpdate,
            ): CodeScanningUpdateDefaultSetupResult
        }

        interface Sarifs {
            @Serializable
            data class CodeScanningGetSarifResponse(
                val code: String? = null,
                val message: String? = null,
                @SerialName("documentation_url") val documentationUrl: String? = null,
            )


            @Serializable
            data class CodeScanningUploadSarifBody(
                @SerialName("commit_sha") val commitSha: CodeScanningAnalysisCommitSha,
                val ref: CodeScanningRefFull,
                val sarif: CodeScanningAnalysisSarifFile,
                @SerialName("checkout_uri") val checkoutUri: String? = null,
                @SerialName("started_at") val startedAt: LocalDateTime? = null,
                @SerialName("tool_name") val toolName: String? = null,
                val validate: Boolean? = null,
            )


            @Serializable
            data class CodeScanningUploadSarifResponse(
                val code: String? = null,
                val message: String? = null,
                @SerialName("documentation_url") val documentationUrl: String? = null,
            )

            sealed interface CodeScanningUploadSarifResult {
                data class Accepted(val value: CodeScanningSarifsReceiptResponse) : CodeScanningUploadSarifResult

                data object BadRequest : CodeScanningUploadSarifResult

                data class Forbidden(val value: BasicError) : CodeScanningUploadSarifResult

                data class NotFound(val value: BasicError) : CodeScanningUploadSarifResult

                data object PayloadTooLarge : CodeScanningUploadSarifResult

                data class ServiceUnavailable(val value: CodeScanningUploadSarifResponse) : CodeScanningUploadSarifResult
            }

            suspend fun codeScanningUploadSarif(
                owner: String,
                repo: String,
                body: CodeScanningUploadSarifBody,
            ): CodeScanningUploadSarifResult

            sealed interface CodeScanningGetSarifResult {
                data class OK(val value: CodeScanningSarifsStatusResponse) : CodeScanningGetSarifResult

                data class Forbidden(val value: BasicError) : CodeScanningGetSarifResult

                data object NotFound : CodeScanningGetSarifResult

                data class ServiceUnavailable(val value: CodeScanningGetSarifResponse) : CodeScanningGetSarifResult
            }

            suspend fun codeScanningGetSarif(
                owner: String,
                repo: String,
                sarifId: String,
            ): CodeScanningGetSarifResult
        }
    }

    interface CodeSecurityConfiguration {
        sealed interface CodeSecurityGetConfigurationForRepositoryResult {
            data class OK(val value: CodeSecurityConfigurationForRepository) : CodeSecurityGetConfigurationForRepositoryResult

            data object NoContent : CodeSecurityGetConfigurationForRepositoryResult

            data object NotModified : CodeSecurityGetConfigurationForRepositoryResult

            data class Forbidden(val value: BasicError) : CodeSecurityGetConfigurationForRepositoryResult

            data class NotFound(val value: BasicError) : CodeSecurityGetConfigurationForRepositoryResult
        }

        suspend fun codeSecurityGetConfigurationForRepository(
            owner: String,
            repo: String,
        ): CodeSecurityGetConfigurationForRepositoryResult
    }

    interface Codeowners {
        val errors: Repos.Codeowners.Errors

        interface Errors {
            sealed interface ReposCodeownersErrorsResult {
                data class OK(val value: CodeownersErrors) : ReposCodeownersErrorsResult

                data object NotFound : ReposCodeownersErrorsResult
            }

            suspend fun reposCodeownersErrors(
                owner: String,
                repo: String,
                ref: String? = null,
            ): ReposCodeownersErrorsResult
        }
    }

    interface Codespaces {
        val devcontainers: Repos.Codespaces.Devcontainers

        val machines: Repos.Codespaces.Machines

        val new: Repos.Codespaces.New

        val permissionsCheck: Repos.Codespaces.PermissionsCheck

        val secrets: Repos.Codespaces.Secrets

        @Serializable
        data class CodespacesCreateWithRepoForAuthenticatedUserBody(
            val ref: String? = null,
            val location: String? = null,
            val geo: Geo? = null,
            @SerialName("client_ip") val clientIp: String? = null,
            val machine: String? = null,
            @SerialName("devcontainer_path") val devcontainerPath: String? = null,
            @SerialName("multi_repo_permissions_opt_out") val multiRepoPermissionsOptOut: Boolean? = null,
            @SerialName("working_directory") val workingDirectory: String? = null,
            @SerialName("idle_timeout_minutes") val idleTimeoutMinutes: Long? = null,
            @SerialName("display_name") val displayName: String? = null,
            @SerialName("retention_period_minutes") val retentionPeriodMinutes: Long? = null,
        ) {
            @Serializable
            enum class Geo {
                EuropeWest, SoutheastAsia, UsEast, UsWest;
            }
        }


        @Serializable
        data class CodespacesCreateWithRepoForAuthenticatedUserResponse(
            val code: String? = null,
            val message: String? = null,
            @SerialName("documentation_url") val documentationUrl: String? = null,
        )


        @Serializable
        data class CodespacesListInRepositoryForAuthenticatedUserResponse(
            @SerialName("total_count") val totalCount: Long,
            val codespaces: List<Codespace>,
        )

        sealed interface CodespacesListInRepositoryForAuthenticatedUserResult {
            data class OK(val value: CodespacesListInRepositoryForAuthenticatedUserResponse) : CodespacesListInRepositoryForAuthenticatedUserResult

            data class Unauthorized(val value: BasicError) : CodespacesListInRepositoryForAuthenticatedUserResult

            data class Forbidden(val value: BasicError) : CodespacesListInRepositoryForAuthenticatedUserResult

            data class NotFound(val value: BasicError) : CodespacesListInRepositoryForAuthenticatedUserResult

            data class InternalServerError(val value: BasicError) : CodespacesListInRepositoryForAuthenticatedUserResult
        }

        suspend fun codespacesListInRepositoryForAuthenticatedUser(
            owner: String,
            repo: String,
            page: Long = 1L,
            perPage: Long = 30L,
        ): CodespacesListInRepositoryForAuthenticatedUserResult

        sealed interface CodespacesCreateWithRepoForAuthenticatedUserResult {
            data class Created(val value: Codespace) : CodespacesCreateWithRepoForAuthenticatedUserResult

            data class Accepted(val value: Codespace) : CodespacesCreateWithRepoForAuthenticatedUserResult

            data class BadRequest(val value: BasicError) : CodespacesCreateWithRepoForAuthenticatedUserResult

            data class Unauthorized(val value: BasicError) : CodespacesCreateWithRepoForAuthenticatedUserResult

            data class Forbidden(val value: BasicError) : CodespacesCreateWithRepoForAuthenticatedUserResult

            data class NotFound(val value: BasicError) : CodespacesCreateWithRepoForAuthenticatedUserResult

            data class ServiceUnavailable(val value: CodespacesCreateWithRepoForAuthenticatedUserResponse) : CodespacesCreateWithRepoForAuthenticatedUserResult
        }

        suspend fun codespacesCreateWithRepoForAuthenticatedUser(
            owner: String,
            repo: String,
            body: CodespacesCreateWithRepoForAuthenticatedUserBody,
        ): CodespacesCreateWithRepoForAuthenticatedUserResult

        interface Devcontainers {
            @Serializable
            data class CodespacesListDevcontainersInRepositoryForAuthenticatedUserResponse(
                @SerialName("total_count") val totalCount: Long,
                val devcontainers: List<Devcontainers>,
            ) {
                @Serializable
                data class Devcontainers(
                    val path: String,
                    val name: String? = null,
                    @SerialName("display_name") val displayName: String? = null,
                )
            }

            sealed interface CodespacesListDevcontainersInRepositoryForAuthenticatedUserResult {
                data class OK(val value: CodespacesListDevcontainersInRepositoryForAuthenticatedUserResponse) : CodespacesListDevcontainersInRepositoryForAuthenticatedUserResult

                data class BadRequest(val value: BasicError) : CodespacesListDevcontainersInRepositoryForAuthenticatedUserResult

                data class Unauthorized(val value: BasicError) : CodespacesListDevcontainersInRepositoryForAuthenticatedUserResult

                data class Forbidden(val value: BasicError) : CodespacesListDevcontainersInRepositoryForAuthenticatedUserResult

                data class NotFound(val value: BasicError) : CodespacesListDevcontainersInRepositoryForAuthenticatedUserResult

                data class InternalServerError(val value: BasicError) : CodespacesListDevcontainersInRepositoryForAuthenticatedUserResult
            }

            suspend fun codespacesListDevcontainersInRepositoryForAuthenticatedUser(
                owner: String,
                repo: String,
                page: Long = 1L,
                perPage: Long = 30L,
            ): CodespacesListDevcontainersInRepositoryForAuthenticatedUserResult
        }

        interface Machines {
            @Serializable
            data class CodespacesRepoMachinesForAuthenticatedUserResponse(
                @SerialName("total_count") val totalCount: Long,
                val machines: List<CodespaceMachine>,
            )

            sealed interface CodespacesRepoMachinesForAuthenticatedUserResult {
                data class OK(val value: CodespacesRepoMachinesForAuthenticatedUserResponse) : CodespacesRepoMachinesForAuthenticatedUserResult

                data object NotModified : CodespacesRepoMachinesForAuthenticatedUserResult

                data class Unauthorized(val value: BasicError) : CodespacesRepoMachinesForAuthenticatedUserResult

                data class Forbidden(val value: BasicError) : CodespacesRepoMachinesForAuthenticatedUserResult

                data class NotFound(val value: BasicError) : CodespacesRepoMachinesForAuthenticatedUserResult

                data class InternalServerError(val value: BasicError) : CodespacesRepoMachinesForAuthenticatedUserResult
            }

            suspend fun codespacesRepoMachinesForAuthenticatedUser(
                owner: String,
                repo: String,
                clientIp: String? = null,
                location: String? = null,
                ref: String? = null,
            ): CodespacesRepoMachinesForAuthenticatedUserResult
        }

        interface New {
            @Serializable
            data class CodespacesPreFlightWithRepoForAuthenticatedUserResponse(
                @SerialName("billable_owner") val billableOwner: SimpleUser? = null,
                val defaults: Defaults? = null,
            ) {
                @Serializable
                data class Defaults(val location: String, @SerialName("devcontainer_path") val devcontainerPath: String?)
            }

            sealed interface CodespacesPreFlightWithRepoForAuthenticatedUserResult {
                data class OK(val value: CodespacesPreFlightWithRepoForAuthenticatedUserResponse) : CodespacesPreFlightWithRepoForAuthenticatedUserResult

                data class Unauthorized(val value: BasicError) : CodespacesPreFlightWithRepoForAuthenticatedUserResult

                data class Forbidden(val value: BasicError) : CodespacesPreFlightWithRepoForAuthenticatedUserResult

                data class NotFound(val value: BasicError) : CodespacesPreFlightWithRepoForAuthenticatedUserResult
            }

            suspend fun codespacesPreFlightWithRepoForAuthenticatedUser(
                owner: String,
                repo: String,
                clientIp: String? = null,
                ref: String? = null,
            ): CodespacesPreFlightWithRepoForAuthenticatedUserResult
        }

        interface PermissionsCheck {
            @Serializable
            data class CodespacesCheckPermissionsForDevcontainerResponse(
                val code: String? = null,
                val message: String? = null,
                @SerialName("documentation_url") val documentationUrl: String? = null,
            )

            sealed interface CodespacesCheckPermissionsForDevcontainerResult {
                data class OK(val value: CodespacesPermissionsCheckForDevcontainer) : CodespacesCheckPermissionsForDevcontainerResult

                data class Unauthorized(val value: BasicError) : CodespacesCheckPermissionsForDevcontainerResult

                data class Forbidden(val value: BasicError) : CodespacesCheckPermissionsForDevcontainerResult

                data class NotFound(val value: BasicError) : CodespacesCheckPermissionsForDevcontainerResult

                data class UnprocessableEntity(val value: ValidationError) : CodespacesCheckPermissionsForDevcontainerResult

                data class ServiceUnavailable(val value: CodespacesCheckPermissionsForDevcontainerResponse) : CodespacesCheckPermissionsForDevcontainerResult
            }

            suspend fun codespacesCheckPermissionsForDevcontainer(
                owner: String,
                repo: String,
                devcontainerPath: String,
                ref: String,
            ): CodespacesCheckPermissionsForDevcontainerResult
        }

        interface Secrets {
            val publicKey: Repos.Codespaces.Secrets.PublicKey

            @Serializable
            data class CodespacesCreateOrUpdateRepoSecretBody(
                @SerialName("encrypted_value") val encryptedValue: String? = null,
                @SerialName("key_id") val keyId: String? = null,
            )


            @Serializable
            data class CodespacesListRepoSecretsResponse(
                @SerialName("total_count") val totalCount: Long,
                val secrets: List<RepoCodespacesSecret>,
            )

            suspend fun codespacesListRepoSecrets(
                owner: String,
                repo: String,
                page: Long = 1L,
                perPage: Long = 30L,
            ): CodespacesListRepoSecretsResponse

            suspend fun codespacesGetRepoSecret(
                owner: String,
                repo: String,
                secretName: String,
            ): RepoCodespacesSecret

            sealed interface CodespacesCreateOrUpdateRepoSecretResult {
                data class Created(val value: EmptyObject) : CodespacesCreateOrUpdateRepoSecretResult

                data object NoContent : CodespacesCreateOrUpdateRepoSecretResult
            }

            suspend fun codespacesCreateOrUpdateRepoSecret(
                owner: String,
                repo: String,
                secretName: String,
                body: CodespacesCreateOrUpdateRepoSecretBody,
            ): CodespacesCreateOrUpdateRepoSecretResult

            suspend fun codespacesDeleteRepoSecret(
                owner: String,
                repo: String,
                secretName: String,
            ): Unit

            interface PublicKey {
                suspend fun codespacesGetRepoPublicKey(
                    owner: String,
                    repo: String,
                ): CodespacesPublicKey
            }
        }
    }

    interface Collaborators {
        val permission: Repos.Collaborators.Permission

        @Serializable
        enum class Affiliation {
            @SerialName("outside") Outside, @SerialName("direct") Direct, @SerialName("all") All;
        }


        @Serializable
        @JvmInline
        value class ReposAddCollaboratorBody(val permission: String? = null)


        @Serializable
        enum class ReposListCollaboratorsPermission {
            @SerialName("pull")
            Pull,
            @SerialName("triage")
            Triage,
            @SerialName("push")
            Push,
            @SerialName("maintain")
            Maintain,
            @SerialName("admin")
            Admin;
        }

        sealed interface ReposListCollaboratorsResult {
            data class OK(val value: List<Collaborator>) : ReposListCollaboratorsResult

            data class NotFound(val value: BasicError) : ReposListCollaboratorsResult
        }

        suspend fun reposListCollaborators(
            owner: String,
            repo: String,
            affiliation: Affiliation = Affiliation.All,
            page: Long = 1L,
            perPage: Long = 30L,
            permission: ReposListCollaboratorsPermission? = null,
        ): ReposListCollaboratorsResult

        sealed interface ReposCheckCollaboratorResult {
            data object NoContent : ReposCheckCollaboratorResult

            data object NotFound : ReposCheckCollaboratorResult
        }

        suspend fun reposCheckCollaborator(
            owner: String,
            repo: String,
            username: String,
        ): ReposCheckCollaboratorResult

        sealed interface ReposAddCollaboratorResult {
            data class Created(val value: RepositoryInvitation) : ReposAddCollaboratorResult

            data object NoContent : ReposAddCollaboratorResult

            data class Forbidden(val value: BasicError) : ReposAddCollaboratorResult

            data class UnprocessableEntity(val value: ValidationError) : ReposAddCollaboratorResult
        }

        suspend fun reposAddCollaborator(
            owner: String,
            repo: String,
            username: String,
            body: ReposAddCollaboratorBody? = null,
        ): ReposAddCollaboratorResult

        sealed interface ReposRemoveCollaboratorResult {
            data object NoContent : ReposRemoveCollaboratorResult

            data class Forbidden(val value: BasicError) : ReposRemoveCollaboratorResult

            data class UnprocessableEntity(val value: ValidationError) : ReposRemoveCollaboratorResult
        }

        suspend fun reposRemoveCollaborator(
            owner: String,
            repo: String,
            username: String,
        ): ReposRemoveCollaboratorResult

        interface Permission {
            sealed interface ReposGetCollaboratorPermissionLevelResult {
                data class OK(val value: RepositoryCollaboratorPermission) : ReposGetCollaboratorPermissionLevelResult

                data class NotFound(val value: BasicError) : ReposGetCollaboratorPermissionLevelResult
            }

            suspend fun reposGetCollaboratorPermissionLevel(
                owner: String,
                repo: String,
                username: String,
            ): ReposGetCollaboratorPermissionLevelResult
        }
    }

    interface Comments {
        val reactions: Repos.Comments.Reactions

        @Serializable
        @JvmInline
        value class ReposUpdateCommitCommentBody(val body: String)

        suspend fun reposListCommitCommentsForRepo(
            owner: String,
            repo: String,
            page: Long = 1L,
            perPage: Long = 30L,
        ): List<CommitComment>

        sealed interface ReposGetCommitCommentResult {
            data class OK(val value: CommitComment) : ReposGetCommitCommentResult

            data class NotFound(val value: BasicError) : ReposGetCommitCommentResult
        }

        suspend fun reposGetCommitComment(
            owner: String,
            repo: String,
            commentId: Long,
        ): ReposGetCommitCommentResult

        sealed interface ReposDeleteCommitCommentResult {
            data object NoContent : ReposDeleteCommitCommentResult

            data class NotFound(val value: BasicError) : ReposDeleteCommitCommentResult
        }

        suspend fun reposDeleteCommitComment(
            owner: String,
            repo: String,
            commentId: Long,
        ): ReposDeleteCommitCommentResult

        sealed interface ReposUpdateCommitCommentResult {
            data class OK(val value: CommitComment) : ReposUpdateCommitCommentResult

            data class NotFound(val value: BasicError) : ReposUpdateCommitCommentResult
        }

        suspend fun reposUpdateCommitComment(
            owner: String,
            repo: String,
            commentId: Long,
            body: ReposUpdateCommitCommentBody,
        ): ReposUpdateCommitCommentResult

        interface Reactions {
            @Serializable
            enum class Content {
                @SerialName("+1")
                `+1`,
                @SerialName("-1")
                `-1`,
                @SerialName("laugh")
                Laugh,
                @SerialName("confused")
                Confused,
                @SerialName("heart")
                Heart,
                @SerialName("hooray")
                Hooray,
                @SerialName("rocket")
                Rocket,
                @SerialName("eyes")
                Eyes;
            }


            @Serializable
            @JvmInline
            value class ReactionsCreateForCommitCommentBody(val content: Content) {
                @Serializable
                enum class Content {
                    @SerialName("+1")
                    `+1`,
                    @SerialName("-1")
                    `-1`,
                    @SerialName("laugh")
                    Laugh,
                    @SerialName("confused")
                    Confused,
                    @SerialName("heart")
                    Heart,
                    @SerialName("hooray")
                    Hooray,
                    @SerialName("rocket")
                    Rocket,
                    @SerialName("eyes")
                    Eyes;
                }
            }

            sealed interface ReactionsListForCommitCommentResult {
                data class OK(val value: List<Reaction>) : ReactionsListForCommitCommentResult

                data class NotFound(val value: BasicError) : ReactionsListForCommitCommentResult
            }

            suspend fun reactionsListForCommitComment(
                owner: String,
                repo: String,
                commentId: Long,
                page: Long = 1L,
                perPage: Long = 30L,
                content: Content? = null,
            ): ReactionsListForCommitCommentResult

            sealed interface ReactionsCreateForCommitCommentResult {
                data class OK(val value: Reaction) : ReactionsCreateForCommitCommentResult

                data class Created(val value: Reaction) : ReactionsCreateForCommitCommentResult

                data class UnprocessableEntity(val value: ValidationError) : ReactionsCreateForCommitCommentResult
            }

            suspend fun reactionsCreateForCommitComment(
                owner: String,
                repo: String,
                commentId: Long,
                body: ReactionsCreateForCommitCommentBody,
            ): ReactionsCreateForCommitCommentResult

            suspend fun reactionsDeleteForCommitComment(
                owner: String,
                repo: String,
                commentId: Long,
                reactionId: Long,
            ): Unit
        }
    }

    interface Commits {
        val branchesWhereHead: Repos.Commits.BranchesWhereHead

        val comments: Repos.Commits.CommentsApi

        val pulls: Repos.Commits.PullsApi

        val checkRuns: Repos.Commits.CheckRunsApi

        val checkSuites: Repos.Commits.CheckSuitesApi

        val status: Repos.Commits.Status

        val statuses: Repos.Commits.StatusesApi

        @Serializable
        data class ReposGetCommitResponse(
            val code: String? = null,
            val message: String? = null,
            @SerialName("documentation_url") val documentationUrl: String? = null,
        )

        sealed interface ReposListCommitsResult {
            data class OK(val value: List<Commit>) : ReposListCommitsResult

            data class BadRequest(val value: BasicError) : ReposListCommitsResult

            data class NotFound(val value: BasicError) : ReposListCommitsResult

            data class Conflict(val value: BasicError) : ReposListCommitsResult

            data class InternalServerError(val value: BasicError) : ReposListCommitsResult
        }

        suspend fun reposListCommits(
            owner: String,
            repo: String,
            page: Long = 1L,
            perPage: Long = 30L,
            author: String? = null,
            committer: String? = null,
            path: String? = null,
            sha: String? = null,
            since: LocalDateTime? = null,
            until: LocalDateTime? = null,
        ): ReposListCommitsResult

        sealed interface ReposGetCommitResult {
            data class OK(val value: Commit) : ReposGetCommitResult

            data class NotFound(val value: BasicError) : ReposGetCommitResult

            data class Conflict(val value: BasicError) : ReposGetCommitResult

            data class UnprocessableEntity(val value: ValidationError) : ReposGetCommitResult

            data class InternalServerError(val value: BasicError) : ReposGetCommitResult

            data class ServiceUnavailable(val value: ReposGetCommitResponse) : ReposGetCommitResult
        }

        suspend fun reposGetCommit(
            owner: String,
            repo: String,
            ref: String,
            page: Long = 1L,
            perPage: Long = 30L,
        ): ReposGetCommitResult

        interface BranchesWhereHead {
            sealed interface ReposListBranchesForHeadCommitResult {
                data class OK(val value: List<BranchShort>) : ReposListBranchesForHeadCommitResult

                data class Conflict(val value: BasicError) : ReposListBranchesForHeadCommitResult

                data class UnprocessableEntity(val value: ValidationError) : ReposListBranchesForHeadCommitResult
            }

            suspend fun reposListBranchesForHeadCommit(
                owner: String,
                repo: String,
                commitSha: String,
            ): ReposListBranchesForHeadCommitResult
        }

        interface CommentsApi {
            @Serializable
            data class ReposCreateCommitCommentBody(
                val body: String,
                val path: String? = null,
                val position: Long? = null,
                val line: Long? = null,
            )

            suspend fun reposListCommentsForCommit(
                owner: String,
                repo: String,
                commitSha: String,
                page: Long = 1L,
                perPage: Long = 30L,
            ): List<CommitComment>

            sealed interface ReposCreateCommitCommentResult {
                data class Created(val value: CommitComment) : ReposCreateCommitCommentResult

                data class Forbidden(val value: BasicError) : ReposCreateCommitCommentResult

                data class UnprocessableEntity(val value: ValidationError) : ReposCreateCommitCommentResult
            }

            suspend fun reposCreateCommitComment(
                owner: String,
                repo: String,
                commitSha: String,
                body: ReposCreateCommitCommentBody,
            ): ReposCreateCommitCommentResult
        }

        interface PullsApi {
            sealed interface ReposListPullRequestsAssociatedWithCommitResult {
                data class OK(val value: List<PullRequestSimple>) : ReposListPullRequestsAssociatedWithCommitResult

                data class Conflict(val value: BasicError) : ReposListPullRequestsAssociatedWithCommitResult
            }

            suspend fun reposListPullRequestsAssociatedWithCommit(
                owner: String,
                repo: String,
                commitSha: String,
                page: Long = 1L,
                perPage: Long = 30L,
            ): ReposListPullRequestsAssociatedWithCommitResult
        }

        interface CheckRunsApi {
            @Serializable
            data class ChecksListForRefResponse(
                @SerialName("total_count") val totalCount: Long,
                @SerialName("check_runs") val checkRuns: List<CheckRun>,
            )


            @Serializable
            enum class ChecksListForRefStatus {
                @SerialName("queued") Queued, @SerialName("in_progress") InProgress, @SerialName("completed") Completed;
            }


            @Serializable
            enum class Filter {
                @SerialName("latest") Latest, @SerialName("all") All;
            }

            suspend fun checksListForRef(
                owner: String,
                repo: String,
                ref: String,
                filter: Filter = Filter.Latest,
                page: Long = 1L,
                perPage: Long = 30L,
                appId: Long? = null,
                checkName: String? = null,
                status: ChecksListForRefStatus? = null,
            ): ChecksListForRefResponse
        }

        interface CheckSuitesApi {
            @Serializable
            data class ChecksListSuitesForRefResponse(
                @SerialName("total_count") val totalCount: Long,
                @SerialName("check_suites") val checkSuites: List<CheckSuite>,
            )

            suspend fun checksListSuitesForRef(
                owner: String,
                repo: String,
                ref: String,
                page: Long = 1L,
                perPage: Long = 30L,
                appId: Long? = null,
                checkName: String? = null,
            ): ChecksListSuitesForRefResponse
        }

        interface Status {
            sealed interface ReposGetCombinedStatusForRefResult {
                data class OK(val value: CombinedCommitStatus) : ReposGetCombinedStatusForRefResult

                data class NotFound(val value: BasicError) : ReposGetCombinedStatusForRefResult
            }

            suspend fun reposGetCombinedStatusForRef(
                owner: String,
                repo: String,
                ref: String,
                page: Long = 1L,
                perPage: Long = 30L,
            ): ReposGetCombinedStatusForRefResult
        }

        interface StatusesApi {
            sealed interface ReposListCommitStatusesForRefResult {
                data class OK(val value: List<Status>) : ReposListCommitStatusesForRefResult

                data class MovedPermanently(val value: BasicError) : ReposListCommitStatusesForRefResult
            }

            suspend fun reposListCommitStatusesForRef(
                owner: String,
                repo: String,
                ref: String,
                page: Long = 1L,
                perPage: Long = 30L,
            ): ReposListCommitStatusesForRefResult
        }
    }

    interface Community {
        val profile: Repos.Community.Profile

        interface Profile {
            suspend fun reposGetCommunityProfileMetrics(
                owner: String,
                repo: String,
            ): CommunityProfile
        }
    }

    interface Compare {
        @Serializable
        data class ReposCompareCommitsResponse(
            val code: String? = null,
            val message: String? = null,
            @SerialName("documentation_url") val documentationUrl: String? = null,
        )

        sealed interface ReposCompareCommitsResult {
            data class OK(val value: CommitComparison) : ReposCompareCommitsResult

            data class NotFound(val value: BasicError) : ReposCompareCommitsResult

            data class InternalServerError(val value: BasicError) : ReposCompareCommitsResult

            data class ServiceUnavailable(val value: ReposCompareCommitsResponse) : ReposCompareCommitsResult
        }

        suspend fun reposCompareCommits(
            owner: String,
            repo: String,
            basehead: String,
            page: Long = 1L,
            perPage: Long = 30L,
        ): ReposCompareCommitsResult
    }

    interface Contents {
        @Serializable
        data class ReposCreateOrUpdateFileContentsBody(
            val message: String,
            val content: String,
            val sha: String? = null,
            val branch: String? = null,
            val committer: Committer? = null,
            val author: Author? = null,
        ) {
            @Serializable
            data class Committer(val name: String, val email: String, val date: String? = null)

            @Serializable
            data class Author(val name: String, val email: String, val date: String? = null)
        }


        @Serializable(with = ReposCreateOrUpdateFileContentsResponse.Serializer::class)
        sealed interface ReposCreateOrUpdateFileContentsResponse {
            @Serializable
            @JvmInline
            value class CaseBasicError(val value: BasicError) : ReposCreateOrUpdateFileContentsResponse

            @Serializable
            @JvmInline
            value class CaseRepositoryRuleViolationError(val value: RepositoryRuleViolationError) : ReposCreateOrUpdateFileContentsResponse

            object Serializer : KSerializer<ReposCreateOrUpdateFileContentsResponse> {
                @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
                override val descriptor: SerialDescriptor =
                    buildSerialDescriptor("io.github.nomisrev.api.Repos.Contents.ReposCreateOrUpdateFileContentsResponse", PolymorphicKind.SEALED) {
                        element("CaseBasicError", BasicError.serializer().descriptor)
                        element("CaseRepositoryRuleViolationError", RepositoryRuleViolationError.serializer().descriptor)
                    }

                override fun deserialize(decoder: Decoder): ReposCreateOrUpdateFileContentsResponse {
                    val value = decoder.decodeSerializableValue(JsonElement.serializer())
                    val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                    return json.attemptDeserialize(
                        value,
                        CaseBasicError::class to { CaseBasicError(decodeFromJsonElement(BasicError.serializer(), it)) },
                        CaseRepositoryRuleViolationError::class to { CaseRepositoryRuleViolationError(decodeFromJsonElement(RepositoryRuleViolationError.serializer(), it)) },
                    )
                }

                override fun serialize(encoder: Encoder, value: ReposCreateOrUpdateFileContentsResponse) = when(value) {
                    is CaseBasicError -> encoder.encodeSerializableValue(BasicError.serializer(), value.value)
                    is CaseRepositoryRuleViolationError -> encoder.encodeSerializableValue(RepositoryRuleViolationError.serializer(), value.value)
                }
            }
        }


        @Serializable
        data class ReposDeleteFileBody(
            val message: String,
            val sha: String,
            val branch: String? = null,
            val committer: Committer? = null,
            val author: Author? = null,
        ) {
            @Serializable
            data class Committer(val name: String? = null, val email: String? = null)

            @Serializable
            data class Author(val name: String? = null, val email: String? = null)
        }


        @Serializable
        data class ReposDeleteFileResponse(
            val code: String? = null,
            val message: String? = null,
            @SerialName("documentation_url") val documentationUrl: String? = null,
        )


        @OptIn(ExperimentalSerializationApi::class)
        @JsonClassDiscriminator("type")
        @Serializable
        sealed interface ReposGetContentResponse {
            @SerialName("array")
            @Serializable
            @JvmInline
            value class Array(val value: ContentDirectory) : ReposGetContentResponse

            @SerialName("file")
            @Serializable
            @JvmInline
            value class File(val value: ContentFile) : ReposGetContentResponse

            @SerialName("symlink")
            @Serializable
            @JvmInline
            value class Symlink(val value: ContentSymlink) : ReposGetContentResponse

            @SerialName("submodule")
            @Serializable
            @JvmInline
            value class Submodule(val value: ContentSubmodule) : ReposGetContentResponse
        }

        sealed interface ReposGetContentResult {
            data class OK(val value: ReposGetContentResponse) : ReposGetContentResult

            data object Found : ReposGetContentResult

            data object NotModified : ReposGetContentResult

            data class Forbidden(val value: BasicError) : ReposGetContentResult

            data class NotFound(val value: BasicError) : ReposGetContentResult
        }

        suspend fun reposGetContent(
            owner: String,
            repo: String,
            path: String,
            ref: String? = null,
        ): ReposGetContentResult

        sealed interface ReposCreateOrUpdateFileContentsResult {
            data class OK(val value: FileCommit) : ReposCreateOrUpdateFileContentsResult

            data class Created(val value: FileCommit) : ReposCreateOrUpdateFileContentsResult

            data class NotFound(val value: BasicError) : ReposCreateOrUpdateFileContentsResult

            data class Conflict(val value: ReposCreateOrUpdateFileContentsResponse) : ReposCreateOrUpdateFileContentsResult

            data class UnprocessableEntity(val value: ValidationError) : ReposCreateOrUpdateFileContentsResult
        }

        suspend fun reposCreateOrUpdateFileContents(
            owner: String,
            repo: String,
            path: String,
            body: ReposCreateOrUpdateFileContentsBody,
        ): ReposCreateOrUpdateFileContentsResult

        sealed interface ReposDeleteFileResult {
            data class OK(val value: FileCommit) : ReposDeleteFileResult

            data class NotFound(val value: BasicError) : ReposDeleteFileResult

            data class Conflict(val value: BasicError) : ReposDeleteFileResult

            data class UnprocessableEntity(val value: ValidationError) : ReposDeleteFileResult

            data class ServiceUnavailable(val value: ReposDeleteFileResponse) : ReposDeleteFileResult
        }

        suspend fun reposDeleteFile(
            owner: String,
            repo: String,
            path: String,
            body: ReposDeleteFileBody,
        ): ReposDeleteFileResult
    }

    interface Contributors {
        sealed interface ReposListContributorsResult {
            data class OK(val value: List<Contributor>) : ReposListContributorsResult

            data object NoContent : ReposListContributorsResult

            data class Forbidden(val value: BasicError) : ReposListContributorsResult

            data class NotFound(val value: BasicError) : ReposListContributorsResult
        }

        suspend fun reposListContributors(
            owner: String,
            repo: String,
            page: Long = 1L,
            perPage: Long = 30L,
            anon: String? = null,
        ): ReposListContributorsResult
    }

    interface Dependabot {
        val alerts: Repos.Dependabot.Alerts

        val secrets: Repos.Dependabot.Secrets

        interface Alerts {
            @Serializable
            data class DependabotUpdateAlertBody(
                val state: State? = null,
                @SerialName("dismissed_reason") val dismissedReason: DismissedReason? = null,
                @SerialName("dismissed_comment") val dismissedComment: String? = null,
                val assignees: List<String>? = null,
            ) {
                @Serializable
                enum class State {
                    @SerialName("dismissed") Dismissed, @SerialName("open") Open;
                }

                @Serializable
                enum class DismissedReason {
                    @SerialName("fix_started")
                    FixStarted,
                    @SerialName("inaccurate")
                    Inaccurate,
                    @SerialName("no_bandwidth")
                    NoBandwidth,
                    @SerialName("not_used")
                    NotUsed,
                    @SerialName("tolerable_risk")
                    TolerableRisk;
                }
            }


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
                value class Patchs(val value: List<Patch>) : Has {
                    @Serializable
                    enum class Patch {
                        @SerialName("patch") Patch;
                    }
                }

                object Serializer : KSerializer<Has> {
                    @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
                    override val descriptor: SerialDescriptor =
                        buildSerialDescriptor("io.github.nomisrev.api.Repos.Dependabot.Alerts.Has", PolymorphicKind.SEALED) {
                            element("CaseString", String.serializer().descriptor)
                            element("Patchs", ListSerializer(Patchs.Patch.serializer()).descriptor)
                        }

                    override fun deserialize(decoder: Decoder): Has {
                        val value = decoder.decodeSerializableValue(JsonElement.serializer())
                        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                        return json.attemptDeserialize(
                            value,
                            Patchs::class to { Patchs(decodeFromJsonElement(ListSerializer(Patchs.Patch.serializer()), it)) },
                            CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                        )
                    }

                    override fun serialize(encoder: Encoder, value: Has) = when(value) {
                        is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                        is Patchs -> encoder.encodeSerializableValue(ListSerializer(Patchs.Patch.serializer()), value.value)
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

            sealed interface DependabotListAlertsForRepoResult {
                data class OK(val value: List<DependabotAlertResponse>) : DependabotListAlertsForRepoResult

                data object NotModified : DependabotListAlertsForRepoResult

                data class BadRequest(val value: BasicError) : DependabotListAlertsForRepoResult

                data class Forbidden(val value: BasicError) : DependabotListAlertsForRepoResult

                data class NotFound(val value: BasicError) : DependabotListAlertsForRepoResult

                data class UnprocessableEntity(val value: ValidationErrorSimple) : DependabotListAlertsForRepoResult
            }

            suspend fun dependabotListAlertsForRepo(
                owner: String,
                repo: String,
                direction: Direction = Direction.Desc,
                perPage: Long = 30L,
                sort: Sort = Sort.Created,
                after: String? = null,
                assignee: String? = null,
                before: String? = null,
                ecosystem: String? = null,
                epssPercentage: String? = null,
                has: Has? = null,
                manifest: String? = null,
                `package`: String? = null,
                scope: Scope? = null,
                severity: String? = null,
                state: String? = null,
            ): DependabotListAlertsForRepoResult

            sealed interface DependabotGetAlertResult {
                data class OK(val value: DependabotAlertResponse) : DependabotGetAlertResult

                data object NotModified : DependabotGetAlertResult

                data class Forbidden(val value: BasicError) : DependabotGetAlertResult

                data class NotFound(val value: BasicError) : DependabotGetAlertResult
            }

            suspend fun dependabotGetAlert(
                owner: String,
                repo: String,
                alertNumber: AlertNumberRequest,
            ): DependabotGetAlertResult

            sealed interface DependabotUpdateAlertResult {
                data class OK(val value: DependabotAlertResponse) : DependabotUpdateAlertResult

                data class BadRequest(val value: BasicError) : DependabotUpdateAlertResult

                data class Forbidden(val value: BasicError) : DependabotUpdateAlertResult

                data class NotFound(val value: BasicError) : DependabotUpdateAlertResult

                data class Conflict(val value: BasicError) : DependabotUpdateAlertResult

                data class UnprocessableEntity(val value: ValidationErrorSimple) : DependabotUpdateAlertResult
            }

            suspend fun dependabotUpdateAlert(
                owner: String,
                repo: String,
                alertNumber: AlertNumberRequest,
                body: DependabotUpdateAlertBody,
            ): DependabotUpdateAlertResult
        }

        interface Secrets {
            val publicKey: Repos.Dependabot.Secrets.PublicKey

            @Serializable
            data class DependabotCreateOrUpdateRepoSecretBody(
                @SerialName("encrypted_value") val encryptedValue: String? = null,
                @SerialName("key_id") val keyId: String? = null,
            )


            @Serializable
            data class DependabotListRepoSecretsResponse(
                @SerialName("total_count") val totalCount: Long,
                val secrets: List<DependabotSecret>,
            )

            suspend fun dependabotListRepoSecrets(
                owner: String,
                repo: String,
                page: Long = 1L,
                perPage: Long = 30L,
            ): DependabotListRepoSecretsResponse

            suspend fun dependabotGetRepoSecret(
                owner: String,
                repo: String,
                secretName: String,
            ): DependabotSecret

            sealed interface DependabotCreateOrUpdateRepoSecretResult {
                data class Created(val value: EmptyObject) : DependabotCreateOrUpdateRepoSecretResult

                data object NoContent : DependabotCreateOrUpdateRepoSecretResult
            }

            suspend fun dependabotCreateOrUpdateRepoSecret(
                owner: String,
                repo: String,
                secretName: String,
                body: DependabotCreateOrUpdateRepoSecretBody,
            ): DependabotCreateOrUpdateRepoSecretResult

            suspend fun dependabotDeleteRepoSecret(
                owner: String,
                repo: String,
                secretName: String,
            ): Unit

            interface PublicKey {
                suspend fun dependabotGetRepoPublicKey(
                    owner: String,
                    repo: String,
                ): DependabotPublicKey
            }
        }
    }

    interface DependencyGraph {
        val compare: Repos.DependencyGraph.CompareApi

        val sbom: Repos.DependencyGraph.Sbom

        val snapshots: Repos.DependencyGraph.Snapshots

        interface CompareApi {
            sealed interface DependencyGraphDiffRangeResult {
                data class OK(val value: DependencyGraphDiff) : DependencyGraphDiffRangeResult

                data class Forbidden(val value: BasicError) : DependencyGraphDiffRangeResult

                data class NotFound(val value: BasicError) : DependencyGraphDiffRangeResult
            }

            suspend fun dependencyGraphDiffRange(
                owner: String,
                repo: String,
                basehead: String,
                name: String? = null,
            ): DependencyGraphDiffRangeResult
        }

        interface Sbom {
            sealed interface DependencyGraphExportSbomResult {
                data class OK(val value: DependencyGraphSpdxSbom) : DependencyGraphExportSbomResult

                data class Forbidden(val value: BasicError) : DependencyGraphExportSbomResult

                data class NotFound(val value: BasicError) : DependencyGraphExportSbomResult
            }

            suspend fun dependencyGraphExportSbom(
                owner: String,
                repo: String,
            ): DependencyGraphExportSbomResult
        }

        interface Snapshots {
            @Serializable
            data class DependencyGraphCreateRepositorySnapshotResponse(
                val id: Long,
                @SerialName("created_at") val createdAt: String,
                val result: String,
                val message: String,
            )

            suspend fun dependencyGraphCreateRepositorySnapshot(
                owner: String,
                repo: String,
                body: Snapshot,
            ): DependencyGraphCreateRepositorySnapshotResponse
        }
    }

    interface Deployments {
        val statuses: Repos.Deployments.StatusesApi

        @Serializable
        data class ReposCreateDeploymentBody(
            val ref: String,
            val task: String? = null,
            @SerialName("auto_merge") val autoMerge: Boolean? = null,
            @SerialName("required_contexts") val requiredContexts: List<String>? = null,
            val payload: Payload? = null,
            val environment: String? = null,
            val description: String? = null,
            @SerialName("transient_environment") val transientEnvironment: Boolean? = null,
            @SerialName("production_environment") val productionEnvironment: Boolean? = null,
        ) {
            @Serializable(with = Payload.Serializer::class)
            sealed interface Payload {
                @Serializable
                @JvmInline
                value class CaseJsonElement(val value: JsonElement) : Payload

                @Serializable
                @JvmInline
                value class CaseString(val value: String) : Payload

                object Serializer : KSerializer<Payload> {
                    @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
                    override val descriptor: SerialDescriptor =
                        buildSerialDescriptor("io.github.nomisrev.api.Repos.Deployments.ReposCreateDeploymentBody.Payload", PolymorphicKind.SEALED) {
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

                    override fun serialize(encoder: Encoder, value: Payload) = when(value) {
                        is CaseJsonElement -> encoder.encodeSerializableValue(JsonElement.serializer(), value.value)
                        is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                    }
                }
            }
        }


        @Serializable
        @JvmInline
        value class ReposCreateDeploymentResponse(val message: String? = null)

        suspend fun reposListDeployments(
            owner: String,
            repo: String,
            environment: String = "none",
            page: Long = 1L,
            perPage: Long = 30L,
            ref: String = "none",
            sha: String = "none",
            task: String = "none",
        ): List<Deployment>

        sealed interface ReposCreateDeploymentResult {
            data class Created(val value: Deployment) : ReposCreateDeploymentResult

            data class Accepted(val value: ReposCreateDeploymentResponse) : ReposCreateDeploymentResult

            data object Conflict : ReposCreateDeploymentResult

            data class UnprocessableEntity(val value: ValidationError) : ReposCreateDeploymentResult
        }

        suspend fun reposCreateDeployment(
            owner: String,
            repo: String,
            body: ReposCreateDeploymentBody,
        ): ReposCreateDeploymentResult

        sealed interface ReposGetDeploymentResult {
            data class OK(val value: Deployment) : ReposGetDeploymentResult

            data class NotFound(val value: BasicError) : ReposGetDeploymentResult
        }

        suspend fun reposGetDeployment(
            owner: String,
            repo: String,
            deploymentId: Long,
        ): ReposGetDeploymentResult

        sealed interface ReposDeleteDeploymentResult {
            data object NoContent : ReposDeleteDeploymentResult

            data class NotFound(val value: BasicError) : ReposDeleteDeploymentResult

            data class UnprocessableEntity(val value: ValidationErrorSimple) : ReposDeleteDeploymentResult
        }

        suspend fun reposDeleteDeployment(
            owner: String,
            repo: String,
            deploymentId: Long,
        ): ReposDeleteDeploymentResult

        interface StatusesApi {
            @Serializable
            data class ReposCreateDeploymentStatusBody(
                val state: State,
                @SerialName("target_url") val targetUrl: String? = null,
                @SerialName("log_url") val logUrl: String? = null,
                val description: String? = null,
                val environment: String? = null,
                @SerialName("environment_url") val environmentUrl: String? = null,
                @SerialName("auto_inactive") val autoInactive: Boolean? = null,
            ) {
                @Serializable
                enum class State {
                    @SerialName("error")
                    Error,
                    @SerialName("failure")
                    Failure,
                    @SerialName("inactive")
                    Inactive,
                    @SerialName("in_progress")
                    InProgress,
                    @SerialName("queued")
                    Queued,
                    @SerialName("pending")
                    Pending,
                    @SerialName("success")
                    Success;
                }
            }

            sealed interface ReposListDeploymentStatusesResult {
                data class OK(val value: List<DeploymentStatus>) : ReposListDeploymentStatusesResult

                data class NotFound(val value: BasicError) : ReposListDeploymentStatusesResult
            }

            suspend fun reposListDeploymentStatuses(
                owner: String,
                repo: String,
                deploymentId: Long,
                page: Long = 1L,
                perPage: Long = 30L,
            ): ReposListDeploymentStatusesResult

            sealed interface ReposCreateDeploymentStatusResult {
                data class Created(val value: DeploymentStatus) : ReposCreateDeploymentStatusResult

                data class UnprocessableEntity(val value: ValidationError) : ReposCreateDeploymentStatusResult
            }

            suspend fun reposCreateDeploymentStatus(
                owner: String,
                repo: String,
                deploymentId: Long,
                body: ReposCreateDeploymentStatusBody,
            ): ReposCreateDeploymentStatusResult

            sealed interface ReposGetDeploymentStatusResult {
                data class OK(val value: DeploymentStatus) : ReposGetDeploymentStatusResult

                data class NotFound(val value: BasicError) : ReposGetDeploymentStatusResult
            }

            suspend fun reposGetDeploymentStatus(
                owner: String,
                repo: String,
                deploymentId: Long,
                statusId: Long,
            ): ReposGetDeploymentStatusResult
        }
    }

    interface Dispatches {
        @Serializable
        data class ReposCreateDispatchEventBody(
            @SerialName("event_type") val eventType: String,
            @SerialName("client_payload") val clientPayload: JsonElement? = null,
        )

        sealed interface ReposCreateDispatchEventResult {
            data object NoContent : ReposCreateDispatchEventResult

            data class NotFound(val value: BasicError) : ReposCreateDispatchEventResult

            data class UnprocessableEntity(val value: ValidationError) : ReposCreateDispatchEventResult
        }

        suspend fun reposCreateDispatchEvent(
            owner: String,
            repo: String,
            body: ReposCreateDispatchEventBody,
        ): ReposCreateDispatchEventResult
    }

    interface Environments {
        val deploymentBranchPolicies: Repos.Environments.DeploymentBranchPolicies

        val deploymentProtectionRules: Repos.Environments.DeploymentProtectionRules

        val secrets: Repos.Environments.Secrets

        val variables: Repos.Environments.Variables

        @Serializable
        data class ReposCreateOrUpdateEnvironmentBody(
            @SerialName("wait_timer") val waitTimer: WaitTimer? = null,
            @SerialName("prevent_self_review") val preventSelfReview: PreventSelfReview? = null,
            val reviewers: List<Reviewers>? = null,
            @SerialName("deployment_branch_policy") val deploymentBranchPolicy: DeploymentBranchPolicySettings? = null,
        ) {
            @Serializable
            data class Reviewers(val type: DeploymentReviewerType? = null, val id: Long? = null)
        }


        @Serializable
        data class ReposGetAllEnvironmentsResponse(
            @SerialName("total_count") val totalCount: Long? = null,
            val environments: List<Environment>? = null,
        )

        suspend fun reposGetAllEnvironments(
            owner: String,
            repo: String,
            page: Long = 1L,
            perPage: Long = 30L,
        ): ReposGetAllEnvironmentsResponse

        suspend fun reposGetEnvironment(
            owner: String,
            repo: String,
            environmentName: String,
        ): Environment

        sealed interface ReposCreateOrUpdateEnvironmentResult {
            data class OK(val value: Environment) : ReposCreateOrUpdateEnvironmentResult

            data class UnprocessableEntity(val value: BasicError) : ReposCreateOrUpdateEnvironmentResult
        }

        suspend fun reposCreateOrUpdateEnvironment(
            owner: String,
            repo: String,
            environmentName: String,
            body: ReposCreateOrUpdateEnvironmentBody? = null,
        ): ReposCreateOrUpdateEnvironmentResult

        suspend fun reposDeleteAnEnvironment(
            owner: String,
            repo: String,
            environmentName: String,
        ): Unit

        interface DeploymentBranchPolicies {
            @Serializable
            data class ReposListDeploymentBranchPoliciesResponse(
                @SerialName("total_count") val totalCount: Long,
                @SerialName("branch_policies") val branchPolicies: List<DeploymentBranchPolicy>,
            )

            suspend fun reposListDeploymentBranchPolicies(
                owner: String,
                repo: String,
                environmentName: String,
                page: Long = 1L,
                perPage: Long = 30L,
            ): ReposListDeploymentBranchPoliciesResponse

            sealed interface ReposCreateDeploymentBranchPolicyResult {
                data class OK(val value: DeploymentBranchPolicy) : ReposCreateDeploymentBranchPolicyResult

                data object SeeOther : ReposCreateDeploymentBranchPolicyResult

                data object NotFound : ReposCreateDeploymentBranchPolicyResult
            }

            suspend fun reposCreateDeploymentBranchPolicy(
                owner: String,
                repo: String,
                environmentName: String,
                body: DeploymentBranchPolicyNamePatternWithType,
            ): ReposCreateDeploymentBranchPolicyResult

            suspend fun reposGetDeploymentBranchPolicy(
                owner: String,
                repo: String,
                environmentName: String,
                branchPolicyId: Long,
            ): DeploymentBranchPolicy

            suspend fun reposUpdateDeploymentBranchPolicy(
                owner: String,
                repo: String,
                environmentName: String,
                branchPolicyId: Long,
                body: DeploymentBranchPolicyNamePattern,
            ): DeploymentBranchPolicy

            suspend fun reposDeleteDeploymentBranchPolicy(
                owner: String,
                repo: String,
                environmentName: String,
                branchPolicyId: Long,
            ): Unit
        }

        interface DeploymentProtectionRules {
            val apps: Repos.Environments.DeploymentProtectionRules.Apps

            @Serializable
            @JvmInline
            value class ReposCreateDeploymentProtectionRuleBody(@SerialName("integration_id") val integrationId: Long? = null)


            @Serializable
            data class ReposGetAllDeploymentProtectionRulesResponse(
                @SerialName("total_count") val totalCount: Long? = null,
                @SerialName("custom_deployment_protection_rules") val customDeploymentProtectionRules: List<DeploymentProtectionRule>? = null,
            )

            suspend fun reposGetAllDeploymentProtectionRules(
                owner: String,
                repo: String,
                environmentName: String,
            ): ReposGetAllDeploymentProtectionRulesResponse

            suspend fun reposCreateDeploymentProtectionRule(
                owner: String,
                repo: String,
                environmentName: String,
                body: ReposCreateDeploymentProtectionRuleBody,
            ): DeploymentProtectionRule

            suspend fun reposGetCustomDeploymentProtectionRule(
                owner: String,
                repo: String,
                environmentName: String,
                protectionRuleId: Long,
            ): DeploymentProtectionRule

            suspend fun reposDisableDeploymentProtectionRule(
                owner: String,
                repo: String,
                environmentName: String,
                protectionRuleId: Long,
            ): Unit

            interface Apps {
                @Serializable
                data class ReposListCustomDeploymentRuleIntegrationsResponse(
                    @SerialName("total_count") val totalCount: Long? = null,
                    @SerialName("available_custom_deployment_protection_rule_integrations") val availableCustomDeploymentProtectionRuleIntegrations: List<CustomDeploymentRuleApp>? = null,
                )

                suspend fun reposListCustomDeploymentRuleIntegrations(
                    owner: String,
                    repo: String,
                    environmentName: String,
                    page: Long = 1L,
                    perPage: Long = 30L,
                ): ReposListCustomDeploymentRuleIntegrationsResponse
            }
        }

        interface Secrets {
            val publicKey: Repos.Environments.Secrets.PublicKey

            @Serializable
            data class ActionsCreateOrUpdateEnvironmentSecretBody(
                @SerialName("encrypted_value") val encryptedValue: String,
                @SerialName("key_id") val keyId: String,
            )


            @Serializable
            data class ActionsListEnvironmentSecretsResponse(
                @SerialName("total_count") val totalCount: Long,
                val secrets: List<ActionsSecret>,
            )

            suspend fun actionsListEnvironmentSecrets(
                owner: String,
                repo: String,
                environmentName: String,
                page: Long = 1L,
                perPage: Long = 30L,
            ): ActionsListEnvironmentSecretsResponse

            suspend fun actionsGetEnvironmentSecret(
                owner: String,
                repo: String,
                environmentName: String,
                secretName: String,
            ): ActionsSecret

            sealed interface ActionsCreateOrUpdateEnvironmentSecretResult {
                data class Created(val value: EmptyObject) : ActionsCreateOrUpdateEnvironmentSecretResult

                data object NoContent : ActionsCreateOrUpdateEnvironmentSecretResult
            }

            suspend fun actionsCreateOrUpdateEnvironmentSecret(
                owner: String,
                repo: String,
                environmentName: String,
                secretName: String,
                body: ActionsCreateOrUpdateEnvironmentSecretBody,
            ): ActionsCreateOrUpdateEnvironmentSecretResult

            suspend fun actionsDeleteEnvironmentSecret(
                owner: String,
                repo: String,
                environmentName: String,
                secretName: String,
            ): Unit

            interface PublicKey {
                suspend fun actionsGetEnvironmentPublicKey(
                    owner: String,
                    repo: String,
                    environmentName: String,
                ): ActionsPublicKey
            }
        }

        interface Variables {
            @Serializable
            data class ActionsCreateEnvironmentVariableBody(val name: String, val value: String)


            @Serializable
            data class ActionsListEnvironmentVariablesResponse(
                @SerialName("total_count") val totalCount: Long,
                val variables: List<ActionsVariable>,
            )


            @Serializable
            data class ActionsUpdateEnvironmentVariableBody(val name: String? = null, val value: String? = null)

            suspend fun actionsListEnvironmentVariables(
                owner: String,
                repo: String,
                environmentName: String,
                page: Long = 1L,
                perPage: Long = 10L,
            ): ActionsListEnvironmentVariablesResponse

            suspend fun actionsCreateEnvironmentVariable(
                owner: String,
                repo: String,
                environmentName: String,
                body: ActionsCreateEnvironmentVariableBody,
            ): EmptyObject

            suspend fun actionsGetEnvironmentVariable(
                owner: String,
                repo: String,
                environmentName: String,
                name: String,
            ): ActionsVariable

            suspend fun actionsDeleteEnvironmentVariable(
                owner: String,
                repo: String,
                environmentName: String,
                name: String,
            ): Unit

            suspend fun actionsUpdateEnvironmentVariable(
                owner: String,
                repo: String,
                environmentName: String,
                name: String,
                body: ActionsUpdateEnvironmentVariableBody,
            ): Unit
        }
    }

    interface Events {
        suspend fun activityListRepoEvents(
            owner: String,
            repo: String,
            page: Long = 1L,
            perPage: Long = 30L,
        ): List<Event>
    }

    interface Forks {
        @Serializable
        data class ReposCreateForkBody(
            val organization: String? = null,
            val name: String? = null,
            @SerialName("default_branch_only") val defaultBranchOnly: Boolean? = null,
        )


        @Serializable
        enum class Sort {
            @SerialName("newest")
            Newest,
            @SerialName("oldest")
            Oldest,
            @SerialName("stargazers")
            Stargazers,
            @SerialName("watchers")
            Watchers;
        }

        sealed interface ReposListForksResult {
            data class OK(val value: List<MinimalRepository>) : ReposListForksResult

            data class BadRequest(val value: BasicError) : ReposListForksResult
        }

        suspend fun reposListForks(
            owner: String,
            repo: String,
            page: Long = 1L,
            perPage: Long = 30L,
            sort: Sort = Sort.Newest,
        ): ReposListForksResult

        sealed interface ReposCreateForkResult {
            data class Accepted(val value: FullRepository) : ReposCreateForkResult

            data class BadRequest(val value: BasicError) : ReposCreateForkResult

            data class Forbidden(val value: BasicError) : ReposCreateForkResult

            data class NotFound(val value: BasicError) : ReposCreateForkResult

            data class UnprocessableEntity(val value: ValidationError) : ReposCreateForkResult
        }

        suspend fun reposCreateFork(
            owner: String,
            repo: String,
            body: ReposCreateForkBody? = null,
        ): ReposCreateForkResult
    }

    interface Git {
        val blobs: Repos.Git.Blobs

        val commits: Repos.Git.CommitsApi

        val matchingRefs: Repos.Git.MatchingRefs

        val ref: Repos.Git.Ref

        val refs: Repos.Git.Refs

        val tags: Repos.Git.TagsApi

        val trees: Repos.Git.Trees

        interface Blobs {
            @Serializable
            data class GitCreateBlobBody(val content: String, val encoding: String? = null)


            @Serializable(with = GitCreateBlobResponse.Serializer::class)
            sealed interface GitCreateBlobResponse {
                @Serializable
                @JvmInline
                value class CaseValidationError(val value: ValidationError) : GitCreateBlobResponse

                @Serializable
                @JvmInline
                value class CaseRepositoryRuleViolationError(val value: RepositoryRuleViolationError) : GitCreateBlobResponse

                object Serializer : KSerializer<GitCreateBlobResponse> {
                    @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
                    override val descriptor: SerialDescriptor =
                        buildSerialDescriptor("io.github.nomisrev.api.Repos.Git.Blobs.GitCreateBlobResponse", PolymorphicKind.SEALED) {
                            element("CaseValidationError", ValidationError.serializer().descriptor)
                            element("CaseRepositoryRuleViolationError", RepositoryRuleViolationError.serializer().descriptor)
                        }

                    override fun deserialize(decoder: Decoder): GitCreateBlobResponse {
                        val value = decoder.decodeSerializableValue(JsonElement.serializer())
                        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                        return json.attemptDeserialize(
                            value,
                            CaseValidationError::class to { CaseValidationError(decodeFromJsonElement(ValidationError.serializer(), it)) },
                            CaseRepositoryRuleViolationError::class to { CaseRepositoryRuleViolationError(decodeFromJsonElement(RepositoryRuleViolationError.serializer(), it)) },
                        )
                    }

                    override fun serialize(encoder: Encoder, value: GitCreateBlobResponse) = when(value) {
                        is CaseValidationError -> encoder.encodeSerializableValue(ValidationError.serializer(), value.value)
                        is CaseRepositoryRuleViolationError -> encoder.encodeSerializableValue(RepositoryRuleViolationError.serializer(), value.value)
                    }
                }
            }

            sealed interface GitCreateBlobResult {
                data class Created(val value: ShortBlob) : GitCreateBlobResult

                data class Forbidden(val value: BasicError) : GitCreateBlobResult

                data class NotFound(val value: BasicError) : GitCreateBlobResult

                data class Conflict(val value: BasicError) : GitCreateBlobResult

                data class UnprocessableEntity(val value: GitCreateBlobResponse) : GitCreateBlobResult
            }

            suspend fun gitCreateBlob(
                owner: String,
                repo: String,
                body: GitCreateBlobBody,
            ): GitCreateBlobResult

            sealed interface GitGetBlobResult {
                data class OK(val value: Blob) : GitGetBlobResult

                data class Forbidden(val value: BasicError) : GitGetBlobResult

                data class NotFound(val value: BasicError) : GitGetBlobResult

                data class Conflict(val value: BasicError) : GitGetBlobResult

                data class UnprocessableEntity(val value: ValidationError) : GitGetBlobResult
            }

            suspend fun gitGetBlob(
                owner: String,
                repo: String,
                fileSha: String,
            ): GitGetBlobResult
        }

        interface CommitsApi {
            @Serializable
            data class GitCreateCommitBody(
                val message: String,
                val tree: String,
                val parents: List<String>? = null,
                val author: Author? = null,
                val committer: Committer? = null,
                val signature: String? = null,
            ) {
                @Serializable
                data class Author(val name: String, val email: String, val date: LocalDateTime? = null)

                @Serializable
                data class Committer(val name: String? = null, val email: String? = null, val date: LocalDateTime? = null)
            }

            sealed interface GitCreateCommitResult {
                data class Created(val value: GitCommit) : GitCreateCommitResult

                data class NotFound(val value: BasicError) : GitCreateCommitResult

                data class Conflict(val value: BasicError) : GitCreateCommitResult

                data class UnprocessableEntity(val value: ValidationError) : GitCreateCommitResult
            }

            suspend fun gitCreateCommit(
                owner: String,
                repo: String,
                body: GitCreateCommitBody,
            ): GitCreateCommitResult

            sealed interface GitGetCommitResult {
                data class OK(val value: GitCommit) : GitGetCommitResult

                data class NotFound(val value: BasicError) : GitGetCommitResult

                data class Conflict(val value: BasicError) : GitGetCommitResult
            }

            suspend fun gitGetCommit(
                owner: String,
                repo: String,
                commitSha: String,
            ): GitGetCommitResult
        }

        interface MatchingRefs {
            sealed interface GitListMatchingRefsResult {
                data class OK(val value: List<GitRef>) : GitListMatchingRefsResult

                data class Conflict(val value: BasicError) : GitListMatchingRefsResult
            }

            suspend fun gitListMatchingRefs(
                owner: String,
                repo: String,
                ref: String,
            ): GitListMatchingRefsResult
        }

        interface Ref {
            sealed interface GitGetRefResult {
                data class OK(val value: GitRef) : GitGetRefResult

                data class NotFound(val value: BasicError) : GitGetRefResult

                data class Conflict(val value: BasicError) : GitGetRefResult
            }

            suspend fun gitGetRef(
                owner: String,
                repo: String,
                ref: String,
            ): GitGetRefResult
        }

        interface Refs {
            @Serializable
            data class GitCreateRefBody(val ref: String, val sha: String)


            @Serializable
            data class GitUpdateRefBody(val sha: String, val force: Boolean? = null)

            sealed interface GitCreateRefResult {
                data class Created(val value: GitRef) : GitCreateRefResult

                data class Conflict(val value: BasicError) : GitCreateRefResult

                data class UnprocessableEntity(val value: ValidationError) : GitCreateRefResult
            }

            suspend fun gitCreateRef(
                owner: String,
                repo: String,
                body: GitCreateRefBody,
            ): GitCreateRefResult

            sealed interface GitDeleteRefResult {
                data object NoContent : GitDeleteRefResult

                data class Conflict(val value: BasicError) : GitDeleteRefResult

                data object UnprocessableEntity : GitDeleteRefResult
            }

            suspend fun gitDeleteRef(
                owner: String,
                repo: String,
                ref: String,
            ): GitDeleteRefResult

            sealed interface GitUpdateRefResult {
                data class OK(val value: GitRef) : GitUpdateRefResult

                data class Conflict(val value: BasicError) : GitUpdateRefResult

                data class UnprocessableEntity(val value: ValidationError) : GitUpdateRefResult
            }

            suspend fun gitUpdateRef(
                owner: String,
                repo: String,
                ref: String,
                body: GitUpdateRefBody,
            ): GitUpdateRefResult
        }

        interface TagsApi {
            @Serializable
            data class GitCreateTagBody(
                val tag: String,
                val message: String,
                @SerialName("object") val `object`: String,
                val type: Type,
                val tagger: Tagger? = null,
            ) {
                @Serializable
                enum class Type {
                    @SerialName("commit") Commit, @SerialName("tree") Tree, @SerialName("blob") Blob;
                }

                @Serializable
                data class Tagger(val name: String, val email: String, val date: LocalDateTime? = null)
            }

            sealed interface GitCreateTagResult {
                data class Created(val value: GitTag) : GitCreateTagResult

                data class Conflict(val value: BasicError) : GitCreateTagResult

                data class UnprocessableEntity(val value: ValidationError) : GitCreateTagResult
            }

            suspend fun gitCreateTag(
                owner: String,
                repo: String,
                body: GitCreateTagBody,
            ): GitCreateTagResult

            sealed interface GitGetTagResult {
                data class OK(val value: GitTag) : GitGetTagResult

                data class NotFound(val value: BasicError) : GitGetTagResult

                data class Conflict(val value: BasicError) : GitGetTagResult
            }

            suspend fun gitGetTag(
                owner: String,
                repo: String,
                tagSha: String,
            ): GitGetTagResult
        }

        interface Trees {
            @Serializable
            data class GitCreateTreeBody(val tree: List<Tree>, @SerialName("base_tree") val baseTree: String? = null) {
                @Serializable
                data class Tree(
                    val path: String? = null,
                    val mode: Mode? = null,
                    val type: Type? = null,
                    val sha: String? = null,
                    val content: String? = null,
                ) {
                    @Serializable
                    enum class Mode {
                        @JsName("_100644")@SerialName("100644")
                        `100644`,
                        @JsName("_100755")@SerialName("100755")
                        `100755`,
                        @JsName("_040000")@SerialName("040000")
                        `040000`,
                        @JsName("_160000")@SerialName("160000")
                        `160000`,
                        @JsName("_120000")@SerialName("120000")
                        `120000`;
                    }

                    @Serializable
                    enum class Type {
                        @SerialName("blob") Blob, @SerialName("tree") Tree, @SerialName("commit") Commit;
                    }
                }
            }

            sealed interface GitCreateTreeResult {
                data class Created(val value: GitTree) : GitCreateTreeResult

                data class Forbidden(val value: BasicError) : GitCreateTreeResult

                data class NotFound(val value: BasicError) : GitCreateTreeResult

                data class Conflict(val value: BasicError) : GitCreateTreeResult

                data class UnprocessableEntity(val value: ValidationError) : GitCreateTreeResult
            }

            suspend fun gitCreateTree(
                owner: String,
                repo: String,
                body: GitCreateTreeBody,
            ): GitCreateTreeResult

            sealed interface GitGetTreeResult {
                data class OK(val value: GitTree) : GitGetTreeResult

                data class NotFound(val value: BasicError) : GitGetTreeResult

                data class Conflict(val value: BasicError) : GitGetTreeResult

                data class UnprocessableEntity(val value: ValidationError) : GitGetTreeResult
            }

            suspend fun gitGetTree(
                owner: String,
                repo: String,
                treeSha: String,
                recursive: String? = null,
            ): GitGetTreeResult
        }
    }

    interface Hooks {
        val config: Repos.Hooks.Config

        val deliveries: Repos.Hooks.Deliveries

        val pings: Repos.Hooks.Pings

        val tests: Repos.Hooks.Tests

        @Serializable
        data class ReposCreateWebhookBody(
            val name: String? = null,
            val config: Config? = null,
            val events: List<String>? = null,
            val active: Boolean? = null,
        ) {
            @Serializable
            data class Config(
                val url: WebhookConfigUrl? = null,
                @SerialName("content_type") val contentType: WebhookConfigContentType? = null,
                val secret: WebhookConfigSecret? = null,
                @SerialName("insecure_ssl") val insecureSsl: WebhookConfigInsecureSsl? = null,
            )
        }


        @Serializable
        data class ReposUpdateWebhookBody(
            val config: WebhookConfig? = null,
            val events: List<String>? = null,
            @SerialName("add_events") val addEvents: List<String>? = null,
            @SerialName("remove_events") val removeEvents: List<String>? = null,
            val active: Boolean? = null,
        )

        sealed interface ReposListWebhooksResult {
            data class OK(val value: List<Hook>) : ReposListWebhooksResult

            data class NotFound(val value: BasicError) : ReposListWebhooksResult
        }

        suspend fun reposListWebhooks(
            owner: String,
            repo: String,
            page: Long = 1L,
            perPage: Long = 30L,
        ): ReposListWebhooksResult

        sealed interface ReposCreateWebhookResult {
            data class Created(val value: Hook) : ReposCreateWebhookResult

            data class Forbidden(val value: BasicError) : ReposCreateWebhookResult

            data class NotFound(val value: BasicError) : ReposCreateWebhookResult

            data class UnprocessableEntity(val value: ValidationError) : ReposCreateWebhookResult
        }

        suspend fun reposCreateWebhook(
            owner: String,
            repo: String,
            body: ReposCreateWebhookBody? = null,
        ): ReposCreateWebhookResult

        sealed interface ReposGetWebhookResult {
            data class OK(val value: Hook) : ReposGetWebhookResult

            data class NotFound(val value: BasicError) : ReposGetWebhookResult
        }

        suspend fun reposGetWebhook(
            owner: String,
            repo: String,
            hookId: Long,
        ): ReposGetWebhookResult

        sealed interface ReposDeleteWebhookResult {
            data object NoContent : ReposDeleteWebhookResult

            data class NotFound(val value: BasicError) : ReposDeleteWebhookResult
        }

        suspend fun reposDeleteWebhook(
            owner: String,
            repo: String,
            hookId: Long,
        ): ReposDeleteWebhookResult

        sealed interface ReposUpdateWebhookResult {
            data class OK(val value: Hook) : ReposUpdateWebhookResult

            data class NotFound(val value: BasicError) : ReposUpdateWebhookResult

            data class UnprocessableEntity(val value: ValidationError) : ReposUpdateWebhookResult
        }

        suspend fun reposUpdateWebhook(
            owner: String,
            repo: String,
            hookId: Long,
            body: ReposUpdateWebhookBody,
        ): ReposUpdateWebhookResult

        interface Config {
            @Serializable
            data class ReposUpdateWebhookConfigForRepoBody(
                val url: WebhookConfigUrl? = null,
                @SerialName("content_type") val contentType: WebhookConfigContentType? = null,
                val secret: WebhookConfigSecret? = null,
                @SerialName("insecure_ssl") val insecureSsl: WebhookConfigInsecureSsl? = null,
            )

            suspend fun reposGetWebhookConfigForRepo(
                owner: String,
                repo: String,
                hookId: Long,
            ): WebhookConfig

            suspend fun reposUpdateWebhookConfigForRepo(
                owner: String,
                repo: String,
                hookId: Long,
                body: ReposUpdateWebhookConfigForRepoBody? = null,
            ): WebhookConfig
        }

        interface Deliveries {
            val attempts: Repos.Hooks.Deliveries.Attempts

            sealed interface ReposListWebhookDeliveriesResult {
                data class OK(val value: List<HookDeliveryItem>) : ReposListWebhookDeliveriesResult

                data class BadRequest(val value: BasicError) : ReposListWebhookDeliveriesResult

                data class UnprocessableEntity(val value: ValidationError) : ReposListWebhookDeliveriesResult
            }

            suspend fun reposListWebhookDeliveries(
                owner: String,
                repo: String,
                hookId: Long,
                perPage: Long = 30L,
                cursor: String? = null,
            ): ReposListWebhookDeliveriesResult

            sealed interface ReposGetWebhookDeliveryResult {
                data class OK(val value: HookDelivery) : ReposGetWebhookDeliveryResult

                data class BadRequest(val value: BasicError) : ReposGetWebhookDeliveryResult

                data class UnprocessableEntity(val value: ValidationError) : ReposGetWebhookDeliveryResult
            }

            suspend fun reposGetWebhookDelivery(
                owner: String,
                repo: String,
                hookId: Long,
                deliveryId: Long,
            ): ReposGetWebhookDeliveryResult

            interface Attempts {
                sealed interface ReposRedeliverWebhookDeliveryResult {
                    data class Accepted(val value: JsonElement) : ReposRedeliverWebhookDeliveryResult

                    data class BadRequest(val value: BasicError) : ReposRedeliverWebhookDeliveryResult

                    data class UnprocessableEntity(val value: ValidationError) : ReposRedeliverWebhookDeliveryResult
                }

                suspend fun reposRedeliverWebhookDelivery(
                    owner: String,
                    repo: String,
                    hookId: Long,
                    deliveryId: Long,
                ): ReposRedeliverWebhookDeliveryResult
            }
        }

        interface Pings {
            sealed interface ReposPingWebhookResult {
                data object NoContent : ReposPingWebhookResult

                data class NotFound(val value: BasicError) : ReposPingWebhookResult
            }

            suspend fun reposPingWebhook(
                owner: String,
                repo: String,
                hookId: Long,
            ): ReposPingWebhookResult
        }

        interface Tests {
            sealed interface ReposTestPushWebhookResult {
                data object NoContent : ReposTestPushWebhookResult

                data class NotFound(val value: BasicError) : ReposTestPushWebhookResult
            }

            suspend fun reposTestPushWebhook(
                owner: String,
                repo: String,
                hookId: Long,
            ): ReposTestPushWebhookResult
        }
    }

    interface ImmutableReleases {
        sealed interface ReposCheckImmutableReleasesResult {
            data class OK(val value: CheckImmutableReleases) : ReposCheckImmutableReleasesResult

            data object NotFound : ReposCheckImmutableReleasesResult
        }

        suspend fun reposCheckImmutableReleases(
            owner: String,
            repo: String,
        ): ReposCheckImmutableReleasesResult

        sealed interface ReposEnableImmutableReleasesResult {
            data object NoContent : ReposEnableImmutableReleasesResult

            data class Conflict(val value: BasicError) : ReposEnableImmutableReleasesResult
        }

        suspend fun reposEnableImmutableReleases(
            owner: String,
            repo: String,
        ): ReposEnableImmutableReleasesResult

        sealed interface ReposDisableImmutableReleasesResult {
            data object NoContent : ReposDisableImmutableReleasesResult

            data class Conflict(val value: BasicError) : ReposDisableImmutableReleasesResult
        }

        suspend fun reposDisableImmutableReleases(
            owner: String,
            repo: String,
        ): ReposDisableImmutableReleasesResult
    }

    interface Import {
        val authors: Repos.Import.Authors

        val largeFiles: Repos.Import.LargeFiles

        val lfs: Repos.Import.Lfs

        @Serializable
        data class MigrationsStartImportBody(
            @SerialName("vcs_url") val vcsUrl: String,
            val vcs: Vcs? = null,
            @SerialName("vcs_username") val vcsUsername: String? = null,
            @SerialName("vcs_password") val vcsPassword: String? = null,
            @SerialName("tfvc_project") val tfvcProject: String? = null,
        ) {
            @Serializable
            enum class Vcs {
                @SerialName("subversion")
                Subversion,
                @SerialName("git")
                Git,
                @SerialName("mercurial")
                Mercurial,
                @SerialName("tfvc")
                Tfvc;
            }
        }


        @Serializable
        data class MigrationsUpdateImportBody(
            @SerialName("vcs_username") val vcsUsername: String? = null,
            @SerialName("vcs_password") val vcsPassword: String? = null,
            val vcs: Vcs? = null,
            @SerialName("tfvc_project") val tfvcProject: String? = null,
        ) {
            @Serializable
            enum class Vcs {
                @SerialName("subversion")
                Subversion,
                @SerialName("tfvc")
                Tfvc,
                @SerialName("git")
                Git,
                @SerialName("mercurial")
                Mercurial;
            }
        }

        sealed interface MigrationsGetImportStatusResult {
            data class OK(val value: Import) : MigrationsGetImportStatusResult

            data class NotFound(val value: BasicError) : MigrationsGetImportStatusResult

            data class ServiceUnavailable(val value: BasicError) : MigrationsGetImportStatusResult
        }

        @Deprecated("Deprecated by the API provider")
        suspend fun migrationsGetImportStatus(
            owner: String,
            repo: String,
        ): MigrationsGetImportStatusResult

        sealed interface MigrationsStartImportResult {
            data class Created(val value: Import) : MigrationsStartImportResult

            data class NotFound(val value: BasicError) : MigrationsStartImportResult

            data class UnprocessableEntity(val value: ValidationError) : MigrationsStartImportResult

            data class ServiceUnavailable(val value: BasicError) : MigrationsStartImportResult
        }

        @Deprecated("Deprecated by the API provider")
        suspend fun migrationsStartImport(
            owner: String,
            repo: String,
            body: MigrationsStartImportBody,
        ): MigrationsStartImportResult

        sealed interface MigrationsCancelImportResult {
            data object NoContent : MigrationsCancelImportResult

            data class ServiceUnavailable(val value: BasicError) : MigrationsCancelImportResult
        }

        @Deprecated("Deprecated by the API provider")
        suspend fun migrationsCancelImport(
            owner: String,
            repo: String,
        ): MigrationsCancelImportResult

        sealed interface MigrationsUpdateImportResult {
            data class OK(val value: Import) : MigrationsUpdateImportResult

            data class ServiceUnavailable(val value: BasicError) : MigrationsUpdateImportResult
        }

        @Deprecated("Deprecated by the API provider")
        suspend fun migrationsUpdateImport(
            owner: String,
            repo: String,
            body: MigrationsUpdateImportBody? = null,
        ): MigrationsUpdateImportResult

        interface Authors {
            @Serializable
            data class MigrationsMapCommitAuthorBody(val email: String? = null, val name: String? = null)

            sealed interface MigrationsGetCommitAuthorsResult {
                data class OK(val value: List<PorterAuthor>) : MigrationsGetCommitAuthorsResult

                data class NotFound(val value: BasicError) : MigrationsGetCommitAuthorsResult

                data class ServiceUnavailable(val value: BasicError) : MigrationsGetCommitAuthorsResult
            }

            @Deprecated("Deprecated by the API provider")
            suspend fun migrationsGetCommitAuthors(
                owner: String,
                repo: String,
                since: Long? = null,
            ): MigrationsGetCommitAuthorsResult

            sealed interface MigrationsMapCommitAuthorResult {
                data class OK(val value: PorterAuthor) : MigrationsMapCommitAuthorResult

                data class NotFound(val value: BasicError) : MigrationsMapCommitAuthorResult

                data class UnprocessableEntity(val value: ValidationError) : MigrationsMapCommitAuthorResult

                data class ServiceUnavailable(val value: BasicError) : MigrationsMapCommitAuthorResult
            }

            @Deprecated("Deprecated by the API provider")
            suspend fun migrationsMapCommitAuthor(
                owner: String,
                repo: String,
                authorId: Long,
                body: MigrationsMapCommitAuthorBody? = null,
            ): MigrationsMapCommitAuthorResult
        }

        interface LargeFiles {
            sealed interface MigrationsGetLargeFilesResult {
                data class OK(val value: List<PorterLargeFile>) : MigrationsGetLargeFilesResult

                data class ServiceUnavailable(val value: BasicError) : MigrationsGetLargeFilesResult
            }

            @Deprecated("Deprecated by the API provider")
            suspend fun migrationsGetLargeFiles(
                owner: String,
                repo: String,
            ): MigrationsGetLargeFilesResult
        }

        interface Lfs {
            @Serializable
            @JvmInline
            value class MigrationsSetLfsPreferenceBody(@SerialName("use_lfs") val useLfs: UseLfs) {
                @Serializable
                enum class UseLfs {
                    @SerialName("opt_in") OptIn, @SerialName("opt_out") OptOut;
                }
            }

            sealed interface MigrationsSetLfsPreferenceResult {
                data class OK(val value: Import) : MigrationsSetLfsPreferenceResult

                data class UnprocessableEntity(val value: ValidationError) : MigrationsSetLfsPreferenceResult

                data class ServiceUnavailable(val value: BasicError) : MigrationsSetLfsPreferenceResult
            }

            @Deprecated("Deprecated by the API provider")
            suspend fun migrationsSetLfsPreference(
                owner: String,
                repo: String,
                body: MigrationsSetLfsPreferenceBody,
            ): MigrationsSetLfsPreferenceResult
        }
    }

    interface Installation {
        sealed interface AppsGetRepoInstallationResult {
            data class OK(val value: Installation) : AppsGetRepoInstallationResult

            data class MovedPermanently(val value: BasicError) : AppsGetRepoInstallationResult

            data class NotFound(val value: BasicError) : AppsGetRepoInstallationResult
        }

        suspend fun appsGetRepoInstallation(
            owner: String,
            repo: String,
        ): AppsGetRepoInstallationResult
    }

    interface InteractionLimits {
        @Serializable(with = InteractionsGetRestrictionsForRepoResponse.Serializer::class)
        sealed interface InteractionsGetRestrictionsForRepoResponse {
            @Serializable
            @JvmInline
            value class CaseInteractionLimitResponse(val value: InteractionLimitResponse) : InteractionsGetRestrictionsForRepoResponse

            @Serializable
            data object Empty : InteractionsGetRestrictionsForRepoResponse

            object Serializer : KSerializer<InteractionsGetRestrictionsForRepoResponse> {
                @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
                override val descriptor: SerialDescriptor =
                    buildSerialDescriptor("io.github.nomisrev.api.Repos.InteractionLimits.InteractionsGetRestrictionsForRepoResponse", PolymorphicKind.SEALED) {
                        element("CaseInteractionLimitResponse", InteractionLimitResponse.serializer().descriptor)
                        element("Empty", Unit.serializer().descriptor)
                    }

                override fun deserialize(decoder: Decoder): InteractionsGetRestrictionsForRepoResponse {
                    val value = decoder.decodeSerializableValue(JsonElement.serializer())
                    val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                    return json.attemptDeserialize(
                        value,
                        CaseInteractionLimitResponse::class to { CaseInteractionLimitResponse(decodeFromJsonElement(InteractionLimitResponse.serializer(), it)) },
                        Empty::class to { decodeFromJsonElement(Empty.serializer(), it) },
                    )
                }

                override fun serialize(encoder: Encoder, value: InteractionsGetRestrictionsForRepoResponse) = when(value) {
                    is CaseInteractionLimitResponse -> encoder.encodeSerializableValue(InteractionLimitResponse.serializer(), value.value)
                    is Empty -> encoder.encodeSerializableValue(Empty.serializer(), value)
                }
            }
        }

        suspend fun interactionsGetRestrictionsForRepo(
            owner: String,
            repo: String,
        ): InteractionsGetRestrictionsForRepoResponse

        sealed interface InteractionsSetRestrictionsForRepoResult {
            data class OK(val value: InteractionLimitResponse) : InteractionsSetRestrictionsForRepoResult

            data object Conflict : InteractionsSetRestrictionsForRepoResult
        }

        suspend fun interactionsSetRestrictionsForRepo(
            owner: String,
            repo: String,
            body: InteractionLimit,
        ): InteractionsSetRestrictionsForRepoResult

        sealed interface InteractionsRemoveRestrictionsForRepoResult {
            data object NoContent : InteractionsRemoveRestrictionsForRepoResult

            data object Conflict : InteractionsRemoveRestrictionsForRepoResult
        }

        suspend fun interactionsRemoveRestrictionsForRepo(
            owner: String,
            repo: String,
        ): InteractionsRemoveRestrictionsForRepoResult
    }

    interface Invitations {
        @Serializable
        @JvmInline
        value class ReposUpdateInvitationBody(val permissions: Permissions? = null) {
            @Serializable
            enum class Permissions {
                @SerialName("read")
                Read,
                @SerialName("write")
                Write,
                @SerialName("maintain")
                Maintain,
                @SerialName("triage")
                Triage,
                @SerialName("admin")
                Admin;
            }
        }

        suspend fun reposListInvitations(
            owner: String,
            repo: String,
            page: Long = 1L,
            perPage: Long = 30L,
        ): List<RepositoryInvitation>

        suspend fun reposDeleteInvitation(
            owner: String,
            repo: String,
            invitationId: Long,
        ): Unit

        suspend fun reposUpdateInvitation(
            owner: String,
            repo: String,
            invitationId: Long,
            body: ReposUpdateInvitationBody? = null,
        ): RepositoryInvitation
    }

    interface Issues {
        val comments: Repos.Issues.CommentsApi

        val events: Repos.Issues.EventsApi

        val assignees: Repos.Issues.AssigneesApi

        val dependencies: Repos.Issues.Dependencies

        val issueFieldValues: Repos.Issues.IssueFieldValues

        val labels: Repos.Issues.LabelsApi

        val lock: Repos.Issues.Lock

        val parent: Repos.Issues.Parent

        val reactions: Repos.Issues.Reactions

        val subIssue: Repos.Issues.SubIssue

        val subIssues: Repos.Issues.SubIssues

        val timeline: Repos.Issues.Timeline

        @Serializable
        enum class Direction {
            @SerialName("asc") Asc, @SerialName("desc") Desc;
        }


        @Serializable
        data class IssuesCreateBody(
            val title: Title,
            val body: String? = null,
            val assignee: String? = null,
            val milestone: Milestone? = null,
            val labels: List<Labels>? = null,
            val assignees: List<String>? = null,
            val type: String? = null,
        ) {
            @Serializable(with = Title.Serializer::class)
            sealed interface Title {
                @Serializable
                @JvmInline
                value class CaseString(val value: String) : Title

                @Serializable
                @JvmInline
                value class CaseLong(val value: Long) : Title

                object Serializer : KSerializer<Title> {
                    @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
                    override val descriptor: SerialDescriptor =
                        buildSerialDescriptor("io.github.nomisrev.api.Repos.Issues.IssuesCreateBody.Title", PolymorphicKind.SEALED) {
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

                    override fun serialize(encoder: Encoder, value: Title) = when(value) {
                        is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                        is CaseLong -> encoder.encodeSerializableValue(Long.serializer(), value.value)
                    }
                }
            }

            @Serializable(with = Milestone.Serializer::class)
            sealed interface Milestone {
                @Serializable
                @JvmInline
                value class CaseString(val value: String) : Milestone

                @Serializable
                @JvmInline
                value class CaseLong(val value: Long) : Milestone

                object Serializer : KSerializer<Milestone> {
                    @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
                    override val descriptor: SerialDescriptor =
                        buildSerialDescriptor("io.github.nomisrev.api.Repos.Issues.IssuesCreateBody.Milestone", PolymorphicKind.SEALED) {
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

                    override fun serialize(encoder: Encoder, value: Milestone) = when(value) {
                        is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                        is CaseLong -> encoder.encodeSerializableValue(Long.serializer(), value.value)
                    }
                }
            }

            @Serializable(with = Labels.Serializer::class)
            sealed interface Labels {
                @Serializable
                @JvmInline
                value class CaseString(val value: String) : Labels

                @Serializable
                data class IdAndNameAndDescriptionAndColor(
                    val id: Long? = null,
                    val name: String? = null,
                    val description: String? = null,
                    val color: String? = null,
                ) : Labels

                object Serializer : KSerializer<Labels> {
                    @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
                    override val descriptor: SerialDescriptor =
                        buildSerialDescriptor("io.github.nomisrev.api.Repos.Issues.IssuesCreateBody.Labels", PolymorphicKind.SEALED) {
                            element("CaseString", String.serializer().descriptor)
                            element("IdAndNameAndDescriptionAndColor", IssuesCreateBody.Labels.IdAndNameAndDescriptionAndColor.serializer().descriptor)
                        }

                    override fun deserialize(decoder: Decoder): Labels {
                        val value = decoder.decodeSerializableValue(JsonElement.serializer())
                        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                        return json.attemptDeserialize(
                            value,
                            IdAndNameAndDescriptionAndColor::class to { decodeFromJsonElement(IssuesCreateBody.Labels.IdAndNameAndDescriptionAndColor.serializer(), it) },
                            CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                        )
                    }

                    override fun serialize(encoder: Encoder, value: Labels) = when(value) {
                        is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                        is IdAndNameAndDescriptionAndColor -> encoder.encodeSerializableValue(IssuesCreateBody.Labels.IdAndNameAndDescriptionAndColor.serializer(), value)
                    }
                }
            }
        }


        @Serializable
        data class IssuesCreateResponse(
            val code: String? = null,
            val message: String? = null,
            @SerialName("documentation_url") val documentationUrl: String? = null,
        )


        @Serializable
        data class IssuesUpdateBody(
            val title: Title? = null,
            val body: String? = null,
            val assignee: String? = null,
            val state: State? = null,
            @SerialName("state_reason") val stateReason: StateReason? = null,
            val milestone: Milestone? = null,
            val labels: List<Labels>? = null,
            val assignees: List<String>? = null,
            @SerialName("issue_field_values") val issueFieldValues: List<IssueFieldValues>? = null,
            val type: String? = null,
        ) {
            @Serializable(with = Title.Serializer::class)
            sealed interface Title {
                @Serializable
                @JvmInline
                value class CaseString(val value: String) : Title

                @Serializable
                @JvmInline
                value class CaseLong(val value: Long) : Title

                object Serializer : KSerializer<Title> {
                    @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
                    override val descriptor: SerialDescriptor =
                        buildSerialDescriptor("io.github.nomisrev.api.Repos.Issues.IssuesUpdateBody.Title", PolymorphicKind.SEALED) {
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

                    override fun serialize(encoder: Encoder, value: Title) = when(value) {
                        is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                        is CaseLong -> encoder.encodeSerializableValue(Long.serializer(), value.value)
                    }
                }
            }

            @Serializable
            enum class State {
                @SerialName("open") Open, @SerialName("closed") Closed;
            }

            @Serializable
            enum class StateReason {
                @SerialName("completed")
                Completed,
                @SerialName("not_planned")
                NotPlanned,
                @SerialName("duplicate")
                Duplicate,
                @SerialName("reopened")
                Reopened;
            }

            @Serializable(with = Milestone.Serializer::class)
            sealed interface Milestone {
                @Serializable
                @JvmInline
                value class CaseString(val value: String) : Milestone

                @Serializable
                @JvmInline
                value class CaseLong(val value: Long) : Milestone

                object Serializer : KSerializer<Milestone> {
                    @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
                    override val descriptor: SerialDescriptor =
                        buildSerialDescriptor("io.github.nomisrev.api.Repos.Issues.IssuesUpdateBody.Milestone", PolymorphicKind.SEALED) {
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

                    override fun serialize(encoder: Encoder, value: Milestone) = when(value) {
                        is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                        is CaseLong -> encoder.encodeSerializableValue(Long.serializer(), value.value)
                    }
                }
            }

            @Serializable(with = Labels.Serializer::class)
            sealed interface Labels {
                @Serializable
                @JvmInline
                value class CaseString(val value: String) : Labels

                @Serializable
                data class IdAndNameAndDescriptionAndColor(
                    val id: Long? = null,
                    val name: String? = null,
                    val description: String? = null,
                    val color: String? = null,
                ) : Labels

                object Serializer : KSerializer<Labels> {
                    @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
                    override val descriptor: SerialDescriptor =
                        buildSerialDescriptor("io.github.nomisrev.api.Repos.Issues.IssuesUpdateBody.Labels", PolymorphicKind.SEALED) {
                            element("CaseString", String.serializer().descriptor)
                            element("IdAndNameAndDescriptionAndColor", IssuesUpdateBody.Labels.IdAndNameAndDescriptionAndColor.serializer().descriptor)
                        }

                    override fun deserialize(decoder: Decoder): Labels {
                        val value = decoder.decodeSerializableValue(JsonElement.serializer())
                        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                        return json.attemptDeserialize(
                            value,
                            IdAndNameAndDescriptionAndColor::class to { decodeFromJsonElement(IssuesUpdateBody.Labels.IdAndNameAndDescriptionAndColor.serializer(), it) },
                            CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                        )
                    }

                    override fun serialize(encoder: Encoder, value: Labels) = when(value) {
                        is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                        is IdAndNameAndDescriptionAndColor -> encoder.encodeSerializableValue(IssuesUpdateBody.Labels.IdAndNameAndDescriptionAndColor.serializer(), value)
                    }
                }
            }

            @Serializable
            data class IssueFieldValues(@SerialName("field_id") val fieldId: Long, val value: Value) {
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
                            buildSerialDescriptor("io.github.nomisrev.api.Repos.Issues.IssuesUpdateBody.IssueFieldValues.Value", PolymorphicKind.SEALED) {
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


        @Serializable
        data class IssuesUpdateResponse(
            val code: String? = null,
            val message: String? = null,
            @SerialName("documentation_url") val documentationUrl: String? = null,
        )


        @Serializable
        enum class Sort {
            @SerialName("created") Created, @SerialName("updated") Updated, @SerialName("comments") Comments;
        }


        @Serializable
        enum class State {
            @SerialName("open") Open, @SerialName("closed") Closed, @SerialName("all") All;
        }

        sealed interface IssuesListForRepoResult {
            data class OK(val value: List<Issue>) : IssuesListForRepoResult

            data class MovedPermanently(val value: BasicError) : IssuesListForRepoResult

            data class NotFound(val value: BasicError) : IssuesListForRepoResult

            data class UnprocessableEntity(val value: ValidationError) : IssuesListForRepoResult
        }

        suspend fun issuesListForRepo(
            owner: String,
            repo: String,
            direction: Direction = Direction.Desc,
            page: Long = 1L,
            perPage: Long = 30L,
            sort: Sort = Sort.Created,
            state: State = State.Open,
            assignee: String? = null,
            creator: String? = null,
            labels: String? = null,
            mentioned: String? = null,
            milestone: String? = null,
            since: LocalDateTime? = null,
            type: String? = null,
        ): IssuesListForRepoResult

        sealed interface IssuesCreateResult {
            data class Created(val value: Issue) : IssuesCreateResult

            data class BadRequest(val value: BasicError) : IssuesCreateResult

            data class Forbidden(val value: BasicError) : IssuesCreateResult

            data class NotFound(val value: BasicError) : IssuesCreateResult

            data class Gone(val value: BasicError) : IssuesCreateResult

            data class UnprocessableEntity(val value: ValidationError) : IssuesCreateResult

            data class ServiceUnavailable(val value: IssuesCreateResponse) : IssuesCreateResult
        }

        suspend fun issuesCreate(
            owner: String,
            repo: String,
            body: IssuesCreateBody,
        ): IssuesCreateResult

        sealed interface IssuesGetResult {
            data class OK(val value: Issue) : IssuesGetResult

            data class MovedPermanently(val value: BasicError) : IssuesGetResult

            data object NotModified : IssuesGetResult

            data class NotFound(val value: BasicError) : IssuesGetResult

            data class Gone(val value: BasicError) : IssuesGetResult
        }

        suspend fun issuesGet(
            owner: String,
            repo: String,
            issueNumber: Long,
        ): IssuesGetResult

        sealed interface IssuesUpdateResult {
            data class OK(val value: Issue) : IssuesUpdateResult

            data class MovedPermanently(val value: BasicError) : IssuesUpdateResult

            data class Forbidden(val value: BasicError) : IssuesUpdateResult

            data class NotFound(val value: BasicError) : IssuesUpdateResult

            data class Gone(val value: BasicError) : IssuesUpdateResult

            data class UnprocessableEntity(val value: ValidationError) : IssuesUpdateResult

            data class ServiceUnavailable(val value: IssuesUpdateResponse) : IssuesUpdateResult
        }

        suspend fun issuesUpdate(
            owner: String,
            repo: String,
            issueNumber: Long,
            body: IssuesUpdateBody? = null,
        ): IssuesUpdateResult

        interface CommentsApi {
            val pin: Repos.Issues.CommentsApi.Pin

            val reactions: Repos.Issues.CommentsApi.ReactionsApi

            @Serializable
            @JvmInline
            value class IssuesCreateCommentBody(val body: String)


            @Serializable
            enum class IssuesListCommentsForRepoDirection {
                @SerialName("asc") Asc, @SerialName("desc") Desc;
            }


            @Serializable
            enum class IssuesListCommentsForRepoSort {
                @SerialName("created") Created, @SerialName("updated") Updated;
            }


            @Serializable
            @JvmInline
            value class IssuesUpdateCommentBody(val body: String)

            sealed interface IssuesListCommentsForRepoResult {
                data class OK(val value: List<IssueComment>) : IssuesListCommentsForRepoResult

                data class NotFound(val value: BasicError) : IssuesListCommentsForRepoResult

                data class UnprocessableEntity(val value: ValidationError) : IssuesListCommentsForRepoResult
            }

            suspend fun issuesListCommentsForRepo(
                owner: String,
                repo: String,
                page: Long = 1L,
                perPage: Long = 30L,
                sort: IssuesListCommentsForRepoSort = IssuesListCommentsForRepoSort.Created,
                direction: IssuesListCommentsForRepoDirection? = null,
                since: LocalDateTime? = null,
            ): IssuesListCommentsForRepoResult

            sealed interface IssuesGetCommentResult {
                data class OK(val value: IssueComment) : IssuesGetCommentResult

                data class NotFound(val value: BasicError) : IssuesGetCommentResult
            }

            suspend fun issuesGetComment(
                owner: String,
                repo: String,
                commentId: Long,
            ): IssuesGetCommentResult

            suspend fun issuesDeleteComment(
                owner: String,
                repo: String,
                commentId: Long,
            ): Unit

            sealed interface IssuesUpdateCommentResult {
                data class OK(val value: IssueComment) : IssuesUpdateCommentResult

                data class UnprocessableEntity(val value: ValidationError) : IssuesUpdateCommentResult
            }

            suspend fun issuesUpdateComment(
                owner: String,
                repo: String,
                commentId: Long,
                body: IssuesUpdateCommentBody,
            ): IssuesUpdateCommentResult

            sealed interface IssuesListCommentsResult {
                data class OK(val value: List<IssueComment>) : IssuesListCommentsResult

                data class NotFound(val value: BasicError) : IssuesListCommentsResult

                data class Gone(val value: BasicError) : IssuesListCommentsResult
            }

            suspend fun issuesListComments(
                owner: String,
                repo: String,
                issueNumber: Long,
                page: Long = 1L,
                perPage: Long = 30L,
                since: LocalDateTime? = null,
            ): IssuesListCommentsResult

            sealed interface IssuesCreateCommentResult {
                data class Created(val value: IssueComment) : IssuesCreateCommentResult

                data class Forbidden(val value: BasicError) : IssuesCreateCommentResult

                data class NotFound(val value: BasicError) : IssuesCreateCommentResult

                data class Gone(val value: BasicError) : IssuesCreateCommentResult

                data class UnprocessableEntity(val value: ValidationError) : IssuesCreateCommentResult
            }

            suspend fun issuesCreateComment(
                owner: String,
                repo: String,
                issueNumber: Long,
                body: IssuesCreateCommentBody,
            ): IssuesCreateCommentResult

            interface Pin {
                @Serializable
                data class IssuesUnpinCommentResponse(
                    val code: String? = null,
                    val message: String? = null,
                    @SerialName("documentation_url") val documentationUrl: String? = null,
                )

                sealed interface IssuesPinCommentResult {
                    data class OK(val value: IssueComment) : IssuesPinCommentResult

                    data class Unauthorized(val value: BasicError) : IssuesPinCommentResult

                    data class Forbidden(val value: BasicError) : IssuesPinCommentResult

                    data class NotFound(val value: BasicError) : IssuesPinCommentResult

                    data class Gone(val value: BasicError) : IssuesPinCommentResult

                    data class UnprocessableEntity(val value: ValidationError) : IssuesPinCommentResult
                }

                suspend fun issuesPinComment(
                    owner: String,
                    repo: String,
                    commentId: Long,
                ): IssuesPinCommentResult

                sealed interface IssuesUnpinCommentResult {
                    data object NoContent : IssuesUnpinCommentResult

                    data class Unauthorized(val value: BasicError) : IssuesUnpinCommentResult

                    data class Forbidden(val value: BasicError) : IssuesUnpinCommentResult

                    data class NotFound(val value: BasicError) : IssuesUnpinCommentResult

                    data class Gone(val value: BasicError) : IssuesUnpinCommentResult

                    data class ServiceUnavailable(val value: IssuesUnpinCommentResponse) : IssuesUnpinCommentResult
                }

                suspend fun issuesUnpinComment(
                    owner: String,
                    repo: String,
                    commentId: Long,
                ): IssuesUnpinCommentResult
            }

            interface ReactionsApi {
                @Serializable
                enum class Content {
                    @SerialName("+1")
                    `+1`,
                    @SerialName("-1")
                    `-1`,
                    @SerialName("laugh")
                    Laugh,
                    @SerialName("confused")
                    Confused,
                    @SerialName("heart")
                    Heart,
                    @SerialName("hooray")
                    Hooray,
                    @SerialName("rocket")
                    Rocket,
                    @SerialName("eyes")
                    Eyes;
                }


                @Serializable
                @JvmInline
                value class ReactionsCreateForIssueCommentBody(val content: Content) {
                    @Serializable
                    enum class Content {
                        @SerialName("+1")
                        `+1`,
                        @SerialName("-1")
                        `-1`,
                        @SerialName("laugh")
                        Laugh,
                        @SerialName("confused")
                        Confused,
                        @SerialName("heart")
                        Heart,
                        @SerialName("hooray")
                        Hooray,
                        @SerialName("rocket")
                        Rocket,
                        @SerialName("eyes")
                        Eyes;
                    }
                }

                sealed interface ReactionsListForIssueCommentResult {
                    data class OK(val value: List<Reaction>) : ReactionsListForIssueCommentResult

                    data class NotFound(val value: BasicError) : ReactionsListForIssueCommentResult
                }

                suspend fun reactionsListForIssueComment(
                    owner: String,
                    repo: String,
                    commentId: Long,
                    page: Long = 1L,
                    perPage: Long = 30L,
                    content: Content? = null,
                ): ReactionsListForIssueCommentResult

                sealed interface ReactionsCreateForIssueCommentResult {
                    data class OK(val value: Reaction) : ReactionsCreateForIssueCommentResult

                    data class Created(val value: Reaction) : ReactionsCreateForIssueCommentResult

                    data class UnprocessableEntity(val value: ValidationError) : ReactionsCreateForIssueCommentResult
                }

                suspend fun reactionsCreateForIssueComment(
                    owner: String,
                    repo: String,
                    commentId: Long,
                    body: ReactionsCreateForIssueCommentBody,
                ): ReactionsCreateForIssueCommentResult

                suspend fun reactionsDeleteForIssueComment(
                    owner: String,
                    repo: String,
                    commentId: Long,
                    reactionId: Long,
                ): Unit
            }
        }

        interface EventsApi {
            sealed interface IssuesListEventsForRepoResult {
                data class OK(val value: List<IssueEvent>) : IssuesListEventsForRepoResult

                data class UnprocessableEntity(val value: ValidationError) : IssuesListEventsForRepoResult
            }

            suspend fun issuesListEventsForRepo(
                owner: String,
                repo: String,
                page: Long = 1L,
                perPage: Long = 30L,
            ): IssuesListEventsForRepoResult

            sealed interface IssuesGetEventResult {
                data class OK(val value: IssueEvent) : IssuesGetEventResult

                data class Forbidden(val value: BasicError) : IssuesGetEventResult

                data class NotFound(val value: BasicError) : IssuesGetEventResult

                data class Gone(val value: BasicError) : IssuesGetEventResult
            }

            suspend fun issuesGetEvent(
                owner: String,
                repo: String,
                eventId: Long,
            ): IssuesGetEventResult

            sealed interface IssuesListEventsResult {
                data class OK(val value: List<IssueEventForIssue>) : IssuesListEventsResult

                data class Gone(val value: BasicError) : IssuesListEventsResult
            }

            suspend fun issuesListEvents(
                owner: String,
                repo: String,
                issueNumber: Long,
                page: Long = 1L,
                perPage: Long = 30L,
            ): IssuesListEventsResult
        }

        interface AssigneesApi {
            @Serializable
            @JvmInline
            value class IssuesAddAssigneesBody(val assignees: List<String>? = null)


            @Serializable
            @JvmInline
            value class IssuesRemoveAssigneesBody(val assignees: List<String>? = null)

            suspend fun issuesAddAssignees(
                owner: String,
                repo: String,
                issueNumber: Long,
                body: IssuesAddAssigneesBody? = null,
            ): Issue

            suspend fun issuesRemoveAssignees(
                owner: String,
                repo: String,
                issueNumber: Long,
                body: IssuesRemoveAssigneesBody? = null,
            ): Issue

            sealed interface IssuesCheckUserCanBeAssignedToIssueResult {
                data object NoContent : IssuesCheckUserCanBeAssignedToIssueResult

                data class NotFound(val value: BasicError) : IssuesCheckUserCanBeAssignedToIssueResult
            }

            suspend fun issuesCheckUserCanBeAssignedToIssue(
                owner: String,
                repo: String,
                issueNumber: Long,
                assignee: String,
            ): IssuesCheckUserCanBeAssignedToIssueResult
        }

        interface Dependencies {
            val blockedBy: Repos.Issues.Dependencies.BlockedBy

            val blocking: Repos.Issues.Dependencies.Blocking

            interface BlockedBy {
                @Serializable
                @JvmInline
                value class IssuesAddBlockedByDependencyBody(@SerialName("issue_id") val issueId: Long)

                sealed interface IssuesListDependenciesBlockedByResult {
                    data class OK(val value: List<Issue>) : IssuesListDependenciesBlockedByResult

                    data class MovedPermanently(val value: BasicError) : IssuesListDependenciesBlockedByResult

                    data class NotFound(val value: BasicError) : IssuesListDependenciesBlockedByResult

                    data class Gone(val value: BasicError) : IssuesListDependenciesBlockedByResult
                }

                suspend fun issuesListDependenciesBlockedBy(
                    owner: String,
                    repo: String,
                    issueNumber: Long,
                    page: Long = 1L,
                    perPage: Long = 30L,
                ): IssuesListDependenciesBlockedByResult

                sealed interface IssuesAddBlockedByDependencyResult {
                    data class Created(val value: Issue) : IssuesAddBlockedByDependencyResult

                    data class MovedPermanently(val value: BasicError) : IssuesAddBlockedByDependencyResult

                    data class Forbidden(val value: BasicError) : IssuesAddBlockedByDependencyResult

                    data class NotFound(val value: BasicError) : IssuesAddBlockedByDependencyResult

                    data class Gone(val value: BasicError) : IssuesAddBlockedByDependencyResult

                    data class UnprocessableEntity(val value: ValidationError) : IssuesAddBlockedByDependencyResult
                }

                suspend fun issuesAddBlockedByDependency(
                    owner: String,
                    repo: String,
                    issueNumber: Long,
                    body: IssuesAddBlockedByDependencyBody,
                ): IssuesAddBlockedByDependencyResult

                sealed interface IssuesRemoveDependencyBlockedByResult {
                    data class OK(val value: Issue) : IssuesRemoveDependencyBlockedByResult

                    data class MovedPermanently(val value: BasicError) : IssuesRemoveDependencyBlockedByResult

                    data class BadRequest(val value: BasicError) : IssuesRemoveDependencyBlockedByResult

                    data class Unauthorized(val value: BasicError) : IssuesRemoveDependencyBlockedByResult

                    data class Forbidden(val value: BasicError) : IssuesRemoveDependencyBlockedByResult

                    data class NotFound(val value: BasicError) : IssuesRemoveDependencyBlockedByResult

                    data class Gone(val value: BasicError) : IssuesRemoveDependencyBlockedByResult
                }

                suspend fun issuesRemoveDependencyBlockedBy(
                    owner: String,
                    repo: String,
                    issueNumber: Long,
                    issueId: Long,
                ): IssuesRemoveDependencyBlockedByResult
            }

            interface Blocking {
                sealed interface IssuesListDependenciesBlockingResult {
                    data class OK(val value: List<Issue>) : IssuesListDependenciesBlockingResult

                    data class MovedPermanently(val value: BasicError) : IssuesListDependenciesBlockingResult

                    data class NotFound(val value: BasicError) : IssuesListDependenciesBlockingResult

                    data class Gone(val value: BasicError) : IssuesListDependenciesBlockingResult
                }

                suspend fun issuesListDependenciesBlocking(
                    owner: String,
                    repo: String,
                    issueNumber: Long,
                    page: Long = 1L,
                    perPage: Long = 30L,
                ): IssuesListDependenciesBlockingResult
            }
        }

        interface IssueFieldValues {
            sealed interface IssuesListIssueFieldValuesForIssueResult {
                data class OK(val value: List<IssueFieldValue>) : IssuesListIssueFieldValuesForIssueResult

                data class MovedPermanently(val value: BasicError) : IssuesListIssueFieldValuesForIssueResult

                data class NotFound(val value: BasicError) : IssuesListIssueFieldValuesForIssueResult

                data class Gone(val value: BasicError) : IssuesListIssueFieldValuesForIssueResult
            }

            suspend fun issuesListIssueFieldValuesForIssue(
                owner: String,
                repo: String,
                issueNumber: Long,
                page: Long = 1L,
                perPage: Long = 30L,
            ): IssuesListIssueFieldValuesForIssueResult
        }

        interface LabelsApi {
            @Serializable(with = IssuesAddLabelsBody.Serializer::class)
            sealed interface IssuesAddLabelsBody {
                @Serializable
                @JvmInline
                value class Labels(val labels: List<String>? = null) : IssuesAddLabelsBody

                @Serializable
                @JvmInline
                value class CaseStrings(val value: List<String>) : IssuesAddLabelsBody

                @Serializable
                @JvmInline
                value class Names(val value: List<Name>) : IssuesAddLabelsBody {
                    @Serializable
                    @JvmInline
                    value class Name(val name: String)
                }

                object Serializer : KSerializer<IssuesAddLabelsBody> {
                    @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
                    override val descriptor: SerialDescriptor =
                        buildSerialDescriptor("io.github.nomisrev.api.Repos.Issues.LabelsApi.IssuesAddLabelsBody", PolymorphicKind.SEALED) {
                            element("Labels", IssuesAddLabelsBody.Labels.serializer().descriptor)
                            element("CaseStrings", ListSerializer(String.serializer()).descriptor)
                            element("Names", ListSerializer(Names.Name.serializer()).descriptor)
                        }

                    override fun deserialize(decoder: Decoder): IssuesAddLabelsBody {
                        val value = decoder.decodeSerializableValue(JsonElement.serializer())
                        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                        return json.attemptDeserialize(
                            value,
                            Labels::class to { decodeFromJsonElement(IssuesAddLabelsBody.Labels.serializer(), it) },
                            CaseStrings::class to { CaseStrings(decodeFromJsonElement(ListSerializer(String.serializer()), it)) },
                            Names::class to { Names(decodeFromJsonElement(ListSerializer(Names.Name.serializer()), it)) },
                        )
                    }

                    override fun serialize(encoder: Encoder, value: IssuesAddLabelsBody) = when(value) {
                        is Labels -> encoder.encodeSerializableValue(IssuesAddLabelsBody.Labels.serializer(), value)
                        is CaseStrings -> encoder.encodeSerializableValue(ListSerializer(String.serializer()), value.value)
                        is Names -> encoder.encodeSerializableValue(ListSerializer(Names.Name.serializer()), value.value)
                    }
                }
            }


            @Serializable(with = IssuesSetLabelsBody.Serializer::class)
            sealed interface IssuesSetLabelsBody {
                @Serializable
                @JvmInline
                value class Labels(val labels: List<String>? = null) : IssuesSetLabelsBody

                @Serializable
                @JvmInline
                value class CaseStrings(val value: List<String>) : IssuesSetLabelsBody

                @Serializable
                @JvmInline
                value class Labels(val labels: List<Labels>? = null) : IssuesSetLabelsBody {
                    @Serializable
                    @JvmInline
                    value class Labels(val name: String)
                }

                @Serializable
                @JvmInline
                value class Names(val value: List<Name>) : IssuesSetLabelsBody {
                    @Serializable
                    @JvmInline
                    value class Name(val name: String)
                }

                @Serializable
                @JvmInline
                value class CaseString(val value: String) : IssuesSetLabelsBody

                object Serializer : KSerializer<IssuesSetLabelsBody> {
                    @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
                    override val descriptor: SerialDescriptor =
                        buildSerialDescriptor("io.github.nomisrev.api.Repos.Issues.LabelsApi.IssuesSetLabelsBody", PolymorphicKind.SEALED) {
                            element("Labels", IssuesSetLabelsBody.Labels.serializer().descriptor)
                            element("CaseStrings", ListSerializer(String.serializer()).descriptor)
                            element("Labels", IssuesSetLabelsBody.Labels.serializer().descriptor)
                            element("Names", ListSerializer(Names.Name.serializer()).descriptor)
                            element("CaseString", String.serializer().descriptor)
                        }

                    override fun deserialize(decoder: Decoder): IssuesSetLabelsBody {
                        val value = decoder.decodeSerializableValue(JsonElement.serializer())
                        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                        return json.attemptDeserialize(
                            value,
                            Labels::class to { decodeFromJsonElement(IssuesSetLabelsBody.Labels.serializer(), it) },
                            Labels::class to { decodeFromJsonElement(IssuesSetLabelsBody.Labels.serializer(), it) },
                            CaseStrings::class to { CaseStrings(decodeFromJsonElement(ListSerializer(String.serializer()), it)) },
                            Names::class to { Names(decodeFromJsonElement(ListSerializer(Names.Name.serializer()), it)) },
                            CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                        )
                    }

                    override fun serialize(encoder: Encoder, value: IssuesSetLabelsBody) = when(value) {
                        is Labels -> encoder.encodeSerializableValue(IssuesSetLabelsBody.Labels.serializer(), value)
                        is CaseStrings -> encoder.encodeSerializableValue(ListSerializer(String.serializer()), value.value)
                        is Labels -> encoder.encodeSerializableValue(IssuesSetLabelsBody.Labels.serializer(), value)
                        is Names -> encoder.encodeSerializableValue(ListSerializer(Names.Name.serializer()), value.value)
                        is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                    }
                }
            }

            sealed interface IssuesListLabelsOnIssueResult {
                data class OK(val value: List<Label>) : IssuesListLabelsOnIssueResult

                data class MovedPermanently(val value: BasicError) : IssuesListLabelsOnIssueResult

                data class NotFound(val value: BasicError) : IssuesListLabelsOnIssueResult

                data class Gone(val value: BasicError) : IssuesListLabelsOnIssueResult
            }

            suspend fun issuesListLabelsOnIssue(
                owner: String,
                repo: String,
                issueNumber: Long,
                page: Long = 1L,
                perPage: Long = 30L,
            ): IssuesListLabelsOnIssueResult

            sealed interface IssuesSetLabelsResult {
                data class OK(val value: List<Label>) : IssuesSetLabelsResult

                data class MovedPermanently(val value: BasicError) : IssuesSetLabelsResult

                data class NotFound(val value: BasicError) : IssuesSetLabelsResult

                data class Gone(val value: BasicError) : IssuesSetLabelsResult

                data class UnprocessableEntity(val value: ValidationError) : IssuesSetLabelsResult
            }

            suspend fun issuesSetLabels(
                owner: String,
                repo: String,
                issueNumber: Long,
                body: IssuesSetLabelsBody? = null,
            ): IssuesSetLabelsResult

            sealed interface IssuesAddLabelsResult {
                data class OK(val value: List<Label>) : IssuesAddLabelsResult

                data class MovedPermanently(val value: BasicError) : IssuesAddLabelsResult

                data class NotFound(val value: BasicError) : IssuesAddLabelsResult

                data class Gone(val value: BasicError) : IssuesAddLabelsResult

                data class UnprocessableEntity(val value: ValidationError) : IssuesAddLabelsResult
            }

            suspend fun issuesAddLabels(
                owner: String,
                repo: String,
                issueNumber: Long,
                body: IssuesAddLabelsBody? = null,
            ): IssuesAddLabelsResult

            sealed interface IssuesRemoveAllLabelsResult {
                data object NoContent : IssuesRemoveAllLabelsResult

                data class MovedPermanently(val value: BasicError) : IssuesRemoveAllLabelsResult

                data class NotFound(val value: BasicError) : IssuesRemoveAllLabelsResult

                data class Gone(val value: BasicError) : IssuesRemoveAllLabelsResult
            }

            suspend fun issuesRemoveAllLabels(
                owner: String,
                repo: String,
                issueNumber: Long,
            ): IssuesRemoveAllLabelsResult

            sealed interface IssuesRemoveLabelResult {
                data class OK(val value: List<Label>) : IssuesRemoveLabelResult

                data class MovedPermanently(val value: BasicError) : IssuesRemoveLabelResult

                data class NotFound(val value: BasicError) : IssuesRemoveLabelResult

                data class Gone(val value: BasicError) : IssuesRemoveLabelResult
            }

            suspend fun issuesRemoveLabel(
                owner: String,
                repo: String,
                issueNumber: Long,
                name: String,
            ): IssuesRemoveLabelResult
        }

        interface Lock {
            @Serializable
            @JvmInline
            value class IssuesLockBody(@SerialName("lock_reason") val lockReason: LockReason? = null) {
                @Serializable
                enum class LockReason {
                    @SerialName("off-topic")
                    OffTopic,
                    @SerialName("too heated")
                    TooHeated,
                    @SerialName("resolved")
                    Resolved,
                    @SerialName("spam")
                    Spam;
                }
            }

            sealed interface IssuesLockResult {
                data object NoContent : IssuesLockResult

                data class Forbidden(val value: BasicError) : IssuesLockResult

                data class NotFound(val value: BasicError) : IssuesLockResult

                data class Gone(val value: BasicError) : IssuesLockResult

                data class UnprocessableEntity(val value: ValidationError) : IssuesLockResult
            }

            suspend fun issuesLock(
                owner: String,
                repo: String,
                issueNumber: Long,
                body: IssuesLockBody? = null,
            ): IssuesLockResult

            sealed interface IssuesUnlockResult {
                data object NoContent : IssuesUnlockResult

                data class Forbidden(val value: BasicError) : IssuesUnlockResult

                data class NotFound(val value: BasicError) : IssuesUnlockResult
            }

            suspend fun issuesUnlock(
                owner: String,
                repo: String,
                issueNumber: Long,
            ): IssuesUnlockResult
        }

        interface Parent {
            sealed interface IssuesGetParentResult {
                data class OK(val value: Issue) : IssuesGetParentResult

                data class MovedPermanently(val value: BasicError) : IssuesGetParentResult

                data class NotFound(val value: BasicError) : IssuesGetParentResult

                data class Gone(val value: BasicError) : IssuesGetParentResult
            }

            suspend fun issuesGetParent(
                owner: String,
                repo: String,
                issueNumber: Long,
            ): IssuesGetParentResult
        }

        interface Reactions {
            @Serializable
            enum class Content {
                @SerialName("+1")
                `+1`,
                @SerialName("-1")
                `-1`,
                @SerialName("laugh")
                Laugh,
                @SerialName("confused")
                Confused,
                @SerialName("heart")
                Heart,
                @SerialName("hooray")
                Hooray,
                @SerialName("rocket")
                Rocket,
                @SerialName("eyes")
                Eyes;
            }


            @Serializable
            @JvmInline
            value class ReactionsCreateForIssueBody(val content: Content) {
                @Serializable
                enum class Content {
                    @SerialName("+1")
                    `+1`,
                    @SerialName("-1")
                    `-1`,
                    @SerialName("laugh")
                    Laugh,
                    @SerialName("confused")
                    Confused,
                    @SerialName("heart")
                    Heart,
                    @SerialName("hooray")
                    Hooray,
                    @SerialName("rocket")
                    Rocket,
                    @SerialName("eyes")
                    Eyes;
                }
            }

            sealed interface ReactionsListForIssueResult {
                data class OK(val value: List<Reaction>) : ReactionsListForIssueResult

                data class NotFound(val value: BasicError) : ReactionsListForIssueResult

                data class Gone(val value: BasicError) : ReactionsListForIssueResult
            }

            suspend fun reactionsListForIssue(
                owner: String,
                repo: String,
                issueNumber: Long,
                page: Long = 1L,
                perPage: Long = 30L,
                content: Content? = null,
            ): ReactionsListForIssueResult

            sealed interface ReactionsCreateForIssueResult {
                data class OK(val value: Reaction) : ReactionsCreateForIssueResult

                data class Created(val value: Reaction) : ReactionsCreateForIssueResult

                data class UnprocessableEntity(val value: ValidationError) : ReactionsCreateForIssueResult
            }

            suspend fun reactionsCreateForIssue(
                owner: String,
                repo: String,
                issueNumber: Long,
                body: ReactionsCreateForIssueBody,
            ): ReactionsCreateForIssueResult

            suspend fun reactionsDeleteForIssue(
                owner: String,
                repo: String,
                issueNumber: Long,
                reactionId: Long,
            ): Unit
        }

        interface SubIssue {
            @Serializable
            @JvmInline
            value class IssuesRemoveSubIssueBody(@SerialName("sub_issue_id") val subIssueId: Long)

            sealed interface IssuesRemoveSubIssueResult {
                data class OK(val value: Issue) : IssuesRemoveSubIssueResult

                data class BadRequest(val value: BasicError) : IssuesRemoveSubIssueResult

                data class NotFound(val value: BasicError) : IssuesRemoveSubIssueResult
            }

            suspend fun issuesRemoveSubIssue(
                owner: String,
                repo: String,
                issueNumber: Long,
                body: IssuesRemoveSubIssueBody,
            ): IssuesRemoveSubIssueResult
        }

        interface SubIssues {
            val priority: Repos.Issues.SubIssues.Priority

            @Serializable
            data class IssuesAddSubIssueBody(
                @SerialName("sub_issue_id") val subIssueId: Long,
                @SerialName("replace_parent") val replaceParent: Boolean? = null,
            )

            sealed interface IssuesListSubIssuesResult {
                data class OK(val value: List<Issue>) : IssuesListSubIssuesResult

                data class NotFound(val value: BasicError) : IssuesListSubIssuesResult

                data class Gone(val value: BasicError) : IssuesListSubIssuesResult
            }

            suspend fun issuesListSubIssues(
                owner: String,
                repo: String,
                issueNumber: Long,
                page: Long = 1L,
                perPage: Long = 30L,
            ): IssuesListSubIssuesResult

            sealed interface IssuesAddSubIssueResult {
                data class Created(val value: Issue) : IssuesAddSubIssueResult

                data class Forbidden(val value: BasicError) : IssuesAddSubIssueResult

                data class NotFound(val value: BasicError) : IssuesAddSubIssueResult

                data class Gone(val value: BasicError) : IssuesAddSubIssueResult

                data class UnprocessableEntity(val value: ValidationError) : IssuesAddSubIssueResult
            }

            suspend fun issuesAddSubIssue(
                owner: String,
                repo: String,
                issueNumber: Long,
                body: IssuesAddSubIssueBody,
            ): IssuesAddSubIssueResult

            interface Priority {
                @Serializable
                data class IssuesReprioritizeSubIssueBody(
                    @SerialName("sub_issue_id") val subIssueId: Long,
                    @SerialName("after_id") val afterId: Long? = null,
                    @SerialName("before_id") val beforeId: Long? = null,
                )


                @Serializable
                data class IssuesReprioritizeSubIssueResponse(
                    val code: String? = null,
                    val message: String? = null,
                    @SerialName("documentation_url") val documentationUrl: String? = null,
                )

                sealed interface IssuesReprioritizeSubIssueResult {
                    data class OK(val value: Issue) : IssuesReprioritizeSubIssueResult

                    data class Forbidden(val value: BasicError) : IssuesReprioritizeSubIssueResult

                    data class NotFound(val value: BasicError) : IssuesReprioritizeSubIssueResult

                    data class UnprocessableEntity(val value: ValidationErrorSimple) : IssuesReprioritizeSubIssueResult

                    data class ServiceUnavailable(val value: IssuesReprioritizeSubIssueResponse) : IssuesReprioritizeSubIssueResult
                }

                suspend fun issuesReprioritizeSubIssue(
                    owner: String,
                    repo: String,
                    issueNumber: Long,
                    body: IssuesReprioritizeSubIssueBody,
                ): IssuesReprioritizeSubIssueResult
            }
        }

        interface Timeline {
            sealed interface IssuesListEventsForTimelineResult {
                data class OK(val value: List<TimelineIssueEvents>) : IssuesListEventsForTimelineResult

                data class NotFound(val value: BasicError) : IssuesListEventsForTimelineResult

                data class Gone(val value: BasicError) : IssuesListEventsForTimelineResult
            }

            suspend fun issuesListEventsForTimeline(
                owner: String,
                repo: String,
                issueNumber: Long,
                page: Long = 1L,
                perPage: Long = 30L,
            ): IssuesListEventsForTimelineResult
        }
    }

    interface Keys {
        @Serializable
        data class ReposCreateDeployKeyBody(
            val title: String? = null,
            val key: String,
            @SerialName("read_only") val readOnly: Boolean? = null,
        )

        suspend fun reposListDeployKeys(
            owner: String,
            repo: String,
            page: Long = 1L,
            perPage: Long = 30L,
        ): List<DeployKey>

        sealed interface ReposCreateDeployKeyResult {
            data class Created(val value: DeployKey) : ReposCreateDeployKeyResult

            data class UnprocessableEntity(val value: ValidationError) : ReposCreateDeployKeyResult
        }

        suspend fun reposCreateDeployKey(
            owner: String,
            repo: String,
            body: ReposCreateDeployKeyBody,
        ): ReposCreateDeployKeyResult

        sealed interface ReposGetDeployKeyResult {
            data class OK(val value: DeployKey) : ReposGetDeployKeyResult

            data class NotFound(val value: BasicError) : ReposGetDeployKeyResult
        }

        suspend fun reposGetDeployKey(
            owner: String,
            repo: String,
            keyId: Long,
        ): ReposGetDeployKeyResult

        suspend fun reposDeleteDeployKey(
            owner: String,
            repo: String,
            keyId: Long,
        ): Unit
    }

    interface Labels {
        @Serializable
        data class IssuesCreateLabelBody(val name: String, val color: String? = null, val description: String? = null)


        @Serializable
        data class IssuesUpdateLabelBody(
            @SerialName("new_name") val newName: String? = null,
            val color: String? = null,
            val description: String? = null,
        )

        sealed interface IssuesListLabelsForRepoResult {
            data class OK(val value: List<Label>) : IssuesListLabelsForRepoResult

            data class NotFound(val value: BasicError) : IssuesListLabelsForRepoResult
        }

        suspend fun issuesListLabelsForRepo(
            owner: String,
            repo: String,
            page: Long = 1L,
            perPage: Long = 30L,
        ): IssuesListLabelsForRepoResult

        sealed interface IssuesCreateLabelResult {
            data class Created(val value: Label) : IssuesCreateLabelResult

            data class NotFound(val value: BasicError) : IssuesCreateLabelResult

            data class UnprocessableEntity(val value: ValidationError) : IssuesCreateLabelResult
        }

        suspend fun issuesCreateLabel(
            owner: String,
            repo: String,
            body: IssuesCreateLabelBody,
        ): IssuesCreateLabelResult

        sealed interface IssuesGetLabelResult {
            data class OK(val value: Label) : IssuesGetLabelResult

            data class NotFound(val value: BasicError) : IssuesGetLabelResult
        }

        suspend fun issuesGetLabel(
            owner: String,
            repo: String,
            name: String,
        ): IssuesGetLabelResult

        suspend fun issuesDeleteLabel(
            owner: String,
            repo: String,
            name: String,
        ): Unit

        suspend fun issuesUpdateLabel(
            owner: String,
            repo: String,
            name: String,
            body: IssuesUpdateLabelBody? = null,
        ): Label
    }

    interface Languages {
        suspend fun reposListLanguages(
            owner: String,
            repo: String,
        ): Language
    }

    interface License {
        sealed interface LicensesGetForRepoResult {
            data class OK(val value: LicenseContent) : LicensesGetForRepoResult

            data class NotFound(val value: BasicError) : LicensesGetForRepoResult
        }

        suspend fun licensesGetForRepo(
            owner: String,
            repo: String,
            ref: CodeScanningRef? = null,
        ): LicensesGetForRepoResult
    }

    interface MergeUpstream {
        @Serializable
        @JvmInline
        value class ReposMergeUpstreamBody(val branch: String)

        sealed interface ReposMergeUpstreamResult {
            data class OK(val value: MergedUpstream) : ReposMergeUpstreamResult

            data object Conflict : ReposMergeUpstreamResult

            data object UnprocessableEntity : ReposMergeUpstreamResult
        }

        suspend fun reposMergeUpstream(
            owner: String,
            repo: String,
            body: ReposMergeUpstreamBody,
        ): ReposMergeUpstreamResult
    }

    interface Merges {
        @Serializable
        data class ReposMergeBody(
            val base: String,
            val head: String,
            @SerialName("commit_message") val commitMessage: String? = null,
        )

        sealed interface ReposMergeResult {
            data class Created(val value: Commit) : ReposMergeResult

            data object NoContent : ReposMergeResult

            data class Forbidden(val value: BasicError) : ReposMergeResult

            data object NotFound : ReposMergeResult

            data object Conflict : ReposMergeResult

            data class UnprocessableEntity(val value: ValidationError) : ReposMergeResult
        }

        suspend fun reposMerge(
            owner: String,
            repo: String,
            body: ReposMergeBody,
        ): ReposMergeResult
    }

    interface Milestones {
        val labels: Repos.Milestones.LabelsApi

        @Serializable
        enum class Direction {
            @SerialName("asc") Asc, @SerialName("desc") Desc;
        }


        @Serializable
        data class IssuesCreateMilestoneBody(
            val title: String,
            val state: State? = null,
            val description: String? = null,
            @SerialName("due_on") val dueOn: LocalDateTime? = null,
        ) {
            @Serializable
            enum class State {
                @SerialName("open") Open, @SerialName("closed") Closed;
            }
        }


        @Serializable
        data class IssuesUpdateMilestoneBody(
            val title: String? = null,
            val state: State? = null,
            val description: String? = null,
            @SerialName("due_on") val dueOn: LocalDateTime? = null,
        ) {
            @Serializable
            enum class State {
                @SerialName("open") Open, @SerialName("closed") Closed;
            }
        }


        @Serializable
        enum class Sort {
            @SerialName("due_on") DueOn, @SerialName("completeness") Completeness;
        }


        @Serializable
        enum class State {
            @SerialName("open") Open, @SerialName("closed") Closed, @SerialName("all") All;
        }

        sealed interface IssuesListMilestonesResult {
            data class OK(val value: List<Milestone>) : IssuesListMilestonesResult

            data class NotFound(val value: BasicError) : IssuesListMilestonesResult
        }

        suspend fun issuesListMilestones(
            owner: String,
            repo: String,
            direction: Direction = Direction.Asc,
            page: Long = 1L,
            perPage: Long = 30L,
            sort: Sort = Sort.DueOn,
            state: State = State.Open,
        ): IssuesListMilestonesResult

        sealed interface IssuesCreateMilestoneResult {
            data class Created(val value: Milestone) : IssuesCreateMilestoneResult

            data class NotFound(val value: BasicError) : IssuesCreateMilestoneResult

            data class UnprocessableEntity(val value: ValidationError) : IssuesCreateMilestoneResult
        }

        suspend fun issuesCreateMilestone(
            owner: String,
            repo: String,
            body: IssuesCreateMilestoneBody,
        ): IssuesCreateMilestoneResult

        sealed interface IssuesGetMilestoneResult {
            data class OK(val value: Milestone) : IssuesGetMilestoneResult

            data class NotFound(val value: BasicError) : IssuesGetMilestoneResult
        }

        suspend fun issuesGetMilestone(
            owner: String,
            repo: String,
            milestoneNumber: Long,
        ): IssuesGetMilestoneResult

        sealed interface IssuesDeleteMilestoneResult {
            data object NoContent : IssuesDeleteMilestoneResult

            data class NotFound(val value: BasicError) : IssuesDeleteMilestoneResult
        }

        suspend fun issuesDeleteMilestone(
            owner: String,
            repo: String,
            milestoneNumber: Long,
        ): IssuesDeleteMilestoneResult

        suspend fun issuesUpdateMilestone(
            owner: String,
            repo: String,
            milestoneNumber: Long,
            body: IssuesUpdateMilestoneBody? = null,
        ): Milestone

        interface LabelsApi {
            suspend fun issuesListLabelsForMilestone(
                owner: String,
                repo: String,
                milestoneNumber: Long,
                page: Long = 1L,
                perPage: Long = 30L,
            ): List<Label>
        }
    }

    interface Notifications {
        @Serializable
        @JvmInline
        value class ActivityMarkRepoNotificationsAsReadBody(@SerialName("last_read_at") val lastReadAt: LocalDateTime? = null)


        @Serializable
        data class ActivityMarkRepoNotificationsAsReadResponse(val message: String? = null, val url: String? = null)

        suspend fun activityListRepoNotificationsForAuthenticatedUser(
            owner: String,
            repo: String,
            all: Boolean = false,
            page: Long = 1L,
            participating: Boolean = false,
            perPage: Long = 30L,
            before: LocalDateTime? = null,
            since: LocalDateTime? = null,
        ): List<Thread>

        sealed interface ActivityMarkRepoNotificationsAsReadResult {
            data class Accepted(val value: ActivityMarkRepoNotificationsAsReadResponse) : ActivityMarkRepoNotificationsAsReadResult

            data object ResetContent : ActivityMarkRepoNotificationsAsReadResult
        }

        suspend fun activityMarkRepoNotificationsAsRead(
            owner: String,
            repo: String,
            body: ActivityMarkRepoNotificationsAsReadBody? = null,
        ): ActivityMarkRepoNotificationsAsReadResult
    }

    interface Pages {
        val builds: Repos.Pages.Builds

        val deployments: Repos.Pages.DeploymentsApi

        val health: Repos.Pages.Health

        @Serializable
        data class ReposCreatePagesSiteBody(
            @SerialName("build_type") val buildType: BuildType? = null,
            val source: Source? = null,
        ) {
            @Serializable
            enum class BuildType {
                @SerialName("legacy") Legacy, @SerialName("workflow") Workflow;
            }

            @Serializable
            data class Source(val branch: String, val path: Path? = null) {
                @Serializable
                enum class Path {
                    @SerialName("/") Slash, @SerialName("/docs") Docs;
                }
            }
        }


        @Serializable
        data class ReposUpdateInformationAboutPagesSiteBody(
            val cname: String? = null,
            @SerialName("https_enforced") val httpsEnforced: Boolean? = null,
            @SerialName("build_type") val buildType: BuildType? = null,
            val source: Source? = null,
        ) {
            @Serializable
            enum class BuildType {
                @SerialName("legacy") Legacy, @SerialName("workflow") Workflow;
            }

            @Serializable(with = Source.Serializer::class)
            sealed interface Source {
                @Serializable
                enum class GhPagesOrMasterOrMasterDocs : Source {
                    @SerialName("gh-pages") GhPages, @SerialName("master") Master, @SerialName("master /docs") MasterDocs;
                }

                @Serializable
                data class BranchAndPath(val branch: String, val path: Path) : Source {
                    @Serializable
                    enum class Path {
                        @SerialName("/") Slash, @SerialName("/docs") Docs;
                    }
                }

                object Serializer : KSerializer<Source> {
                    @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
                    override val descriptor: SerialDescriptor =
                        buildSerialDescriptor("io.github.nomisrev.api.Repos.Pages.ReposUpdateInformationAboutPagesSiteBody.Source", PolymorphicKind.SEALED) {
                            element("GhPagesOrMasterOrMasterDocs", ReposUpdateInformationAboutPagesSiteBody.Source.GhPagesOrMasterOrMasterDocs.serializer().descriptor)
                            element("BranchAndPath", ReposUpdateInformationAboutPagesSiteBody.Source.BranchAndPath.serializer().descriptor)
                        }

                    override fun deserialize(decoder: Decoder): Source {
                        val value = decoder.decodeSerializableValue(JsonElement.serializer())
                        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                        return json.attemptDeserialize(
                            value,
                            BranchAndPath::class to { decodeFromJsonElement(ReposUpdateInformationAboutPagesSiteBody.Source.BranchAndPath.serializer(), it) },
                            GhPagesOrMasterOrMasterDocs::class to { decodeFromJsonElement(ReposUpdateInformationAboutPagesSiteBody.Source.GhPagesOrMasterOrMasterDocs.serializer(), it) },
                        )
                    }

                    override fun serialize(encoder: Encoder, value: Source) = when(value) {
                        is GhPagesOrMasterOrMasterDocs -> encoder.encodeSerializableValue(ReposUpdateInformationAboutPagesSiteBody.Source.GhPagesOrMasterOrMasterDocs.serializer(), value)
                        is BranchAndPath -> encoder.encodeSerializableValue(ReposUpdateInformationAboutPagesSiteBody.Source.BranchAndPath.serializer(), value)
                    }
                }
            }
        }

        sealed interface ReposGetPagesResult {
            data class OK(val value: Page) : ReposGetPagesResult

            data class NotFound(val value: BasicError) : ReposGetPagesResult
        }

        suspend fun reposGetPages(
            owner: String,
            repo: String,
        ): ReposGetPagesResult

        sealed interface ReposUpdateInformationAboutPagesSiteResult {
            data object NoContent : ReposUpdateInformationAboutPagesSiteResult

            data class BadRequest(val value: BasicError) : ReposUpdateInformationAboutPagesSiteResult

            data class Conflict(val value: BasicError) : ReposUpdateInformationAboutPagesSiteResult

            data class UnprocessableEntity(val value: ValidationError) : ReposUpdateInformationAboutPagesSiteResult
        }

        suspend fun reposUpdateInformationAboutPagesSite(
            owner: String,
            repo: String,
            body: ReposUpdateInformationAboutPagesSiteBody,
        ): ReposUpdateInformationAboutPagesSiteResult

        sealed interface ReposCreatePagesSiteResult {
            data class Created(val value: Page) : ReposCreatePagesSiteResult

            data class Conflict(val value: BasicError) : ReposCreatePagesSiteResult

            data class UnprocessableEntity(val value: ValidationError) : ReposCreatePagesSiteResult
        }

        suspend fun reposCreatePagesSite(
            owner: String,
            repo: String,
            body: ReposCreatePagesSiteBody,
        ): ReposCreatePagesSiteResult

        sealed interface ReposDeletePagesSiteResult {
            data object NoContent : ReposDeletePagesSiteResult

            data class NotFound(val value: BasicError) : ReposDeletePagesSiteResult

            data class Conflict(val value: BasicError) : ReposDeletePagesSiteResult

            data class UnprocessableEntity(val value: ValidationError) : ReposDeletePagesSiteResult
        }

        suspend fun reposDeletePagesSite(
            owner: String,
            repo: String,
        ): ReposDeletePagesSiteResult

        interface Builds {
            val latest: Repos.Pages.Builds.Latest

            suspend fun reposListPagesBuilds(
                owner: String,
                repo: String,
                page: Long = 1L,
                perPage: Long = 30L,
            ): List<PageBuild>

            suspend fun reposRequestPagesBuild(
                owner: String,
                repo: String,
            ): PageBuildStatus

            suspend fun reposGetPagesBuild(
                owner: String,
                repo: String,
                buildId: Long,
            ): PageBuild

            interface Latest {
                suspend fun reposGetLatestPagesBuild(
                    owner: String,
                    repo: String,
                ): PageBuild
            }
        }

        interface DeploymentsApi {
            val cancel: Repos.Pages.DeploymentsApi.Cancel

            @Serializable(with = PagesDeploymentId.Serializer::class)
            sealed interface PagesDeploymentId {
                @Serializable
                @JvmInline
                value class CaseLong(val value: Long) : PagesDeploymentId

                @Serializable
                @JvmInline
                value class CaseString(val value: String) : PagesDeploymentId

                object Serializer : KSerializer<PagesDeploymentId> {
                    @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
                    override val descriptor: SerialDescriptor =
                        buildSerialDescriptor("io.github.nomisrev.api.Repos.Pages.DeploymentsApi.PagesDeploymentId", PolymorphicKind.SEALED) {
                            element("CaseLong", Long.serializer().descriptor)
                            element("CaseString", String.serializer().descriptor)
                        }

                    override fun deserialize(decoder: Decoder): PagesDeploymentId {
                        val value = decoder.decodeSerializableValue(JsonElement.serializer())
                        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                        return json.attemptDeserialize(
                            value,
                            CaseLong::class to { CaseLong(decodeFromJsonElement(Long.serializer(), it)) },
                            CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                        )
                    }

                    override fun serialize(encoder: Encoder, value: PagesDeploymentId) = when(value) {
                        is CaseLong -> encoder.encodeSerializableValue(Long.serializer(), value.value)
                        is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                    }
                }
            }


            @Serializable
            data class ReposCreatePagesDeploymentBody(
                @SerialName("artifact_id") val artifactId: Double? = null,
                @SerialName("artifact_url") val artifactUrl: String? = null,
                val environment: String? = null,
                @SerialName("pages_build_version") @Required val pagesBuildVersion: String,
                @SerialName("oidc_token") val oidcToken: String,
            )

            sealed interface ReposCreatePagesDeploymentResult {
                data class OK(val value: PageDeployment) : ReposCreatePagesDeploymentResult

                data class BadRequest(val value: BasicError) : ReposCreatePagesDeploymentResult

                data class NotFound(val value: BasicError) : ReposCreatePagesDeploymentResult

                data class UnprocessableEntity(val value: ValidationError) : ReposCreatePagesDeploymentResult
            }

            suspend fun reposCreatePagesDeployment(
                owner: String,
                repo: String,
                body: ReposCreatePagesDeploymentBody,
            ): ReposCreatePagesDeploymentResult

            sealed interface ReposGetPagesDeploymentResult {
                data class OK(val value: PagesDeploymentStatus) : ReposGetPagesDeploymentResult

                data class NotFound(val value: BasicError) : ReposGetPagesDeploymentResult
            }

            suspend fun reposGetPagesDeployment(
                owner: String,
                repo: String,
                pagesDeploymentId: PagesDeploymentId,
            ): ReposGetPagesDeploymentResult

            interface Cancel {
                @Serializable(with = ReposCancelPagesDeploymentPagesDeploymentId.Serializer::class)
                sealed interface ReposCancelPagesDeploymentPagesDeploymentId {
                    @Serializable
                    @JvmInline
                    value class CaseLong(val value: Long) : ReposCancelPagesDeploymentPagesDeploymentId

                    @Serializable
                    @JvmInline
                    value class CaseString(val value: String) : ReposCancelPagesDeploymentPagesDeploymentId

                    object Serializer : KSerializer<ReposCancelPagesDeploymentPagesDeploymentId> {
                        @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
                        override val descriptor: SerialDescriptor =
                            buildSerialDescriptor("io.github.nomisrev.api.Repos.Pages.DeploymentsApi.Cancel.ReposCancelPagesDeploymentPagesDeploymentId", PolymorphicKind.SEALED) {
                                element("CaseLong", Long.serializer().descriptor)
                                element("CaseString", String.serializer().descriptor)
                            }

                        override fun deserialize(decoder: Decoder): ReposCancelPagesDeploymentPagesDeploymentId {
                            val value = decoder.decodeSerializableValue(JsonElement.serializer())
                            val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                            return json.attemptDeserialize(
                                value,
                                CaseLong::class to { CaseLong(decodeFromJsonElement(Long.serializer(), it)) },
                                CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                            )
                        }

                        override fun serialize(encoder: Encoder, value: ReposCancelPagesDeploymentPagesDeploymentId) = when(value) {
                            is CaseLong -> encoder.encodeSerializableValue(Long.serializer(), value.value)
                            is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                        }
                    }
                }

                sealed interface ReposCancelPagesDeploymentResult {
                    data object NoContent : ReposCancelPagesDeploymentResult

                    data class NotFound(val value: BasicError) : ReposCancelPagesDeploymentResult
                }

                suspend fun reposCancelPagesDeployment(
                    owner: String,
                    repo: String,
                    pagesDeploymentId: ReposCancelPagesDeploymentPagesDeploymentId,
                ): ReposCancelPagesDeploymentResult
            }
        }

        interface Health {
            sealed interface ReposGetPagesHealthCheckResult {
                data class OK(val value: PagesHealthCheck) : ReposGetPagesHealthCheckResult

                data class Accepted(val value: EmptyObject) : ReposGetPagesHealthCheckResult

                data object BadRequest : ReposGetPagesHealthCheckResult

                data class NotFound(val value: BasicError) : ReposGetPagesHealthCheckResult

                data object UnprocessableEntity : ReposGetPagesHealthCheckResult
            }

            suspend fun reposGetPagesHealthCheck(
                owner: String,
                repo: String,
            ): ReposGetPagesHealthCheckResult
        }
    }

    interface PrivateVulnerabilityReporting {
        @Serializable
        @JvmInline
        value class ReposCheckPrivateVulnerabilityReportingResponse(val enabled: Boolean)

        sealed interface ReposCheckPrivateVulnerabilityReportingResult {
            data class OK(val value: ReposCheckPrivateVulnerabilityReportingResponse) : ReposCheckPrivateVulnerabilityReportingResult

            data class UnprocessableEntity(val value: BasicError) : ReposCheckPrivateVulnerabilityReportingResult
        }

        suspend fun reposCheckPrivateVulnerabilityReporting(
            owner: String,
            repo: String,
        ): ReposCheckPrivateVulnerabilityReportingResult

        sealed interface ReposEnablePrivateVulnerabilityReportingResult {
            data object NoContent : ReposEnablePrivateVulnerabilityReportingResult

            data class UnprocessableEntity(val value: BasicError) : ReposEnablePrivateVulnerabilityReportingResult
        }

        suspend fun reposEnablePrivateVulnerabilityReporting(
            owner: String,
            repo: String,
        ): ReposEnablePrivateVulnerabilityReportingResult

        sealed interface ReposDisablePrivateVulnerabilityReportingResult {
            data object NoContent : ReposDisablePrivateVulnerabilityReportingResult

            data class UnprocessableEntity(val value: BasicError) : ReposDisablePrivateVulnerabilityReportingResult
        }

        suspend fun reposDisablePrivateVulnerabilityReporting(
            owner: String,
            repo: String,
        ): ReposDisablePrivateVulnerabilityReportingResult
    }

    interface Properties {
        val values: Repos.Properties.Values

        interface Values {
            @Serializable
            @JvmInline
            value class ReposCustomPropertiesForReposCreateOrUpdateRepositoryValuesBody(val properties: List<CustomPropertyValue>)

            sealed interface ReposCustomPropertiesForReposGetRepositoryValuesResult {
                data class OK(val value: List<CustomPropertyValue>) : ReposCustomPropertiesForReposGetRepositoryValuesResult

                data class Forbidden(val value: BasicError) : ReposCustomPropertiesForReposGetRepositoryValuesResult

                data class NotFound(val value: BasicError) : ReposCustomPropertiesForReposGetRepositoryValuesResult
            }

            suspend fun reposCustomPropertiesForReposGetRepositoryValues(
                owner: String,
                repo: String,
            ): ReposCustomPropertiesForReposGetRepositoryValuesResult

            sealed interface ReposCustomPropertiesForReposCreateOrUpdateRepositoryValuesResult {
                data object NoContent : ReposCustomPropertiesForReposCreateOrUpdateRepositoryValuesResult

                data class Forbidden(val value: BasicError) : ReposCustomPropertiesForReposCreateOrUpdateRepositoryValuesResult

                data class NotFound(val value: BasicError) : ReposCustomPropertiesForReposCreateOrUpdateRepositoryValuesResult

                data class UnprocessableEntity(val value: ValidationError) : ReposCustomPropertiesForReposCreateOrUpdateRepositoryValuesResult
            }

            suspend fun reposCustomPropertiesForReposCreateOrUpdateRepositoryValues(
                owner: String,
                repo: String,
                body: ReposCustomPropertiesForReposCreateOrUpdateRepositoryValuesBody,
            ): ReposCustomPropertiesForReposCreateOrUpdateRepositoryValuesResult
        }
    }

    interface Pulls {
        val comments: Repos.Pulls.CommentsApi

        val codespaces: Repos.Pulls.CodespacesApi

        val commits: Repos.Pulls.CommitsApi

        val files: Repos.Pulls.Files

        val merge: Repos.Pulls.Merge

        val requestedReviewers: Repos.Pulls.RequestedReviewers

        val reviews: Repos.Pulls.Reviews

        val updateBranch: Repos.Pulls.UpdateBranch

        @Serializable
        enum class Direction {
            @SerialName("asc") Asc, @SerialName("desc") Desc;
        }


        @Serializable
        data class PullsCreateBody(
            val title: String? = null,
            val head: String,
            @SerialName("head_repo") val headRepo: String? = null,
            val base: String,
            val body: String? = null,
            @SerialName("maintainer_can_modify") val maintainerCanModify: Boolean? = null,
            val draft: Boolean? = null,
            val issue: Long? = null,
        )


        @Serializable
        data class PullsGetResponse(
            val code: String? = null,
            val message: String? = null,
            @SerialName("documentation_url") val documentationUrl: String? = null,
        )


        @Serializable
        data class PullsUpdateBody(
            val title: String? = null,
            val body: String? = null,
            val state: State? = null,
            val base: String? = null,
            @SerialName("maintainer_can_modify") val maintainerCanModify: Boolean? = null,
        ) {
            @Serializable
            enum class State {
                @SerialName("open") Open, @SerialName("closed") Closed;
            }
        }


        @Serializable
        enum class Sort {
            @SerialName("created")
            Created,
            @SerialName("updated")
            Updated,
            @SerialName("popularity")
            Popularity,
            @SerialName("long-running")
            LongRunning;
        }


        @Serializable
        enum class State {
            @SerialName("open") Open, @SerialName("closed") Closed, @SerialName("all") All;
        }

        sealed interface PullsListResult {
            data class OK(val value: List<PullRequestSimple>) : PullsListResult

            data object NotModified : PullsListResult

            data class UnprocessableEntity(val value: ValidationError) : PullsListResult
        }

        suspend fun pullsList(
            owner: String,
            repo: String,
            page: Long = 1L,
            perPage: Long = 30L,
            sort: Sort = Sort.Created,
            state: State = State.Open,
            base: String? = null,
            direction: Direction? = null,
            head: String? = null,
        ): PullsListResult

        sealed interface PullsCreateResult {
            data class Created(val value: PullRequest) : PullsCreateResult

            data class Forbidden(val value: BasicError) : PullsCreateResult

            data class UnprocessableEntity(val value: ValidationError) : PullsCreateResult
        }

        suspend fun pullsCreate(
            owner: String,
            repo: String,
            body: PullsCreateBody,
        ): PullsCreateResult

        sealed interface PullsGetResult {
            data class OK(val value: PullRequest) : PullsGetResult

            data object NotModified : PullsGetResult

            data class NotFound(val value: BasicError) : PullsGetResult

            data class NotAcceptable(val value: BasicError) : PullsGetResult

            data class InternalServerError(val value: BasicError) : PullsGetResult

            data class ServiceUnavailable(val value: PullsGetResponse) : PullsGetResult
        }

        suspend fun pullsGet(
            owner: String,
            repo: String,
            pullNumber: Long,
        ): PullsGetResult

        sealed interface PullsUpdateResult {
            data class OK(val value: PullRequest) : PullsUpdateResult

            data class Forbidden(val value: BasicError) : PullsUpdateResult

            data class UnprocessableEntity(val value: ValidationError) : PullsUpdateResult
        }

        suspend fun pullsUpdate(
            owner: String,
            repo: String,
            pullNumber: Long,
            body: PullsUpdateBody? = null,
        ): PullsUpdateResult

        interface CommentsApi {
            val reactions: Repos.Pulls.CommentsApi.Reactions

            val replies: Repos.Pulls.CommentsApi.Replies

            @Serializable
            data class PullsCreateReviewCommentBody(
                val body: String,
                @SerialName("commit_id") val commitId: String,
                val path: String,
                val position: Long? = null,
                val side: Side? = null,
                val line: Long? = null,
                @SerialName("start_line") val startLine: Long? = null,
                @SerialName("start_side") val startSide: StartSide? = null,
                @SerialName("in_reply_to") val inReplyTo: Long? = null,
                @SerialName("subject_type") val subjectType: SubjectType? = null,
            ) {
                @Serializable
                enum class Side {
                    LEFT, RIGHT;
                }

                @Serializable
                enum class StartSide {
                    LEFT, RIGHT, @SerialName("side") Side;
                }

                @Serializable
                enum class SubjectType {
                    @SerialName("line") Line, @SerialName("file") File;
                }
            }


            @Serializable
            enum class PullsListReviewCommentsDirection {
                @SerialName("asc") Asc, @SerialName("desc") Desc;
            }


            @Serializable
            enum class PullsListReviewCommentsForRepoDirection {
                @SerialName("asc") Asc, @SerialName("desc") Desc;
            }


            @Serializable
            enum class PullsListReviewCommentsForRepoSort {
                @SerialName("created") Created, @SerialName("updated") Updated, @SerialName("created_at") CreatedAt;
            }


            @Serializable
            enum class PullsListReviewCommentsSort {
                @SerialName("created") Created, @SerialName("updated") Updated;
            }


            @Serializable
            @JvmInline
            value class PullsUpdateReviewCommentBody(val body: String)

            suspend fun pullsListReviewCommentsForRepo(
                owner: String,
                repo: String,
                page: Long = 1L,
                perPage: Long = 30L,
                direction: PullsListReviewCommentsForRepoDirection? = null,
                since: LocalDateTime? = null,
                sort: PullsListReviewCommentsForRepoSort? = null,
            ): List<PullRequestReviewComment>

            sealed interface PullsGetReviewCommentResult {
                data class OK(val value: PullRequestReviewComment) : PullsGetReviewCommentResult

                data class NotFound(val value: BasicError) : PullsGetReviewCommentResult
            }

            suspend fun pullsGetReviewComment(
                owner: String,
                repo: String,
                commentId: Long,
            ): PullsGetReviewCommentResult

            sealed interface PullsDeleteReviewCommentResult {
                data object NoContent : PullsDeleteReviewCommentResult

                data class NotFound(val value: BasicError) : PullsDeleteReviewCommentResult
            }

            suspend fun pullsDeleteReviewComment(
                owner: String,
                repo: String,
                commentId: Long,
            ): PullsDeleteReviewCommentResult

            suspend fun pullsUpdateReviewComment(
                owner: String,
                repo: String,
                commentId: Long,
                body: PullsUpdateReviewCommentBody,
            ): PullRequestReviewComment

            suspend fun pullsListReviewComments(
                owner: String,
                repo: String,
                pullNumber: Long,
                page: Long = 1L,
                perPage: Long = 30L,
                sort: PullsListReviewCommentsSort = PullsListReviewCommentsSort.Created,
                direction: PullsListReviewCommentsDirection? = null,
                since: LocalDateTime? = null,
            ): List<PullRequestReviewComment>

            sealed interface PullsCreateReviewCommentResult {
                data class Created(val value: PullRequestReviewComment) : PullsCreateReviewCommentResult

                data class Forbidden(val value: BasicError) : PullsCreateReviewCommentResult

                data class UnprocessableEntity(val value: ValidationError) : PullsCreateReviewCommentResult
            }

            suspend fun pullsCreateReviewComment(
                owner: String,
                repo: String,
                pullNumber: Long,
                body: PullsCreateReviewCommentBody,
            ): PullsCreateReviewCommentResult

            interface Reactions {
                @Serializable
                enum class Content {
                    @SerialName("+1")
                    `+1`,
                    @SerialName("-1")
                    `-1`,
                    @SerialName("laugh")
                    Laugh,
                    @SerialName("confused")
                    Confused,
                    @SerialName("heart")
                    Heart,
                    @SerialName("hooray")
                    Hooray,
                    @SerialName("rocket")
                    Rocket,
                    @SerialName("eyes")
                    Eyes;
                }


                @Serializable
                @JvmInline
                value class ReactionsCreateForPullRequestReviewCommentBody(val content: Content) {
                    @Serializable
                    enum class Content {
                        @SerialName("+1")
                        `+1`,
                        @SerialName("-1")
                        `-1`,
                        @SerialName("laugh")
                        Laugh,
                        @SerialName("confused")
                        Confused,
                        @SerialName("heart")
                        Heart,
                        @SerialName("hooray")
                        Hooray,
                        @SerialName("rocket")
                        Rocket,
                        @SerialName("eyes")
                        Eyes;
                    }
                }

                sealed interface ReactionsListForPullRequestReviewCommentResult {
                    data class OK(val value: List<Reaction>) : ReactionsListForPullRequestReviewCommentResult

                    data class NotFound(val value: BasicError) : ReactionsListForPullRequestReviewCommentResult
                }

                suspend fun reactionsListForPullRequestReviewComment(
                    owner: String,
                    repo: String,
                    commentId: Long,
                    page: Long = 1L,
                    perPage: Long = 30L,
                    content: Content? = null,
                ): ReactionsListForPullRequestReviewCommentResult

                sealed interface ReactionsCreateForPullRequestReviewCommentResult {
                    data class OK(val value: Reaction) : ReactionsCreateForPullRequestReviewCommentResult

                    data class Created(val value: Reaction) : ReactionsCreateForPullRequestReviewCommentResult

                    data class UnprocessableEntity(val value: ValidationError) : ReactionsCreateForPullRequestReviewCommentResult
                }

                suspend fun reactionsCreateForPullRequestReviewComment(
                    owner: String,
                    repo: String,
                    commentId: Long,
                    body: ReactionsCreateForPullRequestReviewCommentBody,
                ): ReactionsCreateForPullRequestReviewCommentResult

                suspend fun reactionsDeleteForPullRequestComment(
                    owner: String,
                    repo: String,
                    commentId: Long,
                    reactionId: Long,
                ): Unit
            }

            interface Replies {
                @Serializable
                @JvmInline
                value class PullsCreateReplyForReviewCommentBody(val body: String)

                sealed interface PullsCreateReplyForReviewCommentResult {
                    data class Created(val value: PullRequestReviewComment) : PullsCreateReplyForReviewCommentResult

                    data class NotFound(val value: BasicError) : PullsCreateReplyForReviewCommentResult
                }

                suspend fun pullsCreateReplyForReviewComment(
                    owner: String,
                    repo: String,
                    pullNumber: Long,
                    commentId: Long,
                    body: PullsCreateReplyForReviewCommentBody,
                ): PullsCreateReplyForReviewCommentResult
            }
        }

        interface CodespacesApi {
            @Serializable
            data class CodespacesCreateWithPrForAuthenticatedUserBody(
                val location: String? = null,
                val geo: Geo? = null,
                @SerialName("client_ip") val clientIp: String? = null,
                val machine: String? = null,
                @SerialName("devcontainer_path") val devcontainerPath: String? = null,
                @SerialName("multi_repo_permissions_opt_out") val multiRepoPermissionsOptOut: Boolean? = null,
                @SerialName("working_directory") val workingDirectory: String? = null,
                @SerialName("idle_timeout_minutes") val idleTimeoutMinutes: Long? = null,
                @SerialName("display_name") val displayName: String? = null,
                @SerialName("retention_period_minutes") val retentionPeriodMinutes: Long? = null,
            ) {
                @Serializable
                enum class Geo {
                    EuropeWest, SoutheastAsia, UsEast, UsWest;
                }
            }


            @Serializable
            data class CodespacesCreateWithPrForAuthenticatedUserResponse(
                val code: String? = null,
                val message: String? = null,
                @SerialName("documentation_url") val documentationUrl: String? = null,
            )

            sealed interface CodespacesCreateWithPrForAuthenticatedUserResult {
                data class Created(val value: Codespace) : CodespacesCreateWithPrForAuthenticatedUserResult

                data class Accepted(val value: Codespace) : CodespacesCreateWithPrForAuthenticatedUserResult

                data class Unauthorized(val value: BasicError) : CodespacesCreateWithPrForAuthenticatedUserResult

                data class Forbidden(val value: BasicError) : CodespacesCreateWithPrForAuthenticatedUserResult

                data class NotFound(val value: BasicError) : CodespacesCreateWithPrForAuthenticatedUserResult

                data class ServiceUnavailable(val value: CodespacesCreateWithPrForAuthenticatedUserResponse) : CodespacesCreateWithPrForAuthenticatedUserResult
            }

            suspend fun codespacesCreateWithPrForAuthenticatedUser(
                owner: String,
                repo: String,
                pullNumber: Long,
                body: CodespacesCreateWithPrForAuthenticatedUserBody,
            ): CodespacesCreateWithPrForAuthenticatedUserResult
        }

        interface CommitsApi {
            suspend fun pullsListCommits(
                owner: String,
                repo: String,
                pullNumber: Long,
                page: Long = 1L,
                perPage: Long = 30L,
            ): List<Commit>
        }

        interface Files {
            @Serializable
            data class PullsListFilesResponse(
                val code: String? = null,
                val message: String? = null,
                @SerialName("documentation_url") val documentationUrl: String? = null,
            )

            sealed interface PullsListFilesResult {
                data class OK(val value: List<DiffEntry>) : PullsListFilesResult

                data class UnprocessableEntity(val value: ValidationError) : PullsListFilesResult

                data class InternalServerError(val value: BasicError) : PullsListFilesResult

                data class ServiceUnavailable(val value: PullsListFilesResponse) : PullsListFilesResult
            }

            suspend fun pullsListFiles(
                owner: String,
                repo: String,
                pullNumber: Long,
                page: Long = 1L,
                perPage: Long = 30L,
            ): PullsListFilesResult
        }

        interface Merge {
            @Serializable
            data class PullsMergeBody(
                @SerialName("commit_title") val commitTitle: String? = null,
                @SerialName("commit_message") val commitMessage: String? = null,
                val sha: String? = null,
                @SerialName("merge_method") val mergeMethod: MergeMethod? = null,
            ) {
                @Serializable
                enum class MergeMethod {
                    @SerialName("merge") Merge, @SerialName("squash") Squash, @SerialName("rebase") Rebase;
                }
            }


            @Serializable
            data class PullsMergeResponse(
                val message: String? = null,
                @SerialName("documentation_url") val documentationUrl: String? = null,
            )

            sealed interface PullsCheckIfMergedResult {
                data object NoContent : PullsCheckIfMergedResult

                data object NotFound : PullsCheckIfMergedResult
            }

            suspend fun pullsCheckIfMerged(
                owner: String,
                repo: String,
                pullNumber: Long,
            ): PullsCheckIfMergedResult

            sealed interface PullsMergeResult {
                data class OK(val value: PullRequestMergeResult) : PullsMergeResult

                data class Forbidden(val value: BasicError) : PullsMergeResult

                data class NotFound(val value: BasicError) : PullsMergeResult

                data class MethodNotAllowed(val value: PullsMergeResponse) : PullsMergeResult

                data class Conflict(val value: PullsMergeResponse) : PullsMergeResult

                data class UnprocessableEntity(val value: ValidationError) : PullsMergeResult
            }

            suspend fun pullsMerge(
                owner: String,
                repo: String,
                pullNumber: Long,
                body: PullsMergeBody? = null,
            ): PullsMergeResult
        }

        interface RequestedReviewers {
            @Serializable
            data class PullsRemoveRequestedReviewersBody(
                val reviewers: List<String>,
                @SerialName("team_reviewers") val teamReviewers: List<String>? = null,
            )


            @Serializable
            data class PullsRequestReviewersBody(
                val reviewers: List<String>? = null,
                @SerialName("team_reviewers") val teamReviewers: List<String>? = null,
            )

            suspend fun pullsListRequestedReviewers(
                owner: String,
                repo: String,
                pullNumber: Long,
            ): PullRequestReviewRequest

            sealed interface PullsRequestReviewersResult {
                data class Created(val value: PullRequestSimple) : PullsRequestReviewersResult

                data class Forbidden(val value: BasicError) : PullsRequestReviewersResult

                data object UnprocessableEntity : PullsRequestReviewersResult
            }

            suspend fun pullsRequestReviewers(
                owner: String,
                repo: String,
                pullNumber: Long,
                body: PullsRequestReviewersBody? = null,
            ): PullsRequestReviewersResult

            sealed interface PullsRemoveRequestedReviewersResult {
                data class OK(val value: PullRequestSimple) : PullsRemoveRequestedReviewersResult

                data class UnprocessableEntity(val value: ValidationError) : PullsRemoveRequestedReviewersResult
            }

            suspend fun pullsRemoveRequestedReviewers(
                owner: String,
                repo: String,
                pullNumber: Long,
                body: PullsRemoveRequestedReviewersBody,
            ): PullsRemoveRequestedReviewersResult
        }

        interface Reviews {
            val comments: Repos.Pulls.Reviews.CommentsApi2

            val dismissals: Repos.Pulls.Reviews.Dismissals

            val events: Repos.Pulls.Reviews.EventsApi

            @Serializable
            data class PullsCreateReviewBody(
                @SerialName("commit_id") val commitId: String? = null,
                val body: String? = null,
                val event: Event? = null,
                val comments: List<Comments>? = null,
            ) {
                @Serializable
                enum class Event {
                    APPROVE, @SerialName("REQUEST_CHANGES") REQUESTCHANGES, COMMENT;
                }

                @Serializable
                data class Comments(
                    val path: String,
                    val position: Long? = null,
                    val body: String,
                    val line: Long? = null,
                    val side: String? = null,
                    @SerialName("start_line") val startLine: Long? = null,
                    @SerialName("start_side") val startSide: String? = null,
                )
            }


            @Serializable
            @JvmInline
            value class PullsUpdateReviewBody(val body: String)

            suspend fun pullsListReviews(
                owner: String,
                repo: String,
                pullNumber: Long,
                page: Long = 1L,
                perPage: Long = 30L,
            ): List<PullRequestReview>

            sealed interface PullsCreateReviewResult {
                data class OK(val value: PullRequestReview) : PullsCreateReviewResult

                data class Forbidden(val value: BasicError) : PullsCreateReviewResult

                data class UnprocessableEntity(val value: ValidationErrorSimple) : PullsCreateReviewResult
            }

            suspend fun pullsCreateReview(
                owner: String,
                repo: String,
                pullNumber: Long,
                body: PullsCreateReviewBody? = null,
            ): PullsCreateReviewResult

            sealed interface PullsGetReviewResult {
                data class OK(val value: PullRequestReview) : PullsGetReviewResult

                data class NotFound(val value: BasicError) : PullsGetReviewResult
            }

            suspend fun pullsGetReview(
                owner: String,
                repo: String,
                pullNumber: Long,
                reviewId: Long,
            ): PullsGetReviewResult

            sealed interface PullsUpdateReviewResult {
                data class OK(val value: PullRequestReview) : PullsUpdateReviewResult

                data class UnprocessableEntity(val value: ValidationErrorSimple) : PullsUpdateReviewResult
            }

            suspend fun pullsUpdateReview(
                owner: String,
                repo: String,
                pullNumber: Long,
                reviewId: Long,
                body: PullsUpdateReviewBody,
            ): PullsUpdateReviewResult

            sealed interface PullsDeletePendingReviewResult {
                data class OK(val value: PullRequestReview) : PullsDeletePendingReviewResult

                data class NotFound(val value: BasicError) : PullsDeletePendingReviewResult

                data class UnprocessableEntity(val value: ValidationErrorSimple) : PullsDeletePendingReviewResult
            }

            suspend fun pullsDeletePendingReview(
                owner: String,
                repo: String,
                pullNumber: Long,
                reviewId: Long,
            ): PullsDeletePendingReviewResult

            interface CommentsApi2 {
                sealed interface PullsListCommentsForReviewResult {
                    data class OK(val value: List<ReviewComment>) : PullsListCommentsForReviewResult

                    data class NotFound(val value: BasicError) : PullsListCommentsForReviewResult
                }

                suspend fun pullsListCommentsForReview(
                    owner: String,
                    repo: String,
                    pullNumber: Long,
                    reviewId: Long,
                    page: Long = 1L,
                    perPage: Long = 30L,
                ): PullsListCommentsForReviewResult
            }

            interface Dismissals {
                @Serializable
                data class PullsDismissReviewBody(val message: String, val event: Event? = null) {
                    @Serializable
                    enum class Event {
                        DISMISS;
                    }
                }

                sealed interface PullsDismissReviewResult {
                    data class OK(val value: PullRequestReview) : PullsDismissReviewResult

                    data class NotFound(val value: BasicError) : PullsDismissReviewResult

                    data class UnprocessableEntity(val value: ValidationErrorSimple) : PullsDismissReviewResult
                }

                suspend fun pullsDismissReview(
                    owner: String,
                    repo: String,
                    pullNumber: Long,
                    reviewId: Long,
                    body: PullsDismissReviewBody,
                ): PullsDismissReviewResult
            }

            interface EventsApi {
                @Serializable
                data class PullsSubmitReviewBody(val body: String? = null, val event: Event) {
                    @Serializable
                    enum class Event {
                        APPROVE, @SerialName("REQUEST_CHANGES") REQUESTCHANGES, COMMENT;
                    }
                }

                sealed interface PullsSubmitReviewResult {
                    data class OK(val value: PullRequestReview) : PullsSubmitReviewResult

                    data class Forbidden(val value: BasicError) : PullsSubmitReviewResult

                    data class NotFound(val value: BasicError) : PullsSubmitReviewResult

                    data class UnprocessableEntity(val value: ValidationErrorSimple) : PullsSubmitReviewResult
                }

                suspend fun pullsSubmitReview(
                    owner: String,
                    repo: String,
                    pullNumber: Long,
                    reviewId: Long,
                    body: PullsSubmitReviewBody,
                ): PullsSubmitReviewResult
            }
        }

        interface UpdateBranch {
            @Serializable
            @JvmInline
            value class PullsUpdateBranchBody(@SerialName("expected_head_sha") val expectedHeadSha: String? = null)


            @Serializable
            data class PullsUpdateBranchResponse(val message: String? = null, val url: String? = null)

            sealed interface PullsUpdateBranchResult {
                data class Accepted(val value: PullsUpdateBranchResponse) : PullsUpdateBranchResult

                data class Forbidden(val value: BasicError) : PullsUpdateBranchResult

                data class UnprocessableEntity(val value: ValidationError) : PullsUpdateBranchResult
            }

            suspend fun pullsUpdateBranch(
                owner: String,
                repo: String,
                pullNumber: Long,
                body: PullsUpdateBranchBody? = null,
            ): PullsUpdateBranchResult
        }
    }

    interface Readme {
        sealed interface ReposGetReadmeResult {
            data class OK(val value: ContentFile) : ReposGetReadmeResult

            data object NotModified : ReposGetReadmeResult

            data class NotFound(val value: BasicError) : ReposGetReadmeResult

            data class UnprocessableEntity(val value: ValidationError) : ReposGetReadmeResult
        }

        suspend fun reposGetReadme(
            owner: String,
            repo: String,
            ref: String? = null,
        ): ReposGetReadmeResult

        sealed interface ReposGetReadmeInDirectoryResult {
            data class OK(val value: ContentFile) : ReposGetReadmeInDirectoryResult

            data class NotFound(val value: BasicError) : ReposGetReadmeInDirectoryResult

            data class UnprocessableEntity(val value: ValidationError) : ReposGetReadmeInDirectoryResult
        }

        suspend fun reposGetReadmeInDirectory(
            owner: String,
            repo: String,
            dir: String,
            ref: String? = null,
        ): ReposGetReadmeInDirectoryResult
    }

    interface Releases {
        val assets: Repos.Releases.Assets

        val generateNotes: Repos.Releases.GenerateNotes

        val latest: Repos.Releases.Latest

        val tags: Repos.Releases.TagsApi

        val reactions: Repos.Releases.Reactions

        @Serializable
        data class ReposCreateReleaseBody(
            @SerialName("tag_name") val tagName: String,
            @SerialName("target_commitish") val targetCommitish: String? = null,
            val name: String? = null,
            val body: String? = null,
            val draft: Boolean? = null,
            val prerelease: Boolean? = null,
            @SerialName("discussion_category_name") val discussionCategoryName: String? = null,
            @SerialName("generate_release_notes") val generateReleaseNotes: Boolean? = null,
            @SerialName("make_latest") val makeLatest: MakeLatest? = null,
        ) {
            @Serializable
            enum class MakeLatest {
                @SerialName("true") True, @SerialName("false") False, @SerialName("legacy") Legacy;
            }
        }


        @Serializable
        data class ReposUpdateReleaseBody(
            @SerialName("tag_name") val tagName: String? = null,
            @SerialName("target_commitish") val targetCommitish: String? = null,
            val name: String? = null,
            val body: String? = null,
            val draft: Boolean? = null,
            val prerelease: Boolean? = null,
            @SerialName("make_latest") val makeLatest: MakeLatest? = null,
            @SerialName("discussion_category_name") val discussionCategoryName: String? = null,
        ) {
            @Serializable
            enum class MakeLatest {
                @SerialName("true") True, @SerialName("false") False, @SerialName("legacy") Legacy;
            }
        }

        sealed interface ReposListReleasesResult {
            data class OK(val value: List<Release>) : ReposListReleasesResult

            data class NotFound(val value: BasicError) : ReposListReleasesResult
        }

        suspend fun reposListReleases(
            owner: String,
            repo: String,
            page: Long = 1L,
            perPage: Long = 30L,
        ): ReposListReleasesResult

        sealed interface ReposCreateReleaseResult {
            data class Created(val value: Release) : ReposCreateReleaseResult

            data class NotFound(val value: BasicError) : ReposCreateReleaseResult

            data class UnprocessableEntity(val value: ValidationError) : ReposCreateReleaseResult
        }

        suspend fun reposCreateRelease(
            owner: String,
            repo: String,
            body: ReposCreateReleaseBody,
        ): ReposCreateReleaseResult

        sealed interface ReposGetReleaseResult {
            data class OK(val value: Release) : ReposGetReleaseResult

            data object Unauthorized : ReposGetReleaseResult
        }

        suspend fun reposGetRelease(
            owner: String,
            repo: String,
            releaseId: Long,
        ): ReposGetReleaseResult

        suspend fun reposDeleteRelease(
            owner: String,
            repo: String,
            releaseId: Long,
        ): Unit

        sealed interface ReposUpdateReleaseResult {
            data class OK(val value: Release) : ReposUpdateReleaseResult

            data class NotFound(val value: BasicError) : ReposUpdateReleaseResult
        }

        suspend fun reposUpdateRelease(
            owner: String,
            repo: String,
            releaseId: Long,
            body: ReposUpdateReleaseBody? = null,
        ): ReposUpdateReleaseResult

        interface Assets {
            @Serializable
            data class ReposUpdateReleaseAssetBody(val name: String? = null, val label: String? = null, val state: String? = null)

            sealed interface ReposGetReleaseAssetResult {
                data class OK(val value: ReleaseAsset) : ReposGetReleaseAssetResult

                data object Found : ReposGetReleaseAssetResult

                data class NotFound(val value: BasicError) : ReposGetReleaseAssetResult
            }

            suspend fun reposGetReleaseAsset(
                owner: String,
                repo: String,
                assetId: Long,
            ): ReposGetReleaseAssetResult

            suspend fun reposDeleteReleaseAsset(
                owner: String,
                repo: String,
                assetId: Long,
            ): Unit

            suspend fun reposUpdateReleaseAsset(
                owner: String,
                repo: String,
                assetId: Long,
                body: ReposUpdateReleaseAssetBody? = null,
            ): ReleaseAsset

            suspend fun reposListReleaseAssets(
                owner: String,
                repo: String,
                releaseId: Long,
                page: Long = 1L,
                perPage: Long = 30L,
            ): List<ReleaseAsset>

            sealed interface ReposUploadReleaseAssetResult {
                data class Created(val value: ReleaseAsset) : ReposUploadReleaseAssetResult

                data object UnprocessableEntity : ReposUploadReleaseAssetResult
            }

            suspend fun reposUploadReleaseAsset(
                owner: String,
                repo: String,
                releaseId: Long,
                name: String,
                label: String? = null,
                body: ByteArray? = null,
            ): ReposUploadReleaseAssetResult
        }

        interface GenerateNotes {
            @Serializable
            data class ReposGenerateReleaseNotesBody(
                @SerialName("tag_name") val tagName: String,
                @SerialName("target_commitish") val targetCommitish: String? = null,
                @SerialName("previous_tag_name") val previousTagName: String? = null,
                @SerialName("configuration_file_path") val configurationFilePath: String? = null,
            )

            sealed interface ReposGenerateReleaseNotesResult {
                data class OK(val value: ReleaseNotesContent) : ReposGenerateReleaseNotesResult

                data class NotFound(val value: BasicError) : ReposGenerateReleaseNotesResult
            }

            suspend fun reposGenerateReleaseNotes(
                owner: String,
                repo: String,
                body: ReposGenerateReleaseNotesBody,
            ): ReposGenerateReleaseNotesResult
        }

        interface Latest {
            suspend fun reposGetLatestRelease(
                owner: String,
                repo: String,
            ): Release
        }

        interface TagsApi {
            sealed interface ReposGetReleaseByTagResult {
                data class OK(val value: Release) : ReposGetReleaseByTagResult

                data class NotFound(val value: BasicError) : ReposGetReleaseByTagResult
            }

            suspend fun reposGetReleaseByTag(
                owner: String,
                repo: String,
                tag: String,
            ): ReposGetReleaseByTagResult
        }

        interface Reactions {
            @Serializable
            enum class Content {
                @SerialName("+1")
                `+1`,
                @SerialName("laugh")
                Laugh,
                @SerialName("heart")
                Heart,
                @SerialName("hooray")
                Hooray,
                @SerialName("rocket")
                Rocket,
                @SerialName("eyes")
                Eyes;
            }


            @Serializable
            @JvmInline
            value class ReactionsCreateForReleaseBody(val content: Content) {
                @Serializable
                enum class Content {
                    @SerialName("+1")
                    `+1`,
                    @SerialName("laugh")
                    Laugh,
                    @SerialName("heart")
                    Heart,
                    @SerialName("hooray")
                    Hooray,
                    @SerialName("rocket")
                    Rocket,
                    @SerialName("eyes")
                    Eyes;
                }
            }

            sealed interface ReactionsListForReleaseResult {
                data class OK(val value: List<Reaction>) : ReactionsListForReleaseResult

                data class NotFound(val value: BasicError) : ReactionsListForReleaseResult
            }

            suspend fun reactionsListForRelease(
                owner: String,
                repo: String,
                releaseId: Long,
                page: Long = 1L,
                perPage: Long = 30L,
                content: Content? = null,
            ): ReactionsListForReleaseResult

            sealed interface ReactionsCreateForReleaseResult {
                data class OK(val value: Reaction) : ReactionsCreateForReleaseResult

                data class Created(val value: Reaction) : ReactionsCreateForReleaseResult

                data class UnprocessableEntity(val value: ValidationError) : ReactionsCreateForReleaseResult
            }

            suspend fun reactionsCreateForRelease(
                owner: String,
                repo: String,
                releaseId: Long,
                body: ReactionsCreateForReleaseBody,
            ): ReactionsCreateForReleaseResult

            suspend fun reactionsDeleteForRelease(
                owner: String,
                repo: String,
                releaseId: Long,
                reactionId: Long,
            ): Unit
        }
    }

    interface Rules {
        val branches: Repos.Rules.BranchesApi

        interface BranchesApi {
            suspend fun reposGetBranchRules(
                owner: String,
                repo: String,
                branch: String,
                page: Long = 1L,
                perPage: Long = 30L,
            ): List<RepositoryRuleDetailed>
        }
    }

    interface Rulesets {
        val ruleSuites: Repos.Rulesets.RuleSuites

        val history: Repos.Rulesets.History

        @Serializable
        data class ReposCreateRepoRulesetBody(
            val name: String,
            val target: Target? = null,
            val enforcement: RepositoryRuleEnforcement,
            @SerialName("bypass_actors") val bypassActors: List<RepositoryRulesetBypassActor>? = null,
            val conditions: RepositoryRulesetConditions? = null,
            val rules: List<RepositoryRule>? = null,
        ) {
            @Serializable
            enum class Target {
                @SerialName("branch") Branch, @SerialName("tag") Tag, @SerialName("push") Push;
            }
        }


        @Serializable
        data class ReposUpdateRepoRulesetBody(
            val name: String? = null,
            val target: Target? = null,
            val enforcement: RepositoryRuleEnforcement? = null,
            @SerialName("bypass_actors") val bypassActors: List<RepositoryRulesetBypassActor>? = null,
            val conditions: RepositoryRulesetConditions? = null,
            val rules: List<RepositoryRule>? = null,
        ) {
            @Serializable
            enum class Target {
                @SerialName("branch") Branch, @SerialName("tag") Tag, @SerialName("push") Push;
            }
        }

        sealed interface ReposGetRepoRulesetsResult {
            data class OK(val value: List<RepositoryRuleset>) : ReposGetRepoRulesetsResult

            data class NotFound(val value: BasicError) : ReposGetRepoRulesetsResult

            data class InternalServerError(val value: BasicError) : ReposGetRepoRulesetsResult
        }

        suspend fun reposGetRepoRulesets(
            owner: String,
            repo: String,
            includesParents: Boolean = true,
            page: Long = 1L,
            perPage: Long = 30L,
            targets: String? = null,
        ): ReposGetRepoRulesetsResult

        sealed interface ReposCreateRepoRulesetResult {
            data class Created(val value: RepositoryRuleset) : ReposCreateRepoRulesetResult

            data class NotFound(val value: BasicError) : ReposCreateRepoRulesetResult

            data class UnprocessableEntity(val value: ValidationError) : ReposCreateRepoRulesetResult

            data class InternalServerError(val value: BasicError) : ReposCreateRepoRulesetResult
        }

        suspend fun reposCreateRepoRuleset(
            owner: String,
            repo: String,
            body: ReposCreateRepoRulesetBody,
        ): ReposCreateRepoRulesetResult

        sealed interface ReposGetRepoRulesetResult {
            data class OK(val value: RepositoryRuleset) : ReposGetRepoRulesetResult

            data class NotFound(val value: BasicError) : ReposGetRepoRulesetResult

            data class InternalServerError(val value: BasicError) : ReposGetRepoRulesetResult
        }

        suspend fun reposGetRepoRuleset(
            owner: String,
            repo: String,
            rulesetId: Long,
            includesParents: Boolean = true,
        ): ReposGetRepoRulesetResult

        sealed interface ReposUpdateRepoRulesetResult {
            data class OK(val value: RepositoryRuleset) : ReposUpdateRepoRulesetResult

            data class NotFound(val value: BasicError) : ReposUpdateRepoRulesetResult

            data class UnprocessableEntity(val value: ValidationError) : ReposUpdateRepoRulesetResult

            data class InternalServerError(val value: BasicError) : ReposUpdateRepoRulesetResult
        }

        suspend fun reposUpdateRepoRuleset(
            owner: String,
            repo: String,
            rulesetId: Long,
            body: ReposUpdateRepoRulesetBody? = null,
        ): ReposUpdateRepoRulesetResult

        sealed interface ReposDeleteRepoRulesetResult {
            data object NoContent : ReposDeleteRepoRulesetResult

            data class NotFound(val value: BasicError) : ReposDeleteRepoRulesetResult

            data class InternalServerError(val value: BasicError) : ReposDeleteRepoRulesetResult
        }

        suspend fun reposDeleteRepoRuleset(
            owner: String,
            repo: String,
            rulesetId: Long,
        ): ReposDeleteRepoRulesetResult

        interface RuleSuites {
            @Serializable
            enum class RuleSuiteResult {
                @SerialName("pass") Pass, @SerialName("fail") Fail, @SerialName("bypass") Bypass, @SerialName("all") All;
            }


            @Serializable
            enum class TimePeriod {
                @SerialName("hour") Hour, @SerialName("day") Day, @SerialName("week") Week, @SerialName("month") Month;
            }

            sealed interface ReposGetRepoRuleSuitesResult {
                data class OK(val value: RuleSuites) : ReposGetRepoRuleSuitesResult

                data class NotFound(val value: BasicError) : ReposGetRepoRuleSuitesResult

                data class InternalServerError(val value: BasicError) : ReposGetRepoRuleSuitesResult
            }

            suspend fun reposGetRepoRuleSuites(
                owner: String,
                repo: String,
                page: Long = 1L,
                perPage: Long = 30L,
                ruleSuiteResult: RuleSuiteResult = RuleSuiteResult.All,
                timePeriod: TimePeriod = TimePeriod.Day,
                actorName: String? = null,
                ref: String? = null,
            ): ReposGetRepoRuleSuitesResult

            sealed interface ReposGetRepoRuleSuiteResult {
                data class OK(val value: RuleSuite) : ReposGetRepoRuleSuiteResult

                data class NotFound(val value: BasicError) : ReposGetRepoRuleSuiteResult

                data class InternalServerError(val value: BasicError) : ReposGetRepoRuleSuiteResult
            }

            suspend fun reposGetRepoRuleSuite(
                owner: String,
                repo: String,
                ruleSuiteId: Long,
            ): ReposGetRepoRuleSuiteResult
        }

        interface History {
            sealed interface ReposGetRepoRulesetHistoryResult {
                data class OK(val value: List<RulesetVersion>) : ReposGetRepoRulesetHistoryResult

                data class NotFound(val value: BasicError) : ReposGetRepoRulesetHistoryResult

                data class InternalServerError(val value: BasicError) : ReposGetRepoRulesetHistoryResult
            }

            suspend fun reposGetRepoRulesetHistory(
                owner: String,
                repo: String,
                rulesetId: Long,
                page: Long = 1L,
                perPage: Long = 30L,
            ): ReposGetRepoRulesetHistoryResult

            sealed interface ReposGetRepoRulesetVersionResult {
                data class OK(val value: RulesetVersionWithState) : ReposGetRepoRulesetVersionResult

                data class NotFound(val value: BasicError) : ReposGetRepoRulesetVersionResult

                data class InternalServerError(val value: BasicError) : ReposGetRepoRulesetVersionResult
            }

            suspend fun reposGetRepoRulesetVersion(
                owner: String,
                repo: String,
                rulesetId: Long,
                versionId: Long,
            ): ReposGetRepoRulesetVersionResult
        }
    }

    interface SecretScanning {
        val alerts: Repos.SecretScanning.Alerts

        val pushProtectionBypasses: Repos.SecretScanning.PushProtectionBypasses

        val scanHistory: Repos.SecretScanning.ScanHistory

        interface Alerts {
            val locations: Repos.SecretScanning.Alerts.Locations

            @Serializable
            enum class Direction {
                @SerialName("asc") Asc, @SerialName("desc") Desc;
            }


            @Serializable
            data class SecretScanningGetAlertResponse(
                val code: String? = null,
                val message: String? = null,
                @SerialName("documentation_url") val documentationUrl: String? = null,
            )


            @Serializable
            data class SecretScanningListAlertsForRepoResponse(
                val code: String? = null,
                val message: String? = null,
                @SerialName("documentation_url") val documentationUrl: String? = null,
            )


            @Serializable
            data class SecretScanningUpdateAlertBody(
                val state: SecretScanningAlertState? = null,
                val resolution: SecretScanningAlertResolution? = null,
                @SerialName("resolution_comment") val resolutionComment: SecretScanningAlertResolutionComment? = null,
                val assignee: SecretScanningAlertAssignee? = null,
            )


            @Serializable
            data class SecretScanningUpdateAlertResponse(
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

            sealed interface SecretScanningListAlertsForRepoResult {
                data class OK(val value: List<SecretScanningAlertResponse>) : SecretScanningListAlertsForRepoResult

                data object NotFound : SecretScanningListAlertsForRepoResult

                data class ServiceUnavailable(val value: SecretScanningListAlertsForRepoResponse) : SecretScanningListAlertsForRepoResult
            }

            suspend fun secretScanningListAlertsForRepo(
                owner: String,
                repo: String,
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
            ): SecretScanningListAlertsForRepoResult

            sealed interface SecretScanningGetAlertResult {
                data class OK(val value: SecretScanningAlertResponse) : SecretScanningGetAlertResult

                data object NotModified : SecretScanningGetAlertResult

                data object NotFound : SecretScanningGetAlertResult

                data class ServiceUnavailable(val value: SecretScanningGetAlertResponse) : SecretScanningGetAlertResult
            }

            suspend fun secretScanningGetAlert(
                owner: String,
                repo: String,
                alertNumber: AlertNumberRequest,
                hideSecret: Boolean = false,
            ): SecretScanningGetAlertResult

            sealed interface SecretScanningUpdateAlertResult {
                data class OK(val value: SecretScanningAlertResponse) : SecretScanningUpdateAlertResult

                data object BadRequest : SecretScanningUpdateAlertResult

                data object NotFound : SecretScanningUpdateAlertResult

                data object UnprocessableEntity : SecretScanningUpdateAlertResult

                data class ServiceUnavailable(val value: SecretScanningUpdateAlertResponse) : SecretScanningUpdateAlertResult
            }

            suspend fun secretScanningUpdateAlert(
                owner: String,
                repo: String,
                alertNumber: AlertNumberRequest,
                body: SecretScanningUpdateAlertBody,
            ): SecretScanningUpdateAlertResult

            interface Locations {
                @Serializable
                data class SecretScanningListLocationsForAlertResponse(
                    val code: String? = null,
                    val message: String? = null,
                    @SerialName("documentation_url") val documentationUrl: String? = null,
                )

                sealed interface SecretScanningListLocationsForAlertResult {
                    data class OK(val value: List<SecretScanningLocation>) : SecretScanningListLocationsForAlertResult

                    data object NotFound : SecretScanningListLocationsForAlertResult

                    data class ServiceUnavailable(val value: SecretScanningListLocationsForAlertResponse) : SecretScanningListLocationsForAlertResult
                }

                suspend fun secretScanningListLocationsForAlert(
                    owner: String,
                    repo: String,
                    alertNumber: AlertNumberRequest,
                    page: Long = 1L,
                    perPage: Long = 30L,
                ): SecretScanningListLocationsForAlertResult
            }
        }

        interface PushProtectionBypasses {
            @Serializable
            data class SecretScanningCreatePushProtectionBypassBody(
                val reason: SecretScanningPushProtectionBypassReason,
                @SerialName("placeholder_id") val placeholderId: SecretScanningPushProtectionBypassPlaceholderId,
            )


            @Serializable
            data class SecretScanningCreatePushProtectionBypassResponse(
                val code: String? = null,
                val message: String? = null,
                @SerialName("documentation_url") val documentationUrl: String? = null,
            )

            sealed interface SecretScanningCreatePushProtectionBypassResult {
                data class OK(val value: SecretScanningPushProtectionBypass) : SecretScanningCreatePushProtectionBypassResult

                data object Forbidden : SecretScanningCreatePushProtectionBypassResult

                data object NotFound : SecretScanningCreatePushProtectionBypassResult

                data object UnprocessableEntity : SecretScanningCreatePushProtectionBypassResult

                data class ServiceUnavailable(val value: SecretScanningCreatePushProtectionBypassResponse) : SecretScanningCreatePushProtectionBypassResult
            }

            suspend fun secretScanningCreatePushProtectionBypass(
                owner: String,
                repo: String,
                body: SecretScanningCreatePushProtectionBypassBody,
            ): SecretScanningCreatePushProtectionBypassResult
        }

        interface ScanHistory {
            @Serializable
            data class SecretScanningGetScanHistoryResponse(
                val code: String? = null,
                val message: String? = null,
                @SerialName("documentation_url") val documentationUrl: String? = null,
            )

            sealed interface SecretScanningGetScanHistoryResult {
                data class OK(val value: SecretScanningScanHistory) : SecretScanningGetScanHistoryResult

                data object NotFound : SecretScanningGetScanHistoryResult

                data class ServiceUnavailable(val value: SecretScanningGetScanHistoryResponse) : SecretScanningGetScanHistoryResult
            }

            suspend fun secretScanningGetScanHistory(
                owner: String,
                repo: String,
            ): SecretScanningGetScanHistoryResult
        }
    }

    interface SecurityAdvisories {
        val reports: Repos.SecurityAdvisories.Reports

        val cve: Repos.SecurityAdvisories.Cve

        val forks: Repos.SecurityAdvisories.ForksApi

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

        sealed interface SecurityAdvisoriesListRepositoryAdvisoriesResult {
            data class OK(val value: List<RepositoryAdvisoryResponse>) : SecurityAdvisoriesListRepositoryAdvisoriesResult

            data class BadRequest(val value: BasicError) : SecurityAdvisoriesListRepositoryAdvisoriesResult

            data class NotFound(val value: BasicError) : SecurityAdvisoriesListRepositoryAdvisoriesResult
        }

        suspend fun securityAdvisoriesListRepositoryAdvisories(
            owner: String,
            repo: String,
            direction: Direction = Direction.Desc,
            perPage: Long = 30L,
            sort: Sort = Sort.Created,
            after: String? = null,
            before: String? = null,
            state: State? = null,
        ): SecurityAdvisoriesListRepositoryAdvisoriesResult

        sealed interface SecurityAdvisoriesCreateRepositoryAdvisoryResult {
            data class Created(val value: RepositoryAdvisoryResponse) : SecurityAdvisoriesCreateRepositoryAdvisoryResult

            data class Forbidden(val value: BasicError) : SecurityAdvisoriesCreateRepositoryAdvisoryResult

            data class NotFound(val value: BasicError) : SecurityAdvisoriesCreateRepositoryAdvisoryResult

            data class UnprocessableEntity(val value: ValidationError) : SecurityAdvisoriesCreateRepositoryAdvisoryResult
        }

        suspend fun securityAdvisoriesCreateRepositoryAdvisory(
            owner: String,
            repo: String,
            body: RepositoryAdvisoryCreate,
        ): SecurityAdvisoriesCreateRepositoryAdvisoryResult

        sealed interface SecurityAdvisoriesGetRepositoryAdvisoryResult {
            data class OK(val value: RepositoryAdvisoryResponse) : SecurityAdvisoriesGetRepositoryAdvisoryResult

            data class Forbidden(val value: BasicError) : SecurityAdvisoriesGetRepositoryAdvisoryResult

            data class NotFound(val value: BasicError) : SecurityAdvisoriesGetRepositoryAdvisoryResult
        }

        suspend fun securityAdvisoriesGetRepositoryAdvisory(
            owner: String,
            repo: String,
            ghsaId: String,
        ): SecurityAdvisoriesGetRepositoryAdvisoryResult

        sealed interface SecurityAdvisoriesUpdateRepositoryAdvisoryResult {
            data class OK(val value: RepositoryAdvisoryResponse) : SecurityAdvisoriesUpdateRepositoryAdvisoryResult

            data class Forbidden(val value: BasicError) : SecurityAdvisoriesUpdateRepositoryAdvisoryResult

            data class NotFound(val value: BasicError) : SecurityAdvisoriesUpdateRepositoryAdvisoryResult

            data class UnprocessableEntity(val value: ValidationError) : SecurityAdvisoriesUpdateRepositoryAdvisoryResult
        }

        suspend fun securityAdvisoriesUpdateRepositoryAdvisory(
            owner: String,
            repo: String,
            ghsaId: String,
            body: RepositoryAdvisoryUpdate,
        ): SecurityAdvisoriesUpdateRepositoryAdvisoryResult

        interface Reports {
            sealed interface SecurityAdvisoriesCreatePrivateVulnerabilityReportResult {
                data class Created(val value: RepositoryAdvisoryResponse) : SecurityAdvisoriesCreatePrivateVulnerabilityReportResult

                data class Forbidden(val value: BasicError) : SecurityAdvisoriesCreatePrivateVulnerabilityReportResult

                data class NotFound(val value: BasicError) : SecurityAdvisoriesCreatePrivateVulnerabilityReportResult

                data class UnprocessableEntity(val value: ValidationError) : SecurityAdvisoriesCreatePrivateVulnerabilityReportResult
            }

            suspend fun securityAdvisoriesCreatePrivateVulnerabilityReport(
                owner: String,
                repo: String,
                body: PrivateVulnerabilityReportCreate,
            ): SecurityAdvisoriesCreatePrivateVulnerabilityReportResult
        }

        interface Cve {
            sealed interface SecurityAdvisoriesCreateRepositoryAdvisoryCveRequestResult {
                data class Accepted(val value: JsonElement) : SecurityAdvisoriesCreateRepositoryAdvisoryCveRequestResult

                data class BadRequest(val value: BasicError) : SecurityAdvisoriesCreateRepositoryAdvisoryCveRequestResult

                data class Forbidden(val value: BasicError) : SecurityAdvisoriesCreateRepositoryAdvisoryCveRequestResult

                data class NotFound(val value: BasicError) : SecurityAdvisoriesCreateRepositoryAdvisoryCveRequestResult

                data class UnprocessableEntity(val value: ValidationError) : SecurityAdvisoriesCreateRepositoryAdvisoryCveRequestResult
            }

            suspend fun securityAdvisoriesCreateRepositoryAdvisoryCveRequest(
                owner: String,
                repo: String,
                ghsaId: String,
            ): SecurityAdvisoriesCreateRepositoryAdvisoryCveRequestResult
        }

        interface ForksApi {
            sealed interface SecurityAdvisoriesCreateForkResult {
                data class Accepted(val value: FullRepository) : SecurityAdvisoriesCreateForkResult

                data class BadRequest(val value: BasicError) : SecurityAdvisoriesCreateForkResult

                data class Forbidden(val value: BasicError) : SecurityAdvisoriesCreateForkResult

                data class NotFound(val value: BasicError) : SecurityAdvisoriesCreateForkResult

                data class UnprocessableEntity(val value: ValidationError) : SecurityAdvisoriesCreateForkResult
            }

            suspend fun securityAdvisoriesCreateFork(
                owner: String,
                repo: String,
                ghsaId: String,
            ): SecurityAdvisoriesCreateForkResult
        }
    }

    interface Stargazers {
        @Serializable(with = ActivityListStargazersForRepoResponse.Serializer::class)
        sealed interface ActivityListStargazersForRepoResponse {
            @Serializable
            @JvmInline
            value class CaseSimpleUsers(val value: List<SimpleUser>) : ActivityListStargazersForRepoResponse

            @Serializable
            @JvmInline
            value class CaseStargazers(val value: List<Stargazer>) : ActivityListStargazersForRepoResponse

            object Serializer : KSerializer<ActivityListStargazersForRepoResponse> {
                @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
                override val descriptor: SerialDescriptor =
                    buildSerialDescriptor("io.github.nomisrev.api.Repos.Stargazers.ActivityListStargazersForRepoResponse", PolymorphicKind.SEALED) {
                        element("CaseSimpleUsers", ListSerializer(SimpleUser.serializer()).descriptor)
                        element("CaseStargazers", ListSerializer(Stargazer.serializer()).descriptor)
                    }

                override fun deserialize(decoder: Decoder): ActivityListStargazersForRepoResponse {
                    val value = decoder.decodeSerializableValue(JsonElement.serializer())
                    val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                    return json.attemptDeserialize(
                        value,
                        CaseSimpleUsers::class to { CaseSimpleUsers(decodeFromJsonElement(ListSerializer(SimpleUser.serializer()), it)) },
                        CaseStargazers::class to { CaseStargazers(decodeFromJsonElement(ListSerializer(Stargazer.serializer()), it)) },
                    )
                }

                override fun serialize(encoder: Encoder, value: ActivityListStargazersForRepoResponse) = when(value) {
                    is CaseSimpleUsers -> encoder.encodeSerializableValue(ListSerializer(SimpleUser.serializer()), value.value)
                    is CaseStargazers -> encoder.encodeSerializableValue(ListSerializer(Stargazer.serializer()), value.value)
                }
            }
        }

        sealed interface ActivityListStargazersForRepoResult {
            data class OK(val value: ActivityListStargazersForRepoResponse) : ActivityListStargazersForRepoResult

            data class UnprocessableEntity(val value: ValidationError) : ActivityListStargazersForRepoResult
        }

        suspend fun activityListStargazersForRepo(
            owner: String,
            repo: String,
            page: Long = 1L,
            perPage: Long = 30L,
        ): ActivityListStargazersForRepoResult
    }

    interface Stats {
        val codeFrequency: Repos.Stats.CodeFrequency

        val commitActivity: Repos.Stats.CommitActivity

        val contributors: Repos.Stats.ContributorsApi

        val participation: Repos.Stats.Participation

        val punchCard: Repos.Stats.PunchCard

        interface CodeFrequency {
            sealed interface ReposGetCodeFrequencyStatsResult {
                data class OK(val value: List<CodeFrequencyStat>) : ReposGetCodeFrequencyStatsResult

                data class Accepted(val value: JsonElement) : ReposGetCodeFrequencyStatsResult

                data object NoContent : ReposGetCodeFrequencyStatsResult

                data object UnprocessableEntity : ReposGetCodeFrequencyStatsResult
            }

            suspend fun reposGetCodeFrequencyStats(
                owner: String,
                repo: String,
            ): ReposGetCodeFrequencyStatsResult
        }

        interface CommitActivity {
            sealed interface ReposGetCommitActivityStatsResult {
                data class OK(val value: List<CommitActivity>) : ReposGetCommitActivityStatsResult

                data class Accepted(val value: JsonElement) : ReposGetCommitActivityStatsResult

                data object NoContent : ReposGetCommitActivityStatsResult
            }

            suspend fun reposGetCommitActivityStats(
                owner: String,
                repo: String,
            ): ReposGetCommitActivityStatsResult
        }

        interface ContributorsApi {
            sealed interface ReposGetContributorsStatsResult {
                data class OK(val value: List<ContributorActivity>) : ReposGetContributorsStatsResult

                data class Accepted(val value: JsonElement) : ReposGetContributorsStatsResult

                data object NoContent : ReposGetContributorsStatsResult
            }

            suspend fun reposGetContributorsStats(
                owner: String,
                repo: String,
            ): ReposGetContributorsStatsResult
        }

        interface Participation {
            sealed interface ReposGetParticipationStatsResult {
                data class OK(val value: ParticipationStats) : ReposGetParticipationStatsResult

                data class NotFound(val value: BasicError) : ReposGetParticipationStatsResult
            }

            suspend fun reposGetParticipationStats(
                owner: String,
                repo: String,
            ): ReposGetParticipationStatsResult
        }

        interface PunchCard {
            sealed interface ReposGetPunchCardStatsResult {
                data class OK(val value: List<CodeFrequencyStat>) : ReposGetPunchCardStatsResult

                data object NoContent : ReposGetPunchCardStatsResult
            }

            suspend fun reposGetPunchCardStats(
                owner: String,
                repo: String,
            ): ReposGetPunchCardStatsResult
        }
    }

    interface Statuses {
        @Serializable
        data class ReposCreateCommitStatusBody(
            val state: State,
            @SerialName("target_url") val targetUrl: String? = null,
            val description: String? = null,
            val context: String? = null,
        ) {
            @Serializable
            enum class State {
                @SerialName("error")
                Error,
                @SerialName("failure")
                Failure,
                @SerialName("pending")
                Pending,
                @SerialName("success")
                Success;
            }
        }

        suspend fun reposCreateCommitStatus(
            owner: String,
            repo: String,
            sha: String,
            body: ReposCreateCommitStatusBody,
        ): Status
    }

    interface Subscribers {
        suspend fun activityListWatchersForRepo(
            owner: String,
            repo: String,
            page: Long = 1L,
            perPage: Long = 30L,
        ): List<SimpleUser>
    }

    interface Subscription {
        @Serializable
        data class ActivitySetRepoSubscriptionBody(val subscribed: Boolean? = null, val ignored: Boolean? = null)

        sealed interface ActivityGetRepoSubscriptionResult {
            data class OK(val value: RepositorySubscription) : ActivityGetRepoSubscriptionResult

            data class Forbidden(val value: BasicError) : ActivityGetRepoSubscriptionResult

            data object NotFound : ActivityGetRepoSubscriptionResult
        }

        suspend fun activityGetRepoSubscription(
            owner: String,
            repo: String,
        ): ActivityGetRepoSubscriptionResult

        suspend fun activitySetRepoSubscription(
            owner: String,
            repo: String,
            body: ActivitySetRepoSubscriptionBody? = null,
        ): RepositorySubscription

        suspend fun activityDeleteRepoSubscription(
            owner: String,
            repo: String,
        ): Unit
    }

    interface Tags {
        suspend fun reposListTags(
            owner: String,
            repo: String,
            page: Long = 1L,
            perPage: Long = 30L,
        ): List<Tag>
    }

    interface Tarball {
        suspend fun reposDownloadTarballArchive(
            owner: String,
            repo: String,
            ref: String,
        ): Unit
    }

    interface Teams {
        sealed interface ReposListTeamsResult {
            data class OK(val value: List<Team>) : ReposListTeamsResult

            data class NotFound(val value: BasicError) : ReposListTeamsResult
        }

        suspend fun reposListTeams(
            owner: String,
            repo: String,
            page: Long = 1L,
            perPage: Long = 30L,
        ): ReposListTeamsResult
    }

    interface Topics {
        @Serializable
        @JvmInline
        value class ReposReplaceAllTopicsBody(val names: List<String>)

        sealed interface ReposGetAllTopicsResult {
            data class OK(val value: Topic) : ReposGetAllTopicsResult

            data class NotFound(val value: BasicError) : ReposGetAllTopicsResult
        }

        suspend fun reposGetAllTopics(
            owner: String,
            repo: String,
            page: Long = 1L,
            perPage: Long = 30L,
        ): ReposGetAllTopicsResult

        sealed interface ReposReplaceAllTopicsResult {
            data class OK(val value: Topic) : ReposReplaceAllTopicsResult

            data class NotFound(val value: BasicError) : ReposReplaceAllTopicsResult

            data class UnprocessableEntity(val value: ValidationErrorSimple) : ReposReplaceAllTopicsResult
        }

        suspend fun reposReplaceAllTopics(
            owner: String,
            repo: String,
            body: ReposReplaceAllTopicsBody,
        ): ReposReplaceAllTopicsResult
    }

    interface Traffic {
        val clones: Repos.Traffic.Clones

        val popular: Repos.Traffic.Popular

        val views: Repos.Traffic.Views

        interface Clones {
            @Serializable
            enum class Per {
                @SerialName("day") Day, @SerialName("week") Week;
            }

            sealed interface ReposGetClonesResult {
                data class OK(val value: CloneTraffic) : ReposGetClonesResult

                data class Forbidden(val value: BasicError) : ReposGetClonesResult
            }

            suspend fun reposGetClones(
                owner: String,
                repo: String,
                per: Per = Per.Day,
            ): ReposGetClonesResult
        }

        interface Popular {
            val paths: Repos.Traffic.Popular.Paths

            val referrers: Repos.Traffic.Popular.Referrers

            interface Paths {
                sealed interface ReposGetTopPathsResult {
                    data class OK(val value: List<ContentTraffic>) : ReposGetTopPathsResult

                    data class Forbidden(val value: BasicError) : ReposGetTopPathsResult
                }

                suspend fun reposGetTopPaths(
                    owner: String,
                    repo: String,
                ): ReposGetTopPathsResult
            }

            interface Referrers {
                sealed interface ReposGetTopReferrersResult {
                    data class OK(val value: List<ReferrerTraffic>) : ReposGetTopReferrersResult

                    data class Forbidden(val value: BasicError) : ReposGetTopReferrersResult
                }

                suspend fun reposGetTopReferrers(
                    owner: String,
                    repo: String,
                ): ReposGetTopReferrersResult
            }
        }

        interface Views {
            @Serializable
            enum class Per {
                @SerialName("day") Day, @SerialName("week") Week;
            }

            sealed interface ReposGetViewsResult {
                data class OK(val value: ViewTraffic) : ReposGetViewsResult

                data class Forbidden(val value: BasicError) : ReposGetViewsResult
            }

            suspend fun reposGetViews(
                owner: String,
                repo: String,
                per: Per = Per.Day,
            ): ReposGetViewsResult
        }
    }

    interface Transfer {
        @Serializable
        data class ReposTransferBody(
            @SerialName("new_owner") val newOwner: String,
            @SerialName("new_name") val newName: String? = null,
            @SerialName("team_ids") val teamIds: List<Long>? = null,
        )

        suspend fun reposTransfer(
            owner: String,
            repo: String,
            body: ReposTransferBody,
        ): MinimalRepository
    }

    interface VulnerabilityAlerts {
        sealed interface ReposCheckVulnerabilityAlertsResult {
            data object NoContent : ReposCheckVulnerabilityAlertsResult

            data object NotFound : ReposCheckVulnerabilityAlertsResult
        }

        suspend fun reposCheckVulnerabilityAlerts(
            owner: String,
            repo: String,
        ): ReposCheckVulnerabilityAlertsResult

        suspend fun reposEnableVulnerabilityAlerts(
            owner: String,
            repo: String,
        ): Unit

        suspend fun reposDisableVulnerabilityAlerts(
            owner: String,
            repo: String,
        ): Unit
    }

    interface Zipball {
        suspend fun reposDownloadZipballArchive(
            owner: String,
            repo: String,
            ref: String,
        ): Unit
    }

    interface Generate {
        @Serializable
        data class ReposCreateUsingTemplateBody(
            val owner: String? = null,
            val name: String,
            val description: String? = null,
            @SerialName("include_all_branches") val includeAllBranches: Boolean? = null,
            val private: Boolean? = null,
        )

        suspend fun reposCreateUsingTemplate(
            templateOwner: String,
            templateRepo: String,
            body: ReposCreateUsingTemplateBody,
        ): FullRepository
    }
}

internal class KtorRepos(private val client: HttpClient) : Repos {
    override val actions: Repos.Actions = KtorReposActions(client)

    override val activity: Repos.Activity = KtorReposActivity(client)

    override val assignees: Repos.Assignees = KtorReposAssignees(client)

    override val attestations: Repos.Attestations = KtorReposAttestations(client)

    override val autolinks: Repos.Autolinks = KtorReposAutolinks(client)

    override val automatedSecurityFixes: Repos.AutomatedSecurityFixes = KtorReposAutomatedSecurityFixes(client)

    override val branches: Repos.Branches = KtorReposBranches(client)

    override val checkRuns: Repos.CheckRuns = KtorReposCheckRuns(client)

    override val checkSuites: Repos.CheckSuites = KtorReposCheckSuites(client)

    override val codeScanning: Repos.CodeScanning = KtorReposCodeScanning(client)

    override val codeSecurityConfiguration: Repos.CodeSecurityConfiguration = KtorReposCodeSecurityConfiguration(client)

    override val codeowners: Repos.Codeowners = KtorReposCodeowners(client)

    override val codespaces: Repos.Codespaces = KtorReposCodespaces(client)

    override val collaborators: Repos.Collaborators = KtorReposCollaborators(client)

    override val comments: Repos.Comments = KtorReposComments(client)

    override val commits: Repos.Commits = KtorReposCommits(client)

    override val community: Repos.Community = KtorReposCommunity(client)

    override val compare: Repos.Compare = KtorReposCompare(client)

    override val contents: Repos.Contents = KtorReposContents(client)

    override val contributors: Repos.Contributors = KtorReposContributors(client)

    override val dependabot: Repos.Dependabot = KtorReposDependabot(client)

    override val dependencyGraph: Repos.DependencyGraph = KtorReposDependencyGraph(client)

    override val deployments: Repos.Deployments = KtorReposDeployments(client)

    override val dispatches: Repos.Dispatches = KtorReposDispatches(client)

    override val environments: Repos.Environments = KtorReposEnvironments(client)

    override val events: Repos.Events = KtorReposEvents(client)

    override val forks: Repos.Forks = KtorReposForks(client)

    override val git: Repos.Git = KtorReposGit(client)

    override val hooks: Repos.Hooks = KtorReposHooks(client)

    override val immutableReleases: Repos.ImmutableReleases = KtorReposImmutableReleases(client)

    override val import: Repos.Import = KtorReposImport(client)

    override val installation: Repos.Installation = KtorReposInstallation(client)

    override val interactionLimits: Repos.InteractionLimits = KtorReposInteractionLimits(client)

    override val invitations: Repos.Invitations = KtorReposInvitations(client)

    override val issues: Repos.Issues = KtorReposIssues(client)

    override val keys: Repos.Keys = KtorReposKeys(client)

    override val labels: Repos.Labels = KtorReposLabels(client)

    override val languages: Repos.Languages = KtorReposLanguages(client)

    override val license: Repos.License = KtorReposLicense(client)

    override val mergeUpstream: Repos.MergeUpstream = KtorReposMergeUpstream(client)

    override val merges: Repos.Merges = KtorReposMerges(client)

    override val milestones: Repos.Milestones = KtorReposMilestones(client)

    override val notifications: Repos.Notifications = KtorReposNotifications(client)

    override val pages: Repos.Pages = KtorReposPages(client)

    override val privateVulnerabilityReporting: Repos.PrivateVulnerabilityReporting = KtorReposPrivateVulnerabilityReporting(client)

    override val properties: Repos.Properties = KtorReposProperties(client)

    override val pulls: Repos.Pulls = KtorReposPulls(client)

    override val readme: Repos.Readme = KtorReposReadme(client)

    override val releases: Repos.Releases = KtorReposReleases(client)

    override val rules: Repos.Rules = KtorReposRules(client)

    override val rulesets: Repos.Rulesets = KtorReposRulesets(client)

    override val secretScanning: Repos.SecretScanning = KtorReposSecretScanning(client)

    override val securityAdvisories: Repos.SecurityAdvisories = KtorReposSecurityAdvisories(client)

    override val stargazers: Repos.Stargazers = KtorReposStargazers(client)

    override val stats: Repos.Stats = KtorReposStats(client)

    override val statuses: Repos.Statuses = KtorReposStatuses(client)

    override val subscribers: Repos.Subscribers = KtorReposSubscribers(client)

    override val subscription: Repos.Subscription = KtorReposSubscription(client)

    override val tags: Repos.Tags = KtorReposTags(client)

    override val tarball: Repos.Tarball = KtorReposTarball(client)

    override val teams: Repos.Teams = KtorReposTeams(client)

    override val topics: Repos.Topics = KtorReposTopics(client)

    override val traffic: Repos.Traffic = KtorReposTraffic(client)

    override val transfer: Repos.Transfer = KtorReposTransfer(client)

    override val vulnerabilityAlerts: Repos.VulnerabilityAlerts = KtorReposVulnerabilityAlerts(client)

    override val zipball: Repos.Zipball = KtorReposZipball(client)

    override val generate: Repos.Generate = KtorReposGenerate(client)

    override suspend fun reposGet(owner: String, repo: String): Repos.ReposGetResult {
        val response = client.get("/repos/$owner/$repo")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.ReposGetResult.OK(response.body())
            HttpStatusCode.MovedPermanently -> Repos.ReposGetResult.MovedPermanently(response.body())
            HttpStatusCode.Forbidden -> Repos.ReposGetResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.ReposGetResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposDelete(owner: String, repo: String): Repos.ReposDeleteResult {
        val response = client.delete("/repos/$owner/$repo")
        return when (response.status) {
            HttpStatusCode.NoContent -> Repos.ReposDeleteResult.NoContent
            HttpStatusCode.TemporaryRedirect -> Repos.ReposDeleteResult.TemporaryRedirect(response.body())
            HttpStatusCode.Forbidden -> Repos.ReposDeleteResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.ReposDeleteResult.NotFound(response.body())
            HttpStatusCode.Conflict -> Repos.ReposDeleteResult.Conflict(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposUpdate(owner: String, repo: String, body: Repos.ReposUpdateBody?): Repos.ReposUpdateResult {
        val response = client.patch("/repos/$owner/$repo") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.ReposUpdateResult.OK(response.body())
            HttpStatusCode.TemporaryRedirect -> Repos.ReposUpdateResult.TemporaryRedirect(response.body())
            HttpStatusCode.Forbidden -> Repos.ReposUpdateResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.ReposUpdateResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.ReposUpdateResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposActions(private val client: HttpClient) : Repos.Actions {
    override val artifacts: Repos.Actions.Artifacts = KtorReposActionsArtifacts(client)

    override val cache: Repos.Actions.Cache = KtorReposActionsCache(client)

    override val caches: Repos.Actions.Caches = KtorReposActionsCaches(client)

    override val jobs: Repos.Actions.Jobs = KtorReposActionsJobs(client)

    override val oidc: Repos.Actions.Oidc = KtorReposActionsOidc(client)

    override val organizationSecrets: Repos.Actions.OrganizationSecrets = KtorReposActionsOrganizationSecrets(client)

    override val organizationVariables: Repos.Actions.OrganizationVariables = KtorReposActionsOrganizationVariables(client)

    override val permissions: Repos.Actions.Permissions = KtorReposActionsPermissions(client)

    override val runners: Repos.Actions.Runners = KtorReposActionsRunners(client)

    override val runs: Repos.Actions.Runs = KtorReposActionsRuns(client)

    override val secrets: Repos.Actions.Secrets = KtorReposActionsSecrets(client)

    override val variables: Repos.Actions.Variables = KtorReposActionsVariables(client)

    override val workflows: Repos.Actions.Workflows = KtorReposActionsWorkflows(client)
}

internal class KtorReposActionsArtifacts(private val client: HttpClient) : Repos.Actions.Artifacts {
    override suspend fun actionsListArtifactsForRepo(owner: String, repo: String, page: Long, perPage: Long, name: String?): Repos.Actions.Artifacts.ActionsListArtifactsForRepoResponse =
        client.get("/repos/$owner/$repo/actions/artifacts") {
            parameter("page", page)
            parameter("per_page", perPage)
            name?.let { parameter("name", it) }
        }.body()

    override suspend fun actionsGetArtifact(owner: String, repo: String, artifactId: Long): Artifact =
        client.get("/repos/$owner/$repo/actions/artifacts/$artifactId").body()

    override suspend fun actionsDeleteArtifact(owner: String, repo: String, artifactId: Long): Unit =
        client.delete("/repos/$owner/$repo/actions/artifacts/$artifactId").body()

    override suspend fun actionsDownloadArtifact(owner: String, repo: String, artifactId: Long, archiveFormat: String): Repos.Actions.Artifacts.ActionsDownloadArtifactResult {
        val response = client.get("/repos/$owner/$repo/actions/artifacts/$artifactId/$archiveFormat")
        return when (response.status) {
            HttpStatusCode.Found -> Repos.Actions.Artifacts.ActionsDownloadArtifactResult.Found
            HttpStatusCode.Gone -> Repos.Actions.Artifacts.ActionsDownloadArtifactResult.Gone(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposActionsCache(private val client: HttpClient) : Repos.Actions.Cache {
    override val retentionLimit: Repos.Actions.Cache.RetentionLimit = KtorReposActionsCacheRetentionLimit(client)

    override val storageLimit: Repos.Actions.Cache.StorageLimit = KtorReposActionsCacheStorageLimit(client)

    override val usage: Repos.Actions.Cache.Usage = KtorReposActionsCacheUsage(client)
}

internal class KtorReposActionsCacheRetentionLimit(private val client: HttpClient) : Repos.Actions.Cache.RetentionLimit {
    override suspend fun actionsGetActionsCacheRetentionLimitForRepository(owner: String, repo: String): Repos.Actions.Cache.RetentionLimit.ActionsGetActionsCacheRetentionLimitForRepositoryResult {
        val response = client.get("/repos/$owner/$repo/actions/cache/retention-limit")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Actions.Cache.RetentionLimit.ActionsGetActionsCacheRetentionLimitForRepositoryResult.OK(response.body())
            HttpStatusCode.Forbidden -> Repos.Actions.Cache.RetentionLimit.ActionsGetActionsCacheRetentionLimitForRepositoryResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.Actions.Cache.RetentionLimit.ActionsGetActionsCacheRetentionLimitForRepositoryResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun actionsSetActionsCacheRetentionLimitForRepository(owner: String, repo: String, body: ActionsCacheRetentionLimitForRepository): Repos.Actions.Cache.RetentionLimit.ActionsSetActionsCacheRetentionLimitForRepositoryResult {
        val response = client.put("/repos/$owner/$repo/actions/cache/retention-limit") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.NoContent -> Repos.Actions.Cache.RetentionLimit.ActionsSetActionsCacheRetentionLimitForRepositoryResult.NoContent
            HttpStatusCode.BadRequest -> Repos.Actions.Cache.RetentionLimit.ActionsSetActionsCacheRetentionLimitForRepositoryResult.BadRequest(response.body())
            HttpStatusCode.Forbidden -> Repos.Actions.Cache.RetentionLimit.ActionsSetActionsCacheRetentionLimitForRepositoryResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.Actions.Cache.RetentionLimit.ActionsSetActionsCacheRetentionLimitForRepositoryResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposActionsCacheStorageLimit(private val client: HttpClient) : Repos.Actions.Cache.StorageLimit {
    override suspend fun actionsGetActionsCacheStorageLimitForRepository(owner: String, repo: String): Repos.Actions.Cache.StorageLimit.ActionsGetActionsCacheStorageLimitForRepositoryResult {
        val response = client.get("/repos/$owner/$repo/actions/cache/storage-limit")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Actions.Cache.StorageLimit.ActionsGetActionsCacheStorageLimitForRepositoryResult.OK(response.body())
            HttpStatusCode.Forbidden -> Repos.Actions.Cache.StorageLimit.ActionsGetActionsCacheStorageLimitForRepositoryResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.Actions.Cache.StorageLimit.ActionsGetActionsCacheStorageLimitForRepositoryResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun actionsSetActionsCacheStorageLimitForRepository(owner: String, repo: String, body: ActionsCacheStorageLimitForRepository): Repos.Actions.Cache.StorageLimit.ActionsSetActionsCacheStorageLimitForRepositoryResult {
        val response = client.put("/repos/$owner/$repo/actions/cache/storage-limit") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.NoContent -> Repos.Actions.Cache.StorageLimit.ActionsSetActionsCacheStorageLimitForRepositoryResult.NoContent
            HttpStatusCode.BadRequest -> Repos.Actions.Cache.StorageLimit.ActionsSetActionsCacheStorageLimitForRepositoryResult.BadRequest(response.body())
            HttpStatusCode.Forbidden -> Repos.Actions.Cache.StorageLimit.ActionsSetActionsCacheStorageLimitForRepositoryResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.Actions.Cache.StorageLimit.ActionsSetActionsCacheStorageLimitForRepositoryResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposActionsCacheUsage(private val client: HttpClient) : Repos.Actions.Cache.Usage {
    override suspend fun actionsGetActionsCacheUsage(owner: String, repo: String): ActionsCacheUsageByRepository =
        client.get("/repos/$owner/$repo/actions/cache/usage").body()
}

internal class KtorReposActionsCaches(private val client: HttpClient) : Repos.Actions.Caches {
    override suspend fun actionsGetActionsCacheList(owner: String, repo: String, direction: Repos.Actions.Caches.Direction, page: Long, perPage: Long, sort: Repos.Actions.Caches.Sort, key: String?, ref: String?): ActionsCacheList =
        client.get("/repos/$owner/$repo/actions/caches") {
            parameter("direction", direction)
            parameter("page", page)
            parameter("per_page", perPage)
            parameter("sort", sort)
            key?.let { parameter("key", it) }
            ref?.let { parameter("ref", it) }
        }.body()

    override suspend fun actionsDeleteActionsCacheByKey(owner: String, repo: String, key: String, ref: String?): ActionsCacheList =
        client.delete("/repos/$owner/$repo/actions/caches") {
            parameter("key", key)
            ref?.let { parameter("ref", it) }
        }.body()

    override suspend fun actionsDeleteActionsCacheById(owner: String, repo: String, cacheId: Long): Unit =
        client.delete("/repos/$owner/$repo/actions/caches/$cacheId").body()
}

internal class KtorReposActionsJobs(private val client: HttpClient) : Repos.Actions.Jobs {
    override val logs: Repos.Actions.Jobs.Logs = KtorReposActionsJobsLogs(client)

    override val rerun: Repos.Actions.Jobs.Rerun = KtorReposActionsJobsRerun(client)

    override suspend fun actionsGetJobForWorkflowRun(owner: String, repo: String, jobId: Long): Job =
        client.get("/repos/$owner/$repo/actions/jobs/$jobId").body()
}

internal class KtorReposActionsJobsLogs(private val client: HttpClient) : Repos.Actions.Jobs.Logs {
    override suspend fun actionsDownloadJobLogsForWorkflowRun(owner: String, repo: String, jobId: Long): Unit =
        client.get("/repos/$owner/$repo/actions/jobs/$jobId/logs").body()
}

internal class KtorReposActionsJobsRerun(private val client: HttpClient) : Repos.Actions.Jobs.Rerun {
    override suspend fun actionsReRunJobForWorkflowRun(owner: String, repo: String, jobId: Long, body: Repos.Actions.Jobs.Rerun.ActionsReRunJobForWorkflowRunBody?): Repos.Actions.Jobs.Rerun.ActionsReRunJobForWorkflowRunResult {
        val response = client.post("/repos/$owner/$repo/actions/jobs/$jobId/rerun") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }
        return when (response.status) {
            HttpStatusCode.Created -> Repos.Actions.Jobs.Rerun.ActionsReRunJobForWorkflowRunResult.Created(response.body())
            HttpStatusCode.Forbidden -> Repos.Actions.Jobs.Rerun.ActionsReRunJobForWorkflowRunResult.Forbidden(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposActionsOidc(private val client: HttpClient) : Repos.Actions.Oidc {
    override val customization: Repos.Actions.Oidc.Customization = KtorReposActionsOidcCustomization(client)
}

internal class KtorReposActionsOidcCustomization(private val client: HttpClient) : Repos.Actions.Oidc.Customization {
    override val sub: Repos.Actions.Oidc.Customization.Sub = KtorReposActionsOidcCustomizationSub(client)
}

internal class KtorReposActionsOidcCustomizationSub(private val client: HttpClient) : Repos.Actions.Oidc.Customization.Sub {
    override suspend fun actionsGetCustomOidcSubClaimForRepo(owner: String, repo: String): Repos.Actions.Oidc.Customization.Sub.ActionsGetCustomOidcSubClaimForRepoResult {
        val response = client.get("/repos/$owner/$repo/actions/oidc/customization/sub")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Actions.Oidc.Customization.Sub.ActionsGetCustomOidcSubClaimForRepoResult.OK(response.body())
            HttpStatusCode.BadRequest -> Repos.Actions.Oidc.Customization.Sub.ActionsGetCustomOidcSubClaimForRepoResult.BadRequest(response.body())
            HttpStatusCode.NotFound -> Repos.Actions.Oidc.Customization.Sub.ActionsGetCustomOidcSubClaimForRepoResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun actionsSetCustomOidcSubClaimForRepo(owner: String, repo: String, body: Repos.Actions.Oidc.Customization.Sub.ActionsSetCustomOidcSubClaimForRepoBody): Repos.Actions.Oidc.Customization.Sub.ActionsSetCustomOidcSubClaimForRepoResult {
        val response = client.put("/repos/$owner/$repo/actions/oidc/customization/sub") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> Repos.Actions.Oidc.Customization.Sub.ActionsSetCustomOidcSubClaimForRepoResult.Created(response.body())
            HttpStatusCode.BadRequest -> Repos.Actions.Oidc.Customization.Sub.ActionsSetCustomOidcSubClaimForRepoResult.BadRequest(response.body())
            HttpStatusCode.NotFound -> Repos.Actions.Oidc.Customization.Sub.ActionsSetCustomOidcSubClaimForRepoResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Actions.Oidc.Customization.Sub.ActionsSetCustomOidcSubClaimForRepoResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposActionsOrganizationSecrets(private val client: HttpClient) : Repos.Actions.OrganizationSecrets {
    override suspend fun actionsListRepoOrganizationSecrets(owner: String, repo: String, page: Long, perPage: Long): Repos.Actions.OrganizationSecrets.ActionsListRepoOrganizationSecretsResponse =
        client.get("/repos/$owner/$repo/actions/organization-secrets") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()
}

internal class KtorReposActionsOrganizationVariables(private val client: HttpClient) : Repos.Actions.OrganizationVariables {
    override suspend fun actionsListRepoOrganizationVariables(owner: String, repo: String, page: Long, perPage: Long): Repos.Actions.OrganizationVariables.ActionsListRepoOrganizationVariablesResponse =
        client.get("/repos/$owner/$repo/actions/organization-variables") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()
}

internal class KtorReposActionsPermissions(private val client: HttpClient) : Repos.Actions.Permissions {
    override val access: Repos.Actions.Permissions.Access = KtorReposActionsPermissionsAccess(client)

    override val artifactAndLogRetention: Repos.Actions.Permissions.ArtifactAndLogRetention = KtorReposActionsPermissionsArtifactAndLogRetention(client)

    override val forkPrContributorApproval: Repos.Actions.Permissions.ForkPrContributorApproval = KtorReposActionsPermissionsForkPrContributorApproval(client)

    override val forkPrWorkflowsPrivateRepos: Repos.Actions.Permissions.ForkPrWorkflowsPrivateRepos = KtorReposActionsPermissionsForkPrWorkflowsPrivateRepos(client)

    override val selectedActions: Repos.Actions.Permissions.SelectedActions = KtorReposActionsPermissionsSelectedActions(client)

    override val workflow: Repos.Actions.Permissions.Workflow = KtorReposActionsPermissionsWorkflow(client)

    override suspend fun actionsGetGithubActionsPermissionsRepository(owner: String, repo: String): ActionsRepositoryPermissions =
        client.get("/repos/$owner/$repo/actions/permissions").body()

    override suspend fun actionsSetGithubActionsPermissionsRepository(owner: String, repo: String, body: Repos.Actions.Permissions.ActionsSetGithubActionsPermissionsRepositoryBody): Unit =
        client.put("/repos/$owner/$repo/actions/permissions") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()
}

internal class KtorReposActionsPermissionsAccess(private val client: HttpClient) : Repos.Actions.Permissions.Access {
    override suspend fun actionsGetWorkflowAccessToRepository(owner: String, repo: String): ActionsWorkflowAccessToRepository =
        client.get("/repos/$owner/$repo/actions/permissions/access").body()

    override suspend fun actionsSetWorkflowAccessToRepository(owner: String, repo: String, body: ActionsWorkflowAccessToRepository): Unit =
        client.put("/repos/$owner/$repo/actions/permissions/access") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()
}

internal class KtorReposActionsPermissionsArtifactAndLogRetention(private val client: HttpClient) : Repos.Actions.Permissions.ArtifactAndLogRetention {
    override suspend fun actionsGetArtifactAndLogRetentionSettingsRepository(owner: String, repo: String): Repos.Actions.Permissions.ArtifactAndLogRetention.ActionsGetArtifactAndLogRetentionSettingsRepositoryResult {
        val response = client.get("/repos/$owner/$repo/actions/permissions/artifact-and-log-retention")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Actions.Permissions.ArtifactAndLogRetention.ActionsGetArtifactAndLogRetentionSettingsRepositoryResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Actions.Permissions.ArtifactAndLogRetention.ActionsGetArtifactAndLogRetentionSettingsRepositoryResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun actionsSetArtifactAndLogRetentionSettingsRepository(owner: String, repo: String, body: ActionsArtifactAndLogRetention): Repos.Actions.Permissions.ArtifactAndLogRetention.ActionsSetArtifactAndLogRetentionSettingsRepositoryResult {
        val response = client.put("/repos/$owner/$repo/actions/permissions/artifact-and-log-retention") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.NoContent -> Repos.Actions.Permissions.ArtifactAndLogRetention.ActionsSetArtifactAndLogRetentionSettingsRepositoryResult.NoContent
            HttpStatusCode.NotFound -> Repos.Actions.Permissions.ArtifactAndLogRetention.ActionsSetArtifactAndLogRetentionSettingsRepositoryResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Actions.Permissions.ArtifactAndLogRetention.ActionsSetArtifactAndLogRetentionSettingsRepositoryResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposActionsPermissionsForkPrContributorApproval(private val client: HttpClient) : Repos.Actions.Permissions.ForkPrContributorApproval {
    override suspend fun actionsGetForkPrContributorApprovalPermissionsRepository(owner: String, repo: String): Repos.Actions.Permissions.ForkPrContributorApproval.ActionsGetForkPrContributorApprovalPermissionsRepositoryResult {
        val response = client.get("/repos/$owner/$repo/actions/permissions/fork-pr-contributor-approval")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Actions.Permissions.ForkPrContributorApproval.ActionsGetForkPrContributorApprovalPermissionsRepositoryResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Actions.Permissions.ForkPrContributorApproval.ActionsGetForkPrContributorApprovalPermissionsRepositoryResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun actionsSetForkPrContributorApprovalPermissionsRepository(owner: String, repo: String, body: ActionsForkPrContributorApproval): Repos.Actions.Permissions.ForkPrContributorApproval.ActionsSetForkPrContributorApprovalPermissionsRepositoryResult {
        val response = client.put("/repos/$owner/$repo/actions/permissions/fork-pr-contributor-approval") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.NoContent -> Repos.Actions.Permissions.ForkPrContributorApproval.ActionsSetForkPrContributorApprovalPermissionsRepositoryResult.NoContent
            HttpStatusCode.NotFound -> Repos.Actions.Permissions.ForkPrContributorApproval.ActionsSetForkPrContributorApprovalPermissionsRepositoryResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Actions.Permissions.ForkPrContributorApproval.ActionsSetForkPrContributorApprovalPermissionsRepositoryResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposActionsPermissionsForkPrWorkflowsPrivateRepos(private val client: HttpClient) : Repos.Actions.Permissions.ForkPrWorkflowsPrivateRepos {
    override suspend fun actionsGetPrivateRepoForkPrWorkflowsSettingsRepository(owner: String, repo: String): Repos.Actions.Permissions.ForkPrWorkflowsPrivateRepos.ActionsGetPrivateRepoForkPrWorkflowsSettingsRepositoryResult {
        val response = client.get("/repos/$owner/$repo/actions/permissions/fork-pr-workflows-private-repos")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Actions.Permissions.ForkPrWorkflowsPrivateRepos.ActionsGetPrivateRepoForkPrWorkflowsSettingsRepositoryResult.OK(response.body())
            HttpStatusCode.Forbidden -> Repos.Actions.Permissions.ForkPrWorkflowsPrivateRepos.ActionsGetPrivateRepoForkPrWorkflowsSettingsRepositoryResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.Actions.Permissions.ForkPrWorkflowsPrivateRepos.ActionsGetPrivateRepoForkPrWorkflowsSettingsRepositoryResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun actionsSetPrivateRepoForkPrWorkflowsSettingsRepository(owner: String, repo: String, body: ActionsForkPrWorkflowsPrivateReposRequest): Repos.Actions.Permissions.ForkPrWorkflowsPrivateRepos.ActionsSetPrivateRepoForkPrWorkflowsSettingsRepositoryResult {
        val response = client.put("/repos/$owner/$repo/actions/permissions/fork-pr-workflows-private-repos") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.NoContent -> Repos.Actions.Permissions.ForkPrWorkflowsPrivateRepos.ActionsSetPrivateRepoForkPrWorkflowsSettingsRepositoryResult.NoContent
            HttpStatusCode.NotFound -> Repos.Actions.Permissions.ForkPrWorkflowsPrivateRepos.ActionsSetPrivateRepoForkPrWorkflowsSettingsRepositoryResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Actions.Permissions.ForkPrWorkflowsPrivateRepos.ActionsSetPrivateRepoForkPrWorkflowsSettingsRepositoryResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposActionsPermissionsSelectedActions(private val client: HttpClient) : Repos.Actions.Permissions.SelectedActions {
    override suspend fun actionsGetAllowedActionsRepository(owner: String, repo: String): SelectedActions =
        client.get("/repos/$owner/$repo/actions/permissions/selected-actions").body()

    override suspend fun actionsSetAllowedActionsRepository(owner: String, repo: String, body: SelectedActions?): Unit =
        client.put("/repos/$owner/$repo/actions/permissions/selected-actions") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }.body()
}

internal class KtorReposActionsPermissionsWorkflow(private val client: HttpClient) : Repos.Actions.Permissions.Workflow {
    override suspend fun actionsGetGithubActionsDefaultWorkflowPermissionsRepository(owner: String, repo: String): ActionsGetDefaultWorkflowPermissions =
        client.get("/repos/$owner/$repo/actions/permissions/workflow").body()

    override suspend fun actionsSetGithubActionsDefaultWorkflowPermissionsRepository(owner: String, repo: String, body: ActionsSetDefaultWorkflowPermissions): Repos.Actions.Permissions.Workflow.ActionsSetGithubActionsDefaultWorkflowPermissionsRepositoryResult {
        val response = client.put("/repos/$owner/$repo/actions/permissions/workflow") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.NoContent -> Repos.Actions.Permissions.Workflow.ActionsSetGithubActionsDefaultWorkflowPermissionsRepositoryResult.NoContent
            HttpStatusCode.Conflict -> Repos.Actions.Permissions.Workflow.ActionsSetGithubActionsDefaultWorkflowPermissionsRepositoryResult.Conflict
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposActionsRunners(private val client: HttpClient) : Repos.Actions.Runners {
    override val downloads: Repos.Actions.Runners.Downloads = KtorReposActionsRunnersDownloads(client)

    override val generateJitconfig: Repos.Actions.Runners.GenerateJitconfig = KtorReposActionsRunnersGenerateJitconfig(client)

    override val registrationToken: Repos.Actions.Runners.RegistrationToken = KtorReposActionsRunnersRegistrationToken(client)

    override val removeToken: Repos.Actions.Runners.RemoveToken = KtorReposActionsRunnersRemoveToken(client)

    override val labels: Repos.Actions.Runners.LabelsApi = KtorReposActionsRunnersLabelsApi(client)

    override suspend fun actionsListSelfHostedRunnersForRepo(owner: String, repo: String, page: Long, perPage: Long, name: String?): Repos.Actions.Runners.ActionsListSelfHostedRunnersForRepoResponse =
        client.get("/repos/$owner/$repo/actions/runners") {
            parameter("page", page)
            parameter("per_page", perPage)
            name?.let { parameter("name", it) }
        }.body()

    override suspend fun actionsGetSelfHostedRunnerForRepo(owner: String, repo: String, runnerId: Long): Runner =
        client.get("/repos/$owner/$repo/actions/runners/$runnerId").body()

    override suspend fun actionsDeleteSelfHostedRunnerFromRepo(owner: String, repo: String, runnerId: Long): Repos.Actions.Runners.ActionsDeleteSelfHostedRunnerFromRepoResult {
        val response = client.delete("/repos/$owner/$repo/actions/runners/$runnerId")
        return when (response.status) {
            HttpStatusCode.NoContent -> Repos.Actions.Runners.ActionsDeleteSelfHostedRunnerFromRepoResult.NoContent
            HttpStatusCode.UnprocessableEntity -> Repos.Actions.Runners.ActionsDeleteSelfHostedRunnerFromRepoResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposActionsRunnersDownloads(private val client: HttpClient) : Repos.Actions.Runners.Downloads {
    override suspend fun actionsListRunnerApplicationsForRepo(owner: String, repo: String): List<RunnerApplication> =
        client.get("/repos/$owner/$repo/actions/runners/downloads").body()
}

internal class KtorReposActionsRunnersGenerateJitconfig(private val client: HttpClient) : Repos.Actions.Runners.GenerateJitconfig {
    override suspend fun actionsGenerateRunnerJitconfigForRepo(owner: String, repo: String, body: Repos.Actions.Runners.GenerateJitconfig.ActionsGenerateRunnerJitconfigForRepoBody): Repos.Actions.Runners.GenerateJitconfig.ActionsGenerateRunnerJitconfigForRepoResult {
        val response = client.post("/repos/$owner/$repo/actions/runners/generate-jitconfig") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> Repos.Actions.Runners.GenerateJitconfig.ActionsGenerateRunnerJitconfigForRepoResult.Created(response.body())
            HttpStatusCode.NotFound -> Repos.Actions.Runners.GenerateJitconfig.ActionsGenerateRunnerJitconfigForRepoResult.NotFound(response.body())
            HttpStatusCode.Conflict -> Repos.Actions.Runners.GenerateJitconfig.ActionsGenerateRunnerJitconfigForRepoResult.Conflict(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Actions.Runners.GenerateJitconfig.ActionsGenerateRunnerJitconfigForRepoResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposActionsRunnersRegistrationToken(private val client: HttpClient) : Repos.Actions.Runners.RegistrationToken {
    override suspend fun actionsCreateRegistrationTokenForRepo(owner: String, repo: String): AuthenticationToken =
        client.post("/repos/$owner/$repo/actions/runners/registration-token").body()
}

internal class KtorReposActionsRunnersRemoveToken(private val client: HttpClient) : Repos.Actions.Runners.RemoveToken {
    override suspend fun actionsCreateRemoveTokenForRepo(owner: String, repo: String): AuthenticationToken =
        client.post("/repos/$owner/$repo/actions/runners/remove-token").body()
}

internal class KtorReposActionsRunnersLabelsApi(private val client: HttpClient) : Repos.Actions.Runners.LabelsApi {
    override suspend fun actionsListLabelsForSelfHostedRunnerForRepo(owner: String, repo: String, runnerId: Long): Repos.Actions.Runners.LabelsApi.ActionsListLabelsForSelfHostedRunnerForRepoResult {
        val response = client.get("/repos/$owner/$repo/actions/runners/$runnerId/labels")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Actions.Runners.LabelsApi.ActionsListLabelsForSelfHostedRunnerForRepoResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Actions.Runners.LabelsApi.ActionsListLabelsForSelfHostedRunnerForRepoResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun actionsSetCustomLabelsForSelfHostedRunnerForRepo(owner: String, repo: String, runnerId: Long, body: Repos.Actions.Runners.LabelsApi.ActionsSetCustomLabelsForSelfHostedRunnerForRepoBody): Repos.Actions.Runners.LabelsApi.ActionsSetCustomLabelsForSelfHostedRunnerForRepoResult {
        val response = client.put("/repos/$owner/$repo/actions/runners/$runnerId/labels") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Actions.Runners.LabelsApi.ActionsSetCustomLabelsForSelfHostedRunnerForRepoResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Actions.Runners.LabelsApi.ActionsSetCustomLabelsForSelfHostedRunnerForRepoResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Actions.Runners.LabelsApi.ActionsSetCustomLabelsForSelfHostedRunnerForRepoResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun actionsAddCustomLabelsToSelfHostedRunnerForRepo(owner: String, repo: String, runnerId: Long, body: Repos.Actions.Runners.LabelsApi.ActionsAddCustomLabelsToSelfHostedRunnerForRepoBody): Repos.Actions.Runners.LabelsApi.ActionsAddCustomLabelsToSelfHostedRunnerForRepoResult {
        val response = client.post("/repos/$owner/$repo/actions/runners/$runnerId/labels") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Actions.Runners.LabelsApi.ActionsAddCustomLabelsToSelfHostedRunnerForRepoResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Actions.Runners.LabelsApi.ActionsAddCustomLabelsToSelfHostedRunnerForRepoResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Actions.Runners.LabelsApi.ActionsAddCustomLabelsToSelfHostedRunnerForRepoResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun actionsRemoveAllCustomLabelsFromSelfHostedRunnerForRepo(owner: String, repo: String, runnerId: Long): Repos.Actions.Runners.LabelsApi.ActionsRemoveAllCustomLabelsFromSelfHostedRunnerForRepoResult {
        val response = client.delete("/repos/$owner/$repo/actions/runners/$runnerId/labels")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Actions.Runners.LabelsApi.ActionsRemoveAllCustomLabelsFromSelfHostedRunnerForRepoResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Actions.Runners.LabelsApi.ActionsRemoveAllCustomLabelsFromSelfHostedRunnerForRepoResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun actionsRemoveCustomLabelFromSelfHostedRunnerForRepo(owner: String, repo: String, runnerId: Long, name: String): Repos.Actions.Runners.LabelsApi.ActionsRemoveCustomLabelFromSelfHostedRunnerForRepoResult {
        val response = client.delete("/repos/$owner/$repo/actions/runners/$runnerId/labels/$name")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Actions.Runners.LabelsApi.ActionsRemoveCustomLabelFromSelfHostedRunnerForRepoResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Actions.Runners.LabelsApi.ActionsRemoveCustomLabelFromSelfHostedRunnerForRepoResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Actions.Runners.LabelsApi.ActionsRemoveCustomLabelFromSelfHostedRunnerForRepoResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposActionsRuns(private val client: HttpClient) : Repos.Actions.Runs {
    override val approvals: Repos.Actions.Runs.Approvals = KtorReposActionsRunsApprovals(client)

    override val approve: Repos.Actions.Runs.Approve = KtorReposActionsRunsApprove(client)

    override val artifacts: Repos.Actions.Runs.ArtifactsApi = KtorReposActionsRunsArtifactsApi(client)

    override val attempts: Repos.Actions.Runs.Attempts = KtorReposActionsRunsAttempts(client)

    override val cancel: Repos.Actions.Runs.Cancel = KtorReposActionsRunsCancel(client)

    override val deploymentProtectionRule: Repos.Actions.Runs.DeploymentProtectionRule = KtorReposActionsRunsDeploymentProtectionRule(client)

    override val forceCancel: Repos.Actions.Runs.ForceCancel = KtorReposActionsRunsForceCancel(client)

    override val jobs: Repos.Actions.Runs.JobsApi = KtorReposActionsRunsJobsApi(client)

    override val logs: Repos.Actions.Runs.Logs = KtorReposActionsRunsLogs(client)

    override val pendingDeployments: Repos.Actions.Runs.PendingDeployments = KtorReposActionsRunsPendingDeployments(client)

    override val rerun: Repos.Actions.Runs.Rerun = KtorReposActionsRunsRerun(client)

    override val rerunFailedJobs: Repos.Actions.Runs.RerunFailedJobs = KtorReposActionsRunsRerunFailedJobs(client)

    override val timing: Repos.Actions.Runs.Timing = KtorReposActionsRunsTiming(client)

    override suspend fun actionsListWorkflowRunsForRepo(owner: String, repo: String, excludePullRequests: Boolean, page: Long, perPage: Long, actor: String?, branch: String?, checkSuiteId: Long?, created: LocalDateTime?, event: String?, headSha: String?, status: Repos.Actions.Runs.Status?): Repos.Actions.Runs.ActionsListWorkflowRunsForRepoResponse =
        client.get("/repos/$owner/$repo/actions/runs") {
            parameter("exclude_pull_requests", excludePullRequests)
            parameter("page", page)
            parameter("per_page", perPage)
            actor?.let { parameter("actor", it) }
            branch?.let { parameter("branch", it) }
            checkSuiteId?.let { parameter("check_suite_id", it) }
            created?.let { parameter("created", it) }
            event?.let { parameter("event", it) }
            headSha?.let { parameter("head_sha", it) }
            status?.let { parameter("status", it) }
        }.body()

    override suspend fun actionsGetWorkflowRun(owner: String, repo: String, runId: Long, excludePullRequests: Boolean): WorkflowRun =
        client.get("/repos/$owner/$repo/actions/runs/$runId") {
            parameter("exclude_pull_requests", excludePullRequests)
        }.body()

    override suspend fun actionsDeleteWorkflowRun(owner: String, repo: String, runId: Long): Unit =
        client.delete("/repos/$owner/$repo/actions/runs/$runId").body()
}

internal class KtorReposActionsRunsApprovals(private val client: HttpClient) : Repos.Actions.Runs.Approvals {
    override suspend fun actionsGetReviewsForRun(owner: String, repo: String, runId: Long): List<EnvironmentApprovals> =
        client.get("/repos/$owner/$repo/actions/runs/$runId/approvals").body()
}

internal class KtorReposActionsRunsApprove(private val client: HttpClient) : Repos.Actions.Runs.Approve {
    override suspend fun actionsApproveWorkflowRun(owner: String, repo: String, runId: Long): Repos.Actions.Runs.Approve.ActionsApproveWorkflowRunResult {
        val response = client.post("/repos/$owner/$repo/actions/runs/$runId/approve")
        return when (response.status) {
            HttpStatusCode.Created -> Repos.Actions.Runs.Approve.ActionsApproveWorkflowRunResult.Created(response.body())
            HttpStatusCode.Forbidden -> Repos.Actions.Runs.Approve.ActionsApproveWorkflowRunResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.Actions.Runs.Approve.ActionsApproveWorkflowRunResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposActionsRunsArtifactsApi(private val client: HttpClient) : Repos.Actions.Runs.ArtifactsApi {
    override suspend fun actionsListWorkflowRunArtifacts(owner: String, repo: String, runId: Long, direction: Repos.Actions.Runs.ArtifactsApi.Direction, page: Long, perPage: Long, name: String?): Repos.Actions.Runs.ArtifactsApi.ActionsListWorkflowRunArtifactsResponse =
        client.get("/repos/$owner/$repo/actions/runs/$runId/artifacts") {
            parameter("direction", direction)
            parameter("page", page)
            parameter("per_page", perPage)
            name?.let { parameter("name", it) }
        }.body()
}

internal class KtorReposActionsRunsAttempts(private val client: HttpClient) : Repos.Actions.Runs.Attempts {
    override val jobs: Repos.Actions.Runs.Attempts.JobsApi2 = KtorReposActionsRunsAttemptsJobsApi2(client)

    override val logs: Repos.Actions.Runs.Attempts.LogsApi = KtorReposActionsRunsAttemptsLogsApi(client)

    override suspend fun actionsGetWorkflowRunAttempt(owner: String, repo: String, runId: Long, attemptNumber: Long, excludePullRequests: Boolean): WorkflowRun =
        client.get("/repos/$owner/$repo/actions/runs/$runId/attempts/$attemptNumber") {
            parameter("exclude_pull_requests", excludePullRequests)
        }.body()
}

internal class KtorReposActionsRunsAttemptsJobsApi2(private val client: HttpClient) : Repos.Actions.Runs.Attempts.JobsApi2 {
    override suspend fun actionsListJobsForWorkflowRunAttempt(owner: String, repo: String, runId: Long, attemptNumber: Long, page: Long, perPage: Long): Repos.Actions.Runs.Attempts.JobsApi2.ActionsListJobsForWorkflowRunAttemptResult {
        val response = client.get("/repos/$owner/$repo/actions/runs/$runId/attempts/$attemptNumber/jobs") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Actions.Runs.Attempts.JobsApi2.ActionsListJobsForWorkflowRunAttemptResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Actions.Runs.Attempts.JobsApi2.ActionsListJobsForWorkflowRunAttemptResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposActionsRunsAttemptsLogsApi(private val client: HttpClient) : Repos.Actions.Runs.Attempts.LogsApi {
    override suspend fun actionsDownloadWorkflowRunAttemptLogs(owner: String, repo: String, runId: Long, attemptNumber: Long): Unit =
        client.get("/repos/$owner/$repo/actions/runs/$runId/attempts/$attemptNumber/logs").body()
}

internal class KtorReposActionsRunsCancel(private val client: HttpClient) : Repos.Actions.Runs.Cancel {
    override suspend fun actionsCancelWorkflowRun(owner: String, repo: String, runId: Long): Repos.Actions.Runs.Cancel.ActionsCancelWorkflowRunResult {
        val response = client.post("/repos/$owner/$repo/actions/runs/$runId/cancel")
        return when (response.status) {
            HttpStatusCode.Accepted -> Repos.Actions.Runs.Cancel.ActionsCancelWorkflowRunResult.Accepted(response.body())
            HttpStatusCode.Conflict -> Repos.Actions.Runs.Cancel.ActionsCancelWorkflowRunResult.Conflict(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposActionsRunsDeploymentProtectionRule(private val client: HttpClient) : Repos.Actions.Runs.DeploymentProtectionRule {
    override suspend fun actionsReviewCustomGatesForRun(owner: String, repo: String, runId: Long, body: Repos.Actions.Runs.DeploymentProtectionRule.ActionsReviewCustomGatesForRunBody): Unit =
        client.post("/repos/$owner/$repo/actions/runs/$runId/deployment_protection_rule") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()
}

internal class KtorReposActionsRunsForceCancel(private val client: HttpClient) : Repos.Actions.Runs.ForceCancel {
    override suspend fun actionsForceCancelWorkflowRun(owner: String, repo: String, runId: Long): Repos.Actions.Runs.ForceCancel.ActionsForceCancelWorkflowRunResult {
        val response = client.post("/repos/$owner/$repo/actions/runs/$runId/force-cancel")
        return when (response.status) {
            HttpStatusCode.Accepted -> Repos.Actions.Runs.ForceCancel.ActionsForceCancelWorkflowRunResult.Accepted(response.body())
            HttpStatusCode.Conflict -> Repos.Actions.Runs.ForceCancel.ActionsForceCancelWorkflowRunResult.Conflict(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposActionsRunsJobsApi(private val client: HttpClient) : Repos.Actions.Runs.JobsApi {
    override suspend fun actionsListJobsForWorkflowRun(owner: String, repo: String, runId: Long, filter: Repos.Actions.Runs.JobsApi.Filter, page: Long, perPage: Long): Repos.Actions.Runs.JobsApi.ActionsListJobsForWorkflowRunResponse =
        client.get("/repos/$owner/$repo/actions/runs/$runId/jobs") {
            parameter("filter", filter)
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()
}

internal class KtorReposActionsRunsLogs(private val client: HttpClient) : Repos.Actions.Runs.Logs {
    override suspend fun actionsDownloadWorkflowRunLogs(owner: String, repo: String, runId: Long): Unit =
        client.get("/repos/$owner/$repo/actions/runs/$runId/logs").body()

    override suspend fun actionsDeleteWorkflowRunLogs(owner: String, repo: String, runId: Long): Repos.Actions.Runs.Logs.ActionsDeleteWorkflowRunLogsResult {
        val response = client.delete("/repos/$owner/$repo/actions/runs/$runId/logs")
        return when (response.status) {
            HttpStatusCode.NoContent -> Repos.Actions.Runs.Logs.ActionsDeleteWorkflowRunLogsResult.NoContent
            HttpStatusCode.Forbidden -> Repos.Actions.Runs.Logs.ActionsDeleteWorkflowRunLogsResult.Forbidden(response.body())
            HttpStatusCode.InternalServerError -> Repos.Actions.Runs.Logs.ActionsDeleteWorkflowRunLogsResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposActionsRunsPendingDeployments(private val client: HttpClient) : Repos.Actions.Runs.PendingDeployments {
    override suspend fun actionsGetPendingDeploymentsForRun(owner: String, repo: String, runId: Long): List<PendingDeployment> =
        client.get("/repos/$owner/$repo/actions/runs/$runId/pending_deployments").body()

    override suspend fun actionsReviewPendingDeploymentsForRun(owner: String, repo: String, runId: Long, body: Repos.Actions.Runs.PendingDeployments.ActionsReviewPendingDeploymentsForRunBody): List<Deployment> =
        client.post("/repos/$owner/$repo/actions/runs/$runId/pending_deployments") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()
}

internal class KtorReposActionsRunsRerun(private val client: HttpClient) : Repos.Actions.Runs.Rerun {
    override suspend fun actionsReRunWorkflow(owner: String, repo: String, runId: Long, body: Repos.Actions.Runs.Rerun.ActionsReRunWorkflowBody?): EmptyObject =
        client.post("/repos/$owner/$repo/actions/runs/$runId/rerun") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }.body()
}

internal class KtorReposActionsRunsRerunFailedJobs(private val client: HttpClient) : Repos.Actions.Runs.RerunFailedJobs {
    override suspend fun actionsReRunWorkflowFailedJobs(owner: String, repo: String, runId: Long, body: Repos.Actions.Runs.RerunFailedJobs.ActionsReRunWorkflowFailedJobsBody?): EmptyObject =
        client.post("/repos/$owner/$repo/actions/runs/$runId/rerun-failed-jobs") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }.body()
}

internal class KtorReposActionsRunsTiming(private val client: HttpClient) : Repos.Actions.Runs.Timing {
    override suspend fun actionsGetWorkflowRunUsage(owner: String, repo: String, runId: Long): WorkflowRunUsage =
        client.get("/repos/$owner/$repo/actions/runs/$runId/timing").body()
}

internal class KtorReposActionsSecrets(private val client: HttpClient) : Repos.Actions.Secrets {
    override val publicKey: Repos.Actions.Secrets.PublicKey = KtorReposActionsSecretsPublicKey(client)

    override suspend fun actionsListRepoSecrets(owner: String, repo: String, page: Long, perPage: Long): Repos.Actions.Secrets.ActionsListRepoSecretsResponse =
        client.get("/repos/$owner/$repo/actions/secrets") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()

    override suspend fun actionsGetRepoSecret(owner: String, repo: String, secretName: String): ActionsSecret =
        client.get("/repos/$owner/$repo/actions/secrets/$secretName").body()

    override suspend fun actionsCreateOrUpdateRepoSecret(owner: String, repo: String, secretName: String, body: Repos.Actions.Secrets.ActionsCreateOrUpdateRepoSecretBody): Repos.Actions.Secrets.ActionsCreateOrUpdateRepoSecretResult {
        val response = client.put("/repos/$owner/$repo/actions/secrets/$secretName") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> Repos.Actions.Secrets.ActionsCreateOrUpdateRepoSecretResult.Created(response.body())
            HttpStatusCode.NoContent -> Repos.Actions.Secrets.ActionsCreateOrUpdateRepoSecretResult.NoContent
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun actionsDeleteRepoSecret(owner: String, repo: String, secretName: String): Unit =
        client.delete("/repos/$owner/$repo/actions/secrets/$secretName").body()
}

internal class KtorReposActionsSecretsPublicKey(private val client: HttpClient) : Repos.Actions.Secrets.PublicKey {
    override suspend fun actionsGetRepoPublicKey(owner: String, repo: String): ActionsPublicKey =
        client.get("/repos/$owner/$repo/actions/secrets/public-key").body()
}

internal class KtorReposActionsVariables(private val client: HttpClient) : Repos.Actions.Variables {
    override suspend fun actionsListRepoVariables(owner: String, repo: String, page: Long, perPage: Long): Repos.Actions.Variables.ActionsListRepoVariablesResponse =
        client.get("/repos/$owner/$repo/actions/variables") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()

    override suspend fun actionsCreateRepoVariable(owner: String, repo: String, body: Repos.Actions.Variables.ActionsCreateRepoVariableBody): EmptyObject =
        client.post("/repos/$owner/$repo/actions/variables") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()

    override suspend fun actionsGetRepoVariable(owner: String, repo: String, name: String): ActionsVariable =
        client.get("/repos/$owner/$repo/actions/variables/$name").body()

    override suspend fun actionsDeleteRepoVariable(owner: String, repo: String, name: String): Unit =
        client.delete("/repos/$owner/$repo/actions/variables/$name").body()

    override suspend fun actionsUpdateRepoVariable(owner: String, repo: String, name: String, body: Repos.Actions.Variables.ActionsUpdateRepoVariableBody): Unit =
        client.patch("/repos/$owner/$repo/actions/variables/$name") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()
}

internal class KtorReposActionsWorkflows(private val client: HttpClient) : Repos.Actions.Workflows {
    override val disable: Repos.Actions.Workflows.Disable = KtorReposActionsWorkflowsDisable(client)

    override val dispatches: Repos.Actions.Workflows.DispatchesApi = KtorReposActionsWorkflowsDispatchesApi(client)

    override val enable: Repos.Actions.Workflows.Enable = KtorReposActionsWorkflowsEnable(client)

    override val runs: Repos.Actions.Workflows.RunsApi = KtorReposActionsWorkflowsRunsApi(client)

    override val timing: Repos.Actions.Workflows.Timing = KtorReposActionsWorkflowsTiming(client)

    override suspend fun actionsListRepoWorkflows(owner: String, repo: String, page: Long, perPage: Long): Repos.Actions.Workflows.ActionsListRepoWorkflowsResponse =
        client.get("/repos/$owner/$repo/actions/workflows") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()

    override suspend fun actionsGetWorkflow(owner: String, repo: String, workflowId: Repos.Actions.Workflows.WorkflowId): Workflow =
        client.get("/repos/$owner/$repo/actions/workflows/$workflowId").body()
}

internal class KtorReposActionsWorkflowsDisable(private val client: HttpClient) : Repos.Actions.Workflows.Disable {
    override suspend fun actionsDisableWorkflow(owner: String, repo: String, workflowId: Repos.Actions.Workflows.Disable.ActionsDisableWorkflowWorkflowId): Unit =
        client.put("/repos/$owner/$repo/actions/workflows/$workflowId/disable").body()
}

internal class KtorReposActionsWorkflowsDispatchesApi(private val client: HttpClient) : Repos.Actions.Workflows.DispatchesApi {
    override suspend fun actionsCreateWorkflowDispatch(owner: String, repo: String, workflowId: Repos.Actions.Workflows.DispatchesApi.ActionsCreateWorkflowDispatchWorkflowId, body: Repos.Actions.Workflows.DispatchesApi.ActionsCreateWorkflowDispatchBody): Repos.Actions.Workflows.DispatchesApi.ActionsCreateWorkflowDispatchResult {
        val response = client.post("/repos/$owner/$repo/actions/workflows/$workflowId/dispatches") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Actions.Workflows.DispatchesApi.ActionsCreateWorkflowDispatchResult.OK(response.body())
            HttpStatusCode.NoContent -> Repos.Actions.Workflows.DispatchesApi.ActionsCreateWorkflowDispatchResult.NoContent
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposActionsWorkflowsEnable(private val client: HttpClient) : Repos.Actions.Workflows.Enable {
    override suspend fun actionsEnableWorkflow(owner: String, repo: String, workflowId: Repos.Actions.Workflows.Enable.ActionsEnableWorkflowWorkflowId): Unit =
        client.put("/repos/$owner/$repo/actions/workflows/$workflowId/enable").body()
}

internal class KtorReposActionsWorkflowsRunsApi(private val client: HttpClient) : Repos.Actions.Workflows.RunsApi {
    override suspend fun actionsListWorkflowRuns(owner: String, repo: String, workflowId: Repos.Actions.Workflows.RunsApi.ActionsListWorkflowRunsWorkflowId, excludePullRequests: Boolean, page: Long, perPage: Long, actor: String?, branch: String?, checkSuiteId: Long?, created: LocalDateTime?, event: String?, headSha: String?, status: Repos.Actions.Workflows.RunsApi.Status?): Repos.Actions.Workflows.RunsApi.ActionsListWorkflowRunsResponse =
        client.get("/repos/$owner/$repo/actions/workflows/$workflowId/runs") {
            parameter("exclude_pull_requests", excludePullRequests)
            parameter("page", page)
            parameter("per_page", perPage)
            actor?.let { parameter("actor", it) }
            branch?.let { parameter("branch", it) }
            checkSuiteId?.let { parameter("check_suite_id", it) }
            created?.let { parameter("created", it) }
            event?.let { parameter("event", it) }
            headSha?.let { parameter("head_sha", it) }
            status?.let { parameter("status", it) }
        }.body()
}

internal class KtorReposActionsWorkflowsTiming(private val client: HttpClient) : Repos.Actions.Workflows.Timing {
    override suspend fun actionsGetWorkflowUsage(owner: String, repo: String, workflowId: Repos.Actions.Workflows.Timing.ActionsGetWorkflowUsageWorkflowId): WorkflowUsage =
        client.get("/repos/$owner/$repo/actions/workflows/$workflowId/timing").body()
}

internal class KtorReposActivity(private val client: HttpClient) : Repos.Activity {
    override suspend fun reposListActivities(owner: String, repo: String, direction: Repos.Activity.Direction, perPage: Long, activityType: Repos.Activity.ActivityType?, actor: String?, after: String?, before: String?, ref: String?, timePeriod: Repos.Activity.TimePeriod?): Repos.Activity.ReposListActivitiesResult {
        val response = client.get("/repos/$owner/$repo/activity") {
            parameter("direction", direction)
            parameter("per_page", perPage)
            activityType?.let { parameter("activity_type", it) }
            actor?.let { parameter("actor", it) }
            after?.let { parameter("after", it) }
            before?.let { parameter("before", it) }
            ref?.let { parameter("ref", it) }
            timePeriod?.let { parameter("time_period", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Activity.ReposListActivitiesResult.OK(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Activity.ReposListActivitiesResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposAssignees(private val client: HttpClient) : Repos.Assignees {
    override suspend fun issuesListAssignees(owner: String, repo: String, page: Long, perPage: Long): Repos.Assignees.IssuesListAssigneesResult {
        val response = client.get("/repos/$owner/$repo/assignees") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Assignees.IssuesListAssigneesResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Assignees.IssuesListAssigneesResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun issuesCheckUserCanBeAssigned(owner: String, repo: String, assignee: String): Repos.Assignees.IssuesCheckUserCanBeAssignedResult {
        val response = client.get("/repos/$owner/$repo/assignees/$assignee")
        return when (response.status) {
            HttpStatusCode.NoContent -> Repos.Assignees.IssuesCheckUserCanBeAssignedResult.NoContent
            HttpStatusCode.NotFound -> Repos.Assignees.IssuesCheckUserCanBeAssignedResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposAttestations(private val client: HttpClient) : Repos.Attestations {
    override suspend fun reposCreateAttestation(owner: String, repo: String, body: Repos.Attestations.ReposCreateAttestationBody): Repos.Attestations.ReposCreateAttestationResult {
        val response = client.post("/repos/$owner/$repo/attestations") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> Repos.Attestations.ReposCreateAttestationResult.Created(response.body())
            HttpStatusCode.Forbidden -> Repos.Attestations.ReposCreateAttestationResult.Forbidden(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Attestations.ReposCreateAttestationResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposListAttestations(owner: String, repo: String, subjectDigest: String, perPage: Long, after: String?, before: String?, predicateType: String?): Repos.Attestations.ReposListAttestationsResponse =
        client.get("/repos/$owner/$repo/attestations/$subjectDigest") {
            parameter("per_page", perPage)
            after?.let { parameter("after", it) }
            before?.let { parameter("before", it) }
            predicateType?.let { parameter("predicate_type", it) }
        }.body()
}

internal class KtorReposAutolinks(private val client: HttpClient) : Repos.Autolinks {
    override suspend fun reposListAutolinks(owner: String, repo: String): List<Autolink> =
        client.get("/repos/$owner/$repo/autolinks").body()

    override suspend fun reposCreateAutolink(owner: String, repo: String, body: Repos.Autolinks.ReposCreateAutolinkBody): Repos.Autolinks.ReposCreateAutolinkResult {
        val response = client.post("/repos/$owner/$repo/autolinks") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> Repos.Autolinks.ReposCreateAutolinkResult.Created(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Autolinks.ReposCreateAutolinkResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposGetAutolink(owner: String, repo: String, autolinkId: Long): Repos.Autolinks.ReposGetAutolinkResult {
        val response = client.get("/repos/$owner/$repo/autolinks/$autolinkId")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Autolinks.ReposGetAutolinkResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Autolinks.ReposGetAutolinkResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposDeleteAutolink(owner: String, repo: String, autolinkId: Long): Repos.Autolinks.ReposDeleteAutolinkResult {
        val response = client.delete("/repos/$owner/$repo/autolinks/$autolinkId")
        return when (response.status) {
            HttpStatusCode.NoContent -> Repos.Autolinks.ReposDeleteAutolinkResult.NoContent
            HttpStatusCode.NotFound -> Repos.Autolinks.ReposDeleteAutolinkResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposAutomatedSecurityFixes(private val client: HttpClient) : Repos.AutomatedSecurityFixes {
    override suspend fun reposCheckAutomatedSecurityFixes(owner: String, repo: String): Repos.AutomatedSecurityFixes.ReposCheckAutomatedSecurityFixesResult {
        val response = client.get("/repos/$owner/$repo/automated-security-fixes")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.AutomatedSecurityFixes.ReposCheckAutomatedSecurityFixesResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.AutomatedSecurityFixes.ReposCheckAutomatedSecurityFixesResult.NotFound
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposEnableAutomatedSecurityFixes(owner: String, repo: String): Unit =
        client.put("/repos/$owner/$repo/automated-security-fixes").body()

    override suspend fun reposDisableAutomatedSecurityFixes(owner: String, repo: String): Unit =
        client.delete("/repos/$owner/$repo/automated-security-fixes").body()
}

internal class KtorReposBranches(private val client: HttpClient) : Repos.Branches {
    override val protection: Repos.Branches.Protection = KtorReposBranchesProtection(client)

    override val rename: Repos.Branches.Rename = KtorReposBranchesRename(client)

    override suspend fun reposListBranches(owner: String, repo: String, page: Long, perPage: Long, protected: Boolean?): Repos.Branches.ReposListBranchesResult {
        val response = client.get("/repos/$owner/$repo/branches") {
            parameter("page", page)
            parameter("per_page", perPage)
            protected?.let { parameter("protected", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Branches.ReposListBranchesResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Branches.ReposListBranchesResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposGetBranch(owner: String, repo: String, branch: String): Repos.Branches.ReposGetBranchResult {
        val response = client.get("/repos/$owner/$repo/branches/$branch")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Branches.ReposGetBranchResult.OK(response.body())
            HttpStatusCode.MovedPermanently -> Repos.Branches.ReposGetBranchResult.MovedPermanently(response.body())
            HttpStatusCode.NotFound -> Repos.Branches.ReposGetBranchResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposBranchesProtection(private val client: HttpClient) : Repos.Branches.Protection {
    override val enforceAdmins: Repos.Branches.Protection.EnforceAdmins = KtorReposBranchesProtectionEnforceAdmins(client)

    override val requiredPullRequestReviews: Repos.Branches.Protection.RequiredPullRequestReviews = KtorReposBranchesProtectionRequiredPullRequestReviews(client)

    override val requiredSignatures: Repos.Branches.Protection.RequiredSignatures = KtorReposBranchesProtectionRequiredSignatures(client)

    override val requiredStatusChecks: Repos.Branches.Protection.RequiredStatusChecks = KtorReposBranchesProtectionRequiredStatusChecks(client)

    override val restrictions: Repos.Branches.Protection.Restrictions = KtorReposBranchesProtectionRestrictions(client)

    override suspend fun reposGetBranchProtection(owner: String, repo: String, branch: String): Repos.Branches.Protection.ReposGetBranchProtectionResult {
        val response = client.get("/repos/$owner/$repo/branches/$branch/protection")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Branches.Protection.ReposGetBranchProtectionResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Branches.Protection.ReposGetBranchProtectionResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposUpdateBranchProtection(owner: String, repo: String, branch: String, body: Repos.Branches.Protection.ReposUpdateBranchProtectionBody): Repos.Branches.Protection.ReposUpdateBranchProtectionResult {
        val response = client.put("/repos/$owner/$repo/branches/$branch/protection") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Branches.Protection.ReposUpdateBranchProtectionResult.OK(response.body())
            HttpStatusCode.Forbidden -> Repos.Branches.Protection.ReposUpdateBranchProtectionResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.Branches.Protection.ReposUpdateBranchProtectionResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Branches.Protection.ReposUpdateBranchProtectionResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposDeleteBranchProtection(owner: String, repo: String, branch: String): Repos.Branches.Protection.ReposDeleteBranchProtectionResult {
        val response = client.delete("/repos/$owner/$repo/branches/$branch/protection")
        return when (response.status) {
            HttpStatusCode.NoContent -> Repos.Branches.Protection.ReposDeleteBranchProtectionResult.NoContent
            HttpStatusCode.Forbidden -> Repos.Branches.Protection.ReposDeleteBranchProtectionResult.Forbidden(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposBranchesProtectionEnforceAdmins(private val client: HttpClient) : Repos.Branches.Protection.EnforceAdmins {
    override suspend fun reposGetAdminBranchProtection(owner: String, repo: String, branch: String): ProtectedBranchAdminEnforced =
        client.get("/repos/$owner/$repo/branches/$branch/protection/enforce_admins").body()

    override suspend fun reposSetAdminBranchProtection(owner: String, repo: String, branch: String): ProtectedBranchAdminEnforced =
        client.post("/repos/$owner/$repo/branches/$branch/protection/enforce_admins").body()

    override suspend fun reposDeleteAdminBranchProtection(owner: String, repo: String, branch: String): Repos.Branches.Protection.EnforceAdmins.ReposDeleteAdminBranchProtectionResult {
        val response = client.delete("/repos/$owner/$repo/branches/$branch/protection/enforce_admins")
        return when (response.status) {
            HttpStatusCode.NoContent -> Repos.Branches.Protection.EnforceAdmins.ReposDeleteAdminBranchProtectionResult.NoContent
            HttpStatusCode.NotFound -> Repos.Branches.Protection.EnforceAdmins.ReposDeleteAdminBranchProtectionResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposBranchesProtectionRequiredPullRequestReviews(private val client: HttpClient) : Repos.Branches.Protection.RequiredPullRequestReviews {
    override suspend fun reposGetPullRequestReviewProtection(owner: String, repo: String, branch: String): ProtectedBranchPullRequestReview =
        client.get("/repos/$owner/$repo/branches/$branch/protection/required_pull_request_reviews").body()

    override suspend fun reposDeletePullRequestReviewProtection(owner: String, repo: String, branch: String): Repos.Branches.Protection.RequiredPullRequestReviews.ReposDeletePullRequestReviewProtectionResult {
        val response = client.delete("/repos/$owner/$repo/branches/$branch/protection/required_pull_request_reviews")
        return when (response.status) {
            HttpStatusCode.NoContent -> Repos.Branches.Protection.RequiredPullRequestReviews.ReposDeletePullRequestReviewProtectionResult.NoContent
            HttpStatusCode.NotFound -> Repos.Branches.Protection.RequiredPullRequestReviews.ReposDeletePullRequestReviewProtectionResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposUpdatePullRequestReviewProtection(owner: String, repo: String, branch: String, body: Repos.Branches.Protection.RequiredPullRequestReviews.ReposUpdatePullRequestReviewProtectionBody?): Repos.Branches.Protection.RequiredPullRequestReviews.ReposUpdatePullRequestReviewProtectionResult {
        val response = client.patch("/repos/$owner/$repo/branches/$branch/protection/required_pull_request_reviews") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Branches.Protection.RequiredPullRequestReviews.ReposUpdatePullRequestReviewProtectionResult.OK(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Branches.Protection.RequiredPullRequestReviews.ReposUpdatePullRequestReviewProtectionResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposBranchesProtectionRequiredSignatures(private val client: HttpClient) : Repos.Branches.Protection.RequiredSignatures {
    override suspend fun reposGetCommitSignatureProtection(owner: String, repo: String, branch: String): Repos.Branches.Protection.RequiredSignatures.ReposGetCommitSignatureProtectionResult {
        val response = client.get("/repos/$owner/$repo/branches/$branch/protection/required_signatures")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Branches.Protection.RequiredSignatures.ReposGetCommitSignatureProtectionResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Branches.Protection.RequiredSignatures.ReposGetCommitSignatureProtectionResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposCreateCommitSignatureProtection(owner: String, repo: String, branch: String): Repos.Branches.Protection.RequiredSignatures.ReposCreateCommitSignatureProtectionResult {
        val response = client.post("/repos/$owner/$repo/branches/$branch/protection/required_signatures")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Branches.Protection.RequiredSignatures.ReposCreateCommitSignatureProtectionResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Branches.Protection.RequiredSignatures.ReposCreateCommitSignatureProtectionResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposDeleteCommitSignatureProtection(owner: String, repo: String, branch: String): Repos.Branches.Protection.RequiredSignatures.ReposDeleteCommitSignatureProtectionResult {
        val response = client.delete("/repos/$owner/$repo/branches/$branch/protection/required_signatures")
        return when (response.status) {
            HttpStatusCode.NoContent -> Repos.Branches.Protection.RequiredSignatures.ReposDeleteCommitSignatureProtectionResult.NoContent
            HttpStatusCode.NotFound -> Repos.Branches.Protection.RequiredSignatures.ReposDeleteCommitSignatureProtectionResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposBranchesProtectionRequiredStatusChecks(private val client: HttpClient) : Repos.Branches.Protection.RequiredStatusChecks {
    override val contexts: Repos.Branches.Protection.RequiredStatusChecks.Contexts = KtorReposBranchesProtectionRequiredStatusChecksContexts(client)

    override suspend fun reposGetStatusChecksProtection(owner: String, repo: String, branch: String): Repos.Branches.Protection.RequiredStatusChecks.ReposGetStatusChecksProtectionResult {
        val response = client.get("/repos/$owner/$repo/branches/$branch/protection/required_status_checks")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Branches.Protection.RequiredStatusChecks.ReposGetStatusChecksProtectionResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Branches.Protection.RequiredStatusChecks.ReposGetStatusChecksProtectionResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposRemoveStatusCheckProtection(owner: String, repo: String, branch: String): Unit =
        client.delete("/repos/$owner/$repo/branches/$branch/protection/required_status_checks").body()

    override suspend fun reposUpdateStatusCheckProtection(owner: String, repo: String, branch: String, body: Repos.Branches.Protection.RequiredStatusChecks.ReposUpdateStatusCheckProtectionBody?): Repos.Branches.Protection.RequiredStatusChecks.ReposUpdateStatusCheckProtectionResult {
        val response = client.patch("/repos/$owner/$repo/branches/$branch/protection/required_status_checks") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Branches.Protection.RequiredStatusChecks.ReposUpdateStatusCheckProtectionResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Branches.Protection.RequiredStatusChecks.ReposUpdateStatusCheckProtectionResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Branches.Protection.RequiredStatusChecks.ReposUpdateStatusCheckProtectionResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposBranchesProtectionRequiredStatusChecksContexts(private val client: HttpClient) : Repos.Branches.Protection.RequiredStatusChecks.Contexts {
    override suspend fun reposGetAllStatusCheckContexts(owner: String, repo: String, branch: String): Repos.Branches.Protection.RequiredStatusChecks.Contexts.ReposGetAllStatusCheckContextsResult {
        val response = client.get("/repos/$owner/$repo/branches/$branch/protection/required_status_checks/contexts")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Branches.Protection.RequiredStatusChecks.Contexts.ReposGetAllStatusCheckContextsResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Branches.Protection.RequiredStatusChecks.Contexts.ReposGetAllStatusCheckContextsResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposSetStatusCheckContexts(owner: String, repo: String, branch: String, body: Repos.Branches.Protection.RequiredStatusChecks.Contexts.ReposSetStatusCheckContextsBody?): Repos.Branches.Protection.RequiredStatusChecks.Contexts.ReposSetStatusCheckContextsResult {
        val response = client.put("/repos/$owner/$repo/branches/$branch/protection/required_status_checks/contexts") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Branches.Protection.RequiredStatusChecks.Contexts.ReposSetStatusCheckContextsResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Branches.Protection.RequiredStatusChecks.Contexts.ReposSetStatusCheckContextsResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Branches.Protection.RequiredStatusChecks.Contexts.ReposSetStatusCheckContextsResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposAddStatusCheckContexts(owner: String, repo: String, branch: String, body: Repos.Branches.Protection.RequiredStatusChecks.Contexts.ReposAddStatusCheckContextsBody?): Repos.Branches.Protection.RequiredStatusChecks.Contexts.ReposAddStatusCheckContextsResult {
        val response = client.post("/repos/$owner/$repo/branches/$branch/protection/required_status_checks/contexts") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Branches.Protection.RequiredStatusChecks.Contexts.ReposAddStatusCheckContextsResult.OK(response.body())
            HttpStatusCode.Forbidden -> Repos.Branches.Protection.RequiredStatusChecks.Contexts.ReposAddStatusCheckContextsResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.Branches.Protection.RequiredStatusChecks.Contexts.ReposAddStatusCheckContextsResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Branches.Protection.RequiredStatusChecks.Contexts.ReposAddStatusCheckContextsResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposRemoveStatusCheckContexts(owner: String, repo: String, branch: String, body: Repos.Branches.Protection.RequiredStatusChecks.Contexts.ReposRemoveStatusCheckContextsBody?): Repos.Branches.Protection.RequiredStatusChecks.Contexts.ReposRemoveStatusCheckContextsResult {
        val response = client.delete("/repos/$owner/$repo/branches/$branch/protection/required_status_checks/contexts") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Branches.Protection.RequiredStatusChecks.Contexts.ReposRemoveStatusCheckContextsResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Branches.Protection.RequiredStatusChecks.Contexts.ReposRemoveStatusCheckContextsResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Branches.Protection.RequiredStatusChecks.Contexts.ReposRemoveStatusCheckContextsResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposBranchesProtectionRestrictions(private val client: HttpClient) : Repos.Branches.Protection.Restrictions {
    override val apps: Repos.Branches.Protection.Restrictions.Apps = KtorReposBranchesProtectionRestrictionsApps(client)

    override val teams: Repos.Branches.Protection.Restrictions.TeamsApi = KtorReposBranchesProtectionRestrictionsTeamsApi(client)

    override val users: Repos.Branches.Protection.Restrictions.Users = KtorReposBranchesProtectionRestrictionsUsers(client)

    override suspend fun reposGetAccessRestrictions(owner: String, repo: String, branch: String): Repos.Branches.Protection.Restrictions.ReposGetAccessRestrictionsResult {
        val response = client.get("/repos/$owner/$repo/branches/$branch/protection/restrictions")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Branches.Protection.Restrictions.ReposGetAccessRestrictionsResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Branches.Protection.Restrictions.ReposGetAccessRestrictionsResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposDeleteAccessRestrictions(owner: String, repo: String, branch: String): Unit =
        client.delete("/repos/$owner/$repo/branches/$branch/protection/restrictions").body()
}

internal class KtorReposBranchesProtectionRestrictionsApps(private val client: HttpClient) : Repos.Branches.Protection.Restrictions.Apps {
    override suspend fun reposGetAppsWithAccessToProtectedBranch(owner: String, repo: String, branch: String): Repos.Branches.Protection.Restrictions.Apps.ReposGetAppsWithAccessToProtectedBranchResult {
        val response = client.get("/repos/$owner/$repo/branches/$branch/protection/restrictions/apps")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Branches.Protection.Restrictions.Apps.ReposGetAppsWithAccessToProtectedBranchResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Branches.Protection.Restrictions.Apps.ReposGetAppsWithAccessToProtectedBranchResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposSetAppAccessRestrictions(owner: String, repo: String, branch: String, body: Repos.Branches.Protection.Restrictions.Apps.ReposSetAppAccessRestrictionsBody): Repos.Branches.Protection.Restrictions.Apps.ReposSetAppAccessRestrictionsResult {
        val response = client.put("/repos/$owner/$repo/branches/$branch/protection/restrictions/apps") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Branches.Protection.Restrictions.Apps.ReposSetAppAccessRestrictionsResult.OK(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Branches.Protection.Restrictions.Apps.ReposSetAppAccessRestrictionsResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposAddAppAccessRestrictions(owner: String, repo: String, branch: String, body: Repos.Branches.Protection.Restrictions.Apps.ReposAddAppAccessRestrictionsBody): Repos.Branches.Protection.Restrictions.Apps.ReposAddAppAccessRestrictionsResult {
        val response = client.post("/repos/$owner/$repo/branches/$branch/protection/restrictions/apps") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Branches.Protection.Restrictions.Apps.ReposAddAppAccessRestrictionsResult.OK(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Branches.Protection.Restrictions.Apps.ReposAddAppAccessRestrictionsResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposRemoveAppAccessRestrictions(owner: String, repo: String, branch: String, body: Repos.Branches.Protection.Restrictions.Apps.ReposRemoveAppAccessRestrictionsBody): Repos.Branches.Protection.Restrictions.Apps.ReposRemoveAppAccessRestrictionsResult {
        val response = client.delete("/repos/$owner/$repo/branches/$branch/protection/restrictions/apps") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Branches.Protection.Restrictions.Apps.ReposRemoveAppAccessRestrictionsResult.OK(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Branches.Protection.Restrictions.Apps.ReposRemoveAppAccessRestrictionsResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposBranchesProtectionRestrictionsTeamsApi(private val client: HttpClient) : Repos.Branches.Protection.Restrictions.TeamsApi {
    override suspend fun reposGetTeamsWithAccessToProtectedBranch(owner: String, repo: String, branch: String): Repos.Branches.Protection.Restrictions.TeamsApi.ReposGetTeamsWithAccessToProtectedBranchResult {
        val response = client.get("/repos/$owner/$repo/branches/$branch/protection/restrictions/teams")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Branches.Protection.Restrictions.TeamsApi.ReposGetTeamsWithAccessToProtectedBranchResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Branches.Protection.Restrictions.TeamsApi.ReposGetTeamsWithAccessToProtectedBranchResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposSetTeamAccessRestrictions(owner: String, repo: String, branch: String, body: Repos.Branches.Protection.Restrictions.TeamsApi.ReposSetTeamAccessRestrictionsBody?): Repos.Branches.Protection.Restrictions.TeamsApi.ReposSetTeamAccessRestrictionsResult {
        val response = client.put("/repos/$owner/$repo/branches/$branch/protection/restrictions/teams") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Branches.Protection.Restrictions.TeamsApi.ReposSetTeamAccessRestrictionsResult.OK(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Branches.Protection.Restrictions.TeamsApi.ReposSetTeamAccessRestrictionsResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposAddTeamAccessRestrictions(owner: String, repo: String, branch: String, body: Repos.Branches.Protection.Restrictions.TeamsApi.ReposAddTeamAccessRestrictionsBody?): Repos.Branches.Protection.Restrictions.TeamsApi.ReposAddTeamAccessRestrictionsResult {
        val response = client.post("/repos/$owner/$repo/branches/$branch/protection/restrictions/teams") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Branches.Protection.Restrictions.TeamsApi.ReposAddTeamAccessRestrictionsResult.OK(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Branches.Protection.Restrictions.TeamsApi.ReposAddTeamAccessRestrictionsResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposRemoveTeamAccessRestrictions(owner: String, repo: String, branch: String, body: Repos.Branches.Protection.Restrictions.TeamsApi.ReposRemoveTeamAccessRestrictionsBody?): Repos.Branches.Protection.Restrictions.TeamsApi.ReposRemoveTeamAccessRestrictionsResult {
        val response = client.delete("/repos/$owner/$repo/branches/$branch/protection/restrictions/teams") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Branches.Protection.Restrictions.TeamsApi.ReposRemoveTeamAccessRestrictionsResult.OK(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Branches.Protection.Restrictions.TeamsApi.ReposRemoveTeamAccessRestrictionsResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposBranchesProtectionRestrictionsUsers(private val client: HttpClient) : Repos.Branches.Protection.Restrictions.Users {
    override suspend fun reposGetUsersWithAccessToProtectedBranch(owner: String, repo: String, branch: String): Repos.Branches.Protection.Restrictions.Users.ReposGetUsersWithAccessToProtectedBranchResult {
        val response = client.get("/repos/$owner/$repo/branches/$branch/protection/restrictions/users")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Branches.Protection.Restrictions.Users.ReposGetUsersWithAccessToProtectedBranchResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Branches.Protection.Restrictions.Users.ReposGetUsersWithAccessToProtectedBranchResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposSetUserAccessRestrictions(owner: String, repo: String, branch: String, body: Repos.Branches.Protection.Restrictions.Users.ReposSetUserAccessRestrictionsBody): Repos.Branches.Protection.Restrictions.Users.ReposSetUserAccessRestrictionsResult {
        val response = client.put("/repos/$owner/$repo/branches/$branch/protection/restrictions/users") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Branches.Protection.Restrictions.Users.ReposSetUserAccessRestrictionsResult.OK(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Branches.Protection.Restrictions.Users.ReposSetUserAccessRestrictionsResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposAddUserAccessRestrictions(owner: String, repo: String, branch: String, body: Repos.Branches.Protection.Restrictions.Users.ReposAddUserAccessRestrictionsBody): Repos.Branches.Protection.Restrictions.Users.ReposAddUserAccessRestrictionsResult {
        val response = client.post("/repos/$owner/$repo/branches/$branch/protection/restrictions/users") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Branches.Protection.Restrictions.Users.ReposAddUserAccessRestrictionsResult.OK(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Branches.Protection.Restrictions.Users.ReposAddUserAccessRestrictionsResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposRemoveUserAccessRestrictions(owner: String, repo: String, branch: String, body: Repos.Branches.Protection.Restrictions.Users.ReposRemoveUserAccessRestrictionsBody): Repos.Branches.Protection.Restrictions.Users.ReposRemoveUserAccessRestrictionsResult {
        val response = client.delete("/repos/$owner/$repo/branches/$branch/protection/restrictions/users") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Branches.Protection.Restrictions.Users.ReposRemoveUserAccessRestrictionsResult.OK(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Branches.Protection.Restrictions.Users.ReposRemoveUserAccessRestrictionsResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposBranchesRename(private val client: HttpClient) : Repos.Branches.Rename {
    override suspend fun reposRenameBranch(owner: String, repo: String, branch: String, body: Repos.Branches.Rename.ReposRenameBranchBody): Repos.Branches.Rename.ReposRenameBranchResult {
        val response = client.post("/repos/$owner/$repo/branches/$branch/rename") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> Repos.Branches.Rename.ReposRenameBranchResult.Created(response.body())
            HttpStatusCode.Forbidden -> Repos.Branches.Rename.ReposRenameBranchResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.Branches.Rename.ReposRenameBranchResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Branches.Rename.ReposRenameBranchResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposCheckRuns(private val client: HttpClient) : Repos.CheckRuns {
    override val annotations: Repos.CheckRuns.Annotations = KtorReposCheckRunsAnnotations(client)

    override val rerequest: Repos.CheckRuns.Rerequest = KtorReposCheckRunsRerequest(client)

    override suspend fun checksCreate(owner: String, repo: String, body: Repos.CheckRuns.ChecksCreateBody): CheckRun =
        client.post("/repos/$owner/$repo/check-runs") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()

    override suspend fun checksGet(owner: String, repo: String, checkRunId: Long): CheckRun =
        client.get("/repos/$owner/$repo/check-runs/$checkRunId").body()

    override suspend fun checksUpdate(owner: String, repo: String, checkRunId: Long, body: Repos.CheckRuns.ChecksUpdateBody): CheckRun =
        client.patch("/repos/$owner/$repo/check-runs/$checkRunId") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()
}

internal class KtorReposCheckRunsAnnotations(private val client: HttpClient) : Repos.CheckRuns.Annotations {
    override suspend fun checksListAnnotations(owner: String, repo: String, checkRunId: Long, page: Long, perPage: Long): List<CheckAnnotation> =
        client.get("/repos/$owner/$repo/check-runs/$checkRunId/annotations") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()
}

internal class KtorReposCheckRunsRerequest(private val client: HttpClient) : Repos.CheckRuns.Rerequest {
    override suspend fun checksRerequestRun(owner: String, repo: String, checkRunId: Long): Repos.CheckRuns.Rerequest.ChecksRerequestRunResult {
        val response = client.post("/repos/$owner/$repo/check-runs/$checkRunId/rerequest")
        return when (response.status) {
            HttpStatusCode.Created -> Repos.CheckRuns.Rerequest.ChecksRerequestRunResult.Created(response.body())
            HttpStatusCode.Forbidden -> Repos.CheckRuns.Rerequest.ChecksRerequestRunResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.CheckRuns.Rerequest.ChecksRerequestRunResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.CheckRuns.Rerequest.ChecksRerequestRunResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposCheckSuites(private val client: HttpClient) : Repos.CheckSuites {
    override val preferences: Repos.CheckSuites.Preferences = KtorReposCheckSuitesPreferences(client)

    override val checkRuns: Repos.CheckSuites.CheckRunsApi = KtorReposCheckSuitesCheckRunsApi(client)

    override val rerequest: Repos.CheckSuites.Rerequest = KtorReposCheckSuitesRerequest(client)

    override suspend fun checksCreateSuite(owner: String, repo: String, body: Repos.CheckSuites.ChecksCreateSuiteBody): Repos.CheckSuites.ChecksCreateSuiteResult {
        val response = client.post("/repos/$owner/$repo/check-suites") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.CheckSuites.ChecksCreateSuiteResult.OK(response.body())
            HttpStatusCode.Created -> Repos.CheckSuites.ChecksCreateSuiteResult.Created(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun checksGetSuite(owner: String, repo: String, checkSuiteId: Long): CheckSuite =
        client.get("/repos/$owner/$repo/check-suites/$checkSuiteId").body()
}

internal class KtorReposCheckSuitesPreferences(private val client: HttpClient) : Repos.CheckSuites.Preferences {
    override suspend fun checksSetSuitesPreferences(owner: String, repo: String, body: Repos.CheckSuites.Preferences.ChecksSetSuitesPreferencesBody): CheckSuitePreference =
        client.patch("/repos/$owner/$repo/check-suites/preferences") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()
}

internal class KtorReposCheckSuitesCheckRunsApi(private val client: HttpClient) : Repos.CheckSuites.CheckRunsApi {
    override suspend fun checksListForSuite(owner: String, repo: String, checkSuiteId: Long, filter: Repos.CheckSuites.CheckRunsApi.Filter, page: Long, perPage: Long, checkName: String?, status: Repos.CheckSuites.CheckRunsApi.Status?): Repos.CheckSuites.CheckRunsApi.ChecksListForSuiteResponse =
        client.get("/repos/$owner/$repo/check-suites/$checkSuiteId/check-runs") {
            parameter("filter", filter)
            parameter("page", page)
            parameter("per_page", perPage)
            checkName?.let { parameter("check_name", it) }
            status?.let { parameter("status", it) }
        }.body()
}

internal class KtorReposCheckSuitesRerequest(private val client: HttpClient) : Repos.CheckSuites.Rerequest {
    override suspend fun checksRerequestSuite(owner: String, repo: String, checkSuiteId: Long): EmptyObject =
        client.post("/repos/$owner/$repo/check-suites/$checkSuiteId/rerequest").body()
}

internal class KtorReposCodeScanning(private val client: HttpClient) : Repos.CodeScanning {
    override val alerts: Repos.CodeScanning.Alerts = KtorReposCodeScanningAlerts(client)

    override val analyses: Repos.CodeScanning.Analyses = KtorReposCodeScanningAnalyses(client)

    override val codeql: Repos.CodeScanning.Codeql = KtorReposCodeScanningCodeql(client)

    override val defaultSetup: Repos.CodeScanning.DefaultSetup = KtorReposCodeScanningDefaultSetup(client)

    override val sarifs: Repos.CodeScanning.Sarifs = KtorReposCodeScanningSarifs(client)
}

internal class KtorReposCodeScanningAlerts(private val client: HttpClient) : Repos.CodeScanning.Alerts {
    override val autofix: Repos.CodeScanning.Alerts.Autofix = KtorReposCodeScanningAlertsAutofix(client)

    override val instances: Repos.CodeScanning.Alerts.Instances = KtorReposCodeScanningAlertsInstances(client)

    override suspend fun codeScanningListAlertsForRepo(owner: String, repo: String, direction: Repos.CodeScanning.Alerts.Direction, page: Long, perPage: Long, sort: Repos.CodeScanning.Alerts.Sort, after: String?, assignees: String?, before: String?, pr: Long?, ref: CodeScanningRef?, severity: CodeScanningAlertSeverity?, state: CodeScanningAlertStateQuery?, toolGuid: CodeScanningAnalysisToolGuid?, toolName: CodeScanningAnalysisToolName?): Repos.CodeScanning.Alerts.CodeScanningListAlertsForRepoResult {
        val response = client.get("/repos/$owner/$repo/code-scanning/alerts") {
            parameter("direction", direction)
            parameter("page", page)
            parameter("per_page", perPage)
            parameter("sort", sort)
            after?.let { parameter("after", it) }
            assignees?.let { parameter("assignees", it) }
            before?.let { parameter("before", it) }
            pr?.let { parameter("pr", it) }
            ref?.let { parameter("ref", it) }
            severity?.let { parameter("severity", it) }
            state?.let { parameter("state", it) }
            toolGuid?.let { parameter("tool_guid", it) }
            toolName?.let { parameter("tool_name", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.CodeScanning.Alerts.CodeScanningListAlertsForRepoResult.OK(response.body())
            HttpStatusCode.NotModified -> Repos.CodeScanning.Alerts.CodeScanningListAlertsForRepoResult.NotModified
            HttpStatusCode.Forbidden -> Repos.CodeScanning.Alerts.CodeScanningListAlertsForRepoResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.CodeScanning.Alerts.CodeScanningListAlertsForRepoResult.NotFound(response.body())
            HttpStatusCode.ServiceUnavailable -> Repos.CodeScanning.Alerts.CodeScanningListAlertsForRepoResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun codeScanningGetAlert(owner: String, repo: String, alertNumber: AlertNumberRequest): Repos.CodeScanning.Alerts.CodeScanningGetAlertResult {
        val response = client.get("/repos/$owner/$repo/code-scanning/alerts/$alertNumber")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.CodeScanning.Alerts.CodeScanningGetAlertResult.OK(response.body())
            HttpStatusCode.NotModified -> Repos.CodeScanning.Alerts.CodeScanningGetAlertResult.NotModified
            HttpStatusCode.Forbidden -> Repos.CodeScanning.Alerts.CodeScanningGetAlertResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.CodeScanning.Alerts.CodeScanningGetAlertResult.NotFound(response.body())
            HttpStatusCode.ServiceUnavailable -> Repos.CodeScanning.Alerts.CodeScanningGetAlertResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun codeScanningUpdateAlert(owner: String, repo: String, alertNumber: AlertNumberRequest, body: Repos.CodeScanning.Alerts.CodeScanningUpdateAlertBody): Repos.CodeScanning.Alerts.CodeScanningUpdateAlertResult {
        val response = client.patch("/repos/$owner/$repo/code-scanning/alerts/$alertNumber") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.CodeScanning.Alerts.CodeScanningUpdateAlertResult.OK(response.body())
            HttpStatusCode.BadRequest -> Repos.CodeScanning.Alerts.CodeScanningUpdateAlertResult.BadRequest(response.body())
            HttpStatusCode.Forbidden -> Repos.CodeScanning.Alerts.CodeScanningUpdateAlertResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.CodeScanning.Alerts.CodeScanningUpdateAlertResult.NotFound(response.body())
            HttpStatusCode.ServiceUnavailable -> Repos.CodeScanning.Alerts.CodeScanningUpdateAlertResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposCodeScanningAlertsAutofix(private val client: HttpClient) : Repos.CodeScanning.Alerts.Autofix {
    override val commits: Repos.CodeScanning.Alerts.Autofix.CommitsApi = KtorReposCodeScanningAlertsAutofixCommitsApi(client)

    override suspend fun codeScanningGetAutofix(owner: String, repo: String, alertNumber: AlertNumberRequest): Repos.CodeScanning.Alerts.Autofix.CodeScanningGetAutofixResult {
        val response = client.get("/repos/$owner/$repo/code-scanning/alerts/$alertNumber/autofix")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.CodeScanning.Alerts.Autofix.CodeScanningGetAutofixResult.OK(response.body())
            HttpStatusCode.BadRequest -> Repos.CodeScanning.Alerts.Autofix.CodeScanningGetAutofixResult.BadRequest(response.body())
            HttpStatusCode.Forbidden -> Repos.CodeScanning.Alerts.Autofix.CodeScanningGetAutofixResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.CodeScanning.Alerts.Autofix.CodeScanningGetAutofixResult.NotFound(response.body())
            HttpStatusCode.ServiceUnavailable -> Repos.CodeScanning.Alerts.Autofix.CodeScanningGetAutofixResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun codeScanningCreateAutofix(owner: String, repo: String, alertNumber: AlertNumberRequest): Repos.CodeScanning.Alerts.Autofix.CodeScanningCreateAutofixResult {
        val response = client.post("/repos/$owner/$repo/code-scanning/alerts/$alertNumber/autofix")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.CodeScanning.Alerts.Autofix.CodeScanningCreateAutofixResult.OK(response.body())
            HttpStatusCode.Accepted -> Repos.CodeScanning.Alerts.Autofix.CodeScanningCreateAutofixResult.Accepted(response.body())
            HttpStatusCode.BadRequest -> Repos.CodeScanning.Alerts.Autofix.CodeScanningCreateAutofixResult.BadRequest(response.body())
            HttpStatusCode.Forbidden -> Repos.CodeScanning.Alerts.Autofix.CodeScanningCreateAutofixResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.CodeScanning.Alerts.Autofix.CodeScanningCreateAutofixResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.CodeScanning.Alerts.Autofix.CodeScanningCreateAutofixResult.UnprocessableEntity
            HttpStatusCode.ServiceUnavailable -> Repos.CodeScanning.Alerts.Autofix.CodeScanningCreateAutofixResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposCodeScanningAlertsAutofixCommitsApi(private val client: HttpClient) : Repos.CodeScanning.Alerts.Autofix.CommitsApi {
    override suspend fun codeScanningCommitAutofix(owner: String, repo: String, alertNumber: AlertNumberRequest, body: CodeScanningAutofixCommits?): Repos.CodeScanning.Alerts.Autofix.CommitsApi.CodeScanningCommitAutofixResult {
        val response = client.post("/repos/$owner/$repo/code-scanning/alerts/$alertNumber/autofix/commits") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }
        return when (response.status) {
            HttpStatusCode.Created -> Repos.CodeScanning.Alerts.Autofix.CommitsApi.CodeScanningCommitAutofixResult.Created(response.body())
            HttpStatusCode.BadRequest -> Repos.CodeScanning.Alerts.Autofix.CommitsApi.CodeScanningCommitAutofixResult.BadRequest(response.body())
            HttpStatusCode.Forbidden -> Repos.CodeScanning.Alerts.Autofix.CommitsApi.CodeScanningCommitAutofixResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.CodeScanning.Alerts.Autofix.CommitsApi.CodeScanningCommitAutofixResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.CodeScanning.Alerts.Autofix.CommitsApi.CodeScanningCommitAutofixResult.UnprocessableEntity
            HttpStatusCode.ServiceUnavailable -> Repos.CodeScanning.Alerts.Autofix.CommitsApi.CodeScanningCommitAutofixResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposCodeScanningAlertsInstances(private val client: HttpClient) : Repos.CodeScanning.Alerts.Instances {
    override suspend fun codeScanningListAlertInstances(owner: String, repo: String, alertNumber: AlertNumberRequest, page: Long, perPage: Long, pr: Long?, ref: CodeScanningRef?): Repos.CodeScanning.Alerts.Instances.CodeScanningListAlertInstancesResult {
        val response = client.get("/repos/$owner/$repo/code-scanning/alerts/$alertNumber/instances") {
            parameter("page", page)
            parameter("per_page", perPage)
            pr?.let { parameter("pr", it) }
            ref?.let { parameter("ref", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.CodeScanning.Alerts.Instances.CodeScanningListAlertInstancesResult.OK(response.body())
            HttpStatusCode.Forbidden -> Repos.CodeScanning.Alerts.Instances.CodeScanningListAlertInstancesResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.CodeScanning.Alerts.Instances.CodeScanningListAlertInstancesResult.NotFound(response.body())
            HttpStatusCode.ServiceUnavailable -> Repos.CodeScanning.Alerts.Instances.CodeScanningListAlertInstancesResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposCodeScanningAnalyses(private val client: HttpClient) : Repos.CodeScanning.Analyses {
    override suspend fun codeScanningListRecentAnalyses(owner: String, repo: String, direction: Repos.CodeScanning.Analyses.Direction, page: Long, perPage: Long, sort: Repos.CodeScanning.Analyses.Sort, pr: Long?, ref: CodeScanningRef?, sarifId: CodeScanningAnalysisSarifId?, toolGuid: CodeScanningAnalysisToolGuid?, toolName: CodeScanningAnalysisToolName?): Repos.CodeScanning.Analyses.CodeScanningListRecentAnalysesResult {
        val response = client.get("/repos/$owner/$repo/code-scanning/analyses") {
            parameter("direction", direction)
            parameter("page", page)
            parameter("per_page", perPage)
            parameter("sort", sort)
            pr?.let { parameter("pr", it) }
            ref?.let { parameter("ref", it) }
            sarifId?.let { parameter("sarif_id", it) }
            toolGuid?.let { parameter("tool_guid", it) }
            toolName?.let { parameter("tool_name", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.CodeScanning.Analyses.CodeScanningListRecentAnalysesResult.OK(response.body())
            HttpStatusCode.Forbidden -> Repos.CodeScanning.Analyses.CodeScanningListRecentAnalysesResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.CodeScanning.Analyses.CodeScanningListRecentAnalysesResult.NotFound(response.body())
            HttpStatusCode.ServiceUnavailable -> Repos.CodeScanning.Analyses.CodeScanningListRecentAnalysesResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun codeScanningGetAnalysis(owner: String, repo: String, analysisId: Long): Repos.CodeScanning.Analyses.CodeScanningGetAnalysisResult {
        val response = client.get("/repos/$owner/$repo/code-scanning/analyses/$analysisId")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.CodeScanning.Analyses.CodeScanningGetAnalysisResult.OK(response.body())
            HttpStatusCode.Forbidden -> Repos.CodeScanning.Analyses.CodeScanningGetAnalysisResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.CodeScanning.Analyses.CodeScanningGetAnalysisResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.CodeScanning.Analyses.CodeScanningGetAnalysisResult.UnprocessableEntity(response.body())
            HttpStatusCode.ServiceUnavailable -> Repos.CodeScanning.Analyses.CodeScanningGetAnalysisResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun codeScanningDeleteAnalysis(owner: String, repo: String, analysisId: Long, confirmDelete: String?): Repos.CodeScanning.Analyses.CodeScanningDeleteAnalysisResult {
        val response = client.delete("/repos/$owner/$repo/code-scanning/analyses/$analysisId") {
            confirmDelete?.let { parameter("confirm_delete", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.CodeScanning.Analyses.CodeScanningDeleteAnalysisResult.OK(response.body())
            HttpStatusCode.BadRequest -> Repos.CodeScanning.Analyses.CodeScanningDeleteAnalysisResult.BadRequest(response.body())
            HttpStatusCode.Forbidden -> Repos.CodeScanning.Analyses.CodeScanningDeleteAnalysisResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.CodeScanning.Analyses.CodeScanningDeleteAnalysisResult.NotFound(response.body())
            HttpStatusCode.ServiceUnavailable -> Repos.CodeScanning.Analyses.CodeScanningDeleteAnalysisResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposCodeScanningCodeql(private val client: HttpClient) : Repos.CodeScanning.Codeql {
    override val databases: Repos.CodeScanning.Codeql.Databases = KtorReposCodeScanningCodeqlDatabases(client)

    override val variantAnalyses: Repos.CodeScanning.Codeql.VariantAnalyses = KtorReposCodeScanningCodeqlVariantAnalyses(client)
}

internal class KtorReposCodeScanningCodeqlDatabases(private val client: HttpClient) : Repos.CodeScanning.Codeql.Databases {
    override suspend fun codeScanningListCodeqlDatabases(owner: String, repo: String): Repos.CodeScanning.Codeql.Databases.CodeScanningListCodeqlDatabasesResult {
        val response = client.get("/repos/$owner/$repo/code-scanning/codeql/databases")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.CodeScanning.Codeql.Databases.CodeScanningListCodeqlDatabasesResult.OK(response.body())
            HttpStatusCode.Forbidden -> Repos.CodeScanning.Codeql.Databases.CodeScanningListCodeqlDatabasesResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.CodeScanning.Codeql.Databases.CodeScanningListCodeqlDatabasesResult.NotFound(response.body())
            HttpStatusCode.ServiceUnavailable -> Repos.CodeScanning.Codeql.Databases.CodeScanningListCodeqlDatabasesResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun codeScanningGetCodeqlDatabase(owner: String, repo: String, language: String): Repos.CodeScanning.Codeql.Databases.CodeScanningGetCodeqlDatabaseResult {
        val response = client.get("/repos/$owner/$repo/code-scanning/codeql/databases/$language")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.CodeScanning.Codeql.Databases.CodeScanningGetCodeqlDatabaseResult.OK(response.body())
            HttpStatusCode.Found -> Repos.CodeScanning.Codeql.Databases.CodeScanningGetCodeqlDatabaseResult.Found
            HttpStatusCode.Forbidden -> Repos.CodeScanning.Codeql.Databases.CodeScanningGetCodeqlDatabaseResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.CodeScanning.Codeql.Databases.CodeScanningGetCodeqlDatabaseResult.NotFound(response.body())
            HttpStatusCode.ServiceUnavailable -> Repos.CodeScanning.Codeql.Databases.CodeScanningGetCodeqlDatabaseResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun codeScanningDeleteCodeqlDatabase(owner: String, repo: String, language: String): Repos.CodeScanning.Codeql.Databases.CodeScanningDeleteCodeqlDatabaseResult {
        val response = client.delete("/repos/$owner/$repo/code-scanning/codeql/databases/$language")
        return when (response.status) {
            HttpStatusCode.NoContent -> Repos.CodeScanning.Codeql.Databases.CodeScanningDeleteCodeqlDatabaseResult.NoContent
            HttpStatusCode.Forbidden -> Repos.CodeScanning.Codeql.Databases.CodeScanningDeleteCodeqlDatabaseResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.CodeScanning.Codeql.Databases.CodeScanningDeleteCodeqlDatabaseResult.NotFound(response.body())
            HttpStatusCode.ServiceUnavailable -> Repos.CodeScanning.Codeql.Databases.CodeScanningDeleteCodeqlDatabaseResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposCodeScanningCodeqlVariantAnalyses(private val client: HttpClient) : Repos.CodeScanning.Codeql.VariantAnalyses {
    override val repos: Repos.CodeScanning.Codeql.VariantAnalyses.ReposApi = KtorReposCodeScanningCodeqlVariantAnalysesReposApi(client)

    override suspend fun codeScanningCreateVariantAnalysis(owner: String, repo: String, body: Repos.CodeScanning.Codeql.VariantAnalyses.CodeScanningCreateVariantAnalysisBody): Repos.CodeScanning.Codeql.VariantAnalyses.CodeScanningCreateVariantAnalysisResult {
        val response = client.post("/repos/$owner/$repo/code-scanning/codeql/variant-analyses") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> Repos.CodeScanning.Codeql.VariantAnalyses.CodeScanningCreateVariantAnalysisResult.Created(response.body())
            HttpStatusCode.NotFound -> Repos.CodeScanning.Codeql.VariantAnalyses.CodeScanningCreateVariantAnalysisResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.CodeScanning.Codeql.VariantAnalyses.CodeScanningCreateVariantAnalysisResult.UnprocessableEntity(response.body())
            HttpStatusCode.ServiceUnavailable -> Repos.CodeScanning.Codeql.VariantAnalyses.CodeScanningCreateVariantAnalysisResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun codeScanningGetVariantAnalysis(owner: String, repo: String, codeqlVariantAnalysisId: Long): Repos.CodeScanning.Codeql.VariantAnalyses.CodeScanningGetVariantAnalysisResult {
        val response = client.get("/repos/$owner/$repo/code-scanning/codeql/variant-analyses/$codeqlVariantAnalysisId")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.CodeScanning.Codeql.VariantAnalyses.CodeScanningGetVariantAnalysisResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.CodeScanning.Codeql.VariantAnalyses.CodeScanningGetVariantAnalysisResult.NotFound(response.body())
            HttpStatusCode.ServiceUnavailable -> Repos.CodeScanning.Codeql.VariantAnalyses.CodeScanningGetVariantAnalysisResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposCodeScanningCodeqlVariantAnalysesReposApi(private val client: HttpClient) : Repos.CodeScanning.Codeql.VariantAnalyses.ReposApi {
    override suspend fun codeScanningGetVariantAnalysisRepoTask(owner: String, repo: String, codeqlVariantAnalysisId: Long, repoOwner: String, repoName: String): Repos.CodeScanning.Codeql.VariantAnalyses.ReposApi.CodeScanningGetVariantAnalysisRepoTaskResult {
        val response = client.get("/repos/$owner/$repo/code-scanning/codeql/variant-analyses/$codeqlVariantAnalysisId/repos/$repoOwner/$repoName")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.CodeScanning.Codeql.VariantAnalyses.ReposApi.CodeScanningGetVariantAnalysisRepoTaskResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.CodeScanning.Codeql.VariantAnalyses.ReposApi.CodeScanningGetVariantAnalysisRepoTaskResult.NotFound(response.body())
            HttpStatusCode.ServiceUnavailable -> Repos.CodeScanning.Codeql.VariantAnalyses.ReposApi.CodeScanningGetVariantAnalysisRepoTaskResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposCodeScanningDefaultSetup(private val client: HttpClient) : Repos.CodeScanning.DefaultSetup {
    override suspend fun codeScanningGetDefaultSetup(owner: String, repo: String): Repos.CodeScanning.DefaultSetup.CodeScanningGetDefaultSetupResult {
        val response = client.get("/repos/$owner/$repo/code-scanning/default-setup")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.CodeScanning.DefaultSetup.CodeScanningGetDefaultSetupResult.OK(response.body())
            HttpStatusCode.Forbidden -> Repos.CodeScanning.DefaultSetup.CodeScanningGetDefaultSetupResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.CodeScanning.DefaultSetup.CodeScanningGetDefaultSetupResult.NotFound(response.body())
            HttpStatusCode.ServiceUnavailable -> Repos.CodeScanning.DefaultSetup.CodeScanningGetDefaultSetupResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun codeScanningUpdateDefaultSetup(owner: String, repo: String, body: CodeScanningDefaultSetupUpdate): Repos.CodeScanning.DefaultSetup.CodeScanningUpdateDefaultSetupResult {
        val response = client.patch("/repos/$owner/$repo/code-scanning/default-setup") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.CodeScanning.DefaultSetup.CodeScanningUpdateDefaultSetupResult.OK(response.body())
            HttpStatusCode.Accepted -> Repos.CodeScanning.DefaultSetup.CodeScanningUpdateDefaultSetupResult.Accepted(response.body())
            HttpStatusCode.Forbidden -> Repos.CodeScanning.DefaultSetup.CodeScanningUpdateDefaultSetupResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.CodeScanning.DefaultSetup.CodeScanningUpdateDefaultSetupResult.NotFound(response.body())
            HttpStatusCode.Conflict -> Repos.CodeScanning.DefaultSetup.CodeScanningUpdateDefaultSetupResult.Conflict(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.CodeScanning.DefaultSetup.CodeScanningUpdateDefaultSetupResult.UnprocessableEntity(response.body())
            HttpStatusCode.ServiceUnavailable -> Repos.CodeScanning.DefaultSetup.CodeScanningUpdateDefaultSetupResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposCodeScanningSarifs(private val client: HttpClient) : Repos.CodeScanning.Sarifs {
    override suspend fun codeScanningUploadSarif(owner: String, repo: String, body: Repos.CodeScanning.Sarifs.CodeScanningUploadSarifBody): Repos.CodeScanning.Sarifs.CodeScanningUploadSarifResult {
        val response = client.post("/repos/$owner/$repo/code-scanning/sarifs") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Accepted -> Repos.CodeScanning.Sarifs.CodeScanningUploadSarifResult.Accepted(response.body())
            HttpStatusCode.BadRequest -> Repos.CodeScanning.Sarifs.CodeScanningUploadSarifResult.BadRequest
            HttpStatusCode.Forbidden -> Repos.CodeScanning.Sarifs.CodeScanningUploadSarifResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.CodeScanning.Sarifs.CodeScanningUploadSarifResult.NotFound(response.body())
            HttpStatusCode.PayloadTooLarge -> Repos.CodeScanning.Sarifs.CodeScanningUploadSarifResult.PayloadTooLarge
            HttpStatusCode.ServiceUnavailable -> Repos.CodeScanning.Sarifs.CodeScanningUploadSarifResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun codeScanningGetSarif(owner: String, repo: String, sarifId: String): Repos.CodeScanning.Sarifs.CodeScanningGetSarifResult {
        val response = client.get("/repos/$owner/$repo/code-scanning/sarifs/$sarifId")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.CodeScanning.Sarifs.CodeScanningGetSarifResult.OK(response.body())
            HttpStatusCode.Forbidden -> Repos.CodeScanning.Sarifs.CodeScanningGetSarifResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.CodeScanning.Sarifs.CodeScanningGetSarifResult.NotFound
            HttpStatusCode.ServiceUnavailable -> Repos.CodeScanning.Sarifs.CodeScanningGetSarifResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposCodeSecurityConfiguration(private val client: HttpClient) : Repos.CodeSecurityConfiguration {
    override suspend fun codeSecurityGetConfigurationForRepository(owner: String, repo: String): Repos.CodeSecurityConfiguration.CodeSecurityGetConfigurationForRepositoryResult {
        val response = client.get("/repos/$owner/$repo/code-security-configuration")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.CodeSecurityConfiguration.CodeSecurityGetConfigurationForRepositoryResult.OK(response.body())
            HttpStatusCode.NoContent -> Repos.CodeSecurityConfiguration.CodeSecurityGetConfigurationForRepositoryResult.NoContent
            HttpStatusCode.NotModified -> Repos.CodeSecurityConfiguration.CodeSecurityGetConfigurationForRepositoryResult.NotModified
            HttpStatusCode.Forbidden -> Repos.CodeSecurityConfiguration.CodeSecurityGetConfigurationForRepositoryResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.CodeSecurityConfiguration.CodeSecurityGetConfigurationForRepositoryResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposCodeowners(private val client: HttpClient) : Repos.Codeowners {
    override val errors: Repos.Codeowners.Errors = KtorReposCodeownersErrors(client)
}

internal class KtorReposCodeownersErrors(private val client: HttpClient) : Repos.Codeowners.Errors {
    override suspend fun reposCodeownersErrors(owner: String, repo: String, ref: String?): Repos.Codeowners.Errors.ReposCodeownersErrorsResult {
        val response = client.get("/repos/$owner/$repo/codeowners/errors") {
            ref?.let { parameter("ref", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Codeowners.Errors.ReposCodeownersErrorsResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Codeowners.Errors.ReposCodeownersErrorsResult.NotFound
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposCodespaces(private val client: HttpClient) : Repos.Codespaces {
    override val devcontainers: Repos.Codespaces.Devcontainers = KtorReposCodespacesDevcontainers(client)

    override val machines: Repos.Codespaces.Machines = KtorReposCodespacesMachines(client)

    override val new: Repos.Codespaces.New = KtorReposCodespacesNew(client)

    override val permissionsCheck: Repos.Codespaces.PermissionsCheck = KtorReposCodespacesPermissionsCheck(client)

    override val secrets: Repos.Codespaces.Secrets = KtorReposCodespacesSecrets(client)

    override suspend fun codespacesListInRepositoryForAuthenticatedUser(owner: String, repo: String, page: Long, perPage: Long): Repos.Codespaces.CodespacesListInRepositoryForAuthenticatedUserResult {
        val response = client.get("/repos/$owner/$repo/codespaces") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Codespaces.CodespacesListInRepositoryForAuthenticatedUserResult.OK(response.body())
            HttpStatusCode.Unauthorized -> Repos.Codespaces.CodespacesListInRepositoryForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Repos.Codespaces.CodespacesListInRepositoryForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.Codespaces.CodespacesListInRepositoryForAuthenticatedUserResult.NotFound(response.body())
            HttpStatusCode.InternalServerError -> Repos.Codespaces.CodespacesListInRepositoryForAuthenticatedUserResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun codespacesCreateWithRepoForAuthenticatedUser(owner: String, repo: String, body: Repos.Codespaces.CodespacesCreateWithRepoForAuthenticatedUserBody): Repos.Codespaces.CodespacesCreateWithRepoForAuthenticatedUserResult {
        val response = client.post("/repos/$owner/$repo/codespaces") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> Repos.Codespaces.CodespacesCreateWithRepoForAuthenticatedUserResult.Created(response.body())
            HttpStatusCode.Accepted -> Repos.Codespaces.CodespacesCreateWithRepoForAuthenticatedUserResult.Accepted(response.body())
            HttpStatusCode.BadRequest -> Repos.Codespaces.CodespacesCreateWithRepoForAuthenticatedUserResult.BadRequest(response.body())
            HttpStatusCode.Unauthorized -> Repos.Codespaces.CodespacesCreateWithRepoForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Repos.Codespaces.CodespacesCreateWithRepoForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.Codespaces.CodespacesCreateWithRepoForAuthenticatedUserResult.NotFound(response.body())
            HttpStatusCode.ServiceUnavailable -> Repos.Codespaces.CodespacesCreateWithRepoForAuthenticatedUserResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposCodespacesDevcontainers(private val client: HttpClient) : Repos.Codespaces.Devcontainers {
    override suspend fun codespacesListDevcontainersInRepositoryForAuthenticatedUser(owner: String, repo: String, page: Long, perPage: Long): Repos.Codespaces.Devcontainers.CodespacesListDevcontainersInRepositoryForAuthenticatedUserResult {
        val response = client.get("/repos/$owner/$repo/codespaces/devcontainers") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Codespaces.Devcontainers.CodespacesListDevcontainersInRepositoryForAuthenticatedUserResult.OK(response.body())
            HttpStatusCode.BadRequest -> Repos.Codespaces.Devcontainers.CodespacesListDevcontainersInRepositoryForAuthenticatedUserResult.BadRequest(response.body())
            HttpStatusCode.Unauthorized -> Repos.Codespaces.Devcontainers.CodespacesListDevcontainersInRepositoryForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Repos.Codespaces.Devcontainers.CodespacesListDevcontainersInRepositoryForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.Codespaces.Devcontainers.CodespacesListDevcontainersInRepositoryForAuthenticatedUserResult.NotFound(response.body())
            HttpStatusCode.InternalServerError -> Repos.Codespaces.Devcontainers.CodespacesListDevcontainersInRepositoryForAuthenticatedUserResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposCodespacesMachines(private val client: HttpClient) : Repos.Codespaces.Machines {
    override suspend fun codespacesRepoMachinesForAuthenticatedUser(owner: String, repo: String, clientIp: String?, location: String?, ref: String?): Repos.Codespaces.Machines.CodespacesRepoMachinesForAuthenticatedUserResult {
        val response = client.get("/repos/$owner/$repo/codespaces/machines") {
            clientIp?.let { parameter("client_ip", it) }
            location?.let { parameter("location", it) }
            ref?.let { parameter("ref", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Codespaces.Machines.CodespacesRepoMachinesForAuthenticatedUserResult.OK(response.body())
            HttpStatusCode.NotModified -> Repos.Codespaces.Machines.CodespacesRepoMachinesForAuthenticatedUserResult.NotModified
            HttpStatusCode.Unauthorized -> Repos.Codespaces.Machines.CodespacesRepoMachinesForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Repos.Codespaces.Machines.CodespacesRepoMachinesForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.Codespaces.Machines.CodespacesRepoMachinesForAuthenticatedUserResult.NotFound(response.body())
            HttpStatusCode.InternalServerError -> Repos.Codespaces.Machines.CodespacesRepoMachinesForAuthenticatedUserResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposCodespacesNew(private val client: HttpClient) : Repos.Codespaces.New {
    override suspend fun codespacesPreFlightWithRepoForAuthenticatedUser(owner: String, repo: String, clientIp: String?, ref: String?): Repos.Codespaces.New.CodespacesPreFlightWithRepoForAuthenticatedUserResult {
        val response = client.get("/repos/$owner/$repo/codespaces/new") {
            clientIp?.let { parameter("client_ip", it) }
            ref?.let { parameter("ref", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Codespaces.New.CodespacesPreFlightWithRepoForAuthenticatedUserResult.OK(response.body())
            HttpStatusCode.Unauthorized -> Repos.Codespaces.New.CodespacesPreFlightWithRepoForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Repos.Codespaces.New.CodespacesPreFlightWithRepoForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.Codespaces.New.CodespacesPreFlightWithRepoForAuthenticatedUserResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposCodespacesPermissionsCheck(private val client: HttpClient) : Repos.Codespaces.PermissionsCheck {
    override suspend fun codespacesCheckPermissionsForDevcontainer(owner: String, repo: String, devcontainerPath: String, ref: String): Repos.Codespaces.PermissionsCheck.CodespacesCheckPermissionsForDevcontainerResult {
        val response = client.get("/repos/$owner/$repo/codespaces/permissions_check") {
            parameter("devcontainer_path", devcontainerPath)
            parameter("ref", ref)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Codespaces.PermissionsCheck.CodespacesCheckPermissionsForDevcontainerResult.OK(response.body())
            HttpStatusCode.Unauthorized -> Repos.Codespaces.PermissionsCheck.CodespacesCheckPermissionsForDevcontainerResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Repos.Codespaces.PermissionsCheck.CodespacesCheckPermissionsForDevcontainerResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.Codespaces.PermissionsCheck.CodespacesCheckPermissionsForDevcontainerResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Codespaces.PermissionsCheck.CodespacesCheckPermissionsForDevcontainerResult.UnprocessableEntity(response.body())
            HttpStatusCode.ServiceUnavailable -> Repos.Codespaces.PermissionsCheck.CodespacesCheckPermissionsForDevcontainerResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposCodespacesSecrets(private val client: HttpClient) : Repos.Codespaces.Secrets {
    override val publicKey: Repos.Codespaces.Secrets.PublicKey = KtorReposCodespacesSecretsPublicKey(client)

    override suspend fun codespacesListRepoSecrets(owner: String, repo: String, page: Long, perPage: Long): Repos.Codespaces.Secrets.CodespacesListRepoSecretsResponse =
        client.get("/repos/$owner/$repo/codespaces/secrets") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()

    override suspend fun codespacesGetRepoSecret(owner: String, repo: String, secretName: String): RepoCodespacesSecret =
        client.get("/repos/$owner/$repo/codespaces/secrets/$secretName").body()

    override suspend fun codespacesCreateOrUpdateRepoSecret(owner: String, repo: String, secretName: String, body: Repos.Codespaces.Secrets.CodespacesCreateOrUpdateRepoSecretBody): Repos.Codespaces.Secrets.CodespacesCreateOrUpdateRepoSecretResult {
        val response = client.put("/repos/$owner/$repo/codespaces/secrets/$secretName") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> Repos.Codespaces.Secrets.CodespacesCreateOrUpdateRepoSecretResult.Created(response.body())
            HttpStatusCode.NoContent -> Repos.Codespaces.Secrets.CodespacesCreateOrUpdateRepoSecretResult.NoContent
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun codespacesDeleteRepoSecret(owner: String, repo: String, secretName: String): Unit =
        client.delete("/repos/$owner/$repo/codespaces/secrets/$secretName").body()
}

internal class KtorReposCodespacesSecretsPublicKey(private val client: HttpClient) : Repos.Codespaces.Secrets.PublicKey {
    override suspend fun codespacesGetRepoPublicKey(owner: String, repo: String): CodespacesPublicKey =
        client.get("/repos/$owner/$repo/codespaces/secrets/public-key").body()
}

internal class KtorReposCollaborators(private val client: HttpClient) : Repos.Collaborators {
    override val permission: Repos.Collaborators.Permission = KtorReposCollaboratorsPermission(client)

    override suspend fun reposListCollaborators(owner: String, repo: String, affiliation: Repos.Collaborators.Affiliation, page: Long, perPage: Long, permission: Repos.Collaborators.ReposListCollaboratorsPermission?): Repos.Collaborators.ReposListCollaboratorsResult {
        val response = client.get("/repos/$owner/$repo/collaborators") {
            parameter("affiliation", affiliation)
            parameter("page", page)
            parameter("per_page", perPage)
            permission?.let { parameter("permission", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Collaborators.ReposListCollaboratorsResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Collaborators.ReposListCollaboratorsResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposCheckCollaborator(owner: String, repo: String, username: String): Repos.Collaborators.ReposCheckCollaboratorResult {
        val response = client.get("/repos/$owner/$repo/collaborators/$username")
        return when (response.status) {
            HttpStatusCode.NoContent -> Repos.Collaborators.ReposCheckCollaboratorResult.NoContent
            HttpStatusCode.NotFound -> Repos.Collaborators.ReposCheckCollaboratorResult.NotFound
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposAddCollaborator(owner: String, repo: String, username: String, body: Repos.Collaborators.ReposAddCollaboratorBody?): Repos.Collaborators.ReposAddCollaboratorResult {
        val response = client.put("/repos/$owner/$repo/collaborators/$username") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }
        return when (response.status) {
            HttpStatusCode.Created -> Repos.Collaborators.ReposAddCollaboratorResult.Created(response.body())
            HttpStatusCode.NoContent -> Repos.Collaborators.ReposAddCollaboratorResult.NoContent
            HttpStatusCode.Forbidden -> Repos.Collaborators.ReposAddCollaboratorResult.Forbidden(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Collaborators.ReposAddCollaboratorResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposRemoveCollaborator(owner: String, repo: String, username: String): Repos.Collaborators.ReposRemoveCollaboratorResult {
        val response = client.delete("/repos/$owner/$repo/collaborators/$username")
        return when (response.status) {
            HttpStatusCode.NoContent -> Repos.Collaborators.ReposRemoveCollaboratorResult.NoContent
            HttpStatusCode.Forbidden -> Repos.Collaborators.ReposRemoveCollaboratorResult.Forbidden(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Collaborators.ReposRemoveCollaboratorResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposCollaboratorsPermission(private val client: HttpClient) : Repos.Collaborators.Permission {
    override suspend fun reposGetCollaboratorPermissionLevel(owner: String, repo: String, username: String): Repos.Collaborators.Permission.ReposGetCollaboratorPermissionLevelResult {
        val response = client.get("/repos/$owner/$repo/collaborators/$username/permission")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Collaborators.Permission.ReposGetCollaboratorPermissionLevelResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Collaborators.Permission.ReposGetCollaboratorPermissionLevelResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposComments(private val client: HttpClient) : Repos.Comments {
    override val reactions: Repos.Comments.Reactions = KtorReposCommentsReactions(client)

    override suspend fun reposListCommitCommentsForRepo(owner: String, repo: String, page: Long, perPage: Long): List<CommitComment> =
        client.get("/repos/$owner/$repo/comments") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()

    override suspend fun reposGetCommitComment(owner: String, repo: String, commentId: Long): Repos.Comments.ReposGetCommitCommentResult {
        val response = client.get("/repos/$owner/$repo/comments/$commentId")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Comments.ReposGetCommitCommentResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Comments.ReposGetCommitCommentResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposDeleteCommitComment(owner: String, repo: String, commentId: Long): Repos.Comments.ReposDeleteCommitCommentResult {
        val response = client.delete("/repos/$owner/$repo/comments/$commentId")
        return when (response.status) {
            HttpStatusCode.NoContent -> Repos.Comments.ReposDeleteCommitCommentResult.NoContent
            HttpStatusCode.NotFound -> Repos.Comments.ReposDeleteCommitCommentResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposUpdateCommitComment(owner: String, repo: String, commentId: Long, body: Repos.Comments.ReposUpdateCommitCommentBody): Repos.Comments.ReposUpdateCommitCommentResult {
        val response = client.patch("/repos/$owner/$repo/comments/$commentId") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Comments.ReposUpdateCommitCommentResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Comments.ReposUpdateCommitCommentResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposCommentsReactions(private val client: HttpClient) : Repos.Comments.Reactions {
    override suspend fun reactionsListForCommitComment(owner: String, repo: String, commentId: Long, page: Long, perPage: Long, content: Repos.Comments.Reactions.Content?): Repos.Comments.Reactions.ReactionsListForCommitCommentResult {
        val response = client.get("/repos/$owner/$repo/comments/$commentId/reactions") {
            parameter("page", page)
            parameter("per_page", perPage)
            content?.let { parameter("content", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Comments.Reactions.ReactionsListForCommitCommentResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Comments.Reactions.ReactionsListForCommitCommentResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reactionsCreateForCommitComment(owner: String, repo: String, commentId: Long, body: Repos.Comments.Reactions.ReactionsCreateForCommitCommentBody): Repos.Comments.Reactions.ReactionsCreateForCommitCommentResult {
        val response = client.post("/repos/$owner/$repo/comments/$commentId/reactions") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Comments.Reactions.ReactionsCreateForCommitCommentResult.OK(response.body())
            HttpStatusCode.Created -> Repos.Comments.Reactions.ReactionsCreateForCommitCommentResult.Created(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Comments.Reactions.ReactionsCreateForCommitCommentResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reactionsDeleteForCommitComment(owner: String, repo: String, commentId: Long, reactionId: Long): Unit =
        client.delete("/repos/$owner/$repo/comments/$commentId/reactions/$reactionId").body()
}

internal class KtorReposCommits(private val client: HttpClient) : Repos.Commits {
    override val branchesWhereHead: Repos.Commits.BranchesWhereHead = KtorReposCommitsBranchesWhereHead(client)

    override val comments: Repos.Commits.CommentsApi = KtorReposCommitsCommentsApi(client)

    override val pulls: Repos.Commits.PullsApi = KtorReposCommitsPullsApi(client)

    override val checkRuns: Repos.Commits.CheckRunsApi = KtorReposCommitsCheckRunsApi(client)

    override val checkSuites: Repos.Commits.CheckSuitesApi = KtorReposCommitsCheckSuitesApi(client)

    override val status: Repos.Commits.Status = KtorReposCommitsStatus(client)

    override val statuses: Repos.Commits.StatusesApi = KtorReposCommitsStatusesApi(client)

    override suspend fun reposListCommits(owner: String, repo: String, page: Long, perPage: Long, author: String?, committer: String?, path: String?, sha: String?, since: LocalDateTime?, until: LocalDateTime?): Repos.Commits.ReposListCommitsResult {
        val response = client.get("/repos/$owner/$repo/commits") {
            parameter("page", page)
            parameter("per_page", perPage)
            author?.let { parameter("author", it) }
            committer?.let { parameter("committer", it) }
            path?.let { parameter("path", it) }
            sha?.let { parameter("sha", it) }
            since?.let { parameter("since", it) }
            until?.let { parameter("until", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Commits.ReposListCommitsResult.OK(response.body())
            HttpStatusCode.BadRequest -> Repos.Commits.ReposListCommitsResult.BadRequest(response.body())
            HttpStatusCode.NotFound -> Repos.Commits.ReposListCommitsResult.NotFound(response.body())
            HttpStatusCode.Conflict -> Repos.Commits.ReposListCommitsResult.Conflict(response.body())
            HttpStatusCode.InternalServerError -> Repos.Commits.ReposListCommitsResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposGetCommit(owner: String, repo: String, ref: String, page: Long, perPage: Long): Repos.Commits.ReposGetCommitResult {
        val response = client.get("/repos/$owner/$repo/commits/$ref") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Commits.ReposGetCommitResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Commits.ReposGetCommitResult.NotFound(response.body())
            HttpStatusCode.Conflict -> Repos.Commits.ReposGetCommitResult.Conflict(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Commits.ReposGetCommitResult.UnprocessableEntity(response.body())
            HttpStatusCode.InternalServerError -> Repos.Commits.ReposGetCommitResult.InternalServerError(response.body())
            HttpStatusCode.ServiceUnavailable -> Repos.Commits.ReposGetCommitResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposCommitsBranchesWhereHead(private val client: HttpClient) : Repos.Commits.BranchesWhereHead {
    override suspend fun reposListBranchesForHeadCommit(owner: String, repo: String, commitSha: String): Repos.Commits.BranchesWhereHead.ReposListBranchesForHeadCommitResult {
        val response = client.get("/repos/$owner/$repo/commits/$commitSha/branches-where-head")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Commits.BranchesWhereHead.ReposListBranchesForHeadCommitResult.OK(response.body())
            HttpStatusCode.Conflict -> Repos.Commits.BranchesWhereHead.ReposListBranchesForHeadCommitResult.Conflict(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Commits.BranchesWhereHead.ReposListBranchesForHeadCommitResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposCommitsCommentsApi(private val client: HttpClient) : Repos.Commits.CommentsApi {
    override suspend fun reposListCommentsForCommit(owner: String, repo: String, commitSha: String, page: Long, perPage: Long): List<CommitComment> =
        client.get("/repos/$owner/$repo/commits/$commitSha/comments") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()

    override suspend fun reposCreateCommitComment(owner: String, repo: String, commitSha: String, body: Repos.Commits.CommentsApi.ReposCreateCommitCommentBody): Repos.Commits.CommentsApi.ReposCreateCommitCommentResult {
        val response = client.post("/repos/$owner/$repo/commits/$commitSha/comments") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> Repos.Commits.CommentsApi.ReposCreateCommitCommentResult.Created(response.body())
            HttpStatusCode.Forbidden -> Repos.Commits.CommentsApi.ReposCreateCommitCommentResult.Forbidden(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Commits.CommentsApi.ReposCreateCommitCommentResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposCommitsPullsApi(private val client: HttpClient) : Repos.Commits.PullsApi {
    override suspend fun reposListPullRequestsAssociatedWithCommit(owner: String, repo: String, commitSha: String, page: Long, perPage: Long): Repos.Commits.PullsApi.ReposListPullRequestsAssociatedWithCommitResult {
        val response = client.get("/repos/$owner/$repo/commits/$commitSha/pulls") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Commits.PullsApi.ReposListPullRequestsAssociatedWithCommitResult.OK(response.body())
            HttpStatusCode.Conflict -> Repos.Commits.PullsApi.ReposListPullRequestsAssociatedWithCommitResult.Conflict(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposCommitsCheckRunsApi(private val client: HttpClient) : Repos.Commits.CheckRunsApi {
    override suspend fun checksListForRef(owner: String, repo: String, ref: String, filter: Repos.Commits.CheckRunsApi.Filter, page: Long, perPage: Long, appId: Long?, checkName: String?, status: Repos.Commits.CheckRunsApi.ChecksListForRefStatus?): Repos.Commits.CheckRunsApi.ChecksListForRefResponse =
        client.get("/repos/$owner/$repo/commits/$ref/check-runs") {
            parameter("filter", filter)
            parameter("page", page)
            parameter("per_page", perPage)
            appId?.let { parameter("app_id", it) }
            checkName?.let { parameter("check_name", it) }
            status?.let { parameter("status", it) }
        }.body()
}

internal class KtorReposCommitsCheckSuitesApi(private val client: HttpClient) : Repos.Commits.CheckSuitesApi {
    override suspend fun checksListSuitesForRef(owner: String, repo: String, ref: String, page: Long, perPage: Long, appId: Long?, checkName: String?): Repos.Commits.CheckSuitesApi.ChecksListSuitesForRefResponse =
        client.get("/repos/$owner/$repo/commits/$ref/check-suites") {
            parameter("page", page)
            parameter("per_page", perPage)
            appId?.let { parameter("app_id", it) }
            checkName?.let { parameter("check_name", it) }
        }.body()
}

internal class KtorReposCommitsStatus(private val client: HttpClient) : Repos.Commits.Status {
    override suspend fun reposGetCombinedStatusForRef(owner: String, repo: String, ref: String, page: Long, perPage: Long): Repos.Commits.Status.ReposGetCombinedStatusForRefResult {
        val response = client.get("/repos/$owner/$repo/commits/$ref/status") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Commits.Status.ReposGetCombinedStatusForRefResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Commits.Status.ReposGetCombinedStatusForRefResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposCommitsStatusesApi(private val client: HttpClient) : Repos.Commits.StatusesApi {
    override suspend fun reposListCommitStatusesForRef(owner: String, repo: String, ref: String, page: Long, perPage: Long): Repos.Commits.StatusesApi.ReposListCommitStatusesForRefResult {
        val response = client.get("/repos/$owner/$repo/commits/$ref/statuses") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Commits.StatusesApi.ReposListCommitStatusesForRefResult.OK(response.body())
            HttpStatusCode.MovedPermanently -> Repos.Commits.StatusesApi.ReposListCommitStatusesForRefResult.MovedPermanently(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposCommunity(private val client: HttpClient) : Repos.Community {
    override val profile: Repos.Community.Profile = KtorReposCommunityProfile(client)
}

internal class KtorReposCommunityProfile(private val client: HttpClient) : Repos.Community.Profile {
    override suspend fun reposGetCommunityProfileMetrics(owner: String, repo: String): CommunityProfile =
        client.get("/repos/$owner/$repo/community/profile").body()
}

internal class KtorReposCompare(private val client: HttpClient) : Repos.Compare {
    override suspend fun reposCompareCommits(owner: String, repo: String, basehead: String, page: Long, perPage: Long): Repos.Compare.ReposCompareCommitsResult {
        val response = client.get("/repos/$owner/$repo/compare/$basehead") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Compare.ReposCompareCommitsResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Compare.ReposCompareCommitsResult.NotFound(response.body())
            HttpStatusCode.InternalServerError -> Repos.Compare.ReposCompareCommitsResult.InternalServerError(response.body())
            HttpStatusCode.ServiceUnavailable -> Repos.Compare.ReposCompareCommitsResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposContents(private val client: HttpClient) : Repos.Contents {
    override suspend fun reposGetContent(owner: String, repo: String, path: String, ref: String?): Repos.Contents.ReposGetContentResult {
        val response = client.get("/repos/$owner/$repo/contents/$path") {
            ref?.let { parameter("ref", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Contents.ReposGetContentResult.OK(response.body())
            HttpStatusCode.Found -> Repos.Contents.ReposGetContentResult.Found
            HttpStatusCode.NotModified -> Repos.Contents.ReposGetContentResult.NotModified
            HttpStatusCode.Forbidden -> Repos.Contents.ReposGetContentResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.Contents.ReposGetContentResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposCreateOrUpdateFileContents(owner: String, repo: String, path: String, body: Repos.Contents.ReposCreateOrUpdateFileContentsBody): Repos.Contents.ReposCreateOrUpdateFileContentsResult {
        val response = client.put("/repos/$owner/$repo/contents/$path") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Contents.ReposCreateOrUpdateFileContentsResult.OK(response.body())
            HttpStatusCode.Created -> Repos.Contents.ReposCreateOrUpdateFileContentsResult.Created(response.body())
            HttpStatusCode.NotFound -> Repos.Contents.ReposCreateOrUpdateFileContentsResult.NotFound(response.body())
            HttpStatusCode.Conflict -> Repos.Contents.ReposCreateOrUpdateFileContentsResult.Conflict(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Contents.ReposCreateOrUpdateFileContentsResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposDeleteFile(owner: String, repo: String, path: String, body: Repos.Contents.ReposDeleteFileBody): Repos.Contents.ReposDeleteFileResult {
        val response = client.delete("/repos/$owner/$repo/contents/$path") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Contents.ReposDeleteFileResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Contents.ReposDeleteFileResult.NotFound(response.body())
            HttpStatusCode.Conflict -> Repos.Contents.ReposDeleteFileResult.Conflict(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Contents.ReposDeleteFileResult.UnprocessableEntity(response.body())
            HttpStatusCode.ServiceUnavailable -> Repos.Contents.ReposDeleteFileResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposContributors(private val client: HttpClient) : Repos.Contributors {
    override suspend fun reposListContributors(owner: String, repo: String, page: Long, perPage: Long, anon: String?): Repos.Contributors.ReposListContributorsResult {
        val response = client.get("/repos/$owner/$repo/contributors") {
            parameter("page", page)
            parameter("per_page", perPage)
            anon?.let { parameter("anon", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Contributors.ReposListContributorsResult.OK(response.body())
            HttpStatusCode.NoContent -> Repos.Contributors.ReposListContributorsResult.NoContent
            HttpStatusCode.Forbidden -> Repos.Contributors.ReposListContributorsResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.Contributors.ReposListContributorsResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposDependabot(private val client: HttpClient) : Repos.Dependabot {
    override val alerts: Repos.Dependabot.Alerts = KtorReposDependabotAlerts(client)

    override val secrets: Repos.Dependabot.Secrets = KtorReposDependabotSecrets(client)
}

internal class KtorReposDependabotAlerts(private val client: HttpClient) : Repos.Dependabot.Alerts {
    override suspend fun dependabotListAlertsForRepo(owner: String, repo: String, direction: Repos.Dependabot.Alerts.Direction, perPage: Long, sort: Repos.Dependabot.Alerts.Sort, after: String?, assignee: String?, before: String?, ecosystem: String?, epssPercentage: String?, has: Repos.Dependabot.Alerts.Has?, manifest: String?, `package`: String?, scope: Repos.Dependabot.Alerts.Scope?, severity: String?, state: String?): Repos.Dependabot.Alerts.DependabotListAlertsForRepoResult {
        val response = client.get("/repos/$owner/$repo/dependabot/alerts") {
            parameter("direction", direction)
            parameter("per_page", perPage)
            parameter("sort", sort)
            after?.let { parameter("after", it) }
            assignee?.let { parameter("assignee", it) }
            before?.let { parameter("before", it) }
            ecosystem?.let { parameter("ecosystem", it) }
            epssPercentage?.let { parameter("epss_percentage", it) }
            has?.let { parameter("has", it) }
            manifest?.let { parameter("manifest", it) }
            `package`?.let { parameter("package", it) }
            scope?.let { parameter("scope", it) }
            severity?.let { parameter("severity", it) }
            state?.let { parameter("state", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Dependabot.Alerts.DependabotListAlertsForRepoResult.OK(response.body())
            HttpStatusCode.NotModified -> Repos.Dependabot.Alerts.DependabotListAlertsForRepoResult.NotModified
            HttpStatusCode.BadRequest -> Repos.Dependabot.Alerts.DependabotListAlertsForRepoResult.BadRequest(response.body())
            HttpStatusCode.Forbidden -> Repos.Dependabot.Alerts.DependabotListAlertsForRepoResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.Dependabot.Alerts.DependabotListAlertsForRepoResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Dependabot.Alerts.DependabotListAlertsForRepoResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun dependabotGetAlert(owner: String, repo: String, alertNumber: AlertNumberRequest): Repos.Dependabot.Alerts.DependabotGetAlertResult {
        val response = client.get("/repos/$owner/$repo/dependabot/alerts/$alertNumber")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Dependabot.Alerts.DependabotGetAlertResult.OK(response.body())
            HttpStatusCode.NotModified -> Repos.Dependabot.Alerts.DependabotGetAlertResult.NotModified
            HttpStatusCode.Forbidden -> Repos.Dependabot.Alerts.DependabotGetAlertResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.Dependabot.Alerts.DependabotGetAlertResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun dependabotUpdateAlert(owner: String, repo: String, alertNumber: AlertNumberRequest, body: Repos.Dependabot.Alerts.DependabotUpdateAlertBody): Repos.Dependabot.Alerts.DependabotUpdateAlertResult {
        val response = client.patch("/repos/$owner/$repo/dependabot/alerts/$alertNumber") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Dependabot.Alerts.DependabotUpdateAlertResult.OK(response.body())
            HttpStatusCode.BadRequest -> Repos.Dependabot.Alerts.DependabotUpdateAlertResult.BadRequest(response.body())
            HttpStatusCode.Forbidden -> Repos.Dependabot.Alerts.DependabotUpdateAlertResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.Dependabot.Alerts.DependabotUpdateAlertResult.NotFound(response.body())
            HttpStatusCode.Conflict -> Repos.Dependabot.Alerts.DependabotUpdateAlertResult.Conflict(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Dependabot.Alerts.DependabotUpdateAlertResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposDependabotSecrets(private val client: HttpClient) : Repos.Dependabot.Secrets {
    override val publicKey: Repos.Dependabot.Secrets.PublicKey = KtorReposDependabotSecretsPublicKey(client)

    override suspend fun dependabotListRepoSecrets(owner: String, repo: String, page: Long, perPage: Long): Repos.Dependabot.Secrets.DependabotListRepoSecretsResponse =
        client.get("/repos/$owner/$repo/dependabot/secrets") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()

    override suspend fun dependabotGetRepoSecret(owner: String, repo: String, secretName: String): DependabotSecret =
        client.get("/repos/$owner/$repo/dependabot/secrets/$secretName").body()

    override suspend fun dependabotCreateOrUpdateRepoSecret(owner: String, repo: String, secretName: String, body: Repos.Dependabot.Secrets.DependabotCreateOrUpdateRepoSecretBody): Repos.Dependabot.Secrets.DependabotCreateOrUpdateRepoSecretResult {
        val response = client.put("/repos/$owner/$repo/dependabot/secrets/$secretName") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> Repos.Dependabot.Secrets.DependabotCreateOrUpdateRepoSecretResult.Created(response.body())
            HttpStatusCode.NoContent -> Repos.Dependabot.Secrets.DependabotCreateOrUpdateRepoSecretResult.NoContent
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun dependabotDeleteRepoSecret(owner: String, repo: String, secretName: String): Unit =
        client.delete("/repos/$owner/$repo/dependabot/secrets/$secretName").body()
}

internal class KtorReposDependabotSecretsPublicKey(private val client: HttpClient) : Repos.Dependabot.Secrets.PublicKey {
    override suspend fun dependabotGetRepoPublicKey(owner: String, repo: String): DependabotPublicKey =
        client.get("/repos/$owner/$repo/dependabot/secrets/public-key").body()
}

internal class KtorReposDependencyGraph(private val client: HttpClient) : Repos.DependencyGraph {
    override val compare: Repos.DependencyGraph.CompareApi = KtorReposDependencyGraphCompareApi(client)

    override val sbom: Repos.DependencyGraph.Sbom = KtorReposDependencyGraphSbom(client)

    override val snapshots: Repos.DependencyGraph.Snapshots = KtorReposDependencyGraphSnapshots(client)
}

internal class KtorReposDependencyGraphCompareApi(private val client: HttpClient) : Repos.DependencyGraph.CompareApi {
    override suspend fun dependencyGraphDiffRange(owner: String, repo: String, basehead: String, name: String?): Repos.DependencyGraph.CompareApi.DependencyGraphDiffRangeResult {
        val response = client.get("/repos/$owner/$repo/dependency-graph/compare/$basehead") {
            name?.let { parameter("name", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.DependencyGraph.CompareApi.DependencyGraphDiffRangeResult.OK(response.body())
            HttpStatusCode.Forbidden -> Repos.DependencyGraph.CompareApi.DependencyGraphDiffRangeResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.DependencyGraph.CompareApi.DependencyGraphDiffRangeResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposDependencyGraphSbom(private val client: HttpClient) : Repos.DependencyGraph.Sbom {
    override suspend fun dependencyGraphExportSbom(owner: String, repo: String): Repos.DependencyGraph.Sbom.DependencyGraphExportSbomResult {
        val response = client.get("/repos/$owner/$repo/dependency-graph/sbom")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.DependencyGraph.Sbom.DependencyGraphExportSbomResult.OK(response.body())
            HttpStatusCode.Forbidden -> Repos.DependencyGraph.Sbom.DependencyGraphExportSbomResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.DependencyGraph.Sbom.DependencyGraphExportSbomResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposDependencyGraphSnapshots(private val client: HttpClient) : Repos.DependencyGraph.Snapshots {
    override suspend fun dependencyGraphCreateRepositorySnapshot(owner: String, repo: String, body: Snapshot): Repos.DependencyGraph.Snapshots.DependencyGraphCreateRepositorySnapshotResponse =
        client.post("/repos/$owner/$repo/dependency-graph/snapshots") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()
}

internal class KtorReposDeployments(private val client: HttpClient) : Repos.Deployments {
    override val statuses: Repos.Deployments.StatusesApi = KtorReposDeploymentsStatusesApi(client)

    override suspend fun reposListDeployments(owner: String, repo: String, environment: String, page: Long, perPage: Long, ref: String, sha: String, task: String): List<Deployment> =
        client.get("/repos/$owner/$repo/deployments") {
            parameter("environment", environment)
            parameter("page", page)
            parameter("per_page", perPage)
            parameter("ref", ref)
            parameter("sha", sha)
            parameter("task", task)
        }.body()

    override suspend fun reposCreateDeployment(owner: String, repo: String, body: Repos.Deployments.ReposCreateDeploymentBody): Repos.Deployments.ReposCreateDeploymentResult {
        val response = client.post("/repos/$owner/$repo/deployments") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> Repos.Deployments.ReposCreateDeploymentResult.Created(response.body())
            HttpStatusCode.Accepted -> Repos.Deployments.ReposCreateDeploymentResult.Accepted(response.body())
            HttpStatusCode.Conflict -> Repos.Deployments.ReposCreateDeploymentResult.Conflict
            HttpStatusCode.UnprocessableEntity -> Repos.Deployments.ReposCreateDeploymentResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposGetDeployment(owner: String, repo: String, deploymentId: Long): Repos.Deployments.ReposGetDeploymentResult {
        val response = client.get("/repos/$owner/$repo/deployments/$deploymentId")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Deployments.ReposGetDeploymentResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Deployments.ReposGetDeploymentResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposDeleteDeployment(owner: String, repo: String, deploymentId: Long): Repos.Deployments.ReposDeleteDeploymentResult {
        val response = client.delete("/repos/$owner/$repo/deployments/$deploymentId")
        return when (response.status) {
            HttpStatusCode.NoContent -> Repos.Deployments.ReposDeleteDeploymentResult.NoContent
            HttpStatusCode.NotFound -> Repos.Deployments.ReposDeleteDeploymentResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Deployments.ReposDeleteDeploymentResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposDeploymentsStatusesApi(private val client: HttpClient) : Repos.Deployments.StatusesApi {
    override suspend fun reposListDeploymentStatuses(owner: String, repo: String, deploymentId: Long, page: Long, perPage: Long): Repos.Deployments.StatusesApi.ReposListDeploymentStatusesResult {
        val response = client.get("/repos/$owner/$repo/deployments/$deploymentId/statuses") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Deployments.StatusesApi.ReposListDeploymentStatusesResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Deployments.StatusesApi.ReposListDeploymentStatusesResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposCreateDeploymentStatus(owner: String, repo: String, deploymentId: Long, body: Repos.Deployments.StatusesApi.ReposCreateDeploymentStatusBody): Repos.Deployments.StatusesApi.ReposCreateDeploymentStatusResult {
        val response = client.post("/repos/$owner/$repo/deployments/$deploymentId/statuses") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> Repos.Deployments.StatusesApi.ReposCreateDeploymentStatusResult.Created(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Deployments.StatusesApi.ReposCreateDeploymentStatusResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposGetDeploymentStatus(owner: String, repo: String, deploymentId: Long, statusId: Long): Repos.Deployments.StatusesApi.ReposGetDeploymentStatusResult {
        val response = client.get("/repos/$owner/$repo/deployments/$deploymentId/statuses/$statusId")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Deployments.StatusesApi.ReposGetDeploymentStatusResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Deployments.StatusesApi.ReposGetDeploymentStatusResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposDispatches(private val client: HttpClient) : Repos.Dispatches {
    override suspend fun reposCreateDispatchEvent(owner: String, repo: String, body: Repos.Dispatches.ReposCreateDispatchEventBody): Repos.Dispatches.ReposCreateDispatchEventResult {
        val response = client.post("/repos/$owner/$repo/dispatches") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.NoContent -> Repos.Dispatches.ReposCreateDispatchEventResult.NoContent
            HttpStatusCode.NotFound -> Repos.Dispatches.ReposCreateDispatchEventResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Dispatches.ReposCreateDispatchEventResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposEnvironments(private val client: HttpClient) : Repos.Environments {
    override val deploymentBranchPolicies: Repos.Environments.DeploymentBranchPolicies = KtorReposEnvironmentsDeploymentBranchPolicies(client)

    override val deploymentProtectionRules: Repos.Environments.DeploymentProtectionRules = KtorReposEnvironmentsDeploymentProtectionRules(client)

    override val secrets: Repos.Environments.Secrets = KtorReposEnvironmentsSecrets(client)

    override val variables: Repos.Environments.Variables = KtorReposEnvironmentsVariables(client)

    override suspend fun reposGetAllEnvironments(owner: String, repo: String, page: Long, perPage: Long): Repos.Environments.ReposGetAllEnvironmentsResponse =
        client.get("/repos/$owner/$repo/environments") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()

    override suspend fun reposGetEnvironment(owner: String, repo: String, environmentName: String): Environment =
        client.get("/repos/$owner/$repo/environments/$environmentName").body()

    override suspend fun reposCreateOrUpdateEnvironment(owner: String, repo: String, environmentName: String, body: Repos.Environments.ReposCreateOrUpdateEnvironmentBody?): Repos.Environments.ReposCreateOrUpdateEnvironmentResult {
        val response = client.put("/repos/$owner/$repo/environments/$environmentName") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Environments.ReposCreateOrUpdateEnvironmentResult.OK(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Environments.ReposCreateOrUpdateEnvironmentResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposDeleteAnEnvironment(owner: String, repo: String, environmentName: String): Unit =
        client.delete("/repos/$owner/$repo/environments/$environmentName").body()
}

internal class KtorReposEnvironmentsDeploymentBranchPolicies(private val client: HttpClient) : Repos.Environments.DeploymentBranchPolicies {
    override suspend fun reposListDeploymentBranchPolicies(owner: String, repo: String, environmentName: String, page: Long, perPage: Long): Repos.Environments.DeploymentBranchPolicies.ReposListDeploymentBranchPoliciesResponse =
        client.get("/repos/$owner/$repo/environments/$environmentName/deployment-branch-policies") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()

    override suspend fun reposCreateDeploymentBranchPolicy(owner: String, repo: String, environmentName: String, body: DeploymentBranchPolicyNamePatternWithType): Repos.Environments.DeploymentBranchPolicies.ReposCreateDeploymentBranchPolicyResult {
        val response = client.post("/repos/$owner/$repo/environments/$environmentName/deployment-branch-policies") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Environments.DeploymentBranchPolicies.ReposCreateDeploymentBranchPolicyResult.OK(response.body())
            HttpStatusCode.SeeOther -> Repos.Environments.DeploymentBranchPolicies.ReposCreateDeploymentBranchPolicyResult.SeeOther
            HttpStatusCode.NotFound -> Repos.Environments.DeploymentBranchPolicies.ReposCreateDeploymentBranchPolicyResult.NotFound
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposGetDeploymentBranchPolicy(owner: String, repo: String, environmentName: String, branchPolicyId: Long): DeploymentBranchPolicy =
        client.get("/repos/$owner/$repo/environments/$environmentName/deployment-branch-policies/$branchPolicyId").body()

    override suspend fun reposUpdateDeploymentBranchPolicy(owner: String, repo: String, environmentName: String, branchPolicyId: Long, body: DeploymentBranchPolicyNamePattern): DeploymentBranchPolicy =
        client.put("/repos/$owner/$repo/environments/$environmentName/deployment-branch-policies/$branchPolicyId") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()

    override suspend fun reposDeleteDeploymentBranchPolicy(owner: String, repo: String, environmentName: String, branchPolicyId: Long): Unit =
        client.delete("/repos/$owner/$repo/environments/$environmentName/deployment-branch-policies/$branchPolicyId").body()
}

internal class KtorReposEnvironmentsDeploymentProtectionRules(private val client: HttpClient) : Repos.Environments.DeploymentProtectionRules {
    override val apps: Repos.Environments.DeploymentProtectionRules.Apps = KtorReposEnvironmentsDeploymentProtectionRulesApps(client)

    override suspend fun reposGetAllDeploymentProtectionRules(owner: String, repo: String, environmentName: String): Repos.Environments.DeploymentProtectionRules.ReposGetAllDeploymentProtectionRulesResponse =
        client.get("/repos/$owner/$repo/environments/$environmentName/deployment_protection_rules").body()

    override suspend fun reposCreateDeploymentProtectionRule(owner: String, repo: String, environmentName: String, body: Repos.Environments.DeploymentProtectionRules.ReposCreateDeploymentProtectionRuleBody): DeploymentProtectionRule =
        client.post("/repos/$owner/$repo/environments/$environmentName/deployment_protection_rules") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()

    override suspend fun reposGetCustomDeploymentProtectionRule(owner: String, repo: String, environmentName: String, protectionRuleId: Long): DeploymentProtectionRule =
        client.get("/repos/$owner/$repo/environments/$environmentName/deployment_protection_rules/$protectionRuleId").body()

    override suspend fun reposDisableDeploymentProtectionRule(owner: String, repo: String, environmentName: String, protectionRuleId: Long): Unit =
        client.delete("/repos/$owner/$repo/environments/$environmentName/deployment_protection_rules/$protectionRuleId").body()
}

internal class KtorReposEnvironmentsDeploymentProtectionRulesApps(private val client: HttpClient) : Repos.Environments.DeploymentProtectionRules.Apps {
    override suspend fun reposListCustomDeploymentRuleIntegrations(owner: String, repo: String, environmentName: String, page: Long, perPage: Long): Repos.Environments.DeploymentProtectionRules.Apps.ReposListCustomDeploymentRuleIntegrationsResponse =
        client.get("/repos/$owner/$repo/environments/$environmentName/deployment_protection_rules/apps") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()
}

internal class KtorReposEnvironmentsSecrets(private val client: HttpClient) : Repos.Environments.Secrets {
    override val publicKey: Repos.Environments.Secrets.PublicKey = KtorReposEnvironmentsSecretsPublicKey(client)

    override suspend fun actionsListEnvironmentSecrets(owner: String, repo: String, environmentName: String, page: Long, perPage: Long): Repos.Environments.Secrets.ActionsListEnvironmentSecretsResponse =
        client.get("/repos/$owner/$repo/environments/$environmentName/secrets") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()

    override suspend fun actionsGetEnvironmentSecret(owner: String, repo: String, environmentName: String, secretName: String): ActionsSecret =
        client.get("/repos/$owner/$repo/environments/$environmentName/secrets/$secretName").body()

    override suspend fun actionsCreateOrUpdateEnvironmentSecret(owner: String, repo: String, environmentName: String, secretName: String, body: Repos.Environments.Secrets.ActionsCreateOrUpdateEnvironmentSecretBody): Repos.Environments.Secrets.ActionsCreateOrUpdateEnvironmentSecretResult {
        val response = client.put("/repos/$owner/$repo/environments/$environmentName/secrets/$secretName") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> Repos.Environments.Secrets.ActionsCreateOrUpdateEnvironmentSecretResult.Created(response.body())
            HttpStatusCode.NoContent -> Repos.Environments.Secrets.ActionsCreateOrUpdateEnvironmentSecretResult.NoContent
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun actionsDeleteEnvironmentSecret(owner: String, repo: String, environmentName: String, secretName: String): Unit =
        client.delete("/repos/$owner/$repo/environments/$environmentName/secrets/$secretName").body()
}

internal class KtorReposEnvironmentsSecretsPublicKey(private val client: HttpClient) : Repos.Environments.Secrets.PublicKey {
    override suspend fun actionsGetEnvironmentPublicKey(owner: String, repo: String, environmentName: String): ActionsPublicKey =
        client.get("/repos/$owner/$repo/environments/$environmentName/secrets/public-key").body()
}

internal class KtorReposEnvironmentsVariables(private val client: HttpClient) : Repos.Environments.Variables {
    override suspend fun actionsListEnvironmentVariables(owner: String, repo: String, environmentName: String, page: Long, perPage: Long): Repos.Environments.Variables.ActionsListEnvironmentVariablesResponse =
        client.get("/repos/$owner/$repo/environments/$environmentName/variables") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()

    override suspend fun actionsCreateEnvironmentVariable(owner: String, repo: String, environmentName: String, body: Repos.Environments.Variables.ActionsCreateEnvironmentVariableBody): EmptyObject =
        client.post("/repos/$owner/$repo/environments/$environmentName/variables") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()

    override suspend fun actionsGetEnvironmentVariable(owner: String, repo: String, environmentName: String, name: String): ActionsVariable =
        client.get("/repos/$owner/$repo/environments/$environmentName/variables/$name").body()

    override suspend fun actionsDeleteEnvironmentVariable(owner: String, repo: String, environmentName: String, name: String): Unit =
        client.delete("/repos/$owner/$repo/environments/$environmentName/variables/$name").body()

    override suspend fun actionsUpdateEnvironmentVariable(owner: String, repo: String, environmentName: String, name: String, body: Repos.Environments.Variables.ActionsUpdateEnvironmentVariableBody): Unit =
        client.patch("/repos/$owner/$repo/environments/$environmentName/variables/$name") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()
}

internal class KtorReposEvents(private val client: HttpClient) : Repos.Events {
    override suspend fun activityListRepoEvents(owner: String, repo: String, page: Long, perPage: Long): List<Event> =
        client.get("/repos/$owner/$repo/events") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()
}

internal class KtorReposForks(private val client: HttpClient) : Repos.Forks {
    override suspend fun reposListForks(owner: String, repo: String, page: Long, perPage: Long, sort: Repos.Forks.Sort): Repos.Forks.ReposListForksResult {
        val response = client.get("/repos/$owner/$repo/forks") {
            parameter("page", page)
            parameter("per_page", perPage)
            parameter("sort", sort)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Forks.ReposListForksResult.OK(response.body())
            HttpStatusCode.BadRequest -> Repos.Forks.ReposListForksResult.BadRequest(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposCreateFork(owner: String, repo: String, body: Repos.Forks.ReposCreateForkBody?): Repos.Forks.ReposCreateForkResult {
        val response = client.post("/repos/$owner/$repo/forks") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }
        return when (response.status) {
            HttpStatusCode.Accepted -> Repos.Forks.ReposCreateForkResult.Accepted(response.body())
            HttpStatusCode.BadRequest -> Repos.Forks.ReposCreateForkResult.BadRequest(response.body())
            HttpStatusCode.Forbidden -> Repos.Forks.ReposCreateForkResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.Forks.ReposCreateForkResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Forks.ReposCreateForkResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposGit(private val client: HttpClient) : Repos.Git {
    override val blobs: Repos.Git.Blobs = KtorReposGitBlobs(client)

    override val commits: Repos.Git.CommitsApi = KtorReposGitCommitsApi(client)

    override val matchingRefs: Repos.Git.MatchingRefs = KtorReposGitMatchingRefs(client)

    override val ref: Repos.Git.Ref = KtorReposGitRef(client)

    override val refs: Repos.Git.Refs = KtorReposGitRefs(client)

    override val tags: Repos.Git.TagsApi = KtorReposGitTagsApi(client)

    override val trees: Repos.Git.Trees = KtorReposGitTrees(client)
}

internal class KtorReposGitBlobs(private val client: HttpClient) : Repos.Git.Blobs {
    override suspend fun gitCreateBlob(owner: String, repo: String, body: Repos.Git.Blobs.GitCreateBlobBody): Repos.Git.Blobs.GitCreateBlobResult {
        val response = client.post("/repos/$owner/$repo/git/blobs") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> Repos.Git.Blobs.GitCreateBlobResult.Created(response.body())
            HttpStatusCode.Forbidden -> Repos.Git.Blobs.GitCreateBlobResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.Git.Blobs.GitCreateBlobResult.NotFound(response.body())
            HttpStatusCode.Conflict -> Repos.Git.Blobs.GitCreateBlobResult.Conflict(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Git.Blobs.GitCreateBlobResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun gitGetBlob(owner: String, repo: String, fileSha: String): Repos.Git.Blobs.GitGetBlobResult {
        val response = client.get("/repos/$owner/$repo/git/blobs/$fileSha")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Git.Blobs.GitGetBlobResult.OK(response.body())
            HttpStatusCode.Forbidden -> Repos.Git.Blobs.GitGetBlobResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.Git.Blobs.GitGetBlobResult.NotFound(response.body())
            HttpStatusCode.Conflict -> Repos.Git.Blobs.GitGetBlobResult.Conflict(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Git.Blobs.GitGetBlobResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposGitCommitsApi(private val client: HttpClient) : Repos.Git.CommitsApi {
    override suspend fun gitCreateCommit(owner: String, repo: String, body: Repos.Git.CommitsApi.GitCreateCommitBody): Repos.Git.CommitsApi.GitCreateCommitResult {
        val response = client.post("/repos/$owner/$repo/git/commits") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> Repos.Git.CommitsApi.GitCreateCommitResult.Created(response.body())
            HttpStatusCode.NotFound -> Repos.Git.CommitsApi.GitCreateCommitResult.NotFound(response.body())
            HttpStatusCode.Conflict -> Repos.Git.CommitsApi.GitCreateCommitResult.Conflict(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Git.CommitsApi.GitCreateCommitResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun gitGetCommit(owner: String, repo: String, commitSha: String): Repos.Git.CommitsApi.GitGetCommitResult {
        val response = client.get("/repos/$owner/$repo/git/commits/$commitSha")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Git.CommitsApi.GitGetCommitResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Git.CommitsApi.GitGetCommitResult.NotFound(response.body())
            HttpStatusCode.Conflict -> Repos.Git.CommitsApi.GitGetCommitResult.Conflict(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposGitMatchingRefs(private val client: HttpClient) : Repos.Git.MatchingRefs {
    override suspend fun gitListMatchingRefs(owner: String, repo: String, ref: String): Repos.Git.MatchingRefs.GitListMatchingRefsResult {
        val response = client.get("/repos/$owner/$repo/git/matching-refs/$ref")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Git.MatchingRefs.GitListMatchingRefsResult.OK(response.body())
            HttpStatusCode.Conflict -> Repos.Git.MatchingRefs.GitListMatchingRefsResult.Conflict(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposGitRef(private val client: HttpClient) : Repos.Git.Ref {
    override suspend fun gitGetRef(owner: String, repo: String, ref: String): Repos.Git.Ref.GitGetRefResult {
        val response = client.get("/repos/$owner/$repo/git/ref/$ref")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Git.Ref.GitGetRefResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Git.Ref.GitGetRefResult.NotFound(response.body())
            HttpStatusCode.Conflict -> Repos.Git.Ref.GitGetRefResult.Conflict(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposGitRefs(private val client: HttpClient) : Repos.Git.Refs {
    override suspend fun gitCreateRef(owner: String, repo: String, body: Repos.Git.Refs.GitCreateRefBody): Repos.Git.Refs.GitCreateRefResult {
        val response = client.post("/repos/$owner/$repo/git/refs") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> Repos.Git.Refs.GitCreateRefResult.Created(response.body())
            HttpStatusCode.Conflict -> Repos.Git.Refs.GitCreateRefResult.Conflict(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Git.Refs.GitCreateRefResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun gitDeleteRef(owner: String, repo: String, ref: String): Repos.Git.Refs.GitDeleteRefResult {
        val response = client.delete("/repos/$owner/$repo/git/refs/$ref")
        return when (response.status) {
            HttpStatusCode.NoContent -> Repos.Git.Refs.GitDeleteRefResult.NoContent
            HttpStatusCode.Conflict -> Repos.Git.Refs.GitDeleteRefResult.Conflict(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Git.Refs.GitDeleteRefResult.UnprocessableEntity
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun gitUpdateRef(owner: String, repo: String, ref: String, body: Repos.Git.Refs.GitUpdateRefBody): Repos.Git.Refs.GitUpdateRefResult {
        val response = client.patch("/repos/$owner/$repo/git/refs/$ref") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Git.Refs.GitUpdateRefResult.OK(response.body())
            HttpStatusCode.Conflict -> Repos.Git.Refs.GitUpdateRefResult.Conflict(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Git.Refs.GitUpdateRefResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposGitTagsApi(private val client: HttpClient) : Repos.Git.TagsApi {
    override suspend fun gitCreateTag(owner: String, repo: String, body: Repos.Git.TagsApi.GitCreateTagBody): Repos.Git.TagsApi.GitCreateTagResult {
        val response = client.post("/repos/$owner/$repo/git/tags") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> Repos.Git.TagsApi.GitCreateTagResult.Created(response.body())
            HttpStatusCode.Conflict -> Repos.Git.TagsApi.GitCreateTagResult.Conflict(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Git.TagsApi.GitCreateTagResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun gitGetTag(owner: String, repo: String, tagSha: String): Repos.Git.TagsApi.GitGetTagResult {
        val response = client.get("/repos/$owner/$repo/git/tags/$tagSha")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Git.TagsApi.GitGetTagResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Git.TagsApi.GitGetTagResult.NotFound(response.body())
            HttpStatusCode.Conflict -> Repos.Git.TagsApi.GitGetTagResult.Conflict(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposGitTrees(private val client: HttpClient) : Repos.Git.Trees {
    override suspend fun gitCreateTree(owner: String, repo: String, body: Repos.Git.Trees.GitCreateTreeBody): Repos.Git.Trees.GitCreateTreeResult {
        val response = client.post("/repos/$owner/$repo/git/trees") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> Repos.Git.Trees.GitCreateTreeResult.Created(response.body())
            HttpStatusCode.Forbidden -> Repos.Git.Trees.GitCreateTreeResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.Git.Trees.GitCreateTreeResult.NotFound(response.body())
            HttpStatusCode.Conflict -> Repos.Git.Trees.GitCreateTreeResult.Conflict(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Git.Trees.GitCreateTreeResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun gitGetTree(owner: String, repo: String, treeSha: String, recursive: String?): Repos.Git.Trees.GitGetTreeResult {
        val response = client.get("/repos/$owner/$repo/git/trees/$treeSha") {
            recursive?.let { parameter("recursive", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Git.Trees.GitGetTreeResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Git.Trees.GitGetTreeResult.NotFound(response.body())
            HttpStatusCode.Conflict -> Repos.Git.Trees.GitGetTreeResult.Conflict(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Git.Trees.GitGetTreeResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposHooks(private val client: HttpClient) : Repos.Hooks {
    override val config: Repos.Hooks.Config = KtorReposHooksConfig(client)

    override val deliveries: Repos.Hooks.Deliveries = KtorReposHooksDeliveries(client)

    override val pings: Repos.Hooks.Pings = KtorReposHooksPings(client)

    override val tests: Repos.Hooks.Tests = KtorReposHooksTests(client)

    override suspend fun reposListWebhooks(owner: String, repo: String, page: Long, perPage: Long): Repos.Hooks.ReposListWebhooksResult {
        val response = client.get("/repos/$owner/$repo/hooks") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Hooks.ReposListWebhooksResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Hooks.ReposListWebhooksResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposCreateWebhook(owner: String, repo: String, body: Repos.Hooks.ReposCreateWebhookBody?): Repos.Hooks.ReposCreateWebhookResult {
        val response = client.post("/repos/$owner/$repo/hooks") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }
        return when (response.status) {
            HttpStatusCode.Created -> Repos.Hooks.ReposCreateWebhookResult.Created(response.body())
            HttpStatusCode.Forbidden -> Repos.Hooks.ReposCreateWebhookResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.Hooks.ReposCreateWebhookResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Hooks.ReposCreateWebhookResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposGetWebhook(owner: String, repo: String, hookId: Long): Repos.Hooks.ReposGetWebhookResult {
        val response = client.get("/repos/$owner/$repo/hooks/$hookId")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Hooks.ReposGetWebhookResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Hooks.ReposGetWebhookResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposDeleteWebhook(owner: String, repo: String, hookId: Long): Repos.Hooks.ReposDeleteWebhookResult {
        val response = client.delete("/repos/$owner/$repo/hooks/$hookId")
        return when (response.status) {
            HttpStatusCode.NoContent -> Repos.Hooks.ReposDeleteWebhookResult.NoContent
            HttpStatusCode.NotFound -> Repos.Hooks.ReposDeleteWebhookResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposUpdateWebhook(owner: String, repo: String, hookId: Long, body: Repos.Hooks.ReposUpdateWebhookBody): Repos.Hooks.ReposUpdateWebhookResult {
        val response = client.patch("/repos/$owner/$repo/hooks/$hookId") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Hooks.ReposUpdateWebhookResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Hooks.ReposUpdateWebhookResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Hooks.ReposUpdateWebhookResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposHooksConfig(private val client: HttpClient) : Repos.Hooks.Config {
    override suspend fun reposGetWebhookConfigForRepo(owner: String, repo: String, hookId: Long): WebhookConfig =
        client.get("/repos/$owner/$repo/hooks/$hookId/config").body()

    override suspend fun reposUpdateWebhookConfigForRepo(owner: String, repo: String, hookId: Long, body: Repos.Hooks.Config.ReposUpdateWebhookConfigForRepoBody?): WebhookConfig =
        client.patch("/repos/$owner/$repo/hooks/$hookId/config") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }.body()
}

internal class KtorReposHooksDeliveries(private val client: HttpClient) : Repos.Hooks.Deliveries {
    override val attempts: Repos.Hooks.Deliveries.Attempts = KtorReposHooksDeliveriesAttempts(client)

    override suspend fun reposListWebhookDeliveries(owner: String, repo: String, hookId: Long, perPage: Long, cursor: String?): Repos.Hooks.Deliveries.ReposListWebhookDeliveriesResult {
        val response = client.get("/repos/$owner/$repo/hooks/$hookId/deliveries") {
            parameter("per_page", perPage)
            cursor?.let { parameter("cursor", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Hooks.Deliveries.ReposListWebhookDeliveriesResult.OK(response.body())
            HttpStatusCode.BadRequest -> Repos.Hooks.Deliveries.ReposListWebhookDeliveriesResult.BadRequest(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Hooks.Deliveries.ReposListWebhookDeliveriesResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposGetWebhookDelivery(owner: String, repo: String, hookId: Long, deliveryId: Long): Repos.Hooks.Deliveries.ReposGetWebhookDeliveryResult {
        val response = client.get("/repos/$owner/$repo/hooks/$hookId/deliveries/$deliveryId")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Hooks.Deliveries.ReposGetWebhookDeliveryResult.OK(response.body())
            HttpStatusCode.BadRequest -> Repos.Hooks.Deliveries.ReposGetWebhookDeliveryResult.BadRequest(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Hooks.Deliveries.ReposGetWebhookDeliveryResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposHooksDeliveriesAttempts(private val client: HttpClient) : Repos.Hooks.Deliveries.Attempts {
    override suspend fun reposRedeliverWebhookDelivery(owner: String, repo: String, hookId: Long, deliveryId: Long): Repos.Hooks.Deliveries.Attempts.ReposRedeliverWebhookDeliveryResult {
        val response = client.post("/repos/$owner/$repo/hooks/$hookId/deliveries/$deliveryId/attempts")
        return when (response.status) {
            HttpStatusCode.Accepted -> Repos.Hooks.Deliveries.Attempts.ReposRedeliverWebhookDeliveryResult.Accepted(response.body())
            HttpStatusCode.BadRequest -> Repos.Hooks.Deliveries.Attempts.ReposRedeliverWebhookDeliveryResult.BadRequest(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Hooks.Deliveries.Attempts.ReposRedeliverWebhookDeliveryResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposHooksPings(private val client: HttpClient) : Repos.Hooks.Pings {
    override suspend fun reposPingWebhook(owner: String, repo: String, hookId: Long): Repos.Hooks.Pings.ReposPingWebhookResult {
        val response = client.post("/repos/$owner/$repo/hooks/$hookId/pings")
        return when (response.status) {
            HttpStatusCode.NoContent -> Repos.Hooks.Pings.ReposPingWebhookResult.NoContent
            HttpStatusCode.NotFound -> Repos.Hooks.Pings.ReposPingWebhookResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposHooksTests(private val client: HttpClient) : Repos.Hooks.Tests {
    override suspend fun reposTestPushWebhook(owner: String, repo: String, hookId: Long): Repos.Hooks.Tests.ReposTestPushWebhookResult {
        val response = client.post("/repos/$owner/$repo/hooks/$hookId/tests")
        return when (response.status) {
            HttpStatusCode.NoContent -> Repos.Hooks.Tests.ReposTestPushWebhookResult.NoContent
            HttpStatusCode.NotFound -> Repos.Hooks.Tests.ReposTestPushWebhookResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposImmutableReleases(private val client: HttpClient) : Repos.ImmutableReleases {
    override suspend fun reposCheckImmutableReleases(owner: String, repo: String): Repos.ImmutableReleases.ReposCheckImmutableReleasesResult {
        val response = client.get("/repos/$owner/$repo/immutable-releases")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.ImmutableReleases.ReposCheckImmutableReleasesResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.ImmutableReleases.ReposCheckImmutableReleasesResult.NotFound
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposEnableImmutableReleases(owner: String, repo: String): Repos.ImmutableReleases.ReposEnableImmutableReleasesResult {
        val response = client.put("/repos/$owner/$repo/immutable-releases")
        return when (response.status) {
            HttpStatusCode.NoContent -> Repos.ImmutableReleases.ReposEnableImmutableReleasesResult.NoContent
            HttpStatusCode.Conflict -> Repos.ImmutableReleases.ReposEnableImmutableReleasesResult.Conflict(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposDisableImmutableReleases(owner: String, repo: String): Repos.ImmutableReleases.ReposDisableImmutableReleasesResult {
        val response = client.delete("/repos/$owner/$repo/immutable-releases")
        return when (response.status) {
            HttpStatusCode.NoContent -> Repos.ImmutableReleases.ReposDisableImmutableReleasesResult.NoContent
            HttpStatusCode.Conflict -> Repos.ImmutableReleases.ReposDisableImmutableReleasesResult.Conflict(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposImport(private val client: HttpClient) : Repos.Import {
    override val authors: Repos.Import.Authors = KtorReposImportAuthors(client)

    override val largeFiles: Repos.Import.LargeFiles = KtorReposImportLargeFiles(client)

    override val lfs: Repos.Import.Lfs = KtorReposImportLfs(client)

    @Deprecated("Deprecated by the API provider")
    override suspend fun migrationsGetImportStatus(owner: String, repo: String): Repos.Import.MigrationsGetImportStatusResult {
        val response = client.get("/repos/$owner/$repo/import")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Import.MigrationsGetImportStatusResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Import.MigrationsGetImportStatusResult.NotFound(response.body())
            HttpStatusCode.ServiceUnavailable -> Repos.Import.MigrationsGetImportStatusResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    @Deprecated("Deprecated by the API provider")
    override suspend fun migrationsStartImport(owner: String, repo: String, body: Repos.Import.MigrationsStartImportBody): Repos.Import.MigrationsStartImportResult {
        val response = client.put("/repos/$owner/$repo/import") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> Repos.Import.MigrationsStartImportResult.Created(response.body())
            HttpStatusCode.NotFound -> Repos.Import.MigrationsStartImportResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Import.MigrationsStartImportResult.UnprocessableEntity(response.body())
            HttpStatusCode.ServiceUnavailable -> Repos.Import.MigrationsStartImportResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    @Deprecated("Deprecated by the API provider")
    override suspend fun migrationsCancelImport(owner: String, repo: String): Repos.Import.MigrationsCancelImportResult {
        val response = client.delete("/repos/$owner/$repo/import")
        return when (response.status) {
            HttpStatusCode.NoContent -> Repos.Import.MigrationsCancelImportResult.NoContent
            HttpStatusCode.ServiceUnavailable -> Repos.Import.MigrationsCancelImportResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    @Deprecated("Deprecated by the API provider")
    override suspend fun migrationsUpdateImport(owner: String, repo: String, body: Repos.Import.MigrationsUpdateImportBody?): Repos.Import.MigrationsUpdateImportResult {
        val response = client.patch("/repos/$owner/$repo/import") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Import.MigrationsUpdateImportResult.OK(response.body())
            HttpStatusCode.ServiceUnavailable -> Repos.Import.MigrationsUpdateImportResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposImportAuthors(private val client: HttpClient) : Repos.Import.Authors {
    @Deprecated("Deprecated by the API provider")
    override suspend fun migrationsGetCommitAuthors(owner: String, repo: String, since: Long?): Repos.Import.Authors.MigrationsGetCommitAuthorsResult {
        val response = client.get("/repos/$owner/$repo/import/authors") {
            since?.let { parameter("since", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Import.Authors.MigrationsGetCommitAuthorsResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Import.Authors.MigrationsGetCommitAuthorsResult.NotFound(response.body())
            HttpStatusCode.ServiceUnavailable -> Repos.Import.Authors.MigrationsGetCommitAuthorsResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    @Deprecated("Deprecated by the API provider")
    override suspend fun migrationsMapCommitAuthor(owner: String, repo: String, authorId: Long, body: Repos.Import.Authors.MigrationsMapCommitAuthorBody?): Repos.Import.Authors.MigrationsMapCommitAuthorResult {
        val response = client.patch("/repos/$owner/$repo/import/authors/$authorId") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Import.Authors.MigrationsMapCommitAuthorResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Import.Authors.MigrationsMapCommitAuthorResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Import.Authors.MigrationsMapCommitAuthorResult.UnprocessableEntity(response.body())
            HttpStatusCode.ServiceUnavailable -> Repos.Import.Authors.MigrationsMapCommitAuthorResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposImportLargeFiles(private val client: HttpClient) : Repos.Import.LargeFiles {
    @Deprecated("Deprecated by the API provider")
    override suspend fun migrationsGetLargeFiles(owner: String, repo: String): Repos.Import.LargeFiles.MigrationsGetLargeFilesResult {
        val response = client.get("/repos/$owner/$repo/import/large_files")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Import.LargeFiles.MigrationsGetLargeFilesResult.OK(response.body())
            HttpStatusCode.ServiceUnavailable -> Repos.Import.LargeFiles.MigrationsGetLargeFilesResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposImportLfs(private val client: HttpClient) : Repos.Import.Lfs {
    @Deprecated("Deprecated by the API provider")
    override suspend fun migrationsSetLfsPreference(owner: String, repo: String, body: Repos.Import.Lfs.MigrationsSetLfsPreferenceBody): Repos.Import.Lfs.MigrationsSetLfsPreferenceResult {
        val response = client.patch("/repos/$owner/$repo/import/lfs") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Import.Lfs.MigrationsSetLfsPreferenceResult.OK(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Import.Lfs.MigrationsSetLfsPreferenceResult.UnprocessableEntity(response.body())
            HttpStatusCode.ServiceUnavailable -> Repos.Import.Lfs.MigrationsSetLfsPreferenceResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposInstallation(private val client: HttpClient) : Repos.Installation {
    override suspend fun appsGetRepoInstallation(owner: String, repo: String): Repos.Installation.AppsGetRepoInstallationResult {
        val response = client.get("/repos/$owner/$repo/installation")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Installation.AppsGetRepoInstallationResult.OK(response.body())
            HttpStatusCode.MovedPermanently -> Repos.Installation.AppsGetRepoInstallationResult.MovedPermanently(response.body())
            HttpStatusCode.NotFound -> Repos.Installation.AppsGetRepoInstallationResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposInteractionLimits(private val client: HttpClient) : Repos.InteractionLimits {
    override suspend fun interactionsGetRestrictionsForRepo(owner: String, repo: String): Repos.InteractionLimits.InteractionsGetRestrictionsForRepoResponse =
        client.get("/repos/$owner/$repo/interaction-limits").body()

    override suspend fun interactionsSetRestrictionsForRepo(owner: String, repo: String, body: InteractionLimit): Repos.InteractionLimits.InteractionsSetRestrictionsForRepoResult {
        val response = client.put("/repos/$owner/$repo/interaction-limits") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.InteractionLimits.InteractionsSetRestrictionsForRepoResult.OK(response.body())
            HttpStatusCode.Conflict -> Repos.InteractionLimits.InteractionsSetRestrictionsForRepoResult.Conflict
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun interactionsRemoveRestrictionsForRepo(owner: String, repo: String): Repos.InteractionLimits.InteractionsRemoveRestrictionsForRepoResult {
        val response = client.delete("/repos/$owner/$repo/interaction-limits")
        return when (response.status) {
            HttpStatusCode.NoContent -> Repos.InteractionLimits.InteractionsRemoveRestrictionsForRepoResult.NoContent
            HttpStatusCode.Conflict -> Repos.InteractionLimits.InteractionsRemoveRestrictionsForRepoResult.Conflict
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposInvitations(private val client: HttpClient) : Repos.Invitations {
    override suspend fun reposListInvitations(owner: String, repo: String, page: Long, perPage: Long): List<RepositoryInvitation> =
        client.get("/repos/$owner/$repo/invitations") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()

    override suspend fun reposDeleteInvitation(owner: String, repo: String, invitationId: Long): Unit =
        client.delete("/repos/$owner/$repo/invitations/$invitationId").body()

    override suspend fun reposUpdateInvitation(owner: String, repo: String, invitationId: Long, body: Repos.Invitations.ReposUpdateInvitationBody?): RepositoryInvitation =
        client.patch("/repos/$owner/$repo/invitations/$invitationId") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }.body()
}

internal class KtorReposIssues(private val client: HttpClient) : Repos.Issues {
    override val comments: Repos.Issues.CommentsApi = KtorReposIssuesCommentsApi(client)

    override val events: Repos.Issues.EventsApi = KtorReposIssuesEventsApi(client)

    override val assignees: Repos.Issues.AssigneesApi = KtorReposIssuesAssigneesApi(client)

    override val dependencies: Repos.Issues.Dependencies = KtorReposIssuesDependencies(client)

    override val issueFieldValues: Repos.Issues.IssueFieldValues = KtorReposIssuesIssueFieldValues(client)

    override val labels: Repos.Issues.LabelsApi = KtorReposIssuesLabelsApi(client)

    override val lock: Repos.Issues.Lock = KtorReposIssuesLock(client)

    override val parent: Repos.Issues.Parent = KtorReposIssuesParent(client)

    override val reactions: Repos.Issues.Reactions = KtorReposIssuesReactions(client)

    override val subIssue: Repos.Issues.SubIssue = KtorReposIssuesSubIssue(client)

    override val subIssues: Repos.Issues.SubIssues = KtorReposIssuesSubIssues(client)

    override val timeline: Repos.Issues.Timeline = KtorReposIssuesTimeline(client)

    override suspend fun issuesListForRepo(owner: String, repo: String, direction: Repos.Issues.Direction, page: Long, perPage: Long, sort: Repos.Issues.Sort, state: Repos.Issues.State, assignee: String?, creator: String?, labels: String?, mentioned: String?, milestone: String?, since: LocalDateTime?, type: String?): Repos.Issues.IssuesListForRepoResult {
        val response = client.get("/repos/$owner/$repo/issues") {
            parameter("direction", direction)
            parameter("page", page)
            parameter("per_page", perPage)
            parameter("sort", sort)
            parameter("state", state)
            assignee?.let { parameter("assignee", it) }
            creator?.let { parameter("creator", it) }
            labels?.let { parameter("labels", it) }
            mentioned?.let { parameter("mentioned", it) }
            milestone?.let { parameter("milestone", it) }
            since?.let { parameter("since", it) }
            type?.let { parameter("type", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Issues.IssuesListForRepoResult.OK(response.body())
            HttpStatusCode.MovedPermanently -> Repos.Issues.IssuesListForRepoResult.MovedPermanently(response.body())
            HttpStatusCode.NotFound -> Repos.Issues.IssuesListForRepoResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Issues.IssuesListForRepoResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun issuesCreate(owner: String, repo: String, body: Repos.Issues.IssuesCreateBody): Repos.Issues.IssuesCreateResult {
        val response = client.post("/repos/$owner/$repo/issues") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> Repos.Issues.IssuesCreateResult.Created(response.body())
            HttpStatusCode.BadRequest -> Repos.Issues.IssuesCreateResult.BadRequest(response.body())
            HttpStatusCode.Forbidden -> Repos.Issues.IssuesCreateResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.Issues.IssuesCreateResult.NotFound(response.body())
            HttpStatusCode.Gone -> Repos.Issues.IssuesCreateResult.Gone(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Issues.IssuesCreateResult.UnprocessableEntity(response.body())
            HttpStatusCode.ServiceUnavailable -> Repos.Issues.IssuesCreateResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun issuesGet(owner: String, repo: String, issueNumber: Long): Repos.Issues.IssuesGetResult {
        val response = client.get("/repos/$owner/$repo/issues/$issueNumber")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Issues.IssuesGetResult.OK(response.body())
            HttpStatusCode.MovedPermanently -> Repos.Issues.IssuesGetResult.MovedPermanently(response.body())
            HttpStatusCode.NotModified -> Repos.Issues.IssuesGetResult.NotModified
            HttpStatusCode.NotFound -> Repos.Issues.IssuesGetResult.NotFound(response.body())
            HttpStatusCode.Gone -> Repos.Issues.IssuesGetResult.Gone(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun issuesUpdate(owner: String, repo: String, issueNumber: Long, body: Repos.Issues.IssuesUpdateBody?): Repos.Issues.IssuesUpdateResult {
        val response = client.patch("/repos/$owner/$repo/issues/$issueNumber") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Issues.IssuesUpdateResult.OK(response.body())
            HttpStatusCode.MovedPermanently -> Repos.Issues.IssuesUpdateResult.MovedPermanently(response.body())
            HttpStatusCode.Forbidden -> Repos.Issues.IssuesUpdateResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.Issues.IssuesUpdateResult.NotFound(response.body())
            HttpStatusCode.Gone -> Repos.Issues.IssuesUpdateResult.Gone(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Issues.IssuesUpdateResult.UnprocessableEntity(response.body())
            HttpStatusCode.ServiceUnavailable -> Repos.Issues.IssuesUpdateResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposIssuesCommentsApi(private val client: HttpClient) : Repos.Issues.CommentsApi {
    override val pin: Repos.Issues.CommentsApi.Pin = KtorReposIssuesCommentsApiPin(client)

    override val reactions: Repos.Issues.CommentsApi.ReactionsApi = KtorReposIssuesCommentsApiReactionsApi(client)

    override suspend fun issuesListCommentsForRepo(owner: String, repo: String, page: Long, perPage: Long, sort: Repos.Issues.CommentsApi.IssuesListCommentsForRepoSort, direction: Repos.Issues.CommentsApi.IssuesListCommentsForRepoDirection?, since: LocalDateTime?): Repos.Issues.CommentsApi.IssuesListCommentsForRepoResult {
        val response = client.get("/repos/$owner/$repo/issues/comments") {
            parameter("page", page)
            parameter("per_page", perPage)
            parameter("sort", sort)
            direction?.let { parameter("direction", it) }
            since?.let { parameter("since", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Issues.CommentsApi.IssuesListCommentsForRepoResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Issues.CommentsApi.IssuesListCommentsForRepoResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Issues.CommentsApi.IssuesListCommentsForRepoResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun issuesGetComment(owner: String, repo: String, commentId: Long): Repos.Issues.CommentsApi.IssuesGetCommentResult {
        val response = client.get("/repos/$owner/$repo/issues/comments/$commentId")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Issues.CommentsApi.IssuesGetCommentResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Issues.CommentsApi.IssuesGetCommentResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun issuesDeleteComment(owner: String, repo: String, commentId: Long): Unit =
        client.delete("/repos/$owner/$repo/issues/comments/$commentId").body()

    override suspend fun issuesUpdateComment(owner: String, repo: String, commentId: Long, body: Repos.Issues.CommentsApi.IssuesUpdateCommentBody): Repos.Issues.CommentsApi.IssuesUpdateCommentResult {
        val response = client.patch("/repos/$owner/$repo/issues/comments/$commentId") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Issues.CommentsApi.IssuesUpdateCommentResult.OK(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Issues.CommentsApi.IssuesUpdateCommentResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun issuesListComments(owner: String, repo: String, issueNumber: Long, page: Long, perPage: Long, since: LocalDateTime?): Repos.Issues.CommentsApi.IssuesListCommentsResult {
        val response = client.get("/repos/$owner/$repo/issues/$issueNumber/comments") {
            parameter("page", page)
            parameter("per_page", perPage)
            since?.let { parameter("since", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Issues.CommentsApi.IssuesListCommentsResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Issues.CommentsApi.IssuesListCommentsResult.NotFound(response.body())
            HttpStatusCode.Gone -> Repos.Issues.CommentsApi.IssuesListCommentsResult.Gone(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun issuesCreateComment(owner: String, repo: String, issueNumber: Long, body: Repos.Issues.CommentsApi.IssuesCreateCommentBody): Repos.Issues.CommentsApi.IssuesCreateCommentResult {
        val response = client.post("/repos/$owner/$repo/issues/$issueNumber/comments") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> Repos.Issues.CommentsApi.IssuesCreateCommentResult.Created(response.body())
            HttpStatusCode.Forbidden -> Repos.Issues.CommentsApi.IssuesCreateCommentResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.Issues.CommentsApi.IssuesCreateCommentResult.NotFound(response.body())
            HttpStatusCode.Gone -> Repos.Issues.CommentsApi.IssuesCreateCommentResult.Gone(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Issues.CommentsApi.IssuesCreateCommentResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposIssuesCommentsApiPin(private val client: HttpClient) : Repos.Issues.CommentsApi.Pin {
    override suspend fun issuesPinComment(owner: String, repo: String, commentId: Long): Repos.Issues.CommentsApi.Pin.IssuesPinCommentResult {
        val response = client.put("/repos/$owner/$repo/issues/comments/$commentId/pin")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Issues.CommentsApi.Pin.IssuesPinCommentResult.OK(response.body())
            HttpStatusCode.Unauthorized -> Repos.Issues.CommentsApi.Pin.IssuesPinCommentResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Repos.Issues.CommentsApi.Pin.IssuesPinCommentResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.Issues.CommentsApi.Pin.IssuesPinCommentResult.NotFound(response.body())
            HttpStatusCode.Gone -> Repos.Issues.CommentsApi.Pin.IssuesPinCommentResult.Gone(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Issues.CommentsApi.Pin.IssuesPinCommentResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun issuesUnpinComment(owner: String, repo: String, commentId: Long): Repos.Issues.CommentsApi.Pin.IssuesUnpinCommentResult {
        val response = client.delete("/repos/$owner/$repo/issues/comments/$commentId/pin")
        return when (response.status) {
            HttpStatusCode.NoContent -> Repos.Issues.CommentsApi.Pin.IssuesUnpinCommentResult.NoContent
            HttpStatusCode.Unauthorized -> Repos.Issues.CommentsApi.Pin.IssuesUnpinCommentResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Repos.Issues.CommentsApi.Pin.IssuesUnpinCommentResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.Issues.CommentsApi.Pin.IssuesUnpinCommentResult.NotFound(response.body())
            HttpStatusCode.Gone -> Repos.Issues.CommentsApi.Pin.IssuesUnpinCommentResult.Gone(response.body())
            HttpStatusCode.ServiceUnavailable -> Repos.Issues.CommentsApi.Pin.IssuesUnpinCommentResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposIssuesCommentsApiReactionsApi(private val client: HttpClient) : Repos.Issues.CommentsApi.ReactionsApi {
    override suspend fun reactionsListForIssueComment(owner: String, repo: String, commentId: Long, page: Long, perPage: Long, content: Repos.Issues.CommentsApi.ReactionsApi.Content?): Repos.Issues.CommentsApi.ReactionsApi.ReactionsListForIssueCommentResult {
        val response = client.get("/repos/$owner/$repo/issues/comments/$commentId/reactions") {
            parameter("page", page)
            parameter("per_page", perPage)
            content?.let { parameter("content", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Issues.CommentsApi.ReactionsApi.ReactionsListForIssueCommentResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Issues.CommentsApi.ReactionsApi.ReactionsListForIssueCommentResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reactionsCreateForIssueComment(owner: String, repo: String, commentId: Long, body: Repos.Issues.CommentsApi.ReactionsApi.ReactionsCreateForIssueCommentBody): Repos.Issues.CommentsApi.ReactionsApi.ReactionsCreateForIssueCommentResult {
        val response = client.post("/repos/$owner/$repo/issues/comments/$commentId/reactions") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Issues.CommentsApi.ReactionsApi.ReactionsCreateForIssueCommentResult.OK(response.body())
            HttpStatusCode.Created -> Repos.Issues.CommentsApi.ReactionsApi.ReactionsCreateForIssueCommentResult.Created(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Issues.CommentsApi.ReactionsApi.ReactionsCreateForIssueCommentResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reactionsDeleteForIssueComment(owner: String, repo: String, commentId: Long, reactionId: Long): Unit =
        client.delete("/repos/$owner/$repo/issues/comments/$commentId/reactions/$reactionId").body()
}

internal class KtorReposIssuesEventsApi(private val client: HttpClient) : Repos.Issues.EventsApi {
    override suspend fun issuesListEventsForRepo(owner: String, repo: String, page: Long, perPage: Long): Repos.Issues.EventsApi.IssuesListEventsForRepoResult {
        val response = client.get("/repos/$owner/$repo/issues/events") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Issues.EventsApi.IssuesListEventsForRepoResult.OK(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Issues.EventsApi.IssuesListEventsForRepoResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun issuesGetEvent(owner: String, repo: String, eventId: Long): Repos.Issues.EventsApi.IssuesGetEventResult {
        val response = client.get("/repos/$owner/$repo/issues/events/$eventId")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Issues.EventsApi.IssuesGetEventResult.OK(response.body())
            HttpStatusCode.Forbidden -> Repos.Issues.EventsApi.IssuesGetEventResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.Issues.EventsApi.IssuesGetEventResult.NotFound(response.body())
            HttpStatusCode.Gone -> Repos.Issues.EventsApi.IssuesGetEventResult.Gone(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun issuesListEvents(owner: String, repo: String, issueNumber: Long, page: Long, perPage: Long): Repos.Issues.EventsApi.IssuesListEventsResult {
        val response = client.get("/repos/$owner/$repo/issues/$issueNumber/events") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Issues.EventsApi.IssuesListEventsResult.OK(response.body())
            HttpStatusCode.Gone -> Repos.Issues.EventsApi.IssuesListEventsResult.Gone(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposIssuesAssigneesApi(private val client: HttpClient) : Repos.Issues.AssigneesApi {
    override suspend fun issuesAddAssignees(owner: String, repo: String, issueNumber: Long, body: Repos.Issues.AssigneesApi.IssuesAddAssigneesBody?): Issue =
        client.post("/repos/$owner/$repo/issues/$issueNumber/assignees") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }.body()

    override suspend fun issuesRemoveAssignees(owner: String, repo: String, issueNumber: Long, body: Repos.Issues.AssigneesApi.IssuesRemoveAssigneesBody?): Issue =
        client.delete("/repos/$owner/$repo/issues/$issueNumber/assignees") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }.body()

    override suspend fun issuesCheckUserCanBeAssignedToIssue(owner: String, repo: String, issueNumber: Long, assignee: String): Repos.Issues.AssigneesApi.IssuesCheckUserCanBeAssignedToIssueResult {
        val response = client.get("/repos/$owner/$repo/issues/$issueNumber/assignees/$assignee")
        return when (response.status) {
            HttpStatusCode.NoContent -> Repos.Issues.AssigneesApi.IssuesCheckUserCanBeAssignedToIssueResult.NoContent
            HttpStatusCode.NotFound -> Repos.Issues.AssigneesApi.IssuesCheckUserCanBeAssignedToIssueResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposIssuesDependencies(private val client: HttpClient) : Repos.Issues.Dependencies {
    override val blockedBy: Repos.Issues.Dependencies.BlockedBy = KtorReposIssuesDependenciesBlockedBy(client)

    override val blocking: Repos.Issues.Dependencies.Blocking = KtorReposIssuesDependenciesBlocking(client)
}

internal class KtorReposIssuesDependenciesBlockedBy(private val client: HttpClient) : Repos.Issues.Dependencies.BlockedBy {
    override suspend fun issuesListDependenciesBlockedBy(owner: String, repo: String, issueNumber: Long, page: Long, perPage: Long): Repos.Issues.Dependencies.BlockedBy.IssuesListDependenciesBlockedByResult {
        val response = client.get("/repos/$owner/$repo/issues/$issueNumber/dependencies/blocked_by") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Issues.Dependencies.BlockedBy.IssuesListDependenciesBlockedByResult.OK(response.body())
            HttpStatusCode.MovedPermanently -> Repos.Issues.Dependencies.BlockedBy.IssuesListDependenciesBlockedByResult.MovedPermanently(response.body())
            HttpStatusCode.NotFound -> Repos.Issues.Dependencies.BlockedBy.IssuesListDependenciesBlockedByResult.NotFound(response.body())
            HttpStatusCode.Gone -> Repos.Issues.Dependencies.BlockedBy.IssuesListDependenciesBlockedByResult.Gone(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun issuesAddBlockedByDependency(owner: String, repo: String, issueNumber: Long, body: Repos.Issues.Dependencies.BlockedBy.IssuesAddBlockedByDependencyBody): Repos.Issues.Dependencies.BlockedBy.IssuesAddBlockedByDependencyResult {
        val response = client.post("/repos/$owner/$repo/issues/$issueNumber/dependencies/blocked_by") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> Repos.Issues.Dependencies.BlockedBy.IssuesAddBlockedByDependencyResult.Created(response.body())
            HttpStatusCode.MovedPermanently -> Repos.Issues.Dependencies.BlockedBy.IssuesAddBlockedByDependencyResult.MovedPermanently(response.body())
            HttpStatusCode.Forbidden -> Repos.Issues.Dependencies.BlockedBy.IssuesAddBlockedByDependencyResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.Issues.Dependencies.BlockedBy.IssuesAddBlockedByDependencyResult.NotFound(response.body())
            HttpStatusCode.Gone -> Repos.Issues.Dependencies.BlockedBy.IssuesAddBlockedByDependencyResult.Gone(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Issues.Dependencies.BlockedBy.IssuesAddBlockedByDependencyResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun issuesRemoveDependencyBlockedBy(owner: String, repo: String, issueNumber: Long, issueId: Long): Repos.Issues.Dependencies.BlockedBy.IssuesRemoveDependencyBlockedByResult {
        val response = client.delete("/repos/$owner/$repo/issues/$issueNumber/dependencies/blocked_by/$issueId")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Issues.Dependencies.BlockedBy.IssuesRemoveDependencyBlockedByResult.OK(response.body())
            HttpStatusCode.MovedPermanently -> Repos.Issues.Dependencies.BlockedBy.IssuesRemoveDependencyBlockedByResult.MovedPermanently(response.body())
            HttpStatusCode.BadRequest -> Repos.Issues.Dependencies.BlockedBy.IssuesRemoveDependencyBlockedByResult.BadRequest(response.body())
            HttpStatusCode.Unauthorized -> Repos.Issues.Dependencies.BlockedBy.IssuesRemoveDependencyBlockedByResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Repos.Issues.Dependencies.BlockedBy.IssuesRemoveDependencyBlockedByResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.Issues.Dependencies.BlockedBy.IssuesRemoveDependencyBlockedByResult.NotFound(response.body())
            HttpStatusCode.Gone -> Repos.Issues.Dependencies.BlockedBy.IssuesRemoveDependencyBlockedByResult.Gone(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposIssuesDependenciesBlocking(private val client: HttpClient) : Repos.Issues.Dependencies.Blocking {
    override suspend fun issuesListDependenciesBlocking(owner: String, repo: String, issueNumber: Long, page: Long, perPage: Long): Repos.Issues.Dependencies.Blocking.IssuesListDependenciesBlockingResult {
        val response = client.get("/repos/$owner/$repo/issues/$issueNumber/dependencies/blocking") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Issues.Dependencies.Blocking.IssuesListDependenciesBlockingResult.OK(response.body())
            HttpStatusCode.MovedPermanently -> Repos.Issues.Dependencies.Blocking.IssuesListDependenciesBlockingResult.MovedPermanently(response.body())
            HttpStatusCode.NotFound -> Repos.Issues.Dependencies.Blocking.IssuesListDependenciesBlockingResult.NotFound(response.body())
            HttpStatusCode.Gone -> Repos.Issues.Dependencies.Blocking.IssuesListDependenciesBlockingResult.Gone(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposIssuesIssueFieldValues(private val client: HttpClient) : Repos.Issues.IssueFieldValues {
    override suspend fun issuesListIssueFieldValuesForIssue(owner: String, repo: String, issueNumber: Long, page: Long, perPage: Long): Repos.Issues.IssueFieldValues.IssuesListIssueFieldValuesForIssueResult {
        val response = client.get("/repos/$owner/$repo/issues/$issueNumber/issue-field-values") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Issues.IssueFieldValues.IssuesListIssueFieldValuesForIssueResult.OK(response.body())
            HttpStatusCode.MovedPermanently -> Repos.Issues.IssueFieldValues.IssuesListIssueFieldValuesForIssueResult.MovedPermanently(response.body())
            HttpStatusCode.NotFound -> Repos.Issues.IssueFieldValues.IssuesListIssueFieldValuesForIssueResult.NotFound(response.body())
            HttpStatusCode.Gone -> Repos.Issues.IssueFieldValues.IssuesListIssueFieldValuesForIssueResult.Gone(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposIssuesLabelsApi(private val client: HttpClient) : Repos.Issues.LabelsApi {
    override suspend fun issuesListLabelsOnIssue(owner: String, repo: String, issueNumber: Long, page: Long, perPage: Long): Repos.Issues.LabelsApi.IssuesListLabelsOnIssueResult {
        val response = client.get("/repos/$owner/$repo/issues/$issueNumber/labels") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Issues.LabelsApi.IssuesListLabelsOnIssueResult.OK(response.body())
            HttpStatusCode.MovedPermanently -> Repos.Issues.LabelsApi.IssuesListLabelsOnIssueResult.MovedPermanently(response.body())
            HttpStatusCode.NotFound -> Repos.Issues.LabelsApi.IssuesListLabelsOnIssueResult.NotFound(response.body())
            HttpStatusCode.Gone -> Repos.Issues.LabelsApi.IssuesListLabelsOnIssueResult.Gone(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun issuesSetLabels(owner: String, repo: String, issueNumber: Long, body: Repos.Issues.LabelsApi.IssuesSetLabelsBody?): Repos.Issues.LabelsApi.IssuesSetLabelsResult {
        val response = client.put("/repos/$owner/$repo/issues/$issueNumber/labels") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Issues.LabelsApi.IssuesSetLabelsResult.OK(response.body())
            HttpStatusCode.MovedPermanently -> Repos.Issues.LabelsApi.IssuesSetLabelsResult.MovedPermanently(response.body())
            HttpStatusCode.NotFound -> Repos.Issues.LabelsApi.IssuesSetLabelsResult.NotFound(response.body())
            HttpStatusCode.Gone -> Repos.Issues.LabelsApi.IssuesSetLabelsResult.Gone(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Issues.LabelsApi.IssuesSetLabelsResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun issuesAddLabels(owner: String, repo: String, issueNumber: Long, body: Repos.Issues.LabelsApi.IssuesAddLabelsBody?): Repos.Issues.LabelsApi.IssuesAddLabelsResult {
        val response = client.post("/repos/$owner/$repo/issues/$issueNumber/labels") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Issues.LabelsApi.IssuesAddLabelsResult.OK(response.body())
            HttpStatusCode.MovedPermanently -> Repos.Issues.LabelsApi.IssuesAddLabelsResult.MovedPermanently(response.body())
            HttpStatusCode.NotFound -> Repos.Issues.LabelsApi.IssuesAddLabelsResult.NotFound(response.body())
            HttpStatusCode.Gone -> Repos.Issues.LabelsApi.IssuesAddLabelsResult.Gone(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Issues.LabelsApi.IssuesAddLabelsResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun issuesRemoveAllLabels(owner: String, repo: String, issueNumber: Long): Repos.Issues.LabelsApi.IssuesRemoveAllLabelsResult {
        val response = client.delete("/repos/$owner/$repo/issues/$issueNumber/labels")
        return when (response.status) {
            HttpStatusCode.NoContent -> Repos.Issues.LabelsApi.IssuesRemoveAllLabelsResult.NoContent
            HttpStatusCode.MovedPermanently -> Repos.Issues.LabelsApi.IssuesRemoveAllLabelsResult.MovedPermanently(response.body())
            HttpStatusCode.NotFound -> Repos.Issues.LabelsApi.IssuesRemoveAllLabelsResult.NotFound(response.body())
            HttpStatusCode.Gone -> Repos.Issues.LabelsApi.IssuesRemoveAllLabelsResult.Gone(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun issuesRemoveLabel(owner: String, repo: String, issueNumber: Long, name: String): Repos.Issues.LabelsApi.IssuesRemoveLabelResult {
        val response = client.delete("/repos/$owner/$repo/issues/$issueNumber/labels/$name")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Issues.LabelsApi.IssuesRemoveLabelResult.OK(response.body())
            HttpStatusCode.MovedPermanently -> Repos.Issues.LabelsApi.IssuesRemoveLabelResult.MovedPermanently(response.body())
            HttpStatusCode.NotFound -> Repos.Issues.LabelsApi.IssuesRemoveLabelResult.NotFound(response.body())
            HttpStatusCode.Gone -> Repos.Issues.LabelsApi.IssuesRemoveLabelResult.Gone(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposIssuesLock(private val client: HttpClient) : Repos.Issues.Lock {
    override suspend fun issuesLock(owner: String, repo: String, issueNumber: Long, body: Repos.Issues.Lock.IssuesLockBody?): Repos.Issues.Lock.IssuesLockResult {
        val response = client.put("/repos/$owner/$repo/issues/$issueNumber/lock") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }
        return when (response.status) {
            HttpStatusCode.NoContent -> Repos.Issues.Lock.IssuesLockResult.NoContent
            HttpStatusCode.Forbidden -> Repos.Issues.Lock.IssuesLockResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.Issues.Lock.IssuesLockResult.NotFound(response.body())
            HttpStatusCode.Gone -> Repos.Issues.Lock.IssuesLockResult.Gone(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Issues.Lock.IssuesLockResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun issuesUnlock(owner: String, repo: String, issueNumber: Long): Repos.Issues.Lock.IssuesUnlockResult {
        val response = client.delete("/repos/$owner/$repo/issues/$issueNumber/lock")
        return when (response.status) {
            HttpStatusCode.NoContent -> Repos.Issues.Lock.IssuesUnlockResult.NoContent
            HttpStatusCode.Forbidden -> Repos.Issues.Lock.IssuesUnlockResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.Issues.Lock.IssuesUnlockResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposIssuesParent(private val client: HttpClient) : Repos.Issues.Parent {
    override suspend fun issuesGetParent(owner: String, repo: String, issueNumber: Long): Repos.Issues.Parent.IssuesGetParentResult {
        val response = client.get("/repos/$owner/$repo/issues/$issueNumber/parent")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Issues.Parent.IssuesGetParentResult.OK(response.body())
            HttpStatusCode.MovedPermanently -> Repos.Issues.Parent.IssuesGetParentResult.MovedPermanently(response.body())
            HttpStatusCode.NotFound -> Repos.Issues.Parent.IssuesGetParentResult.NotFound(response.body())
            HttpStatusCode.Gone -> Repos.Issues.Parent.IssuesGetParentResult.Gone(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposIssuesReactions(private val client: HttpClient) : Repos.Issues.Reactions {
    override suspend fun reactionsListForIssue(owner: String, repo: String, issueNumber: Long, page: Long, perPage: Long, content: Repos.Issues.Reactions.Content?): Repos.Issues.Reactions.ReactionsListForIssueResult {
        val response = client.get("/repos/$owner/$repo/issues/$issueNumber/reactions") {
            parameter("page", page)
            parameter("per_page", perPage)
            content?.let { parameter("content", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Issues.Reactions.ReactionsListForIssueResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Issues.Reactions.ReactionsListForIssueResult.NotFound(response.body())
            HttpStatusCode.Gone -> Repos.Issues.Reactions.ReactionsListForIssueResult.Gone(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reactionsCreateForIssue(owner: String, repo: String, issueNumber: Long, body: Repos.Issues.Reactions.ReactionsCreateForIssueBody): Repos.Issues.Reactions.ReactionsCreateForIssueResult {
        val response = client.post("/repos/$owner/$repo/issues/$issueNumber/reactions") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Issues.Reactions.ReactionsCreateForIssueResult.OK(response.body())
            HttpStatusCode.Created -> Repos.Issues.Reactions.ReactionsCreateForIssueResult.Created(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Issues.Reactions.ReactionsCreateForIssueResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reactionsDeleteForIssue(owner: String, repo: String, issueNumber: Long, reactionId: Long): Unit =
        client.delete("/repos/$owner/$repo/issues/$issueNumber/reactions/$reactionId").body()
}

internal class KtorReposIssuesSubIssue(private val client: HttpClient) : Repos.Issues.SubIssue {
    override suspend fun issuesRemoveSubIssue(owner: String, repo: String, issueNumber: Long, body: Repos.Issues.SubIssue.IssuesRemoveSubIssueBody): Repos.Issues.SubIssue.IssuesRemoveSubIssueResult {
        val response = client.delete("/repos/$owner/$repo/issues/$issueNumber/sub_issue") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Issues.SubIssue.IssuesRemoveSubIssueResult.OK(response.body())
            HttpStatusCode.BadRequest -> Repos.Issues.SubIssue.IssuesRemoveSubIssueResult.BadRequest(response.body())
            HttpStatusCode.NotFound -> Repos.Issues.SubIssue.IssuesRemoveSubIssueResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposIssuesSubIssues(private val client: HttpClient) : Repos.Issues.SubIssues {
    override val priority: Repos.Issues.SubIssues.Priority = KtorReposIssuesSubIssuesPriority(client)

    override suspend fun issuesListSubIssues(owner: String, repo: String, issueNumber: Long, page: Long, perPage: Long): Repos.Issues.SubIssues.IssuesListSubIssuesResult {
        val response = client.get("/repos/$owner/$repo/issues/$issueNumber/sub_issues") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Issues.SubIssues.IssuesListSubIssuesResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Issues.SubIssues.IssuesListSubIssuesResult.NotFound(response.body())
            HttpStatusCode.Gone -> Repos.Issues.SubIssues.IssuesListSubIssuesResult.Gone(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun issuesAddSubIssue(owner: String, repo: String, issueNumber: Long, body: Repos.Issues.SubIssues.IssuesAddSubIssueBody): Repos.Issues.SubIssues.IssuesAddSubIssueResult {
        val response = client.post("/repos/$owner/$repo/issues/$issueNumber/sub_issues") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> Repos.Issues.SubIssues.IssuesAddSubIssueResult.Created(response.body())
            HttpStatusCode.Forbidden -> Repos.Issues.SubIssues.IssuesAddSubIssueResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.Issues.SubIssues.IssuesAddSubIssueResult.NotFound(response.body())
            HttpStatusCode.Gone -> Repos.Issues.SubIssues.IssuesAddSubIssueResult.Gone(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Issues.SubIssues.IssuesAddSubIssueResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposIssuesSubIssuesPriority(private val client: HttpClient) : Repos.Issues.SubIssues.Priority {
    override suspend fun issuesReprioritizeSubIssue(owner: String, repo: String, issueNumber: Long, body: Repos.Issues.SubIssues.Priority.IssuesReprioritizeSubIssueBody): Repos.Issues.SubIssues.Priority.IssuesReprioritizeSubIssueResult {
        val response = client.patch("/repos/$owner/$repo/issues/$issueNumber/sub_issues/priority") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Issues.SubIssues.Priority.IssuesReprioritizeSubIssueResult.OK(response.body())
            HttpStatusCode.Forbidden -> Repos.Issues.SubIssues.Priority.IssuesReprioritizeSubIssueResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.Issues.SubIssues.Priority.IssuesReprioritizeSubIssueResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Issues.SubIssues.Priority.IssuesReprioritizeSubIssueResult.UnprocessableEntity(response.body())
            HttpStatusCode.ServiceUnavailable -> Repos.Issues.SubIssues.Priority.IssuesReprioritizeSubIssueResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposIssuesTimeline(private val client: HttpClient) : Repos.Issues.Timeline {
    override suspend fun issuesListEventsForTimeline(owner: String, repo: String, issueNumber: Long, page: Long, perPage: Long): Repos.Issues.Timeline.IssuesListEventsForTimelineResult {
        val response = client.get("/repos/$owner/$repo/issues/$issueNumber/timeline") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Issues.Timeline.IssuesListEventsForTimelineResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Issues.Timeline.IssuesListEventsForTimelineResult.NotFound(response.body())
            HttpStatusCode.Gone -> Repos.Issues.Timeline.IssuesListEventsForTimelineResult.Gone(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposKeys(private val client: HttpClient) : Repos.Keys {
    override suspend fun reposListDeployKeys(owner: String, repo: String, page: Long, perPage: Long): List<DeployKey> =
        client.get("/repos/$owner/$repo/keys") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()

    override suspend fun reposCreateDeployKey(owner: String, repo: String, body: Repos.Keys.ReposCreateDeployKeyBody): Repos.Keys.ReposCreateDeployKeyResult {
        val response = client.post("/repos/$owner/$repo/keys") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> Repos.Keys.ReposCreateDeployKeyResult.Created(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Keys.ReposCreateDeployKeyResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposGetDeployKey(owner: String, repo: String, keyId: Long): Repos.Keys.ReposGetDeployKeyResult {
        val response = client.get("/repos/$owner/$repo/keys/$keyId")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Keys.ReposGetDeployKeyResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Keys.ReposGetDeployKeyResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposDeleteDeployKey(owner: String, repo: String, keyId: Long): Unit =
        client.delete("/repos/$owner/$repo/keys/$keyId").body()
}

internal class KtorReposLabels(private val client: HttpClient) : Repos.Labels {
    override suspend fun issuesListLabelsForRepo(owner: String, repo: String, page: Long, perPage: Long): Repos.Labels.IssuesListLabelsForRepoResult {
        val response = client.get("/repos/$owner/$repo/labels") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Labels.IssuesListLabelsForRepoResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Labels.IssuesListLabelsForRepoResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun issuesCreateLabel(owner: String, repo: String, body: Repos.Labels.IssuesCreateLabelBody): Repos.Labels.IssuesCreateLabelResult {
        val response = client.post("/repos/$owner/$repo/labels") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> Repos.Labels.IssuesCreateLabelResult.Created(response.body())
            HttpStatusCode.NotFound -> Repos.Labels.IssuesCreateLabelResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Labels.IssuesCreateLabelResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun issuesGetLabel(owner: String, repo: String, name: String): Repos.Labels.IssuesGetLabelResult {
        val response = client.get("/repos/$owner/$repo/labels/$name")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Labels.IssuesGetLabelResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Labels.IssuesGetLabelResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun issuesDeleteLabel(owner: String, repo: String, name: String): Unit =
        client.delete("/repos/$owner/$repo/labels/$name").body()

    override suspend fun issuesUpdateLabel(owner: String, repo: String, name: String, body: Repos.Labels.IssuesUpdateLabelBody?): Label =
        client.patch("/repos/$owner/$repo/labels/$name") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }.body()
}

internal class KtorReposLanguages(private val client: HttpClient) : Repos.Languages {
    override suspend fun reposListLanguages(owner: String, repo: String): Language =
        client.get("/repos/$owner/$repo/languages").body()
}

internal class KtorReposLicense(private val client: HttpClient) : Repos.License {
    override suspend fun licensesGetForRepo(owner: String, repo: String, ref: CodeScanningRef?): Repos.License.LicensesGetForRepoResult {
        val response = client.get("/repos/$owner/$repo/license") {
            ref?.let { parameter("ref", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.License.LicensesGetForRepoResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.License.LicensesGetForRepoResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposMergeUpstream(private val client: HttpClient) : Repos.MergeUpstream {
    override suspend fun reposMergeUpstream(owner: String, repo: String, body: Repos.MergeUpstream.ReposMergeUpstreamBody): Repos.MergeUpstream.ReposMergeUpstreamResult {
        val response = client.post("/repos/$owner/$repo/merge-upstream") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.MergeUpstream.ReposMergeUpstreamResult.OK(response.body())
            HttpStatusCode.Conflict -> Repos.MergeUpstream.ReposMergeUpstreamResult.Conflict
            HttpStatusCode.UnprocessableEntity -> Repos.MergeUpstream.ReposMergeUpstreamResult.UnprocessableEntity
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposMerges(private val client: HttpClient) : Repos.Merges {
    override suspend fun reposMerge(owner: String, repo: String, body: Repos.Merges.ReposMergeBody): Repos.Merges.ReposMergeResult {
        val response = client.post("/repos/$owner/$repo/merges") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> Repos.Merges.ReposMergeResult.Created(response.body())
            HttpStatusCode.NoContent -> Repos.Merges.ReposMergeResult.NoContent
            HttpStatusCode.Forbidden -> Repos.Merges.ReposMergeResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.Merges.ReposMergeResult.NotFound
            HttpStatusCode.Conflict -> Repos.Merges.ReposMergeResult.Conflict
            HttpStatusCode.UnprocessableEntity -> Repos.Merges.ReposMergeResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposMilestones(private val client: HttpClient) : Repos.Milestones {
    override val labels: Repos.Milestones.LabelsApi = KtorReposMilestonesLabelsApi(client)

    override suspend fun issuesListMilestones(owner: String, repo: String, direction: Repos.Milestones.Direction, page: Long, perPage: Long, sort: Repos.Milestones.Sort, state: Repos.Milestones.State): Repos.Milestones.IssuesListMilestonesResult {
        val response = client.get("/repos/$owner/$repo/milestones") {
            parameter("direction", direction)
            parameter("page", page)
            parameter("per_page", perPage)
            parameter("sort", sort)
            parameter("state", state)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Milestones.IssuesListMilestonesResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Milestones.IssuesListMilestonesResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun issuesCreateMilestone(owner: String, repo: String, body: Repos.Milestones.IssuesCreateMilestoneBody): Repos.Milestones.IssuesCreateMilestoneResult {
        val response = client.post("/repos/$owner/$repo/milestones") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> Repos.Milestones.IssuesCreateMilestoneResult.Created(response.body())
            HttpStatusCode.NotFound -> Repos.Milestones.IssuesCreateMilestoneResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Milestones.IssuesCreateMilestoneResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun issuesGetMilestone(owner: String, repo: String, milestoneNumber: Long): Repos.Milestones.IssuesGetMilestoneResult {
        val response = client.get("/repos/$owner/$repo/milestones/$milestoneNumber")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Milestones.IssuesGetMilestoneResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Milestones.IssuesGetMilestoneResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun issuesDeleteMilestone(owner: String, repo: String, milestoneNumber: Long): Repos.Milestones.IssuesDeleteMilestoneResult {
        val response = client.delete("/repos/$owner/$repo/milestones/$milestoneNumber")
        return when (response.status) {
            HttpStatusCode.NoContent -> Repos.Milestones.IssuesDeleteMilestoneResult.NoContent
            HttpStatusCode.NotFound -> Repos.Milestones.IssuesDeleteMilestoneResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun issuesUpdateMilestone(owner: String, repo: String, milestoneNumber: Long, body: Repos.Milestones.IssuesUpdateMilestoneBody?): Milestone =
        client.patch("/repos/$owner/$repo/milestones/$milestoneNumber") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }.body()
}

internal class KtorReposMilestonesLabelsApi(private val client: HttpClient) : Repos.Milestones.LabelsApi {
    override suspend fun issuesListLabelsForMilestone(owner: String, repo: String, milestoneNumber: Long, page: Long, perPage: Long): List<Label> =
        client.get("/repos/$owner/$repo/milestones/$milestoneNumber/labels") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()
}

internal class KtorReposNotifications(private val client: HttpClient) : Repos.Notifications {
    override suspend fun activityListRepoNotificationsForAuthenticatedUser(owner: String, repo: String, all: Boolean, page: Long, participating: Boolean, perPage: Long, before: LocalDateTime?, since: LocalDateTime?): List<Thread> =
        client.get("/repos/$owner/$repo/notifications") {
            parameter("all", all)
            parameter("page", page)
            parameter("participating", participating)
            parameter("per_page", perPage)
            before?.let { parameter("before", it) }
            since?.let { parameter("since", it) }
        }.body()

    override suspend fun activityMarkRepoNotificationsAsRead(owner: String, repo: String, body: Repos.Notifications.ActivityMarkRepoNotificationsAsReadBody?): Repos.Notifications.ActivityMarkRepoNotificationsAsReadResult {
        val response = client.put("/repos/$owner/$repo/notifications") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }
        return when (response.status) {
            HttpStatusCode.Accepted -> Repos.Notifications.ActivityMarkRepoNotificationsAsReadResult.Accepted(response.body())
            HttpStatusCode.ResetContent -> Repos.Notifications.ActivityMarkRepoNotificationsAsReadResult.ResetContent
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposPages(private val client: HttpClient) : Repos.Pages {
    override val builds: Repos.Pages.Builds = KtorReposPagesBuilds(client)

    override val deployments: Repos.Pages.DeploymentsApi = KtorReposPagesDeploymentsApi(client)

    override val health: Repos.Pages.Health = KtorReposPagesHealth(client)

    override suspend fun reposGetPages(owner: String, repo: String): Repos.Pages.ReposGetPagesResult {
        val response = client.get("/repos/$owner/$repo/pages")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Pages.ReposGetPagesResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Pages.ReposGetPagesResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposUpdateInformationAboutPagesSite(owner: String, repo: String, body: Repos.Pages.ReposUpdateInformationAboutPagesSiteBody): Repos.Pages.ReposUpdateInformationAboutPagesSiteResult {
        val response = client.put("/repos/$owner/$repo/pages") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.NoContent -> Repos.Pages.ReposUpdateInformationAboutPagesSiteResult.NoContent
            HttpStatusCode.BadRequest -> Repos.Pages.ReposUpdateInformationAboutPagesSiteResult.BadRequest(response.body())
            HttpStatusCode.Conflict -> Repos.Pages.ReposUpdateInformationAboutPagesSiteResult.Conflict(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Pages.ReposUpdateInformationAboutPagesSiteResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposCreatePagesSite(owner: String, repo: String, body: Repos.Pages.ReposCreatePagesSiteBody): Repos.Pages.ReposCreatePagesSiteResult {
        val response = client.post("/repos/$owner/$repo/pages") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> Repos.Pages.ReposCreatePagesSiteResult.Created(response.body())
            HttpStatusCode.Conflict -> Repos.Pages.ReposCreatePagesSiteResult.Conflict(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Pages.ReposCreatePagesSiteResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposDeletePagesSite(owner: String, repo: String): Repos.Pages.ReposDeletePagesSiteResult {
        val response = client.delete("/repos/$owner/$repo/pages")
        return when (response.status) {
            HttpStatusCode.NoContent -> Repos.Pages.ReposDeletePagesSiteResult.NoContent
            HttpStatusCode.NotFound -> Repos.Pages.ReposDeletePagesSiteResult.NotFound(response.body())
            HttpStatusCode.Conflict -> Repos.Pages.ReposDeletePagesSiteResult.Conflict(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Pages.ReposDeletePagesSiteResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposPagesBuilds(private val client: HttpClient) : Repos.Pages.Builds {
    override val latest: Repos.Pages.Builds.Latest = KtorReposPagesBuildsLatest(client)

    override suspend fun reposListPagesBuilds(owner: String, repo: String, page: Long, perPage: Long): List<PageBuild> =
        client.get("/repos/$owner/$repo/pages/builds") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()

    override suspend fun reposRequestPagesBuild(owner: String, repo: String): PageBuildStatus =
        client.post("/repos/$owner/$repo/pages/builds").body()

    override suspend fun reposGetPagesBuild(owner: String, repo: String, buildId: Long): PageBuild =
        client.get("/repos/$owner/$repo/pages/builds/$buildId").body()
}

internal class KtorReposPagesBuildsLatest(private val client: HttpClient) : Repos.Pages.Builds.Latest {
    override suspend fun reposGetLatestPagesBuild(owner: String, repo: String): PageBuild =
        client.get("/repos/$owner/$repo/pages/builds/latest").body()
}

internal class KtorReposPagesDeploymentsApi(private val client: HttpClient) : Repos.Pages.DeploymentsApi {
    override val cancel: Repos.Pages.DeploymentsApi.Cancel = KtorReposPagesDeploymentsApiCancel(client)

    override suspend fun reposCreatePagesDeployment(owner: String, repo: String, body: Repos.Pages.DeploymentsApi.ReposCreatePagesDeploymentBody): Repos.Pages.DeploymentsApi.ReposCreatePagesDeploymentResult {
        val response = client.post("/repos/$owner/$repo/pages/deployments") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Pages.DeploymentsApi.ReposCreatePagesDeploymentResult.OK(response.body())
            HttpStatusCode.BadRequest -> Repos.Pages.DeploymentsApi.ReposCreatePagesDeploymentResult.BadRequest(response.body())
            HttpStatusCode.NotFound -> Repos.Pages.DeploymentsApi.ReposCreatePagesDeploymentResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Pages.DeploymentsApi.ReposCreatePagesDeploymentResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposGetPagesDeployment(owner: String, repo: String, pagesDeploymentId: Repos.Pages.DeploymentsApi.PagesDeploymentId): Repos.Pages.DeploymentsApi.ReposGetPagesDeploymentResult {
        val response = client.get("/repos/$owner/$repo/pages/deployments/$pagesDeploymentId")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Pages.DeploymentsApi.ReposGetPagesDeploymentResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Pages.DeploymentsApi.ReposGetPagesDeploymentResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposPagesDeploymentsApiCancel(private val client: HttpClient) : Repos.Pages.DeploymentsApi.Cancel {
    override suspend fun reposCancelPagesDeployment(owner: String, repo: String, pagesDeploymentId: Repos.Pages.DeploymentsApi.Cancel.ReposCancelPagesDeploymentPagesDeploymentId): Repos.Pages.DeploymentsApi.Cancel.ReposCancelPagesDeploymentResult {
        val response = client.post("/repos/$owner/$repo/pages/deployments/$pagesDeploymentId/cancel")
        return when (response.status) {
            HttpStatusCode.NoContent -> Repos.Pages.DeploymentsApi.Cancel.ReposCancelPagesDeploymentResult.NoContent
            HttpStatusCode.NotFound -> Repos.Pages.DeploymentsApi.Cancel.ReposCancelPagesDeploymentResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposPagesHealth(private val client: HttpClient) : Repos.Pages.Health {
    override suspend fun reposGetPagesHealthCheck(owner: String, repo: String): Repos.Pages.Health.ReposGetPagesHealthCheckResult {
        val response = client.get("/repos/$owner/$repo/pages/health")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Pages.Health.ReposGetPagesHealthCheckResult.OK(response.body())
            HttpStatusCode.Accepted -> Repos.Pages.Health.ReposGetPagesHealthCheckResult.Accepted(response.body())
            HttpStatusCode.BadRequest -> Repos.Pages.Health.ReposGetPagesHealthCheckResult.BadRequest
            HttpStatusCode.NotFound -> Repos.Pages.Health.ReposGetPagesHealthCheckResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Pages.Health.ReposGetPagesHealthCheckResult.UnprocessableEntity
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposPrivateVulnerabilityReporting(private val client: HttpClient) : Repos.PrivateVulnerabilityReporting {
    override suspend fun reposCheckPrivateVulnerabilityReporting(owner: String, repo: String): Repos.PrivateVulnerabilityReporting.ReposCheckPrivateVulnerabilityReportingResult {
        val response = client.get("/repos/$owner/$repo/private-vulnerability-reporting")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.PrivateVulnerabilityReporting.ReposCheckPrivateVulnerabilityReportingResult.OK(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.PrivateVulnerabilityReporting.ReposCheckPrivateVulnerabilityReportingResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposEnablePrivateVulnerabilityReporting(owner: String, repo: String): Repos.PrivateVulnerabilityReporting.ReposEnablePrivateVulnerabilityReportingResult {
        val response = client.put("/repos/$owner/$repo/private-vulnerability-reporting")
        return when (response.status) {
            HttpStatusCode.NoContent -> Repos.PrivateVulnerabilityReporting.ReposEnablePrivateVulnerabilityReportingResult.NoContent
            HttpStatusCode.UnprocessableEntity -> Repos.PrivateVulnerabilityReporting.ReposEnablePrivateVulnerabilityReportingResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposDisablePrivateVulnerabilityReporting(owner: String, repo: String): Repos.PrivateVulnerabilityReporting.ReposDisablePrivateVulnerabilityReportingResult {
        val response = client.delete("/repos/$owner/$repo/private-vulnerability-reporting")
        return when (response.status) {
            HttpStatusCode.NoContent -> Repos.PrivateVulnerabilityReporting.ReposDisablePrivateVulnerabilityReportingResult.NoContent
            HttpStatusCode.UnprocessableEntity -> Repos.PrivateVulnerabilityReporting.ReposDisablePrivateVulnerabilityReportingResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposProperties(private val client: HttpClient) : Repos.Properties {
    override val values: Repos.Properties.Values = KtorReposPropertiesValues(client)
}

internal class KtorReposPropertiesValues(private val client: HttpClient) : Repos.Properties.Values {
    override suspend fun reposCustomPropertiesForReposGetRepositoryValues(owner: String, repo: String): Repos.Properties.Values.ReposCustomPropertiesForReposGetRepositoryValuesResult {
        val response = client.get("/repos/$owner/$repo/properties/values")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Properties.Values.ReposCustomPropertiesForReposGetRepositoryValuesResult.OK(response.body())
            HttpStatusCode.Forbidden -> Repos.Properties.Values.ReposCustomPropertiesForReposGetRepositoryValuesResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.Properties.Values.ReposCustomPropertiesForReposGetRepositoryValuesResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposCustomPropertiesForReposCreateOrUpdateRepositoryValues(owner: String, repo: String, body: Repos.Properties.Values.ReposCustomPropertiesForReposCreateOrUpdateRepositoryValuesBody): Repos.Properties.Values.ReposCustomPropertiesForReposCreateOrUpdateRepositoryValuesResult {
        val response = client.patch("/repos/$owner/$repo/properties/values") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.NoContent -> Repos.Properties.Values.ReposCustomPropertiesForReposCreateOrUpdateRepositoryValuesResult.NoContent
            HttpStatusCode.Forbidden -> Repos.Properties.Values.ReposCustomPropertiesForReposCreateOrUpdateRepositoryValuesResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.Properties.Values.ReposCustomPropertiesForReposCreateOrUpdateRepositoryValuesResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Properties.Values.ReposCustomPropertiesForReposCreateOrUpdateRepositoryValuesResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposPulls(private val client: HttpClient) : Repos.Pulls {
    override val comments: Repos.Pulls.CommentsApi = KtorReposPullsCommentsApi(client)

    override val codespaces: Repos.Pulls.CodespacesApi = KtorReposPullsCodespacesApi(client)

    override val commits: Repos.Pulls.CommitsApi = KtorReposPullsCommitsApi(client)

    override val files: Repos.Pulls.Files = KtorReposPullsFiles(client)

    override val merge: Repos.Pulls.Merge = KtorReposPullsMerge(client)

    override val requestedReviewers: Repos.Pulls.RequestedReviewers = KtorReposPullsRequestedReviewers(client)

    override val reviews: Repos.Pulls.Reviews = KtorReposPullsReviews(client)

    override val updateBranch: Repos.Pulls.UpdateBranch = KtorReposPullsUpdateBranch(client)

    override suspend fun pullsList(owner: String, repo: String, page: Long, perPage: Long, sort: Repos.Pulls.Sort, state: Repos.Pulls.State, base: String?, direction: Repos.Pulls.Direction?, head: String?): Repos.Pulls.PullsListResult {
        val response = client.get("/repos/$owner/$repo/pulls") {
            parameter("page", page)
            parameter("per_page", perPage)
            parameter("sort", sort)
            parameter("state", state)
            base?.let { parameter("base", it) }
            direction?.let { parameter("direction", it) }
            head?.let { parameter("head", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Pulls.PullsListResult.OK(response.body())
            HttpStatusCode.NotModified -> Repos.Pulls.PullsListResult.NotModified
            HttpStatusCode.UnprocessableEntity -> Repos.Pulls.PullsListResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun pullsCreate(owner: String, repo: String, body: Repos.Pulls.PullsCreateBody): Repos.Pulls.PullsCreateResult {
        val response = client.post("/repos/$owner/$repo/pulls") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> Repos.Pulls.PullsCreateResult.Created(response.body())
            HttpStatusCode.Forbidden -> Repos.Pulls.PullsCreateResult.Forbidden(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Pulls.PullsCreateResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun pullsGet(owner: String, repo: String, pullNumber: Long): Repos.Pulls.PullsGetResult {
        val response = client.get("/repos/$owner/$repo/pulls/$pullNumber")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Pulls.PullsGetResult.OK(response.body())
            HttpStatusCode.NotModified -> Repos.Pulls.PullsGetResult.NotModified
            HttpStatusCode.NotFound -> Repos.Pulls.PullsGetResult.NotFound(response.body())
            HttpStatusCode.NotAcceptable -> Repos.Pulls.PullsGetResult.NotAcceptable(response.body())
            HttpStatusCode.InternalServerError -> Repos.Pulls.PullsGetResult.InternalServerError(response.body())
            HttpStatusCode.ServiceUnavailable -> Repos.Pulls.PullsGetResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun pullsUpdate(owner: String, repo: String, pullNumber: Long, body: Repos.Pulls.PullsUpdateBody?): Repos.Pulls.PullsUpdateResult {
        val response = client.patch("/repos/$owner/$repo/pulls/$pullNumber") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Pulls.PullsUpdateResult.OK(response.body())
            HttpStatusCode.Forbidden -> Repos.Pulls.PullsUpdateResult.Forbidden(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Pulls.PullsUpdateResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposPullsCommentsApi(private val client: HttpClient) : Repos.Pulls.CommentsApi {
    override val reactions: Repos.Pulls.CommentsApi.Reactions = KtorReposPullsCommentsApiReactions(client)

    override val replies: Repos.Pulls.CommentsApi.Replies = KtorReposPullsCommentsApiReplies(client)

    override suspend fun pullsListReviewCommentsForRepo(owner: String, repo: String, page: Long, perPage: Long, direction: Repos.Pulls.CommentsApi.PullsListReviewCommentsForRepoDirection?, since: LocalDateTime?, sort: Repos.Pulls.CommentsApi.PullsListReviewCommentsForRepoSort?): List<PullRequestReviewComment> =
        client.get("/repos/$owner/$repo/pulls/comments") {
            parameter("page", page)
            parameter("per_page", perPage)
            direction?.let { parameter("direction", it) }
            since?.let { parameter("since", it) }
            sort?.let { parameter("sort", it) }
        }.body()

    override suspend fun pullsGetReviewComment(owner: String, repo: String, commentId: Long): Repos.Pulls.CommentsApi.PullsGetReviewCommentResult {
        val response = client.get("/repos/$owner/$repo/pulls/comments/$commentId")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Pulls.CommentsApi.PullsGetReviewCommentResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Pulls.CommentsApi.PullsGetReviewCommentResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun pullsDeleteReviewComment(owner: String, repo: String, commentId: Long): Repos.Pulls.CommentsApi.PullsDeleteReviewCommentResult {
        val response = client.delete("/repos/$owner/$repo/pulls/comments/$commentId")
        return when (response.status) {
            HttpStatusCode.NoContent -> Repos.Pulls.CommentsApi.PullsDeleteReviewCommentResult.NoContent
            HttpStatusCode.NotFound -> Repos.Pulls.CommentsApi.PullsDeleteReviewCommentResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun pullsUpdateReviewComment(owner: String, repo: String, commentId: Long, body: Repos.Pulls.CommentsApi.PullsUpdateReviewCommentBody): PullRequestReviewComment =
        client.patch("/repos/$owner/$repo/pulls/comments/$commentId") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()

    override suspend fun pullsListReviewComments(owner: String, repo: String, pullNumber: Long, page: Long, perPage: Long, sort: Repos.Pulls.CommentsApi.PullsListReviewCommentsSort, direction: Repos.Pulls.CommentsApi.PullsListReviewCommentsDirection?, since: LocalDateTime?): List<PullRequestReviewComment> =
        client.get("/repos/$owner/$repo/pulls/$pullNumber/comments") {
            parameter("page", page)
            parameter("per_page", perPage)
            parameter("sort", sort)
            direction?.let { parameter("direction", it) }
            since?.let { parameter("since", it) }
        }.body()

    override suspend fun pullsCreateReviewComment(owner: String, repo: String, pullNumber: Long, body: Repos.Pulls.CommentsApi.PullsCreateReviewCommentBody): Repos.Pulls.CommentsApi.PullsCreateReviewCommentResult {
        val response = client.post("/repos/$owner/$repo/pulls/$pullNumber/comments") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> Repos.Pulls.CommentsApi.PullsCreateReviewCommentResult.Created(response.body())
            HttpStatusCode.Forbidden -> Repos.Pulls.CommentsApi.PullsCreateReviewCommentResult.Forbidden(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Pulls.CommentsApi.PullsCreateReviewCommentResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposPullsCommentsApiReactions(private val client: HttpClient) : Repos.Pulls.CommentsApi.Reactions {
    override suspend fun reactionsListForPullRequestReviewComment(owner: String, repo: String, commentId: Long, page: Long, perPage: Long, content: Repos.Pulls.CommentsApi.Reactions.Content?): Repos.Pulls.CommentsApi.Reactions.ReactionsListForPullRequestReviewCommentResult {
        val response = client.get("/repos/$owner/$repo/pulls/comments/$commentId/reactions") {
            parameter("page", page)
            parameter("per_page", perPage)
            content?.let { parameter("content", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Pulls.CommentsApi.Reactions.ReactionsListForPullRequestReviewCommentResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Pulls.CommentsApi.Reactions.ReactionsListForPullRequestReviewCommentResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reactionsCreateForPullRequestReviewComment(owner: String, repo: String, commentId: Long, body: Repos.Pulls.CommentsApi.Reactions.ReactionsCreateForPullRequestReviewCommentBody): Repos.Pulls.CommentsApi.Reactions.ReactionsCreateForPullRequestReviewCommentResult {
        val response = client.post("/repos/$owner/$repo/pulls/comments/$commentId/reactions") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Pulls.CommentsApi.Reactions.ReactionsCreateForPullRequestReviewCommentResult.OK(response.body())
            HttpStatusCode.Created -> Repos.Pulls.CommentsApi.Reactions.ReactionsCreateForPullRequestReviewCommentResult.Created(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Pulls.CommentsApi.Reactions.ReactionsCreateForPullRequestReviewCommentResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reactionsDeleteForPullRequestComment(owner: String, repo: String, commentId: Long, reactionId: Long): Unit =
        client.delete("/repos/$owner/$repo/pulls/comments/$commentId/reactions/$reactionId").body()
}

internal class KtorReposPullsCommentsApiReplies(private val client: HttpClient) : Repos.Pulls.CommentsApi.Replies {
    override suspend fun pullsCreateReplyForReviewComment(owner: String, repo: String, pullNumber: Long, commentId: Long, body: Repos.Pulls.CommentsApi.Replies.PullsCreateReplyForReviewCommentBody): Repos.Pulls.CommentsApi.Replies.PullsCreateReplyForReviewCommentResult {
        val response = client.post("/repos/$owner/$repo/pulls/$pullNumber/comments/$commentId/replies") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> Repos.Pulls.CommentsApi.Replies.PullsCreateReplyForReviewCommentResult.Created(response.body())
            HttpStatusCode.NotFound -> Repos.Pulls.CommentsApi.Replies.PullsCreateReplyForReviewCommentResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposPullsCodespacesApi(private val client: HttpClient) : Repos.Pulls.CodespacesApi {
    override suspend fun codespacesCreateWithPrForAuthenticatedUser(owner: String, repo: String, pullNumber: Long, body: Repos.Pulls.CodespacesApi.CodespacesCreateWithPrForAuthenticatedUserBody): Repos.Pulls.CodespacesApi.CodespacesCreateWithPrForAuthenticatedUserResult {
        val response = client.post("/repos/$owner/$repo/pulls/$pullNumber/codespaces") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> Repos.Pulls.CodespacesApi.CodespacesCreateWithPrForAuthenticatedUserResult.Created(response.body())
            HttpStatusCode.Accepted -> Repos.Pulls.CodespacesApi.CodespacesCreateWithPrForAuthenticatedUserResult.Accepted(response.body())
            HttpStatusCode.Unauthorized -> Repos.Pulls.CodespacesApi.CodespacesCreateWithPrForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Repos.Pulls.CodespacesApi.CodespacesCreateWithPrForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.Pulls.CodespacesApi.CodespacesCreateWithPrForAuthenticatedUserResult.NotFound(response.body())
            HttpStatusCode.ServiceUnavailable -> Repos.Pulls.CodespacesApi.CodespacesCreateWithPrForAuthenticatedUserResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposPullsCommitsApi(private val client: HttpClient) : Repos.Pulls.CommitsApi {
    override suspend fun pullsListCommits(owner: String, repo: String, pullNumber: Long, page: Long, perPage: Long): List<Commit> =
        client.get("/repos/$owner/$repo/pulls/$pullNumber/commits") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()
}

internal class KtorReposPullsFiles(private val client: HttpClient) : Repos.Pulls.Files {
    override suspend fun pullsListFiles(owner: String, repo: String, pullNumber: Long, page: Long, perPage: Long): Repos.Pulls.Files.PullsListFilesResult {
        val response = client.get("/repos/$owner/$repo/pulls/$pullNumber/files") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Pulls.Files.PullsListFilesResult.OK(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Pulls.Files.PullsListFilesResult.UnprocessableEntity(response.body())
            HttpStatusCode.InternalServerError -> Repos.Pulls.Files.PullsListFilesResult.InternalServerError(response.body())
            HttpStatusCode.ServiceUnavailable -> Repos.Pulls.Files.PullsListFilesResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposPullsMerge(private val client: HttpClient) : Repos.Pulls.Merge {
    override suspend fun pullsCheckIfMerged(owner: String, repo: String, pullNumber: Long): Repos.Pulls.Merge.PullsCheckIfMergedResult {
        val response = client.get("/repos/$owner/$repo/pulls/$pullNumber/merge")
        return when (response.status) {
            HttpStatusCode.NoContent -> Repos.Pulls.Merge.PullsCheckIfMergedResult.NoContent
            HttpStatusCode.NotFound -> Repos.Pulls.Merge.PullsCheckIfMergedResult.NotFound
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun pullsMerge(owner: String, repo: String, pullNumber: Long, body: Repos.Pulls.Merge.PullsMergeBody?): Repos.Pulls.Merge.PullsMergeResult {
        val response = client.put("/repos/$owner/$repo/pulls/$pullNumber/merge") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Pulls.Merge.PullsMergeResult.OK(response.body())
            HttpStatusCode.Forbidden -> Repos.Pulls.Merge.PullsMergeResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.Pulls.Merge.PullsMergeResult.NotFound(response.body())
            HttpStatusCode.MethodNotAllowed -> Repos.Pulls.Merge.PullsMergeResult.MethodNotAllowed(response.body())
            HttpStatusCode.Conflict -> Repos.Pulls.Merge.PullsMergeResult.Conflict(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Pulls.Merge.PullsMergeResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposPullsRequestedReviewers(private val client: HttpClient) : Repos.Pulls.RequestedReviewers {
    override suspend fun pullsListRequestedReviewers(owner: String, repo: String, pullNumber: Long): PullRequestReviewRequest =
        client.get("/repos/$owner/$repo/pulls/$pullNumber/requested_reviewers").body()

    override suspend fun pullsRequestReviewers(owner: String, repo: String, pullNumber: Long, body: Repos.Pulls.RequestedReviewers.PullsRequestReviewersBody?): Repos.Pulls.RequestedReviewers.PullsRequestReviewersResult {
        val response = client.post("/repos/$owner/$repo/pulls/$pullNumber/requested_reviewers") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }
        return when (response.status) {
            HttpStatusCode.Created -> Repos.Pulls.RequestedReviewers.PullsRequestReviewersResult.Created(response.body())
            HttpStatusCode.Forbidden -> Repos.Pulls.RequestedReviewers.PullsRequestReviewersResult.Forbidden(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Pulls.RequestedReviewers.PullsRequestReviewersResult.UnprocessableEntity
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun pullsRemoveRequestedReviewers(owner: String, repo: String, pullNumber: Long, body: Repos.Pulls.RequestedReviewers.PullsRemoveRequestedReviewersBody): Repos.Pulls.RequestedReviewers.PullsRemoveRequestedReviewersResult {
        val response = client.delete("/repos/$owner/$repo/pulls/$pullNumber/requested_reviewers") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Pulls.RequestedReviewers.PullsRemoveRequestedReviewersResult.OK(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Pulls.RequestedReviewers.PullsRemoveRequestedReviewersResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposPullsReviews(private val client: HttpClient) : Repos.Pulls.Reviews {
    override val comments: Repos.Pulls.Reviews.CommentsApi2 = KtorReposPullsReviewsCommentsApi2(client)

    override val dismissals: Repos.Pulls.Reviews.Dismissals = KtorReposPullsReviewsDismissals(client)

    override val events: Repos.Pulls.Reviews.EventsApi = KtorReposPullsReviewsEventsApi(client)

    override suspend fun pullsListReviews(owner: String, repo: String, pullNumber: Long, page: Long, perPage: Long): List<PullRequestReview> =
        client.get("/repos/$owner/$repo/pulls/$pullNumber/reviews") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()

    override suspend fun pullsCreateReview(owner: String, repo: String, pullNumber: Long, body: Repos.Pulls.Reviews.PullsCreateReviewBody?): Repos.Pulls.Reviews.PullsCreateReviewResult {
        val response = client.post("/repos/$owner/$repo/pulls/$pullNumber/reviews") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Pulls.Reviews.PullsCreateReviewResult.OK(response.body())
            HttpStatusCode.Forbidden -> Repos.Pulls.Reviews.PullsCreateReviewResult.Forbidden(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Pulls.Reviews.PullsCreateReviewResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun pullsGetReview(owner: String, repo: String, pullNumber: Long, reviewId: Long): Repos.Pulls.Reviews.PullsGetReviewResult {
        val response = client.get("/repos/$owner/$repo/pulls/$pullNumber/reviews/$reviewId")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Pulls.Reviews.PullsGetReviewResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Pulls.Reviews.PullsGetReviewResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun pullsUpdateReview(owner: String, repo: String, pullNumber: Long, reviewId: Long, body: Repos.Pulls.Reviews.PullsUpdateReviewBody): Repos.Pulls.Reviews.PullsUpdateReviewResult {
        val response = client.put("/repos/$owner/$repo/pulls/$pullNumber/reviews/$reviewId") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Pulls.Reviews.PullsUpdateReviewResult.OK(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Pulls.Reviews.PullsUpdateReviewResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun pullsDeletePendingReview(owner: String, repo: String, pullNumber: Long, reviewId: Long): Repos.Pulls.Reviews.PullsDeletePendingReviewResult {
        val response = client.delete("/repos/$owner/$repo/pulls/$pullNumber/reviews/$reviewId")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Pulls.Reviews.PullsDeletePendingReviewResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Pulls.Reviews.PullsDeletePendingReviewResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Pulls.Reviews.PullsDeletePendingReviewResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposPullsReviewsCommentsApi2(private val client: HttpClient) : Repos.Pulls.Reviews.CommentsApi2 {
    override suspend fun pullsListCommentsForReview(owner: String, repo: String, pullNumber: Long, reviewId: Long, page: Long, perPage: Long): Repos.Pulls.Reviews.CommentsApi2.PullsListCommentsForReviewResult {
        val response = client.get("/repos/$owner/$repo/pulls/$pullNumber/reviews/$reviewId/comments") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Pulls.Reviews.CommentsApi2.PullsListCommentsForReviewResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Pulls.Reviews.CommentsApi2.PullsListCommentsForReviewResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposPullsReviewsDismissals(private val client: HttpClient) : Repos.Pulls.Reviews.Dismissals {
    override suspend fun pullsDismissReview(owner: String, repo: String, pullNumber: Long, reviewId: Long, body: Repos.Pulls.Reviews.Dismissals.PullsDismissReviewBody): Repos.Pulls.Reviews.Dismissals.PullsDismissReviewResult {
        val response = client.put("/repos/$owner/$repo/pulls/$pullNumber/reviews/$reviewId/dismissals") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Pulls.Reviews.Dismissals.PullsDismissReviewResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Pulls.Reviews.Dismissals.PullsDismissReviewResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Pulls.Reviews.Dismissals.PullsDismissReviewResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposPullsReviewsEventsApi(private val client: HttpClient) : Repos.Pulls.Reviews.EventsApi {
    override suspend fun pullsSubmitReview(owner: String, repo: String, pullNumber: Long, reviewId: Long, body: Repos.Pulls.Reviews.EventsApi.PullsSubmitReviewBody): Repos.Pulls.Reviews.EventsApi.PullsSubmitReviewResult {
        val response = client.post("/repos/$owner/$repo/pulls/$pullNumber/reviews/$reviewId/events") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Pulls.Reviews.EventsApi.PullsSubmitReviewResult.OK(response.body())
            HttpStatusCode.Forbidden -> Repos.Pulls.Reviews.EventsApi.PullsSubmitReviewResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.Pulls.Reviews.EventsApi.PullsSubmitReviewResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Pulls.Reviews.EventsApi.PullsSubmitReviewResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposPullsUpdateBranch(private val client: HttpClient) : Repos.Pulls.UpdateBranch {
    override suspend fun pullsUpdateBranch(owner: String, repo: String, pullNumber: Long, body: Repos.Pulls.UpdateBranch.PullsUpdateBranchBody?): Repos.Pulls.UpdateBranch.PullsUpdateBranchResult {
        val response = client.put("/repos/$owner/$repo/pulls/$pullNumber/update-branch") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }
        return when (response.status) {
            HttpStatusCode.Accepted -> Repos.Pulls.UpdateBranch.PullsUpdateBranchResult.Accepted(response.body())
            HttpStatusCode.Forbidden -> Repos.Pulls.UpdateBranch.PullsUpdateBranchResult.Forbidden(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Pulls.UpdateBranch.PullsUpdateBranchResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposReadme(private val client: HttpClient) : Repos.Readme {
    override suspend fun reposGetReadme(owner: String, repo: String, ref: String?): Repos.Readme.ReposGetReadmeResult {
        val response = client.get("/repos/$owner/$repo/readme") {
            ref?.let { parameter("ref", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Readme.ReposGetReadmeResult.OK(response.body())
            HttpStatusCode.NotModified -> Repos.Readme.ReposGetReadmeResult.NotModified
            HttpStatusCode.NotFound -> Repos.Readme.ReposGetReadmeResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Readme.ReposGetReadmeResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposGetReadmeInDirectory(owner: String, repo: String, dir: String, ref: String?): Repos.Readme.ReposGetReadmeInDirectoryResult {
        val response = client.get("/repos/$owner/$repo/readme/$dir") {
            ref?.let { parameter("ref", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Readme.ReposGetReadmeInDirectoryResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Readme.ReposGetReadmeInDirectoryResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Readme.ReposGetReadmeInDirectoryResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposReleases(private val client: HttpClient) : Repos.Releases {
    override val assets: Repos.Releases.Assets = KtorReposReleasesAssets(client)

    override val generateNotes: Repos.Releases.GenerateNotes = KtorReposReleasesGenerateNotes(client)

    override val latest: Repos.Releases.Latest = KtorReposReleasesLatest(client)

    override val tags: Repos.Releases.TagsApi = KtorReposReleasesTagsApi(client)

    override val reactions: Repos.Releases.Reactions = KtorReposReleasesReactions(client)

    override suspend fun reposListReleases(owner: String, repo: String, page: Long, perPage: Long): Repos.Releases.ReposListReleasesResult {
        val response = client.get("/repos/$owner/$repo/releases") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Releases.ReposListReleasesResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Releases.ReposListReleasesResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposCreateRelease(owner: String, repo: String, body: Repos.Releases.ReposCreateReleaseBody): Repos.Releases.ReposCreateReleaseResult {
        val response = client.post("/repos/$owner/$repo/releases") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> Repos.Releases.ReposCreateReleaseResult.Created(response.body())
            HttpStatusCode.NotFound -> Repos.Releases.ReposCreateReleaseResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Releases.ReposCreateReleaseResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposGetRelease(owner: String, repo: String, releaseId: Long): Repos.Releases.ReposGetReleaseResult {
        val response = client.get("/repos/$owner/$repo/releases/$releaseId")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Releases.ReposGetReleaseResult.OK(response.body())
            HttpStatusCode.Unauthorized -> Repos.Releases.ReposGetReleaseResult.Unauthorized
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposDeleteRelease(owner: String, repo: String, releaseId: Long): Unit =
        client.delete("/repos/$owner/$repo/releases/$releaseId").body()

    override suspend fun reposUpdateRelease(owner: String, repo: String, releaseId: Long, body: Repos.Releases.ReposUpdateReleaseBody?): Repos.Releases.ReposUpdateReleaseResult {
        val response = client.patch("/repos/$owner/$repo/releases/$releaseId") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Releases.ReposUpdateReleaseResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Releases.ReposUpdateReleaseResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposReleasesAssets(private val client: HttpClient) : Repos.Releases.Assets {
    override suspend fun reposGetReleaseAsset(owner: String, repo: String, assetId: Long): Repos.Releases.Assets.ReposGetReleaseAssetResult {
        val response = client.get("/repos/$owner/$repo/releases/assets/$assetId")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Releases.Assets.ReposGetReleaseAssetResult.OK(response.body())
            HttpStatusCode.Found -> Repos.Releases.Assets.ReposGetReleaseAssetResult.Found
            HttpStatusCode.NotFound -> Repos.Releases.Assets.ReposGetReleaseAssetResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposDeleteReleaseAsset(owner: String, repo: String, assetId: Long): Unit =
        client.delete("/repos/$owner/$repo/releases/assets/$assetId").body()

    override suspend fun reposUpdateReleaseAsset(owner: String, repo: String, assetId: Long, body: Repos.Releases.Assets.ReposUpdateReleaseAssetBody?): ReleaseAsset =
        client.patch("/repos/$owner/$repo/releases/assets/$assetId") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }.body()

    override suspend fun reposListReleaseAssets(owner: String, repo: String, releaseId: Long, page: Long, perPage: Long): List<ReleaseAsset> =
        client.get("/repos/$owner/$repo/releases/$releaseId/assets") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()

    override suspend fun reposUploadReleaseAsset(owner: String, repo: String, releaseId: Long, name: String, label: String?, body: ByteArray?): Repos.Releases.Assets.ReposUploadReleaseAssetResult {
        val response = client.post("/repos/$owner/$repo/releases/$releaseId/assets") {
            parameter("name", name)
            label?.let { parameter("label", it) }
            contentType(ContentType.Application.OctetStream)
            body?.let { setBody(it) }
        }
        return when (response.status) {
            HttpStatusCode.Created -> Repos.Releases.Assets.ReposUploadReleaseAssetResult.Created(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Releases.Assets.ReposUploadReleaseAssetResult.UnprocessableEntity
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposReleasesGenerateNotes(private val client: HttpClient) : Repos.Releases.GenerateNotes {
    override suspend fun reposGenerateReleaseNotes(owner: String, repo: String, body: Repos.Releases.GenerateNotes.ReposGenerateReleaseNotesBody): Repos.Releases.GenerateNotes.ReposGenerateReleaseNotesResult {
        val response = client.post("/repos/$owner/$repo/releases/generate-notes") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Releases.GenerateNotes.ReposGenerateReleaseNotesResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Releases.GenerateNotes.ReposGenerateReleaseNotesResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposReleasesLatest(private val client: HttpClient) : Repos.Releases.Latest {
    override suspend fun reposGetLatestRelease(owner: String, repo: String): Release =
        client.get("/repos/$owner/$repo/releases/latest").body()
}

internal class KtorReposReleasesTagsApi(private val client: HttpClient) : Repos.Releases.TagsApi {
    override suspend fun reposGetReleaseByTag(owner: String, repo: String, tag: String): Repos.Releases.TagsApi.ReposGetReleaseByTagResult {
        val response = client.get("/repos/$owner/$repo/releases/tags/$tag")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Releases.TagsApi.ReposGetReleaseByTagResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Releases.TagsApi.ReposGetReleaseByTagResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposReleasesReactions(private val client: HttpClient) : Repos.Releases.Reactions {
    override suspend fun reactionsListForRelease(owner: String, repo: String, releaseId: Long, page: Long, perPage: Long, content: Repos.Releases.Reactions.Content?): Repos.Releases.Reactions.ReactionsListForReleaseResult {
        val response = client.get("/repos/$owner/$repo/releases/$releaseId/reactions") {
            parameter("page", page)
            parameter("per_page", perPage)
            content?.let { parameter("content", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Releases.Reactions.ReactionsListForReleaseResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Releases.Reactions.ReactionsListForReleaseResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reactionsCreateForRelease(owner: String, repo: String, releaseId: Long, body: Repos.Releases.Reactions.ReactionsCreateForReleaseBody): Repos.Releases.Reactions.ReactionsCreateForReleaseResult {
        val response = client.post("/repos/$owner/$repo/releases/$releaseId/reactions") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Releases.Reactions.ReactionsCreateForReleaseResult.OK(response.body())
            HttpStatusCode.Created -> Repos.Releases.Reactions.ReactionsCreateForReleaseResult.Created(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Releases.Reactions.ReactionsCreateForReleaseResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reactionsDeleteForRelease(owner: String, repo: String, releaseId: Long, reactionId: Long): Unit =
        client.delete("/repos/$owner/$repo/releases/$releaseId/reactions/$reactionId").body()
}

internal class KtorReposRules(private val client: HttpClient) : Repos.Rules {
    override val branches: Repos.Rules.BranchesApi = KtorReposRulesBranchesApi(client)
}

internal class KtorReposRulesBranchesApi(private val client: HttpClient) : Repos.Rules.BranchesApi {
    override suspend fun reposGetBranchRules(owner: String, repo: String, branch: String, page: Long, perPage: Long): List<RepositoryRuleDetailed> =
        client.get("/repos/$owner/$repo/rules/branches/$branch") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()
}

internal class KtorReposRulesets(private val client: HttpClient) : Repos.Rulesets {
    override val ruleSuites: Repos.Rulesets.RuleSuites = KtorReposRulesetsRuleSuites(client)

    override val history: Repos.Rulesets.History = KtorReposRulesetsHistory(client)

    override suspend fun reposGetRepoRulesets(owner: String, repo: String, includesParents: Boolean, page: Long, perPage: Long, targets: String?): Repos.Rulesets.ReposGetRepoRulesetsResult {
        val response = client.get("/repos/$owner/$repo/rulesets") {
            parameter("includes_parents", includesParents)
            parameter("page", page)
            parameter("per_page", perPage)
            targets?.let { parameter("targets", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Rulesets.ReposGetRepoRulesetsResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Rulesets.ReposGetRepoRulesetsResult.NotFound(response.body())
            HttpStatusCode.InternalServerError -> Repos.Rulesets.ReposGetRepoRulesetsResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposCreateRepoRuleset(owner: String, repo: String, body: Repos.Rulesets.ReposCreateRepoRulesetBody): Repos.Rulesets.ReposCreateRepoRulesetResult {
        val response = client.post("/repos/$owner/$repo/rulesets") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> Repos.Rulesets.ReposCreateRepoRulesetResult.Created(response.body())
            HttpStatusCode.NotFound -> Repos.Rulesets.ReposCreateRepoRulesetResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Rulesets.ReposCreateRepoRulesetResult.UnprocessableEntity(response.body())
            HttpStatusCode.InternalServerError -> Repos.Rulesets.ReposCreateRepoRulesetResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposGetRepoRuleset(owner: String, repo: String, rulesetId: Long, includesParents: Boolean): Repos.Rulesets.ReposGetRepoRulesetResult {
        val response = client.get("/repos/$owner/$repo/rulesets/$rulesetId") {
            parameter("includes_parents", includesParents)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Rulesets.ReposGetRepoRulesetResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Rulesets.ReposGetRepoRulesetResult.NotFound(response.body())
            HttpStatusCode.InternalServerError -> Repos.Rulesets.ReposGetRepoRulesetResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposUpdateRepoRuleset(owner: String, repo: String, rulesetId: Long, body: Repos.Rulesets.ReposUpdateRepoRulesetBody?): Repos.Rulesets.ReposUpdateRepoRulesetResult {
        val response = client.put("/repos/$owner/$repo/rulesets/$rulesetId") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Rulesets.ReposUpdateRepoRulesetResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Rulesets.ReposUpdateRepoRulesetResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Rulesets.ReposUpdateRepoRulesetResult.UnprocessableEntity(response.body())
            HttpStatusCode.InternalServerError -> Repos.Rulesets.ReposUpdateRepoRulesetResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposDeleteRepoRuleset(owner: String, repo: String, rulesetId: Long): Repos.Rulesets.ReposDeleteRepoRulesetResult {
        val response = client.delete("/repos/$owner/$repo/rulesets/$rulesetId")
        return when (response.status) {
            HttpStatusCode.NoContent -> Repos.Rulesets.ReposDeleteRepoRulesetResult.NoContent
            HttpStatusCode.NotFound -> Repos.Rulesets.ReposDeleteRepoRulesetResult.NotFound(response.body())
            HttpStatusCode.InternalServerError -> Repos.Rulesets.ReposDeleteRepoRulesetResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposRulesetsRuleSuites(private val client: HttpClient) : Repos.Rulesets.RuleSuites {
    override suspend fun reposGetRepoRuleSuites(owner: String, repo: String, page: Long, perPage: Long, ruleSuiteResult: Repos.Rulesets.RuleSuites.RuleSuiteResult, timePeriod: Repos.Rulesets.RuleSuites.TimePeriod, actorName: String?, ref: String?): Repos.Rulesets.RuleSuites.ReposGetRepoRuleSuitesResult {
        val response = client.get("/repos/$owner/$repo/rulesets/rule-suites") {
            parameter("page", page)
            parameter("per_page", perPage)
            parameter("rule_suite_result", ruleSuiteResult)
            parameter("time_period", timePeriod)
            actorName?.let { parameter("actor_name", it) }
            ref?.let { parameter("ref", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Rulesets.RuleSuites.ReposGetRepoRuleSuitesResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Rulesets.RuleSuites.ReposGetRepoRuleSuitesResult.NotFound(response.body())
            HttpStatusCode.InternalServerError -> Repos.Rulesets.RuleSuites.ReposGetRepoRuleSuitesResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposGetRepoRuleSuite(owner: String, repo: String, ruleSuiteId: Long): Repos.Rulesets.RuleSuites.ReposGetRepoRuleSuiteResult {
        val response = client.get("/repos/$owner/$repo/rulesets/rule-suites/$ruleSuiteId")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Rulesets.RuleSuites.ReposGetRepoRuleSuiteResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Rulesets.RuleSuites.ReposGetRepoRuleSuiteResult.NotFound(response.body())
            HttpStatusCode.InternalServerError -> Repos.Rulesets.RuleSuites.ReposGetRepoRuleSuiteResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposRulesetsHistory(private val client: HttpClient) : Repos.Rulesets.History {
    override suspend fun reposGetRepoRulesetHistory(owner: String, repo: String, rulesetId: Long, page: Long, perPage: Long): Repos.Rulesets.History.ReposGetRepoRulesetHistoryResult {
        val response = client.get("/repos/$owner/$repo/rulesets/$rulesetId/history") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Rulesets.History.ReposGetRepoRulesetHistoryResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Rulesets.History.ReposGetRepoRulesetHistoryResult.NotFound(response.body())
            HttpStatusCode.InternalServerError -> Repos.Rulesets.History.ReposGetRepoRulesetHistoryResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposGetRepoRulesetVersion(owner: String, repo: String, rulesetId: Long, versionId: Long): Repos.Rulesets.History.ReposGetRepoRulesetVersionResult {
        val response = client.get("/repos/$owner/$repo/rulesets/$rulesetId/history/$versionId")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Rulesets.History.ReposGetRepoRulesetVersionResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Rulesets.History.ReposGetRepoRulesetVersionResult.NotFound(response.body())
            HttpStatusCode.InternalServerError -> Repos.Rulesets.History.ReposGetRepoRulesetVersionResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposSecretScanning(private val client: HttpClient) : Repos.SecretScanning {
    override val alerts: Repos.SecretScanning.Alerts = KtorReposSecretScanningAlerts(client)

    override val pushProtectionBypasses: Repos.SecretScanning.PushProtectionBypasses = KtorReposSecretScanningPushProtectionBypasses(client)

    override val scanHistory: Repos.SecretScanning.ScanHistory = KtorReposSecretScanningScanHistory(client)
}

internal class KtorReposSecretScanningAlerts(private val client: HttpClient) : Repos.SecretScanning.Alerts {
    override val locations: Repos.SecretScanning.Alerts.Locations = KtorReposSecretScanningAlertsLocations(client)

    override suspend fun secretScanningListAlertsForRepo(owner: String, repo: String, direction: Repos.SecretScanning.Alerts.Direction, hideSecret: Boolean, isMultiRepo: Boolean, isPubliclyLeaked: Boolean, page: Long, perPage: Long, sort: Repos.SecretScanning.Alerts.Sort, after: String?, assignee: String?, before: String?, resolution: String?, secretType: String?, state: Repos.SecretScanning.Alerts.State?, validity: String?): Repos.SecretScanning.Alerts.SecretScanningListAlertsForRepoResult {
        val response = client.get("/repos/$owner/$repo/secret-scanning/alerts") {
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
            HttpStatusCode.OK -> Repos.SecretScanning.Alerts.SecretScanningListAlertsForRepoResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.SecretScanning.Alerts.SecretScanningListAlertsForRepoResult.NotFound
            HttpStatusCode.ServiceUnavailable -> Repos.SecretScanning.Alerts.SecretScanningListAlertsForRepoResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun secretScanningGetAlert(owner: String, repo: String, alertNumber: AlertNumberRequest, hideSecret: Boolean): Repos.SecretScanning.Alerts.SecretScanningGetAlertResult {
        val response = client.get("/repos/$owner/$repo/secret-scanning/alerts/$alertNumber") {
            parameter("hide_secret", hideSecret)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.SecretScanning.Alerts.SecretScanningGetAlertResult.OK(response.body())
            HttpStatusCode.NotModified -> Repos.SecretScanning.Alerts.SecretScanningGetAlertResult.NotModified
            HttpStatusCode.NotFound -> Repos.SecretScanning.Alerts.SecretScanningGetAlertResult.NotFound
            HttpStatusCode.ServiceUnavailable -> Repos.SecretScanning.Alerts.SecretScanningGetAlertResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun secretScanningUpdateAlert(owner: String, repo: String, alertNumber: AlertNumberRequest, body: Repos.SecretScanning.Alerts.SecretScanningUpdateAlertBody): Repos.SecretScanning.Alerts.SecretScanningUpdateAlertResult {
        val response = client.patch("/repos/$owner/$repo/secret-scanning/alerts/$alertNumber") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.SecretScanning.Alerts.SecretScanningUpdateAlertResult.OK(response.body())
            HttpStatusCode.BadRequest -> Repos.SecretScanning.Alerts.SecretScanningUpdateAlertResult.BadRequest
            HttpStatusCode.NotFound -> Repos.SecretScanning.Alerts.SecretScanningUpdateAlertResult.NotFound
            HttpStatusCode.UnprocessableEntity -> Repos.SecretScanning.Alerts.SecretScanningUpdateAlertResult.UnprocessableEntity
            HttpStatusCode.ServiceUnavailable -> Repos.SecretScanning.Alerts.SecretScanningUpdateAlertResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposSecretScanningAlertsLocations(private val client: HttpClient) : Repos.SecretScanning.Alerts.Locations {
    override suspend fun secretScanningListLocationsForAlert(owner: String, repo: String, alertNumber: AlertNumberRequest, page: Long, perPage: Long): Repos.SecretScanning.Alerts.Locations.SecretScanningListLocationsForAlertResult {
        val response = client.get("/repos/$owner/$repo/secret-scanning/alerts/$alertNumber/locations") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.SecretScanning.Alerts.Locations.SecretScanningListLocationsForAlertResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.SecretScanning.Alerts.Locations.SecretScanningListLocationsForAlertResult.NotFound
            HttpStatusCode.ServiceUnavailable -> Repos.SecretScanning.Alerts.Locations.SecretScanningListLocationsForAlertResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposSecretScanningPushProtectionBypasses(private val client: HttpClient) : Repos.SecretScanning.PushProtectionBypasses {
    override suspend fun secretScanningCreatePushProtectionBypass(owner: String, repo: String, body: Repos.SecretScanning.PushProtectionBypasses.SecretScanningCreatePushProtectionBypassBody): Repos.SecretScanning.PushProtectionBypasses.SecretScanningCreatePushProtectionBypassResult {
        val response = client.post("/repos/$owner/$repo/secret-scanning/push-protection-bypasses") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.SecretScanning.PushProtectionBypasses.SecretScanningCreatePushProtectionBypassResult.OK(response.body())
            HttpStatusCode.Forbidden -> Repos.SecretScanning.PushProtectionBypasses.SecretScanningCreatePushProtectionBypassResult.Forbidden
            HttpStatusCode.NotFound -> Repos.SecretScanning.PushProtectionBypasses.SecretScanningCreatePushProtectionBypassResult.NotFound
            HttpStatusCode.UnprocessableEntity -> Repos.SecretScanning.PushProtectionBypasses.SecretScanningCreatePushProtectionBypassResult.UnprocessableEntity
            HttpStatusCode.ServiceUnavailable -> Repos.SecretScanning.PushProtectionBypasses.SecretScanningCreatePushProtectionBypassResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposSecretScanningScanHistory(private val client: HttpClient) : Repos.SecretScanning.ScanHistory {
    override suspend fun secretScanningGetScanHistory(owner: String, repo: String): Repos.SecretScanning.ScanHistory.SecretScanningGetScanHistoryResult {
        val response = client.get("/repos/$owner/$repo/secret-scanning/scan-history")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.SecretScanning.ScanHistory.SecretScanningGetScanHistoryResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.SecretScanning.ScanHistory.SecretScanningGetScanHistoryResult.NotFound
            HttpStatusCode.ServiceUnavailable -> Repos.SecretScanning.ScanHistory.SecretScanningGetScanHistoryResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposSecurityAdvisories(private val client: HttpClient) : Repos.SecurityAdvisories {
    override val reports: Repos.SecurityAdvisories.Reports = KtorReposSecurityAdvisoriesReports(client)

    override val cve: Repos.SecurityAdvisories.Cve = KtorReposSecurityAdvisoriesCve(client)

    override val forks: Repos.SecurityAdvisories.ForksApi = KtorReposSecurityAdvisoriesForksApi(client)

    override suspend fun securityAdvisoriesListRepositoryAdvisories(owner: String, repo: String, direction: Repos.SecurityAdvisories.Direction, perPage: Long, sort: Repos.SecurityAdvisories.Sort, after: String?, before: String?, state: Repos.SecurityAdvisories.State?): Repos.SecurityAdvisories.SecurityAdvisoriesListRepositoryAdvisoriesResult {
        val response = client.get("/repos/$owner/$repo/security-advisories") {
            parameter("direction", direction)
            parameter("per_page", perPage)
            parameter("sort", sort)
            after?.let { parameter("after", it) }
            before?.let { parameter("before", it) }
            state?.let { parameter("state", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.SecurityAdvisories.SecurityAdvisoriesListRepositoryAdvisoriesResult.OK(response.body())
            HttpStatusCode.BadRequest -> Repos.SecurityAdvisories.SecurityAdvisoriesListRepositoryAdvisoriesResult.BadRequest(response.body())
            HttpStatusCode.NotFound -> Repos.SecurityAdvisories.SecurityAdvisoriesListRepositoryAdvisoriesResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun securityAdvisoriesCreateRepositoryAdvisory(owner: String, repo: String, body: RepositoryAdvisoryCreate): Repos.SecurityAdvisories.SecurityAdvisoriesCreateRepositoryAdvisoryResult {
        val response = client.post("/repos/$owner/$repo/security-advisories") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> Repos.SecurityAdvisories.SecurityAdvisoriesCreateRepositoryAdvisoryResult.Created(response.body())
            HttpStatusCode.Forbidden -> Repos.SecurityAdvisories.SecurityAdvisoriesCreateRepositoryAdvisoryResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.SecurityAdvisories.SecurityAdvisoriesCreateRepositoryAdvisoryResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.SecurityAdvisories.SecurityAdvisoriesCreateRepositoryAdvisoryResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun securityAdvisoriesGetRepositoryAdvisory(owner: String, repo: String, ghsaId: String): Repos.SecurityAdvisories.SecurityAdvisoriesGetRepositoryAdvisoryResult {
        val response = client.get("/repos/$owner/$repo/security-advisories/$ghsaId")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.SecurityAdvisories.SecurityAdvisoriesGetRepositoryAdvisoryResult.OK(response.body())
            HttpStatusCode.Forbidden -> Repos.SecurityAdvisories.SecurityAdvisoriesGetRepositoryAdvisoryResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.SecurityAdvisories.SecurityAdvisoriesGetRepositoryAdvisoryResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun securityAdvisoriesUpdateRepositoryAdvisory(owner: String, repo: String, ghsaId: String, body: RepositoryAdvisoryUpdate): Repos.SecurityAdvisories.SecurityAdvisoriesUpdateRepositoryAdvisoryResult {
        val response = client.patch("/repos/$owner/$repo/security-advisories/$ghsaId") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.SecurityAdvisories.SecurityAdvisoriesUpdateRepositoryAdvisoryResult.OK(response.body())
            HttpStatusCode.Forbidden -> Repos.SecurityAdvisories.SecurityAdvisoriesUpdateRepositoryAdvisoryResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.SecurityAdvisories.SecurityAdvisoriesUpdateRepositoryAdvisoryResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.SecurityAdvisories.SecurityAdvisoriesUpdateRepositoryAdvisoryResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposSecurityAdvisoriesReports(private val client: HttpClient) : Repos.SecurityAdvisories.Reports {
    override suspend fun securityAdvisoriesCreatePrivateVulnerabilityReport(owner: String, repo: String, body: PrivateVulnerabilityReportCreate): Repos.SecurityAdvisories.Reports.SecurityAdvisoriesCreatePrivateVulnerabilityReportResult {
        val response = client.post("/repos/$owner/$repo/security-advisories/reports") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> Repos.SecurityAdvisories.Reports.SecurityAdvisoriesCreatePrivateVulnerabilityReportResult.Created(response.body())
            HttpStatusCode.Forbidden -> Repos.SecurityAdvisories.Reports.SecurityAdvisoriesCreatePrivateVulnerabilityReportResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.SecurityAdvisories.Reports.SecurityAdvisoriesCreatePrivateVulnerabilityReportResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.SecurityAdvisories.Reports.SecurityAdvisoriesCreatePrivateVulnerabilityReportResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposSecurityAdvisoriesCve(private val client: HttpClient) : Repos.SecurityAdvisories.Cve {
    override suspend fun securityAdvisoriesCreateRepositoryAdvisoryCveRequest(owner: String, repo: String, ghsaId: String): Repos.SecurityAdvisories.Cve.SecurityAdvisoriesCreateRepositoryAdvisoryCveRequestResult {
        val response = client.post("/repos/$owner/$repo/security-advisories/$ghsaId/cve")
        return when (response.status) {
            HttpStatusCode.Accepted -> Repos.SecurityAdvisories.Cve.SecurityAdvisoriesCreateRepositoryAdvisoryCveRequestResult.Accepted(response.body())
            HttpStatusCode.BadRequest -> Repos.SecurityAdvisories.Cve.SecurityAdvisoriesCreateRepositoryAdvisoryCveRequestResult.BadRequest(response.body())
            HttpStatusCode.Forbidden -> Repos.SecurityAdvisories.Cve.SecurityAdvisoriesCreateRepositoryAdvisoryCveRequestResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.SecurityAdvisories.Cve.SecurityAdvisoriesCreateRepositoryAdvisoryCveRequestResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.SecurityAdvisories.Cve.SecurityAdvisoriesCreateRepositoryAdvisoryCveRequestResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposSecurityAdvisoriesForksApi(private val client: HttpClient) : Repos.SecurityAdvisories.ForksApi {
    override suspend fun securityAdvisoriesCreateFork(owner: String, repo: String, ghsaId: String): Repos.SecurityAdvisories.ForksApi.SecurityAdvisoriesCreateForkResult {
        val response = client.post("/repos/$owner/$repo/security-advisories/$ghsaId/forks")
        return when (response.status) {
            HttpStatusCode.Accepted -> Repos.SecurityAdvisories.ForksApi.SecurityAdvisoriesCreateForkResult.Accepted(response.body())
            HttpStatusCode.BadRequest -> Repos.SecurityAdvisories.ForksApi.SecurityAdvisoriesCreateForkResult.BadRequest(response.body())
            HttpStatusCode.Forbidden -> Repos.SecurityAdvisories.ForksApi.SecurityAdvisoriesCreateForkResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.SecurityAdvisories.ForksApi.SecurityAdvisoriesCreateForkResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.SecurityAdvisories.ForksApi.SecurityAdvisoriesCreateForkResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposStargazers(private val client: HttpClient) : Repos.Stargazers {
    override suspend fun activityListStargazersForRepo(owner: String, repo: String, page: Long, perPage: Long): Repos.Stargazers.ActivityListStargazersForRepoResult {
        val response = client.get("/repos/$owner/$repo/stargazers") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Stargazers.ActivityListStargazersForRepoResult.OK(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Stargazers.ActivityListStargazersForRepoResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposStats(private val client: HttpClient) : Repos.Stats {
    override val codeFrequency: Repos.Stats.CodeFrequency = KtorReposStatsCodeFrequency(client)

    override val commitActivity: Repos.Stats.CommitActivity = KtorReposStatsCommitActivity(client)

    override val contributors: Repos.Stats.ContributorsApi = KtorReposStatsContributorsApi(client)

    override val participation: Repos.Stats.Participation = KtorReposStatsParticipation(client)

    override val punchCard: Repos.Stats.PunchCard = KtorReposStatsPunchCard(client)
}

internal class KtorReposStatsCodeFrequency(private val client: HttpClient) : Repos.Stats.CodeFrequency {
    override suspend fun reposGetCodeFrequencyStats(owner: String, repo: String): Repos.Stats.CodeFrequency.ReposGetCodeFrequencyStatsResult {
        val response = client.get("/repos/$owner/$repo/stats/code_frequency")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Stats.CodeFrequency.ReposGetCodeFrequencyStatsResult.OK(response.body())
            HttpStatusCode.Accepted -> Repos.Stats.CodeFrequency.ReposGetCodeFrequencyStatsResult.Accepted(response.body())
            HttpStatusCode.NoContent -> Repos.Stats.CodeFrequency.ReposGetCodeFrequencyStatsResult.NoContent
            HttpStatusCode.UnprocessableEntity -> Repos.Stats.CodeFrequency.ReposGetCodeFrequencyStatsResult.UnprocessableEntity
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposStatsCommitActivity(private val client: HttpClient) : Repos.Stats.CommitActivity {
    override suspend fun reposGetCommitActivityStats(owner: String, repo: String): Repos.Stats.CommitActivity.ReposGetCommitActivityStatsResult {
        val response = client.get("/repos/$owner/$repo/stats/commit_activity")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Stats.CommitActivity.ReposGetCommitActivityStatsResult.OK(response.body())
            HttpStatusCode.Accepted -> Repos.Stats.CommitActivity.ReposGetCommitActivityStatsResult.Accepted(response.body())
            HttpStatusCode.NoContent -> Repos.Stats.CommitActivity.ReposGetCommitActivityStatsResult.NoContent
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposStatsContributorsApi(private val client: HttpClient) : Repos.Stats.ContributorsApi {
    override suspend fun reposGetContributorsStats(owner: String, repo: String): Repos.Stats.ContributorsApi.ReposGetContributorsStatsResult {
        val response = client.get("/repos/$owner/$repo/stats/contributors")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Stats.ContributorsApi.ReposGetContributorsStatsResult.OK(response.body())
            HttpStatusCode.Accepted -> Repos.Stats.ContributorsApi.ReposGetContributorsStatsResult.Accepted(response.body())
            HttpStatusCode.NoContent -> Repos.Stats.ContributorsApi.ReposGetContributorsStatsResult.NoContent
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposStatsParticipation(private val client: HttpClient) : Repos.Stats.Participation {
    override suspend fun reposGetParticipationStats(owner: String, repo: String): Repos.Stats.Participation.ReposGetParticipationStatsResult {
        val response = client.get("/repos/$owner/$repo/stats/participation")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Stats.Participation.ReposGetParticipationStatsResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Stats.Participation.ReposGetParticipationStatsResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposStatsPunchCard(private val client: HttpClient) : Repos.Stats.PunchCard {
    override suspend fun reposGetPunchCardStats(owner: String, repo: String): Repos.Stats.PunchCard.ReposGetPunchCardStatsResult {
        val response = client.get("/repos/$owner/$repo/stats/punch_card")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Stats.PunchCard.ReposGetPunchCardStatsResult.OK(response.body())
            HttpStatusCode.NoContent -> Repos.Stats.PunchCard.ReposGetPunchCardStatsResult.NoContent
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposStatuses(private val client: HttpClient) : Repos.Statuses {
    override suspend fun reposCreateCommitStatus(owner: String, repo: String, sha: String, body: Repos.Statuses.ReposCreateCommitStatusBody): Status =
        client.post("/repos/$owner/$repo/statuses/$sha") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()
}

internal class KtorReposSubscribers(private val client: HttpClient) : Repos.Subscribers {
    override suspend fun activityListWatchersForRepo(owner: String, repo: String, page: Long, perPage: Long): List<SimpleUser> =
        client.get("/repos/$owner/$repo/subscribers") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()
}

internal class KtorReposSubscription(private val client: HttpClient) : Repos.Subscription {
    override suspend fun activityGetRepoSubscription(owner: String, repo: String): Repos.Subscription.ActivityGetRepoSubscriptionResult {
        val response = client.get("/repos/$owner/$repo/subscription")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Subscription.ActivityGetRepoSubscriptionResult.OK(response.body())
            HttpStatusCode.Forbidden -> Repos.Subscription.ActivityGetRepoSubscriptionResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repos.Subscription.ActivityGetRepoSubscriptionResult.NotFound
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun activitySetRepoSubscription(owner: String, repo: String, body: Repos.Subscription.ActivitySetRepoSubscriptionBody?): RepositorySubscription =
        client.put("/repos/$owner/$repo/subscription") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }.body()

    override suspend fun activityDeleteRepoSubscription(owner: String, repo: String): Unit =
        client.delete("/repos/$owner/$repo/subscription").body()
}

internal class KtorReposTags(private val client: HttpClient) : Repos.Tags {
    override suspend fun reposListTags(owner: String, repo: String, page: Long, perPage: Long): List<Tag> =
        client.get("/repos/$owner/$repo/tags") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()
}

internal class KtorReposTarball(private val client: HttpClient) : Repos.Tarball {
    override suspend fun reposDownloadTarballArchive(owner: String, repo: String, ref: String): Unit =
        client.get("/repos/$owner/$repo/tarball/$ref").body()
}

internal class KtorReposTeams(private val client: HttpClient) : Repos.Teams {
    override suspend fun reposListTeams(owner: String, repo: String, page: Long, perPage: Long): Repos.Teams.ReposListTeamsResult {
        val response = client.get("/repos/$owner/$repo/teams") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Teams.ReposListTeamsResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Teams.ReposListTeamsResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposTopics(private val client: HttpClient) : Repos.Topics {
    override suspend fun reposGetAllTopics(owner: String, repo: String, page: Long, perPage: Long): Repos.Topics.ReposGetAllTopicsResult {
        val response = client.get("/repos/$owner/$repo/topics") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Topics.ReposGetAllTopicsResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Topics.ReposGetAllTopicsResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposReplaceAllTopics(owner: String, repo: String, body: Repos.Topics.ReposReplaceAllTopicsBody): Repos.Topics.ReposReplaceAllTopicsResult {
        val response = client.put("/repos/$owner/$repo/topics") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Topics.ReposReplaceAllTopicsResult.OK(response.body())
            HttpStatusCode.NotFound -> Repos.Topics.ReposReplaceAllTopicsResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Repos.Topics.ReposReplaceAllTopicsResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposTraffic(private val client: HttpClient) : Repos.Traffic {
    override val clones: Repos.Traffic.Clones = KtorReposTrafficClones(client)

    override val popular: Repos.Traffic.Popular = KtorReposTrafficPopular(client)

    override val views: Repos.Traffic.Views = KtorReposTrafficViews(client)
}

internal class KtorReposTrafficClones(private val client: HttpClient) : Repos.Traffic.Clones {
    override suspend fun reposGetClones(owner: String, repo: String, per: Repos.Traffic.Clones.Per): Repos.Traffic.Clones.ReposGetClonesResult {
        val response = client.get("/repos/$owner/$repo/traffic/clones") {
            parameter("per", per)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Traffic.Clones.ReposGetClonesResult.OK(response.body())
            HttpStatusCode.Forbidden -> Repos.Traffic.Clones.ReposGetClonesResult.Forbidden(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposTrafficPopular(private val client: HttpClient) : Repos.Traffic.Popular {
    override val paths: Repos.Traffic.Popular.Paths = KtorReposTrafficPopularPaths(client)

    override val referrers: Repos.Traffic.Popular.Referrers = KtorReposTrafficPopularReferrers(client)
}

internal class KtorReposTrafficPopularPaths(private val client: HttpClient) : Repos.Traffic.Popular.Paths {
    override suspend fun reposGetTopPaths(owner: String, repo: String): Repos.Traffic.Popular.Paths.ReposGetTopPathsResult {
        val response = client.get("/repos/$owner/$repo/traffic/popular/paths")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Traffic.Popular.Paths.ReposGetTopPathsResult.OK(response.body())
            HttpStatusCode.Forbidden -> Repos.Traffic.Popular.Paths.ReposGetTopPathsResult.Forbidden(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposTrafficPopularReferrers(private val client: HttpClient) : Repos.Traffic.Popular.Referrers {
    override suspend fun reposGetTopReferrers(owner: String, repo: String): Repos.Traffic.Popular.Referrers.ReposGetTopReferrersResult {
        val response = client.get("/repos/$owner/$repo/traffic/popular/referrers")
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Traffic.Popular.Referrers.ReposGetTopReferrersResult.OK(response.body())
            HttpStatusCode.Forbidden -> Repos.Traffic.Popular.Referrers.ReposGetTopReferrersResult.Forbidden(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposTrafficViews(private val client: HttpClient) : Repos.Traffic.Views {
    override suspend fun reposGetViews(owner: String, repo: String, per: Repos.Traffic.Views.Per): Repos.Traffic.Views.ReposGetViewsResult {
        val response = client.get("/repos/$owner/$repo/traffic/views") {
            parameter("per", per)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repos.Traffic.Views.ReposGetViewsResult.OK(response.body())
            HttpStatusCode.Forbidden -> Repos.Traffic.Views.ReposGetViewsResult.Forbidden(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorReposTransfer(private val client: HttpClient) : Repos.Transfer {
    override suspend fun reposTransfer(owner: String, repo: String, body: Repos.Transfer.ReposTransferBody): MinimalRepository =
        client.post("/repos/$owner/$repo/transfer") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()
}

internal class KtorReposVulnerabilityAlerts(private val client: HttpClient) : Repos.VulnerabilityAlerts {
    override suspend fun reposCheckVulnerabilityAlerts(owner: String, repo: String): Repos.VulnerabilityAlerts.ReposCheckVulnerabilityAlertsResult {
        val response = client.get("/repos/$owner/$repo/vulnerability-alerts")
        return when (response.status) {
            HttpStatusCode.NoContent -> Repos.VulnerabilityAlerts.ReposCheckVulnerabilityAlertsResult.NoContent
            HttpStatusCode.NotFound -> Repos.VulnerabilityAlerts.ReposCheckVulnerabilityAlertsResult.NotFound
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposEnableVulnerabilityAlerts(owner: String, repo: String): Unit =
        client.put("/repos/$owner/$repo/vulnerability-alerts").body()

    override suspend fun reposDisableVulnerabilityAlerts(owner: String, repo: String): Unit =
        client.delete("/repos/$owner/$repo/vulnerability-alerts").body()
}

internal class KtorReposZipball(private val client: HttpClient) : Repos.Zipball {
    override suspend fun reposDownloadZipballArchive(owner: String, repo: String, ref: String): Unit =
        client.get("/repos/$owner/$repo/zipball/$ref").body()
}

internal class KtorReposGenerate(private val client: HttpClient) : Repos.Generate {
    override suspend fun reposCreateUsingTemplate(templateOwner: String, templateRepo: String, body: Repos.Generate.ReposCreateUsingTemplateBody): FullRepository =
        client.post("/repos/$templateOwner/$templateRepo/generate") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()
}
