package io.github.model

import kotlin.Long
import kotlin.OptIn
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * GitHub Actions cache storage policy for an organization.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class ActionsCacheStorageLimitForOrganization(
  @SerialName("max_cache_size_gb")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val maxCacheSizeGb: Long? = null,
)
