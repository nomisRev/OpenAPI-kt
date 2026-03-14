package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class CodeScanningAnalysisDeletionResponse(
    @SerialName("next_analysis_url") val nextAnalysisUrl: String?,
    @SerialName("confirm_delete_url") val confirmDeleteUrl: String?,
)
