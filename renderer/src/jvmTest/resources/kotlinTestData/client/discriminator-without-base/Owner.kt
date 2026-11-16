package io.github.nomisrev.render.test.client.discriminator.without.base

import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

@Serializable
public data class Owner(
  public val cat: Pet.Feline,
  public val cats: List<Pet.Feline>,
  public val pet: Pet,
  public val favourite: Favourite,
) {
  @Serializable(with = Favourite.Serializer::class)
  public sealed interface Favourite {
    @Serializable
    @JvmInline
    public value class CaseFeline(
      public val `value`: Pet.Feline,
    ) : Favourite

    @Serializable
    @JvmInline
    public value class CaseString(
      public val `value`: String,
    ) : Favourite

    public object Serializer : KSerializer<Favourite> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.github.nomisrev.render.test.client.discriminator.without.base.Owner.Favourite", PolymorphicKind.SEALED) {
        element("CaseFeline", Pet.Feline.serializer().descriptor)
        element("CaseString", String.serializer().descriptor)
      }

      override fun deserialize(decoder: Decoder): Favourite {
        val value = decoder.decodeSerializableValue(JsonElement.serializer())
        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
        return when {
          value is JsonPrimitive && value.isString -> json.attemptDeserialize(
            value,
            CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
          )
          value is JsonObject -> json.attemptDeserialize(
            value,
            CaseFeline::class to { CaseFeline(decodeFromJsonElement(Pet.Feline.serializer(), it)) },
          )
          else -> throw UnionSerializationException(value, emptyMap())
        }
      }

      override fun serialize(encoder: Encoder, `value`: Favourite) {
        when(value) {
          is CaseFeline -> encoder.encodeSerializableValue(Pet.Feline.serializer(), value.value)
          is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
        }
      }
    }
  }
}
