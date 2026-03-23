package io.github.model

import kotlin.Long
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A CodeQL database.
 */
@Serializable
public data class CodeScanningCodeqlDatabase(
  public val id: Long,
  public val name: String,
  public val language: String,
  public val uploader: SimpleUser,
  @SerialName("content_type")
  public val contentType: String,
  public val size: Long,
  @SerialName("created_at")
  public val createdAt: Instant,
  @SerialName("updated_at")
  public val updatedAt: Instant,
  public val url: String,
  @SerialName("commit_oid")
  public val commitOid: String? = null,
)
