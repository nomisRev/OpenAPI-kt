package io.github.nomisrev.render.test.union.overlapping.objects

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement

@Serializable(with = Union.Serializer::class)
public sealed interface Union {
  @Serializable
  public data class AAndB(
    public val a: String,
    public val b: String,
  ) : Union

  @Serializable
  public data class AAndBAndC(
    public val a: String,
    public val b: String,
    public val c: String,
  ) : Union

  public object Serializer : KSerializer<Union> {
    @OptIn(
      InternalSerializationApi::class,
      ExperimentalSerializationApi::class,
    )
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("io.github.nomisrev.render.test.union.overlapping.objects.Union", PolymorphicKind.SEALED) {
      element("AAndB", AAndB.serializer().descriptor)
      element("AAndBAndC", AAndBAndC.serializer().descriptor)
    }

    override fun deserialize(decoder: Decoder): Union {
      val value = decoder.decodeSerializableValue(JsonElement.serializer())
      val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
      return json.attemptDeserialize(
        value,
        AAndBAndC::class to { decodeFromJsonElement(AAndBAndC.serializer(), it) },
        AAndB::class to { decodeFromJsonElement(AAndB.serializer(), it) },
      )
    }

    override fun serialize(encoder: Encoder, `value`: Union) {
      when(value) {
        is AAndB -> encoder.encodeSerializableValue(AAndB.serializer(), value)
        is AAndBAndC -> encoder.encodeSerializableValue(AAndBAndC.serializer(), value)
      }
    }
  }
}
