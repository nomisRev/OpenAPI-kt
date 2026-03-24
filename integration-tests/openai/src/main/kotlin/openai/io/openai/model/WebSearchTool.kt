package io.openai.model

import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Search the Internet for sources related to the prompt. Learn more about the
 * [web search tool](/docs/guides/tools-web-search).
 *
 */
@Serializable
public data class WebSearchTool(
  @Required
  public val type: Type = Type.WebSearch,
  public val filters: Filters? = null,
  @SerialName("user_location")
  public val userLocation: WebSearchApproximateLocation? = null,
  @SerialName("search_context_size")
  public val searchContextSize: SearchContextSize? = null,
) {
  /**
   * Filters for the search.
   *
   */
  @JvmInline
  @Serializable
  public value class Filters(
    @SerialName("allowed_domains")
    public val allowedDomains: List<String>? = null,
  )

  @Serializable
  public enum class SearchContextSize(
    public val `value`: String,
  ) {
    @SerialName("low")
    Low("low"),
    @SerialName("medium")
    Medium("medium"),
    @SerialName("high")
    High("high"),
    ;
  }

  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("web_search")
    WebSearch("web_search"),
    @SerialName("web_search_2025_08_26")
    WebSearch20250826("web_search_2025_08_26"),
    ;
  }
}
