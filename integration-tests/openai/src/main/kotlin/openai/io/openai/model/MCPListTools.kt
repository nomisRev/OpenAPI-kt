package io.openai.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A list of tools available on an MCP server.
 *
 */
@Serializable
public data class MCPListTools(
  public val type: Type,
  public val id: String,
  @SerialName("server_label")
  public val serverLabel: String,
  public val tools: List<MCPListToolsTool>,
  public val error: String? = null,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("mcp_list_tools")
    McpListTools("mcp_list_tools"),
    ;
  }
}
