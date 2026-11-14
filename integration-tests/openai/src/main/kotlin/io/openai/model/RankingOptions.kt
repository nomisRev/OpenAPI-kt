package io.openai.model

import kotlin.Double
import kotlin.OptIn
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class RankingOptions(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val ranker: RankerVersionType? = null,
  @SerialName("score_threshold")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val scoreThreshold: Double? = null,
  @SerialName("hybrid_search")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val hybridSearch: HybridSearchOptions? = null,
)
