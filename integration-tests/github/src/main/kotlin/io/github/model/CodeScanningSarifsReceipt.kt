package io.github.model

import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class CodeScanningSarifsReceipt(
  public val id: CodeScanningAnalysisSarifId? = null,
  public val url: String? = null,
)
