package io.github.model

import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Combined Commit Status
 */
@Serializable
public data class CombinedCommitStatus(
  public val state: String,
  public val statuses: List<SimpleCommitStatus>,
  public val sha: String,
  @SerialName("total_count")
  public val totalCount: Long,
  public val repository: MinimalRepository,
  @SerialName("commit_url")
  public val commitUrl: String,
  public val url: String,
)
