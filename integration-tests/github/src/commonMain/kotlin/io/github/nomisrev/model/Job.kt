package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Job(
    val id: Long,
    @SerialName("run_id") val runId: Long,
    @SerialName("run_url") val runUrl: String,
    @SerialName("run_attempt") val runAttempt: Long? = null,
    @SerialName("node_id") val nodeId: String,
    @SerialName("head_sha") val headSha: String,
    val url: String,
    @SerialName("html_url") val htmlUrl: String?,
    val status: Status,
    val conclusion: Conclusion?,
    @SerialName("created_at") val createdAt: LocalDateTime,
    @SerialName("started_at") val startedAt: LocalDateTime,
    @SerialName("completed_at") val completedAt: LocalDateTime?,
    val name: String,
    val steps: List<Steps>? = null,
    @SerialName("check_run_url") val checkRunUrl: String,
    val labels: List<String>,
    @SerialName("runner_id") val runnerId: Long?,
    @SerialName("runner_name") val runnerName: String?,
    @SerialName("runner_group_id") val runnerGroupId: Long?,
    @SerialName("runner_group_name") val runnerGroupName: String?,
    @SerialName("workflow_name") val workflowName: String?,
    @SerialName("head_branch") val headBranch: String?,
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
        ActionRequired;
    }

    @Serializable
    data class Steps(
        val status: Status,
        val conclusion: String?,
        val name: String,
        val number: Long,
        @SerialName("started_at") val startedAt: LocalDateTime? = null,
        @SerialName("completed_at") val completedAt: LocalDateTime? = null,
    ) {
        @Serializable
        enum class Status {
            @SerialName("queued") Queued, @SerialName("in_progress") InProgress, @SerialName("completed") Completed;
        }
    }
}
