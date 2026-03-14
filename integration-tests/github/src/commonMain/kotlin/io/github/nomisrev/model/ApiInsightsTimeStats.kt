package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName

@Serializable
@JvmInline
value class ApiInsightsTimeStats(val items: List<Item>) {
    @Serializable
    data class Item(
        val timestamp: String? = null,
        @SerialName("total_request_count") val totalRequestCount: Long? = null,
        @SerialName("rate_limited_request_count") val rateLimitedRequestCount: Long? = null,
    )
}
