package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Emitted when an image generation tool call has completed and the final image is available.
 *
 */
@Serializable
public data class ResponseImageGenCallCompletedEvent(
  public val type: Type,
  @SerialName("output_index")
  public val outputIndex: Long,
  @SerialName("sequence_number")
  public val sequenceNumber: Long,
  @SerialName("item_id")
  public val itemId: String,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("response.image_generation_call.completed")
    ResponseImageGenerationCallCompleted("response.image_generation_call.completed"),
    ;
  }
}
