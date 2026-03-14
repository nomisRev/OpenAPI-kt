package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.datetime.LocalDateTime

@Serializable
data class ActionsCacheList(
    @SerialName("total_count") val totalCount: Long,
    @SerialName("actions_caches") val actionsCaches: List<ActionsCaches>,
) {
    @Serializable
    data class ActionsCaches(
        val id: Long? = null,
        val ref: String? = null,
        val key: String? = null,
        val version: String? = null,
        @SerialName("last_accessed_at") val lastAccessedAt: LocalDateTime? = null,
        @SerialName("created_at") val createdAt: LocalDateTime? = null,
        @SerialName("size_in_bytes") val sizeInBytes: Long? = null,
    )
}
