package io.github.model

import kotlin.Long
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Gist Commit
 */
@Serializable
public data class GistCommit(
  public val url: String,
  public val version: String,
  public val user: NullableSimpleUser?,
  @SerialName("change_status")
  public val changeStatus: ChangeStatus,
  @SerialName("committed_at")
  public val committedAt: Instant,
) {
  @Serializable
  public data class ChangeStatus(
    public val total: Long? = null,
    public val additions: Long? = null,
    public val deletions: Long? = null,
  )
}
