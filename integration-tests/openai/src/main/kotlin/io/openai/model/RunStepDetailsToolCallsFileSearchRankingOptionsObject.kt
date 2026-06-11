package io.openai.model

import kotlin.Double
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The ranking options for the file search.
 */
@Serializable
public data class RunStepDetailsToolCallsFileSearchRankingOptionsObject(
  public val ranker: FileSearchRanker,
  @SerialName("score_threshold")
  public val scoreThreshold: Double,
)
