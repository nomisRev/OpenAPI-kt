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
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val version: Optional<CodeScanningAnalysisToolVersion> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val guid: Optional<CodeScanningAnalysisToolGuid> = Optional.Absent,
)
