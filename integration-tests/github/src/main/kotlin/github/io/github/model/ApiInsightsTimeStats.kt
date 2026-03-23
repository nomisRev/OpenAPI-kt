package io.github.model

import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * API Insights usage time stats for an organization
 */
@JvmInline
@Serializable
public value class ApiInsightsTimeStats(
  public val items: List<Item>,
) {
  @Serializable
  public data class Item(
    public val timestamp: String? = null,
    @SerialName("total_request_count")
    public val totalRequestCount: Long? = null,
    @SerialName("rate_limited_request_count")
    public val rateLimitedRequestCount: Long? = null,
  )
}
