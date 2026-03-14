package union.nested.union.model

import io.ktor.client.request.invoke
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

@Serializable(with = Union.Serializer::class)
sealed interface Union {
    @Serializable(with = Inner.Serializer::class)
    sealed interface Inner : Union {
        @Serializable
        @JvmInline
        value class CaseInt(val value: Int) : Inner

        @Serializable
        @JvmInline
        value class CaseString(val value: String) : Inner

        object Serializer : KSerializer<Inner> {
            @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
            override val descriptor: SerialDescriptor =
                buildSerialDescriptor("union.nested.union.model.Union.Inner", PolymorphicKind.SEALED) {
                    element("CaseInt", Int.serializer().descriptor)
                    element("CaseString", String.serializer().descriptor)
                }

            override fun deserialize(decoder: Decoder): Inner {
                val value = decoder.decodeSerializableValue(JsonElement.serializer())
                val json =
                    requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                return json.attemptDeserialize(
                    value,
                    CaseInt::class to { CaseInt(decodeFromJsonElement(Int.serializer(), it)) },
                    CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                )
            }

            override fun serialize(encoder: Encoder, value: Inner) = when (value) {
                is CaseInt -> encoder.encodeSerializableValue(Int.serializer(), value.value)
                is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
            }
        }
    }

    @Serializable
    @JvmInline
    value class CaseBoolean(val value: Boolean) : Union

    object Serializer : KSerializer<Union> {
        @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
        override val descriptor: SerialDescriptor =
            buildSerialDescriptor("union.nested.union.model.Union", PolymorphicKind.SEALED) {
                element("CaseInner", Inner.serializer().descriptor)
                element("CaseBoolean", Boolean.serializer().descriptor)
            }

        override fun deserialize(decoder: Decoder): Union {
            val value = decoder.decodeSerializableValue(JsonElement.serializer())
            val json =
                requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
            return json.attemptDeserialize(
                value,
                Inner::class to { decodeFromJsonElement(Inner.serializer(), it) },
                CaseBoolean::class to { CaseBoolean(decodeFromJsonElement(Boolean.serializer(), it)) },
            )
        }

        override fun serialize(encoder: Encoder, value: Union) = when (value) {
            is Inner -> encoder.encodeSerializableValue(Inner.serializer(), value)
            is CaseBoolean -> encoder.encodeSerializableValue(Boolean.serializer(), value.value)
        }
    }
}
