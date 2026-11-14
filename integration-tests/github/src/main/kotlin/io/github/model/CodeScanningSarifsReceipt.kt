package io.github.model

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class CodeScanningSarifsReceipt(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: CodeScanningAnalysisSarifId? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val url: String? = null,
)
