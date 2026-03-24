package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Emitted when a content part is done.
 */
@Serializable
public data class ResponseContentPartDoneEvent(
  public val type: Type,
  @SerialName("item_id")
  public val itemId: String,
  @SerialName("output_index")
  public val outputIndex: Long,
  @SerialName("content_index")
  public val contentIndex: Long,
  @SerialName("sequence_number")
  public val sequenceNumber: Long,
  public val part: OutputContent,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("response.content_part.done")
    ResponseContentPartDone("response.content_part.done"),
    ;
  }
}
