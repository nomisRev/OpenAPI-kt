package io.github.model

import kotlinx.serialization.Serializable

@Serializable
public data class CodeScanningAnalysisTool(
  public val name: CodeScanningAnalysisToolName? = null,
  public val version: CodeScanningAnalysisToolVersion? = null,
  public val guid: CodeScanningAnalysisToolGuid? = null,
)
