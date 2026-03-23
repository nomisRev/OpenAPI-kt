package io.github.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Referrer Traffic
 */
@Serializable
public data class ReferrerTraffic(
  public val referrer: String,
  public val count: Long,
  public val uniques: Long,
)
