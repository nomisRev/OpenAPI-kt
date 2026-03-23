package io.github.model

import kotlinx.serialization.Serializable

/**
 * Limit interactions to a specific type of user for a specified duration
 */
@Serializable
public data class InteractionLimit(
  public val limit: InteractionGroup,
  public val expiry: InteractionExpiry? = null,
)
