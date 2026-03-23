package io.github.model

import kotlin.Double
import kotlin.OptIn
import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement

@Serializable(with = WebhookConfigInsecureSsl.Serializer::class)
public sealed interface WebhookConfigInsecureSsl {
  @Serializable
  @JvmInline
  public value class CaseString(
    public val `value`: String,
  ) : WebhookConfigInsecureSsl

  @Serializable
  @JvmInline
  public value class CaseDouble(
    public val `value`: Double,
  ) : WebhookConfigInsecureSsl

  public object Serializer : KSerializer<WebhookConfigInsecureSsl> {
    @OptIn(
      InternalSerializationApi::class,
      ExperimentalSerializationApi::class,
    )
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("io.github.model.WebhookConfigInsecureSsl", PolymorphicKind.SEALED) {
      element("CaseString", String.serializer().descriptor)
      element("CaseDouble", Double.serializer().descriptor)
    }

    override fun deserialize(decoder: Decoder): WebhookConfigInsecureSsl {
      val value = decoder.decodeSerializableValue(JsonElement.serializer())
      val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
      return json.attemptDeserialize(
        value,
        CaseDouble::class to { CaseDouble(decodeFromJsonElement(Double.serializer(), it)) },
        CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
      )
    }

    override fun serialize(encoder: Encoder, `value`: WebhookConfigInsecureSsl) {
      when(value) {
        is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
        is CaseDouble -> encoder.encodeSerializableValue(Double.serializer(), value.value)
      }
    }
  }
}
