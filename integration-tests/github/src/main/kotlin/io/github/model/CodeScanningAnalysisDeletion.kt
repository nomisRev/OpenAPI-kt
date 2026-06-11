package io.github.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Successful deletion of a code scanning analysis
 */
@Serializable
public data class CodeScanningAnalysisDeletion(
  @SerialName("next_analysis_url")
  public val nextAnalysisUrl: String?,
  @SerialName("confirm_delete_url")
  public val confirmDeleteUrl: String?,
)
