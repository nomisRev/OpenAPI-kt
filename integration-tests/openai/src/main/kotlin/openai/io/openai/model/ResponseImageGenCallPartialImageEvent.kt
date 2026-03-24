package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Emitted when a partial image is available during image generation streaming.
 *
 */
@Serializable
public data class ResponseImageGenCallPartialImageEvent(
  public val type: Type,
  @SerialName("output_index")
  public val outputIndex: Long,
  @SerialName("item_id")
  public val itemId: String,
  @SerialName("sequence_number")
  public val sequenceNumber: Long,
  @SerialName("partial_image_index")
  public val partialImageIndex: Long,
  @SerialName("partial_image_b64")
  public val partialImageB64: String,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("response.image_generation_call.partial_image")
    ResponseImageGenerationCallPartialImage("response.image_generation_call.partial_image"),
    ;
  }
}
