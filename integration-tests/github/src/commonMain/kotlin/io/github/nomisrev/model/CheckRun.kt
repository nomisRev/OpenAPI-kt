package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlin.jvm.JvmInline

@Serializable
data class CheckRun(
    val id: Long,
    @SerialName("head_sha") val headSha: String,
    @SerialName("node_id") val nodeId: String,
    @SerialName("external_id") val externalId: String?,
    val url: String,
    @SerialName("html_url") val htmlUrl: String?,
    @SerialName("details_url") val detailsUrl: String?,
    val status: Status,
    val conclusion: Conclusion?,
    @SerialName("started_at") val startedAt: LocalDateTime?,
    @SerialName("completed_at") val completedAt: LocalDateTime?,
    val output: Output,
    val name: String,
    @SerialName("check_suite") val checkSuite: CheckSuite?,
    val app: NullableIntegration?,
    @SerialName("pull_requests") val pullRequests: List<PullRequestMinimal>,
    val deployment: DeploymentSimple? = null,
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
    data class Output(
        val title: String?,
        val summary: String?,
        val text: String?,
        @SerialName("annotations_count") val annotationsCount: Long,
        @SerialName("annotations_url") val annotationsUrl: String,
    )

    @Serializable
    @JvmInline
    value class CheckSuite(val id: Long)
}
