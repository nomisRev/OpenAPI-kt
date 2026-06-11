package io.openai.model

import kotlin.Boolean
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ListFilesResponse(
  public val `object`: String,
  public val `data`: List<OpenAIFile>,
  @SerialName("first_id")
  public val firstId: String,
  @SerialName("last_id")
  public val lastId: String,
  @SerialName("has_more")
  public val hasMore: Boolean,
)
