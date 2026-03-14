package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ActionsHostedRunnerMachineSpec(
    val id: String,
    @SerialName("cpu_cores") val cpuCores: Long,
    @SerialName("memory_gb") val memoryGb: Long,
    @SerialName("storage_gb") val storageGb: Long,
)
