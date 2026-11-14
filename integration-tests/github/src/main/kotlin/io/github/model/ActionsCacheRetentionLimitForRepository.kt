package io.github.model

import kotlin.Long
import kotlin.OptIn
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * GitHub Actions cache retention policy for a repository.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class ActionsCacheRetentionLimitForRepository(
  @SerialName("max_cache_retention_days")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val maxCacheRetentionDays: Long? = null,
)
