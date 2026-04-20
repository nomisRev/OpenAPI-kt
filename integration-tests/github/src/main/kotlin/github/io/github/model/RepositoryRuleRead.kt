package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * A repository rule.
 */
@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("type")
@Serializable
public sealed interface RepositoryRuleRead {
  @Serializable
  @SerialName("creation")
  public data object Creation : RepositoryRuleRead

  /**
   * Only allow users with bypass permission to update matching refs.
   */
  @JvmInline
  @SerialName("update")
  @Serializable
  public value class Update(
    public val parameters: Parameters? = null,
  ) : RepositoryRuleRead {
    @JvmInline
    @Serializable
    public value class Parameters(
      @SerialName("update_allows_fetch_and_merge")
      public val updateAllowsFetchAndMerge: Boolean,
    )
  }

  @Serializable
  @SerialName("deletion")
  public data object Deletion : RepositoryRuleRead

  @Serializable
  @SerialName("required_linear_history")
  public data object RequiredLinearHistory : RepositoryRuleRead

  /**
   * Merges must be performed via a merge queue.
   */
  @JvmInline
  @SerialName("merge_queue")
  @Serializable
  public value class MergeQueue(
    public val parameters: Parameters? = null,
  ) : RepositoryRuleRead {
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
  }

  /**
   * Choose which environments must be successfully deployed to before refs can be pushed into a ref that matches this rule.
   */
  @JvmInline
  @SerialName("required_deployments")
  @Serializable
  public value class RequiredDeployments(
    public val parameters: Parameters? = null,
  ) : RepositoryRuleRead {
    @JvmInline
    @Serializable
    public value class Parameters(
      @SerialName("required_deployment_environments")
      public val requiredDeploymentEnvironments: List<String>,
    )
  }

  @Serializable
  @SerialName("required_signatures")
  public data object RequiredSignatures : RepositoryRuleRead

  /**
   * Require all commits be made to a non-target branch and submitted via a pull request before they can be merged.
   */
  @JvmInline
  @SerialName("pull_request")
  @Serializable
  public value class PullRequest(
    public val parameters: Parameters? = null,
  ) : RepositoryRuleRead {
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
      public val requiredReviewers:
          List<RepositoryRuleParamsRequiredReviewerConfigurationRead>? = null,
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
  }

  /**
   * Choose which status checks must pass before the ref is updated. When enabled, commits must first be pushed to another ref where the checks pass.
   */
  @JvmInline
  @SerialName("required_status_checks")
  @Serializable
  public value class RequiredStatusChecks(
    public val parameters: Parameters? = null,
  ) : RepositoryRuleRead {
    @Serializable
    public data class Parameters(
      @SerialName("do_not_enforce_on_create")
      public val doNotEnforceOnCreate: Boolean? = null,
      @SerialName("required_status_checks")
      public val requiredStatusChecks: List<RepositoryRuleParamsStatusCheckConfigurationRead>,
      @SerialName("strict_required_status_checks_policy")
      public val strictRequiredStatusChecksPolicy: Boolean,
    )
  }

  @Serializable
  @SerialName("non_fast_forward")
  public data object NonFastForward : RepositoryRuleRead

  /**
   * Parameters to be used for the commit_message_pattern rule
   */
  @JvmInline
  @SerialName("commit_message_pattern")
  @Serializable
  public value class CommitMessagePattern(
    public val parameters: Parameters? = null,
  ) : RepositoryRuleRead {
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
  }

  /**
   * Parameters to be used for the commit_author_email_pattern rule
   */
  @JvmInline
  @SerialName("commit_author_email_pattern")
  @Serializable
  public value class CommitAuthorEmailPattern(
    public val parameters: Parameters? = null,
  ) : RepositoryRuleRead {
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
  }

  /**
   * Parameters to be used for the committer_email_pattern rule
   */
  @JvmInline
  @SerialName("committer_email_pattern")
  @Serializable
  public value class CommitterEmailPattern(
    public val parameters: Parameters? = null,
  ) : RepositoryRuleRead {
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
  }

  /**
   * Parameters to be used for the branch_name_pattern rule
   */
  @JvmInline
  @SerialName("branch_name_pattern")
  @Serializable
  public value class BranchNamePattern(
    public val parameters: Parameters? = null,
  ) : RepositoryRuleRead {
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
  }

  /**
   * Parameters to be used for the tag_name_pattern rule
   */
  @JvmInline
  @SerialName("tag_name_pattern")
  @Serializable
  public value class TagNamePattern(
    public val parameters: Parameters? = null,
  ) : RepositoryRuleRead {
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
  }

  /**
   * Prevent commits that include changes in specified file and folder paths from being pushed to the commit graph. This includes absolute paths that contain file names.
   */
  @JvmInline
  @SerialName("file_path_restriction")
  @Serializable
  public value class FilePathRestriction(
    public val parameters: Parameters? = null,
  ) : RepositoryRuleRead {
    @JvmInline
    @Serializable
    public value class Parameters(
      @SerialName("restricted_file_paths")
      public val restrictedFilePaths: List<String>,
    )
  }

  /**
   * Prevent commits that include file paths that exceed the specified character limit from being pushed to the commit graph.
   */
  @JvmInline
  @SerialName("max_file_path_length")
  @Serializable
  public value class MaxFilePathLength(
    public val parameters: Parameters? = null,
  ) : RepositoryRuleRead {
    @JvmInline
    @Serializable
    public value class Parameters(
      @SerialName("max_file_path_length")
      public val maxFilePathLength: Long,
    )
  }

  /**
   * Prevent commits that include files with specified file extensions from being pushed to the commit graph.
   */
  @JvmInline
  @SerialName("file_extension_restriction")
  @Serializable
  public value class FileExtensionRestriction(
    public val parameters: Parameters? = null,
  ) : RepositoryRuleRead {
    @JvmInline
    @Serializable
    public value class Parameters(
      @SerialName("restricted_file_extensions")
      public val restrictedFileExtensions: List<String>,
    )
  }

  /**
   * Prevent commits with individual files that exceed the specified limit from being pushed to the commit graph.
   */
  @JvmInline
  @SerialName("max_file_size")
  @Serializable
  public value class MaxFileSize(
    public val parameters: Parameters? = null,
  ) : RepositoryRuleRead {
    @JvmInline
    @Serializable
    public value class Parameters(
      @SerialName("max_file_size")
      public val maxFileSize: Long,
    )
  }

  /**
   * Require all changes made to a targeted branch to pass the specified workflows before they can be merged.
   */
  @JvmInline
  @SerialName("workflows")
  @Serializable
  public value class Workflows(
    public val parameters: Parameters? = null,
  ) : RepositoryRuleRead {
    @Serializable
    public data class Parameters(
      @SerialName("do_not_enforce_on_create")
      public val doNotEnforceOnCreate: Boolean? = null,
      public val workflows: List<RepositoryRuleParamsWorkflowFileReferenceRead>,
    )
  }

  /**
   * Choose which tools must provide code scanning results before the reference is updated. When configured, code scanning must be enabled and have results for both the commit and the reference being updated.
   */
  @JvmInline
  @SerialName("code_scanning")
  @Serializable
  public value class CodeScanning(
    public val parameters: Parameters? = null,
  ) : RepositoryRuleRead {
    @JvmInline
    @Serializable
    public value class Parameters(
      @SerialName("code_scanning_tools")
      public val codeScanningTools: List<RepositoryRuleParamsCodeScanningToolRead>,
    )
  }

  /**
   * Request Copilot code review for new pull requests automatically if the author has access to Copilot code review and their premium requests quota has not reached the limit.
   */
  @JvmInline
  @SerialName("copilot_code_review")
  @Serializable
  public value class CopilotCodeReview(
    public val parameters: Parameters? = null,
  ) : RepositoryRuleRead {
    @Serializable
    public data class Parameters(
      @SerialName("review_draft_pull_requests")
      public val reviewDraftPullRequests: Boolean? = null,
      @SerialName("review_on_push")
      public val reviewOnPush: Boolean? = null,
    )
  }
}
