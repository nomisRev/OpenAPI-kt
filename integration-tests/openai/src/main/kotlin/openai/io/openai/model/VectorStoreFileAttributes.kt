package io.openai.model

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
 * Set of 16 key-value pairs that can be attached to an object. This can be
 * useful for storing additional information about the object in a structured
 * format, and querying for objects via API or the dashboard. Keys are strings
 * with a maximum length of 64 characters. Values are strings with a maximum
 * length of 512 characters, booleans, or numbers.
 *
 */
@JvmInline
@Serializable
public value class VectorStoreFileAttributes(
  public val values: List<AdditionalProperties>? = null,
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
          buildSerialDescriptor("io.openai.model.VectorStoreFileAttributes.AdditionalProperties", PolymorphicKind.SEALED) {
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
