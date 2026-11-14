package io.github.model

import kotlin.Long
import kotlin.OptIn
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Workflow Usage
 */
@Serializable
public data class WorkflowUsage(
  public val billable: Billable,
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
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val totalMs: Long? = null,
    )

    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class UBUNTU(
      @SerialName("total_ms")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val totalMs: Long? = null,
    )

    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class WINDOWS(
      @SerialName("total_ms")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val totalMs: Long? = null,
    )
  }
}
