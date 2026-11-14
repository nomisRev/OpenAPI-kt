package io.openai.model

import kotlin.Long
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Active per-minute request limit for the session.
 */
@Serializable
public data class ChatSessionRateLimits(
  @SerialName("max_requests_per_1_minute")
  public val maxRequestsPer1Minute: Long,
)
