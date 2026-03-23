package io.github.model

import kotlin.Long
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Gist History
 */
@Serializable
public data class GistHistory(
  public val user: NullableSimpleUser? = null,
  public val version: String? = null,
  @SerialName("committed_at")
  public val committedAt: Instant? = null,
  @SerialName("change_status")
  public val changeStatus: ChangeStatus? = null,
  public val url: String? = null,
) {
  @Serializable
  public data class ChangeStatus(
    public val total: Long? = null,
    public val additions: Long? = null,
    public val deletions: Long? = null,
  )
}
