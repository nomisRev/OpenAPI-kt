package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * An image generation request made by the model.
 *
 */
@Serializable
public data class ImageGenToolCall(
  public val type: Type,
  public val id: String,
  public val status: Status,
  public val result: String?,
) {
  @Serializable
  public enum class Status(
    public val `value`: String,
  ) {
    @SerialName("in_progress")
    InProgress("in_progress"),
    @SerialName("completed")
    Completed("completed"),
    @SerialName("generating")
    Generating("generating"),
    @SerialName("failed")
    Failed("failed"),
    ;
  }

  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("image_generation_call")
    ImageGenerationCall("image_generation_call"),
    ;
  }
}
