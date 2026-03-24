package io.openai.model

import kotlin.String
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Hosted or BYOT tool search configuration for deferred tools.
 */
@Serializable
public data class ToolSearchToolParam(
  @Required
  public val type: Type = Type.ToolSearch,
  public val execution: ToolSearchExecutionType? = null,
  public val description: String? = null,
  public val parameters: EmptyModelParam? = null,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("tool_search")
    ToolSearch("tool_search"),
    ;
  }
}
