package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName

@Serializable
@JvmInline
value class ApiInsightsRouteStats(val items: List<Item>) {
    @Serializable
    data class Item(
        @SerialName("http_method") val httpMethod: String? = null,
        @SerialName("api_route") val apiRoute: String? = null,
        @SerialName("total_request_count") val totalRequestCount: Long? = null,
        @SerialName("rate_limited_request_count") val rateLimitedRequestCount: Long? = null,
        @SerialName("last_rate_limited_timestamp") val lastRateLimitedTimestamp: String? = null,
        @SerialName("last_request_timestamp") val lastRequestTimestamp: String? = null,
    )
}
