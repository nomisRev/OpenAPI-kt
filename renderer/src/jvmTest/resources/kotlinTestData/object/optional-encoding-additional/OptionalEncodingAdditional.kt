package io.github.nomisrev.render.test.object_.optional.encoding.additional

import kotlin.OptIn
import kotlin.String
import kotlin.collections.Map
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject

@OptIn(ExperimentalSerializationApi::class)
@KeepGeneratedSerializer
@Serializable(with = OptionalEncodingAdditional.Serializer::class)
public data class OptionalEncodingAdditional(
  public val id: String,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val note: String? = null,
  public val additional: Map<String, String>? = null,
) {
  public object Serializer : KSerializer<OptionalEncodingAdditional> {
    override val descriptor: SerialDescriptor = generatedSerializer().descriptor

    override fun serialize(encoder: Encoder, `value`: OptionalEncodingAdditional) {
      val json = (encoder as JsonEncoder).json
      val known = json.encodeToJsonElement(generatedSerializer(), value.copy(additional = null)) as JsonObject
      val content = mutableMapOf<String, JsonElement>()
      known.forEach {
        if (it.key != "additional") {
          content[it.key] = it.value
        }
      }
      value.additional?.forEach {
        content[it.key] = json.encodeToJsonElement(String.serializer(), it.value)
      }
      encoder.encodeSerializableValue(JsonObject.serializer(), JsonObject(content))
    }

    override fun deserialize(decoder: Decoder): OptionalEncodingAdditional {
      val json = (decoder as JsonDecoder).json
      val element = decoder.decodeSerializableValue(JsonObject.serializer())
      val knownNames = setOf("id", "note")
      val known = json.decodeFromJsonElement(generatedSerializer(), JsonObject(element.filterKeys { it in knownNames }))
      val additional = (element - knownNames)
        .mapValues { json.decodeFromJsonElement(String.serializer(), it.value) }
        .ifEmpty { null }
      return known.copy(additional = additional)
    }
  }
}
