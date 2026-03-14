package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ActionsCacheUsageByRepository(
    @SerialName("full_name") val fullName: String,
    @SerialName("active_caches_size_in_bytes") val activeCachesSizeInBytes: Long,
    @SerialName("active_caches_count") val activeCachesCount: Long,
)
