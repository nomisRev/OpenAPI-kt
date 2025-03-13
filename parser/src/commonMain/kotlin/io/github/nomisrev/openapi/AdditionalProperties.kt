package io.github.nomisrev.openapi

import kotlin.jvm.JvmInline
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
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
      override val descriptor =
        buildClassSerialDescriptor("io.github.nomisrev.openapi.AdditionalProperties")

      override fun deserialize(decoder: Decoder): AdditionalProperties {
        decoder as JsonDecoder
        val json = decoder.decodeSerializableValue(JsonElement.serializer())
        return when {
          json is JsonPrimitive && json.booleanOrNull != null -> Allowed(json.boolean)
          json is JsonObject ->
            PSchema(
              decoder.json.decodeFromJsonElement(ReferenceOr.serializer(Schema.serializer()), json)
            )
          else ->
            throw SerializationException("AdditionalProperties can only be a boolean or a schema")
        }
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
