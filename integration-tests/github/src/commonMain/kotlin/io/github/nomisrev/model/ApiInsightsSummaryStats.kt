package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ApiInsightsSummaryStats(
    @SerialName("total_request_count") val totalRequestCount: Long? = null,
    @SerialName("rate_limited_request_count") val rateLimitedRequestCount: Long? = null,
)
