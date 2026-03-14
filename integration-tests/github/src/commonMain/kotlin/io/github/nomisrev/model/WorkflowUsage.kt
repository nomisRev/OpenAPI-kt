package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName

@Serializable
@JvmInline
value class WorkflowUsage(val billable: Billable) {
    @Serializable
    data class Billable(
        @SerialName("UBUNTU") val uBUNTU: UBUNTU? = null,
        @SerialName("MACOS") val mACOS: MACOS? = null,
        @SerialName("WINDOWS") val wINDOWS: WINDOWS? = null,
    ) {
        @Serializable
        @JvmInline
        value class UBUNTU(@SerialName("total_ms") val totalMs: Long? = null)

        @Serializable
        @JvmInline
        value class MACOS(@SerialName("total_ms") val totalMs: Long? = null)

        @Serializable
        @JvmInline
        value class WINDOWS(@SerialName("total_ms") val totalMs: Long? = null)
    }
}
