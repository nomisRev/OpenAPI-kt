package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ProjectsV2DraftIssue(
    val id: Double,
    @SerialName("node_id") val nodeId: String,
    val title: String,
    val body: String? = null,
    val user: NullableSimpleUser?,
    @SerialName("created_at") val createdAt: LocalDateTime,
    @SerialName("updated_at") val updatedAt: LocalDateTime,
)
