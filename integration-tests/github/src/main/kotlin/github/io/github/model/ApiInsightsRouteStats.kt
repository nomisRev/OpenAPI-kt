package io.github.model

import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * API Insights usage route stats for an actor
 */
@JvmInline
@Serializable
public value class ApiInsightsRouteStats(
  public val items: List<Item>,
) {
  @Serializable
  public data class Item(
    @SerialName("http_method")
    public val httpMethod: String? = null,
    @SerialName("api_route")
    public val apiRoute: String? = null,
    @SerialName("total_request_count")
    public val totalRequestCount: Long? = null,
    @SerialName("rate_limited_request_count")
    public val rateLimitedRequestCount: Long? = null,
    @SerialName("last_rate_limited_timestamp")
    public val lastRateLimitedTimestamp: String? = null,
    @SerialName("last_request_timestamp")
    public val lastRequestTimestamp: String? = null,
  )
}
