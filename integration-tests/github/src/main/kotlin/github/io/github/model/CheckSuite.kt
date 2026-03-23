package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A suite of checks performed on the code of a given code change
 */
@Serializable
public data class CheckSuite(
  public val id: Long,
  @SerialName("node_id")
  public val nodeId: String,
  @SerialName("head_branch")
  public val headBranch: String?,
  @SerialName("head_sha")
  public val headSha: String,
  public val status: Status?,
  public val conclusion: Conclusion?,
  public val url: String?,
  public val before: String?,
  public val after: String?,
  @SerialName("pull_requests")
  public val pullRequests: List<PullRequestMinimal>?,
  public val app: NullableIntegration?,
  public val repository: MinimalRepository,
  @SerialName("created_at")
  public val createdAt: Instant?,
  @SerialName("updated_at")
  public val updatedAt: Instant?,
  @SerialName("head_commit")
  public val headCommit: SimpleCommit,
  @SerialName("latest_check_runs_count")
  public val latestCheckRunsCount: Long,
  @SerialName("check_runs_url")
  public val checkRunsUrl: String,
  public val rerequestable: Boolean? = null,
  @SerialName("runs_rerequestable")
  public val runsRerequestable: Boolean? = null,
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
    @SerialName("startup_failure")
    StartupFailure("startup_failure"),
    @SerialName("stale")
    Stale("stale"),
    @SerialName("null")
    Null("null"),
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
}
