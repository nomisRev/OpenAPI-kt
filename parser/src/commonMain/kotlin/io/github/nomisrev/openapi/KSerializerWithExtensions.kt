package io.github.nomisrev.openapi

import com.charleskorn.kaml.YamlInput
import com.charleskorn.kaml.YamlNode
import com.charleskorn.kaml.yamlMap
import kotlin.collections.component1
import kotlin.collections.component2
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject

internal abstract class KSerializerWithExtensions<T>(
  private val serializer: KSerializer<T>,
  private val extensions: (T) -> Map<String, JsonElement>,
  private val withExtensions: (T, Map<String, JsonElement>) -> T,
) : KSerializer<T> {
  override val descriptor: SerialDescriptor = serializer.descriptor

  override fun deserialize(decoder: Decoder): T =
    when {
      decoder is JsonDecoder -> {
        val jsObject = decoder.decodeSerializableValue(JsonElement.serializer())
        val value =
          decoder.json.decodeFromJsonElement(
            serializer,
            JsonObject(jsObject.jsonObject.filterNot { (key, _) -> key.startsWith("x-") }),
          )
        val extensions = jsObject.jsonObject.filter { (key, _) -> key.startsWith("x-") }
        withExtensions(value, extensions)
      }

      decoder is YamlInput -> {
        val node = decoder.decodeSerializableValue(YamlNode.serializer())
        val map = node.yamlMap
        val value = decoder.yaml.decodeFromYamlNode(serializer, map)
        val extensions =
          buildMap<String, JsonElement> {
            map.entries.forEach { (key, value) ->
              if (key.content.startsWith("x-")) {
                // TODO
                //  For OpenAI: Caused by: MissingTypeTagException at
                // paths./assistants.get.x-oaiMeta on line 104, column 9: Value is missing a type
                // tag (eg. !<type>)
                //                        put(key.content,
                // decoder.yaml.decodeFromYamlNode(JsonElement.serializer(), value))
              }
            }
          }
        withExtensions(value, extensions)
      }

      else -> error("Unknown decoder ($decoder) cannot deserialize ${this::class.simpleName}.")
    }

  override fun serialize(encoder: Encoder, value: T) {
    encoder as JsonEncoder
    val jsObject = encoder.json.encodeToJsonElement(serializer, value).jsonObject - "extensions"
    encoder.encodeSerializableValue(
      JsonElement.serializer(),
      JsonObject(jsObject + extensions(value)),
    )
  }
}
