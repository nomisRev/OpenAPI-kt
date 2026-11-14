package io.github.model

import kotlin.Double
import kotlin.OptIn
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A draft issue in a project
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class ProjectsV2DraftIssue(
  public val id: Double,
  @SerialName("node_id")
  public val nodeId: String,
  public val title: String,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val body: Optional<String> = Optional.Absent,
  public val user: NullableSimpleUser?,
  @SerialName("created_at")
  public val createdAt: Instant,
  @SerialName("updated_at")
  public val updatedAt: Instant,
)
