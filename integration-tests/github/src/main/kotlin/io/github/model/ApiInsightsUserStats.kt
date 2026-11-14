package io.github.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
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
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Item(
    @SerialName("actor_type")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val actorType: String? = null,
    @SerialName("actor_name")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val actorName: String? = null,
    @SerialName("actor_id")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val actorId: Long? = null,
    @SerialName("integration_id")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val integrationId: Optional<Long> = Optional.Absent,
    @SerialName("oauth_application_id")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val oauthApplicationId: Optional<Long> = Optional.Absent,
    @SerialName("total_request_count")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val totalRequestCount: Long? = null,
    @SerialName("rate_limited_request_count")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val rateLimitedRequestCount: Long? = null,
    @SerialName("last_rate_limited_timestamp")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val lastRateLimitedTimestamp: Optional<String> = Optional.Absent,
    @SerialName("last_request_timestamp")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val lastRequestTimestamp: String? = null,
  )
}
