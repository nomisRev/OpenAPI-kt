package io.github.nomisrev.openapi

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject

internal abstract class KSerializerWithExtensions<T>(
  private val json: Json,
  private val serializer: KSerializer<T>,
  private val extensions: (T) -> Map<String, JsonElement>,
  private val withExtensions: (T, Map<String, JsonElement>) -> T
): KSerializer<T> {
  override val descriptor: SerialDescriptor = serializer.descriptor

  override fun deserialize(decoder: Decoder): T {
    val jsObject = decoder.decodeSerializableValue(JsonElement.serializer())
    val value = json.decodeFromJsonElement(serializer,
      JsonObject(jsObject.jsonObject.filterNot { (key, _) -> key.startsWith("x-") })
    )
    val extensions = jsObject.jsonObject.filter { (key, _) -> key.startsWith("x-") }
    return withExtensions(value, extensions)
  }

  override fun serialize(encoder: Encoder, value: T) {
    val jsObject = json.encodeToJsonElement(serializer, value).jsonObject - "extensions"
    encoder.encodeSerializableValue(JsonElement.serializer(), JsonObject(jsObject + extensions(value)))
  }
}
