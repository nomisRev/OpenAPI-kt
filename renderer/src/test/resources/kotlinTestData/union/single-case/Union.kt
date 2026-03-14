package union.single.case.model

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
    @Serializable
    @JvmInline
    value class CaseString(val value: String) : Union

    object Serializer : KSerializer<Union> {
        @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
        override val descriptor: SerialDescriptor =
            buildSerialDescriptor("union.single.case.model.Union", PolymorphicKind.SEALED) {
                element("CaseString", String.serializer().descriptor)
            }

        override fun deserialize(decoder: Decoder): Union {
            val value = decoder.decodeSerializableValue(JsonElement.serializer())
            val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
            return json.attemptDeserialize(
                value,
                CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
            )
        }

        override fun serialize(encoder: Encoder, value: Union) = when(value) {
            is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
        }
    }
}
