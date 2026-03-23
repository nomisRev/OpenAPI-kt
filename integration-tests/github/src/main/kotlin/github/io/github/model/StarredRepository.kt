package io.github.model

import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Starred Repository
 */
@Serializable
public data class StarredRepository(
  @SerialName("starred_at")
  public val starredAt: Instant,
  public val repo: Repository,
)
