package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Emitted when an output item is marked done.
 */
@Serializable
public data class ResponseOutputItemDoneEvent(
  public val type: Type,
  @SerialName("output_index")
  public val outputIndex: Long,
  @SerialName("sequence_number")
  public val sequenceNumber: Long,
  public val item: OutputItem,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("response.output_item.done")
    ResponseOutputItemDone("response.output_item.done"),
    ;
  }
}
