package union.nested.collection.primitive.model

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
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonDecoder

@Serializable(with = Union.Serializer::class)
sealed interface Union {
    @Serializable
    @JvmInline
    value class CaseStringsList(val value: List<List<String>>) : Union

    @Serializable
    @JvmInline
    value class CaseInt(val value: Int) : Union

    object Serializer : KSerializer<Union> {
        @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
        override val descriptor: SerialDescriptor =
            buildSerialDescriptor("union.nested.collection.primitive.model.Union", PolymorphicKind.SEALED) {
                element("CaseStringsList", ListSerializer(ListSerializer(String.serializer())).descriptor)
                element("CaseInt", Int.serializer().descriptor)
            }

        override fun deserialize(decoder: Decoder): Union {
            val value = decoder.decodeSerializableValue(JsonElement.serializer())
            val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
            return json.attemptDeserialize(
                value,
                CaseStringsList::class to { CaseStringsList(decodeFromJsonElement(ListSerializer(ListSerializer(String.serializer())), it)) },
                CaseInt::class to { CaseInt(decodeFromJsonElement(Int.serializer(), it)) },
            )
        }

        override fun serialize(encoder: Encoder, value: Union) = when(value) {
            is CaseStringsList -> encoder.encodeSerializableValue(ListSerializer(ListSerializer(String.serializer())), value.value)
            is CaseInt -> encoder.encodeSerializableValue(Int.serializer(), value.value)
        }
    }
}
