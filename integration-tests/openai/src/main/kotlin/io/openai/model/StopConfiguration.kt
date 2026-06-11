package io.openai.model

import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement

/**
 * Not supported with latest reasoning models `o3` and `o4-mini`.
 *
 * Up to 4 sequences where the API will stop generating further tokens. The
 * returned text will not contain the stop sequence.
 *
 */
@Serializable(with = StopConfiguration.Serializer::class)
public sealed interface StopConfiguration {
  @Serializable
  @JvmInline
  public value class CaseString(
    public val `value`: String?,
  ) : StopConfiguration

  @Serializable
  @JvmInline
  public value class CaseStrings(
    public val `value`: List<String>,
  ) : StopConfiguration

  public object Serializer : KSerializer<StopConfiguration> {
    @OptIn(
      InternalSerializationApi::class,
      ExperimentalSerializationApi::class,
    )
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("io.openai.model.StopConfiguration", PolymorphicKind.SEALED) {
      element("CaseString", String.serializer().nullable.descriptor)
      element("CaseStrings", ListSerializer(String.serializer()).descriptor)
    }

    override fun deserialize(decoder: Decoder): StopConfiguration {
      val value = decoder.decodeSerializableValue(JsonElement.serializer())
      val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
      return json.attemptDeserialize(
        value,
        CaseStrings::class to { CaseStrings(decodeFromJsonElement(ListSerializer(String.serializer()), it)) },
        CaseString::class to { CaseString(decodeFromJsonElement(String.serializer().nullable, it)) },
      )
    }

    override fun serialize(encoder: Encoder, `value`: StopConfiguration) {
      when(value) {
        is CaseString -> encoder.encodeSerializableValue(String.serializer().nullable, value.value)
        is CaseStrings -> encoder.encodeSerializableValue(ListSerializer(String.serializer()), value.value)
      }
    }
  }
}
