package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Emitted when refusal text is finalized.
 */
@Serializable
public data class ResponseRefusalDoneEvent(
  public val type: Type,
  @SerialName("item_id")
  public val itemId: String,
  @SerialName("output_index")
  public val outputIndex: Long,
  @SerialName("content_index")
  public val contentIndex: Long,
  public val refusal: String,
  @SerialName("sequence_number")
  public val sequenceNumber: Long,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("response.refusal.done")
    ResponseRefusalDone("response.refusal.done"),
    ;
  }
}
