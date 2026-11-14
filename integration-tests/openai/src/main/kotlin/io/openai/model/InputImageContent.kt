package io.openai.model

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * An image input to the model. Learn about [image inputs](/docs/guides/vision).
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class InputImageContent(
  @Required
  public val type: Type = Type.InputImage,
  @SerialName("image_url")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val imageUrl: Optional<String> = Optional.Absent,
  @SerialName("file_id")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val fileId: Optional<String> = Optional.Absent,
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
