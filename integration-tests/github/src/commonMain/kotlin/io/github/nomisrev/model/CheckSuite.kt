package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class CheckSuite(
    val id: Long,
    @SerialName("node_id") val nodeId: String,
    @SerialName("head_branch") val headBranch: String?,
    @SerialName("head_sha") val headSha: String,
    val status: Status?,
    val conclusion: Conclusion?,
    val url: String?,
    val before: String?,
    val after: String?,
    @SerialName("pull_requests") val pullRequests: List<PullRequestMinimal>?,
    val app: NullableIntegration?,
    val repository: MinimalRepository,
    @SerialName("created_at") val createdAt: LocalDateTime?,
    @SerialName("updated_at") val updatedAt: LocalDateTime?,
    @SerialName("head_commit") val headCommit: SimpleCommit,
    @SerialName("latest_check_runs_count") val latestCheckRunsCount: Long,
    @SerialName("check_runs_url") val checkRunsUrl: String,
    val rerequestable: Boolean? = null,
    @SerialName("runs_rerequestable") val runsRerequestable: Boolean? = null,
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
        @SerialName("success")
        Success,
        @SerialName("failure")
        Failure,
        @SerialName("neutral")
        Neutral,
        @SerialName("cancelled")
        Cancelled,
        @SerialName("skipped")
        Skipped,
        @SerialName("timed_out")
        TimedOut,
        @SerialName("action_required")
        ActionRequired,
        @SerialName("startup_failure")
        StartupFailure,
        @SerialName("stale")
        Stale,
        @SerialName("null")
        Null;
    }
}
