package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class AssistantToolsFileSearch(
  public val type: Type,
  @SerialName("file_search")
  public val fileSearch: FileSearch? = null,
) {
  /**
   * Overrides for the file search tool.
   */
  @Serializable
  public data class FileSearch(
    @SerialName("max_num_results")
    public val maxNumResults: Long? = null,
    @SerialName("ranking_options")
    public val rankingOptions: FileSearchRankingOptions? = null,
  )

  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("file_search")
    FileSearch("file_search"),
    ;
  }
}
