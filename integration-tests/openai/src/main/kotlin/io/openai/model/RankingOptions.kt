package io.openai.model

import kotlin.Double
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class RankingOptions(
  public val ranker: RankerVersionType? = null,
  @SerialName("score_threshold")
  public val scoreThreshold: Double? = null,
  @SerialName("hybrid_search")
  public val hybridSearch: HybridSearchOptions? = null,
)
