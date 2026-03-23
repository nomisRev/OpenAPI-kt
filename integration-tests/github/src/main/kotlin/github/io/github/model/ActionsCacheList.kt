package io.github.model

import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Repository actions caches
 */
@Serializable
public data class ActionsCacheList(
  @SerialName("total_count")
  public val totalCount: Long,
  @SerialName("actions_caches")
  public val actionsCaches: List<ActionsCaches>,
) {
  @Serializable
  public data class ActionsCaches(
    public val id: Long? = null,
    public val ref: String? = null,
    public val key: String? = null,
    public val version: String? = null,
    @SerialName("last_accessed_at")
    public val lastAccessedAt: Instant? = null,
    @SerialName("created_at")
    public val createdAt: Instant? = null,
    @SerialName("size_in_bytes")
    public val sizeInBytes: Long? = null,
  )
}
