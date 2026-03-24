package io.openai.model

import kotlin.Boolean
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

@Serializable
public data class CreateImageRequest(
  public val prompt: String,
  public val model: Model? = null,
  public val n: Long? = null,
  public val quality: Quality? = null,
  @SerialName("response_format")
  public val responseFormat: ResponseFormat? = null,
  @SerialName("output_format")
  public val outputFormat: OutputFormat? = null,
  @SerialName("output_compression")
  public val outputCompression: Long? = null,
  public val stream: Boolean? = null,
  @SerialName("partial_images")
  public val partialImages: PartialImages? = null,
  public val size: Size? = null,
  public val moderation: Moderation? = null,
  public val background: Background? = null,
  public val style: Style? = null,
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
