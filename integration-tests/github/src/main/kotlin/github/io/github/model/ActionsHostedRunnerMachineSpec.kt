package io.github.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Provides details of a particular machine spec.
 */
@Serializable
public data class ActionsHostedRunnerMachineSpec(
  public val id: String,
  @SerialName("cpu_cores")
  public val cpuCores: Long,
  @SerialName("memory_gb")
  public val memoryGb: Long,
  @SerialName("storage_gb")
  public val storageGb: Long,
)
