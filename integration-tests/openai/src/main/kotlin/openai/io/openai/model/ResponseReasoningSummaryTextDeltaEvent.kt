package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Emitted when a delta is added to a reasoning summary text.
 */
@Serializable
public data class ResponseReasoningSummaryTextDeltaEvent(
  public val type: Type,
  @SerialName("item_id")
  public val itemId: String,
  @SerialName("output_index")
  public val outputIndex: Long,
  @SerialName("summary_index")
  public val summaryIndex: Long,
  public val delta: String,
  @SerialName("sequence_number")
  public val sequenceNumber: Long,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("response.reasoning_summary_text.delta")
    ResponseReasoningSummaryTextDelta("response.reasoning_summary_text.delta"),
    ;
  }
}
