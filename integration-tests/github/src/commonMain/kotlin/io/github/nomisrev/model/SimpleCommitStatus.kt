package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class SimpleCommitStatus(
    val description: String?,
    val id: Long,
    @SerialName("node_id") val nodeId: String,
    val state: String,
    val context: String,
    @SerialName("target_url") val targetUrl: String?,
    val required: Boolean? = null,
    @SerialName("avatar_url") val avatarUrl: String?,
    val url: String,
    @SerialName("created_at") val createdAt: LocalDateTime,
    @SerialName("updated_at") val updatedAt: LocalDateTime,
)
