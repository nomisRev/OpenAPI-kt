package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A request for human approval of a tool invocation.
 *
 */
@Serializable
public data class MCPApprovalRequest(
  public val type: Type,
  public val id: String,
  @SerialName("server_label")
  public val serverLabel: String,
  public val name: String,
  public val arguments: String,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("mcp_approval_request")
    McpApprovalRequest("mcp_approval_request"),
    ;
  }
}
