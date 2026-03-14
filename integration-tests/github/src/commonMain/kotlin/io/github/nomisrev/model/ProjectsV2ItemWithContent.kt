package io.github.nomisrev.model

import kotlinx.serialization.json.JsonElement
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ProjectsV2ItemWithContent(
    val id: Double,
    @SerialName("node_id") val nodeId: String? = null,
    @SerialName("project_url") val projectUrl: String? = null,
    @SerialName("content_type") val contentType: ProjectsV2ItemContentType,
    val content: JsonElement? = null,
    val creator: SimpleUser? = null,
    @SerialName("created_at") val createdAt: LocalDateTime,
    @SerialName("updated_at") val updatedAt: LocalDateTime,
    @SerialName("archived_at") val archivedAt: LocalDateTime?,
    @SerialName("item_url") val itemUrl: String? = null,
    val fields: JsonArray? = null,
)
