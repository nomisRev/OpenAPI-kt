package io.github.model

import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Information of a job execution in a workflow run
 */
@Serializable
public data class Job(
  public val id: Long,
  @SerialName("run_id")
  public val runId: Long,
  @SerialName("run_url")
  public val runUrl: String,
  @SerialName("run_attempt")
  public val runAttempt: Long? = null,
  @SerialName("node_id")
  public val nodeId: String,
  @SerialName("head_sha")
  public val headSha: String,
  public val url: String,
  @SerialName("html_url")
  public val htmlUrl: String?,
  public val status: Status,
  public val conclusion: Conclusion?,
  @SerialName("created_at")
  public val createdAt: Instant,
  @SerialName("started_at")
  public val startedAt: Instant,
  @SerialName("completed_at")
  public val completedAt: Instant?,
  public val name: String,
  public val steps: List<Steps>? = null,
  @SerialName("check_run_url")
  public val checkRunUrl: String,
  public val labels: List<String>,
  @SerialName("runner_id")
  public val runnerId: Long?,
  @SerialName("runner_name")
  public val runnerName: String?,
  @SerialName("runner_group_id")
  public val runnerGroupId: Long?,
  @SerialName("runner_group_name")
  public val runnerGroupName: String?,
  @SerialName("workflow_name")
  public val workflowName: String?,
  @SerialName("head_branch")
  public val headBranch: String?,
) {
  @Serializable
  public enum class Conclusion(
    public val `value`: String,
  ) {
    @SerialName("success")
    Success("success"),
    @SerialName("failure")
    Failure("failure"),
    @SerialName("neutral")
    Neutral("neutral"),
    @SerialName("cancelled")
    Cancelled("cancelled"),
    @SerialName("skipped")
    Skipped("skipped"),
    @SerialName("timed_out")
    TimedOut("timed_out"),
    @SerialName("action_required")
    ActionRequired("action_required"),
    ;
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
  public data class Steps(
    public val status: Status,
    public val conclusion: String?,
    public val name: String,
    public val number: Long,
    @SerialName("started_at")
    public val startedAt: Instant? = null,
    @SerialName("completed_at")
    public val completedAt: Instant? = null,
  ) {
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
  }
}
