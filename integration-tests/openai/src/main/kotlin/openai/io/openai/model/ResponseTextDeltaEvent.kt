package io.openai.model

import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Emitted when there is an additional text delta.
 */
@Serializable
public data class ResponseTextDeltaEvent(
  public val type: Type,
  @SerialName("item_id")
  public val itemId: String,
  @SerialName("output_index")
  public val outputIndex: Long,
  @SerialName("content_index")
  public val contentIndex: Long,
  public val delta: String,
  @SerialName("sequence_number")
  public val sequenceNumber: Long,
  public val logprobs: List<ResponseLogProb>,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("response.output_text.delta")
    ResponseOutputTextDelta("response.output_text.delta"),
    ;
  }
}
