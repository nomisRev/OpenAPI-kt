package io.github.nomisrev.render.test.client.`enum`.typed.parameters

import kotlin.Long
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
import kotlinx.serialization.json.longOrNull

@Serializable(with = Baudrate.Serializer::class)
public enum class Baudrate(
  public val `value`: Long,
) {
  `300`(300L),
  `600`(600L),
  `1200`(1_200L),
  ;

  internal object Serializer : KSerializer<Baudrate> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Baudrate", PrimitiveKind.LONG)

    override fun serialize(encoder: Encoder, `value`: Baudrate) {
      encoder.encodeLong(value.value)
    }

    override fun deserialize(decoder: Decoder): Baudrate {
      val value = if (decoder is JsonDecoder) {
        val element = decoder.decodeJsonElement()
        val primitive = (element as? JsonPrimitive)?.takeIf { !it.isString }
        primitive?.longOrNull ?: throw SerializationException("""Baudrate expects a JSON integer, got $element""")
      } else {
        decoder.decodeLong()
      }
      val member = Baudrate.entries.firstOrNull { it.value == value }
      return member ?: throw SerializationException("""Baudrate has no member with value $value""")
    }
  }
}
