package io.openai.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * JSON request body for image edits.
 *
 * Use `images` (array of `ImageRefParam`) instead of multipart `image` uploads.
 * You can reference images via external URLs, data URLs, or uploaded file IDs.
 * JSON edits support GPT image models only; DALL-E edits require multipart (`dall-e-2` only).
 *
 */
@Serializable
public data class EditImageBodyJsonParam(
  public val model: Model? = null,
  public val images: List<ImageRefParam>,
  public val mask: ImageRefParam? = null,
  public val prompt: String,
  public val n: Long? = null,
  public val quality: Quality? = null,
  @SerialName("input_fidelity")
  public val inputFidelity: InputFidelity? = null,
  public val size: Size? = null,
  public val user: String? = null,
  @SerialName("output_format")
  public val outputFormat: OutputFormat? = null,
  @SerialName("output_compression")
  public val outputCompression: Long? = null,
  public val moderation: Moderation? = null,
  public val background: Background? = null,
  public val stream: Boolean? = null,
  @SerialName("partial_images")
  public val partialImages: PartialImages? = null,
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
  public enum class InputFidelity(
    public val `value`: String,
  ) {
    @SerialName("high")
    High("high"),
    @SerialName("low")
    Low("low"),
    ;
  }

  @Serializable(with = Model.Serializer::class)
  public sealed interface Model {
    public val `value`: String

    @Serializable
    @JvmInline
    public value class CaseString(
      override val `value`: String,
    ) : Model

    @Serializable
    public enum class GptImage15OrGptImage1OrGptImage1MiniOrChatgptImageLatest(
      override val `value`: String,
    ) : Model {
      @SerialName("gpt-image-1.5")
      GptImage15("gpt-image-1.5"),
      @SerialName("gpt-image-1")
      GptImage1("gpt-image-1"),
      @SerialName("gpt-image-1-mini")
      GptImage1Mini("gpt-image-1-mini"),
      @SerialName("chatgpt-image-latest")
      ChatgptImageLatest("chatgpt-image-latest"),
      ;
    }

    public object Serializer : KSerializer<Model> {
      override val descriptor: SerialDescriptor = String.serializer().descriptor

      override fun serialize(encoder: Encoder, `value`: Model) {
        when(value) {
          GptImage15OrGptImage1OrGptImage1MiniOrChatgptImageLatest.GptImage15 -> encoder.encodeString("gpt-image-1.5")
          GptImage15OrGptImage1OrGptImage1MiniOrChatgptImageLatest.GptImage1 -> encoder.encodeString("gpt-image-1")
          GptImage15OrGptImage1OrGptImage1MiniOrChatgptImageLatest.GptImage1Mini -> encoder.encodeString("gpt-image-1-mini")
          GptImage15OrGptImage1OrGptImage1MiniOrChatgptImageLatest.ChatgptImageLatest -> encoder.encodeString("chatgpt-image-latest")
          is CaseString -> encoder.encodeString(value.value)
        }
      }

      override fun deserialize(decoder: Decoder): Model = when(val value = decoder.decodeString()) {
        "gpt-image-1.5" -> GptImage15OrGptImage1OrGptImage1MiniOrChatgptImageLatest.GptImage15
        "gpt-image-1" -> GptImage15OrGptImage1OrGptImage1MiniOrChatgptImageLatest.GptImage1
        "gpt-image-1-mini" -> GptImage15OrGptImage1OrGptImage1MiniOrChatgptImageLatest.GptImage1Mini
        "chatgpt-image-latest" -> GptImage15OrGptImage1OrGptImage1MiniOrChatgptImageLatest.ChatgptImageLatest
        else -> CaseString(value)
      }
    }
  }

  @Serializable
  public enum class Moderation(
    public val `value`: String,
  ) {
    @SerialName("low")
    Low("low"),
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
    @SerialName("jpeg")
    Jpeg("jpeg"),
    @SerialName("webp")
    Webp("webp"),
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
    @SerialName("auto")
    Auto("auto"),
    `1024x1024`("1024x1024"),
    `1536x1024`("1536x1024"),
    `1024x1536`("1024x1536"),
    ;
  }
}
