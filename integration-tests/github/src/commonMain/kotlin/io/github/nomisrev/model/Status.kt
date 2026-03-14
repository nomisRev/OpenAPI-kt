package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Status(
    val url: String,
    @SerialName("avatar_url") val avatarUrl: String?,
    val id: Long,
    @SerialName("node_id") val nodeId: String,
    val state: String,
    val description: String?,
    @SerialName("target_url") val targetUrl: String?,
    val context: String,
    @SerialName("created_at") val createdAt: String,
    @SerialName("updated_at") val updatedAt: String,
    val creator: NullableSimpleUser?,
)
