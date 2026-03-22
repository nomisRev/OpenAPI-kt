package io.github.nomisrev.render.test.union.all.primitives

import kotlin.Boolean
import kotlin.ByteArray
import kotlin.Double
import kotlin.Float
import kotlin.Int
import kotlin.OptIn
import kotlin.String
import kotlin.jvm.JvmInline
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlinx.datetime.LocalDate
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ByteArraySerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement

@Serializable(with = Union.Serializer::class)
@OptIn(
  ExperimentalUuidApi::class,
  ExperimentalTime::class,
)
public sealed interface Union {
  @Serializable
  @JvmInline
  public value class CaseString(
    public val `value`: String,
  ) : Union

  @Serializable
  @JvmInline
  public value class CaseInt(
    public val `value`: Int,
  ) : Union

  @Serializable
  @JvmInline
  public value class CaseFloat(
    public val `value`: Float,
  ) : Union

  @Serializable
  @JvmInline
  public value class CaseDouble(
    public val `value`: Double,
  ) : Union

  @Serializable
  @JvmInline
  public value class CaseBoolean(
    public val `value`: Boolean,
  ) : Union

  @Serializable
  @JvmInline
  public value class CaseDate(
    public val `value`: LocalDate,
  ) : Union

  @Serializable
  @JvmInline
  public value class CaseDateTime(
    public val `value`: Instant,
  ) : Union

  @Serializable
  @JvmInline
  public value class CaseBinary(
    public val `value`: ByteArray,
  ) : Union

  @Serializable
  @JvmInline
  public value class CaseUuid(
    public val `value`: Uuid,
  ) : Union

  public object Serializer : KSerializer<Union> {
    @OptIn(
      InternalSerializationApi::class,
      ExperimentalSerializationApi::class,
    )
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("io.github.nomisrev.render.test.union.all.primitives.Union", PolymorphicKind.SEALED) {
      element("CaseString", String.serializer().descriptor)
      element("CaseInt", Int.serializer().descriptor)
      element("CaseFloat", Float.serializer().descriptor)
      element("CaseDouble", Double.serializer().descriptor)
      element("CaseBoolean", Boolean.serializer().descriptor)
      element("CaseDate", LocalDate.serializer().descriptor)
      element("CaseDateTime", Instant.serializer().descriptor)
      element("CaseBinary", ByteArraySerializer().descriptor)
      element("CaseUuid", Uuid.serializer().descriptor)
    }

    override fun deserialize(decoder: Decoder): Union {
      val value = decoder.decodeSerializableValue(JsonElement.serializer())
      val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
      return json.attemptDeserialize(
        value,
        CaseInt::class to { CaseInt(decodeFromJsonElement(Int.serializer(), it)) },
        CaseFloat::class to { CaseFloat(decodeFromJsonElement(Float.serializer(), it)) },
        CaseDouble::class to { CaseDouble(decodeFromJsonElement(Double.serializer(), it)) },
        CaseBoolean::class to { CaseBoolean(decodeFromJsonElement(Boolean.serializer(), it)) },
        CaseUuid::class to { CaseUuid(decodeFromJsonElement(Uuid.serializer(), it)) },
        CaseDate::class to { CaseDate(decodeFromJsonElement(LocalDate.serializer(), it)) },
        CaseDateTime::class to { CaseDateTime(decodeFromJsonElement(Instant.serializer(), it)) },
        CaseBinary::class to { CaseBinary(decodeFromJsonElement(ByteArraySerializer(), it)) },
        CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
      )
    }

    override fun serialize(encoder: Encoder, `value`: Union) {
      when(value) {
        is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
        is CaseInt -> encoder.encodeSerializableValue(Int.serializer(), value.value)
        is CaseFloat -> encoder.encodeSerializableValue(Float.serializer(), value.value)
        is CaseDouble -> encoder.encodeSerializableValue(Double.serializer(), value.value)
        is CaseBoolean -> encoder.encodeSerializableValue(Boolean.serializer(), value.value)
        is CaseDate -> encoder.encodeSerializableValue(LocalDate.serializer(), value.value)
        is CaseDateTime -> encoder.encodeSerializableValue(Instant.serializer(), value.value)
        is CaseBinary -> encoder.encodeSerializableValue(ByteArraySerializer(), value.value)
        is CaseUuid -> encoder.encodeSerializableValue(Uuid.serializer(), value.value)
      }
    }
  }
}
