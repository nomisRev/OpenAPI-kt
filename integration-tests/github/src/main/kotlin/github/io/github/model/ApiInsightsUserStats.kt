package io.github.model

import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * API Insights usage stats for a user
 */
@JvmInline
@Serializable
public value class ApiInsightsUserStats(
  public val items: List<Item>,
) {
  @Serializable
  public data class Item(
    @SerialName("actor_type")
    public val actorType: String? = null,
    @SerialName("actor_name")
    public val actorName: String? = null,
    @SerialName("actor_id")
    public val actorId: Long? = null,
    @SerialName("integration_id")
    public val integrationId: Long? = null,
    @SerialName("oauth_application_id")
    public val oauthApplicationId: Long? = null,
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
