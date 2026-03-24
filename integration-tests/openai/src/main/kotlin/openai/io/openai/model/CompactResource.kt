package io.openai.model

import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class CompactResource(
  public val id: String,
  @Required
  public val `object`: Object = Object.ResponseCompaction,
  public val output: List<ItemField>,
  @SerialName("created_at")
  public val createdAt: Long,
  public val usage: ResponseUsage,
) {
  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("response.compaction")
    ResponseCompaction("response.compaction"),
    ;
  }
}
