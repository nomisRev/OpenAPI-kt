package io.github.model

import kotlin.Double
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A draft issue in a project
 */
@Serializable
public data class ProjectsV2DraftIssue(
  public val id: Double,
  @SerialName("node_id")
  public val nodeId: String,
  public val title: String,
  public val body: String? = null,
  public val user: NullableSimpleUser?,
  @SerialName("created_at")
  public val createdAt: Instant,
  @SerialName("updated_at")
  public val updatedAt: Instant,
)
