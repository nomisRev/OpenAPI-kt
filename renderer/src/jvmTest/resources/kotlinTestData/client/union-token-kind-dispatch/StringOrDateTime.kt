package io.github.nomisrev.render.test.client.union.token.kind.dispatch

import kotlin.OptIn
import kotlin.String
import kotlin.jvm.JvmInline
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
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

@Serializable(with = StringOrDateTime.Serializer::class)
@OptIn(ExperimentalTime::class)
public sealed interface StringOrDateTime {
  @Serializable
  @JvmInline
  public value class CaseString(
    public val `value`: String,
  ) : StringOrDateTime

  @Serializable
  @JvmInline
  public value class CaseDateTime(
    public val `value`: Instant,
  ) : StringOrDateTime

  public object Serializer : KSerializer<StringOrDateTime> {
    @OptIn(
      InternalSerializationApi::class,
      ExperimentalSerializationApi::class,
    )
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("io.github.nomisrev.render.test.client.union.token.kind.dispatch.StringOrDateTime", PolymorphicKind.SEALED) {
      element("CaseString", String.serializer().descriptor)
      element("CaseDateTime", Instant.serializer().descriptor)
    }

    override fun deserialize(decoder: Decoder): StringOrDateTime {
      val value = decoder.decodeSerializableValue(JsonElement.serializer())
      val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
      return when {
        value is JsonPrimitive && value.isString -> json.attemptDeserialize(
          value,
          CaseDateTime::class to { CaseDateTime(decodeFromJsonElement(Instant.serializer(), it)) },
          CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
        )
        else -> throw UnionSerializationException(value, emptyMap())
      }
    }

    override fun serialize(encoder: Encoder, `value`: StringOrDateTime) {
      when(value) {
        is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
        is CaseDateTime -> encoder.encodeSerializableValue(Instant.serializer(), value.value)
      }
    }
  }
}
