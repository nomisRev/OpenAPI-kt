package io.openai.model

import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A tool that searches for relevant content from uploaded files. Learn more about the [file search tool](https://platform.openai.com/docs/guides/tools-file-search).
 */
@Serializable
public data class FileSearchTool(
  @Required
  public val type: Type = Type.FileSearch,
  @SerialName("vector_store_ids")
  public val vectorStoreIds: List<String>,
  @SerialName("max_num_results")
  public val maxNumResults: Long? = null,
  @SerialName("ranking_options")
  public val rankingOptions: RankingOptions? = null,
  public val filters: Filters? = null,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("file_search")
    FileSearch("file_search"),
    ;
  }
}
