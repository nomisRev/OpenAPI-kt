package io.openai.model

import kotlin.Double
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class VectorStoreSearchResultItem(
  @SerialName("file_id")
  public val fileId: String,
  public val filename: String,
  public val score: Double,
  public val attributes: VectorStoreFileAttributes,
  public val content: List<VectorStoreSearchResultContentObject>,
)
