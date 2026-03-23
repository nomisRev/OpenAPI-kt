package io.github.model

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
 * Validation Error
 */
@Serializable
public data class ValidationError(
  public val message: String,
  @SerialName("documentation_url")
  public val documentationUrl: String,
  public val errors: List<Errors>? = null,
) {
  @Serializable
  public data class Errors(
    public val resource: String? = null,
    public val `field`: String? = null,
    public val message: String? = null,
    public val code: String,
    public val index: Long? = null,
    public val `value`: Value? = null,
  ) {
    @Serializable(with = Value.Serializer::class)
    public sealed interface Value {
      @Serializable
      @JvmInline
      public value class CaseString(
        public val `value`: String?,
      ) : Value

      @Serializable
      @JvmInline
      public value class CaseLong(
        public val `value`: Long?,
      ) : Value

      @Serializable
      @JvmInline
      public value class CaseStrings(
        public val `value`: List<String>?,
      ) : Value

      public object Serializer : KSerializer<Value> {
        @OptIn(
          InternalSerializationApi::class,
          ExperimentalSerializationApi::class,
        )
        override val descriptor: SerialDescriptor =
            buildSerialDescriptor("io.github.model.ValidationError.Errors.Value", PolymorphicKind.SEALED) {
          element("CaseString", String.serializer().nullable.descriptor)
          element("CaseLong", Long.serializer().nullable.descriptor)
          element("CaseStrings", ListSerializer(String.serializer()).nullable.descriptor)
        }

        override fun deserialize(decoder: Decoder): Value {
          val value = decoder.decodeSerializableValue(JsonElement.serializer())
          val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
          return json.attemptDeserialize(
            value,
            CaseStrings::class to { CaseStrings(decodeFromJsonElement(ListSerializer(String.serializer()).nullable, it)) },
            CaseLong::class to { CaseLong(decodeFromJsonElement(Long.serializer().nullable, it)) },
            CaseString::class to { CaseString(decodeFromJsonElement(String.serializer().nullable, it)) },
          )
        }

        override fun serialize(encoder: Encoder, `value`: Value) {
          when(value) {
            is CaseString -> encoder.encodeSerializableValue(String.serializer().nullable, value.value)
            is CaseLong -> encoder.encodeSerializableValue(Long.serializer().nullable, value.value)
            is CaseStrings -> encoder.encodeSerializableValue(ListSerializer(String.serializer()).nullable, value.value)
          }
        }
      }
    }
  }
}
