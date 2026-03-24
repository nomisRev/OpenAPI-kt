package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Emitted when image generation has completed and the final image is available.
 *
 */
@Serializable
public data class ImageGenCompletedEvent(
  public val type: Type,
  @SerialName("b64_json")
  public val b64Json: String,
  @SerialName("created_at")
  public val createdAt: Long,
  public val size: Size,
  public val quality: Quality,
  public val background: Background,
  @SerialName("output_format")
  public val outputFormat: OutputFormat,
  public val usage: ImagesUsage,
) {
  @Serializable
  public enum class Background(
    public val `value`: String,
  ) {
    @SerialName("transparent")
    Transparent("transparent"),
    @SerialName("opaque")
    Opaque("opaque"),
    @SerialName("auto")
    Auto("auto"),
    ;
  }

  @Serializable
  public enum class OutputFormat(
    public val `value`: String,
  ) {
    @SerialName("png")
    Png("png"),
    @SerialName("webp")
    Webp("webp"),
    @SerialName("jpeg")
    Jpeg("jpeg"),
    ;
  }

  @Serializable
  public enum class Quality(
    public val `value`: String,
  ) {
    @SerialName("low")
    Low("low"),
    @SerialName("medium")
    Medium("medium"),
    @SerialName("high")
    High("high"),
    @SerialName("auto")
    Auto("auto"),
    ;
  }

  @Serializable
  public enum class Size(
    public val `value`: String,
  ) {
    `1024x1024`("1024x1024"),
    `1024x1536`("1024x1536"),
    `1536x1024`("1536x1024"),
    @SerialName("auto")
    Auto("auto"),
    ;
  }

  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("image_generation.completed")
    ImageGenerationCompleted("image_generation.completed"),
    ;
  }
}
