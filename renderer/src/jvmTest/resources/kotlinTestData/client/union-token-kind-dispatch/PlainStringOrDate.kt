package io.github.nomisrev.render.test.client.union.token.kind.dispatch

import kotlin.OptIn
import kotlin.jvm.JvmInline
import kotlinx.datetime.LocalDate
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
import kotlinx.serialization.json.JsonPrimitive

@Serializable(with = PlainStringOrDate.Serializer::class)
public sealed interface PlainStringOrDate {
  @Serializable
  @JvmInline
  public value class CasePlainString(
    public val `value`: PlainString,
  ) : PlainStringOrDate

  @Serializable
  @JvmInline
  public value class CaseDate(
    public val `value`: LocalDate,
  ) : PlainStringOrDate

  public object Serializer : KSerializer<PlainStringOrDate> {
    @OptIn(
      InternalSerializationApi::class,
      ExperimentalSerializationApi::class,
    )
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("io.github.nomisrev.render.test.client.union.token.kind.dispatch.PlainStringOrDate", PolymorphicKind.SEALED) {
      element("CasePlainString", PlainString.serializer().descriptor)
      element("CaseDate", LocalDate.serializer().descriptor)
    }

    override fun deserialize(decoder: Decoder): PlainStringOrDate {
      val value = decoder.decodeSerializableValue(JsonElement.serializer())
      val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
      return when {
        value is JsonPrimitive && value.isString -> json.attemptDeserialize(
          value,
          CaseDate::class to { CaseDate(decodeFromJsonElement(LocalDate.serializer(), it)) },
          CasePlainString::class to { CasePlainString(decodeFromJsonElement(PlainString.serializer(), it)) },
        )
        else -> throw UnionSerializationException(value, emptyMap())
      }
    }

    override fun serialize(encoder: Encoder, `value`: PlainStringOrDate) {
      when(value) {
        is CasePlainString -> encoder.encodeSerializableValue(PlainString.serializer(), value.value)
        is CaseDate -> encoder.encodeSerializableValue(LocalDate.serializer(), value.value)
      }
    }
  }
}
