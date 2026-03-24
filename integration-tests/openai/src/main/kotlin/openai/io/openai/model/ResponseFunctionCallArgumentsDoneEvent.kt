package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Emitted when function-call arguments are finalized.
 */
@Serializable
public data class ResponseFunctionCallArgumentsDoneEvent(
  public val type: Type,
  @SerialName("item_id")
  public val itemId: String,
  public val name: String,
  @SerialName("output_index")
  public val outputIndex: Long,
  @SerialName("sequence_number")
  public val sequenceNumber: Long,
  public val arguments: String,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("response.function_call_arguments.done")
    ResponseFunctionCallArgumentsDone("response.function_call_arguments.done"),
    ;
  }
}
