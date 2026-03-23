package io.github.model

import kotlin.Long
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ActionsCacheUsageOrgEnterprise(
  @SerialName("total_active_caches_count")
  public val totalActiveCachesCount: Long,
  @SerialName("total_active_caches_size_in_bytes")
  public val totalActiveCachesSizeInBytes: Long,
)
