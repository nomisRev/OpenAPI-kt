package io.openai.model

import kotlin.ByteArray
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
public data class CreateImageVariationRequest(
  public val image: ByteArray,
  public val model: Model? = null,
  public val n: Long? = null,
  @SerialName("response_format")
  public val responseFormat: ResponseFormat? = null,
  public val size: Size? = null,
  public val user: String? = null,
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
    public enum class DallE2(
      override val `value`: String,
    ) : Model {
      @SerialName("dall-e-2")
      DallE2("dall-e-2"),
      ;
    }

    public object Serializer : KSerializer<Model> {
      override val descriptor: SerialDescriptor = String.serializer().descriptor

      override fun serialize(encoder: Encoder, `value`: Model) {
        when(value) {
          DallE2.DallE2 -> encoder.encodeString("dall-e-2")
          is CaseString -> encoder.encodeString(value.value)
        }
      }

      override fun deserialize(decoder: Decoder): Model = when(val value = decoder.decodeString()) {
        "dall-e-2" -> DallE2.DallE2
        else -> CaseString(value)
      }
    }
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
  public enum class Size {
    `256x256`,
    `512x512`,
    `1024x1024`,
  }
}
