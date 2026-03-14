package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class CodeScanningAnalysisTool(
    val name: CodeScanningAnalysisToolName? = null,
    val version: CodeScanningAnalysisToolVersion? = null,
    val guid: CodeScanningAnalysisToolGuid? = null,
)
