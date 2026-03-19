package io.github.nomisrev.render.test.union.nullable.cases

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
import kotlinx.serialization.builtins.nullable
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
  public value class CaseString(
    public val `value`: String?,
  ) : Union

  @Serializable
  @JvmInline
  public value class CaseInt(
    public val `value`: Int,
  ) : Union

  @Serializable
  @JvmInline
  public value class CaseStrings(
    public val `value`: List<String>?,
  ) : Union

  public object Serializer : KSerializer<Union> {
    @OptIn(
      InternalSerializationApi::class,
      ExperimentalSerializationApi::class,
    )
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("io.github.nomisrev.render.test.union.nullable.cases.Union", PolymorphicKind.SEALED) {
      element("CaseString", String.serializer().nullable.descriptor)
      element("CaseInt", Int.serializer().descriptor)
      element("CaseStrings", ListSerializer(String.serializer()).nullable.descriptor)
    }

    override fun deserialize(decoder: Decoder): Union {
      val value = decoder.decodeSerializableValue(JsonElement.serializer())
      val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
      return json.attemptDeserialize(
        value,
        CaseStrings::class to { CaseStrings(decodeFromJsonElement(ListSerializer(String.serializer()).nullable, it)) },
        CaseInt::class to { CaseInt(decodeFromJsonElement(Int.serializer(), it)) },
        CaseString::class to { CaseString(decodeFromJsonElement(String.serializer().nullable, it)) },
      )
    }

    override fun serialize(encoder: Encoder, `value`: Union) {
      when(value) {
        is CaseString -> encoder.encodeSerializableValue(String.serializer().nullable, value.value)
        is CaseInt -> encoder.encodeSerializableValue(Int.serializer(), value.value)
        is CaseStrings -> encoder.encodeSerializableValue(ListSerializer(String.serializer()).nullable, value.value)
      }
    }
  }
}
