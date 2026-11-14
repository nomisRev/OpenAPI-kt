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
 * API Insights usage time stats for an organization
 */
@JvmInline
@Serializable
public value class ApiInsightsTimeStats(
  public val items: List<Item>,
) {
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Item(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val timestamp: String? = null,
    @SerialName("total_request_count")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val totalRequestCount: Long? = null,
    @SerialName("rate_limited_request_count")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val rateLimitedRequestCount: Long? = null,
  )
}
