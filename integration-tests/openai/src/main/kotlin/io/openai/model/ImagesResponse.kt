package io.openai.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The response from the image generation endpoint.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class ImagesResponse(
  public val created: Long,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val `data`: List<Image>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val background: Background? = null,
  @SerialName("output_format")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val outputFormat: OutputFormat? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val size: Size? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val quality: Quality? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val usage: ImageGenUsage? = null,
) {
  @Serializable
  public enum class Background(
    public val `value`: String,
  ) {
    @SerialName("transparent")
    Transparent("transparent"),
    @SerialName("opaque")
    Opaque("opaque"),
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
    ;
  }

  @Serializable
  public enum class Size {
    `1024x1024`,
    `1024x1536`,
    `1536x1024`,
  }
}
