package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName

@Serializable
@JvmInline
value class ActionsCacheRetentionLimitForRepository(@SerialName("max_cache_retention_days") val maxCacheRetentionDays: Long? = null)
