package io.github.nomisrev.render.test.map.union.case

import kotlin.Int
import kotlin.OptIn
import kotlin.String
import kotlin.collections.Map
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

@Serializable(with = Setting.Serializer::class)
public sealed interface Setting {
  @Serializable
  @JvmInline
  public value class CaseString(
    public val `value`: String,
  ) : Setting

  @Serializable
  @JvmInline
  public value class CaseIntsMap(
    public val `value`: Map<String, Int>,
  ) : Setting

  public object Serializer : KSerializer<Setting> {
    @OptIn(
      InternalSerializationApi::class,
      ExperimentalSerializationApi::class,
    )
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("io.github.nomisrev.render.test.map.union.case.Setting", PolymorphicKind.SEALED) {
      element("CaseString", String.serializer().descriptor)
      element("CaseIntsMap", MapSerializer(String.serializer(), Int.serializer()).descriptor)
    }

    override fun deserialize(decoder: Decoder): Setting {
      val value = decoder.decodeSerializableValue(JsonElement.serializer())
      val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
      return when {
        value is JsonPrimitive && value.isString -> json.attemptDeserialize(
          value,
          CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
        )
        value is JsonObject -> json.attemptDeserialize(
          value,
          CaseIntsMap::class to { CaseIntsMap(decodeFromJsonElement(MapSerializer(String.serializer(), Int.serializer()), it)) },
        )
        else -> throw UnionSerializationException(value, emptyMap())
      }
    }

    override fun serialize(encoder: Encoder, `value`: Setting) {
      when(value) {
        is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
        is CaseIntsMap -> encoder.encodeSerializableValue(MapSerializer(String.serializer(), Int.serializer()), value.value)
      }
    }
  }
}
