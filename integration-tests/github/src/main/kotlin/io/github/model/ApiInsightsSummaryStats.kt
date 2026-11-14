package io.github.model

import kotlin.Long
import kotlin.OptIn
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * API Insights usage summary stats for an organization
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class ApiInsightsSummaryStats(
  @SerialName("total_request_count")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val totalRequestCount: Long? = null,
  @SerialName("rate_limited_request_count")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val rateLimitedRequestCount: Long? = null,
)
