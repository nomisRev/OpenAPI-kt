package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Emitted when the arguments for an MCP tool call are finalized.
 *
 */
@Serializable
public data class ResponseMCPCallArgumentsDoneEvent(
  public val type: Type,
  @SerialName("output_index")
  public val outputIndex: Long,
  @SerialName("item_id")
  public val itemId: String,
  public val arguments: String,
  @SerialName("sequence_number")
  public val sequenceNumber: Long,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("response.mcp_call_arguments.done")
    ResponseMcpCallArgumentsDone("response.mcp_call_arguments.done"),
    ;
  }
}
