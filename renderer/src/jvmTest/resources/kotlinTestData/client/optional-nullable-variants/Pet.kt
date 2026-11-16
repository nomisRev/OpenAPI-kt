package io.github.nomisrev.render.test.client.optional.nullable.variants

import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement

@Serializable(with = Pet.Serializer::class)
public sealed interface Pet {
  public val nickname: Optional<String>

  @SerialName("cat")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = Cat.Serializer::class)
  public data class Cat(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val nickname: Optional<String> = Optional.Absent,
  ) : Pet {
    public object Serializer : TaggedCaseSerializer<Cat>(Cat.generatedSerializer(), "type", "cat")
  }

  @SerialName("dog")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = Dog.Serializer::class)
  public data class Dog(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val nickname: Optional<String> = Optional.Absent,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val tricks: Optional<List<String>> = Optional.Absent,
  ) : Pet {
    public object Serializer : TaggedCaseSerializer<Dog>(Dog.generatedSerializer(), "type", "dog")
  }

  public object Serializer : JsonContentPolymorphicSerializer<Pet>(Pet::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<Pet> = when (val tag = element.discriminatorValue("type")) {
      "cat" -> Cat.serializer()
      "dog" -> Dog.serializer()
      else -> throw SerializationException("'type' of Pet selects none of cat, dog: " + tag)
    }
  }
}
