package io.github.model

import kotlin.Long
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Workflow Run Usage
 */
@Serializable
public data class WorkflowRunUsage(
  public val billable: Billable,
  @SerialName("run_duration_ms")
  public val runDurationMs: Long? = null,
) {
  @Serializable
  public data class Billable(
    @SerialName("UBUNTU")
    public val ubuntu: UBUNTU? = null,
    @SerialName("MACOS")
    public val macos: MACOS? = null,
    @SerialName("WINDOWS")
    public val windows: WINDOWS? = null,
  ) {
    @Serializable
    public data class MACOS(
      @SerialName("total_ms")
      public val totalMs: Long,
      public val jobs: Long,
      @SerialName("job_runs")
      public val jobRuns: List<JobRuns>? = null,
    ) {
      @Serializable
      public data class JobRuns(
        @SerialName("job_id")
        public val jobId: Long,
        @SerialName("duration_ms")
        public val durationMs: Long,
      )
    }

    @Serializable
    public data class UBUNTU(
      @SerialName("total_ms")
      public val totalMs: Long,
      public val jobs: Long,
      @SerialName("job_runs")
      public val jobRuns: List<JobRuns>? = null,
    ) {
      @Serializable
      public data class JobRuns(
        @SerialName("job_id")
        public val jobId: Long,
        @SerialName("duration_ms")
        public val durationMs: Long,
      )
    }

    @Serializable
    public data class WINDOWS(
      @SerialName("total_ms")
      public val totalMs: Long,
      public val jobs: Long,
      @SerialName("job_runs")
      public val jobRuns: List<JobRuns>? = null,
    ) {
      @Serializable
      public data class JobRuns(
        @SerialName("job_id")
        public val jobId: Long,
        @SerialName("duration_ms")
        public val durationMs: Long,
      )
    }
  }
}
