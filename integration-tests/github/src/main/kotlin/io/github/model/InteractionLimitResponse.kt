package io.github.model

import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Interaction limit settings.
 */
@Serializable
public data class InteractionLimitResponse(
  public val limit: InteractionGroup,
  public val origin: String,
  @SerialName("expires_at")
  public val expiresAt: Instant,
)
