package io.github.model

import kotlin.Long
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * GitHub Actions cache storage policy for an enterprise.
 */
@JvmInline
@Serializable
public value class ActionsCacheStorageLimitForEnterpriseRead(
  @SerialName("max_cache_size_gb")
  public val maxCacheSizeGb: Long? = null,
)
