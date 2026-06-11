package io.github.nomisrev.render.test.union.inlined.object_.and.primitives

import kotlin.Boolean
import kotlin.Int
import kotlin.OptIn
import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement

@Serializable(with = Union.Serializer::class)
public sealed interface Union {
  @JvmInline
  @Serializable
  public value class Id(
    public val id: Int,
  ) : Union

  @Serializable
  @JvmInline
  public value class CaseString(
    public val `value`: String,
  ) : Union

  @Serializable
  @JvmInline
  public value class CaseBoolean(
    public val `value`: Boolean,
  ) : Union

  public object Serializer : KSerializer<Union> {
    @OptIn(
      InternalSerializationApi::class,
      ExperimentalSerializationApi::class,
    )
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("io.github.nomisrev.render.test.union.inlined.object_.and.primitives.Union", PolymorphicKind.SEALED) {
      element("Id", Id.serializer().descriptor)
      element("CaseString", String.serializer().descriptor)
      element("CaseBoolean", Boolean.serializer().descriptor)
    }

    override fun deserialize(decoder: Decoder): Union {
      val value = decoder.decodeSerializableValue(JsonElement.serializer())
      val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
      return json.attemptDeserialize(
        value,
        Id::class to { decodeFromJsonElement(Id.serializer(), it) },
        CaseBoolean::class to { CaseBoolean(decodeFromJsonElement(Boolean.serializer(), it)) },
        CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
      )
    }

    override fun serialize(encoder: Encoder, `value`: Union) {
      when(value) {
        is Id -> encoder.encodeSerializableValue(Id.serializer(), value)
        is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
        is CaseBoolean -> encoder.encodeSerializableValue(Boolean.serializer(), value.value)
      }
    }
  }
}
