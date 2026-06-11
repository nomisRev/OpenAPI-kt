package io.github.nomisrev.render.test.object_.additional.complex

import kotlin.Boolean
import kotlin.OptIn
import kotlin.String
import kotlin.collections.Map
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject

@OptIn(ExperimentalSerializationApi::class)
@KeepGeneratedSerializer
@Serializable(with = AdditionalComplex.Serializer::class)
public data class AdditionalComplex(
  public val name: String,
  public val additional: Map<String, AdditionalProperties>? = null,
) {
  @JvmInline
  @Serializable
  public value class AdditionalProperties(
    public val enabled: Boolean,
  )

  public object Serializer : KSerializer<AdditionalComplex> {
    override val descriptor: SerialDescriptor = generatedSerializer().descriptor

    override fun serialize(encoder: Encoder, `value`: AdditionalComplex) {
      val json = (encoder as JsonEncoder).json
      val known = json.encodeToJsonElement(generatedSerializer(), value.copy(additional = null)) as JsonObject
      val content = mutableMapOf<String, JsonElement>()
      known.forEach {
        if (it.key != "additional") {
          content[it.key] = it.value
        }
      }
      value.additional?.forEach {
        content[it.key] = json.encodeToJsonElement(AdditionalProperties.serializer(), it.value)
      }
      encoder.encodeSerializableValue(JsonObject.serializer(), JsonObject(content))
    }

    override fun deserialize(decoder: Decoder): AdditionalComplex {
      val json = (decoder as JsonDecoder).json
      val element = decoder.decodeSerializableValue(JsonObject.serializer())
      val knownNames = setOf("name")
      val known = json.decodeFromJsonElement(generatedSerializer(), JsonObject(element.filterKeys { it in knownNames }))
      val additional = (element - knownNames)
        .mapValues { json.decodeFromJsonElement(AdditionalProperties.serializer(), it.value) }
        .ifEmpty { null }
      return known.copy(additional = additional)
    }
  }
}
