package io.github.nomisrev.render.test.`enum`.typed.int

import kotlin.Int
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = Baudrate.Serializer::class)
public enum class Baudrate(
  public val `value`: Int,
) {
  `300`(300),
  `600`(600),
  `1200`(1_200),
  ;

  internal object Serializer : KSerializer<Baudrate> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Baudrate", PrimitiveKind.INT)

    override fun serialize(encoder: Encoder, `value`: Baudrate) {
      encoder.encodeInt(value.value)
    }

    override fun deserialize(decoder: Decoder): Baudrate {
      val value = decoder.decodeInt()
      val member = Baudrate.entries.firstOrNull { it.value == value }
      return member ?: throw SerializationException("""Baudrate has no member with value $value""")
    }
  }
}
