package io.github.model

import kotlin.Boolean
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Repository invitations let you manage who you collaborate with.
 */
@Serializable
public data class RepositorySubscription(
  public val subscribed: Boolean,
  public val ignored: Boolean,
  public val reason: String?,
  @SerialName("created_at")
  public val createdAt: Instant,
  public val url: String,
  @SerialName("repository_url")
  public val repositoryUrl: String,
)
