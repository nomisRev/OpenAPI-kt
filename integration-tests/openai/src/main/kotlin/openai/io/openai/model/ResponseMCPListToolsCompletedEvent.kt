package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Emitted when the list of available MCP tools has been successfully retrieved.
 *
 */
@Serializable
public data class ResponseMCPListToolsCompletedEvent(
  public val type: Type,
  @SerialName("item_id")
  public val itemId: String,
  @SerialName("output_index")
  public val outputIndex: Long,
  @SerialName("sequence_number")
  public val sequenceNumber: Long,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("response.mcp_list_tools.completed")
    ResponseMcpListToolsCompleted("response.mcp_list_tools.completed"),
    ;
  }
}
