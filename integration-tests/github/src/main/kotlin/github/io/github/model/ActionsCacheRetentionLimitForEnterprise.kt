package io.github.model

import kotlin.Long
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * GitHub Actions cache retention policy for an enterprise.
 */
@JvmInline
@Serializable
public value class ActionsCacheRetentionLimitForEnterprise(
  @SerialName("max_cache_retention_days")
  public val maxCacheRetentionDays: Long? = null,
)
