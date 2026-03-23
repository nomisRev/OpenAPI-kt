package io.github.model

import kotlin.Long
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Workflow Usage
 */
@JvmInline
@Serializable
public value class WorkflowUsage(
  public val billable: Billable,
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
    @JvmInline
    @Serializable
    public value class MACOS(
      @SerialName("total_ms")
      public val totalMs: Long? = null,
    )

    @JvmInline
    @Serializable
    public value class UBUNTU(
      @SerialName("total_ms")
      public val totalMs: Long? = null,
    )

    @JvmInline
    @Serializable
    public value class WINDOWS(
      @SerialName("total_ms")
      public val totalMs: Long? = null,
    )
  }
}
