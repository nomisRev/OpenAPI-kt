package io.github.model

import kotlin.Double
import kotlin.String
import kotlin.time.Instant
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * An status update belonging to a project
 */
@Serializable
public data class NullableProjectsV2StatusUpdate(
  public val id: Double,
  @SerialName("node_id")
  public val nodeId: String,
  @SerialName("project_node_id")
  public val projectNodeId: String? = null,
  public val creator: SimpleUser? = null,
  @SerialName("created_at")
  public val createdAt: Instant,
  @SerialName("updated_at")
  public val updatedAt: Instant,
  public val status: Status? = null,
  @SerialName("start_date")
  public val startDate: LocalDate? = null,
  @SerialName("target_date")
  public val targetDate: LocalDate? = null,
  public val body: String? = null,
) {
  @Serializable
  public enum class Status(
    public val `value`: String,
  ) {
    INACTIVE("INACTIVE"),
    @SerialName("ON_TRACK")
    ONTRACK("ON_TRACK"),
    @SerialName("AT_RISK")
    ATRISK("AT_RISK"),
    @SerialName("OFF_TRACK")
    OFFTRACK("OFF_TRACK"),
    COMPLETE("COMPLETE"),
    ;
  }
}
