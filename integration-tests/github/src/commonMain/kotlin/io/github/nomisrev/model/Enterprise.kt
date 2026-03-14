package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Enterprise(
    val description: String? = null,
    @SerialName("html_url") val htmlUrl: String,
    @SerialName("website_url") val websiteUrl: String? = null,
    val id: Long,
    @SerialName("node_id") val nodeId: String,
    val name: String,
    val slug: String,
    @SerialName("created_at") val createdAt: LocalDateTime?,
    @SerialName("updated_at") val updatedAt: LocalDateTime?,
    @SerialName("avatar_url") val avatarUrl: String,
)
