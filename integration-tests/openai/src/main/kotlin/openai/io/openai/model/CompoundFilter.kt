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
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement

/**
 * Combine multiple filters using `and` or `or`.
 */
@Serializable
public data class CompoundFilter(
  public val type: Type,
  public val filters: List<Filters>,
) {
  @OptIn(ExperimentalSerializationApi::class)
  @JsonClassDiscriminator("type")
  @Serializable
  public sealed interface Filters {
    /**
     * A filter used to compare a specified attribute key to a given value using a defined comparison operation.
     *
     */
    @SerialName("ComparisonFilter")
    @Serializable
    public data class ComparisonFilter(
      @Required
      public val type: Type = Type.Eq,
      public val key: String,
      public val `value`: Value,
    ) : Filters {
      @Serializable
      public enum class Type(
        public val `value`: String,
      ) {
        @SerialName("eq")
        Eq("eq"),
        @SerialName("ne")
        Ne("ne"),
        @SerialName("gt")
        Gt("gt"),
        @SerialName("gte")
        Gte("gte"),
        @SerialName("lt")
        Lt("lt"),
        @SerialName("lte")
        Lte("lte"),
        ;
      }

      /**
       * The value to compare against the attribute key; supports string, number, or boolean types.
       */
      @Serializable(with = Value.Serializer::class)
      public sealed interface Value {
        @Serializable
        @JvmInline
        public value class CaseString(
          public val `value`: String,
        ) : Value

        @Serializable
        @JvmInline
        public value class CaseDouble(
          public val `value`: Double,
        ) : Value

        @Serializable
        @JvmInline
        public value class CaseBoolean(
          public val `value`: Boolean,
        ) : Value

        @Serializable
        @JvmInline
        public value class CaseStringOrDoubleList(
          public val `value`: List<StringOrDouble>,
        ) : Value

        @Serializable(with = StringOrDouble.Serializer::class)
        public sealed interface StringOrDouble {
          @Serializable
          @JvmInline
          public value class CaseString(
            public val `value`: String,
          ) : StringOrDouble

          @Serializable
          @JvmInline
          public value class CaseDouble(
            public val `value`: Double,
          ) : StringOrDouble

          public object Serializer : KSerializer<StringOrDouble> {
            @OptIn(
              InternalSerializationApi::class,
              ExperimentalSerializationApi::class,
            )
            override val descriptor: SerialDescriptor =
                buildSerialDescriptor("io.openai.model.CompoundFilter.Filters.ComparisonFilter.Value.StringOrDouble", PolymorphicKind.SEALED) {
              element("CaseString", String.serializer().descriptor)
              element("CaseDouble", Double.serializer().descriptor)
            }

            override fun deserialize(decoder: Decoder): StringOrDouble {
              val value = decoder.decodeSerializableValue(JsonElement.serializer())
              val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
              return json.attemptDeserialize(
                value,
                CaseDouble::class to { CaseDouble(decodeFromJsonElement(Double.serializer(), it)) },
                CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
              )
            }

            override fun serialize(encoder: Encoder, `value`: StringOrDouble) {
              when(value) {
                is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                is CaseDouble -> encoder.encodeSerializableValue(Double.serializer(), value.value)
              }
            }
          }
        }

        public object Serializer : KSerializer<Value> {
          @OptIn(
            InternalSerializationApi::class,
            ExperimentalSerializationApi::class,
          )
          override val descriptor: SerialDescriptor =
              buildSerialDescriptor("io.openai.model.CompoundFilter.Filters.ComparisonFilter.Value", PolymorphicKind.SEALED) {
            element("CaseString", String.serializer().descriptor)
            element("CaseDouble", Double.serializer().descriptor)
            element("CaseBoolean", Boolean.serializer().descriptor)
            element("CaseStringOrDoubleList", ListSerializer(StringOrDouble.serializer()).descriptor)
          }

          override fun deserialize(decoder: Decoder): Value {
            val value = decoder.decodeSerializableValue(JsonElement.serializer())
            val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
            return json.attemptDeserialize(
              value,
              CaseStringOrDoubleList::class to { CaseStringOrDoubleList(decodeFromJsonElement(ListSerializer(StringOrDouble.serializer()), it)) },
              CaseDouble::class to { CaseDouble(decodeFromJsonElement(Double.serializer(), it)) },
              CaseBoolean::class to { CaseBoolean(decodeFromJsonElement(Boolean.serializer(), it)) },
              CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
            )
          }

          override fun serialize(encoder: Encoder, `value`: Value) {
            when(value) {
              is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
              is CaseDouble -> encoder.encodeSerializableValue(Double.serializer(), value.value)
              is CaseBoolean -> encoder.encodeSerializableValue(Boolean.serializer(), value.value)
              is CaseStringOrDoubleList -> encoder.encodeSerializableValue(ListSerializer(StringOrDouble.serializer()), value.value)
            }
          }
        }
      }
    }

    @Serializable
    @JvmInline
    @SerialName("#")
    public value class `#`(
      public val `value`: Filters.Unnamed,
    ) : Filters
  }

  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("and")
    And("and"),
    @SerialName("or")
    Or("or"),
    ;
  }
}
