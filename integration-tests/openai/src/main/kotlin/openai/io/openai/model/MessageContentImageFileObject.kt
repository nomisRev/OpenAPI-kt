package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * References an image [File](/docs/api-reference/files) in the content of a message.
 */
@Serializable
public data class MessageContentImageFileObject(
  public val type: Type,
  @SerialName("image_file")
  public val imageFile: ImageFile,
) {
  @Serializable
  public data class ImageFile(
    @SerialName("file_id")
    public val fileId: String,
    public val detail: Detail? = null,
  ) {
    @Serializable
    public enum class Detail(
      public val `value`: String,
    ) {
      @SerialName("auto")
      Auto("auto"),
      @SerialName("low")
      Low("low"),
      @SerialName("high")
      High("high"),
      ;
    }
  }

  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("image_file")
    ImageFile("image_file"),
    ;
  }
}
