package io.github.nomisrev.render.test.`enum`.typed.double

import kotlin.Double
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
import kotlinx.serialization.json.doubleOrNull

@Serializable(with = Scale.Serializer::class)
public enum class Scale(
  public val `value`: Double,
) {
  `05`(0.5),
  `10`(1.0),
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
