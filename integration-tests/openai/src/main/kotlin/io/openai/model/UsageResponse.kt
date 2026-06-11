package io.openai.model

import kotlin.Boolean
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class UsageResponse(
  public val `object`: Object,
  public val `data`: List<UsageTimeBucket>,
  @SerialName("has_more")
  public val hasMore: Boolean,
  @SerialName("next_page")
  public val nextPage: String,
) {
  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("page")
    Page("page"),
    ;
  }
}
