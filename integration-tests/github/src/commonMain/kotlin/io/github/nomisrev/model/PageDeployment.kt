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
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonDecoder

@Serializable
data class PageDeployment(
    val id: Id,
    @SerialName("status_url") val statusUrl: String,
    @SerialName("page_url") val pageUrl: String,
    @SerialName("preview_url") val previewUrl: String? = null,
) {
    @Serializable(with = Id.Serializer::class)
    sealed interface Id {
        @Serializable
        @JvmInline
        value class CaseLong(val value: Long) : Id

        @Serializable
        @JvmInline
        value class CaseString(val value: String) : Id

        object Serializer : KSerializer<Id> {
            @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
            override val descriptor: SerialDescriptor =
                buildSerialDescriptor("io.github.nomisrev.model.PageDeployment.Id", PolymorphicKind.SEALED) {
                    element("CaseLong", Long.serializer().descriptor)
                    element("CaseString", String.serializer().descriptor)
                }

            override fun deserialize(decoder: Decoder): Id {
                val value = decoder.decodeSerializableValue(JsonElement.serializer())
                val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                return json.attemptDeserialize(
                    value,
                    CaseLong::class to { CaseLong(decodeFromJsonElement(Long.serializer(), it)) },
                    CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                )
            }

            override fun serialize(encoder: Encoder, value: Id) = when(value) {
                is CaseLong -> encoder.encodeSerializableValue(Long.serializer(), value.value)
                is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
            }
        }
    }
}
