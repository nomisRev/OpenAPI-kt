package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * The results of a web search tool call. See the
 * [web search guide](/docs/guides/tools-web-search) for more information.
 *
 */
@Serializable
public data class WebSearchToolCall(
  public val id: String,
  public val type: Type,
  public val status: Status,
  public val action: JsonElement,
) {
  @Serializable
  public enum class Status(
    public val `value`: String,
  ) {
    @SerialName("in_progress")
    InProgress("in_progress"),
    @SerialName("searching")
    Searching("searching"),
    @SerialName("completed")
    Completed("completed"),
    @SerialName("failed")
    Failed("failed"),
    ;
  }

  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("web_search_call")
    WebSearchCall("web_search_call"),
    ;
  }
}
