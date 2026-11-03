package io.github.nomisrev.render.test.client.discriminated.union.nullable

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

@Serializable(with = TaggedNullableMemberPet.Serializer::class)
public sealed interface TaggedNullableMemberPet {
  @Serializable
  public data class NullableCat(
    public val petType: String,
    public val meow: String,
  ) : TaggedNullableMemberPet

  @Serializable
  public data class Dog(
    public val petType: String,
    public val bark: String,
  ) : TaggedNullableMemberPet

  public object Serializer : KSerializer<TaggedNullableMemberPet> {
    @OptIn(
      InternalSerializationApi::class,
      ExperimentalSerializationApi::class,
    )
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("io.github.nomisrev.render.test.client.discriminated.union.nullable.TaggedNullableMemberPet", PolymorphicKind.SEALED) {
      element("NullableCat", NullableCat.serializer().descriptor)
      element("Dog", Dog.serializer().descriptor)
    }

    override fun deserialize(decoder: Decoder): TaggedNullableMemberPet {
      val value = decoder.decodeSerializableValue(JsonElement.serializer())
      val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
      val obj = value as? JsonObject
      val tag = (obj?.get("petType") as? JsonPrimitive)?.content
      when(tag) {
        "cat", "kitty" -> return json.decodeFromJsonElement(NullableCat.serializer(), value)
        "dog" -> return json.decodeFromJsonElement(Dog.serializer(), value)
        else -> throw SerializationException("Unknown tag: " + tag + " for io.github.nomisrev.render.test.client.discriminated.union.nullable.TaggedNullableMemberPet")
      }
    }

    override fun serialize(encoder: Encoder, `value`: TaggedNullableMemberPet) {
      when(value) {
        is NullableCat -> encoder.encodeSerializableValue(NullableCat.serializer(), value)
        is Dog -> encoder.encodeSerializableValue(Dog.serializer(), value)
      }
    }
  }
}
