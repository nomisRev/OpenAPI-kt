package io.github.model

import kotlin.Long
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * GitHub Actions cache storage policy for a repository.
 */
@JvmInline
@Serializable
public value class ActionsCacheStorageLimitForRepositoryWrite(
  @SerialName("max_cache_size_gb")
  public val maxCacheSizeGb: Long? = null,
)
