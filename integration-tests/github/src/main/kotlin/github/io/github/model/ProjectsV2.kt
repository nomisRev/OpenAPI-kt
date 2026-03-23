package io.github.model

import kotlin.Boolean
import kotlin.Double
import kotlin.Long
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A projects v2 project
 */
@Serializable
public data class ProjectsV2(
  public val id: Double,
  @SerialName("node_id")
  public val nodeId: String,
  public val owner: SimpleUser,
  public val creator: SimpleUser,
  public val title: String,
  public val description: String?,
  public val `public`: Boolean,
  @SerialName("closed_at")
  public val closedAt: Instant?,
  @SerialName("created_at")
  public val createdAt: Instant,
  @SerialName("updated_at")
  public val updatedAt: Instant,
  public val number: Long,
  @SerialName("short_description")
  public val shortDescription: String?,
  @SerialName("deleted_at")
  public val deletedAt: Instant?,
  @SerialName("deleted_by")
  public val deletedBy: NullableSimpleUser?,
  public val state: State? = null,
  @SerialName("latest_status_update")
  public val latestStatusUpdate: NullableProjectsV2StatusUpdate? = null,
  @SerialName("is_template")
  public val isTemplate: Boolean? = null,
) {
  @Serializable
  public enum class State(
    public val `value`: String,
  ) {
    @SerialName("open")
    Open("open"),
    @SerialName("closed")
    Closed("closed"),
    ;
  }
}
