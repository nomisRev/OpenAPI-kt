package io.github.nomisrev.render.test.`enum`.typed.in_.union

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.doubleOrNull

@Serializable(with = Speed.Serializer::class)
public sealed interface Speed {
  @Serializable(with = CaseLong.Serializer::class)
  public enum class CaseLong(
    public val `value`: Long,
  ) : Speed {
    `300`(300L),
    `600`(600L),
    ;

    internal object Serializer : KSerializer<CaseLong> {
      override val descriptor: SerialDescriptor =
          PrimitiveSerialDescriptor("CaseLong", PrimitiveKind.LONG)

      override fun serialize(encoder: Encoder, `value`: CaseLong) {
        encoder.encodeLong(value.value)
      }

      override fun deserialize(decoder: Decoder): CaseLong {
        val value = decoder.decodeLong()
        val member = CaseLong.entries.firstOrNull { it.value == value }
        return member ?: throw SerializationException("""CaseLong has no member with value $value""")
      }
    }
  }

  @Serializable
  @JvmInline
  public value class CaseString(
    public val `value`: String,
  ) : Speed

  public object Serializer : KSerializer<Speed> {
    @OptIn(
      InternalSerializationApi::class,
      ExperimentalSerializationApi::class,
    )
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("io.github.nomisrev.render.test.enum.typed.in_.union.Speed", PolymorphicKind.SEALED) {
      element("CaseLong", CaseLong.serializer().descriptor)
      element("CaseString", String.serializer().descriptor)
    }

    override fun deserialize(decoder: Decoder): Speed {
      val value = decoder.decodeSerializableValue(JsonElement.serializer())
      val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
      return when {
        value is JsonPrimitive && value.isString -> json.attemptDeserialize(
          value,
          CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
        )
        value is JsonPrimitive && !value.isString && value.doubleOrNull != null -> json.attemptDeserialize(
          value,
          CaseLong::class to { decodeFromJsonElement(CaseLong.serializer(), it) },
        )
        else -> throw UnionSerializationException(value, emptyMap())
      }
    }

    override fun serialize(encoder: Encoder, `value`: Speed) {
      when(value) {
        is CaseLong -> encoder.encodeSerializableValue(CaseLong.serializer(), value)
        is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
      }
    }
  }
}
