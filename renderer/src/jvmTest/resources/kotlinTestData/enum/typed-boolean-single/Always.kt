package io.github.nomisrev.render.test.`enum`.typed.boolean.single

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

@Serializable(with = Always.Serializer::class)
public enum class Always(
  public val `value`: Boolean,
) {
  True(true),
  ;

  internal object Serializer : KSerializer<Always> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Always", PrimitiveKind.BOOLEAN)

    override fun serialize(encoder: Encoder, `value`: Always) {
      encoder.encodeBoolean(value.value)
    }

    override fun deserialize(decoder: Decoder): Always {
      val value = if (decoder is JsonDecoder) {
        val element = decoder.decodeJsonElement()
        val primitive = (element as? JsonPrimitive)?.takeIf { !it.isString }
        primitive?.booleanOrNull ?: throw SerializationException("""Always expects a JSON boolean, got $element""")
      } else {
        decoder.decodeBoolean()
      }
      val member = Always.entries.firstOrNull { it.value == value }
      return member ?: throw SerializationException("""Always has no member with value $value""")
    }
  }
}
