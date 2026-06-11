package io.openai.model

import kotlin.Double
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The ranking options for the file search. If not specified, the file search tool will use the `auto` ranker and a score_threshold of 0.
 *
 * See the [file search tool documentation](/docs/assistants/tools/file-search#customizing-file-search-settings) for more information.
 *
 */
@Serializable
public data class FileSearchRankingOptions(
  public val ranker: FileSearchRanker? = null,
  @SerialName("score_threshold")
  public val scoreThreshold: Double,
)
