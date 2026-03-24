package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Event representing a delta (partial update) to the input of a custom tool call.
 *
 */
@Serializable
public data class ResponseCustomToolCallInputDeltaEvent(
  public val type: Type,
  @SerialName("sequence_number")
  public val sequenceNumber: Long,
  @SerialName("output_index")
  public val outputIndex: Long,
  @SerialName("item_id")
  public val itemId: String,
  public val delta: String,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("response.custom_tool_call_input.delta")
    ResponseCustomToolCallInputDelta("response.custom_tool_call_input.delta"),
    ;
  }
}
