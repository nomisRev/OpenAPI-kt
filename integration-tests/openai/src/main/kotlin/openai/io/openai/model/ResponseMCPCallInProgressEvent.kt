package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Emitted when an MCP  tool call is in progress.
 *
 */
@Serializable
public data class ResponseMCPCallInProgressEvent(
  public val type: Type,
  @SerialName("sequence_number")
  public val sequenceNumber: Long,
  @SerialName("output_index")
  public val outputIndex: Long,
  @SerialName("item_id")
  public val itemId: String,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("response.mcp_call.in_progress")
    ResponseMcpCallInProgress("response.mcp_call.in_progress"),
    ;
  }
}
