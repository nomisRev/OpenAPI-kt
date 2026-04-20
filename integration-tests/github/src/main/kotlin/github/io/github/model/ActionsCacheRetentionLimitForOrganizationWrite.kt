package io.github.model

import kotlin.Long
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * GitHub Actions cache retention policy for an organization.
 */
@JvmInline
@Serializable
public value class ActionsCacheRetentionLimitForOrganizationWrite(
  @SerialName("max_cache_retention_days")
  public val maxCacheRetentionDays: Long? = null,
)
