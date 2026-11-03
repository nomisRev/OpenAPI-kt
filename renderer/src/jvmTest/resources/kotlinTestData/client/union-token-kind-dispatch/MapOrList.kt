package io.github.nomisrev.render.test.client.union.token.kind.dispatch

import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement

@Serializable(with = MapOrList.Serializer::class)
public sealed interface MapOrList {
  @Serializable
  @JvmInline
  public value class CaseStringsMap(
    public val `value`: Map<String, String>,
  ) : MapOrList

  @Serializable
  @JvmInline
  public value class CaseStrings(
    public val `value`: List<String>,
  ) : MapOrList

  public object Serializer : KSerializer<MapOrList> {
    @OptIn(
      InternalSerializationApi::class,
      ExperimentalSerializationApi::class,
    )
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("io.github.nomisrev.render.test.client.union.token.kind.dispatch.MapOrList", PolymorphicKind.SEALED) {
      element("CaseStringsMap", MapSerializer(String.serializer(), String.serializer()).descriptor)
      element("CaseStrings", ListSerializer(String.serializer()).descriptor)
    }

    override fun deserialize(decoder: Decoder): MapOrList {
      val value = decoder.decodeSerializableValue(JsonElement.serializer())
      val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
      return json.attemptDeserialize(
        value,
        CaseStringsMap::class to { CaseStringsMap(decodeFromJsonElement(MapSerializer(String.serializer(), String.serializer()), it)) },
        CaseStrings::class to { CaseStrings(decodeFromJsonElement(ListSerializer(String.serializer()), it)) },
      )
    }

    override fun serialize(encoder: Encoder, `value`: MapOrList) {
      when(value) {
        is CaseStringsMap -> encoder.encodeSerializableValue(MapSerializer(String.serializer(), String.serializer()), value.value)
        is CaseStrings -> encoder.encodeSerializableValue(ListSerializer(String.serializer()), value.value)
      }
    }
  }
}
