package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Emitted when a partial code snippet is streamed by the code interpreter.
 */
@Serializable
public data class ResponseCodeInterpreterCallCodeDeltaEvent(
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
    @SerialName("response.code_interpreter_call_code.delta")
    ResponseCodeInterpreterCallCodeDelta("response.code_interpreter_call_code.delta"),
    ;
  }
}
