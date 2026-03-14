package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class CodeScanningVariantAnalysis(
    val id: Long,
    @SerialName("controller_repo") val controllerRepo: SimpleRepository,
    val actor: SimpleUser,
    @SerialName("query_language") val queryLanguage: CodeScanningVariantAnalysisLanguage,
    @SerialName("query_pack_url") val queryPackUrl: String,
    @SerialName("created_at") val createdAt: LocalDateTime? = null,
    @SerialName("updated_at") val updatedAt: LocalDateTime? = null,
    @SerialName("completed_at") val completedAt: LocalDateTime? = null,
    val status: Status,
    @SerialName("actions_workflow_run_id") val actionsWorkflowRunId: Long? = null,
    @SerialName("failure_reason") val failureReason: FailureReason? = null,
    @SerialName("scanned_repositories") val scannedRepositories: List<ScannedRepositories>? = null,
    @SerialName("skipped_repositories") val skippedRepositories: SkippedRepositories? = null,
) {
    @Serializable
    enum class Status {
        @SerialName("in_progress")
        InProgress,
        @SerialName("succeeded")
        Succeeded,
        @SerialName("failed")
        Failed,
        @SerialName("cancelled")
        Cancelled;
    }

    @Serializable
    enum class FailureReason {
        @SerialName("no_repos_queried")
        NoReposQueried,
        @SerialName("actions_workflow_run_failed")
        ActionsWorkflowRunFailed,
        @SerialName("internal_error")
        InternalError;
    }

    @Serializable
    data class ScannedRepositories(
        val repository: CodeScanningVariantAnalysisRepository,
        @SerialName("analysis_status") val analysisStatus: CodeScanningVariantAnalysisStatus,
        @SerialName("result_count") val resultCount: Long? = null,
        @SerialName("artifact_size_in_bytes") val artifactSizeInBytes: Long? = null,
        @SerialName("failure_message") val failureMessage: String? = null,
    )

    @Serializable
    data class SkippedRepositories(
        @SerialName("access_mismatch_repos") val accessMismatchRepos: CodeScanningVariantAnalysisSkippedRepoGroup,
        @SerialName("not_found_repos") val notFoundRepos: NotFoundRepos,
        @SerialName("no_codeql_db_repos") val noCodeqlDbRepos: CodeScanningVariantAnalysisSkippedRepoGroup,
        @SerialName("over_limit_repos") val overLimitRepos: CodeScanningVariantAnalysisSkippedRepoGroup,
    ) {
        @Serializable
        data class NotFoundRepos(
            @SerialName("repository_count") val repositoryCount: Long,
            @SerialName("repository_full_names") val repositoryFullNames: List<String>,
        )
    }
}
