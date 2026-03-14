package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlin.jvm.JvmInline

@Serializable
data class CodeScanningAlertInstance(
    val ref: CodeScanningRef? = null,
    @SerialName("analysis_key") val analysisKey: CodeScanningAnalysisAnalysisKey? = null,
    val environment: CodeScanningAlertEnvironment? = null,
    val category: CodeScanningAnalysisCategory? = null,
    val state: CodeScanningAlertState? = null,
    @SerialName("commit_sha") val commitSha: String? = null,
    val message: Message? = null,
    val location: CodeScanningAlertLocation? = null,
    @SerialName("html_url") val htmlUrl: String? = null,
    val classifications: List<CodeScanningAlertClassification>? = null,
) {
    @Serializable
    @JvmInline
    value class Message(val text: String? = null)
}
