package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class CodeScanningAnalysisResponse(
    val ref: CodeScanningRef,
    @SerialName("commit_sha") val commitSha: CodeScanningAnalysisCommitSha,
    @SerialName("analysis_key") val analysisKey: CodeScanningAnalysisAnalysisKey,
    val environment: CodeScanningAnalysisEnvironment,
    val category: CodeScanningAnalysisCategory? = null,
    val error: String,
    @SerialName("created_at") val createdAt: CodeScanningAnalysisCreatedAtResponse,
    @SerialName("results_count") val resultsCount: Long,
    @SerialName("rules_count") val rulesCount: Long,
    val id: Long,
    val url: CodeScanningAnalysisUrlResponse,
    @SerialName("sarif_id") val sarifId: CodeScanningAnalysisSarifId,
    val tool: CodeScanningAnalysisTool,
    val deletable: Boolean,
    val warning: String,
)
