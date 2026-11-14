package io.github.nomisrev.render.test.`enum`.typed.wire

import kotlin.Boolean
import kotlin.Double
import kotlin.Int
import kotlin.Long
import kotlin.OptIn
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.longOrNull

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class Meter(
  public val rate: Baudrate,
  public val fallback: Baudrate? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val rates: List<Baudrate>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val priority: Priority? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val epoch: Epoch? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val scale: Scale? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val on: On? = null,
) {
  @Serializable(with = Epoch.Serializer::class)
  public enum class Epoch(
    public val `value`: Long,
  ) {
    `1`(1L),
    `5000000000`(5_000_000_000L),
    ;

    internal object Serializer : KSerializer<Epoch> {
      override val descriptor: SerialDescriptor =
          PrimitiveSerialDescriptor("Epoch", PrimitiveKind.LONG)

      override fun serialize(encoder: Encoder, `value`: Epoch) {
        encoder.encodeLong(value.value)
      }

      override fun deserialize(decoder: Decoder): Epoch {
        val value = if (decoder is JsonDecoder) {
          val element = decoder.decodeJsonElement()
          val primitive = (element as? JsonPrimitive)?.takeIf { !it.isString }
          primitive?.longOrNull ?: throw SerializationException("""Epoch expects a JSON integer, got $element""")
        } else {
          decoder.decodeLong()
        }
        val member = Epoch.entries.firstOrNull { it.value == value }
        return member ?: throw SerializationException("""Epoch has no member with value $value""")
      }
    }
  }

  @Serializable(with = On.Serializer::class)
  public enum class On(
    public val `value`: Boolean,
  ) {
    True(true),
    False(false),
    ;

    internal object Serializer : KSerializer<On> {
      override val descriptor: SerialDescriptor =
          PrimitiveSerialDescriptor("On", PrimitiveKind.BOOLEAN)

      override fun serialize(encoder: Encoder, `value`: On) {
        encoder.encodeBoolean(value.value)
      }

      override fun deserialize(decoder: Decoder): On {
        val value = if (decoder is JsonDecoder) {
          val element = decoder.decodeJsonElement()
          val primitive = (element as? JsonPrimitive)?.takeIf { !it.isString }
          primitive?.booleanOrNull ?: throw SerializationException("""On expects a JSON boolean, got $element""")
        } else {
          decoder.decodeBoolean()
        }
        val member = On.entries.firstOrNull { it.value == value }
        return member ?: throw SerializationException("""On has no member with value $value""")
      }
    }
  }

  @Serializable(with = Priority.Serializer::class)
  public enum class Priority(
    public val `value`: Int,
  ) {
    `1`(1),
    `2`(2),
    ;

    internal object Serializer : KSerializer<Priority> {
      override val descriptor: SerialDescriptor =
          PrimitiveSerialDescriptor("Priority", PrimitiveKind.INT)

      override fun serialize(encoder: Encoder, `value`: Priority) {
        encoder.encodeInt(value.value)
      }

      override fun deserialize(decoder: Decoder): Priority {
        val value = if (decoder is JsonDecoder) {
          val element = decoder.decodeJsonElement()
          val primitive = (element as? JsonPrimitive)?.takeIf { !it.isString }
          primitive?.intOrNull ?: throw SerializationException("""Priority expects a JSON integer, got $element""")
        } else {
          decoder.decodeInt()
        }
        val member = Priority.entries.firstOrNull { it.value == value }
        return member ?: throw SerializationException("""Priority has no member with value $value""")
      }
    }
  }

  @Serializable(with = Scale.Serializer::class)
  public enum class Scale(
    public val `value`: Double,
  ) {
    `05`(0.5),
    `15`(1.5),
    ;

    internal object Serializer : KSerializer<Scale> {
      override val descriptor: SerialDescriptor =
          PrimitiveSerialDescriptor("Scale", PrimitiveKind.DOUBLE)

      override fun serialize(encoder: Encoder, `value`: Scale) {
        encoder.encodeDouble(value.value)
      }

      override fun deserialize(decoder: Decoder): Scale {
        val value = if (decoder is JsonDecoder) {
          val element = decoder.decodeJsonElement()
          val primitive = (element as? JsonPrimitive)?.takeIf { !it.isString }
          primitive?.doubleOrNull ?: throw SerializationException("""Scale expects a JSON number, got $element""")
        } else {
          decoder.decodeDouble()
        }
        val member = Scale.entries.firstOrNull { it.value == value }
        return member ?: throw SerializationException("""Scale has no member with value $value""")
      }
    }
  }
}
