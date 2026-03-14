package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ActionsCacheUsageOrgEnterprise(
    @SerialName("total_active_caches_count") val totalActiveCachesCount: Long,
    @SerialName("total_active_caches_size_in_bytes") val totalActiveCachesSizeInBytes: Long,
)
