package io.openai.model

import kotlin.Boolean
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ApiKeyList(
  public val `object`: String? = null,
  public val `data`: List<AdminApiKey>? = null,
  @SerialName("has_more")
  public val hasMore: Boolean? = null,
  @SerialName("first_id")
  public val firstId: String? = null,
  @SerialName("last_id")
  public val lastId: String? = null,
)
