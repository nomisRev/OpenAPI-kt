package io.openai.model

import kotlin.Boolean
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class SkillListResource(
  @Required
  public val `object`: Object = Object.List,
  public val `data`: List<SkillResource>,
  @SerialName("first_id")
  public val firstId: String?,
  @SerialName("last_id")
  public val lastId: String?,
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
