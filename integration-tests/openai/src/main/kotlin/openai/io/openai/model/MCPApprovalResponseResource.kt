package io.openai.model

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A response to an MCP approval request.
 *
 */
@Serializable
public data class MCPApprovalResponseResource(
  public val type: Type,
  public val id: String,
  @SerialName("approval_request_id")
  public val approvalRequestId: String,
  public val approve: Boolean,
  public val reason: String? = null,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("mcp_approval_response")
    McpApprovalResponse("mcp_approval_response"),
    ;
  }
}
