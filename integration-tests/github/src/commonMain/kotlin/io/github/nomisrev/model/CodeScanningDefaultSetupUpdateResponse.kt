package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class CodeScanningDefaultSetupUpdateResponse(
    @SerialName("run_id") val runId: Long? = null,
    @SerialName("run_url") val runUrl: String? = null,
)
