package io.github.model

import kotlin.Double
import kotlin.OptIn
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement

/**
 * An item belonging to a project
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class ProjectsV2ItemWithContent(
  public val id: Double,
  @SerialName("node_id")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val nodeId: String? = null,
  @SerialName("project_url")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val projectUrl: String? = null,
  @SerialName("content_type")
  public val contentType: ProjectsV2ItemContentType,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val content: Optional<JsonElement> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val creator: SimpleUser? = null,
  @SerialName("created_at")
  public val createdAt: Instant,
  @SerialName("updated_at")
  public val updatedAt: Instant,
  @SerialName("archived_at")
  public val archivedAt: Instant?,
  @SerialName("item_url")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val itemUrl: Optional<String> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val fields: JsonArray? = null,
)
