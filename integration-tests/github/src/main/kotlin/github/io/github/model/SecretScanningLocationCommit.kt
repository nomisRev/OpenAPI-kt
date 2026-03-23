package io.github.model

import kotlin.Double
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a 'commit' secret scanning location type. This location type shows that a secret was detected inside a commit to a repository.
 */
@Serializable
public data class SecretScanningLocationCommit(
  public val path: String,
  @SerialName("start_line")
  public val startLine: Double,
  @SerialName("end_line")
  public val endLine: Double,
  @SerialName("start_column")
  public val startColumn: Double,
  @SerialName("end_column")
  public val endColumn: Double,
  @SerialName("blob_sha")
  public val blobSha: String,
  @SerialName("blob_url")
  public val blobUrl: String,
  @SerialName("commit_sha")
  public val commitSha: String,
  @SerialName("commit_url")
  public val commitUrl: String,
)
