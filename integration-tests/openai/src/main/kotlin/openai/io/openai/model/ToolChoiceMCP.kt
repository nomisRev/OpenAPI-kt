package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Use this option to force the model to call a specific tool on a remote MCP server.
 *
 */
@Serializable
public data class ToolChoiceMCP(
  public val type: Type,
  @SerialName("server_label")
  public val serverLabel: String,
  public val name: String? = null,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("mcp")
    Mcp("mcp"),
    ;
  }
}
