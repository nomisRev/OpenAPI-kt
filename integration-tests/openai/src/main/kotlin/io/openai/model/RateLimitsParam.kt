package io.openai.model

import kotlin.Long
import kotlin.OptIn
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Controls request rate limits for the session.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class RateLimitsParam(
  @SerialName("max_requests_per_1_minute")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val maxRequestsPer1Minute: Long? = null,
)
