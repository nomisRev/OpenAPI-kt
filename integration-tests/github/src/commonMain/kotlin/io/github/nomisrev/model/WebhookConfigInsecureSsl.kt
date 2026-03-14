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

@Serializable(with = WebhookConfigInsecureSsl.Serializer::class)
sealed interface WebhookConfigInsecureSsl {
    @Serializable
    @JvmInline
    value class CaseString(val value: String) : WebhookConfigInsecureSsl

    @Serializable
    @JvmInline
    value class CaseDouble(val value: Double) : WebhookConfigInsecureSsl

    object Serializer : KSerializer<WebhookConfigInsecureSsl> {
        @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
        override val descriptor: SerialDescriptor =
            buildSerialDescriptor("io.github.nomisrev.model.WebhookConfigInsecureSsl", PolymorphicKind.SEALED) {
                element("CaseString", String.serializer().descriptor)
                element("CaseDouble", Double.serializer().descriptor)
            }

        override fun deserialize(decoder: Decoder): WebhookConfigInsecureSsl {
            val value = decoder.decodeSerializableValue(JsonElement.serializer())
            val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
            return json.attemptDeserialize(
                value,
                CaseDouble::class to { CaseDouble(decodeFromJsonElement(Double.serializer(), it)) },
                CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
            )
        }

        override fun serialize(encoder: Encoder, value: WebhookConfigInsecureSsl) = when(value) {
            is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
            is CaseDouble -> encoder.encodeSerializableValue(Double.serializer(), value.value)
        }
    }
}
