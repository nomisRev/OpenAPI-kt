package union.free.form.json.last.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.JsonDecoder

@Serializable(with = Union.Serializer::class)
sealed interface Union {
    @Serializable
    @JvmInline
    value class Id(val id: Int) : Union

    @Serializable
    @JvmInline
    value class CaseString(val value: String) : Union

    @Serializable
    @JvmInline
    value class CaseJsonElement(val value: JsonElement) : Union

    object Serializer : KSerializer<Union> {
        @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
        override val descriptor: SerialDescriptor =
            buildSerialDescriptor("union.free.form.json.last.model.Union", PolymorphicKind.SEALED) {
                element("Id", Id.serializer().descriptor)
                element("CaseString", String.serializer().descriptor)
                element("CaseJsonElement", JsonElement.serializer().descriptor)
            }

        override fun deserialize(decoder: Decoder): Union {
            val value = decoder.decodeSerializableValue(JsonElement.serializer())
            val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
            return json.attemptDeserialize(
                value,
                Id::class to { decodeFromJsonElement(Id.serializer(), it) },
                CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                CaseJsonElement::class to { CaseJsonElement(decodeFromJsonElement(JsonElement.serializer(), it)) },
            )
        }

        override fun serialize(encoder: Encoder, value: Union) = when(value) {
            is Id -> encoder.encodeSerializableValue(Id.serializer(), value)
            is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
            is CaseJsonElement -> encoder.encodeSerializableValue(JsonElement.serializer(), value.value)
        }
    }
}
