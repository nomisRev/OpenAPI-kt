package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName

@Serializable
@JvmInline
value class ApiInsightsUserStats(val items: List<Item>) {
    @Serializable
    data class Item(
        @SerialName("actor_type") val actorType: String? = null,
        @SerialName("actor_name") val actorName: String? = null,
        @SerialName("actor_id") val actorId: Long? = null,
        @SerialName("integration_id") val integrationId: Long? = null,
        @SerialName("oauth_application_id") val oauthApplicationId: Long? = null,
        @SerialName("total_request_count") val totalRequestCount: Long? = null,
        @SerialName("rate_limited_request_count") val rateLimitedRequestCount: Long? = null,
        @SerialName("last_rate_limited_timestamp") val lastRateLimitedTimestamp: String? = null,
        @SerialName("last_request_timestamp") val lastRequestTimestamp: String? = null,
    )
}
