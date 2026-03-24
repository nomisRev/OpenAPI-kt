package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Emitted when there is a partial function-call arguments delta.
 */
@Serializable
public data class ResponseFunctionCallArgumentsDeltaEvent(
  public val type: Type,
  @SerialName("item_id")
  public val itemId: String,
  @SerialName("output_index")
  public val outputIndex: Long,
  @SerialName("sequence_number")
  public val sequenceNumber: Long,
  public val delta: String,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("response.function_call_arguments.delta")
    ResponseFunctionCallArgumentsDelta("response.function_call_arguments.delta"),
    ;
  }
}
