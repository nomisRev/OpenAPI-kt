package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName

@Serializable
@JvmInline
value class ActionsCacheStorageLimitForEnterprise(@SerialName("max_cache_size_gb") val maxCacheSizeGb: Long? = null)
