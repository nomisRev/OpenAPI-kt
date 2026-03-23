package io.github.model

import kotlin.Boolean
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Check immutable releases
 */
@Serializable
public data class CheckImmutableReleases(
  public val enabled: Boolean,
  @SerialName("enforced_by_owner")
  public val enforcedByOwner: Boolean,
)
