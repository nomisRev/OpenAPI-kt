package io.github.nomisrev.render.test.client.union.token.kind.dispatch

import kotlin.Long
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
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.doubleOrNull

@Serializable(with = IntegerOrString.Serializer::class)
public sealed interface IntegerOrString {
  @Serializable
  @JvmInline
  public value class CaseLong(
    public val `value`: Long,
  ) : IntegerOrString

  @Serializable
  @JvmInline
  public value class CaseString(
    public val `value`: String,
  ) : IntegerOrString

  public object Serializer : KSerializer<IntegerOrString> {
    @OptIn(
      InternalSerializationApi::class,
      ExperimentalSerializationApi::class,
    )
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("io.github.nomisrev.render.test.client.union.token.kind.dispatch.IntegerOrString", PolymorphicKind.SEALED) {
      element("CaseLong", Long.serializer().descriptor)
      element("CaseString", String.serializer().descriptor)
    }

    override fun deserialize(decoder: Decoder): IntegerOrString {
      val value = decoder.decodeSerializableValue(JsonElement.serializer())
      val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
      return when {
        value is JsonPrimitive && value.isString -> json.attemptDeserialize(
          value,
          CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
        )
        value is JsonPrimitive && !value.isString && value.doubleOrNull != null -> json.attemptDeserialize(
          value,
          CaseLong::class to { CaseLong(decodeFromJsonElement(Long.serializer(), it)) },
        )
        else -> throw UnionSerializationException(value, emptyMap())
      }
    }

    override fun serialize(encoder: Encoder, `value`: IntegerOrString) {
      when(value) {
        is CaseLong -> encoder.encodeSerializableValue(Long.serializer(), value.value)
        is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
      }
    }
  }
}
