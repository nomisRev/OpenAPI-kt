package io.github.model

import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * An invocation of a workflow
 */
@Serializable
public data class WorkflowRun(
  public val id: Long,
  public val name: String? = null,
  @SerialName("node_id")
  public val nodeId: String,
  @SerialName("check_suite_id")
  public val checkSuiteId: Long? = null,
  @SerialName("check_suite_node_id")
  public val checkSuiteNodeId: String? = null,
  @SerialName("head_branch")
  public val headBranch: String?,
  @SerialName("head_sha")
  public val headSha: String,
  public val path: String,
  @SerialName("run_number")
  public val runNumber: Long,
  @SerialName("run_attempt")
  public val runAttempt: Long? = null,
  @SerialName("referenced_workflows")
  public val referencedWorkflows: List<ReferencedWorkflow>? = null,
  public val event: String,
  public val status: String?,
  public val conclusion: String?,
  @SerialName("workflow_id")
  public val workflowId: Long,
  public val url: String,
  @SerialName("html_url")
  public val htmlUrl: String,
  @SerialName("pull_requests")
  public val pullRequests: List<PullRequestMinimal>?,
  @SerialName("created_at")
  public val createdAt: Instant,
  @SerialName("updated_at")
  public val updatedAt: Instant,
  public val actor: SimpleUser? = null,
  @SerialName("triggering_actor")
  public val triggeringActor: SimpleUser? = null,
  @SerialName("run_started_at")
  public val runStartedAt: Instant? = null,
  @SerialName("jobs_url")
  public val jobsUrl: String,
  @SerialName("logs_url")
  public val logsUrl: String,
  @SerialName("check_suite_url")
  public val checkSuiteUrl: String,
  @SerialName("artifacts_url")
  public val artifactsUrl: String,
  @SerialName("cancel_url")
  public val cancelUrl: String,
  @SerialName("rerun_url")
  public val rerunUrl: String,
  @SerialName("previous_attempt_url")
  public val previousAttemptUrl: String? = null,
  @SerialName("workflow_url")
  public val workflowUrl: String,
  @SerialName("head_commit")
  public val headCommit: NullableSimpleCommit?,
  public val repository: MinimalRepository,
  @SerialName("head_repository")
  public val headRepository: MinimalRepository,
  @SerialName("head_repository_id")
  public val headRepositoryId: Long? = null,
  @SerialName("display_title")
  public val displayTitle: String,
)
