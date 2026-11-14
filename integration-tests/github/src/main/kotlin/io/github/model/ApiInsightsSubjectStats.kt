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
 * API Insights usage subject stats for an organization
 */
@JvmInline
@Serializable
public value class ApiInsightsSubjectStats(
  public val items: List<Item>,
) {
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Item(
    @SerialName("subject_type")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val subjectType: String? = null,
    @SerialName("subject_name")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val subjectName: String? = null,
    @SerialName("subject_id")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val subjectId: Long? = null,
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
