package io.github.model

import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Information on a single scan performed by secret scanning on the repository
 */
@Serializable
public data class SecretScanningScan(
  public val type: String? = null,
  public val status: String? = null,
  @SerialName("completed_at")
  public val completedAt: Instant? = null,
  @SerialName("started_at")
  public val startedAt: Instant? = null,
)
