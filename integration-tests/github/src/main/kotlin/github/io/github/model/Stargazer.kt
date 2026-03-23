package io.github.model

import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Stargazer
 */
@Serializable
public data class Stargazer(
  @SerialName("starred_at")
  public val starredAt: Instant,
  public val user: NullableSimpleUser?,
)
