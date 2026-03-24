package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Emitted when the code interpreter is actively interpreting the code snippet.
 */
@Serializable
public data class ResponseCodeInterpreterCallInterpretingEvent(
  public val type: Type,
  @SerialName("output_index")
  public val outputIndex: Long,
  @SerialName("item_id")
  public val itemId: String,
  @SerialName("sequence_number")
  public val sequenceNumber: Long,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("response.code_interpreter_call.interpreting")
    ResponseCodeInterpreterCallInterpreting("response.code_interpreter_call.interpreting"),
    ;
  }
}
