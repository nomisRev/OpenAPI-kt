package io.github.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Describe a region within a file for the alert.
 */
@Serializable
public data class CodeScanningAlertLocation(
  public val path: String? = null,
  @SerialName("start_line")
  public val startLine: Long? = null,
  @SerialName("end_line")
  public val endLine: Long? = null,
  @SerialName("start_column")
  public val startColumn: Long? = null,
  @SerialName("end_column")
  public val endColumn: Long? = null,
)
