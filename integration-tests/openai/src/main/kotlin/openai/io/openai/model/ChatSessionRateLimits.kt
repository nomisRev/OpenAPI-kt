package io.openai.model

import kotlin.Long
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Active per-minute request limit for the session.
 */
@JvmInline
@Serializable
public value class ChatSessionRateLimits(
  @SerialName("max_requests_per_1_minute")
  public val maxRequestsPer1Minute: Long,
)
