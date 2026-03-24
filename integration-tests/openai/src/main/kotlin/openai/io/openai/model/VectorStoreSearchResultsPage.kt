package io.openai.model

import kotlin.Boolean
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class VectorStoreSearchResultsPage(
  public val `object`: Object,
  @SerialName("search_query")
  public val searchQuery: List<String>,
  public val `data`: List<VectorStoreSearchResultItem>,
  @SerialName("has_more")
  public val hasMore: Boolean,
  @SerialName("next_page")
  public val nextPage: String?,
) {
  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("vector_store.search_results.page")
    VectorStoreSearchResultsPage("vector_store.search_results.page"),
    ;
  }
}
