package io.github.model

import kotlin.Long
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * API Insights usage summary stats for an organization
 */
@Serializable
public data class ApiInsightsSummaryStats(
  @SerialName("total_request_count")
  public val totalRequestCount: Long? = null,
  @SerialName("rate_limited_request_count")
  public val rateLimitedRequestCount: Long? = null,
)
