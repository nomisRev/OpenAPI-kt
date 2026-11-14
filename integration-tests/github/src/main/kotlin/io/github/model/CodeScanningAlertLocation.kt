package io.github.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Describe a region within a file for the alert.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class CodeScanningAlertLocation(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val path: String? = null,
  @SerialName("start_line")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val startLine: Long? = null,
  @SerialName("end_line")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val endLine: Long? = null,
  @SerialName("start_column")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val startColumn: Long? = null,
  @SerialName("end_column")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val endColumn: Long? = null,
)
