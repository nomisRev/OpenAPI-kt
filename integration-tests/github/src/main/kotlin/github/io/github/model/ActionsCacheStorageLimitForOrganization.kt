package io.github.model

import kotlin.Long
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * GitHub Actions cache storage policy for an organization.
 */
@JvmInline
@Serializable
public value class ActionsCacheStorageLimitForOrganization(
  @SerialName("max_cache_size_gb")
  public val maxCacheSizeGb: Long? = null,
)
