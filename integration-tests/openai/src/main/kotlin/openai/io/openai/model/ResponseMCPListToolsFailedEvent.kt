package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Emitted when the attempt to list available MCP tools has failed.
 *
 */
@Serializable
public data class ResponseMCPListToolsFailedEvent(
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
    @SerialName("response.mcp_list_tools.failed")
    ResponseMcpListToolsFailed("response.mcp_list_tools.failed"),
    ;
  }
}
