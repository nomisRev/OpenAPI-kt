package io.github.model

import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A check performed on the code of a given code change
 */
@Serializable
public data class CheckRun(
  public val id: Long,
  @SerialName("head_sha")
  public val headSha: String,
  @SerialName("node_id")
  public val nodeId: String,
  @SerialName("external_id")
  public val externalId: String?,
  public val url: String,
  @SerialName("html_url")
  public val htmlUrl: String?,
  @SerialName("details_url")
  public val detailsUrl: String?,
  public val status: Status,
  public val conclusion: Conclusion?,
  @SerialName("started_at")
  public val startedAt: Instant?,
  @SerialName("completed_at")
  public val completedAt: Instant?,
  public val output: Output,
  public val name: String,
  @SerialName("check_suite")
  public val checkSuite: CheckSuite?,
  public val app: NullableIntegration?,
  @SerialName("pull_requests")
  public val pullRequests: List<PullRequestMinimal>,
  public val deployment: DeploymentSimple? = null,
) {
  @JvmInline
  @Serializable
  public value class CheckSuite(
    public val id: Long,
  )

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
  public data class Output(
    public val title: String?,
    public val summary: String?,
    public val text: String?,
    @SerialName("annotations_count")
    public val annotationsCount: Long,
    @SerialName("annotations_url")
    public val annotationsUrl: String,
  )

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
