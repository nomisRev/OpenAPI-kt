package io.github.model

import kotlin.Long
import kotlin.OptIn
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Workflow Run Usage
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class WorkflowRunUsage(
  public val billable: Billable,
  @SerialName("run_duration_ms")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val runDurationMs: Long? = null,
) {
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Billable(
    @SerialName("UBUNTU")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val ubuntu: UBUNTU? = null,
    @SerialName("MACOS")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val macos: MACOS? = null,
    @SerialName("WINDOWS")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val windows: WINDOWS? = null,
  ) {
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class MACOS(
      @SerialName("total_ms")
      public val totalMs: Long,
      public val jobs: Long,
      @SerialName("job_runs")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
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

    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class UBUNTU(
      @SerialName("total_ms")
      public val totalMs: Long,
      public val jobs: Long,
      @SerialName("job_runs")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
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

    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class WINDOWS(
      @SerialName("total_ms")
      public val totalMs: Long,
      public val jobs: Long,
      @SerialName("job_runs")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
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
