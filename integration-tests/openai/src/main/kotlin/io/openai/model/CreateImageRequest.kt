package io.openai.model

import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class CreateImageRequest(
  public val prompt: String,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val model: Optional<Model> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val n: Optional<Long> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val quality: Optional<Quality> = Optional.Absent,
  @SerialName("response_format")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val responseFormat: Optional<ResponseFormat> = Optional.Absent,
  @SerialName("output_format")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val outputFormat: Optional<OutputFormat> = Optional.Absent,
  @SerialName("output_compression")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val outputCompression: Optional<Long> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val stream: Optional<Boolean> = Optional.Absent,
  @SerialName("partial_images")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val partialImages: Optional<PartialImages> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val size: Optional<Size> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val moderation: Optional<Moderation> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val background: Optional<Background> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val style: Optional<Style> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val user: String? = null,
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

  @Serializable(with = Model.Serializer::class)
  public sealed interface Model {
    public val `value`: String

    @Serializable
    @JvmInline
    public value class CaseString(
      override val `value`: String,
    ) : Model

    @Serializable
    public enum class GptImage15OrDallE2OrDallE3OrGptImage1OrGptImage1Mini(
      override val `value`: String,
    ) : Model {
      @SerialName("gpt-image-1.5")
      GptImage15("gpt-image-1.5"),
      @SerialName("dall-e-2")
      DallE2("dall-e-2"),
      @SerialName("dall-e-3")
      DallE3("dall-e-3"),
      @SerialName("gpt-image-1")
      GptImage1("gpt-image-1"),
      @SerialName("gpt-image-1-mini")
      GptImage1Mini("gpt-image-1-mini"),
      ;
    }

    public object Serializer : KSerializer<Model> {
      override val descriptor: SerialDescriptor = String.serializer().descriptor

      override fun serialize(encoder: Encoder, `value`: Model) {
        when(value) {
          GptImage15OrDallE2OrDallE3OrGptImage1OrGptImage1Mini.GptImage15 -> encoder.encodeString("gpt-image-1.5")
          GptImage15OrDallE2OrDallE3OrGptImage1OrGptImage1Mini.DallE2 -> encoder.encodeString("dall-e-2")
          GptImage15OrDallE2OrDallE3OrGptImage1OrGptImage1Mini.DallE3 -> encoder.encodeString("dall-e-3")
          GptImage15OrDallE2OrDallE3OrGptImage1OrGptImage1Mini.GptImage1 -> encoder.encodeString("gpt-image-1")
          GptImage15OrDallE2OrDallE3OrGptImage1OrGptImage1Mini.GptImage1Mini -> encoder.encodeString("gpt-image-1-mini")
          is CaseString -> encoder.encodeString(value.value)
        }
      }

      override fun deserialize(decoder: Decoder): Model = when(val value = decoder.decodeString()) {
        "gpt-image-1.5" -> GptImage15OrDallE2OrDallE3OrGptImage1OrGptImage1Mini.GptImage15
        "dall-e-2" -> GptImage15OrDallE2OrDallE3OrGptImage1OrGptImage1Mini.DallE2
        "dall-e-3" -> GptImage15OrDallE2OrDallE3OrGptImage1OrGptImage1Mini.DallE3
        "gpt-image-1" -> GptImage15OrDallE2OrDallE3OrGptImage1OrGptImage1Mini.GptImage1
        "gpt-image-1-mini" -> GptImage15OrDallE2OrDallE3OrGptImage1OrGptImage1Mini.GptImage1Mini
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
    @SerialName("standard")
    Standard("standard"),
    @SerialName("hd")
    Hd("hd"),
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
    @SerialName("auto")
    Auto("auto"),
    `1024x1024`("1024x1024"),
    `1536x1024`("1536x1024"),
    `1024x1536`("1024x1536"),
    `256x256`("256x256"),
    `512x512`("512x512"),
    `1792x1024`("1792x1024"),
    `1024x1792`("1024x1792"),
    ;
  }

  @Serializable
  public enum class Style(
    public val `value`: String,
  ) {
    @SerialName("vivid")
    Vivid("vivid"),
    @SerialName("natural")
    Natural("natural"),
    ;
  }
}
