package io.openai.model

import kotlin.Double
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class HybridSearchOptions(
  @SerialName("embedding_weight")
  public val embeddingWeight: Double,
  @SerialName("text_weight")
  public val textWeight: Double,
)
