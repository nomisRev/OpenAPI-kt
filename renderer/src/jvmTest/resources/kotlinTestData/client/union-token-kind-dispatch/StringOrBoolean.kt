package io.github.nomisrev.render.test.client.union.token.kind.dispatch

import kotlin.Boolean
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
import kotlinx.serialization.json.booleanOrNull

@Serializable(with = StringOrBoolean.Serializer::class)
public sealed interface StringOrBoolean {
  @Serializable
  @JvmInline
  public value class CaseString(
    public val `value`: String,
  ) : StringOrBoolean

  @Serializable
  @JvmInline
  public value class CaseBoolean(
    public val `value`: Boolean,
  ) : StringOrBoolean

  public object Serializer : KSerializer<StringOrBoolean> {
    @OptIn(
      InternalSerializationApi::class,
      ExperimentalSerializationApi::class,
    )
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("io.github.nomisrev.render.test.client.union.token.kind.dispatch.StringOrBoolean", PolymorphicKind.SEALED) {
      element("CaseString", String.serializer().descriptor)
      element("CaseBoolean", Boolean.serializer().descriptor)
    }

    override fun deserialize(decoder: Decoder): StringOrBoolean {
      val value = decoder.decodeSerializableValue(JsonElement.serializer())
      val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
      return when {
        value is JsonPrimitive && value.isString -> json.attemptDeserialize(
          value,
          CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
        )
        value is JsonPrimitive && !value.isString && value.booleanOrNull != null -> json.attemptDeserialize(
          value,
          CaseBoolean::class to { CaseBoolean(decodeFromJsonElement(Boolean.serializer(), it)) },
        )
        else -> throw UnionSerializationException(value, emptyMap())
      }
    }

    override fun serialize(encoder: Encoder, `value`: StringOrBoolean) {
      when(value) {
        is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
        is CaseBoolean -> encoder.encodeSerializableValue(Boolean.serializer(), value.value)
      }
    }
  }
}
