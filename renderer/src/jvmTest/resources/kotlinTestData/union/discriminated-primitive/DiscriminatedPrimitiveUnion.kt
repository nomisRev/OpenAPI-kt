package io.github.nomisrev.render.test.union.discriminated.primitive

import kotlin.OptIn
import kotlin.String
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

@Serializable(with = DiscriminatedPrimitiveUnion.Serializer::class)
public sealed interface DiscriminatedPrimitiveUnion {
  @Serializable
  @JvmInline
  public value class CaseString(
    public val `value`: String,
  ) : DiscriminatedPrimitiveUnion

  @Serializable
  @JvmInline
  public value class CasePrimitiveEmployee(
    public val `value`: PrimitiveEmployee,
  ) : DiscriminatedPrimitiveUnion

  public object Serializer : KSerializer<DiscriminatedPrimitiveUnion> {
    @OptIn(
      InternalSerializationApi::class,
      ExperimentalSerializationApi::class,
    )
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("io.github.nomisrev.render.test.union.discriminated.primitive.DiscriminatedPrimitiveUnion", PolymorphicKind.SEALED) {
      element("CaseString", String.serializer().descriptor)
      element("CasePrimitiveEmployee", PrimitiveEmployee.serializer().descriptor)
    }

    override fun deserialize(decoder: Decoder): DiscriminatedPrimitiveUnion {
      val value = decoder.decodeSerializableValue(JsonElement.serializer())
      val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
      return json.attemptDeserialize(
        value,
        CasePrimitiveEmployee::class to { CasePrimitiveEmployee(decodeFromJsonElement(PrimitiveEmployee.serializer(), it)) },
        CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
      )
    }

    override fun serialize(encoder: Encoder, `value`: DiscriminatedPrimitiveUnion) {
      when(value) {
        is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
        is CasePrimitiveEmployee -> encoder.encodeSerializableValue(PrimitiveEmployee.serializer(), value.value)
      }
    }
  }
}
