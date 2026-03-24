package io.openai.model

import kotlin.Boolean
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ListFineTuningJobEventsResponse(
  public val `data`: List<FineTuningJobEvent>,
  public val `object`: Object,
  @SerialName("has_more")
  public val hasMore: Boolean,
) {
  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("list")
    List("list"),
    ;
  }
}
