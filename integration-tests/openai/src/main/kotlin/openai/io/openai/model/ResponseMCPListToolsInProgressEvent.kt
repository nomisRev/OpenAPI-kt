package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Emitted when the system is in the process of retrieving the list of available MCP tools.
 *
 */
@Serializable
public data class ResponseMCPListToolsInProgressEvent(
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
    @SerialName("response.mcp_list_tools.in_progress")
    ResponseMcpListToolsInProgress("response.mcp_list_tools.in_progress"),
    ;
  }
}
