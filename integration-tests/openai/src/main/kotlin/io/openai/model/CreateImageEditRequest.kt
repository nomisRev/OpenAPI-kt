package io.openai.model

import kotlin.Boolean
import kotlin.ByteArray
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ByteArraySerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement

@Serializable
public data class CreateImageEditRequest(
  public val image: Image,
  public val prompt: String,
  public val mask: ByteArray? = null,
  public val background: Background? = null,
  public val model: Model? = null,
  public val n: Long? = null,
  public val size: Size? = null,
  @SerialName("response_format")
  public val responseFormat: ResponseFormat? = null,
  @SerialName("output_format")
  public val outputFormat: OutputFormat? = null,
  @SerialName("output_compression")
  public val outputCompression: Long? = null,
  public val user: String? = null,
  @SerialName("input_fidelity")
  public val inputFidelity: InputFidelity? = null,
  public val stream: Boolean? = null,
  @SerialName("partial_images")
  public val partialImages: PartialImages? = null,
  public val quality: Quality? = null,
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
   * The image(s) to edit. Must be a supported image file or an array of images.
   *
   * For the GPT image models (`gpt-image-1`, `gpt-image-1-mini`, and `gpt-image-1.5`), each image should be a `png`, `webp`, or `jpg`
   * file less than 50MB. You can provide up to 16 images.
   * `chatgpt-image-latest` follows the same input constraints as GPT image models.
   *
   * For `dall-e-2`, you can only provide one image, and it should be a square
   * `png` file less than 4MB.
   *
   */
  @Serializable(with = Image.Serializer::class)
  public sealed interface Image {
    @Serializable
    @JvmInline
    public value class CaseBinary(
      public val `value`: ByteArray,
    ) : Image

    @Serializable
    @JvmInline
    public value class CaseBinaries(
      public val `value`: List<ByteArray>,
    ) : Image

    public object Serializer : KSerializer<Image> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.openai.model.CreateImageEditRequest.Image", PolymorphicKind.SEALED) {
        element("CaseBinary", ByteArraySerializer().descriptor)
        element("CaseBinaries", ListSerializer(ByteArraySerializer()).descriptor)
      }

      override fun deserialize(decoder: Decoder): Image {
        val value = decoder.decodeSerializableValue(JsonElement.serializer())
        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
        return json.attemptDeserialize(
          value,
          CaseBinaries::class to { CaseBinaries(decodeFromJsonElement(ListSerializer(ByteArraySerializer()), it)) },
          CaseBinary::class to { CaseBinary(decodeFromJsonElement(ByteArraySerializer(), it)) },
        )
      }

      override fun serialize(encoder: Encoder, `value`: Image) {
        when(value) {
          is CaseBinary -> encoder.encodeSerializableValue(ByteArraySerializer(), value.value)
          is CaseBinaries -> encoder.encodeSerializableValue(ListSerializer(ByteArraySerializer()), value.value)
        }
      }
    }
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
    public enum class GptImage15OrDallE2OrGptImage1OrGptImage1MiniOrChatgptImageLatest(
      override val `value`: String,
    ) : Model {
      @SerialName("gpt-image-1.5")
      GptImage15("gpt-image-1.5"),
      @SerialName("dall-e-2")
      DallE2("dall-e-2"),
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
          GptImage15OrDallE2OrGptImage1OrGptImage1MiniOrChatgptImageLatest.GptImage15 -> encoder.encodeString("gpt-image-1.5")
          GptImage15OrDallE2OrGptImage1OrGptImage1MiniOrChatgptImageLatest.DallE2 -> encoder.encodeString("dall-e-2")
          GptImage15OrDallE2OrGptImage1OrGptImage1MiniOrChatgptImageLatest.GptImage1 -> encoder.encodeString("gpt-image-1")
          GptImage15OrDallE2OrGptImage1OrGptImage1MiniOrChatgptImageLatest.GptImage1Mini -> encoder.encodeString("gpt-image-1-mini")
          GptImage15OrDallE2OrGptImage1OrGptImage1MiniOrChatgptImageLatest.ChatgptImageLatest -> encoder.encodeString("chatgpt-image-latest")
          is CaseString -> encoder.encodeString(value.value)
        }
      }

      override fun deserialize(decoder: Decoder): Model = when(val value = decoder.decodeString()) {
        "gpt-image-1.5" -> GptImage15OrDallE2OrGptImage1OrGptImage1MiniOrChatgptImageLatest.GptImage15
        "dall-e-2" -> GptImage15OrDallE2OrGptImage1OrGptImage1MiniOrChatgptImageLatest.DallE2
        "gpt-image-1" -> GptImage15OrDallE2OrGptImage1OrGptImage1MiniOrChatgptImageLatest.GptImage1
        "gpt-image-1-mini" -> GptImage15OrDallE2OrGptImage1OrGptImage1MiniOrChatgptImageLatest.GptImage1Mini
        "chatgpt-image-latest" -> GptImage15OrDallE2OrGptImage1OrGptImage1MiniOrChatgptImageLatest.ChatgptImageLatest
        else -> CaseString(value)
      }
    }
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
    @SerialName("standard")
    Standard("standard"),
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
  public enum class ResponseFormat(
    public val `value`: String,
  ) {
    @SerialName("url")
    Url("url"),
    @SerialName("b64_json")
    B64Json("b64_json"),
    ;
  }

  @Serializable
  public enum class Size(
    public val `value`: String,
  ) {
    `256x256`("256x256"),
    `512x512`("512x512"),
    `1024x1024`("1024x1024"),
    `1536x1024`("1536x1024"),
    `1024x1536`("1024x1536"),
    @SerialName("auto")
    Auto("auto"),
    ;
  }
}
