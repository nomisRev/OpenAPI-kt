package union.collection.of.references.model

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
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonDecoder

@Serializable(with = Union.Serializer::class)
sealed interface Union {
    @Serializable
    @JvmInline
    value class CaseItems(val value: List<Item>) : Union

    @Serializable
    @JvmInline
    value class CaseItem(val value: Item) : Union

    object Serializer : KSerializer<Union> {
        @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
        override val descriptor: SerialDescriptor =
            buildSerialDescriptor("union.collection.of.references.model.Union", PolymorphicKind.SEALED) {
                element("CaseItems", ListSerializer(Item.serializer()).descriptor)
                element("CaseItem", Item.serializer().descriptor)
            }

        override fun deserialize(decoder: Decoder): Union {
            val value = decoder.decodeSerializableValue(JsonElement.serializer())
            val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
            return json.attemptDeserialize(
                value,
                CaseItems::class to { CaseItems(decodeFromJsonElement(ListSerializer(Item.serializer()), it)) },
                CaseItem::class to { CaseItem(decodeFromJsonElement(Item.serializer(), it)) },
            )
        }

        override fun serialize(encoder: Encoder, value: Union) = when(value) {
            is CaseItems -> encoder.encodeSerializableValue(ListSerializer(Item.serializer()), value.value)
            is CaseItem -> encoder.encodeSerializableValue(Item.serializer(), value.value)
        }
    }
}
