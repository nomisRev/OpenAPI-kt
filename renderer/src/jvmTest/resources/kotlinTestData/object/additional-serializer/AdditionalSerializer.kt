package io.github.nomisrev.render.test.object_.additional.serializer

import kotlin.Boolean
import kotlin.Int
import kotlin.OptIn
import kotlin.String
import kotlin.collections.Map
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.SerialName
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
@Serializable(with = AdditionalSerializer.Serializer::class)
public data class AdditionalSerializer(
  @SerialName("snake_case")
  public val snakeCase: String,
  public val optionalFlag: Boolean? = null,
  public val requiredNullable: String?,
  public val additional: Map<String, Int>? = null,
) {
  public object Serializer : KSerializer<AdditionalSerializer> {
    override val descriptor: SerialDescriptor = generatedSerializer().descriptor

    override fun serialize(encoder: Encoder, `value`: AdditionalSerializer) {
      val json = (encoder as JsonEncoder).json
      val known = json.encodeToJsonElement(generatedSerializer(), value.copy(additional = null)) as JsonObject
      val content = mutableMapOf<String, JsonElement>()
      known.forEach { (key, jsonElement) ->
        if (key != "additional") {
          content[key] = jsonElement
        }
      }
      value.additional?.forEach { (key, additionalValue) ->
        content[key] = json.encodeToJsonElement(Int.serializer(), additionalValue)
      }
      encoder.encodeSerializableValue(JsonObject.serializer(), JsonObject(content))
    }

    override fun deserialize(decoder: Decoder): AdditionalSerializer {
      val json = (decoder as JsonDecoder).json
      val element = decoder.decodeSerializableValue(JsonObject.serializer())
      val knownNames = setOf("snake_case", "optionalFlag", "requiredNullable")
      val known = json.decodeFromJsonElement(generatedSerializer(), JsonObject(element.filterKeys { it in knownNames }))
      val additional = (element - knownNames)
        .mapValues { (_, jsonElement) -> json.decodeFromJsonElement(Int.serializer(), jsonElement) }
        .ifEmpty { null }
      return known.copy(additional = additional)
    }
  }
}
