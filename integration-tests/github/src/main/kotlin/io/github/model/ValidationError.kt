package io.github.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.doubleOrNull

/**
 * Validation Error
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class ValidationError(
  public val message: String,
  @SerialName("documentation_url")
  public val documentationUrl: String,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val errors: List<Errors>? = null,
) {
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Errors(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val resource: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `field`: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val message: String? = null,
    public val code: String,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val index: Long? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `value`: Optional<Value> = Optional.Absent,
  ) {
    @Serializable(with = Value.Serializer::class)
    public sealed interface Value {
      @Serializable
      @JvmInline
      public value class CaseString(
        public val `value`: String,
      ) : Value

      @Serializable
      @JvmInline
      public value class CaseLong(
        public val `value`: Long,
      ) : Value

      @Serializable
      @JvmInline
      public value class CaseStrings(
        public val `value`: List<String>,
      ) : Value

      public object Serializer : KSerializer<Value> {
        @OptIn(
          InternalSerializationApi::class,
          ExperimentalSerializationApi::class,
        )
        override val descriptor: SerialDescriptor =
            buildSerialDescriptor("io.github.model.ValidationError.Errors.Value", PolymorphicKind.SEALED) {
          element("CaseString", String.serializer().descriptor)
          element("CaseLong", Long.serializer().descriptor)
          element("CaseStrings", ListSerializer(String.serializer()).descriptor)
        }

        override fun deserialize(decoder: Decoder): Value {
          val value = decoder.decodeSerializableValue(JsonElement.serializer())
          val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
          return when {
            value is JsonPrimitive && value.isString -> json.attemptDeserialize(
              value,
              CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
            )
            value is JsonPrimitive && !value.isString && value.doubleOrNull != null -> json.attemptDeserialize(
              value,
              CaseLong::class to { CaseLong(decodeFromJsonElement(Long.serializer(), it)) },
            )
            value is JsonArray -> json.attemptDeserialize(
              value,
              CaseStrings::class to { CaseStrings(decodeFromJsonElement(ListSerializer(String.serializer()), it)) },
            )
            else -> throw UnionSerializationException(value, emptyMap())
          }
        }

        override fun serialize(encoder: Encoder, `value`: Value) {
          when(value) {
            is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
            is CaseLong -> encoder.encodeSerializableValue(Long.serializer(), value.value)
            is CaseStrings -> encoder.encodeSerializableValue(ListSerializer(String.serializer()), value.value)
          }
        }
      }
    }
  }
}
