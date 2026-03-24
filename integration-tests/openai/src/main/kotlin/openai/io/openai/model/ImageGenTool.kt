package io.openai.model

import kotlin.Long
import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * A tool that generates images using the GPT image models.
 *
 */
@Serializable
public data class ImageGenTool(
  public val type: Type,
  public val model: Model? = null,
  public val quality: Quality? = null,
  public val size: Size? = null,
  @SerialName("output_format")
  public val outputFormat: OutputFormat? = null,
  @SerialName("output_compression")
  public val outputCompression: Long? = null,
  public val moderation: Moderation? = null,
  public val background: Background? = null,
  @SerialName("input_fidelity")
  public val inputFidelity: InputFidelity? = null,
  @SerialName("input_image_mask")
  public val inputImageMask: InputImageMask? = null,
  @SerialName("partial_images")
  public val partialImages: Long? = null,
  public val action: ImageGenActionEnum? = null,
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

  /**
   * Optional mask for inpainting. Contains `image_url`
   * (string, optional) and `file_id` (string, optional).
   *
   */
  @Serializable
  public data class InputImageMask(
    @SerialName("image_url")
    public val imageUrl: String? = null,
    @SerialName("file_id")
    public val fileId: String? = null,
  )

  @Serializable(with = Model.Serializer::class)
  public sealed interface Model {
    public val `value`: String

    @Serializable
    @JvmInline
    public value class CaseString(
      override val `value`: String,
    ) : Model

    @Serializable
    public enum class GptImage1OrGptImage1MiniOrGptImage15(
      override val `value`: String,
    ) : Model {
      @SerialName("gpt-image-1")
      GptImage1("gpt-image-1"),
      @SerialName("gpt-image-1-mini")
      GptImage1Mini("gpt-image-1-mini"),
      @SerialName("gpt-image-1.5")
      GptImage15("gpt-image-1.5"),
      ;
    }

    public object Serializer : KSerializer<Model> {
      override val descriptor: SerialDescriptor = String.serializer().descriptor

      override fun serialize(encoder: Encoder, `value`: Model) {
        when(value) {
          GptImage1OrGptImage1MiniOrGptImage15.GptImage1 -> encoder.encodeString("gpt-image-1")
          GptImage1OrGptImage1MiniOrGptImage15.GptImage1Mini -> encoder.encodeString("gpt-image-1-mini")
          GptImage1OrGptImage1MiniOrGptImage15.GptImage15 -> encoder.encodeString("gpt-image-1.5")
          is CaseString -> encoder.encodeString(value.value)
        }
      }

      override fun deserialize(decoder: Decoder): Model = when(val value = decoder.decodeString()) {
        "gpt-image-1" -> GptImage1OrGptImage1MiniOrGptImage15.GptImage1
        "gpt-image-1-mini" -> GptImage1OrGptImage1MiniOrGptImage15.GptImage1Mini
        "gpt-image-1.5" -> GptImage1OrGptImage1MiniOrGptImage15.GptImage15
        else -> CaseString(value)
      }
    }
  }

  @Serializable
  public enum class Moderation(
    public val `value`: String,
  ) {
    @SerialName("auto")
    Auto("auto"),
    @SerialName("low")
    Low("low"),
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
    @SerialName("image_generation")
    ImageGeneration("image_generation"),
    ;
  }
}
