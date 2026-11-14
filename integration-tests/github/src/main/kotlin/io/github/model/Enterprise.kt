package io.github.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * An enterprise on GitHub.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class Enterprise(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val description: Optional<String> = Optional.Absent,
  @SerialName("html_url")
  public val htmlUrl: String,
  @SerialName("website_url")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val websiteUrl: Optional<String> = Optional.Absent,
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
