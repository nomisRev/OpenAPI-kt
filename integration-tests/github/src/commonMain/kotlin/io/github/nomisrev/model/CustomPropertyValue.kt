package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlin.jvm.JvmInline
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonDecoder

@Serializable
data class CustomPropertyValue(@SerialName("property_name") val propertyName: String, val value: Value?) {
    @Serializable(with = Value.Serializer::class)
    sealed interface Value {
        @Serializable
        @JvmInline
        value class CaseString(val value: String) : Value

        @Serializable
        @JvmInline
        value class CaseStrings(val value: List<String>) : Value

        object Serializer : KSerializer<Value> {
            @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
            override val descriptor: SerialDescriptor =
                buildSerialDescriptor("io.github.nomisrev.model.CustomPropertyValue.Value", PolymorphicKind.SEALED) {
                    element("CaseString", String.serializer().descriptor)
                    element("CaseStrings", ListSerializer(String.serializer()).descriptor)
                }

            override fun deserialize(decoder: Decoder): Value {
                val value = decoder.decodeSerializableValue(JsonElement.serializer())
                val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                return json.attemptDeserialize(
                    value,
                    CaseStrings::class to { CaseStrings(decodeFromJsonElement(ListSerializer(String.serializer()), it)) },
                    CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                )
            }

            override fun serialize(encoder: Encoder, value: Value) = when(value) {
                is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                is CaseStrings -> encoder.encodeSerializableValue(ListSerializer(String.serializer()), value.value)
            }
        }
    }
}
