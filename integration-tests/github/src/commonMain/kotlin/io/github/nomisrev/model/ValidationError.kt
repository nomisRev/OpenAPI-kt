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
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonDecoder

@Serializable
data class ValidationError(
    val message: String,
    @SerialName("documentation_url") val documentationUrl: String,
    val errors: List<Errors>? = null,
) {
    @Serializable
    data class Errors(
        val resource: String? = null,
        val field: String? = null,
        val message: String? = null,
        val code: String,
        val index: Long? = null,
        val value: Value? = null,
    ) {
        @Serializable(with = Value.Serializer::class)
        sealed interface Value {
            @Serializable
            @JvmInline
            value class CaseString(val value: String?) : Value

            @Serializable
            @JvmInline
            value class CaseLong(val value: Long?) : Value

            @Serializable
            @JvmInline
            value class CaseStrings(val value: List<String>?) : Value

            object Serializer : KSerializer<Value> {
                @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
                override val descriptor: SerialDescriptor =
                    buildSerialDescriptor("io.github.nomisrev.model.ValidationError.Errors.Value", PolymorphicKind.SEALED) {
                        element("CaseString", String.serializer().nullable.descriptor)
                        element("CaseLong", Long.serializer().nullable.descriptor)
                        element("CaseStrings", ListSerializer(String.serializer()).nullable.descriptor)
                    }

                override fun deserialize(decoder: Decoder): Value {
                    val value = decoder.decodeSerializableValue(JsonElement.serializer())
                    val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                    return json.attemptDeserialize(
                        value,
                        CaseStrings::class to { CaseStrings(decodeFromJsonElement(ListSerializer(String.serializer()).nullable, it)) },
                        CaseLong::class to { CaseLong(decodeFromJsonElement(Long.serializer().nullable, it)) },
                        CaseString::class to { CaseString(decodeFromJsonElement(String.serializer().nullable, it)) },
                    )
                }

                override fun serialize(encoder: Encoder, value: Value) = when(value) {
                    is CaseString -> encoder.encodeSerializableValue(String.serializer().nullable, value.value)
                    is CaseLong -> encoder.encodeSerializableValue(Long.serializer().nullable, value.value)
                    is CaseStrings -> encoder.encodeSerializableValue(ListSerializer(String.serializer()).nullable, value.value)
                }
            }
        }
    }
}
