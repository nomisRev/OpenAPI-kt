package io.github.model

import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class CodeScanningAlertInstance(
  public val ref: CodeScanningRef? = null,
  @SerialName("analysis_key")
  public val analysisKey: CodeScanningAnalysisAnalysisKey? = null,
  public val environment: CodeScanningAlertEnvironment? = null,
  public val category: CodeScanningAnalysisCategory? = null,
  public val state: CodeScanningAlertState? = null,
  @SerialName("commit_sha")
  public val commitSha: String? = null,
  public val message: Message? = null,
  public val location: CodeScanningAlertLocation? = null,
  @SerialName("html_url")
  public val htmlUrl: String? = null,
  public val classifications: List<CodeScanningAlertClassification?>? = null,
) {
  @JvmInline
  @Serializable
  public value class Message(
    public val text: String? = null,
  )
}
