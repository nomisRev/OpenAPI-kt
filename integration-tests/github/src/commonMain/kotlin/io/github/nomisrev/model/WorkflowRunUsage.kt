package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class WorkflowRunUsage(val billable: Billable, @SerialName("run_duration_ms") val runDurationMs: Long? = null) {
    @Serializable
    data class Billable(
        @SerialName("UBUNTU") val uBUNTU: UBUNTU? = null,
        @SerialName("MACOS") val mACOS: MACOS? = null,
        @SerialName("WINDOWS") val wINDOWS: WINDOWS? = null,
    ) {
        @Serializable
        data class UBUNTU(
            @SerialName("total_ms") val totalMs: Long,
            val jobs: Long,
            @SerialName("job_runs") val jobRuns: List<JobRuns>? = null,
        ) {
            @Serializable
            data class JobRuns(@SerialName("job_id") val jobId: Long, @SerialName("duration_ms") val durationMs: Long)
        }

        @Serializable
        data class MACOS(
            @SerialName("total_ms") val totalMs: Long,
            val jobs: Long,
            @SerialName("job_runs") val jobRuns: List<JobRuns>? = null,
        ) {
            @Serializable
            data class JobRuns(@SerialName("job_id") val jobId: Long, @SerialName("duration_ms") val durationMs: Long)
        }

        @Serializable
        data class WINDOWS(
            @SerialName("total_ms") val totalMs: Long,
            val jobs: Long,
            @SerialName("job_runs") val jobRuns: List<JobRuns>? = null,
        ) {
            @Serializable
            data class JobRuns(@SerialName("job_id") val jobId: Long, @SerialName("duration_ms") val durationMs: Long)
        }
    }
}
