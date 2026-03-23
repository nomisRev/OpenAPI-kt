package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class CodeScanningAnalysis(
  public val ref: CodeScanningRef,
  @SerialName("commit_sha")
  public val commitSha: CodeScanningAnalysisCommitSha,
  @SerialName("analysis_key")
  public val analysisKey: CodeScanningAnalysisAnalysisKey,
  public val environment: CodeScanningAnalysisEnvironment,
  public val category: CodeScanningAnalysisCategory? = null,
  public val error: String,
  @SerialName("created_at")
  public val createdAt: CodeScanningAnalysisCreatedAt,
  @SerialName("results_count")
  public val resultsCount: Long,
  @SerialName("rules_count")
  public val rulesCount: Long,
  public val id: Long,
  public val url: CodeScanningAnalysisUrl,
  @SerialName("sarif_id")
  public val sarifId: CodeScanningAnalysisSarifId,
  public val tool: CodeScanningAnalysisTool,
  public val deletable: Boolean,
  public val warning: String,
)
