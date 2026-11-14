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

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class Meter(
  public val rate: Baudrate,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val fallback: Optional<Baudrate> = Optional.Absent,
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
        val value = decoder.decodeLong()
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
        val value = decoder.decodeBoolean()
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
        val value = decoder.decodeInt()
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
        val value = decoder.decodeDouble()
        val member = Scale.entries.firstOrNull { it.value == value }
        return member ?: throw SerializationException("""Scale has no member with value $value""")
      }
    }
  }
}
