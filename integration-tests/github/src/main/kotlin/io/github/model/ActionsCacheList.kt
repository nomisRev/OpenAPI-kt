package io.github.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.time.Instant
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
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
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class ActionsCaches(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val id: Long? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val ref: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val key: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val version: String? = null,
    @SerialName("last_accessed_at")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val lastAccessedAt: Instant? = null,
    @SerialName("created_at")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val createdAt: Instant? = null,
    @SerialName("size_in_bytes")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val sizeInBytes: Long? = null,
  )
}
