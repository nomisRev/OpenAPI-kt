package io.github.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The status of a commit.
 */
@Serializable
public data class Status(
  public val url: String,
  @SerialName("avatar_url")
  public val avatarUrl: String?,
  public val id: Long,
  @SerialName("node_id")
  public val nodeId: String,
  public val state: String,
  public val description: String?,
  @SerialName("target_url")
  public val targetUrl: String?,
  public val context: String,
  @SerialName("created_at")
  public val createdAt: String,
  @SerialName("updated_at")
  public val updatedAt: String,
  public val creator: NullableSimpleUser?,
)
