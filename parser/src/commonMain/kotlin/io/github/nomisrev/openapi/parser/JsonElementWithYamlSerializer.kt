package io.github.nomisrev.openapi.parser

import com.charleskorn.kaml.YamlInput
import com.charleskorn.kaml.YamlNode
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.SerialKind
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

@OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
internal object JsonElementWithYamlSerializer : KSerializer<JsonElement> {
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor(
            "io.github.nomisrev.openapi.parser.JsonElementWithYamlSerializer",
            SerialKind.CONTEXTUAL,
        )

    override fun deserialize(decoder: Decoder): JsonElement =
        when (decoder) {
            is JsonDecoder -> decoder.decodeSerializableValue(JsonElement.serializer())
            is YamlInput ->
                OpenAPI.Json.decodeFromJsonElement(
                    JsonElement.serializer(),
                    decoder.decodeSerializableValue(YamlNode.serializer()).toJsonElement(),
                )

            else -> error("Unknown decoder ($decoder) cannot deserialize JsonElement.")
        }

    override fun serialize(encoder: Encoder, value: JsonElement) {
        when (encoder) {
            is JsonEncoder -> encoder.encodeSerializableValue(JsonElement.serializer(), value)
            else ->
                when (value) {
                    is JsonObject ->
                        encoder.encodeSerializableValue(
                            MapSerializer(String.serializer(), JsonElementWithYamlSerializer),
                            value,
                        )

                    is JsonArray ->
                        encoder.encodeSerializableValue(
                            ListSerializer(JsonElementWithYamlSerializer),
                            value,
                        )

                    is JsonNull -> encoder.encodeNull()

                    is JsonPrimitive ->
                        if (value.isString) {
                            encoder.encodeString(value.content)
                        } else {
                            value.content.toLongOrNull()?.let(encoder::encodeLong)
                                ?: value.content.toDoubleOrNull()?.let(encoder::encodeDouble)
                                ?: value.content.toBooleanStrictOrNull()?.let(encoder::encodeBoolean)
                                ?: encoder.encodeString(value.content)
                        }
                }
        }
    }
}
