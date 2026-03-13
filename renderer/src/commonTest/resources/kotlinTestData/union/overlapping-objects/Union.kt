package union.overlapping.objects.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonDecoder

@Serializable(with = Union.Serializer::class)
sealed interface Union {
    @Serializable
    data class AAndB(val a: String, val b: String) : Union

    @Serializable
    data class AAndBAndC(val a: String, val b: String, val c: String) : Union

    object Serializer : KSerializer<Union> {
        @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
        override val descriptor: SerialDescriptor =
            buildSerialDescriptor("union.overlapping.objects.model.Union", PolymorphicKind.SEALED) {
                element("AAndB", AAndB.serializer().descriptor)
                element("AAndBAndC", AAndBAndC.serializer().descriptor)
            }

        override fun deserialize(decoder: Decoder): Union {
            val value = decoder.decodeSerializableValue(JsonElement.serializer())
            val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
            return json.attemptDeserialize(
                value,
                AAndBAndC::class to { decodeFromJsonElement(AAndBAndC.serializer(), it) },
                AAndB::class to { decodeFromJsonElement(AAndB.serializer(), it) },
            )
        }

        override fun serialize(encoder: Encoder, value: Union) = when(value) {
            is AAndB -> encoder.encodeSerializableValue(AAndB.serializer(), value)
            is AAndBAndC -> encoder.encodeSerializableValue(AAndBAndC.serializer(), value)
        }
    }
}
