package io.github.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * GitHub Actions Cache Usage by repository.
 */
@Serializable
public data class ActionsCacheUsageByRepository(
  @SerialName("full_name")
  public val fullName: String,
  @SerialName("active_caches_size_in_bytes")
  public val activeCachesSizeInBytes: Long,
  @SerialName("active_caches_count")
  public val activeCachesCount: Long,
)
