package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Event indicating that input for a custom tool call is complete.
 *
 */
@Serializable
public data class ResponseCustomToolCallInputDoneEvent(
  public val type: Type,
  @SerialName("sequence_number")
  public val sequenceNumber: Long,
  @SerialName("output_index")
  public val outputIndex: Long,
  @SerialName("item_id")
  public val itemId: String,
  public val input: String,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("response.custom_tool_call_input.done")
    ResponseCustomToolCallInputDone("response.custom_tool_call_input.done"),
    ;
  }
}
