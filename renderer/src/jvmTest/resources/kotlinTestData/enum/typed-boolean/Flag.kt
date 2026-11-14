package io.github.nomisrev.render.test.`enum`.typed.boolean

import kotlin.Boolean
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

@Serializable(with = Flag.Serializer::class)
public enum class Flag(
  public val `value`: Boolean,
) {
  True(true),
  False(false),
  ;

  internal object Serializer : KSerializer<Flag> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Flag", PrimitiveKind.BOOLEAN)

    override fun serialize(encoder: Encoder, `value`: Flag) {
      encoder.encodeBoolean(value.value)
    }

    override fun deserialize(decoder: Decoder): Flag {
      val value = if (decoder is JsonDecoder) {
        val element = decoder.decodeJsonElement()
        val primitive = (element as? JsonPrimitive)?.takeIf { !it.isString }
        primitive?.booleanOrNull ?: throw SerializationException("""Flag expects a JSON boolean, got $element""")
      } else {
        decoder.decodeBoolean()
      }
      val member = Flag.entries.firstOrNull { it.value == value }
      return member ?: throw SerializationException("""Flag has no member with value $value""")
    }
  }
}
