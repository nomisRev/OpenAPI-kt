package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Emitted when an image generation tool call is in progress.
 *
 */
@Serializable
public data class ResponseImageGenCallInProgressEvent(
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
    @SerialName("response.image_generation_call.in_progress")
    ResponseImageGenerationCallInProgress("response.image_generation_call.in_progress"),
    ;
  }
}
