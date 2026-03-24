package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Learn about [image inputs](/docs/guides/vision).
 *
 */
@Serializable
public data class ChatCompletionRequestMessageContentPartImage(
  public val type: Type,
  @SerialName("image_url")
  public val imageUrl: ImageUrl,
) {
  @Serializable
  public data class ImageUrl(
    public val url: String,
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
    @SerialName("image_url")
    ImageUrl("image_url"),
    ;
  }
}
