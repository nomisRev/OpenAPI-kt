package io.github.nomisrev.render.test.union.discriminated.`enum`.case

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

@Serializable(with = DiscriminatedEnumUnion.Serializer::class)
public sealed interface DiscriminatedEnumUnion {
  @Serializable
  public enum class AscOrDesc(
    public val `value`: String,
  ) : DiscriminatedEnumUnion {
    @SerialName("asc")
    Asc("asc"),
    @SerialName("desc")
    Desc("desc"),
    ;
  }

  @JvmInline
  @Serializable
  public value class Manual(
    public val kind: String,
  ) : DiscriminatedEnumUnion

  public object Serializer : KSerializer<DiscriminatedEnumUnion> {
    @OptIn(
      InternalSerializationApi::class,
      ExperimentalSerializationApi::class,
    )
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("io.github.nomisrev.render.test.union.discriminated.enum.case.DiscriminatedEnumUnion", PolymorphicKind.SEALED) {
      element("AscOrDesc", AscOrDesc.serializer().descriptor)
      element("Manual", Manual.serializer().descriptor)
    }

    override fun deserialize(decoder: Decoder): DiscriminatedEnumUnion {
      val value = decoder.decodeSerializableValue(JsonElement.serializer())
      val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
      val obj = value as? JsonObject
      val tag = (obj?.get("kind") as? JsonPrimitive)?.content
      when(tag) {
        "manual" -> return json.decodeFromJsonElement(Manual.serializer(), value)
        else -> {
          return json.attemptDeserialize(
            value,
            AscOrDesc::class to { decodeFromJsonElement(AscOrDesc.serializer(), it) },
          )
        }
      }
    }

    override fun serialize(encoder: Encoder, `value`: DiscriminatedEnumUnion) {
      when(value) {
        is AscOrDesc -> encoder.encodeSerializableValue(AscOrDesc.serializer(), value)
        is Manual -> encoder.encodeSerializableValue(Manual.serializer(), value)
      }
    }
  }
}
