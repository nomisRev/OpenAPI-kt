package io.github.nomisrev.render.test.object_.type.array.nullable

import kotlin.Boolean
import kotlin.Double
import kotlin.Int
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlin.time.Instant
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.doubleOrNull

@Serializable
public data class TypeArrayNullable(
  public val requiredString: String?,
  public val optionalString: String? = null,
  public val nullFirst: String?,
  public val anyOfRequiredString: String?,
  public val anyOfOptionalString: String? = null,
  public val int32: Int?,
  public val int64: Long?,
  public val number: Double?,
  public val flag: Boolean?,
  public val createdAt: Instant?,
  public val status: Status?,
  public val owner: Owner?,
  public val tags: List<String>?,
  public val alias: NullableAlias?,
  public val anyOfAlias: AnyOfNullableAlias?,
  public val single: String,
  public val stringOrInt: StringOrInt,
  public val stringOrIntOrNull: StringOrIntOrNull?,
) {
  @Serializable
  public data class Owner(
    public val name: String,
  )

  @Serializable
  public enum class Status(
    public val `value`: String,
  ) {
    @SerialName("active")
    Active("active"),
    @SerialName("inactive")
    Inactive("inactive"),
    @SerialName("null")
    Null("null"),
    ;
  }

  @Serializable(with = StringOrInt.Serializer::class)
  public sealed interface StringOrInt {
    @Serializable
    @JvmInline
    public value class CaseString(
      public val `value`: String,
    ) : StringOrInt

    @Serializable
    @JvmInline
    public value class CaseLong(
      public val `value`: Long,
    ) : StringOrInt

    public object Serializer : KSerializer<StringOrInt> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.github.nomisrev.render.test.object_.type.array.nullable.TypeArrayNullable.StringOrInt", PolymorphicKind.SEALED) {
        element("CaseString", String.serializer().descriptor)
        element("CaseLong", Long.serializer().descriptor)
      }

      override fun deserialize(decoder: Decoder): StringOrInt {
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
          else -> throw UnionSerializationException(value, emptyMap())
        }
      }

      override fun serialize(encoder: Encoder, `value`: StringOrInt) {
        when(value) {
          is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
          is CaseLong -> encoder.encodeSerializableValue(Long.serializer(), value.value)
        }
      }
    }
  }

  @Serializable(with = StringOrIntOrNull.Serializer::class)
  public sealed interface StringOrIntOrNull {
    @Serializable
    @JvmInline
    public value class CaseString(
      public val `value`: String,
    ) : StringOrIntOrNull

    @Serializable
    @JvmInline
    public value class CaseLong(
      public val `value`: Long,
    ) : StringOrIntOrNull

    public object Serializer : KSerializer<StringOrIntOrNull> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.github.nomisrev.render.test.object_.type.array.nullable.TypeArrayNullable.StringOrIntOrNull", PolymorphicKind.SEALED) {
        element("CaseString", String.serializer().descriptor)
        element("CaseLong", Long.serializer().descriptor)
      }

      override fun deserialize(decoder: Decoder): StringOrIntOrNull {
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
          else -> throw UnionSerializationException(value, emptyMap())
        }
      }

      override fun serialize(encoder: Encoder, `value`: StringOrIntOrNull) {
        when(value) {
          is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
          is CaseLong -> encoder.encodeSerializableValue(Long.serializer(), value.value)
        }
      }
    }
  }
}
