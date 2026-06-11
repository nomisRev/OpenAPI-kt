package io.openai.model

import kotlin.Boolean
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A list of Response items.
 */
@Serializable
public data class ResponseItemList(
  public val `object`: Object,
  public val `data`: List<ItemResource>,
  @SerialName("has_more")
  public val hasMore: Boolean,
  @SerialName("first_id")
  public val firstId: String,
  @SerialName("last_id")
  public val lastId: String,
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
