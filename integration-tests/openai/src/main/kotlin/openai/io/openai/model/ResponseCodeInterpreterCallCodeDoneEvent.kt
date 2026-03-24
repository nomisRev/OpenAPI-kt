package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Emitted when the code snippet is finalized by the code interpreter.
 */
@Serializable
public data class ResponseCodeInterpreterCallCodeDoneEvent(
  public val type: Type,
  @SerialName("output_index")
  public val outputIndex: Long,
  @SerialName("item_id")
  public val itemId: String,
  public val code: String,
  @SerialName("sequence_number")
  public val sequenceNumber: Long,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("response.code_interpreter_call_code.done")
    ResponseCodeInterpreterCallCodeDone("response.code_interpreter_call_code.done"),
    ;
  }
}
