package io.github.model

import kotlin.Long
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * An enterprise on GitHub.
 */
@Serializable
public data class Enterprise(
  public val description: String? = null,
  @SerialName("html_url")
  public val htmlUrl: String,
  @SerialName("website_url")
  public val websiteUrl: String? = null,
  public val id: Long,
  @SerialName("node_id")
  public val nodeId: String,
  public val name: String,
  public val slug: String,
  @SerialName("created_at")
  public val createdAt: Instant?,
  @SerialName("updated_at")
  public val updatedAt: Instant?,
  @SerialName("avatar_url")
  public val avatarUrl: String,
)
