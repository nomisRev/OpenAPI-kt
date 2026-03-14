package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class CodeScanningVariantAnalysisRepoTask(
    val repository: SimpleRepository,
    @SerialName("analysis_status") val analysisStatus: CodeScanningVariantAnalysisStatus,
    @SerialName("artifact_size_in_bytes") val artifactSizeInBytes: Long? = null,
    @SerialName("result_count") val resultCount: Long? = null,
    @SerialName("failure_message") val failureMessage: String? = null,
    @SerialName("database_commit_sha") val databaseCommitSha: String? = null,
    @SerialName("source_location_prefix") val sourceLocationPrefix: String? = null,
    @SerialName("artifact_url") val artifactUrl: String? = null,
)
