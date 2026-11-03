package io.github.nomisrev.render.test.client.union.nullable.member

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

@Serializable(with = ObjectMember.Serializer::class)
public sealed interface ObjectMember {
  @Serializable
  @JvmInline
  public value class CaseNullableCat(
    public val `value`: NullableCat,
  ) : ObjectMember

  @Serializable
  @JvmInline
  public value class CaseString(
    public val `value`: String,
  ) : ObjectMember

  public object Serializer : KSerializer<ObjectMember> {
    @OptIn(
      InternalSerializationApi::class,
      ExperimentalSerializationApi::class,
    )
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("io.github.nomisrev.render.test.client.union.nullable.member.ObjectMember", PolymorphicKind.SEALED) {
      element("CaseNullableCat", NullableCat.serializer().descriptor)
      element("CaseString", String.serializer().descriptor)
    }

    override fun deserialize(decoder: Decoder): ObjectMember {
      val value = decoder.decodeSerializableValue(JsonElement.serializer())
      val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
      return json.attemptDeserialize(
        value,
        CaseNullableCat::class to { CaseNullableCat(decodeFromJsonElement(NullableCat.serializer(), it)) },
        CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
      )
    }

    override fun serialize(encoder: Encoder, `value`: ObjectMember) {
      when(value) {
        is CaseNullableCat -> encoder.encodeSerializableValue(NullableCat.serializer(), value.value)
        is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
      }
    }
  }
}
