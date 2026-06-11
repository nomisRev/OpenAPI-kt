package io.github.model

import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * API Insights usage subject stats for an organization
 */
@JvmInline
@Serializable
public value class ApiInsightsSubjectStats(
  public val items: List<Item>,
) {
  @Serializable
  public data class Item(
    @SerialName("subject_type")
    public val subjectType: String? = null,
    @SerialName("subject_name")
    public val subjectName: String? = null,
    @SerialName("subject_id")
    public val subjectId: Long? = null,
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
