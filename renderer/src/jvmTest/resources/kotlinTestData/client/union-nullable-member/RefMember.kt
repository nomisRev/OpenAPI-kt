package io.github.nomisrev.render.test.client.union.nullable.member

import kotlin.Long
import kotlin.OptIn
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

@Serializable(with = RefMember.Serializer::class)
public sealed interface RefMember {
  @Serializable
  @JvmInline
  public value class CaseNullableString(
    public val `value`: NullableString,
  ) : RefMember

  @Serializable
  @JvmInline
  public value class CaseLong(
    public val `value`: Long,
  ) : RefMember

  public object Serializer : KSerializer<RefMember> {
    @OptIn(
      InternalSerializationApi::class,
      ExperimentalSerializationApi::class,
    )
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("io.github.nomisrev.render.test.client.union.nullable.member.RefMember", PolymorphicKind.SEALED) {
      element("CaseNullableString", NullableString.serializer().descriptor)
      element("CaseLong", Long.serializer().descriptor)
    }

    override fun deserialize(decoder: Decoder): RefMember {
      val value = decoder.decodeSerializableValue(JsonElement.serializer())
      val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
      return json.attemptDeserialize(
        value,
        CaseNullableString::class to { CaseNullableString(decodeFromJsonElement(NullableString.serializer(), it)) },
        CaseLong::class to { CaseLong(decodeFromJsonElement(Long.serializer(), it)) },
      )
    }

    override fun serialize(encoder: Encoder, `value`: RefMember) {
      when(value) {
        is CaseNullableString -> encoder.encodeSerializableValue(NullableString.serializer(), value.value)
        is CaseLong -> encoder.encodeSerializableValue(Long.serializer(), value.value)
      }
    }
  }
}
