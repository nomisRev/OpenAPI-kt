package io.openai.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This tool searches the web for relevant results to use in a response. Learn more about the [web search tool](https://platform.openai.com/docs/guides/tools-web-search).
 */
@Serializable
public data class WebSearchPreviewTool(
  @Required
  public val type: Type = Type.WebSearchPreview,
  @SerialName("user_location")
  public val userLocation: ApproximateLocation? = null,
  @SerialName("search_context_size")
  public val searchContextSize: SearchContextSize? = null,
  @SerialName("search_content_types")
  public val searchContentTypes: List<SearchContentType>? = null,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("web_search_preview")
    WebSearchPreview("web_search_preview"),
    @SerialName("web_search_preview_2025_03_11")
    WebSearchPreview20250311("web_search_preview_2025_03_11"),
    ;
  }
}
