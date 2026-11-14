package io.github.nomisrev.render.test.`enum`.int

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

@Serializable(with = Priority.Serializer::class)
public enum class Priority(
  public val `value`: Long,
) {
  `1`(1L),
  `2`(2L),
  `3`(3L),
  ;

  internal object Serializer : KSerializer<Priority> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Priority", PrimitiveKind.LONG)

    override fun serialize(encoder: Encoder, `value`: Priority) {
      encoder.encodeLong(value.value)
    }

    override fun deserialize(decoder: Decoder): Priority {
      val value = if (decoder is JsonDecoder) {
        val element = decoder.decodeJsonElement()
        val primitive = (element as? JsonPrimitive)?.takeIf { !it.isString }
        primitive?.longOrNull ?: throw SerializationException("""Priority expects a JSON integer, got $element""")
      } else {
        decoder.decodeLong()
      }
      val member = Priority.entries.firstOrNull { it.value == value }
      return member ?: throw SerializationException("""Priority has no member with value $value""")
    }
  }
}
