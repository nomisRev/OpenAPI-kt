package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement

/**
 * A repository rule with ruleset details.
 */
@Serializable(with = RepositoryRuleDetailed.Serializer::class)
public sealed interface RepositoryRuleDetailed {
  /**
   * Only allow users with bypass permission to create matching refs.
   */
  @Serializable
  public data class RepositoryRuleCreationOrRepositoryRuleRulesetInfo(
    public val type: Type,
    @SerialName("ruleset_source_type")
    public val rulesetSourceType: RulesetSourceType? = null,
    @SerialName("ruleset_source")
    public val rulesetSource: String? = null,
    @SerialName("ruleset_id")
    public val rulesetId: Long? = null,
  ) : RepositoryRuleDetailed {
    @Serializable
    public enum class RulesetSourceType {
      Repository,
      Organization,
    }

    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("creation")
      Creation("creation"),
      ;
    }
  }

  /**
   * Only allow users with bypass permission to update matching refs.
   */
  @Serializable
  public data class RepositoryRuleUpdateOrRepositoryRuleRulesetInfo(
    public val type: Type,
    public val parameters: Parameters? = null,
    @SerialName("ruleset_source_type")
    public val rulesetSourceType: RulesetSourceType? = null,
    @SerialName("ruleset_source")
    public val rulesetSource: String? = null,
    @SerialName("ruleset_id")
    public val rulesetId: Long? = null,
  ) : RepositoryRuleDetailed {
    @JvmInline
    @Serializable
    public value class Parameters(
      @SerialName("update_allows_fetch_and_merge")
      public val updateAllowsFetchAndMerge: Boolean,
    )

    @Serializable
    public enum class RulesetSourceType {
      Repository,
      Organization,
    }

    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("update")
      Update("update"),
      ;
    }
  }

  /**
   * Only allow users with bypass permissions to delete matching refs.
   */
  @Serializable
  public data class RepositoryRuleDeletionOrRepositoryRuleRulesetInfo(
    public val type: Type,
    @SerialName("ruleset_source_type")
    public val rulesetSourceType: RulesetSourceType? = null,
    @SerialName("ruleset_source")
    public val rulesetSource: String? = null,
    @SerialName("ruleset_id")
    public val rulesetId: Long? = null,
  ) : RepositoryRuleDetailed {
    @Serializable
    public enum class RulesetSourceType {
      Repository,
      Organization,
    }

    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("deletion")
      Deletion("deletion"),
      ;
    }
  }

  /**
   * Prevent merge commits from being pushed to matching refs.
   */
  @Serializable
  public data class RepositoryRuleRequiredLinearHistoryOrRepositoryRuleRulesetInfo(
    public val type: Type,
    @SerialName("ruleset_source_type")
    public val rulesetSourceType: RulesetSourceType? = null,
    @SerialName("ruleset_source")
    public val rulesetSource: String? = null,
    @SerialName("ruleset_id")
    public val rulesetId: Long? = null,
  ) : RepositoryRuleDetailed {
    @Serializable
    public enum class RulesetSourceType {
      Repository,
      Organization,
    }

    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("required_linear_history")
      RequiredLinearHistory("required_linear_history"),
      ;
    }
  }

  /**
   * Merges must be performed via a merge queue.
   */
  @Serializable
  public data class RepositoryRuleMergeQueueOrRepositoryRuleRulesetInfo(
    public val type: Type,
    public val parameters: Parameters? = null,
    @SerialName("ruleset_source_type")
    public val rulesetSourceType: RulesetSourceType? = null,
    @SerialName("ruleset_source")
    public val rulesetSource: String? = null,
    @SerialName("ruleset_id")
    public val rulesetId: Long? = null,
  ) : RepositoryRuleDetailed {
    @Serializable
    public data class Parameters(
      @SerialName("check_response_timeout_minutes")
      public val checkResponseTimeoutMinutes: Long,
      @SerialName("grouping_strategy")
      public val groupingStrategy: GroupingStrategy,
      @SerialName("max_entries_to_build")
      public val maxEntriesToBuild: Long,
      @SerialName("max_entries_to_merge")
      public val maxEntriesToMerge: Long,
      @SerialName("merge_method")
      public val mergeMethod: MergeMethod,
      @SerialName("min_entries_to_merge")
      public val minEntriesToMerge: Long,
      @SerialName("min_entries_to_merge_wait_minutes")
      public val minEntriesToMergeWaitMinutes: Long,
    ) {
      @Serializable
      public enum class GroupingStrategy {
        ALLGREEN,
        HEADGREEN,
      }

      @Serializable
      public enum class MergeMethod {
        MERGE,
        SQUASH,
        REBASE,
      }
    }

    @Serializable
    public enum class RulesetSourceType {
      Repository,
      Organization,
    }

    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("merge_queue")
      MergeQueue("merge_queue"),
      ;
    }
  }

  /**
   * Choose which environments must be successfully deployed to before refs can be pushed into a ref that matches this rule.
   */
  @Serializable
  public data class RepositoryRuleRequiredDeploymentsOrRepositoryRuleRulesetInfo(
    public val type: Type,
    public val parameters: Parameters? = null,
    @SerialName("ruleset_source_type")
    public val rulesetSourceType: RulesetSourceType? = null,
    @SerialName("ruleset_source")
    public val rulesetSource: String? = null,
    @SerialName("ruleset_id")
    public val rulesetId: Long? = null,
  ) : RepositoryRuleDetailed {
    @JvmInline
    @Serializable
    public value class Parameters(
      @SerialName("required_deployment_environments")
      public val requiredDeploymentEnvironments: List<String>,
    )

    @Serializable
    public enum class RulesetSourceType {
      Repository,
      Organization,
    }

    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("required_deployments")
      RequiredDeployments("required_deployments"),
      ;
    }
  }

  /**
   * Commits pushed to matching refs must have verified signatures.
   */
  @Serializable
  public data class RepositoryRuleRequiredSignaturesOrRepositoryRuleRulesetInfo(
    public val type: Type,
    @SerialName("ruleset_source_type")
    public val rulesetSourceType: RulesetSourceType? = null,
    @SerialName("ruleset_source")
    public val rulesetSource: String? = null,
    @SerialName("ruleset_id")
    public val rulesetId: Long? = null,
  ) : RepositoryRuleDetailed {
    @Serializable
    public enum class RulesetSourceType {
      Repository,
      Organization,
    }

    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("required_signatures")
      RequiredSignatures("required_signatures"),
      ;
    }
  }

  /**
   * Require all commits be made to a non-target branch and submitted via a pull request before they can be merged.
   */
  @Serializable
  public data class RepositoryRulePullRequestOrRepositoryRuleRulesetInfo(
    public val type: Type,
    public val parameters: Parameters? = null,
    @SerialName("ruleset_source_type")
    public val rulesetSourceType: RulesetSourceType? = null,
    @SerialName("ruleset_source")
    public val rulesetSource: String? = null,
    @SerialName("ruleset_id")
    public val rulesetId: Long? = null,
  ) : RepositoryRuleDetailed {
    @Serializable
    public data class Parameters(
      @SerialName("allowed_merge_methods")
      public val allowedMergeMethods: List<AllowedMergeMethods>? = null,
      @SerialName("dismiss_stale_reviews_on_push")
      public val dismissStaleReviewsOnPush: Boolean,
      @SerialName("require_code_owner_review")
      public val requireCodeOwnerReview: Boolean,
      @SerialName("require_last_push_approval")
      public val requireLastPushApproval: Boolean,
      @SerialName("required_approving_review_count")
      public val requiredApprovingReviewCount: Long,
      @SerialName("required_review_thread_resolution")
      public val requiredReviewThreadResolution: Boolean,
      @SerialName("required_reviewers")
      public val requiredReviewers: List<RepositoryRuleParamsRequiredReviewerConfiguration>? = null,
    ) {
      @Serializable
      public enum class AllowedMergeMethods(
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
    }

    @Serializable
    public enum class RulesetSourceType {
      Repository,
      Organization,
    }

    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("pull_request")
      PullRequest("pull_request"),
      ;
    }
  }

  /**
   * Choose which status checks must pass before the ref is updated. When enabled, commits must first be pushed to another ref where the checks pass.
   */
  @Serializable
  public data class RepositoryRuleRequiredStatusChecksOrRepositoryRuleRulesetInfo(
    public val type: Type,
    public val parameters: Parameters? = null,
    @SerialName("ruleset_source_type")
    public val rulesetSourceType: RulesetSourceType? = null,
    @SerialName("ruleset_source")
    public val rulesetSource: String? = null,
    @SerialName("ruleset_id")
    public val rulesetId: Long? = null,
  ) : RepositoryRuleDetailed {
    @Serializable
    public data class Parameters(
      @SerialName("do_not_enforce_on_create")
      public val doNotEnforceOnCreate: Boolean? = null,
      @SerialName("required_status_checks")
      public val requiredStatusChecks: List<RepositoryRuleParamsStatusCheckConfiguration>,
      @SerialName("strict_required_status_checks_policy")
      public val strictRequiredStatusChecksPolicy: Boolean,
    )

    @Serializable
    public enum class RulesetSourceType {
      Repository,
      Organization,
    }

    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("required_status_checks")
      RequiredStatusChecks("required_status_checks"),
      ;
    }
  }

  /**
   * Prevent users with push access from force pushing to refs.
   */
  @Serializable
  public data class RepositoryRuleNonFastForwardOrRepositoryRuleRulesetInfo(
    public val type: Type,
    @SerialName("ruleset_source_type")
    public val rulesetSourceType: RulesetSourceType? = null,
    @SerialName("ruleset_source")
    public val rulesetSource: String? = null,
    @SerialName("ruleset_id")
    public val rulesetId: Long? = null,
  ) : RepositoryRuleDetailed {
    @Serializable
    public enum class RulesetSourceType {
      Repository,
      Organization,
    }

    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("non_fast_forward")
      NonFastForward("non_fast_forward"),
      ;
    }
  }

  /**
   * Parameters to be used for the commit_message_pattern rule
   */
  @Serializable
  public data class RepositoryRuleCommitMessagePatternOrRepositoryRuleRulesetInfo(
    public val type: Type,
    public val parameters: Parameters? = null,
    @SerialName("ruleset_source_type")
    public val rulesetSourceType: RulesetSourceType? = null,
    @SerialName("ruleset_source")
    public val rulesetSource: String? = null,
    @SerialName("ruleset_id")
    public val rulesetId: Long? = null,
  ) : RepositoryRuleDetailed {
    @Serializable
    public data class Parameters(
      public val name: String? = null,
      public val negate: Boolean? = null,
      public val `operator`: Operator,
      public val pattern: String,
    ) {
      @Serializable
      public enum class Operator(
        public val `value`: String,
      ) {
        @SerialName("starts_with")
        StartsWith("starts_with"),
        @SerialName("ends_with")
        EndsWith("ends_with"),
        @SerialName("contains")
        Contains("contains"),
        @SerialName("regex")
        Regex("regex"),
        ;
      }
    }

    @Serializable
    public enum class RulesetSourceType {
      Repository,
      Organization,
    }

    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("commit_message_pattern")
      CommitMessagePattern("commit_message_pattern"),
      ;
    }
  }

  /**
   * Parameters to be used for the commit_author_email_pattern rule
   */
  @Serializable
  public data class RepositoryRuleCommitAuthorEmailPatternOrRepositoryRuleRulesetInfo(
    public val type: Type,
    public val parameters: Parameters? = null,
    @SerialName("ruleset_source_type")
    public val rulesetSourceType: RulesetSourceType? = null,
    @SerialName("ruleset_source")
    public val rulesetSource: String? = null,
    @SerialName("ruleset_id")
    public val rulesetId: Long? = null,
  ) : RepositoryRuleDetailed {
    @Serializable
    public data class Parameters(
      public val name: String? = null,
      public val negate: Boolean? = null,
      public val `operator`: Operator,
      public val pattern: String,
    ) {
      @Serializable
      public enum class Operator(
        public val `value`: String,
      ) {
        @SerialName("starts_with")
        StartsWith("starts_with"),
        @SerialName("ends_with")
        EndsWith("ends_with"),
        @SerialName("contains")
        Contains("contains"),
        @SerialName("regex")
        Regex("regex"),
        ;
      }
    }

    @Serializable
    public enum class RulesetSourceType {
      Repository,
      Organization,
    }

    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("commit_author_email_pattern")
      CommitAuthorEmailPattern("commit_author_email_pattern"),
      ;
    }
  }

  /**
   * Parameters to be used for the committer_email_pattern rule
   */
  @Serializable
  public data class RepositoryRuleCommitterEmailPatternOrRepositoryRuleRulesetInfo(
    public val type: Type,
    public val parameters: Parameters? = null,
    @SerialName("ruleset_source_type")
    public val rulesetSourceType: RulesetSourceType? = null,
    @SerialName("ruleset_source")
    public val rulesetSource: String? = null,
    @SerialName("ruleset_id")
    public val rulesetId: Long? = null,
  ) : RepositoryRuleDetailed {
    @Serializable
    public data class Parameters(
      public val name: String? = null,
      public val negate: Boolean? = null,
      public val `operator`: Operator,
      public val pattern: String,
    ) {
      @Serializable
      public enum class Operator(
        public val `value`: String,
      ) {
        @SerialName("starts_with")
        StartsWith("starts_with"),
        @SerialName("ends_with")
        EndsWith("ends_with"),
        @SerialName("contains")
        Contains("contains"),
        @SerialName("regex")
        Regex("regex"),
        ;
      }
    }

    @Serializable
    public enum class RulesetSourceType {
      Repository,
      Organization,
    }

    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("committer_email_pattern")
      CommitterEmailPattern("committer_email_pattern"),
      ;
    }
  }

  /**
   * Parameters to be used for the branch_name_pattern rule
   */
  @Serializable
  public data class RepositoryRuleBranchNamePatternOrRepositoryRuleRulesetInfo(
    public val type: Type,
    public val parameters: Parameters? = null,
    @SerialName("ruleset_source_type")
    public val rulesetSourceType: RulesetSourceType? = null,
    @SerialName("ruleset_source")
    public val rulesetSource: String? = null,
    @SerialName("ruleset_id")
    public val rulesetId: Long? = null,
  ) : RepositoryRuleDetailed {
    @Serializable
    public data class Parameters(
      public val name: String? = null,
      public val negate: Boolean? = null,
      public val `operator`: Operator,
      public val pattern: String,
    ) {
      @Serializable
      public enum class Operator(
        public val `value`: String,
      ) {
        @SerialName("starts_with")
        StartsWith("starts_with"),
        @SerialName("ends_with")
        EndsWith("ends_with"),
        @SerialName("contains")
        Contains("contains"),
        @SerialName("regex")
        Regex("regex"),
        ;
      }
    }

    @Serializable
    public enum class RulesetSourceType {
      Repository,
      Organization,
    }

    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("branch_name_pattern")
      BranchNamePattern("branch_name_pattern"),
      ;
    }
  }

  /**
   * Parameters to be used for the tag_name_pattern rule
   */
  @Serializable
  public data class RepositoryRuleTagNamePatternOrRepositoryRuleRulesetInfo(
    public val type: Type,
    public val parameters: Parameters? = null,
    @SerialName("ruleset_source_type")
    public val rulesetSourceType: RulesetSourceType? = null,
    @SerialName("ruleset_source")
    public val rulesetSource: String? = null,
    @SerialName("ruleset_id")
    public val rulesetId: Long? = null,
  ) : RepositoryRuleDetailed {
    @Serializable
    public data class Parameters(
      public val name: String? = null,
      public val negate: Boolean? = null,
      public val `operator`: Operator,
      public val pattern: String,
    ) {
      @Serializable
      public enum class Operator(
        public val `value`: String,
      ) {
        @SerialName("starts_with")
        StartsWith("starts_with"),
        @SerialName("ends_with")
        EndsWith("ends_with"),
        @SerialName("contains")
        Contains("contains"),
        @SerialName("regex")
        Regex("regex"),
        ;
      }
    }

    @Serializable
    public enum class RulesetSourceType {
      Repository,
      Organization,
    }

    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("tag_name_pattern")
      TagNamePattern("tag_name_pattern"),
      ;
    }
  }

  /**
   * Prevent commits that include changes in specified file and folder paths from being pushed to the commit graph. This includes absolute paths that contain file names.
   */
  @Serializable
  public data class RepositoryRuleFilePathRestrictionOrRepositoryRuleRulesetInfo(
    public val type: Type,
    public val parameters: Parameters? = null,
    @SerialName("ruleset_source_type")
    public val rulesetSourceType: RulesetSourceType? = null,
    @SerialName("ruleset_source")
    public val rulesetSource: String? = null,
    @SerialName("ruleset_id")
    public val rulesetId: Long? = null,
  ) : RepositoryRuleDetailed {
    @JvmInline
    @Serializable
    public value class Parameters(
      @SerialName("restricted_file_paths")
      public val restrictedFilePaths: List<String>,
    )

    @Serializable
    public enum class RulesetSourceType {
      Repository,
      Organization,
    }

    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("file_path_restriction")
      FilePathRestriction("file_path_restriction"),
      ;
    }
  }

  /**
   * Prevent commits that include file paths that exceed the specified character limit from being pushed to the commit graph.
   */
  @Serializable
  public data class RepositoryRuleMaxFilePathLengthOrRepositoryRuleRulesetInfo(
    public val type: Type,
    public val parameters: Parameters? = null,
    @SerialName("ruleset_source_type")
    public val rulesetSourceType: RulesetSourceType? = null,
    @SerialName("ruleset_source")
    public val rulesetSource: String? = null,
    @SerialName("ruleset_id")
    public val rulesetId: Long? = null,
  ) : RepositoryRuleDetailed {
    @JvmInline
    @Serializable
    public value class Parameters(
      @SerialName("max_file_path_length")
      public val maxFilePathLength: Long,
    )

    @Serializable
    public enum class RulesetSourceType {
      Repository,
      Organization,
    }

    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("max_file_path_length")
      MaxFilePathLength("max_file_path_length"),
      ;
    }
  }

  /**
   * Prevent commits that include files with specified file extensions from being pushed to the commit graph.
   */
  @Serializable
  public data class RepositoryRuleFileExtensionRestrictionOrRepositoryRuleRulesetInfo(
    public val type: Type,
    public val parameters: Parameters? = null,
    @SerialName("ruleset_source_type")
    public val rulesetSourceType: RulesetSourceType? = null,
    @SerialName("ruleset_source")
    public val rulesetSource: String? = null,
    @SerialName("ruleset_id")
    public val rulesetId: Long? = null,
  ) : RepositoryRuleDetailed {
    @JvmInline
    @Serializable
    public value class Parameters(
      @SerialName("restricted_file_extensions")
      public val restrictedFileExtensions: List<String>,
    )

    @Serializable
    public enum class RulesetSourceType {
      Repository,
      Organization,
    }

    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("file_extension_restriction")
      FileExtensionRestriction("file_extension_restriction"),
      ;
    }
  }

  /**
   * Prevent commits with individual files that exceed the specified limit from being pushed to the commit graph.
   */
  @Serializable
  public data class RepositoryRuleMaxFileSizeOrRepositoryRuleRulesetInfo(
    public val type: Type,
    public val parameters: Parameters? = null,
    @SerialName("ruleset_source_type")
    public val rulesetSourceType: RulesetSourceType? = null,
    @SerialName("ruleset_source")
    public val rulesetSource: String? = null,
    @SerialName("ruleset_id")
    public val rulesetId: Long? = null,
  ) : RepositoryRuleDetailed {
    @JvmInline
    @Serializable
    public value class Parameters(
      @SerialName("max_file_size")
      public val maxFileSize: Long,
    )

    @Serializable
    public enum class RulesetSourceType {
      Repository,
      Organization,
    }

    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("max_file_size")
      MaxFileSize("max_file_size"),
      ;
    }
  }

  /**
   * Require all changes made to a targeted branch to pass the specified workflows before they can be merged.
   */
  @Serializable
  public data class RepositoryRuleWorkflowsOrRepositoryRuleRulesetInfo(
    public val type: Type,
    public val parameters: Parameters? = null,
    @SerialName("ruleset_source_type")
    public val rulesetSourceType: RulesetSourceType? = null,
    @SerialName("ruleset_source")
    public val rulesetSource: String? = null,
    @SerialName("ruleset_id")
    public val rulesetId: Long? = null,
  ) : RepositoryRuleDetailed {
    @Serializable
    public data class Parameters(
      @SerialName("do_not_enforce_on_create")
      public val doNotEnforceOnCreate: Boolean? = null,
      public val workflows: List<RepositoryRuleParamsWorkflowFileReference>,
    )

    @Serializable
    public enum class RulesetSourceType {
      Repository,
      Organization,
    }

    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("workflows")
      Workflows("workflows"),
      ;
    }
  }

  /**
   * Choose which tools must provide code scanning results before the reference is updated. When configured, code scanning must be enabled and have results for both the commit and the reference being updated.
   */
  @Serializable
  public data class RepositoryRuleCodeScanningOrRepositoryRuleRulesetInfo(
    public val type: Type,
    public val parameters: Parameters? = null,
    @SerialName("ruleset_source_type")
    public val rulesetSourceType: RulesetSourceType? = null,
    @SerialName("ruleset_source")
    public val rulesetSource: String? = null,
    @SerialName("ruleset_id")
    public val rulesetId: Long? = null,
  ) : RepositoryRuleDetailed {
    @JvmInline
    @Serializable
    public value class Parameters(
      @SerialName("code_scanning_tools")
      public val codeScanningTools: List<RepositoryRuleParamsCodeScanningTool>,
    )

    @Serializable
    public enum class RulesetSourceType {
      Repository,
      Organization,
    }

    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("code_scanning")
      CodeScanning("code_scanning"),
      ;
    }
  }

  /**
   * Request Copilot code review for new pull requests automatically if the author has access to Copilot code review and their premium requests quota has not reached the limit.
   */
  @Serializable
  public data class RepositoryRuleCopilotCodeReviewOrRepositoryRuleRulesetInfo(
    public val type: Type,
    public val parameters: Parameters? = null,
    @SerialName("ruleset_source_type")
    public val rulesetSourceType: RulesetSourceType? = null,
    @SerialName("ruleset_source")
    public val rulesetSource: String? = null,
    @SerialName("ruleset_id")
    public val rulesetId: Long? = null,
  ) : RepositoryRuleDetailed {
    @Serializable
    public data class Parameters(
      @SerialName("review_draft_pull_requests")
      public val reviewDraftPullRequests: Boolean? = null,
      @SerialName("review_on_push")
      public val reviewOnPush: Boolean? = null,
    )

    @Serializable
    public enum class RulesetSourceType {
      Repository,
      Organization,
    }

    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("copilot_code_review")
      CopilotCodeReview("copilot_code_review"),
      ;
    }
  }

  public object Serializer : KSerializer<RepositoryRuleDetailed> {
    @OptIn(
      InternalSerializationApi::class,
      ExperimentalSerializationApi::class,
    )
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("io.github.model.RepositoryRuleDetailed", PolymorphicKind.SEALED) {
      element("RepositoryRuleCreationOrRepositoryRuleRulesetInfo", RepositoryRuleCreationOrRepositoryRuleRulesetInfo.serializer().descriptor)
      element("RepositoryRuleUpdateOrRepositoryRuleRulesetInfo", RepositoryRuleUpdateOrRepositoryRuleRulesetInfo.serializer().descriptor)
      element("RepositoryRuleDeletionOrRepositoryRuleRulesetInfo", RepositoryRuleDeletionOrRepositoryRuleRulesetInfo.serializer().descriptor)
      element("RepositoryRuleRequiredLinearHistoryOrRepositoryRuleRulesetInfo", RepositoryRuleRequiredLinearHistoryOrRepositoryRuleRulesetInfo.serializer().descriptor)
      element("RepositoryRuleMergeQueueOrRepositoryRuleRulesetInfo", RepositoryRuleMergeQueueOrRepositoryRuleRulesetInfo.serializer().descriptor)
      element("RepositoryRuleRequiredDeploymentsOrRepositoryRuleRulesetInfo", RepositoryRuleRequiredDeploymentsOrRepositoryRuleRulesetInfo.serializer().descriptor)
      element("RepositoryRuleRequiredSignaturesOrRepositoryRuleRulesetInfo", RepositoryRuleRequiredSignaturesOrRepositoryRuleRulesetInfo.serializer().descriptor)
      element("RepositoryRulePullRequestOrRepositoryRuleRulesetInfo", RepositoryRulePullRequestOrRepositoryRuleRulesetInfo.serializer().descriptor)
      element("RepositoryRuleRequiredStatusChecksOrRepositoryRuleRulesetInfo", RepositoryRuleRequiredStatusChecksOrRepositoryRuleRulesetInfo.serializer().descriptor)
      element("RepositoryRuleNonFastForwardOrRepositoryRuleRulesetInfo", RepositoryRuleNonFastForwardOrRepositoryRuleRulesetInfo.serializer().descriptor)
      element("RepositoryRuleCommitMessagePatternOrRepositoryRuleRulesetInfo", RepositoryRuleCommitMessagePatternOrRepositoryRuleRulesetInfo.serializer().descriptor)
      element("RepositoryRuleCommitAuthorEmailPatternOrRepositoryRuleRulesetInfo", RepositoryRuleCommitAuthorEmailPatternOrRepositoryRuleRulesetInfo.serializer().descriptor)
      element("RepositoryRuleCommitterEmailPatternOrRepositoryRuleRulesetInfo", RepositoryRuleCommitterEmailPatternOrRepositoryRuleRulesetInfo.serializer().descriptor)
      element("RepositoryRuleBranchNamePatternOrRepositoryRuleRulesetInfo", RepositoryRuleBranchNamePatternOrRepositoryRuleRulesetInfo.serializer().descriptor)
      element("RepositoryRuleTagNamePatternOrRepositoryRuleRulesetInfo", RepositoryRuleTagNamePatternOrRepositoryRuleRulesetInfo.serializer().descriptor)
      element("RepositoryRuleFilePathRestrictionOrRepositoryRuleRulesetInfo", RepositoryRuleFilePathRestrictionOrRepositoryRuleRulesetInfo.serializer().descriptor)
      element("RepositoryRuleMaxFilePathLengthOrRepositoryRuleRulesetInfo", RepositoryRuleMaxFilePathLengthOrRepositoryRuleRulesetInfo.serializer().descriptor)
      element("RepositoryRuleFileExtensionRestrictionOrRepositoryRuleRulesetInfo", RepositoryRuleFileExtensionRestrictionOrRepositoryRuleRulesetInfo.serializer().descriptor)
      element("RepositoryRuleMaxFileSizeOrRepositoryRuleRulesetInfo", RepositoryRuleMaxFileSizeOrRepositoryRuleRulesetInfo.serializer().descriptor)
      element("RepositoryRuleWorkflowsOrRepositoryRuleRulesetInfo", RepositoryRuleWorkflowsOrRepositoryRuleRulesetInfo.serializer().descriptor)
      element("RepositoryRuleCodeScanningOrRepositoryRuleRulesetInfo", RepositoryRuleCodeScanningOrRepositoryRuleRulesetInfo.serializer().descriptor)
      element("RepositoryRuleCopilotCodeReviewOrRepositoryRuleRulesetInfo", RepositoryRuleCopilotCodeReviewOrRepositoryRuleRulesetInfo.serializer().descriptor)
    }

    override fun deserialize(decoder: Decoder): RepositoryRuleDetailed {
      val value = decoder.decodeSerializableValue(JsonElement.serializer())
      val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
      return json.attemptDeserialize(
        value,
        RepositoryRuleUpdateOrRepositoryRuleRulesetInfo::class to { decodeFromJsonElement(RepositoryRuleUpdateOrRepositoryRuleRulesetInfo.serializer(), it) },
        RepositoryRuleMergeQueueOrRepositoryRuleRulesetInfo::class to { decodeFromJsonElement(RepositoryRuleMergeQueueOrRepositoryRuleRulesetInfo.serializer(), it) },
        RepositoryRuleRequiredDeploymentsOrRepositoryRuleRulesetInfo::class to { decodeFromJsonElement(RepositoryRuleRequiredDeploymentsOrRepositoryRuleRulesetInfo.serializer(), it) },
        RepositoryRulePullRequestOrRepositoryRuleRulesetInfo::class to { decodeFromJsonElement(RepositoryRulePullRequestOrRepositoryRuleRulesetInfo.serializer(), it) },
        RepositoryRuleRequiredStatusChecksOrRepositoryRuleRulesetInfo::class to { decodeFromJsonElement(RepositoryRuleRequiredStatusChecksOrRepositoryRuleRulesetInfo.serializer(), it) },
        RepositoryRuleCommitMessagePatternOrRepositoryRuleRulesetInfo::class to { decodeFromJsonElement(RepositoryRuleCommitMessagePatternOrRepositoryRuleRulesetInfo.serializer(), it) },
        RepositoryRuleCommitAuthorEmailPatternOrRepositoryRuleRulesetInfo::class to { decodeFromJsonElement(RepositoryRuleCommitAuthorEmailPatternOrRepositoryRuleRulesetInfo.serializer(), it) },
        RepositoryRuleCommitterEmailPatternOrRepositoryRuleRulesetInfo::class to { decodeFromJsonElement(RepositoryRuleCommitterEmailPatternOrRepositoryRuleRulesetInfo.serializer(), it) },
        RepositoryRuleBranchNamePatternOrRepositoryRuleRulesetInfo::class to { decodeFromJsonElement(RepositoryRuleBranchNamePatternOrRepositoryRuleRulesetInfo.serializer(), it) },
        RepositoryRuleTagNamePatternOrRepositoryRuleRulesetInfo::class to { decodeFromJsonElement(RepositoryRuleTagNamePatternOrRepositoryRuleRulesetInfo.serializer(), it) },
        RepositoryRuleFilePathRestrictionOrRepositoryRuleRulesetInfo::class to { decodeFromJsonElement(RepositoryRuleFilePathRestrictionOrRepositoryRuleRulesetInfo.serializer(), it) },
        RepositoryRuleMaxFilePathLengthOrRepositoryRuleRulesetInfo::class to { decodeFromJsonElement(RepositoryRuleMaxFilePathLengthOrRepositoryRuleRulesetInfo.serializer(), it) },
        RepositoryRuleFileExtensionRestrictionOrRepositoryRuleRulesetInfo::class to { decodeFromJsonElement(RepositoryRuleFileExtensionRestrictionOrRepositoryRuleRulesetInfo.serializer(), it) },
        RepositoryRuleMaxFileSizeOrRepositoryRuleRulesetInfo::class to { decodeFromJsonElement(RepositoryRuleMaxFileSizeOrRepositoryRuleRulesetInfo.serializer(), it) },
        RepositoryRuleWorkflowsOrRepositoryRuleRulesetInfo::class to { decodeFromJsonElement(RepositoryRuleWorkflowsOrRepositoryRuleRulesetInfo.serializer(), it) },
        RepositoryRuleCodeScanningOrRepositoryRuleRulesetInfo::class to { decodeFromJsonElement(RepositoryRuleCodeScanningOrRepositoryRuleRulesetInfo.serializer(), it) },
        RepositoryRuleCopilotCodeReviewOrRepositoryRuleRulesetInfo::class to { decodeFromJsonElement(RepositoryRuleCopilotCodeReviewOrRepositoryRuleRulesetInfo.serializer(), it) },
        RepositoryRuleCreationOrRepositoryRuleRulesetInfo::class to { decodeFromJsonElement(RepositoryRuleCreationOrRepositoryRuleRulesetInfo.serializer(), it) },
        RepositoryRuleDeletionOrRepositoryRuleRulesetInfo::class to { decodeFromJsonElement(RepositoryRuleDeletionOrRepositoryRuleRulesetInfo.serializer(), it) },
        RepositoryRuleRequiredLinearHistoryOrRepositoryRuleRulesetInfo::class to { decodeFromJsonElement(RepositoryRuleRequiredLinearHistoryOrRepositoryRuleRulesetInfo.serializer(), it) },
        RepositoryRuleRequiredSignaturesOrRepositoryRuleRulesetInfo::class to { decodeFromJsonElement(RepositoryRuleRequiredSignaturesOrRepositoryRuleRulesetInfo.serializer(), it) },
        RepositoryRuleNonFastForwardOrRepositoryRuleRulesetInfo::class to { decodeFromJsonElement(RepositoryRuleNonFastForwardOrRepositoryRuleRulesetInfo.serializer(), it) },
      )
    }

    override fun serialize(encoder: Encoder, `value`: RepositoryRuleDetailed) {
      when(value) {
        is RepositoryRuleCreationOrRepositoryRuleRulesetInfo -> encoder.encodeSerializableValue(RepositoryRuleCreationOrRepositoryRuleRulesetInfo.serializer(), value)
        is RepositoryRuleUpdateOrRepositoryRuleRulesetInfo -> encoder.encodeSerializableValue(RepositoryRuleUpdateOrRepositoryRuleRulesetInfo.serializer(), value)
        is RepositoryRuleDeletionOrRepositoryRuleRulesetInfo -> encoder.encodeSerializableValue(RepositoryRuleDeletionOrRepositoryRuleRulesetInfo.serializer(), value)
        is RepositoryRuleRequiredLinearHistoryOrRepositoryRuleRulesetInfo -> encoder.encodeSerializableValue(RepositoryRuleRequiredLinearHistoryOrRepositoryRuleRulesetInfo.serializer(), value)
        is RepositoryRuleMergeQueueOrRepositoryRuleRulesetInfo -> encoder.encodeSerializableValue(RepositoryRuleMergeQueueOrRepositoryRuleRulesetInfo.serializer(), value)
        is RepositoryRuleRequiredDeploymentsOrRepositoryRuleRulesetInfo -> encoder.encodeSerializableValue(RepositoryRuleRequiredDeploymentsOrRepositoryRuleRulesetInfo.serializer(), value)
        is RepositoryRuleRequiredSignaturesOrRepositoryRuleRulesetInfo -> encoder.encodeSerializableValue(RepositoryRuleRequiredSignaturesOrRepositoryRuleRulesetInfo.serializer(), value)
        is RepositoryRulePullRequestOrRepositoryRuleRulesetInfo -> encoder.encodeSerializableValue(RepositoryRulePullRequestOrRepositoryRuleRulesetInfo.serializer(), value)
        is RepositoryRuleRequiredStatusChecksOrRepositoryRuleRulesetInfo -> encoder.encodeSerializableValue(RepositoryRuleRequiredStatusChecksOrRepositoryRuleRulesetInfo.serializer(), value)
        is RepositoryRuleNonFastForwardOrRepositoryRuleRulesetInfo -> encoder.encodeSerializableValue(RepositoryRuleNonFastForwardOrRepositoryRuleRulesetInfo.serializer(), value)
        is RepositoryRuleCommitMessagePatternOrRepositoryRuleRulesetInfo -> encoder.encodeSerializableValue(RepositoryRuleCommitMessagePatternOrRepositoryRuleRulesetInfo.serializer(), value)
        is RepositoryRuleCommitAuthorEmailPatternOrRepositoryRuleRulesetInfo -> encoder.encodeSerializableValue(RepositoryRuleCommitAuthorEmailPatternOrRepositoryRuleRulesetInfo.serializer(), value)
        is RepositoryRuleCommitterEmailPatternOrRepositoryRuleRulesetInfo -> encoder.encodeSerializableValue(RepositoryRuleCommitterEmailPatternOrRepositoryRuleRulesetInfo.serializer(), value)
        is RepositoryRuleBranchNamePatternOrRepositoryRuleRulesetInfo -> encoder.encodeSerializableValue(RepositoryRuleBranchNamePatternOrRepositoryRuleRulesetInfo.serializer(), value)
        is RepositoryRuleTagNamePatternOrRepositoryRuleRulesetInfo -> encoder.encodeSerializableValue(RepositoryRuleTagNamePatternOrRepositoryRuleRulesetInfo.serializer(), value)
        is RepositoryRuleFilePathRestrictionOrRepositoryRuleRulesetInfo -> encoder.encodeSerializableValue(RepositoryRuleFilePathRestrictionOrRepositoryRuleRulesetInfo.serializer(), value)
        is RepositoryRuleMaxFilePathLengthOrRepositoryRuleRulesetInfo -> encoder.encodeSerializableValue(RepositoryRuleMaxFilePathLengthOrRepositoryRuleRulesetInfo.serializer(), value)
        is RepositoryRuleFileExtensionRestrictionOrRepositoryRuleRulesetInfo -> encoder.encodeSerializableValue(RepositoryRuleFileExtensionRestrictionOrRepositoryRuleRulesetInfo.serializer(), value)
        is RepositoryRuleMaxFileSizeOrRepositoryRuleRulesetInfo -> encoder.encodeSerializableValue(RepositoryRuleMaxFileSizeOrRepositoryRuleRulesetInfo.serializer(), value)
        is RepositoryRuleWorkflowsOrRepositoryRuleRulesetInfo -> encoder.encodeSerializableValue(RepositoryRuleWorkflowsOrRepositoryRuleRulesetInfo.serializer(), value)
        is RepositoryRuleCodeScanningOrRepositoryRuleRulesetInfo -> encoder.encodeSerializableValue(RepositoryRuleCodeScanningOrRepositoryRuleRulesetInfo.serializer(), value)
        is RepositoryRuleCopilotCodeReviewOrRepositoryRuleRulesetInfo -> encoder.encodeSerializableValue(RepositoryRuleCopilotCodeReviewOrRepositoryRuleRulesetInfo.serializer(), value)
      }
    }
  }
}
