package io.github.nomisrev.model

import kotlinx.serialization.Serializable
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
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonDecoder

@Serializable
@JvmInline
value class Metadata(val values: List<Additional>? = null) {
    @Serializable(with = Additional.Serializer::class)
    sealed interface Additional {
        @Serializable
        @JvmInline
        value class CaseString(val value: String) : Additional

        @Serializable
        @JvmInline
        value class CaseDouble(val value: Double) : Additional

        @Serializable
        @JvmInline
        value class CaseBoolean(val value: Boolean) : Additional

        object Serializer : KSerializer<Additional> {
            @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
            override val descriptor: SerialDescriptor =
                buildSerialDescriptor("io.github.nomisrev.model.Metadata.Additional", PolymorphicKind.SEALED) {
                    element("CaseString", String.serializer().descriptor)
                    element("CaseDouble", Double.serializer().descriptor)
                    element("CaseBoolean", Boolean.serializer().descriptor)
                }

            override fun deserialize(decoder: Decoder): Additional {
                val value = decoder.decodeSerializableValue(JsonElement.serializer())
                val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                return json.attemptDeserialize(
                    value,
                    CaseDouble::class to { CaseDouble(decodeFromJsonElement(Double.serializer(), it)) },
                    CaseBoolean::class to { CaseBoolean(decodeFromJsonElement(Boolean.serializer(), it)) },
                    CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                )
            }

            override fun serialize(encoder: Encoder, value: Additional) = when(value) {
                is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                is CaseDouble -> encoder.encodeSerializableValue(Double.serializer(), value.value)
                is CaseBoolean -> encoder.encodeSerializableValue(Boolean.serializer(), value.value)
            }
        }
    }
}
