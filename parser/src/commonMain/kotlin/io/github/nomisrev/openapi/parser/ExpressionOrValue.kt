package io.github.nomisrev.openapi.parser

import com.charleskorn.kaml.YamlInput
import com.charleskorn.kaml.YamlNode
import com.charleskorn.kaml.YamlScalar
import kotlin.jvm.JvmInline
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonPrimitive

@Serializable(with = ExpressionOrValue.Serializer::class)
public sealed interface ExpressionOrValue {
  @JvmInline public value class Expression(public val value: String) : ExpressionOrValue

  @JvmInline public value class Value(public val value: JsonElement) : ExpressionOrValue

  public object Serializer : KSerializer<ExpressionOrValue> {
    override val descriptor: SerialDescriptor =
      PrimitiveSerialDescriptor("ExpressionOrValue", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): ExpressionOrValue =
      when (decoder) {
        is JsonDecoder -> {
          val json = decoder.decodeJsonElement()
          val str = (json as? JsonPrimitive)?.takeIf { it.isString }?.content
          if (str != null && str.startsWith("\$")) Expression(str)
          else Value(json)
        }

        is YamlInput -> {
          val node = decoder.decodeSerializableValue(YamlNode.serializer())
          val str = (node as? YamlScalar)?.content
          if (str != null && str.startsWith("\$")) Expression(str)
          else Value(JsonPrimitive(str ?: node.toString()))
        }

        else -> {
          val str = decoder.decodeString()
          if (str.startsWith("\$")) Expression(str)
          else Value(JsonPrimitive(str))
        }
      }

    override fun serialize(encoder: Encoder, value: ExpressionOrValue) {
      when (value) {
        is Expression -> (encoder as JsonEncoder).encodeJsonElement(JsonPrimitive(value.value))
        is Value -> (encoder as JsonEncoder).encodeJsonElement(value.value)
      }
    }
  }
}
