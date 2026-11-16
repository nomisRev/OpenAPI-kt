package io.github.nomisrev.render.test.client.discriminator.without.base

import kotlin.Boolean
import kotlin.Double
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.Map
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject

@Serializable(with = Pet.Serializer::class)
public sealed interface Pet {
  public val name: String

  @SerialName("feline")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = Feline.Serializer::class)
  public data class Feline(
    override val name: String,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val huntingSkill: String? = null,
  ) : Pet {
    public object Serializer : TaggedCaseSerializer<Feline>(Feline.generatedSerializer(), "petType", "feline")
  }

  @SerialName("bird")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = Bird.Serializer::class)
  public data class Bird(
    override val name: String,
    public val span: Double,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val canFly: Boolean? = null,
  ) : Pet {
    public object Serializer : TaggedCaseSerializer<Bird>(Bird.generatedSerializer(), "petType", "bird")
  }

  @SerialName("Dog")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = Dog.Serializer::class)
  public data class Dog(
    override val name: String,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val packSize: Long? = null,
    public val additional: Map<String, Long>? = null,
  ) : Pet {
    public object AdditionalPropertiesSerializer : KSerializer<Dog> {
      override val descriptor: SerialDescriptor = generatedSerializer().descriptor

      override fun serialize(encoder: Encoder, `value`: Dog) {
        val json = (encoder as JsonEncoder).json
        val known = json.encodeToJsonElement(generatedSerializer(), value.copy(additional = null)) as JsonObject
        val content = mutableMapOf<String, JsonElement>()
        known.forEach {
          if (it.key != "additional") {
            content[it.key] = it.value
          }
        }
        value.additional?.forEach {
          content[it.key] = json.encodeToJsonElement(Long.serializer(), it.value)
        }
        encoder.encodeSerializableValue(JsonObject.serializer(), JsonObject(content))
      }

      override fun deserialize(decoder: Decoder): Dog {
        val json = (decoder as JsonDecoder).json
        val element = decoder.decodeSerializableValue(JsonObject.serializer())
        val knownNames = setOf("name", "packSize")
        val known = json.decodeFromJsonElement(generatedSerializer(), JsonObject(element.filterKeys { it in knownNames }))
        val additional = (element - knownNames)
          .mapValues { json.decodeFromJsonElement(Long.serializer(), it.value) }
          .ifEmpty { null }
        return known.copy(additional = additional)
      }
    }

    public object Serializer : TaggedCaseSerializer<Dog>(AdditionalPropertiesSerializer, "petType", "Dog")
  }

  public object Serializer : JsonContentPolymorphicSerializer<Pet>(Pet::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<Pet> = when (val tag = element.discriminatorValue("petType")) {
      "feline" -> Feline.serializer()
      "bird" -> Bird.serializer()
      "Dog" -> Dog.serializer()
      else -> throw SerializationException("'petType' of Pet selects none of feline, bird, Dog: " + tag)
    }
  }
}
