package io.github.nomisrev.openapi

import com.charleskorn.kaml.YamlInput
import com.charleskorn.kaml.YamlMap
import com.charleskorn.kaml.YamlNode
import com.charleskorn.kaml.YamlScalar
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialKind
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.booleanOrNull

@Serializable(with = AdditionalProperties.Companion.Serializer::class)
public sealed interface AdditionalProperties {
  @JvmInline public value class Allowed(public val value: Boolean) : AdditionalProperties

  @JvmInline
  public value class PSchema(public val value: ReferenceOr<Schema>) : AdditionalProperties

  public companion object {
    internal object Serializer : KSerializer<AdditionalProperties> {
      @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
      override val descriptor =
        buildSerialDescriptor(
          "io.github.nomisrev.openapi.AdditionalProperties",
          SerialKind.CONTEXTUAL,
        )

      override fun deserialize(decoder: Decoder): AdditionalProperties =
        when {
          decoder is JsonDecoder -> {
            val json = decoder.decodeSerializableValue(JsonElement.serializer())
            when {
              json is JsonPrimitive && json.booleanOrNull != null -> Allowed(json.boolean)
              json is JsonObject ->
                PSchema(
                  decoder.json.decodeFromJsonElement(
                    ReferenceOr.serializer(Schema.serializer()),
                    json,
                  )
                )

              else ->
                throw SerializationException(
                  "AdditionalProperties can only be a boolean or a schema"
                )
            }
          }
          decoder is YamlInput -> {
            val node = decoder.decodeSerializableValue(YamlNode.serializer())
            when {
              node is YamlScalar -> Allowed(node.content.toBooleanStrict())
              node is YamlMap ->
                PSchema(ReferenceOr.serializer(Schema.serializer()).deserialize(decoder))

              else ->
                throw SerializationException(
                  "AdditionalProperties can only be a boolean or a schema"
                )
            }
          }
          else -> error("Unknown decoder ($decoder) cannot deserialise AdditionalProperties.")
        }

      override fun serialize(encoder: Encoder, value: AdditionalProperties) {
        when (value) {
          is Allowed -> encoder.encodeBoolean(value.value)
          is PSchema ->
            encoder.encodeSerializableValue(
              ReferenceOr.serializer(Schema.serializer()),
              value.value,
            )
        }
      }
    }
  }
}
