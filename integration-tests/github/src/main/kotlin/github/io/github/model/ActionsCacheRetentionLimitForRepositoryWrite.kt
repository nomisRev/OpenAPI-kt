package io.github.model

import kotlin.Long
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * GitHub Actions cache retention policy for a repository.
 */
@JvmInline
@Serializable
public value class ActionsCacheRetentionLimitForRepositoryWrite(
  @SerialName("max_cache_retention_days")
  public val maxCacheRetentionDays: Long? = null,
)
