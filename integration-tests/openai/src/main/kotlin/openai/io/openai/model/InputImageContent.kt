package io.openai.model

import kotlin.String
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * An image input to the model. Learn about [image inputs](/docs/guides/vision).
 */
@Serializable
public data class InputImageContent(
  @Required
  public val type: Type = Type.InputImage,
  @SerialName("image_url")
  public val imageUrl: String? = null,
  @SerialName("file_id")
  public val fileId: String? = null,
  public val detail: ImageDetail,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("input_image")
    InputImage("input_image"),
    ;
  }
}
