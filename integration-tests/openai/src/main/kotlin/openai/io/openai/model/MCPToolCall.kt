package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * An invocation of a tool on an MCP server.
 *
 */
@Serializable
public data class MCPToolCall(
  public val type: Type,
  public val id: String,
  @SerialName("server_label")
  public val serverLabel: String,
  public val name: String,
  public val arguments: String,
  public val output: String? = null,
  public val error: String? = null,
  public val status: MCPToolCallStatus? = null,
  @SerialName("approval_request_id")
  public val approvalRequestId: String? = null,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("mcp_call")
    McpCall("mcp_call"),
    ;
  }
}
