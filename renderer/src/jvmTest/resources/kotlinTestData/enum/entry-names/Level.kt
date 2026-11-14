package io.github.nomisrev.render.test.`enum`.entry.names

import kotlin.Double
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = Level.Serializer::class)
public enum class Level(
  public val `value`: Double,
) {
  `0Dot5`(0.5),
  Minus0Dot5(-0.5),
  ;

  internal object Serializer : KSerializer<Level> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Level", PrimitiveKind.DOUBLE)

    override fun serialize(encoder: Encoder, `value`: Level) {
      encoder.encodeDouble(value.value)
    }

    override fun deserialize(decoder: Decoder): Level {
      val value = decoder.decodeDouble()
      val member = Level.entries.firstOrNull { it.value == value }
      return member ?: throw SerializationException("""Level has no member with value $value""")
    }
  }
}
