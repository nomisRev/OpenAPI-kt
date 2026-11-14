package io.github.nomisrev.render.test.`enum`.typed.long

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
