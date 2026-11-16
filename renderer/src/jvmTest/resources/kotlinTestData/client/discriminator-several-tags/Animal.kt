package io.github.nomisrev.render.test.client.discriminator.several.tags

import kotlin.Double
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement

@Serializable(with = Animal.Serializer::class)
public sealed interface Animal {
  public val name: String?

  @SerialName("Animal")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = Default.Serializer::class)
  public data class Default(
    public val kind: Kind,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
  ) : Animal {
    @Serializable
    public enum class Kind(
      public val `value`: String,
    ) {
      @SerialName("animal")
      Animal("animal"),
      @SerialName("beast")
      Beast("beast"),
      ;
    }

    public object Serializer : TaggedCaseSerializer<Default>(Default.generatedSerializer(), "kind", setOf("animal", "beast"))
  }

  @SerialName("bird")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = Bird.Serializer::class)
  public data class Bird(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val span: Double? = null,
  ) : Animal {
    public object Serializer : TaggedCaseSerializer<Bird>(Bird.generatedSerializer(), "kind", "bird")
  }

  public object Serializer : JsonContentPolymorphicSerializer<Animal>(Animal::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<Animal> = when (val tag = element.discriminatorValue("kind")) {
      "animal", "beast" -> Default.serializer()
      "bird" -> Bird.serializer()
      else -> throw SerializationException("'kind' of Animal selects none of animal, beast, bird: " + tag)
    }
  }
}
