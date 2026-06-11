package io.github.model

import kotlin.Double
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement

/**
 * An item belonging to a project
 */
@Serializable
public data class ProjectsV2ItemWithContent(
  public val id: Double,
  @SerialName("node_id")
  public val nodeId: String? = null,
  @SerialName("project_url")
  public val projectUrl: String? = null,
  @SerialName("content_type")
  public val contentType: ProjectsV2ItemContentType,
  public val content: JsonElement? = null,
  public val creator: SimpleUser? = null,
  @SerialName("created_at")
  public val createdAt: Instant,
  @SerialName("updated_at")
  public val updatedAt: Instant,
  @SerialName("archived_at")
  public val archivedAt: Instant?,
  @SerialName("item_url")
  public val itemUrl: String? = null,
  public val fields: JsonArray? = null,
)
