package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class WorkflowRun(
    val id: Long,
    val name: String? = null,
    @SerialName("node_id") val nodeId: String,
    @SerialName("check_suite_id") val checkSuiteId: Long? = null,
    @SerialName("check_suite_node_id") val checkSuiteNodeId: String? = null,
    @SerialName("head_branch") val headBranch: String?,
    @SerialName("head_sha") val headSha: String,
    val path: String,
    @SerialName("run_number") val runNumber: Long,
    @SerialName("run_attempt") val runAttempt: Long? = null,
    @SerialName("referenced_workflows") val referencedWorkflows: List<ReferencedWorkflow>? = null,
    val event: String,
    val status: String?,
    val conclusion: String?,
    @SerialName("workflow_id") val workflowId: Long,
    val url: String,
    @SerialName("html_url") val htmlUrl: String,
    @SerialName("pull_requests") val pullRequests: List<PullRequestMinimal>?,
    @SerialName("created_at") val createdAt: LocalDateTime,
    @SerialName("updated_at") val updatedAt: LocalDateTime,
    val actor: SimpleUser? = null,
    @SerialName("triggering_actor") val triggeringActor: SimpleUser? = null,
    @SerialName("run_started_at") val runStartedAt: LocalDateTime? = null,
    @SerialName("jobs_url") val jobsUrl: String,
    @SerialName("logs_url") val logsUrl: String,
    @SerialName("check_suite_url") val checkSuiteUrl: String,
    @SerialName("artifacts_url") val artifactsUrl: String,
    @SerialName("cancel_url") val cancelUrl: String,
    @SerialName("rerun_url") val rerunUrl: String,
    @SerialName("previous_attempt_url") val previousAttemptUrl: String? = null,
    @SerialName("workflow_url") val workflowUrl: String,
    @SerialName("head_commit") val headCommit: NullableSimpleCommit?,
    val repository: MinimalRepository,
    @SerialName("head_repository") val headRepository: MinimalRepository,
    @SerialName("head_repository_id") val headRepositoryId: Long? = null,
    @SerialName("display_title") val displayTitle: String,
)
