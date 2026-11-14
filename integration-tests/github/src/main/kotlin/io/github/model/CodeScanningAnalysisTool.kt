package io.github.model

import kotlin.OptIn
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class CodeScanningAnalysisTool(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val name: CodeScanningAnalysisToolName? = null,
  public val version: CodeScanningAnalysisToolVersion? = null,
  public val guid: CodeScanningAnalysisToolGuid? = null,
)
