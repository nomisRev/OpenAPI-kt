package io.openai.model

import kotlin.ByteArray
import kotlin.Double
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
public data class CreateTranslationRequest(
  public val `file`: ByteArray,
  public val model: Model,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val prompt: String? = null,
  @SerialName("response_format")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val responseFormat: ResponseFormat? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val temperature: Double? = null,
) {
  @Serializable(with = Model.Serializer::class)
  public sealed interface Model {
    public val `value`: String

    @Serializable
    @JvmInline
    public value class CaseString(
      override val `value`: String,
    ) : Model

    @Serializable
    public enum class Whisper1(
      override val `value`: String,
    ) : Model {
      @SerialName("whisper-1")
      Whisper1("whisper-1"),
      ;
    }

    public object Serializer : KSerializer<Model> {
      override val descriptor: SerialDescriptor = String.serializer().descriptor

      override fun serialize(encoder: Encoder, `value`: Model) {
        when(value) {
          Whisper1.Whisper1 -> encoder.encodeString("whisper-1")
          is CaseString -> encoder.encodeString(value.value)
        }
      }

      override fun deserialize(decoder: Decoder): Model = when(val value = decoder.decodeString()) {
        "whisper-1" -> Whisper1.Whisper1
        else -> CaseString(value)
      }
    }
  }

  @Serializable
  public enum class ResponseFormat(
    public val `value`: String,
  ) {
    @SerialName("json")
    Json("json"),
    @SerialName("text")
    Text("text"),
    @SerialName("srt")
    Srt("srt"),
    @SerialName("verbose_json")
    VerboseJson("verbose_json"),
    @SerialName("vtt")
    Vtt("vtt"),
    ;
  }
}
