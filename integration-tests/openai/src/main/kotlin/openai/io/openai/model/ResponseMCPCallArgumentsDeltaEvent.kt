package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Emitted when there is a delta (partial update) to the arguments of an MCP tool call.
 *
 */
@Serializable
public data class ResponseMCPCallArgumentsDeltaEvent(
  public val type: Type,
  @SerialName("output_index")
  public val outputIndex: Long,
  @SerialName("item_id")
  public val itemId: String,
  public val delta: String,
  @SerialName("sequence_number")
  public val sequenceNumber: Long,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("response.mcp_call_arguments.delta")
    ResponseMcpCallArgumentsDelta("response.mcp_call_arguments.delta"),
    ;
  }
}
