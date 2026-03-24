package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Emitted when a reasoning text is completed.
 */
@Serializable
public data class ResponseReasoningTextDoneEvent(
  public val type: Type,
  @SerialName("item_id")
  public val itemId: String,
  @SerialName("output_index")
  public val outputIndex: Long,
  @SerialName("content_index")
  public val contentIndex: Long,
  public val text: String,
  @SerialName("sequence_number")
  public val sequenceNumber: Long,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("response.reasoning_text.done")
    ResponseReasoningTextDone("response.reasoning_text.done"),
    ;
  }
}
