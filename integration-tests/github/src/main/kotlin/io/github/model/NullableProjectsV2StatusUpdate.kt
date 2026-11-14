package io.github.model

import kotlin.Double
import kotlin.OptIn
import kotlin.String
import kotlin.time.Instant
import kotlinx.datetime.LocalDate
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * An status update belonging to a project
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class NullableProjectsV2StatusUpdate(
  public val id: Double,
  @SerialName("node_id")
  public val nodeId: String,
  @SerialName("project_node_id")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val projectNodeId: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val creator: SimpleUser? = null,
  @SerialName("created_at")
  public val createdAt: Instant,
  @SerialName("updated_at")
  public val updatedAt: Instant,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val status: Optional<Status> = Optional.Absent,
  @SerialName("start_date")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val startDate: LocalDate? = null,
  @SerialName("target_date")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val targetDate: LocalDate? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val body: Optional<String> = Optional.Absent,
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
