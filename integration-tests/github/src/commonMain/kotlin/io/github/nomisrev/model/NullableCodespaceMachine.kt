package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class NullableCodespaceMachine(
    val name: String,
    @SerialName("display_name") val displayName: String,
    @SerialName("operating_system") val operatingSystem: String,
    @SerialName("storage_in_bytes") val storageInBytes: Long,
    @SerialName("memory_in_bytes") val memoryInBytes: Long,
    val cpus: Long,
    @SerialName("prebuild_availability") val prebuildAvailability: PrebuildAvailability?,
) {
    @Serializable
    enum class PrebuildAvailability {
        @SerialName("none") None, @SerialName("ready") Ready, @SerialName("in_progress") InProgress;
    }
}
