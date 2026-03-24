package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Emitted when a code interpreter call is in progress.
 */
@Serializable
public data class ResponseCodeInterpreterCallInProgressEvent(
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
    @SerialName("response.code_interpreter_call.in_progress")
    ResponseCodeInterpreterCallInProgress("response.code_interpreter_call.in_progress"),
    ;
  }
}
