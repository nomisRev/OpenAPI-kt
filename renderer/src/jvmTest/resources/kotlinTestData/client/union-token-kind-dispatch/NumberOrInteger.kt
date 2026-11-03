package io.github.nomisrev.render.test.client.union.token.kind.dispatch

import kotlin.Double
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
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.doubleOrNull

@Serializable(with = NumberOrInteger.Serializer::class)
public sealed interface NumberOrInteger {
  @Serializable
  @JvmInline
  public value class CaseDouble(
    public val `value`: Double,
  ) : NumberOrInteger

  @Serializable
  @JvmInline
  public value class CaseLong(
    public val `value`: Long,
  ) : NumberOrInteger

  public object Serializer : KSerializer<NumberOrInteger> {
    @OptIn(
      InternalSerializationApi::class,
      ExperimentalSerializationApi::class,
    )
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("io.github.nomisrev.render.test.client.union.token.kind.dispatch.NumberOrInteger", PolymorphicKind.SEALED) {
      element("CaseDouble", Double.serializer().descriptor)
      element("CaseLong", Long.serializer().descriptor)
    }

    override fun deserialize(decoder: Decoder): NumberOrInteger {
      val value = decoder.decodeSerializableValue(JsonElement.serializer())
      val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
      return when {
        value is JsonPrimitive && !value.isString && value.doubleOrNull != null -> json.attemptDeserialize(
          value,
          CaseLong::class to { CaseLong(decodeFromJsonElement(Long.serializer(), it)) },
          CaseDouble::class to { CaseDouble(decodeFromJsonElement(Double.serializer(), it)) },
        )
        else -> throw UnionSerializationException(value, emptyMap())
      }
    }

    override fun serialize(encoder: Encoder, `value`: NumberOrInteger) {
      when(value) {
        is CaseDouble -> encoder.encodeSerializableValue(Double.serializer(), value.value)
        is CaseLong -> encoder.encodeSerializableValue(Long.serializer(), value.value)
      }
    }
  }
}
