package io.github.model

import kotlin.Boolean
import kotlin.Double
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
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

/**
 * User-defined metadata to store domain-specific information limited to 8 keys with scalar values.
 */
@JvmInline
@Serializable
public value class Metadata(
  public val values: List<AdditionalProperties?>? = null,
) {
  @Serializable(with = AdditionalProperties.Serializer::class)
  public sealed interface AdditionalProperties {
    @Serializable
    @JvmInline
    public value class CaseString(
      public val `value`: String,
    ) : AdditionalProperties

    @Serializable
    @JvmInline
    public value class CaseDouble(
      public val `value`: Double,
    ) : AdditionalProperties

    @Serializable
    @JvmInline
    public value class CaseBoolean(
      public val `value`: Boolean,
    ) : AdditionalProperties

    public object Serializer : KSerializer<AdditionalProperties> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.github.model.Metadata.AdditionalProperties", PolymorphicKind.SEALED) {
        element("CaseString", String.serializer().descriptor)
        element("CaseDouble", Double.serializer().descriptor)
        element("CaseBoolean", Boolean.serializer().descriptor)
      }

      override fun deserialize(decoder: Decoder): AdditionalProperties {
        val value = decoder.decodeSerializableValue(JsonElement.serializer())
        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
        return json.attemptDeserialize(
          value,
          CaseDouble::class to { CaseDouble(decodeFromJsonElement(Double.serializer(), it)) },
          CaseBoolean::class to { CaseBoolean(decodeFromJsonElement(Boolean.serializer(), it)) },
          CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
        )
      }

      override fun serialize(encoder: Encoder, `value`: AdditionalProperties) {
        when(value) {
          is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
          is CaseDouble -> encoder.encodeSerializableValue(Double.serializer(), value.value)
          is CaseBoolean -> encoder.encodeSerializableValue(Boolean.serializer(), value.value)
        }
      }
    }
  }
}
