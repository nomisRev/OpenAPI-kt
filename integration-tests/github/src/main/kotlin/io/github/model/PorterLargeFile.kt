package io.github.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Porter Large File
 */
@Serializable
public data class PorterLargeFile(
  @SerialName("ref_name")
  public val refName: String,
  public val path: String,
  public val oid: String,
  public val size: Long,
)
