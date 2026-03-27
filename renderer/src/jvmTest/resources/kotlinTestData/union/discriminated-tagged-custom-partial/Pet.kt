package io.github.nomisrev.render.test.union.discriminated.tagged.custom.partial

import kotlin.Boolean
import kotlin.OptIn
import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

@Serializable(with = Pet.Serializer::class)
public sealed interface Pet {
  @Serializable
  public data class Cat(
    public val type: Type,
    public val name: String,
  ) : Pet {
    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("cat")
      Cat("cat"),
      ;
    }
  }

  @JvmInline
  @Serializable
  public value class Barks(
    public val barks: Boolean,
  ) : Pet

  public object Serializer : KSerializer<Pet> {
    @OptIn(
      InternalSerializationApi::class,
      ExperimentalSerializationApi::class,
    )
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("io.github.nomisrev.render.test.union.discriminated.tagged.custom.partial.Pet", PolymorphicKind.SEALED) {
      element("Cat", Cat.serializer().descriptor)
      element("Barks", Barks.serializer().descriptor)
    }

    override fun deserialize(decoder: Decoder): Pet {
      val value = decoder.decodeSerializableValue(JsonElement.serializer())
      val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
      val obj = value as? JsonObject
      val tag = (obj?.get("type") as? JsonPrimitive)?.content
      when(tag) {
        "cat" -> return json.decodeFromJsonElement(Cat.serializer(), value)
        else -> {
          return json.attemptDeserialize(
            value,
            Barks::class to { decodeFromJsonElement(Barks.serializer(), it) },
          )
        }
      }
    }

    override fun serialize(encoder: Encoder, `value`: Pet) {
      when(value) {
        is Cat -> encoder.encodeSerializableValue(Cat.serializer(), value)
        is Barks -> encoder.encodeSerializableValue(Barks.serializer(), value)
      }
    }
  }
}
