package io.github.model

import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A run of a CodeQL query against one or more repositories.
 */
@Serializable
public data class CodeScanningVariantAnalysis(
  public val id: Long,
  @SerialName("controller_repo")
  public val controllerRepo: SimpleRepository,
  public val actor: SimpleUser,
  @SerialName("query_language")
  public val queryLanguage: CodeScanningVariantAnalysisLanguage,
  @SerialName("query_pack_url")
  public val queryPackUrl: String,
  @SerialName("created_at")
  public val createdAt: Instant? = null,
  @SerialName("updated_at")
  public val updatedAt: Instant? = null,
  @SerialName("completed_at")
  public val completedAt: Instant? = null,
  public val status: Status,
  @SerialName("actions_workflow_run_id")
  public val actionsWorkflowRunId: Long? = null,
  @SerialName("failure_reason")
  public val failureReason: FailureReason? = null,
  @SerialName("scanned_repositories")
  public val scannedRepositories: List<ScannedRepositories>? = null,
  @SerialName("skipped_repositories")
  public val skippedRepositories: SkippedRepositories? = null,
) {
  @Serializable
  public enum class FailureReason(
    public val `value`: String,
  ) {
    @SerialName("no_repos_queried")
    NoReposQueried("no_repos_queried"),
    @SerialName("actions_workflow_run_failed")
    ActionsWorkflowRunFailed("actions_workflow_run_failed"),
    @SerialName("internal_error")
    InternalError("internal_error"),
    ;
  }

  @Serializable
  public data class ScannedRepositories(
    public val repository: CodeScanningVariantAnalysisRepository,
    @SerialName("analysis_status")
    public val analysisStatus: CodeScanningVariantAnalysisStatus,
    @SerialName("result_count")
    public val resultCount: Long? = null,
    @SerialName("artifact_size_in_bytes")
    public val artifactSizeInBytes: Long? = null,
    @SerialName("failure_message")
    public val failureMessage: String? = null,
  )

  /**
   * Information about repositories that were skipped from processing. This information is only available to the user that initiated the variant analysis.
   */
  @Serializable
  public data class SkippedRepositories(
    @SerialName("access_mismatch_repos")
    public val accessMismatchRepos: CodeScanningVariantAnalysisSkippedRepoGroup,
    @SerialName("not_found_repos")
    public val notFoundRepos: NotFoundRepos,
    @SerialName("no_codeql_db_repos")
    public val noCodeqlDbRepos: CodeScanningVariantAnalysisSkippedRepoGroup,
    @SerialName("over_limit_repos")
    public val overLimitRepos: CodeScanningVariantAnalysisSkippedRepoGroup,
  ) {
    @Serializable
    public data class NotFoundRepos(
      @SerialName("repository_count")
      public val repositoryCount: Long,
      @SerialName("repository_full_names")
      public val repositoryFullNames: List<String>,
    )
  }

  @Serializable
  public enum class Status(
    public val `value`: String,
  ) {
    @SerialName("in_progress")
    InProgress("in_progress"),
    @SerialName("succeeded")
    Succeeded("succeeded"),
    @SerialName("failed")
    Failed("failed"),
    @SerialName("cancelled")
    Cancelled("cancelled"),
    ;
  }
}
