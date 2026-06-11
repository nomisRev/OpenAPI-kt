package io.github.nomisrev.render.test.union.additional.properties.last

import kotlin.Int
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject

@Serializable(with = Union.Serializer::class)
public sealed interface Union {
  @Serializable
  public data class IdAndName(
    public val id: Int,
    public val name: String,
  ) : Union

  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = Name.Serializer::class)
  public data class Name(
    public val name: String,
    public val additional: JsonObject? = null,
  ) : Union {
    public object Serializer : KSerializer<Name> {
      override val descriptor: SerialDescriptor = generatedSerializer().descriptor

      override fun serialize(encoder: Encoder, `value`: Name) {
        val json = (encoder as JsonEncoder).json
        val known = json.encodeToJsonElement(generatedSerializer(), value.copy(additional = null)) as JsonObject
        val content = mutableMapOf<String, JsonElement>()
        known.forEach {
          if (it.key != "additional") {
            content[it.key] = it.value
          }
        }
        value.additional?.forEach {
          content[it.key] = it.value
        }
        encoder.encodeSerializableValue(JsonObject.serializer(), JsonObject(content))
      }

      override fun deserialize(decoder: Decoder): Name {
        val json = (decoder as JsonDecoder).json
        val element = decoder.decodeSerializableValue(JsonObject.serializer())
        val knownNames = setOf("name")
        val known = json.decodeFromJsonElement(generatedSerializer(), JsonObject(element.filterKeys { it in knownNames }))
        val additional = JsonObject(element - knownNames).ifEmpty { null }
        return known.copy(additional = additional)
      }
    }
  }

  public object Serializer : KSerializer<Union> {
    @OptIn(
      InternalSerializationApi::class,
      ExperimentalSerializationApi::class,
    )
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("io.github.nomisrev.render.test.union.additional.properties.last.Union", PolymorphicKind.SEALED) {
      element("IdAndName", IdAndName.serializer().descriptor)
      element("Name", Name.serializer().descriptor)
    }

    override fun deserialize(decoder: Decoder): Union {
      val value = decoder.decodeSerializableValue(JsonElement.serializer())
      val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
      return json.attemptDeserialize(
        value,
        IdAndName::class to { decodeFromJsonElement(IdAndName.serializer(), it) },
        Name::class to { decodeFromJsonElement(Name.serializer(), it) },
      )
    }

    override fun serialize(encoder: Encoder, `value`: Union) {
      when(value) {
        is IdAndName -> encoder.encodeSerializableValue(IdAndName.serializer(), value)
        is Name -> encoder.encodeSerializableValue(Name.serializer(), value)
      }
    }
  }
}
