package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Emitted when a new reasoning summary part is added.
 */
@Serializable
public data class ResponseReasoningSummaryPartAddedEvent(
  public val type: Type,
  @SerialName("item_id")
  public val itemId: String,
  @SerialName("output_index")
  public val outputIndex: Long,
  @SerialName("summary_index")
  public val summaryIndex: Long,
  @SerialName("sequence_number")
  public val sequenceNumber: Long,
  public val part: Part,
) {
  /**
   * The summary part that was added.
   *
   */
  @Serializable
  public data class Part(
    public val type: Type,
    public val text: String,
  ) {
    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("summary_text")
      SummaryText("summary_text"),
      ;
    }
  }

  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("response.reasoning_summary_part.added")
    ResponseReasoningSummaryPartAdded("response.reasoning_summary_part.added"),
    ;
  }
}
