package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class SimpleCommitStatus(
  public val description: String?,
  public val id: Long,
  @SerialName("node_id")
  public val nodeId: String,
  public val state: String,
  public val context: String,
  @SerialName("target_url")
  public val targetUrl: String?,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val required: Optional<Boolean> = Optional.Absent,
  @SerialName("avatar_url")
  public val avatarUrl: String?,
  public val url: String,
  @SerialName("created_at")
  public val createdAt: Instant,
  @SerialName("updated_at")
  public val updatedAt: Instant,
)
