package io.github.nomisrev.render.test.client.union.token.kind.dispatch

import kotlin.OptIn
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

@Serializable(with = FreeFormOrCat.Serializer::class)
public sealed interface FreeFormOrCat {
  @Serializable
  @JvmInline
  public value class CaseJsonElement(
    public val `value`: JsonElement,
  ) : FreeFormOrCat

  @Serializable
  @JvmInline
  public value class CaseCat(
    public val `value`: Cat,
  ) : FreeFormOrCat

  public object Serializer : KSerializer<FreeFormOrCat> {
    @OptIn(
      InternalSerializationApi::class,
      ExperimentalSerializationApi::class,
    )
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("io.github.nomisrev.render.test.client.union.token.kind.dispatch.FreeFormOrCat", PolymorphicKind.SEALED) {
      element("CaseJsonElement", JsonElement.serializer().descriptor)
      element("CaseCat", Cat.serializer().descriptor)
    }

    override fun deserialize(decoder: Decoder): FreeFormOrCat {
      val value = decoder.decodeSerializableValue(JsonElement.serializer())
      val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
      return when {
        value is JsonObject -> json.attemptDeserialize(
          value,
          CaseCat::class to { CaseCat(decodeFromJsonElement(Cat.serializer(), it)) },
          CaseJsonElement::class to { CaseJsonElement(decodeFromJsonElement(JsonElement.serializer(), it)) },
        )
        else -> json.attemptDeserialize(
          value,
          CaseJsonElement::class to { CaseJsonElement(decodeFromJsonElement(JsonElement.serializer(), it)) },
        )
      }
    }

    override fun serialize(encoder: Encoder, `value`: FreeFormOrCat) {
      when(value) {
        is CaseJsonElement -> encoder.encodeSerializableValue(JsonElement.serializer(), value.value)
        is CaseCat -> encoder.encodeSerializableValue(Cat.serializer(), value.value)
      }
    }
  }
}
