package io.github.nomisrev.openapi

import com.charleskorn.kaml.YamlInput
import com.charleskorn.kaml.YamlList
import com.charleskorn.kaml.YamlMap
import com.charleskorn.kaml.YamlNode
import com.charleskorn.kaml.YamlNull
import com.charleskorn.kaml.YamlScalar
import com.charleskorn.kaml.YamlTaggedNode
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.SerialKind
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

public typealias DefaultValue = ExampleValue

@Serializable(with = ExampleValue.Companion.Serializer::class)
public sealed interface ExampleValue {

  @JvmInline
  public value class Single(public val value: String) : ExampleValue {
    override fun toString(): String = value
  }

  @JvmInline
  public value class Multiple(public val values: List<String>) : ExampleValue {
    override fun toString(): String = values.toString()
  }

  public companion object {

    internal class Serializer : KSerializer<ExampleValue> {
      private val multipleSerializer = ListSerializer(String.serializer())

      // TODO implement proper SerialDescriptor
      @OptIn(ExperimentalSerializationApi::class, InternalSerializationApi::class)
      override val descriptor: SerialDescriptor =
        buildSerialDescriptor(
          "io.github.nomisrev.openapi.ExampleValueSerializer",
          SerialKind.CONTEXTUAL,
        )

      override fun serialize(encoder: Encoder, value: ExampleValue) {
        when (value) {
          is Single -> encoder.encodeString(value.value)
          is Multiple -> encoder.encodeSerializableValue(multipleSerializer, value.values)
        }
      }

      override fun deserialize(decoder: Decoder): ExampleValue =
        when (decoder) {
          is JsonDecoder -> when (val json = decoder.decodeSerializableValue(JsonElement.serializer())) {
            is JsonArray -> Multiple(decoder.decodeSerializableValue(multipleSerializer))
            is JsonPrimitive -> Single(json.content)
            is JsonObject -> Single(json.toString())
          }

          is YamlInput -> when (val node = decoder.decodeSerializableValue(YamlNode.serializer())) {
            is YamlList -> Multiple(decoder.decodeSerializableValue(multipleSerializer))
            is YamlScalar -> Single(node.content)
            is YamlMap -> Single(Json.encodeToString(JsonElement.serializer(), node.toJsonElement()))
            else -> throw SerializationException("ExampleValue can only be a primitive, object or an array. Actual is ${node::class}")
          }

          else -> error("This $decoder is not supported")
        }
    }

    public operator fun invoke(v: String): ExampleValue = Single(v)
  }
}

private fun YamlNode.toJsonElement(): kotlinx.serialization.json.JsonElement = when (this) {
  is YamlScalar -> JsonPrimitive(this.content)
  is YamlNull -> JsonPrimitive(null as String?)
  is YamlList -> JsonArray(this.items.map { it.toJsonElement() })
  is YamlMap -> JsonObject(this.entries.mapKeys { it.key.content }.mapValues { it.value.toJsonElement() })
  is YamlTaggedNode -> this.innerNode.toJsonElement()
}
