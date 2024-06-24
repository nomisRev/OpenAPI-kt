package io.github.nomisrev.openapi

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlInput
import com.charleskorn.kaml.YamlMap
import com.charleskorn.kaml.yamlMap
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject

internal abstract class KSerializerWithExtensions<T>(
  private val serializer: KSerializer<T>,
  private val extensions: (T) -> Map<String, JsonElement>,
  private val withExtensions: (T, Map<String, JsonElement>) -> T
) : KSerializer<T> {
  override val descriptor: SerialDescriptor = serializer.descriptor

  override fun deserialize(decoder: Decoder): T =
    when (decoder) {
      is JsonDecoder -> deserializeJson(decoder)
      is YamlInput -> deserializeYaml(decoder)
      else ->
        throw RuntimeException("Unsupported decoder: $decoder")
    }

  private fun deserializeYaml(decoder: YamlInput): T {
    val map = (decoder.node as YamlMap)
    val withoutExtensions = map.entries.filter { !it.key.content.startsWith("x-") }

    val value = decoder.yaml.decodeFromYamlNode(
      serializer,
      YamlMap(withoutExtensions, decoder.getCurrentPath())
    )
    val extensions = map
      .entries
      .filter { it.key.content.startsWith("x-") }
      .map { (k, v) ->
        Pair(k.content, Json.parseToJsonElement(v.contentToString()))
      }.toMap()
    return withExtensions(value, extensions)
  }

  private fun deserializeJson(decoder: JsonDecoder): T {
    val jsObject = decoder.decodeSerializableValue(JsonElement.serializer())
    val value =
      decoder.json.decodeFromJsonElement(
        serializer,
        JsonObject(jsObject.jsonObject.filterNot { (key, _) -> key.startsWith("x-") })
      )
    val extensions = jsObject.jsonObject.filter { (key, _) -> key.startsWith("x-") }
    return withExtensions(value, extensions)
  }

  override fun serialize(encoder: Encoder, value: T) =
    when (encoder) {
      is JsonEncoder -> serializeJson(encoder, value)
      else ->
        throw RuntimeException("Unsupported encoder: $encoder")
    }

  private fun serializeJson(encoder: JsonEncoder, value: T) {
    val jsObject = encoder.json.encodeToJsonElement(serializer, value).jsonObject - "extensions"
    encoder.encodeSerializableValue(
      JsonElement.serializer(),
      JsonObject(jsObject + extensions(value))
    )
  }
}
