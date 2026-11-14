package io.github.nomisrev.render.test.`enum`.typed.integer.no.format

import kotlin.Long
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = Retries.Serializer::class)
public enum class Retries(
  public val `value`: Long,
) {
  `1`(1L),
  `2`(2L),
  `3`(3L),
  ;

  internal object Serializer : KSerializer<Retries> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Retries", PrimitiveKind.LONG)

    override fun serialize(encoder: Encoder, `value`: Retries) {
      encoder.encodeLong(value.value)
    }

    override fun deserialize(decoder: Decoder): Retries {
      val value = decoder.decodeLong()
      val member = Retries.entries.firstOrNull { it.value == value }
      return member ?: throw SerializationException("""Retries has no member with value $value""")
    }
  }
}
