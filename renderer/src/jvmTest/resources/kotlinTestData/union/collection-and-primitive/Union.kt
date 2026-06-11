package io.github.nomisrev.render.test.union.collection.and.primitive

import kotlin.Int
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
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
  @Serializable
  @JvmInline
  public value class CaseStrings(
    public val `value`: List<String>,
  ) : Union

  @Serializable
  @JvmInline
  public value class CaseInt(
    public val `value`: Int,
  ) : Union

  public object Serializer : KSerializer<Union> {
    @OptIn(
      InternalSerializationApi::class,
      ExperimentalSerializationApi::class,
    )
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("io.github.nomisrev.render.test.union.collection.and.primitive.Union", PolymorphicKind.SEALED) {
      element("CaseStrings", ListSerializer(String.serializer()).descriptor)
      element("CaseInt", Int.serializer().descriptor)
    }

    override fun deserialize(decoder: Decoder): Union {
      val value = decoder.decodeSerializableValue(JsonElement.serializer())
      val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
      return json.attemptDeserialize(
        value,
        CaseStrings::class to { CaseStrings(decodeFromJsonElement(ListSerializer(String.serializer()), it)) },
        CaseInt::class to { CaseInt(decodeFromJsonElement(Int.serializer(), it)) },
      )
    }

    override fun serialize(encoder: Encoder, `value`: Union) {
      when(value) {
        is CaseStrings -> encoder.encodeSerializableValue(ListSerializer(String.serializer()), value.value)
        is CaseInt -> encoder.encodeSerializableValue(Int.serializer(), value.value)
      }
    }
  }
}
